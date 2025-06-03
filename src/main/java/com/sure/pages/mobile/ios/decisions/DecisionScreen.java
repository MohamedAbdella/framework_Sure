package com.sure.pages.mobile.ios.decisions;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class DecisionScreen extends PageBase {
    public DecisionScreen(DriverManager driverManager) {
        super(driverManager);
    }

    By decisionScreenTitle = AppiumBy.accessibilityId("Decisions");
    By resolutionTab = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Resolutions \"`]");
    By decisionSection = By.id("Descion Number:");


    @Step("Click On Resolution Tab")
    public DecisionScreen clickOnResolutionTab() {
        clickElement(resolutionTab);
        return this;
    }

    @Step("Get The Screen Title")
    public String getScreenTitle() {
        return getElementText(decisionScreenTitle);
    }

    @Step("Click On Decision Section To Vote")
    public DecisionVotingPopUp clickOnDecisionSectionToVote() {
        clickElement(decisionSection);
        return new DecisionVotingPopUp(driverManager);
    }

}