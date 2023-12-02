package com.sumadhura.employeeservice.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Aniket Chavan
 * @since 07-10-2020
 * @time 05-30 PM
 */
@Setter
@Getter
@ToString
public class FinancialTransactionDetailsInfo {
	
	private Long siteId;
	private String siteName;
	private String transactionTypeName;
	private String transactionModeName;
	private String transferModeName;
	private String transactionReceiptNo;
	private String finTransactionNo;
	private String transactionAmount;
	private String transactionDate;
	private String chequeNumber;
	private String comment;
	private String waiverReason;
	private String customerName;
	private Long flatId;
	private String flatNo;
	private String chequeHandoverDate;
	private String chequeClearanceDate;
	private String bankName;
}
