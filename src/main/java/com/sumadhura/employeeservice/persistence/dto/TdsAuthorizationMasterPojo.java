package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TdsAuthorizationMasterPojo {

	@Column(name="TDS_AUTHORIZATION_ID")
	private Long tdsAuthorizationId;
	@Column(name="NAME")
	private String tdsAuthorizationName;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="OPTION_TYPE")
	private String tdsAuthorizationType;
	
	public Long getTdsAuthorizationId() {
		return tdsAuthorizationId;
	}
	public void setTdsAuthorizationId(Long tdsAuthorizationId) {
		this.tdsAuthorizationId = tdsAuthorizationId;
	}
	public String getTdsAuthorizationName() {
		return tdsAuthorizationName;
	}
	public void setTdsAuthorizationName(String tdsAuthorizationName) {
		this.tdsAuthorizationName = tdsAuthorizationName;
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
	public String getTdsAuthorizationType() {
		return tdsAuthorizationType;
	}
	public void setTdsAuthorizationType(String tdsAuthorizationType) {
		this.tdsAuthorizationType = tdsAuthorizationType;
	}	
	
}
