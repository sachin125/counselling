package com.inn.counselling.service;

import java.util.List;

import com.inn.counselling.model.CollegeStudent;
import com.inn.counselling.service.generic.IGenericService;
import com.inn.counselling.wrapper.CollegeStudentWrapper;

public interface ICollegeStudentService extends IGenericService<Long, CollegeStudent> {

	CollegeStudent createCollegeStudent(Long userId, Long collegeId) throws Exception;

	boolean removeCollegeStudent(Long userId, Long collegeId) throws Exception;

	List<CollegeStudent> findByCollegeId(long collegeId);

	CollegeStudent approve(Long collegeStudentId) throws Exception;

	CollegeStudent reject(Long collegeStudentId) throws Exception;

}
