package com.inn.counselling.security.spring;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public class CustomEntryPoint implements AuthenticationEntryPoint,InitializingBean{

	private static final Logger LOGGER=LoggerFactory.getLogger(CustomEntryPoint.class);

	private PortMapper portmapper = new PortMapperImpl();

	private PortResolver portResolver = new PortResolverImpl();

	private String loginFormUrl;

	private boolean forceHttps = false;

	private boolean useForward = false;

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  

	public CustomEntryPoint() {

	}

	public CustomEntryPoint(String loginFormUrl) {
		this.loginFormUrl = loginFormUrl;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(StringUtils.hasText(loginFormUrl) && UrlUtils.isValidRedirectUrl(loginFormUrl), "loginFormurl must be specified and must be a valid redirect url");
		if(useForward && UrlUtils.isAbsoluteUrl(loginFormUrl)) {
			throw new IllegalArgumentException("useforward must be false if using an absolete loginformurl");
		}
		Assert.notNull(portmapper, "portmapper must be specified");
		Assert.notNull(portResolver, "portResolver must be specified");
	}

	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException)
			throws IOException, ServletException {

		String requestUrI = httpServletRequest.getRequestURI().toLowerCase();
		httpServletRequest.getParameter("");
		LOGGER.debug("@param requestUrI and loginform "+requestUrI+"  "+getLoginFormUrl());
		HttpSession httpSession = httpServletRequest.getSession();
		if(requestUrI!=null && "counselling".contains(requestUrI)) {
			//httpSession.setAttribute(arg0, arg1);
		}
		String redirectUrl=null;
		LOGGER.debug("@param httpServletRequest.getScheme() "+httpServletRequest.getScheme());
		if(useForward) {
			if(forceHttps && "Http".equals(httpServletRequest.getScheme())) {
				redirectUrl = buildHttpsRedirectUrlForRequest(httpServletRequest);
			}
			if(redirectUrl == null) {
				String loginForm = determineUrlToUseForThisRequest(httpServletRequest, httpServletResponse, authenticationException);
				RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher(loginForm);
				dispatcher.forward(httpServletRequest, httpServletResponse);
				return;
			}
		} else {
			redirectUrl = buildRedirectUrlToLoginPage(httpServletRequest, httpServletResponse, authenticationException);
		}

/*		if (requestUrI.contains("jsp")){
			redirectUrl = getLoginFormUrl();
		}
*/		redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);
	}

	protected String buildRedirectUrlToLoginPage(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {

		String loginForm = determineUrlToUseForThisRequest(request, response, authException);

		if (UrlUtils.isAbsoluteUrl(loginForm)) {
			return loginForm;
		}

		int serverPort = portResolver.getServerPort(request);
		String scheme = request.getScheme();

		RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();

		urlBuilder.setScheme(scheme);
		urlBuilder.setServerName(request.getServerName());
		urlBuilder.setPort(serverPort);
		urlBuilder.setContextPath(request.getContextPath());
		urlBuilder.setPathInfo(loginForm);

		if (forceHttps && "http".equals(scheme)) {
			Integer httpsPort = portmapper.lookupHttpsPort(Integer.valueOf(serverPort));

			if (httpsPort != null) {
				urlBuilder.setScheme("https");
				urlBuilder.setPort(httpsPort.intValue());
			} else {
				LOGGER.warn("Unable to redirect to HTTPS as no port mapping found for HTTP port " + serverPort);
			}
		}

		return urlBuilder.getUrl();
	}

	private String buildHttpsRedirectUrlForRequest(HttpServletRequest httpServletRequest) {
		int serverport = portResolver.getServerPort(httpServletRequest);
		Integer httpsport = portmapper.lookupHttpsPort(serverport);
		if(httpsport!=null) {
			RedirectUrlBuilder redirectUrlBuilder = new RedirectUrlBuilder();
			redirectUrlBuilder.setScheme("https");
			redirectUrlBuilder.setServerName(httpServletRequest.getServerName());
			redirectUrlBuilder.setPort(httpsport);
			redirectUrlBuilder.setContextPath(httpServletRequest.getContextPath());
			redirectUrlBuilder.setServletPath(httpServletRequest.getServletPath());
			redirectUrlBuilder.setPathInfo(httpServletRequest.getPathInfo());
			redirectUrlBuilder.setQuery(httpServletRequest.getQueryString());
			return redirectUrlBuilder.getUrl();
		}
		LOGGER.warn("Unable to redirect to https as no port mapping found for http port "+serverport);
		return null;
	}

	protected String determineUrlToUseForThisRequest(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,AuthenticationException authenticationException) {
		return getLoginFormUrl();
	}

	public PortMapper getPortmapper() {
		return portmapper;
	}

	public void setPortmapper(PortMapper portmapper) {
		this.portmapper = portmapper;
	}

	public PortResolver getPortResolver() {
		return portResolver;
	}

	public void setPortResolver(PortResolver portResolver) {
		this.portResolver = portResolver;
	}

	public String getLoginFormUrl() {
		return loginFormUrl;
	}

	public void setLoginFormUrl(String loginFormUrl) {
		this.loginFormUrl = loginFormUrl;
	}

	public boolean isForceHttps() {
		return forceHttps;
	}

	public void setForceHttps(boolean forceHttps) {
		this.forceHttps = forceHttps;
	}

	public boolean isUseForward() {
		return useForward;
	}

	public void setUseForward(boolean useForward) {
		this.useForward = useForward;
	}

	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}




}
