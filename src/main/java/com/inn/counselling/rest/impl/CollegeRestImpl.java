package com.inn.counselling.rest.impl;

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

import com.inn.counselling.exception.RestException;
import com.inn.counselling.model.College;
import com.inn.counselling.rest.generic.AbstractCXFRestService;
import com.inn.counselling.service.ICollegeService;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.utils.QueryObject;


@Path("/college")
@Produces("application/json")
@Consumes("application/json")
@Service("CollegeRestImpl")
public class CollegeRestImpl extends AbstractCXFRestService<Long, College> {
	
	private static final  Logger LOGGER=LoggerFactory.getLogger(CollegeRestImpl.class);

	public CollegeRestImpl() {
		super(College.class);
	}

	@Autowired
	private ICollegeService collegeService;

	@Context
	private SearchContext context;

	@Override
	public IGenericService<Long, College> getService() {
		return collegeService;
	}
	@Override
	public SearchContext getSearchContext() {
		return context;
	}

	public List<College> findAll()throws Exception{
		return collegeService.findAll();
	}

	@GET
	@Path("findByPk")
	public College findByPk(@QueryParam("id") Long id)throws Exception{
		return collegeService.findByPk(id);
	}
	
	@GET
	@Path("search")
	public List<College> search(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,@QueryParam("orderBy") String orderBy)throws Exception{
		try {
			LOGGER.info("Entry inside @class CollegerestImpl @method search @param lowerLimit {} ",lowerLimit);
			return collegeService.search(context,upperLimit,lowerLimit,orderBy);
			
		}catch(Exception e) {
			LOGGER.info("Error inside @class CollegerestImpl @method search @cause {} ",e);
			throw new RestException(e);
		}
	}
	
	public List<College> search(College college) throws Exception{
		return collegeService.search(college);
	}

	
	@Override
	@POST
	@Path("create")
	public College create(@Valid College college) throws Exception{
		College newUserCollege=collegeService.create(college);
		return newUserCollege;
	}

	@Override
	@POST
	@Path("update")
	public College update(@Valid College college) throws Exception{
		College newUserCollege=collegeService.update(college);
		return newUserCollege;
	}

	@Override
	@Path("delete")
	public boolean delete(@Valid College college) throws Exception{
		return collegeService.delete(college);
	}

	@POST
	@Override
	@Path("delete/{id}")
	public boolean deleteByPk(@PathParam("id") Long primaryKey) throws Exception{
		College college=collegeService.findByPk(primaryKey);
		return collegeService.deleteByPk(primaryKey);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean contains(College anEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public List<College> find(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
