package com.sure.base.factory;

import com.sure.configuration.ConfigManager;
import org.openqa.selenium.WebDriver;

public interface DriverSetup {
    WebDriver createDriver(ConfigManager configManager) throws Exception;
}
