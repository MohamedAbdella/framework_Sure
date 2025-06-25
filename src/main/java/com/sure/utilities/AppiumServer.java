package com.sure.utilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import com.sure.configuration.ConfigManager;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Utility class that starts and stops a local Appium server instance. It tries
 * to auto-detect the Appium installation path if not explicitly configured.
 */
@Log4j2
public class AppiumServer {
    private AppiumServer() {
    }

    @Getter
    private static AppiumDriverLocalService service;

    /**
     * Starts the Appium server and returns its URL. The method looks for the
     * Appium main script either from configuration, via npm detection, or via
     * common install locations.
     *
     * @return service URL when started or {@code null} on failure
     */
    public static String startAppium() {
        String appiumUrl = "";
        ConfigManager configManager = ConfigManager.getInstance();
        String appiumJSPath = configManager.getProperty("appiumJSPath");

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingAnyFreePort();

        File mainJS = null;

        if (appiumJSPath != null && !appiumJSPath.isBlank()) {
            File candidate = new File(appiumJSPath);
            if (candidate.exists()) {
                mainJS = candidate;
            } else {
                log.warn("Specified appiumJSPath does not exist: {}", candidate.getAbsolutePath());
            }
        }

        if (mainJS == null) {
            // attempt to detect using `npm root -g` when npm is available
            try {
                Process process = new ProcessBuilder("npm", "root", "-g").start();
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String npmRoot = br.readLine();
                process.waitFor();
                if (npmRoot != null) {
                    File candidate = new File(npmRoot, "appium/build/lib/main.js");
                    if (candidate.exists()) {
                        mainJS = candidate;
                        log.info("Detected Appium main script at {}", mainJS.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                log.warn("npm command not found; skipping auto-detection of Appium path");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while detecting Appium path via npm");
            } catch (Exception e) {
                log.warn("Failed to auto-detect Appium main script using npm: {}", e.getMessage());
            }
        }

        if (mainJS == null) {
            // fallback to common installation paths
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                String appData = System.getenv("APPDATA");
                if (appData != null) {
                    File candidate = new File(appData, "npm/node_modules/appium/build/lib/main.js");
                    if (candidate.exists()) {
                        mainJS = candidate;
                    }
                }
            } else {
                File candidate = new File("/usr/local/lib/node_modules/appium/build/lib/main.js");
                if (candidate.exists()) {
                    mainJS = candidate;
                }
            }
        }

        if (mainJS == null || !mainJS.exists()) {
            log.error("Appium main script not found. Skipping Appium startup.");
            log.error("Set 'appiumJSPath' in path.properties if Appium is installed in a custom location.");
            return null;
        }

        builder = builder.withAppiumJS(mainJS);

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        // Ensure the service has started before getting the URL
        if (service.isRunning()) {
            appiumUrl = service.getUrl().toString();
            log.info("Appium service started at: {}", appiumUrl);
            // Now initialize your AndroidDriver or other test logic here
        } else {
            log.error("Appium service failed to start.");
        }
        return appiumUrl;
    }

    /**
     * Stops the Appium service if it is running.
     */
    public static void stopAppium() {
        if (service != null && service.isRunning()) {
            service.stop();
        }
        log.info("Appium server stopped.");
    }
}