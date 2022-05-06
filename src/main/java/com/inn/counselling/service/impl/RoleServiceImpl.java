package com.inn.counselling.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.dao.IRoleDao;
import com.inn.counselling.model.Permission;
import com.inn.counselling.model.Role;
import com.inn.counselling.service.IRoleService;
import com.inn.counselling.service.generic.AbstractService;


@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Long, Role> implements IRoleService {

	private static final Logger logger=LoggerFactory.getLogger(RoleServiceImpl.class);

	private	IRoleDao roleDao;

	@Autowired
	public void setDao(IRoleDao dao) {
		super.setDao(dao);
		roleDao = dao;
	}

	@Override
	public List<Role> search(Role roles) throws Exception{
		return super.search(roles);
	}

	@Override
	public List<Role> search(SearchContext ctx,Integer maxLimit, Integer minLimit, String orderby)throws Exception {
		return roleDao.search(ctx, maxLimit, minLimit, orderby);
	}

	@Override
	public Role findByPk(Long  primaryKey) throws Exception{
		return super.findByPk(primaryKey);
	}

	@Override
	public List<Role> findAll() throws Exception{
		return super.findAll();
	}

	@Override
	public Role create(Role roles) throws Exception{
		return super.create(roles);
	}

	@Override
	public Role update(Role role) throws Exception{
		try{
			Role roles2=findByPk(role.getId());
			Set<Permission> per=roles2.getPermissions();
			for (Permission permissions : per) {
				if(permissions.getName().equalsIgnoreCase("Userread")||permissions.getName().equalsIgnoreCase("worklist")||permissions.getName().equalsIgnoreCase("usermgmt")||permissions.getName().equalsIgnoreCase("reporting")||permissions.getName().equalsIgnoreCase("monitoring")){
					role.getPermissions().add(permissions);
				}
			}
			return super.update(role);
		}catch(Exception ex){
			throw ex;
		}
	}

	@Override
	public boolean delete(Role roles) throws Exception{
		return super.delete(roles);
	}

	@Override
	public boolean deleteByPk(Long primaryKey) throws Exception{
		return super.deleteByPk(primaryKey);
	}


	@Override
	public Role findbyName(String rolename){
		return roleDao.findbyName(rolename);
	}

}
