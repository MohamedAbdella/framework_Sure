package com.sure.base;

import com.sure.configuration.ConfigKeys;
import com.sure.configuration.ConfigManager;
import com.sure.utilities.helpers.ElementActions;
import com.sure.utilities.helpers.WaitUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import static com.sure.base.DriverManager.*;

@Log4j2
public class PageBase {

    protected WebDriver driver;
    protected JavascriptExecutor js;
    protected ConfigManager configManager;
    protected DriverManager driverManager;
    protected WaitUtils waitUtils;
    protected ElementActions actions;

    /**
     * Base constructor used by all page objects.
     * <p>
     * It pulls the {@link WebDriver} from the provided {@link DriverManager}
     * and initializes helper utilities such as {@link WaitUtils} and
     * {@link ElementActions}. These utilities are then used across all derived
     * pages for common element interactions.
     *
     * @param driverManager provides access to the current driver and configuration
     */
    public PageBase(DriverManager driverManager) {
        this.driverManager = driverManager;
        this.driver = driverManager.getDriver();
        this.configManager = ConfigManager.getInstance();
        this.js = (JavascriptExecutor) driver;
        this.waitUtils = new WaitUtils(driver, 10);
        this.actions = new ElementActions(driver, waitUtils);
    }

    /**
     * Navigates the browser to a URL composed of the base URL and the given endpoint.
     *
     * @param endpointURL the path that will be appended to the base URL
     */
    @Step("Navigate to Specific Url")
    public void navigateTo(String endpointURL) {
        driver.get(configManager.getProperty(ConfigKeys.BASE_URL) + endpointURL);
    }

    /**
     * Waits until the provided element is clickable.
     *
     * @param element locator of the element to wait for
     */
    protected void visibilityWaitForElementToBeClickable(By element) {
        waitUtils.waitForClickable(element);
    }

    /**
     * Waits for the visibility of the given element.
     *
     * @param element locator to wait for
     */
    protected void visibilityWaitForElementLocated(By element) {
        waitUtils.waitForVisibility(element);
    }

    /**
     * Waits until the element is visible on the page.
     */
    protected void waitUntilVisible(By element) {
        waitUtils.waitForVisibility(element);
    }

    /**
     * Waits until the specified element disappears from the DOM.
     *
     * @param element locator that should become invisible
     */
    public void waitForElementDisappear(By element) {
        waitUtils.waitForInvisibility(element);
    }

    /**
     * Clears any existing text in the targeted element.
     *
     * @param element locator of the input field
     */
    protected void clearText(By element) {
        waitUtils.waitForVisibility(element).clear();
    }

    /**
     * Sends the provided text to the element.
     *
     * @param element locator of the input field
     * @param text    value to be typed
     */
    protected void sendText(By element, String text) {
        actions.sendKeys(element, text);
    }

    /**
     * Clicks on an element identified by its HTML {@code name} attribute.
     *
     * @param name attribute value used to locate the element
     */
    protected void clickOnElementByName(String name) {
        actions.click(By.name(name));
    }

    /**
     * Clicks on an element that displays the provided text.
     * <p>
     * The method first tries to locate a button containing the text; if not found
     * it attempts a generic search for any element with the text.
     *
     * @param textValue visible text of the element to click
     */
    protected void clickOnElementByText(String textValue) {
        By btn = By.xpath(String.format("//button[contains(text(),'%s')]", textValue));
        if (actions.isDisplayed(btn)) {
            actions.click(btn);
        } else {
            actions.click(By.xpath(String.format("//*[contains(text(),'%s')]", textValue)));
        }
    }

    /**
     * Clicks on the element represented by the locator.
     *
     * @param element locator of the element to click
     */
    protected void clickElement(By element) {
        actions.click(element);
    }

    /**
     * Performs a safe click on the element handling common Selenium issues.
     */
    protected void safeClick(By element) {
        actions.safeClick(element);
    }

    /**
     * Retrieves the current browser page title.
     *
     * @return page title string
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Checks whether the specified element is visible on the page.
     *
     * @param element locator of the element
     * @return {@code true} if displayed
     */
    protected boolean checkElementIsDisplayed(By element) {
        return actions.isDisplayed(element);
    }

    /**
     * Checks if the element exists in the DOM regardless of visibility.
     */
    protected boolean isElementPresent(By element) {
        return actions.isPresent(element);
    }

    /**
     * Determines if the given element is enabled for interaction.
     *
     * @param element locator of the element
     * @return {@code true} when the element is enabled
     */
    public boolean checkElementIsEnabled(By element) {
        return actions.isEnabled(element);
    }

    /**
     * Returns the visible text from the element.
     *
     * @param element locator of the element
     * @return extracted text content
     */
    protected String getElementText(By element) {
        return actions.getText(element);
    }

    /**
     * Scrolls until the provided element is within the viewport.
     */
    protected void scrollIntoView(By element) {
        actions.scrollIntoView(element);
    }

    /** Scrolls the web page down to its bottom. */
    protected void scrollToTheEndOfThePage() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    /** Scrolls the web page to the very top. */
    protected void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    /** Scrolls the page to the bottom using JavaScript. */
    protected void scrollToDown() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scrolls the page down by a specific number of pixels.
     *
     * @param pixels the vertical distance to scroll
     */
    protected void scrollDownByPixels(int pixels) {
        js.executeScript("window.scrollBy(0, " + pixels + ");");
    }

    /**
     * Attempts to accept any open alert dialogs. If no alert is present the
     * method logs the absence but does not fail the test.
     */
    public void acceptAlerts() {
        try {
            alertWait();
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            log.info("Alert not displayed :{}", e.getMessage());
        }

    }

    /** Waits for an alert dialog to become present. */
    public void alertWait() {
        waitUtils.waitForAlert();
    }

    /**
     * Scrolls the mobile or web page until an element containing the given text is visible.
     *
     * @param text visible text to look for
     */
    protected void scrollByVisibleText(String text) {
        if (driver instanceof IOSDriver iosDriver) {
            iosDriver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' && name == '" + text + "'"));
        } else {
            actions.scrollToElement(By.xpath("//*[contains(text(),'" + text + "')]"));
        }

    }

    /**
     * Scrolls until an element with the given text becomes visible, handling web
     * and mobile platforms appropriately.
     *
     * @param text target text of the element to reveal
     */
    public void scrollToElement(String text) {
        String platformType = configManager.getProperty(ConfigKeys.PLATFORM_TYPE);
        if (platformType.equalsIgnoreCase(PLATFORM_ANDROID) && (driver instanceof AndroidDriver androidDriver)) {
            String uiSelector = "new UiScrollable(new UiSelector().scrollable(true)).scrollForward().scrollIntoView(new UiSelector().text(\"" + text + "\"))";
            androidDriver.findElement(AppiumBy.androidUIAutomator(uiSelector));
        } else if (platformType.equalsIgnoreCase(PLATFORM_IOS) && driver instanceof IOSDriver iosDriver) {
            String iosPredicate = "type == 'XCUIElementTypeStaticText' && name == '" + text + "'";
            iosDriver.findElement(AppiumBy.iOSNsPredicateString(iosPredicate));
        } else if (platformType.equalsIgnoreCase(PLATFORM_WEB)) {
            actions.scrollToElement(By.xpath("//*[contains(text(),'" + text + "')]"));
        }
    }

    /**
     * Navigates back a specified number of times depending on the platform.
     *
     * @param iterations number of back actions to perform
     * @param element    locator used for iOS navigation
     * @return current {@code PageBase} instance for chaining
     */
    public PageBase goBack(int iterations, By element) {
        for (int i = 1; i <= iterations; i++) {
            try {
                if (driver instanceof AndroidDriver androidDriver) {
                    androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
                } else if (driver instanceof IOSDriver iosDriver) {
                    iosDriver.findElement(element).click();
                } else {
                    driver.navigate().back();
                }
            } catch (Exception e) {
                log.error("Error while navigating back: " + e.getMessage());
                break;
            }
        }
        return this;
    }

    /**
     * Waits for the page or specified element to be ready for interaction.
     *
     * @param element locator used on mobile platforms to ensure the page is loaded
     */
    protected void waitForPageToLoad(By element) {
        try {
            if (driver instanceof AndroidDriver || driver instanceof IOSDriver) {
                waitUtils.waitForVisibility(element);
            } else {
                js.executeScript("return document.readyState");
            }
        } catch (Exception e) {
            log.error("An error occurred while waiting for the page to load: " + e.getMessage());
        }
    }
}
