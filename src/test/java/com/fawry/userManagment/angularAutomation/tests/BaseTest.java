package com.fawry.userManagment.angularAutomation.tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.fawry.userManagment.angularAutomation.backendServices.ServicesDelegate;
import com.fawry.userManagment.angularAutomation.pages.HomePage;
import com.fawry.userManagment.angularAutomation.pages.LoginPage;
import com.fawry.userManagment.angularAutomation.strategy.testData.TestDataStrategy;
import com.fawry.userManagment.angularAutomation.utils.Log;
import com.fawry.userManagment.angularAutomation.utils.ServerLog;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.ITestResult;
import org.testng.annotations.*;

import com.fawry.userManagment.angularAutomation.utils.PropertiesFilesHandler;
import com.fawry.userManagment.angularAutomation.constants.GeneralConstants;
import io.github.bonigarcia.wdm.WebDriverManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import javax.mail.*;
import javax.mail.internet.*;



public class BaseTest {

	//Selenium and Angular webdrivers
	public WebDriver driver;
	NgWebDriver ngDriver;
	JavascriptExecutor jsDriver;

	//Extent report objects
	public ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public ExtentTest test;
	String vaildUserMail = "";


	//Initialize instances of properties files to be used in all tests
	PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
	Properties generalCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
	Properties testdataCofigsProps = propHandler.loadPropertiesFile(GeneralConstants.TEST_DATA_CONFIG_FILE_NAME);

	// Browser's default download path config from properties file
	String browserDefaultDownloadpath = System.getProperty("user.dir") + generalCofigsProps.getProperty(GeneralConstants.DEFAULT_DOWNLOAD_PATH);

	HomePage homepage;
	ServicesDelegate backendService;

	String dateTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

	@BeforeSuite(description = "Setting up extent report", alwaysRun = true)
	@Parameters("browserType")
	public void setExtent(String browserType)
	{
		try {
			Log.info("Setting up extent report before test on browser: " + browserType);
			// get report file path
			String extentReportFilePath = generalCofigsProps.getProperty(GeneralConstants.EXTENT_REPORT_FILE_PATH);
			// specify location of the report
			htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + extentReportFilePath +dateTime+ ".html");

			htmlReporter.config().setDocumentTitle(generalCofigsProps.getProperty(GeneralConstants.EXTENT_REPORT_TITLE)); // Tile of report
			htmlReporter.config().setReportName(generalCofigsProps.getProperty(GeneralConstants.EXTENT_REPORT_NAME)); // Name of the report
			htmlReporter.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);

			// Passing General information
			extent.setSystemInfo("Environemnt", "QA");
			extent.setSystemInfo("Browser", browserType);
		}
		catch(Exception e)
		{
			Log.error("Error occured while setting up extent reports on " + browserType, e);
		}

	}

	@BeforeSuite(description = "Deleting all previously downloaded files before running suite", alwaysRun = true)
	public void clearDownloadedFiles()
	{
		try {
			FileUtils.cleanDirectory(new File(browserDefaultDownloadpath));
		} catch (IOException e) {
			Log.error("Error occured while deleting downloaed files before test " + new Object() {
			}
					.getClass()
					.getName() + "." + new Object() {
			}
					.getClass()
					.getEnclosingMethod()
					.getName(), e);
		}
	}


	@Parameters({"url","browserType"})
	@BeforeClass(description = "Setting up selenium webdriver before each class run", alwaysRun = true)
	public void loadConfiguration(String url,String browserType)
	{
		try {
			Log.info("Initialize Selenium webdriver before tests' Class");

			// initialize selenium driver that is set as a config in testng.xml
			switch (browserType) {
				case ("Chrome"):
					WebDriverManager.chromedriver().setup();
					driver = new ChromeDriver(setChromeOption());
					break;
				case ("Firefox"):
					WebDriverManager.firefoxdriver().setup();
					driver = new FirefoxDriver(setFireFoxOption());
					break;
				case ("IE"):
					WebDriverManager.iedriver().setup();
					driver = new InternetExplorerDriver();
					break;
				case ("Edge"):
					WebDriverManager.edgedriver().setup();
					driver = new EdgeDriver();
					break;
				case ("Opera"):
					WebDriverManager.operadriver().setup();
					driver = new OperaDriver();
					break;
			}

			// initialize angular webdriver
			jsDriver = (JavascriptExecutor) driver;
			ngDriver = new NgWebDriver(jsDriver).withRootSelector("\"app-root\"");
			driver.manage().window().maximize();
			driver.get(url);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			Log.info("Selenium webdriver was initialized successfully");
		}
		catch(Exception e)
		{
			Log.error("Error occured while initializing selenium web driver", e);
		}

	}


	@BeforeClass(description = "Login successfully to the application in order to proceed in business tests", alwaysRun = true)
	public void loginSuccessfully()
	{
		Log.info("Login with valid credentials before class tests to be able to navigate to required pages from homepage");

		String validUserMail = generalCofigsProps.getProperty(GeneralConstants.VALID_MAIL);
		String validUserPassword = generalCofigsProps.getProperty(GeneralConstants.VALID_PASSWORD);

		LoginPage loginPage = new LoginPage(driver);
		String loggedInSuccessfully = loginPage.loginSuccessfully(validUserMail, validUserPassword);

		//Check if logged in to the application successfully
		if(loggedInSuccessfully.equals(GeneralConstants.SUCCESS)) {
			homepage = new HomePage(driver);
			backendService = new ServicesDelegate();
		}
		//If user could login to application then it's a blocking issue. Exit
		else
		{
			Log.fatal("Could not login to business entity using the supplied credentials username: " + validUserMail + " and password: " + validUserPassword);
			closeDriver();
			endReport();
			System.exit(1);
		}
	}





	private ChromeOptions setChromeOption()
	{
		ChromeOptions options = new ChromeOptions();
		HashMap<String, Object> ChromePrefs = new HashMap<>();
		ChromePrefs.put("profile.default.content_settings.popups", 0);


		ChromePrefs.put("download.default_directory", browserDefaultDownloadpath);
		options.setExperimentalOption("prefs", ChromePrefs);
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS,true);
		
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		System.out.println(options);
		return options;
	}

	private FirefoxOptions setFireFoxOption()
	{
		FirefoxOptions option = new FirefoxOptions();
		option.addPreference("browser.download.folderlist", 2);
		option.addPreference("browser.download.dir", browserDefaultDownloadpath);
		option.addPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream");
		option.addPreference("browser.download.manager.showWhenStarting", false);
		return option;
	}



	@AfterMethod(description = "Logging test status to log file and extent report", alwaysRun = true)
	public void logTestStatusForReport(ITestResult result) {
		try {
			if (result.getStatus() == ITestResult.FAILURE)
			{
				Log.info("logging Testcase FAILED " + result.getName() + " in Extent Report");
				test.log(Status.FAIL, result.getName() + " HAS FAILED"); // to add name in extent report
				test.log(Status.FAIL, "EXCEPTION Thrown is " + result.getThrowable()); // to add error/exception in extent report
				// adding log file
				ServerLog serverLog = new ServerLog();
				String logFilePath = serverLog.getLocalLogFilePath(result.getName());
				System.out.println("logFilePath " + logFilePath);
				test.log(Status.FAIL, "Application's log file <a href='" + logFilePath + "'>log</a>");

				// adding screen shot.
				String screenshotPath = getScreenshot(driver, result.getName());
				System.out.println("screenshotPath " + screenshotPath);
				test.addScreenCaptureFromPath(screenshotPath);

			} else if (result.getStatus() == ITestResult.SKIP) {
				Log.info("logging Testcase SKIPPED " + result.getName() + " in Extent Report");
				test.log(Status.SKIP, "Test Case SKIPPED is " + result.getName());
			} else if (result.getStatus() == ITestResult.SUCCESS) {
				Log.info("logging Testcase SUCCESS " + result.getName() + " in Extent Report");
				test.log(Status.PASS, "Test Case PASSED is " + result.getName());
			}
			// log that test case has ended
			Log.endTestCase(result.getName());
		}
		catch(Exception e)
		{
			Log.warn("Error occured while logging testcase " +result.getName()+ " result to extent report", e);
			e.printStackTrace();
		}
	}

	private String getScreenshot(WebDriver driver, String screenshotName) throws IOException {

		dateTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		Log.info("Taking Screenshot for the FAILED Testcase");

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		//get the path of failed tests screenshots
		String screenShotsPath = generalCofigsProps.getProperty(GeneralConstants.SCREENSHOT_FAILED_TESTS_PATH);

		// after execution, you could see a folder "FailedTestsScreenshots" under src folder
		String destination = System.getProperty("user.dir") + screenShotsPath + date + GeneralConstants.FILE_DELIMITER + screenshotName + dateTime + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	public void sendMail(String testReportFilePath) throws  MessagingException
	{
		String recipient = generalCofigsProps.getProperty(GeneralConstants.TEST_REPORT_MAIL_RECIPIENTS);
		String sender = generalCofigsProps.getProperty(GeneralConstants.TEST_REPORT_MAIL_SENDER);
		String subject = generalCofigsProps.getProperty(GeneralConstants.TEST_REPORT_MAIL_SUBJECT);
		String mailBody = generalCofigsProps.getProperty(GeneralConstants.TEST_REPORT_MAIL_BODY);
		String host = generalCofigsProps.getProperty(GeneralConstants.TEST_REPORT_MAIL_HOST);

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		Session session = Session.getDefaultInstance(properties);

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(sender));

		String[] recipientList = recipient.split(",");
		InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
		int counter = 0;
		for (String testReportRecipient : recipientList) {
			recipientAddress[counter] = new InternetAddress(testReportRecipient.trim());
			counter++;
		}
		message.setRecipients(Message.RecipientType.TO, recipientAddress);

		message.setSubject(subject);


		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent("<h1>"+subject+"</h1>" +
				"<p>"+mailBody+"</p>" , "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Add attachment
//        messageBodyPart = new MimeBodyPart();
//        DataSource source = new FileDataSource("receipt.txt");
//        multipart.addBodyPart(messageBodyPart);

		message.setContent(multipart);


		//Transport.send(message);

	}



	@AfterClass(description = "Quitting selenium driver after each class run", alwaysRun = true)
	public void closeDriver()
	{
		Log.info("Closing selenium Web driver after Class");
		if(driver !=null)
			driver.quit();
	}

	@AfterSuite(description = "Closing extent report after running all tests", alwaysRun = true)
	public void endReport() {
		try {
			Log.info("Closing Extent report after Suite");
			if (extent != null)
				extent.flush();
			// Check if send test report by mail enabled then send report file path to the required recipients
			String sendReportByMailEnabled = generalCofigsProps.getProperty(GeneralConstants.SEND_REPORT_BY_MAIL_ENABLED);

			if (sendReportByMailEnabled.equalsIgnoreCase(GeneralConstants.TRUE)) {
				Log.info("Sending test report file path to recipients listed in cinfig file");

				sendMail("");
			}
		}
			catch (MessagingException e) {
				Log.error("Error occured while sending test report to recipients " + new Object() {
				}
						.getClass()
						.getName() + "." + new Object() {
				}
						.getClass()
						.getEnclosingMethod()
						.getName(), e);
			}

	}


// Check testdata type and retrieve it from its source accordingly
	protected ArrayList<ArrayList<Object>> provideTestData(String methodName)
	{
		Log.info("Retrieving Test data of testcase " + methodName);

		String connectionProperties = testdataCofigsProps.getProperty(methodName);

		ArrayList<ArrayList<Object>> result = null;
		TestDataStrategy testData;
		String testDataType;
		String testDataTypeClassPath;
		try
		{
			//get test data type to connect to the proper test data source accordingly
			testDataType = testdataCofigsProps.getProperty(GeneralConstants.TEST_DATA_TYPE);

			//get class path of the class that implements methods of proper class path
			testDataTypeClassPath = testdataCofigsProps.getProperty(GeneralConstants.TEST_DATA_TYPE_CLASS_PATH + testDataType);
			//create instance from the proper class of specified data source
			testData = (TestDataStrategy) Class.forName(testDataTypeClassPath).newInstance();

			//load test data from the proper source
			result = testData.loadTestData(connectionProperties);


		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Log.error("Error occured while retriving test data for Test: " + methodName, e);
		}

		return result;

	}



	// Check testdata type and retrieve it from its source accordingly
	protected Object[][] getTestDataFromExtSource(String methodName, Class dmClass)
	{
		Log.info("Retrieving Test data of testcase " + methodName);

		Object[][] result = null;
		TestDataStrategy testData;
		String testDataType;
		String testDataTypeClassPath;
		try
		{
			//get test data type to connect to the proper test data source accordingly
			testDataType = testdataCofigsProps.getProperty(GeneralConstants.TEST_DATA_TYPE);

			//get class path of the class that implements methods of proper class path
			testDataTypeClassPath = testdataCofigsProps.getProperty(GeneralConstants.TEST_DATA_TYPE_CLASS_PATH + testDataType);
			//create instance from the proper class of specified data source
			testData = (TestDataStrategy) Class.forName(testDataTypeClassPath).newInstance();

			//load test data from the proper source
			String connectionProperties = testdataCofigsProps.getProperty(methodName);
			result = testData.getTestDataFromExtSource(connectionProperties, dmClass);


		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Log.error("Error occured while retriving test data for Test: " + methodName, e);
		}

		return result;

	}



}
