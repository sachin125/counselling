package com.inn.counselling.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCustomBasicDataSource extends org.apache.tomcat.jdbc.pool.DataSource{

	private static Logger logger=LoggerFactory.getLogger(MyCustomBasicDataSource.class);

	public MyCustomBasicDataSource() {
		super();
	}

	public synchronized void setPassword(String encodedPassword){
		this.poolProperties.setPassword(decode(encodedPassword)) ;
	}

	public synchronized void setUsername(String encodedUsername){
		this.poolProperties.setUsername(decode(encodedUsername)) ;
	}


	private static String decode(String password) {
		byte[] decodedBytes = Base64.decodeBase64(password.getBytes());
		logger.info("Inside MyCustomBasicDataSource @method decode @param password: "+password+" "+new String(decodedBytes));
		return new String(decodedBytes).replace(ConfigUtil.getDbProp(ConfigUtil.DBUsernamePwdAppenderKey),"");
	}

	private static String encode(String value) {
		byte[] encodedBytes = Base64.encodeBase64(value.getBytes());
		logger.info("Inside MyCustomBasicDataSource @method encode @param encodedBytes: "+encodedBytes+" "+value);
		return new String(encodedBytes);
	}

	public static void main(String[] args) {
		String value="cm9vdHN5c3RlbQ==";
		System.out.println("decodeValue: "+decode(value));
		
		String value1="root";
		System.out.println("encodevalue: "+encode(value1));

	}
}
