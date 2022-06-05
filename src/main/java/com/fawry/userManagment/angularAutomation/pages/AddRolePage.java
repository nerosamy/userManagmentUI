package com.fawry.userManagment.angularAutomation.pages;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddRolePage extends MainPage{
    public AddRolePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(name="roleNameEn")
    WebElement roleNameEnTextBox;

    @FindBy(name="roleNameAr")
    WebElement roleNameArTextBox;

    @FindBy(name="desc")
    WebElement descriptionTextBox;

    @FindBy(xpath = "//*[@class='p-inputswitch-slider']")
   List<WebElement> permissionsRadioButtons;

    @FindBy(xpath="//*[@class='p-datatable-tbody']//tr//td[1]")
    List<WebElement> permissionsText;

    @FindBy(xpath = "//span[contains(text(),'Save')]")
    WebElement saveButton;

    String timeStamp = new SimpleDateFormat("hhmmssss").format(new Date());


    public String setRoleDetails(RolesDM rolesDM) {
        try {

            Log.info("Setting role details ........................ ");
            rolesDM.setEnglishName("RoleEnglishName" + timeStamp );
            rolesDM.setArabicName("RoleArabicName" + timeStamp );
            rolesDM.setDescription("RoleDescription" + timeStamp );

            setTextValue( roleNameEnTextBox , rolesDM.getEnglishName());
            setTextValue( roleNameArTextBox , rolesDM.getArabicName());
            setTextValue( descriptionTextBox , rolesDM.getDescription());

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

    public ArrayList permissionName;

    public String selectAllPermissions() {
        try {
            Log.info("Selecting all permissions from GUI ");
            int size = permissionsRadioButtons.size();
            permissionName = new ArrayList<>();
            for (int i = 0 ; i < size ; i++ )
            {
                scrollIntoView(permissionsRadioButtons.get(i));
                permissionsRadioButtons.get(i).click();
                permissionName.add(permissionsText.get(i).getText());
            }
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

    public String selectSpecificPermissions() {
        try {
            Log.info("Selecting Specific permission from GUI ");
            permissionName = new ArrayList<>();
            permissionsRadioButtons.get(0).click();
            permissionName.add(permissionsText.get(0).getText());

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

    public String clickSaveButton() {
        try {
            Log.info("click save button");
            scrollIntoView(saveButton);
            saveButton.click();

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

    public String editRole(RolesDM rolesDM) {
        try {

            Log.info("Edit role data ........................ ");
            rolesDM.setArabicName("UpdateRoleArName" + timeStamp );
            rolesDM.setDescription("UpdateRoleDescription" + timeStamp );

            setTextValue( roleNameArTextBox , rolesDM.getArabicName());
            setTextValue( descriptionTextBox , rolesDM.getDescription());

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
