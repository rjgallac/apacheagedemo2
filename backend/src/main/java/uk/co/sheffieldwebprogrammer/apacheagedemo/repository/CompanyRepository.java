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

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.CompanyDto;

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

    public List<CompanyDto> getAllCompanies() {
         List<CompanyDto> companies = new ArrayList<>();
         try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }
            // Build nodes list by querying distinct person names
            List<String> nodes = new ArrayList<>();
            try (Statement stmt = conn.createStatement()) {
                String nodeQuery = "SELECT * FROM cypher('graph_name', $$ MATCH (n:Company) RETURN DISTINCT n.name AS name, id(n) AS id $$) as (name text, id bigint);";
                try (ResultSet rs = stmt.executeQuery(nodeQuery)) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        Long id = rs.getLong("id");
                        if (name != null && !name.isEmpty()) nodes.add(name);
                        companies.add(new CompanyDto(name, id));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving nodes: " + e.getMessage());
        }
        return companies;
    }

    public String deleteCompany(Long id) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                String cypher = String.format(
                        "SELECT * FROM cypher('graph_name', $$ MATCH (a:Company) WHERE id(a) = %d DELETE a RETURN a $$) as (a agtype);", id);

                ResultSet rs = stmt.executeQuery(cypher);
                if (rs != null) rs.close();

                return "Company deleted successfully: " + id;
            }
        } catch (Exception e) {
            return "Error deleting company: " + e.getMessage();
        }
    }

}
