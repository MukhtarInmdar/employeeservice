package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class FinAnnyApproveStatResponse {
	private Long finAnnyApproveStatId;
	private Long anonymousEntryId;
	private Long actionType;
	private String actionTypeName;
	private Long empId;
	private String empName;
	private String comment;
	private Timestamp createdDate;
}
