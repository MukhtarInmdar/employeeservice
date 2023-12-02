package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionTypeResponse {
	private Long transactionTypeId;
	private String name;
	private String description;
	private Long createdBy;
	private Timestamp createdDate;
	private Long statusId;
}
