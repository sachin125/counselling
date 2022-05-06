package com.inn.counselling.security.authentication;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for providing the Spring ApplicationContext  object.
 * It implements ApplicationContextAware interface and so, is notified of the ApplicationContext that it runs in. 
 *
 */
@Component
public class ContextProvider implements ApplicationContextAware{

	private static ApplicationContext appContext;

	public void setApplicationContext(ApplicationContext appContext)throws BeansException{
		this.appContext = appContext;
	}

	public static ApplicationContext getContext(){
		return appContext;
	}
}
