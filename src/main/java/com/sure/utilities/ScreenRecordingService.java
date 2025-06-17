package com.sure.utilities;

import com.sure.base.DriverManager;
import com.sure.configuration.ConfigKeys;
import com.sure.configuration.ConfigManager;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.android.AndroidStopScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStopScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
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

@Log4j2
public class ScreenRecordingService {

    private final ConfigManager configManager = ConfigManager.getInstance();
    private ScreenRecorder webScreenRecorder;

    public void startRecording(WebDriver driver) {
        String platform = configManager.getProperty(ConfigKeys.PLATFORM_TYPE);
        try {
            if (DriverManager.PLATFORM_IOS.equalsIgnoreCase(platform)) {
                startIOSRecording(driver);
            } else if (DriverManager.PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
                startAndroidRecording(driver);
            } else if (DriverManager.PLATFORM_WEB.equalsIgnoreCase(platform)) {
                startWebRecording();
            }
        } catch (Exception e) {
            log.error("Failed to start screen recording", e);
        }
    }

    public void stopRecording(WebDriver driver, String className) {
        String platform = configManager.getProperty(ConfigKeys.PLATFORM_TYPE);
        try {
            if (DriverManager.PLATFORM_IOS.equalsIgnoreCase(platform)) {
                stopIOSRecording(driver, className);
            } else if (DriverManager.PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
                stopAndroidRecording(driver, className);
            } else if (DriverManager.PLATFORM_WEB.equalsIgnoreCase(platform)) {
                stopWebRecording(className);
            }
        } catch (Exception e) {
            log.error("Failed to stop screen recording", e);
        }
    }

    private void startIOSRecording(WebDriver driver) {
        if (driver instanceof CanRecordScreen screenRecorder) {
            screenRecorder.startRecordingScreen(new IOSStartScreenRecordingOptions()
                    .withFps(30)
                    .withVideoScale("320:-2")
                    .withVideoType("h264")
                    .withTimeLimit(Duration.ofMinutes(5)));
        }
    }

    private void stopIOSRecording(WebDriver driver, String className) {
        if (driver instanceof CanRecordScreen screenRecorder) {
            String base64 = screenRecorder.stopRecordingScreen(new IOSStopScreenRecordingOptions());
            saveVideo(base64, className, DriverManager.PLATFORM_IOS);
        }
    }

    private void startAndroidRecording(WebDriver driver) {
        if (driver instanceof CanRecordScreen screenRecorder) {
            screenRecorder.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                    .withTimeLimit(Duration.ofMinutes(5))
                    .withBitRate(5000000)
                    .withVideoSize("1280x720"));
        }
    }

    private void stopAndroidRecording(WebDriver driver, String className) {
        if (driver instanceof CanRecordScreen screenRecorder) {
            String base64 = screenRecorder.stopRecordingScreen(new AndroidStopScreenRecordingOptions());
            saveVideo(base64, className, DriverManager.PLATFORM_ANDROID);
        }
    }

    private void startWebRecording() throws IOException, AWTException {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        webScreenRecorder = new ScreenRecorder(gc, gc.getBounds(),
                new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_QUICKTIME),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_QUICKTIME_JPEG,
                        CompressorNameKey, ENCODING_QUICKTIME_JPEG,
                        DepthKey, 24, FrameRateKey, Rational.valueOf(30),
                        QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                null, null);
        webScreenRecorder.start();
    }

    private void stopWebRecording(String className) throws IOException {
        if (webScreenRecorder != null) {
            webScreenRecorder.stop();
            File recordedFile = webScreenRecorder.getCreatedMovieFiles().get(0);
            File newFile = new File(screenRecordingFolderPath.toString(), className + "_web.avi");
            if (!recordedFile.renameTo(newFile)) {
                log.error("Failed to move recorded file");
            }
            Path mp4FilePath = screenRecordingFolderPath.resolve(className + "_web.mp4");
            convertAviToMp4(newFile, mp4FilePath.toFile());
        }
    }

    private void convertAviToMp4(File inputFile, File outputFile) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", inputFile.getAbsolutePath(),
                    "-vcodec", "libx264", "-acodec", "aac", outputFile.getAbsolutePath());
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                Files.deleteIfExists(inputFile.toPath());
            } else {
                log.error("ffmpeg exited with code {}", exitCode);
            }
        } catch (Exception e) {
            log.error("Failed to convert video", e);
        }
    }

    private void saveVideo(String base64String, String className, String platform) {
        try {
            byte[] data = Base64.decodeBase64(base64String);
            String fileName = className + "_" + platform + ".mp4";
            Path path = Paths.get(String.valueOf(screenRecordingFolderPath), fileName);
            Files.write(path, data);
        } catch (IOException e) {
            log.error("Failed to save screen recording", e);
        }
    }
}
