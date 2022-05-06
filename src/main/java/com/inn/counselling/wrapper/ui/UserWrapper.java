package com.inn.counselling.wrapper.ui;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.counselling.enumWrapper.Gender;
import com.inn.counselling.model.Users;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserWrapper {

	public UserWrapper() {
		
	}
	public UserWrapper(Users user) {
		this.id = user.getId();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.enabled = user.getEnabled();
		this.username = user.getUsername();
		this.contactno = user.getContactno();
		this.dob = user.getDob();
		this.gender = user.getGender();
		this.age = user.getAge();
				
	}
	
	private Long id;

	private String firstname;

	private String lastname;

	private String username;

	private Long contactno;

	private Boolean enabled;

	private Date dob;

	private Gender gender;

	private Integer age;
	
	private String checksum;
	
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
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

	public Long getContactno() {
		return contactno;
	}

	public void setContactno(Long contactno) {
		this.contactno = contactno;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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

	

}
