package com.inn.counselling.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.dao.ICollegeDao;
import com.inn.counselling.model.College;
import com.inn.counselling.service.ICollegeService;
import com.inn.counselling.service.generic.AbstractService;

@Service
@Transactional
public class CollegeServiceImpl extends AbstractService<Long, College> implements ICollegeService {

	private static final Logger LOGGER=LoggerFactory.getLogger(CollegeServiceImpl.class);

	private ICollegeDao collegeDao;

	@Autowired
	public void setDao(ICollegeDao dao) {
		super.setDao(dao);
		collegeDao = dao;
	}

	@Override
	public List<College> search(College college) throws Exception{
		return super.search(college);
	}

	@Override
	public College findByPk(Long  primaryKey)throws Exception{
		return (collegeDao.findByPk(primaryKey));
	}

	@Override
	public List<College> findAll() throws Exception{
		return collegeDao.findAll();
	}

	@Override
	public College create(@Valid College family) throws Exception{
		return collegeDao.create(family);
	}

	@Override
	public College update(@Valid College family)throws Exception {
		return collegeDao.update(family);
	}

	@Override
	public boolean delete(College family) throws Exception {
		return super.delete(family);
	}

	@Override
	public boolean deleteByPk(Long primaryKey) throws Exception  {
		return super.deleteByPk(primaryKey);
	}

}
