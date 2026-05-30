package com.qa.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads database settings from config.properties on the classpath.
 *
 * The real file is gitignored. config.example.properties is committed so
 * anyone cloning the repo knows which keys to fill in.
 */
public class DatabaseConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private static Properties load() {
        Properties properties = new Properties();

        try (InputStream input =
                     DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new IllegalStateException(
                        CONFIG_FILE + " not found. Copy config.example.properties to "
                                + CONFIG_FILE + " and fill in your database details.");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + CONFIG_FILE, e);
        }

        return properties;
    }

    /**
     * Read a required configuration value.
     *
     * @param key the property name
     * @return the configured value
     */
    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing " + key + " in " + CONFIG_FILE);
        }

        return value;
    }
}
