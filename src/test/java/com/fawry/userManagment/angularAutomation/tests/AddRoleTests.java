package com.fawry.userManagment.angularAutomation.tests;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.constants.database.RolesDBTable;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.pages.AddRolePage;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;

public class AddRoleTests extends BaseTest{



    @Parameters("url")
    @BeforeMethod(alwaysRun = true)
    public void navigateToAddRolePage(String url) {
        Assert.assertEquals(homepage.getWelcomeMsg(), "Welcome", "BLOCKING ISSUE - CAN NOT LOGIN TO APPLICATION");
        String[] splitURL = url.split("/");
        String newURL = splitURL[0]+"//"+splitURL[2]+"/user-manage-v2/users";
        driver.navigate().to(newURL);
        String navigatedToPageSuccessfully = homepage.navigateToAddRolesPage();
        Assert.assertEquals(navigatedToPageSuccessfully, GeneralConstants.SUCCESS, "Could not navigate to Add Roles page successfully");
    }

    @Test(description = "Validate add new role by select all permissions functionalities",priority = 0  ,enabled = true)
    public void addRoleWithAllPermissions() {
        //Create extent test to be logged in report using test case title
        test = extent.createTest("TC-17 --- Validate add new role by select all permissions");
        Log.test = test;
        Log.startTestCase("TC-17 --- Validate add new role by select all permissions");
        AddRolePage addRolePage = new AddRolePage(driver);
        RolesDM rolesDM = new RolesDM();
        String actualResults = addRolePage.setRoleDetails(rolesDM);
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While setting roles data.");
        actualResults = addRolePage.selectAllPermissions();
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While selecting all permissions.");
        actualResults = addRolePage.clickSaveButton();
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While clicking save button.");
        actualResults = addRolePage.getAllErrMsgs(GeneralConstants.ERR_TYPE_NOTIFICATION);
        rolesDM.setExpectedMessage(GeneralConstants.SUCCESS_MASSAGE);
        assertAddRole(rolesDM ,actualResults , addRolePage.permissionName);
    }

    @Test(description = "Validate add new role by specific permission permission functionalities",priority = 1  ,enabled = true)
    public void addRoleWithSelectedPermission() {
        //Create extent test to be logged in report using test case title
        test = extent.createTest("TC-18 --- Validate add new role by specific permission permission");
        Log.test = test;
        Log.startTestCase("TC-18 --- Validate add new role by specific permission permission");
        AddRolePage addRolePage = new AddRolePage(driver);
        RolesDM rolesDM = new RolesDM();
        String actualResults = addRolePage.setRoleDetails(rolesDM);
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While setting roles data.");
        actualResults = addRolePage.selectSpecificPermissions();
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While specific permissions.");
        actualResults = addRolePage.clickSaveButton();
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While clicking save button.");
        actualResults = addRolePage.getAllErrMsgs(GeneralConstants.ERR_TYPE_NOTIFICATION);
        rolesDM.setExpectedMessage(GeneralConstants.SUCCESS_MASSAGE);
        assertAddRole(rolesDM ,actualResults , addRolePage.permissionName);
    }

    public void assertAddRole(RolesDM frontEndModel , String actualResults , ArrayList permissionList)
    {
        Log.info(" *********  Start frontend Assertion  ********");
        Log.info("Expected massage = "+ frontEndModel.getExpectedMessage());
        Assert.assertEquals(actualResults,frontEndModel.getExpectedMessage() , GeneralConstants.Expected_ERR_MSG);
        Log.info(" *********  Frontend Assertion passed successfully ********");

        //Backend verification. Assert that all data inserted in screen are the same inserted to corresponding DB columns
        SoftAssert softAssert = new SoftAssert();
        ArrayList<RolesDM> rolesInDB = backendService.getRolesDetails(frontEndModel.getEnglishName());

        // if program was found in DB, Use soft assert to validate all data and identify all failures if exist
        if(rolesInDB != null) {
            softAssert.assertEquals(rolesInDB.get(0).getEnglishName(), frontEndModel.getEnglishName(), RolesDBTable.TABLE_NAME + "." + RolesDBTable.NAME_EN + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(rolesInDB.get(0).getArabicName(), frontEndModel.getArabicName(), RolesDBTable.TABLE_NAME + "." + RolesDBTable.NAME_AR + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(rolesInDB.get(0).getDescription(), frontEndModel.getDescription(), RolesDBTable.TABLE_NAME + "." + RolesDBTable.DESCRIPTION + GeneralConstants.MISMATCH_ERR_MSG);

            if(rolesInDB.size() == permissionList.size())
            {
                for (int i = 0 ; i < permissionList.size() ; i++ )
                {
                    if(!permissionList.contains(rolesInDB.get(i).getPermissions()))
                    {
                        softAssert.fail("Permission with NAME_EN = "+rolesInDB.get(i).getPermissions()+" not found in frontend Model");
                    }
                }
            }
            softAssert.assertAll();
            Log.info(" *********  Backend Assertion passed successfully ********");
        }
        else {
            Log.info(" *********  Backend Assertion Failed as Role  : " + frontEndModel.getEnglishName() + " was not found in DB ********");
            Assert.fail("DB error occurred or Role was not found in DB");
        }

    }

}
