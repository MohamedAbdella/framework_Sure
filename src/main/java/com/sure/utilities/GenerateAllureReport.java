package com.sure.utilities;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.sure.utilities.FilesDirectories.USER_DIR;

/**
 * Utility responsible for launching the Allure report viewer.
 * <p>
 * The class exposes only static methods and is used by the build or test
 * process once results have been generated. It executes the appropriate
 * operating system command in order to serve the report locally.
 */
@Log4j2
public final class GenerateAllureReport {

    /** Utility class - prevent instantiation. */
    private GenerateAllureReport() {
    }

    /**
     * Executes a shell command using {@link ProcessBuilder}.
     *
     * @param command command and its arguments as an array
     * @throws IOException if the process fails to start
     */
    private static void executeCommand(String... command) throws IOException {
        new ProcessBuilder(command).start();
    }

    /**
     * Launches the Allure report viewer depending on the host OS.
     * <p>
     * On Windows the method invokes {@code allure serve} directly. On Unix like
     * systems it runs a helper shell script located in the resources folder.
     * This method blocks briefly to allow the server to start.
     *
     * @throws IOException          if executing the command fails
     * @throws InterruptedException if the sleep is interrupted
     */
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
