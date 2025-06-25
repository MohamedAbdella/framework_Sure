package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

/**
 * Listener implementation for hooking into various Selenium WebDriver events.
 * <p>
 * The methods are intentionally left empty and can be extended to add custom
 * logging or debugging output when running web tests.
 */
@Log4j2
public class SeleniumEventReporter implements WebDriverListener {

    /** Invoked before accepting an alert. */
    @Override
    public void beforeAccept(Alert alert) {
        // Custom logic before accepting an alert
    }

    /** Invoked after accepting an alert. */
    @Override
    public void afterAccept(Alert alert) {
        // Custom logic after accepting an alert
    }

    /** Invoked before dismissing an alert. */
    @Override
    public void beforeDismiss(Alert alert) {
        // Custom logic before dismissing an alert
    }

    /** Invoked after dismissing an alert. */
    @Override
    public void afterDismiss(Alert alert) {
        // Custom logic after dismissing an alert
    }

    /** Invoked before navigating to the specified URL. */
    @Override
    public void beforeTo(WebDriver.Navigation navigation, String url) {
        // Custom logic before navigating to a URL
    }

    /** Invoked after navigating to the specified URL. */
    @Override
    public void afterTo(WebDriver.Navigation navigation, String url) {
        // Custom logic after navigating to a URL
    }

    /** Invoked before navigating back in browser history. */
    @Override
    public void beforeBack(WebDriver.Navigation navigation) {
        // Custom logic before navigating back
    }

    /** Invoked after navigating back in browser history. */
    @Override
    public void afterBack(WebDriver.Navigation navigation) {
        // Custom logic after navigating back
    }

    /** Invoked before navigating forward in browser history. */
    @Override
    public void beforeForward(WebDriver.Navigation navigation) {
        // Custom logic before navigating forward
    }

    /** Invoked after navigating forward in browser history. */
    @Override
    public void afterForward(WebDriver.Navigation navigation) {
        // Custom logic after navigating forward
    }

    /** Invoked before refreshing the current page. */
    @Override
    public void beforeRefresh(WebDriver.Navigation navigation) {
        // Custom logic before refreshing the page
    }

    /** Invoked after refreshing the current page. */
    @Override
    public void afterRefresh(WebDriver.Navigation navigation) {
        // Custom logic after refreshing the page
    }

    /** Invoked before locating an element. */
    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        // Custom logic before finding an element
    }

    /** Invoked after locating an element. */
    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        // Custom logic after finding an element
    }

    /** Invoked before clicking an element. */
    @Override
    public void beforeClick(WebElement element) {
        // Custom logic before clicking an element
    }

    /** Invoked after clicking an element. */
    @Override
    public void afterClick(WebElement element) {
        // Custom logic after clicking an element
    }

    /** Invoked before sending keys to an element. */
    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        // Custom logic before changing the value of an element
    }

    /** Invoked after sending keys to an element. */
    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        // Custom logic after changing the value of an element
    }

    /** Invoked before executing JavaScript via WebDriver. */
    @Override
    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        // Custom logic before executing a script
    }

    /** Invoked after executing JavaScript via WebDriver. */
    @Override
    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        // Custom logic after executing a script
    }

    /** Invoked before retrieving text from an element. */
    @Override
    public void beforeGetText(WebElement element) {
        // Custom logic before getting text from an element
    }

    /** Invoked after retrieving text from an element. */
    @Override
    public void afterGetText(WebElement element, String result) {
        // Custom logic after getting text from an element
    }
}
