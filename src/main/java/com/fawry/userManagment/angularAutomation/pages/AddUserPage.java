package com.fawry.userManagment.angularAutomation.pages;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.dataModels.RolesDM;
import com.fawry.userManagment.angularAutomation.dataModels.UsersDM;
import com.fawry.userManagment.angularAutomation.utils.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddUserPage extends MainPage{

    public AddUserPage(WebDriver driver) {
        super(driver);
    }

//    @FindBy(xpath =  "//input[@name='email']")
    @FindBy(id = "users_addUser_email")
    WebElement emailTextBox;


    @FindBy(id = "users_addUser_name")
    WebElement nameTextBox;

    @FindBy(id = "phone")
    WebElement phoneTextBox;

//    @FindBy(xpath =  "//p-multiselect[@name='roles']")
    @FindBy(id ="users_addUser_role")
    WebElement rolesDropDown;

    @FindBy(xpath =  "//p-multiselectitem//li")
    List<WebElement> rolesDropDownOptions;

//    @FindBy(xpath =  "//p-dropdown[@name='branch']")
            @FindBy(id ="users_addUser_branch")
    WebElement branchDropDown;

//    @FindBy(xpath =  "//p-radiobutton/div")
 @ FindBy(id="users_addUser_status")
    List<WebElement> statusRadioButtons;

//    @FindBy(xpath =  "//p-radiobutton/div//input")
//p-radiobutton[@id="users_addUser_status"]/div//input
@FindBy(xpath =  "//p-radiobutton[@id='users_addUser_status']/div//input")

List<WebElement> statusText;

//    @FindBy(xpath =  "//p-checkbox[@name='mustChangePassword']")
@FindBy(id = "users_addUser_mustChangePassword")
    WebElement mustChangePasswordCheckBox;

//    @FindBy(xpath =  "//p-checkbox[@name='passwordNeverExpire']")
@FindBy(id = "users_addUser_passwordNeverExpire")
    WebElement passwordNeverExpireCheckBox;

    @FindBy(xpath =  "//*[contains(@role,'checkbox')]")
    WebElement selectAllRole;
//    @FindBy(xpath = "//span[contains(text(),'Save')]")
@FindBy(id = "users_addUser_saveRole")
    WebElement saveButton;

    String timeStamp = new SimpleDateFormat("hhmmssss").format(new Date());
//    JSWaiter jSWaiter = new JSWaiter();
    public String setAddUsersDetails(UsersDM usersDM) {
        String displayedMsgs;
        try {
            Log.info("Setting User details ..............");
            if(usersDM.getEmail() != "")
            {
                usersDM.setEmail(usersDM.getEmail()+timeStamp+"@automation.com");
                setTextValue(emailTextBox , usersDM.getEmail());
            }


            if(usersDM.getName() != "")
            {
                usersDM.setName(usersDM.getName()+timeStamp);
                setTextValue(nameTextBox , usersDM.getName());
            }


            if(usersDM.getPhone() != "")
            {
                usersDM.setPhone(usersDM.getPhone()+timeStamp);
                setTextValue(phoneTextBox , usersDM.getPhone());
            }

            if(!usersDM.getRole().equals("")) {

                rolesDropDown.click();
                usersDM.setRole(rolesDropDownOptions.get(1).getText());
                rolesDropDownOptions.get(1).click();
                    }

            usersDM.setBranch(selectOptionByindex(branchDropDown , "0"));

            driver.findElement(By.xpath("//input[@value='"+usersDM.getStatus()+"']/ancestor::p-radiobutton")).click();

            mustChangePasswordCheckBox.click();

            if (usersDM.getMustChangePassword().equals("1"))
            {
                mustChangePasswordCheckBox.click();
            }

            if (usersDM.getPasswordNeverExpire().equals("1"))
            {
                passwordNeverExpireCheckBox.click();
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

    public String editUser(UsersDM usersDM) {
        try {
            Log.info("Edit user's data in GUI");
            if (usersDM.getRole() != "") {
                selectRole();

                ArrayList<String> selectedOptions = unselectAllOptions();

                rolesDropDown.click();
                for (int i = 0; i< rolesDropDownOptions.size() ; i++)
                {
                    if (!selectedOptions.contains(rolesDropDownOptions.get(i).getText()))
                    {
                        usersDM.setRole(rolesDropDownOptions.get(i).getText());
                        System.out.println("edit role option" +rolesDropDownOptions);
                        rolesDropDownOptions.get(i).click();
                        break;
                    }
                }
            }
            if (usersDM.getBranch() != "") {
                usersDM.setBranch(selectAnotherOption(branchDropDown, usersDM.getBranch()));
                selectRole();

            }

            if (usersDM.getStatus() != "") {
    selectRole();
                for (int i = 0; i < statusRadioButtons.size(); i++) {
                    if (!statusRadioButtons.get(i).getAttribute("class").contains("p-radiobutton-checked")) {
                        statusRadioButtons.get(i).click();
                        usersDM.setStatus(statusText.get(i).getText());
                        break;
                    }
                }
            }
            if (usersDM.getMustChangePassword() != "")
            {
       selectRole();
                mustChangePasswordCheckBox.click();

               WebElement mustChangePasswordFlag = driver.findElement(By.xpath("//p-checkbox[@name='mustChangePassword']/div"));
//                System.out.println("mustChangePasswordFlag : " +mustChangePasswordFlag.getAttribute("class"));

                if (mustChangePasswordFlag.getAttribute("class").contains("p-checkbox-checked"))
                {
                    usersDM.setMustChangePassword("1");
                }
                else
                {
                    usersDM.setMustChangePassword("0");

                }
            }

            if (usersDM.getPasswordNeverExpire() != "")
            {
 selectRole();
                passwordNeverExpireCheckBox.click();
     WebElement passwordNeverExpireFlag = driver.findElement(By.xpath("//p-checkbox[@name='passwordNeverExpire']/div"));

                System.out.println("getPasswordNeverExpire : " +passwordNeverExpireFlag.getAttribute("class"));
                if (passwordNeverExpireFlag.getAttribute("class").contains("p-checkbox-checked"))
                {
                    usersDM.setPasswordNeverExpire("1");
                }
                else
                {
                    usersDM.setPasswordNeverExpire("0");

                }
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


    public String selectRole() {
try{
        rolesDropDown.click();
        WebDriverWait waitUntilRoleSelected = new WebDriverWait(driver, 10);
        Log.info("Wait until Role Selected");
        waitUntilRoleSelected.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'p-multiselect-items-wrapper')]")));

        if (rolesDropDownOptions.get(3).getAttribute("class").contains("p-highlight"))
            rolesDropDownOptions.get(2).click();
        else
            rolesDropDownOptions.get(3).click();

        waitUntilRoleSelected.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'filled ')]")));
        rolesDropDown.click();
        Log.info(" Role Selected");

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

    public ArrayList<String> unselectAllOptions() {
        ArrayList<String> selectedOptions=new ArrayList<>();
        try {
            rolesDropDown.click();
            int size = rolesDropDownOptions.size();

            for (int i = 0 ; i < size ; i++)
            {

                    if (selectAllRole.getAttribute("class").contains("p-highlight"))

                    {
                    selectedOptions.add(rolesDropDownOptions.get(i).getText());
                    System.out.println("roles option is " + selectedOptions);
                    rolesDropDownOptions.get(i).click();
                }
            }
            rolesDropDown.click();

        } catch (Exception e) {
            Log.error("Error occurred in " + new Object() {
            }
                    .getClass().getName() + "." + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName(), e);
            return null;
        }
        return selectedOptions;
    }
}
