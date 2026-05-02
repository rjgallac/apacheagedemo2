package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Repository;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.EdgeGraphDto;

@Repository
public class EdgeRepository {
    private final DataSource dataSource;

    public EdgeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createEdit(Long id1, Long id2, String relation) {
            try (Connection conn = dataSource.getConnection()) {
                try {
                    PGConnection pgConn = conn.unwrap(PGConnection.class);
                    if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
                } catch (Exception ignore) {
                }
    
                try (Statement stmt = conn.createStatement()) {
                        String cypher = String.format(
                                "SELECT * FROM cypher('graph_name', $$ MATCH (a),(b) WHERE id(a)=%d AND id(b)=%d CREATE (a)-[:%s]->(b) RETURN a $$) as (a agtype);",
                                id1, id2, relation);
    
                        ResultSet rs = stmt.executeQuery(cypher);
                        if (rs != null) rs.close();
    
                    }
            } catch (Exception e) {
                System.err.println("Error creating edge: " + e.getMessage());
            }
    }

    public Iterable<EdgeGraphDto> getAllEdges() {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String cypher = "SELECT * FROM cypher('graph_name', $$ MATCH (a)-[r]->(b) RETURN  id(r) AS id, label(a) AS source, label(r) AS relation, label(b) as target  $$) as (id bigint,source text,relation text,  target text);";

                    ResultSet rs = stmt.executeQuery(cypher);
                    List<EdgeGraphDto> edges = new ArrayList<>();
                    while (rs.next()) {
                        // Agtype relationship = (Agtype) rs.getObject("r");
                        Long id = rs.getLong(1);
                        String source = rs.getString(2);
                        String relation = rs.getString(3);
                        String target = rs.getString(4);
                        edges.add(new EdgeGraphDto(id, source, target, relation));
                    }
                    return edges;
                }
        } catch (Exception e) {
            System.err.println("Error retrieving edges: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void deleteEdge(Long id) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String cypher = String.format(
                            "SELECT * FROM cypher('graph_name', $$ MATCH ()-[r]->() WHERE id(r)=%d DELETE r RETURN 1 $$) as (result int);",
                            id);

                    ResultSet rs = stmt.executeQuery(cypher);
                    if (rs != null) rs.close();
                }
        } catch (Exception e) {
            System.err.println("Error deleting edge: " + e.getMessage());
        }
    }
}
