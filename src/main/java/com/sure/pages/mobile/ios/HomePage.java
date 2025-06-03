package com.sure.pages.mobile.ios;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import com.sure.pages.mobile.ios.decisions.DecisionScreen;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class HomePage extends PageBase {
    public HomePage(DriverManager driverManager) {
        super(driverManager);
    }

    By avatarImage = AppiumBy.accessibilityId("avatar");
    By menuButton = AppiumBy.iOSClassChain("**/XCUIElementTypeTabBar[`name == \"Tab Bar\"`]/XCUIElementTypeButton[3]");
    By decisionButton = By.xpath("//XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther[1]//XCUIElementTypeOther//XCUIElementTypeOther[3]//XCUIElementTypeButton");


    @Step("Check The User is Logged in To The App and Home Screen appears")
    public Boolean checkTheHomeScreenIsOpened() {
        return checkElementIsDisplayed(avatarImage);
    }

    @Step("Click On Menu Button")
    public HomePage clickOnMenuButton() {
        clickElement(menuButton);
        return this;
    }

    @Step("Click On Decision Button")
    public DecisionScreen clickOnDecisionButton() {
        clickElement(decisionButton);
        return new DecisionScreen(driverManager);
    }

}
