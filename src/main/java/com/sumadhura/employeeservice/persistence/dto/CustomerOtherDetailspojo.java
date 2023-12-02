package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerOtherDetailspojo {

	@Column(name="ID")
	private Long id;
	@Column(name="PURPOUSE_OF_PURCHASE")
	private String purposeofPurchase;
	@Column(name="CURRENT_RESIDENTIAL_STATUS")
	private String currentResidentialStatus;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	@Column(name="EXISTED_FLAT_BOOK_ID")
	private Long existedFlatBookId;
	
	public Long getExistedFlatBookId() {
		return existedFlatBookId;
	}
	public void setExistedFlatBookId(Long existedFlatBookId) {
		this.existedFlatBookId = existedFlatBookId;
	}
	public Long getFlatBookId() {
		return flatBookId;
	}
	public void setFlatBookId(Long flatBookId) {
		this.flatBookId = flatBookId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPurposeofPurchase() {
		return purposeofPurchase;
	}
	public void setPurposeofPurchase(String purposeofPurchase) {
		this.purposeofPurchase = purposeofPurchase;
	}
	public String getCurrentResidentialStatus() {
		return currentResidentialStatus;
	}
	public void setCurrentResidentialStatus(String currentResidentialStatus) {
		this.currentResidentialStatus = currentResidentialStatus;
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
