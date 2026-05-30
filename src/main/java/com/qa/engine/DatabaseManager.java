package com.qa.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages JDBC connections and SQL queries for the Backend Validation Engine.
 *
 * Provides methods to connect to a MySQL database and query user account
 * status for automated state transition validation. Connection details are
 * read from config.properties, which is not committed to the repository.
 */
public class DatabaseManager {

    private static final String DB_URL = DatabaseConfig.get("db.url");
    private static final String DB_USER = DatabaseConfig.get("db.user");
    private static final String DB_PASSWORD = DatabaseConfig.get("db.password");

    /**
     * Establish a JDBC connection to the QA test database.
     *
     * @return a Connection object if successful
     * @throws SQLException if the connection cannot be established
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Query the account_status of a specific user by their ID.
     *
     * @param userId the ID of the user to look up
     * @return the account_status string if the user exists, or null if not found
     */
    public static String getUserStatus(int userId) {
        String query = "SELECT account_status FROM Users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("account_status");
            }
            return null;

        } catch (SQLException e) {
            System.out.println("Error querying user status for ID " + userId + ": " + e.getMessage());
            return null;
        }
    }
}
