package com.comp3005.finalproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  Handles PostgreSQL database connections for the Health and Fitness Club Management System.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";
    
    /**
     * Establishes a connection to the PostgreSQL database.
     * 
     * @return a Connection object if successful, or null otherwise
     */
    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to database: ");
            e.printStackTrace();
        }
        
        return null;
    }
}
