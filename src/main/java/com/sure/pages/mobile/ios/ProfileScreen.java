package com.sure.pages.mobile.ios;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class ProfileScreen extends PageBase {
    public ProfileScreen(DriverManager driverManager) {
        super(driverManager);
    }

    By welcomeText = AppiumBy.accessibilityId("Welcome Back");
    By profileIcon = AppiumBy.iOSClassChain("**/XCUIElementTypeTabBar[`name == \"Tab Bar\"`]/XCUIElementTypeButton[5]");
    By logoutButton = By.xpath("//XCUIElementTypeOther[5]//XCUIElementTypeButton");
    By yesButton = By.xpath("//XCUIElementTypeImage[@name=\"fullLogo\"]");

    //
    @Step("Click On Logout Button")
    public void clickOnLogoutButton() {
        clickElement(logoutButton);
        clickElement(yesButton);
    }

    @Step("Check The User is Logged out From The App and Home Screen appears again")
    public boolean checkUserIsLogout(String welcomeTextValue) {
        return getElementText(welcomeText).contains(welcomeTextValue);
    }

    @Step("Click On Profile Icon")
    public ProfileScreen clickOnProfileIcon() {
        clickElement(profileIcon);
        return this;
    }

    @Step("Logout From The App")
    public ProfileScreen logout() {
        clickOnProfileIcon()
                .clickOnLogoutButton();
        return this;
    }

}