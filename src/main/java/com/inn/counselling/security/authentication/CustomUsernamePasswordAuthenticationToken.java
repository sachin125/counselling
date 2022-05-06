package com.inn.counselling.security.authentication;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * AdvAuthenticationToken serves as the Adapter
 * for the original authentication token. Whenever default
 * functionality is desired we would delegate it to the adaptiveAuthenticationToken.
 * In case we require storing the domain name etc. we would set it over here. 
 */

public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken{

	private static final Logger LOGGER =  LoggerFactory.getLogger(CustomUsernamePasswordAuthenticationToken.class);

	private String domainName;

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public CustomUsernamePasswordAuthenticationToken(Object principal,Object credentials,Collection<GrantedAuthority> authorities){
		super(principal,credentials,authorities);
		LOGGER.info("1 Entry inside @class DomainAuthenticationToken @method DomainAuthenticationToken @param principal "+principal+
				" credentials: "+credentials+" authorities "+authorities);
	}

/*	public CustomUsernamePasswordAuthenticationToken(String username,String password ){		
		super(username,password);
		LOGGER.info("2 Entry inside @class DomainAuthenticationToken @method DomainAuthenticationToken @param username "+username);
	}
*/
	public CustomUsernamePasswordAuthenticationToken(Object principal,Object credentials, String domainName) {
		super(principal, credentials);
		this.domainName = domainName;
		LOGGER.info("3 Entry inside @class DomainAuthenticationToken @method DomainAuthenticationToken @param domainName "+domainName);
	}
	
	
}
