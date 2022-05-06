package com.inn.counselling.security.mgmt;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * represents password expiration details bean, this is a place holder for all password expiration
 * related properties.
 */

public class PasswordExpiryDetailBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordExpiryDetailBean.class);

	/**
	 * represents the container for keeping all the properties. 
	 */
	private	Map<String, Object> context;
	/**
	 * creates an instance of PasswordExpiryDetailBean class. 
	 */
	public PasswordExpiryDetailBean(){
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method PasswordExpiryDetailBean ");
		context = new HashMap<String, Object>();
	}
	/**
	 * creates an instance of PasswordExpiryDetailBean class with the given properties.
	 * @param props
	 */
	public PasswordExpiryDetailBean(Map<String, Object> props)
	{
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method PasswordExpiryDetailBean @props "+props);
		if(props != null){
			this.context = props;
		}
	}
	/**
	 * sets the property in the container.
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value){
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method setProperty @params key: "+key+" value:"+value);
		if(context == null){
			context = new HashMap<String, Object>();
		}
		context.put(key, value);
	}
	/**
	 * returns the property associated to given key.
	 * @param key
	 * @return
	 */
	public Object getProperty(String key){
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method getProperty @params key: "+key);
		if(context != null){
			return context.get(key);
		}
		return null; 
	}
	/**
	 * @param obj
	 * @return
	 */
	private int getIntegerValue(Object obj){
		int result;
		if(obj instanceof Integer){
			result = ((Integer)obj).intValue();
		}
		else if (obj instanceof String){
			result = Integer.parseInt((String) obj);
		}
		else if(obj instanceof BigDecimal)
		{
			result = Integer.parseInt(((BigDecimal)obj).toString());

		}
		else{
			throw new IllegalArgumentException("getIntegerValue Unsupported type "+obj+"]");
		}
		return result;
	}
	/**
	 * @return returns true if user is logging in for the first time otherwise return false.
	 */
	public boolean isFirstTimeLogin()
	{
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method isFirstTimeLogin @params ");
		Object loggedin = getProperty(PasswordExpiryConstants.IS_FIRST_TIME_LOGIN);
		int loginBool = getIntegerValue(loggedin);
		return (loginBool==1) ? true : false;
	}
	/**
	 * @return returns true if user is already locked otherwise returns false.
	 */
	public boolean isLocked()
	{
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method isLocked @params ");
		Object isLocked = getProperty(PasswordExpiryConstants.LOCKED);
		int locked = getIntegerValue(isLocked);
		return (locked==1) ? true : false;
	}
	/**
	 * @return returns the number of times the login attempt is already failed.
	 */
	public int getFailedAttemptsCount()
	{
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method getFailedAttemptsCount @params ");
		Object failedCnt = getProperty(PasswordExpiryConstants.FAILED_LOGIN_ATTEMPTS);
		return getIntegerValue(failedCnt);
	}
	@Override
	public String toString() 
	{
		if(context == null){
			return "[]";
		}
		else{
			return context.toString();
		}
	}
	/**
	 * @return returns a new detailed beans with defaulted properties.
	 */
	public static PasswordExpiryDetailBean getNewDetailBean() 
	{
		LOGGER.info("Entry inside @class PasswordExpiryDetailBean @method getNewDetailBean @params ");
		PasswordExpiryDetailBean pedb = new PasswordExpiryDetailBean();
		pedb.setProperty(PasswordExpiryConstants.FIRST_TIME_CHANGE, 0);
		pedb.setProperty(PasswordExpiryConstants.FAILED_LOGIN_ATTEMPTS, 0);
		pedb.setProperty(PasswordExpiryConstants.LOCKED, 0);
		return pedb;
	}
	/**
	 * @return returns the properties associated to password expiry as Map.
	 */
	public Map<String, Object> getContext() 
	{
		return context;
	}

}
