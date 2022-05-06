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
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.counselling.model.Address;
import com.inn.counselling.rest.generic.AbstractCXFRestService;
import com.inn.counselling.service.IAddressService;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.utils.QueryObject;


@Path("/Address")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service("AddressRestImpl")
public class AddressRestImpl extends AbstractCXFRestService<Long, Address> {
	
	private static final  Logger logger=LoggerFactory.getLogger(AddressRestImpl.class);

	public AddressRestImpl() {
		super(Address.class);
	}

	@Autowired
	private IAddressService addressService;

	@Context
	private SearchContext context;

	@Override
	public IGenericService<Long, Address> getService() {
		return addressService;
	}
	@Override
	public SearchContext getSearchContext() {
		return context;
	}

	public List<Address> findAll()throws Exception{
		return addressService.findAll();
	}

	public Address findByPk(@QueryParam("id") Long id)throws Exception{
		return addressService.findByPk(id);
	}

	@GET
	public List<Address> search(@QueryParam("") Address address) throws Exception{
		return addressService.search(address);
	}
	
	@GET
	@Path("search")
	@Produces("application/json")
	public List<Address> search(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,@QueryParam("orderBy") String orderBy)throws Exception{
		return addressService.search(context,upperLimit,lowerLimit,orderBy);
	}
	
	@Override
	@POST
	@Path("create")
	public Address create(@Valid Address address) throws Exception{
		Address newAddress=addressService.create(address);
		return newAddress;
	}

	@Override
	@POST
	@Path("update")
	public Address update(@Valid Address address) throws Exception{
		Address newAddress=addressService.update(address);
		return newAddress;
	}

	@Override
	@Path("delete")
	public boolean delete(@Valid Address address) throws Exception{
		return addressService.delete(address);
	}

	@POST
	@Override
	@Path("delete/{id}")
	public boolean deleteByPk(@PathParam("id") Long primaryKey) throws Exception{
		Address address=addressService.findByPk(primaryKey);
		return addressService.deleteByPk(primaryKey);
	}

	@Override
	public long count(QueryObject queryObject) throws Exception {
		return 0l;
	}
	@Override
	public boolean contains(Address anEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<Address> find(QueryObject queryObject) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public long searchCount(SearchContext ctx) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
