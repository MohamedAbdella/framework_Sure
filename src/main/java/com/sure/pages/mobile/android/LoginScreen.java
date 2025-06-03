package com.sure.pages.mobile.android;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
@Log4j2
public class LoginScreen extends PageBase {
    public LoginScreen(DriverManager driverManager) {
        super(driverManager);
    }
    By textBox = By.id("com.sure.majlestech:id/edSlug");
    By nextAndSendButtons = By.id("com.sure.majlestech:id/next");

    @Step("Enter The Email To The TextBox")
    public LoginScreen enterEmail(String email) {
        sendText(textBox, email);
        return this;
    }

    @Step("Click On Next Button")
    public LoginScreen clickOnNextButton() {
        clickElement(nextAndSendButtons);
        return this;
    }

    @Step("Enter The Password To The TextBox")
    public LoginScreen enterPassword(String password) {
        sendText(textBox, password);
        return this;
    }

    @Step("Click On Send Button")
    public void clickOnSendButton() {
        clickElement(nextAndSendButtons);
    }

    @Step("Login to The App")
    public HomeScreen login(String email, String password) {
        enterEmail(email)
                .clickOnNextButton()
                .enterPassword(password)
                .clickOnSendButton();
        return new HomeScreen(driverManager);
    }

}

