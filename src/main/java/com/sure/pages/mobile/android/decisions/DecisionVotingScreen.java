package com.sure.pages.mobile.android.decisions;

import com.sure.base.DriverManager;
import com.sure.base.PageBase;
import com.sure.pages.mobile.android.HomeScreen;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;

@Log4j2
public class DecisionVotingScreen extends PageBase {
    public DecisionVotingScreen(DriverManager driverManager) {
        super(driverManager);
    }

    By agreeButton = By.id("com.sure.majlestech:id/agreeBtn");
    By commentTextBox = By.id("com.sure.majlestech:id/comment");
    By sendButton = By.id("com.sure.majlestech:id/sendNotesBtn");
    By closeIcon = By.xpath("//XCUIElementTypeOther//XCUIElementTypeButton[3]");
    By backButton = By.id("com.sure.majlestech:id/closeBtn");


    // Methods to get dynamic locators
    private By getDynamicLocator(String name) {
        return By.xpath("//android.widget.TextView[@resource-id=\"com.sure.majlestech:id/" + name + "\"]");
    }

    @Step("Click On Agree Button on Decision Voting Screen")
    public DecisionVotingScreen clickOnAgreeButton() {
        clickElement(agreeButton);
        return this;
    }

    @Step("Enter The Voting Reason")
    public DecisionVotingScreen enterVotingReason(String votingReason) {
        sendText(commentTextBox, votingReason);
        return this;
    }

    @Step("Click On Send Button on Decision Voting Screen")
    public VoteReportScreen clickOnSendButton() {
        scrollToElement("Send");
        clickElement(sendButton);
        return new VoteReportScreen(driverManager);
    }

    @Step("Get Voting Result")
    public boolean isVotingResultCorrect(String expectedVoteResult) {
        String result = String.valueOf(getElementText(getDynamicLocator("agree")));
        log.info("expectedVoteResult:{}", result);
        return result.equalsIgnoreCase(expectedVoteResult);
    }

    @Step("Get The Voting Status")
    public boolean isVotingStatusCorrect(String expectedVoteStatus) {
        String voteStatus = String.valueOf(getElementText(getDynamicLocator("status")));
        log.info("expectedVoteStatus:{}", voteStatus);
        return voteStatus.equalsIgnoreCase(expectedVoteStatus);
    }

    @Step("Get Voting Mechanism")
    public boolean isVotingMechanismCorrect(String expectedVoteMechanism) {
        String voteMechanism = String.valueOf(getElementText(getDynamicLocator("votingType")));
        log.info("expectedVoteMechanism:{}", voteMechanism);
        return voteMechanism.equalsIgnoreCase(expectedVoteMechanism);
    }

    @Step("Click On Close Icon")
    public HomeScreen clickOnCloseIcon() {
        clickElement(closeIcon);
        goBack(2, backButton);
        return new HomeScreen(driverManager);
    }
}
