package com.inn.counselling.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.dao.ICollegeDao;
import com.inn.counselling.dao.ICollegeStudentDao;
import com.inn.counselling.dao.IStudentDao;
import com.inn.counselling.model.College;
import com.inn.counselling.model.CollegeStudent;
import com.inn.counselling.model.Student;
import com.inn.counselling.service.ICollegeStudentService;
import com.inn.counselling.service.generic.AbstractService;
import com.inn.counselling.wrapper.CollegeStudentWrapper;

@Service
@Transactional
public class CollegeStudentServiceImpl extends AbstractService<Long, CollegeStudent> implements ICollegeStudentService {

	private static final Logger LOGGER=LoggerFactory.getLogger(CollegeStudentServiceImpl.class);

	private  ICollegeStudentDao collegeStudentDao;

	@Autowired
	private  IStudentDao studentDao;

	@Autowired
	private  ICollegeDao collegeDao;

	@Autowired
	public void setDao(ICollegeStudentDao dao) {
		super.setDao(dao);
		collegeStudentDao = dao;
	}

	@Override
	public List<CollegeStudent> search(CollegeStudent collegeStudent) throws Exception{
		return super.search(collegeStudent);
	}

	@Override
	public CollegeStudent findByPk(Long  primaryKey)throws Exception{
		return (collegeStudentDao.findByPk(primaryKey));
	}

	@Override
	public List<CollegeStudent> findAll() throws Exception{
		return collegeStudentDao.findAll();
	}

	@Override
	public CollegeStudent create(@Valid CollegeStudent collegeStudent) throws Exception{
		return collegeStudentDao.create(collegeStudent);
	}

	@Override
	public CollegeStudent update(@Valid CollegeStudent collegeStudent)throws Exception {
		return collegeStudentDao.update(collegeStudent);
	}

	@Override
	public boolean delete(CollegeStudent collegeStudent) throws Exception {
		return super.delete(collegeStudent);
	}

	@Override
	public boolean deleteByPk(Long primaryKey) throws Exception  {
		return super.deleteByPk(primaryKey);
	}

	@Override
	public List<CollegeStudent> findByCollegeId(long collegeId) {
		return collegeStudentDao.findByCollegeId(collegeId);
	}

	//	@Override
	//	public boolean createCollegeStudentList(Long studentId, List<CollegeStudentWrapper> collegeStudentWrapper) throws Exception {
	//		CollegeStudent collegeStudentOld = collegeStudentDao.findByStudentId(studentId);
	//		List<CollegeStudent> collegeStudentList = new ArrayList<>();
	//		if(collegeStudentOld!=null) {
	//			collegeStudentDao.deleteByPk(collegeStudentOld.getId());
	//		}
	//		
	//		Student student = studentDao.findByPk(studentId);
	//		for(CollegeStudentWrapper csw:collegeStudentWrapper) {
	//			College college = collegeDao.findByPk(csw.getCollegeId());
	//			CollegeStudent collegeStudent = new CollegeStudent();
	//			collegeStudent.setStudent(student);
	//			collegeStudent.setCollege(college);
	//			collegeStudentList.add(collegeStudent);
	//		}
	//
	//		collegeStudentDao.createCollegeStudent(collegeStudentList);
	//		return true;
	//	}

	@Override
	public CollegeStudent createCollegeStudent(Long userId,Long collegeId) throws Exception {
		CollegeStudent collegeStudentOld = collegeStudentDao.findByStudentIdAndCollegeId(userId, collegeId);
		if(collegeStudentOld!=null) {
			return collegeStudentOld;
		}else {
			Student student = studentDao.findByPk(userId);
			College college = collegeDao.findByPk(collegeId);
			CollegeStudent collegeStudent = new CollegeStudent();
			collegeStudent.setStudent(student);
			collegeStudent.setCollege(college);
			collegeStudentDao.create(collegeStudent);
			return collegeStudent;
		}	
	}

	@Override
	public boolean removeCollegeStudent(Long userId, Long collegeId) throws Exception {
		CollegeStudent collegeStudent = collegeStudentDao.findByStudentIdAndCollegeId(userId, collegeId);
		return collegeStudentDao.delete(collegeStudent);
	}

	@Override
	public CollegeStudent approve(Long collegeStudentId) throws Exception {
		CollegeStudent collegeStudent = collegeStudentDao.findByPk(collegeStudentId);
		if(collegeStudent.isApproved()) {

		}else {
			College college = collegeDao.findByPk(collegeStudent.getCollege().getId());
			int totalseat = college.getTotalSeat();
			if(totalseat>college.getAllocated()) {
				college.setAllocated(college.getAllocated()+1);
				college.setFreeseat(college.getFreeseat()-1);				
			}else if(totalseat<college.getAllocated()) {
				throw new Exception("College seat full");			
			}
			collegeDao.update(college);
			collegeStudent.setRemark("Application has been approved");
			collegeStudent.setApproved(true);	
			collegeStudentDao.update(collegeStudent);
		}
		return collegeStudent;
	}

	@Override
	public CollegeStudent reject(Long collegeStudentId) throws Exception{
		CollegeStudent collegeStudent = collegeStudentDao.findByPk(collegeStudentId);
		College college = collegeDao.findByPk(collegeStudent.getCollege().getId());
		int totalseat = college.getTotalSeat();
		college.setAllocated(college.getAllocated()-1);
		college.setFreeseat(college.getFreeseat()+1);				
		collegeDao.update(college);
		collegeStudent.setRemark("Application has been reject");
		collegeStudent.setApproved(false);
		collegeStudentDao.update(collegeStudent);
		return collegeStudent;
	}

}
