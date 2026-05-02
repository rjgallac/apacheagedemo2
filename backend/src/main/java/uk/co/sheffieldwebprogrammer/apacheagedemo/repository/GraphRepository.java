package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Repository
public class GraphRepository {
    private final DataSource dataSource;

    public GraphRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public String getAll() {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }
            // Build nodes list by querying distinct person names
            List<String> nodes = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                String nodeQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (n) RETURN DISTINCT n.name AS name $$) as (name text);";
                try (ResultSet rs = stmt.executeQuery(nodeQuery)) {
                    while (rs.next()) {
                        String name = rs.getString(1);
                        if (name != null && !name.isEmpty()) nodes.add(name);
                    }
                }
            }

            // Build links list by querying relationships between persons and companies
            class Link { public String source; public String target; public int value = 1; }
            List<Link> links = new ArrayList<>();

            // Build links list by querying relationships between persons and companies
            try (Statement stmt = conn.createStatement()) {
                String linkQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (a)-[e]->(b) RETURN a.name AS source, b.name AS target $$) as (source text, target text);";
                try (ResultSet rs = stmt.executeQuery(linkQuery)) {
                    while (rs.next()) {
                        String s = rs.getString(1);
                        String t = rs.getString(2);
                        if (s != null && t != null) {
                            Link l = new Link();
                            l.source = s;
                            l.target = t;
                            links.add(l);
                        }
                    }
                }
            }

            // Convert to JSON using Jackson
            try {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode root = mapper.createObjectNode();
                ArrayNode nodesArr = mapper.createArrayNode();
                for (int i = 0; i < nodes.size(); i++) {
                    ObjectNode n = mapper.createObjectNode();
                    n.put("id", nodes.get(i));
                    n.put("group", 1);
                    nodesArr.add(n);
                }

                ArrayNode linksArr = mapper.createArrayNode();
                for (Link l : links) {
                    ObjectNode o = mapper.createObjectNode();
                    o.put("source", l.source);
                    o.put("target", l.target);
                    o.put("value", l.value);
                    linksArr.add(o);
                }

                root.set("nodes", nodesArr);
                root.set("links", linksArr);

                return mapper.writeValueAsString(root);
            } catch (Exception je) {
                return "Error building JSON: " + je.getMessage();
            }
        } catch (Exception e) {
            System.err.println("Error retrieving nodes: " + e.getMessage());
            return "Error retrieving nodes: " + e.getMessage();
        }
    }

}
