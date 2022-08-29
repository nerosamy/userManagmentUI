package com.fawry.userManagment.angularAutomation.tests;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.constants.database.RolesDBTable;
import com.fawry.userManagment.angularAutomation.constants.database.UserDBTable;
import com.fawry.userManagment.angularAutomation.constants.excelIndices.AddUserIndices;
import com.fawry.userManagment.angularAutomation.constants.excelIndices.EditUserIndices;
import com.fawry.userManagment.angularAutomation.constants.excelIndices.ListUserIndices;
import com.fawry.userManagment.angularAutomation.dataModels.UsersDM;
import com.fawry.userManagment.angularAutomation.pages.AddUserPage;
import com.fawry.userManagment.angularAutomation.pages.ViewUserPage;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;

public class ViewUserTest extends BaseTest{

    ArrayList listUsersTestData = new ArrayList();
    ArrayList editUserTestData = new ArrayList();

    @Parameters("url")
    @BeforeMethod(alwaysRun = true)
    public void navigateToViewUserPage(String url) {
        Assert.assertEquals(homepage.getWelcomeMsg(), "Welcome", "BLOCKING ISSUE - CAN NOT LOGIN TO APPLICATION");
        String[] splitURL = url.split("/");
        String newURL = splitURL[0]+"//"+splitURL[2]+"/user-manage-v2/users";
        driver.navigate().to(newURL);
        String navigatedToPageSuccessfully = homepage.navigateToViewUsersPage();
        Assert.assertEquals(navigatedToPageSuccessfully, GeneralConstants.SUCCESS, "Could not navigate to View User page successfully");
    }


    @Test(description = "Validate view User functionalities",priority = 0 ,dataProvider = "listUsersDP" ,enabled = false)
    public void listUser(UsersDM usersDM) {
        //Create extent test to be logged in report using test case title
        test = extent.createTest(usersDM.getTestCaseId() + " --- " + usersDM.getTestCaseTitle());
        Log.test = test;
        Log.startTestCase(usersDM.getTestCaseId() + " --- " + usersDM.getTestCaseTitle());
        if(usersDM.getEmail() != "")
        {
            usersDM.setEmail(backendService.getUsersList(null).get(3).getEmail());
        }
        ViewUserPage viewUserPage = new ViewUserPage(driver);
        ArrayList<UsersDM> frontendDMs = viewUserPage.searchUser(usersDM);
        Assert.assertEquals(viewUserPage.actual,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While search user and store result in ArrayList.");


        Log.info(" *********  Start backend Assertion  ********");
        SoftAssert softAssert = new SoftAssert();
        ArrayList<UsersDM> backendModel = backendService.getUsersList(usersDM);

        // if program was found in DB, Use soft assert to validate all data and identify all failures if exist
        if(backendModel != null) {
            Assert.assertEquals(backendModel.size(),frontendDMs.size() ,"backend result set size and frontend result set size doesn't match");
            for (int i = 0 ; i < backendModel.size() ; i++)
            {
                boolean flag = false;
                for(int j = 0 ; j < frontendDMs.size() ; j++)
                {
                    if(backendModel.get(i).getEmail().trim().equalsIgnoreCase(frontendDMs.get(j).getEmail().trim())
                            && backendModel.get(i).getPhone().trim().equalsIgnoreCase(frontendDMs.get(j).getPhone().trim()))
                    {
                        softAssert.assertEquals(backendModel.get(i).getStatus(), frontendDMs.get(j).getStatus(), UserDBTable.TABLE_NAME + "." + UserDBTable.STATUS + GeneralConstants.MISMATCH_ERR_MSG);
                        softAssert.assertEquals(backendModel.get(i).getRole(), frontendDMs.get(j).getRole(), UserDBTable.TABLE_NAME + "." + UserDBTable. ROLE_NAME + GeneralConstants.MISMATCH_ERR_MSG);
                        flag = true;
                        break;
                    }

                }
                if(!flag)
                {
                    softAssert.fail(" User :" +backendModel.get(i).getEmail().trim()+" found in DB and not listed in UI");
                }
            }
            softAssert.assertAll();
            Log.info(" *********  Backend Assertion passed successfully ********");
        }
    }

    @Test(description = "Validate edit user functionalities",priority = 1 ,dataProvider = "editUsersDP" ,enabled = true)
    public void editUser(UsersDM usersDM) {
        //Create extent test to be logged in report using test case title
        test = extent.createTest(usersDM.getTestCaseId() + " --- " + usersDM.getTestCaseTitle());
        Log.test = test;
        Log.startTestCase(usersDM.getTestCaseId() + " --- " + usersDM.getTestCaseTitle());

        AddUserPage addUserPage = new AddUserPage(driver);
        ViewUserPage viewUserPage = new ViewUserPage(driver);

        String userEmail = viewUserPage.clickEditUserButton();
        Assert.assertNotEquals(userEmail,GeneralConstants.FAILED,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While click edit button and capture user name from UI.");

        usersDM.setEmail(userEmail);

        String actualResult = addUserPage.editUser(usersDM);
        Assert.assertEquals(actualResult,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While click edit button and capture user name from UI.");

        actualResult = addUserPage.clickSaveButton();
        Assert.assertNotEquals(actualResult,GeneralConstants.FAILED,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While click save button");

        Log.info(" *********  Start frontend Assertion  ********");
        Assert.assertTrue(driver.getCurrentUrl().endsWith("users") , "Error happened while saving updated data and browser doesn't navigate to View users Page. and current URL : "+driver.getCurrentUrl());
        Log.info(" *********  Frontend Assertion passed successfully ********");

        Log.info(" *********  Start backend assertion  ********");
        UsersDM usersDM1 = backendService .getUserDetails(userEmail);
        prepareUpdatedUserModelForAssertion(usersDM , usersDM1);
        SoftAssert softAssert = new SoftAssert();
        UsersDM backendModel = backendService.getUserDetails(usersDM.getEmail());


        // if program was found in DB, Use soft assert to validate all data and identify all failures if exist
        if(backendModel != null) {
            softAssert.assertEquals(backendModel.getEmail(), usersDM.getEmail(), UserDBTable.TABLE_NAME + "." + UserDBTable.EMAIL + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendModel.getRole(), usersDM.getRole(), UserDBTable.TABLE_NAME + "." + UserDBTable.ROLE_NAME + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendModel.getBranch(), usersDM.getBranch(), UserDBTable.TABLE_NAME + "." + UserDBTable.BRANCH + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendModel.getStatus(), usersDM.getStatus(), UserDBTable.TABLE_NAME + "." + UserDBTable.STATUS + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendModel.getMustChangePassword(), usersDM.getMustChangePassword(), UserDBTable.MUST_CHANGE_PASSWORD + "." + UserDBTable.EMAIL + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendModel.getPasswordNeverExpire(), usersDM.getPasswordNeverExpire(), UserDBTable.PASSWORD_NEVER_EXPIRE + "." + UserDBTable.EMAIL + GeneralConstants.MISMATCH_ERR_MSG);

            softAssert.assertAll();
            Log.info(" *********  Backend Assertion passed successfully ********");
        }
        else {
            Log.info(" *********  Backend Assertion Failed as User  : " + usersDM.getEmail() + " was not found in DB ********");
            Assert.fail("DB error occurred or User was not found in DB");
        }
    }

    @Test(description = "Validate remove user functionality",priority = 2 ,enabled = true)
    public void removeUser() {
        //Create extent test to be logged in report using test case title
        test = extent.createTest("TC-16 --- Validate remove user functionality");
        Log.test = test;
        Log.startTestCase("TC-16 --- Validate remove user functionality");

        ViewUserPage viewUserPage = new ViewUserPage(driver);

        String userEmail = viewUserPage.clickRemoveUserButton();
        Assert.assertNotEquals(userEmail,GeneralConstants.FAILED,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While click edit button and capture user name from UI.");

        Log.info(" *********  Start backend assertion  ********");
        SoftAssert softAssert = new SoftAssert();
        UsersDM backendModel = backendService.getUserDetails(userEmail);


        // if program was found in DB, Use soft assert to validate all data and identify all failures if exist
        if(backendModel != null) {
            softAssert.assertEquals(backendModel.getEmail(), userEmail, UserDBTable.TABLE_NAME + "." + UserDBTable.EMAIL + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendModel.getStatus().toLowerCase().trim(), GeneralConstants.STATUS_DELETED.toLowerCase().trim() , UserDBTable.TABLE_NAME + "." + UserDBTable.STATUS + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertAll();
            Log.info(" *********  Backend Assertion passed successfully ********");
        }
        else {
            Log.info(" *********  Backend Assertion Failed as User  : " + userEmail + " was not found in DB ********");
            Assert.fail("DB error occurred or User was not found in DB");
        }
    }

    /* Prepare user model to include updated data from test data sheet while all blank fields in test data sheet should be replaced with data from DB before updating the User
     to assert that only fields in test data were updated successfully and the other fields are kept as is before the update */
    public UsersDM prepareUpdatedUserModelForAssertion(UsersDM updatedUser, UsersDM userFromDBBeforeUpdate) {
        if (updatedUser.getRole().isEmpty())
            updatedUser.setRole(userFromDBBeforeUpdate.getRole());
        if (updatedUser.getBranch().isEmpty())
            updatedUser.setBranch(userFromDBBeforeUpdate.getBranch());
        if (updatedUser.getStatus().isEmpty())
            updatedUser.setStatus(userFromDBBeforeUpdate.getStatus());
        if (updatedUser.getMustChangePassword().isEmpty())
            updatedUser.setMustChangePassword(userFromDBBeforeUpdate.getMustChangePassword());
        if (updatedUser.getPasswordNeverExpire().isEmpty())
            updatedUser.setPasswordNeverExpire(userFromDBBeforeUpdate.getPasswordNeverExpire());

        return updatedUser;
    }

    @DataProvider(name = "listUsersDP")
    public Object[][] provideListUsersTD() {
        Object[][] listUsersDP = new Object[listUsersTestData.size()][1];
        for (int i = 0; i < listUsersTestData.size(); i++)
            listUsersDP[i][0] = listUsersTestData.get(i);

        return listUsersDP;
    }

    @DataProvider(name = "editUsersDP")
    public Object[][] provideEditUsersTD() {
        Object[][] editUsersDP = new Object[editUserTestData.size()][1];
        for (int i = 0; i < editUserTestData.size(); i++)
            editUsersDP[i][0] = editUserTestData.get(i);

        return editUsersDP;
    }


    @BeforeClass
    public void listUserTD() {

        ArrayList<ArrayList<Object>> resultArray = provideTestData("listUser");

        for (int i = 0; i < resultArray.size(); i++) {
            UsersDM usersDM = new UsersDM();

            // fill Customer data model whether for add or update test
            usersDM.setTestCaseId(resultArray.get(i).get(ListUserIndices.TEST_CASE_ID_INDEX).toString());
            usersDM.setTestCaseTitle(resultArray.get(i).get(ListUserIndices.TEST_CASE_TITLE_INDEX).toString());
            usersDM.setTestScope(resultArray.get(i).get(ListUserIndices.TEST_SCOPE_INDEX).toString());
            usersDM.setEmail(resultArray.get(i).get(ListUserIndices.EMAIL_INDEX).toString());
            usersDM.setRole(resultArray.get(i).get(ListUserIndices.ROLE_INDEX).toString());
            usersDM.setStatus(resultArray.get(i).get(ListUserIndices.STATUS_INDEX).toString());


            listUsersTestData.add(usersDM);

        }

    }

    @BeforeClass
    public void editUserTD() {

        ArrayList<ArrayList<Object>> resultArray = provideTestData("editUser");

        for (int i = 0; i < resultArray.size(); i++) {
            UsersDM usersDM = new UsersDM();

            // fill Customer data model whether for add or update test
            usersDM.setTestCaseId(resultArray.get(i).get(EditUserIndices.TEST_CASE_ID_INDEX).toString());
            usersDM.setTestCaseTitle(resultArray.get(i).get(EditUserIndices.TEST_CASE_TITLE_INDEX).toString());
            usersDM.setTestScope(resultArray.get(i).get(EditUserIndices.TEST_SCOPE_INDEX).toString());
            usersDM.setRole(resultArray.get(i).get(EditUserIndices.ROLE_INDEX).toString());
            usersDM.setBranch(resultArray.get(i).get(EditUserIndices. BRANCH_INDEX).toString());
            usersDM.setStatus(resultArray.get(i).get(EditUserIndices.STATUS_INDEX).toString());
            usersDM.setMustChangePassword(resultArray.get(i).get(EditUserIndices.MUST_CHANGE_PASSWORD_INDEX).toString());
            usersDM.setPasswordNeverExpire(resultArray.get(i).get(EditUserIndices.PASSWORD_NEVER_EXPIRED_INDEX).toString());


            editUserTestData.add(usersDM);

        }

    }
}
