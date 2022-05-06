package com.inn.counselling.security.mgmt;

import java.util.List;


/**
 * Represents exception which will be thrown when password is not vaildated successfully. 
 * Also, this encapsulate error messages list which represents complexity checks 
 * and other validation failure.  
 *
 */
public class PasswordValidationFailedException extends RuntimeException{

	private List<String> errorMessages;

	public PasswordValidationFailedException(List<String> errorMessages) {
		super();
		this.errorMessages = errorMessages;
	}

	public PasswordValidationFailedException() {
		super();
	}
	
	public PasswordValidationFailedException(String message,
			Throwable throwable) {
		super(message, throwable);
	}

	public PasswordValidationFailedException(String message) {
		super(message);
	}

	public PasswordValidationFailedException(Throwable throwable) {
		super(throwable);
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
	
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
}
