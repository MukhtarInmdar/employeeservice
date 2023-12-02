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
public class FinBookingFormAccountPaymentDetailsPojo {
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "TRANSACTION_RECEIPT_NO") private String transactionReceiptNo;
	@Column(name = "AMOUNT") private Double transactionAmount;
	@Column(name = "TRANSACTION_DATE") private Timestamp transactionDate;
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "FIN_TRANSACTION_MODE_ID") private Long transactionModeId;
	@Column(name = "FIN_TRANSACTION_TYPE_ID") private Long transactionTypeId;
	@Column(name = "REFUND_AMOUNT") private Double refundAmount;
	@Column(name = "TOTAL_REFUND_AMOUNT") private Double totalRefundAmount;

	@Column(name = "FIN_BOK_FRM_ACC_PMT_ID") private Long bookingFormAccountPaymentId;
	@Column(name = "FIN_TRN_SET_OFF_APPR_ID") private Long transactionSetOffApprovalId;
	@Column(name = "FIN_TRN_SET_OFF_ENT_ID")   private Long transactionSetOffEntryId;
	@Column(name = "FIN_BOK_FRM_ACC_PMT_DTLS_ID") private Long bookingFormAccountPaymentDetailsId;
	@Column(name = "TRANSACTION_CLOSED_DATE") private Timestamp transactionClosedDate;
	
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "FIN_TRANSACTION_CHEQUE_ID") private Long transactionChequeId;
	@Column(name = "FIN_TRANSACTION_NO") private String finTransactionNo;
	@Column(name = "CHEQUE_NO") private Long chequeNumber;
	@Column(name = "CHEQUE_DATE") private Timestamp chequeDate;
	
	@Column(name = "FIN_TRANSFER_MODE_ID") private Long transferModeId;
	
	@Column(name = "TRANSACTION_MODE") private String transactionModeName;
	@Column(name = "TRANSACTION_TYPE_NAME") private String transactionTypeName;
	@Column(name = "TRANSFER_MODE") private String transferMode;
	
	@Column(name = "TRANSACTION_FOR") private String transactionFor;
	@Column(name = "TRANSACTION_FOR_ID") private String transactionForId;
	
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "IS_RECORD_UPLOADED") private Long isRecordUploaded;
	@Column(name = "EXCEL_RECORD_NO") private String excelRecordNo;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
