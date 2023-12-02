package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionModeInfo {
	
	private Long transactionModeId;
	private String name;
	private String description;
	private Long createdBy;
	private Timestamp createdDate;
	private Long statusId;
}
