package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinProjectAccountResponse;
import com.sumadhura.employeeservice.dto.FinTransactionEntryDetailsResponse;
import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprovalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.ModicationInvoiceAppRej;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author @NIKET CH@V@N
 * @since 11.01.2020
 * @time 06:50 PM
 * @description this class is for holding request data for financial module
 */
@ToString
@Getter
@Setter
public class EmployeeFinancialTransactionServiceInfo implements Cloneable {
	private Long employeeId;
	private String empIdFromUrl;
	private String employeeName;
	private Long pendingTrnByEmpId;
	private Long portNumber;
	private String departmentId; 
	private String departmentName; 
	private Long siteId;
	private String siteName;

	private Long transactionEntryId;
	private Long prevTransactionEntryId;
	private String prevTransactionReceiptNo;
	private Long transactionSetOffEntryId;
	private Long prevTransactionSetOffEntryId;
	private Long transactionSetOffApprovalLevelId;
	private Long transactionChequeId;
	private Long transactionSetOffApprovalId;
	private Long transactionApproveStatId;
	private Long bookingFormReceiptId;
	private Long anonymousEntryId;
	private Long editOrShiftUsingTrnEntId;
	private Timestamp transactionClosedDate;
//	private Long 

	private String condition;
	private String buttonType;
	private String requestUrl;
	private String actionUrl;
	private Long transactionTypeId;
	private Long transactionModeId;
	private Long transactionForId;
	private Long transferModeId;
	private Long statusId;
	private Long transactionStatusId;

	private String transactionTypeName;
	private String transactionModeName;
	private String transferModeName;

	private String chequeNumber;
	private String referenceNo;
	private Double receivedAmount;
	private Double settledAmount;
	
	private Timestamp date;//this date is holding for payment or receipt dates
	
	private Timestamp transactionDate;
	private Timestamp transactionReceiveDate;
	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;
	private String chequeBounceComment;
	private Timestamp chequeBounceDate;
	private String optionalButtonType;
	private String bankName;
	private Long bankId;
	private Long bankAccountId;
	private String bankAccountNumber;
	private String transactionReceiptNo;
	private String salesFrTransactionReceiptNo;
	//private String uploadTransactionReceiptNo;
	private String transactionNo;
	private String transactionFor;
	
	// private Double chequeAmount;
	private Double transactionAmount;
	private Double payableAmount;
	private String chequeBounceReasonValue;
	private List<Long> blockIds;
	private List<Long> floorIds;
	private List<Long> flatIds;
	private List<String> flatNos;
	private List<Long> bookingFormIds;
	private List<Long> siteIds;
	private Long bookingFormId;
	private String paymentSetOff;
	private List<Long> paymentSetOffTypes;
	private List<String> transactionTowards;
	private Boolean isShowGstInPDF;
	private Long transactionActionType;
	private String finBokAccInvoiceNo;
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
	private List<EmployeeFinTranPaymentSetOffInfo> paymentSetOffDetails;
	private List<EmployeeFinModiCostInfo> modiCostInfos;
	private List<EmployeeFinModiCostInfo> modiCostDtlsInfos;

	private Long siteAccountId;
	private String siteBankAccountNumber;

	// fields for CRM payment cheque
	private Long paymentFor;
	private String cancelReason;
	private String filePath;
	private String fileUrlPath;
	private List<Map<String, String>> imageLocationDetails;
	private List<FileInfo> fileInfos;
	private List<FileInfo> deleteFileInfos;
	private Long createdBy;
	private Long typeId;
	private List<Long> typeIds;
	private String typeName;
	private List<FinBookingFormLegalCostInfo> finBookingFormLegalCostList;
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
	
	//private Long flatSaleOwnerId;
	private String searchBySetOffType;

	private String operationType;
	private boolean isThisIsModifyTransaction;
	private boolean isThisIsFinalLevel;
	private boolean isThisPaymentTransaction;
	private boolean isThisReceiptTransaction;
	private boolean isFlatAgreementCompleted;
	private boolean isThisShiftTransaction;
	private boolean isThisReCallService;
	private boolean isThisEditTransaction; 
	private boolean isThisDeleteTransaction; 
	private boolean isThisShiftTransactionFromApproval;
	private boolean insertOldApprToNewTrn;
	
	private boolean sendNotification;
	private boolean isThisUplaodedData;
	private boolean isDemandNoteGenerated;
	private boolean isThisTransactionRequest;
	private Long isRecordUploaded;
	private List<Long> actualFlatIds;
	private List<String> actualFlatNos; 
	private Long actualBookingFormId;
	private Timestamp actualTransactionReceiveDate;
	private Timestamp actualTransactionDate;
	private Double flatPayableAmount;
	private String currentApprovalLevelName;
	private String nextApprovalLevelName;
	private Long nextApprovalModule;
	private String receiptStage;
	//private String showGstInPdf;
	private String excelRecordNo;
	private String sourceOfFunds;
	private Long flatSaleOwnerId;
	private CustomerPropertyDetailsInfo customerPropertyDetailsInfo;
	private List<FinTransactionApprovalDetailsPojo> listOfNextApprovalEmaEmails;
	private List<EmployeeFinancialTransactionServiceInfo> reConstructedtransactionServiceRequest;
	private List<FinTransactionApprStatPojo> finTransactionApprStatPojoList;
	
	private List<AddressInfo> customerAddressInfoList;
	private List<AddressInfo> siteAddressInfoList;
	private List<FinProjectAccountResponse> finProjectAccountResponseList;
	private List<OfficeDtlsResponse> officeDetailsList;
	private List<CustomerPropertyDetailsInfo> flatsResponse;
	private FinTransactionEntryDetailsResponse prevTransactionEntryDetailsResponse;
	private boolean isRecievedDateORSetOffChanged;
	private Timestamp chequeHandoverDate;
	private EmployeeFinancialTransactionServiceInfo actualTransactionRequest;
	
	private List<ModicationInvoiceAppRej> modificationApproveRejectDetailsList;
	private List<ModicationInvoiceAppRej> modificationNextApprovalDetailsList;
	
	/* Malladi Changes */
	private Long invoiceType;
	private Long deptId;
	private Long roleId;
	private BookingFormRequest bookingRequest;
	
	
	/*bvr*/
	private Long flatSaleOwnerIdBasedOnAccountId;
	private String flatSaleOwnerNameBasedOnAccountId;
	private List<Long> fbStatusId;
	private String flatBookingStatusName;
	
	
	private List<Long> searchBySetOffTypes;
	private List<Long> siteAccountIds;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
