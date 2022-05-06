package com.inn.counselling.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.IUserDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.Users;
import com.inn.counselling.service.impl.UserServiceImpl;
import com.inn.counselling.utils.QueryObject;

@Dao
public class UserDaoImpl extends HibernateGenericDao<Long, Users> implements IUserDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserDaoImpl() {
		super(Users.class);
	}

	@Override
	public Users create(Users user) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method create @param user: "+user);
		return super.create(user);
	}

	@Override
	public Users update(Users user) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method update @param user: "+user);
		return super.update(user);
	}

	@Override
	public Users findByPk(Long userPk) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method findByPk @param userPk: "+userPk);
		return super.findByPk(userPk);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method count @param queryObject: "+queryObject);
		return super.count(queryObject);
	}

	@Override
	public boolean delete(Users user)  throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method delete @param user: "+user);
		return super.delete(user);
	}

	@Override
	public boolean deleteByPk(Long userPk) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method deleteByPk @param userPk: "+userPk);
		return super.deleteByPk(userPk);
	}

	@Override
	public boolean contains(Users user) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method create @param user: "+user);
		return super.contains(user);
	}

	@Override
	public List<Users> findAll() throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method findAll");
		return super.findAll();
	}

	@Override
	public List<Users> find(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method find @param queryObject: "+queryObject);
		return super.find(queryObject);
	}

	@Override
	public Users findByUsername(String username) {
		LOGGER.info("Inside @class UserDaoImpl @method findByUserName @param username: "+username);
		try {
			String upperUsername = username.toLowerCase();
			Query query = getEntityManager().createNamedQuery("User.findByUsername")
					.setParameter("username", upperUsername);
			Users user = (Users) query.getSingleResult();
//			LOGGER.info("Inside @class UserDaoImpl @method findByUserName @param user: "+user);
			return user;
		} catch (NoResultException e) {
			LOGGER.error("NoResultException Error Inside @class UserDaoImpl @method findByUsernameOrConatctNo @param username: {}  ",username);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class UserDaoImpl @method findByUserName @param username: "+username);
			return null;
		}
	}

	@Override
	public Users findByUsernameOrConatctNo(String username,long contactno) {
		LOGGER.info("Inside @class UserDaoImpl @method findByUsernameOrConatctNo @param username: {} and conatctno {} ",username,contactno);
		try {
			String lowerUsername = username.toLowerCase();
			Query query = getEntityManager().createNamedQuery("User.findByUsernameOrConatctNo")
					.setParameter("username", lowerUsername)
					.setParameter("contactno", contactno);
			Users user = (Users) query.getSingleResult();
			LOGGER.info("Inside @class UserDaoImpl @method findByUsernameOrConatctNo @param user: "+user);
			return user;
		} catch (NoResultException e) {
			LOGGER.error("NoResultException Error Inside @class UserDaoImpl @method findByUsernameOrConatctNo @param username: {} and conatct no {} ",username,contactno);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class UserDaoImpl @method findByUsernameOrConatctNo @param username: {} and conatct no {} @cause: {} ",username,contactno,e);
			throw e;
		}
	}

	@Override
	public List<Users> findByExample(Users refEntity, String[] excludeProperty) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method findByExample @param refEntity: "+refEntity);
		return super.findByExample(refEntity, excludeProperty);
	}

	@Override
	public List<Users> search(SearchContext ctx, Integer maxLimit,Integer minLimit, String orderby)throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method search ");
		return super.search(ctx, maxLimit, minLimit, orderby);
	}

	@Override
	public List<JSONObject> findAudit(Long userPk) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method findAudit @param userPk: "+userPk);
		return super.findAudit(userPk);
	}

	@Override
	public long searchCount(SearchContext ctx) throws Exception {
		LOGGER.info("Inside @class UserDaoImpl @method searchCount");
		return super.searchCount(ctx);
	}

	@Override
	public Users loadDomainUsersByUserNameDomain(String username,String domainname){
		Query query = getEntityManager().createNamedQuery("Users.findByUsernameAndDomain")
				.setParameter("username", username)
				.setParameter("domainname", domainname);
		return (Users) query.getSingleResult();
	}

	@Override
	public Boolean unLock(String username) {
		LOGGER.info("Updating users status by username:"+username);
		Query query = this.getEntityManager().createNativeQuery("update password_expiry_details set locked=0,failed_attempts=0 where username = :username ").setParameter("username", username);
		Query query2 = this.getEntityManager().createNativeQuery("update password_feature_config set first_time_change=0 where username = :username ").setParameter("username", username);
		query2.executeUpdate();
		Integer  lock = (Integer) query.executeUpdate();
		if (lock==1){
			return true;
		}
		return false ;
	}


}
