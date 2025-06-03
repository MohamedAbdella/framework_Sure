package com.sure.utilities;

import java.io.IOException;

import static com.sure.utilities.FilesDirectories.USER_DIR;

public class IOSSimulator extends AndroidEmulator {

    public static void startSimulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/startIOSSimulator.sh";
        executeCommand(scriptPath);
        Thread.sleep(9000);
    }


    public static void stopSimulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/stopIOSSimulator.sh";
        executeCommand(scriptPath);
        Thread.sleep(1000);
    }
}
