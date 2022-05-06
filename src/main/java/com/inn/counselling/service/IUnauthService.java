package com.inn.counselling.service;

import com.inn.counselling.model.Users;

public interface IUnauthService {

	void sendActivationEmail(Users users) throws Exception;

	void forgetCheckSum(Users user) throws Exception;

}
