package com.qa.engine;

import java.util.List;

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