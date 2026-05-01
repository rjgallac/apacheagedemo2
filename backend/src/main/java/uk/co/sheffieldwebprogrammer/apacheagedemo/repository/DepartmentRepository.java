package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.PGConnection;
import org.springframework.stereotype.Repository;

import uk.co.sheffieldwebprogrammer.apacheagedemo.dto.DepartmentDto;


@Repository
public class DepartmentRepository {
    private final DataSource dataSource;

    public DepartmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("'", "''");
    }

    public String createDepartment(String name) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String safe = escape(name);
                    String cypher = String.format(
                            "SELECT * FROM cypher('graph_name', $$ CREATE (a:Department {name:\"%s\"}) RETURN a $$) as (a agtype);",
                            safe);

                    ResultSet rs = stmt.executeQuery(cypher);
                    if (rs != null) rs.close();

                    return "Node created successfully: " + name;
                }
        } catch (Exception e) {
            return "Error creating node: " + e.getMessage();
        }
    }

    public String deleteDepartment(Long id) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String cypher = String.format(
                            "SELECT * FROM cypher('graph_name', $$ MATCH (a:Department) WHERE id(a) = %d DETACH DELETE a RETURN count(*) as deletedCount $$) as (deletedCount int);",
                            id);

                    ResultSet rs = stmt.executeQuery(cypher);
                    if (rs != null && rs.next()) {
                        int deletedCount = rs.getInt("deletedCount");
                        rs.close();
                        return "Deleted " + deletedCount + " node(s) with ID: " + id;
                    } else {
                        return "No node found with ID: " + id;
                    }
                }
        } catch (Exception e) {
            return "Error deleting node: " + e.getMessage();
        }
    }

    public List<DepartmentDto> getAllDepartments() {
        try (Connection conn = dataSource.getConnection()) {
            try {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                if (pgConn != null) pgConn.addDataType("agtype", Agtype.class);
            } catch (Exception ignore) {
            }

            try (Statement stmt = conn.createStatement()) {
                    String cypher = "SELECT * FROM cypher('graph_name', $$ MATCH (a:Department) RETURN id(a) as id, a.name as name $$) as (id bigint, name text);";

                    ResultSet rs = stmt.executeQuery(cypher);
                    List<DepartmentDto> departments = new java.util.ArrayList<>();
                    while (rs != null && rs.next()) {
                        departments.add(new DepartmentDto(rs.getLong("id"), rs.getString("name")));
                    }
                    if (rs != null) rs.close();
                    return departments;
                }
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

}
