package com.sure.configuration;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Log4j2
/**
 * Singleton service responsible for loading and providing access to all
 * configuration values used within the automation framework. It reads
 * the main {@code path.properties} file to locate additional property
 * files and merges them into a single {@link Properties} instance.
 */
public class ConfigManager {
    private static ConfigManager instance;
    /** Internal storage of all loaded configuration values. */
    private final Properties properties = new Properties();

    /**
     * Creates a new instance and immediately loads all configuration files.
     * <p>
     * Steps:
     * <ol>
     *   <li>Loads the "path.properties" file which defines where other
     *       property files are located.</li>
     *   <li>Reads the {@link ConfigKeys#PROPERTIES_FOLDER_PATH} value to obtain
     *       the directory containing additional property files.</li>
     *   <li>Validates that the directory path exists and then loads every
     *       <code>*.properties</code> file within it.</li>
     * </ol>
     * This constructor is private as instances should only be retrieved via
     * {@link #getInstance()}.
     */
    private ConfigManager() {
        loadPathsProperties();
        String propertiesFolderPath = getProperty(ConfigKeys.PROPERTIES_FOLDER_PATH);
        if (propertiesFolderPath == null || propertiesFolderPath.trim().isEmpty()) {
            throw new RuntimeException("'propertiesFolderPath' is not set");
        }
        propertiesFolderPath = System.getProperty("user.dir") + propertiesFolderPath;
        loadPropertiesFromDirectory(propertiesFolderPath);
    }

    /**
     * Loads the initial {@code path.properties} file that lives under
     * <code>src/main/resources/properties</code>.
     * <p>
     * The file defines locations of other property files. Its contents are
     * loaded into the {@link #properties} instance.
     */
    private void loadPathsProperties() {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "properties", "path.properties");
        try (var inputStream = Files.newInputStream(path)) {
            properties.load(inputStream);
            log.info("Loaded path properties from {}", path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load path properties", e);
        }
    }

    /**
     * Reads every <code>*.properties</code> file from the supplied directory
     * and merges its values into the main property store.
     *
     * @param directoryPath absolute path to the folder containing property files
     */
    private void loadPropertiesFromDirectory(String directoryPath) {
        Path directory = Paths.get(directoryPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.properties")) {
            for (Path entry : stream) {
                // Skip the main path definition file as it has already been processed
                if (entry.getFileName().toString().equals("path.properties")) continue;
                loadProperties(entry);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from directory: " + directory, e);
        }
    }

    /**
     * Provides the singleton instance of the configuration manager.
     * Thread safe to ensure that configuration is loaded only once
     * even if accessed from multiple tests simultaneously.
     *
     * @return shared {@link ConfigManager} instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Loads the specified <code>.properties</code> file and merges its
     * contents with the existing configuration values.
     *
     * @param filePath path to the properties file on disk
     */
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

    /**
     * Retrieves a configuration value with optional overrides.
     * <p>
     * The lookup order is:
     * <ol>
     *   <li>JVM system property of the same name.</li>
     *   <li>Environment variable with that name.</li>
     *   <li>The properties loaded from configuration files.</li>
     * </ol>
     *
     * @param key property name to look up
     * @return resolved configuration value or {@code null} if none is found
     */
    public String getProperty(String key) {
        String override = System.getProperty(key);
        if (override != null) {
            return override;
        }
        override = System.getenv(key);
        if (override != null) {
            return override;
        }
        return properties.getProperty(key);
    }

    /**
     * Convenience method for boolean properties.
     *
     * @param key name of the property
     * @return {@code true} if the property value equals "true" (case insensitive)
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    /**
     * Retrieves a property as an integer.
     *
     * @param key name of the property
     * @return integer representation of the value
     * @throws IllegalArgumentException if the value is not a valid integer
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Property '" + key + "' is not an integer: " + value);
        }
    }
}
