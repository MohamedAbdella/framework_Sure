package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * Suite listener that cleans the Allure results directory before tests run.
 */
@Log4j2
public class AllureResultsCleaner implements ISuiteListener {
    private static final String ALLURE_RESULTS_PATH = FilesDirectories.USER_DIR + "/target/allure-results";

    @Override
    public void onStart(ISuite suite) {
        log.info("Cleaning Allure results folder: {}", ALLURE_RESULTS_PATH);
        FilesDirectories.deleteDirectory(ALLURE_RESULTS_PATH);
    }
}