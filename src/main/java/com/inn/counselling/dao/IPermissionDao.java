package com.inn.counselling.dao;

import java.util.List;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.model.Permission;

public interface IPermissionDao extends IGenericDao<Long, Permission>{

	List<Permission> findByRoleId(Integer roleid);

}
