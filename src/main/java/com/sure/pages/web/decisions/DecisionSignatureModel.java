package com.sure.pages.web.decisions;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import com.sure.utilities.Upload;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
public class DecisionSignatureModel extends PageBase {
    public DecisionSignatureModel(DriverManager driverManager) {
        super(driverManager);
    }

    By decisionSignatureModalTitle = By.xpath("//span[contains(text(),'ملف القرار')]");
    By signHereButton = By.xpath("//div[@id='some signature field name']//div//div//div[contains(text(),'وقع هنا')]");
    By createNewSignatureModalTitle = By.xpath("//p[contains(text(),'إنشاء توقيع جديد')]");
    By agreeButton = By.xpath("//*[@id=\"voting-modal___BV_modal_body_\"]/div/div[2]/div[1]/div[1]/button");
    By addButton = By.xpath("//div[@data-element='imageSignaturePanel']//button[@class='signature-create'][contains(text(),'إضافة')]");
    By iframeLocator = By.xpath("//iframe[@title='webviewer']");

    @Step("Click On Agree Button on Decision Voting Model")
    public DecisionSignatureModel clickOnAgreeButton() {
        clickElement(agreeButton);
        return this;
    }

    @Step("Get The Decision Signature Modal Title")
    public String getPopUpTitle() {
        return getElementText(decisionSignatureModalTitle);
    }


//    @Step("Check The Decision Report Appearance")
//    public boolean checkDecisionReportAppearance() {
//        // Switch to the iframe
//        WebElement frame = driver.findElement(iframeLocator);
//        driver.switchTo().frame(frame);
//
//        visibilityWaitForFrameToBeAvailable(iframeLocator, signHereButton);
//
//        // Check if the element is visible
//        return checkElementIsDisplayed(signHereButton);
//    }

    @Step("Check The Decision Report Appearance")
    public boolean checkDecisionReportAppearance() {
        try {
            WebElement frame = driver.findElement(iframeLocator);
            driver.switchTo().frame(frame);

            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            longWait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
            longWait.until(ExpectedConditions.visibilityOfElementLocated(signHereButton));
            log.info("Sign Here Button is visible.");
            return true;
        } catch (NoSuchElementException | TimeoutException e) {
            log.warn("Sign Here Button was not found: {}", e.getMessage());
        } finally {
            driver.switchTo().defaultContent();
        }
        return false;
    }

    @Step("Click On Sign Here Button")
    public DecisionSignatureModel clickOnSignHereButton() {
        clickElement(signHereButton);
        return this;
    }

    @Step("Get New Signature Model Title")
    public String getNewSignatureModelTitle() {
        String result = getElementText(createNewSignatureModalTitle);
        log.info("expectedTitle:{}", result);
        return result;
    }

    @Step("Click On Upload Button")
    public void clickOnUploadButton() {
        clickOnElementByText("رفع");
    }

    @Step("Add New Signature")
    public DecisionPage uploadImage(String fileName) {
        String filePath = System.getProperty("user.dir") + "/attachments/downloadedFiles/Images/" + fileName;
        clickOnUploadButton();
        Upload.uploadFileUsingInput(driver, "//input[@type='file']", filePath);
        clickElement(addButton);
        driver.switchTo().defaultContent();
        return new DecisionPage(driverManager);
    }
}