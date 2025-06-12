package com.sure.utilities;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import static com.sure.utilities.FilesDirectories.USER_DIR;

@Log4j2
public final class AndroidEmulator {
    private AndroidEmulator() {
    }

    public static void executeCommand(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
        processBuilder.start();
    }

    public static void startEmulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/startEmulator.sh";
        log.info("scriptPath: {}", scriptPath);
        executeCommand(scriptPath);
        Thread.sleep(9000);
    }

    public static void uninstallAutomationDriver() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/uninstallAutomationDriver.sh";
        executeCommand(scriptPath);
        Thread.sleep(1000);
    }

    public static void closeEmulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/StopAndroidEmulator.sh";
        executeCommand(scriptPath);
        Thread.sleep(1000);
    }
}
