package com.inn.counselling.security.authentication;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.inn.counselling.model.Users;
import com.inn.counselling.service.IUserService;

public class SessionsTimeoutHandler extends HttpSessionEventPublisher {

	private static final Logger LOGGER=LoggerFactory.getLogger(SessionsTimeoutHandler.class);

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		LOGGER.debug("SESSION EXPIRED EVENT RECEIVED");

		String userName =CustomerInfo.getCustomerUsername();
		if(userName!=null){
			LOGGER.debug(" User "+userName+" has logged out @ "+new Date());
			try {
				Users user = getUserService().findByUsername(userName);
				//auditLogout(user);
			} catch (Exception ex) {
				LOGGER.error("Exception class{} and message{}"+ex.getMessage());
			}


			LOGGER.debug(" -----[SESSION EXPIRED HANDELED]-----");
		}
		super.sessionDestroyed(event);
	}
	/*private void auditLogout(User user) {
		Audit audit = buildLogoutAudit(user);
		try {
			getAuditService().create(audit);
		} catch (Exception e) {
			LOGGER.error("Error Inside  @class :"+this.getClass().getName()+" @Method :auditLogout()"+e.getMessage());
		}
	}
	private Audit buildLogoutAudit(User user){
		Audit audit =  new Audit();
		audit.setUser(user);
		audit.setUserFullName(user != null ? user.getUsername() : null);
		audit.setDate(new Date());
		audit.setSuccess(Boolean.TRUE);
		audit.setAction(ConfigUtil.LOGOUT);
		audit.setAuditActionType(AuditActionType.LOGOUT);
		audit.setParameters(ConfigUtil.LOGGED_OUT);


		audit.setAuditActionName(AuditActionName.LOGOUT)	;
		if(getRequest()!=null){
			audit.setUserAgent(getRequest().getHeader("user-agent"));
			audit.setRemoteHost(getRequest().getRemoteHost());
			audit.setSessionid(getRequest().getRequestedSessionId());
			audit.setHost(getRequest().getLocalAddr()+":"+getRequest().getLocalPort());
			getRequest().getHeader("user-agent");
		}
		return audit;
	}*/
	private HttpServletRequest getRequest(){
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		if(attr!=null){
			return attr.getRequest();
		}
		return null;
	}

	private IUserService getUserService(){
		return ContextProvider.getContext().getBean(IUserService.class);
	}
	/*private IAuditService getAuditService(){
		return ContextProvider.getContext().getBean(IAuditService.class);
	}*/
}
