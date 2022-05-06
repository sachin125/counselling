package com.inn.counselling.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.inn.counselling.utils.view.View.FamilyView;
import com.inn.counselling.utils.view.View.UserBasicView;

@XmlRootElement(name="College")
@Entity
@Table(name="college")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class College implements Serializable{

	public College() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(unique=true)
	private String code;

	private Integer contactno;

	@OneToOne
	@JoinColumn(name="user_id")
	private Users user;
	
	@OneToOne
	@JoinColumn(name="address_id")
	private Address address;

	private Integer totalSeat=0;

	private Integer allocated=0;

	private Integer freeseat=0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getContactno() {
		return contactno;
	}

	public void setContactno(Integer contactno) {
		this.contactno = contactno;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Integer getTotalSeat() {
		return totalSeat;
	}

	public void setTotalSeat(Integer totalSeat) {
		this.totalSeat = totalSeat;
	}

	public Integer getAllocated() {
		return allocated;
	}

	public void setAllocated(Integer allocated) {
		this.allocated = allocated;
	}

	public Integer getFreeseat() {
		return freeseat;
	}

	public void setFreeseat(Integer freeseat) {
		this.freeseat = freeseat;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
