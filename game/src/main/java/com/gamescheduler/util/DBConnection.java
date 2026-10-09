package com.gamescheduler.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/game_scheduler_db";

    private static final String USER = "root";

    // Replace with your actual MySQL password
    private static final String PASSWORD = "1234";

    private DBConnection() {
        // Prevent object creation
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {
            System.out.println("MySQL connected successfully!");
            System.out.println("Database: " + connection.getCatalog());

        } catch (SQLException e) {
            System.err.println("MySQL connection failed: " + e.getMessage());

            if ("28000".equals(e.getSQLState())) {
                System.err.println(
                        "Check your MySQL username and password."
                );
            } else if ("08S01".equals(e.getSQLState())) {
                System.err.println(
                        "Check whether MySQL Server is running on port 3306."
                );
            }
        }
    }
}