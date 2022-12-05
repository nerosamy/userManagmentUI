package com.fawry.userManagment.angularAutomation.tests;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.constants.database.RolesDBTable;
import com.fawry.userManagment.angularAutomation.constants.excelIndices.ListRolesIndices;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.pages.AddRolePage;
import com.fawry.userManagment.angularAutomation.pages.ViewRolesPage;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ViewRolesTest extends BaseTest{

    ArrayList listRolesTestData = new ArrayList();

    @Parameters("url")
    @BeforeMethod(alwaysRun = true)
    public void navigateToViewRolePage(String url) {
        Assert.assertEquals(homepage.getWelcomeMsg(), "Welcome", "BLOCKING ISSUE - CAN NOT LOGIN TO APPLICATION");
        String[] splitURL = url.split("/");
        String newURL = splitURL[0]+"//"+splitURL[2]+"/user-manage-v2/users";
        driver.navigate().to(newURL);
        String navigatedToPageSuccessfully = homepage.navigateToViewRolesPage();
        Assert.assertEquals(navigatedToPageSuccessfully, GeneralConstants.SUCCESS, "Could not navigate to View Roles page successfully");
    }

    @Test(description = "Validate List Roles functionalities",priority = 2 ,dataProvider = "listRolesDP" ,enabled = true)
    public void listRoles(RolesDM rolesDM) {
        //Create extent test to be logged in report using test case title
        test = extent.createTest(rolesDM.getTestCaseId() + " --- " + rolesDM.getTestCaseTitle());
        Log.test = test;
        Log.startTestCase(rolesDM.getTestCaseId() + " --- " + rolesDM.getTestCaseTitle());
        ViewRolesPage viewRolesPage = new ViewRolesPage(driver);
        ArrayList<RolesDM> frontendDM = viewRolesPage.searchRoles(rolesDM);
        Assert.assertEquals(viewRolesPage.actual,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While search role and store result in ArrayList.");


        Log.info(" *********  Start backend Assertion  ********");

        //Backend verification. Assert that all data inserted in screen are the same inserted to corresponding DB columns
        SoftAssert softAssert = new SoftAssert();
        ArrayList<RolesDM> backendDM = backendService.getRolesList(rolesDM);

        // if program was found in DB, Use soft assert to validate all data and identify all failures if exist
        if(backendDM != null) {

            for (int i = 0 ; i < backendDM.size() ; i++)
            {
                boolean flag = false;

                for(int j = 0 ; j < frontendDM.size() ; j++)
                {
                    if(backendDM.get(i).getEnglishName().trim().equalsIgnoreCase(frontendDM.get(j).getEnglishName().trim())
                            && backendDM.get(i).getDescription().trim().equalsIgnoreCase(frontendDM.get(j).getDescription().trim()))
                    {
                        softAssert.assertTrue(true);
                        flag = true;
                        break;
                    }

                }
                if(!flag)
                {
                    softAssert.fail(" Role :" +backendDM.get(i).getEnglishName().trim()+" found in DB and not listed in UI");
                }
            }
            softAssert.assertAll();
            Log.info(" *********  Backend Assertion passed successfully ********");
        }
    }


    @Test(description = "Validate edit role functionality",priority = 3 ,enabled = true)
    public void editRole() {
        //Create extent test to be logged in report using test case title
        test = extent.createTest("TC-22 --- Validate edit role functionality");
        Log.test = test;
        Log.startTestCase("TC-22 --- Validate edit role functionality");
        ViewRolesPage viewRolesPage = new ViewRolesPage(driver);
        RolesDM rolesDM= new RolesDM();
        String roleEnName = viewRolesPage.clickEditRoleButton();
        Assert.assertNotEquals(roleEnName,GeneralConstants.FAILED,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While get role name from GUI and Click edit Button.");
        rolesDM.setEnglishName(roleEnName);
        AddRolePage addRolePage = new AddRolePage(driver);
        String actualResult = addRolePage.editRole(rolesDM);
        Assert.assertEquals(actualResult,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While editing role data.");
        actualResult= addRolePage.clickSaveButton();
        Assert.assertEquals(actualResult,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While click save Button.");
        actualResult = addRolePage.getAllErrMsgs(GeneralConstants.ERR_TYPE_NOTIFICATION);
        Log.info(" *********  Start frontend Assertion  ********");
        Log.info("Expected massage = "+ GeneralConstants.SUCCESS_MASSAGE);
        Assert.assertEquals(actualResult,GeneralConstants.SUCCESS_MASSAGE , GeneralConstants.Expected_ERR_MSG);
        Log.info(" *********  Frontend Assertion passed successfully ********");

        Log.info(" *********  Start BackEnd Assertion  ********");

        //Backend verification. Assert that all data inserted in screen are the same inserted to corresponding DB columns
        SoftAssert softAssert = new SoftAssert();
        ArrayList<RolesDM> backendDM = backendService.getRolesDetails(rolesDM.getEnglishName());

        // if program was found in DB, Use soft assert to validate all data and identify all failures if exist
        if(backendDM != null) {
            softAssert.assertEquals(backendDM.get(0).getEnglishName(), rolesDM.getEnglishName(), RolesDBTable.TABLE_NAME + "." + RolesDBTable.NAME_EN + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendDM.get(0).getArabicName(), rolesDM.getArabicName(), RolesDBTable.TABLE_NAME + "." + RolesDBTable.NAME_AR + GeneralConstants.MISMATCH_ERR_MSG);
            softAssert.assertEquals(backendDM.get(0).getDescription(), rolesDM.getDescription(), RolesDBTable.TABLE_NAME + "." + RolesDBTable.DESCRIPTION + GeneralConstants.MISMATCH_ERR_MSG);

        }
        softAssert.assertAll();
        Log.info(" *********  Backend Assertion passed successfully ********");
    }

    @Test(description = "Validate delete Un-assigned Role functionality",priority = 0 ,enabled = true)
    public void deleteUnassignedRole() throws InterruptedException {
        //Create extent test to be logged in report using test case title
        test = extent.createTest("TC-23 --- Validate delete Un-assigned Role functionality");
        Log.test = test;
        Log.startTestCase("TC-23 --- Validate delete Un-assigned Role functionality");

        String roleName = backendService.getNotAssignedRoleName();
        Assert.assertNotEquals(roleName,"","There is no un-assigned role found in DB.");

        ViewRolesPage viewRolesPage = new ViewRolesPage(driver);
        String actualResults = viewRolesPage.searchRoleByName(roleName);
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While search role by name : "+roleName);

        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        actualResults = viewRolesPage.clickRemoveRoleButton();
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While click remove button.");

        actualResults = viewRolesPage.getAllErrMsgs(GeneralConstants.ERR_TYPE_NOTIFICATION);

        Log.info(" *********  Start frontend Assertion  ********");
        Log.info("Expected massage = "+ GeneralConstants.SUCCESS_MASSAGE);
        Assert.assertTrue(actualResults.contains(GeneralConstants.SUCCESS_MASSAGE) , GeneralConstants.Expected_ERR_MSG);
        Log.info(" *********  Frontend Assertion passed successfully ********");

        Log.info(" *********  Start BackEnd Assertion  ********");

        //Backend verification.
        SoftAssert softAssert = new SoftAssert();
        String roleStatus = backendService.getRoleStatus(roleName);

        softAssert.assertEquals(roleStatus, "0", RolesDBTable.TABLE_NAME + "." + RolesDBTable.STATUS + GeneralConstants.MISMATCH_ERR_MSG);

       softAssert.assertAll();
        Log.info(" *********  Backend Assertion passed successfully ********");
    }

    @Test(description = "Validate delete assigned Role functionality",priority = 1 ,enabled = true)
    public void deleteAssignedRole() {
        //Create extent test to be logged in report using test case title
        test = extent.createTest("TC-24 --- Validate delete assigned Role functionality");
        Log.test = test;
        Log.startTestCase("TC-24 --- Validate delete assigned Role functionality");

        String roleName = backendService.getAssignedRoleName();
        Assert.assertNotEquals(roleName,"","There is no Assigned role found in DB.");

        ViewRolesPage viewRolesPage = new ViewRolesPage(driver);
        String actualResults = viewRolesPage.searchRoleByName(roleName);
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While search role by name : "+roleName);

        actualResults = viewRolesPage.clickRemoveRoleButton();
        Assert.assertEquals(actualResults,GeneralConstants.SUCCESS,GeneralConstants.POM_EXCEPTION_ERR_MSG +" While click remove button.");

        actualResults = viewRolesPage.getAllErrMsgs(GeneralConstants.ERR_TYPE_NOTIFICATION);

        Log.info(" *********  Start frontend Assertion  ********");
        Log.info("Expected massage = "+ GeneralConstants.DELETE_ERROR_MASSAGE);
        Assert.assertTrue(actualResults.contains(GeneralConstants.SUCCESS_MASSAGE) , GeneralConstants.Expected_ERR_MSG);
        Log.info(" *********  Frontend Assertion passed successfully ********");

    }

    @DataProvider(name = "listRolesDP")
    public Object[][] provideUpdateBusinessEntityTD() {
        Object[][] listRolesDP = new Object[listRolesTestData.size()][1];
        for (int i = 0; i < listRolesTestData.size(); i++)
            listRolesDP[i][0] = listRolesTestData.get(i);

        return listRolesDP;
    }

    @BeforeClass
    public void listRolesTD() {

        ArrayList<ArrayList<Object>> resultArray = provideTestData("listRoles");

        for (int i = 0; i < resultArray.size(); i++) {
            RolesDM rolesDM = new RolesDM();

            // fill Customer data model whether for add or update test
            rolesDM.setTestCaseId(resultArray.get(i).get(ListRolesIndices.TEST_CASE_ID_INDEX).toString());
            rolesDM.setTestCaseTitle(resultArray.get(i).get(ListRolesIndices.TEST_CASE_TITLE_INDEX).toString());
            rolesDM.setTestScope(resultArray.get(i).get(ListRolesIndices.TEST_SCOPE_INDEX).toString());
            rolesDM.setEnglishName(resultArray.get(i).get(ListRolesIndices.ROLE_NAME).toString());
            rolesDM.setPermissions(resultArray.get(i).get(ListRolesIndices.PERMISSION).toString());


            listRolesTestData.add(rolesDM);

        }

    }

}
