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

@Log4j2
public final class JsonFileManager {
    private JsonObject jsonObject;
    private final ConfigManager configManager;

    // Constructor to initialize config manager
    public JsonFileManager() {
        this.configManager = new ConfigManager();
    }

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

    public void getJsonFilePath(String folderName, String filePath) throws FileNotFoundException {
        this.jsonObject = readJSONFile(folderName, filePath);
    }

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


    public String getJsonFileContent(String folderName, String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + configManager.getProperty(folderName) + filePath)));
        } catch (IOException e) {
            log.error("Error reading JSON file content: {}", e.getMessage(), e);
            return null;
        }
    }
}
