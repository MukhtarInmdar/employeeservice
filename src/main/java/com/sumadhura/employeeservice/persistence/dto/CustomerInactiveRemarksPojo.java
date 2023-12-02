package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerInactiveRemarksPojo {

	@Column(name="inactive_remark_id")
	private Long inactiveRemarkId;
	
	@Column(name="inactive_remark_name")
	private String inactiveRemarkName;
	
	@Column(name="inactive_remark_status")
	private String inactiveRemarkStats;
	
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

	public Long getInactiveRemarkId() {
		return inactiveRemarkId;
	}

	public void setInactiveRemarkId(Long inactiveRemarkId) {
		this.inactiveRemarkId = inactiveRemarkId;
	}

	public String getInactiveRemarkName() {
		return inactiveRemarkName;
	}

	public void setInactiveRemarkName(String inactiveRemarkName) {
		this.inactiveRemarkName = inactiveRemarkName;
	}

	public String getInactiveRemarkStats() {
		return inactiveRemarkStats;
	}

	public void setInactiveRemarkStats(String inactiveRemarkStats) {
		this.inactiveRemarkStats = inactiveRemarkStats;
	}

	
	
	
	
	
}
