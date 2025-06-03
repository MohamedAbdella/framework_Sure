package com.sure.pages.mobile.android;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class ProfileScreen extends PageBase {
    public ProfileScreen(DriverManager driverManager) {
        super(driverManager);
    }

    By logoutButton = By.id("com.sure.majlestech:id/logoutContainer");
    By yesButton = By.id("com.sure.majlestech:id/yes");
    By welcomeText = By.xpath("//android.widget.TextView[@text ='Welcome back']");
    By profileIcon = By.id("com.sure.majlestech:id/ProfileFragment");

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