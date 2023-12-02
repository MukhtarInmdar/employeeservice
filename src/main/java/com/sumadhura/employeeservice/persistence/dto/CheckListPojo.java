package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CheckListPojo {

	@Column(name="CHECKLIST_ID")
	private Long checkListId;
	@Column(name="NAME")
	private String checkListName;
	@Column(name="DESCRIPTION")
	private String description;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	public Long getCheckListId() {
		return checkListId;
	}
	public void setCheckListId(Long checkListId) {
		this.checkListId = checkListId;
	}
	public String getCheckListName() {
		return checkListName;
	}
	public void setCheckListName(String checkListName) {
		this.checkListName = checkListName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
