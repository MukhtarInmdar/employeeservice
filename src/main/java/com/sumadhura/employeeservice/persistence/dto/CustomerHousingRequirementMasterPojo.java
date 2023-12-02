package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerHousingRequirementMasterPojo {

	@Column(name="HOUSING_ID")
	private Long housingId;
	
	@Column(name="HOUSING_TYPE")
	private String housingType;
	
	@Column(name="HOUSING_STATUS")
	private String housingStatus;
	
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

	public Long getHousingId() {
		return housingId;
	}

	public void setHousingId(Long housingId) {
		this.housingId = housingId;
	}

	public String getHousingType() {
		return housingType;
	}

	public void setHousingType(String housingType) {
		this.housingType = housingType;
	}

	public String getHousingStatus() {
		return housingStatus;
	}

	public void setHousingStatus(String housingStatus) {
		this.housingStatus = housingStatus;
	}

	
	
	
	
	
}
