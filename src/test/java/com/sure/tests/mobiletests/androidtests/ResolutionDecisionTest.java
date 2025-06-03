package com.sure.tests.mobiletests.androidtests;

import com.sure.api.decisions.AddResolutionDecision;
import com.sure.api.decisions.DeleteResolutionDecision;
import com.sure.api.decisions.StartVoting;
import com.sure.api.userauth.LoginByCriteriaApi;
import com.sure.base.TestBase;
import com.sure.enums.VotingType;
import com.sure.pages.mobile.android.HomeScreen;
import com.sure.pages.mobile.android.LoginScreen;
import com.sure.pages.mobile.android.ProfileScreen;
import com.sure.pages.mobile.android.decisions.DecisionScreen;
import com.sure.pages.mobile.android.decisions.DecisionVotingScreen;
import com.sure.pages.mobile.android.decisions.VoteReportScreen;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.Test;

@Log4j2
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
        new LoginScreen(driverManager)
                .login(email, password);

        String checkDecisionScreenIsOpened = new HomeScreen(driverManager)
                .clickOnMenuButton()
                .clickOnDecisionButton()
                .clickOnResolutionTab()
                .getScreenTitle();
        Assert.assertEquals(checkDecisionScreenIsOpened, "Decisions");
    }

    @Test(priority = 3, description = "Given I am on Decision Screen ,When The User Click on vote Section" +
            "Then the User is able to Vote and Signature Screen Is Opened.")
    public void checkSignatureScreenIsOpenedAfterVoting() {
        Assert.assertEquals(new DecisionScreen(driverManager)
                .clickOnVoteIcon()
                .clickOnAgreeButton()
                .enterVotingReason("I'm Agree")
                .clickOnSendButton()
                .getScreenTitle(), "Vote report");
    }

    @Test(priority = 4, description = "Given I am on Vote Report Screen ,When The Users Click Sign button" +
            "Then the User Add new signature")
    public void addSignature() {
        Assert.assertTrue(new VoteReportScreen(driverManager)
                .clickOnSignHereButton()
                .addSignature()
                .checkSignHereButtonNotDisplayedAfterSign(), "The Signature Process Not Done");
    }

    @Test(priority = 5, description = "Given I am on Vote Report Screen ,When The Users Click go Back button and" +
            "Click on menu button then clicks on Decision Button then clicks on Resolution Tab then Clicks on" +
            "Vote Icon ,The User is able to View Voting Details After Acceptance")
    public void checkVotingDetailsAfterAcceptance() {
        new HomeScreen(driverManager)
                .clickOnMenuButton()
                .clickOnDecisionButton()
                .clickOnResolutionTab()
                .clickOnVoteIcon();
        String expectedResult = "Agree: 100%";
        String expectedStatus = "Agree";
        String expectedVoteMechanism = "Public";
        Assert.assertTrue(new DecisionVotingScreen(driverManager).isVotingResultCorrect(expectedResult), "Voting result does not match.");
        Assert.assertTrue(new DecisionVotingScreen(driverManager).isVotingStatusCorrect(expectedStatus), "Voting status does not match.");
        Assert.assertTrue(new DecisionVotingScreen(driverManager).isVotingMechanismCorrect(expectedVoteMechanism), "Voting mechanism does not match.");
    }

    @Test(priority = 6, description = "Given I am on the Vote For Decision PopUp screen,When User Clicks on Close Button" +
            "then Go Back, Then I should be able to View Home Screen.")
    public void checkTheHomeScreenIsOpenedAfterGoingBack() {
        Assert.assertTrue(new DecisionVotingScreen(driverManager)
                .clickOnCloseIcon()
                .checkTheHomeScreenIsOpened(), "Home Page not Opened.");
    }

    @Test(priority = 7, description = "Given I am on Home Screen, When The User Clicks on Profile Screen From Navbar," +
            "Then the User Can view Logout button to Logged out from The App.")
    public void logout() {
        Assert.assertTrue(new ProfileScreen(driverManager)
                .clickOnProfileIcon()
                .logout()
                .checkUserIsLogout("Welcome back"));
    }

    @Test(priority = 8, description = "Delete The Newly Created Meeting from Api to Make The Test Class more Dynamic.")
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