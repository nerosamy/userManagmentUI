package com.fawry.promoLoyalty.angularAutomation.pages;

import com.fawry.promoLoyalty.angularAutomation.constants.GeneralConstants;
import com.fawry.promoLoyalty.angularAutomation.utils.Log;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class HomePage extends MainPage{
    //invoke parent's constructor
    public HomePage(WebDriver driver, NgWebDriver ngWebDriver)
    {
        super(driver, ngWebDriver);
    }

    // Initialize web elements
    @FindBy(tagName= "small")
    WebElement welcomeMsg;

    @FindBy(id= "dropdownMenu2")
    WebElement logoutDropDown;

    @FindBy(xpath= "//button/span[contains(text(), 'Sign out')]")
    WebElement signOutBtn;

    @FindBy(id= "LoyaltyLink")
    public WebElement loyaltySystemsMenuItem;

    @FindBy(id= "loyaltyPrograms")
    public WebElement loyaltyProgramsMenuItem;

    @FindBy(id= "loyaltyProgramsPortalConfiguration")
    public WebElement loyaltyPortalConfigMenuItem;

    @FindBy(id= "loyaltyProgramPartnersLink")
    public WebElement loyaltyProgPartnersMenuItem;

    @FindBy(id= "loyaltyProgramInputsConfiguration")
    public WebElement loyaltyProgInputConfigMenuItem;

    @FindBy(id= "loyaltyProgramCategoriesLink")
    public WebElement loyaltyCategoriesMenuItem;

    @FindBy(id= "loyaltyProgramRulesLink")
    public WebElement loyaltyProgRulesMenuItem;

    @FindBy(linkText= "Program Transactions")
    public WebElement loyaltyProgTransMenuItem;

    @FindBy(id= "loyaltyProgramCustomersLink")
    public WebElement loyaltyCustomersMenuItem;

    @FindBy(id= "loyaltyExternalProgramsLink")
    public WebElement loyaltyExternalProgsMenuItem;

    @FindBy(id= "loyaltyoffersLink")
    public WebElement loyaltyOffersMenuItem;

    @FindBy(linkText= "Loyalty Reports")
    public WebElement loyaltyReportsMenuItem;

    @FindBy(id= "loyaltyReportsEarningLink")
    public WebElement loyaltyEarningReportsMenuItem;

    @FindBy(id= "loyaltyReportsBurningLink")
    public WebElement loyaltyBurningReportsMenuItem;




//    list page's actions

    //get welcome message
    public String getWelcomeMsg()
    {
        String welocmeMsg = "";
        try {
             welocmeMsg = welcomeMsg.getText();
        }
        catch (Exception e)
        {
            Log.error("Error occured in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return welocmeMsg;
    }

    public String logout()
    {
        try {
            logoutDropDown.click( );
            signOutBtn.click();
        }
        catch (Exception e)
        {
            Log.error("Error occured in " + new Object() {}
                    .getClass().getName() + "." + new Object() {}
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }


    public void selectLoyaltySystemsMenuLink()
    {
        if(loyaltySystemsMenuItem.getAttribute("aria-expanded").equalsIgnoreCase("false"))
            loyaltySystemsMenuItem.sendKeys(Keys.RETURN);
    }

    public String navigateToLoyaltyPrograms()
    {
        try
        {
            Log.info("Navigate to Loyalty programs list page" );
            selectLoyaltySystemsMenuLink();
            loyaltyProgramsMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyPortalConfigs()
    {
        try {
            Log.info("Navigate to Loyalty Portal Configs page" );
            selectLoyaltySystemsMenuLink();
            loyaltyPortalConfigMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyProgramPartners()
    {
        try {
            Log.info("Navigate to Loyalty Program Partners page" );
            selectLoyaltySystemsMenuLink();
            loyaltyProgPartnersMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyProgramInputConfig()
    {
        try {
            Log.info("Navigate to Loyalty Program Input Config page" );
            selectLoyaltySystemsMenuLink();
            loyaltyProgInputConfigMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyCategories()
    {
        try {
            Log.info("Navigate to Loyalty Categories page" );
            selectLoyaltySystemsMenuLink();
            loyaltyCategoriesMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyProgramRules()
    {
        try {
            Log.info("Navigate to Loyalty Program Rules page" );
            selectLoyaltySystemsMenuLink();
            loyaltyProgRulesMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyProgramTrans()
    {
        try {
            Log.info("Navigate to Loyalty Program Transactions page" );
            selectLoyaltySystemsMenuLink();
            loyaltyProgTransMenuItem.click();
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyCustomers()
    {
        try {
            Log.info("Navigate to Loyalty Customers page" );
            selectLoyaltySystemsMenuLink();
            loyaltyCustomersMenuItem.click();
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyExtLoyaltyProgs()
    {
        try {
            Log.info("Navigate to Loyalty external loyalty programs page" );
            selectLoyaltySystemsMenuLink();
            loyaltyExternalProgsMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyOffers()
    {
        try {
            Log.info("Navigate to Loyalty Offers page" );
            selectLoyaltySystemsMenuLink();
            loyaltyOffersMenuItem.click();
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyEarningReports()
    {
        try {
            Log.info("Navigate to Loyalty Earning reports page" );
            selectLoyaltySystemsMenuLink();
            loyaltyReportsMenuItem.click();
            loyaltyEarningReportsMenuItem.click();
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }

    public String navigateToLoyaltyBurningReports()
    {
        try {
            Log.info("Navigate to Loyalty Burning reports page" );
            selectLoyaltySystemsMenuLink();
            loyaltyReportsMenuItem.click();
            loyaltyBurningReportsMenuItem.click();
        } catch (Exception e) {
            Log.error("Error occured in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return GeneralConstants.SUCCESS;
    }



}
