package com.sure.utilities;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

/**
 * Simplified logging wrapper that also exposes Allure steps for each log level.
 */
@Log4j2
public class LoggerClass {
    /**
     * Records a test step in the logs and in Allure.
     *
     * @param step textual description of the test step
     */
    @Step("Executing step: {step}")
    public static void logStep(String step) {
        log.info("Step: {}", step); // Log step information
    }

    /**
     * Writes a debug level message.
     */
    @Step("Debug message: {message}")
    public static void logDebug(String message) {
        log.debug(message);
    }

    /**
     * Writes an info level message.
     */
    @Step("Info message: {message}")
    public static void logInfo(String message) {
        log.info(message);
    }

    /**
     * Writes a warning level message.
     */
    @Step("Warning message: {message}")
    public static void logWarn(String message) {
        log.warn(message);
    }

    /**
     * Writes a fatal level message for unrecoverable errors.
     */
    @Step("Fatal error: {fatalMessage}")
    public static void logFatal(String fatalMessage) {
        log.fatal("Fatal: {}", fatalMessage);
    }

    /**
     * Logs an error along with the thrown exception.
     */
    @Step("Error message: {message}")
    public static void logError(String message, Throwable exception) {
        log.error(message, exception);
    }
}
