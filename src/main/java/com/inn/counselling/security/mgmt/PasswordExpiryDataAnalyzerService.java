package com.inn.counselling.security.mgmt;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.inn.counselling.security.authentication.CustomerInfo;
import com.inn.counselling.utils.ConfigUtil;
import com.inn.counselling.utils.StringUtils;

/**
 * Analyses {@link PasswordExpiryDetailBean} and {@link CustomerContext} and generates various
 * interpretations for managing passwords. Also, this service assists {@link PasswordManagementService} in management
 * of passwords and related features.
 * 
 */
public class PasswordExpiryDataAnalyzerService {

	private static final Logger LOGGER = Logger.getLogger(PasswordExpiryDataAnalyzerService.class);

	/**
	 * represents the duration when notifications shall be enabled to reset password prior to password expiration.
	 */
	private int expiryNotificationDuration;
	/**
	 * represents the default interval in days when password shall expired, and user shall be force to change the password.
	 */
	private int defaultExpiryInterval;
	/**
	 * represents the maximum number of login attempts an user can make, exceeding this limit will result in locking of account.
	 */
	private int maxAttempts;
	/**
	 * represents the comma separated list of organizations for which password expiry feature shall be forced to be ON.
	 */
	private String orgList;
	/**
	 * indicates the password will expire never, which means password expiration is turned
	 * off. This represent an indicator which shall be used in configuration as is. 
	 */
	private int neverExpireIndicator;
	/**
	 * indicates the password default expire value, in this case defaultExpiryInterval value
	 * shall be used. 
	 */
	private int defaultExpireIndicator;

	/**
	 * returns true if organization associated to current user is configured to force on the
	 * password expiration features. 
	 *
	 * @param custInfoMap represents customer information, refer {@link CustomerInfo}
	 * getCustomerInfo API.
	 * @return true, if is pwd features forced on
	 */

	private boolean isPwdFeaturesForcedOn(Map<String, Object> custInfoMap){
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method isPwdFeaturesForcedOn @param:custInfoMap "+custInfoMap);
		if(custInfoMap == null){
			custInfoMap = new HashMap<String, Object>(CustomerInfo.getCustomerInfo());
		}
		String org = (String)custInfoMap.get("ORGNAME");

		LOGGER.debug("isPwdFeatureForceOn org["+org+"] configuredOrgList["+getOrgList()+"]");

		// checks whether the organization present in the customer context is configured to
		// have password expiration ON by default
		// orgList is comma separated list. 

		if( StringUtils.hasValue(getOrgList()) && StringUtils.hasValue(org))
		{
			String[] orgs = getOrgList().split(",");
			for (int i = 0; orgs!=null && i < orgs.length; i++) 
			{
				if(org.equalsIgnoreCase(orgs[i]))
				{
					LOGGER.debug("isPwdFeatureForceOn returning true");
					return true;
				}
			}
		}

		LOGGER.debug("isPwdFeatureForceOn returning false");

		return false;
	}

	/**
	 * returns true if the password expiration feature is ON in configuration. Also this returns true when the
	 * current user belong to an organization which is marked to be forced ON.
	 * refer: isPwdFeaturesForcedOn API this is used internally.
	 *
	 * @param custInfoMap represents customer information, refer {@link CustomerInfo}
	 * getCustomerInfo API.  
	 * @param featureBean represents password features holder, refer {@link PasswordFeatureBean}.
	 * @return true, if is password expiry on
	 */
	public boolean isPasswordExpiryOn(Map<String, Object> custInfoMap, PasswordFeatureBean featureBean){
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method isPasswordExpiryOn @param:custInfoMap "+custInfoMap);

		if(isPwdFeaturesForcedOn(custInfoMap)){
			LOGGER.debug(" In isPwdFeaturesForcedOn");
			return true;
		}

		if(featureBean == null){
			LOGGER.debug("In featureBean is null");
			return false;
		}

		int passwordExpiryInterval = featureBean.getPasswordExpiryVal();

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("passwordExpiryInterval["+passwordExpiryInterval+"]");
		}

		if(passwordExpiryInterval == getNeverExpireIndicator()){
			LOGGER.debug("passwordExpiryInterval == getNeverExpireIndicator()");
			return false;
		}

		return true;
	}

	/**
	 * returns true if the first time login feature ON in configuration and user is logging in for the first
	 * time.  
	 * also refer: isPwdFeaturesForcedOn API this is used internally.
	 *
	 * @param custInfoMap represents customer information, refer {@link CustomerInfo}
	 * getCustomerInfo API.  
	 * @param featureBean represents password features holder, refer {@link PasswordFeatureBean}.
	 * @param detailBean the detail bean
	 * @return true, if is first time login
	 */
	public boolean isFirstTimeLogin(Map<String, Object> custInfoMap, PasswordFeatureBean featureBean, PasswordExpiryDetailBean detailBean){
	
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method isFirstTimeLogin @param:custInfoMap "+custInfoMap);
		// check if password expiration is on and user is logging in for the first time
		// if both are true return true otherwise a false.

		boolean expFeature = isPasswordExpiryOn(custInfoMap,featureBean);

		if(expFeature && featureBean == null & detailBean == null){
			return true;
		}

		boolean pwdFeature = true; 

		// If password feature is forced on then overlooked the feature bean configuration
		if(featureBean != null && !isPwdFeaturesForcedOn(custInfoMap)){
			pwdFeature = featureBean.isFirstTimeLoginFeature();
		}

		boolean ftl = false;
		if(detailBean == null){
			ftl = false;
		}
		else{
			ftl = detailBean.isFirstTimeLogin(); 
		}


		LOGGER.debug("isFirstTimeLogin detailBean["+detailBean+"][(expFeature("+expFeature+") && pwdFeature("+pwdFeature+")&& ftl("+ftl+")] ["+(expFeature && pwdFeature && !ftl)+"]");


		return expFeature && pwdFeature && !ftl;
	}

	/**
	 * returns the password expiration duration.
	 *
	 * @param featureBean the feature bean
	 * @return the password expiry duration
	 */
	public int getPasswordExpiryDuration(PasswordFeatureBean featureBean)
	{
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method getPasswordExpiryDuration ");

		if(featureBean == null){
			return getDefaultExpiryInterval();
		}

		int passwordExpiryDuration = featureBean.getPasswordExpiryVal();

		if(passwordExpiryDuration == getDefaultExpireIndicator()){
			passwordExpiryDuration = getDefaultExpiryInterval();
		}

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("getPasswordExpiryDuration returning ["+passwordExpiryDuration+"]");
		}

		return passwordExpiryDuration;
	}

	/**
	 * returns true if account is already locked otherwise return false.ory
	 *
	 * @param detailBean the detail bean
	 * @return true, if is account locked
	 */
	public boolean isAccountLocked(PasswordExpiryDetailBean detailBean){
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method isAccountLocked ");
		// by default account locking is assumed to be unlocked.
		if(detailBean == null){
			return false;
		}

		boolean result = detailBean.isLocked();

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("returning isAccountLocked["+result+"]");
		}

		return result;
	}   
	/**
	 * returns true if the current request is unsuccessful login and has reached the max allowed
	 * attempts of unsuccessful login.
	 *
	 * @param detailBean the detail bean
	 * @param isCurrentUnsuccessfulAttempt the is current unsuccessful attempt
	 * @return true, if is last attempt
	 */
	public boolean isLastAttempt(PasswordExpiryDetailBean detailBean, boolean isCurrentUnsuccessfulAttempt){

		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method isLastAttempt ");

		if(detailBean == null){
			LOGGER.error("invalid parameter value detailBean[null]");
			throw new IllegalArgumentException("invalid parameter detailBean null");
		}

		// if current is a successful attempt the account wont be locked and also user can try again.
		if(!isCurrentUnsuccessfulAttempt){
			return false;
		}

		int failedAttemptCount = detailBean.getFailedAttemptsCount();

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Max Login Attempt["+getMaxAttempts()+"] & Current Failed Count["+failedAttemptCount+"]");
		}

		if((failedAttemptCount+1) == getMaxAttempts()){
			return true;
		}

		return false;
	}
	/**
	 * returns true if notification for changing password shall be switched on otherwise returns false.
	 *
	 * @param detailBean the detail bean
	 * @param featureBean the feature bean
	 * @return true, if is notification window on
	 */
	public boolean isNotificationWindowOn(PasswordExpiryDetailBean detailBean, PasswordFeatureBean featureBean)
	{

		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method isNotificationWindowOn ");

		if(detailBean == null || !isPasswordExpiryOn(null, featureBean)){
			return false;
		}

		int expiryInDays = getPasswordExpiryDuration(featureBean);

		java.sql.Timestamp timeStamp = (java.sql.Timestamp) detailBean.getProperty(PasswordExpiryConstants.LAST_UPDATED_DT);

		if(timeStamp == null){
			throw new IllegalArgumentException("Invalid value for last updated date[null]");
		}

		long lastUpdateDateLong = timeStamp.getTime();
		Date lastUpdateDate = new Date(lastUpdateDateLong);


		// get the password expiry duration 
		int notifyDays = getExpiryNotificationDuration();

		// calculate the date when notifications shall be switched on.
		Calendar calendar = Calendar.getInstance();

		// set the current time to the date on which password was last updated.
		calendar.setTime(lastUpdateDate);

		// calculate the date on which password shall expire.
		calendar.add(Calendar.DATE, expiryInDays);

		// finally get the date on which notification shall switch on.
		calendar.add(Calendar.DATE, - notifyDays);
		Date notifyONDate = calendar.getTime();

		// get the current date
		Date now = new Date();

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Notification On Date ["+notifyONDate+"], Last Updated Date["+lastUpdateDate+"], Now["+now+"], " +
					"Notify Days Window["+notifyDays+"] and Password Expiry Duration["+expiryInDays+"] " +
					"returning["+(notifyONDate.getTime() <= now.getTime())+"]");
		}

		return notifyONDate.getTime() <= now.getTime();
	}
	/**
	 * returns the number of days left for password to expire.
	 * @param detailBean
	 * @param featureBean
	 * @return
	 */
	public int daysLeftToExpire(PasswordExpiryDetailBean detailBean, PasswordFeatureBean featureBean)
	{
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method daysLeftToExpire ");

		if(detailBean == null || !isPasswordExpiryOn(null, featureBean)){
			return -1;
		}

		int expiryInDays = getPasswordExpiryDuration(featureBean);

		java.sql.Timestamp timeStamp = (java.sql.Timestamp) detailBean.getProperty(PasswordExpiryConstants.LAST_UPDATED_DT);

		if(timeStamp == null){
			throw new IllegalArgumentException("Invalid value for last updated date[null]");
		}

		long lastUpdateDateLong = timeStamp.getTime();
		Date lastUpdateDate = new Date(lastUpdateDateLong);


		// calculate the date when notifications shall be switched on.
		Calendar calendar = Calendar.getInstance();

		// set the current time to the date on which password was last updated.
		calendar.setTime(lastUpdateDate);

		// calculate the date on which password shall expire.
		calendar.add(Calendar.DATE, expiryInDays);

		Date expiryONDate = calendar.getTime();

		// get the current date
		Date now = new Date();
		int daystoexpire;
		if(expiryONDate.getTime() < now.getTime()){
			daystoexpire = 0;
		}
		else{
			daystoexpire = (int)Math.ceil(((double)(expiryONDate.getTime() - now.getTime())/(double)(24*60*60*1000)));
		}

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Expiry On Date ["+expiryONDate+"], Last Updated Date["+lastUpdateDate+"], Now["+now+"], " +
					"and Days to expire["+daystoexpire+"] ");
		}

		return daystoexpire;
	}

	/**
	 * update the detail bean for authentication failure scenario. When authentication fails increment the
	 * failed attempts count by and lock the current user account if MAX is reached.
	 * 
	 * Also, this returns 
	 *
	 * @param detailBean the detail bean
	 * @return the int
	 */
	public int updateDetailBeanForAuthFail(PasswordExpiryDetailBean detailBean) 
	{
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method updateDetailBeanForAuthFail ");

		int attemptsMade = detailBean.getFailedAttemptsCount();
		attemptsMade++;

		detailBean.setProperty(PasswordExpiryConstants.FAILED_LOGIN_ATTEMPTS, attemptsMade);

		if(getMaxAttempts() == attemptsMade){
			detailBean.setProperty(PasswordExpiryConstants.LOCKED, 1);
		}

		return getMaxAttempts() - attemptsMade;
	}

	/**
	 * returns true if password has expired otherwise return false.
	 *
	 * @param detailBean the detail bean
	 * @param featureBean the feature bean
	 * @return true, if is password expired
	 */
	public boolean isPasswordExpired(PasswordExpiryDetailBean detailBean, PasswordFeatureBean featureBean){
		LOGGER.info("Entry inside @class PasswordExpiryDataAnalyzerService @method isPasswordExpired ");
		// if no this feature is not configured then return false as the default behaviour.
		// also return a false if feature is configured to never expire value.
		if(!isPasswordExpiryOn(null, featureBean) || getPasswordExpiryDuration(featureBean) == getNeverExpireIndicator()){
			return false;
		}

		// null detailbean is assumed to first time login, so in this case return a false.
		if(detailBean == null){
			return false;
		}

		int expiryInDays = getPasswordExpiryDuration(featureBean);
		java.sql.Timestamp timeStamp = (java.sql.Timestamp) detailBean.getProperty(PasswordExpiryConstants.LAST_UPDATED_DT);

		if(timeStamp == null){
			return false;
		}

		long lastUpdateDateLong = timeStamp.getTime();
		Date lastUpdateDate = new Date(lastUpdateDateLong);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastUpdateDate);
		calendar.add(Calendar.DATE, expiryInDays);
		Date expiryDate = calendar.getTime();
		Date now = new Date();

		if(expiryDate.getTime() <= now.getTime()){
			return true;
		}
		return false;
	}
	/**
	 * @return returns password expiration duration in days as integer.
	 */
	public int getExpiryNotificationDuration() 
	{
		return expiryNotificationDuration;
	}
	/**
	 * sets the provided expiration notification duration as integer.
	 * 
	 * @param expiryNotificationDuration 
	 */
	public void setExpiryNotificationDuration(int expiryNotificationDuration) 
	{
		this.expiryNotificationDuration = expiryNotificationDuration;

	}
	/**
	 * @return returns default interval after which password will expire.
	 */
	public int getDefaultExpiryInterval() 
	{
		return defaultExpiryInterval;
	}
	/**
	 * sets the default interval in days after which password shall be assumed to 
	 * be expired.
	 * @param defaultExpiryInterval
	 */
	public void setDefaultExpiryInterval(int defaultExpiryInterval)
	{
		this.defaultExpiryInterval = defaultExpiryInterval;

	}
	/**
	 * @return returns the maximum number of attempts an user can make to login before
	 * getting the account locked.
	 */
	public int getMaxAttempts()
	{
		return maxAttempts;
	}
	/**
	 * sets the maximum number of attempts an user can make to login before
	 * getting the account locked.
	 * @param maxAttempts
	 */
	public void setMaxAttempts(int maxAttempts)
	{
		String attemptsare = ConfigUtil.getConfigProp(ConfigUtil.MAX_ATTEMPTS);
		this.maxAttempts = Integer.parseInt(attemptsare);
	}
	/**
	 * @return returns the comma separated list of organizations for which password 
	 * expiration feature shall be forced ON.
	 */
	public String getOrgList() 
	{
		return orgList;
	}
	/**
	 * sets the comma separated list of organizations for which password 
	 * expiration feature shall be forced ON.
	 * @param orgList
	 */
	public void setOrgList(String orgList) 
	{
		this.orgList = orgList;
	}

	public int getNeverExpireIndicator() 
	{
		return neverExpireIndicator;
	}
	public void setNeverExpireIndicator(int neverExpireIndicator) 
	{
		this.neverExpireIndicator = neverExpireIndicator;
	}
	public int getDefaultExpireIndicator() 
	{
		return defaultExpireIndicator;
	}
	public void setDefaultExpireIndicator(int defaultExpireIndicator) 
	{
		this.defaultExpireIndicator = defaultExpireIndicator;
	}

}
