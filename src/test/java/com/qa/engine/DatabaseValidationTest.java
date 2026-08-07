package com.qa.engine;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    public void unknownUserReturnsNull() {
        assertNull(DatabaseManager.getUserStatus(9999),
                "A user id that does not exist should come back as null, not an empty string");
    }

    @Test
    public void testDataFileLoadsEveryScenario() {
        List<User> users = DataLoader.loadUsers(TEST_DATA);
        assertEquals(3, users.size(), "Expected three scenarios in " + TEST_DATA);
    }
}
