package com.sure.pages.mobile.ios;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class MyInformationScreen extends PageBase {
    public MyInformationScreen(DriverManager driverManager) {
        super(driverManager);
    }


    By logoutButton = By.id("com.sure.majlestech:id/logoutContainer");
    By yesButton = By.id("com.sure.majlestech:id/yes");
    By welcomeText = By.xpath("//android.widget.TextView[@text ='Welcome back']");
    By profileIcon = By.id("com.sure.majlestech:id/ProfileFragment");
    By personalInfoButton = By.id("com.sure.majlestech:id/personalInfoContainer");
    By rightIcon = By.id("com.sure.majlestech:id/iv_icon");
    By confirmButton = By.id("com.sure.majlestech:id/confirm");
    By toastMessage = By.id("com.sure.majlestech:id/tv_message");


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
    public MyInformationScreen clickOnProfileIcon() {
        clickElement(profileIcon);
        return this;
    }


    @Step("Click On Personal Info Button")
    public MyInformationScreen clickOnPersonalInfoButton() {
        clickElement(personalInfoButton);
        return this;
    }

    @Step("Logout From The App")
    public MyInformationScreen logout() {
        clickOnProfileIcon()
                .clickOnLogoutButton();
        return this;
    }

    @Step("Click On Confirm Button")
    public MyInformationScreen clickOnConfirmButton() {
        clickElement(confirmButton);
        return this;
    }

    @Step("Get Toast Message")
    public boolean getToastMessage(String text) {
        return getElementText(toastMessage).contains(text);
    }

    @Step("Check Password Is Updated Successfully")
    public boolean checkPasswordIsUpdatedSuccessfully(String text) {
        return checkElementIsDisplayed(rightIcon) && getToastMessage(text);
    }

    @Step("Check Success Message Appears")
    public boolean checkSuccessMessageAppears(String text) {
        return checkElementIsDisplayed(rightIcon) && getToastMessage(text);
    }
}