package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerProjectPreferedLocationMasterPojo {

	@Column(name="LOCATION_ID")
	private Long locationId;
	
	@Column(name="LOCATION_NAME")
	private String locationName;
	
	@Column(name="LOCATION_STATUS")
	private String locationStatus;
	
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

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(String locationStatus) {
		this.locationStatus = locationStatus;
	}

	


	

	
	
	
	
	
	
}
