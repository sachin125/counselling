package com.inn.counselling.security.authentication;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.ValidationErrorList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.TextEscapeUtils;

import com.inn.counselling.utils.ConfigUtil;


public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationFilter.class);

	public CustomUsernamePasswordAuthenticationFilter() {
		super();
	}

	public static final String SPRING_SECURITY_FORM_DOMAIN_KEY = "domain";

	private PasswordEncoder passwordEncoder;

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	private String alwaysUseDefaultTargetUrl;

	private String authenticationFailureUrl;

	private String defaultTargetUrl;

	private String loginPage;
	
	public String getAlwaysUseDefaultTargetUrl() {
		return alwaysUseDefaultTargetUrl;
	}

	public void setAlwaysUseDefaultTargetUrl(String alwaysUseDefaultTargetUrl) {
		this.alwaysUseDefaultTargetUrl = alwaysUseDefaultTargetUrl;
	}

	public String getAuthenticationFailureUrl() {
		return authenticationFailureUrl;
	}

	public void setAuthenticationFailureUrl(String authenticationFailureUrl) {
		this.authenticationFailureUrl = authenticationFailureUrl;
	}

	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}
	
	protected String obtainDomain(HttpServletRequest request) {
		return request.getParameter(SPRING_SECURITY_FORM_DOMAIN_KEY);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		LOGGER.info("Entry inside @class usernameAuthenticationFilter @method attemptAuthentication");

		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}		
		String username = obtainUsername(request);
		String password =  obtainPassword(request);
		//String domain = obtainDomain(request);
		String domain = "admin";
		
		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}
		
		if (domain == null) {
			domain = "";
		}
		
		username = username.trim();
		domain = domain.trim();
				
		/*Encrypted Credential needs to be decrypt starts here*/

		String passphrase = "";
		String salt = request.getParameter("salt");
		String iv = request.getParameter("iv");

		String cookieName="JSESSIONID";
		Cookie[] cookies = request.getCookies();
		if (cookies != null){
			for (int i = 0; i < cookies.length; i++) {
				LOGGER.debug("cokkie name {} value {} ",cookies[i].getName(),cookies[i].getValue());
				if (cookies[i].getName().equals (cookieName)){
					passphrase=cookies[i].getValue();
					break;
				}
			}
		}
		LOGGER.debug("2 Entry inside @class usernameAuthenticationFilter @method attemptAuthentication @param username:{}  ,password {}, domain {} ,salt {} , iv {} , cookies {} passphrase {} "
				,username,password,domain,salt,iv,cookies,passphrase);

		username=ConfigUtil.decryptCredentials(username,passphrase,iv,salt);
		//domain=ConfigUtil.decryptCredentials(username,passphrase,iv,salt);
		password=ConfigUtil.decryptCredentials(password, passphrase, iv, salt);

		/*Encrypted Credentials needs to be decrypt ends here*/
		LOGGER.debug("3 Entry inside @class usernameAuthenticationFilter @method attemptAuthentication @param username:{}  ,password {}, domain {} "
				,username,password,domain);
		
		ValidationErrorList errorList = new ValidationErrorList();
		if(username.equals("")){
			username=ESAPI.validator().getValidInput("UserName",username, "FirstNameRegex", 255, false, errorList);
		}

		CustomUsernamePasswordAuthenticationToken customUsernamePasswordAuthenticationToken
				= new CustomUsernamePasswordAuthenticationToken(username,password,domain);
		// Place the last username attempted into HttpSession for views
		HttpSession session = request.getSession(false);

		if (session != null || getAllowSessionCreation()) {
			request.getSession().setAttribute("SPRING_SECURITY_LAST_USERNAME", TextEscapeUtils.escapeEntities(username));
		}

		// Allow subclasses to set the "details" property
		setDetails(request, customUsernamePasswordAuthenticationToken);

		Authentication authentication = this.getAuthenticationManager().authenticate(customUsernamePasswordAuthenticationToken);

		if(!(authentication instanceof  CustomUsernamePasswordAuthenticationToken )){
			throw new RuntimeException("Undesirable toke type");
		}
		return authentication;
	}


}
