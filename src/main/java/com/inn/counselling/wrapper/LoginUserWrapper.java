package com.inn.counselling.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoginUserWrapper {

	private UserInContextWrapper userInContextWrapper;
	
	private List<UserFeatureWrapper> userFeatureWrapperList = new ArrayList<>();

	public List<UserFeatureWrapper> getUserFeatureWrapperList() {
		return userFeatureWrapperList;
	}

	public void setUserFeatureWrapperList(List<UserFeatureWrapper> userFeatureWrapperList) {
		this.userFeatureWrapperList = userFeatureWrapperList;
	}

	public UserInContextWrapper getUserInContextWrapper() {
		return userInContextWrapper;
	}

	public void setUserInContextWrapper(UserInContextWrapper userInContextWrapper) {
		this.userInContextWrapper = userInContextWrapper;
	}


}
