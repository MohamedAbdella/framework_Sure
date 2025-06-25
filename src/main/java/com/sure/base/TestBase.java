package com.sure.base;

import com.sure.utilities.*;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;

import static com.sure.base.DriverManager.*;
import com.sure.configuration.ConfigKeys;

@Log4j2
public class TestBase {
    public DriverManager driverManager;
    protected JsonFileManager jsonFileManagerLoginTestData;


    /**
     * Framework level setup executed before any test class.
     * <p>
     * Initializes the {@link DriverManager}, loads test data files and creates the
     * WebDriver instance based on the configured platform type.
     */
    @BeforeClass
    public void setUp() throws Exception {
        log.info("Setting up the driver / server");
        driverManager = new DriverManager();
        jsonFileManagerLoginTestData = new JsonFileManager();
        jsonFileManagerLoginTestData.getJsonFilePath("testDataFolderPath", "Login.json");

        if (isMobilePlatform()) {
            prepareMobilePlatform();
        } else {
            setUpWebBrowser();
        }
    }

    /**
     * Cleans up resources after all tests in the class have run.
     * <p>
     * Depending on the platform, this stops emulators/simulators or simply quits
     * the browser. Finally it triggers the Allure report generation.
     */
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

    /**
     * Starts the required emulator or simulator and creates the mobile driver.
     */
    private void prepareMobilePlatform() throws Exception {
        startEmulatorOrSimulator();
        driverManager.setExecutionType();
    }

    /**
     * Stops the mobile driver, shuts down Appium and closes the emulator or simulator.
     */
    private void cleanUpMobilePlatform() throws IOException, InterruptedException {
        driverManager.quitDriver(this.getClass());
        AppiumServer.stopAppium();
        stopEmulatorOrSimulator();
    }

    /**
     * Creates a web driver if the configured platform is web.
     */
    private void setUpWebBrowser() throws Exception {
        if (isWebPlatform() && driverManager.getDriver() == null) {
            driverManager.setExecutionType();
        }
    }

    /**
     * @return {@code true} if the tests are running on Android or iOS
     */
    protected boolean isMobilePlatform() {
        return PLATFORM_ANDROID.equalsIgnoreCase(getPlatform()) || PLATFORM_IOS.equalsIgnoreCase(getPlatform());
    }

    /**
     * @return {@code true} if the platform type is web
     */
    protected boolean isWebPlatform() {
        return PLATFORM_WEB.equalsIgnoreCase(getPlatform());
    }

    /**
     * Launches the appropriate mobile emulator or simulator depending on the platform.
     */
    private void startEmulatorOrSimulator() throws IOException, InterruptedException {
        String platform = getPlatform();
        if (PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
            AndroidEmulator.uninstallAutomationDriver();
            AndroidEmulator.startEmulator();
        } else if (PLATFORM_IOS.equalsIgnoreCase(platform)) {
            IOSSimulator.startSimulator();
        }
    }

    /**
     * Shuts down the emulator or simulator depending on the platform.
     */
    private void stopEmulatorOrSimulator() throws IOException, InterruptedException {
        String platform = getPlatform();
        if (PLATFORM_ANDROID.equalsIgnoreCase(platform)) {
            AndroidEmulator.closeEmulator();
        } else if (PLATFORM_IOS.equalsIgnoreCase(platform)) {
            IOSSimulator.stopSimulator();
        }
    }

    /**
     * Reads the platform type from the configuration manager.
     *
     * @return the configured platform name
     */
    private String getPlatform() {
        return driverManager.getConfigManager().getProperty(ConfigKeys.PLATFORM_TYPE);
    }
}
