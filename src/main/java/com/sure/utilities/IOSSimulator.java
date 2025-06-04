package com.sure.utilities;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.sure.utilities.FilesDirectories.USER_DIR;

public class IOSSimulator extends AndroidEmulator {

    public static void startSimulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/startIOSSimulator.sh";
        executeCommand(scriptPath);
        TimeUnit.SECONDS.sleep(9);
    }


    public static void stopSimulator() throws IOException, InterruptedException {
        String scriptPath = USER_DIR + "/src/main/resources/stopIOSSimulator.sh";
        executeCommand(scriptPath);
        TimeUnit.SECONDS.sleep(1);
    }
}
