package com.sure.utilities;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggerClass {
    @Step("Executing step: {step}")
    public static void logStep(String step) {
        log.info("Step: {}", step); // Log step information
    }

    @Step("Debug message: {message}")
    public static void logDebug(String message) {
        log.debug(message);
    }

    @Step("Info message: {message}")
    public static void logInfo(String message) {
        log.info(message);
    }

    @Step("Warning message: {message}")
    public static void logWarn(String message) {
        log.warn(message);
    }

    @Step("Fatal error: {fatalMessage}")
    public static void logFatal(String fatalMessage) {
        log.fatal("Fatal: {}", fatalMessage);
    }

    @Step("Error message: {message}")
    public static void logError(String message, Throwable exception) {
        log.error(message, exception);
    }
}
