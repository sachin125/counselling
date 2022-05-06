package com.inn.counselling.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.ICollegeStudentDao;
import com.inn.counselling.dao.annotation.Dao;
import com.inn.counselling.dao.generic.impl.HibernateGenericDao;
import com.inn.counselling.model.CollegeStudent;
import com.inn.counselling.model.Student;

@Dao
public class CollegeStudentDaoImpl extends HibernateGenericDao<Long, CollegeStudent> implements ICollegeStudentDao {

	private static final Logger LOGGER=LoggerFactory.getLogger(CollegeStudentDaoImpl.class);

	public CollegeStudentDaoImpl() {
		super(CollegeStudent.class);
	}

	@Override
	public CollegeStudent create(@Valid CollegeStudent collegeStudent)throws Exception {
		return super.create(collegeStudent);
	}


	@Override
	public CollegeStudent update(@Valid CollegeStudent collegeStudent) throws Exception {
		return super.update(collegeStudent);
	}

	@Override
	public boolean delete(CollegeStudent collegeStudent) throws Exception {
		return super.delete(collegeStudent);
	}

	@Override
	public boolean deleteByPk(Long collegeStudentPk) throws Exception{
		return super.deleteByPk(collegeStudentPk);
	}

	@Override
	public List<CollegeStudent> findAll() throws Exception{
		return super.findAll();
	}

	@Override
	public CollegeStudent findByPk(Long  collegeStudentPk) throws Exception{
		return super.findByPk(collegeStudentPk);
	}

	@Override
	public List<CollegeStudent> findByStudentId(long studentId) {
		LOGGER.info("Inside @class CollegeStudentDaoImpl @method findByStudentId @param userId: "+studentId);
		try {
			Query query = getEntityManager().createNamedQuery("CollegeStudent.findByStudentId")
					.setParameter("studentId", studentId);
			return query.getResultList();
		} catch (NoResultException e) {
			LOGGER.error("NoResultException Inside @class CollegeStudentDaoImpl @method findByStudentId @param userId: {} @cause {} ",studentId,e);
			return null;
		}catch (Exception e) {
			LOGGER.error("Error Inside @class CollegeStudentDaoImpl @method findByStudentId @param userId: {} @cause {} ",studentId,e);
			throw e;
		}
	}
	
	@Override
	public List<CollegeStudent> findByCollegeId(long collegeId) {
		LOGGER.info("Inside @class CollegeStudentDaoImpl @method findByCollegeId @param findByCollegeId: "+collegeId);
		try {
			Query query = getEntityManager().createNamedQuery("CollegeStudent.findByCollegeId")
					.setParameter("collegeId", collegeId);
			return query.getResultList();
		}catch (NoResultException e) {
			LOGGER.error("No Result FOund Inside @class CollegeStudentDaoImpl @method findByCollegeId @param collegeId: {}",collegeId);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class CollegeStudentDaoImpl @method findByCollegeId @param userId: {} @cause {} ",collegeId,e);
			throw e;
		}
	}
	
	@Override
	public CollegeStudent findByStudentIdAndCollegeId(long studentId,long collegeId) {
		LOGGER.info("Inside @class CollegeStudentDaoImpl @method findByStudentIdAndCollegeId @param studentId and collegeId: {} , {}",studentId,collegeId);
		try {
			Query query = getEntityManager().createNamedQuery("CollegeStudent.findByStudentIdAndCollegeId")
					.setParameter("studentId", studentId)
					.setParameter("collegeId", collegeId).setMaxResults(1);
			return (CollegeStudent) query.getSingleResult();
		} catch (NoResultException e) {
			LOGGER.error("No Result FOund Inside @class CollegeStudentDaoImpl @method findByStudentIdAndCollegeId @param collegeId: {}, {}",studentId,collegeId);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class CollegeStudentDaoImpl @method findByStudentIdAndCollegeId @param userId: {} @cause {} ",studentId,collegeId,e);
			throw e;
		}
	}
	
	@Override
	public boolean deleteByStudentIdAndCollegeId(long studentId,long collegeId) {
		LOGGER.info("Inside @class CollegeStudentDaoImpl @method deleteByStudentIdAndCollegeId @param studentId and collegeId: {} , {} ",studentId,collegeId);
		try {
			Query query = getEntityManager().createNamedQuery("CollegeStudent.deleteByStudentIdAndCollegeId")
					.setParameter("studentId", studentId)
					.setParameter("collegeId", collegeId);
			 query.executeUpdate();
			 return true;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class CollegeStudentDaoImpl @method deleteByStudentIdAndCollegeId @param userId: {} @cause {} ",studentId,e);
			throw e;
		}
	}
	
	@Override
	public void createCollegeStudent(List<CollegeStudent> collegeStudentList) throws Exception {
		LOGGER.info("Inside @class CollegeStudentDaoImpl @method createCollegeStudent @param ");
		try {
			for(CollegeStudent collegeStudent:collegeStudentList) {
				create(collegeStudent);
			}
		} catch (Exception e) {
			LOGGER.error("Error Inside @class CollegeStudentDaoImpl @method createCollegeStudent @param {} ",e);
			throw e;
		}
	}

	/*@Override
	public Education findHigherEducationUserId(long userId) {
		LOGGER.info("Inside @class CollegeStudentDaoImpl @method findHigherEducationUserId @param userId: {} {}",userId);
		try {
			Education myEducation = null;
			List<Education> educationList = null;
			educationList = this.findEducationByUserIdAndEducationLevel(userId, EducationLevel.Post_graducation);
			if(educationList.size()==0) {
				educationList = this.findEducationByUserIdAndEducationLevel(userId, EducationLevel.Graducation);
				if(educationList.size()==0) {
					educationList = this.findEducationByUserIdAndEducationLevel(userId, EducationLevel.Highschool);
					if(educationList.size()==0) {
						educationList = this.findEducationByUserIdAndEducationLevel(userId, EducationLevel.None);	
					}
				}
			}
			if(educationList.size()==1) {
				return educationList.get(0);
			}else if(educationList.size()>1) {
				Education education1 = educationList.get(0);
				for(int i=1;i<educationList.size();i++) {
					Education education2 = educationList.get(i);
					if(education1.getToDate().after(education2.getToDate())) {
						myEducation = education1;
					}else {
						myEducation = education2;
					}
				}
			}
			return myEducation;
		} catch (Exception e) {
			LOGGER.error("Error Inside @class CollegeStudentDaoImpl @method findHigherEducationUserId @param userId: {} {}",userId);
			throw e;
		}
	}*/
}
