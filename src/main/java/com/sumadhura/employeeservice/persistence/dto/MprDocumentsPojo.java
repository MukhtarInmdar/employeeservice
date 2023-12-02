package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Entity;

import java.sql.Timestamp;

import javax.persistence.Column;

import lombok.Data;

@Entity
@Data
public class MprDocumentsPojo {
	@Column(name="MPR_DOC_ID")
	private Long mprDocId;
	
	@Column(name="MPR_ID")
	private Long mprId;
	
	@Column(name="LOCATION_TYPE")
	private String locationType;
	
	@Column(name="DOCUMENT_LOCATION")
	private String documentLocation;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private Long modifiedBy;
	
	@Column(name="STATUS_ID")
	private Long statusId;
}
