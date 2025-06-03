package com.sure.tests.webtests;

import com.sure.base.TestBase;
import com.sure.pages.web.LoginPage;
import com.sure.pages.web.MajlesHomePage;
import com.sure.pages.web.decisions.DecisionPage;
import com.sure.pages.web.decisions.DecisionSignatureModel;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Log4j2
@Listeners(com.sure.utilities.TestNGListener.class)
public class ResolutionDecisionTest extends TestBase {
    String email, password;

    @Test(priority = 1, description = "Given I am on Sign In Page, When The User Logged In Then User Name Appears")
    public void checkHomeDashboardIsOpened() {
        String url = "login/SGT";
        new LoginPage(driverManager).navigateTo(url);

        email = jsonFileManagerLoginTestData.getTestData("Users.BoardSecretary");
        password = jsonFileManagerLoginTestData.getTestData("Users.Password");

        Assert.assertTrue(new LoginPage(driverManager)
                .login(email, password)
                .checkHomeDashboardIsOpened("الرئيسية"));
    }

    @Test(priority = 2, description = "Given I am on Majles Home Page ,When The User Click on Decision Button" +
            " From Left Side Menu ,Then Decision Page is opened Successfully")
    public void checkDecisionPageIsOpened() {
        Assert.assertEquals(new MajlesHomePage(driverManager)
                .clickOnDecisionButton()
                .getScreenTitle(), "القرارات");
    }

    @Test(priority = 3, description = "Given I am on Majles Home Page ,When The User Click on Decision Button" +
            " From Left Side Menu ,Then the User is able to Add New Resolution and Publish it.")
    public void publishNewResolution() {
        Assert.assertTrue(new DecisionPage(driverManager)
                .addNewResolution("Description", "مجلس جديد", "للمعلومية", "اول", "معلن")
                .clickOnPublishButton()
                .checkResolutionIsCreated());
    }

    @Test(priority = 4, description = "Given I am on Decision Page ,When The Users Click Vote and Start Voting buttons" +
            "Then the Voting is Started Successfully")
    public void startVoting() {
        Assert.assertEquals(new DecisionPage(driverManager)
                .clickOnVoteButton()
                .clickOnStartVoteButton()
                .clickOnCloseButton()
                .getTheVotingStatusAfterStarting(), "جاري التصويت");
    }


    @Test(priority = 5, description = "Given I am on Decision Screen ,When The User Click on vote button" +
            "Then the User is able to Vote and Decision Signature Modal Is Opened.")
    public void checkDecisionSignatureModalIsOpenedAfterVoting() {
        new DecisionPage(driverManager)
                .clickOnVoteButton();
        Assert.assertEquals(new DecisionSignatureModel(driverManager)
                .clickOnAgreeButton()
                .getPopUpTitle(), "ملف القرار");
        Assert.assertTrue(new DecisionSignatureModel(driverManager)
                .checkDecisionReportAppearance());
    }

    @Test(priority = 6, description = "Given I am on Decision Screen ,When The User Click on vote button" +
            "Then the User is able to Vote and Decision Signature Modal Is Opened.")
    public void addNewSignature() {
        Assert.assertEquals(new DecisionSignatureModel(driverManager)
                .clickOnSignHereButton()
                .getNewSignatureModelTitle(), "إنشاء توقيع جديد");
    }

    @Test(priority = 7, description = "Given I am on the Vote For Decision PopUp Page,When I view the voting details," +
            "Then I should be able to verify their correctness.")
    public void checkVotingDetailsAfterAcceptanceAreCorrect() {

        new DecisionSignatureModel(driverManager)
                .uploadImage("test.png");

        String expectedResult = "موافق 100 %";
        String expectedStatus = "موافق";
        String expectedVoteMechanism = "معلن";

        Assert.assertTrue(new DecisionPage(driverManager).isVotingResultCorrect(expectedResult), "Voting result does not match.");
        Assert.assertTrue(new DecisionPage(driverManager).isVotingStatusCorrect(expectedStatus), "Voting status does not match.");
        Assert.assertTrue(new DecisionPage(driverManager).isVotingMechanismCorrect(expectedVoteMechanism), "Voting mechanism does not match.");
    }

    @Test(priority = 8, description = "Given I am on the Decision Page,When User Clicks on Close button and Clicks on" +
            " Delete Button and Confirm Button,Then User should be able to Delete The Resolution Decision")
    public void deleteTheResolution() {
        Assert.assertTrue(new DecisionPage(driverManager)
                .clickOnCloseButton()
                .clickOnSaveButton()
                .deleteResolution()
                .checkTheResolutionIsDeletedSuccessfully());
    }

    @Test(priority = 9, description = "Given I am on the Decision Page,When User Clicks on Menu button and Clicks" +
            "On Logout Button ,Then User should be able to Logout")
    public void logout() {
        Assert.assertTrue(new MajlesHomePage(driverManager)
                .logout()
                .checkLoginButtonAppearance());
    }

}
