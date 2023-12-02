package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CoApplicantCheckListVerificationPojo {


	@Column(name = "APPLICANTCHECKLISTVERFIID")
	private Long  checkListVerfiId;  
	@Column(name = "APPLICANT_ID")
	private Long  coApplicantId;
	@Column(name = "CHECK_LIST_DEPT_MAPPING_ID")
	private Long  checkListDeptMappingId;
	@Column(name = "STATUS_ID")
	private Long is_verified;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookId;
	public Long getCheckListVerfiId() {
		return checkListVerfiId;
	}
	public void setCheckListVerfiId(Long checkListVerfiId) {
		this.checkListVerfiId = checkListVerfiId;
	}
	public Long getCoApplicantId() {
		return coApplicantId;
	}
	public void setCoApplicantId(Long coApplicantId) {
		this.coApplicantId = coApplicantId;
	}
	public Long getCheckListDeptMappingId() {
		return checkListDeptMappingId;
	}
	public void setCheckListDeptMappingId(Long checkListDeptMappingId) {
		this.checkListDeptMappingId = checkListDeptMappingId;
	}
	public Long getIs_verified() {
		return is_verified;
	}
	public void setIs_verified(Long is_verified) {
		this.is_verified = is_verified;
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
