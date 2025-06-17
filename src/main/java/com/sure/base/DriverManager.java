package com.sure.base;

import com.sure.configuration.ConfigManager;
import com.sure.utilities.ScreenRecordingService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

@Getter
@Log4j2
public class DriverManager {

    private final ConfigManager configManager;
    protected final ThreadLocal<WebDriver> driver;
    private final ScreenRecordingService recordingService;

    public static final String PLATFORM_WEB = "web";
    public static final String PLATFORM_ANDROID = "android";
    public static final String PLATFORM_IOS = "ios";

    public DriverManager() {
        this.driver = new ThreadLocal<>();
        this.configManager = ConfigManager.getInstance();
        this.recordingService = new ScreenRecordingService();
    }

    public void setExecutionType() throws Exception {
        WebDriver driverInstance = DriverFactory.createDriver(configManager);
        setDriver(driverInstance);
        recordingService.startRecording(driverInstance);
    }

    public void quitDriver(Class<?> testClass) throws IOException {
        WebDriver currentDriver = getDriver();
        if (currentDriver != null) {
            recordingService.stopRecording(currentDriver, testClass.getSimpleName());
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
}
