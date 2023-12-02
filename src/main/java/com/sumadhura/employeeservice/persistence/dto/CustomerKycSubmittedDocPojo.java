package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerKycSubmittedDocPojo {

	@Column(name="SUBMITTED_DOC_ID")
	private Long submittedDocId;
	@Column(name="CUST_BOOK_INFO_ID")
	private Long custBookInfoId;
	@Column(name="EMP_ID")
	private Long empId;
	@Column(name="DOCUMENT_ID")
	private Long documentId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	
	
	
	


	public Long getFlatBookId() {
		return flatBookId;
	}
	public void setFlatBookId(Long flatBookId) {
		this.flatBookId = flatBookId;
	}
	public Long getSubmittedDocId() {
		return submittedDocId;
	}
	public void setSubmittedDocId(Long submittedDocId) {
		this.submittedDocId = submittedDocId;
	}
	public Long getCustBookInfoId() {
		return custBookInfoId;
	}
	public void setCustBookInfoId(Long custBookInfoId) {
		this.custBookInfoId = custBookInfoId;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
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
	
	
	
	
	
	
	
	
	
	
}
