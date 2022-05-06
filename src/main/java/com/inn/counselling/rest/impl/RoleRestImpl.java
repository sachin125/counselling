package com.inn.counselling.rest.impl;

import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.counselling.model.Permission;
import com.inn.counselling.model.Role;
import com.inn.counselling.model.Users;
import com.inn.counselling.rest.generic.AbstractCXFRestService;
import com.inn.counselling.security.authentication.CustomerInfo;
import com.inn.counselling.service.IPermissionService;
import com.inn.counselling.service.IRoleService;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.utils.QueryObject;


@Path("/Roles")
@Produces("application/json")
@Consumes("application/json")
@Service("RoleRestImpl")
public class RoleRestImpl extends AbstractCXFRestService<Long, Role> {
	
	private static final  Logger logger=LoggerFactory.getLogger(RoleRestImpl.class);
	
	public RoleRestImpl() {
		super(Role.class);
	}
	
	@Autowired
	private IRoleService service;
	
	@Autowired
	private IPermissionService permissionService;

	@Context
	private SearchContext context;

	@Override
	public IGenericService<Long, Role> getService() {
		return service;
	}
	@Override
	public SearchContext getSearchContext() {
		return context;
	}

	public List<Role> findAll()throws Exception{
		return service.findAll();
	}

	public Role findByPk(@QueryParam("id") Long id)throws Exception{
		return service.findByPk(id);
	}

	@GET
	public List<Role> search(@QueryParam("") Role roles)throws Exception{
		return service.search(roles);
	}

	@GET
	@Path("search")
	public List<Role> search(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,@QueryParam("orderBy") String orderBy)throws Exception{
		return service.search(context,upperLimit,lowerLimit,orderBy);
	}

	@Override
	@POST
	@Path("create")
	public Role create(@Valid Role roles) throws Exception{
		logger.info("Creating roles by roles :");
		try{
			HashSet<Permission> set = new HashSet<Permission>();
			set.addAll(roles.getPermissions());
			set.add(permissionService.findByPk(300l));
			roles.setPermissions(set);
			Users username =CustomerInfo.getUserInContext();
			Role createrole=service.create(roles);
			return createrole;
		}catch(Exception ex)
		{
			logger.error("Error  occurred  @class"   + this.getClass().getName()  , ex);
			throw ex;
		}
	}

	@Override
	@POST
	@Path("update")
	public Role update(@Valid Role roles) throws Exception{
		return service.update(roles);
	}

	@Override
	@Path("delete")
	public boolean delete(@Valid Role roles) throws Exception{
		return service.delete(roles);
	}

	@POST
	@Override
	@Path("/{id}")
	public boolean deleteByPk(@PathParam("id") Long primaryKey) throws Exception{
		return service.deleteByPk(primaryKey);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean contains(Role anEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	public List<Role> search(SearchContext ctx, Long maxLimit,
			Long minLimit, String orderby, String orderType)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Role> find(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public long searchCount(SearchContext ctx) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
