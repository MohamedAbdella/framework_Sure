package com.sure.utilities;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.io.File;


import static com.sure.utilities.FilesDirectories.USER_DIR;

/**
 * Helper for controlling the Android emulator via shell scripts.
 */
@Log4j2
public final class AndroidEmulator {
    private AndroidEmulator() {
    }

    /**
     * Executes the provided shell command using an OS appropriate shell. On
     * Windows the method attempts to locate Git Bash to execute the script.
     *
     * @param command path to the script to execute
     */
    public static void executeCommand(String command) throws IOException {
        String osName = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder;
        if (osName.contains("win")) {
            String bash = "bash";
            String programFiles = System.getenv("ProgramFiles");
            if (programFiles != null) {
                String gitPath = programFiles + "\\Git\\bin\\bash.exe";
                if (new File(gitPath).exists()) {
                    bash = gitPath;
                }
            }
            if ("bash".equals(bash)) {
                String programFilesX86 = System.getenv("ProgramFiles(x86)");
                if (programFilesX86 != null) {
                    String gitPath = programFilesX86 + "\\Git\\bin\\bash.exe";
                    if (new File(gitPath).exists()) {
                        bash = gitPath;
                    }
                }
            }
            processBuilder = new ProcessBuilder(bash, command);
        } else {
            processBuilder = new ProcessBuilder("bash", command);
        }
        processBuilder.start();
    }


    /**
     * Launches the Android emulator using the bundled start script. Waits a few
     * seconds to ensure the device boots before returning.
     */
    public static void startEmulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/startEmulator.sh";
        log.info("scriptPath: {}", scriptPath);
        executeCommand(scriptPath);
        TimeUnit.SECONDS.sleep(9);
    }

    /**
     * Removes the Appium automation driver from the emulator to start clean.
     */
    public static void uninstallAutomationDriver() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/uninstallAutomationDriver.sh";
        executeCommand(scriptPath);
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * Shuts down the running emulator instance.
     */
    public static void closeEmulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/StopAndroidEmulator.sh";
        executeCommand(scriptPath);
        TimeUnit.SECONDS.sleep(1);
    }
}
