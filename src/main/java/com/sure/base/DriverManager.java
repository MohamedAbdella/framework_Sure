package com.sure.base;

import com.sure.configuration.ConfigManager;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.android.AndroidStopScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static com.sure.utilities.TestNGListener.screenRecordingFolderPath;
import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

@Getter
@Log4j2
public class DriverManager {

    private final ConfigManager configManager;
    protected final ThreadLocal<WebDriver> driver;
    protected Process screenRecordingProcess;

    public static final String PLATFORM_WEB = "web";
    public static final String PLATFORM_ANDROID = "android";
    public static final String PLATFORM_IOS = "ios";
    private ScreenRecorder webScreenRecorder;


    public DriverManager() {
        this.driver = new ThreadLocal<>();
        this.configManager = ConfigManager.getInstance();
    }

    public void setExecutionType() throws Exception {
        WebDriver driverInstance = DriverFactory.createDriver(configManager);
        setDriver(driverInstance);
        startScreenRecording();
    }

    public void quitDriver(Class<?> testClass) throws IOException {
        WebDriver currentDriver = getDriver();
        if (currentDriver != null) {
            stopScreenRecording(testClass.getSimpleName());
            currentDriver.quit();
            driver.remove();
        }
    }

    public WebDriver getDriver() {
        return driver.get();

    }

    private void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    @Step("Start Screen Recording")
    private void startScreenRecording() {
        String platform = configManager.getProperty("platformType");
        try {
            if (PLATFORM_IOS.equalsIgnoreCase(platform)) {
                startIOSRecording();
            } else if (PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
                startAndroidRecording();
            } else if (PLATFORM_WEB.equalsIgnoreCase(platform)) {
                startWebScreenRecording();
            } else {
                log.warn("Unsupported platform type: " + platform);
            }
        } catch (Exception e) {
            log.error("Failed to start screen recording: " + e.getMessage(), e);
        }
    }

    @Step("Stop Screen Recording")
    private void stopScreenRecording(String className) throws IOException {
        String platform = configManager.getProperty("platformType");
        if (PLATFORM_IOS.equalsIgnoreCase(platform)) {
            stopIOSRecording(className);
        } else if (PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
            stopAndroidRecording(className);
        } else if (PLATFORM_WEB.equalsIgnoreCase(platform)) {
            stopWebScreenRecording(className);
        } else {
            log.warn("Unsupported platform type: " + platform);
        }
    }

    private void startIOSRecording() {
        WebDriver currentDriver = getDriver();
        if (currentDriver instanceof CanRecordScreen screenRecorder) {
            screenRecorder.startRecordingScreen(new IOSStartScreenRecordingOptions()
                    .withFps(30)
                    .withVideoScale("320:-2")
                    .withVideoType("h264")
                    .withTimeLimit(Duration.ofMinutes(5)));
            log.info("Started screen recording for iOS.");
        } else {
            log.info("Driver does not support screen recording for iOS");
        }
    }

    private void stopIOSRecording(String className) {
        WebDriver currentDriver = getDriver();
        if (currentDriver instanceof CanRecordScreen screenRecorder) {
            String base64Video = screenRecorder.stopRecordingScreen(new IOSStopScreenRecordingOptions());
            saveVideo(base64Video, className, PLATFORM_IOS);
            log.info("Stopped screen recording for iOS.");
        } else {
            log.info("Driver does not support screen recording.");
        }
    }

    private void startAndroidRecording() {
        WebDriver currentDriver = getDriver();
        if (currentDriver instanceof CanRecordScreen screenRecorder) {
            screenRecorder.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                    .withTimeLimit(Duration.ofMinutes(5))
                    .withBitRate(5000000)
                    .withVideoSize("1280x720"));
            log.info("Started screen recording for Android.");
        } else {
            log.info("Driver does not support screen recording for Android");
        }
    }

    private void stopAndroidRecording(String className) {
        WebDriver currentDriver = getDriver();
        if (currentDriver instanceof CanRecordScreen screenRecorder) {
            String base64Video = screenRecorder.stopRecordingScreen(new AndroidStopScreenRecordingOptions());
            saveVideo(base64Video, className, PLATFORM_ANDROID);
            log.info("Stopped screen recording for Android.");
        } else {
            log.info("Driver does not support screen recording.");
        }
    }

    public void startWebScreenRecording() throws IOException, AWTException {
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        webScreenRecorder = new ScreenRecorder(
                gc,
                gc.getBounds(),
                new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_QUICKTIME),
                new Format(
                        MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_QUICKTIME_JPEG,
                        CompressorNameKey, ENCODING_QUICKTIME_JPEG,
                        DepthKey, 24, FrameRateKey, Rational.valueOf(30),
                        QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                null, null);

        webScreenRecorder.start();
        log.info("Web screen recording started");
    }

    public void stopWebScreenRecording(String className) throws IOException {
        webScreenRecorder.stop();

        // Get the recorded file
        File recordedFile = webScreenRecorder.getCreatedMovieFiles().get(0);
        log.info("Recording saved at: " + recordedFile.getAbsolutePath());

        // Define the new file name and location
        File newFile = new File(screenRecordingFolderPath.toString(), className + "_" + PLATFORM_WEB + ".avi");
        log.info("New Recording saved at: " + newFile.getAbsolutePath());

        // Rename or move the file to the new location
        if (recordedFile.renameTo(newFile)) {
            log.info("File successfully moved to: " + newFile.getAbsolutePath());
        } else {
            log.error("Failed to move the file to: " + newFile.getAbsolutePath());
        }

        // Optionally, convert the AVI file to MP4
        Path mp4FilePath = screenRecordingFolderPath.resolve(className + "_" + PLATFORM_WEB + ".mp4");
        convertAviToMp4(newFile, mp4FilePath.toFile());
    }

    public void convertAviToMp4(File inputFile, File outputFile) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg",
                    "-i", inputFile.getAbsolutePath(),
                    "-vcodec", "libx264",
                    "-acodec", "aac",
                    outputFile.getAbsolutePath()
            );

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {  // Check if the conversion was successful
                log.info("Converted AVI to MP4: " + outputFile.getAbsolutePath());

                // Delete the original AVI file using java.nio.file.Files
                try {
                    Files.delete(inputFile.toPath());
                    log.info("Deleted original AVI file: " + inputFile.getAbsolutePath());
                } catch (IOException e) {
                    log.error("Failed to delete original AVI file: " + inputFile.getAbsolutePath(), e);
                }
            } else {
                log.error("Failed to convert AVI to MP4. Process exited with code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Re-interrupt the thread
            log.error("Conversion process was interrupted", e);
        } catch (IOException e) {
            log.error("Failed to convert AVI to MP4: " + e.getMessage(), e);
        }
    }

    private void saveVideo(String base64String, String className, String platform) {
        try {
            byte[] data = Base64.decodeBase64(base64String);
            String fileName = className + "_" + platform + ".mp4";
            Path path = Paths.get(String.valueOf(screenRecordingFolderPath), fileName);
            Files.write(path, data);
            log.info("Saved screen recording to: " + path);
        } catch (IOException e) {
            log.error("Failed to save screen recording: " + e.getMessage(), e);
        }
    }
}