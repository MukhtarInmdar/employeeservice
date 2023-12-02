package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class FinTransactionEntryPojo {
	
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "EDIT_OR_SHIFT_USING_TRN_ENT_ID")private Long editOrShiftUsingTrnEntId;
	@Column(name = "PREV_FIN_TRANSACTION_ENTRY_ID") private Long prevTransactionEntryId;
	@Column(name = "FIN_TRANSACTION_MODE_ID") private Long transactionModeId;
	@Column(name = "FIN_TRANSACTION_TYPE_ID") private Long transactionTypeId;
	@Column(name = "FIN_SITE_PROJ_ACC_MAP_ID") private Long finSiteProjAccountMapId;
	//@Column(name = "FIN_TRANSACTION_NO") private Long fintTransactionNo;
	@Column(name = "FIN_TRANSACTION_NO") private String finTransactionNo;
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "SITE_NAME") private String siteName;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name="SALESFORCE_BOOKING_ID") private String salesforceBookingId;
	@Column(name="SALESFORCE_TRANSACTION_ID") private String salesforceTransactionId;
	@Column(name="FB_STATUS_NAME") private String flatBookingStatusName;
	
	@Column(name = "TYPE") private Long type;
	@Column(name = "AMOUNT") private Double transactionAmount;
	@Column(name = "PAYABLE_AMOUNT") private Double payableAmount;
	@Column(name = "CHEQUE_OR_REFERENCE_NO") private String chequeOrReferenceNo;
	@Column(name = "CHEQUE_OR_ONLINE_DATE") private Timestamp chequeOrOnlineDate;
	@Column(name = "CLEARANCE_DATE") private Timestamp chequeClearanceDate;
	@Column(name = "CHEQUE_DEPOSITED_DATE") private Timestamp chequeDepositedDate;
	@Column(name = "CHEQUE_HANDOVER_DATE") private Timestamp chequeHandoverDate;
	@Column(name = "CHEQUE_BOUNCE_COMMENT") private String chequeBounceComment;
	@Column(name = "CHEQUE_BOUNCE_DATE") private Timestamp chequeBounceDate;
	@Column(name = "BANK_NAME") private String bankName;
	@Column(name = "FIN_BANK_ID") private Long bankId;	
	@Column(name = "PAYMENT_DATE") private Timestamp transactionDate;
	@Column(name = "RECEIVED_DATE") private Timestamp transactionReceiveDate;
	@Column(name = "TRANSACTION_FOR") private Long transactionFor; 
	@Column(name = "TRANSACTION_RECEIPT_NO") private String transactionReceiptNo;
	@Column(name = "TRANSACTION_CLOSED_DATE") private Timestamp transactionClosedDate;
	
	@Column(name = "FIN_PROJ_ACC_ID") private Long siteAccountId;
	@Column(name = "ACCOUNT_NO") private String siteBankAccountNumber;
	
	@Column(name = "LOCATION") private String location;
	@Column(name = "DOC_NAME") private String docName;
	@Column(name = "MULTIPLE_LOCATION") private String multipleLocation;
	@Column(name = "TRANSACTION_RECEIPT") private String transactionReceipt;
	
	@Column(name = "LAST_APPR_BY") private String lastApprovalby; 	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	@Column(name = "CUSTOMER_NAME") private String customerName;
	
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "FLAT_BOOKING_STATUS_ID") private Long flatBookingStatusId;
	@Column(name = "TRANSACTION_MODE_NAME") private String transactionModeName;
	@Column(name = "TRANSACTION_TYPE_NAME") private String transactionTypeName;
	@Column(name = "TRANSACTION_STATUS_NAME")private String transactionStatusName;
	@Column(name = "TRANSACTION_STATUS_ID") private Long transactionStatusId;
	@Column(name = "PENDING_BY_LEVEL") private String pendingByLevel;
	@Column(name = "TRANSACTION_PENDING_DEPT_ID") private String departmentId;
	@Column(name = "TRANSACTION_PENDING_DEPT_NAME") private String departmentName;
	@Column(name = "FLAT_ID") private Long flatId; 
	@Column(name = "FLAT_NO") private String flatNo;
	@Column(name = "IS_RECORD_UPLOADED") private Long isRecordUploaded;
	@Column(name = "EXCEL_RECORD_NO") private String excelRecordNo;
	@Column(name = "TRANSACTION_PAYMENT_SET_OFF") private String transactionPaymentSetOff;
	@Column(name = "SOURCE_OF_FUNDS") private String sourceOfFunds;
	@Column(name = "TRN_SET_OFF_NAME") private String trnSetOffName;
	@Column(name = "TRN_SET_OFF_AMOUNT") private Double trnSetOffAmount;
	@Column(name = "LAST_APPROVED_DATE") private Timestamp lastApprovedDate;
	
	
}
