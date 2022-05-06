package com.inn.counselling.wrapper;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class passwordWrapper {

	
	private String pwd1;

	private String pwd2;

	private String pwd3;
	
	
	public String getPwd1() {
		return pwd1;
	}


	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}


	public String getPwd2() {
		return pwd2;
	}


	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}


	public String getPwd3() {
		return pwd3;
	}


	public void setPwd3(String pwd3) {
		this.pwd3 = pwd3;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
