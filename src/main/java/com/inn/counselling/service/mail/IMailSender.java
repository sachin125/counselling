package com.inn.counselling.service.mail;


import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.scheduling.annotation.Async;

public interface IMailSender {
	@Async
	public void sendMail(String subject, String message, String string,String eod) throws AddressException, MessagingException;
	@Async
	public void sendMailWithCC(String subject, String message, String string,String cc) throws AddressException, MessagingException;
	@Async
	public void sendMailWithCCAndBCC(String subject, String message, String string,String cc, String bcc) throws AddressException, MessagingException;
	
}
