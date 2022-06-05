package com.fawry.userManagment.angularAutomation.pages;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.utils.Log;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class HomePage extends MainPage{

    //invoke parent's constructor
    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Initialize web elements
    @FindBy(tagName = "small")
    WebElement welcomeMsg;
    @FindBy(xpath = "//button/span[contains(text(), 'Sign out')]")
    WebElement signOutBtn;


    @FindBy(xpath = "//span[contains(text(),'Manage Users')]")
    WebElement mangeUsersDropdownMenu;

    @FindBy(xpath = "//*[@href='/user-manage-v2/users']")
    WebElement viewUsersMenuItem;

    @FindBy(xpath = "//*[@href='/user-manage-v2/users/add']")
    WebElement addUsersMenuItem;


    @FindBy(xpath = "//span[contains(text(),'Manage Roles')]")
    WebElement mangeRolesDropdownMenu;

    @FindBy(xpath = "//*[@href='/user-manage-v2/roles']")
    WebElement viewRolesMenuItem;

    @FindBy(xpath = "//*[@href='/user-manage-v2/roles/add']")
    WebElement addRolesMenuItem;


//    list page's actions

    //get welcome message
    public String getWelcomeMsg() {
        String welcomeMsgStr = "";
        try {
            welcomeMsgStr = welcomeMsg.getText();
        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return GeneralConstants.FAILED;
        }
        return welcomeMsgStr;
    }

    public String logout() {
        try {
            welcomeMsg.click();
            signOutBtn.click();
        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
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


    public void selectMangeUsersMenuLink() throws InterruptedException {
        scrollIntoView(mangeUsersDropdownMenu);
        if (!mangeUsersDropdownMenu.getAttribute("class").equalsIgnoreCase("p-menuitem-active"))
            mangeUsersDropdownMenu.click();
    }

    public String navigateToViewUsersPage() {
        try {
            Log.info("Navigate to View Users page");
           /* if (!viewUsersMenuItem.getAttribute("class").contains("p-menuitem-active")) {
                selectBusinessEntityMenuLink();
                wait.until(ExpectedConditions.elementToBeClickable(viewBusinessEntityMenuItem));*/
            selectMangeUsersMenuLink();
            viewUsersMenuItem.sendKeys(Keys.RETURN);

        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
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

    public String navigateToAddUsersPage() {
        try {
            Log.info("Navigate to Add Users Page");
            selectMangeUsersMenuLink();
            addUsersMenuItem.click();
        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
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

    public void selectURolesManagementMenuLink() {
        if (!mangeRolesDropdownMenu.getAttribute("class").equalsIgnoreCase("p-menuitem-active"))
            mangeRolesDropdownMenu.click();
    }

    public String navigateToViewRolesPage() {
        try {
            Log.info("Navigate to View Roles page");
            selectURolesManagementMenuLink();
            viewRolesMenuItem.sendKeys(Keys.RETURN);
        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
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

    public String navigateToAddRolesPage() {
        try {
            Log.info("Navigate to Add Roles Page");
            selectURolesManagementMenuLink();
            addRolesMenuItem.click();
        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
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
