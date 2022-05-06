package com.inn.counselling.dao;

import java.util.List;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.model.CollegeStudent;

public interface ICollegeStudentDao extends IGenericDao<Long, CollegeStudent> {

	List<CollegeStudent> findByStudentId(long studentId);

	List<CollegeStudent> findByCollegeId(long collegeId);

	CollegeStudent findByStudentIdAndCollegeId(long studentId, long collegeId);

	boolean deleteByStudentIdAndCollegeId(long studentId, long collegeId);

	void createCollegeStudent(List<CollegeStudent> collegeStudentList) throws Exception;



}
