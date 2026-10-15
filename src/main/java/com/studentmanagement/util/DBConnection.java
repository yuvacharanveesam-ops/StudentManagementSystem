package com.studentmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Database Connection Utility
 *
 * Module 5 Concept: JDBC Architecture
 *
 * JDBC Architecture layers demonstrated here:
 *   Java Application
 *       ↓
 *   JDBC API (java.sql.*)
 *       ↓
 *   JDBC Driver Manager (DriverManager)
 *       ↓
 *   JDBC Driver (MySQL Connector/J)
 *       ↓
 *   MySQL Database
 *
 * This class uses DriverManager to obtain a Connection object.
 * All database configuration is centralised here – change only this
 * file if the host / credentials change.
 */
public class DBConnection {

    // ── Database Configuration ─────────────────────────────────────────────────
    // UPDATE these values to match your local MySQL installation
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER     = "root";       // <-- change if needed
    private static final String DB_PASSWORD = "root";       // <-- change to your MySQL password

    // MySQL JDBC driver class name (auto-loaded in JDBC 4+, but registering
    // explicitly makes the concept visible for academic purposes)
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    // Static block: loads the MySQL JDBC driver once when the class is loaded
    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.err.println("[DBConnection] MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Returns a new JDBC Connection to the student_management database.
     *
     * Usage example (try-with-resources):
     *   try (Connection con = DBConnection.getConnection()) {
     *       // use connection
     *   }  // connection is auto-closed here
     *
     * @return java.sql.Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Tests whether the database is reachable.
     * Used by the application at startup to give a helpful error message.
     *
     * @return true if connection succeeds, false otherwise
     */
    public static boolean testConnection() {
        try (Connection con = getConnection()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            System.err.println("[DBConnection] Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
