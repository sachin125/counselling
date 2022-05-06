package com.inn.counselling.rest.impl;

import java.util.ArrayList;
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
import com.inn.counselling.model.CollegeStudent;
import com.inn.counselling.model.Student;
import com.inn.counselling.rest.generic.AbstractCXFRestService;
import com.inn.counselling.security.authentication.CustomerInfo;
import com.inn.counselling.service.ICollegeStudentService;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.utils.QueryObject;
import com.inn.counselling.wrapper.CollegeStudentWrapper;


@Path("/CollegeStudent")
@Produces("application/json")
@Consumes("application/json")
@Service("CollegeStudentRestImpl")
public class CollegeStudentRestImpl extends AbstractCXFRestService<Long, CollegeStudent> {
	
	private static final  Logger logger=LoggerFactory.getLogger(CollegeStudentRestImpl.class);

	public CollegeStudentRestImpl() {
		super(CollegeStudent.class);
	}

	@Autowired
	private ICollegeStudentService collegeStudentService;

	@Context
	private SearchContext context;

	@Override
	public IGenericService<Long, CollegeStudent> getService() {
		return collegeStudentService;
	}
	@Override
	public SearchContext getSearchContext() {
		return context;
	}

	public List<CollegeStudent> findAll()throws Exception{
		return collegeStudentService.findAll();
	}

	public CollegeStudent findByPk(@QueryParam("id") Long id)throws Exception{
		return collegeStudentService.findByPk(id);
	}

	@GET
	public List<CollegeStudent> search(@QueryParam("") CollegeStudent collegeStudent) throws Exception{
		return collegeStudentService.search(collegeStudent);
	}
	

	public List<Student> search(SearchContext ctx, Long maxLimit,
			Long minLimit, String orderby, String orderType)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("search")
	@Produces("application/json")
	public List<CollegeStudent> search(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,@QueryParam("orderBy") String orderBy)throws Exception{
		return collegeStudentService.search(context,upperLimit,lowerLimit,orderBy);
	}
	
	@GET
	@Path("searchCollege")
	@Produces("application/json")
	public List<College> searchCollege(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,@QueryParam("orderBy") String orderBy)throws Exception{
		List<College> list = new ArrayList();
		try {
			List<CollegeStudent> csList = collegeStudentService.search(context,upperLimit,lowerLimit,orderBy);
			for(CollegeStudent cs: csList) {
				list.add(cs.getCollege());
			}
		}catch(Exception e) {
			logger.error("Error inside @class ColegeSTudentRestImpl @method searchCollege @cause {} ",e);
		}
		return list;
	}
	
	@GET
	@Path("searchStudent")
	@Produces("application/json")
	public List<Student> searchStudent(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,@QueryParam("orderBy") String orderBy)throws Exception{
		List<Student> list = new ArrayList();
		try {
			List<CollegeStudent> csList = collegeStudentService.search(context,upperLimit,lowerLimit,orderBy);
			for(CollegeStudent cs: csList) {
				list.add(cs.getStudent());
			}
		}catch(Exception e) {
			logger.error("Error inside @class ColegeSTudentRestImpl @method searchCollege @cause {} ",e);
		}
		return list;
	}
	
	@GET
	@Path("searchStudent/collegeId")
	public List<CollegeStudent> findByCollegeId(@PathParam("collegeId")Long collegeId) {
		return collegeStudentService.findByCollegeId(collegeId);
	}
	
//	@POST
//	@Path("createCollegeStudentList")
//	public boolean createCollegeStudentList(List<CollegeStudentWrapper> collegeStudentWrapper) throws Exception{
//		Long UserId = CustomerInfo.getUserInContext().getId();
//		boolean value = collegeStudentService.createCollegeStudentList(UserId,collegeStudentWrapper);
//		return value;
//	}
	
	@GET
	@Path("createCollegeStudent/{id}")
	public CollegeStudent createCollegeStudent(@PathParam("id")Long collegeId) throws Exception{
		try {
			Long UserId = CustomerInfo.getUserInContext().getId();
			CollegeStudent collegeStudent = collegeStudentService.createCollegeStudent(UserId,collegeId);
			return collegeStudent;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	@GET
	@Path("removeCollegeStudent/{id}")
	public boolean  removeCollegeStudent(@PathParam("id")Long collegeId) throws Exception{
		try {
			Long UserId = CustomerInfo.getUserInContext().getId();
			return collegeStudentService.removeCollegeStudent(UserId,collegeId);
		} catch (Exception e) {
			throw new RestException(e);
		}
	}

	@GET
	@Path("approve/{id}")
	public CollegeStudent approve(@PathParam("id")Long collegeStudentId) throws Exception{
		try {
			CollegeStudent collegeStudent = collegeStudentService.approve(collegeStudentId);
			return collegeStudent;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	@GET
	@Path("reject/{id}")
	public CollegeStudent reject(@PathParam("id")Long collegeStudentId) throws Exception{
		try {
			CollegeStudent collegeStudent = collegeStudentService.reject(collegeStudentId);
			return collegeStudent;
		} catch (Exception e) {
			throw new RestException(e);
		}
	}
	
	@Override
	@POST
	@Path("create")
	public CollegeStudent create(@Valid CollegeStudent collegeStudent) throws Exception{
		CollegeStudent newUserStudent=collegeStudentService.create(collegeStudent);
		return newUserStudent;
	}

	@Override
	@POST
	@Path("update")
	public CollegeStudent update(@Valid CollegeStudent collegeStudent) throws Exception{
		CollegeStudent newUserStudent=collegeStudentService.update(collegeStudent);
		return newUserStudent;
	}

	@Override
	@Path("delete")
	public boolean delete(@Valid CollegeStudent collegeStudent) throws Exception{
		return collegeStudentService.delete(collegeStudent);
	}

	@POST
	@Override
	@Path("delete/{id}")
	public boolean deleteByPk(@PathParam("id") Long primaryKey) throws Exception{
		CollegeStudent collegeStudent=collegeStudentService.findByPk(primaryKey);
		return collegeStudentService.deleteByPk(primaryKey);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean contains(CollegeStudent anEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public List<CollegeStudent> find(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public long searchCount(SearchContext ctx) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
