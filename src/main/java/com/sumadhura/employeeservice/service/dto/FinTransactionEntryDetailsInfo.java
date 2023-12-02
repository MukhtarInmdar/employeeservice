package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionEntryDetailsInfo implements Cloneable {
	private Long transactionChequeId;
	private Long transactionEntryId;
	private Long transactionSetOffEntryId;
	private String transactionReceiptNo;
	private String chequeNumber;
	private Timestamp chequeDate;
	private String chequeOrReferenceNo;
	private String condition;
	private Double transactionAmount;
	private String strTransactionAmount;
	private String transactionAmountInWords;

	private String strTransactionAmountExcludeGST;
	private String transactionAmountExcludeGSTInWords;

	private String strTransactionAmountGST;
	private String transactionAmountGSTInWords;

	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	// private Timestamp receivedDate;
	private Timestamp transactionReceiveDate;
	private Timestamp actualTransactionDate;
	private Timestamp actualTransactionReceiveDate;
	private String comments;
	private String finTransactionNo;
	private Double payableAmount;
	private Long finBankId;
	private String bankName;

	private Long siteAccountId;
	private String siteBankAccountNumber;
	private Long siteId;
	private String siteName;

	private Long bookingFormId;
	private Long finTransactionOnlineId;
	private String referenceNo;
	private Timestamp transactionDate;
	private String transferMode;
	private String location;
	private String docName;
	private String transactionFor;
	// private String transactionMode;
	private Timestamp paymentDate;

	private Long transactionModeId;
	private Long transactionTypeId;
	private String transactionStatusName;
	private Long transactionStatusId;
	private String transactionModeName;
	private String transactionTypeName;
	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;
	private Long chequeBounceReasonId;
	private String chequeBounceReasonValue;
	private List<Long> actualFlatIds;
	private Long actualBookingFormId;
	private String sourceOfFunds;
	private Timestamp chequeHandoverDate;
	private Timestamp chequeBounceDate;
	private String chequeBounceComment;
	private Timestamp transactionClosedDate;
	private Long isRecordUploaded;
	private String excelRecordNo;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
