package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionChequeResponse {
	private Long transactionChequeId;
	private Long transactionEntryId;
	private Long chequeNumber;
	private Timestamp chequeDate;
	private Double chequeAmount;
	private Timestamp chequeBounceDate;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
}
