package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.testng.IExecutionListener;

/**
 * Execution listener that cleans the Allure results directory before tests run.
 */
@Log4j2
public class AllureResultsCleaner implements IExecutionListener {
    private static final String DEFAULT_RESULTS_PATH = FilesDirectories.USER_DIR + "/target/allure-results";

    @Override
    public void onExecutionStart() {
        String resultsDir = System.getProperty("allure.results.directory", DEFAULT_RESULTS_PATH);
        log.info("Cleaning Allure results folder: {}", resultsDir);
        FilesDirectories.deleteDirectory(resultsDir);
    }
}

