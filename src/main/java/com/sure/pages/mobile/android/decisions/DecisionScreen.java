package com.sure.pages.mobile.android.decisions;

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

    By decisionScreenTitle = By.id("com.sure.majlestech:id/textView14");
    By resolutionTab = AppiumBy.accessibilityId("Resolutions");
    By voteIcon = By.id("com.sure.majlestech:id/vote");


    @Step("Click On Resolution Tab")
    public DecisionScreen clickOnResolutionTab() {
        clickElement(resolutionTab);
        return this;
    }

    @Step("Get The Screen Title")
    public String getScreenTitle() {
        return getElementText(decisionScreenTitle);
    }

    @Step("Click On Vote Icon")
    public DecisionVotingScreen clickOnVoteIcon() {
        clickElement(voteIcon);
        return new DecisionVotingScreen(driverManager);
    }

}