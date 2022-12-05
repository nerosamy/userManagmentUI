package com.fawry.userManagment.angularAutomation.tests;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.constants.database.RolesDBTable;
import com.fawry.userManagment.angularAutomation.constants.database.UserDBTable;
import com.fawry.userManagment.angularAutomation.constants.excelIndices.AddUserIndices;
import com.fawry.userManagment.angularAutomation.constants.excelIndices.ListRolesIndices;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.dataModels.UsersDM;
import com.fawry.userManagment.angularAutomation.pages.AddUserPage;
import com.fawry.userManagment.angularAutomation.pages.MainPage;
import com.fawry.userManagment.angularAutomation.pages.ViewRolesPage;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;

public class AddUserTests extends BaseTest{

    ArrayList addUserTestData = new ArrayList();

    @Parameters("url")
    @BeforeMethod(alwaysRun = true)
    public void navigateToAddUserPage(String url) {
        Assert.assertEquals(homepage.getWelcomeMsg(), "Welcome", "BLOCKING ISSUE - CAN NOT LOGIN TO APPLICATION");
        String[] splitURL = url.split("/");
        String newURL = splitURL[0]+"//"+splitURL[2]+"/user-manage-v2/users";
        driver.navigate().to(newURL);
        String navigatedToPageSuccessfully = homepage.navigateToAddUsersPage();
        Assert.assertEquals(navigatedToPageSuccessfully, GeneralConstants.SUCCESS, "Could not navigate to Add User page successfully");
    }

    @Test(description = "Validate Add User functionalities",priority = 0 ,dataProvider = "addUserDP" ,enabled = true)
    public void addUser(UsersDM usersDM) {
        //Create extent test to be logged in report using test case title
        test = extent.createTest(usersDM.getTestCaseId() + " --- " + usersDM.getTestCaseTitle());
        Log.test = test;
        Log.startTestCase(usersDM.getTestCaseId() + " --- " + usersDM.getTestCaseTitle());
        AddUserPage addUserPage = new AddUserPage(driver);
        MainPage mainPage = new MainPage(driver);

        String actualResults = addUserPage.setAddUsersDetails(usersDM);
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While Setting User details.");
        actualResults = addUserPage.clickSaveButton();
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While Click Save Button.");

            if (usersDM.getErrType().equals("notification"))
        actualResults = addUserPage.getAllErrMsgs(GeneralConstants.ERR_TYPE_NOTIFICATION);
        else
        actualResults = addUserPage.getAllErrMsgs(GeneralConstants.ERR_TYPE_PAGE);

        Log.info(" *********  Start frontend Assertion  ********");
        Log.info("Expected massage = "+ usersDM.getExpectedMessage());
        Assert.assertEquals(actualResults,usersDM.getExpectedMessage() , GeneralConstants.Expected_ERR_MSG);
        Log.info(" *********  Frontend Assertion passed successfully ********");

        if (usersDM.getErrType().equals("notification")) {
            Log.info(" *********  Start backend Assertion  ********");
            SoftAssert softAssert = new SoftAssert();
            UsersDM backendModel = backendService.getUserDetails(usersDM.getEmail());

            // if program was found in DB, Use soft assert to validate all data and identify all failures if exist
            if (backendModel != null) {
                softAssert.assertEquals(backendModel.getEmail(), usersDM.getEmail(), UserDBTable.TABLE_NAME + "." + UserDBTable.EMAIL + GeneralConstants.MISMATCH_ERR_MSG);
                softAssert.assertEquals(backendModel.getPhone(), usersDM.getPhone(), UserDBTable.TABLE_NAME + "." + UserDBTable.PHONE + GeneralConstants.MISMATCH_ERR_MSG);
                softAssert.assertEquals(backendModel.getRole(), usersDM.getRole(), UserDBTable.TABLE_NAME + "." + UserDBTable.ROLE_NAME + GeneralConstants.MISMATCH_ERR_MSG);
                softAssert.assertEquals(backendModel.getBranch(), usersDM.getBranch(), UserDBTable.TABLE_NAME + "." + UserDBTable.BRANCH + GeneralConstants.MISMATCH_ERR_MSG);
                softAssert.assertEquals(backendModel.getStatus(), usersDM.getStatus(), UserDBTable.TABLE_NAME + "." + UserDBTable.STATUS + GeneralConstants.MISMATCH_ERR_MSG);
                softAssert.assertEquals(backendModel.getMustChangePassword(), usersDM.getMustChangePassword(), UserDBTable.MUST_CHANGE_PASSWORD + "." + UserDBTable.EMAIL + GeneralConstants.MISMATCH_ERR_MSG);
                softAssert.assertEquals(backendModel.getPasswordNeverExpire(), usersDM.getPasswordNeverExpire(), UserDBTable.PASSWORD_NEVER_EXPIRE + "." + UserDBTable.EMAIL + GeneralConstants.MISMATCH_ERR_MSG);

                softAssert.assertAll();
                Log.info(" *********  Backend Assertion passed successfully ********");
            } else {
                Log.info(" *********  Backend Assertion Failed as User  : " + usersDM.getEmail() + " was not found in DB ********");
                Assert.fail("DB error occurred or User was not found in DB");
            }
        }
    }

    @DataProvider(name = "addUserDP")
    public Object[][] provideAddUserTD() {
        Object[][] addUserDP = new Object[addUserTestData.size()][1];
        for (int i = 0; i < addUserTestData.size(); i++)
            addUserDP[i][0] = addUserTestData.get(i);

        return addUserDP;
    }

    @BeforeClass
    public void addUserTD() {

        ArrayList<ArrayList<Object>> resultArray = provideTestData("addUser");

        for (int i = 0; i < resultArray.size(); i++) {
            UsersDM usersDM = new UsersDM();

            // fill Customer data model whether for add or update test
            usersDM.setTestCaseId(resultArray.get(i).get(AddUserIndices.TEST_CASE_ID_INDEX).toString());
            usersDM.setTestCaseTitle(resultArray.get(i).get(AddUserIndices.TEST_CASE_TITLE_INDEX).toString());
            usersDM.setTestScope(resultArray.get(i).get(AddUserIndices.TEST_SCOPE_INDEX).toString());
            usersDM.setEmail(resultArray.get(i).get(AddUserIndices.EMAIL_INDEX).toString());
            usersDM.setPhone(resultArray.get(i).get(AddUserIndices.PHONE_INDEX).toString());
            usersDM.setRole(resultArray.get(i).get(AddUserIndices.ROLE_INDEX).toString());
            usersDM.setBranch(resultArray.get(i).get(AddUserIndices.BRANCH_INDEX).toString());
            usersDM.setStatus(resultArray.get(i).get(AddUserIndices.STATUS_INDEX).toString());
            usersDM.setMustChangePassword(resultArray.get(i).get(AddUserIndices.MUST_CHANGE_PASSWORD_INDEX).toString());
            usersDM.setPasswordNeverExpire(resultArray.get(i).get(AddUserIndices.PASSWORD_NEVER_EXPIRED_INDEX).toString());
            usersDM.setExpectedMessage(resultArray.get(i).get(AddUserIndices.EXPECTED_MASSAGE_INDEX).toString());
            usersDM.setErrType(resultArray.get(i).get(AddUserIndices.ERROR_MSG_TYPE).toString());



            addUserTestData.add(usersDM);

        }

    }
}
