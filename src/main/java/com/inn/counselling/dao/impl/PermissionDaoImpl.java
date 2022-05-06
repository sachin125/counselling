package com.inn.counselling.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.IPermissionDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.Permission;
import com.inn.counselling.service.impl.PermissionServiceImpl;
import com.inn.counselling.utils.QueryObject;

@Dao
public class PermissionDaoImpl extends HibernateGenericDao<Long, Permission> implements IPermissionDao{

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);

	public PermissionDaoImpl() {
		super(Permission.class);
	}

	@Override
	public Permission create(Permission permission) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method create @param permission: "+permission);
		return super.create(permission);
	}

	@Override
	public Permission update(Permission permission) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method update @param permission: "+permission);
		return super.update(permission);
	}

	@Override
	public Permission findByPk(Long  permissionPk) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method findByPk @param permissionPk: "+permissionPk);
		return super.findByPk(permissionPk);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method count @param queryObject: "+queryObject);
		return super.count(queryObject);
	}

	@Override
	public boolean delete(Permission permission)  throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method delete @param permission: "+permission);
		return super.delete(permission);
	}

	@Override
	public boolean deleteByPk(Long permissionPk) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method deleteByPk @param permissionPk: "+permissionPk);
		return super.deleteByPk(permissionPk);
	}

	@Override
	public boolean contains(Permission permission) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method create @param permission: "+permission);
		return super.contains(permission);
	}

	@Override
	public List<Permission> findAll() throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method findAll");
		return super.findAll();
	}

	@Override
	public List<Permission> find(QueryObject queryObject) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method find @param queryObject: "+queryObject);
		return super.find(queryObject);
	}

	@Override
	public List<Permission> findByExample(Permission refEntity, String[] excludeProperty) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method findByExample @param refEntity: "+refEntity);
		return super.findByExample(refEntity, excludeProperty);
	}

	@Override
	public List<Permission> search(SearchContext ctx, Integer maxLimit,Integer minLimit, String orderby)throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method search ");
		return super.search(ctx, maxLimit, minLimit, orderby);
	}

	@Override
	public List<JSONObject> findAudit(Long permissionPk) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method findAudit @param permissionPk: "+permissionPk);
		return super.findAudit(permissionPk);
	}

	@Override
	public long searchCount(SearchContext ctx) throws Exception {
		LOGGER.info("Inside @class PermissionDaoImpl @method searchCount");
		return super.searchCount(ctx);
	}	
	
	@Override
	public List<Permission> findByRoleId(Integer roleid){
		Query query=getEntityManager().createNamedQuery("Role.findPermissionsByRoleId").setParameter("roleId", roleid);
		List<Permission> permissions  = query.getResultList();
		return permissions;
		
	}
}
