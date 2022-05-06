package com.inn.counselling.exception;

/**
 * Represents the exception which is thrown when encountering an error due
 * to system configuration.
 * 
 */
@SuppressWarnings("serial")
public class SystemConfigurationException extends RuntimeException {

	public SystemConfigurationException() {
		super();
	}

	public SystemConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemConfigurationException(String message) {
		super(message);
	}

	public SystemConfigurationException(Throwable cause) {
		super(cause);
	}

}
