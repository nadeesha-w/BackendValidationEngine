package com.qa.engine;

import java.util.List;

/**
 * Manual runner. Prints the expected and actual status for every scenario in
 * the test data file. The same checks run as assertions under mvn test.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Backend Validation Engine");

        List<User> users = DataLoader.loadUsers("data/test-data.csv");
        for (User user : users) {
            String actual = DatabaseManager.getUserStatus(user.getId());
            System.out.println(user.getUsername()
                    + " expected=" + user.getExpectedStatus()
                    + " actual=" + actual);
        }
    }
}
