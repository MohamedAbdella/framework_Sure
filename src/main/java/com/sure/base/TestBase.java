package com.sure.base;

import com.sure.utilities.*;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;

import static com.sure.base.DriverManager.*;

@Log4j2
public class TestBase {
    protected DriverManager driverManager;
    protected JsonFileManager jsonFileManagerLoginTestData;
    protected TestNGListener testNGListener;


    @BeforeClass
    public void setUp() throws Exception {
        log.info("Setting up the driver / server");
        driverManager = new DriverManager();
        testNGListener = new TestNGListener();
        jsonFileManagerLoginTestData = new JsonFileManager();
        jsonFileManagerLoginTestData.getJsonFilePath("testDataFolderPath", "Login.json");

        if (isMobilePlatform()) {
            prepareMobilePlatform();
        } else {
            setUpWebBrowser();
        }
    }

    @AfterClass
    public void tearDown() throws Exception {
        log.info("Tearing down the driver / server ");
        if (isMobilePlatform()) {
            cleanUpMobilePlatform();
        } else {
            driverManager.quitDriver(this.getClass());
        }
        GenerateAllureReport.openAllureReport();
    }

    private void prepareMobilePlatform() throws Exception {
        startEmulatorOrSimulator();
        driverManager.setExecutionType();
    }

    private void cleanUpMobilePlatform() throws IOException, InterruptedException {
        driverManager.quitDriver(this.getClass());
        AppiumServer.stopAppium();
        stopEmulatorOrSimulator();
    }

    private void setUpWebBrowser() throws Exception {
        if (isWebPlatform() && driverManager.getDriver() == null) {
            driverManager.setExecutionType();
        }
    }

    protected boolean isMobilePlatform() {
        return PLATFORM_ANDROID.equalsIgnoreCase(getPlatform()) || PLATFORM_IOS.equalsIgnoreCase(getPlatform());
    }

    protected boolean isWebPlatform() {
        return PLATFORM_WEB.equalsIgnoreCase(getPlatform());
    }

    private void startEmulatorOrSimulator() throws IOException, InterruptedException {
        String platform = getPlatform();
        if (PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
            AndroidEmulator.uninstallAutomationDriver();
            AndroidEmulator.startEmulator();
        } else if (PLATFORM_IOS.equalsIgnoreCase(platform)) {
            IOSSimulator.startSimulator();
        }
    }

    private void stopEmulatorOrSimulator() throws IOException, InterruptedException {
        String platform = getPlatform();
        if (PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
            AndroidEmulator.closeEmulator();
        } else if (PLATFORM_IOS.equalsIgnoreCase(platform)) {
            IOSSimulator.stopSimulator();
        }
    }

    private String getPlatform() {
        return driverManager.getConfigManager().getProperty("platformType");
    }
}
