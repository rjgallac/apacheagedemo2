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

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.NodeDto;
import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.Person;

@Repository
public class NodeRepository {

    private final DataSource dataSource;

    public NodeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String createNode(String name, String type) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String safe = escape(name);
                    String safeType = escape(type);
                    String cypher = String.format(
                            "SELECT * FROM cypher('graph_name', $$ CREATE (a:%s {name:\"%s\"}) RETURN a $$) as (a agtype);",
                            safeType, safe);

                    ResultSet rs = stmt.executeQuery(cypher);
                    if (rs != null) rs.close();

                    return "Node created successfully: " + name;
                }
        } catch (Exception e) {
            return "Error creating node: " + e.getMessage();
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("'", "''");
    }

    public String deleteNode(Long id) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                String cypher = String.format(
                        "SELECT * FROM cypher('graph_name', $$ MATCH (n) WHERE id(n) = %d DETACH DELETE n $$) as (result agtype);",
                        id);

                ResultSet rs = stmt.executeQuery(cypher);
                if (rs != null) rs.close();
            }
        } catch (Exception e) {
            System.err.println("Error deleting node by ID: " + e.getMessage());
        }
        return "Node deleted successfully: " + id;
    }

    public List<NodeDto> getAllNodes() {
        List<NodeDto> nodes = new ArrayList<>();
         try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }
            // Build nodes list by querying distinct person names
            try (Statement stmt = conn.createStatement()) {
                String nodeQuery = String.format(
                        "SELECT * FROM cypher('graph_name', $$ MATCH (n) RETURN DISTINCT n.name AS name, id(n) AS id , label(n) AS type $$) as (name text, id bigint, type text);");
                try (ResultSet rs = stmt.executeQuery(nodeQuery)) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        Long id = rs.getLong("id");
                        String nodeType = rs.getString("type");
                        if (name != null && !name.isEmpty()) nodes.add(new NodeDto(id, name, nodeType));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving nodes: " + e.getMessage());
        }
        return nodes;
    }

    

}
