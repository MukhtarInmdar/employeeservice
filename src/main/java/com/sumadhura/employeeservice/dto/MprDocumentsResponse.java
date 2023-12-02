package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MprDocumentsResponse {
	private Long mprDocId;
	private Long mprId;
	private String locationType;
	private String documentLocation;
	private Timestamp createdDate;
	private Long createdBy;
	private Timestamp modifiedDate;
	private Long modifiedBy;
	private Long statusId;
}
