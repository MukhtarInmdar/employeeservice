/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import com.sumadhura.employeeservice.dto.FinBookingFormLglCostDtlsResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormModiCostDtlsResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DemandNoteGeneratorInfo class provides Demand note generator specific fields.
 * if want to use this class just use for company details field, don't add any fields other than demand note
 * @author Venkat_Koniki
 * @since 27.02.2020
 * @time 05:52PM
 */

@Setter
@Getter
@ToString
public class DemandNoteGeneratorInfo {
	private Long noOfCustomersIncludedInBooking;
	private String demandNoteGeneratedDate;
	private String customerNames;
	private String mobileNumbers;
	private String address;
	private String flatName;
	private String blockName;
	private String floorName;
	private String siteName;
	private String bankName;
	private String surveyNo;
	private String siteAddress;
	private String mileStoneAmount;
	private String totalMilestoneDuePercent;
	private String mileStoneAmountInWords;
	private String siteAccount;
	private String accountHolderName;
	private List<MileStoneInfo> mileStones;
	private String cgstHeading;	
	private String sgstHeading;	
	private String condition;
	private String portalUrl;
	List<FinancialCustomerDetails> customerDetailsList;
	
	private Boolean isInterestOrWithOutInterest;
	private Boolean isShowGstInPDF;
	private String pancard;
	private String currentMileStoneName;
	private Long currentMileStoneNumber;
	private String demandNotePdfFileName;
	private String demandNoteNo;
	private String sumOfDemandNotePercentage;
	private String finSchemeId;
	private String finSchemeName;
	private String finSchemeType;
	//ACP
	private Long finBokFomDmdNoteId;
	private Long bookingFormId;
	private Long transactionEntryId;
	private Long transactionSetOffEntryId;
	private String totalReceiptAmount;
	private String totalReceiptPaidAmount;
	private String totalReceiptPaidAmountInWords;
	private String totalCgstAmount;
	private String totalSgstAmount;
	private String gSTIN;
	private String transactionDate;//transactionDate date is nothing but cheque date
	private String transactionReceiveDate;
	
	private String transactionReceiptDate;
	
	private String transactionTypeName;
	private String transactionModeName;
	private String transferModeName;
	private String chequeNumber;
	private String referenceNo;
	/* Legal Invoice */
	private String invoiceNo;
	private String invoiceDate;
	private String siteState;
	private String siteStateCode;
	private String placeOfSupply;
	private String reraNo;	
	private String b2C_B2B;
	private String customerState;
	private String customerStateCode;
	private String customerGstin;
	private String amount;
	private String totalTaxAmount;
	private String percentageValue;
	private String cgstPercentageValue;
	private String sgstPercentageValue;
	private String totalAmount;
	private String totalAmountInWords;
	private String folderId;
	private List<FinBookingFormLglCostDtlsResponse> finBookingFormLglCostDtlsList;
	/* Modification Cost Invoice */
	private String totalExcessAmount;
	private String totalPayableAmount;
	private List<FinBookingFormModiCostDtlsResponse> finBookingFormModiCostDtlsList;
	private String totalPaidAmount;
	private String totalDueAmount;
	private String totalPendingPenaltyAmount;
	private String siteId;
	private String flatBookingId;
	private String transactionReceiptNo;
	private String pdfTitle;
	private String receivedAmountBy;
	private String companyName;
	private String companySalutation;
	private String companyNameFooter;
	private String companyBillingAddress;
	private String companyTelephoneNo;
	private String companyEmail;
	private String companyCin;
	private String companyLlpin;
	private String companyGstin;
	private String companyWebsite;
	private String companyCity;
	private String lastMilestoneDueDate;
	private String builderOrLandOwner;
	//private String logoForPdf;
	private String rightSidelogoForPdf;
	private String leftSidelogoForPdf;

	private String rightSidelogoFilePath;
	private String leftSidelogoFilePath;
	
	private String thanksAndRegardsFrom;
	private String greetingsFrom;	
	private String currentMilestoneDueDate;
	private String currentMilestoneDueAmount;
	private String previousMilestoneDueAmount;
	private String companyPanNumber;
	private String totalDaysDelayed;
	
	private String folderFilePath;
	private String folderFileUrl;
	/*private Long finBankId;
	private String accountBankName;
	private Long siteAccountId;
	private String siteBankAccountNumber;
	private Long finSiteProjAccMapId;
    private String accountAddress;
	private String ifscCode;*/
	
	/*bvr*/
	private Long flatSaleOwnerIdBasedOnAccountId;
	private String flatSaleOwnerNameBasedOnAccountId;
	private String requestFrom;
}
