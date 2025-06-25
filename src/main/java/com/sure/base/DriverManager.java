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

    /**
     * Initializes the driver manager which is responsible for holding the
     * current {@link WebDriver} for the executing thread. It also prepares the
     * configuration and screen recording service used during tests.
     */
    public DriverManager() {
        this.driver = new ThreadLocal<>();
        this.configManager = ConfigManager.getInstance();
        this.recordingService = new ScreenRecordingService();
    }

    /**
     * Creates the appropriate WebDriver and starts screen recording.
     * <p>
     * The method delegates to {@link DriverFactory} to build the driver based on
     * configuration. Once created the driver is stored in a {@link ThreadLocal}
     * and screen recording is initiated.
     *
     * @throws Exception if driver creation fails
     */
    public void setExecutionType() throws Exception {
        WebDriver driverInstance = DriverFactory.createDriver(configManager);
        setDriver(driverInstance);
        recordingService.startRecording(driverInstance);
    }

    /**
     * Stops the screen recording and closes the current driver.
     *
     * @param testClass the test class requesting the quit, used to name the recording
     * @throws IOException if the recording service fails to save the video
     */
    public void quitDriver(Class<?> testClass) throws IOException {
        WebDriver currentDriver = getDriver();
        if (currentDriver != null) {
            recordingService.stopRecording(currentDriver, testClass.getSimpleName());
            currentDriver.quit();
            driver.remove();
        }
    }

    /**
     * Retrieves the driver for the current thread.
     *
     * @return the active {@link WebDriver} instance or {@code null} if none is set
     */
    public WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Stores the driver instance in the thread local variable.
     *
     * @param driverInstance the driver to store for later retrieval
     */
    private void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }
}
