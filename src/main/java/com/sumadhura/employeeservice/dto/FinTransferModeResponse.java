package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinTransferModeResponse {
	private Long transferModeId;
	private String name;
	private String description;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private Long statusId;
}

