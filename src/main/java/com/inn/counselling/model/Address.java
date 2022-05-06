package com.inn.counselling.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.inn.counselling.utils.view.View.AddressBasicView;


@XmlRootElement(name="Address")
@Entity
@Table(name="address")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Address implements Serializable{

	public Address() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=500)
	private String name;
	
	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=45)
	private String city;
	
	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=45)
	private String district;
	
	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=45)
	private String state;
	
	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=45)
	private String country;
	
	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=20)
	private String latitude;

	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=20)
	private String longitude;
	
	@JsonView(value = {AddressBasicView.class})
	@Basic
	@Column(length=10)
	private Integer pincode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
