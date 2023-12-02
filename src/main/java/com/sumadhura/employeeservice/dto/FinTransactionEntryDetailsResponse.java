package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionEntryDetailsResponse {
	private Long transactionChequeId;
	private Long transactionEntryId;
	private String chequeNumber;
	private Timestamp chequeDate;
	// private Double amount;
	private Double transactionAmount;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private Timestamp transactionReceiveDate;
	private String comments;
	private String finTransactionNo;
	private String transactionReceiptNo;
	private Double payableAmount;
	private Long finBankId;
	private String bankName;

	private Long siteAccountId;
	private String siteBankAccountNumber;

	private Long bookingFormId;
	private Long finTransactionOnlineId;
	private String referenceNo;
	private Timestamp transactionDate;

	private String location;
	private String docName;

	private String transactionForId;
	private Long transactionModeId;
	private Long transactionTypeId;
	private Long transferModeId;

	private String transactionFor;
	private String transactionModeName;
	private String transactionTypeName;
	private String transferMode;

	private String bankAccountNumber;

	private Timestamp paymentDate;
	private Long transactionSetOffEntryId;
	private Long siteId;
	private String siteName;

	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;
	private Timestamp chequeHandoverDate;
	private Long chequeBounceReasonId;
	private String chequeBounceReasonValue;
	private Boolean isThisFinalLevel;
	private String transactionStatusName;
	private Long transactionStatusId;
	private String sourceOfFunds;
	private String chequeBounceComment;
	private Timestamp chequeBounceDate;
	private Timestamp transactionClosedDate;

	private List<FinTransactionSetOffResponse> finTransactionSetOffResponseList;
	private List<FinTransactionCommentsResponse> finTransactionCommentsResponseList;
	private List<FinTransactionApprStatResponse> finTransactionApprStatResponseList;
	private List<FinTransactionChangedDtlsResponse> transactionChangedDetailsResponseList;
	private List<FinTransactionEntryDocResponse> transactionEntryDocResponsesList;
	private List<FinTransactionEntryDocResponse> transactionReceiptDocResponsesList;
	private List<FinancialProjectMileStoneResponse> financialProjectMileStoneResponseList;
	private Map<String,Object> customerAmountData;

}
