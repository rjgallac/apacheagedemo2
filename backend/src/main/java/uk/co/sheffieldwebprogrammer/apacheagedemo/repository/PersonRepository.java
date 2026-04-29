package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.Person;

import javax.sql.DataSource;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {

    private final DataSource dataSource;

    public PersonRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("'", "''");
    }

    public String createPerson(String name) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String safe = escape(name);
                    String cypher = String.format(
                            "SELECT * FROM cypher('graph_name', $$ CREATE (a:Person {name:\"%s\"}) RETURN a $$) as (a agtype);",
                            safe);

                    ResultSet rs = stmt.executeQuery(cypher);
                    if (rs != null) rs.close();

                    return "Node created successfully: " + name;
                }
        } catch (Exception e) {
            return "Error creating node: " + e.getMessage();
        }
    }

    public void createNodeAndRelation(String name, Long id1){

        Person p = findById(id1);

        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String safe = escape(name);
                    // Create the new node and return it 
                    String cypher = String.format(
                            "SELECT * FROM cypher('graph_name', $$ CREATE (a:Person {name:\"%s\"}) RETURN id(a) AS id $$) as (a agtype);",
                            safe);

                    ResultSet rs = stmt.executeQuery(cypher);
                    Long id2 = null;
                    if (rs != null) {
                        if (rs.next()) {
                           
                            id2 = rs.getLong("a");
                        }
                    }
                        
                    rs.close();
                    if (p != null) {
                        String relationCypher = String.format(
                                "SELECT * FROM cypher('graph_name', $$ MATCH (a:Person), (b:Person) WHERE id(a) = %d AND id(b) = %d CREATE (a)-[e:RELTYPE]->(b) RETURN e $$) as (e agtype);", id1, id2);
                        ResultSet relRs = stmt.executeQuery(relationCypher);
                        if (relRs != null) relRs.close();
                    }

                }
        } catch (Exception e) {
            System.err.println("Error creating node: " + e.getMessage());
        }

    }

    public String createRelation(String name1, String name2) {
        try (Connection conn = dataSource.getConnection()) {
                try {
                    PGConnection pgConn = conn.unwrap(PGConnection.class);
                    if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
                } catch (Exception ignore) {
                }

            try (Statement stmt = conn.createStatement()) {
                String a = escape(name1);
                String b = escape(name2);
                String cypher = "SELECT * FROM cypher('graph_name', $$ "
                        + "MATCH (a:Person), (b:Person) "
                        + "WHERE a.name = '" + a + "' AND b.name = '" + b + "' "
                        + "CREATE (a)-[e:RELTYPE {property:a.name + '<->' + b.name}]->(b) "
                        + "RETURN e $$) as (e agtype);";

                ResultSet rs = stmt.executeQuery(cypher);
                if (rs != null) rs.close();

                return "Edge created successfully: between " + name1 + " and " + name2;
            }
        } catch (Exception e) {
            return "Error creating edge: " + e.getMessage();
        }
    }

    public String deleteAll() {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                String cypher = "SELECT * FROM cypher('graph_name', $$ MATCH (n) DETACH DELETE n $$) as (result agtype);";

                ResultSet rs = stmt.executeQuery(cypher);
                if (rs != null) rs.close();

                return "All nodes and edges deleted successfully.";
            }
        } catch (Exception e) {
            return "Error deleting all nodes and edges: " + e.getMessage();
        }
    }

    public List<Person> getNodes(){
        List<Person> persons = new ArrayList<>();
         try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }
            // Build nodes list by querying distinct person names
            List<String> nodes = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                String nodeQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (n:Person) RETURN DISTINCT n.name AS name, id(n) AS id $$) as (name text, id bigint);";
                try (ResultSet rs = stmt.executeQuery(nodeQuery)) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        Long id = rs.getLong("id");
                        if (name != null && !name.isEmpty()) nodes.add(name);
                        persons.add(new Person(name, id));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving nodes: " + e.getMessage());
        }
        return persons;
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
                String nodeQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (n:Person) RETURN DISTINCT n.name AS name $$) as (name text);";
                try (ResultSet rs = stmt.executeQuery(nodeQuery)) {
                    while (rs.next()) {
                        String name = rs.getString(1);
                        if (name != null && !name.isEmpty()) nodes.add(name);
                    }
                }
            }

             // Build nodes list by querying distinct person names
            List<String> companies = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                String nodeQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (n:Company) RETURN DISTINCT n.name AS name $$) as (name text);";
                try (ResultSet rs = stmt.executeQuery(nodeQuery)) {
                    while (rs.next()) {
                        String name = rs.getString(1);
                        if (name != null && !name.isEmpty()) companies.add(name);
                    }
                }
            }

            // Build links list by querying relationships between persons and companies
            class Link { public String source; public String target; public int value = 1; }
            List<Link> links = new ArrayList<>();

            // Build links list by querying relationships between persons
            try (Statement stmt = conn.createStatement()) {
                String linkQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (a:Person)-[e]->(b:Person) RETURN a.name AS source, b.name AS target $$) as (source text, target text);";
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

            try (Statement stmt = conn.createStatement()) {
                String linkQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (a:Person)-[e]->(b:Company) RETURN a.name AS source, b.name AS target $$) as (source text, target text);";
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

            try (Statement stmt = conn.createStatement()) {
                String linkQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (a:Person)-[e]->(b:Company) RETURN a.name AS source, b.name AS target $$) as (source text, target text);";
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

                for (int i = 0; i < companies.size(); i++) {
                    ObjectNode n = mapper.createObjectNode();
                    n.put("id", companies.get(i));
                    n.put("group", 2);
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

    public String getGraph() {
        // Try to load a static JSON dataset from classpath: /static/miserables.json
        try (InputStream is = getClass().getResourceAsStream("/static/miserables.json")) {
            if (is != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    return br.lines().collect(Collectors.joining("\n"));
                }
            }
        } catch (Exception ignore) {
        }

        // Fallback: return a small example graph similar to miserables.json
        return "{\n" +
                "  \"nodes\": [\n" +
                "    {\"id\": \"A\", \"group\": 1},\n" +
                "    {\"id\": \"B\", \"group\": 1},\n" +
                "    {\"id\": \"C\", \"group\": 2}\n" +
                "  ],\n" +
                "  \"links\": [\n" +
                "    {\"source\": \"A\", \"target\": \"B\", \"value\": 1},\n" +
                "    {\"source\": \"A\", \"target\": \"C\", \"value\": 2}\n" +
                "  ]\n" +
                "}";
    }

    public void deletebyid(Long id) {
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
    }

    public Person findById(Long id){
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                String cypher = String.format(
                        "SELECT * FROM cypher('graph_name', $$ MATCH (n) WHERE id(n) = %d RETURN n.name AS name, id(n) AS id  $$) as (name text, id bigint);", id);

                ResultSet rs = stmt.executeQuery(cypher);
                if (rs.next()) {
                    String name = rs.getString("name");
                    Long nodeId = rs.getLong("id");
                    return new Person(name, nodeId);
                } else {
                    System.err.println("Node not found with ID: " + id);
                    return null;
                }

            }
        } catch (Exception e) {
            System.err.println("Error finding node by ID: " + e.getMessage());
        }
        return null;
    }

}
