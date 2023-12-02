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
public class FinTransactionEntryDetailsPojo {
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "TRANSACTION_RECEIPT_NO") private String transactionReceiptNo;
	@Column(name = "AMOUNT") private Double transactionAmount;
	@Column(name = "TRANSACTION_DATE") private Timestamp transactionDate;
	@Column(name = "CHEQUE_NO") private String chequeNumber;
	@Column(name = "CHEQUE_DATE") private Timestamp chequeDate;

	@Column(name = "FIN_TRANSACTION_MODE_ID") private Long transactionModeId;
	@Column(name = "FIN_TRANSACTION_TYPE_ID") private Long transactionTypeId;
	@Column(name = "FIN_TRANSACTION_CHEQUE_ID") private Long transactionChequeId;
	@Column(name = "CHEQUE_HANDOVER_DATE") private Timestamp chequeHandoverDate;
	@Column(name = "CHEQUE_BOUNCE_DATE") private Timestamp chequeBounceDate;
	@Column(name = "TRANSACTION_CLOSED_DATE") private Timestamp transactionClosedDate;
	
	@Column(name = "FIN_TRANSFER_MODE_ID") private Long transferModeId;
	@Column(name = "DUE_DATE") private Timestamp mileStoneDueDate;
	@Column(name = "TRANSACTION_MODE") private String transactionModeName;
	@Column(name = "TRANSACTION_TYPE_NAME") private String transactionTypeName;
	@Column(name = "TRANSFER_MODE") private String transferMode;//online money transfer mode
	@Column(name = "CHEQUE_OR_REFERENCE_NO") private String chequeOrReferenceNo;
	@Column(name = "ACCOUNT_NUMBER") private String bankAccountNumber;
	@Column(name = "TRN_CREATION_DATE") private Timestamp trnCreationDate;
	@Column(name = "CREATED_BY")private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	@Column(name = "RECEIVED_DATE") private Timestamp transactionReceiveDate;
	@Column(name = "COMMENTS")private String comments;
	@Column(name = "FIN_TRANSACTION_NO")private String finTransactionNo;
	@Column(name = "FIN_BANK_ID")private Long finBankId;
	@Column(name = "BANK_NAME")private String bankName;
	
	@Column(name = "FIN_PROJ_ACC_ID") private Long siteAccountId;
	@Column(name = "ACCOUNT_NO") private String siteBankAccountNumber;
	
	@Column(name = "PAYABLE_AMOUNT") private Double payableAmount;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "FIN_TRANSACTION_ONLINE_ID") private Long finTransactionOnlineId;
	@Column(name = "REFERENCE_NO") private String referenceNo;
	
	
	@Column(name = "LOCATION") private String location;
	@Column(name = "DOC_NAME") private String docName;
	@Column(name = "TRANSACTION_FOR") private String transactionFor;
	@Column(name = "TRANSACTION_FOR_ID") private String transactionForId;

	@Column(name = "TRANSACTION_STATUS_NAME")private String transactionStatusName;
	@Column(name = "TRANSACTION_STATUS_ID") private Long transactionStatusId;
	
	@Column(name = "PAYMENT_DATE") private Timestamp paymentDate;
	@Column(name = "METADATA_NAME") private String setOffTypeName;
	
	@Column(name = "STATEMENT_REFUND_AMOUNT") private Double statementRefundAmount;
	@Column(name = "ACTUAL_PAID_AMOUNT") private Double actualPaidAmount;
	
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "FIN_BOK_ACC_INVOICE_NO")private String finBokAccInvoiceNo;
	@Column(name = "FIN_TRN_SET_OFF_ENT_ID") private Long transactionSetOffEntryId;
	@Column(name = "FIN_TRN_SET_OFF_ID") private Long transactionSetOffId;
	@Column(name = "SET_OFF_TYPE") private Long setOffType;
	@Column(name = "SET_OFF_AMOUNT") private Double setOffAmount;
	@Column(name = "FIN_TRN_SET_OFF_ACC_MAP_ID") private Long finTransactionSetOffAccMapId;	
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "REFUND_AMOUNT") private Double refundAmount;
	@Column(name = "TOTAL_REFUND_AMOUNT") private Double totalRefundAmount;
	@Column(name = "INTEREST_SELECTION_TYPE") private Long interestSelectionType;
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "SITE_NAME") private String siteName;

	@Column(name = "CLEARANCE_DATE") private Timestamp chequeClearanceDate;
	@Column(name = "CHEQUE_DEPOSITED_DATE") private Timestamp chequeDepositedDate;
	
	@Column(name = "CHEQUE_BOUNCE_REASON_ID") private Long chequeBounceReasonId;
	@Column(name = "CHEQUE_BOUNCE_REASON_VALUE") private String chequeBounceReasonValue;
	@Column(name = "CHEQUE_BOUNCE_COMMENT") private String chequeBounceComment;
	@Column(name="MILESTONE_NAME") private String milestoneName;
	@Column(name = "FIN_BOK_FRM_ACC_SMT_ID") private Long finBookingFormAccountsStatementId;	
	@Column(name = "SOURCE_OF_FUNDS") private String sourceOfFunds;
	@Column(name = "IS_RECORD_UPLOADED") private Long isRecordUploaded;
	@Column(name = "EXCEL_RECORD_NO") private String excelRecordNo;
}
