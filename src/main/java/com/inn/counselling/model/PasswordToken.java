package com.inn.counselling.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@NamedQueries({
	@NamedQuery(name="PasswordToken.findByUserIdAndToken",query="select p from PasswordToken p where p.token=:token and p.userId=:userId"),
	@NamedQuery(name="PasswordToken.findByUserId",query="select p from PasswordToken p where p.userId=:userId"),
})

@XmlRootElement(name="PasswordToken")
@Entity
@Table(name="password_token")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PasswordToken implements Serializable{

	public PasswordToken() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column
	private String token;

	private Long userId;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	

}
