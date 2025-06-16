package com.sure.utilities;

import com.sure.base.DriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Log4j2
public class TestNGListener implements ITestListener {
    Path screenshotFolderPath;
    public static Path screenRecordingFolderPath;
    byte[] screenshotBytes;

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

    @Attachment(value = "Screenshot for the failure", type = "image/png", fileExtension = ".png")
    public byte[] attachScreenshotToAllure(DriverManager driverManager, String methodName) {
        screenshotBytes = null;

        if (driverManager.getDriver() instanceof TakesScreenshot takesscreenshot) {
            try {
                // Capture screenshot as a byte array
                screenshotBytes = takesscreenshot.getScreenshotAs(OutputType.BYTES);
            } catch (Exception e) {
                log.error("Failed to attach screenshot to Allure report for " + methodName, e);
            }
        } else {
            log.fatal("Driver does not support taking screenshots.");
        }
        return screenshotBytes;
    }

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
