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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddUserPage extends MainPage{

    public AddUserPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath =  "//input[@name='email']")
    WebElement emailTextBox;

    @FindBy(id = "phone")
    WebElement phoneTextBox;

    @FindBy(xpath =  "//p-multiselect[@name='roles']")
    WebElement rolesDropDown;

    @FindBy(xpath =  "//p-multiselectitem//li")
    List<WebElement> rolesDropDownOptions;

    @FindBy(xpath =  "//p-dropdown[@name='branch']")
    WebElement branchDropDown;

    @FindBy(xpath =  "//p-radiobutton/div")
    List<WebElement> statusRadioButtons;

    @FindBy(xpath =  "//p-radiobutton/div//input")
    List<WebElement> statusText;

    @FindBy(xpath =  "//p-checkbox[@name='mustChangePassword']")
    WebElement mustChangePasswordCheckBox;

    @FindBy(xpath =  "//p-checkbox[@name='passwordNeverExpire']")
    WebElement passwordNeverExpireCheckBox;

    @FindBy(xpath = "//span[contains(text(),'Save')]")
    WebElement saveButton;

    String timeStamp = new SimpleDateFormat("hhmmssss").format(new Date());

    public String setAddUsersDetails(UsersDM usersDM) {
        try {
            Log.info("Setting User details ..............");
            if(usersDM.getEmail() != "")
            {
                usersDM.setEmail(usersDM.getEmail()+timeStamp+"@automation.com");
                setTextValue(emailTextBox , usersDM.getEmail());
            }

            if(usersDM.getPhone() != "")
            {
                usersDM.setPhone(usersDM.getPhone()+timeStamp);
                setTextValue(phoneTextBox , usersDM.getPhone());
            }

            if(!usersDM.getRole().equals("")) {
                rolesDropDown.click();
                usersDM.setRole(rolesDropDownOptions.get(0).getText());
                rolesDropDownOptions.get(0).click();
            }

            usersDM.setBranch(selectOptionByindex(branchDropDown , "0"));

            System.out.println("//input[@value='"+usersDM.getStatus()+"']/ancestor::p-radiobutton");
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
            Log.info("Edit user's data ......");
            if (usersDM.getRole() != "") {
                ArrayList<String> selectedOptions = unselectAllOptions();
                rolesDropDown.click();
                for (int i = 0; i< rolesDropDownOptions.size() ; i++)
                {
                    if (!selectedOptions.contains(rolesDropDownOptions.get(i).getText()))
                    {
                        usersDM.setRole(rolesDropDownOptions.get(i).getText());
                        rolesDropDownOptions.get(i).click();
                        break;
                    }
                }
            }

            usersDM.setBranch(selectAnotherOption(branchDropDown , usersDM.getBranch()));

          if (usersDM.getStatus() != "") {
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
                WebElement mustChangePasswordFlag = mustChangePasswordCheckBox.findElement(By.xpath("//div"));
                mustChangePasswordCheckBox.click();
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
                WebElement passwordNeverExpireFlag = passwordNeverExpireCheckBox.findElement(By.xpath("//div"));
                passwordNeverExpireCheckBox.click();
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

    public String clickSaveButton() {
        try {
            Log.info("click save button");
            scrollIntoView(saveButton);
            saveButton.click();
            Thread.sleep(2000);
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
              if (rolesDropDownOptions.get(i).getAttribute("class").contains("p-highlight"))
              {
                  selectedOptions.add(rolesDropDownOptions.get(i).getText());
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
