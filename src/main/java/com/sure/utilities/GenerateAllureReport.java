package com.sure.utilities;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import static com.sure.utilities.FilesDirectories.USER_DIR;

@Log4j2
public final class GenerateAllureReport {
    private GenerateAllureReport() {
    }

    private static void executeCommand(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
        processBuilder.start();
    }

    public static void openAllureReport() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/generateAllureReport.sh";
        log.info("scriptPath: {}", scriptPath);
        executeCommand(scriptPath);
        Thread.sleep(9000);
    }
}
