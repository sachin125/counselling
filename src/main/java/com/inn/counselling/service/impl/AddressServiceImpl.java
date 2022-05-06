package com.inn.counselling.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.dao.IAddressDao;
import com.inn.counselling.model.Address;
import com.inn.counselling.service.IAddressService;
import com.inn.counselling.service.generic.AbstractService;

@Service
@Transactional
public class AddressServiceImpl extends AbstractService<Long, Address> implements IAddressService {

	private static final Logger LOGGER=LoggerFactory.getLogger(AddressServiceImpl.class);

	private  IAddressDao addressDao;

	@Autowired
	public void setDao(IAddressDao dao) {
		super.setDao(dao);
		addressDao = dao;
	}

	@Override
	public List<Address> search(Address address) throws Exception{
		return super.search(address);
	}

	@Override
	public Address findByPk(Long  primaryKey)throws Exception{
		return (addressDao.findByPk(primaryKey));
	}

	@Override
	public List<Address> findAll() throws Exception{
		return addressDao.findAll();
	}

	@Override
	public Address create(@Valid Address address) throws Exception{
		return addressDao.create(address);
	}

	@Override
	public Address update(@Valid Address address)throws Exception {
		return addressDao.update(address);
	}

	@Override
	public boolean delete(Address address) throws Exception {
		return super.delete(address);
	}

	@Override
	public boolean deleteByPk(Long primaryKey) throws Exception  {
		return super.deleteByPk(primaryKey);
	}

}
