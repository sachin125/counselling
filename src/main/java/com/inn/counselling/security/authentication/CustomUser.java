package com.inn.counselling.security.authentication;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
/**
 *  * Just provide an extra field to add domain
 *
 */
@SuppressWarnings("serial")
public class CustomUser extends User {
	
	private static final Logger LOGGER =  LoggerFactory.getLogger(CustomUser.class);
	
	private final String domain;

	public String getDomain() {
		return domain;
	}

	public CustomUser(String username,String domain,String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		
		LOGGER.info("1 Entry inside @class CustomUser @method CustomUser @param : "+
				"username: "+username+"domain: "+domain+" password:"+password+" enabled: "+enabled+
				" accountNonExpired: "+accountNonExpired+" credentialsNonExpired: "+credentialsNonExpired
				+"accountNonLocked: "+accountNonLocked+" authorities: "+authorities);

		this.domain = domain;
	}

	public CustomUser(String username, String password,
			Collection<? extends GrantedAuthority> authorities, String domain) {
		super(username, password, authorities);
		this.domain = domain;
	}

}
