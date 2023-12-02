package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Aniket Chavan
 * @since 22-09-2020
 * @Time 03:00PM
 */
@ToString
@Getter
@Setter
public class EmployeeFinancialMultipleTRNInfo {
	private Long empId;
	private String employeeName;
	private Long portNumber;
	private Long siteId;
	private String siteName;

	private String buttonType;
	private String actionUrl;
	private String requestUrl;
	private String finTransactionNo;
	private String transactionReceiptNo;
	private Long transactionEntryId;
	private Long transactionSetOffEntryId;

	private Long transactionTypeId;
	private Long transactionModeId;
	private Long transferModeId;
	private Long transactionForId;

	private String transactionTypeName;
	private String transactionModeName;
	private String transferModeName;
	private String transactionFor;

	private List<Long> flatIds;
	private Long bookingFormId;

	private String chequeNumber;
	private String referenceNo;
	private Double receivedAmount;
	private Double transactionAmount;
	private Timestamp transactionDate;
	private Timestamp transactionReceiveDate;
	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;
	private String comment;
	private String chequeBounceReasonValue;
	private String optionalButtonType;
	private Timestamp chequeHandoverDate;
	private Timestamp chequeBounceDate;
	private String chequeBounceComment;
	private CustomerPropertyDetailsInfo customerPropertyDetailsInfo;
	private List<EmployeeFinancialMultipleTRNInfo> financialTRNRequests;

}
