package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerLeadSiteVisitPojo {

	@Column(name="LEAD_ID")
	private Long leadId;
	
	
	@Column(name="LEAD_CREATED_TO_SITE_SCHEDULED")
	private String leadCreatedToSiteScheduled;
		
	@Column(name="PROJECT_ID")
	private Long projectId;
	
	
	@Column(name="LEAD_CREATION_STATUS")
	private String leadCreationStatus;

	@Column(name="LEAD_SUB_STATUS_ID")
	private int leadSubStatusId;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="LEAD_SUB_STATUS_TYPE")
	private String leadSubStatusType;

	@Column(name="LEAD_SUB_STATUS")
	private String leadSubstatus;

		
	@Column(name="PROJECT_NAME")
	private String projectName;
	
	@Column(name="SALES_REPLY")
	private String salesReply;

	@Column(name="MEET_COMMENTS")
	private String meetComments;
	
	
	public Long getLeadId() {
		return leadId;
	}

	public void setLeadId(Long leadId) {
		this.leadId = leadId;
	}

		public String getLeadCreatedToSiteScheduled() {
		return leadCreatedToSiteScheduled;
	}

	public void setLeadCreatedToSiteScheduled(String leadCreatedToSiteScheduled) {
		this.leadCreatedToSiteScheduled = leadCreatedToSiteScheduled;
	}

	
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	
	
	public String getLeadCreationStatus() {
		return leadCreationStatus;
	}

	public void setLeadCreationStatus(String leadCreationStatus) {
		this.leadCreationStatus = leadCreationStatus;
	}

	public int getLeadSubStatusId() {
		return leadSubStatusId;
	}

	public void setLeadSubStatusId(int leadSubStatusId) {
		this.leadSubStatusId = leadSubStatusId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getLeadSubStatusType() {
		return leadSubStatusType;
	}

	public void setLeadSubStatusType(String leadSubStatusType) {
		this.leadSubStatusType = leadSubStatusType;
	}

	public String getLeadSubstatus() {
		return leadSubstatus;
	}

	public void setLeadSubstatus(String leadSubstatus) {
		this.leadSubstatus = leadSubstatus;
	}

	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSalesReply() {
		return salesReply;
	}

	public void setSalesReply(String salesReply) {
		this.salesReply = salesReply;
	}

	public String getMeetComments() {
		return meetComments;
	}

	public void setMeetComments(String meetComments) {
		this.meetComments = meetComments;
	}

		

	
}
