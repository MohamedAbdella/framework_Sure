package com.sure.base.factory;

import com.sure.configuration.ConfigManager;
import org.openqa.selenium.WebDriver;

/**
 * Contract for all driver factories used by the framework. Each implementation
 * knows how to create a WebDriver for a specific platform.
 */
public interface DriverSetup {

    /**
     * Creates and returns a fully configured driver.
     *
     * @param configManager provides configuration values required for setup
     * @return newly created driver instance
     */
    WebDriver createDriver(ConfigManager configManager) throws Exception;
}
