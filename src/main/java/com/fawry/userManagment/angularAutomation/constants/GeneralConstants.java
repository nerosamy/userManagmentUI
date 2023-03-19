package com.fawry.userManagment.angularAutomation.constants;

public class GeneralConstants {

	// *************************    General constants used allover the app   ********************************
	public static final String SUCCESS = "Success";
	public static final String FAILED = "Failed";
	public static final String TRUE = "TRUE";
	public static final String FALSE = "FALSE";
	public static final String CLEAR = "Clear";
	public static final String TEST_SCOPE_SMOKE = "Smoke";
	public static final String TEST_SCOPE_FULL = "Full";
	public static final String TEST_CASE_METHOD_ADD = "Add";
	public static final String TEST_CASE_METHOD_UPDATE = "Update";
	public static final String TEST_CASE_METHOD_UPDATE_CONDITIONS = "UpdateWithCond";
	public static final String STRING_DELIMITER = "#";
	public static final String FILE_DELIMITER = "/";
	public static final String MISMATCH_ERR_MSG = " Value in DB Mismatched the VALUE in page";
	public static final String POM_EXCEPTION_ERR_MSG = "Test Failed due to an exception occurred in POM's method";
	public static final String Expected_ERR_MSG = "Test Failed due to Expected and Actual Results doesn't Match";
	public static final String DB_ERROR_MSG = "No results found in DB or DB error occurred";
	public static final String ERR_TYPE_PAGE = "page";
	public static final String WELCOME_MSG = "Welcome";
	public static final String ERR_TYPE_NOTIFICATION = "notification";
	public static final String STATUS_INITIATED = "INITIATED";
	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_INACTIVE = "INACTIVE";
	public static final String STATUS_DELETED = "DELETED";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_REFUSED = "REFUSED";


// **********************************************************************************************************

//  **********************   Test Data config file and its properties key names ***************************

	//Test data configs file and its properties key names
	public static final String TEST_DATA_CONFIG_FILE_NAME = "configFiles//TestDataConfig.properties";

	// Test data strategy to get test data source type and implementing classes
	public static final String TEST_DATA_TYPE = "TestDataType";
	public static final String TEST_DATA_TYPE_CLASS_PATH = "TestDataStrategyClassPath_";
// **********************************************************************************************************

	//  **********************   General config file and its properties key names ***************************
	public static final String GENERAL_CONFIG_FILE_NAME = "configFiles//GeneralConfigs.properties";

	public static final String SMOKE_TEST_FLAG = "isSmockTestScopeEnabled";

	public static final String DEFAULT_DOWNLOAD_PATH = "defaultDownloadPath";

	public static final String MAILSLURP_API_KEY = "mailslurpAPIKey";

	public static final String TEST_REPORT_MAIL_RECIPIENTS = "testReportMailRecipient";
	public static final String TEST_REPORT_MAIL_SENDER= "testReportMailSender";
	public static final String TEST_REPORT_MAIL_SUBJECT= "testReportMailSubject";
	public static final String TEST_REPORT_MAIL_BODY= "testReportMailBody";
	public static final String TEST_REPORT_MAIL_HOST = "smtpHostIP";
	public static final String SEND_REPORT_BY_MAIL_ENABLED = "sendTestReportByMailEnabled";


	// Extent report configs
	public static final String SCREENSHOT_FAILED_TESTS_PATH = "screenshotsOfFailedTestsPath";
	public static final String EXTENT_REPORT_FILE_PATH = "extentReportFilepath";
	public static final String EXTENT_REPORT_TITLE = "extentReportTitle";
	public static final String EXTENT_REPORT_NAME = "extentReportName";
	public static final String ADD_LOG_TO_EXTENT_REPORT = "addLogToExtentReport";

	//Login Credentials
	public static final String VALID_MAIL = "login.userMail";
	public static final String VALID_PASSWORD = "login.password";

	// Deployment server's configs to get server log file
	public static final String LOG_SERVER_IP = "logServerIp";
	public static final String LOG_SERVER_USERNAME = "logServerUserName";
	public static final String LOG_SERVER_PASSWORD = "logServerPassword";
	public static final String LOG_SERVER_PORT = "logServerPort";
	public static final String LOG_SERVER_IN_CONTAINER = "isDockerContainer";
	public static final String LOG_SERVER_FILE_PATH_IN_CONTAINER = "logFilePathOnServerInContainer";
	public static final String SSH_CMD_TO_GET_CONTAINER_ID = "sshRunCmdToGetContainerID";
	public static final String LOG_SERVER_FILE_PATH = "logFilePathOnServer";
	public static final String LOG_LOCAL_FILE_PATH = "localLogFileDirectory";
	public static final String LOG_LOCAL_FILE_SIZE = "logFileSizeInBytes";
// **********************************************************************************************************


	// *****************     Database config file and its properties key names     **************************
	public static final String DB_CONFIG_FILE_NAME = "configFiles//DBConfigs.properties";

	//Different DB configs
	public static final String USERS_DB_NAME = "USERS";


	public static final String USERS_DB_URL_KEY = "USERS_DB_URL";
	public static final String USERS_DB_USERNAME_KEY = "USERS_DB_USERNAME";
	public static final String USERS_DB_PASSWORD_KEY = "USERS_DB_PASSWORD";


	// **********************************************************************************************************
	public static final String SUCCESS_MASSAGE = "success#Operation done successfully!";
	public static final String UNASSIGNED_ROLE_SUCCESS_MASSAGE = "success#Operation done successfully!#success#Operation done successfully!";

	public static final String DELETE_ERROR_MASSAGE = "Error#Unable to delete role 5150057, it has users";




}
