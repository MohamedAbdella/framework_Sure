package com.sure.utilities.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Wrapper around common element interactions that includes waiting and
 * fallback JavaScript operations.
 */
public class ElementActions {
    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WaitUtils wait;

    /**
     * Creates an instance using the provided driver and wait utilities.
     */
    public ElementActions(WebDriver driver, WaitUtils wait) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = wait;
    }

    /**
     * Clicks on the element located by the given locator. Falls back to
     * JavaScript click if standard click fails.
     */
    public void click(By locator) {
        try {
            WebElement element = wait.waitForClickable(locator);
            element.click();
        } catch (Exception e) {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Attempts to click the element while handling common interaction issues.
     * This method waits for the element to be clickable and if the standard
     * click fails it falls back to a JavaScript click.
     */
    public void safeClick(By locator) {
        try {
            wait.waitForClickable(locator).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", driver.findElement(locator));
        }
    }

    /**
     * Sends text to the element after waiting for its visibility.
     */
    public void sendKeys(By locator, String text) {
        WebElement element = wait.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Checks whether the element is visible on the page.
     */
    public boolean isDisplayed(By locator) {
        try {
            return wait.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks whether at least one element matching the locator exists in the DOM.
     */
    public boolean isPresent(By locator) {
        try {
            return !driver.findElements(locator).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns whether the element is enabled.
     */
    public boolean isEnabled(By locator) {
        try {
            return wait.waitForVisibility(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the text of the element after waiting for visibility.
     */
    public String getText(By locator) {
        return wait.waitForVisibility(locator).getText();
    }

    /**
     * Scrolls the page until the element is in view.
     */
    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scrolls the element into view using JavaScript.
     */
    public void scrollIntoView(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
