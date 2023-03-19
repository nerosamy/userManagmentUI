package com.fawry.userManagment.angularAutomation.pages;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.dataModels.UsersDM;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class ViewUserPage extends MainPage{
    public ViewUserPage(WebDriver driver) {
        super(driver);
    }


    @FindBy( name="email")
    WebElement emailTextBox;

    @FindBy(name="roleName")
    WebElement roleDropdown;

    @FindBy(name="status")
    WebElement statusDropdown;

//    @FindBy(xpath = "//span[contains(text(),'Search')]")
@FindBy(id ="users_users_search")

    WebElement searchButton;

    @FindBy(xpath = "//*[contains(text(),'No users found')]")
    List<WebElement> noDataMassage;

    //p-table[@id='user-manage-ui-users-table']//tbody//tr
    @FindBy(xpath = "//tbody//tr")
    List<WebElement> searchResultsRows;



   @FindBy(xpath = "//*[contains(@class,'p-paginator-next')]")
    WebElement nextPageButton;

    @FindBy(id = "users_users_edit")
   // @FindBy(xpath = "//span[contains(text(),'Search')]")

    List<WebElement> editUserButton;

//    @FindBy(xpath = "//button[@icon='pi pi-trash']")
    @FindBy(id = "users_users_confirmDelete")
    List<WebElement> deleteUserButton;

    @FindBy(xpath = "//span[contains(text(),'Yes')]")
    WebElement yesButton;

    public String actual;

    public ArrayList<UsersDM> searchUser(UsersDM searchDM) {
        ArrayList<UsersDM> usersDMS= new ArrayList<>();
        Actions action = new Actions(driver);

        UsersDM usersDM;

        try {

            setTextValue(emailTextBox, searchDM.getEmail());
            if (searchDM.getRole() != "")
            {
                searchDM.setRole(selectOptionByindex(roleDropdown, "0"));
            }
            selectOptionByDisplayedText(statusDropdown , searchDM.getStatus());
            searchButton.click();
            Thread.sleep(2000);
            while (true) {
                for (WebElement searchResultsRow : searchResultsRows) {
                    usersDM = new UsersDM();
                    List<WebElement> rows = searchResultsRow.findElements(By.tagName("td"));
                    action.moveToElement(rows.get(1));
                    usersDM.setEmail(rows.get(0).getText());
                    usersDM.setPhone(rows.get(1).getText());
                    usersDM.setStatus(rows.get(2).getText());
                    usersDM.setRole(rows.get(3).getText());

                    usersDMS.add(usersDM);

                }
                if (!nextPageButton.isEnabled())
                    break;
                scrollIntoView(nextPageButton);
                nextPageButton.click();
                scrollUp();
            }

            driver.navigate().refresh();

        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            actual = GeneralConstants.FAILED;
            driver.navigate().refresh();
            return null;

        }

        actual = GeneralConstants.SUCCESS;
        return usersDMS;

    }

    public String clickEditUserButton()
    {

        String UserEmail = "";
        try {
            Log.info("Click edit button");
            Thread.sleep(500);
            driver.navigate().refresh();
            Thread.sleep(2000);
            UserEmail = searchResultsRows.get(0).findElements(By.tagName("td")).get(0).getText();
            editUserButton.get(0).click();
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
        return UserEmail;
    }


    public String clickRemoveUserButton()
    {
        String userEmail = "";
        try {
            Log.info("Click remove button");
            driver.navigate().refresh();
            Thread.sleep(2000);
            userEmail = searchResultsRows.get(0).findElements(By.tagName("td")).get(0).getText();
            deleteUserButton.get(0).click();
            yesButton.click();
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
        return userEmail;
    }


}
