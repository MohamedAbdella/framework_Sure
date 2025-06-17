package com.sure.utilities;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.sure.utilities.FilesDirectories.USER_DIR;

@Log4j2
public final class GenerateAllureReport {
    private GenerateAllureReport() {
    }

    private static void executeCommand(String... command) throws IOException {
        new ProcessBuilder(command).start();
    }

    public static void openAllureReport() throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                executeCommand("cmd", "/c", "allure", "serve");
            } catch (IOException e) {
                log.error("Failed to open Allure report on Windows", e);
            }
        } else {
            String scriptPath = USER_DIR + "/src/main/resources/generateAllureReport.sh";
            log.info("scriptPath: {}", scriptPath);
            try {
                executeCommand("bash", scriptPath);
            } catch (IOException e) {
                log.error("Failed to execute Allure script", e);
            }
        }
        TimeUnit.SECONDS.sleep(9);
    }
}
