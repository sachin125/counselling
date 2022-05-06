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

import com.inn.counselling.dao.IUserDao;
import com.inn.counselling.exception.RestException;
import com.inn.counselling.model.Student;
import com.inn.counselling.model.Users;
import com.inn.counselling.rest.generic.AbstractCXFRestService;
import com.inn.counselling.service.IStudentService;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.utils.QueryObject;


@Path("/student")
@Produces("application/json")
@Consumes("application/json")
@Service("StudentRestImpl")
public class StudentRestImpl extends AbstractCXFRestService<Long, Student> {
	
	private static final  Logger logger=LoggerFactory.getLogger(StudentRestImpl.class);

	public StudentRestImpl() {
		super(Student.class);
	}

	@Autowired
	private IStudentService studentService;
	
	@Autowired
	private IUserDao userDao;

	@Context
	private SearchContext context;

	@Override
	public IGenericService<Long, Student> getService() {
		return studentService;
	}
	@Override
	public SearchContext getSearchContext() {
		return context;
	}

	public List<Student> findAll()throws Exception{
		return studentService.findAll();
	}

	public Student findByPk(@QueryParam("id") Long id)throws Exception{
		return studentService.findByPk(id);
	}
	
	@GET
	@Path("findByName")
	public Student findByName(@QueryParam("username") String username)throws RestException{
		try {
			Users user = userDao.findByUsername(username);
			if(user!=null) {
				return studentService.findByUserId(user.getId());	
			}else {
				throw new RestException("User not found for username: "+username);
			}			
		}catch(Exception e) {
			throw new RestException(e);
		}
	}

	@GET
	public List<Student> search(@QueryParam("") Student student) throws Exception{
		return studentService.search(student);
	}
	
	@GET
	@Path("search")
	@Produces("application/json")
	public List<Student> search(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,@QueryParam("orderBy") String orderBy)throws Exception{
		return studentService.search(context,upperLimit,lowerLimit,orderBy);
	}
	
	@Override
	@POST
	@Path("create")
	public Student create(@Valid Student student) throws Exception{
		Student newUserStudent=studentService.create(student);
		return newUserStudent;
	}

	@Override
	@POST
	@Path("update")
	public Student update(@Valid Student student) throws Exception{
		Student newUserStudent=studentService.update(student);
		return newUserStudent;
	}

	@Override
	@Path("delete")
	public boolean delete(@Valid Student student) throws Exception{
		return studentService.delete(student);
	}

	@POST
	@Override
	@Path("delete/{id}")
	public boolean deleteByPk(@PathParam("id") Long primaryKey) throws Exception{
		Student student=studentService.findByPk(primaryKey);
		return studentService.deleteByPk(primaryKey);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean contains(Student anEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public List<Student> search(SearchContext ctx, Long maxLimit,
			Long minLimit, String orderby, String orderType)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Student> find(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public long searchCount(SearchContext ctx) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
