package com.fawry.userManagment.angularAutomation.pages;

import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import com.fawry.userManagment.angularAutomation.utils.Log;
import com.fawry.userManagment.angularAutomation.utils.PropertiesFilesHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MainPage {

    // Initialize web drivers
    WebDriver driver;
    JavascriptExecutor jsDriver;
    WebDriverWait wait;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.jsDriver = (JavascriptExecutor) driver;
        //Set a delay of 30 secs to wait for elements' visibility
        AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(driver, 30);
        PageFactory.initElements(factory, this);
        wait = new WebDriverWait(driver, 30);
    }

    //locate all mat error messages dispalyed in the page
    @FindBy(xpath = "//small[contains(@class,'error')]")
//    @FindBy(xpath = "//*[@class='col-md-12']/h4")

    public List<WebElement> matErrMsgs;

    // locate all pop-up notification errors
    @FindBy(xpath = "//*[@role='alert']//div//div")
    List<WebElement> notificationMsgs;

    @FindBy(xpath = "//h6[contains(@class,'text-muted')]")
    List<WebElement> WelcomeMsgs;

    @FindBy(xpath = "//*[@role='progressbar]")
    WebElement progressBar;

    @FindBy(xpath = "//*[@role='option']")
    List<WebElement> selectOptions;


    //Initialize instances of properties files to be used in all pages
    PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);

    // Browser's default download path config from properties file
    String browserDefaultDownloadpath = System.getProperty("user.dir") + generalCofigsProps.getProperty(GeneralConstants.DEFAULT_DOWNLOAD_PATH);

    //Do not perform action on input text unless  text in test data sheet contains a values. Otherwise, keep it value as is.
    public void setTextValue(WebElement inputText, String testDataText) throws Exception {

        if ((testDataText != null && !testDataText.trim().isEmpty()))
            if (inputText != null) {
                {
                    new Actions(driver).moveToElement(inputText)
                            .perform();
                    inputText.click();
                    inputText.clear();
                    if (!testDataText.equalsIgnoreCase(GeneralConstants.CLEAR)) {
                        inputText.sendKeys(testDataText);
                    }
                }
            } else
                throw new Exception("Web element 'Input' is null .. it could not be located");
        Thread.sleep(100);
    }

    // select mat-select's option(s) by displayed text
    public void selectOptionByDisplayedText(WebElement select, String displayedText) throws Exception {
        if (select != null) {
            // check if matselect is not disabled and selected option(s) are not empty
            if (displayedText != null && !displayedText.trim()
                    .isEmpty()) {
                select.click();
                Thread.sleep(500);
                if (selectOptions.size() != 0) {
                    String[] selectedOptions = null;
                    for (WebElement selectOption : selectOptions)
                        if (selectOption.getText().trim().equalsIgnoreCase(displayedText.trim())) {
                            selectOption.click();
                            break;
                        }
                } else {
                    throw new Exception("Drop down list is empty and has no listed options");
                }
            }
        } else
            throw new Exception("Web element 'dropDown' is null .. it could not be located");

    }

    // select another option than the selected and return its value.
    public String selectAnotherOption(WebElement select, String flag) throws Exception {
        String displayedText = "";
        if (select != null) {
            // check if matselect is not disabled and selected option(s) are not empty
            if (flag != null && !flag.trim()
                    .isEmpty()) {
                select.click();
                int size = selectOptions.size();
                Thread.sleep(500);
                if (size != 0) {
                    for (WebElement selectOption : selectOptions)
                        if (selectOption.getAttribute("aria-selected").equalsIgnoreCase("false")) {
                            displayedText = selectOption.getText();
                            selectOption.click();
                            break;
                        }
                } else {
                    throw new Exception("Drop down list is empty and has no listed options");
                }
            }
        } else
            throw new Exception("Web element 'dropDown' is null .. it could not be located");
        return displayedText;
    }

    public void selectOptionsFromMultiSelect(WebElement inputText, String testDataText) throws Exception {

        if ((testDataText != null && !testDataText.trim().isEmpty()))
            if (inputText != null) {
                {
                    String[] weekDays = testDataText.split("-");
                    if (weekDays.length != 0) {
                        inputText.click();
                        for (int i = 0; i < weekDays.length; i++) {
                            driver.findElement(By.xpath("//li[@aria-label='" + weekDays[i] + "']")).click();
                        }
                    }
                }
            } else
                throw new Exception("Web element 'Input' is null .. it could not be located");
        Thread.sleep(100);
    }

    // Get all error messages whether mat-error or pop-up notification into one string separated with GeneralConstants.STRING_DELIMETER
    public String getAllErrMsgs(String errType) {
        Log.info("Start collecting all massages");

        StringBuilder allErrorMsgsString = new StringBuilder();
        try {
//            Thread.sleep(300);
            if (errType.equalsIgnoreCase(GeneralConstants.WELCOME_MSG)) {
                //wait just 3 seconds for all errors to be displayed

                Log.info("Number of displayed Welcom MSGS messages in page" + WelcomeMsgs.size());


                for (int i = 0; i < WelcomeMsgs.size(); i++) {
                    if (WelcomeMsgs.get(i).getText().isEmpty())
                        continue;
                    allErrorMsgsString.append(WelcomeMsgs.get(i).getText());
                    if (i != WelcomeMsgs.size() - 1)
                        allErrorMsgsString.append(GeneralConstants.STRING_DELIMITER);
                }
            }
            if (errType.equalsIgnoreCase(GeneralConstants.ERR_TYPE_PAGE)) {
                //wait just 3 seconds for all errors to be displayed

                Log.info("Number of displayed mat error messages in page" + matErrMsgs.size());


                for (int i = 0; i < matErrMsgs.size(); i++) {
                    if (matErrMsgs.get(i).getText().isEmpty())
                        continue;
                    allErrorMsgsString.append(matErrMsgs.get(i).getText());
                    if (i != matErrMsgs.size() - 1)
                        allErrorMsgsString.append(GeneralConstants.STRING_DELIMITER);
                }
            } else if (errType.equalsIgnoreCase(GeneralConstants.ERR_TYPE_NOTIFICATION)) {
                Log.info("Number of displayed pop-up notification error messages in page is :  " + notificationMsgs.size());
                for (int j = 0; j < notificationMsgs.size(); j++) {
                    if (notificationMsgs.get(j).getText().isEmpty())
                        continue;
                    allErrorMsgsString.append(notificationMsgs.get(j).getText());
                    if (j != notificationMsgs.size() - 1)
                        allErrorMsgsString.append(GeneralConstants.STRING_DELIMITER);
                }
            }
            Log.info("Actual Result = " + allErrorMsgsString);
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
        return allErrorMsgsString.toString();
    }


    // select mat-select's option(s) by displayed texts for multiple selection drop down lists
    public String selectOptionByindex(WebElement select, String optionIndex) throws Exception {
        String selectedOptionString = "";

        if (select != null) {
            // check if matselect is not disabled and selected option(s) are not empty
            if (optionIndex != null && !optionIndex.trim()
                    .isEmpty()) {
                select.click();
                Thread.sleep(2000);
                if (selectOptions.size() != 0) {
                    selectedOptionString = selectOptions.get(Integer.parseInt(optionIndex)).getText();
                    selectOptions.get(Integer.parseInt(optionIndex)).click();
                } else
                    throw new Exception("Drop down list is empty and has no listed options");
            }
        } else
            throw new Exception("Web element 'matSelect' is null .. it could not be located");

        return selectedOptionString;
    }

    public void waitForFileDownload(String expectedFileName) {
        FluentWait<WebDriver> wait = new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(60))
                .pollingEvery(Duration.ofNanos(10));

        File fileToCheck = new File(expectedFileName);

        wait.until((WebDriver wd) -> fileToCheck.exists());

    }

    public void scrollUp() throws InterruptedException {
        //to scroll up the page
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,-700)", "");
        Thread.sleep(1000);

    }

    public void scrollIntoView(WebElement element) throws InterruptedException {
        //to scroll down the page
        Thread.sleep(1000);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView();", element);
        Thread.sleep(1000);

    }

//    public void untilAngularFinishHttpCalls() {
//        final String javaScriptToLoadAngular =
//                "var injector = window.angular.element('body').injector();" +
//                        "var $http = injector.get('$http');" +
//                        "return ($http.pendingRequests.length === 0)";
//
//        ExpectedCondition<Boolean> pendingHttpCallsCondition = new ExpectedCondition<Boolean>() {
//            public Boolean apply(WebDriver driver) {
//                return ((JavascriptExecutor) driver).executeScript(javaScriptToLoadAngular).equals(true);
//            }
//        };
//        WebDriverWait wait = new WebDriverWait(driver, 20); // timeout = 20 secs
//        wait.until(pendingHttpCallsCondition);
//    }


}
