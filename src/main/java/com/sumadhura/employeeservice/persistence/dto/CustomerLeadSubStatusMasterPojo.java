package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerLeadSubStatusMasterPojo {

	@Column(name="LEAD_SUB_STATUS_ID")
	private Long leadSubStatusId;
	
	@Column(name="LEAD_SUB_STATUS_TYPE")
	private String leadSubStatusType;
	
	@Column(name="LEAD_SUB_STATUS")
	private String leadSubstaus;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	

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

	public Long getLeadSubStatusId() {
		return leadSubStatusId;
	}

	public void setLeadSubStatusId(Long leadSubStatusId) {
		this.leadSubStatusId = leadSubStatusId;
	}

	public String getLeadSubStatusType() {
		return leadSubStatusType;
	}

	public void setLeadSubStatusType(String leadSubStatusType) {
		this.leadSubStatusType = leadSubStatusType;
	}

	public String getLeadSubstaus() {
		return leadSubstaus;
	}

	public void setLeadSubstaus(String leadSubstaus) {
		this.leadSubstaus = leadSubstaus;
	}
	
	
	
	
	
}
