package com.asha.asha.health.system.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database URL
    private static final String URL = "jdbc:mysql://localhost:3306/asha_system";

    // Database username
    private static final String USER = "root";

    // Database password
    private static final String PASSWORD = "1234"; // Change if you set password

    // Method to establish connection
    public static Connection getConnection() {

        try {
            // Load MySQL Driver (optional in newer versions but safe)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create and return connection
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}