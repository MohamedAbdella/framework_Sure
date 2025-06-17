package com.sure.configuration;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Log4j2
public class ConfigManager {
    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        loadPathsProperties();
        String propertiesFolderPath = getProperty(ConfigKeys.PROPERTIES_FOLDER_PATH);
        if (propertiesFolderPath == null || propertiesFolderPath.trim().isEmpty()) {
            throw new RuntimeException("'propertiesFolderPath' is not set");
        }
        propertiesFolderPath = System.getProperty("user.dir") + propertiesFolderPath;
        loadPropertiesFromDirectory(propertiesFolderPath);
    }

    private void loadPathsProperties() {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "properties", "path.properties");
        try (var inputStream = Files.newInputStream(path)) {
            properties.load(inputStream);
            log.info("Loaded path properties from {}", path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load path properties", e);
        }
    }

    private void loadPropertiesFromDirectory(String directoryPath) {
        Path directory = Paths.get(directoryPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.properties")) {
            for (Path entry : stream) {
                if (entry.getFileName().toString().equals("path.properties")) continue;
                loadProperties(entry);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from directory: " + directory, e);
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
            Properties props = new Properties();
            props.load(inputStream);
            properties.putAll(props);
            log.info("Loaded properties from {}", filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Property '" + key + "' is not an integer: " + value);
        }
    }
}
