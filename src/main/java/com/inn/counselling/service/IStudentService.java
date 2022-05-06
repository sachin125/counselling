package com.inn.counselling.service;

import java.util.List;

import com.inn.counselling.model.Student;
import com.inn.counselling.service.generic.IGenericService;


public interface IStudentService extends IGenericService<Long, Student> {

	Student findByUserId(long userId) throws Exception;

}
