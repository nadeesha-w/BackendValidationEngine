package com.qa.engine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Data-driven validation of database state transitions.
 *
 * Requires a running MySQL instance seeded with db/schema.sql, db/seed.sql
 * and db/queries.sql.
 */
public class DatabaseValidationTest {

    private static final String TEST_DATA = "data/test-data.csv";

    @Test
    public void runnerIsWorking() {
        System.out.println("JUnit is running.");
    }

    @Test
    public void validateUserStatusUpdate() {
        List<User> users = DataLoader.loadUsers(TEST_DATA);

        for (User user : users) {
            String actual = DatabaseManager.getUserStatus(user.getId());
            assertEquals(user.getExpectedStatus(), actual,
                    "Status mismatch for user " + user.getUsername());
        }
    }
}
