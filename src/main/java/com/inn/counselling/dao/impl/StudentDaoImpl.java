package com.inn.counselling.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.IStudentDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.Student;
import com.inn.counselling.model.Users;

@Dao
public class StudentDaoImpl extends HibernateGenericDao<Long, Student> implements IStudentDao {

	private static final Logger LOGGER=LoggerFactory.getLogger(StudentDaoImpl.class);

	public StudentDaoImpl() {
		super(Student.class);
	}

	@Override
	public Student create(Student student)throws Exception {
		return super.create(student);
	}


	@Override
	public Student update(Student student) throws Exception {
		return super.update(student);
	}

	@Override
	public boolean delete(Student student) throws Exception {
		return super.delete(student);
	}

	@Override
	public boolean deleteByPk(Long studentPk) throws Exception{
		return super.deleteByPk(studentPk);
	}

	@Override
	public List<Student> findAll() throws Exception{
		return super.findAll();
	}

	@Override
	public Student findByPk(Long  studentPk) throws Exception{
		return super.findByPk(studentPk);
	}

	@Override
	public Student findByUserId(long userId) throws Exception{
		LOGGER.info("Inside @class EducationDaoImpl @method findByUserId @param userId: "+userId);
		try {
			Query query = getEntityManager().createNamedQuery("Student.findByUserId")
					.setParameter("userId", userId);
			return (Student) query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("Error Inside @class StudentDaoImpl @method findByUserId @param userId: "+userId);
			throw e;
		}
	}

	@Override
	public Student createnStudent(Users newUsers) throws Exception {
		Student student = new Student();
		student.setFname(newUsers.getFirstname());
		student.setLname(newUsers.getLastname());
		student.setContactno(newUsers.getContactno());
		student.setDob(newUsers.getDob());
		student.setGender(newUsers.getGender());
		student.setUser(newUsers);
		return create(student);
	}

}
