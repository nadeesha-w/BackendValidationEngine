package com.qa.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<User> loadUsers(String filePath) {
        List<User> users = new ArrayList<User>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // skip the header row

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                int id = Integer.parseInt(columns[0]);
                String username = columns[1];
                String expectedStatus = columns[2];
                users.add(new User(id, username, expectedStatus));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}