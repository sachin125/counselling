package com.inn.counselling.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.dao.IPermissionDao;
import com.inn.counselling.model.Permission;
import com.inn.counselling.service.IPermissionService;
import com.inn.counselling.service.generic.AbstractService;

@Service
@Transactional
public class PermissionServiceImpl extends AbstractService<Long, Permission> implements IPermissionService {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(PermissionServiceImpl.class);
	
	private IPermissionDao permissionDao;
	
	@Autowired
	public void setDao(IPermissionDao dao) {
		super.setDao(dao);
		permissionDao = dao;
	}

	

	@Override
	public List<Permission> search(Permission permission) throws Exception{
		return super.search(permission);
	}

	@Override
	public Permission findByPk(Long  primaryKey) throws Exception{
		return super.findByPk(primaryKey);
	}

	@Override
	public List<Permission> findAll() throws Exception{
		return super.findAll();
	}

	@Override
	public Permission create(Permission permission) throws Exception{
		return super.create(permission);
	}

	@Override
	public Permission update(Permission permission) throws Exception{
		return super.update(permission);
	}

	@Override
	public boolean delete(Permission permission) throws Exception{
		return super.delete(permission);
	}

	@Override
	public boolean deleteByPk(Long primaryKey)throws Exception {
		return super.deleteByPk(primaryKey);
	}

}
