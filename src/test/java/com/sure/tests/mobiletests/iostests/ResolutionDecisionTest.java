package com.sure.tests.mobiletests.iostests;

import com.sure.api.decisions.AddResolutionDecision;
import com.sure.api.decisions.DeleteResolutionDecision;
import com.sure.api.decisions.StartVoting;
import com.sure.api.userauth.LoginByCriteriaApi;
import com.sure.base.TestBase;
import com.sure.enums.VotingType;
import com.sure.pages.mobile.ios.HomePage;
import com.sure.pages.mobile.ios.LoginPage;
import com.sure.pages.mobile.ios.ProfileScreen;
import com.sure.pages.mobile.ios.decisions.DecisionScreen;
import com.sure.pages.mobile.ios.decisions.DecisionVotingPopUp;
import com.sure.pages.mobile.ios.decisions.VoteReportScreen;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Log4j2
@Listeners(com.sure.utilities.TestNGListener.class)
public class ResolutionDecisionTest extends TestBase {
    private JsonPath jsonPath;
    private String token;
    private int resolutionDecisionId;

    @Test(priority = 1, description = "Login From Api ,Create a New Resolution Decision then Start Voting from Api.")
    public void createResolutionDecisionWorkFlowFromApi() {
        login();
        addNewResolutionDecision();
        startVoting();
    }

    @Test(priority = 2, description = "Given I am on the Majles Home Screen , When The User Click Decision Button From Menu Button," +
            " Then the User Can See Decision Screen.")
    public void checkDecisionScreenIsOpened() {
        String email = jsonFileManagerLoginTestData.getTestData("Users.BoardSecretary");
        String password = jsonFileManagerLoginTestData.getTestData("Users.Password");

        new LoginPage(driverManager)
                .login(email, password);

        Assert.assertEquals(new HomePage(driverManager)
                .clickOnMenuButton()
                .clickOnDecisionButton()
                .clickOnResolutionTab()
                .getScreenTitle(), "Decisions");
    }

    @Test(priority = 3, description = "Given I am on Decision Screen ,When The User Click on vote Section" +
            "Then the User is able to Vote and Signature Screen Is Opened.")
    public void checkSignatureScreenIsOpenedAfterVoting() {
        Assert.assertTrue(new DecisionScreen(driverManager)
                .clickOnDecisionSectionToVote()
                .clickOnAgreeButton()
                .addNotes("I'm Agree")
                .clickOnSendButton()
                .checkSignatureScreenIsOpened(), "Signature Screen not opened");
    }

    @Test(priority = 4, description = "Given I am on Vote Report Screen ,When The Users Click Sign button" +
            "Then the User Add new signature")
    public void addSignature() {
        Assert.assertEquals(new VoteReportScreen(driverManager)
                .clickOnSignHereButton()
                .addSignature()
                .clickOnSendButton()
                .clickOnDecisionSectionToVote()
                .getVoteForDecisionPopUpTitle(), "Vote for a decision");
    }

    @Test(priority = 5, description = "Given I am on the Vote For Decision PopUp screen,When I view the voting details," +
            "Then I should be able to verify their correctness.")
    public void checkVotingDetailsAfterAcceptanceAreCorrect() {
        String expectedResult = "Agree: 100.00%";
        String expectedStatus = "Accept";
        String expectedVoteMechanism = "Voting mechanism: public";

        Assert.assertTrue(new DecisionVotingPopUp(driverManager).isVotingResultCorrect(expectedResult), "Voting result does not match.");
        Assert.assertTrue(new DecisionVotingPopUp(driverManager).isVotingStatusCorrect(expectedStatus), "Voting status does not match.");
        Assert.assertTrue(new DecisionVotingPopUp(driverManager).isVotingMechanismCorrect(expectedVoteMechanism), "Voting mechanism does not match.");

    }

    @Test(priority = 6, description = "Given I am on the Vote For Decision PopUp screen,When User Clicks on Close Button" +
            "then Go Back, Then I should be able to View Home Screen.")
    public void checkTheHomeScreenIsOpenedAfterGoingBack() {
        Assert.assertTrue(new DecisionVotingPopUp(driverManager)
                .clickOnCloseIcon()
                .checkTheHomeScreenIsOpened(), "Home Page not Opened.");
    }

    @Test(priority = 7, description = "Given I am on Home Screen, When The User Clicks on Profile Screen From Navbar," +
            "Then the User Can view Logout button to Logged out from The App.")
    public void logout() {
        Assert.assertTrue(new ProfileScreen(driverManager)
                .clickOnProfileIcon()
                .logout()
                .checkUserIsLogout("Welcome Back"), "User isn't able to Logout from The App .");
    }

    @Test(priority = 8, description = "Delete The Newly Created Resolution Decision from Api to Make The Test Class more Dynamic.")
    public void deleteResolutionDecisionFromApi() {
        deleteResolutionDecision();
    }

    @Step("Login From Api to get The Token")
    private void login() {
        token = new LoginByCriteriaApi().loginByCriteriaApi(94, "yabdelmenaam@sure.com.sa", "123456");
    }

    @Step("Add a new Resolution Decision From Api")
    private void addNewResolutionDecision() {
        jsonPath = new AddResolutionDecision().addResolutionDecisions(token, VotingType.votingType.PUBLIC_VOTING);
        resolutionDecisionId = new AddResolutionDecision().getResolutionDecisionId(jsonPath);
        assertResolutionDecisionCreated();
    }

    @Step("Assert Resolution Decision creation From Api")
    private void assertResolutionDecisionCreated() {
        Assert.assertEquals(new AddResolutionDecision().getResolutionDecisionCreatedMessage(jsonPath), "Action created successfully");
    }

    @Step("Start Voting From Api")
    private void startVoting() {
        Assert.assertEquals(new StartVoting().startVoting(token, resolutionDecisionId), "Action updated successfully");
    }

    @Step("Delete the meeting From Api")
    private void deleteResolutionDecision() {
        Assert.assertEquals(new DeleteResolutionDecision().deleteResolutionDecision(token, resolutionDecisionId), "Action deleted successfully");
    }
}