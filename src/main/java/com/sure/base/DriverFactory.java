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

/**
 * Central factory that selects the correct {@link DriverSetup} implementation
 * based on the platform type configured in the {@link ConfigManager}. It
 * exposes a single method for creating a new {@link WebDriver} instance.
 */
public final class DriverFactory {

    /** Utility class - prevent instantiation. */
    private DriverFactory() {}

    private static final Map<String, DriverSetup> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("web", new WebDriverFactory());
        FACTORIES.put("android", new AndroidDriverFactory());
        FACTORIES.put("ios", new IOSDriverFactory());
    }

    /**
     * Creates a new {@link WebDriver} based on the configured platform.
     * <p>
     * The method reads the {@code platformType} property and retrieves the
     * matching {@link DriverSetup} from the internal map. The chosen factory
     * then builds the driver instance.
     *
     * @param configManager provides access to framework configuration values
     * @return newly created driver ready for use in tests
     * @throws Exception if the platform is unsupported or driver initialization fails
     */
    public static WebDriver createDriver(ConfigManager configManager) throws Exception {
        String platform = configManager.getProperty(ConfigKeys.PLATFORM_TYPE).toLowerCase();
        DriverSetup setup = FACTORIES.get(platform);
        if (setup == null) {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        return setup.createDriver(configManager);
    }
}
