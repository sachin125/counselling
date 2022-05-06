package com.inn.counselling.wrapper.service.impl;

import java.util.HashSet;
import java.util.Set;

import com.inn.counselling.model.Permission;
import com.inn.counselling.model.Role;
import com.inn.counselling.model.Users;
import com.inn.counselling.wrapper.PermissionWrapper;
import com.inn.counselling.wrapper.RoleWrapper;
import com.inn.counselling.wrapper.UserInContextWrapper;

public class LoginUserWrapperServiceImpl {

	static public UserInContextWrapper setUser(UserInContextWrapper userInContextWrapper,Users user) {
		userInContextWrapper.setId(user.getId());
		userInContextWrapper.setFirstname(user.getFirstname());
		userInContextWrapper.setLastname(user.getLastname());
		userInContextWrapper.setLastname(user.getLastname());
		userInContextWrapper.setGender(user.getGender());
		userInContextWrapper.setAge(user.getAge());
		userInContextWrapper.setUsername(user.getUsername());
		Set<RoleWrapper> roleWrapperSet = copyRoles(user.getRoles());
		userInContextWrapper.setRoleWrapper(roleWrapperSet);
		return userInContextWrapper;
	}

	static private Set<RoleWrapper> copyRoles(Set<Role> roles) {
		Set<RoleWrapper> roleWrapperSet = new HashSet<>();
		for(Role role:roles) {
			RoleWrapper roleWrapper = new RoleWrapper();
			roleWrapper.setId(role.getId());
			roleWrapper.setName(role.getName());
			roleWrapper.setPermissionWrapperSet(copyPermission(role.getPermissions()));
		}
		return roleWrapperSet;
	}
	
	static private Set<PermissionWrapper> copyPermission(Set<Permission> permissions) {
		Set<PermissionWrapper> permissionWrappers = new HashSet<>();
		for(Permission permission:permissions) {
			PermissionWrapper permissionWrapper = new PermissionWrapper();
			permissionWrapper.setId(permission.getId());
			permissionWrapper.setName(permissionWrapper.getName());
		}
		return permissionWrappers;
	}

}
