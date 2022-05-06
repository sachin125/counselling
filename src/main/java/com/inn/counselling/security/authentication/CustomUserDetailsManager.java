package com.inn.counselling.security.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.inn.counselling.dao.IUserDao;
/**
 * 
 * The purpose of this class is to extend the functionality
 * provided by the JdbcDaoImpl class. The extra features the class
 * provides is availability of the domain name selection for a 
 * particular user.
 */
public class CustomUserDetailsManager extends JdbcUserDetailsManager {

	private static final Logger LOGGER =  LoggerFactory.getLogger(CustomUserDetailsManager.class);
	
/*	public static final String USER_BY_USERNAME_DOMAIN = "select user.username,user.checksum,user.enabled,"
 			+ "domain.name from user,domain where user.domain_id = domain.id and "
 			+ "user.username = ? and domain.name = ?";

	public static final String AUTHORITIES_BY_USERNAME_DOMAIN = "select u.username as username,p.name as authorityname from user u," +
			"role r,permission p,user_role ur, role_permission rp,domain d where u.username = ? and d.id = u.domain_id and " +
			"d.name = ? and ur.role_id = r.id and ur.user_id = u.id and r.id = rp.role_id and rp.permission_id = p.id";
	*/
	public static final String USER_BY_USERNAME_DOMAIN = "select u.username,u.checksum,u.enabled,"
 			+ "d.name from users u,domain d where u.domain_id = d.id and "
 			+ "u.username = ? and d.name = ?";

	public static final String AUTHORITIES_BY_USERNAME_DOMAIN = "select u.username as username,p.name as authorityname from users u,role r,permission p, user_role ur,role_permission rp,domain d where u.username = ? and d.id = u.domain_id and d.name = ? and ur.user_id=u.id and ur.role_id=r.id and r.id = rp.role_id and rp.permission_id = p.id";	

	private String authoritiesByUserNameDomainQuery = AUTHORITIES_BY_USERNAME_DOMAIN;

	private String userByUserNameDomainQuery = USER_BY_USERNAME_DOMAIN;
	
	public String getAuthoritiesByUserNameDomainQuery() {
		LOGGER.info("inside @class CustomUserDetailsManager @method getAuthoritiesByUserNameDomainQuery");
		return authoritiesByUserNameDomainQuery;
	}

	public void setAuthoritiesByUserNameDomainQuery(String authoritiesByUserNameDomainQuery) {
		LOGGER.info("inside @class CustomUserDetailsManager @method setAuthoritiesByUserNameDomainQuery @param "+authoritiesByUserNameDomainQuery);
		this.authoritiesByUserNameDomainQuery = authoritiesByUserNameDomainQuery;
	}

	public String getUserByUserNameDomainQuery() {
		LOGGER.info("inside @class CustomUserDetailsManager @method getUserByUserNameDomainQuery");
		return userByUserNameDomainQuery;
	}

	public void setUserByUserNameDomainQuery(String userByUserNameDomainQuery) {
		LOGGER.info("inside @class CustomUserDetailsManager @method setUserByUserNameDomainQuery @param "+userByUserNameDomainQuery);
		this.userByUserNameDomainQuery = userByUserNameDomainQuery;
	}

	//yeh call  hoti hain
	public CustomUser loadDomainUserByUserNameDomain(String username,String domainname) throws UsernameNotFoundException, DataAccessException {

		LOGGER.info("First Entry inside @class CustomUserDetailsManager @method loadDomainUserByUserNameDomain @param "+username+" domainname:"+domainname);

		LOGGER.debug("inside @class CustomUserDetailsManager @method loadDomainUserByUserNameDomain @param userByUserNameDomainQuery: "+userByUserNameDomainQuery);
		LOGGER.debug("inside @class CustomUserDetailsManager @method loadDomainUserByUserNameDomain @param authoritiesByUserNameDomainQuery: "+authoritiesByUserNameDomainQuery);
		
		List<CustomUser> users = loadDomainUsersByUserNameDomain(username,  domainname );

		if (users.size() == 0) {
			throw new UsernameNotFoundException(
					messages.getMessage("JdbcDaoImpl.notFound", new Object[]{username}, "Username {0} not found"));
		}

		CustomUser customUser = users.get(0); 

		LOGGER.info("inside @class CustomUserDetailsManager @method loadDomainUserByUserNameDomain @param domainUser: "+customUser.toString());

		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();

		if (this.getEnableAuthorities()) {
			dbAuthsSet.addAll(loadDomainUserAuthorities(username,   domainname ));
		}

		if (this.getEnableGroups()) {
			dbAuthsSet.addAll(loadGroupAuthorities(customUser.getUsername()));
		}

		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);
		for(GrantedAuthority auth: dbAuths) {
			LOGGER.debug("for lopp for auth inside @class CustomUserDetailsManager @method loadDomainUserByUserNameDomain @param authoritiesByUserNameDomainQuery: "+auth.getAuthority());
		}
		super.addCustomAuthorities(customUser.getUsername(), dbAuths);

		if (dbAuths.size() == 0) {
			throw new UsernameNotFoundException(
					messages.getMessage("JdbcDaoImpl.noAuthority",
							new Object[] {username}, "User {0} has no GrantedAuthority"));
		}

		return createDomainUserDetails(username, customUser, dbAuths);
	}
	
	protected List<CustomUser> loadDomainUsersByUserNameDomain(String username,String domain) {
		LOGGER.info("inside @cclass CustmomUserDetailManager @method loadDomainUsersByUserNameDomain @username "+username+" domain: "+domain);
		
		/*User user = userDao.loadDomainUsersByUserNameDomain(username, domain);
		CustomUser customUser = new CustomUser(username,   domain , user.getCheckSum(), user.isEnabled(), true, true, true, AuthorityUtils.NO_AUTHORITIES);
		List<CustomUser> customUsers = new ArrayList<CustomUser>();
		customUsers.add(customUser);
		return  customUsers;
		*/
		return getJdbcTemplate().query(this.userByUserNameDomainQuery, new String[] {username,domain}, new RowMapper<CustomUser>() {
			public CustomUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				String username = rs.getString(1);
				String password = rs.getString(2);
				boolean enabled = rs.getBoolean(3);
				String domain = rs.getString(4);
				return new CustomUser(username,   domain , password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
			}
		});
	}
	protected List<GrantedAuthority> loadDomainUserAuthorities(String username,String domain) {
		LOGGER.info("inside @cclass CustmomUserDetailManager @method loadDomainUserAuthorities @username "+username+" domain: "+domain);
		return getJdbcTemplate().query(this.authoritiesByUserNameDomainQuery, new String[] {username,domain}, new RowMapper<GrantedAuthority>() {
			public GrantedAuthority mapRow(ResultSet rs, int rowNum) throws SQLException {
				String roleName = getRolePrefix() + rs.getString(2);
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
				return authority;
			}
		});
	}
	protected CustomUser createDomainUserDetails(String username, CustomUser customUser,
			List<GrantedAuthority> combinedAuthorities) {
		LOGGER.info("inside @class CustomUserDetailsManager @method createDomainUserDetails @param username: "+username+" customUser: "+customUser+" combinedAuthorities: "+combinedAuthorities);    	
		String returnUsername = customUser.getUsername();
		if (!this.isUsernameBasedPrimaryKey()) {
			returnUsername = username;
		}
		return new CustomUser(returnUsername, customUser.getDomain() ,customUser.getPassword(), customUser.isEnabled(),
				true, true, true, combinedAuthorities);
	}
}
