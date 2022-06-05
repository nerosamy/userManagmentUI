package com.fawry.userManagment.angularAutomation.tests;

import com.fawry.userManagment.angularAutomation.pages.LoginPage;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import java.lang.reflect.Method;

import org.testng.Assert;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.dataModels.LoginDM;


public class LoginTests extends BaseTest{

	@BeforeMethod(alwaysRun = true)
	@Parameters("url")
	public void goToLoginPage(String url)
	{
		driver.get(url);
	}
		  
  @Test(description="Validate Login Functionality", dataProvider="loginTDProvider")
	public void login(LoginDM loginObject)
  {
  	//Create extent test to be logged in report using test case title
	  test = extent.createTest(loginObject.getTestCaseTitle());
	  Log.test = test;
	  Log.startTestCase(loginObject.getTestCaseTitle());

	  // initialize an object of login page
	  LoginPage loginPage = new LoginPage(driver);

	  // call login method and capture the displayed message on screen
	  String actual= loginPage.login(loginObject);


	  ///Happy scenario verifications. Check that browser is navigated to home page
	  if(actual.equals(GeneralConstants.SUCCESS) && loginObject.getExpectedMessage().trim().equalsIgnoreCase(GeneralConstants.SUCCESS))
			Assert.assertEquals(homepage.getWelcomeMsg(),"Welcome", loginObject.getTestCaseTitle() + " " + GeneralConstants.FAILED);
			//Assert.assertTrue(driver.getCurrentUrl().contains("dashboard") , loginObject.getTestCaseTitle() + " " + GeneralConstants.FAILED);

	  // Test Failed due to an exception occured in POM's method
	  else if(actual.equalsIgnoreCase(GeneralConstants.FAILED))
		  Assert.assertEquals(actual, loginObject.getExpectedMessage(), GeneralConstants.POM_EXCEPTION_ERR_MSG);

	  //Negative scenarios verifications on validation error messages
	  else
			Assert.assertEquals(actual.toLowerCase(), loginObject.getExpectedMessage().trim().toLowerCase() , loginObject.getTestCaseTitle() + " " + GeneralConstants.FAILED);

	}


	@DataProvider(name = "loginTDProvider")
	public Object[][] provideLoginTD(Method method)
	{
		Object[][] result = getTestDataFromExtSource(method.getName(), LoginDM.class);
		return result;


//		ArrayList<ArrayList<Object>> resultArray = provideTestData(method.getName());
//		Object[][] result = new Object[resultArray.size()][1];
//
//		for(int i=0; i<resultArray.size(); i++)
//		{
//			LoginDM loginTestData = new LoginDM();
//			loginTestData.setTestCaseTitle(resultArray.get(i).get(LoginExcelIndices.TEST_CASE_TITLE_INDEX).toString());
//			loginTestData.setUserMail(resultArray.get(i).get(LoginExcelIndices.USER_MAIL_INDEX).toString());
//			loginTestData.setPassword(resultArray.get(i).get(LoginExcelIndices.USER_PASSWORD_INDEX).toString());
//			loginTestData.setErrType(resultArray.get(i).get(LoginExcelIndices.ERR_TYPE_INDEX).toString());
//			loginTestData.setExpectedMessage(resultArray.get(i).get(LoginExcelIndices.EXPECTED_RES_INDEX).toString());
//			result[i][0] = loginTestData;
//		}

  }
	}


