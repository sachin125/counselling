package com.inn.counselling.security.mgmt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.inn.counselling.utils.StringUtils;

public class PasswordHistoryService {

	private static final Logger LOGGER = Logger.getLogger(PasswordHistoryService.class);

	/**
	 * represents maximum number of password history which shall be maintained. 
	 */
	private int maxHistoryCount;
	/**
	 * represents the Map of regular expression with regular expression and description as value.
	 * These regular expression shall be validated on the new password which is set. If failed 
	 * the associated message shall be shown.
	 */
	private Map<String,String> complexityChecks;
	/**
	 * return return false if given password already present in the history 
	 * otherwise return true.
	 * @param history comma separated list of passwords
	 * @param password
	 * @return
	 */
	public boolean isNew(String history, String password){
		LOGGER.info("Entry inside @class PasswordHistoryService @method isNew @param history:"+history+" password:"+password);

		if(!StringUtils.hasValue(history)){
			return true;
		}

		String[] passwordArray = history.split(",");

		Stack<String> pswdStack = new Stack<String>();
		for (int i = 0; i < passwordArray.length; i++) {
			pswdStack.push(passwordArray[i]);
		}

		int maxHistory = getMaxHistoryCount();

		LOGGER.debug("isNew() - maxHistory["+maxHistory+"], history["+history+"], newPassword["+password+"]");

		for(int j=0; j<maxHistory && !pswdStack.isEmpty(); j++)
		{
			String oldPwd = pswdStack.pop();
			if(password.equals(oldPwd)){
				return false;
			}
		}
		return true;
	}
	/**
	 * update the newPassword in the history and return the new password history.
	 * @param history
	 * @param newPassword
	 * @return
	 */
	public String updateHistory(String history, String newPassword)
	{
		LOGGER.info("Entry inside @class PasswordHistoryService @method updateHistory @param history:"+history+" password:"+newPassword);

		if(!StringUtils.hasValue(history)){
			return newPassword;
		}
		String[] passwordArray = history.split(",");
		Stack<String> pswdStack = new Stack<String>();
		for (int i = 0; i < passwordArray.length; i++) {
			pswdStack.push(passwordArray[i]);
		}

		int maxHistory = getMaxHistoryCount();

		String resultHistory = newPassword;
		for(int j=0; j<(maxHistory-1) && !pswdStack.isEmpty(); j++)
		{
			resultHistory = pswdStack.pop()+","+resultHistory;
		}

		return resultHistory;
	}
	/**
	 * password complexity checks are verified and respective error messages are generated.
	 * @param detailBean
	 * @param newPassword
	 * @return returns null if password is validated successfully otherwise List of error 
	 * messages are returned.
	 */
	public List<String> validatePassword(String history, String newPassword){
		LOGGER.info("Entry inside @class PasswordHistoryService @method validatePassword @param history:"+history+" password:"+newPassword);
		
		List<String> errorMessages = new LinkedList<String>();


		Map<String, String> complexMap = getComplexityChecks();


		if(complexMap==null){

			complexMap = new HashMap<String, String>();
		}

		Set<String> regExps = complexMap.keySet();
		for(String regex : regExps){

			if(!newPassword.matches(regex))
			{
				errorMessages.add(complexMap.get(regex));
			}
			LOGGER.debug("regex["+regex+"] passed["+newPassword.matches(regex)+"]");
		}


		if(!isNew(history, newPassword)){
			errorMessages.add("message.password.alreadyInHistory");
		}
		LOGGER.debug("Error Messages "+ errorMessages);
		if(errorMessages.size()>0){
			return errorMessages;
		}
		else{
			return null;
		}
	}


	/**
	 * @return
	 */
	public int getMaxHistoryCount() {
		return maxHistoryCount;
	}
	/**
	 * @param maxHistoryCount
	 */
	public void setMaxHistoryCount(int maxHistoryCount) {
		this.maxHistoryCount = maxHistoryCount;
	}
	/**
	 * @return
	 */
	public Map<String, String> getComplexityChecks() {
		return complexityChecks;
	}
	/**
	 * @param complexityChecks
	 */
	public void setComplexityChecks(Map<String, String> complexityChecks) {
		this.complexityChecks = complexityChecks;
	}

}
