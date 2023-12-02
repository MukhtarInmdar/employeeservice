package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ApplicantCheckListVerificationPojo {

	 
	 @Column(name = "APPLICANTCHECKLISTVERFIID")
	 private Long  applicantCheckListVerfiId;  
	 @Column(name = "APPLICANT_ID")
	 private Long  applicationId;
	 @Column(name = "CHECK_LIST_DEPT_MAPPING_ID")
	 private Long  checkListDeptMappingId;
	 @Column(name = "STATUS_ID")
	private Long statusId;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookId;
	
	public Long getApplicantCheckListVerfiId() {
		return applicantCheckListVerfiId;
	}
	public void setApplicantCheckListVerfiId(Long applicantCheckListVerfiId) {
		this.applicantCheckListVerfiId = applicantCheckListVerfiId;
	}
	public Long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}
	public Long getCheckListDeptMappingId() {
		return checkListDeptMappingId;
	}
	public void setCheckListDeptMappingId(Long checkListDeptMappingId) {
		this.checkListDeptMappingId = checkListDeptMappingId;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Long getFlatBookId() {
		return flatBookId;
	}
	public void setFlatBookId(Long flatBookId) {
		this.flatBookId = flatBookId;
	}
	
	
	
}
