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

    public PageBase(DriverManager driverManager) {
        this.driverManager = driverManager;
        this.driver = driverManager.getDriver();
        this.configManager = ConfigManager.getInstance();
        this.js = (JavascriptExecutor) driver;
        this.waitUtils = new WaitUtils(driver, 10);
        this.actions = new ElementActions(driver, waitUtils);
    }

    @Step("Navigate to Specific Url")
    public void navigateTo(String endpointURL) {
        driver.get(configManager.getProperty(ConfigKeys.BASE_URL) + endpointURL);
    }

    protected void visibilityWaitForElementToBeClickable(By element) {
        waitUtils.waitForClickable(element);
    }

    protected void visibilityWaitForElementLocated(By element) {
        waitUtils.waitForVisibility(element);
    }

    public void waitForElementDisappear(By element) {
        waitUtils.waitForInvisibility(element);
    }

    protected void clearText(By element) {
        waitUtils.waitForVisibility(element).clear();
    }

    protected void sendText(By element, String text) {
        actions.sendKeys(element, text);
    }

    protected void clickOnElementByName(String name) {
        actions.click(By.name(name));
    }

    protected void clickOnElementByText(String textValue) {
        By btn = By.xpath(String.format("//button[contains(text(),'%s')]", textValue));
        if (actions.isDisplayed(btn)) {
            actions.click(btn);
        } else {
            actions.click(By.xpath(String.format("//*[contains(text(),'%s')]", textValue)));
        }
    }

    protected void clickElement(By element) {
        actions.click(element);
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    protected boolean checkElementIsDisplayed(By element) {
        return actions.isDisplayed(element);
    }

    public boolean checkElementIsEnabled(By element) {
        return actions.isEnabled(element);
    }

    protected String getElementText(By element) {
        return actions.getText(element);
    }

    protected void scrollToTheEndOfThePage() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    protected void scrollToDown() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    protected void scrollDownByPixels(int pixels) {
        js.executeScript("window.scrollBy(0, " + pixels + ");");
    }

    public void acceptAlerts() {
        try {
            alertWait();
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            log.info("Alert not displayed :{}", e.getMessage());
        }

    }

    public void alertWait() {
        waitUtils.waitForAlert();
    }

    protected void scrollByVisibleText(String text) {
        if (driver instanceof IOSDriver iosDriver) {
            iosDriver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' && name == '" + text + "'"));
        } else {
            actions.scrollToElement(By.xpath("//*[contains(text(),'" + text + "')]"));
        }

    }

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
