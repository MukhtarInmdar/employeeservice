package com.sumadhura.employeeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FinTransactionEntryDocResponse {
	private Long transactionEntryDocId;
	private Long transactionEntryId;
	private String documentLocation;
	private String documentName;
	private Long documentType;
	private String filePath;
}
