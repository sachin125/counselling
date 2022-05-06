package com.inn.counselling.dao;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.model.Student;
import com.inn.counselling.model.Users;

public interface IStudentDao extends IGenericDao<Long, Student> {

	Student findByUserId(long userId)  throws Exception;

	Student createnStudent(Users newUsers) throws Exception;

}
