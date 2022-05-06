package com.inn.counselling.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.dao.IAddressDao;
import com.inn.counselling.dao.IStudentDao;
import com.inn.counselling.model.Address;
import com.inn.counselling.model.Student;
import com.inn.counselling.service.IStudentService;
import com.inn.counselling.service.generic.AbstractService;

@Service
@Transactional
public class StudentServiceImpl extends AbstractService<Long, Student> implements IStudentService {

	private static final Logger LOGGER=LoggerFactory.getLogger(StudentServiceImpl.class);

	private  IStudentDao studentDao;

	@Autowired
	private  IAddressDao addressDao;
	
	@Autowired
	public void setDao(IStudentDao dao) {
		super.setDao(dao);
		studentDao = dao;
	}

	@Override
	public List<Student> search(Student student) throws Exception{
		return super.search(student);
	}

	@Override
	public Student findByPk(Long  primaryKey)throws Exception{
		return (studentDao.findByPk(primaryKey));
	}

	@Override
	public List<Student> findAll() throws Exception{
		return studentDao.findAll();
	}

	@Override
	public Student create(@Valid Student student) throws Exception{
		return studentDao.create(student);
	}

	@Override
	public Student update(@Valid Student student)throws Exception {
		Address address = student.getAddress();
		if(address.getId()==null) {
			address = addressDao.create(address);	
		}else {
			address = addressDao.update(address);
		}
		student.setAddress(address);
		return studentDao.update(student);
	}

	@Override
	public boolean delete(Student student) throws Exception {
		return super.delete(student);
	}

	@Override
	public boolean deleteByPk(Long primaryKey) throws Exception  {
		return super.deleteByPk(primaryKey);
	}

	@Override
	public Student findByUserId(long userId) throws Exception {
		return studentDao.findByUserId(userId);
	}
}
