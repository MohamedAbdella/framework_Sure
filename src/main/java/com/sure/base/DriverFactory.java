package com.sure.base;

import com.sure.base.factory.AndroidDriverFactory;
import com.sure.base.factory.DriverSetup;
import com.sure.base.factory.IOSDriverFactory;
import com.sure.base.factory.WebDriverFactory;
import com.sure.configuration.ConfigKeys;
import com.sure.configuration.ConfigManager;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public final class DriverFactory {
    private DriverFactory() {}

    private static final Map<String, DriverSetup> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("web", new WebDriverFactory());
        FACTORIES.put("android", new AndroidDriverFactory());
        FACTORIES.put("ios", new IOSDriverFactory());
    }

    public static WebDriver createDriver(ConfigManager configManager) throws Exception {
        String platform = configManager.getProperty(ConfigKeys.PLATFORM_TYPE).toLowerCase();
        DriverSetup setup = FACTORIES.get(platform);
        if (setup == null) {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        return setup.createDriver(configManager);
    }
}
