package com.example.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:src/main/resources/crud_app.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS entities ("
                + "id TEXT PRIMARY KEY, "
                + "name TEXT NOT NULL, "
                + "description TEXT, "
                + "createdAt TEXT DEFAULT CURRENT_TIMESTAMP, "
                + "updatedAt TEXT DEFAULT CURRENT_TIMESTAMP"
                + ");";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица 'entities' проверена/создана успешно.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
