package com.inn.counselling.security.authentication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.inn.counselling.model.Permission;
import com.inn.counselling.model.Role;
import com.inn.counselling.model.Users;
import com.inn.counselling.service.IPermissionService;
import com.inn.counselling.service.IRoleService;
import com.inn.counselling.service.IUserService;

public class CustomerInfo {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerInfo.class);

	public static Map<String, String> getCustomerInfo() {
		SecurityContext sc = SecurityContextHolder.getContextHolderStrategy().getContext();		
		Authentication authentication = sc.getAuthentication();
		if(authentication == null) {
			return null;
		}
		LOGGER.info("Entry inside @class CustomerInfo @method customerInfo authentication "+authentication);
		Map<String, String> customerInfo = new HashMap<String, String>();
		if(authentication instanceof CustomUsernamePasswordAuthenticationToken){
			CustomUsernamePasswordAuthenticationToken domainAuthenticationToken = (CustomUsernamePasswordAuthenticationToken)authentication;
			UserDetails userDetails = (UserDetails) domainAuthenticationToken.getPrincipal();
			customerInfo.put("username", userDetails.getUsername());
			customerInfo.put("userid", domainAuthenticationToken.getPrincipal().toString()+"");
		}
		return customerInfo;
	}

	public static Long getCustomerUserId(){
		if(getCustomerInfo()==null){
			return null;
		}
		return Long.parseLong(getCustomerInfo().get("userid"));

	}
	
	public static String getCustomerUsername(){
		if(getCustomerInfo()==null){
			return null;
		}
		return getCustomerInfo().get("username");

	}

	private static IUserService getUsersService() {
		IUserService usersService = ContextProvider.getContext().getBean(IUserService.class);
		return usersService;
	}
//	
//	private static IUserConfigService getUserConfigService() {
//		IUserConfigService userConfigService = ContextProvider.getContext().getBean(IUserConfigService.class);
//		return userConfigService;
//	}
//	
	private static IRoleService getRoleService() {
		IRoleService roleService = ContextProvider.getContext().getBean(IRoleService.class);
		return roleService;
	}
	
	private static IPermissionService getPermissionService() {
		IPermissionService permissionService = ContextProvider.getContext().getBean(IPermissionService.class);
		return permissionService;
	}
	
	public static Users getUserInContext(){
		IUserService usersService = getUsersService();
		return usersService.findByUsername(getCustomerUsername()); 
	}
	
	
	public static Set<Role> getRoleInContext(){
		IUserService usersService = getUsersService();
		Users user = usersService.findByUsername(getCustomerUsername());
		return user.getRoles();
	}
	
	public static Set<Permission> getAllPermissonInContext(){
		IUserService usersService = getUsersService();
		Users user = usersService.findByUsername(getCustomerUsername());
		Set<Role> roles = user.getRoles();
		Set<Permission> permissions = new HashSet<>();
		for(Role role:roles) {
			permissions.addAll(role.getPermissions());
		}
		return permissions;
	}
	
//	public static UserConfig getLocaleInContext(){
//		UserConfig userConfig = null;
//		try {
//			String username = getCustomerUsername();
//			IUserService usersService = getUsersService();
//			IUserConfigService userConfigService = getUserConfigService();
//			Users user = usersService.findByUsername(username);
//			userConfig = userConfigService.findByUserId(user.getId());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return userConfig;
//	}
}
