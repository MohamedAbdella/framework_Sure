package com.sure.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sure.configuration.ConfigManager;
import lombok.extern.log4j.Log4j2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility for reading JSON files stored under paths configured in
 * {@link ConfigManager}. Provides helper methods to retrieve test data values.
 */
@Log4j2
public final class JsonFileManager {
    private JsonObject jsonObject;
    private final ConfigManager configManager;

    /** Creates a new instance with access to the global configuration. */
    public JsonFileManager() {
        this.configManager = ConfigManager.getInstance();

    }

    /**
     * Reads a JSON file from the given folder and returns it as a
     * {@link JsonObject}.
     */
    private JsonObject readJSONFile(String folderName, String filePath) throws FileNotFoundException {
        FileReader fileReader = new FileReader(System.getProperty("user.dir") + configManager.getProperty(folderName) + filePath);
        log.info("JSON file: {}", fileReader);

        JsonElement jsonElement = JsonParser.parseReader(fileReader);
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        } else {
            throw new IllegalStateException("The JSON file does not contain a valid JSON object.");
        }

    }

    /**
     * Loads the JSON file so that values can be queried.
     */
    public void getJsonFilePath(String folderName, String filePath) throws FileNotFoundException {
        this.jsonObject = readJSONFile(folderName, filePath);
    }

    /**
     * Retrieves a value from the loaded JSON using a dotted path expression.
     */
    public String getTestData(String jsonPath) {
        try {
            if (jsonObject == null) {
                throw new IllegalStateException("JsonObject is not initialized. Make sure to call getJsonFilePath() first.");
            }

            // Split the jsonPath by "." to handle nested JSON objects
            String[] keys = jsonPath.split("\\.");
            JsonElement currentElement = jsonObject;

            for (String key : keys) {
                if (currentElement == null || !currentElement.isJsonObject()) {
                    log.error("Invalid path: {}", jsonPath);
                    return null;
                }
                currentElement = currentElement.getAsJsonObject().get(key);
            }

            if (currentElement == null) {
                log.error("No value found for path: {}", jsonPath);
                return null;
            }

            return currentElement.getAsString();
        } catch (Exception e) {
            log.error("Error retrieving test data for path {}: {}", jsonPath, e.getMessage(), e);
            return null;
        }
    }


    /**
     * Reads the entire JSON file as raw string content.
     */
    public String getJsonFileContent(String folderName, String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + configManager.getProperty(folderName) + filePath)));
        } catch (IOException e) {
            log.error("Error reading JSON file content: {}", e.getMessage(), e);
            return null;
        }
    }
}
