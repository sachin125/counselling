package com.inn.counselling.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonView;
import com.inn.counselling.model.Users;
import com.inn.counselling.rest.generic.AbstractCXFRestService;
import com.inn.counselling.security.authentication.CustomerInfo;
import com.inn.counselling.service.IUserService;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.utils.AdvanceSearchResult;
import com.inn.counselling.utils.QueryObject;
import com.inn.counselling.utils.view.View.UserBasicView;
import com.inn.counselling.wrapper.LoginUserWrapper;
import com.inn.counselling.wrapper.UserInContextWrapper;
import com.inn.counselling.wrapper.service.impl.LoginUserWrapperServiceImpl;

@Path("/User")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service
public class UserRestImpl extends AbstractCXFRestService<Long, Users> {

	public UserRestImpl() {
		super(Users.class);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestImpl.class);
	
	@Autowired
	private IUserService userService;
	
	@Context
	private SearchContext searchContext;
	
	@Override
	@Path("create")
	@POST
	public Users create(Users user) throws Exception{
		return userService.create(user);
	}

	@Override
	@Path("update")
	@POST
	public Users update(Users user) throws Exception{
		return userService.update(user);
	}
	
	@Override
	public Users findByPk(Long  userPk) throws Exception{
		return userService.findByPk(userPk);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception{
		return userService.count(queryObject);
	}

	@Override
	@POST
	@Path("delete")
	public boolean delete(Users user) throws Exception{
		return userService.delete(user);
	}

	@Override
	@POST
	@Path("delete")
	public boolean deleteByPk(Long userPk) throws Exception{
		return userService.deleteByPk(userPk);
	}

	@Override
	public boolean contains(Users user) throws Exception{
		return userService.contains(user);
	}

	@Override
	public List<Users> findAll() throws Exception{
		return userService.findAll();
	}

	@Override
	public List<Users> find(QueryObject queryObject) throws Exception{
		return userService.find(queryObject);
	}

	@Override
	public AdvanceSearchResult<Users> advanceSearch(QueryObject queryObject) throws Exception{
		return userService.advanceSearch(queryObject);
	}
	
	@Override
	@GET
	@Path("/search")
	@JsonView({UserBasicView.class})
	public List<Users> search(@QueryParam("ulimit") Integer ulimit,@QueryParam("llimit") Integer llimit,
			@QueryParam("orderBy")  String orderby) throws Exception{
		return userService.search(searchContext, ulimit, llimit, orderby);
	}
	
	@Override
	@GET
	@Path("/search")
	@JsonView({UserBasicView.class})
	public List<Users> search(Users entity) throws Exception {
		return userService.search(entity);
	}

	
	@GET
	@Path("/count")
	public long searchCount(SearchContext ctx) throws Exception{
		return userService.searchCount(ctx);
	}

	@Override
	public IGenericService<Long, Users> getService() {
		return userService;
	}

	@Override
	public SearchContext getSearchContext() {
		return searchContext;
	}
	
	@GET
	@Path("userincontext")
	public LoginUserWrapper getUserInContext() throws Exception{
		LoginUserWrapper loginUserWrapper = new LoginUserWrapper();
		UserInContextWrapper userInContextWrapper = new UserInContextWrapper();
		
		Users currentUser = CustomerInfo.getUserInContext();
//		List<UserFeatureWrapper> userfeatureWrapperList = userFeatureService.findFeatureWhichIsPendingByUserId(currentUser.getId());
		
		LoginUserWrapperServiceImpl.setUser(userInContextWrapper, currentUser);

		loginUserWrapper.setUserInContextWrapper(userInContextWrapper);
	//	loginUserWrapper.setUserFeatureWrapperList(userfeatureWrapperList);
		return loginUserWrapper;
	}
	
	
}
