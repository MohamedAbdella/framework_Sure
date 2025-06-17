package com.sure.base.factory;

import com.sure.configuration.ConfigKeys;
import com.sure.configuration.ConfigManager;
import com.sure.utilities.AppiumServer;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URL;

@Log4j2
public class AndroidDriverFactory implements DriverSetup {

    @Override
    public WebDriver createDriver(ConfigManager configManager) throws Exception {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setDeviceName(configManager.getProperty(ConfigKeys.ANDROID_DEVICE_NAME))
                .setPlatformVersion(configManager.getProperty(ConfigKeys.ANDROID_PLATFORM_VERSION))
                .setAutomationName(configManager.getProperty(ConfigKeys.ANDROID_AUTOMATION_NAME))
                .setAppPackage(configManager.getProperty(ConfigKeys.ANDROID_APP_PACKAGE))
                .setAppWaitActivity(configManager.getProperty(ConfigKeys.ANDROID_APP_WAIT_ACTIVITY))
                .setApp(System.getProperty("user.dir")
                        + configManager.getProperty(ConfigKeys.APPS_FOLDER_PATH)
                        + configManager.getProperty(ConfigKeys.ANDROID_APP_FILE_PATH))
                .setNoReset(false)
                .setAutoGrantPermissions(true);
        options.setCapability("autoAcceptAlerts",
                configManager.getBooleanProperty(ConfigKeys.AUTO_ACCEPT_ALERTS));
        options.setCapability("androidEmulatorId",
                configManager.getProperty(ConfigKeys.ANDROID_EMULATOR_ID));
        options.setCapability("hideKeyboard", true);

        String appiumUrl = AppiumServer.startAppium();
        if (appiumUrl == null) {
            throw new IllegalStateException("Appium is not available");
        }
        log.info("Connecting to Appium at {}", appiumUrl);
        return new AndroidDriver(new URL(appiumUrl), options);
    }
}
