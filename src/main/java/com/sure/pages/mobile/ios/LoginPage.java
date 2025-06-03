package com.sure.pages.mobile.ios;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class LoginPage extends PageBase {

    By emailTextBox = AppiumBy.iOSClassChain("**/XCUIElementTypeTextField[`value == \"Email or Mobile number\"`]");
    By passwordTextBox = AppiumBy.iOSClassChain("**/XCUIElementTypeSecureTextField[`value == \"Password\"`]");

    public LoginPage(DriverManager driverManager) {
        super(driverManager);
    }

    @Step("Enter Email")
    private LoginPage enterEmail(String email) {
        sendText(emailTextBox, email);
        clickOnElementByName("Done");
        return this;
    }

    @Step("Enter Password")
    private void enterPassword(String password) {
        sendText(passwordTextBox, password);
        clickOnElementByName("Done");
    }


    @Step("Login")
    public HomePage login(String email, String password) {
        enterEmail(email)
                .enterPassword(password);
        return new HomePage(driverManager);
    }

}

