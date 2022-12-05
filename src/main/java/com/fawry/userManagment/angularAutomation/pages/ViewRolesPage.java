package com.fawry.userManagment.angularAutomation.pages;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ViewRolesPage extends MainPage{
    public ViewRolesPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(name="roleName")
    WebElement roleNameTextInput;


    @FindBy(name="permissions")
    WebElement permissionsDropDown;

    @FindBy(xpath = "//p-multiselectitem//li")
    List<WebElement> permissionList;

    @FindBy(xpath = "//span[contains(text(),'Search')]")
    WebElement searchBtn;

    @FindBy(xpath = "//tbody//tr")
    List<WebElement> searchResultsRows;

    @FindBy(xpath = "//*[contains(@class,'p-paginator-next')]")
    WebElement nextPageButton;

    @FindBy(xpath = "//h3[contains(text(),'There is no data')]")
    List<WebElement> noDataMassage;

    @FindBy(xpath = "//button[@icon='pi pi-pencil']")
    List<WebElement> editRoleButton;

    @FindBy(xpath = "//button[@icon='pi pi-trash']")
    List<WebElement> deleteRoleButton;

    @FindBy(xpath = "//span[contains(text(),'Yes')]")
    WebElement yesButton;

    public String actual;

    public ArrayList<RolesDM> searchRoles(RolesDM searchDM) {
        ArrayList<RolesDM> rolesDMS= new ArrayList<>();
        RolesDM rolesDM;

        try {

            setTextValue(roleNameTextInput, searchDM.getEnglishName());
            if(!searchDM.getPermissions().equals("")) {
                permissionsDropDown.click();
                searchDM.setPermissions(permissionList.get(0).getText());
                permissionList.get(0).click();
            }
            searchBtn.click();

            if (noDataMassage.size() != 0) {
                actual = GeneralConstants.SUCCESS;
                return rolesDMS;
            } else {
                while (true) {
                    for (WebElement searchResultsRow : searchResultsRows) {
                        rolesDM = new RolesDM();
                        List<WebElement> rows = searchResultsRow.findElements(By.tagName("td"));
                        rolesDM.setEnglishName(rows.get(0).getText());
                        rolesDM.setDescription(rows.get(1).getText());

                        rolesDMS.add(rolesDM);

                    }
                    if (!nextPageButton.isEnabled())
                        break;
                    scrollIntoView(nextPageButton);
                    nextPageButton.click();
                    scrollUp();
                }
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
        return rolesDMS;

    }

    public String searchRoleByName(String roleName)
    {
        try {
            Log.info("Search Roles by name : "+roleName);
            setTextValue(roleNameTextInput, roleName);
            searchBtn.click();
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


    public String clickEditRoleButton()
    {
        String roleName = null;
        try {
            Log.info("Click edit button");
            Thread.sleep(500);
            roleName = searchResultsRows.get(0).findElements(By.tagName("td")).get(0).getText();
            editRoleButton.get(0).click();
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
        return roleName;
    }

    public String clickRemoveRoleButton()
    {
        try {
            Log.info("Click remove button");
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            deleteRoleButton.get(0).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
        return GeneralConstants.SUCCESS;
    }




}
