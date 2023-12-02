package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionEntryResponse {
	
	private Long transactionEntryId;
	private Long transactionModeId;
	private Long transactionTypeId;
	private Long siteId;
	private String siteName;
	private Long bookingFormId;
	private String salesforceBookingId;
	private String salesforceTransactionId;
	 
	private Long type;
	private Double transactionAmount;
	private Double payableAmount;
	private String chequeOrReferenceNo;
	private Timestamp chequeOrOnlineDate;
	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;
	private Timestamp chequeHandoverDate;
	private String chequeBounceComment;
	private Timestamp chequeBounceDate;
	private String bankName;
	private Long finBankId;
	private Timestamp transactionDate;
	private Timestamp transactionReceiveDate;
	private Long transactionFor;
	private Timestamp transactionClosedDate;
	private String lastApprovalby;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private String customerName;
	private Long statusId;
	private String transactionModeName;
	private String finTransactionNo;
	private String transactionTypeName;
	private String transactionStatusName;
	private Long transactionStatusId;
	// private String transactionPendingDeptId;
	// private String transactionPendingDeptName;
	private String pendingByLevel;
	private String departmentId;
	private String departmentName;
	private Timestamp fromDate;
	private Timestamp toDate;
	private String operationType;
	private Long flatId;
	private String flatNo;
	private String transactionPaymentSetOff;

	private Long siteAccountId;
	private String siteBankAccountNumber;
	private Long finSiteProjAccountMapId;
	private String location;
	private String docName;
	//private String multipleLocation;
	private List<Map<String,String>> multipleLocations;
	private List<Map<String,String>> transactionReceipts;
	private String receiptStage;
	private String sourceOfFunds;
	private List<Long> fbStatusId;
	private String flatBookingStatusName;
	private Timestamp lastApprovedDate;
}
