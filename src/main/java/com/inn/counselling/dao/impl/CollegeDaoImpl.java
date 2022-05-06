package com.inn.counselling.dao.impl;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.ICollegeDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.College;

@Dao
public class CollegeDaoImpl extends HibernateGenericDao<Long, College> implements ICollegeDao {

	private static final Logger LOGGER=LoggerFactory.getLogger(CollegeDaoImpl.class);

	public CollegeDaoImpl() {
		super(College.class);
	}

	@Override
	public College create(@Valid College family)throws Exception {
		return super.create(family);
	}


	@Override
	public College update(@Valid College family) throws Exception {
		return super.update(family);
	}

	@Override
	public boolean delete(College family) throws Exception {
		return super.delete(family);
	}

	@Override
	public boolean deleteByPk(Long familyPk) throws Exception{
		return super.deleteByPk(familyPk);
	}

	@Override
	public List<College> findAll() throws Exception{
		return super.findAll();
	}

	@Override
	public College findByPk(Long  familyPk) throws Exception{
		return super.findByPk(familyPk);
	}

}
