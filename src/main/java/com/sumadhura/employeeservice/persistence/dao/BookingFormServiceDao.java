/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;
import java.util.Map;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.dto.SearchReferrerCustomer;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.AddressMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.AddressPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraCostPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraSiteWisePojo;
import com.sumadhura.employeeservice.persistence.dto.AminititesInfraDetails;
import com.sumadhura.employeeservice.persistence.dto.AminititesInfraFlatWisePojo;
import com.sumadhura.employeeservice.persistence.dto.BlockPojo;
import com.sumadhura.employeeservice.persistence.dto.BookingStatusChangedPojo;
import com.sumadhura.employeeservice.persistence.dto.ChannelPartnerMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListLegalOfficerPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListRegistrationPojo;
import com.sumadhura.employeeservice.persistence.dto.CheckListSalesHeadPojo;
import com.sumadhura.employeeservice.persistence.dto.ChecklistCrmPojo;
import com.sumadhura.employeeservice.persistence.dto.ChecklistDepartmentMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.CoAppBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantCheckListVerificationPojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.Co_ApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.ContactInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustChecklistVerificationPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerApplicationPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerData;
import com.sumadhura.employeeservice.persistence.dto.CustomerDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerKycSubmittedDocPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerOtherDetailspojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountSummaryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemePojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemeTaxMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingSchemeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDtlsAmenitsFlatMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorPojo;
import com.sumadhura.employeeservice.persistence.dto.MenuLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDocumentsList;
import com.sumadhura.employeeservice.persistence.dto.NOCReleasePojo;
import com.sumadhura.employeeservice.persistence.dto.PoaHolderPojo;
import com.sumadhura.employeeservice.persistence.dto.ProfessionalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferenceMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesCustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesFriendPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteOtherChargesDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
import com.sumadhura.employeeservice.persistence.dto.TdsAuthorizationMasterPojo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.CoApplicentBookingInfo;
import com.sumadhura.employeeservice.service.dto.CoApplicentDetailsInfo;
import com.sumadhura.employeeservice.service.dto.Co_ApplicantInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerKYCDocumentSubmitedInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.CustomerSchemeInfo;
import com.sumadhura.employeeservice.service.dto.DynamicKeyValueInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;

/**
 * EmployeeTicketDao Interface provides Employee Ticketing specific services.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:50PM
 */
public interface BookingFormServiceDao {
	public Integer ValidateCityState(BookingFormRequest bookingFormRequest);
	public List<CustomerPojo> getCustomer(BookingFormRequest bookingFormRequest);
	public List<CustBookInfoPojo> getCustBookInfo(BookingFormRequest bookingFormRequest);
	public List<Co_ApplicantPojo> getCo_Applicant(BookingFormRequest bookingFormRequest);
	public List<CoAppBookInfoPojo> getCoAppBookInfo(BookingFormRequest bookingFormRequest);
	public List<FlatPojo> getFlat(BookingFormRequest bookingFormRequest);
	public List<FlatDetailsPojo> getFlatDetails(BookingFormRequest bookingFormRequest);
	public List<FlatCostPojo> getFlatCost(BookingFormRequest bookingFormRequest);
	public List<FloorPojo> getFloor(BookingFormRequest bookingFormRequest);
	public List<BlockPojo> getBlock(BookingFormRequest bookingFormRequest);
	public List<SitePojo> getSite(BookingFormRequest bookingFormRequest);
	public List<ProfessionalDetailsPojo> getProfessionalDetails(BookingFormRequest bookingFormRequest);
	public List<CustomerAddressPojo> getCustomerAddress(BookingFormRequest bookingFormRequest);
	public List<CustomerAddressPojo> getApplicantAddress(BookingFormRequest bookingFormRequest);
	public List<CustomerOtherDetailspojo> getCustomerOtherDetails(BookingFormRequest bookingFormRequest);
	public List<CheckListSalesHeadPojo> getCheckListSalesHead(BookingFormRequest bookingFormRequest);
	public List<CheckListLegalOfficerPojo> getCheckListLegalOfficer(BookingFormRequest bookingFormRequest);
	public List<CheckListRegistrationPojo> getCheckListRegistration(BookingFormRequest bookingFormRequest);
	public List<ChecklistCrmPojo> getChecklistCrm(BookingFormRequest bookingFormRequest);
	public List<CustChecklistVerificationPojo> getCustChecklistVerification(BookingFormRequest bookingFormRequest);
	public List<CoApplicantCheckListVerificationPojo> getApplicantChecklistVerification(BookingFormRequest bookingFormRequest);
	public List<CheckListPojo> getCheckList(BookingFormRequest bookingFormRequest);
	public List<ChecklistDepartmentMappingPojo> getCheckListDepartmentMapping(BookingFormRequest bookingFormRequest);
	public List<FlatBookingPojo> getFlatbookingDetails(BookingFormRequest bookingFormRequest);
	public List<AddressMappingPojo> getAddressMapping(BookingFormRequest bookingFormRequest);
	public List<AminitiesInfraMasterPojo> getAminitiesInfraMaster(BookingFormRequest bookingFormRequest);
	public List<AminititesInfraFlatWisePojo> getAminitiesInfraFlatWise(BookingFormRequest bookingFormRequest);
	public List<AminitiesInfraSiteWisePojo> getAminitiesInfraSiteWise(BookingFormRequest bookingFormRequest);
	public List<AminitiesInfraCostPojo> getAminitiesInfraCost(BookingFormRequest bookingFormRequest);
	public List<CustomerPropertyDetailsPojo> getCustomerPropertyDetails(BookingFormRequest bookingFormRequest);
	public List<CustomerPropertyDetailsPojo> getFlatFloorBlockSiteByNames(BookingFormRequest bookingFormRequest);
	
	public List<AddressPojo> getAddress(Long addressId);
	public Long saveCustomerDetails(CustomerPojo customerPojo);
	public Long saveCustBookInfo(CustBookInfoPojo custBookInfoPojo);
	public Long saveCoAppBookInfo(CoAppBookInfoPojo custBookInfoPojo);
	public Long saveAddressDetails(AddressPojo addressPojo);
	public Long saveAddressmapping(AddressMappingPojo addressMappingPojo);
	public Long saveCustomerProfessionalDetails(ProfessionalDetailsPojo professionalDetailsPojo);
	public Long saveReferencesCustomer(ReferencesCustomerPojo referencesCustomerPojo);
	public Long saveReferencesFriend(ReferencesFriendPojo referencesFriendPojo);
	public Long saveReferencesMapping(ReferencesMappingPojo referencesMappingPojo);
	public Long saveChannelPartnerMaster(ChannelPartnerMasterPojo channelPartnerMasterPojo);
	public Long saveCustomerOtherDetails(CustomerOtherDetailspojo customerOtherDetailspojo);
	public Long savePoaHolder(PoaHolderPojo PoaHolderPojo);
	public Long saveCustomerApplication(CustomerApplicationPojo customerApplicationPojo);
	public Long saveCustomerKycSubmitted(CustomerKycSubmittedDocPojo customerKycSubmittedDocPojo);
	public Long saveApplicant(CoApplicantPojo coApplicantPojo);
	public Long saveCoApplicant(Co_ApplicantPojo coApplicantPojo);
	public Long saveFlatDetails(FlatDetailsPojo flatDetailsPojo);
	public Long saveFlatCost(FlatCostPojo flatCostPojo);
	public Long saveAminiteInfraCost(AminitiesInfraCostPojo aminitiesInfraCostPojo);
	public Long saveFlatBooking(FlatBookingPojo flatBookingPojo);
	public Long saveFlatDtlsAmenitsFlatMapping(FlatDtlsAmenitsFlatMappingPojo flatDtlsAmenitsFlatMappingPojo);
	public Long saveCoApplicationCheckListVerification(CoApplicantCheckListVerificationPojo applicantCheckListVerificationPojo);
	public Long saveCheckListSalesHead(CheckListSalesHeadPojo checkListSalesHeadPojo);
	public Long saveChecklistCrm(ChecklistCrmPojo checklistCrmPojo);
	public Long saveCheckListLegalOfficer(CheckListLegalOfficerPojo checkListLegalOfficerPojo);
	public Long saveCheckListRegistration(CheckListRegistrationPojo checkListRegistrationPojo);
	public Long saveCustChecklistVerification(CustChecklistVerificationPojo custChecklistVerificationPojo );
	public Long saveContactInfo(ContactInfoPojo contactInfoPojo);
	
	
	//public List<EmployeePojo> getEmployeeDetails(String empName, Long empId, Department dept);
	public List<TdsAuthorizationMasterPojo> getTdsAuthorizationMaster(String optionType);
	public List<FlatPojo> getFlatByName(String flatName);
	public List<BlockPojo> getBlockByName(String blockName);
	public List<SitePojo> getSiteByName(String siteName);
	public List<ChannelPartnerMasterPojo> getChannelPartnerMasterByIdName(String cpId, String cpCompanyName);
	public List<PoaHolderPojo> getPoaHolder(Long custOtherDetailsId, Object object, String condition);
	public List<ReferencesMappingPojo> getReferencesMapping(Long custOtherDetailsId);
	public List<ReferencesCustomerPojo> getReferencesCustomer(Long typeId);
	public List<ReferencesFriendPojo> getReferencesFriend(Long typeId);
	public List<ReferenceMasterPojo> getReferenceMaster(Long typeId);
	public List<ChannelPartnerMasterPojo> getChannelPartnerMaster(Long typeId);
	public List<CustomerApplicationPojo> getCustomerApplication(Long flatBookId);
	public List<CustomerKycSubmittedDocPojo> getCustomerKycSubmittedDoc(Long flatBookId, Long custBookInfoId);
	
	public int updateCustomer(BookingFormRequest bookingFormRequest,List<Long> statusIds);
	public List<MenuLevelMappingPojo> getMenuLevelMappingId(CustomerBookingFormInfo customerBookingFormInfo);
	public List<CustomerPojo> getCustomerDetailsByPanCardorPassport(String panCard, String passport, Status status);
	public List<Co_ApplicantPojo> getCo_ApplicantByPanCard(String panCard, String passport, Status status);
	public int updateFlatCost(BookingFormRequest bookingFormRequest,Long status);
	public int updateCustBookInfo(BookingFormRequest bookingFormRequest, Long status);
	public int updateCoAppBookInfo(BookingFormRequest bookingFormRequest, Long status);
	public int updateCoApplicant(BookingFormRequest bookingFormRequest, Long status);
	public int updateFlatBooking(BookingFormRequest bookingFormRequest, Long status, boolean flag);
	public List<EmployeePojo> getEmployeeDetails(String empName, Long empId, Department ... dept);
	public int updateCustChecklistVerification(BookingFormRequest bookingFormRequest);
	public int updateCheckList(String query);
	public int updateCoApplicantChecklistVerification(BookingFormRequest request);
	public List<EmployeeDetailsPojo> getEmployeeDetails(Status status);

	Long updateCustomerInfo(CustomerPojo customerPojo);
	Long updateCustomerProfessionalDetails(ProfessionalDetailsPojo professionalDetailsPojo);
	public Long updateAddressDetails(AddressPojo ddressPojo);
	public List<CustomerPropertyDetailsPojo> getBookingFlatData(CustomerPropertyDetailsPojo flatData);
	public Long deleteCoApplicantBookInfoData(Long coAppBookInfoId);
	public Long deleteAddressMappingOfApplicantOrCoApplicant(Long custBookInfoId, Long type);
	public Long deleteCustomerOtherDetails(Long flatBookId);
	public void deleteKYCDetails(Long custBookInfoId);
	public Long deleteContactInfo(ContactInfoPojo contactInfoPojo);
	public List<CoAppBookInfoPojo> getSameCoApplicantforMulCustomerCount(CoApplicentDetailsInfo coApplInfo, Long custBookInfoId);
	public Long updateCoApplicantInfo(Co_ApplicantInfo co_ApplicantInfo);
	public Long updateCoApplicantBookInfo(CoApplicentBookingInfo coApplicentBookingInfo, Long custBookInfoId, CoApplicentDetailsInfo coApplicentDetailsInfo);
	public Long deleteAddressOfApplicantOrCoApplicant(Long addressId);
	public Long deleteCoApplicantProffesionalDetails(Long coAppProfId);
	public Long delteCoApplicantInfoDetails(Long coAppId);
	public Long updateCustomerBookInfo(CustBookInfoPojo custBookInfoPojo);
	public Long deleteCustomerPOAHolderDetails(CustomerOtherDetailspojo customerOtherDetailspojo);
	public Long updateCustomerOtherDetails(CustomerOtherDetailspojo customerOtherDetailspojo);
	public Long updatePoaHolder(PoaHolderPojo poaHolderPojo);
	public Long updateFlatBookingInfo(FlatBookingPojo flatBookingPojo);
	public Long updateReferencesMapping(ReferencesMappingPojo referencesMappingPojo);
	public int updateFlatDetails(FlatBookingInfo flatBookingInfo, Long status, boolean flag);
	public List<CustomerPojo> getCustomerDetailsByName(String custName, String projectName, String unitNo);
    //ACP
	public int updateFinancialBookingFormAccountSummaryCost(FinBookingFormAccountSummaryPojo accountSummaryPojo);
	public int insertFinancialBookingFormAccountSummaryCost(FinBookingFormAccountSummaryPojo accountSummaryPojo);
	public List<FinSchemeTaxMappingPojo> getCustomerSchemeDetailsBySchemeName(CustomerSchemeInfo customerSchemeInfo,FlatBookingInfo flatBookingInfo);
	public int inActivePastCustomerSchemeDetails(FlatBookingSchemeMappingPojo inActivePastCustomerSchemepojo);
	public Long updateFlatBookingMileStoneDetails(FlatBookingPojo flatBookingInfoToFlatBookingPojo);
	public int saveBookingChangedStatus(BookingStatusChangedPojo pojo);
	public List<CustomerPojo> getCustomerCoApplicantFlatDetails(Co_ApplicantPojo co_ApplicantPojo);
	public Long getNumberOfCarParkingAllotedNumber(BookingFormRequest bookingFormRequestCopy);
	public List<DynamicKeyValueInfo> getTheTermsAndConditions(BookingFormRequest bookingFormRequestCopy, CustomerPropertyDetailsInfo customerPropertyDetailsInfo);
	public List<SiteOtherChargesDetailsPojo> getTheSiteOtherChargesDetails(BookingFormRequest bookingFormRequestCopy);
	public List<Map<String, Object>> getAggrementTypesList(BookingFormRequest bookingFormRequest);
	public int updateWelcomeMailSendDetails(BookingFormRequest bookingFormRequestCopy);	
    public List<FlatBookingPojo> getOldFlatBookingDetails(FlatBookingInfo flatBookingInfo,ServiceRequestEnum requestEnum);
	public void updateNewFlatBookingIdInTicketDetails(FlatBookingPojo oldFlatBookingPojo,BookingFormRequest newBookingrequest,MetadataId metadataId);
	public void updateNewCustomerIdInTicketConversation(BookingFormRequest newBookingrequest,Status status);
	public void updateAppRegistrationStatus(BookingFormRequest newBookingrequest);
	public void updateNewFlatBookingIdInMessengerDetails(FlatBookingPojo oldFlatBookingPojo,BookingFormRequest newBookingrequest,Status status);
	public List<Map<String, Object>> getCustomerPropertyDetailsToInactive(BookingFormRequest bookingFormRequest);
	public Integer updateCustomerApplicationDetails(CustomerApplicationPojo custAppPojo);
	public List<NOCDetailsPojo> getNOCCheckListDetails(BookingFormRequest bookingFormRequest);
	public Long updateNocLetter(FileInfo info,CustomerPropertyDetailsInfo customerPropertyDetailsInfo,BookingFormRequest bookingFormRequestCopy);
	public List<NOCReleasePojo> getNOCReleaseDetails(BookingFormRequest bookingFormRequest);
	public List<Map<String, Object>> loadExtraCarParkingDetails(BookingFormRequest bookingFormRequest);
	Long updateNocDocuments(FileInfo info, CustomerPropertyDetailsInfo customerPropertyDetailsInfo,
			BookingFormRequest bookingFormRequestCopy);
	List<NOCReleasePojo> getNOCDocumentsDetails(BookingFormRequest bookingFormRequest);
	public boolean isAlreadyMailSentToLoanBanker(BookingFormRequest bookingFormRequest);
	Integer isMailSentToBankerOnBooking(BookingFormRequest bookingFormRequest);
	List<NOCDocumentsList> getNOCDocumentsList(BookingFormRequest bookingFormRequest);
	List<CustomerData> getCustomerData(CustomerInfo customerInfo, Status status);
	List<FlatCostPojo> getFlatCostByFlatBooking(BookingFormRequest bookingFormRequest);
	List<EmployeeDetailsMailPojo> getEmployeeDetails(CustomerDetailsPojo customerDetailsPojo);
	List<CustomerKYCDocumentSubmitedInfo> getKycDocumentsList(BookingFormRequest bookingFormRequest);
	List<AminititesInfraDetails> getAminities(BookingFormRequest bookingFormRequest);
	List<CustomerData> getflatDetailsByflat(BookingFormRequest bookingFormRequest);
	List<CustomerData> getNonBookedDetails(BookingFormRequest bookingFormRequest);
	
		public List<CustomerAddressPojo> getcityList(CustomerInfo customerData);
	public List<CustomerAddressPojo> getCountryList();
	List<FinSchemePojo> getFinSchemes(BookingFormRequest bookingFormRequest);
	List<FlatBookingPojo> getSalesForcesBookingIds(BookingFormRequest bookingFormRequest);
	Long getcountryIdByName(String name);
	Long getcityIdByName(String name);
	Long getstateIdByName(String name);
	
	
}
