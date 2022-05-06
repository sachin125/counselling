package com.inn.counselling.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.counselling.model.Permission;
import com.inn.counselling.rest.generic.AbstractCXFRestService;
import com.inn.counselling.service.IPermissionService;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.utils.AdvanceSearchResult;
import com.inn.counselling.utils.QueryObject;

@Path("/Permissions")
@Produces("application/json")
@Consumes("application/json")
@Service("PermissionRestImpl")
public class PermissionRestImpl extends AbstractCXFRestService<Long, Permission> {

	public PermissionRestImpl() {
		super(Permission.class);
	}

	@Autowired
	private IPermissionService permissionService;
	
	
	@Override
	@Path("create")
	@POST
	public Permission create(Permission permission) throws Exception{
		return permissionService.create(permission);
	}

	@Override
	@Path("update")
	@POST
	public Permission update(Permission permission) throws Exception{
		return permissionService.update(permission);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception{
		return permissionService.count(queryObject);
	}

	@Override
	@POST
	@Path("delete")
	public boolean delete(Permission permission) throws Exception{
		return permissionService.delete(permission);
	}

	@Override
	@POST
	@Path("delete")
	public boolean deleteByPk(Long permissionPk) throws Exception{
		return permissionService.deleteByPk(permissionPk);
	}

	@Override
	public boolean contains(Permission permission) throws Exception{
		return permissionService.contains(permission);
	}

	@Override
	public List<Permission> findAll() throws Exception{
		return permissionService.findAll();
	}

	@Override
	public List<Permission> find(QueryObject queryObject) throws Exception{
		return permissionService.find(queryObject);
	}

	@Override
	public AdvanceSearchResult<Permission> advanceSearch(QueryObject queryObject) throws Exception{
		return permissionService.advanceSearch(queryObject);
	}

	@GET
	public List<Permission> search(SearchContext ctx, Integer maxLimit,Integer minLimit, String orderby) throws Exception{
		return permissionService.search(ctx, maxLimit, minLimit, orderby);
	}

	@Override
	public Permission findByPk(Long  permissionPk) throws Exception {
		return permissionService.findByPk(permissionPk);
	}

	public long searchCount(SearchContext ctx) throws Exception {
		return 0;
	}

	@Override
	public List<Permission> search(Permission entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGenericService<Long, Permission> getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
