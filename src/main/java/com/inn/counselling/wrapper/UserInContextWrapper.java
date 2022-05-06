package com.inn.counselling.wrapper;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.counselling.enumWrapper.Gender;
import com.inn.counselling.model.Role;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserInContextWrapper {

	private Long id;

	private String firstname;

	private String lastname;

	private String username;

	private Set<Role> roles = new HashSet<Role>();

	private String dpPath;

	private Date dob;

	private Gender gender;

	private Integer age;

	private Set<RoleWrapper> roleWrapper = new HashSet<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getDpPath() {
		return dpPath;
	}

	public void setDpPath(String dpPath) {
		this.dpPath = dpPath;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Set<RoleWrapper> getRoleWrapper() {
		return roleWrapper;
	}

	public void setRoleWrapper(Set<RoleWrapper> roleWrapper) {
		this.roleWrapper = roleWrapper;
	}	
	
	
}
