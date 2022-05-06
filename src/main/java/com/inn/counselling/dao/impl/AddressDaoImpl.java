package com.inn.counselling.dao.impl;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.IAddressDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.Address;

@Dao
public class AddressDaoImpl extends HibernateGenericDao<Long, Address> implements IAddressDao {

	private static final Logger LOGGER=LoggerFactory.getLogger(AddressDaoImpl.class);

	public AddressDaoImpl() {
		super(Address.class);
	}

	@Override
	public Address create(@Valid Address address)throws Exception {
		return super.create(address);
	}


	@Override
	public Address update(@Valid Address address) throws Exception {
		return super.update(address);
	}

	@Override
	public boolean delete(Address address) throws Exception {
		return super.delete(address);
	}

	@Override
	public boolean deleteByPk(Long addressPk) throws Exception{
		return super.deleteByPk(addressPk);
	}

	@Override
	public List<Address> findAll() throws Exception{
		return super.findAll();
	}

	@Override
	public Address findByPk(Long  addressPk) throws Exception{
		return super.findByPk(addressPk);
	}

}
