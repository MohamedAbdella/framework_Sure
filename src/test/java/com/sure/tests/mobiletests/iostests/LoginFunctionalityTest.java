package com.sure.tests.mobiletests.iostests;

import com.sure.base.TestBase;
import com.sure.pages.mobile.ios.LoginPage;
import com.sure.pages.mobile.ios.ProfileScreen;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.Test;

@Log4j2
public class LoginFunctionalityTest extends TestBase {

    @Test(description = "Given I am on the iOS Majles Login page, Then the User is logging into Majles App Then The Avatar Image Is displayed.")
    public void checkLoginFunctionality() {

        String email = jsonFileManagerLoginTestData.getTestData("Users.EmailUser1");
        String password = jsonFileManagerLoginTestData.getTestData("Users.Password");
        Assert.assertTrue(new LoginPage(driverManager).login(email, password).checkTheHomeScreenIsOpened());
    }

    @Test(priority = 2, description = "Given I am on the Majles Profile Screen , When The User Logout, " + "Then the User Can Logged out from The App.")
    public void checkLogoutFunctionality() {
        Assert.assertTrue(new ProfileScreen(driverManager)
                .clickOnProfileIcon()
                .logout()
                .checkUserIsLogout("Welcome Back"));
    }
}