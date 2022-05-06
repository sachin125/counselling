package com.inn.counselling.dao;

import java.util.List;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.model.Role;

public interface IRoleDao extends IGenericDao<Long, Role>{

	Role findbyName(String rolename);

}
