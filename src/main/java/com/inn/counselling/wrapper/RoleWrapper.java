package com.inn.counselling.wrapper;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RoleWrapper {

	private long id;
	
	private String name;

	private Set<PermissionWrapper> PermissionWrapperSet = new HashSet<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PermissionWrapper> getPermissionWrapperSet() {
		return PermissionWrapperSet;
	}

	public void setPermissionWrapperSet(Set<PermissionWrapper> permissionWrapperSet) {
		PermissionWrapperSet = permissionWrapperSet;
	}

	public RoleWrapper() {
		
	}
	
}
