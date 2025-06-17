package com.sure.utilities.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementActions {
    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WaitUtils wait;

    public ElementActions(WebDriver driver, WaitUtils wait) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = wait;
    }

    public void click(By locator) {
        try {
            WebElement element = wait.waitForClickable(locator);
            element.click();
        } catch (Exception e) {
            WebElement element = driver.findElement(locator);
            js.executeScript("arguments[0].click();", element);
        }
    }

    public void sendKeys(By locator, String text) {
        WebElement element = wait.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    public boolean isDisplayed(By locator) {
        try {
            return wait.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        try {
            return wait.waitForVisibility(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public String getText(By locator) {
        return wait.waitForVisibility(locator).getText();
    }

    public void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
