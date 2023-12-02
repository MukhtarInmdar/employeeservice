package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsResponse;
import com.sumadhura.employeeservice.service.dto.AddressInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;

import lombok.Data;

@Data
public class EmployeeFinancialTransactionResponse implements Serializable {
	private static final long serialVersionUID = -6611767197860950662L;
	//private String responseMessage;
	private Long siteId;
	private String siteName;
	private Long transactionTypeId;
	private Long transactionModeId;
	private Long transferModeId;
	private String transactionTypeName;
	private String transactionModeName;
	private String transferModeName;//online money transfer mode
	private Long transactionEntryId;
	private Long transactionSetOffEntryId;
	private Long bookingFormId;
	private String chequeNumber;
	private String referenceNo;
	private Boolean isShowGstInPDF;
	private Double receivedAmount; 
	private Timestamp transactionDate;
	private Timestamp transactionReceiveDate;
	private String transactionReceiptNo;
	private String transactionFor;
	private Long transactionForId;
	private Double totalReceiptAmount;
	private Double totalReceiptPaidAmount;
	private Double totalCgstAmount;
	private Double totalSgstAmount;
	private Double totalGSTAmount;
	private String operationType;
	private String demandNotePdfFileName;
	private String transactionPdfFileName;
	private String bankName;
	private List<FinBankResponse> finBankResponseList;
	private List<FinTransactionModeResponse> finTransactionModeResponseList;
	private List<FinTransactionTypeResponse> finTrnasactionTypeResponseList;
	
	private List<FinancialProjectMileStoneResponse> financialProjectMileStoneResponse;
	private List<FinTransactionEntryResponse> finTransactionEntryResponseList;
	private List<CustomerPropertyDetailsInfo> customerPropertyDetailsInfoList;
	private List<FinTransactionEntryDetailsResponse> finTransactionEntryDetailsResponseList;
	private List<FinAnonymousEntryResponse> finAnonymousEntryResponseList;
	private List<FinTransferModeResponse> finTransferModeResponseList;
	private List<FinPenaltyTaxResponse> finPenaltyTaxResponseList;
	private List<AddressInfo> customerAddressInfoList;
	private List<AddressInfo> siteAddressInfoList;
	private List<FinProjectAccountResponse> finProjectAccountResponseList;
	private List<OfficeDtlsResponse> officeDetailsList;
	private List<CustomerPropertyDetailsInfo> flatsResponse;
	private FinBookingFormLegalCostPdfResponse finBookingFormLegalCostPdfResponse;
	private List<FileInfo> fileInfoList;
	private FileInfo fileResponse;
	private Double percentageValue;
	private String invoiceNo;
	private FinBookingFormAccountsResponse finBookingFormAccountsResponse;
	private FinBookingFormModiCostResponse finBookingFormModiCostResponse;
	private List<FinTransactionForResponse> transactionForResponseList;
	private Long employeeId;
	
	private List<FinancialProjectMileStoneResponse> modificationCostResponseList;
	private List<FinancialProjectMileStoneResponse> legalCostResponseList;
	private List<FinancialProjectMileStoneResponse> maintenanceCostResponseList;
	private List<FinancialProjectMileStoneResponse> flatKhataResponseList;
	private List<FinancialProjectMileStoneResponse> corpusFundsResponseList;

	private Timestamp lastTransactionEditedDate;
	private String lastTransactionEditedBy;
	
	/* Malladi */
	private List<FinTransactionSetOffResponse> finTransactionSetOffResponseList;
	private List<FinancialProjectMileStoneResponse> interestCostResponseList;
}	
