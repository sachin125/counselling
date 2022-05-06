package com.inn.counselling.exception.mapper;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.rest.impl.CollegeRestImpl;

public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception>{

	private static final  Logger LOGGER=LoggerFactory.getLogger(CollegeRestImpl.class);
	
	@Override
	public Response toResponse(Exception restException) {
		String errormsg = "";
		if(restException!=null) {
			if(restException.getMessage()!=null) {
				errormsg = restException.getMessage().replaceAll(":", "-");
			}else {
				errormsg = restException.toString();
			}			
		}
		LOGGER.debug("Entry inside @class ExceptionMapper @method toResponse @param {} ",errormsg);
		if(errormsg.contains("NullPointerException")) {
			errormsg = "NullPointerException";			
		}else if(errormsg.contains("Exception")) {
			errormsg = errormsg.substring(errormsg.indexOf("Exception")+11, errormsg.length()-1);			
		}
		String error ="{\"errormsg\":\""+errormsg+"\"}";
		
		return Response.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(error).build();
	}

}
