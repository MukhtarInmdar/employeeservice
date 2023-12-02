package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinProjectAccountResponse;
import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;

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
public class EmployeeFinancialServiceInfo implements Cloneable {
	private Long empId;
	
	private Long transactionEntryId;
	private Long prevTransactionEntryId;
	private String prevTransactionReceiptNo;
	private Long transactionSetOffEntryId;
	private Long prevTransactionSetOffEntryId;
	private String sourceOfFunds;
	
	private Long siteId;
	private String siteName;
	private String mileStoneAliasName;
	private Long finMilestoneClassifidesId;
	private String demandNoteSelectionType;
	private boolean isNewCustomer;
	private Timestamp demandNoteDate;
	private String isInterestOrWithOutInterest;
	private String condition;
	private String actionUrl;
	private String requestUrl;
	// private Long flatBookingId;
	private Long bookingFormId;
	private Long portNumber;
	private Long finBookingFormDemandNoteId;
	private boolean isReGenerateDemandNote;
	private boolean isThisUpdateDemandNote;
	private boolean isShowGstInPDF;
	private boolean sendNotification;
	private boolean isThisUplaodedData;
	// private List<Long> demandNoteIds;
	private List<Long> bookingFormIds;
	private List<Long> flatIds;
	private List<String> flatNos;
	private List<Long> floorIds;
	private List<Long> blockIds;
	private List<Long> siteIds;
	private List<Long> projectMileStoneIds;
	private List<Long> statusIds;
	private Long siteAccountId;
	private String siteBankAccountNumber;
	private String excelRecordNo;
	private String dataUploadCondition;
	private String typeOfDemandNoteFormat;
	private String filePath;
	private String fileUrl;
	//private String deleteOldFiles;
	private CustomerPropertyDetailsInfo customerPropertyDetailsInfo;
	private List<CustomerPropertyDetailsInfo> demandNoteSelectionTypeValues;
	private List<FinancialProjectMileStoneInfo> financialProjectMileStoneRequests;
	private List<FinBookingFormAccountsInfo> bookingFormAccountsRequests;
	private List<FinancialGstDetailsInfo> financialGstDetailsInfos;
	private List<FileInfo> fileInfos;
	private List<FinancialDemandNoteMS_TRN_Info> demandNoteMSRequests;
	private List<FinancialDemandNoteMS_TRN_Info> demandNoteTransactionRequests;
	private List<FinancialSchemeInfo> financialSchemeRequests;
	//private List<FinancialFlatCostDetailsInfo> financialFlatCostDetailsRequests;;
	private Double sumOfDemandNotePercentage;
	private List<CustomerPropertyDetailsInfo> nonSavedDemandNoteDetails;
 	private List<AddressInfo> customerAddressInfoList;
	private List<AddressInfo> siteAddressInfoList;
	private List<FinProjectAccountResponse> finProjectAccountResponseList;
	private List<OfficeDtlsResponse> officeDetailsList;
	private List<CustomerPropertyDetailsInfo> flatsResponse;
	private List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList;
	private List<Long> currentprojectMileStoneIds;
	private List<FinancialUploadDataInfo> financialUploadDataRequests;
	private String flatBookingId;
	//private List<Long> flatSaleOwnerIds;
	//private Long transactionEntryId;
	
	//used for interest waiver
	private String comment;
	private List<Map<String, String>> comments;
	
	/* Malladi */
	private List<DemandNoteGeneratorInfo> demandNoteGeneratorInfoList;
	private Long flatId;
	private Timestamp startDate;
	private Timestamp endDate;
	
	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}

}
