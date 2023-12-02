package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class EmployeeFinancialTransactionRequest extends Result implements Serializable,Cloneable {
	private static final long serialVersionUID = 7474738479064456264L;
	private Long empId;
	private String empIdFromUrl;
	private String employeeName;
	private Long pendingTrnByEmpId;
	private Long portNumber;
	private String departmentId;
	private String departmentName;
	private Long siteId;
	private String siteName;
	private String deviceToken;
	private String condition;
	private String buttonType;
	private String requestUrl;
	private String actionUrl;
	private Long transactionTypeId;
	private Long transactionModeId;
	private Long transferModeId;
	private Long transactionForId;

	private String transactionTypeName;
	private String transactionModeName;
	private String transferModeName;
	private String transactionFor;

	private String chequeNumber;
	private String referenceNo;
	private Double receivedAmount;
	private Timestamp transactionDate;
	private Timestamp transactionReceiveDate;
	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;

	private String bankName;
	private Long bankId;
	private Long bankAccountId;
	private String bankAccountNumber;
	private List<String> bankAccountNumbers;

	// private Double chequeAmount;
	private Double transactionAmount;
	private Double payableAmount;
	private String chequeBounceReasonValue;
	private String chequeBounceComment;
	private Timestamp chequeBounceDate;
	private String optionalButtonType;
	private List<Long> siteIds;
	private List<Long> blockIds;
	private List<Long> floorIds;
	private List<Long> flatIds;
	private List<String> flatNos;
	private List<String> flatNo;
	//flatNames and flatNo these two fields will hold same data only names changed
	private List<Long> bookingFormIds;

	private Long bookingFormId;
	private String paymentSetOff;
	private String isShowGstInPDF;

//	private String setOffPrincialAmount;
//	private String setOffTDSAmount;
//	private String setOffmodificationCharges;
//	private String setOffinterestAmount;
//	private String setOffLegalCharges;
//
//	private String setOffModificationInvoiceNo;
//	private String setOffLegalChargesInvoiceNo; 
	private Long finBookingFormModiCostId;
	private Long finsetOffAppLevelId;
	private String comment;
	private List<Map<String, String>> comments;
	private List<EmployeeFinTranPaymentSetOffRequest> paymentSetOffDetails;
	private List<EmployeeFinModiChargesRequest> modiCostRequests;// not in use
	private List<EmployeeFinModiChargesRequest> modiCostDtlsRequests;

	private Long siteAccountId;
	private String siteBankAccountNumber;

	// fields for CRM payment cheque
	private Long paymentFor;
	private String cancelReason;

	private List<FileInfo> fileInfos;
	private List<FileInfo> deleteFileInfos;
	private Long transactionEntryId;
	private Long prevTransactionEntryId;
	private Long transactionSetOffEntryId;
	private Long transactionSetOffApprovalLevelId;
	private Long transactionChequeId;
	private Long anonymousEntryId;
	private String transactionReceiptNo;
	private String transactionNo;
	private String typeName;
	private List<FinBookingFormLegalCostRequest> finBookingFormLegalCostList;
	private Long finTaxMappingId;
	private Long percentageId;
	private Double percentageValue;
	private Timestamp fromDate;
	private Timestamp toDate;
	
	private Timestamp trnCreatedFromDate;
	private Timestamp trnCreatedTotoDate;
	
	private Timestamp receivedFromDate;
	private Timestamp receivedToDate;
	
	private Timestamp clearanceFromDate;
	private Timestamp clearanceToDate;
	private Long flatSaleOwnerId;
	
	private String searchBySetOffType;
	
	private String operationType;
	private List<Long> actualFlatIds;
	private List<String> actualFlatNos;
	private Long actualBookingFormId;
	private Timestamp actualTransactionReceiveDate;
	private Timestamp actualTransactionDate;
	private String sourceOfFunds;
	private String isRecievedDateORSetOffChanged;
	private Timestamp chequeHandoverDate;
	private EmployeeFinancialTransactionRequest actualTransactionRequest;
	
	/* Malladi Changes */
	private Long invoiceType;
	private Long deptId;
	private Long roleId;
	
	private List<Long> fbStatusId;
	private List<Long> stateId;
	
	
	private List<Long> searchBySetOffTypes;
	private List<Long> siteAccountIds;
	
	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}

}
