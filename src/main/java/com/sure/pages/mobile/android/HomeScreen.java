package com.sure.pages.mobile.android;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import com.sure.pages.mobile.android.decisions.DecisionScreen;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class HomeScreen extends PageBase {
    public HomeScreen(DriverManager driverManager) {
        super(driverManager);
    }

    By titleName = By.id("com.sure.majlestech:id/profileImage");
    By avatarImage = By.id("com.sure.majlestech:id/logo");
    By menuButton = By.id("com.sure.majlestech:id/fab");
    By decisionButton = By.id("com.sure.majlestech:id/decisionsLy");

    @Step("Check The User is Logged in To The App and Home Screen appears")
    public Boolean checkTheHomeScreenIsOpened() {
        return checkElementIsDisplayed(avatarImage)
                && checkElementIsDisplayed(titleName);
    }

    @Step("Click On Menu Button")
    public HomeScreen clickOnMenuButton() {
        clickElement(menuButton);
        return this;
    }

    @Step("Click On Decision Button")
    public DecisionScreen clickOnDecisionButton() {
        clickElement(decisionButton);
        return new DecisionScreen(driverManager);
    }

}
