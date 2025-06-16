package com.sure.configuration;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2
public class ConfigManager {
    private static ConfigManager instance;
    private final Map<String, Properties> propertiesMap;

    private ConfigManager() {
        propertiesMap = new HashMap<>();
        loadPathsProperties();
        // Get the properties folder path from the configuration
        String propertiesFolderPath = getProperty("propertiesFolderPath");
        log.info("Retrieved propertiesFolderPath: " + propertiesFolderPath);

        // Validate and use the properties folder path
        if (propertiesFolderPath == null || propertiesFolderPath.trim().isEmpty()) {
            throw new RuntimeException("The 'propertiesFolderPath' property is not set or is empty.");
        }

        // Construct the full path
        propertiesFolderPath = System.getProperty("user.dir") + propertiesFolderPath;
        log.info("Full properties folder path: " + propertiesFolderPath);
        loadPropertiesFromDirectory(propertiesFolderPath);
    }

    private void loadPathsProperties() {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "properties", "path.properties");
        try (var inputStream = Files.newInputStream(path)) {
            Properties pathsProperties = new Properties();
            pathsProperties.load(inputStream);
            propertiesMap.put("path.properties", pathsProperties);
            log.info("Loaded paths properties from: " + path);
        } catch (IOException e) {
            log.error("Failed to load paths properties file: " + path, e);
            throw new RuntimeException("Failed to load paths properties file: " + path, e);
        }
    }

    private void loadPropertiesFromDirectory(String directoryPath) {
        Path directory = Paths.get(directoryPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.properties")) {
            for (Path entry : stream) {
                loadProperties(entry);
            }
        } catch (IOException e) {
            log.error("Failed to load properties from directory:{}", directory.toAbsolutePath(), e);
            throw new RuntimeException("Failed to load properties from directory: " + directory.toAbsolutePath(), e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties(Path filePath) {
        try (var inputStream = Files.newInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            propertiesMap.put(filePath.getFileName().toString(), properties);
            log.info("Loaded properties from file: " + filePath);
        } catch (IOException e) {
            log.error("Failed to load properties file: " + filePath.toAbsolutePath(), e);
            throw new RuntimeException("Failed to load properties file: " + filePath.toAbsolutePath(), e);
        }
    }

    public String getProperty(String key) {
        // Log each property retrieval for debugging
        for (Map.Entry<String, Properties> entry : propertiesMap.entrySet()) {
            log.info("Checking properties file: " + entry.getKey());
            Properties properties = entry.getValue();
            String value = properties.getProperty(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}