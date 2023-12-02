package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinTransactionApprStatResponse {
	private Long finTrnApprStatId;
	private Long finTrnSetOffEntId;
	private Long actionType;
	private Long empId;
	private String comments;
	private Timestamp createdDate;
	private String empName;
}
