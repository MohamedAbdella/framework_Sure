package com.sure.pages.web.decisions;

import com.github.javafaker.Faker;
import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static com.sure.utilities.DateTimeUtils.today;

@Log4j2
public class DecisionPage extends PageBase {
    public DecisionPage(DriverManager driverManager) {
        super(driverManager);
    }

    String decisionNumber = String.valueOf(new Faker().number().randomDigitNotZero());
    By decisionScreenTitle = By.xpath("//h3[contains(text(),'القرارات')]");
    By governanceSettingsTab = By.xpath("//a[contains(text(),'اعدادات الحوكمة')]");
    By decisionDetailsTab = By.xpath("//a[contains(text(),'تفاصيل القرار')]");
    By signatureRadioButton = By.xpath("//form[@id=\"decision-form\"]//div[1]/div[1]//div[@class='v-switch-button']");
    By decisionNumberTextField = By.xpath("//div[@role='tabpanel']//fieldset[1]//input[@type='text']");
    By decisionDateTextField = By.xpath("//div[@role='tabpanel']//fieldset//input[@name='date']");
    By decisionDescriptionTextField = By.xpath("//div[@role='tabpanel']//fieldset//textarea");
    By committeeDropDownList = By.xpath("//form[@id='decision-form']/div[1]/div[1]//input[@type='search']");
    By selectCommittee = By.xpath("//form[@id='decision-form']/div[1]//ul[@role='listbox']/li");
    By decisionTypeDropDownList = By.xpath("//form[@id='decision-form']/div[1]/div[2]//input[@type='search']");
    By selectDecisionType = By.xpath("//ul[@id='vs6__listbox']");
    By responsiblePersonDropDownList = By.xpath("//form[@id='decision-form']/div[1]/div[3]//input[@type='search']");
    By selectResponsiblePerson = By.xpath("//ul[@id='vs7__listbox']");
    By votingMechanismDropDownList = By.xpath("//form[@id='decision-form']/div[1]/div[4]//input[@type='search']");
    By selectVotingMechanism = By.xpath("//ul[@id='vs8__listbox']/li[@id='vs8__option-0']");
    By startVoteButton = By.xpath("//div[@class='d-flex align-items-center mb-3']//div[@class='d-flex align-items-center']//p//span[contains(text(),'التصويت')]");
    By createdDecisionNumber = By.xpath("//div[@class=\"col-lg-3 col-12 d-flex py-1 my-lg-0 my-2\"]/p");
    By voteStartedText = By.xpath("//p[@class='mb-0 rounded-pill bg-secondary px-3 py-1 mx-2 bg-action-10']");
    By deleteIcon = By.xpath("//div[@class='d-flex align-items-center mx-2']");
    By noResultText = By.xpath("//*[@class='empty-section text-center']//p");
    By voteResult = By.xpath("//div[@class=\"mt-3\"]//span[contains(text(),'موافق 100 %')]");
    By voteStatus = By.xpath("//ul[@class=\"p-0\"]//span[contains(text(),'موافق')]");
    By voteType = By.xpath("//span[contains(text(),'معلن')]");
    By closeIcon = By.xpath("//header//img[@class='pointer']");

    @Step("Click On Resolution Button")
    public DecisionPage clickOnResolutionButton() {
        clickOnElementByText("إضافة قرار بالتمرير");
        return this;
    }

    @Step("Click On Vote Button")
    public DecisionPage clickOnVoteButton() {
        clickElement(startVoteButton);
        return this;
    }

    @Step("Delete The Resolution Decision")
    public DecisionPage deleteResolution() {
        clickElement(deleteIcon);
        clickOnElementByText("حذف");
        return this;
    }

    @Step("Check The Resolution Decision Is Deleted Successfully")
    public boolean checkTheResolutionIsDeletedSuccessfully() {
        return checkElementIsDisplayed(noResultText);
    }

    @Step("Click On Start Vote Button")
    public DecisionPage clickOnStartVoteButton(){
        clickOnElementByText("بدء التصويت");
        return this;
    }

    @Step("Click On Close Button")
    public DecisionPage clickOnCloseButton() {
        clickOnElementByText("إغلاق");
        return this;
    }

    @Step("Click On Save Icon")
    public DecisionPage clickOnSaveButton() {
        clickOnElementByText("حفظ");
        return this;
    }

    @Step("Click On Publish Button")
    public DecisionPage clickOnPublishButton() {
        scrollByVisibleText(" نشر القرار");
        clickOnElementByText(" نشر القرار");
        return this;
    }

    @Step("Click On Governance Settings Tab")
    public DecisionPage clickOnGovernanceSettingsTab() {
        clickElement(governanceSettingsTab);
        return this;
    }

    @Step("Click On Decision Details Tab")
    public DecisionPage clickOnDecisionDetailsTab() {
        clickElement(decisionDetailsTab);
        return this;
    }

    @Step("Select responsible Person Option")
    public DecisionPage selectResponsiblePerson(String responsiblePerson) {
        sendText(responsiblePersonDropDownList, responsiblePerson);
        visibilityWaitForElementLocated(selectResponsiblePerson);
        clickElement(selectResponsiblePerson);
        return this;
    }

    @Step("Select Committee Option")
    public DecisionPage selectCommitteeOption(String committeeName) {
        sendText(committeeDropDownList, committeeName);
        visibilityWaitForElementLocated(selectCommittee);
        clickElement(selectCommittee);
        return this;
    }

    @Step("Select Decision Type")
    public DecisionPage selectDecisionType(String decisionType) {
        sendText(decisionTypeDropDownList, decisionType);
        clickElement(selectDecisionType);
        return this;
    }

    @Step("Select Voting Type")
    public DecisionPage selectVotingMechanism(String voteType) {
        sendText(votingMechanismDropDownList, voteType);
        clickElement(selectVotingMechanism);
        return this;
    }

    @Step("Enter Resolution Details")
    public DecisionPage enterResolutionDetails(String decisionDescription, String committeeName, String decisionType, String responsiblePerson, String voteType) {
        sendText(decisionNumberTextField, decisionNumber);
        sendText(decisionDateTextField, today("yyyy/MM/dd"));
        sendText(decisionDescriptionTextField, decisionDescription);
        selectCommitteeOption(committeeName);
        selectDecisionType(decisionType);
        selectResponsiblePerson(responsiblePerson);
        selectVotingMechanism(voteType);
        return this;
    }

    @Step("Set Governance Setting")
    public DecisionPage setGovernanceSetting() {
        clickElement(signatureRadioButton);
        return this;
    }

    @Step("Add New Resolution")
    public DecisionPage addNewResolution(String decisionDescription, String committeeName, String decisionType, String responsiblePerson, String voteType) {
        clickOnResolutionButton();
        clickOnGovernanceSettingsTab();
        setGovernanceSetting();
        clickOnDecisionDetailsTab();
        enterResolutionDetails(decisionDescription, committeeName, decisionType, responsiblePerson, voteType);
        return this;
    }

    @Step("Get The Screen Title")
    public String getScreenTitle() {
        return getElementText(decisionScreenTitle);
    }

    @Step("Get the created decision number and compare it to the decision number that user entered")
    public boolean checkResolutionIsCreated() {
        return getElementText(createdDecisionNumber).contains(decisionNumber);
    }

    @Step("Get The Voting Status After Starting")
    public String getTheVotingStatusAfterStarting() {
        return getElementText(voteStartedText);
    }

//    @Step("Get Voting Result")
//    public boolean isVotingResultCorrect(String expectedVoteResult) {
//        try {
//            if (!checkElementIsDisplayed(voteResult)) {
//                visibilityWaitForElementLocated(voteResult);
//            }
//        } catch (Exception ignored) {
//        }
//        String result = getElementText(voteResult);
//        log.info("expectedVoteResult:{}", result);
//        return result.equalsIgnoreCase(expectedVoteResult);
//    }

    @Step("Get Voting Result")
    public boolean isVotingResultCorrect(String expectedVoteResult) {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
            WebElement resultElement = longWait.until(
                    ExpectedConditions.visibilityOfElementLocated(voteResult));
            String result = resultElement.getText();
            log.info("Voting result found: {}", result);
            return result.equalsIgnoreCase(expectedVoteResult);
        } catch (TimeoutException e) {
            log.warn("Voting result was not found within the expected time.");
            return false;
        }
    }

    @Step("Get The Voting Status")
    public boolean isVotingStatusCorrect(String expectedVoteStatus) {
        String votingStatus = getElementText(voteStatus);
        log.info("expectedVoteStatus:{}", votingStatus);
        return votingStatus.equalsIgnoreCase(expectedVoteStatus);
    }

    @Step("Get Voting Mechanism")
    public boolean isVotingMechanismCorrect(String expectedVoteMechanism) {
        String voteMechanism = getElementText(voteType);
        log.info("expectedVoteMechanism:{}", voteMechanism);
        return voteMechanism.equalsIgnoreCase(expectedVoteMechanism);
    }

}