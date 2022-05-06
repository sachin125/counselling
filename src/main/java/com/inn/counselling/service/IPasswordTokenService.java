package com.inn.counselling.service;


import com.inn.counselling.model.PasswordToken;
import com.inn.counselling.model.Users;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.wrapper.PasswordResetWrapper;


public interface IPasswordTokenService extends IGenericService<Long, PasswordToken> {

	Users forgetCheckSum(String username) throws Exception;

	String resetCheckSum(PasswordResetWrapper passwordResetWrapper) throws Exception;

	String changePasswordActivation(PasswordResetWrapper passwordResetWrapper) throws Exception;

	String userActivation(PasswordResetWrapper passwordResetWrapper) throws Exception;

	PasswordToken findByUserId(long userId);

	String resetCheckSumForMobile(PasswordResetWrapper passwordResetWrapper) throws Exception;


}
