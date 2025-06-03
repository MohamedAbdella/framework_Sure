package com.sure.pages.web;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import com.sure.pages.web.decisions.DecisionPage;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class MajlesHomePage extends PageBase {
    public MajlesHomePage(DriverManager driverManager) {
        super(driverManager);
    }

    By homeText = By.xpath("//a[@href='/home']");
    By decisionsText = By.xpath("//span[@class='mx-3 font-14'][contains(text(),'القرارات')]");
    By logoutButton = By.xpath("//div[@class='dropdown b-dropdown show btn-group']//ul[@role='menu']/li[4]/a");
    By menuButton = By.xpath("//div[@class='dropdown b-dropdown btn-group']/button[@aria-haspopup='menu']");
    By acceptCookies = By.cssSelector("body > div:nth-child(2) > div.Cookie.Cookie--bottom.Cookie--base > div.Cookie__buttons > button");

    @Step("User Clicks on Menu Button")
    public MajlesHomePage clickOnMenuButton() {
        clickElement(menuButton);
        return this;
    }

    @Step("User Clicks on Logout Button")
    public MajlesHomePage clickOnLogoutButton() {
        clickElement(logoutButton);
        clickOnElementByText("نعم");
        return this;
    }

    @Step("Logout")
    public LoginPage logout() {
        clickOnMenuButton()
                .clickOnLogoutButton();
        return new LoginPage(driverManager);
    }

    @Step("Accept The Cookies")
    public MajlesHomePage acceptTheCookies() {
        clickElement(acceptCookies);
        return this;
    }

    @Step("Home Dashboard Is Opened Successfully")
    public boolean checkHomeDashboardIsOpened(String text) {
        acceptTheCookies();
        return getElementText(homeText).contains(text);
    }

    @Step("click On Decision Button")
    public DecisionPage clickOnDecisionButton() {
        clickElement(decisionsText);
        return new DecisionPage(driverManager);
    }
}
