package com.inn.counselling.security.spring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.inn.counselling.security.authentication.ContextProvider;
import com.inn.counselling.security.mgmt.PasswordExpiryConstants;
import com.inn.counselling.security.mgmt.PasswordExpiryConstants.PostAuthenticationAction;
import com.inn.counselling.security.mgmt.PasswordManagementService;
import com.inn.counselling.utils.StringUtils;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

	private Map<String, String> redirectUrlMap;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)
					throws IOException, ServletException {
		
		LOGGER.debug("Entry inside @class CustomAuthenticationSuccessHandler @method onAuthenticationSuccess");
		
		if(logger.isDebugEnabled()){
			logger.debug("inside onAuthenticationSuccess()");
		}
		// get the password management service bean from the Spring context.
		PasswordManagementService pwdMgmtService = getPasswordMgmtServiceBean();

		if(pwdMgmtService!=null){
			System.out.println("pwdMgmtService is not null");
		}
		
		// password management service shall return additional parameters in the below map.
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// perform process method on password management service.
		PostAuthenticationAction action = pwdMgmtService.processSuccessfulAuthentication(null, returnMap);

		if(PostAuthenticationAction.LOCKED.equals(action)){
			processAlreadyLocked(request);
		}

		String redirectUrl = getRedirectUrl(action, returnMap);


		if(logger.isDebugEnabled()){
			logger.debug("returning redirectUrl["+redirectUrl+"] for action["+action+"]");
		}

		if(StringUtils.hasValue(redirectUrl))
		{	setDefaultTargetUrl(redirectUrl);}

		super.onAuthenticationSuccess(request, response, authentication);

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
			logger.warn("no map configured for property authSuccessUrlMap in security context.");
			throw new RuntimeException("no map configured for property authSuccessUrlMap in security context.");
		}

		// get the configured URL for action
		String url = getRedirectUrlMap().get(action.toString());

		if(!StringUtils.hasValue(url)){
			return null;
		}

		logger.debug("inside @class CustomAUthentication @method getRedirectUrl url "+url);

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

		if(logger.isDebugEnabled()){
			logger.debug("getRedirectUrl returning url["+urlBuffer.toString()+"] for action["+action+"] " +
					"& returnMap["+returnMap+"]");
		}

		return urlBuffer.toString();
	}
	private PasswordManagementService getPasswordMgmtServiceBean()
	{
		return (PasswordManagementService)ContextProvider.getContext().
				getBean(PasswordExpiryConstants.PSWD_MGMT_SERVICE_BEAN_NAME);
	}
	/**
	 * update the request sets a failed exception in the session.
	 * @param request
	 */
	private void processAlreadyLocked(HttpServletRequest request) 
	{
		// account is already locked
		// updated authentication(locked) exception details in session and redirect to configured URL.
		try {
			HttpSession session = request.getSession(false);
			if (session != null ) {
				session.invalidate();
			}
		}
		catch (Exception ignored) {
			logger.warn("processLocked - Ignoring exception["+ignored.getMessage()+"]");
		}
	}

	public Map<String, String> getRedirectUrlMap() {
		return redirectUrlMap;
	}

	public void setRedirectUrlMap(Map<String, String> redirectUrlMap) {
		this.redirectUrlMap = redirectUrlMap;
	}
}
