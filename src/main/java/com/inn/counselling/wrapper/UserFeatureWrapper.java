package com.inn.counselling.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserFeatureWrapper {

	private Long id;

	private String featureName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public UserFeatureWrapper() {
		super();
	}

	public UserFeatureWrapper(Long id, String featureName) {
		super();
		this.id = id;
		this.featureName = featureName;
	}	

	
	
}
