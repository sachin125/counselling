package com.inn.counselling.exception;


public class ValueNotFoundException extends Exception {
	
	private static final long serialVersionUID = -3892116817542890495L;

	public ValueNotFoundException(Exception e){
		super(e.getMessage(),e);
	}

	public ValueNotFoundException(String string){
		super(string);
	}
	
	public ValueNotFoundException(String string,String guimasaage){
		super(guimasaage);

	}
}
