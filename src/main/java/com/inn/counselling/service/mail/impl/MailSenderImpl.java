package com.inn.counselling.service.mail.impl;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.stereotype.Component;

import com.inn.counselling.service.mail.IMailSender;
import com.inn.counselling.service.mail.MailServiceProvider;
import com.inn.counselling.utils.ConfigUtil;

@Component("mailSender")
public class MailSenderImpl implements IMailSender{

	public MailSenderImpl() {
	}

	
	public void sendMail(String subject, String message, String sentTo,
			String eod) throws AddressException, MessagingException {
		String email=ConfigUtil.getConfigProp(ConfigUtil.EMAIL_ID);
		String password=ConfigUtil.getConfigProp(ConfigUtil.EMAIL_PASSWORD);
		MailServiceProvider.sendEmail(email,ConfigUtil.decode(password), sentTo, "", "", subject,message);		
	}
	public void sendMailWithCC(String subject, String message, String sentTo,
			String cc) throws AddressException, MessagingException {
		String email=ConfigUtil.getConfigProp(ConfigUtil.EMAIL_ID);
		String password=ConfigUtil.getConfigProp(ConfigUtil.EMAIL_PASSWORD);
		MailServiceProvider.sendEmail(email,ConfigUtil.decode(password), sentTo, cc, "", subject,message);		
	}
	
	public void sendMailWithCCAndBCC(String subject, String message, String sentTo,
			String cc,String bcc) throws AddressException, MessagingException {
		String email=ConfigUtil.getConfigProp(ConfigUtil.EMAIL_ID);
		String password=ConfigUtil.getConfigProp(ConfigUtil.EMAIL_PASSWORD);
		MailServiceProvider.sendEmail(email,ConfigUtil.decode(password), sentTo, cc, bcc, subject,message);		
	}
	
}
