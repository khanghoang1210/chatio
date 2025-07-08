package com.khanghoang.database;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DatabaseProvider {
    private static final DataSource dataSource;

    static {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/chatio");
        ds.setUser("admin");
        ds.setPassword("admin123");

        dataSource = ds;
    }

    public static DataSource getDataSource() {
        System.out.println("Getting database connection...");
        return dataSource;
    }
}
