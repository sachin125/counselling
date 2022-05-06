package com.inn.counselling.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

//limit login attempts
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

	private static final Logger LOGGER =  LoggerFactory.getLogger(CustomDaoAuthenticationProvider.class);
	
	@Override
	public Authentication authenticate(Authentication originalAuthentication) throws AuthenticationException {
		Assert.isInstanceOf(CustomUsernamePasswordAuthenticationToken.class, originalAuthentication,
				messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
						"Only DomainAuthenticationToken is supported"));

		LOGGER.info("Entry inside @class CustomDaoAuthenticationProvider @method authenticate @param originalAuthentication "+originalAuthentication);
		
		CustomUsernamePasswordAuthenticationToken customUsernamePasswordAuthenticationToken = (CustomUsernamePasswordAuthenticationToken)originalAuthentication;
		String username = (customUsernamePasswordAuthenticationToken.getPrincipal() == null) ? "NONE_PROVIDED" : customUsernamePasswordAuthenticationToken.getName();
		boolean cacheWasUsed = true;
		CustomUser domainUser = null;
		domainUser = (CustomUser) super.getUserCache().getUserFromCache(username);
		LOGGER.info("cacheWasUsed=true inside @class CustomDaoAuthenticationProvider @method authenticate @param aaaaaaaa {} {}",domainUser,customUsernamePasswordAuthenticationToken);
		
		if(domainUser==null){
			cacheWasUsed = false;			
			try {
				domainUser = retrieveDomainUser(username, customUsernamePasswordAuthenticationToken);
				LOGGER.debug("domainUserdomainUserdomainUserdomainUserdomainUser {} ",domainUser);
			} catch (UsernameNotFoundException notFound) {
				LOGGER.error("Error Inside  @class :CustomDaoAuthenticationProvider @Method :authenticate {} ",notFound.getMessage());
				if (hideUserNotFoundExceptions) {
					throw new BadCredentialsException(messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
				} else {
					throw notFound;
				}
			}
		}
//		LOGGER.info("after if inside @class CustomDaoAuthenticationProvider @method authenticate @param domainUser {} {} ",domainUser,customUsernamePasswordAuthenticationToken);
		Assert.notNull(domainUser, "retrieveUser returned null - a violation of the interface contract");
		try {
			String domainPassword = domainUser.getPassword();
			String presentedPassword = customUsernamePasswordAuthenticationToken.getCredentials().toString();
			LOGGER.info("after if inside @class CustomDaoAuthenticationProvider @method authenticate @param domainPassword presentedPassword {} {} ",domainPassword,presentedPassword);
			super.getPreAuthenticationChecks().check(domainUser);
			super.additionalAuthenticationChecks(domainUser,  customUsernamePasswordAuthenticationToken);
		} catch (AuthenticationException exception) {
			//LOGGER.error("Error Inside  @class :CustomDaoAuthenticationProvider @Method :authenticate: ",exception);
			if (cacheWasUsed) {
				cacheWasUsed = false;
				domainUser = retrieveDomainUser(username, customUsernamePasswordAuthenticationToken);
				LOGGER.debug("sachinnnnnnnnnnn {} ",domainUser);
				super.getPreAuthenticationChecks().check(domainUser);
				super.additionalAuthenticationChecks(domainUser,(UsernamePasswordAuthenticationToken) customUsernamePasswordAuthenticationToken);
			} else {
				LOGGER.error("cacheWasUsed = false; Error Inside  @class :CustomDaoAuthenticationProvider @Method :authenticate: {} ",exception);
				throw exception;
			}
		}
		LOGGER.debug("guptaaaaaaaaaaaaaaaaaaaaa  ");
		super.getPostAuthenticationChecks().check(domainUser);

		if (!cacheWasUsed) {
			super.getUserCache().putUserInCache(domainUser);
		}

		Object principalToReturn = domainUser;

		if (super.isForcePrincipalAsString()) {
			principalToReturn = domainUser.getUsername();
		}

		return createSuccessAuthentication2(principalToReturn, customUsernamePasswordAuthenticationToken, domainUser);
	}

	protected Authentication createSuccessAuthentication2(Object principal, Authentication authentication,
			CustomUser user) {
		LOGGER.info("Entry inside @class CustomDaoAuthenticationProvider @method createSuccessAuthentication2 @param ");

		
		// Ensure we return the original credentials the user supplied,
		// so subsequent attempts are successful even with encoded passwords.
		// Also ensure we return the original getDetails(), so that future
		// authentication events after cache expiry contain the details
		CustomUsernamePasswordAuthenticationToken customUsernamePasswordAuthenticationToken = 
				new CustomUsernamePasswordAuthenticationToken(principal,
													authentication.getCredentials(), user.getAuthorities());
		customUsernamePasswordAuthenticationToken.setDetails(authentication.getDetails());
		return customUsernamePasswordAuthenticationToken;
	}

	protected final CustomUser retrieveDomainUser(String username, CustomUsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		LOGGER.info("Entry inside @class CustomDaoAuthenticationProvider @method retrieveDomainUser @param username: "+username);

		Assert.isInstanceOf(CustomUserDetailsManager.class, this.getUserDetailsService());

		CustomUser loadedDomainUser = null;

		try {
			if(this.getUserDetailsService() instanceof CustomUserDetailsManager){
				CustomUserDetailsManager customUserDetailsManager = (CustomUserDetailsManager)this.getUserDetailsService();        		
				loadedDomainUser = customUserDetailsManager.loadDomainUserByUserNameDomain(username,authentication.getDomainName());
			}
		}
		catch (DataAccessException repositoryProblem) {
			LOGGER.error("Error Inside  @class :CustomDaoAuthenticationProvider @Method :retrieveDomainUser {} "+repositoryProblem.getMessage());
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedDomainUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		LOGGER.debug("debug inside @class CustomDaoAuthenticationProvider @method retrieveDomainUser @param loadedDomainUser:{}  ",loadedDomainUser);
		return loadedDomainUser;
	}
}
