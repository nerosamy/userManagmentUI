package com.fawry.promoLoyalty.angularAutomation.pages;

import com.fawry.promoLoyalty.angularAutomation.constants.GeneralConstants;
import com.fawry.promoLoyalty.angularAutomation.utils.Log;
import com.fawry.promoLoyalty.angularAutomation.utils.PropertiesFilesHandler;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MainPage {

    // Initialize web drivers
    WebDriver driver;
    NgWebDriver ngDriver;
    JavascriptExecutor jsDriver;

    public MainPage(WebDriver driver, NgWebDriver ngDriver) {
        this.driver = driver;
        this.ngDriver = ngDriver;
        this.jsDriver = (JavascriptExecutor)driver;

        //Set a delay of 30 secs to wait for elements' visibility
        AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(driver, 30);
        PageFactory.initElements(factory, this);

        this.waitForPageToLoad();
        ngDriver.waitForAngularRequestsToFinish();

//		PageFactory.initElements(driver, this);

    }

    //locate all mat error messages dispalyed in the page
    @FindBy(tagName = "mat-error")
    public List<WebElement> matErrMsgs;

    // locate all pop-up notification errors
    @FindBy(id="notification-message")
    List<WebElement> notificationMsgs;

    @FindBy(css = "block-ui-content div.block-ui-wrapper")
    WebElement promoLoadImg;

    //Initialize instances of properties files to be used in all pages
    PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);

    // Browser's default download path config from properties file
    String browserDefaultDownloadpath = System.getProperty("user.dir") + generalCofigsProps.getProperty(GeneralConstants.DEFAULT_DOWNLOAD_PATH);

// wait for the loading image to disappear before berforing any acion in the page
    public void waitForPageToLoad()
    {
        WebDriverWait wait = new WebDriverWait (driver, 60);
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(promoLoadImg, "class", "active")));
    }


// Get all error messages whether mat-error or pop-up notification into one string separated with GeneralConstants.STRING_DELIMETER
    public String getAllErrMsgs(String errType) throws InterruptedException {

        String allErrorMsgsString = "";

        if(errType.equalsIgnoreCase(GeneralConstants.ERR_TYPE_PAGE))
        {
            //wait just 3 seconds for all errors to be displayed
            Thread.sleep(300);

            Log.info("Number of displayed mat error messages in page" + matErrMsgs.size());

            for (int i = 0; i < matErrMsgs.size(); i++) {
                if (matErrMsgs.get(i).getText().isEmpty())
                    continue;
                allErrorMsgsString = allErrorMsgsString + matErrMsgs.get(i).getText();
                if (i != matErrMsgs.size() - 1)
                    allErrorMsgsString = allErrorMsgsString + GeneralConstants.STRING_DELIMETER;
            }
        }
        else if(errType.equalsIgnoreCase(GeneralConstants.ERR_TYPE_NOTIFICATION))
        {
            Log.info("Number of displayed pop-up notification error messages in page is :  " + notificationMsgs.size());
            for (int j = 0; j < notificationMsgs.size(); j++) {
                if (notificationMsgs.get(j).getText().isEmpty())
                    continue;
                allErrorMsgsString = allErrorMsgsString + notificationMsgs.get(j).getText();
                if (j != notificationMsgs.size() - 1)
                    allErrorMsgsString = allErrorMsgsString + GeneralConstants.STRING_DELIMETER;
            }
        }
        Log.info("allErrorMsgsString " + allErrorMsgsString);
        return allErrorMsgsString;
}

    public void waitForFileDownload(String expectedFileName) throws IOException {
        FluentWait<WebDriver> wait = new FluentWait(driver)
                .withTimeout(3000, TimeUnit.MILLISECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS);

        File fileToCheck =  new File(expectedFileName);

        wait.until((WebDriver wd) -> fileToCheck.exists());

    }

    // get mat-select's options after locating matselect and click it
    public ArrayList<WebElement> getMatSelectOptions(WebElement matSelect)
    {
        ArrayList<WebElement> selectOptions = new ArrayList<>();
        if(matSelect !=null) {
            matSelect.click();
            ngDriver.waitForAngularRequestsToFinish();
            String selectOptionsIDSString = matSelect.getAttribute("aria-owns");
            System.out.println("matSelect.getAttribute(\"aria-owns\") : " + matSelect.getAttribute("aria-owns"));
            String[] selectOptionsIDs = selectOptionsIDSString.split(" ");
            for (int i = 0; i < selectOptionsIDs.length; i++)
                selectOptions.add(driver.findElement(By.id(selectOptionsIDs[i])));
        }

        return selectOptions;
    }

    // clear a multiple selection mat-select's options by unselecting all options
    public void unselectAllMatSelectOptions(WebElement matSelect)
    {
        String selectOptionsIDSString = matSelect.getAttribute("aria-owns");
        if(selectOptionsIDSString == null) {
            matSelect.click();
            selectOptionsIDSString = matSelect.getAttribute("aria-owns");
        }
        String[] selectOptionsIDs = selectOptionsIDSString.split(" ");
        for(int i=0; i<selectOptionsIDs.length; i++) {
            if (driver.findElement(By.id(selectOptionsIDs[i])).getAttribute("class").contains("mat-selected"))
                driver.findElement(By.id(selectOptionsIDs[i])).click();
        }
    }

    // select mat-select's option(s) by displayed text
    public void  selectOptionByDisplayedText(WebElement matSelect, String displayedText) throws Exception
    {
        if(matSelect != null) {
            // check if matselect is not disabled and selected option(s) are not empty
            if (displayedText != null && !displayedText.trim()
                    .isEmpty()) {
                if (matSelect.getAttribute("aria-disabled")
                        .equalsIgnoreCase(GeneralConstants.FALSE)) {
                    matSelect.click();
                    ngDriver.waitForAngularRequestsToFinish();
                    // check if dropdown is multiple selection and it is required to select more than one option that are separated with ;
                    String[] selectedOptions = null;

                    // check if it is a multiple select dropdown list
                    if (matSelect.getAttribute("aria-multiselectable")
                            .equalsIgnoreCase(GeneralConstants.TRUE)) {
                        unselectAllMatSelectOptions(matSelect);
                        if (displayedText.contains(GeneralConstants.STRING_DELIMETER)) {
                            selectedOptions = displayedText.split(GeneralConstants.STRING_DELIMETER);
                            for (int i = 0; i < selectedOptions.length; i++)
                                driver.findElement(By.xpath("//mat-option/span[contains(text(),'" + selectedOptions[i].trim() + "')]"))
                                        .click();
                        } else
                            driver.findElement(By.xpath("//mat-option/span[contains(text(),'" + displayedText.trim() + "')]"))
                                    .click();

                        matSelect.sendKeys(Keys.ESCAPE);
                    }
                    // else it is a single-select dropdown list
                    else
                        driver.findElement(By.xpath("//mat-option/span[contains(text(),'" + displayedText.trim() + "')]"))
                                .click();
                }
            }
        }
        else
            throw new Exception("Web element 'matSelect' is null .. it could not be located");
    }


    // select mat-select's option(s) by displayed texts for multiple selection drop down lists
    public String  selectOptionsByIndices(WebElement matSelect, String optionsIDs) throws Exception
    {
        String selectedOptionsString = "";

        if(matSelect != null) {
            // check if matselect is not disabled and selected option(s) are not empty
            if (optionsIDs != null && !optionsIDs.trim()
                    .isEmpty()) {
                if (matSelect.getAttribute("aria-disabled")
                        .equalsIgnoreCase(GeneralConstants.FALSE)) {
                    matSelect.click();
                    ngDriver.waitForAngularRequestsToFinish();

                    //get select options IDs
                    String selectOptionsIDSString = matSelect.getAttribute("aria-owns");
                    //Check that the there are lidted option in dropdown and that it is not empty
                    if(selectOptionsIDSString != null) {
                        System.out.println("selectOptionsIDSString " + selectOptionsIDSString);
                        String[] selectOptionsIDs = selectOptionsIDSString.split(" ");

                        // check if dropdown is multiple selection and it is required to select more than one option that are separated with #
                        String[] selectedOptions = null;


                        // check if it is a multiple select dropdown list
                        if (matSelect.getAttribute("aria-multiselectable")
                                .equalsIgnoreCase(GeneralConstants.TRUE)) {
                            unselectAllMatSelectOptions(matSelect);
                            if (optionsIDs.contains(GeneralConstants.STRING_DELIMETER)) {
                                selectedOptions = optionsIDs.split(GeneralConstants.STRING_DELIMETER);
                                for (int i = 0; i < selectedOptions.length; i++) {
                                    WebElement option = driver.findElement(By.id(selectOptionsIDs[Integer.parseInt(selectedOptions[i].trim())]));
                                    selectedOptionsString += option.findElement(By.xpath(".//span[@class='mat-option-text']"))
                                            .getText()
                                            .trim();
                                    if (i != selectedOptions.length - 1)
                                        selectedOptionsString += GeneralConstants.STRING_DELIMETER;
                                    option.click();
                                }

                            } else {
                                WebElement option = driver.findElement(By.id(selectOptionsIDs[Integer.parseInt(optionsIDs.trim())]));
                                selectedOptionsString = option.findElement(By.xpath(".//span[@class='mat-option-text']"))
                                        .getText()
                                        .trim();
                                option.click();
                            }


                        }
                        // else it is a single-select dropdown list
                        else {
                            WebElement option = driver.findElement(By.id(selectOptionsIDs[Integer.parseInt(optionsIDs.trim())]));
                            selectedOptionsString = option.findElement(By.xpath(".//span[@class='mat-option-text']"))
                                    .getText()
                                    .trim();
                            option.click();
                        }

                        matSelect.sendKeys(Keys.ESCAPE);
                    }
                    // dropdown list is empty
                    else
                        throw new Exception("Dropdown list is empty and has no options to select" +
                                "");

                }
            }
        }
        else
            throw new Exception("Web element 'matSelect' is null .. it could not be located");

        System.out.println("selectedOptionsString " + selectedOptionsString);
        return selectedOptionsString;
    }


    // get mat-select's option by index
    public String selectOptionByindex(WebElement matSelect, String optionIndex) throws Exception
    {
        String optionText = "";
        if(matSelect != null) {
            // check if matselect is not disabled and selected option(s) are not empty
            if (optionIndex != null && !optionIndex.isEmpty() && matSelect.getAttribute("aria-disabled")
                    .equalsIgnoreCase(GeneralConstants.FALSE)) {
                matSelect.click();
                String selectOptionsIDSString = matSelect.getAttribute("aria-owns");
                if(selectOptionsIDSString != null && !selectOptionsIDSString.isEmpty()) {
                    String[] selectOptionsIDs = selectOptionsIDSString.split(" ");

                    // check that required index to select exists in the dropdown list to avoid index out of bond exception
                    if (Integer.parseInt(optionIndex) >= selectOptionsIDs.length)
                        throw new Exception("Element required to select does not exist in list - list size = " + selectOptionsIDs.length + " while required element's index to select = " + optionIndex);

                    optionText = driver.findElement(By.xpath("//mat-option[@id='" + selectOptionsIDs[Integer.parseInt(optionIndex)] + "']/span"))
                            .getText();
                    driver.findElement(By.id(selectOptionsIDs[Integer.parseInt(optionIndex)]))
                            .click();
                    return optionText;
                }
                else
                    throw new Exception("Drop down list is empty and has no listed options");
            }
        }
        else
            throw new Exception("Web element 'matSelect' is null .. it could not be located");

        return optionText;
    }






    // get md-select's option by index
    public String selectMDOptionByindex(WebElement mdSelect, String optionIndex) throws Exception
    {
        String optionText = "";
        if(mdSelect != null) {
            // check if matselect is not disabled and selected option(s) are not empty
            if (optionIndex != null && !optionIndex.isEmpty() && mdSelect.getAttribute("aria-disabled")
                    .equalsIgnoreCase(GeneralConstants.FALSE)) {
                mdSelect.click();
                String selectOptionsContainerID = mdSelect.getAttribute("aria-owns");
                if(selectOptionsContainerID != null && !selectOptionsContainerID.isEmpty())
                {
                    List<WebElement> options = driver.findElements(By.xpath("//div[@id='"+selectOptionsContainerID+"']//md-option"));

                    // check that required index to select exists in the dropdown list to avoid index out of bond exception
                    if (Integer.parseInt(optionIndex) >= options.size())
                        throw new Exception("Element required to select does not exist in list - list size = " + options.size() + " while required element's index to select = " + optionIndex);

                    WebElement selectedOption = options.get(Integer.parseInt(optionIndex));
                    optionText = selectedOption.getText();
                    selectedOption.click();

                    return optionText;
                }
                else
                    throw new Exception("Drop down list is empty and has no listed options");
            }
        }
        else
            throw new Exception("Web element 'matSelect' is null .. it could not be located");

        return optionText;
    }






     //Set checkbox value to checked or unchecked as required
    public void setCheckboxValue(WebElement checkbox, String requiredToBeChecked) throws Exception
    {
        if(checkbox !=null) {
            if (requiredToBeChecked != null && !requiredToBeChecked.trim()
                    .isEmpty()) {
                if (!checkbox.getAttribute("class")
                        .contains("disabled"))
                    if ((requiredToBeChecked.equalsIgnoreCase(GeneralConstants.TRUE) && !checkbox.getAttribute("class")
                            .contains("checked"))
                            || ((requiredToBeChecked.equalsIgnoreCase(GeneralConstants.FALSE) && checkbox.getAttribute("class")
                            .contains("checked"))))
                        checkbox.click();
            }
        }
        else
            throw new Exception("Web element 'Check box' is null .. it could not be located");
    }

    //Do not perform action on input text unless  text in test data sheet contains a values. Otherwise, keep it value as is.
    public void setTextValue(WebElement inputText, String testDataText) throws Exception
    {
        if(inputText != null) {
            if ((testDataText != null && !testDataText.trim().isEmpty()))
                if (inputText.getAttribute("aria-invalid")
                        .equalsIgnoreCase(GeneralConstants.FALSE)) {
                    new Actions(driver).moveToElement(inputText)
                            .perform();
                    inputText.clear();
                    if (!testDataText.equalsIgnoreCase(GeneralConstants.CLEAR))
                        inputText.sendKeys(testDataText);
                }
        }
        else
            throw new Exception("Web element 'Input' is null .. it could not be located");
    }




}
