package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinTransactionChangedDtlsResponse {
	private Long transactionChangedDtlsId;
	private Long transactionApproveStatId;
	private String actualValue;
	private String changedValue;
	private Long empId;
	private String remarks;
	private Long columnId;
	private String metaDataName;
	private Long actionType;
	private Timestamp createdDate; 
}
