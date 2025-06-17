package com.sure.base.factory;

import com.sure.configuration.ConfigKeys;
import com.sure.configuration.ConfigManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.URL;
import java.time.Duration;

@Log4j2
public class WebDriverFactory implements DriverSetup {

    @Override
    public WebDriver createDriver(ConfigManager configManager) throws Exception {
        String execution = configManager.getProperty(ConfigKeys.EXECUTE_TYPE);
        if ("remote".equalsIgnoreCase(execution)) {
            return createRemoteWebDriver(configManager);
        }
        return createLocalWebDriver(configManager);
    }

    private WebDriver createLocalWebDriver(ConfigManager configManager) throws Exception {
        String browser = configManager.getProperty(ConfigKeys.WEB_BROWSER_NAME).toLowerCase();
        WebDriver driver = switch (browser) {
            case "firefox" -> new FirefoxDriver(new FirefoxOptions());
            case "chrome" -> new ChromeDriver(setupChromeOptions());
            case "safari" -> new SafariDriver(new SafariOptions());
            case "edge" -> new EdgeDriver(new EdgeOptions());
            default -> throw new IllegalArgumentException("Invalid browser: " + browser);
        };
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        return driver;
    }

    private ChromeOptions setupChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*");
        return options;
    }

    private WebDriver createRemoteWebDriver(ConfigManager configManager) throws Exception {
        String browser = configManager.getProperty(ConfigKeys.WEB_BROWSER_NAME).toLowerCase();
        var options = switch (browser) {
            case "firefox" -> new FirefoxOptions();
            case "chrome" -> setupChromeOptions();
            case "edge" -> new EdgeOptions();
            case "safari" -> new SafariOptions();
            default -> throw new IllegalArgumentException("Invalid browser: " + browser);
        };
        String hubUrl = configManager.getProperty(ConfigKeys.SELENIUM_HUB_URL);
        if (hubUrl == null || hubUrl.isEmpty()) {
            hubUrl = "http://localhost:4444/wd/hub";
        }
        log.info("Connecting to Selenium Hub at {}", hubUrl);
        WebDriver driver = new RemoteWebDriver(new URL(hubUrl), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        return driver;
    }
}
