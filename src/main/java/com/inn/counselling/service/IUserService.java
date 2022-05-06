package com.inn.counselling.service;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;

import com.inn.counselling.model.Users;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.wrapper.UserRegistrationWrapper;


public interface IUserService extends IGenericService<Long, Users> {

	public Boolean unLock(String username);

	public Users findByEmail(String email);
	
	public void setUserAuthentication(Users newsc) throws Exception;

	Users findByUsername(String username);

	Users createUsersFromFacebook(String userID, String facebookToken) throws Exception;

	Users gmailRegistration(String gaccesstoken) throws JSONException, Exception, HttpException, IOException;

	String getEncryptedEncodedPassword(String password);

	Users userRegistration(UserRegistrationWrapper userRegistrationWrapper) throws Exception;

	boolean matchEncryptedPassword(String rawPassword, String encodedPassword);



}
