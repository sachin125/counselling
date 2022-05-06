package com.inn.counselling.rest.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.inn.counselling.exception.RestException;
import com.inn.counselling.model.Users;
import com.inn.counselling.security.authentication.ContextProvider;
import com.inn.counselling.service.IPasswordTokenService;
import com.inn.counselling.service.IUnauthService;
import com.inn.counselling.service.IUserService;
import com.inn.counselling.utils.ConfigUtil;
import com.inn.counselling.wrapper.PasswordResetWrapper;
import com.inn.counselling.wrapper.UserRegistrationWrapper;
import com.inn.counselling.wrapper.passwordWrapper;

@Service("UnauthRestImpl")
@Path("/unauthorize")
@Produces("application/json")
@Consumes("application/json")
public class UnauthRestImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnauthRestImpl.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IUserService userService;

	@Autowired
	private IUnauthService unauthService;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@Autowired
	private IPasswordTokenService passwordTokenService;

	@GET
	@Path("userActivation")
	public Response userActivation(@QueryParam("username") String username, @QueryParam("code") String activationCode)
			throws Exception {
		LOGGER.debug("Entry Inside @class UnauthRestImpl @method userActivation @param userid {} and activationCode {} ",username,activationCode);
		try {
			PasswordResetWrapper passwordResetWrapper = new PasswordResetWrapper();
			passwordResetWrapper.setUsername(username);
			passwordResetWrapper.setToken(activationCode);
			String uriPath = passwordTokenService.userActivation(passwordResetWrapper);
			URI uri = null;	
			uri = new URI(uriPath);
			return Response.temporaryRedirect(uri).build();
		} catch (Exception e) {
			LOGGER.error("Error Inside  @class : UnauthRestImpl  @Method :userActivation() {} " ,e);
			throw new RestException(e);
		}
	}

	@POST
	@Path("matchEncryptedPassword")
	public void matchEncryptedPassword(passwordWrapper passwordWrapper) throws Exception {
		LOGGER.info("Entry Inside  @class : UnauthRestImpl  @Method :matchEncryptedPassword() {} " ,passwordWrapper);
		try {
			userService.matchEncryptedPassword(passwordWrapper.getPwd1(), passwordWrapper.getPwd2());
			
		}catch(Exception e) {
			LOGGER.error("Error Inside  @class : UnauthRestImpl  @Method :matchEncryptedPassword() {} " ,e);
			e.printStackTrace();
		}
	}
	
	
	@POST
	@Path("userRegistration")
	public String userRegistration(UserRegistrationWrapper userRegistrationWrapper) throws Exception {
		
		try {
			if(userRegistrationWrapper!=null && userRegistrationWrapper.getId()==null) {
				if(userRegistrationWrapper.getFirstname()==null ||userRegistrationWrapper.getLastname()==null 
						|| userRegistrationWrapper.getUsername()==null || userRegistrationWrapper.getContactno()==null 
						|| userRegistrationWrapper.getCheckSum()==null
						|| userRegistrationWrapper.getDob()==null || userRegistrationWrapper.getGender()==null ) {
					throw new Exception("Please fill all field");
				}
			}else {
				throw new Exception("Something went wrong");
			}
			Users user = userService.userRegistration(userRegistrationWrapper);
			try {
				//unauthService.sendActivationEmail(user);
			}catch(Exception e) {
				throw new Exception("User created successfully, error in seding mail "+e.getMessage());
			}
		} catch (Exception e){
			LOGGER.error("Error Inside  @class : UnauthRestImpl  @Method :userRegistration {} ",e);
			throw e;
		}
		return ConfigUtil.SUCCESS_JSON;
	}


	@GET
	@Path("forgetCheckSum")
	public String forgetCheckSum(@QueryParam("username") String username) throws RestException {
		LOGGER.info("inside @class UnauthRestimpl @method forgetCheckSum @param username {} ",username);
		try {
			Users user = passwordTokenService.forgetCheckSum(username);
			if(user!=null) {
				unauthService.forgetCheckSum(user);
				return "{\"data\":\"Reset Password Email has been Sent\"}";				
			}else {
				throw new Exception("Invalid Credentials");
			}
		}catch(Exception e) {
			throw new RestException(e);
		}
	}

	@GET
	@Path("resetCheckSum")
	public Response resetCheckSum(@QueryParam("username") String username, @QueryParam("code") String activationCode)
			throws RestException {
		LOGGER.info("Entry inside @class UnauthrestImpl @method resetCheckSum @param userid {} and activationCode {} ",username,activationCode);
		try {
			PasswordResetWrapper passwordResetWrapper = new PasswordResetWrapper();
			passwordResetWrapper.setUsername(username);
			passwordResetWrapper.setToken(activationCode);
			String uriPath = passwordTokenService.resetCheckSum(passwordResetWrapper);
			URI uri = new URI(uriPath);
			return Response.temporaryRedirect(uri).build();
		}catch(Exception e) {
			throw new RestException(e);
		}
	}
	
	@GET
	@Path("resetCheckSumForMobile")
	public String resetCheckSumForMobile(@QueryParam("username") String username, @QueryParam("code") String activationCode)
			throws RestException {
		LOGGER.info("Entry inside @class UnauthrestImpl @method resetCheckSum @param userid {} and activationCode {} ",username,activationCode);
		try {
			PasswordResetWrapper passwordResetWrapper = new PasswordResetWrapper();
			passwordResetWrapper.setUsername(username);
			passwordResetWrapper.setToken(activationCode);
			return passwordTokenService.resetCheckSumForMobile(passwordResetWrapper);
		}catch(Exception e) {
			throw new RestException(e);
		}
	}

	/*	@POST
	@Path("changePassword")
	public Response changePasswordActivation(@FormParam("userid") Integer userid,
			@FormParam("password") String password, @FormParam("activation") String activationCode) throws Exception {
		LOGGER.info("Entry inside @class UnauthrestImpl @method changePasswordActivation @param userid {} and password {} and activationCode {} ",userid,password,activationCode);
		try {
			PasswordResetWrapper passwordResetWrapper = new PasswordResetWrapper();
			passwordResetWrapper.setUserid(userid);
			passwordResetWrapper.setToken(activationCode);
			String uriPath = passwordTokenService.changePasswordActivation(passwordResetWrapper);
			URI uri = new URI(uriPath);
			return Response.temporaryRedirect(uri).build();
		}catch(Exception e) {
			throw new RestException(e);
		}
	}*/

	@POST
	@Path("changePassword")
	public Response changePasswordActivation(PasswordResetWrapper passwordResetWrapper) throws Exception {
		LOGGER.info("Entry inside @class UnauthrestImpl @method changePasswordActivation @param passwordResetWrapper {} ",passwordResetWrapper);
		try {
			if(passwordResetWrapper.getPassword().equals(passwordResetWrapper.getConfirmPassword())) {
				String uriPath = passwordTokenService.changePasswordActivation(passwordResetWrapper);
				URI uri = new URI(uriPath);
				return Response.temporaryRedirect(uri).build();				
			}else {
				throw new Exception("Both password must be match");
			}
		}catch(Exception e) {
			throw new RestException(e);
		}
	}


	@GET
	@Path("usernameSearch")
	public Boolean usernameSearch(@QueryParam("username") String username) throws Exception {
		Users users = null;
		try {
			users = userService.findByUsername(username);
			return users != null;
		} catch (Exception e) {
			LOGGER.error("" + e.getMessage());
			LOGGER.error("No Entity Found For Username");
			throw e;
		}
	}

	@GET
	@Path("emailSearch")
	public Boolean emailSearch(@QueryParam("email") String email) throws Exception {
		Users users = null;
		try {
			users = userService.findByEmail(email);
			if (users != null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		}
	}


	@POST
	@Path("mobileAppAuth")
	public HashMap < String, String > setAuthentication(@FormParam("userid") String username,
			@FormParam("password") String password) throws Exception {
		HashMap < String, String > token = new HashMap < String, String > ();
		try {

			SecurityContextHolder.getContext().getAuthentication();
			final Authentication auth = null; // new CustomUsernamePasswordAuthenticationToken(username,new
			// ShaPasswordEncoder().encodePassword(password, null));

			authManager.authenticate(auth);

			SecurityContext sc = SecurityContextHolder.getContextHolderStrategy().getContext();
			sc.setAuthentication(auth);
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			if (attr != null) {
				attr.getRequest().getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", sc);
			}
			// token.put("AUTH-TOKEN", MobileAuthHelper.createJsonWebToken(username, 1L));

		} catch (Exception updateExp) {
			LOGGER.error(updateExp.getMessage());
			throw updateExp;
		}
		return token;
	}

	@POST
	@Path("mobileAuthentication")
	public Users mobileAuthentication(@FormParam("username") String username, @FormParam("password") String password)
			throws Exception {

		try {
			LOGGER.info("Entry inside @class UnauthRestImpl @method mobileAuthentication @param username "+username+" password: "+password);
			SecurityContextHolder.getContext().getAuthentication();
			IUserService userDao = (IUserService) ContextProvider.getContext().getBean(IUserService.class);
			Users authenticatedUser = null;
			try {
				authenticatedUser = userDao.findByUsername(username);
			} catch (Exception e) {
				LOGGER.error("@method mobileAuthentication exception occurred ", e);
			}

			if (authenticatedUser == null) {
				throw new Exception("Bad Credentials");
			}
			Boolean enableduser = authenticatedUser.isEnabled();
			LOGGER.info("@method mobileAuthentication password" + password);
			if (!enableduser) {
				throw new Exception("Your account is disabled, please contact administrator.");
			}
			password = passwordEncoder.encode(password);

			LOGGER.info("@method mobileAuthentication password after encoding :" + password);
			// CustomUsernamePasswordAuthenticationToken auth= null;//new
			// CustomUsernamePasswordAuthenticationToken(username,password );
			/*
			 * Authentication authentication = authManager.authenticate(auth); if
			 * (!(authentication instanceof CustomUsernamePasswordAuthenticationToken)) {
			 * throw new RuntimeException("Undesirable toke type"); } else {
			 * //CustomUsernamePasswordAuthenticationToken tokenAuth =
			 * (CustomUsernamePasswordAuthenticationToken) authentication;
			 * //tokenAuth.setUserid(authenticatedUser.getId()); }
			 */

			SecurityContext sc = SecurityContextHolder.getContextHolderStrategy().getContext();
			// sc.setAuthentication(authentication);
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			if (attr != null) {
				attr.getRequest().getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", sc);
				attr.getRequest().getSession(true).setAttribute("userid", authenticatedUser.getId());

			}
			LOGGER.info("Entry inside @class UnauthRestImpl @method mobileAuthentication @param Authentication is done  "+authenticatedUser);
			return authenticatedUser;
		}catch(Exception e) {
			LOGGER.error("Error occured inside @class UnauthRestImpl @method mobileAuthentication @error {} ",e);
			throw new RestException(e);
		}
	}

	@GET
	@Path("mobileAuthentication1")
	public Users mobileAuthentication1(@QueryParam("username") String username, @QueryParam("password") String password)
			throws Exception {
		try {

			LOGGER.info("Entry inside @class UnauthRestImpl @method mobileAuthentication1 @param username "+username+" password: "+password);

			SecurityContextHolder.getContext().getAuthentication();
			IUserService userDao = (IUserService) ContextProvider.getContext().getBean(IUserService.class);
			Users authenticatedUser = null;
			try {
				authenticatedUser = userDao.findByUsername(username);
			} catch (Exception e) {
				LOGGER.error("@method mobileAuthentication exception occurred ", e);
			}

			if (authenticatedUser == null) {
				throw new Exception("Bad Credentials");
			}
			Boolean enableduser = authenticatedUser.isEnabled();
			LOGGER.info("@method mobileAuthentication password" + password);
			if (!enableduser) {
				throw new Exception("Your account is disabled, please contact administrator.");
			}
			password = passwordEncoder.encode(password);

			LOGGER.info("@method mobileAuthentication password after encoding :" + password);
			// CustomUsernamePasswordAuthenticationToken auth= null;//new
			// CustomUsernamePasswordAuthenticationToken(username,password );
			/*
			 * Authentication authentication = authManager.authenticate(auth); if
			 * (!(authentication instanceof CustomUsernamePasswordAuthenticationToken)) {
			 * throw new RuntimeException("Undesirable toke type"); } else {
			 * //CustomUsernamePasswordAuthenticationToken tokenAuth =
			 * (CustomUsernamePasswordAuthenticationToken) authentication;
			 * //tokenAuth.setUserid(authenticatedUser.getId()); }
			 */

			SecurityContext sc = SecurityContextHolder.getContextHolderStrategy().getContext();
			// sc.setAuthentication(authentication);
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			if (attr != null) {
				attr.getRequest().getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", sc);
				attr.getRequest().getSession(true).setAttribute("userid", authenticatedUser.getId());

			}
			LOGGER.info("Entry inside @class UnauthRestImpl @method mobileAuthentication1 @param Authentication is done  "+authenticatedUser);
			return authenticatedUser;
		}catch(Exception e) {
			LOGGER.error("Error occured inside @class UnauthRestImpl @method mobileAuthentication1 @error {} ",e);
			throw new RestException(e);
		}

	}

	@POST
	@Path("mobileAuthentication2")
	public Users mobileAuthentication2(@QueryParam("username") String username, @QueryParam("password") String password)
			throws Exception {
		try {
			LOGGER.info("Entry inside @class UnauthRestImpl @method mobileAuthentication2 @param username "+username+" password: "+password);
			SecurityContextHolder.getContext().getAuthentication();
			IUserService userDao = (IUserService) ContextProvider.getContext().getBean(IUserService.class);
			Users authenticatedUser = null;
			try {
				authenticatedUser = userDao.findByUsername(username);
			} catch (Exception e) {
				LOGGER.error("@method mobileAuthentication exception occurred ", e);
			}

			if (authenticatedUser == null) {
				throw new Exception("Bad Credentials");
			}
			Boolean enableduser = authenticatedUser.isEnabled();
			LOGGER.info("@method mobileAuthentication password" + password);
			if (!enableduser) {
				throw new Exception("Your account is disabled, please contact administrator.");
			}
			password = passwordEncoder.encode(password);

			LOGGER.info("@method mobileAuthentication password after encoding :" + password);
			// CustomUsernamePasswordAuthenticationToken auth= null;//new
			// CustomUsernamePasswordAuthenticationToken(username,password );
			/*
			 * Authentication authentication = authManager.authenticate(auth); if
			 * (!(authentication instanceof CustomUsernamePasswordAuthenticationToken)) {
			 * throw new RuntimeException("Undesirable toke type"); } else {
			 * //CustomUsernamePasswordAuthenticationToken tokenAuth =
			 * (CustomUsernamePasswordAuthenticationToken) authentication;
			 * //tokenAuth.setUserid(authenticatedUser.getId()); }
			 */

			SecurityContext sc = SecurityContextHolder.getContextHolderStrategy().getContext();
			// sc.setAuthentication(authentication);
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			if (attr != null) {
				attr.getRequest().getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", sc);
				attr.getRequest().getSession(true).setAttribute("userid", authenticatedUser.getId());

			}
			LOGGER.info("Entry inside @class UnauthRestImpl @method mobileAuthentication2 @param Authentication is done  "+authenticatedUser);
			return authenticatedUser;
		}catch(Exception e) {
			LOGGER.error("Error occured inside @class UnauthRestImpl @method mobileAuthentication2 @error {} ",e);
			throw new RestException(e);
		}

	}

}
