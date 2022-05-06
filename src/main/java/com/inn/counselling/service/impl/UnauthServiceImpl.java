package com.inn.counselling.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.inn.counselling.model.PasswordToken;
import com.inn.counselling.model.Users;
import com.inn.counselling.service.IPasswordTokenService;
import com.inn.counselling.service.IUnauthService;
import com.inn.counselling.service.IUserService;
import com.inn.counselling.service.mail.IMailSender;
import com.inn.counselling.utils.ConfigUtil;

@Service
public class UnauthServiceImpl implements IUnauthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnauthServiceImpl.class);

	public UnauthServiceImpl() {
		super();
	}
	
	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private IMailSender mailsender;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IPasswordTokenService passwordTokenService;
	
	@Override
	public void sendActivationEmail(Users user) throws Exception {
		String activationURL = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL);
		String actObjClass = ConfigUtil.getObjectClassName(activationURL);
		String objClass = ConfigUtil.getObjectClassName(user);
		PasswordToken passwordToken = passwordTokenService.findByUserId(user.getId());
		Map<String,Object> model = new HashMap<String,Object>();
		model.put(objClass, user);
		model.put(actObjClass, activationURL);
		model.put("token", passwordToken.getToken());
		// VelocityContext context = new VelocityContext();
		String path = ConfigUtil.getConfigProp(ConfigUtil.ACTIVATION_TEMPALTE);
		String text;
		try {
			if(velocityEngine!=null) {
				text = VelocityEngineUtils.
						mergeTemplateIntoString
						(velocityEngine, path, "UTF-8", model);
				mailsender.sendMail("New user!!", text, user.getUsername(), "USER_MANAGEMENT");	
			}
			
		} catch (AddressException e) {
			LOGGER.error("Error Inside  @class : UnauthServiceImpl  @Method :sendActivationEmail() @cause {}" ,e);
			throw e;
		} catch (MessagingException e) {
			LOGGER.error("Error Inside  @class : UnauthServiceImpl  @Method :sendActivationEmail() @cause {}" ,e);
			throw e;
		} catch (VelocityException e) {
			LOGGER.error("Error Inside  @class : UnauthServiceImpl  @Method :sendActivationEmail() @cause {}" ,e);
			throw e;
		}catch (NullPointerException e) {
			LOGGER.error("NullPointerException Inside  @class : UnauthServiceImpl  @Method :sendActivationEmail() @cause {}" ,e);
			throw e;
		}catch (Exception e) {
			LOGGER.error("Error Inside  @class : UnauthServiceImpl  @Method :sendActivationEmail() @cause {}" ,e);
			throw e;
		}
	}
	
	@Override
	public void forgetCheckSum(Users user) throws Exception {
		String objClass = ConfigUtil.getObjectClassName(user);
		String activationURL = ConfigUtil.getConfigProp(ConfigUtil.APP_DEPLOY_URL);
		String actObjClass = ConfigUtil.getObjectClassName(activationURL);
		String path = ConfigUtil.getConfigProp(ConfigUtil.FORGET_PASSWORD_TEMPALTE);
		PasswordToken passwordToken = passwordTokenService.findByUserId(user.getId());
		Map<String,Object> model = new HashMap<>();
		model.put(objClass, user);
		model.put(actObjClass, activationURL);
		model.put("token", passwordToken.getToken());
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, path, "UTF-8", model);
		mailsender.sendMail("Reset Password", text, user .getUsername(), "USER_MANAGEMENT");
	}

}
