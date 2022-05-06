package com.inn.counselling.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.IPasswordTokenDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.PasswordToken;

@Dao
public class PasswordTokenDaoImpl extends HibernateGenericDao<Long, PasswordToken> implements IPasswordTokenDao {

	private static final Logger LOGGER=LoggerFactory.getLogger(PasswordTokenDaoImpl.class);

	public PasswordTokenDaoImpl() {
		super(PasswordToken.class);
	}

	@Override
	public PasswordToken create(@Valid PasswordToken passwordResetToken)throws Exception {
		return super.create(passwordResetToken);
	}


	@Override
	public PasswordToken update(@Valid PasswordToken passwordResetToken) throws Exception {
		return super.update(passwordResetToken);
	}

	@Override
	public boolean delete(PasswordToken passwordResetToken) throws Exception {
		return super.delete(passwordResetToken);
	}

	@Override
	public boolean deleteByPk(Long passwordResetTokenPk) throws Exception{
		return super.deleteByPk(passwordResetTokenPk);
	}

	@Override
	public List<PasswordToken> findAll() throws Exception{
		return super.findAll();
	}

	@Override
	public PasswordToken findByPk(Long  passwordResetTokenPk) throws Exception{
		return super.findByPk(passwordResetTokenPk);
	}
	
	@Override
	public PasswordToken findByUserIdAndToken(long userId,String token) {
		LOGGER.info("Inside @class UserDaoImpl @method findByUserIdAndToken @param userId: {} and token {} ",userId,token);
		try {
			Query query = getEntityManager().createNamedQuery("PasswordToken.findByUserIdAndToken")
					.setParameter("userId", userId)
					.setParameter("token", token);
			return  (PasswordToken) query.getSingleResult();
		} catch (NoResultException e) {
			LOGGER.error("NoResultException Inside @class UserDaoImpl @method findByUserIdAndToken @param userId: {} and token {} ",userId,token);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class UserDaoImpl @method findByUserIdAndToken @param userId: {} and token {} ",userId,token);
			throw e;
		}
	}
	
	@Override
	public PasswordToken findByUserId(long userId) {
		LOGGER.info("Inside @class UserDaoImpl @method findByUserIdAndToken @param userId: {}  ",userId);
		try {
			Query query = getEntityManager().createNamedQuery("PasswordToken.findByUserId")
					.setParameter("userId", userId);
			return  (PasswordToken) query.getSingleResult();
		} catch (NoResultException e) {
			LOGGER.error("NoResultException Inside @class UserDaoImpl @method findByUserIdAndToken @param userId: {} ",userId);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class UserDaoImpl @method findByUserIdAndToken @param userId: {} ",userId);
			throw e;
		}
	}

}
