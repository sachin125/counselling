package com.inn.counselling.security.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.inn.counselling.security.authentication.ContextProvider;
import com.inn.counselling.security.mgmt.PasswordExpiryConstants;
import com.inn.counselling.security.mgmt.PasswordExpiryConstants.PostAuthenticationAction;
import com.inn.counselling.security.mgmt.PasswordManagementService;
import com.inn.counselling.utils.ConfigUtil;
import com.inn.counselling.utils.StringUtils;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

	private Map<String, String> redirectUrlMap;
	/**
	 * @return returns the instance of PasswordManagementService configured in Spring application context.
	 */
	private PasswordManagementService getPasswordMgmtServiceBean()
	{
		return (PasswordManagementService)ContextProvider.getContext().
				getBean(PasswordExpiryConstants.PSWD_MGMT_SERVICE_BEAN_NAME);
	}
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
					throws IOException, ServletException 
	{
		LOGGER.debug("Entry inside @class CustomAuthenticationFailureHandler @method onAuthenticationFailure");		

		// do the normal authentication failure processing and get the url.
		// get the password management service bean from the Spring context.
		// process unsuccessful authentication.
		PasswordManagementService pwdMgmtService = getPasswordMgmtServiceBean();

		String passphrase = "";
		String salt = request.getParameter("salt");
		String iv = request.getParameter("iv");
		String cookieName="JSESSIONID";
		Cookie cookies [] = request.getCookies();
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

		String  username=ConfigUtil.decryptCredentials(request.getParameter("username"),passphrase,iv,salt);


		// user details
		Map<String, Object> customerInfo = new HashMap<String, Object>();
		customerInfo.put("username", username);

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("onAuthenticationFailure() - customerInfo["+customerInfo+"]");
		}

		// password management service shall return additional parameters in the below map.
		Map<String, Object> returnMap = new HashMap<String, Object>();
		PostAuthenticationAction action = pwdMgmtService.processUnSuccessfulAuthentication(customerInfo, returnMap);

		String failureUrl = getRedirectUrl(action, returnMap);

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("onAuthenticationFailure() - returning failure url ["+failureUrl+"]");
		}
		if(exception.getMessage().contains("disabled"))
		{
			String disableFailureUrl="/jsp/login.jsp?login_error=4";
			setDefaultFailureUrl(disableFailureUrl);
		}
		else
		{
			setDefaultFailureUrl(failureUrl);
		}

		super.onAuthenticationFailure(request, response, exception);
	}


	/**
	 * returns the redirect URL corresponding to given action and parameters in map as URL params.
	 * @param action
	 * @param returnMap
	 * @return
	 */
	private String getRedirectUrl(PostAuthenticationAction action, Map<String, Object> returnMap) 
	{
		if(getRedirectUrlMap() == null){
			LOGGER.warn("no map configured for property authSuccessUrlMap in security context.");
			throw new RuntimeException("no map configured for property authSuccessUrlMap in security context.");
		}

		// get the configured URL for action
		String url = getRedirectUrlMap().get(action.toString());
		if(!StringUtils.hasValue(url)){
			return null;
		}

		StringBuilder urlBuffer = new StringBuilder(url);

		if(returnMap != null){
			Set<String> keys = returnMap.keySet();
			for(String key : keys){
				if(urlBuffer.toString().contains("?")){
					urlBuffer.append("&"+key+"="+returnMap.get(key));
				}
				else{
					urlBuffer.append("?"+key+"="+returnMap.get(key));
				}
			}
		}

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("getRedirectUrl returning url["+urlBuffer.toString()+"] for action["+action+"] " +
					"& returnMap["+returnMap+"]");
		}

		return urlBuffer.toString();
	}

	public Map<String, String> getRedirectUrlMap() {
		return redirectUrlMap;
	}

	public void setRedirectUrlMap(Map<String, String> redirectUrlMap) {
		this.redirectUrlMap = redirectUrlMap;
	}
}
