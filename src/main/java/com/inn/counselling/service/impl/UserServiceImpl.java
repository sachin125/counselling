package com.inn.counselling.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.inn.counselling.dao.ICollegeDao;
import com.inn.counselling.dao.IRoleDao;
import com.inn.counselling.dao.IStudentDao;
import com.inn.counselling.dao.IUserDao;
import com.inn.counselling.model.Domain;
import com.inn.counselling.model.PasswordToken;
import com.inn.counselling.model.Role;
import com.inn.counselling.model.Student;
import com.inn.counselling.model.Users;
import com.inn.counselling.service.IPasswordTokenService;
import com.inn.counselling.service.IUserService;
import com.inn.counselling.service.generic.AbstractService;
import com.inn.counselling.utils.AdvanceSearchResult;
import com.inn.counselling.utils.ConfigUtil;
import com.inn.counselling.utils.QueryObject;
import com.inn.counselling.wrapper.UserRegistrationWrapper;

@Service
public class UserServiceImpl extends AbstractService<Long, Users> implements IUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserServiceImpl() {
		super();
	}
	
	private IUserDao userDao;
	
	@Autowired
	private IStudentDao studentDao;

	@Autowired
	private ICollegeDao collegeDao;

	
	@Autowired
	public void setDao(IUserDao dao) {
		super.setDao(dao);
		this.userDao=dao;
	}
	
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IPasswordTokenService passwordTokenService;
	
	
	@Transactional
	@Override
	public Users create(Users user) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method create @param user");
		return super.create(user);
	}

	@Transactional
	@Override
	public Users update(Users user) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method update @param user"+user);
		return super.update(user);
	}

	@Override
	public Users findByPk(Long userPk) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method findByPk @param userPk"+userPk);
		return super.findByPk(userPk);
	}

	@Override
	public Long count(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method count @param queryObject "+queryObject);
		return super.count(queryObject);
	}
	@Transactional
	@Override
	public boolean delete(Users user) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method delete @param user"+user);
		return super.delete(user);
	}
	@Transactional
	@Override
	public boolean deleteByPk(Long userPk) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method deleteByPk @param userPk: "+userPk);
		return super.deleteByPk(userPk);
	}

	@Override
	public boolean contains(Users user) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method contains @param user: "+user);
		return super.contains(user);
	}

	@Override
	public List<Users> findAll() throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method findAll");
		return super.findAll();
	}

	@Override
	public List<Users> find(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method find @param queryObject: "+queryObject);
		return super.find(queryObject);
	}

	@Override
	public Users findByUsername(String username) {
		LOGGER.info("Inside @class UserServiceImpl @method findByUserName @param username: "+username);
		return userDao.findByUsername(username);
	}

	@Override
	public AdvanceSearchResult<Users> advanceSearch(QueryObject queryObject)
			throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method advanceSearch @param queryObject: "+queryObject);
		return super.advanceSearch(queryObject);
	}

	@Override
	public List<Users> findByExample(Users refEntity, String[] excludeProperty) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method findByExample @param refEntity: "+refEntity);
		return super.findByExample(refEntity, excludeProperty);
	}

	
	@Transactional
	@Override
	public void setUserAuthentication(Users newsc) throws Exception{
		try{

			SecurityContextHolder.getContext().getAuthentication();
			final Authentication auth=null;//new CustomUsernamePasswordAuthenticationToken(newsc.getUsername(),newsc.getPassword());			 

		//	CustomUsernamePasswordAuthenticationToken tokenAuth=(CustomUsernamePasswordAuthenticationToken) auth;
			//		 	 tokenAuth.setUserid(newsc.getId());

			SecurityContext sc=  SecurityContextHolder.getContextHolderStrategy().getContext();
			sc.setAuthentication(auth);
			ServletRequestAttributes attr=(ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
			if(attr!=null){
				attr.getRequest().getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", sc);
			}

		}catch(Exception updateExp)
		{
			LOGGER.error("Invalid or null Username:: "+updateExp);
			LOGGER.error(updateExp.getMessage());
		}
		LOGGER.info("Authenticate Successfully");
	}

	
	private String getDetailUserByHttp(String detailurl) throws HttpException, IOException{
		HttpClient httpclient = new HttpClient();		
		GetMethod get=new GetMethod(detailurl);
		get.setFollowRedirects(true);
		HttpMethodParams param = new HttpMethodParams();
		get.setParams(param);
		httpclient.executeMethod(get);
		return get.getResponseBodyAsString();
	}
	
	private InputStream getDetailUserByHttpInputStream(String detailurl) throws HttpException, IOException{
		HttpClient httpclient = new HttpClient();		
		GetMethod get=new GetMethod(detailurl);
		get.setFollowRedirects(true);
		HttpMethodParams param = new HttpMethodParams();
		get.setParams(param);
		httpclient.executeMethod(get);
		return get.getResponseBodyAsStream();
	}

	@Override
	public List<JSONObject> findAudit(Long entityPk) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method findAudit @param entityPk: "+entityPk);
		return super.findAudit(entityPk);
	}

	@Override
	public long searchCount(SearchContext ctx) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method searchCount");
		return super.searchCount(ctx);
	}

	
	@Override
	public List<Users> search(SearchContext ctx, Integer maxLimit,Integer minLimit, String orderby) throws Exception {
		LOGGER.info("Inside @class UserServiceImpl @method search");
		return userDao.search(ctx, maxLimit, minLimit, orderby);
	}

	@Transactional
	@Override
	public Boolean unLock(String username) {
		return userDao.unLock(username);
	}

	@Override
	public Users findByEmail(String email) {
		return userDao.findByUsername(email);
	}
	
	
	@Transactional
	@Override
	public Users gmailRegistration(String gaccesstoken) throws JSONException, Exception, HttpException, IOException		{
		LOGGER.debug("Google Social Process Start");
		Users users = new Users();
		String urlGmailUser=ConfigUtil.getConfigProp(ConfigUtil.GOOGLE_USER_DETAIL_URL)+gaccesstoken;		
		String detail=getDetailUserByHttp(urlGmailUser);	 
		JSONObject json=new JSONObject(detail);    
		String userId=(String) json.get("id");
		String emailId=(String) json.get("email");
		Users user=this.findByUsername(emailId);  
		LOGGER.debug("Find User exist in application or not");

		if(user==null){
			LOGGER.debug("New User! Start registration process");
			return createNewGoogleUser(users,json,gaccesstoken );
		}else{
		}
		return user;

	}

	@Transactional
	@Override
	public Users createUsersFromFacebook(String userID,String facebookToken ) throws Exception {
		LOGGER.info("Facebook Registration Started");
		Users users = new Users();
		String urlUserDetail = ConfigUtil.getConfigProp(ConfigUtil.FACEBOOK_GRAPH_API_USER_DETAIL_URL)+facebookToken;
		String detail=getDetailUserByHttp(urlUserDetail);       
		JSONObject json=new JSONObject(detail);
		String userId=(String) json.get("id");
		String userProfilePic="https://graph.facebook.com/"+userId+"/picture?type=small";
		InputStream pic=getDetailUserByHttpInputStream(userProfilePic);
		byte[] image = null;

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = pic.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}


		image = buffer.toByteArray();
		buffer.flush();
		String emailId=(String) json.get("email");
		Users user=this.findByUsername(emailId);

		if(user==null) {
			return createFacebookUser(users,json,facebookToken,image );
		}
		return user;
	}

	private Users createNewGoogleUser(Users user,JSONObject json,String gaccesstoken ) throws JSONException,Exception {
		try{
			user.setCheckSum(passwordEncoder.encode("test123"));
			String []spilt=((String)json.get("email")).split("@");		
			user.setUsername(spilt[0]);
			user.setUsername((String)json.get("email"));
			user.setEnabled(true);
			Role role= roleDao.findByPk(2l);
			Set<Role> roles=new HashSet<Role>();
			roles.add(role);
//			users.setRoles(roles);
			LOGGER.debug("New user on platform");
			Users creUsers= this.create(user);
			PasswordToken passwordToken = passwordTokenService.findByUserId(creUsers.getId());
			if(passwordToken==null) {
				passwordToken = new PasswordToken();
			}
			passwordToken.setToken(UUID.randomUUID().toString());
			passwordToken.setExpiryDate(null);
			passwordToken.setUserId(creUsers.getId());
			passwordTokenService.create(passwordToken);
			LOGGER.debug("User name "+user.getUsername());
			//SocialCredentials scNewntity=new SocialCredentials();
			/*scNewntity.setGmailToken(gaccesstoken);
			scNewntity.setGoogleUserid((String)json.get("id"));
			scNewntity.setUser(creUsers);*/
			//socialCredentials.create(scNewntity);
			return creUsers;	
		}catch(Exception ex){
			LOGGER.error("Error Inside  @class :"+this.getClass().getName()+" @Method :deleteByPk()"+ex.getMessage());
			throw ex;
		}
	}
	
	private String generateActivationCode(){
		return RandomStringUtils.random(10, 0, 20, true,true, "1qaz2xsw3dce4rft5vbg6nhy7ujm8kl9i0o".toCharArray());
	}

	private Users createFacebookUser(Users user, JSONObject json, String facebookToken,byte[] image) throws JSONException ,Exception{
		try{
			user.setCheckSum(passwordEncoder.encode("test123"));
			String []spilt=((String)json.get("email")).split("@");		
			user.setUsername(spilt[0]);
			user.setFirstname((String) json.get("first_name"));
			user.setLastname((String) json.get("last_name"));
			user.setUsername((String) json.get("email"));
			user.setEnabled(true);
//			users.setImageFile(image);
			Role role= roleDao.findByPk(2l);
			Set<Role> roles=new HashSet<Role>();
			roles.add(role);
///			users.setRoles(roles);
			LOGGER.debug("New user on platform");
			LOGGER.debug("Registration is processed");
			Users creUsers= this.create(user);
			PasswordToken passwordToken = passwordTokenService.findByUserId(creUsers.getId());
			if(passwordToken==null) {
				passwordToken = new PasswordToken();
			}
			passwordToken.setToken(UUID.randomUUID().toString());
			passwordToken.setExpiryDate(null);
			passwordToken.setUserId(creUsers.getId());
			passwordTokenService.create(passwordToken);
			
			LOGGER.debug("User name "+user.getUsername());
			/*SocialCredentials scNewntity=new SocialCredentials();
			scNewntity.setFacebookToken(facebookToken);
			scNewntity.setFacebookUserid((String) json.get("id"));
			scNewntity.setUser(creUsers);

			socialCredentials.create(scNewntity);	*/	
			return creUsers;
		}catch(Exception ex)
		{
			LOGGER.error("Error Inside  @class :"+this.getClass().getName()+" @Method :deleteByPk()"+ex.getMessage());
			throw ex;
		}
	}

	@Transactional
	@Override
	public Users userRegistration(UserRegistrationWrapper userRegistrationWrapper) throws Exception {
		LOGGER.info("Entry inside @class UserServiceImpl @method userRegistration @param user {} ",userRegistrationWrapper);
		try {
			Users existingUser = userDao.findByUsernameOrConatctNo(userRegistrationWrapper.getUsername(),userRegistrationWrapper.getContactno());
			if(existingUser!=null) {
				throw new Exception("User already exist with given emailid, PLease use another emailid");
			}
			Role role = roleDao.findbyName("ROLE_Student");
			Set<Role> setRole = new HashSet();
			setRole.add(role);
			Domain domain = new Domain();
			domain.setId(1);
			
			Users user = new Users();
			user.setCheckSum(getEncryptedEncodedPassword(userRegistrationWrapper.getCheckSum()));
			user.setFirstname(userRegistrationWrapper.getFirstname());
			user.setLastname(userRegistrationWrapper.getLastname());
			user.setUsername(userRegistrationWrapper.getUsername());
			user.setContactno(userRegistrationWrapper.getContactno());
			user.setDob(userRegistrationWrapper.getDob());
			user.setGender(userRegistrationWrapper.getGender());
			user.setCreatedTime(new Date());
			user.setModifiedTime(new Date());
			user.setDeleted(false);
			user.setEnabled(true);
			user.setRoles(setRole);
			user.setDomain(domain);
			Users newUsers = userDao.create(user);

			PasswordToken passwordToken = passwordTokenService.findByUserId(newUsers.getId());
			if(passwordToken==null) {
				passwordToken = new PasswordToken();
			}
			passwordToken.setToken(UUID.randomUUID().toString());
			passwordToken.setExpiryDate(null);
			passwordToken.setUserId(newUsers.getId());
			passwordTokenService.create(passwordToken);
			
			Student student = studentDao.createnStudent(newUsers);
			
			return newUsers;
		}catch(Exception e) {
			LOGGER.info("Entry inside @class UserServiceImpl @method userRegistration @cause {} ",e);
			throw e;
		}
	}
	
	@Override
	public String getEncryptedEncodedPassword(String password) {
		password=password.trim();
		byte[] valueDecoded = Base64.decodeBase64(password.getBytes());
		String pwd1 = new String(valueDecoded);
		pwd1 = pwd1.trim();
		String encryptedPassword = passwordEncoder.encode(pwd1);
		LOGGER.debug("@method getEncryptedEncodedPassword : password {} valueDecoded {} new_String(valueDecoded) {} encryptedPassword {} ",password,valueDecoded,pwd1,encryptedPassword);
		return encryptedPassword;
	}

	@Override
	public boolean matchEncryptedPassword(String rawPassword,String encodedPassword) {
		LOGGER.debug("matchEncryptedPassword rawPassword "+rawPassword);
		LOGGER.debug("matchEncryptedPassword encodedPassword "+encodedPassword);
		LOGGER.debug("matchEncryptedPassword passwordEncoder.matches: "+passwordEncoder.matches(rawPassword, encodedPassword));
		LOGGER.debug("matchEncryptedPassword passwordEncoder.encode: "+passwordEncoder.encode(rawPassword));

		return true;
	}
	
}
