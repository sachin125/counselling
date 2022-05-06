package com.inn.counselling.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.IRoleDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.Role;
import com.inn.counselling.service.impl.RoleServiceImpl;
import com.inn.counselling.utils.QueryObject;

@Dao
public class RoleDaoImpl extends HibernateGenericDao<Long, Role> implements IRoleDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleDaoImpl.class);

	public RoleDaoImpl() {
		super(Role.class);
	}

	@Override
	public Role create(Role roles) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method create @param roles: "+roles);
		return super.create(roles);
	}

	@Override
	public Role update(Role roles) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method update @param roles: "+roles);
		return super.update(roles);
	}

	@Override
	public Role findByPk(Long  rolesPk) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method findByPk @param rolesPk: "+rolesPk);
		return super.findByPk(rolesPk);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method count @param queryObject: "+queryObject);
		return super.count(queryObject);
	}

	@Override
	public boolean delete(Role roles)  throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method delete @param roles: "+roles);
		return super.delete(roles);
	}

	@Override
	public boolean deleteByPk(Long rolesPk) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method deleteByPk @param rolesPk: "+rolesPk);
		return super.deleteByPk(rolesPk);
	}

	@Override
	public boolean contains(Role roles) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method create @param roles: "+roles);
		return super.contains(roles);
	}

	@Override
	public List<Role> findAll() throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method findAll");
		return super.findAll();
	}

	@Override
	public List<Role> find(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method find @param queryObject: "+queryObject);
		return super.find(queryObject);
	}

	@Override
	public List<Role> findByExample(Role refEntity, String[] excludeProperty) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method findByExample @param refEntity: "+refEntity);
		return super.findByExample(refEntity, excludeProperty);
	}

	@Override
	public List<Role> search(SearchContext ctx, Integer maxLimit,Integer minLimit, String orderby)throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method search ");
		return super.search(ctx, maxLimit, minLimit, orderby);
	}

	@Override
	public List<JSONObject> findAudit(Long rolesPk) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method findAudit @param rolesPk: "+rolesPk);
		return super.findAudit(rolesPk);
	}

	@Override
	public long searchCount(SearchContext ctx) throws Exception {
		LOGGER.info("Inside @class RoleDaoImpl @method searchCount");
		return super.searchCount(ctx);
	}


	@Override
	public Role findbyName(String rolename){
		Query query=getEntityManager().createNamedQuery("Role.findByName").
				setParameter("rolename", rolename);
		Role roles=(Role) query.getSingleResult();
		return roles;		
	}	
}
