package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Repository
public class TreeRepository {
	private final DataSource dataSource;

	public TreeRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getHierarchy(String[] nodeMatches, String[] relationMatches) {
		try (Connection conn = dataSource.getConnection()) {
			try {
				PGConnection pgConn = conn.unwrap(PGConnection.class);
				if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
			} catch (Exception ignore) {
			}

			List<String> nodes = new ArrayList<>();
			try (Statement stmt = conn.createStatement()) {
				String labels = Arrays.stream(nodeMatches)
						.map(s -> "label(n)='" + s + "'")
						.collect(java.util.stream.Collectors.joining(" OR "));
				String nodeQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (n) WHERE  " + labels + " RETURN DISTINCT n.name AS name $$) as (name text);";
				try (ResultSet rs = stmt.executeQuery(nodeQuery)) {
					while (rs.next()) {
						String name = rs.getString(1);
						if (name != null && !name.isEmpty()) nodes.add(name);
					}
				}
			}

			// Build parent -> children map using node labels to enforce desired hierarchy order
			Map<String, List<String>> childrenMap = new HashMap<>();
			Set<String> allChildren = new HashSet<>();

			// define desired order: company -> dept -> team -> project -> person
			Map<String, Integer> order = new HashMap<>();
			order.put("company", 0);
			order.put("dept", 1);
			order.put("department", 1);
			order.put("team", 2);
			order.put("project", 3);
			order.put("person", 4);

			try (Statement stmt = conn.createStatement()) {
				String relationLabels = Arrays.stream(relationMatches)
						.map(s -> "label(e)='" + s + "'")
						.collect(java.util.stream.Collectors.joining(" OR "));
				String linkQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (a)-[e]->(b) WHERE " + relationLabels + " RETURN a.name AS source, label(a) AS source_label, b.name AS target, label(b) AS target_label $$) as (source text, source_label text, target text, target_label text);";
				try (ResultSet rs = stmt.executeQuery(linkQuery)) {
					while (rs.next()) {
						String s = rs.getString("source");
						String sLabel = rs.getString("source_label");
						String t = rs.getString("target");
						String tLabel = rs.getString("target_label");
						if (s == null || t == null) continue;

						Integer sOrder = null;
						Integer tOrder = null;
						String sLabelNorm = sLabel != null ? sLabel.toLowerCase() : null;
						String tLabelNorm = tLabel != null ? tLabel.toLowerCase() : null;
						if (sLabelNorm != null) sOrder = order.get(sLabelNorm);
						if (tLabelNorm != null) tOrder = order.get(tLabelNorm);

						String parent;
						String child;
						if (sOrder != null && tOrder != null) {
							if (sOrder < tOrder) { parent = s; child = t; }
							else if (sOrder > tOrder) { parent = t; child = s; }
							else { // same level - keep direction as-is
								parent = s; child = t;
							}
						} else if (sOrder != null) {
							// if only source has known order, assume it is higher or lower based on value
							parent = s; child = t;
						} else if (tOrder != null) {
							parent = t; child = s;
						} else {
							// neither label known: fall back to direction
							parent = s; child = t;
						}

						// add mapping
						childrenMap.computeIfAbsent(parent, k -> new ArrayList<>());
						List<String> kids = childrenMap.get(parent);
						if (!kids.contains(child)) kids.add(child);
						allChildren.add(child);
					}
				}
			}

			// Determine roots: nodes that are not children of anyone
			List<String> roots = new ArrayList<>();
			for (String n : nodes) {
				if (!allChildren.contains(n)) roots.add(n);
			}
			if (roots.isEmpty()) {
				for (String k : childrenMap.keySet()) roots.add(k);
			}

			// Build tree structure
			class TreeNode {
				String name;
				List<TreeNode> children = new ArrayList<>();
				TreeNode(String name) { this.name = name; }
			}

			Map<String, TreeNode> created = new HashMap<>();

			java.util.function.Function<String, TreeNode> buildNode = new java.util.function.Function<String, TreeNode>() {
				@Override
				public TreeNode apply(String name) {
					if (created.containsKey(name)) return created.get(name);
					TreeNode tn = new TreeNode(name);
					created.put(name, tn);
					List<String> kids = childrenMap.get(name);
					if (kids != null) {
						for (String c : kids) {
							if (created.containsKey(c) && created.get(c) == tn) continue; // avoid self-loop
							// detect and avoid cycles by checking ancestors via a simple visited check
							tn.children.add(apply(c));
						}
					}
					return tn;
				}
			};

			List<TreeNode> forest = new ArrayList<>();
			Set<String> visitedRootBuild = new HashSet<>();
			for (String r : roots) {
				if (!visitedRootBuild.contains(r)) {
					TreeNode node = buildNode.apply(r);
					forest.add(node);
					visitedRootBuild.add(r);
				}
			}

			// Convert to JSON in the style: [{ parent:[{ child:[ ... ] } ] }]
			try {
				ObjectMapper mapper = new ObjectMapper();
				ArrayNode rootArr = mapper.createArrayNode();

				java.util.function.Function<TreeNode, ObjectNode> toJson = new java.util.function.Function<TreeNode, ObjectNode>() {
					@Override
					public ObjectNode apply(TreeNode tn) {
						ObjectNode obj = mapper.createObjectNode();
						ArrayNode childrenArr = mapper.createArrayNode();
						for (TreeNode c : tn.children) {
							childrenArr.add(apply(c));
						}
						obj.set(tn.name, childrenArr);
						return obj;
					}
				};

				for (TreeNode t : forest) {
					rootArr.add(toJson.apply(t));
				}

				return mapper.writeValueAsString(rootArr);
			} catch (Exception je) {
				return "Error building JSON: " + je.getMessage();
			}

		} catch (Exception e) {
			System.err.println("Error retrieving hierarchy: " + e.getMessage());
			return "Error retrieving hierarchy: " + e.getMessage();
		}
	}

}
