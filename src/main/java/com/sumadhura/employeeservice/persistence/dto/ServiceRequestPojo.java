/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * ServiceRequestDTO class provides Employee Ticketing specific properties.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 06:50PM
 */

@Entity
public class ServiceRequestPojo {
	
	@Column(name="SER_REQ_ID")
	private Long serReqId;
	
	@Column(name="DEPT_ID")
	private Long DeptId;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="ASSIGMENT_BY")
	private Long assignmentBy;
	
	@Column(name="ASSIGMENT_TO")
	private Long assignmentTo;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="FLAT_ID")
	private Long flatId;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	/**
	 * @return the serReqId
	 */
	public Long getSerReqId() {
		return serReqId;
	}
	/**
	 * @param serReqId the serReqId to set
	 */
	public void setSerReqId(Long serReqId) {
		this.serReqId = serReqId;
	}
	/**
	 * @return the deptId
	 */
	public Long getDeptId() {
		return DeptId;
	}
	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(Long deptId) {
		DeptId = deptId;
	}
	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the assignmentBy
	 */
	public Long getAssignmentBy() {
		return assignmentBy;
	}
	/**
	 * @param assignmentBy the assignmentBy to set
	 */
	public void setAssignmentBy(Long assignmentBy) {
		this.assignmentBy = assignmentBy;
	}
	/**
	 * @return the assignmentTo
	 */
	public Long getAssignmentTo() {
		return assignmentTo;
	}
	/**
	 * @param assignmentTo the assignmentTo to set
	 */
	public void setAssignmentTo(Long assignmentTo) {
		this.assignmentTo = assignmentTo;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the flatId
	 */
	public Long getFlatId() {
		return flatId;
	}
	/**
	 * @param flatId the flatId to set
	 */
	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}
	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the modifiedDate
	 */
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceRequestDTO [serReqId=" + serReqId + ", DeptId=" + DeptId + ", createdBy=" + createdBy
				+ ", assignmentBy=" + assignmentBy + ", assignmentTo=" + assignmentTo + ", title=" + title
				+ ", description=" + description + ", flatId=" + flatId + ", statusId=" + statusId + ", createdDate="
				+ createdDate + ", modifiedDate=" + modifiedDate + "]";
	}
	
	
}
