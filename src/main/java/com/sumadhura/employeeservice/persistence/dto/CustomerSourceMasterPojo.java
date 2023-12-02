package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerSourceMasterPojo {

	@Column(name="SOURCE_ID")
	private Long sourceId;
	
	@Column(name="SOURCE_NAME")
	private String sourceName;
	
	@Column(name="SOURCE_DESC")
	private String sourceDesc;
	
	@Column(name="SOURCE_TYPE")
	private String sourceType;
	
	@Column(name="SOURCE_STATUS")
	private String sourceStatus;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	
	public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getSourceDesc() {
		return sourceDesc;
	}
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}

	
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getSourceStatus() {
		return sourceStatus;
	}
	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
	
	
}
