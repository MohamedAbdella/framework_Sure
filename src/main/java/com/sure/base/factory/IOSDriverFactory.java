package com.sure.base.factory;

import com.sure.configuration.ConfigKeys;
import com.sure.configuration.ConfigManager;
import com.sure.utilities.AppiumServer;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.net.URL;

@Log4j2
public class IOSDriverFactory implements DriverSetup {

    @Override
    public WebDriver createDriver(ConfigManager configManager) throws Exception {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setDeviceName(configManager.getProperty(ConfigKeys.IOS_DEVICE_NAME))
                .setPlatformVersion(configManager.getProperty(ConfigKeys.IOS_PLATFORM_VERSION))
                .setAutomationName(configManager.getProperty(ConfigKeys.IOS_AUTOMATION_NAME))
                .setBundleId(configManager.getProperty(ConfigKeys.IOS_APP_PACKAGE))
                .setApp(System.getProperty("user.dir")
                        + configManager.getProperty(ConfigKeys.APPS_FOLDER_PATH)
                        + configManager.getProperty(ConfigKeys.IOS_APP_FILE_PATH))
                .setNoReset(true)
                .setAutoAcceptAlerts(true);
        String appiumUrl = AppiumServer.startAppium();
        if (appiumUrl == null) {
            throw new IllegalStateException("Appium is not available");
        }
        log.info("Connecting to Appium at {}", appiumUrl);
        return new IOSDriver(new URL(appiumUrl), options);
    }
}
