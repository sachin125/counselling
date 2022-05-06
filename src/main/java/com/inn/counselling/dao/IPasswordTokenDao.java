package com.inn.counselling.dao;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.model.PasswordToken;

public interface IPasswordTokenDao extends IGenericDao<Long, PasswordToken> {

	PasswordToken findByUserIdAndToken(long userId, String token);

	PasswordToken findByUserId(long userId);

}
