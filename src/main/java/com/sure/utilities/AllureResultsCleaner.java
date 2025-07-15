package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.testng.IExecutionListener;

/**
 * TestNG {@link IExecutionListener} that cleans the Allure results directory
 * before the test suite begins. This ensures old results do not pollute
 * new reports.
 */
@Log4j2
public class AllureResultsCleaner implements IExecutionListener {

    private static final String ALLURE_RESULTS_PATH = FilesDirectories.USER_DIR + "/target/allure-results";

    @Override
    public void onExecutionStart() {
        log.info("Cleaning Allure results folder: {}", ALLURE_RESULTS_PATH);
        FilesDirectories.deleteDirectory(ALLURE_RESULTS_PATH);
    }
}
