package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FinCustomerLedgerResponse {
	//private Long serialNumber;
	//private Long id;
	private Timestamp date;
	private String description;
	private String referenceNumber;
	private String creditAmount;
	private String debitAmount;
	private String balanceAmount;
	private String recordType;
	private String prefix;
	private String suffix;
	private String url;
}
