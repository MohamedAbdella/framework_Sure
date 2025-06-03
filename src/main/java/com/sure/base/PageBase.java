package com.sure.base;

import com.sure.configuration.ConfigManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.sure.base.DriverManager.*;

@Log4j2
public class PageBase {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected ConfigManager configManager;
    protected DriverManager driverManager;

    public PageBase(DriverManager driverManager) {
        this.driverManager = driverManager;
        this.driver = driverManager.getDriver();
        this.configManager = new ConfigManager();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    @Step("Navigate to Specific Url")
    public void navigateTo(String endpointURL) {
        driver.get(configManager.getProperty("baseURL") + endpointURL);
    }

    @Step("Wait For Element To Be Clickable And Visible")
    protected void visibilityWaitForElementToBeClickable(By element) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Step("Wait For Frame To Be Visible")
    protected void visibilityWaitForFrameToBeAvailable(By iframeLocator, By elementLocator) {
        try {
            // Wait for iframe to be available and switch to it
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeLocator));
            // Wait for the DOM to be fully loaded
            wait.until((ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';")));
            // Wait for a specific element inside the iframe to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
            log.info("Element located inside the iframe is visible.");
        } catch (TimeoutException e) {
            log.error("Frame did not appear within the timeout period: " + e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred while waiting for frame to appear: " + e.getMessage());
        }
    }

    @Step("Wait For Element To Be Located")
    protected void visibilityWaitForElementLocated(By element) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
    }

    @Step("Wait For Element To Be Not Shown")
    public void waitForElementDisappear(By element) {
        try {
            // Wait until the element becomes invisible
            wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
        } catch (TimeoutException e) {
            log.error("Element did not disappear within the timeout period: " + e.getMessage());
        } catch (Exception e) {
            log.error("An error occurred while waiting for element to disappear: " + e.getMessage());
        }
    }

    @Step("Clear The Text")
    protected void clearText(By element) {
        visibilityWaitForElementLocated(element);
        driver.findElement(element).clear();
    }

    @Step("Send The Text")
    protected void sendText(By element, String text) {
        visibilityWaitForElementToBeClickable(element);
        clearText(element);
        driver.findElement(element).sendKeys(text);
    }

    @Step("Click on Element By Name")
    protected void clickOnElementByName(String name) {
        driver.findElement(By.name(name)).click();
    }

    @Step("Click on Element By Text")
    protected void clickOnElementByText(String textValue) {
        // Define XPaths for general text and specific button text
        String xpathForText = String.format("//*[contains(text(),'%s')]", textValue);
        String xpathForButton = String.format("//button[contains(text(),'%s')]", textValue);
        try {
            visibilityWaitForElementToBeClickable(By.xpath(xpathForButton));
            // Try to find and click the element with the general XPath
            WebElement element = driver.findElement(By.xpath(xpathForButton));
            element.click();
            log.info("Clicked on the button with text: '{}'", textValue);
        } catch (NoSuchElementException e) {
            try {
                visibilityWaitForElementToBeClickable(By.xpath(xpathForButton));
                // If not found, try to find and click the element with the general text XPath
                WebElement element = driver.findElement(By.xpath(xpathForText));
                element.click();
                log.info("Clicked on the element with text: '{}'", textValue);
            } catch (NoSuchElementException ex) {
                log.error("Element with text '{}' not found.", textValue);
                throw new RuntimeException("Element with text '" + textValue + "' not found.");
            }
        }
    }

    @Step("Click On Element")
    protected void clickElement(By element) {
        try {
            // Wait for the element to be located and clickable.
            visibilityWaitForElementToBeClickable(element);
        } catch (TimeoutException e) {
            log.info("Element not clickable within the timeout period: " + e.getMessage());
            return;
        } catch (Exception e) {
            log.info("Unexpected error while waiting for element to be clickable: " + e.getMessage());
            return;
        }

        // Attempt to click using Selenium.
        try {
            WebElement webElement = driver.findElement(element);
            webElement.click();
        } catch (Exception e) {
            // Use JavaScript to click if Selenium click fails.
            try {
                WebElement webElement = driver.findElement(element);
                js.executeScript("arguments[0].click();", webElement);
            } catch (Exception jsException) {
                log.info("JavaScript click also failed: " + jsException.getMessage());
            }
        }
    }

    @Step("Get The Title Of The Current Web Page ")
    protected String getPageTitle() {
        return driver.getTitle();
    }

    @Step("Check Element Is Displayed")
    protected boolean checkElementIsDisplayed(By element) {
        try {
            visibilityWaitForElementLocated(element);
            return driver.findElement(element).isDisplayed();
        } catch (Exception e) {
            log.info("Couldn't find element: " + element + ". Exception: " + e.getMessage());
            return false;
        }
    }

    //&& !(driver.findElement(element).getCssValue("display").equalsIgnoreCase("none")
    @Step("Check Element Is Enabled")
    public boolean checkElementIsEnabled(By element) {
        try {
            visibilityWaitForElementLocated(element);
            return driver.findElement(element).isEnabled();
        } catch (Exception e) {
            log.info("Couldn't find or interact with element: " + element + e.getMessage());
            return false;
        }
    }

    @Step("Get Element Text")
    protected String getElementText(By element) {
        try {
            visibilityWaitForElementLocated(element);
            return driver.findElement(element).getText();
        } catch (Exception e) {
            log.info("Couldn't find or retrieve text from element: " + element + e.getMessage());
            return "";
        }
    }

    @Step("Scroll To The End Of The Page")
    protected void scrollToTheEndOfThePage() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    @Step("Scroll To The Top Of The Page")
    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    @Step("Scroll To The Bottom Of The Page")
    protected void scrollToDown() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    @Step("Scroll Down By Pixels")
    protected void scrollDownByPixels(int pixels) {
        js.executeScript("window.scrollBy(0, " + pixels + ");");
    }

    //Mobile
    @Step("Accept All Alerts")
    public void acceptAlerts() {
        try {
            alertWait();
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            log.info("Alert doesn't displayed :{}", e.getMessage());
        }

    }

    @Step("Wait For Alert To Be Present")
    public void alertWait() {
        wait.until(ExpectedConditions.alertIsPresent());
    }

    // common
    @Step("Scroll By Visible Text Based on Platform Type")
    protected void scrollByVisibleText(String text) {
        if (driver instanceof IOSDriver iosDriver) {
            // iOS-specific scrolling
            iosDriver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' && name == '" + text + "'"));
        } else {
            // Web-specific scrolling
            WebElement element = driver.findElement(By.xpath("//*[contains(text(),'" + text + "')]"));
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        }

    }

    @Step("Scroll to element with text '{text}' on platform '{platformType}'")
    public void scrollToElement(String text) {
        String platformType = configManager.getProperty("platformType");
        if (platformType.equalsIgnoreCase(PLATFORM_ANDROID) && (driver instanceof AndroidDriver androidDriver)) {
            String uiSelector = "new UiScrollable(new UiSelector().scrollable(true)).scrollForward().scrollIntoView(new UiSelector().text(\"" + text + "\"))";
            androidDriver.findElement(AppiumBy.androidUIAutomator(uiSelector));
            log.info("Scrolled down to element with text '{}'", text);
        } else if (platformType.equalsIgnoreCase(PLATFORM_IOS) && driver instanceof IOSDriver iosDriver) {
            String iosPredicate = "type == 'XCUIElementTypeStaticText' && name == '" + text + "'";
            iosDriver.findElement(AppiumBy.iOSNsPredicateString(iosPredicate));
            log.info("Scrolled to element with text '{}' on iOS.", text);

        } else if (platformType.equalsIgnoreCase(PLATFORM_WEB)) {
            WebElement element = driver.findElement(By.xpath("//*[contains(text(),'" + text + "')"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            log.info("Scrolled to element with text '{}' on Web.", text);
        } else {
            log.warn("Unsupported platform type: {}", platformType);
        }

    }

    @Step("Navigate Back")
    public PageBase goBack(int iterations, By element) {
        for (int i = 1; i <= iterations; i++) {
            try {
                // Platform-specific back navigation
                if (driver instanceof AndroidDriver androidDriver) {
                    // Android-specific back navigation
                    androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
                } else if (driver instanceof IOSDriver iosDriver) {
                    iosDriver.findElement(element).click();
                } else {
                    // Web-specific back navigation
                    driver.navigate().back();
                }
            } catch (Exception e) {
                log.error("Error while navigating back: " + e.getMessage());
                break; // Exit the loop if an error occurs
            }
        }
        return this;
    }

    @Step("Wait For Page To Load")
    protected void waitForPageToLoad(By element) {
        try {
            if (driver instanceof AndroidDriver || driver instanceof IOSDriver) {
                // For mobile, you might need to wait for specific elements or states
                visibilityWaitForElementLocated(element);
            } else {
                // For web, wait until the page's readyState is complete
                wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
            }
        } catch (TimeoutException timeoutException) {
            log.error("Timeout waiting for page to load: {}", timeoutException.getMessage());
        } catch (Exception e) {
            log.error("An error occurred while waiting for the page to load: " + e.getMessage());
        }
    }
}