package com.sure.tests.webtests;

import com.sure.base.TestBase;
import com.sure.pages.web.LoginPage;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Log4j2
@Listeners(com.sure.utilities.TestNGListener.class)
public class LoginTest extends TestBase {
    @Test(priority = 1, description = "Given I am on Sign In Page, When The User Logged In Then User Name Appears")
    public void loginTest() {
        String url = "login/SGT";

        String email = jsonFileManagerLoginTestData.getTestData("Users.BoardSecretary");
        String password = jsonFileManagerLoginTestData.getTestData("Users.Password");
        log.info("Email: {}", email);
        log.info("Password: {}", password);

        new LoginPage(driverManager).navigateTo(url);
       boolean checkHomeDashboardIsOpened = new LoginPage(driverManager)
                .login(email, password)
                .checkHomeDashboardIsOpened("الرئيسية");
        Assert.assertTrue(checkHomeDashboardIsOpened);

    }
}
