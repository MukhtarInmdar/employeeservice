package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.ToString;


@Entity
@ToString
public class ChecklistDepartmentMappingPojo {
	
	@Column(name="CHECKLIST_DEPT_MAP_ID")
	private Long checklistDeptMapId;
	@Column(name="CHECKLIST_ID")
	private Long checkListId;
	@Column(name="DEPT_ID")
	private Long deptId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="CK_META_TYPE")
	private Long ckMetaType;
	
	
	
	public Long getCkMetaType() {
		return ckMetaType;
	}
	public void setCkMetaType(Long ckMetaType) {
		this.ckMetaType = ckMetaType;
	}
	public Long getChecklistDeptMapId() {
		return checklistDeptMapId;
	}
	public void setChecklistDeptMapId(Long checklistDeptMapId) {
		this.checklistDeptMapId = checklistDeptMapId;
	}
	public Long getCheckListId() {
		return checkListId;
	}
	public void setCheckListId(Long checkListId) {
		this.checkListId = checkListId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
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
