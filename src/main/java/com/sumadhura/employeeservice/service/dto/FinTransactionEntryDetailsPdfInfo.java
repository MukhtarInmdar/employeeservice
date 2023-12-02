package com.sumadhura.employeeservice.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class FinTransactionEntryDetailsPdfInfo {
	private Long srNo;
	private String chequeOrOnlineReceivedDate;
	private String chequeOrOnlineReferenceNo;
	private String chequeOrOnlineTransactionDate;
	
	//private String transactionDate;
	private String principalAmount;
	private String cgstAmount;
	private String sgstAmount;
	private String totalAmount;
	private String status;
}
