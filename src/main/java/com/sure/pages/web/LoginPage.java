package com.sure.pages.web;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class LoginPage extends PageBase {
    public LoginPage(DriverManager driverManager) {
        super(driverManager);
    }

    By emailTextBox = By.id("email");
    By passwordTextBox = By.id("password-login");
    By loginBtn = By.id("normal-login-btn");
    By verificationBtn = By.id("normal-verification-btn");


    @Step("User Can Enter The Email")
    public LoginPage enterEmail(String email) {
        sendText(emailTextBox, email);
        return this;
    }

    @Step("User Can Enter The Password")
    public LoginPage enterPassword(String password) {
        sendText(passwordTextBox, password);
        return this;
    }

    @Step("User Clicks on Login Button")
    public LoginPage clickOnLoginButton() {
        clickElement(loginBtn);
        return this;
    }

    @Step("User Clicks on verification Button")
    public void clickOnVerificationButton() {
        clickElement(verificationBtn);
    }

    @Step("Check Login Button Appearance")
    public boolean checkLoginButtonAppearance() {
        return checkElementIsDisplayed(loginBtn);
    }

    @Step("Login")
    public MajlesHomePage login(String email, String password) {
        enterEmail(email)
                .clickOnLoginButton()
                .enterPassword(password)
                .clickOnVerificationButton();
        return new MajlesHomePage(driverManager);
    }

}

