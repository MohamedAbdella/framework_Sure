package com.sure.utilities.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Helper around Selenium's {@link WebDriverWait} providing common explicit
 * waits used across page objects.
 */
public class WaitUtils {
    private final WebDriverWait wait;

    /**
     * Creates a new wait utility with the desired timeout.
     */
    public WaitUtils(WebDriver driver, long timeoutSeconds) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Waits until the element located by the locator is visible.
     */
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the element is clickable.
     */
    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to become invisible.
     */
    public void waitForInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until an alert dialog is present.
     */
    public void waitForAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
    }
}
