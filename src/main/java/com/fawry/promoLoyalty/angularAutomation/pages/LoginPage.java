package com.fawry.promoLoyalty.angularAutomation.pages;

import com.fawry.promoLoyalty.angularAutomation.utils.Log;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.fawry.promoLoyalty.angularAutomation.constants.GeneralConstants;
import com.fawry.promoLoyalty.angularAutomation.dataModels.LoginDM;


public class LoginPage extends MainPage {

	public LoginPage(WebDriver driver, NgWebDriver ngWebDriver)
	{
		super(driver, ngWebDriver);
	}

// Initialize page's web elements
	@FindBy(linkText="Login")
	WebElement loginLink;

	@FindBy(id="userId")
	WebElement userMailOrMobileText;

	@FindBy(id="loginPassInput")
	WebElement passwordText;

	@FindBy(css="button#loginBtnLink")
	WebElement loginButton;

	@FindBy(id="loginForgetLink")
	WebElement forgotPasswordLnk;
	



// list page's actions
	
	// login action
	public String login(LoginDM loginObj)
	{
		String errorsMessage = "";
		try
		{
			loginLink.click();
			userMailOrMobileText.sendKeys(loginObj.getUserMail());
			passwordText.click();
			passwordText.sendKeys(loginObj.getPassword());
			loginButton.click();

			//Check if there is any error message displayed due to missing required or invalid data
			if (!loginObj.getExpectedMessage().trim().equalsIgnoreCase(GeneralConstants.SUCCESS) && !loginObj.getErrType().trim().isEmpty())
			{
				errorsMessage = getAllErrMsgs(loginObj.getErrType().trim());
				//In case user didn't login successfully, return all displayed error messages in one string separated by #
				return errorsMessage;
			}
		}
		catch(Exception e)
		{
			Log.error("Error occured in " + new Object() {}
					.getClass().getName() + "." + new Object() {}
				.getClass()
				.getEnclosingMethod()
				.getName() + " for testcase *** " +  loginObj.getTestCaseTitle() + " ***", e);
			return GeneralConstants.FAILED;
		}

		// else it the case that user logged in successfully then return success
		return GeneralConstants.SUCCESS;
	
	}

	// login successfully to the app to be able to navigate to application's page and perform tests
	public String loginSuccessfully(String userMail, String password)
	{
		try {
			loginLink.click();
			userMailOrMobileText.sendKeys(userMail);
			passwordText.click();
			passwordText.sendKeys(password);
			loginButton.click();
		}
		catch(Exception e)
		{
			Log.fatal("BLOCKING ISSUE - CAN NOT LOGIN TO APPLIATION");
			Log.error("Error occured While logging in " + new Object() {}
					.getClass().getName() + "." + new Object() {}
					.getClass()
					.getEnclosingMethod()
					.getName(), e);
			// User could not login to application. This is a blocking issue
			return GeneralConstants.FAILED;
		}
		// else it the case that user logged in successfully then return success
		return GeneralConstants.SUCCESS;
	}

	


}