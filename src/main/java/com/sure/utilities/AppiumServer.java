package com.sure.utilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppiumServer {
    private AppiumServer() {
    }

    @Getter
    private static AppiumDriverLocalService service;

    public static String startAppium() {
        String appiumUrl = "";
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingAnyFreePort();

        service = AppiumDriverLocalService.buildService(builder);

        service.start();

        // Ensure the service has started before getting the URL
        if (service.isRunning()) {
            appiumUrl = service.getUrl().toString();
            log.info("Appium service started at: {}", appiumUrl);
            // Now initialize your AndroidDriver or other test logic here
        } else {
            log.info("Appium service failed to start.");
        }
        return appiumUrl;
    }

    public static void stopAppium() {
        if (service != null && service.isRunning()) {
            service.stop();
        }
        log.info("Appium server stopped.");
    }
}