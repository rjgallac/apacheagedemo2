package uk.co.sheffieldwebprogrammer.apacheagedemo.repository;

import java.sql.ResultSet;

import javax.sql.DataSource;

public class DepartmentRepository {
    private final DataSource dataSource;

    public DepartmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
