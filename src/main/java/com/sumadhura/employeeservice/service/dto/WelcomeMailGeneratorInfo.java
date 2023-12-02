/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.List;
import java.util.Map;

import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteOtherChargesDetailsPojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * WelcomeMailGeneratorInfo class provides welcome mail generator specific fields.
 * 
 * @author Aniket Chavan
 * @since 03.08.2021
 * @time 03:30PM
 */

@Setter
@Getter
@ToString
public class WelcomeMailGeneratorInfo {
	
	private String grandTotalIncludingMaintenanceGST;
	private String grandTotalIncludingMaintenanceExcludeGST;
	private String grandTotalIncludingMaintenance;
	private String grandTotalIncludingMaintenanceInWords;
	/*private String grandTotalIncludingMaintenance;
	private String grandTotalIncludingMaintenanceInWords;*/
	
	/*private String totalAgreementCost;
	private String totalAgreementGST;*/
	private String totalBasicFlatCost;
	private String totalBalanceAmount;
	private String totalBalanceAmountInWords;
	private String sbuaIntoCarpusFundSum;
	//private String sbuaIntoMaintenanceSum;
	
	private String totalPaidAmount;
	private String totalPaidAmountInWords;
	private String totalPaidGSTAmount;
	private String totalPaidGSTAmountInWords;
	
	private String totalPaidAmountExcludeGST;
	private String totalPaidAmountExcludeGSTInWords;
	
	private List<CustomerInfo> customerDetailsList ;//firstApplicantDetails
	private List<Co_ApplicantInfo> coApplicantDetailsList ;
	
	private List<ProfessionalInfo> firstApplicantProfessionalDetails;
	private List<ProfessionalInfo> coApplicantProfessionalDetailsList;
	
	private List<OfficeDtlsPojo> officeDetailsPojoList;		
	private List<MileStoneInfo> mileStones;
	private List<DynamicKeyValueInfo> termsAndConditions;
	private List<SiteOtherChargesDetailsPojo> siteOtherChargesDetails;
	//private DemandNoteGeneratorInfo demandNoteGeneratorInfo;
	private Map<String,List<Map<String,Object>>> dataForPdf;
	private List<AgreementDraftCalculations> agreementDraftCalculations;
	private List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos;
	
}
