package com.inn.counselling.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({ 
	@NamedQuery(name = "CollegeStudent.findByStudentId", query = "select uc from CollegeStudent uc where uc.student.id=:studentId"),
	@NamedQuery(name = "CollegeStudent.findByCollegeId", query = "select uc from CollegeStudent uc where uc.college.id=:collegeId"),
	@NamedQuery(name="CollegeStudent.findByStudentIdAndCollegeId",query = "select uc from CollegeStudent uc where uc.student.id=:studentId and uc.college.id=:collegeId"),
	@NamedQuery(name = "CollegeStudent.deleteByStudentIdAndCollegeId", query = "delete from CollegeStudent uc where uc.student.id=:studentId and uc.college.id=:collegeId"),
	
})

@XmlRootElement(name="CollegeStudent")
@Entity
@Table(name="college_student")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CollegeStudent implements Serializable{

	public CollegeStudent() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
		
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="college_id")
	private College college;
	
	@Column(name="college_priority")
	private int collegePriority;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="student_id")
	private Student student;
	
	@Column(name="is_approved")
	private boolean isApproved=false;
	
	@Column(name="remark")
	private String remark;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getCollegePriority() {
		return collegePriority;
	}

	public void setCollegePriority(int collegePriority) {
		this.collegePriority = collegePriority;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
