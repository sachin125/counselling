package com.inn.counselling.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CollegeStudentWrapper {
	
	private Long collegeId;

	private Long priorityNo;
	

	public Long getCollegeId() {
		return collegeId;
	}


	public void setCollegeId(Long collegeId) {
		this.collegeId = collegeId;
	}


	public Long getPriorityNo() {
		return priorityNo;
	}


	public void setPriorityNo(Long priorityNo) {
		this.priorityNo = priorityNo;
	}


	public CollegeStudentWrapper() {
		
	}
	
}
