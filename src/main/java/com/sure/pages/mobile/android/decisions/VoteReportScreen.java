package com.sure.pages.mobile.android.decisions;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class VoteReportScreen extends PageBase {
    public VoteReportScreen(DriverManager driverManager) {
        super(driverManager);
    }

    By voteReportScreenTitle = By.id("com.sure.majlestech:id/title");
    By signHereButton = AppiumBy.androidUIAutomator("new UiSelector().text(\"sign here\")");
    By imageIcon = By.id("com.sure.majlestech:id/tools_dialog_floating_sig_button_image");
    By storeSignatureSwitch = By.id("com.sure.majlestech:id/btn_store_signature");
    By photoGalleryButton = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.LinearLayout\").instance(5)");
    By photoGalleryFolder = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.RelativeLayout\")");
    By selectPhotoFromGalleryFolder = By.xpath("//android.support.v7.widget.RecyclerView//android.view.ViewGroup[2]");
    By backButton = AppiumBy.id("com.sure.majlestech:id/closeBtn");
    By cancelComment = AppiumBy.id("com.sure.majlestech:id/cancel");

    @Step("Get The Screen App Title")
    public String getScreenTitle() {
        return getElementText(voteReportScreenTitle);
    }

    @Step("Click On Sign Here Icon")
    public VoteReportScreen clickOnSignHereButton() {
        waitForPageToLoad(signHereButton);
        clickElement(signHereButton);
        return this;
    }

    @Step("Add New Signature")
    public VoteReportScreen addSignature() {
        try {
            if (checkElementIsDisplayed(storeSignatureSwitch)) {
                String value = driver.findElement(storeSignatureSwitch).getAttribute("checked");
                if (!"true".equals(value)) {
                    clickElement(storeSignatureSwitch);
                }
            }
        } catch (Exception e) {
            log.info("The store signature switch is already checked by default: {}", e.getMessage());
        }
        clickElement(imageIcon);
        clickElement(photoGalleryButton);
        clickElement(photoGalleryFolder);
        clickElement(selectPhotoFromGalleryFolder);
        clickElement(cancelComment);
        goBack(1, backButton);
        return this;
    }

    @Step("Check Sign Here Button Is Disappeared After Sign")
    public boolean checkSignHereButtonNotDisplayedAfterSign() {
        waitForElementDisappear(signHereButton);
        boolean signHereButtonIsDisappear = !checkElementIsDisplayed(signHereButton);
        goBack(1, backButton);
        return signHereButtonIsDisappear;
    }
}