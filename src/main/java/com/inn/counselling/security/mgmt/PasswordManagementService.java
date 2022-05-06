package com.inn.counselling.security.mgmt;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import com.inn.counselling.security.authentication.ContextProvider;
import com.inn.counselling.security.mgmt.PasswordExpiryConstants.PersistDetailBeanCondition;


/**
 * Manages password expiration, locking, first time login features.
 * Actions for password management is performed on successful and unsuccessful authentication.
 *  
 */

public class PasswordManagementService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordManagementService.class);
	/**
	 * represents a helper service which analyzes data for password management.
	 */
	private PasswordExpiryDataAnalyzerService passwordExpiryDataAnalyzer;
	/**
	 * represents a helper service which interacts with database to load and store password  
	 * management related data. 
	 */
	private PasswordExpiryLoadStoreService passwordLoadStoreService;
	/**
	 * represents a helper service which implements algorithm for managing password history.
	 */
	private PasswordHistoryService passwordHistoryService;
	
	public void init(){
		LOGGER.info("Entry inside @class PasswordManagementService @method init ");
		Assert.notNull(passwordExpiryDataAnalyzer,"passwordExpiryDataAnalyzer not configured with PasswordManagementService");
		Assert.notNull(passwordLoadStoreService,"passwordLoadStoreService not configured with PasswordManagementService");
		Assert.notNull(passwordHistoryService,"passwordHistoryService not configured with PasswordManagementService");
		LOGGER.debug("init() - Password management service initialized successfully.");
	}
	/**
	 * @return returns the instance of PasswordManagementService configured in Spring application context.
	 */
	public static PasswordManagementService getInstance(){
		return (PasswordManagementService)ContextProvider.getContext().getBean("passwordMgmtServiceBean");
	}
	/**
	 * place holder method which may free resources when the bean is destroyed. 
	 */
	public void destroy(){

	}

	/**
	 * This is a facade method which abstracts password management actions performed when user is logged in successfully.
	 * This method is responsible for determining the action that needs to be performed depending on the users
	 * current state. The action is returned in form of PostAuthenticationAction enumeration.
	 * 
	 * Also, this method copies extra information in the passed map which may be further used to show details.
	 * 
	 * For example:
	 * When this service determines the user's password is going to expire and user has to be alerted. This service returns
	 * PostAuthenticationAction.NOTIFY action with {@link PasswordExpiryConstants}.FAILED_LOGIN_ATTEMPT parameter
	 * set in returnParams with days left for password to expire.  
	 *  
	 * @param returnParams 
	 * @return 
	 */
	public PasswordExpiryConstants.PostAuthenticationAction processSuccessfulAuthentication(Map<String, String> customerInfo, Map<String, Object> returnParams){
		
		LOGGER.info("Entry inside @class PasswordManagementService @method processSuccessfulAuthentication ");
		PasswordFeatureBean featureBean = getPasswordFeatureBean();
		PasswordExpiryDataAnalyzerService passwordExpiryDataAnalyzerService =  getPasswordExpiryDataAnalyzer();
		LOGGER.debug("processSuccessfulAuthentication() - featureBean["+featureBean+"]");

		if(!passwordExpiryDataAnalyzerService.isPasswordExpiryOn(null, featureBean))
		{

			LOGGER.debug("processSuccessfulAuthentication() - featureBean null or password expiry is turned off.");

			LOGGER.debug("processSuccessfulAuthenticaiton - returning["+PasswordExpiryConstants.PostAuthenticationAction.NORMAL+"]");

			return PasswordExpiryConstants.PostAuthenticationAction.NORMAL;
		}

		// get the password expiration details associated to the current user. Since the user is authenticated successfully
		// user information shall be fetched from the user context. 
		// If user is logging in for the first time this will be null otherwise it will contain details.
		PasswordExpiryDetailBean detailBean = getPasswordDetailBean();


		LOGGER.debug("processSuccessfulAuthenticaiton - detailBean["+detailBean+"]");

		// now since authentication is successful and password expiration is turned ON, we must
		// reset the FAILED_ATTEMPTS value to 0 if in the present bean detail its value is greater than 1.
		if(detailBean != null && detailBean.getFailedAttemptsCount() > 0){
			LOGGER.debug("processSuccessfulAuthenticaiton - reseting FAILED_ATTEMPTS value.");
			detailBean.setProperty(PasswordExpiryConstants.FAILED_LOGIN_ATTEMPTS, 0);
			getPasswordLoadStoreService().persist(detailBean, PersistDetailBeanCondition.ATTEMPT_RESET);
		}

		// In the following order next action shall be determined
		// - Check if account is locked, user shall be thrown back to login.
		// - If user's password is expired, ask him to change the password.
		// - If user is logging in for first time with this feature ON, ask him to change the password.
		// - If notification is ON show him the notification message to change password ASAP.
		// - Otherwise normal flow of execution to main page of application.

		if(passwordExpiryDataAnalyzerService.isAccountLocked(detailBean))
		{
			// account is locked
			LOGGER.debug("processSuccessfulAuthenticaiton - returning["+PasswordExpiryConstants.PostAuthenticationAction.LOCKED+"]");
			return PasswordExpiryConstants.PostAuthenticationAction.LOCKED;
		}
		else if (passwordExpiryDataAnalyzerService.isPasswordExpired(detailBean, featureBean))
		{

			LOGGER.debug("processSuccessfulAuthenticaiton - returning["+PasswordExpiryConstants.PostAuthenticationAction.PASSWORD_EXPIRED+"]");
			return PasswordExpiryConstants.PostAuthenticationAction.PASSWORD_EXPIRED;
		}
		else if (passwordExpiryDataAnalyzerService.isFirstTimeLogin(null, featureBean, detailBean))
		{
			// first time login
			LOGGER.debug("processSuccessfulAuthenticaiton - returning["+PasswordExpiryConstants.PostAuthenticationAction.FIRST_TIME_LOGIN+"]");

			return PasswordExpiryConstants.PostAuthenticationAction.FIRST_TIME_LOGIN;
		}
		else if (passwordExpiryDataAnalyzerService.isNotificationWindowOn(detailBean, featureBean))
		{
			// first time login
			if(returnParams != null){
				returnParams.put(PasswordExpiryConstants.EXPIRY_DAYS_LEFT, getPasswordExpiryDataAnalyzer().daysLeftToExpire(detailBean,featureBean));
			}

			LOGGER.debug("processSuccessfulAuthenticaiton - returning["+PasswordExpiryConstants.PostAuthenticationAction.NOTIFY+"] returnParams"+ returnParams);

			return PasswordExpiryConstants.PostAuthenticationAction.NOTIFY;
		}
		else
		{
			LOGGER.debug("processSuccessfulAuthenticaiton - returning["+PasswordExpiryConstants.PostAuthenticationAction.NORMAL+"]");

			return PasswordExpiryConstants.PostAuthenticationAction.NORMAL;
		}
	}
	/**
	 * This is a facade method which abstracts password management actions performed when user is not authenticated
	 * successfully. This method is responsible for determining the action that needs to be performed depending on the users
	 * current state. The action is returned in form of PostAuthenticationAction enumeration. 
	 * 
	 * Since user is not authenticated API user must provide customer information namely its userid and domainid 
	 * with which he tried to log in. 
	 * 
	 * Also, this method copies extra information in the passed map which may be further used to show details.
	 * 
	 * @param customerInfo
	 * @param returnMap
	 * @return
	 */
	public PasswordExpiryConstants.PostAuthenticationAction processUnSuccessfulAuthentication(Map<String, Object> customerInfo, 
			Map<String, Object> returnMap) 
	{
LOGGER.info("Entry inside @class PasswordManagementService @method processUnSuccessfulAuthentication ");

		// get the feature bean associated to the provided customer information.
		PasswordFeatureBean featureBean = getPasswordFeatureBean(customerInfo);
		PasswordExpiryDataAnalyzerService passwordExpiryDataAnalyzerService =  getPasswordExpiryDataAnalyzer();
		LOGGER.debug("processUnSuccessfulAuthentication() - featureBean["+featureBean+"]");


		if(!passwordExpiryDataAnalyzerService.isPasswordExpiryOn(customerInfo, featureBean))
		{
			if(userExists(customerInfo))
			{
				LOGGER.debug("processUnSuccessfulAuthentication() - featureBean null and user exist.");
				LOGGER.debug("processUnSuccessfulAuthentication() - returning["+PasswordExpiryConstants.PostAuthenticationAction.NORMAL_AUTH_ERR+"]");

				return PasswordExpiryConstants.PostAuthenticationAction.NORMAL_AUTH_ERR;
			}
			else
			{
				if(returnMap !=null){
					returnMap.put("username", customerInfo.get("username"));
				}

				LOGGER.debug("processUnSuccessfulAuthentication() - featureBean null and no such user exist");
				LOGGER.debug("processUnSuccessfulAuthentication() - returning["+PasswordExpiryConstants.PostAuthenticationAction.ERR_USER_NOT_EXIST+"] returnMap["+returnMap+"]");

				return PasswordExpiryConstants.PostAuthenticationAction.ERR_USER_NOT_EXIST;
			}
		}
		else
		{ 
			// only if the password expiration feature is turned ON log the locking details or take appropriate
			// action, other wise when it is off no matter if user is locked resume a normal flow, sending user
			// back to authentication failure screen.

			// get the detail associated to password expiration, if no such information is locked yet i.e.
			// user has not logged in and he started with a failure get a fresh defaulted detail bean.
			PasswordExpiryDetailBean detailBean = getPasswordDetailBean(customerInfo);
			LOGGER.debug("processUnSuccessfulAuthentication - detailBean["+detailBean+"]");

			if(detailBean == null){
				detailBean = PasswordExpiryDetailBean.getNewDetailBean();
			}

			// if account is already locked send user back to locked screen
			// other wise increment the attempts number to user's password expiration details.
			if(passwordExpiryDataAnalyzerService.isAccountLocked(detailBean)){
				return PasswordExpiryConstants.PostAuthenticationAction.LOCKED;
			}

			// update the detail bean and find how many attempts user can again make before getting 
			// the account locked.
			int attemptsLeft = passwordExpiryDataAnalyzerService.updateDetailBeanForAuthFail(detailBean);

			LOGGER.debug("processUnSuccessfulAuthentication() - updated detailBean["+detailBean+"]");


			if(attemptsLeft < 0)
			{
				LOGGER.debug("processUnSuccessfulAuthentication() - returning already locked["+PasswordExpiryConstants.PostAuthenticationAction.LOCKED+"]");

				return PasswordExpiryConstants.PostAuthenticationAction.LOCKED;
			}
			else if(attemptsLeft == 0)
			{
				LOGGER.debug("processUnSuccessfulAuthentication() - locking and returning["+PasswordExpiryConstants.PostAuthenticationAction.LOCKED+"]");

				getPasswordLoadStoreService().persist(customerInfo, detailBean, PersistDetailBeanCondition.AUTH_FAILURE);
				return PasswordExpiryConstants.PostAuthenticationAction.LOCKED;
			}
			else
			{
				getPasswordLoadStoreService().persist(customerInfo, detailBean, PersistDetailBeanCondition.AUTH_FAILURE);
				returnMap.put(PasswordExpiryConstants.ATTEMPTS_LEFT, attemptsLeft);
				LOGGER.debug("processUnSuccessfulAuthentication() - returning["+PasswordExpiryConstants.PostAuthenticationAction.LOCKED+"] returnMap["+returnMap+"]");
				return PasswordExpiryConstants.PostAuthenticationAction.NORMAL_AUTH_ERR;
			}
		}
	}
	/**
	 * returns true if the <userid, domainid> pair exists otherwise returns false.
	 * @param customerInfo
	 * @return
	 */
	private boolean userExists(Map<String, Object> customerInfo) 
	{
		LOGGER.debug("userExists() - customerInfo["+customerInfo+"]");

		Map<String, Object> userDetails = getPasswordLoadStoreService().getUserDetails(customerInfo);

		LOGGER.debug("userExists() - userDetails["+userDetails+"]");
		if(userDetails == null || userDetails.isEmpty()){
			return false;
		}
		return true;
	}
	/**
	 * returns the updated bean.
	 * @param detailBean
	 * @param newPassword
	 * @param pms
	 * @return
	 */
	public PasswordExpiryDetailBean updateDetailBean(PasswordExpiryDetailBean detailBean, String newPassword) 
	{
		PasswordHistoryService historyService = this.getPasswordHistoryService();

		// fetch the history from detail bean.
		String history = (String) detailBean.getProperty(PasswordExpiryConstants.PASSWORD_HISTORY);

		newPassword=new BCryptPasswordEncoder().encode(newPassword);
		// validate the password i.e. apply complexity check and history check
		List<String> errorMsgs = historyService.validatePassword(history, newPassword);


		if(errorMsgs != null){
			throw new PasswordValidationFailedException(errorMsgs);
		}

		// when no error messages
		// updated history, last updated date and set failed attempts to 0 since 
		// user has successfully logged in.
		String newHistory = historyService.updateHistory(history, newPassword);
		detailBean.setProperty(PasswordExpiryConstants.PASSWORD_HISTORY, newHistory);
		detailBean.setProperty(PasswordExpiryConstants.PASSWORD, newPassword);

		detailBean.setProperty(PasswordExpiryConstants.FAILED_LOGIN_ATTEMPTS, 0);

		java.sql.Timestamp lastUpdatedDate = new Timestamp(new Date().getTime());
		detailBean.setProperty(PasswordExpiryConstants.LAST_UPDATED_DT, lastUpdatedDate);
		detailBean.setProperty(PasswordExpiryConstants.FIRST_TIME_CHANGE, 1);

		return detailBean;
	}
	/**
	 * @return returns instance of {@link PasswordExpiryDataAnalyzerService}
	 */
	public PasswordExpiryDataAnalyzerService getPasswordExpiryDataAnalyzer() {
		return passwordExpiryDataAnalyzer;
	}
	/**
	 * configures {@link PasswordExpiryDataAnalyzerService} instance with the management service.
	 * @param passwordExpiryDataAnalyzer
	 */
	public void setPasswordExpiryDataAnalyzer(
			PasswordExpiryDataAnalyzerService passwordExpiryDataAnalyzer) {
		this.passwordExpiryDataAnalyzer = passwordExpiryDataAnalyzer;
	}
	/**
	 * @return returns instance of {@link PasswordExpiryLoadStoreService}
	 */
	public PasswordExpiryLoadStoreService getPasswordLoadStoreService() {
		return passwordLoadStoreService;
	}
	/**
	 * configures {@link PasswordExpiryLoadStoreService} instance with the management service.
	 * @param passwordLoadStoreService
	 */
	public void setPasswordLoadStoreService(
			PasswordExpiryLoadStoreService passwordLoadStoreService) {
		this.passwordLoadStoreService = passwordLoadStoreService;
	}
	/**
	 * @return returns the feature bean associated to current customer context.
	 */
	private PasswordFeatureBean getPasswordFeatureBean()
	{
		return getPasswordLoadStoreService().getPasswordFeatures();
	}
	/**
	 * returns the feature bean associated to customer context passed as parameter.
	 * @param params represents the customer context. This must contain workingdomain, userid and domainid information.
	 * @return
	 */
	private PasswordFeatureBean getPasswordFeatureBean(Map<String, Object> params)
	{
		return getPasswordLoadStoreService().getPasswordFeatures(params);
	}
	/**
	 * @return password expiration details associated to the current customer context.
	 */
	private PasswordExpiryDetailBean getPasswordDetailBean()
	{
		return getPasswordLoadStoreService().getPasswordExpiryDetail();
	}
	/**
	 * returns the password expiration details associated to customer context passed as parameter.
	 * @param params represent the customer context. This must contain customerid, userid and working domain information.
	 * @return
	 */
	private PasswordExpiryDetailBean getPasswordDetailBean(Map<String, Object> params)
	{
		return getPasswordLoadStoreService().getPasswordExpiryDetail(params);
	}
	/**
	 * @return instance of configured {@link PasswordHistoryService} 
	 */
	public PasswordHistoryService getPasswordHistoryService() {
		return passwordHistoryService;
	}
	/**
	 * @param passwordHistoryService
	 */
	public void setPasswordHistoryService(
			PasswordHistoryService passwordHistoryService) {
		this.passwordHistoryService = passwordHistoryService;
	}
	

	public void addSoxCompliance(Map<String, Object> params)
	{		
		passwordExpiryDetailsEntry(params);
		getPasswordLoadStoreService().setPasswordFeatures(params);
	}
	public void addSoxCompliancebyDomain(Map<String, Object> params)
	{		
		passwordExpiryDetailsEntry(params);
		getPasswordLoadStoreService().setPasswordFeaturesbyDomain(params);
	}

	public void passwordExpiryDetailsEntry(Map<String, Object> params){
		LOGGER.info("Inside  @class"+this.getClass().getName()+" @Method :addSoxCompliance @param: newPassword");
		try{
			PasswordManagementService pms = PasswordManagementService.getInstance();
			PasswordExpiryLoadStoreService loadStore = pms.getPasswordLoadStoreService();
			Map<String, Object> paramExp=new HashMap();
			paramExp.put("username", params.get("provisionedUser"));
			paramExp.put("domainname", params.get("domainname"));
			paramExp.put("history", null);
			PasswordExpiryDetailBean detailBean = loadStore.getPasswordExpiryDetail( paramExp);
			if(detailBean == null){
				detailBean = PasswordExpiryDetailBean.getNewDetailBean();
			}
			try
			{
				loadStore.persist(paramExp,detailBean, PersistDetailBeanCondition.ATTEMPT_RESET);
			}
			catch(PasswordValidationFailedException pvfe)
			{
				LOGGER.error("Error Inside  @class :"+this.getClass().getName()+" @Method :passwordExpiryDetailsEntry()"+pvfe.getMessage());
				StringBuilder msgs = new StringBuilder();
				for(String msg : pvfe.getErrorMessages()){
					msgs.append(msg);
				}


				throw new Exception(""+msgs);
			}
		}catch(Exception e){
			LOGGER.error("Error occurred  @class"+this.getClass().getName()+" @Method : addSoxCompliance ",e);
		}
	}
}
