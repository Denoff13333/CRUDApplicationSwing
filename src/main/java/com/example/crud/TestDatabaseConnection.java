package com.example.crud;

import java.sql.Connection;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.connect()) {
            if (conn != null) {
                System.out.println("Успешное подключение к базе данных!");
            } else {
                System.out.println("Не удалось подключиться к базе данных.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
