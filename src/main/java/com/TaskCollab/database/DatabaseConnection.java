package com.taskcollab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Attributes
    private String url;
    private String username;
    private String password;
    private Connection connection;  

    // Constructor
    public DatabaseConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Connect to database
    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }

    // Disconnect from database
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.out.println("Database disconnection failed.");
            e.printStackTrace();
        }
    }

    // Execute a query (e.g., SELECT statements)
    public void executeQuery(String query) {
        if (connection == null) {
            System.out.println("No database connection. Please connect first.");
            return;
        }

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                System.out.println("Query Result: " + resultSet.getString(1)); // Modify based on your schema
            }
        } catch (SQLException e) {
            System.out.println("Error executing query.");
            e.printStackTrace();
        }
    }
}
