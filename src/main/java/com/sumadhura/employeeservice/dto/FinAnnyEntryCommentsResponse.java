package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinAnnyEntryCommentsResponse {
	private Long finAnnyEntryCommentsId;
	private Long typeId;
	private Long type;
	private String comments;
	private String empName;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private String metadataName;
}
