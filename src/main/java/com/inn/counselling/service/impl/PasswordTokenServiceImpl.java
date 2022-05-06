package com.inn.counselling.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.dao.IPasswordTokenDao;
import com.inn.counselling.model.PasswordToken;
import com.inn.counselling.model.Users;
import com.inn.counselling.security.authentication.ContextProvider;
import com.inn.counselling.service.IPasswordTokenService;
import com.inn.counselling.service.IUserService;
import com.inn.counselling.service.generic.AbstractService;
import com.inn.counselling.utils.ConfigUtil;
import com.inn.counselling.wrapper.PasswordResetWrapper;

@Service
@Transactional
public class PasswordTokenServiceImpl extends AbstractService<Long, PasswordToken> implements IPasswordTokenService {

	private static final Logger LOGGER=LoggerFactory.getLogger(PasswordTokenServiceImpl.class);

	private  IPasswordTokenDao passwordTokenDao;

	@Autowired
	public void setDao(IPasswordTokenDao dao) {
		super.setDao(dao);
		passwordTokenDao = dao;
	}

	private IUserService getUserService() {
		IUserService userService = ContextProvider.getContext().getBean(IUserService.class);
		return userService;
	}

	@Override
	public List<PasswordToken> search(PasswordToken passwordResetToken) throws Exception{
		return super.search(passwordResetToken);
	}

	@Override
	public PasswordToken findByPk(Long  primaryKey)throws Exception{
		return (passwordTokenDao.findByPk(primaryKey));
	}

	@Override
	public List<PasswordToken> findAll() throws Exception{
		return passwordTokenDao.findAll();
	}

	@Override
	public PasswordToken create(@Valid PasswordToken passwordResetToken) throws Exception{
		return passwordTokenDao.create(passwordResetToken);
	}

	@Override
	public PasswordToken update(@Valid PasswordToken passwordResetToken)throws Exception {
		return passwordTokenDao.update(passwordResetToken);
	}

	@Override
	public boolean delete(PasswordToken passwordResetToken) throws Exception {
		return super.delete(passwordResetToken);
	}

	@Override
	public boolean deleteByPk(Long primaryKey) throws Exception  {
		return super.deleteByPk(primaryKey);
	}

	@Transactional
	@Override
	public Users forgetCheckSum(String username) throws Exception {
		LOGGER.info("inside @class UnauthRestimpl @method forgetCheckSum @param username {} ",username);
		Users users = getUserService().findByUsername(username);		
		if (users == null) {
			throw new Exception("User Not found ");
		}
		try {
			PasswordToken passwordToken = passwordTokenDao.findByUserId(users.getId());
			passwordToken.setToken(UUID.randomUUID().toString());
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MINUTE, 30);
			passwordToken.setExpiryDate(now.getTime());
			passwordTokenDao.update(passwordToken);
			return users;
		} catch (Exception e) {
			LOGGER.info("inside @class UnauthRestimpl @method forgetCheckSum @param {} @cause {} ",username,e);
			throw e;
		}
	}

	@Transactional
	@Override
	public String resetCheckSum(PasswordResetWrapper passwordResetWrapper)
			throws Exception {
		LOGGER.info("Entry inside @class UnauthrestImpl @method resetCheckSum @param passwordResetWrapper {}  ",passwordResetWrapper);
		try {
			String uriPath = null;
			Users user = getUserService().findByUsername(passwordResetWrapper.getUsername());
			if(user!=null) {
				PasswordToken passwordResetToken = passwordTokenDao.findByUserIdAndToken(user.getId(), passwordResetWrapper.getToken());
				if(passwordResetToken!=null) {
					if (user.isEnabled()) {
						uriPath = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL) + "/jsp/resetPassword.jsp?id=" + user.getId() +
								"&activationCode=" + passwordResetWrapper.getToken();
					}
				}
			}
			if(uriPath!=null && !uriPath.isEmpty()) {
			}else {
				uriPath = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL)+ConfigUtil.getConfigProp(ConfigUtil.APP_LOGIN_URL);
			}
			return uriPath;
		}catch(Exception e) {
			throw e;
		}
	}
	
	@Transactional
	@Override
	public String resetCheckSumForMobile(PasswordResetWrapper passwordResetWrapper)
			throws Exception {
		LOGGER.info("Entry inside @class UnauthrestImpl @method resetCheckSumForMobile @param passwordResetWrapper {}  ",passwordResetWrapper);
		try {
			Users user = getUserService().findByUsername(passwordResetWrapper.getUsername());
			if(user!=null) {
				if(user.isEnabled()) {
					PasswordToken passwordResetToken = passwordTokenDao.findByUserIdAndToken(user.getId(), passwordResetWrapper.getToken());
					if(passwordResetToken!=null) {
						return ConfigUtil.SUCCESS_JSON;
					}else {
						throw new Exception("Code is not correct");
					}
				}else {
					throw new Exception("User is not enabled");
				}
			}else {
				throw new Exception("User not found");
			}
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	@Override
	public String changePasswordActivation(PasswordResetWrapper passwordResetWrapper) throws Exception {
		LOGGER.info("Entry inside @class UnauthrestImpl @method changePasswordActivation @param passwordResetWrapper {}  ",passwordResetWrapper);
		try {
			IUserService userService = getUserService();
			Users user = userService.findByUsername(passwordResetWrapper.getUsername());
			String uriPath = null;
			if(user!=null && user.isEnabled()) {
				PasswordToken passwordResetToken = passwordTokenDao.findByUserIdAndToken(user.getId(), passwordResetWrapper.getToken());
				if(passwordResetToken!=null) {
					user.setEnabled(true);
					user.setCheckSum(userService.getEncryptedEncodedPassword(passwordResetWrapper.getPassword()));
					passwordResetToken.setToken(null);
					userService.update(user);
					userService.unLock(user.getUsername());
					uriPath = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL)+ConfigUtil.getConfigProp(ConfigUtil.APP_LOGIN_URL);
				}
			}
			if(uriPath!=null && !uriPath.isEmpty()) {
				
			}else {
				uriPath = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL)+ConfigUtil.getConfigProp(ConfigUtil.APP_LOGIN_URL);
			}
			return uriPath;
		}catch(Exception e) {
			throw e;
		}
	}

	@Transactional
	@Override
	public String userActivation(PasswordResetWrapper passwordResetWrapper) throws Exception {
		LOGGER.info("Entry inside @class UnauthrestImpl @method changePasswordActivation @param passwordResetWrapper {}  ",passwordResetWrapper);
		try {
			IUserService userService = getUserService();
			Users user = userService.findByUsername(passwordResetWrapper.getUsername());
			String uriPath = null;
			if(user!=null) {
				PasswordToken passwordResetToken = passwordTokenDao.findByUserIdAndToken(user.getId(), passwordResetWrapper.getToken());
				if(passwordResetToken!=null) {
					if(user.isEnabled()) {
						uriPath=ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL)+"/jsp/login.jsp";
					}else {
						user.setEnabled(true);
						uriPath = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL) + "/jsp/useractivation.jsp?id=" + passwordResetWrapper.getUsername()
						+"&activationCode=" + passwordResetWrapper.getToken();
					}
					userService.unLock(user.getUsername());
				}
			}
			if(uriPath!=null && !uriPath.isEmpty()) {
				
			}else {
				uriPath = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL)+ConfigUtil.getConfigProp(ConfigUtil.APP_LOGIN_URL);
			}
			return uriPath;
		} catch (Exception e) {
			LOGGER.error("Error Inside  @class : UnauthRestImpl  @Method :userActivation() {} " ,e);
			throw e;
		}
	}

	@Override
	public PasswordToken findByUserId(long userId) {
		return passwordTokenDao.findByUserId(userId);
	}

}
