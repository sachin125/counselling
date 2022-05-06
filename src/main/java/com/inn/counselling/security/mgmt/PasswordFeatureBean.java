package com.inn.counselling.security.mgmt;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a place holder for Password features.
 *
 */

public class PasswordFeatureBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordExpiryDetailBean.class);

	/**
	 * represents the context which holds the value for password features. 
	 */
	private Map<String, Object> context;

	/**
	 * initialize the PasswordFeatureBean with the given parameters.
	 * @param context
	 */
	public PasswordFeatureBean(Map<String, Object> context) 
	{
		LOGGER.info("Entry inside @class PasswordFeatureBean @method PasswordFeatureBean @param context: "+context);
		if(context == null)
		{
			this.context = new HashMap<String, Object>();
		}
		else
		{
			this.context = context;
		}
	}
	/**
	 * default constructor for the PasswordFeatureBean.
	 */
	public PasswordFeatureBean()
	{
		LOGGER.info("Entry inside @class PasswordFeatureBean @method PasswordFeatureBean");
		this.context = new HashMap<String, Object>();
	}
	/**
	 * returns true when first time login feature is switched on.
	 * false if first time login feature is turned off.
	 * @return
	 */
	public boolean isFirstTimeLoginFeature()
	{
		LOGGER.info("Entry inside @class PasswordFeatureBean @method isFirstTimeLoginFeature @param context: "+context);
		Object ftlFeatureVal = getProperty(PasswordExpiryConstants.FIRST_TIME_CHANGE);
		if(ftlFeatureVal instanceof Integer)
		{
			return ((Integer) ftlFeatureVal).equals(1)? true:false;
		}
		else if(ftlFeatureVal instanceof String)
		{
			return "1".equals(ftlFeatureVal);
		}
		else if(ftlFeatureVal instanceof BigDecimal)
		{
			return (Integer.parseInt(((BigDecimal)ftlFeatureVal).toString())==1);

		}
		else if(ftlFeatureVal == null)
		{
			return false;
		}
		else
		{
			throw new IllegalArgumentException("Illegal value for firstTimeLogin feature ["+ftlFeatureVal+"]");
		}
	}
	/**
	 * @return returns the password expiration value set in the bean.
	 */
	public int getPasswordExpiryVal()
	{
		LOGGER.info("Entry inside @class PasswordFeatureBean @method getPasswordExpiryVal @param context: "+context);
		Object pe = getProperty(PasswordExpiryConstants.PASSWORD_EXPIRY);
		int peIntVal;
		if(pe instanceof Integer)
		{
			peIntVal = ((Integer)pe).intValue();
		}
		else if(pe instanceof String)
		{
			peIntVal = Integer.valueOf((String)pe);
		}
		else if(pe instanceof BigDecimal)
		{
			peIntVal = Integer.parseInt(((BigDecimal)pe).toString());
		}
		else
		{
			throw new IllegalArgumentException("Unsupported type for password expiry ["+pe+"] ");
		}

		return peIntVal;
	}
	/**
	 * sets the a value associated to key in the context.
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, Object value)
	{
		if(context == null)
		{
			context = new HashMap<String, Object>();
		}
		context.put(key, value);
	}

	/**
	 * gets the value associated to key.
	 *
	 * @param key the key
	 * @return the property
	 */
	public Object getProperty(String key)
	{
		if(context != null)
		{
			return context.get(key);
		}
		return null; 
	}
	@Override
	public String toString() {
		if(context != null){
			return context.toString();
		}
		else{
			return "[]";
		}
	}

}
