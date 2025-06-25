package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Injects {@link RetryAnalyzer} into TestNG tests via annotation transformation.
 */
@Log4j2
public class RetryListener implements IAnnotationTransformer {
    /**
     * Modifies the TestNG annotation to apply the {@link RetryAnalyzer} to each
     * test method.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {

        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
