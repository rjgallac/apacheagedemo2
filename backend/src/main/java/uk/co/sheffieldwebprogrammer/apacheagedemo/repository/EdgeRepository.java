package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Repository;

@Repository
public class EdgeRepository {
    private final DataSource dataSource;

    public EdgeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createEdit(Long id1, Long id2, String relationshipType) {
            try (Connection conn = dataSource.getConnection()) {
                try {
                    PGConnection pgConn = conn.unwrap(PGConnection.class);
                    if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
                } catch (Exception ignore) {
                }
    
                try (Statement stmt = conn.createStatement()) {
                        String cypher = String.format(
                                "SELECT * FROM cypher('graph_name', $$ MATCH (a),(b) WHERE id(a)=%d AND id(b)=%d CREATE (a)-[:%s]->(b) RETURN a $$) as (a agtype);",
                                id1, id2, relationshipType);
    
                        ResultSet rs = stmt.executeQuery(cypher);
                        if (rs != null) rs.close();
    
                    }
            } catch (Exception e) {
                System.err.println("Error creating edge: " + e.getMessage());
            }
    }
}
