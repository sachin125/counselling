package com.inn.counselling.service.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.service.impl.UnauthServiceImpl;
import com.inn.counselling.utils.ConfigUtil;
import com.sun.mail.smtp.SMTPTransport;

final public class MailServiceProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UnauthServiceImpl.class);
	
	private static Properties props = null;
	
	private static final  String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	
	private static String GMAIL_MAIL_SERVER="gmail";

	private MailServiceProvider() {
	
	}

	public static Boolean setPropertyForMail(){
		if(ConfigUtil.getConfigProp(ConfigUtil.MAIL_SERVER_CONFIG).equalsIgnoreCase(GMAIL_MAIL_SERVER)){
			props = System.getProperties();
			props.setProperty("mail.smtps.host", ConfigUtil.getConfigProp(ConfigUtil.HOST_NAME));
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", ConfigUtil.getConfigProp(ConfigUtil.SMTP_PORT));
			props.setProperty("mail.smtp.socketFactory.port", ConfigUtil.getConfigProp(ConfigUtil.SMTP_PORT));
			props.setProperty("mail.smtps.auth", "true");
			props.put("mail.smtps.quitwait", "false");
		}else{
			props= System.getProperties();
			props.setProperty("mail.smtps.host", ConfigUtil.getConfigProp(ConfigUtil.HOST_NAME));
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.port", ConfigUtil.getConfigProp(ConfigUtil.SMTP_PORT));
			props.setProperty("mail.smtp.socketFactory.port", ConfigUtil.getConfigProp(ConfigUtil.SMTP_PORT));
		}
		return true;

	}

	public static void sendEmail(final String googleAppsEmailId, final String password, 
			String recipientEmail, String ccEmail, String bccEmail, 
			String title, String message) throws AddressException, MessagingException {
		
		LOGGER.info("Entry inside @class MailServiceProvider @method sendMail @param googleAppsEmailId , password {} {} ",googleAppsEmailId,password);
		
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

		/*
        If set to false, the QUIT command is sent and the connection is immediately closed. If set 
        to true (the default), causes the transport to wait for the response to the QUIT command.

        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
                http://forum.java.sun.com/thread.jspa?threadID=5205249
                smtpsend.java - demo program from javamail
		 */

		setPropertyForMail();
		Session session = Session.getInstance(props, null);

		// -- Create a new message --
		Message msg = new MimeMessage(session);

		// -- Set the FROM and TO fields --
		msg.setFrom(new InternetAddress(googleAppsEmailId /*+ "@gmail.com"*/));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

		if (ccEmail.length() > 0) {
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
		}
		if (bccEmail.length() > 0) {
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccEmail, false));
		}
		msg.setSubject(title);

		msg.setSentDate(new Date());
		msg.setDataHandler(new DataHandler(new HTMLDataSource(message)));
		SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

		t.connect(ConfigUtil.getConfigProp(ConfigUtil.HOST_NAME), googleAppsEmailId, password);
		t.sendMessage(msg, msg.getAllRecipients());      
		t.close();
	}



	static class HTMLDataSource implements DataSource {
		private String html;

		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}

		// Return html string in an InputStream.
		// A new stream must be returned each time.
		public InputStream getInputStream() throws IOException {
			if (html == null) {
				throw new IOException("Null HTML");
			}
			return new ByteArrayInputStream(html.getBytes());
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}

		public String getContentType() {
			return "text/html";
		}

		public String getName() {
			return "JAF text/html dataSource to send e-mail only";
		}
	}

}

