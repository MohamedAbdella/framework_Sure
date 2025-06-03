package com.sure.pages.mobile.ios.decisions;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import com.sure.pages.mobile.ios.HomePage;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class DecisionVotingPopUp extends PageBase {
    public DecisionVotingPopUp(DriverManager driverManager) {
        super(driverManager);
    }


    By agreeButton = By.xpath("//XCUIElementTypeOther[3]//XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeButton");
    By addNotesTextBox = AppiumBy.iOSClassChain("**/XCUIElementTypeTextField[`value == \"Add notes\"`]");
    By sendButton = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Send \"`]");
    By closeIcon = By.xpath("//XCUIElementTypeOther//XCUIElementTypeButton[3]");
    By voteForDecisionPopUpTitle = AppiumBy.accessibilityId("Vote for a decision");
    By backButton = By.xpath("//XCUIElementTypeButton[@name=\"BackEn\"]");


    // Methods to get dynamic locators
    private By getDynamicLocator(String name) {
        // Ensure the name is properly quoted for the XPath expression
        String escapedName = "\"" + name + "\"";
        return By.xpath("//XCUIElementTypeStaticText[@name= " + escapedName + "]");
    }

    @Step("Click On Agree Button on Decision Voting Screen")
    public DecisionVotingPopUp clickOnAgreeButton() {
        clickElement(agreeButton);
        return this;
    }

    @Step("Click On Close Icon")
    public HomePage clickOnCloseIcon() {
        clickElement(closeIcon);
        goBack(1, backButton);
        return new HomePage(driverManager);
    }

    @Step("Check Vote For Decision PopUp Is Opened")
    public String getVoteForDecisionPopUpTitle() {
        return getElementText(voteForDecisionPopUpTitle);
    }

    @Step("Add Notes")
    public DecisionVotingPopUp addNotes(String notes) {
        sendText(addNotesTextBox, notes);
        clickOnElementByName("Done");
        return this;
    }

    @Step("Click On Send Button on Decision Voting Screen")
    public VoteReportScreen clickOnSendButton() {
        clickElement(sendButton);
        return new VoteReportScreen(driverManager);
    }

    @Step("Get Voting Result")
    public boolean isVotingResultCorrect(String expectedVoteResult) {
        return getElementText(getDynamicLocator(expectedVoteResult)).contains(expectedVoteResult);

    }

    @Step("Get The Voting Status")
    public boolean isVotingStatusCorrect(String expectedVoteStatus) {
        return getElementText(getDynamicLocator(expectedVoteStatus)).contains(expectedVoteStatus);
    }

    @Step("Get Voting Mechanism")
    public boolean isVotingMechanismCorrect(String expectedVoteMechanism) {
        return getElementText(getDynamicLocator(expectedVoteMechanism)).contains(expectedVoteMechanism);
    }

}
