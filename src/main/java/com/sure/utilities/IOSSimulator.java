package com.sure.utilities;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.sure.utilities.FilesDirectories.USER_DIR;

/**
 * Small helper that starts and stops the iOS simulator via shell scripts.
 */
public class IOSSimulator  {

    /**
     * Launches the iOS simulator using the predefined script.
     */
    public static void startSimulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/startIOSSimulator.sh";
        AndroidEmulator.executeCommand(scriptPath);
        TimeUnit.SECONDS.sleep(9);
    }


    /**
     * Stops the running iOS simulator instance.
     */
    public static void stopSimulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/stopIOSSimulator.sh";
        AndroidEmulator.executeCommand(scriptPath);
        TimeUnit.SECONDS.sleep(1);
    }
}
