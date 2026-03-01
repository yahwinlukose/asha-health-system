package com.asha.asha.health.system.ui;

import com.asha.asha.health.system.db.DBConnection;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        // Try to get database connection
        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("Database Connected Successfully!");
        } else {
            System.out.println("Database Connection Failed!");
        }
    }
}