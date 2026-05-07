package com.qa.engine;

public class User {

    private int id;
    private String username;
    private String expectedStatus;

    public User(int id, String username, String expectedStatus) {
        this.id = id;
        this.username = username;
        this.expectedStatus = expectedStatus;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getExpectedStatus() {
        return expectedStatus;
    }
}