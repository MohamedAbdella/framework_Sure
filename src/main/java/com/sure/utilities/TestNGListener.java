package com.sure.utilities;

import com.sure.base.DriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TestNG listener that handles screenshot capture and video recording
 * attachment when tests fail.
 */
@Log4j2
public class TestNGListener implements ITestListener {
    Path screenshotFolderPath;
    public static Path screenRecordingFolderPath;
    byte[] screenshotBytes;

    /**
     * Creates the folders used for screenshot and video attachments.
     */
    public TestNGListener() {
        screenshotFolderPath = FilesDirectories.createDir("/attachments/screenshots");
        screenRecordingFolderPath = FilesDirectories.createDir("/attachments/videos");
        try {
            Files.createDirectories(screenshotFolderPath);
            Files.createDirectories(screenRecordingFolderPath);
        } catch (IOException e) {
            log.error("Failed to create directories for screenshots and screen recording", e);
        }
    }


    /**
     * Triggered by TestNG on test failure. Captures a screenshot through the
     * {@link DriverManager} of the running {@code TestBase} instance and stores
     * it locally and within the Allure report.
     */
    @Override
    @Step("Test Failed")
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        log.error("methodName:" + methodName);
        if (result.getInstance() instanceof com.sure.base.TestBase testBase) {
            DriverManager dm = testBase.driverManager;
            // Attach screenshot to Allure report
            attachScreenshotToAllure(dm, methodName);
            // Save screenshot locally
            saveScreenshotLocally(dm, methodName);
        } else {
            log.error("Test instance is not of type TestBase");
        }
        log.error("Failure for " + methodName);
    }

    /**
     * Captures the screenshot bytes and attaches them to the Allure report.
     *
     * @param driverManager manager providing the driver instance
     * @param methodName    the failed test method
     * @return screenshot bytes or {@code null} when not available
     */
    public byte[] attachScreenshotToAllure(DriverManager driverManager, String methodName) {
        screenshotBytes = null;

        if (driverManager.getDriver() instanceof TakesScreenshot takesscreenshot) {
            try {
                // Capture screenshot as a byte array
                screenshotBytes = takesscreenshot.getScreenshotAs(OutputType.BYTES);
                byte[] finalBytes = screenshotBytes;
                Allure.step("Capturing screenshot on failure", () ->
                        Allure.addAttachment(methodName + "-failure", "image/png",
                                new ByteArrayInputStream(finalBytes), "png"));
            } catch (Exception e) {
                log.error("Failed to attach screenshot to Allure report for " + methodName, e);
            }
        } else {
            log.fatal("Driver does not support taking screenshots.");
        }
        return screenshotBytes;
    }

    /**
     * Saves the screenshot to the local attachments directory for future reference.
     */
    @Step("Saving screenshot Locally for {methodName}")
    private void saveScreenshotLocally(DriverManager driverManager, String methodName) {
        if (driverManager.getDriver() instanceof TakesScreenshot takesscreenshot) {
            try {
                // Capture screenshot as a byte array
                screenshotBytes = takesscreenshot.getScreenshotAs(OutputType.BYTES);

                // Define the destination file
                File destinationFile = new File(screenshotFolderPath.toString(), methodName + ".png");

                // Write byte array to the file
                FileUtils.writeByteArrayToFile(destinationFile, screenshotBytes);

                log.info("Screenshot saved locally for " + methodName);
            } catch (IOException e) {
                log.error("Failed to save screenshot for " + methodName, e);
            }
        } else {
            log.fatal("Driver does not support taking screenshots.");
        }
    }
}
