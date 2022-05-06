package com.inn.counselling.security.spring;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.inn.counselling.security.mgmt.PasswordExpiryConstants;
import com.inn.counselling.security.mgmt.PasswordExpiryConstants.PersistDetailBeanCondition;
import com.inn.counselling.security.mgmt.PasswordExpiryDetailBean;
import com.inn.counselling.security.mgmt.PasswordExpiryLoadStoreService;
import com.inn.counselling.security.mgmt.PasswordFeatureBean;
import com.inn.counselling.security.mgmt.PasswordManagementService;
import com.inn.counselling.security.mgmt.PasswordValidationFailedException;
import com.inn.counselling.utils.DecryptUtil;
/**
 * A security filter which intercepts the authentication processing to update the new password to the 
 * database.
 */
public class ChangePasswordSecurityInterceptor implements Filter{

	private static final Logger LOGGER = Logger.getLogger(ChangePasswordSecurityInterceptor.class);

	/**
	 * represents the URL of page from where password shall be updated. 
	 */
	private String passwordUpdateUrl;
	/**
	 * represents the new password parameter which will be looked up in the request for new password value. 
	 */
	private String newPasswordParam;
	/**
	 * represents the URL of page where user control shall be redirected if password is not valid.
	 */
	private String updateFailRedirectUrl;
	/**
	 * represents the URL of page where user control shall be redirected after updating password.
	 */
	private String postUpdateRedirectUrl;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException 
	{
		LOGGER.info("Entry inside @class ChangePasswordSecurityInterceptor @method doFilter: ");
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		PasswordManagementService pms = PasswordManagementService.getInstance();

		if(pms == null){
			LOGGER.error("PasswordManagementService for further processing is not available.");
			throw new RuntimeException("PasswordManagementServiceBean available for processing.");
		}
		// process password management (Change Password) only if the request is from the configured
		// passwordUpdateUrl property.
		if(requiresPasswordMgmt(httpRequest, pms))
		{
			processPasswordChange(httpRequest, httpResponse, pms);
			return;
		}

		chain.doFilter(request, response);
	}
	/**
	 * Helper method which does the password change operation. It takes help of Password Management Service to
	 * complete this task. This method validates the password and redirect the control based on the validation result.
	 * If the validation is successful password is updated and control is redirected to postUpdateRedirectUrl otherwise
	 * error messages are shown on updateFailRedirectUrl.
	 * 
	 * @param httpRequest {@link HttpServletRequest} 
	 * @param httpResponse {@link HttpServletResponse}
	 * @param pms represent {@link PasswordManagementService} instance.
	 * @throws IOException 
	 * @throws ServletException
	 */
	private void processPasswordChange(HttpServletRequest httpRequest, HttpServletResponse httpResponse, PasswordManagementService pms)
			throws IOException, ServletException
	{
		// get the new password from the request identified by newPasswordParam
		String newPassword = httpRequest.getParameter(getNewPasswordParam());

		if(LOGGER.isInfoEnabled()){
			LOGGER.info("processing password change new password["+newPassword+"]");
		}

		String passphrase = "";
		String salt = httpRequest.getParameter("salt");
		String iv = httpRequest.getParameter("iv");
		String cookieName="JSESSIONID";
		Cookie cookies [] = httpRequest.getCookies();
		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++) 
			{				  
				if (cookies [i].getName().equals (cookieName))
				{
					passphrase=cookies[i].getValue();
					break;
				}
			}
		}
		newPassword=decryptCredentials(newPassword, passphrase, iv, salt);

		PasswordExpiryLoadStoreService loadStore = pms.getPasswordLoadStoreService();
		PasswordFeatureBean featureBean = loadStore.getPasswordFeatures();
		PasswordExpiryDetailBean detailBean = loadStore.getPasswordExpiryDetail();

		if(detailBean == null){
			detailBean = PasswordExpiryDetailBean.getNewDetailBean();
		}

		String redirectUrl;

		// update the detail bean with the new password, if new password is not valid
		// PasswordValidationFailedException will be thrown with appropriate error message
		// within. Redirect to the respective URL based on exception thrown.
		try
		{
			pms.updateDetailBean(detailBean, newPassword);
			loadStore.persist(detailBean, PersistDetailBeanCondition.AUTH_SUCCESS);
			redirectUrl = getPostUpdateRedirectUrl(pms, featureBean,detailBean);
		}
		catch(PasswordValidationFailedException pvfe)
		{   
			LOGGER.error("Error Inside  @class :"+this.getClass().getName()+" @Method :processPasswordChange()"+pvfe.getMessage());
			redirectUrl = getUpdateFailRedirectUrl();
			StringBuilder msgs = new StringBuilder();
			for(String msg : pvfe.getErrorMessages()){
				msgs.append(msg);
			}
			redirectUrl+="?"+PasswordExpiryConstants.ERROR_MESSAGES+"="+msgs.toString();
		}
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("Forwarding control to post update redirect url["+redirectUrl+"]");
		}

		httpResponse.sendRedirect(httpRequest.getContextPath() + redirectUrl);
	}

	private String decryptCredentials(String credential, String passphrase,
			String iv, String salt) {
		// TODO Auto-generated method stub
		LOGGER.info("Inside DecryptCredentials @param credential "+credential+" ,passphrase "+passphrase+" ,iv "+iv+" ,salt "+salt);
		try
		{
			LOGGER.info("Going to decrypt");
			String decryptCredential=DecryptUtil.decryptAESEncryptWithSaltAndIV(credential, passphrase, salt, iv);
			LOGGER.info("Decryption is successful with credential "+decryptCredential);
			return decryptCredential;
		} catch (Exception e) 
		{
			LOGGER.error("Error Inside  @class :"+this.getClass().getName()+" @Method :decryptCredentials()"+e.getMessage());
		}
		return credential;
	}

	/**
	 * checks is if the request is from the password update page. It check whether request is from the configured
	 * change password page and with screen information as update.
	 * @param request
	 * @param pms
	 * @return
	 */
	private boolean requiresPasswordMgmt(HttpServletRequest request, PasswordManagementService pms) 
	{
		String uri = request.getRequestURI();
		return uri.contains(getPasswordUpdateUrl());
	}
	public void init(FilterConfig arg0) throws ServletException 
	{
		// TODO Auto-generated method stub

	}
	public String getPasswordUpdateUrl() {
		return passwordUpdateUrl;
	}
	public void setPasswordUpdateUrl(String passwordUpdateUrl) {
		this.passwordUpdateUrl = passwordUpdateUrl;
	}

	public String getPostUpdateRedirectUrl() {
		return postUpdateRedirectUrl;
	}
	public void setPostUpdateRedirectUrl(String postUpdateRedirectUrl) {
		this.postUpdateRedirectUrl = postUpdateRedirectUrl;
	}
	public String getNewPasswordParam() {
		return newPasswordParam;
	}
	public void setNewPasswordParam(String newPasswordParam) {
		this.newPasswordParam = newPasswordParam;
	}
	public String getUpdateFailRedirectUrl() {
		return updateFailRedirectUrl;
	}
	public void setUpdateFailRedirectUrl(String updateFailRedirectUrl) {
		this.updateFailRedirectUrl = updateFailRedirectUrl;
	}
	public String getPostUpdateRedirectUrl(PasswordManagementService pms, PasswordFeatureBean featureBean, PasswordExpiryDetailBean detailBean) {
		String configuredUpdateUrl = getPostUpdateRedirectUrl();
		boolean isNotificationON = pms.getPasswordExpiryDataAnalyzer().isNotificationWindowOn(detailBean, featureBean);
		if(isNotificationON){
			int daysLeft = pms.getPasswordExpiryDataAnalyzer().daysLeftToExpire(detailBean, featureBean);
			configuredUpdateUrl+="?expiry_days_left="+daysLeft;
		}
		return configuredUpdateUrl;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}


}
