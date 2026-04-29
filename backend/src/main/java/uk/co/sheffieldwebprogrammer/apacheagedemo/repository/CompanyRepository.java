package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepository {
    private final DataSource dataSource;

    public CompanyRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public String createCompany(String name) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String safe = escape(name);
                    String cypher = String.format(
                            "SELECT * FROM cypher('graph_name', $$ CREATE (a:Company {name:\"%s\"}) RETURN a $$) as (a agtype);",
                            safe);

                    ResultSet rs = stmt.executeQuery(cypher);
                    if (rs != null) rs.close();

                    return "Company created successfully: " + name;
                }
        } catch (Exception e) {
            return "Error creating company: " + e.getMessage();
        }
    }

    public String createRelation(Long personId, Long companyId) {
        try (Connection conn = dataSource.getConnection()) {
                try {
                    PGConnection pgConn = conn.unwrap(PGConnection.class);
                    if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
                } catch (Exception ignore) {
                }

            try (Statement stmt = conn.createStatement()) {
                String relationCypher = String.format(
                                "SELECT * FROM cypher('graph_name', $$ MATCH (a:Person), (b:Company) WHERE id(a) = %d AND id(b) = %d CREATE (a)-[e:WORKS_FOR]->(b) RETURN e $$) as (e agtype);", personId, companyId);
                ResultSet relRs = stmt.executeQuery(relationCypher);
                if (relRs != null) relRs.close();

                return "Edge created successfully: between " + personId + " and " + companyId;
            }
        } catch (Exception e) {
            return "Error creating edge: " + e.getMessage();
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("'", "''");
    }

}
