package com.sumadhura.employeeservice.dto;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.sumadhura.employeeservice.service.dto.AddressInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.DemandNoteGeneratorInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author @NIKET CH@V@N
 * @since 11.01.2020
 * @time 06:50 PM
 * @description this class is for holding request data for financial module
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class EmployeeFinancialResponse extends Result implements Serializable {
	private static final long serialVersionUID = 4519169866107540353L;
	private Long finMilestoneClassifidesId;
	private Long siteId;
	private String siteName;
	private String mileStoneAliasName;
	private String description;
	private Long statusId;
	private String createdBy;
	private Timestamp createdDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	private boolean isShowGstInPDF;
	private List<FinancialProjectMileStoneResponse> financialProjectMileStoneResponse;
	private List<FinMileStoneCLSMappingBlocksResponse> mappingBlocksResponse;
	
	private List<FinBookingFormDemandNoteResponse>  finBookingFormDemandNoteesponse ;
	
	private List<AddressInfo> customerAddressInfoList;
	private List<AddressInfo> siteAddressInfoList;
	private List<FinProjectAccountResponse> finProjectAccountResponseList;
	private List<OfficeDtlsResponse> officeDetailsList;
	private List<CustomerPropertyDetailsInfo> flatsResponse;
	
	private Double totalMileStoneAmount;
	private Double totalmileStoneBasicAmount;
    private Double totalMileStoneTaxAmount;
    private Double totalSgstAmount;
    private Double totalCgstAmount;
    private Double totalPaidAmount;
    private Double totalPenaltyAmount;
    private Double totalPendingPenaltyAmount;
    private Double totalDueAmount;
    private Double totalMilestoneDuePercent;
    private String demandNotePdfFileName;
    private Double totalDemandNoteAmount;
    private String totalFlatCost;
    private String totalCreditAmount;
    private String totalDebitAmount;
    private String totalBalanceAmount;
    private Double totalPenalityPaidAmount;
    private Double totalInterestWaiverAdjAmount;
    private Timestamp bookingDate;
    private Timestamp demandNoteDate;
    private Boolean isInterestOrWithOutInterest;
    private List<CustomerPropertyDetailsInfo> nonSavedDemandNoteDetails;
    private Double sumOfDemandNotePercentage;
    private List<FileInfo> fileInfoList;
    
    private List<FinCustomerLedgerResponse> customerLedgerResponses;
    private Object data;
    private String currentMilestoneName;
    private Timestamp currentMilestoneDueDate;
    private Long totalDaysDelayed;
    private String flatNo;
    private String milestoneName;
    private List<Map<String, Object>> paymentPaidList;    
    private List<DemandNoteGeneratorInfo> demandNoteGeneratorInfoList;
}
