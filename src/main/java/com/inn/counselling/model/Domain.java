package com.inn.counselling.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


//@Audited 
@Entity
@Table(name="domain")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Domain{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Basic
	@Column(name="code")
	private String Code;

	@Basic
	@Column(name="name", nullable=false)
	private String name;

	@Basic
	@Column(name="is_enabled")
	private Boolean isEnabled;

	@Length(min=0, max=50)
	@Basic
	@Column(name="email", nullable=false, length=50)
	private String email;

	@Basic
	@Column(name="contact_no",length=10, nullable=false)
	private Long contactno;
	
	@Column(name="createdTime",insertable=true,updatable=false)
	private Date createdTime;

	@Column(name="modifiedTime",insertable=true,updatable=true)
	private Date modifiedTime;
	
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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getContactno() {
		return contactno;
	}

	public void setContactno(Long contactno) {
		this.contactno = contactno;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
