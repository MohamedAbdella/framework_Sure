package com.sure.pages.mobile.ios.decisions;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

@Log4j2
public class VoteReportScreen extends PageBase {
    public VoteReportScreen(DriverManager driverManager) {
        super(driverManager);
    }

    By signHereButton = AppiumBy.accessibilityId("Sign here");
    By imageIcon = AppiumBy.accessibilityId("Insert");
    By insertImage = By.xpath("//XCUIElementTypeOther[3]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeOther//XCUIElementTypeImage[1]");
    By storeSignatureRadioButton = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Store Signature\"`]");
    By sendButton = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Send \"`]");
    By backButton = By.xpath("//XCUIElementTypeButton[@name=\"BackEn\"]");

    @Step("Check Signature Screen Is Opened After Clicking on Send Button")
    public boolean checkSignatureScreenIsOpened() {
        return checkElementIsDisplayed(signHereButton);
    }

    @Step("Click On Sign Here Icon")
    public VoteReportScreen clickOnSignHereButton(){
        waitForPageToLoad(signHereButton);
        clickElement(signHereButton);
        return this;
    }

    @Step("Add New Signature")
    public VoteReportScreen addSignature() {
        try {
            if (checkElementIsDisplayed(storeSignatureRadioButton)) {
                String value = driver.findElement(storeSignatureRadioButton).getAttribute("value");
                if (value.equals("1")) {
                    clickElement(storeSignatureRadioButton);
                }
            }
        } catch (Exception e) {
            log.info("the radio button is checked By default: {}", e.getMessage());
        }
        clickElement(imageIcon);
        clickElement(insertImage);
        clickOnElementByName("Done");
        return this;
    }

    @Step("Click On Send Button")
    public DecisionScreen clickOnSendButton() {
        while (true) {
            try {
                // Check if the send button is displayed
                if (checkElementIsDisplayed(sendButton)) {
                    // Click the send button
                    clickElement(sendButton);
                } else {
                    // Break the loop if the send button is no longer displayed
                    break;
                }
            } catch (NoSuchElementException e) {
                // Handle the case where the element is not found
                log.error("Send button element not found: {}", e.getMessage());
            }
        }
        goBack(1, backButton);
        return new DecisionScreen(driverManager);
    }
}