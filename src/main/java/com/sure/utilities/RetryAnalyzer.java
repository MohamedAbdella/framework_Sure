package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG retry analyzer that reruns failed tests a limited number of times.
 * <p>
 * The logic is simple: if a test fails and the maximum retry count has not
 * been reached, TestNG will re-execute the test.
 */
@Log4j2
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 3;

    /**
     * Determines whether a failed test should be retried.
     *
     * @param result TestNG result for the executed test
     * @return {@code true} if the test should run again
     */
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            log.warn("Retrying {} ({} of {})", result.getName(), retryCount, MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }


}