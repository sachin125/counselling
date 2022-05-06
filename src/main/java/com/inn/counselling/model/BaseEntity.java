package com.inn.counselling.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.counselling.security.authentication.ContextProvider;
import com.inn.counselling.security.authentication.CustomerInfo;
import com.inn.counselling.service.IUserService;

@MappedSuperclass
//@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class BaseEntity {

	@Column(name="createdTime",insertable=true,updatable=false)
	private Date createdTime;

	@Column(name="modifiedTime",insertable=true,updatable=true)
	private Date modifiedTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="creator", updatable = false)
	private Users creator;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="last_modifier")
	private Users lastModifier;
	
	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Users getCreator() {
		return creator;
	}

	public void setCreator(Users creator) {
		this.creator = creator;
	}

	public Users getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(Users lastModifier) {
		this.lastModifier = lastModifier;
	}

	@PrePersist
	void onCreate() {	
		Users userInContext = getCurrentUser();
		this.setCreator(userInContext);
		this.setLastModifier(userInContext);
		Date date=new Date();
		this.setCreatedTime(date);
		this.setModifiedTime(date);
	}

	@PreUpdate
	void onPersist() {
	    this.setLastModifier(getCurrentUser());
		this.setModifiedTime((new Date()));
	}
	
	private Users getCurrentUser(){
		IUserService userService=ContextProvider.getContext().getBean(IUserService.class);
	    Users userInContext = null;
		try {
			userInContext = userService.findByPk(CustomerInfo.getCustomerUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInContext;
	}
}
