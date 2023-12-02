package com.sumadhura.employeeservice.service.mappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.BeanUtils;

import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.dto.CustomerProfileResponse;
import com.sumadhura.employeeservice.enums.CheckList;
import com.sumadhura.employeeservice.enums.City;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.KYCDocuments;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Organization;
import com.sumadhura.employeeservice.enums.Refrences;
import com.sumadhura.employeeservice.enums.Sector;
import com.sumadhura.employeeservice.enums.SiteState;
import com.sumadhura.employeeservice.enums.State;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.enums.WorkFunction;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.persistence.dao.BookingFormServiceDao;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;
import com.sumadhura.employeeservice.persistence.dto.AddressMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.AddressPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraCostPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraSiteWisePojo;
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
import com.sumadhura.employeeservice.persistence.dto.CustBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustChecklistVerificationPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerApplicationPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerKycSubmittedDocPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerOtherDetailspojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountSummaryPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorPojo;
import com.sumadhura.employeeservice.persistence.dto.PoaHolderPojo;
import com.sumadhura.employeeservice.persistence.dto.ProfessionalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferenceMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesCustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesFriendPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
import com.sumadhura.employeeservice.service.dto.AddressInfo;
import com.sumadhura.employeeservice.service.dto.AddressMappingInfo;
import com.sumadhura.employeeservice.service.dto.AminitiesInfraCostInfo;
import com.sumadhura.employeeservice.service.dto.BlockInfo;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.BookingFormsListResponse;
import com.sumadhura.employeeservice.service.dto.ChanelPartnerInfo;
import com.sumadhura.employeeservice.service.dto.CheckListCRMInfo;
import com.sumadhura.employeeservice.service.dto.CheckListDepartmentMappingInfo;
import com.sumadhura.employeeservice.service.dto.CheckListInfo;
import com.sumadhura.employeeservice.service.dto.CheckListLegalOfficerInfo;
import com.sumadhura.employeeservice.service.dto.CheckListRegistrationInfo;
import com.sumadhura.employeeservice.service.dto.CheckListSalesHeadInfo;
import com.sumadhura.employeeservice.service.dto.CoApplicantCheckListVerificationInfo;
import com.sumadhura.employeeservice.service.dto.CoApplicentBookingInfo;
import com.sumadhura.employeeservice.service.dto.CoApplicentDetailsInfo;
import com.sumadhura.employeeservice.service.dto.CoApplicentInfo;
import com.sumadhura.employeeservice.service.dto.Co_ApplicantInfo;
import com.sumadhura.employeeservice.service.dto.CustomerApplicationInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingInfo;
import com.sumadhura.employeeservice.service.dto.CustomerCheckListVerificationInfo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerKYCDocumentSubmitedInfo;
import com.sumadhura.employeeservice.service.dto.CustomerOtherDetailsInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.DynamicKeyValueInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;
import com.sumadhura.employeeservice.service.dto.FlatCostInfo;
import com.sumadhura.employeeservice.service.dto.FlatInfo;
import com.sumadhura.employeeservice.service.dto.FloorInfo;
import com.sumadhura.employeeservice.service.dto.OraganizationDetails;
import com.sumadhura.employeeservice.service.dto.POADetailsInfo;
import com.sumadhura.employeeservice.service.dto.ProfessionalInfo;
import com.sumadhura.employeeservice.service.dto.ReferenceMaster;
import com.sumadhura.employeeservice.service.dto.ReferencesCustomerInfo;
import com.sumadhura.employeeservice.service.dto.ReferencesFriendInfo;
import com.sumadhura.employeeservice.service.dto.ReferencesMappingInfo;
import com.sumadhura.employeeservice.service.dto.SectorDetailsInfo;
import com.sumadhura.employeeservice.service.dto.SiteInfo;
import com.sumadhura.employeeservice.service.dto.WorkFunctionInfo;
import com.sumadhura.employeeservice.util.CurrencyUtil;
import com.sumadhura.employeeservice.util.DateToWord;
import com.sumadhura.employeeservice.util.NumberToWord;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

/**
 * BookingFormMapper class provides Booking Form specific converstions.
 * 
 * @author Srivenu
 * @since 21.05.2019
 * @time 01:20PM
 */

public class BookingFormMapper {

	private Logger logger = Logger.getLogger(BookingFormMapper.class);

	//@Autowired(required = true)
	//@Qualifier("BookingFormServiceDaoImpl")
	//private BookingFormServiceDao bookingFormServiceDaoImpl;

	/*@Autowired
	private CurrencyUtil currencyUtil;*/
	
	private static final RoundingMode roundingMode = RoundingMode.HALF_UP;
	private static final int roundingModeSize = 2;

	public EmployeeTicketRequestInfo bookingFormRequestToEmployeeTicketRequestInfo(
			BookingFormRequest bookingFormRequest) {
		logger.info(
				"***** The control is inside the bookingFormRequestToEmployeeTicketRequestInfo in  BookingFormMapper ******");
		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		BeanUtils.copyProperties(bookingFormRequest, employeeTicketRequestInfo);
		return employeeTicketRequestInfo;
	}

	public CustomerInfo customerProfileResponseToCustomerInfo(CustomerProfileResponse customerProfileResponse) {
		logger.info(
				"***** The control is inside the customerProfileResponseToCustomerInfo in  BookingFormMapper ******");
		CustomerInfo customerInfo = new CustomerInfo();
		BeanUtils.copyProperties(customerProfileResponse.getCustomer(), customerInfo);
		return customerInfo;
	}

/*	public List<CoApplicentDetailsInfo> customerProfileResponseToCoApplicentDetails(
			CustomerProfileResponse customerProfileResponse, BookingFormRequest bookingFormRequest, BookingFormServiceDao bookingFormServiceDaoImpl) {
		BookingFormMapper bookingFormMapper = new BookingFormMapper();
		logger.info(
				"***** The control is inside the customerProfileResponseToCoApplicentDetails in  BookingFormMapper ******");
		List<CoApplicant> coApplicants = customerProfileResponse.getCoApplicants();
		List<CoApplicentDetailsInfo> coApplicentDetailsList = new ArrayList<>();
		if(coApplicants!=null&&coApplicants.size()>0)
		{
			Long i = MetadataId.APPLICANT1.getId();
			for (CoApplicant coApplicant : coApplicants) {
				CoApplicentInfo coApplicentInfo = new CoApplicentInfo();
				BeanUtils.copyProperties(coApplicant, coApplicentInfo);
				
				List<AddressInfo> applicantAddressInfos = new ArrayList<>();
				bookingFormRequest.setMetadataId(i);
				bookingFormRequest.setApplicantId(coApplicant.getApplicantId());
				List<AddressMappingPojo> addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
				if(addressMappingPojos!=null&&addressMappingPojos.size()>0) {
					for(AddressMappingPojo addressMappingPojo : addressMappingPojos) {
						List<AddressInfo> addressInfos = bookingFormMapper.addressPojosTocustomerAddressInfos(bookingFormServiceDaoImpl.getAddress(addressMappingPojo.getAddressId()),addressMappingPojo);
						applicantAddressInfos.addAll(addressInfos);
					}
				} else {
					applicantAddressInfos.add(new AddressInfo());
				}
				
				bookingFormRequest.setProffisionalId(coApplicant.getCustProffisionalId());
				List<ProfessionalDetailsPojo> applicantProfessionalDetailsPojos = bookingFormServiceDaoImpl.getProfessionalDetails(bookingFormRequest);
				ProfessionalInfo professionalInfo;
				if(applicantProfessionalDetailsPojos!=null && applicantProfessionalDetailsPojos.size()>0) {
					professionalInfo = bookingFormMapper.professionalDetailsPojoToCustProfessionalInfo(applicantProfessionalDetailsPojos.get(0));
				} else {
					professionalInfo = new ProfessionalInfo();
					professionalInfo.setOraganizationDetails(new OraganizationDetails());
					professionalInfo.setSectorDetailsInfo(new SectorDetailsInfo());
					professionalInfo.setWorkFunctionInfo(new WorkFunctionInfo());
				}
				
				CoApplicentDetailsInfo coApplicentDetailsInfo = new CoApplicentDetailsInfo();
				coApplicentDetailsInfo.setCoApplicentInfo(coApplicentInfo);
				coApplicentDetailsInfo.setAddressInfos(applicantAddressInfos);
				coApplicentDetailsInfo.setProfessionalInfo(professionalInfo);
				coApplicentDetailsList.add(coApplicentDetailsInfo);
				i++; //MetadataId.APPLICANT2.getId()
			}
		}
		return coApplicentDetailsList;
	}*/

	public List<AddressInfo> addressPojosTocustomerAddressInfos(List<AddressPojo> addressPojos, AddressMappingPojo addressMappingPojo) {
		logger.info(
				"***** The control is inside the addressPojosTocustomerAddressInfos in  BookingFormMapper ******");
		List<AddressInfo> customerAddressInfos = new ArrayList<>();
		for (AddressPojo addressPojo : addressPojos) {
			AddressInfo customerAddressInfo = new AddressInfo();
			BeanUtils.copyProperties(addressPojo, customerAddressInfo);
			customerAddressInfo.setState(getStateName(addressPojo.getStateId()));
			//customerAddressInfo.setCity(getCityName(addressPojo.getCityId()));
			customerAddressInfo.setCity(addressPojo.getCity());
			
			AddressMappingInfo addressMappingInfo = new AddressMappingInfo();
			BeanUtils.copyProperties(addressMappingPojo, addressMappingInfo);
			addressMappingInfo.setMetaType(getMetadataName(addressMappingPojo.getType()));
			customerAddressInfo.setAddressMappingType(addressMappingInfo);
			customerAddressInfos.add(customerAddressInfo);
		}
		return customerAddressInfos;
	}

	public ProfessionalInfo professionalDetailsPojoToCustProfessionalInfo(
			ProfessionalDetailsPojo professionalDetailsPojo) {
		logger.info("***** The control is inside the professionalDetailsPojoToCustProfessionalInfo in  BookingFormMapper ******");
		ProfessionalInfo professionalInfo = new ProfessionalInfo();
		BeanUtils.copyProperties(professionalDetailsPojo, professionalInfo);
		
		OraganizationDetails oraganizationDetails = new OraganizationDetails();
		oraganizationDetails.setOrganizationTypeId(professionalDetailsPojo.getOrganizationTypeId());
		oraganizationDetails.setOrganizationTypeName(getOrganizationName(professionalDetailsPojo.getOrganizationTypeId()));
		oraganizationDetails.setIfOtherOrgTypeName(professionalDetailsPojo.getIfOtherOrgTypeName());
		
		WorkFunctionInfo workFunctionInfo = new WorkFunctionInfo();
		workFunctionInfo.setWorkFunctionId(professionalDetailsPojo.getWorkFunctionId());
		workFunctionInfo.setWorkFunctionName(getWorkFunctionName(professionalDetailsPojo.getWorkFunctionId()));
		workFunctionInfo.setIfOtherworkFunctionName(professionalDetailsPojo.getIfOtherworkFunctionName());
		
		SectorDetailsInfo sectorDetailsInfo = new SectorDetailsInfo();
		sectorDetailsInfo.setWorkSectorId(professionalDetailsPojo.getWorkSectorId());
		sectorDetailsInfo.setWorkSectorName(getSectorName(professionalDetailsPojo.getWorkSectorId()));
		sectorDetailsInfo.setIfOtherWorkSectorName(professionalDetailsPojo.getIfOtherWorkSectorName());
		
		professionalInfo.setOraganizationDetails(oraganizationDetails);
		professionalInfo.setWorkFunctionInfo(workFunctionInfo);
		professionalInfo.setSectorDetailsInfo(sectorDetailsInfo);
		return professionalInfo;
	}


	public CustomerOtherDetailsInfo customerOtherDetailspojoToCustomerOtherDetailsInfo(CustomerOtherDetailspojo customerOtherDetailsPojo, BookingFormServiceDao bookingFormServiceDaoImpl, EmployeeTicketDao employeeTicketDaoImpl) {
		logger.info("***** The control is inside the customerOtherDetailspojoToCustomerOtherDetailsInfo in  BookingFormMapper ******");
		BookingFormMapper mapper = new BookingFormMapper();
		CustomerOtherDetailsInfo customerOtherDetailsInfo = new CustomerOtherDetailsInfo();
		BeanUtils.copyProperties(customerOtherDetailsPojo, customerOtherDetailsInfo);
		Long custOtherDetailsId = customerOtherDetailsPojo.getId();
		logger.info(custOtherDetailsId);
		logger.info(bookingFormServiceDaoImpl==null);
		List<PoaHolderPojo> poaHolderPojos = bookingFormServiceDaoImpl.getPoaHolder(custOtherDetailsId,null,null);
		if(poaHolderPojos!=null && poaHolderPojos.size()>0)
			customerOtherDetailsInfo.setPoadetailsInfo(mapper.poaHolderPojoToPoaHolderInfo(poaHolderPojos.get(0)));
		else
			customerOtherDetailsInfo.setPoadetailsInfo(new POADetailsInfo());
		List<CustomerApplicationPojo> customerApplicationPojos = bookingFormServiceDaoImpl.getCustomerApplication(customerOtherDetailsPojo.getFlatBookId());
		if(customerApplicationPojos!=null && customerApplicationPojos.size()>0)
		{
			customerOtherDetailsInfo.setApplicationNumber(customerApplicationPojos.get(0).getAppNo());
			Long stmId = customerApplicationPojos.get(0).getStmId();
			if(stmId==null)
				customerOtherDetailsInfo.setEmpIdOfSTM(0L);
			else
				customerOtherDetailsInfo.setEmpIdOfSTM(stmId);
		}
		
		List<ReferencesMappingPojo> referencesMappingPojos = bookingFormServiceDaoImpl.getReferencesMapping(custOtherDetailsId);
		Long type = 0L;//referencesMappingPojo.getType();
		Long typeId = 0L;//referencesMappingPojo.getTypeId();
		if(referencesMappingPojos!=null && referencesMappingPojos.size()>0)
		{
			ReferencesMappingPojo referencesMappingPojo = referencesMappingPojos.get(0);
			customerOtherDetailsInfo.setReferencesMappingInfo(mapper.referencesMappingPojoToReferencesMappingInfo(referencesMappingPojo));
			type = referencesMappingPojo.getType();
			typeId = referencesMappingPojo.getTypeId();
		}
		else
		{
			customerOtherDetailsInfo.setReferencesMappingInfo(new ReferencesMappingInfo());
		}
		
		if(type==MetadataId.CHANEL_PARTNER.getId()||type.equals(MetadataId.CHANEL_PARTNER.getId()))
		{
			List<ChannelPartnerMasterPojo> pojos = bookingFormServiceDaoImpl.getChannelPartnerMaster(typeId);
			customerOtherDetailsInfo.setChannelPartnerInfo(mapper.chanelPartnerPojoToChanelPartnerInfo(Util.isNotEmptyObject(pojos)?pojos.get(0):new ChannelPartnerMasterPojo()));
			customerOtherDetailsInfo.setReferencesFriend(new ReferencesFriendInfo());
			customerOtherDetailsInfo.setReferencesCustomer(new ReferencesCustomerInfo());
			customerOtherDetailsInfo.setReferenceMaster(new ReferenceMaster());
			customerOtherDetailsInfo.setReferenceName(Refrences.CHANEL_PARTNER.getName());
		}
		if(type==MetadataId.REFERENCES_FRIEND.getId()||type.equals(MetadataId.REFERENCES_FRIEND.getId()))
		{
			List<ReferencesFriendPojo> pojos = bookingFormServiceDaoImpl.getReferencesFriend(typeId);
			customerOtherDetailsInfo.setReferencesFriend(mapper.referencesFriendPojoToReferencesFriendInfo(Util.isNotEmptyObject(pojos)?pojos.get(0):new ReferencesFriendPojo()));
			customerOtherDetailsInfo.setReferencesCustomer(new ReferencesCustomerInfo());
			customerOtherDetailsInfo.setReferenceMaster(new ReferenceMaster());
			customerOtherDetailsInfo.setChannelPartnerInfo(new ChanelPartnerInfo());
			customerOtherDetailsInfo.setReferenceName(Refrences.FRIEND_FAMILY.getName());
		}
		if(type==MetadataId.REFERENCES_CUSTOMER.getId()||type.equals(MetadataId.REFERENCES_CUSTOMER.getId()))
		{
			List<ReferencesCustomerPojo> pojos = bookingFormServiceDaoImpl.getReferencesCustomer(typeId);
			customerOtherDetailsInfo.setReferencesCustomer(mapper.referencesCustomerPojoToReferencesCustomerInfo(Util.isNotEmptyObject(pojos)?pojos.get(0):new ReferencesCustomerPojo()));
			customerOtherDetailsInfo.setReferencesFriend(new ReferencesFriendInfo());
			customerOtherDetailsInfo.setReferenceMaster(new ReferenceMaster());
			customerOtherDetailsInfo.setChannelPartnerInfo(new ChanelPartnerInfo());
			customerOtherDetailsInfo.setReferenceName(Refrences.EXISTING_OWNER.getName());
		}
		if(type==MetadataId.REFRENCE_MASTER.getId()||type.equals(MetadataId.REFRENCE_MASTER.getId()))
		{
			List<ReferenceMasterPojo> pojos = bookingFormServiceDaoImpl.getReferenceMaster(typeId);
			ReferenceMasterPojo referenceMasterPojo = Util.isNotEmptyObject(pojos)?pojos.get(0):new ReferenceMasterPojo();
			customerOtherDetailsInfo.setReferenceMaster(mapper.referenceMasterPojoToReferenceMasterInfo(referenceMasterPojo));
			customerOtherDetailsInfo.setReferencesFriend(new ReferencesFriendInfo());
			customerOtherDetailsInfo.setReferencesCustomer(new ReferencesCustomerInfo());
			customerOtherDetailsInfo.setChannelPartnerInfo(new ChanelPartnerInfo());
			customerOtherDetailsInfo.setReferenceName(referenceMasterPojo.getReferenceType());
		}
		
		if(customerOtherDetailsPojo.getExistedFlatBookId()!=null)
		{
			customerOtherDetailsInfo.setHaveYouOwnedSumadhuraHome(Status.YES.getDescription());
			EmployeeTicketRequestInfo etReq = new EmployeeTicketRequestInfo();
			etReq.setRequestUrl("noCustId");
			etReq.setFlatBookingId(customerOtherDetailsPojo.getExistedFlatBookId());
			List<CustomerPropertyDetailsPojo> custPropertyDets = employeeTicketDaoImpl.getCustomerPropertyDetails(etReq, Status.ACTIVE);
			customerOtherDetailsInfo.setHaveYouOwnedSumadhuraHomeIfYesUnitNo(Util.isNotEmptyObject(custPropertyDets)?custPropertyDets.get(0).getFlatNo():"N/A");
			customerOtherDetailsInfo.setHaveYouOwnedSumadhuraHomeIfYesProjectName(Util.isNotEmptyObject(custPropertyDets)?custPropertyDets.get(0).getSiteName():"N/A");
		}
		else
		{
			customerOtherDetailsInfo.setHaveYouOwnedSumadhuraHome(Status.NO.getDescription());
		}
		
		return customerOtherDetailsInfo;
	}

	private ReferenceMaster referenceMasterPojoToReferenceMasterInfo(ReferenceMasterPojo referenceMasterPojo) {
		logger.info("**** The control is inside the referenceMasterPojoToReferenceMasterInfo in BookingFormMapper *****");
		ReferenceMaster referenceMaster = new ReferenceMaster();
		BeanUtils.copyProperties(referenceMasterPojo, referenceMaster);		
		return referenceMaster;
	}

	private ReferencesCustomerInfo referencesCustomerPojoToReferencesCustomerInfo(
			ReferencesCustomerPojo referencesCustomerPojo) {
		logger.info("**** The control is inside the referencesCustomerPojoToReferencesCustomerInfo in BookingFormMapper *****");
		ReferencesCustomerInfo referencesCustomerInfo = new ReferencesCustomerInfo();
		BeanUtils.copyProperties(referencesCustomerPojo, referencesCustomerInfo);		
		return referencesCustomerInfo;
	}

	private ReferencesFriendInfo referencesFriendPojoToReferencesFriendInfo(ReferencesFriendPojo referencesFriendPojo) {
		logger.info("**** The control is inside the referencesFriendPojoToReferencesFriendInfo in BookingFormMapper *****");
		ReferencesFriendInfo referencesFriendInfo = new ReferencesFriendInfo();
		BeanUtils.copyProperties(referencesFriendPojo, referencesFriendInfo);		
		return referencesFriendInfo;
	}

	private ChanelPartnerInfo chanelPartnerPojoToChanelPartnerInfo(ChannelPartnerMasterPojo channelPartnerMasterPojo) {
		logger.info("**** The control is inside the chanelPartnerPojoToChanelPartnerInfo in BookingFormMapper *****");
		ChanelPartnerInfo chanelPartnerInfo = new ChanelPartnerInfo();
		BeanUtils.copyProperties(channelPartnerMasterPojo, chanelPartnerInfo);		
		return chanelPartnerInfo;
	}

	private ReferencesMappingInfo referencesMappingPojoToReferencesMappingInfo(
			ReferencesMappingPojo referencesMappingPojo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		ReferencesMappingInfo referencesMappingInfo = new ReferencesMappingInfo();
		BeanUtils.copyProperties(referencesMappingPojo, referencesMappingInfo);		
		return referencesMappingInfo;
	}

	public List<FlatBookingInfo> flat$flatDetails$flatCost$floor$block$sitePojos$aminitiesInfraCostPojosToFlatBookingInfo(
			List<FlatPojo> flatPojos, List<FlatDetailsPojo> flatDetailsPojos, List<FlatCostPojo> flatCostPojos,
			List<FloorPojo> floorPojos, List<BlockPojo> blockPojos, List<SitePojo> sitePojos, BookingFormServiceDao bookingFormServiceDaoImpl
			, BookingFormRequest bookingFormRequest2) {//BHK_ID flatDetailsPojos
		String requestUrl = bookingFormRequest2.getRequestUrl()==null?"":bookingFormRequest2.getRequestUrl();
		List<FlatBookingInfo> flatBookingInfos = new ArrayList<>();
		for (int i = 0; i < flatPojos.size(); i++) {
			FlatBookingInfo flatBookingInfo = new FlatBookingInfo();
			FlatInfo flatInfo = new FlatInfo();
			FlatCostInfo flatCostInfo = new FlatCostInfo();
			FloorInfo floorInfo = new FloorInfo();
			BlockInfo blockInfo = new BlockInfo();
			SiteInfo siteInfo = new SiteInfo();
			List<AminitiesInfraCostInfo> AminitiesInfraCostInfos = new ArrayList<>();

			if(flatPojos!=null&&flatPojos.size()>i)
				BeanUtils.copyProperties(flatPojos.get(i), flatInfo);
			if(flatCostPojos!=null&&flatCostPojos.size()>i)
				BeanUtils.copyProperties(flatCostPojos.get(i), flatCostInfo);
			if(floorPojos!=null&&floorPojos.size()>i)
				BeanUtils.copyProperties(floorPojos.get(i), floorInfo);
			if(blockPojos!=null&&blockPojos.size()>i)
				BeanUtils.copyProperties(blockPojos.get(i), blockInfo);
			if(sitePojos!=null&&sitePojos.size()>i)
				BeanUtils.copyProperties(sitePojos.get(i), siteInfo);
			
			BookingFormRequest bookingFormRequest = new BookingFormRequest();
			bookingFormRequest.setFlatCostId(flatCostInfo.getFlatCostId());
			List<AminitiesInfraCostPojo> aminitiesInfraCostPojos = null;
			if(requestUrl.equals("generateBookingAllotmentLetter1")) {
				aminitiesInfraCostPojos = new ArrayList<>();
			} else {
				aminitiesInfraCostPojos = bookingFormServiceDaoImpl.getAminitiesInfraCost(bookingFormRequest);	
			}
			
			if(aminitiesInfraCostPojos!=null&&aminitiesInfraCostPojos.size()>0) {
				for(AminitiesInfraCostPojo aminitiesInfraCostPojo : aminitiesInfraCostPojos) {
					AminitiesInfraCostInfo aminitiesInfraCostInfo = new AminitiesInfraCostInfo();
					BeanUtils.copyProperties(aminitiesInfraCostPojo, aminitiesInfraCostInfo);
					bookingFormRequest.setAminititesInfraFlatWiseId(aminitiesInfraCostPojo.getAminititesInfraFlatWiseId());
					List<AminititesInfraFlatWisePojo> aminititesInfraFlatWisePojos = bookingFormServiceDaoImpl.getAminitiesInfraFlatWise(bookingFormRequest);
					if(aminititesInfraFlatWisePojos!=null && aminititesInfraFlatWisePojos.size()>0) {
						bookingFormRequest.setAminititesInfraSiteWiseId(aminititesInfraFlatWisePojos.get(0).getAminititesInfraSiteWiseId());
						List<AminitiesInfraSiteWisePojo> aminitiesInfraSiteWisePojos = bookingFormServiceDaoImpl.getAminitiesInfraSiteWise(bookingFormRequest);
						if(aminitiesInfraSiteWisePojos!=null && aminitiesInfraSiteWisePojos.size()>0) {
							bookingFormRequest.setAminititesInfraId(aminitiesInfraSiteWisePojos.get(0).getAminititesInfraId());
							List<AminitiesInfraMasterPojo> aminitiesInfraMasterPojos = bookingFormServiceDaoImpl.getAminitiesInfraMaster(bookingFormRequest);
							if(aminitiesInfraMasterPojos!=null && aminitiesInfraMasterPojos.size()>0) {
								aminitiesInfraCostInfo.setAminititesInfraId(aminitiesInfraMasterPojos.get(0).getAminititesInfraId());
								aminitiesInfraCostInfo.setAminititesInfraName(aminitiesInfraMasterPojos.get(0).getAminititesInfraName());
							}
						}
					}
					AminitiesInfraCostInfos.add(aminitiesInfraCostInfo);
				}
			}
			else {
				AminitiesInfraCostInfos.add(new AminitiesInfraCostInfo());
			}
			if(flatDetailsPojos!=null&&flatDetailsPojos.size()>i) {
				BeanUtils.copyProperties(flatDetailsPojos.get(i), flatBookingInfo);
				flatBookingInfo.setNumberOfBeds(flatDetailsPojos.get(i).getNumberOfBeds()==null?"0":String.valueOf(flatDetailsPojos.get(i).getNumberOfBeds()));
			}

			flatBookingInfo.setFlatInfo(flatInfo);
			flatBookingInfo.setAminitiesInfraCostInfo(AminitiesInfraCostInfos);
			flatBookingInfo.setFlatCost(flatCostInfo);
			flatBookingInfo.setFloorInfo(floorInfo);
			flatBookingInfo.setBlockInfo(blockInfo);
			flatBookingInfo.setSiteInfo(siteInfo);
			//flatBookingInfo.setIsEOAApplicable(flatBookingInfo.getEoiApplicable());

			flatBookingInfos.add(flatBookingInfo);
		}
		return flatBookingInfos;
	}

	public CheckListCRMInfo checklistCrmPojoToCheckListCRM(ChecklistCrmPojo checklistCrm) {
		logger.info("***** The control is inside the customerOtherDetailspojoToCustomerOtherDetailsInfo in  BookingFormMapper ******");
		CheckListCRMInfo checkListCRMInfo = new CheckListCRMInfo();
		BeanUtils.copyProperties(checklistCrm, checkListCRMInfo);
		return checkListCRMInfo;
	}

	public CheckListLegalOfficerInfo checkListLegalOfficerPojoToCheckListLegalOfficer(
			CheckListLegalOfficerPojo checkListLegalOfficer) {
		logger.info("***** The control is inside the customerOtherDetailspojoToCustomerOtherDetailsInfo in  BookingFormMapper ******");
		CheckListLegalOfficerInfo checkListLegalInfo = new CheckListLegalOfficerInfo();
		BeanUtils.copyProperties(checkListLegalOfficer, checkListLegalInfo);
		return checkListLegalInfo;
	}

	public CheckListRegistrationInfo checkListRegistrationPojoToCheckListRegistration(
			CheckListRegistrationPojo checkListRegistration) {
		logger.info("***** The control is inside the customerOtherDetailspojoToCustomerOtherDetailsInfo in  BookingFormMapper ******");
		CheckListRegistrationInfo checkListRegInfo = new CheckListRegistrationInfo();
		BeanUtils.copyProperties(checkListRegistration, checkListRegInfo);
		return checkListRegInfo;
	}

	public CustomerCheckListVerificationInfo CustChecklistVerification$ChecklistDepartmentMapping$CheckListPojosToCustomerCheckListVerification(
			CustChecklistVerificationPojo crmChecklistVerificationPojo,
			ChecklistDepartmentMappingPojo crmCheckListDepartmentMappingPojo, CheckListPojo crmCheckListPojo) {
		
		logger.info("***** The control is inside the CustChecklistVerification$ChecklistDepartmentMapping$CheckListPojosToCustomerCheckListVerification in  BookingFormMapper ******");
		
		CustomerCheckListVerificationInfo customerCheckListVerificationInfo = new CustomerCheckListVerificationInfo();
		BeanUtils.copyProperties(crmChecklistVerificationPojo, customerCheckListVerificationInfo);
		
		CheckListDepartmentMappingInfo checkListDepartmentMappingInfo = new CheckListDepartmentMappingInfo();
		BeanUtils.copyProperties(crmCheckListDepartmentMappingPojo, checkListDepartmentMappingInfo);
		
		CheckListInfo checkListInfo = new CheckListInfo();
		BeanUtils.copyProperties(crmCheckListPojo, checkListInfo);
		
		checkListDepartmentMappingInfo.setCheckListInfo(getStatus(crmCheckListDepartmentMappingPojo.getStatusId()));
		//customerCheckListVerificationInfo.setCheckListDepartmentMappingInfo(checkListDepartmentMappingInfo);
		customerCheckListVerificationInfo.setCheckListInfo(checkListInfo);
		customerCheckListVerificationInfo.setCheckListStatus(getStatusDesc(customerCheckListVerificationInfo.getIs_verified()));
		customerCheckListVerificationInfo.setIs_verified(crmChecklistVerificationPojo.getIs_verified());
		
		return customerCheckListVerificationInfo;
	}



	public CheckListSalesHeadInfo checklistSalesPojoToCheckListSales(CheckListSalesHeadPojo checkListSalesHead) {
		logger.info("***** The control is inside the customerOtherDetailspojoToCustomerOtherDetailsInfo in  BookingFormMapper ******");
		CheckListSalesHeadInfo checkListSalesInfo = new CheckListSalesHeadInfo();
		BeanUtils.copyProperties(checkListSalesHead, checkListSalesInfo);
		return checkListSalesInfo;
	}
	
	
	public ProfessionalDetailsPojo CustomerProfessionalInfoToCustomerProfessionalPOJO(ProfessionalInfo professionalInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		ProfessionalDetailsPojo professionalDetailsPojo = new ProfessionalDetailsPojo();
		BeanUtils.copyProperties(professionalInfo, professionalDetailsPojo);
		professionalDetailsPojo.setStatusId(Status.ACTIVE.getStatus());
		professionalDetailsPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return professionalDetailsPojo;	
	}

	public CustomerPojo CustomerInfoToCustomerPojo(CustomerInfo customerInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CustomerPojo customerPojo = new CustomerPojo();
		BeanUtils.copyProperties(customerInfo, customerPojo);
		/* if customer date of birth is 01-01-1971 replace with null. */
		if(Util.isNotEmptyObject(customerPojo.getDob())) {
			Date dob = new Date(customerPojo.getDob().getTime());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dob); 
			dob = TimeUtil.removeHoursMinutes(calendar).getTime();
			Date date = new GregorianCalendar(1971, Calendar.JANUARY, 1).getTime();
			//Date date = new GregorianCalendar(2020, Calendar.MARCH, 16).getTime();
			if(dob.equals(date)) {
				customerPojo.setDob(null);
	        }
		}
		customerPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		customerPojo.setStatusId(Status.PENDING.getStatus());
		return customerPojo;
	}
	
	public FlatBookingPojo FlatBookingInfoToFlatBookingPojo(FlatBookingInfo flatBookingInfo) {
		logger.info("**** The control is inside the FlatBookingInfoToFlatBookingPojo in BookingFormMapper *****");
		FlatBookingPojo flatBookingPojo = new FlatBookingPojo();
		BeanUtils.copyProperties(flatBookingInfo, flatBookingPojo);//added
		flatBookingPojo.setRegistrationDate(flatBookingInfo.getRegistrationDate());
		flatBookingPojo.setCustomerId(flatBookingInfo.getCustomerId());
		flatBookingPojo.setFlatBookingId(flatBookingInfo.getFlatBookingId());
		if(Util.isEmptyObject(flatBookingInfo.getMilestoneDueDays())) {
			flatBookingPojo.setMilestoneDueDays(0l);
		}
		flatBookingPojo.setStatusId(Status.ACTIVE.getStatus());
		return flatBookingPojo;
	}

	public CustBookInfoPojo custBookInfoToCustBookInfoPojo(CustomerBookingInfo customerBookingInfo, BookingFormServiceDao bookingFormServiceDaoImpl, Long flatBookId, Long customerId, Long professionId) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CustBookInfoPojo custBookInfoPojo = new CustBookInfoPojo();
		BeanUtils.copyProperties(customerBookingInfo, custBookInfoPojo);
		EmployeePojo custSalesTeamEmpPojo = null;
		List<EmployeePojo> custSalesTeamEmpPojos = null;
		
		if(Util.isNotEmptyObject(customerBookingInfo.getSalesTeamEmpName())) {
		 custSalesTeamEmpPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingInfo.getSalesTeamEmpName(), null,new Department[] {Department.SALES,Department.MANAGEMENT});
		}
		
		if(Util.isNotEmptyObject(custSalesTeamEmpPojos)){
			 custSalesTeamEmpPojo = custSalesTeamEmpPojos.get(0);
		}
		
		//EmployeeDetailsPojo custSalesTeamHeadEmpPojo = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingInfo.getSalesTeamLeadName(), Department.SALES).get(0);
		custBookInfoPojo.setFlatBookId(flatBookId);
		custBookInfoPojo.setCustId(customerId);
		custBookInfoPojo.setSalesTeamEmpId(Util.isNotEmptyObject(custSalesTeamEmpPojo)?Util.isNotEmptyObject(custSalesTeamEmpPojo.getEmployeeId())?custSalesTeamEmpPojo.getEmployeeId():0l:0l);
		custBookInfoPojo.setSalesTeamLeadId(customerBookingInfo.getSalesTeamLeadId());
		
		if(Util.isNotEmptyObject(customerBookingInfo.getTdsAuthorizationType())) {
		custBookInfoPojo.setTdsAuthorizationId(bookingFormServiceDaoImpl.getTdsAuthorizationMaster(customerBookingInfo.getTdsAuthorizationType()).get(0).getTdsAuthorizationId());
		}else {
		custBookInfoPojo.setTdsAuthorizationId(0l);
		}
		custBookInfoPojo.setCustProffisionalId(professionId);
		custBookInfoPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		custBookInfoPojo.setStatusId(Status.PENDING.getStatus());
		return custBookInfoPojo;
	}
	
	public CoAppBookInfoPojo coAppBookInfoToCoAppBookInfoPojo(CoApplicentBookingInfo coApplicentBookingInfo, Long custBookInfoId, Long coAppId, Long coAppProfessionId,CoApplicentDetailsInfo coApplicentDetailsInfo,Long ... status) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CoAppBookInfoPojo coAppBookInfoPojo = new CoAppBookInfoPojo();
		BeanUtils.copyProperties(coApplicentBookingInfo, coAppBookInfoPojo);
		coAppBookInfoPojo.setCoApplicantId(coAppId);
		coAppBookInfoPojo.setCustBookInfoId(custBookInfoId);
		coAppBookInfoPojo.setCustProffisionalId(coAppProfessionId);
		coAppBookInfoPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		if(Util.isNotEmptyObject(coApplicentBookingInfo.getActionUrl()) && coApplicentBookingInfo.getActionUrl().equalsIgnoreCase("updateCoApplicantData")) {
			if(status!=null && status.length>0) {
			coAppBookInfoPojo.setStatusId(status[0]);
			}
		}else {
			coAppBookInfoPojo.setStatusId(Status.PENDING.getStatus());
		}
		coAppBookInfoPojo.setType(getMetadataId(Util.isNotEmptyObject(coApplicentDetailsInfo.getAddressInfos())?Util.isNotEmptyObject(coApplicentDetailsInfo.getAddressInfos().get(0).getAddressMappingType())?Util.isNotEmptyObject(coApplicentDetailsInfo.getAddressInfos().get(0).getAddressMappingType().getMetaType())?coApplicentDetailsInfo.getAddressInfos().get(0).getAddressMappingType().getMetaType():"N/A":"N/A":"N/A"));
		return coAppBookInfoPojo;
	}
	
	public CustomerInfo CustomerPojoToCustomerInfo(CustomerPojo customerPojo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CustomerInfo customerInfo = new CustomerInfo();
		customerPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		//customerPojo.setStatusId(Status.ACTIVE.getStatus());
		BeanUtils.copyProperties(customerPojo, customerInfo);
		return customerInfo;
	}

	public PoaHolderPojo POADetailsInfoToPoaHolderPojo(POADetailsInfo objPOADetailsInfo, Long customerOtherDetailsId) {
		logger.info("**** The control is inside the POADetailsInfoToPoaHolderPojo in BookingFormMapper *****");
		PoaHolderPojo poaHolderPojo = new PoaHolderPojo();
		BeanUtils.copyProperties(objPOADetailsInfo, poaHolderPojo);
		poaHolderPojo.setStatusId(Status.ACTIVE.getStatus());
		poaHolderPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		poaHolderPojo.setCustOtherDetailsId(customerOtherDetailsId);
		//poaHolderPojo.setAddress1(objPOADetailsInfo.getAddressOfPOA());
		//poaHolderPojo.setStateId(getStateId(objPOADetailsInfo.getStateOfPOA()));
		poaHolderPojo.setStateId(objPOADetailsInfo.getStateId());
		//poaHolderPojo.setCityId(getCityId(objPOADetailsInfo.getCityOfPOA()));
		poaHolderPojo.setCityId(objPOADetailsInfo.getCityId());
		poaHolderPojo.setCountry(objPOADetailsInfo.getCountry());
		poaHolderPojo.setCountryId(objPOADetailsInfo.getCountryId());
		poaHolderPojo.setCity(Util.isNotEmptyObject(objPOADetailsInfo.getCityOfPOA())?objPOADetailsInfo.getCityOfPOA():"");
		return poaHolderPojo;	
	}	
	
	public POADetailsInfo poaHolderPojoToPoaHolderInfo(PoaHolderPojo objPOADetailsPojo) {
		logger.info("**** The control is inside the poaHolderPojoToPoaHolderInfo in BookingFormMapper *****");
		POADetailsInfo poaDetailsInfo = new POADetailsInfo();
		BeanUtils.copyProperties(objPOADetailsPojo, poaDetailsInfo);
		poaDetailsInfo.setStateOfPOA(getStateName(objPOADetailsPojo.getStateId()));
		//poaDetailsInfo.setCityOfPOA(getCityName(objPOADetailsPojo.getCityId()));
		poaDetailsInfo.setCityOfPOA(objPOADetailsPojo.getCity());
		return poaDetailsInfo;	
	}


	public CustomerOtherDetailspojo CustomerOtherDetailInfoToCustomerOtherDetailspojo(CustomerOtherDetailsInfo customerOtherDetailsInfo, Long flatBookId, EmployeeTicketDao employeeTicketDaoImpl, EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		logger.info("**** The control is inside the CustomerOtherDetailInfoToCustomerOtherDetailspojo in BookingFormMapper *****");
		CustomerOtherDetailspojo customerOtherDetailspojo = new CustomerOtherDetailspojo();
		BeanUtils.copyProperties(customerOtherDetailsInfo, customerOtherDetailspojo);
		if(Util.isNotEmptyObject(customerOtherDetailsInfo.getHaveYouOwnedSumadhuraHome())?customerOtherDetailsInfo.getHaveYouOwnedSumadhuraHome().equalsIgnoreCase("yes") || customerOtherDetailsInfo.getHaveYouOwnedSumadhuraHome().equalsIgnoreCase("true"):false) {
			employeeTicketRequestInfo.setSiteName(customerOtherDetailsInfo.getHaveYouOwnedSumadhuraHomeIfYesProjectName());
			employeeTicketRequestInfo.setFlatNo(customerOtherDetailsInfo.getHaveYouOwnedSumadhuraHomeIfYesUnitNo());
			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = employeeTicketDaoImpl.getCustomerPropertyDetails(employeeTicketRequestInfo, Status.ACTIVE);
			if(customerPropertyDetailsPojos!=null && customerPropertyDetailsPojos.size()>0)
				customerOtherDetailspojo.setExistedFlatBookId(customerPropertyDetailsPojos.get(0).getFlatBookingId());
		}
		customerOtherDetailspojo.setFlatBookId(flatBookId);
		customerOtherDetailspojo.setStatusId(Status.ACTIVE.getStatus());
		customerOtherDetailspojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return customerOtherDetailspojo;	
	}

	public AddressPojo CustomerAddressInfoToCustomerAddresspojo(AddressInfo addressInfo) {
		logger.info("**** The control is inside the CustomerAddressInfoToCustomerAddresspojo in BookingFormMapper *****");
		AddressPojo addressPojo = new AddressPojo();
		Long cityId = getCityId(addressInfo.getCity());
		Long stateId = getStateId(addressInfo.getState());
		addressInfo.setStateId(stateId);
		addressInfo.setCityId(cityId);
		BeanUtils.copyProperties(addressInfo, addressPojo);
		addressPojo.setStatusId(Status.ACTIVE.getStatus());
		addressPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return addressPojo;	
	}

	public AddressMappingPojo AddressMappingInfoToAddressMappingPojo(AddressMappingInfo addressMappingInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		AddressMappingPojo addressMappingPojo = new AddressMappingPojo();
		BeanUtils.copyProperties(addressMappingInfo, addressMappingPojo);
		addressMappingPojo.setStatusId(Status.ACTIVE.getStatus());
		addressMappingPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return addressMappingPojo;	
	}

	public ReferencesCustomerPojo ReferencesCustomerToReferencesCustomerPojo(ReferencesCustomerInfo referencesCustomer) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		ReferencesCustomerPojo referencesCustomerPojo = new ReferencesCustomerPojo();
		BeanUtils.copyProperties(referencesCustomer, referencesCustomerPojo);
		return referencesCustomerPojo;	
	}

	public ReferencesFriendPojo ReferencesFriendInfoToReferencesFriendPojo(ReferencesFriendInfo referencesFriendInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		ReferencesFriendPojo referencesFriendPojo = new ReferencesFriendPojo();
		BeanUtils.copyProperties(referencesFriendInfo, referencesFriendPojo);		
		return referencesFriendPojo;	
	}

	public ReferencesMappingPojo ReferencesMappingInfoToReferencesMappingPojo(ReferencesMappingInfo referencesMappingInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		ReferencesMappingPojo referencesMappingPojo = new ReferencesMappingPojo();
		BeanUtils.copyProperties(referencesMappingInfo, referencesMappingPojo);	
		referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustOtherId());
		return referencesMappingPojo;	
	}

	public CustomerApplicationPojo CustomerApplicationInfoToCustomerApplicationPojo(CustomerApplicationInfo customerApplicationInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CustomerApplicationPojo customerApplicationPojo = new CustomerApplicationPojo();
		BeanUtils.copyProperties( customerApplicationInfo, customerApplicationPojo);	

		customerApplicationPojo.setStatusId(Status.ACTIVE.getStatus());
		customerApplicationPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		//	referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());
		return customerApplicationPojo;	
	}

	public CoApplicantPojo CoApplicentInfoToCoApplicentPojo(CoApplicentInfo coApplicentInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CoApplicantPojo coApplicantPojo = new CoApplicantPojo();
		BeanUtils.copyProperties(coApplicentInfo, coApplicantPojo);	
		coApplicantPojo.setStatusId(Status.PENDING.getStatus());
		coApplicantPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		//	referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());
		return coApplicantPojo;	
	}
	
	public Co_ApplicantPojo Co_ApplicentInfoToCo_ApplicentPojo(Co_ApplicantInfo co_ApplicantInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		Co_ApplicantPojo co_ApplicantPojo = new Co_ApplicantPojo();
		BeanUtils.copyProperties(co_ApplicantInfo, co_ApplicantPojo);	
		co_ApplicantPojo.setStatusId(Status.PENDING.getStatus());
		co_ApplicantPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		//	referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());
		return co_ApplicantPojo;	
	}

	public CustomerKycSubmittedDocPojo CustomerKYCDocumentSubmitedInfoToCustomerKycSubmittedDocPojo(CustomerKYCDocumentSubmitedInfo customerKYCDocumentSubmitedInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CustomerKycSubmittedDocPojo customerKycSubmittedDocPojo = new CustomerKycSubmittedDocPojo();
		BeanUtils.copyProperties(customerKYCDocumentSubmitedInfo, customerKycSubmittedDocPojo);	

		customerKycSubmittedDocPojo.setStatusId(getStatusId(customerKYCDocumentSubmitedInfo.getStatus()));
		customerKycSubmittedDocPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		//	referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());
		return customerKycSubmittedDocPojo;	
	}

	public FlatBookingPojo FlatBookingInfoToFlatBookingPojo(CustomerBookingFormInfo customerBookingFormInfo, Long customerId, Long flatId,Long uploadedEmpLevelMapId,Long uploadedEmpId) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		FlatBookingInfo flatBookingInfo = customerBookingFormInfo.getFlatBookingInfo();
		FlatBookingPojo flatBookingPojo = new FlatBookingPojo();
		BeanUtils.copyProperties(flatBookingInfo, flatBookingPojo);
		flatBookingPojo.setCustomerId(customerId);
		flatBookingPojo.setFlatId(flatId);
		flatBookingPojo.setApplicationNumber(customerBookingFormInfo.getCustomerOtherDetailsInfo().getApplicationNumber());
		flatBookingPojo.setStatusId(Status.PENDING.getStatus());
		flatBookingPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		flatBookingPojo.setUploadedEmpLevelMapId(uploadedEmpLevelMapId);
		flatBookingPojo.setUploadedEmpId(uploadedEmpId);
		if(Util.isEmptyObject(flatBookingInfo.getMilestoneDueDays())) {
			flatBookingPojo.setMilestoneDueDays(0l);
		}
		flatBookingPojo.setSalesforceBookingId(flatBookingInfo.getBookingId());
		flatBookingPojo.setSalesforceOldBookingId(flatBookingInfo.getOldBookingName());
		flatBookingPojo.setNewBookingReason(flatBookingInfo.getNewBookingReason());
		return flatBookingPojo;	
	}
	
	public FlatCostPojo FlatCostInfoToFlatCostPojo(FlatCostInfo flatCostInfo, Long flatId) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		FlatCostPojo flatCostPojo = new FlatCostPojo();
		BeanUtils.copyProperties(flatCostInfo, flatCostPojo);	
		flatCostPojo.setTotalCostExcludeGst(flatCostInfo.getFlatCost());
		//flatCostPojo.setStatusId(Status.ACTIVE.getStatus());
		flatCostPojo.setStatusId(Status.PENDING.getStatus());
		flatCostPojo.setFlatId(flatId);
		flatCostPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		/* Malladi Changes */
		flatCostPojo.setFlatBookingId(flatCostInfo.getFlatBookingId());
		return flatCostPojo;	
	}


	public AminitiesInfraCostPojo AminitiesInfraCostInfoToAminitiesInfraCostPojo(AminitiesInfraCostInfo aminitiesInfraCostInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		AminitiesInfraCostPojo aminitiesInfraCostPojo = new AminitiesInfraCostPojo();

		BeanUtils.copyProperties(aminitiesInfraCostInfo, aminitiesInfraCostPojo);	
		//referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());

		aminitiesInfraCostPojo.setStatusId(Status.ACTIVE.getStatus());
		aminitiesInfraCostPojo.setCreationDate(new Timestamp(new Date().getTime()));
		return aminitiesInfraCostPojo;	
	}

	 public CustChecklistVerificationPojo CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(CustomerCheckListVerificationInfo customerCheckListVerificationInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CustChecklistVerificationPojo custChecklistVerificationPojo = new CustChecklistVerificationPojo();
		BeanUtils.copyProperties(customerCheckListVerificationInfo, custChecklistVerificationPojo);	

		//referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());
		custChecklistVerificationPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		custChecklistVerificationPojo.setIs_verified(getStatusId(customerCheckListVerificationInfo.getCheckListStatus()));
		return custChecklistVerificationPojo;	
	}
	
	public CheckListSalesHeadPojo CheckListSalesHeadInfoToCheckListSalesHeadPojo(CheckListSalesHeadInfo checkListSalesHeadInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CheckListSalesHeadPojo checkListSalesHeadPojo = new CheckListSalesHeadPojo();
		BeanUtils.copyProperties(checkListSalesHeadInfo, checkListSalesHeadPojo);	
		//referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());
		checkListSalesHeadPojo.setStatusId(Status.ACTIVE.getStatus());
		checkListSalesHeadPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return checkListSalesHeadPojo;	
	}

	public ChecklistCrmPojo CheckListCRMInfoToChecklistCrmPojo(CheckListCRMInfo checkListCRMInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		ChecklistCrmPojo checklistCrmPojo = new ChecklistCrmPojo();
		BeanUtils.copyProperties(checkListCRMInfo, checklistCrmPojo);	
		//referencesMappingPojo.setCustOtherId(referencesMappingInfo.getCustomerOtherDetailsInfo().getOtherDetailsId());
		checklistCrmPojo.setStatusId(Status.ACTIVE.getStatus());
		checklistCrmPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return checklistCrmPojo;	
	}
	
	public CoApplicantCheckListVerificationPojo CoApplicantCheckListVerificationInfoToCoApplicantCheckListVerificationPojo(CoApplicantCheckListVerificationInfo coApplicantCheckListVerificationInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CoApplicantCheckListVerificationPojo coApplicantCheckListVerificationPojo = new CoApplicantCheckListVerificationPojo();
		BeanUtils.copyProperties(coApplicantCheckListVerificationInfo, coApplicantCheckListVerificationPojo);	
		coApplicantCheckListVerificationPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		coApplicantCheckListVerificationPojo.setIs_verified(getStatusId(coApplicantCheckListVerificationInfo.getCheckListStatus()));
		return coApplicantCheckListVerificationPojo;	
	}
	
	
	public CheckListLegalOfficerPojo CheckListLegalOfficerInfoToCheckListLegalOfficerPojo(CheckListLegalOfficerInfo checkListLegalOfficerInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CheckListLegalOfficerPojo checkListLegalOfficerPojo = new CheckListLegalOfficerPojo();
		BeanUtils.copyProperties(checkListLegalOfficerInfo, checkListLegalOfficerPojo);	
		checkListLegalOfficerPojo.setStatusId(Status.ACTIVE.getStatus());
		checkListLegalOfficerPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return checkListLegalOfficerPojo;	
	}
	
	
	
	public CheckListRegistrationPojo CheckListRegistrationInfoToCheckListRegistrationPojo(CheckListRegistrationInfo checkListRegistrationInfo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CheckListRegistrationPojo checkListRegistrationPojo = new CheckListRegistrationPojo();
		BeanUtils.copyProperties(checkListRegistrationInfo, checkListRegistrationPojo);	
		
		checkListRegistrationPojo.setStatusId(Status.ACTIVE.getStatus());
		checkListRegistrationPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		
		
		return checkListRegistrationPojo;	
	}

	public CustomerApplicationInfo customerApplicationPojoToCustomerApplicationInfo(
			CustomerApplicationPojo customerApplicationPojo) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		CustomerApplicationInfo customerApplicationInfo = new CustomerApplicationInfo();
		BeanUtils.copyProperties(customerApplicationPojo, customerApplicationInfo);	
		return customerApplicationInfo;
	}

	public List<CustomerKYCDocumentSubmitedInfo> customerKycSubmittedDocPojosTocustomerKYCDocumentSubmitedInfos(
			List<CustomerKycSubmittedDocPojo> customerKycSubmittedDocPojos) {
		logger.info("***** The control is inside the customerKycSubmittedDocPojosTocustomerKYCDocumentSubmitedInfos in  BookingFormMapper ******");
		List<CustomerKYCDocumentSubmitedInfo> customerKYCDocumentSubmitedInfos = new ArrayList<>();
		for (CustomerKycSubmittedDocPojo customerKycSubmittedDocPojo : customerKycSubmittedDocPojos) {
			CustomerKYCDocumentSubmitedInfo customerKYCDocumentSubmitedInfo = new CustomerKYCDocumentSubmitedInfo();
			BeanUtils.copyProperties(customerKycSubmittedDocPojo, customerKYCDocumentSubmitedInfo);
			customerKYCDocumentSubmitedInfo.setDocName(getKYCDocumentName(customerKycSubmittedDocPojo.getDocumentId()));
			customerKYCDocumentSubmitedInfo.setStatus(getStatusDesc(customerKycSubmittedDocPojo.getStatusId()));
			customerKYCDocumentSubmitedInfos.add(customerKYCDocumentSubmitedInfo);
		}
		return customerKYCDocumentSubmitedInfos;
	}

	public CoApplicantCheckListVerificationInfo applicantCheckListVerificationPojo$ChecklistDepartmentMapping$CheckListPojosToCoApplicantCheckListVerificationInfo(
			CoApplicantCheckListVerificationPojo checklistVerificationPojo,
			ChecklistDepartmentMappingPojo checkListDepartmentMappingPojo, CheckListPojo checkListPojos, String pancard) {
		logger.info("***** The control is inside the CustChecklistVerification$ChecklistDepartmentMapping$CheckListPojosToCoApplicantCheckListVerificationInfo in  BookingFormMapper ******");
		
		CoApplicantCheckListVerificationInfo coAppCheckListVerificationInfo = new CoApplicantCheckListVerificationInfo();
		BeanUtils.copyProperties(checklistVerificationPojo, coAppCheckListVerificationInfo);
		
		CheckListDepartmentMappingInfo checkListDepartmentMappingInfo = new CheckListDepartmentMappingInfo();
		BeanUtils.copyProperties(checkListDepartmentMappingPojo, checkListDepartmentMappingInfo);
		
		CheckListInfo checkListInfo = new CheckListInfo();
		BeanUtils.copyProperties(checkListPojos, checkListInfo);
		
		checkListDepartmentMappingInfo.setCheckListInfo(getStatus(checkListDepartmentMappingPojo.getStatusId()));
		//coAppCheckListVerificationInfo.setCheckListDepartmentMappingInfo(checkListDepartmentMappingInfo);
		coAppCheckListVerificationInfo.setCheckListInfo(checkListInfo);
		coAppCheckListVerificationInfo.setCheckListStatus(getStatusDesc(checklistVerificationPojo.getIs_verified()));
		coAppCheckListVerificationInfo.setCoapplicentPanCard(pancard);
		
		return coAppCheckListVerificationInfo;
	}

	public CustomerBookingInfo custBookInfoPojoToCustomerBookingInfo(CustBookInfoPojo custBookInfoPojo) {
		logger.info("**** The control is inside the custBookInfoPojoToCustomerBookingInfo in BookingFormMapper *****");
		CustomerBookingInfo customerBookingInfo = new CustomerBookingInfo();
		BeanUtils.copyProperties(custBookInfoPojo, customerBookingInfo);	
		return customerBookingInfo;
	}

	public CoApplicentBookingInfo coAppBookInfoPojoToCoApplicentBookingInfo(CoAppBookInfoPojo coAppBookInfoPojo) {
		logger.info("**** The control is inside the coAppBookInfoPojoToCoApplicentBookingInfo in BookingFormMapper *****");
		CoApplicentBookingInfo coAppBookingInfo = new CoApplicentBookingInfo();
		BeanUtils.copyProperties(coAppBookInfoPojo, coAppBookingInfo);	
		return coAppBookingInfo;
	}

	public Co_ApplicantInfo co_ApplicantPojoToCo_ApplicantInfo(Co_ApplicantPojo co_ApplicantPojo) {
		logger.info("**** The control is inside the coAppBookInfoPojoToCoApplicentBookingInfo in BookingFormMapper *****");
		Co_ApplicantInfo co_ApplicantInfo = new Co_ApplicantInfo();
		BeanUtils.copyProperties(co_ApplicantPojo, co_ApplicantInfo);	
		return co_ApplicantInfo;
	}
	public BookingFormsListResponse customerPropertyDetailsPojosTocustomerPropertyDetailsInfos(
			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos) {
		logger.info("**** The control is inside the coAppBookInfoPojoToCoApplicentBookingInfo in BookingFormMapper *****");
		BookingFormsListResponse bookingFormsListResponse = new BookingFormsListResponse();
		List<CustomerPropertyDetailsInfo> customerPropertyDetailsInfos = new ArrayList<>();
		for(CustomerPropertyDetailsPojo customerPropertyDetailsPojo : customerPropertyDetailsPojos)
		{
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo = new CustomerPropertyDetailsInfo();
			BeanUtils.copyProperties(customerPropertyDetailsPojo, customerPropertyDetailsInfo);
			customerPropertyDetailsInfos.add(customerPropertyDetailsInfo);
		}
		bookingFormsListResponse.setApplicantInfo(customerPropertyDetailsInfos);
		return bookingFormsListResponse;
	}
	public ProfessionalDetailsPojo professionalInfoToProfessionalDetailsPojo(ProfessionalInfo professionalInfo) {
		logger.info("**** The control is inside the coAppBookInfoPojoToCoApplicentBookingInfo in BookingFormMapper *****");
		ProfessionalDetailsPojo professionalDetailsPojo = new ProfessionalDetailsPojo();
		/*  if Organization name is not empty  */
		if(Util.isNotEmptyObject(professionalInfo.getOraganizationDetails().getOrganizationTypeName())) {
		professionalInfo = getOrganiztionId(professionalInfo);
		}
		/*  if sector name is not empty  */
		if(Util.isNotEmptyObject(professionalInfo.getSectorDetailsInfo().getWorkSectorName())) {
		professionalInfo = getSectorId(professionalInfo);
		}
		/*  if work function nameis empty  */
		if(Util.isNotEmptyObject(professionalInfo.getWorkFunctionInfo().getWorkFunctionName())) {
		professionalInfo = getWorkFunctionId(professionalInfo);
		}
		BeanUtils.copyProperties(professionalInfo, professionalDetailsPojo);
		BeanUtils.copyProperties(professionalInfo.getOraganizationDetails(), professionalDetailsPojo);
		BeanUtils.copyProperties(professionalInfo.getWorkFunctionInfo(), professionalDetailsPojo);
		BeanUtils.copyProperties(professionalInfo.getSectorDetailsInfo(), professionalDetailsPojo);
		professionalDetailsPojo.setStatusId(Status.ACTIVE.getStatus());
		professionalDetailsPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return professionalDetailsPojo;
	}
	public List<AddressPojo> addressInfosToAddressPojos(List<AddressInfo> addressInfos) {
		List<AddressPojo> addressPojos = new ArrayList<>();
		for (AddressInfo addressInfo : addressInfos) {
			//Long cityId = getCityId(addressInfo.getCity());
			//addressInfo.setCityId(cityId);
			Long stateId = getStateId(addressInfo.getState());
			addressInfo.setStateId(stateId);
			AddressPojo addressPojo = new AddressPojo();
			BeanUtils.copyProperties(addressInfo, addressPojo);
			addressPojo.setCity(addressInfo.getCity());
			addressPojo.setStatusId(Status.ACTIVE.getStatus());
			addressPojo.setCreatedDate(new Timestamp(new Date().getTime()));
			addressPojos.add(addressPojo);
		}
		return addressPojos;
	}
	
	public List<AddressMappingPojo> addressInfosToAddressMappingPojos(List<AddressInfo> addressInfos, Long typeId) {
		logger.info("**** The control is inside the addressMappingInfoToAddressMappingPojo in BookingFormMapper *****");
		List<AddressMappingPojo> addressMappingPojos = new ArrayList<AddressMappingPojo>();
		for (AddressInfo addressInfo : addressInfos) {
			AddressMappingPojo addressMappingPojo = new AddressMappingPojo();
			BeanUtils.copyProperties(addressInfo.getAddressMappingType(), addressMappingPojo);
			addressMappingPojo.setType(getMetadataId(addressInfo.getAddressMappingType().getMetaType()));
			addressMappingPojo.setTypeId(typeId);
			addressMappingPojo.setStatusId(Status.ACTIVE.getStatus());
			addressMappingPojo.setCreatedDate(new Timestamp(new Date().getTime()));
			addressMappingPojos.add(addressMappingPojo);
		}
		return addressMappingPojos;
	}
	
	public Co_ApplicantPojo co_ApplicantInfoToCo_ApplicantPojo(Co_ApplicantInfo co_ApplicantInfo,Long ... status) {
		logger.info("**** The control is inside the customerToCustomerInfo in BookingFormMapper *****");
		Co_ApplicantPojo co_ApplicantPojo = new Co_ApplicantPojo();
		BeanUtils.copyProperties(co_ApplicantInfo, co_ApplicantPojo);
		co_ApplicantPojo.setCreatedDate(new Timestamp(new Date().getTime()));
      if(Util.isNotEmptyObject(co_ApplicantInfo.getActionUrl()) && co_ApplicantInfo.getActionUrl().equalsIgnoreCase("updateCoApplicantData")) {
    	if(status!=null && status.length>0) { 
    	  co_ApplicantPojo.setStatusId(status[0]);
    	}
      }else {
			co_ApplicantPojo.setStatusId(Status.PENDING.getStatus());
		}
		return co_ApplicantPojo;
	}
	
	public static Long getMetadataId(String metadata) {
		if (metadata != null) {
			MetadataId metaArray[] = MetadataId.values();
			for (MetadataId key : metaArray) {
				if (key.getName().equalsIgnoreCase(metadata)) {
					return key.getId();
				}
			}
		}
		return 0L;
	}
	
	public String getMetadataName(Long metadataId) {
		if (metadataId != null) {
			MetadataId metaArray[] = MetadataId.values();
			for (MetadataId key : metaArray) {
				if (key.getId() == metadataId || key.getId().equals(metadataId)) {
					return key.getName();
				}
			}
		}
		return "";
	}

	public String getCityName(Long cityId) {
		if (cityId != null) {
			City Aarray[] = City.values();
			for (City key : Aarray) {
				if (key.getId() == cityId || cityId.equals(key.getId())) {
					return key.getName();
				}
			}
		}
		return "";
	}

	public String getStateName(Long stateId) {
		if (stateId != null) {
			State Aarray[] = State.values();
			for (State key : Aarray) {
				if (key.getId() == stateId || stateId.equals(key.getId())) {
					return key.getName();
				}
			}
		}
		return "";
	}

	@SuppressWarnings("unlikely-arg-type")
	public String getSectorName(Long sectorId) {
		if (sectorId != null) {
			Sector Aarray[] = Sector.values();
			for (Sector key : Aarray) {
				if (key.getId() == sectorId || sectorId.equals(key.getId())) {
					return key.getName();
				}
			}
		}
		return "";
	}

	public String getWorkFunctionName(Long workFunctionId) {
		if (workFunctionId != null) {
			WorkFunction Aarray[] = WorkFunction.values();
			for (WorkFunction key : Aarray) {
				if (key.getId() == workFunctionId || workFunctionId.equals(key.getId())) {
					return key.getName();
				}
			}
		}
		return "";
	}

	public String getOrganizationName(Long organizationId) {
		if (organizationId != null) {
			Organization Aarray[] = Organization.values();
			for (Organization key : Aarray) {
				if (key.getId() == organizationId || organizationId.equals(key.getId())) {
					return key.getName();
				}
			}
		}
		return "";
	}

	public String getStatusDesc(Long is_verified) {
		if (is_verified != null) {
			Status Aarray[] = Status.values();
			for (Status key : Aarray) {
				if (key.getStatus() == is_verified || is_verified.equals(key.getStatus())) {
					return key.getDescription();
				}
			}
		}
		return "";
	}
	
	public Long getStatusId(String status_desc) {
		if (status_desc != null) {
			Status Aarray[] = Status.values();
			for (Status key : Aarray) {
				if (status_desc.equalsIgnoreCase(key.getDescription())) {
					return key.getStatus();
				}
			}
		}
		return 0L;
	}

	public Status getStatus(Long statusId) {
		if (statusId != null) {
			Status Aarray[] = Status.values();
			for (Status key : Aarray) {
				if (key.getStatus() == statusId || statusId.equals(key.getStatus())) {
					return key;
				}
			}
		}
		return null;
	}

	public String getKYCDocumentName(Long documentId) {
		if (documentId != null) {
			KYCDocuments Aarray[] = KYCDocuments.values();
			for (KYCDocuments key : Aarray) {
				if (key.getId() == documentId || documentId.equals(key.getId())) {
					return key.getName();
				}
			}
		}
		return "";
	}

	public String getSiteState(String siteName) {
		if (siteName != null) {
			SiteState Aarray[] = SiteState.values();
			for (SiteState key : Aarray) {
				if (key.getName().equalsIgnoreCase(siteName)) {
					return key.getId();
				}
			}
		}
		return "";
	}
	
	public String getCKName(Long ckId) {
		if (ckId != null) {
			CheckList Aarray[] = CheckList.values();
			for (CheckList key : Aarray) {
				if (key.getId() == ckId || ckId.equals(key.getId())) {
					return key.getName();
				}
			}
		}
		return "";
	}

	public Long getCKId(String ckName) {
		if (ckName != null) {
			CheckList Aarray[] = CheckList.values();
			for (CheckList key : Aarray) {
				if (key.getName().equalsIgnoreCase(ckName)) {
					return key.getId();
				}
			}
		}
		return 0L;
	}
	
	public Long getCityId(String cityName) {
		if (cityName != null) {
			City Aarray[] = City.values();
			for (City key : Aarray) {
				if (key.getName().equalsIgnoreCase(cityName)) {
					return key.getId();
				}
			}
		}
		return 0L;
	}
	


	public Long getStateId(String stateName) {
		if (stateName != null) {
			State Aarray[] = State.values();
			for (State key : Aarray) {
				if (key.getName().equalsIgnoreCase(stateName)) {
					return key.getId();
				}
			}
		}
		return 0L;
	}

	public Long getRefrenceId(String refrenceName) {
		if (refrenceName != null) {
			Refrences Aarray[] = Refrences.values();
			for (Refrences key : Aarray) {
				if (key.getName().equalsIgnoreCase(refrenceName)) {
					return key.getId();
				}
			}
		}
		/*  If references Name is not given by default it is Hoarding type 
		else {
			return Refrences.HOARDING.getId();
		} */
		return 0L;
	}

	public ProfessionalInfo getCoapplicantOrganiztionId(ProfessionalInfo professionalInfo) {
		Long longOrganizationId = 0L;
		Organization Aarray[] = Organization.values();
		for (Organization key : Aarray) {
			if (key.getName().equalsIgnoreCase(professionalInfo.getOraganizationDetails().getOrganizationTypeName())) {
				longOrganizationId = key.getId();
				break;
			}
		}
		if (longOrganizationId != 0) {
			professionalInfo.getOraganizationDetails().setOrganizationTypeId(longOrganizationId);
		} else {
			professionalInfo.getOraganizationDetails()
					.setIfOtherOrgTypeName(professionalInfo.getOraganizationDetails().getIfOtherOrgTypeName());
		}
		return professionalInfo;
	}

	public ProfessionalInfo getCoapplicantSectorId(ProfessionalInfo professionalInfo) {
		Long longSectorId = 0L;
		Organization Aarray[] = Organization.values();
		for (Organization key : Aarray) {
			if (key.getName().equalsIgnoreCase(professionalInfo.getSectorDetailsInfo().getWorkSectorName())) {
				longSectorId = key.getId();
				break;
			}
		}
		if (longSectorId != 0) {
			professionalInfo.getSectorDetailsInfo().setWorkSectorId(longSectorId);
		} else {
			professionalInfo.getSectorDetailsInfo()
					.setIfOtherWorkSectorName(professionalInfo.getSectorDetailsInfo().getIfOtherWorkSectorName());
		}
		return professionalInfo;
	}

	public ProfessionalInfo getCoapplicentWorkFunctionId(ProfessionalInfo professionalInfo) {
		Long longWorkFunctionId = 0L;
		Organization Aarray[] = Organization.values();
		for (Organization key : Aarray) {
			if (key.getName().equalsIgnoreCase(professionalInfo.getWorkFunctionInfo().getWorkFunctionName())) {
				longWorkFunctionId = key.getId();
				break;
			}
		}
		if (longWorkFunctionId != 0) {
			professionalInfo.getWorkFunctionInfo().setWorkFunctionId(longWorkFunctionId);
		} else {
			professionalInfo.getWorkFunctionInfo()
					.setIfOtherworkFunctionName(professionalInfo.getWorkFunctionInfo().getIfOtherworkFunctionName());
		}
		return professionalInfo;
	}

	public Long getKYCDocumentId(String documentName) {
		if (documentName != null) {
			KYCDocuments Aarray[] = KYCDocuments.values();
			for (KYCDocuments key : Aarray) {
				if (key.getName().equalsIgnoreCase(documentName)) {
					return key.getId();
				}
			}
		}
		return 0L;
	}

	public Long getDepartmentId(String departmentName) {
		if (departmentName != null) {
			Department Aarray[] = Department.values();
			for (Department key : Aarray) {
				if (key.getName().equalsIgnoreCase(departmentName)) {
					return key.getId();
				}
			}
		}
		return 0L;
	}

	public ProfessionalInfo getOrganiztionId(ProfessionalInfo professionalInfo) {
		Long longOrganizationId = 0L;
		Organization Aarray[] = Organization.values();
		for (Organization key : Aarray) {
			if (key.getName().equalsIgnoreCase(professionalInfo.getOraganizationDetails().getOrganizationTypeName())) {
				longOrganizationId = key.getId();
				break;
			}
		}
		if (longOrganizationId != 0) {
			professionalInfo.getOraganizationDetails().setOrganizationTypeId(longOrganizationId);
		} else {
			professionalInfo.getOraganizationDetails().setIfOtherOrgTypeName(professionalInfo.getOraganizationDetails().getOrganizationTypeName());
		}
		return professionalInfo;
	}

	public ProfessionalInfo getSectorId(ProfessionalInfo professionalInfo) {
		Long longSectorId = 0L;
		Sector Aarray[] = Sector.values();
		for (Sector key : Aarray) {
			if (key.getName().equalsIgnoreCase(professionalInfo.getSectorDetailsInfo().getWorkSectorName())) {
				longSectorId = ((Integer)key.getId()).longValue();
				break;
			}
		}
		if (longSectorId != 0) {
			professionalInfo.getSectorDetailsInfo().setWorkSectorId(longSectorId);
		} else {
			professionalInfo.getSectorDetailsInfo()
					.setIfOtherWorkSectorName(professionalInfo.getSectorDetailsInfo().getWorkSectorName());
		}
		return professionalInfo;
	}

	public ProfessionalInfo getWorkFunctionId(ProfessionalInfo professionalInfo) {
		Long longWorkFunctionId = 0L;
		WorkFunction Aarray[] = WorkFunction.values();
		for (WorkFunction key : Aarray) {
			if (key.getName().equalsIgnoreCase(professionalInfo.getWorkFunctionInfo().getWorkFunctionName())) {
				longWorkFunctionId = key.getId();
				break;
			}
		}
		if (longWorkFunctionId != 0) {
			professionalInfo.getWorkFunctionInfo().setWorkFunctionId(longWorkFunctionId);
		} else {
			professionalInfo.getWorkFunctionInfo()
					.setIfOtherworkFunctionName(professionalInfo.getWorkFunctionInfo().getWorkFunctionName());
		}
		return professionalInfo;
	}

	public static void main(String[] args) {
		String a[] = {"Booking Date",
				"Project Name",
				"Flat Number",
				"Area of Flat",
				"Base Rate",
				"Floor Rise",
				"Amenities Facing",
				"Total Cost",
				"Contact Details",
				"DOB Applicant",
				"Anniversary Date",
				"DOB Co-Applicant"};
		BookingFormMapper mapper = new BookingFormMapper();
		for(String b:a)
		{
			System.out.println(mapper.getCKId(b));
		}
	}

	public FinBookingFormAccountSummaryPojo copyFlatCostInfoPropToFinBookingFormAccountSummaryPojo(FlatBookingInfo flatBookingInfo,
			CustomerBookingFormInfo customerBookingFormInfo) {
		logger.info("***** Control inside the BookingFormMapper.copyFlatCostInfoPropToFinBookingFormAccountSummaryPojo() *****");
		FinBookingFormAccountSummaryPojo accountSummaryPojo = new FinBookingFormAccountSummaryPojo();
		FlatCostInfo flatCost = flatBookingInfo.getFlatCost();
		Long bookingFormId = flatBookingInfo.getFlatBookingId();
		double payableAmount = (flatCost.getBasicFlatCost()==null?0d:flatCost.getBasicFlatCost()) + (flatCost.getAmenitiesFlatCost()==null?0d:flatCost.getAmenitiesFlatCost());
		accountSummaryPojo.setPayableAmount(payableAmount);
		accountSummaryPojo.setBalanceAmount(payableAmount);
		accountSummaryPojo.setFlatGstAmount(flatCost.getGstCost());
		accountSummaryPojo.setBookingFormId(bookingFormId);
		accountSummaryPojo.setType(MetadataId.PRINCIPAL_AMOUNT.getId());
		accountSummaryPojo.setStatusId(Status.ACTIVE.getStatus());
		accountSummaryPojo.setCreatedBy(customerBookingFormInfo.getEmployeeId());
		accountSummaryPojo.setModifiedBy(customerBookingFormInfo.getEmployeeId());
		return accountSummaryPojo;
	}

	public List<FlatBookingInfo> copyPojoPropertyToInfoObject(List<FlatBookingPojo> flatBookPojo,
			Class<?> targetClass) {
		List<FlatBookingInfo> flatBookingInfos = new ArrayList<FlatBookingInfo>();
		for (FlatBookingPojo pojo : flatBookPojo) {
			FlatBookingInfo info = new FlatBookingInfo();
			BeanUtils.copyProperties(pojo, info);
			flatBookingInfos.add(info);
		}
		return flatBookingInfos;
	}

	public BookingStatusChangedPojo constructBookingStatusChangedPojo(BookingFormRequest bookingFormRequest) {
		BookingStatusChangedPojo pojo = new BookingStatusChangedPojo();
		pojo.setEmpId(bookingFormRequest.getEmpId());
		pojo.setActualStatusId(bookingFormRequest.getActualStatusId());
		pojo.setChangedStatusId(bookingFormRequest.getStatusId());
		//pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setFlatBookingId(bookingFormRequest.getFlatBookingId());
		pojo.setRemarks(bookingFormRequest.getRemarks());
		return pojo;
	}

    public Map < String, Object > customerInfoKeyValuesPair(CustomerBookingFormInfo customerBookingFormInfo) {
        /*
        	List<CustomerKYCDocumentSubmitedInfo> kycSubmittedInfo = customerBookingFormInfo.getCustomerKYCSubmitedInfo();
        	CustomerOtherDetailsInfo othereDetails = customerBookingFormInfo.getCustomerOtherDetailsInfo();
        	ProfessionalInfo proInfo = customerBookingFormInfo.getProfessionalInfo();
        	CustomerApplicationInfo custAppInfo = customerBookingFormInfo.getCustomerApplicationInfo();
        	List<CustomerSchemeInfo> customerSchemeInfo = customerBookingFormInfo.getCustomerSchemeInfos();
        	FlatBookingInfo flatBookingInfo = customerBookingFormInfo.getFlatBookingInfo();
        */
		Map<String, Object> map = new HashMap<>();
		List<DynamicKeyValueInfo> firstApplicantDetails = new ArrayList<>();
		List<DynamicKeyValueInfo> secondApplicantDetails = new ArrayList<>();
		map.put("FirstApplicant", firstApplicantDetails);
		map.put("SecondApplicant", secondApplicantDetails);
		collectFirstApplicantData(firstApplicantDetails, customerBookingFormInfo);
		collectSecondApplicantData(secondApplicantDetails, customerBookingFormInfo);

        return map;
    }

	private void collectSecondApplicantData(List<DynamicKeyValueInfo> secondApplicantDetails,
			CustomerBookingFormInfo customerBookingFormInfo) {
		List<CoApplicentDetailsInfo> coApplicentDetailsInfos = customerBookingFormInfo.getCoApplicentDetails();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		//second applicant
            if (Util.isNotEmptyObject(coApplicentDetailsInfos)) {

                Co_ApplicantInfo co_ApplicantInfo = coApplicentDetailsInfos.get(0).getCo_ApplicantInfo();
                CoApplicentBookingInfo coApplicentBookingInfo = coApplicentDetailsInfos.get(0).getCoApplicentBookingInfo();
                List < AddressInfo > coApplicentAddressInfos = coApplicentDetailsInfos.get(0).getAddressInfos();

                secondApplicantDetails.add(new DynamicKeyValueInfo("Applicant name", (co_ApplicantInfo.getNamePrefix() == null ? "" : co_ApplicantInfo.getNamePrefix()) + " " + co_ApplicantInfo.getFirstName()));

                if (coApplicentBookingInfo.getPhoneNo() != null) {
                    secondApplicantDetails.add(new DynamicKeyValueInfo("Mobile Number", coApplicentBookingInfo.getPhoneNo()));
                }
                if (coApplicentBookingInfo.getEmail() != null) {
                    secondApplicantDetails.add(new DynamicKeyValueInfo("Email", coApplicentBookingInfo.getEmail()));
                }
                if (co_ApplicantInfo.getPancard() != null) {
                    secondApplicantDetails.add(new DynamicKeyValueInfo("Pan Number", co_ApplicantInfo.getPancard()));
                }
                if (co_ApplicantInfo.getAadharId() != null) {
                    secondApplicantDetails.add(new DynamicKeyValueInfo("Aadhar Number", co_ApplicantInfo.getAadharId()));
                }
                if (co_ApplicantInfo.getDateOfBirth() != null) {
                    secondApplicantDetails.add(new DynamicKeyValueInfo("Date Of Birth", co_ApplicantInfo.getDateOfBirth()));
                }

//                if (Util.isNotEmptyObject(coApplicentAddressInfos)) {
//                    if (coApplicentAddressInfos.get(0).getAddress1() != null) {
//                        secondApplicantDetails.add(new DynamicKeyValueInfo("Permanent Address", coApplicentAddressInfos.get(0).getAddress1()));
//                    }
//                    if (coApplicentAddressInfos.size() > 1 && coApplicentAddressInfos.get(1).getAddress1() != null) {
//                        secondApplicantDetails.add(new DynamicKeyValueInfo("Correspondence Address", coApplicentAddressInfos.get(1).getAddress1()));
//                    }
//                }
                if (Util.isNotEmptyObject(coApplicentAddressInfos)) {
    				for (AddressInfo address : coApplicentAddressInfos) {
    					if ("permenent".equalsIgnoreCase(address.getAddressMappingType().getAddressType())) {
    						if (address.getAddress1() != null) {
    							secondApplicantDetails.add(new DynamicKeyValueInfo("Permanent Address", address.getAddress1()));
    						}
    					}
    					if ("Correspondence".equalsIgnoreCase(address.getAddressMappingType().getAddressType())) {
    						if (coApplicentAddressInfos.size() > 1 && address.getAddress1() != null) {
    							secondApplicantDetails.add(new DynamicKeyValueInfo("Correspondence Address", address.getAddress1()));
    						}
    					}
    				}
    			}
                if (co_ApplicantInfo.getCoApplicantBookingDate() != null) {
                    secondApplicantDetails.add(new DynamicKeyValueInfo("Booking Date", co_ApplicantInfo.getCoApplicantBookingDate()));
                } 
                
            } //second applicant
        }

	private void collectFirstApplicantData(List<DynamicKeyValueInfo> firstApplicantDetails, CustomerBookingFormInfo customerBookingFormInfo) {
		CustomerInfo customerInfo = customerBookingFormInfo.getCustomerInfo();
		CustomerBookingInfo customerBookingInfo = customerBookingFormInfo.getCustomerBookingInfo();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		List<AddressInfo> addressInfos = customerBookingFormInfo.getAddressInfos();
		FlatBookingInfo flatBookingInfo = customerBookingFormInfo.getFlatBookingInfo();
		List<Map<String, Object>> customerFinancialDetails = customerBookingFormInfo.getCustomerFinancialDetails();

            //First applicant
            firstApplicantDetails.add(new DynamicKeyValueInfo("Applicant name",(customerInfo.getNamePrefix()==null?"": customerInfo.getNamePrefix()) + " " + customerInfo.getFirstName()));

            firstApplicantDetails.add(new DynamicKeyValueInfo("Mobile Number", customerBookingInfo.getPhoneNo()));

            if (customerBookingInfo.getPhoneNo() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Email", customerBookingInfo.getEmail()));
            }
            if (customerInfo.getPancard() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Pan Number", customerInfo.getPancard()));
            }
            if (customerInfo.getAdharNumber() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Aadhar Number", customerInfo.getAdharNumber()));
            }
            if (customerInfo.getDob() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Date Of Birth", customerInfo.getDob()));
            }
//            if (Util.isNotEmptyObject(addressInfos)) {
//                if (addressInfos.get(0).getAddress1() != null) {
//                    firstApplicantDetails.add(new DynamicKeyValueInfo("Permanent Address", addressInfos.get(0).getAddress1()));
//                }
//                if (addressInfos.size() > 1 && addressInfos.get(1).getAddress1() != null) {
//                    firstApplicantDetails.add(new DynamicKeyValueInfo("Correspondence Address", addressInfos.get(1).getAddress1()));
//                }
//            }
            if (Util.isNotEmptyObject(addressInfos)) {
    			for (AddressInfo address : addressInfos) {
    				if ("permenent".equalsIgnoreCase(address.getAddressMappingType().getAddressType())) {
    					if (address.getAddress1() != null) {
    						firstApplicantDetails.add(new DynamicKeyValueInfo("Permanent Address", address.getAddress1()));
    					}
    				}
    				if ("Correspondence".equalsIgnoreCase(address.getAddressMappingType().getAddressType())) {
    					if (addressInfos.size() > 1 && addressInfos.get(1).getAddress1() != null) {
    						firstApplicantDetails.add(new DynamicKeyValueInfo("Correspondence Address", address.getAddress1()));
    					}
    				}
    			}
    		}
            if (flatBookingInfo.getSalesforceBookingId() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Booking Id", flatBookingInfo.getSalesforceBookingId()));
            }else
            {
            	   firstApplicantDetails.add(new DynamicKeyValueInfo("Booking Id","-"));
            }
            if (flatBookingInfo.getBookingDate() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Booking Date", flatBookingInfo.getBookingDate()));
            }
            
            if (flatBookingInfo.getRegistrationDate() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Registration Date",formatter.format(flatBookingInfo.getRegistrationDate())));
            }else {
            	 firstApplicantDetails.add(new DynamicKeyValueInfo("Registration Date", "-"));
            }
            
            if (flatBookingInfo.getHandingOverDate() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Handover Date", formatter.format(flatBookingInfo.getHandingOverDate())));
            }else {
            	 firstApplicantDetails.add(new DynamicKeyValueInfo("Handover Date", "-"));
            }
                
            
            if (flatBookingInfo.getAgreementDate() != null) {
              //  firstApplicantDetails.add(new DynamicKeyValueInfo("Aggrement Date", flatBookingInfo.getAgreementDate()));
                firstApplicantDetails.add(new DynamicKeyValueInfo("Agreement Date", flatBookingInfo.getAgreementDate()));
            }
            
            if (flatBookingInfo.getMilestoneDueDays() != null) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Milestone Due Days", flatBookingInfo.getMilestoneDueDays()));
            }
            

            if (customerBookingInfo.getSalesTeamEmpName() != null && !customerBookingInfo.getSalesTeamEmpName().equals("N/A")) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Sales Manager Name", customerBookingInfo.getSalesTeamEmpName()));
            }
            
          
            
            
            if (Util.isNotEmptyObject(customerFinancialDetails)) {
                firstApplicantDetails.add(new DynamicKeyValueInfo("Milestone Completion %", customerFinancialDetails.get(0).get("blockCompletionPercent")));
                firstApplicantDetails.add(new DynamicKeyValueInfo("Total Pending Amount Against Total Flat Cost", customerFinancialDetails.get(0).get("totalPendingAmountAgainstTotalFlatCost")));
                firstApplicantDetails.add(new DynamicKeyValueInfo("Scheme", customerFinancialDetails.get(0).get("schemeCode")));
                //firstApplicantDetails.add(new DynamicKeyValueInfo("Total Due/Excess amount as per Milestone Completion", customerFinancialDetails.get(0).get("totalPendingAmountAsPerWorkCompletion")));
                //firstApplicantDetails.add(new DynamicKeyValueInfo("Total Amount Paid", customerFinancialDetails.get(0).get("totalAmountPaid")));
            }
        }

	public Map<String, Object> customerFlatInfoKeyValuesPair(List<FlatBookingInfo> flatBookingInfos) {
		CurrencyUtil currencyUtil = new CurrencyUtil(); 
		Map<String, Object> map = new HashMap<>();
		List<DynamicKeyValueInfo> unitDetails = new ArrayList<>();
		map.put("UnitDetails", unitDetails);
		if (Util.isNotEmptyObject(flatBookingInfos)) {
			FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
			if(flatBookingInfo!=null) {
				//these values coming as sq.ft 
				if (flatBookingInfo.getSbua() != null) {
					unitDetails.add(new DynamicKeyValueInfo("SBUA", flatBookingInfo.getSbua()));
					//Super Built Area: Sqft //super built up area
				}
				if (flatBookingInfo.getCarpetArea() != null) {
					unitDetails.add(new DynamicKeyValueInfo("Carpet Area", flatBookingInfo.getCarpetArea()));
				}
				if (flatBookingInfo.getUds() != null) {
					unitDetails.add(new DynamicKeyValueInfo("UDS", flatBookingInfo.getUds()));
				}
				if (flatBookingInfo.getBalconyArea() != null) {
					unitDetails.add(new DynamicKeyValueInfo("Balcony Area", flatBookingInfo.getBalconyArea()));
				}
				if (flatBookingInfo.getFloorInfo() != null) {
					unitDetails.add(new DynamicKeyValueInfo("Floor", flatBookingInfo.getFloorInfo().getFloorName()));
				}
				if (flatBookingInfo.getFlatInfo() != null) {
					unitDetails.add(new DynamicKeyValueInfo("Unit Number", flatBookingInfo.getFlatInfo().getFlatNo()));
				}
				if (flatBookingInfo.getBlockInfo() != null) {
					unitDetails.add(new DynamicKeyValueInfo("Block", flatBookingInfo.getBlockInfo().getName()));
				}
				if (flatBookingInfo.getNumberOfBeds() != null) {
					unitDetails.add(new DynamicKeyValueInfo("Type/Beds", flatBookingInfo.getNumberOfBeds()));
					//nothing but BHK
				} else {
					unitDetails.add(new DynamicKeyValueInfo("Type/Beds","-"));
				}
				
				// unitDetails.add(new DynamicKeyValueInfo("Garden/Terrence Area in Sqft",""));

				if (flatBookingInfo.getFacing() != null) {
					unitDetails.add(new DynamicKeyValueInfo("Facing", flatBookingInfo.getFacing()));
				}
				if(flatBookingInfo.getFlatCost()!=null) {
					FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
					if (flatCostInfo.getPerSqftCost() != null) {
						//unitDetails.add(new DynamicKeyValueInfo("Quoted Base Price", flatCostInfo.getQuotedBasePrice()));
						//--------------------------PerSqftCost is Quoted Base Price
						double getPerSqftCost = flatCostInfo.getPerSqftCost()==null?0d:flatCostInfo.getPerSqftCost();
						String perSqftCost = currencyUtil.getTheAmountWithCommas(Double.valueOf(getPerSqftCost),roundingModeSize,roundingMode);
						unitDetails.add(new DynamicKeyValueInfo("Quoted Base Price", perSqftCost));
					}
					if (flatCostInfo.getUnitGroup() != null) {
						unitDetails.add(new DynamicKeyValueInfo("Unit Group", flatCostInfo.getUnitGroup()));
					}
					
					if (flatCostInfo.getSoldBasePrice() != null) {
						
						unitDetails.add(new DynamicKeyValueInfo("Sold Base Price", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getSoldBasePrice()==null?0:flatCostInfo.getSoldBasePrice())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getBasicFlatCost() != null) {
						//
						unitDetails.add(new DynamicKeyValueInfo("Basic Flat Cost", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getBasicFlatCost()==null?0:flatCostInfo.getBasicFlatCost())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getAmenitiesFlatCost() != null) {
						//
						unitDetails.add(new DynamicKeyValueInfo("Amenities Cost",currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getAmenitiesFlatCost() ==null?0:flatCostInfo.getAmenitiesFlatCost())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getTotalCostExcludeGst() != null) {
						//
						unitDetails.add(new DynamicKeyValueInfo("Total Cost (Excl GST)", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getTotalCostExcludeGst()==null?0:flatCostInfo.getTotalCostExcludeGst())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getActualPricePerSft() != null) {
						//Total cost exclusing gst/SBUA = Actual Price Per Sft
						unitDetails.add(new DynamicKeyValueInfo("Actual Price Per Sft", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getActualPricePerSft()==null?0:flatCostInfo.getActualPricePerSft())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getGstCost() != null) {
						//
						unitDetails.add(new DynamicKeyValueInfo("Total GST Amount", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getGstCost()==null?0:flatCostInfo.getGstCost())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getTotalCost() != null) {
						//
						unitDetails.add(new DynamicKeyValueInfo("Total Agreement Cost", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getTotalCost()==null?0:flatCostInfo.getTotalCost())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getTaxesPerSft() != null) {
						//Taxes per sft on = Actual Price Per Sft
						//(Actual Price Per Sft * Scheme Percentage)/100 = Taxes Per Sft
						unitDetails.add(new DynamicKeyValueInfo("Taxes Per Sft",currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getTaxesPerSft()==null?0:flatCostInfo.getTaxesPerSft())),roundingModeSize,roundingMode)));
					}
					if (flatCostInfo.getOverallPricePerSft() != null) {
						//Agreement cost/SBUA = Overall Price Per Sft
						unitDetails.add(new DynamicKeyValueInfo("Overall Price Per Sft", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getOverallPricePerSft()==null?0:flatCostInfo.getOverallPricePerSft())),roundingModeSize,roundingMode)));
					}
				}
			}
		}
		//front end keys 20
		//back end keys 20
		return map;
	}

	public Map<String, Object> getCustomerFlatDetails(List<FlatBookingInfo> flatBookingInfos, CustomerBookingFormInfo customerBookingFormInfo) throws Exception {
		CurrencyUtil currencyUtil = new CurrencyUtil(); 
		Map<String, Object> map = new HashMap<>();
		List<String> errorMsgs = new ArrayList<>();
		errorMsgs.add("Error occured while generating welcome letter, Below are details ");
		if (Util.isNotEmptyObject(flatBookingInfos)) {
			FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
			if(flatBookingInfo!=null) {
				//these values coming as sq.ft 
				//if (flatBookingInfo.getSbua() != null) {
					map.put("SBUA", flatBookingInfo.getSbua()==null?"-":flatBookingInfo.getSbua());
					map.put("SBUA Sq.ft", flatBookingInfo.getSbua()==null?"-":flatBookingInfo.getSbua().longValue());
					if (flatBookingInfo.getSbua() != null) {
						//String value = currencyUtil.getTheAmountWithCommas(Double.valueOf((flatBookingInfo.getSbua()*0.09290304)),2,roundingMode);
						String value = currencyUtil.getTheAmountWithCommas(Double.valueOf((flatBookingInfo.getSbua()*0.09290304)),2,roundingMode.DOWN);
						map.put("SBUA Sq.Mtrs", value);
					} else {
						errorMsgs.add("- SBUA Area is missing.");
						map.put("SBUA Sq.Mtrs", "-");
					}
					//Super Built Area: Sqft
				//}
				//if (flatBookingInfo.getCarpetArea() != null) {
					map.put("Carpet Area", flatBookingInfo.getCarpetArea()==null?"":flatBookingInfo.getCarpetArea());
					map.put("Carpet Area Sq.ft", flatBookingInfo.getCarpetArea()==null?"":flatBookingInfo.getCarpetArea());
					if (flatBookingInfo.getCarpetArea() != null) {
						String value = currencyUtil.getTheAmountWithCommas(Double.valueOf((flatBookingInfo.getCarpetArea()*0.09290304)),2,roundingMode);
						map.put("Carpet Area Sq.Mtrs", value);	
					} else {
						errorMsgs.add("- Carpet Area is missing.");
						map.put("Carpet Area Sq.Mtrs", "-");
					}
				//}
					/* Malladi Changes */
					map.put("Balcony Area", flatBookingInfo.getBalconyArea()==null?"-":flatBookingInfo.getBalconyArea());
					map.put("Balcony Area Sq.ft", flatBookingInfo.getBalconyArea()==null?"-":flatBookingInfo.getBalconyArea());
					if(flatBookingInfo.getBalconyArea() !=null) {
						try {
							Double balconyAreaSqmt = Double.valueOf(flatBookingInfo.getBalconyArea())*0.09290304;
							String value = currencyUtil.getTheAmountWithCommas(balconyAreaSqmt,2,roundingMode);
							map.put("Balcony Area Sq.Mtrs", value);
						}catch (Exception e) {
							//errorMsgs.add("- Balcony Area is missing.");
							map.put("Balcony Area Sq.Mtrs", "-");
						}
					}else {
						//errorMsgs.add("- Balcony Area is missing.");
						map.put("Balcony Area Sq.Mtrs", "-");
					}
					
				//if (flatBookingInfo.getUds() != null) {
					map.put("UDS", flatBookingInfo.getUds()==null?"":flatBookingInfo.getUds());
					map.put("UDS Sq.ft", flatBookingInfo.getUds()==null?"":flatBookingInfo.getUds());
					if (flatBookingInfo.getUds() != null) {
						String value = currencyUtil.getTheAmountWithCommas(Double.valueOf((flatBookingInfo.getUds()*0.09290304)),2,roundingMode);
						map.put("UDS Sq.Mtrs", value);	
						
						value = currencyUtil.getTheAmountWithCommas(Double.valueOf((flatBookingInfo.getUds()/9)),2,roundingMode);
						map.put("UDS_Sq_yards", value);	
						
					} else {
						errorMsgs.add("- UDS value is missing.");
						map.put("UDS Sq.Mtrs", "-");
						map.put("UDS_Sq_yards", "-");		
					}//text-align: center;
				//}
				
					map.put("Proportionate Common Area Sq.ft", flatBookingInfo.getProportionateCommonArea()==null?"-":flatBookingInfo.getProportionateCommonArea());
					
				//if (flatBookingInfo.getBalconyArea() != null) {
					map.put("Balcony Area", flatBookingInfo.getBalconyArea()==null?"":flatBookingInfo.getBalconyArea());
					map.put("Balcony Area Sq.ft", flatBookingInfo.getBalconyArea()==null?"":flatBookingInfo.getBalconyArea());
					/*if (flatBookingInfo.getBalconyArea() != null) {
						String value = currencyUtil.getTheAmountWithCommas(Double.valueOf((flatBookingInfo.getBalconyArea()*0.09290304)),2,roundingMode);
						map.put("Balcony Area Sq.Mtrs", value);	
					}*/
				//}
					
				map.put("Additional Terus Area Sq.ft", flatBookingInfo.getAdditionalTerusArea()==null?"":flatBookingInfo.getAdditionalTerusArea());
				if (flatBookingInfo.getAdditionalTerusArea() != null) {
					String value = currencyUtil.getTheAmountWithCommas(Double.valueOf((flatBookingInfo.getAdditionalTerusArea()*0.09290304)),2,roundingMode);
					map.put("Additional Terus Area Sq.Mtrs", value);
				} else {
					map.put("Additional Terus Area Sq.Mtrs", "-");
				}
				
				if (flatBookingInfo.getNorthSideName() != null) {
					map.put("northSideName", flatBookingInfo.getNorthSideName());
				} else {
					map.put("northSideName", "-");
				}
				if (flatBookingInfo.getSouthSideName() != null) {
					map.put("southsideName", flatBookingInfo.getSouthSideName());
				} else {
					map.put("southsideName", "-");
				}
				if (flatBookingInfo.getEastSideName() != null) {
					map.put("eastSideName", flatBookingInfo.getEastSideName());
				} else {
					map.put("eastSideName", "-");
				}
				if (flatBookingInfo.getWestSideName() != null) {
					map.put("westSideName", flatBookingInfo.getWestSideName());
				} else {
					map.put("westSideName", "-");
				}
				
				if (flatBookingInfo.getFloorPlanLocation() != null) {
					map.put("floorPlanLocation", flatBookingInfo.getFloorPlanLocation());
				} else {
					map.put("floorPlanLocation", "-");
				}
				
				if (customerBookingFormInfo.getFlatBookingInfo()!=null && customerBookingFormInfo.getFlatBookingInfo().getCarParkingSpaces() != null) {
					map.put("carParkingSpaces", customerBookingFormInfo.getFlatBookingInfo().getCarParkingSpaces());
				} else {
					map.put("carParkingSpaces", "-");
				}
				
				if (flatBookingInfo.getCarParkingAllotmentNo() != null) {
					map.put("carParkingAllotedNumber", flatBookingInfo.getCarParkingAllotmentNo());
					map.put("noOfCarParkingsInWord", new NumberToWord().convertNumberToWords(Long.valueOf(flatBookingInfo.getCarParkingAllotmentNo())));
				} else {
					map.put("noOfCarParkingsInWord", "-");
					map.put("carParkingAllotedNumber", "-");
				}
				
				if (flatBookingInfo.getFloorInfo() != null) {
					map.put("Floor", flatBookingInfo.getFloorInfo().getFloorName());
				}
				if (flatBookingInfo.getFlatInfo() != null) {
					map.put("Unit Number", flatBookingInfo.getFlatInfo().getFlatNo());
				}
				if (flatBookingInfo.getBlockInfo() != null) {
					map.put("Block", flatBookingInfo.getBlockInfo().getName());
				}
				if (flatBookingInfo.getNumberOfBeds() != null) {
					boolean isDouble = false;
					try {
						Double.valueOf(flatBookingInfo.getNumberOfBeds());
						isDouble = true;
					} catch (Exception ex) {
						map.put("Type/Beds in word", flatBookingInfo.getNumberOfBeds());
						map.put("Type/Beds", flatBookingInfo.getNumberOfBeds());
					}
					if(isDouble) {
						map.put("Type/Beds", flatBookingInfo.getNumberOfBeds());
						long number = 0l;
						StringBuffer str = null; 
						if(flatBookingInfo.getNumberOfBeds().contains(".")) {
							number = Long.valueOf(flatBookingInfo.getNumberOfBeds().split("\\.")[0]);
						}
						if(number<=1) {
							str = new StringBuffer( new NumberToWord().convertNumberToWords(new BigDecimal(flatBookingInfo.getNumberOfBeds()).setScale(roundingModeSize, roundingMode).longValue())+" Bedroom");
							if(flatBookingInfo.getNumberOfBeds().toString().contains(".")) {
								str.append(" along with one study room,");
							}
							map.put("Type/Beds in word", str);
						} else {
							str = new StringBuffer( new NumberToWord().convertNumberToWords(new BigDecimal(flatBookingInfo.getNumberOfBeds()).setScale(roundingModeSize, roundingMode).longValue())+" Bedrooms");
							if(flatBookingInfo.getNumberOfBeds().toString().contains(".")) {
								str.append(" along with one study room");
							}
							map.put("Type/Beds in word", str);	
						}
						//nothing but BHK
					}
				} else {
					errorMsgs.add("- Flat Bhk type is missing.");
					map.put("Type/Beds", "-");
					map.put("Type/Beds in word","-");
				}
				
				// unitDetails.add(new DynamicKeyValueInfo("Garden/Terrence Area in Sqft",""));

				if (flatBookingInfo.getFacing() != null) {
					map.put("Facing", flatBookingInfo.getFacing());
				}
				if(flatBookingInfo.getFlatCost()!=null) {
					FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
					if (flatCostInfo.getPerSqftCost() != null) {
						/*//unitDetails.add(new DynamicKeyValueInfo("Quoted Base Price", flatCostInfo.getQuotedBasePrice()));
						//PerSqftCost is Quoted Base Price*/
						double getPerSqftCost = flatCostInfo.getPerSqftCost()==null?0d:flatCostInfo.getPerSqftCost();
						String perSqftCost = currencyUtil.getTheAmountWithCommas(Double.valueOf(getPerSqftCost),roundingModeSize,roundingMode);
						map.put("Quoted Base Price", perSqftCost);
					}
					if (flatCostInfo.getUnitGroup() != null) {
						map.put("Unit Group", flatCostInfo.getUnitGroup());
					}
					
					if (flatCostInfo.getSoldBasePrice() != null) {
						map.put("Sold Base Price", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getSoldBasePrice()==null?0:flatCostInfo.getSoldBasePrice())),roundingModeSize,roundingMode));
					} else {
						map.put("Sold Base Price","-");
					}
					
					if (flatCostInfo.getBasicFlatCost() != null) {
						map.put("Basic Flat Cost", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getBasicFlatCost()==null?0:flatCostInfo.getBasicFlatCost())),roundingModeSize,roundingMode));
					} else {
						map.put("Basic Flat Cost","-");
					}
					
					if (flatCostInfo.getAmenitiesFlatCost() != null) {
						map.put("Amenities Cost",currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getAmenitiesFlatCost() ==null?0:flatCostInfo.getAmenitiesFlatCost())),roundingModeSize,roundingMode));
					} else {
						map.put("Amenities Cost","-");
					}
					
					if (flatCostInfo.getBasicFlatCost() != null) {
						if (flatCostInfo.getAmenitiesFlatCost() != null && Util.isEmptyObject(flatCostInfo.getTotalCostExcludeGst())) {
							flatCostInfo.setTotalCostExcludeGst(new BigDecimal(flatCostInfo.getBasicFlatCost()+flatCostInfo.getAmenitiesFlatCost()).setScale(roundingModeSize, roundingMode).doubleValue());
						}
					}
					
					String sbua = "";
					if (flatCostInfo.getTotalCostExcludeGst() != null) {
						map.put("Total Cost (Excl GST)", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getTotalCostExcludeGst()==null?0:flatCostInfo.getTotalCostExcludeGst())),roundingModeSize,roundingMode));
						sbua = currencyUtil.getTheAmountWithCommas(flatCostInfo.getTotalCostExcludeGst()/flatBookingInfo.getSbua(),roundingModeSize,roundingMode);
						map.put("Total Agreement Cost Per Sft ", sbua);
					} else {
						if (flatCostInfo.getBasicFlatCost() != null) {
							if (flatCostInfo.getAmenitiesFlatCost() != null) {
								flatCostInfo.setTotalCostExcludeGst(flatCostInfo.getBasicFlatCost()+flatCostInfo.getAmenitiesFlatCost());
								map.put("Total Cost (Excl GST)", currencyUtil.getTheAmountWithCommas(Double.valueOf(flatCostInfo.getBasicFlatCost()+flatCostInfo.getAmenitiesFlatCost()),roundingModeSize,roundingMode));
								sbua = currencyUtil.getTheAmountWithCommas((flatCostInfo.getBasicFlatCost()+flatCostInfo.getAmenitiesFlatCost())/flatBookingInfo.getSbua(),roundingModeSize,roundingMode);
								map.put("Total Agreement Cost Per Sft ", sbua);
							}
						}
					}
					if (flatCostInfo.getActualPricePerSft() != null) {
						map.put("Actual Price Per Sft", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getActualPricePerSft()==null?0:flatCostInfo.getActualPricePerSft())),roundingModeSize,roundingMode));
					}else {
						map.put("Actual Price Per Sft","-");
					}
					if (flatCostInfo.getGstCost() != null) {
						map.put("Total GST Amount", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getGstCost()==null?0:flatCostInfo.getGstCost())),roundingModeSize,roundingMode));
					}else {
						map.put("Total GST Amount","-");
					}
					if (flatCostInfo.getTotalCost() != null) {
						map.put("Total Agreement Cost", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getTotalCost()==null?0:flatCostInfo.getTotalCost())),roundingModeSize,roundingMode));
					}else {
						map.put("Total Agreement Cost","-");
					}
					if (flatCostInfo.getTaxesPerSft() != null) {
						map.put("Taxes Per Sft",currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getTaxesPerSft()==null?0:flatCostInfo.getTaxesPerSft())),roundingModeSize,roundingMode));
					}else {
						map.put("Taxes Per Sft","-");
					}
					if (flatCostInfo.getOverallPricePerSft() != null) {
						map.put("Overall Price Per Sft", currencyUtil.getTheAmountWithCommas(Double.valueOf((flatCostInfo.getOverallPricePerSft()==null?0:flatCostInfo.getOverallPricePerSft())),roundingModeSize,roundingMode));
					}else {
						map.put("Overall Price Per Sft","-");
					}
				}
			}
		}
		
		if(Util.isNotEmptyObject(errorMsgs) && errorMsgs.size()>1) {
			throw new EmployeeFinancialServiceException(errorMsgs.toString());
		}
		
		//front end keys 20
		//back end keys 20
		return map;
	}

	public void assignDefaultValues(CustomerBookingFormInfo customerBookingFormInfo) {
		CustomerInfo customerInfo = customerBookingFormInfo.getCustomerInfo();
		CustomerBookingInfo customerBookingInfo = customerBookingFormInfo.getCustomerBookingInfo();
		if (Util.isNotEmptyObject(customerInfo)) {
			customerInfo.setNamePrefix(customerInfo.getNamePrefix() == null ? "" : customerInfo.getNamePrefix());
			customerInfo.setFirstName(customerInfo.getFirstName() == null ? "" : customerInfo.getFirstName());
			customerInfo.setRelationWith(customerInfo.getRelationWith() == null ? "" : customerInfo.getRelationWith());
			customerInfo.setRelationNamePrefix(customerInfo.getRelationNamePrefix() == null ? "" : customerInfo.getRelationNamePrefix());
			customerInfo.setRelationName(customerInfo.getRelationName() == null ? "" : customerInfo.getRelationName());
			customerInfo.setAadharNumber(customerInfo.getAadharNumber() == null ? "" : customerInfo.getAadharNumber());
			customerInfo.setAge(customerInfo.getAge() == null ? 0l : customerInfo.getAge());
			customerInfo.setPancard(customerInfo.getPancard() == null ? "" : customerInfo.getPancard());
			if (customerBookingInfo != null) {
				customerInfo.setPhoneNo(customerBookingInfo.getPhoneNo() == null ? "" : customerBookingInfo.getPhoneNo());	
			}
		}
		
		ProfessionalInfo applicantProfessionalInfo = customerBookingFormInfo.getProfessionalInfo();
		if (Util.isNotEmptyObject(applicantProfessionalInfo)) {
			applicantProfessionalInfo.setDesignation(applicantProfessionalInfo.getDesignation()==null?"":applicantProfessionalInfo.getDesignation());
		}
		
		List<CoApplicentDetailsInfo> coApplicantPojoList = customerBookingFormInfo.getCoApplicentDetails();//.get(0).getCo_ApplicantInfo();
		if (Util.isNotEmptyObject(coApplicantPojoList)) {
			for (CoApplicentDetailsInfo applicentDetailsInfoList : coApplicantPojoList) {
				Co_ApplicantInfo ico = applicentDetailsInfoList.getCo_ApplicantInfo();
				ico.setNamePrefix(ico.getNamePrefix() == null ? "" : ico.getNamePrefix());
				ico.setFirstName(ico.getFirstName() == null ? "" : ico.getFirstName());
				ico.setRelationWith(ico.getRelationWith() == null ? "" : ico.getRelationWith());
				ico.setRelationNamePrefix(ico.getRelationNamePrefix() == null ? "" : ico.getRelationNamePrefix());
				ico.setRelationName(ico.getRelationName() == null ? "" : ico.getRelationName());
				ico.setAadharId(ico.getAadharId() == null ? "" : ico.getAadharId());
				ico.setAge(ico.getAge() == null ? 0l : ico.getAge());
				ico.setPancard(ico.getPancard() ==null?"": ico.getPancard());
				
				applicantProfessionalInfo = applicentDetailsInfoList.getProfessionalInfo();
				if(Util.isNotEmptyObject(applicantProfessionalInfo)) {
					applicantProfessionalInfo.setDesignation(applicantProfessionalInfo.getDesignation()==null?"":applicantProfessionalInfo.getDesignation());
				}
			}
		}
	}
	
	public Map<String, Object> getCustomerDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<AddressInfo> customerAddressInfoList, 
			CustomerBookingFormInfo customerBookingFormInfo, BookingFormRequest bookingFormRequestCopy, List<PoaHolderPojo> poHolderPojoList) {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CurrencyUtil currencyUtil = new CurrencyUtil();
		Map<String, Object> customerDetails = new HashMap<>();
		customerDetails.put("portNumber", bookingFormRequestCopy.getPortNumber());
		customerDetails.put("empId", bookingFormRequestCopy.getEmpId());
		customerDetails.put("empName", bookingFormRequestCopy.getEmployeeName());
		customerDetails.put("allotmentDate", TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		customerDetails.put("todayDate", TimeUtil.getTimeInDD_MM_YYYY(new Date()));
		customerDetails.put("todayDateHyphoneFormat", TimeUtil.getTimeInDD_MM_YYYY_SlashFormat(new Date()));
		
		String dateInWords = getTheDateInWords(new Timestamp(new Date().getTime()));
		customerDetails.put("dateInWords", dateInWords);
		
		CustomerInfo customerPojo = customerBookingFormInfo.getCustomerInfo();
		
		StringBuffer customerNameWithRelation = new StringBuffer("");
		StringBuffer customerNameIncaps = new StringBuffer("");
		StringBuffer customerPanNumber = new StringBuffer("");
		StringBuffer customerAdharNumber = new StringBuffer("");
		if(customerPojo!=null) {
			if(customerPojo.getNamePrefix()!=null) {
				customerNameWithRelation.append(customerPojo.getNamePrefix());
				customerNameWithRelation.append(". ");
			}
			if(customerPojo.getFirstName()!=null) {
				customerNameWithRelation.append(customerPojo.getFirstName());
				customerNameWithRelation.append(", ");
			}
			if(customerPojo.getRelationWith()!=null) {
				customerNameWithRelation.append(customerPojo.getRelationWith());
				customerNameWithRelation.append(". ");
			}
			if(customerPojo.getRelationNamePrefix()!=null) {
				customerNameWithRelation.append(customerPojo.getRelationNamePrefix());
				customerNameWithRelation.append(".");
			}
			if(customerPojo.getRelationName()!=null) {
				customerNameWithRelation.append(customerPojo.getRelationName());
				customerNameWithRelation.append(", ");
			}
			
			if(customerPojo.getAdharNumber()!=null) {
				customerAdharNumber.append(customerPojo.getAdharNumber());
			}
			
			if(customerPojo.getPancard()!=null) {
				customerPanNumber.append(customerPojo.getPancard());
			}
			if(customerPojo.getNamePrefix()!=null) {
				customerNameIncaps.append(customerPojo.getNamePrefix());
				customerNameIncaps.append(". ");
			}
			if(customerPojo.getFirstName()!=null) {
				customerNameIncaps.append(customerPojo.getFirstName().toUpperCase());
			}
		}
		
		List<CoApplicentDetailsInfo> coApplicantPojoList = customerBookingFormInfo.getCoApplicentDetails();//.get(0).getCo_ApplicantInfo();

		
		if(Util.isNotEmptyObject(poHolderPojoList)) {
			PoaHolderPojo holderPojo = poHolderPojoList.get(0);
			if(holderPojo!=null) {
				customerDetails.put("poiHolderName", holderPojo.getNameOfPOA());
			}
		}
		
		System.out.println(customerNameWithRelation);
		System.out.println(customerAdharNumber);
		System.out.println(customerPanNumber);
		customerDetails.put("customerNameWithRelation", customerNameWithRelation);
		customerDetails.put("customerNameIncaps", customerNameIncaps.toString());
		customerDetails.put("customerAdharNumber", customerAdharNumber);
		customerDetails.put("customerPanNumber", customerPanNumber);
		
		
		/* setting applicant and coApplicant names and mobile numbers */
		if(Util.isNotEmptyObject(customerPropertyDetailsInfo)) {
			StringBuilder name = new StringBuilder(); 
			StringBuilder mobileNos = new StringBuilder();
			StringBuilder flatNo = new StringBuilder();
			StringBuilder floorName = new StringBuilder();
			StringBuilder blockName = new StringBuilder();
			StringBuilder siteName = new StringBuilder();
			StringBuilder pancard = new StringBuilder();
			StringBuilder finSchemeId = new StringBuilder();
			StringBuilder finSchemeName = new StringBuilder();
			StringBuilder finSchemeType = new StringBuilder();
			StringBuilder custEmailId = new StringBuilder();
			StringBuilder firstname = new StringBuilder(); 
			
			Long noOfCustomersInFlat=0l;
			//for(CustomerPropertyDetailsInfo  customerPropertyDetailsInfo : resp.getFlatsResponse()){
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName()) || Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())) {
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerName())){	
					firstname.append(customerPropertyDetailsInfo.getCustomerName());
					name.append(customerPropertyDetailsInfo.getCustomerName());
					noOfCustomersInFlat++;
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCoAppFullName())){
						name.append(" and &nbsp;");
						name.append(customerPropertyDetailsInfo.getCoAppFullName());
						noOfCustomersInFlat++;
					}
				}else {
					firstname.append("N/A");
					name.append("N/A");
				}
				}else {
					name.append("N/A");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber()) ||  Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())) {
					if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
						mobileNos.append(customerPropertyDetailsInfo.getContactNumber());
					}
					/*if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getAlternatePhoneNo())){
						if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getContactNumber())) {
							mobileNos.append(",");
						}
						mobileNos.append(customerPropertyDetailsInfo.getAlternatePhoneNo());
					}*/
				}else {
					mobileNos.append("-");
				}
				
				if (Util.isNotEmptyObject(coApplicantPojoList)) {
					for (CoApplicentDetailsInfo applicentDetailsInfoList : coApplicantPojoList) {
						CoApplicentBookingInfo ico = applicentDetailsInfoList.getCoApplicentBookingInfo();
						if(Util.isNotEmptyObject(ico.getPhoneNo())) {
							mobileNos.append(" &  &nbsp;");mobileNos.append(ico.getPhoneNo());	
						}
					}
				}
				
				/* setting customer emailId  */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getCustomerEmail())) {
					custEmailId.append(customerPropertyDetailsInfo.getCustomerEmail());
				}else {
					custEmailId.append("N/A");
				}
				
				/* setting flat Number */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatNo())) {
					flatNo.append(customerPropertyDetailsInfo.getFlatNo());
				}else {
					flatNo.append("N/A");
				}
				
				/* setting Floor Name   */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFloorName())) {
					floorName.append(customerPropertyDetailsInfo.getFloorName());
				}else {
					floorName.append("N/A");
				}
				
				/* setting Block name */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getBlockName())) {
					blockName.append(customerPropertyDetailsInfo.getBlockName());
				}else {
					blockName.append("N/A");
				}
				
				/* setting Site Name */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteName())) {
					siteName.append(customerPropertyDetailsInfo.getSiteName());
				}else {
					siteName.append("N/A");
				}
				 
				
				/* setting customer pancard  */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getPancard())) {
					pancard.append(customerPropertyDetailsInfo.getPancard());
				}else {
					pancard.append("N/A");
				}
				
				/* setting siteId */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getSiteId())) {
					//info.setSiteId(customerPropertyDetailsInfo.getSiteId().toString());
					customerDetails.put("siteId", customerPropertyDetailsInfo.getSiteId());
				}else {
					//info.setSiteId("Anonymous_Site");
					customerDetails.put("siteId", customerPropertyDetailsInfo.getSiteId());
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getBookingDate())) {
					customerDetails.put("bookingDate", TimeUtil.timestampToDD_MM_YYYY(customerPropertyDetailsInfo.getBookingDate()));
				} else {
					customerDetails.put("bookingDate", "");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getTotalAgreementCost())) {
					String totalAgreementCost = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(customerPropertyDetailsInfo.getTotalAgreementCost()).setScale(roundingModeSize, roundingMode).toString());
					//String totalAgreementCost = currencyUtil.convertUstoInFormat(BigDecimal.valueOf(customerPropertyDetailsInfo.getTotalAgreementCost()).setScale(0, roundingMode).toString());
					customerDetails.put("totalAgreementCost",totalAgreementCost);
				}else {
					customerDetails.put("totalAgreementCost","0.00");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeName())) {
					finSchemeName.append(customerPropertyDetailsInfo.getFinSchemeName());
				} else {
					finSchemeName.append("N/A");
					logger.info("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
					//throw new EmployeeFinancialServiceException("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeType())) {
					finSchemeType.append(customerPropertyDetailsInfo.getFinSchemeType());
				} else {
					finSchemeType.append("N/A");
				}
				
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFinSchemeId())) {
					finSchemeId.append(customerPropertyDetailsInfo.getFinSchemeId());
				} else {
					finSchemeId.append("N/A");
					logger.info("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
					//throw new EmployeeFinancialServiceException("Failed to generate demand note, Scheme not found for Flat No "+customerPropertyDetailsInfo.getFlatNo());
				}
				
				/* setting flatBookingId */
				if(Util.isNotEmptyObject(customerPropertyDetailsInfo.getFlatBookingId())) {
					//info.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId().toString());
					//info.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
					customerDetails.put("flatBookingId", customerPropertyDetailsInfo.getFlatBookingId());
					customerDetails.put("bookingFormId", customerPropertyDetailsInfo.getFlatBookingId());
				}else {
					//info.setFlatBookingId("Anonymous_Flat_Booking");
					customerDetails.put("flatBookingId", customerPropertyDetailsInfo.getFlatBookingId());
					customerDetails.put("bookingFormId", customerPropertyDetailsInfo.getFlatBookingId());
				}
			//}

			customerPojo.setEmail(customerPropertyDetailsInfo.getCustomerEmail());
			customerDetails.put("noOfCustomersIncludedInBooking", noOfCustomersInFlat);
			customerDetails.put("customerNames", name.toString());
			customerDetails.put("mobileNumbers",mobileNos.toString());
			customerDetails.put("custEmailId",custEmailId.toString());
			customerDetails.put("flatName",flatNo.toString());
			customerDetails.put("floorName",floorName.toString());
			customerDetails.put("blockName",blockName.toString());
			customerDetails.put("customername", firstname.toString());//onlymain applicant name
			customerDetails.put("finSchemeId",finSchemeId);
			customerDetails.put("finSchemeName",finSchemeName);
			customerDetails.put("finSchemeType",finSchemeType);
		
			if(customerPropertyDetailsInfo.getSiteId()!=null && customerPropertyDetailsInfo.getSiteId().equals(131l)) {
				customerDetails.put("siteName","Aspire Aurum");
			}else {
				customerDetails.put("siteName",siteName.toString());
			}
			customerDetails.put("pancard",pancard.toString());
		}
		
		/* setting applicant permanent address  */
		if(Util.isNotEmptyObject(customerAddressInfoList)) {
			for(AddressInfo addressInfo : customerAddressInfoList){
			/* Taking correspondence Address  */	
             if(Util.isNotEmptyObject(addressInfo)) {
            	 if(addressInfo.getAddressMappingType().getAddressType().equalsIgnoreCase("Correspondence")){
            		 if(Util.isNotEmptyObject(addressInfo.getAddress1())) {
            			 customerDetails.put("customerAddress",addressInfo.getAddress1());
            			break;
            		    /*if(Util.isNotEmptyObject(addressInfo.getPincode())) {
            		    	 info.setAddress(info.getAddress()+"-"+addressInfo.getPincode());
            		    	 break;
            		    }*/
            		 }else {
            			 customerDetails.put("customerAddress","N/A"); 
            		 }
            	 }else {
            		 customerDetails.put("customerAddress","N/A");  
            	 }
             }
		  }
		} else {
			customerDetails.put("customerAddress","N/A");
		}
		
		return customerDetails;
	}

	public static String theMonth(int month){
	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    return monthNames[month];
	}
	/**
	 * @get the current date in String format
	 * @param timestamp
	 * @return
	 */
	private String getTheDateInWords(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
 
        // Get day from date
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		        
        StringBuffer sb = new StringBuffer("");
        sb.append(DateToWord.getDayInWordsWithIndex(dayOfMonth)).append(" day of ");
        sb.append(theMonth(month)).append(",");
        sb.append(getTheYearInWords(year));
		return sb.toString();
	}
	
	/**
	 * @get the year in words
	 * @param number
	 * @return
	 */
	private static String getTheYearInWords(Integer number) {
	 	String amount = number.toString();
		//System.out.println(amount+"\t first " +amountToConvertInWords);
		String[] splitedAmount = amount.split("\\.");
		StringBuffer amountInWords = new StringBuffer(new NumberToWord().convertNumberToWords(new BigDecimal(splitedAmount[0]).setScale(roundingModeSize, roundingMode).longValue()));
		if(splitedAmount[0].equals("0")) {
			amountInWords = new StringBuffer("Zero");
		}
		StringBuffer amountInWordsPaisa = new StringBuffer("");
		if(splitedAmount.length>1 && Double.valueOf(splitedAmount[1])!=0) {
			amountInWordsPaisa = new StringBuffer(" ").append(new NumberToWord().convertNumberToWords(new BigDecimal(splitedAmount[1]).setScale(roundingModeSize, roundingMode).longValue()) +" ");
		}
		
		return amountInWords.append(amountInWordsPaisa).toString();
	}
	
	@SuppressWarnings("unused")
	private static String getTheDayInWords(Integer number) {
	 	String amount = number.toString();
		//System.out.println(amount+"\t first " +amountToConvertInWords);
		String[] splitedAmount = amount.split("\\.");
		StringBuffer amountInWords = new StringBuffer(new DateToWord().convertNumberToWords(new BigDecimal(splitedAmount[0]).setScale(roundingModeSize, roundingMode).longValue()));
		if(splitedAmount[0].equals("0")) {
			amountInWords = new StringBuffer("Zero");
		}
		StringBuffer amountInWordsPaisa = new StringBuffer("");
		if(splitedAmount.length>1 && Double.valueOf(splitedAmount[1])!=0) {
			amountInWordsPaisa = new StringBuffer(" ").append(new DateToWord().convertNumberToWords(new BigDecimal(splitedAmount[1]).setScale(roundingModeSize, roundingMode).longValue()) +" ");
		}
		
		return amountInWords.append(amountInWordsPaisa).toString();
	}

	
	public Map<String, Object> getCustomerCoApplicantDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<CoApplicantPojo> coApplicantPojoList,
			BookingFormRequest bookingFormRequestCopy) throws Exception{
		
		return null;
	}
	
	public String getTheEmailContent(String emailContent) throws IOException {
		StringBuffer emailData = new StringBuffer();
		
		org.jsoup.nodes.Document htmlData = Jsoup.parse(emailContent);
        
        for (org.jsoup.nodes.Element table : htmlData.select("[class=welcomeMail]")) {
				String Footer = table.html();
				BufferedReader br = new BufferedReader(new StringReader(Footer)); 
				// creates a buffering character input  stream
				String line;
				while ((line = br.readLine()) != null) {
					emailData.append(Util.html2text(line)); // appends line to string buffer
					emailData.append("\n").append("<br>"); // line feed
				}
				br.close();
		}
		
		System.out.println(emailData);
		return emailData.toString();
	}

	public List<Customer> copyCustomerPojoListToInfoList(List<CustomerPojo> customerPojoLists) {
		List<Customer> customerDetailsList = new ArrayList<>();
		for(CustomerPojo customerPojo : customerPojoLists){
			Customer info = new Customer();
			BeanUtils.copyProperties(customerPojo, info);
			info.setAge(customerPojo.getAge()==null?0:customerPojo.getAge().intValue());
			customerDetailsList.add(info);
		}
		return customerDetailsList;
	}

	public List<Co_ApplicantInfo> copyCoApplicantPojoListToCoApplicantInfoList(List<CoApplicantPojo> coApplicantPojoList) {
		List<Co_ApplicantInfo> coApplicantDetailsList = new ArrayList<>();
		for(CoApplicantPojo coApplicantPojo : coApplicantPojoList){
			Co_ApplicantInfo info = new Co_ApplicantInfo();
			BeanUtils.copyProperties(coApplicantPojo, info);
			info.setAadharId(coApplicantPojo.getAdharId()==null?"-":coApplicantPojo.getAdharId());
			info.setAge(coApplicantPojo.getAge()==null?0:coApplicantPojo.getAge().longValue());
			
			info.setNamePrefix(coApplicantPojo.getNameprefix()==null?"-":coApplicantPojo.getNameprefix());
			info.setRelationNamePrefix(coApplicantPojo.getRelationNamePrefix()==null?"-":coApplicantPojo.getRelationNamePrefix());
			info.setRelationName(coApplicantPojo.getRelationName()==null?"-":coApplicantPojo.getRelationName());
			
			coApplicantDetailsList.add(info);
		}
		return coApplicantDetailsList;
	}

	
}
