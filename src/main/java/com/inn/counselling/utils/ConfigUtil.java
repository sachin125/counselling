package com.inn.counselling.utils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.model.Users;


public class ConfigUtil {

	private static Logger logger=LoggerFactory.getLogger(ConfigUtil.class);
	public static String workFlowAdmin="workFlowAdmin"; 
	public static final String SUCCESS_JSON="{\"status\":\"success\"}";
	public static final String FAILURE_JSON="{\"status\":\"failure\"}";
	public static final String DBUsernamePwdAppenderKey = "DBUsernamePwdAppenderKey";	
	public static final String PATH_DOESNOT_CONTAINS_IN_FILTER = "PATH_DOESNOT_CONTAINS_IN_FILTER";
	public static final String PATH_CONTAINS_IN_FILTER = "PATH_CONTAINS_IN_FILTER";
	public static final String SMS_ID = "SMS_ID";
	public static final String EMAIL_ID = "EMAIL_ID";
	public static final String EMAIL_PASSWORD = "EMAIL_PASSWORD";
	public static final String CONFIG_PROPS="config.properties";

	public static final String MSG_PROPS="message.properties";
	public static final String MSGFRENCH_PROPS="message_french.properties";
	public static final String MSGSPANISH_PROPS="message_spanish.properties";

	public static final String DB_PROPS="db.properties";
	public static final String APP_DEPLOY_URL="APP_DEPLOY_URL";
	public static final String APP_LOGIN_URL="APP_LOGIN_URL";
	public static final String DEFAULT_TIME_ZONE="DEFAULT_TIME_ZONE";
	public static final String ACTIVATION_TEMPALTE="ACTIVATION_TEMPALTE";
	public static final String FORGET_PASSWORD_TEMPALTE="FORGET_PASSWORD_TEMPALTE";
	public static final String HOST_NAME = "HOST_NAME";
	public static final String SMTP_PORT = "SMTP_PORT";
	public static final String PAGING_VALUE = "PAGING_VALUE";
	public static final String PROTOCOL="PROTOCOL";
	public static final String MAIL_SERVER_CONFIG="MAIL_SERVER_CONFIG";
	public static final String CONTEXT_PATH = "CONTEXT_PATH";
	public static final String SSL_SOCKETFACTORY="SSL_SOCKETFACTORY";
	public static final String SOCKETFACTORY_FALLBACK="SOCKETFACTORY_FALLBACK";
	public static final String SOCKETFACTORY_PORT="SOCKETFACTORY_PORT";
	public static final String AUTH="AUTH";
	public static final String QUIT_WAIT="QUIT_WAIT";

	public static final String DB_CONNECTION_URL = "db.connection.url";
	public static final String DRIVER_CLASSNAME = "driverClassName";
	public static final String DB_CONNECTION_USERNAME = "db.connection.username";
	public static final String DB_CONNECTION_PASSWORD = "db.connection.password";

	public static final String FACEBOOK_GRAPH_API_USER_DETAIL_URL="FACEBOOK_GRAPH_API_USER_DETAIL_URL";
	public static final String GOOGLE_USER_DETAIL_URL="GOOGLE_USER_DETAIL_URL";
	public static final String FACEBOOK_APP_CLIENTID="FACEBOOK_APP_CLIENTID";
	public static final String FACEBOOK_APP_CLIENTKEY="FACEBOOK_APP_CLIENTKEY";
	public static final String DEFAULT_IMAGE_PATH="DEFAULT_IMAGE_PATH";	
	public static final String EXPIRY_NOTIFICATION_DURATION="EXPIRY_NOTIFICATION_DURATION";
	public static final String DEFAULT_EXPIRY_INTERVAL="DEFAULT_EXPIRY_INTERVAL";
	public static final String MAX_ATTEMPTS="MAX_ATTEMPTS";
	public static final String SECURITY_COOKIE_CSRF_NAME="SECURITY_COOKIE_CSRF_NAME";
	public static final String SECURITY_COOKIE_CSRF_TOKEN="csrfParam";

	public static final String catalinaBase="catalinaBase";
	public static final String ChartMetaXmlPath="ChartMetaXmlPath";
	public static final String ALLZIPPATH="ALLZIPPATH";
	public static final String ENGLISH = "English";
	public static final String SPANISH = "Spanish";
	public static final String FRENCH = "French";
	public static final String LOGOUT = "Logout";
	public static final String LOGGED_OUT = "Logged Out";
	public static final String LOCALHOST = "localhost";

	public static final String mmddyyyy_slash = "MM/DD/YYYY";
	public static final String yyyymmdd_dash = "YYYY-MM-DD";
	public static final String ddMMyyyy_dash = "DD-MMM-YYYY";
	public static final String ddMMyy_dash = "DD-MM-YYYY";
	public static final String mmddyy_slash = "DD/MM/YY";
	public static final String yymmdd_slash = "YYYY/MM/DD";



	public static final String DB_IP_HEADSTART="DB_IP_HEADSTART";
	public static final String DB_USERNAME_HEADSTART="DB_USERNAME_HEADSTART";
	public static final String DB_PASSWORD_HEADSTART="DB_PASSWORD_HEADSTART";
	public static final String DB_SCHEMA_HEADSTART="DB_SCHEMA_HEADSTART";
	public static final String HEADSTART_SERVER_PROMT="HEADSTART_SERVER_PROMT";
	public static final String HEADSTART_SERVER_USERNAME="HEADSTART_SERVER_USERNAME";
	public static final String HEADSTART_SERVER_PASSWORD="HEADSTART_SERVER_PASSWORD";
	public static final String HEADSTART_SERVER_HOST="HEADSTART_SERVER_HOST";
	public static final String LSMR_PASSWORD_PROMPT="LSMR_PASSWORD_PROMPT";
	public static final String 	LSMR_CONFIRM_PROMPT="LSMR_CONFIRM_PROMPT";
	public static final String BASE_WAR_DIRECTORY="BASE_WAR_DIRECTORY";
	public static final String UPLOAD_FILE_PATH="UPLOAD_FILE_PATH";


	public static final String MODEl_PACKAGE_PATH = "com.inn.counselling.model.";
	public static final String SERVICE_PACKAGE_PATH = "com.inn.counselling.service.";
	public static final String PACKAGE_PATH="com.inn.counselling";

	public static final String	FOLDER_NOT_FOUND_MESSAGE="FOLDER_NOT_FOUND_MESSAGE";

	public static final String SHARED_RESOURCE_CAN_NOT_DELETED_MESSAGE = "SHARED_RESOURCE_CAN_NOT_DELETED_MESSAGE";

	public static final String ACTION_NOT_ALLOWED_MESSAGE = "ACTION_NOT_ALLOWED_MESSAGE";

	public static final String RESOURCE_ALREADY_LOCKED = "RESOURCE_ALREADY_LOCKED";

	public static final String UNLOCK_LOCKED_RESOURCE = "UNLOCK_LOCKED_RESOURCE";

	public static final String ONLY_ADMIN_USER_CAN_UNLOCK = "ONLY_ADMIN_USER_CAN_UNLOCK";

	public static final String RESTORING_LOCKED_RESOURCE_NOT_ALLOWED_MESSAGE = "RESTORING_LOCKED_RESOURCE_NOT_ALLOWED_MESSAGE";

	public static final String RESOURCE_ALREADY_SHARED = "RESOURCE_ALREADY_SHARED";

	public static final String RESOURCE_NOT_ALLOWED_TO_SHARE = "RESOURCE_NOT_ALLOWED_TO_SHARE";

	public static final String RESOURCE_NOT_FOUND_MESSAGE = "RESOURCE_NOT_FOUND_MESSAGE";

	public static final String LOCKED_RESOURCE_MESSAGE = "LOCKED_RESOURCE_MESSAGE";

	public static final String SUCCESS="Success";
	public static final String FAILURE="Failure";

	public static final String PERMISSION_DENIED_TO_CREATE_DIR = "PERMISSION_DENIED_TO_CREATE_DIR";

	public static final String SAIKU_REPORT_PENTAHO_URL = "SAIKU_REPORT_PENTAHO_URL";
	public static final String CONSOLE_DB_HOST = "CONSOLE_DB_HOST";
	public static final String CONSOLE_DB_USERNAME = "CONSOLE_DB_USERNAME";
	public static final String CONSOLE_DB_CHECKSUM = "CONSOLE_DB_CHECKSUM";
	public static final String CONSOLE_LOCAL_ORACLE_PATH = "CONSOLE_LOCAL_ORACLE_PATH";
	public static final String CONSOLE_SERVER_USERNAME = "CONSOLE_SERVER_USERNAME";
	public static final String CONSOLE_SERVER_CHECKSUM = "CONSOLE_SERVER_CHECKSUM";
	public static final String CONSOLE_SERVER_HOST = "localhost";
	public static final String CONSOLE_SERVER_PROMT = "CONSOLE_SERVER_PROMT";
	public static final String ORACLE_DB_URL = "ORACLE_DB_URL";
	public static final String ORACLE_DRIVER = "ORACLE_DRIVER";
	public static final String CSV_CREATE_SCRIPT_PATH = "CSV_CREATE_SCRIPT_PATH";
	public static final String CSV_FILE_SEPARATOR = "CSV_FILE_SEPARATOR";

	private static PropertiesConfiguration config;

	private static PropertiesConfiguration dbConfig;

	static
	{
		try {
			config = new PropertiesConfiguration(CONFIG_PROPS);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

		try {
			dbConfig = new PropertiesConfiguration(DB_PROPS);
			dbConfig.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

	}



	public static String getConfigProp(String key)
	{		
		return (String) config.getProperty(key);
	}
	public static List<String> getConfigPropArray(String key)
	{		
		return (List<String>) config.getProperty(key);
	}

	public static void  setConfigProp(String key,String value)
	{		
		config.setProperty(key,value);
		try {
			config.save();
		} catch (ConfigurationException e) {
			logger.error("Error Inside  @class :"+ConfigUtil.class.getName()+" @Method :setConfigProp()"+e.getMessage());
		}
	}


	public static String getDbProp(String key)
	{		
		return (String) dbConfig.getProperty(key);
	}

	public static void  setDbProp(String key,String value)
	{		
		dbConfig.setProperty(key,value);
		try {
			dbConfig.save();
		} catch (ConfigurationException e) {
			logger.error("Error Inside  @class :"+ConfigUtil.class.getName()+" @Method :setDbProp()"+e.getMessage());
		}
	}
	public static String decodeMYSQLUsernamePwd(String password) {

		logger.info("Inside ConfigUtil @method decodeMYSQLUsernamePwd @param password"+password);

		byte[] decodedBytes = Base64.decodeBase64(password.getBytes());
		return new String(decodedBytes).replace(ConfigUtil.getDbProp(ConfigUtil.DBUsernamePwdAppenderKey),"");
	}
	
	public static boolean stringContainsItemFromList(String inputString, List<String> items)
	{
		for(String item:items)
		{
			if(inputString.contains(item))
			{
				return true;
			}
		}
		return false;
	}    


	public static String decryptCredentials(String credential, String passphrase,String iv, String salt){
		try {
			logger.info("Inside ConfigUtil @param credential "+credential+" ,passphrase "+passphrase+" ,iv "+iv+" ,salt "+salt);
			credential=DecryptUtil.decryptAESEncryptWithSaltAndIV(credential, passphrase, salt, iv);
			logger.info("Inside ConfigUtil Decryption is successful with credential "+credential);
			return credential;
		}catch(Exception e) {
			logger.error("Inside ConfigUtil @param credential {} passphrase iv {} salt {} @cause  {}",credential,passphrase,iv,salt,e);
			return "";
		}
	}



	/***
	 * This method is for calculating pagination
	 * @param result
	 * @param limit
	 * @param page
	 * @return the value of upper limit and lower limit
	 */

	public static Map<String, Integer> pagiantionResult(
			Map<String, Integer> result, Integer limit, Integer page) {

		result = new HashMap<String,Integer>();
		Integer lowerLimit = (limit*(page-1));
		Integer upperLimit = (lowerLimit -1) + limit;

		result.put("lowerLimit", lowerLimit);
		result.put("upperLimit", upperLimit);

		return result;
	}

	public static String getDateFormatforExport() {
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yy");
		String fileNameDate=sdf.format(new Date());
		return fileNameDate;
	}

	public static Map<String, Integer> getPageUpperLowerLimit(
			Integer totalCount, Integer page, Integer limit) {
		if (page > 0 && limit > 0) {
			Map<String, Integer> result = new HashMap<String, Integer>();
			Integer lowerLimit = (limit * (page - 1));
			Integer upperLimit = (lowerLimit - 1) + limit;

			result.put("lowerLimit", lowerLimit);
			result.put("upperLimit", upperLimit);
			return result;
		} else {
			return null;
		}
	}

	public static Period calculateAge(LocalDateTime firstDateTime, LocalDateTime secondDateTime) {
		if ((firstDateTime != null) && (secondDateTime != null)) {
			LocalDate firstDate = LocalDate.of(firstDateTime.getYear(), firstDateTime.getMonth(), firstDateTime.getDayOfMonth());
			LocalDate secondDate = LocalDate.of(secondDateTime.getYear(), secondDateTime.getMonth(), secondDateTime.getDayOfMonth());
			return Period.between(firstDate, secondDate);
		} else {
			return null;
		}
	}

	public static boolean deleteFilePermanentFromServer(String filePathToDelete) throws Exception {
		File file = new File(filePathToDelete);
		if(file.exists()) {
			if(file.isDirectory()) {
				throw new Exception("It is a directory");
			}else {
				return file.delete();
			}
		}else {
			return true;
		}
	}
	public static Period calculateAge(Date birthDate,Date currentDate)
	{
		int years = 0;
		int months = 0;
		int days = 0;
		//create calendar object for birth day
		Calendar birthCalendarDate = Calendar.getInstance();
		birthCalendarDate.setTimeInMillis(birthDate.getTime());
		//create calendar object for current day
		long currentTime = currentDate.getTime();
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(currentTime);
		//Get difference between years
		years = now.get(Calendar.YEAR) - birthCalendarDate.get(Calendar.YEAR);
		int currMonth = now.get(Calendar.MONTH) + 1;
		int birthMonth = birthCalendarDate.get(Calendar.MONTH) + 1;
		//Get difference between months
		months = currMonth - birthMonth;
		//if month difference is in negative then reduce years by one and calculate the number of months.
		if (months < 0)
		{
			years--;
			months = 12 - birthMonth + currMonth;
			if (now.get(Calendar.DATE) < birthCalendarDate.get(Calendar.DATE))
				months--;
		} else if (months == 0 && now.get(Calendar.DATE) < birthCalendarDate.get(Calendar.DATE))
		{
			years--;
			months = 11;
		}
		//Calculate the days
		if (now.get(Calendar.DATE) > birthCalendarDate.get(Calendar.DATE))
			days = now.get(Calendar.DATE) - birthCalendarDate.get(Calendar.DATE);
		else if (now.get(Calendar.DATE) < birthCalendarDate.get(Calendar.DATE))
		{
			int today = now.get(Calendar.DAY_OF_MONTH);
			now.add(Calendar.MONTH, -1);
			days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthCalendarDate.get(Calendar.DAY_OF_MONTH) + today;
		} else
		{
			days = 0;
			if (months == 12)
			{
				years++;
				months = 0;
			}
		}
		Period period = Period.of(years, months, days);
		return period;
	}

	public static boolean isValidString(String value) {
		if(value!=null && !value.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isValidList(List value) {
		if(value!=null && !value.isEmpty()) {
			return true;
		}
		return false;
	}

	public static String getObjectClassName(Object object) {
		/*
		 * The type casting below is necessary in order to read if object that is passed
		 * into this method is really a Hibernate proxy object that wraps any entity
		 * object (due to Hibernate's lazy loading).
		 */
		String objectClassName = "";
		if (object instanceof HibernateProxy) {
			objectClassName = ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass()
					.getSimpleName();
		} else {
			objectClassName = object.getClass().getSimpleName();
		}
		return objectClassName;
	}

	public static String decode(String encodedValue){
		if(encodedValue!=null){
			byte[] decodedValue=Base64.decodeBase64(encodedValue.getBytes());
			return new String(decodedValue);
		}else {
			return encodedValue;
		}
	}
	
	public static String encode(String decodedValue){
		if(decodedValue!=null){
			byte[] encodedValue=Base64.encodeBase64(decodedValue.getBytes());
			return new String(encodedValue);
		}else {
			return decodedValue;
		}
	}

	public static String generateActivationCode() {
		return RandomStringUtils.random(10, 0, 20, true, true, "1qaz2xsw3dce4rft5vbg6nhy7ujm8kl9i0o".toCharArray());
	}
	

	public static String toCamelCase(String s) {
		String[] parts = s.split("_");
		String camelCaseString = "";
		for (String part: parts) {
			camelCaseString = camelCaseString + toProperCase(part);
		}
		return camelCaseString;
	}

	static public String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	
}
