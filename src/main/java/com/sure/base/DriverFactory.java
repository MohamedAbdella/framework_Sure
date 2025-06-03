package com.sure.base;

import com.sure.configuration.ConfigManager;
import com.sure.utilities.AppiumServer;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

@Log4j2
public class DriverFactory {
    private DriverFactory() {
        // Prevent instantiation
    }

    private static final String SELENIUM_HUB_URL = "http://localhost:4444/wd/hub";
    private static final String ANDROID_PLATFORM_NAME = "Android";
    private static final String IOS_PLATFORM_NAME = "iOS";
    private static final String WEB_PLATFORM_NAME = "web";
    private static final String BROWSER_FIREFOX = "firefox";
    private static final String BROWSER_CHROME = "chrome";
    private static final String BROWSER_SAFARI = "safari";
    private static final String BROWSER_EDGE = "edge";

    public static WebDriver createDriver(ConfigManager configManager) throws Exception {
        String executionType = configManager.getProperty("executeType");
        String platform = configManager.getProperty("platformType");
        return "remote".equals(executionType)
                ? createRemoteWebDriver(configManager, platform)
                : createLocalDriver(configManager, platform);
    }

    private static WebDriver createLocalDriver(ConfigManager configManager, String platform) throws Exception {
        return switch (platform) {
            case WEB_PLATFORM_NAME -> createLocalWebDriver(configManager);
            case ANDROID_PLATFORM_NAME -> createAndroidDriver(configManager);
            case IOS_PLATFORM_NAME -> createIOSDriver(configManager);
            default -> throw new IllegalArgumentException("Invalid platform: " + platform);
        };
    }

    private static WebDriver createRemoteWebDriver(ConfigManager configManager, String platform) throws Exception {
        if (platform.equals(WEB_PLATFORM_NAME)) {
            return createDockerWebDriver(configManager);
        }
        throw new IllegalArgumentException("Invalid platform: " + platform);
    }

    private static WebDriver createLocalWebDriver(ConfigManager configManager) throws Exception {
        String browser = configManager.getProperty("webBrowserName").toLowerCase();
        WebDriver driver = switch (browser) {
            case BROWSER_FIREFOX -> new FirefoxDriver(setupFirefoxOptions());
            case BROWSER_CHROME -> new ChromeDriver(setupChromeOptions());
            case BROWSER_SAFARI -> new SafariDriver();
            case BROWSER_EDGE -> new EdgeDriver(setupEdgeOptions());
            default -> throw new Exception("Invalid browser: " + browser);
        };

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        return driver;
    }

    private static FirefoxOptions setupFirefoxOptions() {
        return new FirefoxOptions();
    }

    private static ChromeOptions setupChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*");
        return options;
    }

    private static EdgeOptions setupEdgeOptions() {
        return new EdgeOptions();
    }


    private static AndroidDriver createAndroidDriver(ConfigManager configManager) throws IOException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(ANDROID_PLATFORM_NAME)
                .setDeviceName(configManager.getProperty("androidDeviceName"))
                .setPlatformVersion(configManager.getProperty("androidPlatformVersion"))
                .setAutomationName(configManager.getProperty("androidAutomationName"))
                .setAppPackage(configManager.getProperty("androidAppPackage"))
                .setAppWaitActivity(configManager.getProperty("androidAppWaitActivity"))
                .setApp(System.getProperty("user.dir") + configManager.getProperty("appsFolderPath") + configManager.getProperty("androidAppFilePath"))
                .setNoReset(false)
                .setAutoGrantPermissions(true);

        options.setCapability("autoAcceptAlerts", Boolean.parseBoolean(configManager.getProperty("autoAcceptAlerts")));
        options.setCapability("androidEmulatorId", configManager.getProperty("androidEmulatorId"));
        options.setCapability("hideKeyboard", true);

        String appiumUrl = startAppiumServer();

        log.info("Connecting to Appium server at: {}", appiumUrl);
        return new AndroidDriver(new URL(appiumUrl), options);
    }

    private static IOSDriver createIOSDriver(ConfigManager configManager) throws IOException {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName(IOS_PLATFORM_NAME)
                .setDeviceName(configManager.getProperty("iosDeviceName"))
                .setPlatformVersion(configManager.getProperty("iosPlatformVersion"))
                .setAutomationName(configManager.getProperty("iosAutomationName"))
                .setBundleId(configManager.getProperty("iosAppPackage"))
                .setApp(System.getProperty("user.dir") + configManager.getProperty("appsFolderPath") + configManager.getProperty("iosAppFilePath"))
                .setNoReset(true)
                .setAutoAcceptAlerts(true);

        String appiumUrl = startAppiumServer();

        log.info("Connecting to Appium server at: {}", appiumUrl);
        return new IOSDriver(new URL(appiumUrl), options);
    }

    private static String startAppiumServer() {
        return AppiumServer.startAppium();  // This should start the Appium server and assign the service.

    }

    private static WebDriver createDockerWebDriver(ConfigManager configManager) throws Exception {
        String browser = configManager.getProperty("webBrowserName").toLowerCase();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(browser);
        return new RemoteWebDriver(new URL(SELENIUM_HUB_URL), capabilities);
    }
}
