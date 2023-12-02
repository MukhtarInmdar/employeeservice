
package com.sumadhura.employeeservice.service;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.neovisionaries.i18n.CountryCode;
import com.sumadhura.employeeservice.dto.Email;
import com.sumadhura.employeeservice.dto.EmployeeFinancialResponse;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinProjectAccountResponse;
import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.dto.MessengerResponce;
import com.sumadhura.employeeservice.dto.ReferedCustomer;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.dto.SiteLevelNotifyRequestDTO;
import com.sumadhura.employeeservice.enums.CheckList;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.FinEnum;
import com.sumadhura.employeeservice.enums.FinTransactionType;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Nationality;
import com.sumadhura.employeeservice.enums.Refrences;
import com.sumadhura.employeeservice.enums.Roles;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.DefaultBankerException;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.exception.InvalidStatusException;
import com.sumadhura.employeeservice.exception.RefundAmountException;
import com.sumadhura.employeeservice.exception.SQLInsertionException;
import com.sumadhura.employeeservice.persistence.dao.ApplyLoanDao;
import com.sumadhura.employeeservice.persistence.dao.BookingFormServiceDao;

import com.sumadhura.employeeservice.persistence.dao.EmployeeFinancialServiceDao;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;

import com.sumadhura.employeeservice.persistence.dto.AddressMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.AddressPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraMasterPojo;
import com.sumadhura.employeeservice.persistence.dto.AminitiesInfraSiteWisePojo;
import com.sumadhura.employeeservice.persistence.dto.AminititesInfraDetails;
import com.sumadhura.employeeservice.persistence.dto.AminititesInfraFlatWisePojo;
import com.sumadhura.employeeservice.persistence.dto.BankerList;
import com.sumadhura.employeeservice.persistence.dto.BlockPojo;
import com.sumadhura.employeeservice.persistence.dto.BookingStatusChangedPojo;
import com.sumadhura.employeeservice.persistence.dto.CarParkingAllotmentSlotPojo;
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
import com.sumadhura.employeeservice.persistence.dto.DevicePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountSummaryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormExcessAmountPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinProjectAccountPojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemePojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemeTaxMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionEntryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionSetOffPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialProjectMileStonePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingSchemeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorPojo;
import com.sumadhura.employeeservice.persistence.dto.MenuLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.MessengerDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDcoumetsUrls;
import com.sumadhura.employeeservice.persistence.dto.NOCDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDocumentsList;
import com.sumadhura.employeeservice.persistence.dto.NOCReleasePojo;
import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.PoaHolderPojo;
import com.sumadhura.employeeservice.persistence.dto.ProfessionalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.ReferencesMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteOtherChargesDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.SitePojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketReportingPojo;
import com.sumadhura.employeeservice.rest.service.MessengerRestService;

import com.sumadhura.employeeservice.service.dto.AddressInfo;
import com.sumadhura.employeeservice.service.dto.AddressMappingInfo;
import com.sumadhura.employeeservice.service.dto.AminitiesInfraCostInfo;
import com.sumadhura.employeeservice.service.dto.BookingFormApproveRequest;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.BookingFormSavedStatus;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentInfo;
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
import com.sumadhura.employeeservice.service.dto.Co_ApplicantInfo;
import com.sumadhura.employeeservice.service.dto.CustomerApplicationInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingInfo;
import com.sumadhura.employeeservice.service.dto.CustomerCheckListVerificationInfo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerKYCDocumentSubmitedInfo;
import com.sumadhura.employeeservice.service.dto.CustomerOtherDetailsInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.CustomerSchemeInfo;
import com.sumadhura.employeeservice.service.dto.DynamicKeyValueInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialTransactionServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormAccountsInfo;
import com.sumadhura.employeeservice.service.dto.FinTransactionEntryDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinancialProjectMileStoneInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;
import com.sumadhura.employeeservice.service.dto.FlatCostInfo;
import com.sumadhura.employeeservice.service.dto.MileStoneInfo;
import com.sumadhura.employeeservice.service.dto.OraganizationDetails;
import com.sumadhura.employeeservice.service.dto.POADetailsInfo;
import com.sumadhura.employeeservice.service.dto.ProfessionalInfo;
import com.sumadhura.employeeservice.service.dto.ReferenceMaster;
import com.sumadhura.employeeservice.service.dto.ReferencesCustomerInfo;
import com.sumadhura.employeeservice.service.dto.ReferencesFriendInfo;
import com.sumadhura.employeeservice.service.dto.ReferencesMappingInfo;
import com.sumadhura.employeeservice.service.dto.SectorDetailsInfo;
import com.sumadhura.employeeservice.service.dto.WorkFunctionInfo;
import com.sumadhura.employeeservice.service.helpers.BookingFormServiceHelper;
import com.sumadhura.employeeservice.service.helpers.EmployeeFinancialHelper;
import com.sumadhura.employeeservice.service.mappers.BookingFormMapper;
import com.sumadhura.employeeservice.service.mappers.EmployeeFinancialMapper;

import com.sumadhura.employeeservice.serviceImpl.EmployeeFinancialServiceImpl;
import com.sumadhura.employeeservice.util.Base64FileUtil;
import com.sumadhura.employeeservice.util.CarouselUtils;
import com.sumadhura.employeeservice.util.CurrencyUtil;
import com.sumadhura.employeeservice.util.PdfHelper;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * BookingFormServiceImpl class provides Implementation for
 * BookingFormService.
 * 
 * @author Srivenu
 * @since 30.05.2019
 * @time 11:47AM
 */
@Service("BookingFormServiceImpl")
public class BookingFormServiceImpl implements BookingFormService {

	@Autowired(required = true)
	@Qualifier("EmployeeTicketDaoImpl")
	private EmployeeTicketDao employeeTicketDaoImpl;
	
	@Autowired(required = true)
	@Qualifier("EmployeeTicketServiceImpl")
	private EmployeeTicketService employeeTicketServiceImpl;

	@Autowired(required = true)
	@Qualifier("BookingFormServiceDaoImpl")
	private BookingFormServiceDao bookingFormServiceDaoImpl;
	
	@Autowired
	@Qualifier("EmployeeFinancialServiceDao")
	private EmployeeFinancialServiceDao employeeFinancialServiceDao;
	
	@Autowired
	@Qualifier("EmployeeFinancialService")
	private EmployeeFinancialService employeeFinancialService;
	
	
	@Autowired(required = true)
	@Qualifier("BookingFormServiceHelper")
	private BookingFormServiceHelper bookingFormServiceHelper;
	

	@Autowired(required=true)
	@Qualifier("employeeFinancialHelper")
	private EmployeeFinancialHelper employeeFinancialHelper;
	
	@Autowired(required=true)
	private MessengerRestService messengerRestService;
	
	
	@Autowired(required = true)
	@Qualifier("employeeFinancialMapper")
	private EmployeeFinancialMapper financialMapper;
	
	@Autowired
	private ResponceCodesUtil responceCodesUtil;

	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	
	private BookingFormMapper bookingFormMapper = new BookingFormMapper();

	private Logger logger = Logger.getLogger(BookingFormServiceImpl.class);
	
	@Autowired(required = true)
	private ApplyLaonService applyLaonService;
	
	
	private RoundingMode roundingMode = RoundingMode.HALF_UP;
	private int roundingModeSize = 2;
	private long cgstDividedBy = 2;
	private long sgstDividedBy = 2;

	@Override
	public List<CustomerCheckListVerificationInfo> getCustomerCheckListVerifications(
			BookingFormRequest bookingFormRequest, Department department) {
		List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfos = new ArrayList<>();
		CustomerCheckListVerificationInfo customerCheckListVerificationInfo;
		bookingFormRequest.setRequestUrl("insertOrUpdateChecklistUtil");
		bookingFormRequest.setDeptId(department.getId());
		List<CustChecklistVerificationPojo> checklistVerificationPojos = bookingFormServiceDaoImpl.getCustChecklistVerification(bookingFormRequest);
		if (checklistVerificationPojos != null && checklistVerificationPojos.size() > 0) {
		for(CustChecklistVerificationPojo pojo : checklistVerificationPojos) {
			bookingFormRequest.setCheckListDeptMapId(pojo.getChecklistDeptMapId());
			bookingFormRequest.setRequestUrl("checklistDepartmentMappringId");
			List<ChecklistDepartmentMappingPojo> checkListDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
			for (ChecklistDepartmentMappingPojo checkListDepartmentMappingPojo : checkListDepartmentMappingPojos) {
				bookingFormRequest.setCheckListId(checkListDepartmentMappingPojo.getCheckListId());
				List<CheckListPojo> checkListPojos = bookingFormServiceDaoImpl.getCheckList(bookingFormRequest);
				for(CheckListPojo checkListPojo : checkListPojos ) {
				customerCheckListVerificationInfo = bookingFormMapper.CustChecklistVerification$ChecklistDepartmentMapping$CheckListPojosToCustomerCheckListVerification(
						pojo, checkListDepartmentMappingPojo, checkListPojo);
				customerCheckListVerificationInfos.add(customerCheckListVerificationInfo);
				}
			}
		}
	    }else {
	    	customerCheckListVerificationInfo = new CustomerCheckListVerificationInfo();
			customerCheckListVerificationInfo.setCheckListInfo(new CheckListInfo());
			customerCheckListVerificationInfos.add(customerCheckListVerificationInfo);
		}
		return customerCheckListVerificationInfos;
	}
	
	@Override
	public List<FlatBookingInfo> getFlatBookingInfo(BookingFormRequest bookingFormRequest) {
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = bookingFormServiceDaoImpl.getCustomerPropertyDetails(bookingFormRequest);
		if(Util.isEmptyObject(customerPropertyDetailsPojos)) {
			return new ArrayList<FlatBookingInfo>();
		}
		bookingFormRequest.setFlatId(customerPropertyDetailsPojos.get(0).getFlatId());
		List<FlatPojo> flatPojos = bookingFormServiceDaoImpl.getFlat(bookingFormRequest);
		List<FlatDetailsPojo> flatDetailsPojos = bookingFormServiceDaoImpl.getFlatDetails(bookingFormRequest);
		List<FlatCostPojo> flatCostPojos = bookingFormServiceDaoImpl.getFlatCost(bookingFormRequest);
		bookingFormRequest.setFloorId(customerPropertyDetailsPojos.get(0).getFlooId());
		List<FloorPojo> floorPojos = bookingFormServiceDaoImpl.getFloor(bookingFormRequest);
		bookingFormRequest.setBlockId(customerPropertyDetailsPojos.get(0).getBlockId());
		List<BlockPojo> blockPojos = bookingFormServiceDaoImpl.getBlock(bookingFormRequest);
		bookingFormRequest.setSiteId(customerPropertyDetailsPojos.get(0).getSiteId());
		List<SitePojo> sitePojos = bookingFormServiceDaoImpl.getSite(bookingFormRequest);
		/*
		List<AminitiesInfraCostPojo> aminitiesInfraCostPojos = new ArrayList<>();
		if (flatCostPojos != null && flatCostPojos.size() > 0) {
			bookingFormRequest.setFlatCostId(flatCostPojos.get(0).getFlatCostId());
			aminitiesInfraCostPojos = bookingFormServiceDaoImpl.getAminitiesInfraCost(bookingFormRequest);
		}
		else {
			aminitiesInfraCostPojos.add(new AminitiesInfraCostPojo());
		} */
		
		List<FlatBookingInfo> flatBookingInfos = bookingFormMapper.flat$flatDetails$flatCost$floor$block$sitePojos$aminitiesInfraCostPojosToFlatBookingInfo(flatPojos,
						flatDetailsPojos, flatCostPojos, floorPojos, blockPojos, sitePojos, bookingFormServiceDaoImpl,bookingFormRequest);
		return flatBookingInfos;
	}
	
      private void addFlatWiseAmenities(CustomerBookingFormInfo customerBookingFormInfo,BookingFormRequest bookingFormRequest,List<String> errorMsgs,BookingFormMapper mapper,Long siteId,Long flatId,Long flatCostId) throws InSufficeientInputException {
    	  logger.info("*** The control is inside the addFlatWiseAmenities in BookingFormServiceImpl ****");
    	  try {
			List<AminitiesInfraCostInfo> aminitiesInfraCostInfos = customerBookingFormInfo.getFlatBookingInfo().getAminitiesInfraCostInfo();
			
			if (aminitiesInfraCostInfos != null && aminitiesInfraCostInfos.size() > 0) {
				for (AminitiesInfraCostInfo aminitiesInfraCostInfo : aminitiesInfraCostInfos) {
					
					bookingFormRequest.setAminititesInfraName(aminitiesInfraCostInfo.getAminititesInfraName().trim());
				    AminitiesInfraMasterPojo aminitiesInfraMasterPojo = bookingFormServiceDaoImpl.getAminitiesInfraMaster(bookingFormRequest).get(0);
					bookingFormRequest.setSiteId(siteId);
					
					bookingFormRequest.setAminititesInfraId(aminitiesInfraMasterPojo.getAminititesInfraId());
					AminitiesInfraSiteWisePojo aminitiesInfraSiteWisePojo = bookingFormServiceDaoImpl.getAminitiesInfraSiteWise(bookingFormRequest).get(0);
					bookingFormRequest.setFlatId(flatId);
					
					bookingFormRequest.setAminititesInfraSiteWiseId(aminitiesInfraSiteWisePojo.getAminititesInfraSiteWiseId());
					AminititesInfraFlatWisePojo aminitiesInfraFlatWisePojo = bookingFormServiceDaoImpl.getAminitiesInfraFlatWise(bookingFormRequest).get(0);
					
					aminitiesInfraCostInfo.setAminititesInfraFlatWiseId(aminitiesInfraFlatWisePojo.getAminititesInfraFlatWiseId());
					aminitiesInfraCostInfo.setCreatedBy(customerBookingFormInfo.getUserId());
					
					aminitiesInfraCostInfo.setFlatCostId(flatCostId);
					
					logger.info("***** Control inside the BookingFormServiceImpl.addFlatWiseAmenities() *****");
					bookingFormServiceDaoImpl.saveAminiteInfraCost(mapper.AminitiesInfraCostInfoToAminitiesInfraCostPojo(aminitiesInfraCostInfo));
				}
			}
		} catch (Exception e) {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add("Error occured while Aminities Infra Cost details");
			logger.info("Error occured while Aminities Infra Cost details - " + e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public BookingFormSavedStatus saveBookingFormDetails(CustomerBookingFormInfo customerBookingFormInfo)
			throws InvalidStatusException, InSufficeientInputException, IllegalAccessException {
		
		BookingFormSavedStatus bookingFormSavedStatus = new BookingFormSavedStatus();
		BookingFormMapper mapper = new BookingFormMapper();
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setRequestUrl("saveBookingDetails");
		bookingFormRequest.setPortNumber(customerBookingFormInfo.getPortNumber());
		bookingFormRequest.setEmpId(customerBookingFormInfo.getEmployeeId());
		Long customerId, custBookInfoId, longProfessionId, customerOtherDetailsId, flatBookId, flatId, siteId, blockId;
		CustBookInfoPojo custBookInfoPojo;
		String flatNo="";String flatSaleOwnerId = "";
		List<String> errorMsgs = new ArrayList<String>();
		List<CustomerSchemeInfo> customerSchemeInfo = customerBookingFormInfo.getCustomerSchemeInfos();
		bookingFormSavedStatus.setLeadId(customerBookingFormInfo.getCustomerBookingInfo().getSalesTeamLeadId());
		if (Util.isEmptyObject(customerBookingFormInfo.getCustomerInfo().getLastName())) {
			customerBookingFormInfo.getCustomerInfo().setLastName("");
		}
		bookingFormSavedStatus.setCustName((Util.isNotEmptyObject(customerBookingFormInfo.getCustomerInfo().getFirstName())?customerBookingFormInfo.getCustomerInfo().getFirstName():"") + " "
				+ (Util.isNotEmptyObject(customerBookingFormInfo.getCustomerInfo().getLastName())?customerBookingFormInfo.getCustomerInfo().getLastName():""));
		logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - getting customer details if already exist ***");
		try {
			/* if customer is foreign customer go with passport || if customer is Local customer go with pancard. */
			List<CustomerPojo> listCustomersByPanCardorPassport = null;
			//if((Util.isNotEmptyObject(customerBookingFormInfo.getCustomerInfo().getNationality())) && (customerBookingFormInfo.getCustomerInfo().getNationality().equalsIgnoreCase(CountryCode.IN.getName())  || customerBookingFormInfo.getCustomerInfo().getNationality().equalsIgnoreCase(Nationality.INDIAN.getName()))){
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerInfo().getPancard())){
			         listCustomersByPanCardorPassport = bookingFormServiceDaoImpl
					.getCustomerDetailsByPanCardorPassport(customerBookingFormInfo.getCustomerInfo().getPancard(),null,null);
			}else {
				  String passport = Util.isNotEmptyObject(customerBookingFormInfo.getCustomerInfo().getPassport())? customerBookingFormInfo.getCustomerInfo().getPassport(): "";
				  listCustomersByPanCardorPassport = bookingFormServiceDaoImpl.getCustomerDetailsByPanCardorPassport(customerBookingFormInfo.getCustomerInfo().getPancard(), passport,null);
			}
			if (listCustomersByPanCardorPassport == null || listCustomersByPanCardorPassport.size() == 0) {
				logger.info(
						"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving new customer details ***");
				customerId = bookingFormServiceDaoImpl.saveCustomerDetails(
						mapper.CustomerInfoToCustomerPojo(customerBookingFormInfo.getCustomerInfo()));
			}else {
				logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving new customer details ***");
				customerId = listCustomersByPanCardorPassport.get(0).getCustomerId();
				Long statusId = listCustomersByPanCardorPassport.get(0).getStatusId();
				//ACP
				if(statusId!=null && (!statusId.equals(Status.ACTIVE.getStatus()) && !statusId.equals(Status.PENDING.getStatus()))) {
					//if(checkCustomerAssociatedFlats(bookingFormRequest)) {
					if(true) {
						BookingFormRequest bookingFormRequestCopy = new BookingFormRequest(); 
						BeanUtils.copyProperties(bookingFormRequest,bookingFormRequestCopy);
						bookingFormRequestCopy.setStatusId(Status.PENDING.getStatus());
						bookingFormRequestCopy.setCustomerId(customerId);
						List<Long> listOfBookingStatus = Arrays.asList(Status.CANCEL.getStatus(),Status.PENDING.getStatus(),Status.SWAP.getStatus(),
								Status.AVAILABLE.getStatus(),Status.BLOCKED.getStatus(),Status.NOT_OPEN.getStatus(),Status.PRICE_UPDATE.getStatus(),
								Status.LEGAL_CASE.getStatus(),Status.PMAY_SCHEME_ELIGIBLE.getStatus(),Status.RETAINED.getStatus()
								,Status.CANCELLED_NOT_REFUNDED.getStatus(),Status.ASSIGNMENT.getStatus());
						bookingFormServiceDaoImpl.updateCustomer(bookingFormRequestCopy,listOfBookingStatus);
					}
				}
			}
		} catch (DataIntegrityViolationException ex) {
			logger.error("**** The Exception detailed informtion is ****" , ex);
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving same records to the database plz try again once !");
			logger.info("Error occured while saving the existing customer once again to the database plz try again once -" + ex.toString());
			throw new InSufficeientInputException(errorMsgs);
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving new customer/getting existing customer - ");
			logger.info("Error occured while saving new customer/getting existing customer - " + e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}

		logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving flat booking details ***");
		/*
		 * List<FlatPojo> flatPojo =
		 * bookingFormServiceDaoImpl.getFlatByName(customerBookingFormInfo.
		 * getFlatBookingInfo().getFlatInfo().getFlatNo()); List<BlockPojo> blockPojo =
		 * bookingFormServiceDaoImpl.getBlockByName(customerBookingFormInfo.
		 * getFlatBookingInfo().getBlockInfo().getName()); List<SitePojo> sitePojo =
		 * bookingFormServiceDaoImpl.getSiteByName(customerBookingFormInfo.
		 * getFlatBookingInfo().getSiteInfo().getName());
		 */
		bookingFormRequest.setSiteName(customerBookingFormInfo.getFlatBookingInfo().getSiteInfo().getName());
		bookingFormRequest.setFlatNo(customerBookingFormInfo.getFlatBookingInfo().getFlatInfo().getFlatNo());
		bookingFormRequest.setFloorName(customerBookingFormInfo.getFlatBookingInfo().getFloorInfo().getFloorName());
		bookingFormRequest.setBlockName(customerBookingFormInfo.getFlatBookingInfo().getBlockInfo().getName());

		List<CustomerPropertyDetailsPojo> masterFlatFloorBlockSiteData = bookingFormServiceDaoImpl
				.getFlatFloorBlockSiteByNames(bookingFormRequest);

		if (masterFlatFloorBlockSiteData != null && masterFlatFloorBlockSiteData.size() > 0) {
			flatId = masterFlatFloorBlockSiteData.get(0).getFlatId();
			flatNo = masterFlatFloorBlockSiteData.get(0).getFlatNo();
			flatSaleOwnerId = masterFlatFloorBlockSiteData.get(0).getFlatSaleOwnerId();
		} else {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while getting flat details - Empty Result");
			throw new InSufficeientInputException(errorMsgs);
		}
		if (masterFlatFloorBlockSiteData != null && masterFlatFloorBlockSiteData.size() > 0) {
			blockId = masterFlatFloorBlockSiteData.get(0).getBlockId();
		} else {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while getting block details - Empty Result");
			throw new InSufficeientInputException(errorMsgs);
		}
		if (masterFlatFloorBlockSiteData != null && masterFlatFloorBlockSiteData.size() > 0) {
			siteId = masterFlatFloorBlockSiteData.get(0).getSiteId();
		} else {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while getting site details - Empty Result");
			throw new InSufficeientInputException(errorMsgs);
		}
		try {
			List<MenuLevelMappingPojo> levelMappingPojos = bookingFormServiceDaoImpl.getMenuLevelMappingId(customerBookingFormInfo);
			flatBookId = bookingFormServiceDaoImpl.saveFlatBooking(mapper.FlatBookingInfoToFlatBookingPojo(customerBookingFormInfo, customerId, flatId,Util.isNotEmptyObject(levelMappingPojos.get(0).getMenuLevelMappingId())?levelMappingPojos.get(0).getMenuLevelMappingId():0l,Util.isNotEmptyObject(customerBookingFormInfo.getEmployeeId())?customerBookingFormInfo.getEmployeeId():0l));
			
			// updating EoiApplicable and EoiSequence Number
			customerBookingFormInfo.getFlatBookingInfo().setFlatId(flatId);
			customerBookingFormInfo.getFlatBookingInfo().setFlatBookingId(flatBookId);//Assigned flat book id value by Aniket
			bookingFormServiceDaoImpl.updateFlatDetails(customerBookingFormInfo.getFlatBookingInfo(),Status.ACTIVE.getStatus(),true);
			
			//flatBookId = bookingFormServiceDaoImpl.saveFlatBooking(mapper.FlatBookingInfoToFlatBookingPojo(customerBookingFormInfo.getFlatBookingInfo(), customerId, flatId));
			/* Inserting Statistics in BOOKING_STATUS_CHANGED_DTLS Table */
		    BookingFormRequest bookingFormRequestInsertStatistics = new BookingFormRequest();
		    bookingFormRequestInsertStatistics.setEmpId(customerBookingFormInfo.getEmployeeId());
		    //bookingFormRequestInsertStatistics.setActualStatusId(bookingStatus);
		    bookingFormRequestInsertStatistics.setStatusId(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerAppBookingApproval())?customerBookingFormInfo.getCustomerAppBookingApproval()?Status.ACTIVE.status:Status.PENDING.status:Status.PENDING.status);
		    bookingFormRequestInsertStatistics.setFlatBookingId(flatBookId);
		    bookingFormRequestInsertStatistics.setRemarks(Status.CREATE.getDescription());
		    saveBookingChangedStatus(bookingFormRequestInsertStatistics); 
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			e.printStackTrace();
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving new flat booking details - ");
			logger.info("Error occured while saving new flat booking details - " + e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		try {
			logger.info(
					"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer professional details ***");
			ProfessionalDetailsPojo professionalDetailsPojo = bookingFormMapper
					.professionalInfoToProfessionalDetailsPojo(customerBookingFormInfo.getProfessionalInfo());
			longProfessionId = bookingFormServiceDaoImpl.saveCustomerProfessionalDetails(professionalDetailsPojo);
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving customer professional details - ");
			logger.info("Error occured while saving customer professional details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		try {
			logger.info(
					"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer booking info details ***");
			
			/*  updating Authorization Type */
			customerBookingFormInfo = updateTdsAutherizationType(customerBookingFormInfo);
		
			String stateName = bookingFormMapper.getStateName(masterFlatFloorBlockSiteData.get(0).getStateId()).toUpperCase() + "_TERMSANDCONDITIONS_VERSION";
			customerBookingFormInfo.getCustomerBookingInfo()
					.setTermsConditionFileName(
							new ResponceCodesUtil().getApplicationProperties()
									.getProperty(stateName)
									+ "-TermsAndConditions");
			custBookInfoPojo = mapper.custBookInfoToCustBookInfoPojo(customerBookingFormInfo.getCustomerBookingInfo(),
					bookingFormServiceDaoImpl, flatBookId, customerId, longProfessionId);
			custBookInfoId = bookingFormServiceDaoImpl.saveCustBookInfo(custBookInfoPojo);
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving customer book info details - ");
			logger.info("Error occured while saving customer book info details - "+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		try {
			logger.info(
					"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving contact info details ***");
			ContactInfoPojo contactInfoPojo = new ContactInfoPojo();
			BeanUtils.copyProperties(custBookInfoPojo, contactInfoPojo);
			bookingFormServiceDaoImpl.saveContactInfo(contactInfoPojo);
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving customer contact info details - ");
			logger.info("Error occured while saving customer contact info details - "+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		try {
			customerBookingFormInfo.getCustomerApplicationInfo().setSiteId(siteId);
			customerBookingFormInfo.getCustomerApplicationInfo().setBlockId(blockId);
			customerBookingFormInfo.getCustomerApplicationInfo().setUnit(flatId);
			customerBookingFormInfo.getCustomerApplicationInfo().setStmId(custBookInfoPojo.getSalesTeamEmpId());
			customerBookingFormInfo.getCustomerApplicationInfo().setLeadId(custBookInfoPojo.getSalesTeamLeadId());
			customerBookingFormInfo.getCustomerApplicationInfo().setFlatBookId(flatBookId);
			logger.info(
					"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer application details ***");
			bookingFormServiceDaoImpl.saveCustomerApplication(mapper.CustomerApplicationInfoToCustomerApplicationPojo(
					customerBookingFormInfo.getCustomerApplicationInfo()));
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving customer application info details - ");
			logger.info("Error occured while saving customer application info details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		try {
			List<CustomerKYCDocumentSubmitedInfo> customerKYCSubmitedInfo = customerBookingFormInfo
					.getCustomerKYCSubmitedInfo();
			for (CustomerKYCDocumentSubmitedInfo submitedKYCDetails : customerKYCSubmitedInfo) {
				Long kycId = bookingFormMapper.getKYCDocumentId(submitedKYCDetails.getDocName());
				if (kycId == null || kycId.equals(0L) || kycId == 0) {
					errorMsgs.add("Error occured while saving customer KYC document details -- Invalid KYC doc name -- "
							+ submitedKYCDetails.getDocName());
					throw new InSufficeientInputException(errorMsgs);
				}
				submitedKYCDetails.setDocumentId(kycId);
				submitedKYCDetails.setFlatBookId(flatBookId);
				submitedKYCDetails.setCustBookInfoId(custBookInfoId);// (customerId);
				logger.info(
						"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer submitted KYC details ***");
				bookingFormServiceDaoImpl.saveCustomerKycSubmitted(
						mapper.CustomerKYCDocumentSubmitedInfoToCustomerKycSubmittedDocPojo(submitedKYCDetails));
			}
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(1, bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving customer KYC document details - ");
			logger.info("Error occured while saving customer KYC document details - "+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		/* Adding FlatCost Details */
		try {
			logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving flat cost details ***");
			/* Malladi Changes */
			FlatCostInfo flatCost = customerBookingFormInfo.getFlatBookingInfo().getFlatCost();
			if(Util.isNotEmptyObject(flatCost)) {
				flatCost.setFlatBookingId(flatBookId);
			}
			Long flatCostId = bookingFormServiceDaoImpl.saveFlatCost(mapper.FlatCostInfoToFlatCostPojo(customerBookingFormInfo.getFlatBookingInfo().getFlatCost(), flatId));
			
			/* Adding flat wise amenities and its real price  */
			addFlatWiseAmenities(customerBookingFormInfo,bookingFormRequest,errorMsgs,mapper,siteId,flatId,flatCostId);
			   
			/*updating flat cost for financial module and customer scheme */
			updateFinancialModuleFlatCostAndScheme(customerBookingFormInfo.getFlatBookingInfo(),customerSchemeInfo,customerBookingFormInfo,"saveBookingDetails");
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(1, bookingFormSavedStatus.getCustName());
			 
			if(e.getMessage()!=null && e.getMessage().contains("Scheme not found,")) {
				errorMsgs.add("Error occured while saving scheme details - "+e.getMessage());
			}else {
				errorMsgs.add("Error occured while saving flat cost details - ");
			}
			logger.info("Error occured while saving flat cost details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}

		List<AddressPojo> custAddressPojos = bookingFormMapper
				.addressInfosToAddressPojos(customerBookingFormInfo.getAddressInfos());
		List<AddressMappingPojo> custAddressMappingPojos = bookingFormMapper
				.addressInfosToAddressMappingPojos(customerBookingFormInfo.getAddressInfos(), custBookInfoId);
		
		for (int i = 0; i < custAddressPojos.size(); i++) {
			try {
				logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer address details **");
			 /*	BookingFormRequest info = new BookingFormRequest();
				info.setCityId(custAddressPojos.get(i).getCityId());
				info.setStateId(custAddressPojos.get(i).getStateId());
		    	Integer count = bookingFormServiceDaoImpl.ValidateCityState(info); */
				if(true) {
					Long addrId = bookingFormServiceDaoImpl.saveAddressDetails(custAddressPojos.get(i));
					try {
						logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer address mapping details **");
						custAddressMappingPojos.get(i).setAddressId(addrId);
						bookingFormServiceDaoImpl.saveAddressmapping(custAddressMappingPojos.get(i));
					} catch (Exception e) {
						// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
						logger.info("Error occured while saving customer address mapping details - " + e.toString());
						errorMsgs.add("Error occured while saving customer address mapping details - ");
						throw new InSufficeientInputException(errorMsgs);
					}
				}else {
					//errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
					//errorMsgs.add(1, bookingFormSavedStatus.getCustName());
					errorMsgs.add("Error occured while saving customer address details - Invalid State and City");
					throw new InSufficeientInputException(errorMsgs);
				}
			} catch (Exception e) {
				logger.error("**** The Exception detailed informtion is ****" , e);
				// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
				errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
				errorMsgs.add(1, bookingFormSavedStatus.getCustName());
				errorMsgs.add("Error occured while saving customer address details - ");
				logger.info("Error occured while saving customer address details -"+e.toString());
				throw new InSufficeientInputException(errorMsgs);
			}
		}
		try {
			logger.info(
					"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer other details ***");
			customerOtherDetailsId = bookingFormServiceDaoImpl
					.saveCustomerOtherDetails(mapper.CustomerOtherDetailInfoToCustomerOtherDetailspojo(
							customerBookingFormInfo.getCustomerOtherDetailsInfo(), flatBookId, employeeTicketDaoImpl,
							mapper.bookingFormRequestToEmployeeTicketRequestInfo(bookingFormRequest)));
			try {
				if (customerBookingFormInfo.getCustomerOtherDetailsInfo().getPoadetailsInfo().getNameOfPOA() != null
						&& !customerBookingFormInfo.getCustomerOtherDetailsInfo().getPoadetailsInfo().getNameOfPOA()
								.equals("")) {
					bookingFormServiceDaoImpl.savePoaHolder(mapper.POADetailsInfoToPoaHolderPojo(
							customerBookingFormInfo.getCustomerOtherDetailsInfo().getPoadetailsInfo(),
							customerOtherDetailsId));
				}
			} catch (Exception e) {
				logger.error("**** The Exception detailed informtion is ****" , e);
				// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
				errorMsgs.add("Error occured while saving POA details - ");
				logger.info("Error occured while saving POA details -"+e.toString());
				throw new InSufficeientInputException(errorMsgs);
			}
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(1, bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving customer other details - ");
			logger.info("Error occured while saving customer other details - "+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}

    	try {
			Long intRefrenceId = bookingFormMapper
					.getRefrenceId(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferenceName());
			if (intRefrenceId == null || intRefrenceId.equals(0L) || intRefrenceId == 0) {
				/*
				errorMsgs.add("Error occured while saving reference details -- Invalid reference name -- "
						+ customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferenceName());
				throw new InSufficeientInputException(errorMsgs);
				*/
			}else {
			Long refrenceTableId = 0L;
			Long refrenceType = 0L;
			if (intRefrenceId == Refrences.EXISTING_OWNER.getId()) {
				CustomerPojo existingCustomerPojo = null;
				logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(existing customer) details ***");
					if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer().getCustomerName()) &&  Util.isNotEmptyObject(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer().getProjectName()) && Util.isNotEmptyObject(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer().getUnitNo())){
					List<CustomerPojo> existingCustomerPojos =  bookingFormServiceDaoImpl.getCustomerDetailsByName(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer().getCustomerName(),customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer().getProjectName(),customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer().getUnitNo());
				    if(Util.isNotEmptyObject(existingCustomerPojos)){
					existingCustomerPojo = existingCustomerPojos.get(0);
					customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer().setCustomerId(Util.isNotEmptyObject(existingCustomerPojo)?Util.isNotEmptyObject(existingCustomerPojo.getCustomerId())?existingCustomerPojo.getCustomerId():0l:0l);
					refrenceTableId = bookingFormServiceDaoImpl.saveReferencesCustomer(mapper.ReferencesCustomerToReferencesCustomerPojo(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesCustomer()));
					refrenceType = MetadataId.REFERENCES_CUSTOMER.getId();
					}
				}
			} else if (intRefrenceId == Refrences.FRIEND_FAMILY.getId()) {
				logger.info(
						"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(friend or family) details ***");
				if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesFriend().getReferenceFreindsorFamilyName())) {
				refrenceTableId = bookingFormServiceDaoImpl.saveReferencesFriend(mapper.ReferencesFriendInfoToReferencesFriendPojo(customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesFriend()));
				refrenceType = MetadataId.REFERENCES_FRIEND.getId();
				}
			} else if (intRefrenceId == Refrences.CHANEL_PARTNER.getId()) {
				logger.info(
						"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(channel partner) details ***");
				ChanelPartnerInfo cpInfo = customerBookingFormInfo.getCustomerOtherDetailsInfo().getChannelPartnerInfo();
				if(Util.isNotEmptyObject(cpInfo.getChannelPartnerCPID()) && Util.isNotEmptyObject(cpInfo.getChannelPartnerCompanyName())) {
				List<ChannelPartnerMasterPojo> channelPartnerMasterPojos = bookingFormServiceDaoImpl.getChannelPartnerMasterByIdName(cpInfo.getChannelPartnerCPID(),cpInfo.getChannelPartnerCompanyName());
				if (channelPartnerMasterPojos == null || channelPartnerMasterPojos.size() == 0) {
					// List<String> errorMsgs = new ArrayList<String>();
					//errorMsgs.add("The Insufficient Input is given for requested service -- Invalid Channel Partner Details");
					//throw new InSufficeientInputException(errorMsgs);
					//ChannelPartnerMasterPojo pojo = new ChannelPartnerMasterPojo();
					//refrenceTableId = bookingFormServiceDaoImpl.saveChannelPartnerMaster(pojo);
				}else {
				refrenceTableId = channelPartnerMasterPojos.get(0).getChannelPartnerId();
				refrenceType = MetadataId.CHANEL_PARTNER.getId();
				}
				}
			}else { 
			 logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(others) details ***");
			 refrenceTableId = intRefrenceId; 
			 refrenceType =MetadataId.REFRENCE_MASTER.getId(); 
			}
			if(Util.isNotEmptyObject(refrenceTableId) && Util.isNotEmptyObject(refrenceType)) {
			customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesMappingInfo().setTypeId(refrenceTableId);
			customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesMappingInfo().setType(refrenceType);
			customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesMappingInfo()
					.setCustOtherId(customerOtherDetailsId);
			logger.info(
					"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer reference details ***");
			bookingFormServiceDaoImpl.saveReferencesMapping(mapper.ReferencesMappingInfoToReferencesMappingPojo(
					customerBookingFormInfo.getCustomerOtherDetailsInfo().getReferencesMappingInfo()));
			}
			}
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(1, bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving reference details - ");
			logger.info("Error occured while saving reference details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		/*** Removing Coaplicant Data insertion when coaplicant data is Empty(null) ***/
		List<CoApplicentDetailsInfo> coApplicentDetails = customerBookingFormInfo.getCoApplicentDetails();
		
	//	-------------------------------------------------------------------------------------------------------
			for (CoApplicentDetailsInfo coApplicentDetailsInfo : coApplicentDetails) {
		    if (isCoaaplicant(coApplicentDetailsInfo)) {
				Long coAppId, coAppProfessionId, coAppBookInfoId;
				try {
					List<Co_ApplicantPojo> listCoApplicantsByPanCard = bookingFormServiceDaoImpl
							.getCo_ApplicantByPanCard(coApplicentDetailsInfo.getCo_ApplicantInfo().getPancard(),coApplicentDetailsInfo.getCo_ApplicantInfo().getPassport(),null);
					if (listCoApplicantsByPanCard == null || listCoApplicantsByPanCard.size() == 0) {
						logger.info(
								"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving new Co-Applicant details ***");
						coAppId = bookingFormServiceDaoImpl.saveCoApplicant(mapper
								.co_ApplicantInfoToCo_ApplicantPojo(coApplicentDetailsInfo.getCo_ApplicantInfo()));
					} else {
						logger.info(
								"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - getting existing Co-Applicant details ***");						
						coAppId = listCoApplicantsByPanCard.get(0).getCoApplicantId();
						/* updating previous Booking Status for Co-Applicant to pending state */
						Co_ApplicantInfo co_ApplicantInfo = new Co_ApplicantInfo();
						BeanUtils.copyProperties(listCoApplicantsByPanCard.get(0), co_ApplicantInfo);
						co_ApplicantInfo.setStatusId(Status.PENDING.status);
						bookingFormServiceDaoImpl.updateCoApplicantInfo(co_ApplicantInfo);
					}
				} catch (Exception e) {
					// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
					errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					errorMsgs.add(bookingFormSavedStatus.getCustName());
					errorMsgs.add("Error occured while getting existing coapplicant/saving new coapplicant - ");
					logger.info("Error occured while getting existing coapplicant/saving new coapplicant - "+e.toString());
					throw new InSufficeientInputException(errorMsgs);
				}

				try {
					ProfessionalDetailsPojo coAppProfessionalDetailsPojo = bookingFormMapper
							.professionalInfoToProfessionalDetailsPojo(coApplicentDetailsInfo.getProfessionalInfo());
					logger.info(
							"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant professional details ***");
					coAppProfessionId = bookingFormServiceDaoImpl
							.saveCustomerProfessionalDetails(coAppProfessionalDetailsPojo);
				} catch (Exception e) {
					logger.error("**** The Exception detailed informtion is ****" , e);
					// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
					errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					errorMsgs.add(bookingFormSavedStatus.getCustName());
					errorMsgs.add("Error occured while saving coApp professional details - ");
					logger.info("Error occured while saving coApp professional details - "+e.toString());
					throw new InSufficeientInputException(errorMsgs);
				}
				try {
					logger.info(
							"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant booking info details ***");
					coAppBookInfoId = bookingFormServiceDaoImpl.saveCoAppBookInfo(mapper.coAppBookInfoToCoAppBookInfoPojo(coApplicentDetailsInfo.getCoApplicentBookingInfo(),custBookInfoId, coAppId, coAppProfessionId,coApplicentDetailsInfo));
				} catch (Exception e) {
					logger.error("**** The Exception detailed informtion is ****" , e);
					// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
					errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					errorMsgs.add(bookingFormSavedStatus.getCustName());
					errorMsgs.add("Error occured while saving coApp book info details - ");
					logger.info("Error occured while saving coApp book info details - "+e.toString());
					throw new InSufficeientInputException(errorMsgs);
				}

				List<AddressPojo> coAppAddressPojos = bookingFormMapper
						.addressInfosToAddressPojos(coApplicentDetailsInfo.getAddressInfos());
				List<AddressMappingPojo> coAppAddressMappingPojos = bookingFormMapper
						.addressInfosToAddressMappingPojos(coApplicentDetailsInfo.getAddressInfos(), coAppBookInfoId);
				for (int i = 0; i < coAppAddressPojos.size(); i++) {
					try {
						logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant address details **");
						/*
						BookingFormRequest info = new BookingFormRequest();
						info.setCityId(coAppAddressPojos.get(i).getCityId());
						info.setStateId(coAppAddressPojos.get(i).getStateId());
						Integer count = bookingFormServiceDaoImpl.ValidateCityState(info); 
						*/
						if(true) {
							Long addrId = bookingFormServiceDaoImpl.saveAddressDetails(coAppAddressPojos.get(i));
							try {
								logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant address mapping details **");
								coAppAddressMappingPojos.get(i).setAddressId(addrId);
								bookingFormServiceDaoImpl.saveAddressmapping(coAppAddressMappingPojos.get(i));
							}catch (Exception e) {
								// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
								errorMsgs.add("Error occured while saving coApp address mapping details - ");
								logger.info("Error occured while saving coApp address mapping details -"+e.toString());
								throw new InSufficeientInputException(errorMsgs);
							 }
						}else {
							//errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
							//errorMsgs.add(1, bookingFormSavedStatus.getCustName());
							errorMsgs.add("Error occured while saving customer address details - Invalid State and City");
							logger.info("Error occured while saving customer address details - Invalid State and City");
							throw new InSufficeientInputException(errorMsgs);
						}
					} catch (Exception e) {
						logger.error("**** The Exception detailed informtion is ****" , e);
						// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
						errorMsgs.add(0, bookingFormSavedStatus.getLeadId() + "");
						errorMsgs.add(1, bookingFormSavedStatus.getCustName());
						errorMsgs.add("Error occured while saving coApp address details - ");
					    logger.info("Error occured while saving coApp address details - "+e.toString());
						throw new InSufficeientInputException(errorMsgs);
					}
				}
			}
		}
//--------------------------------------------------------------------------------------------------------------------------------			
	/* Checklist Information */	
	List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = new ArrayList<>();
	/*  SalesHead checkList  */
		try {
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListSalesHead())) {
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListSalesHead().getCustomerCheckListVerification();
			for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
				// Long departmentId =
				// bookingFormMapper.getDepartmentId(objCustomerCheckListVerificationInfo.getDeparmentName());
				
				/* if checklist name is empty we didn't store checklist into the database. */ 
				if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
				Long departmentId = Department.SALES.getId();
				bookingFormRequest.setDeptId(departmentId);
				bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
				Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
				if (ckId == null || ckId.equals(0L) || ckId == 0) {
					errorMsgs.add("Error occured while saving customer Sales Head checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
					throw new InSufficeientInputException(errorMsgs);
				}
				bookingFormRequest.setCheckListId(ckId);
				List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
				objCustomerCheckListVerificationInfo.setCustId(customerId);
				objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
				objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
				objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
				logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist verification details ***");
				bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));
				}
			}
			 /* Authorized signatuer names is null we didnt save these details. */
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListSalesHead().getAuthorizedSignatoryName())  ||  Util.isNotEmptyObject(customerBookingFormInfo.getCheckListSalesHead().getProjectSalesheadName())){
			List<EmployeePojo> salesHeadAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getAuthorizedSignatoryName(),null, new Department[] {Department.SALES,Department.MANAGEMENT});
			List<EmployeePojo> salesHeadEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getProjectSalesheadName(), null,Department.SALES);
			customerBookingFormInfo.getCheckListSalesHead().setProjectSalesheadId(Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos.get(0))?salesHeadEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			customerBookingFormInfo.getCheckListSalesHead().setCustomerId(customerId);
																																	
			customerBookingFormInfo.getCheckListSalesHead().setAuthorizedSignatoryId(Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos.get(0))?salesHeadAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			customerBookingFormInfo.getCheckListSalesHead().setFlatBookingId(flatBookId);
			logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist details ***");
			bookingFormServiceDaoImpl.saveCheckListSalesHead(mapper.CheckListSalesHeadInfoToCheckListSalesHeadPojo(customerBookingFormInfo.getCheckListSalesHead()));
			}
		  }	
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving saleshead checklist details - ");
			logger.info("Error occured while saving saleshead checklist details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
  		
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		/* CRM checklist */
		try {
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListCRM())){
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListCRM().getCustomerCheckListVerification();
			for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
				/* if checklist name is empty we didn't store checklist into the database. */
				if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
				// Long departmentId =
				// bookingFormMapper.getDepartmentId(objCustomerCheckListVerificationInfo.getDeparmentName());
				Long departmentId = Department.CRM.getId();
				bookingFormRequest.setDeptId(departmentId);
				bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());

				Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
				if (ckId == null || ckId.equals(0L) || ckId == 0) {
					errorMsgs.add("Error occured while saving customer CRM checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
					throw new InSufficeientInputException(errorMsgs);
				}
				bookingFormRequest.setCheckListId(ckId);
				List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
				objCustomerCheckListVerificationInfo.setCustId(customerId);
				objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
				objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
				objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
				logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving CRM checklist verification details ***");
				bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));
				}
			}
			/* Authorized signater names is null we didnt save these details.  */ 
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListCRM().getCrmVerifiedByName()) || Util.isNotEmptyObject(customerBookingFormInfo.getCheckListCRM().getAuthorizedSignatoryeName()) ) {
			List<EmployeePojo> crmEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getCrmVerifiedByName(), null,new Department[] { Department.CRM,Department.CRM_MIS});
			List<EmployeePojo> crmAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getAuthorizedSignatoryeName(), null,new Department[] {Department.MANAGEMENT});
			customerBookingFormInfo.getCheckListCRM().setCrmEmpID(Util.isNotEmptyObject(crmEmployeeDetailsPojos)?Util.isNotEmptyObject(crmEmployeeDetailsPojos.get(0))?crmEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			customerBookingFormInfo.getCheckListCRM().setCustomerId(customerId);
																														 
			customerBookingFormInfo.getCheckListCRM().setAuthorizedSignatoryeId(Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos.get(0))?crmAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			customerBookingFormInfo.getCheckListCRM().setFlatBookingId(flatBookId);
			logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving CRM checklist details ***");
			bookingFormServiceDaoImpl.saveChecklistCrm(mapper.CheckListCRMInfoToChecklistCrmPojo(customerBookingFormInfo.getCheckListCRM()));
			}
		  }	
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving CRM checklist details - ");
			logger.info("Error occured while saving CRM checklist details - "+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}

		/* CheckListLegalOfficier verification */
		try {
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListLegalOfficer())) {
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListLegalOfficer().getCustomerCheckListVerification();
			for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
				/* if checklist name is empty we didn't store checklist into the database. */
				if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
				Long departmentId = Department.LEGAL.getId();
				bookingFormRequest.setDeptId(departmentId);
				bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());

				Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
				if (ckId == null || ckId.equals(0L) || ckId == 0) {
					errorMsgs.add("Error occured while saving customer Legal officer checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
					throw new InSufficeientInputException(errorMsgs);
				}
				bookingFormRequest.setCheckListId(ckId);
				// bookingFormRequest.setCheckListId(mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()));
				List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
				objCustomerCheckListVerificationInfo.setCustId(customerId);
				objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
				objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
				objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
				logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer checklist verification details ***");
				bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));
				}	
			}
			// --------------------------------------------------------------------------------------------------------------------
			/* Coapplicant CheckListLegalOfficier verification */
			//if (validateCoAplicantDetails(coApplicentDetails)) {
				List<CoApplicantCheckListVerificationInfo> CoApplicantCheckListVerificationInfo = customerBookingFormInfo.getCheckListLegalOfficer().getCoappCheckListApp();
				
				for (CoApplicantCheckListVerificationInfo objCoApplicantCheckListVerificationInfo : CoApplicantCheckListVerificationInfo) {
					
					if(Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCheckListStatus()) &&  (Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCoapplicentPanCard()) || Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCoapplicentPassport()))){
					if(validateCoAplicantCheckListVerificationDetails(objCoApplicantCheckListVerificationInfo)) {
					bookingFormRequest.setDeptId(Department.LEGAL.getId());
					bookingFormRequest.setMetadataId(MetadataId.APPLICANT1.getId());

					Long ckId = mapper.getCKId(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
					if (ckId == null || ckId.equals(0L) || ckId == 0) {
						errorMsgs.add("Error occured while saving co applicant legal officer checklist details -- Invalid Checklist name -- "+ objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						throw new InSufficeientInputException(errorMsgs);
					}
					bookingFormRequest.setCheckListId(ckId);
					// bookingFormRequest.setCheckListId(mapper.getCKId(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName()));
					List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl
							.getCheckListDepartmentMapping(bookingFormRequest);
					List<Co_ApplicantPojo> applicantPojos = bookingFormServiceDaoImpl.getCo_ApplicantByPanCard(objCoApplicantCheckListVerificationInfo.getCoapplicentPanCard(),objCoApplicantCheckListVerificationInfo.getCoapplicentPassport(),null);
					objCoApplicantCheckListVerificationInfo.setCoApplicantId(applicantPojos.get(0).getCoApplicantId());
					objCoApplicantCheckListVerificationInfo.setCheckListDeptMappingId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
					objCoApplicantCheckListVerificationInfo.setFlatBookId(flatBookId);
					objCoApplicantCheckListVerificationInfo.setCustId(customerId);
					logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer CoApplicant checklist verification details ***");
					bookingFormServiceDaoImpl.saveCoApplicationCheckListVerification(mapper.CoApplicantCheckListVerificationInfoToCoApplicantCheckListVerificationPojo(objCoApplicantCheckListVerificationInfo));
				  }
				//}
				}		
			}
			// -----------------------------------------------------------------------------------------------------------------------------------------------
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListLegalOfficer().getLegalOfficer()) || Util.isNotEmptyObject(customerBookingFormInfo.getCheckListLegalOfficer().getAuthorizedSignatoryName())) {	
			List<EmployeePojo> legalEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getLegalOfficer(), null,new Department[] {Department.LEGAL});
			List<EmployeePojo> legalAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getAuthorizedSignatoryName(),null, new Department[] {Department.MANAGEMENT});
			customerBookingFormInfo.getCheckListLegalOfficer().setEmpId(Util.isNotEmptyObject(legalEmployeeDetailsPojos)?Util.isNotEmptyObject(legalEmployeeDetailsPojos.get(0))?legalEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			customerBookingFormInfo.getCheckListLegalOfficer().setAuthorizedSignatoryId(Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos.get(0))?legalAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			customerBookingFormInfo.getCheckListLegalOfficer().setCustomerId(customerId);
			customerBookingFormInfo.getCheckListLegalOfficer().setFlatBookingId(flatBookId);
			logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer checklist details ***");
			bookingFormServiceDaoImpl.saveCheckListLegalOfficer(mapper.CheckListLegalOfficerInfoToCheckListLegalOfficerPojo(customerBookingFormInfo.getCheckListLegalOfficer()));
			}
		  }	
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving legal officer checklist details - ");
			logger.info("Error occured while saving legal officer checklist details - " + e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		/* Registration checklist */
		try {
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration())) {
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListRegistration().getCustomerCheckListVerification();
			for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
				/* if checklist name is empty we didn't store checklist into the database. */
				if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
				// Long departmentId =
				// bookingFormMapper.getDepartmentId(objCustomerCheckListVerificationInfo.getDeparmentName());
				Long departmentId = Department.MANAGEMENT.getId();
				bookingFormRequest.setDeptId(departmentId);
				bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());

				Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
				if (ckId == null || ckId.equals(0L) || ckId == 0) {
					errorMsgs.add("Error occured while saving customer registration checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
					throw new InSufficeientInputException(errorMsgs);
				}
				bookingFormRequest.setCheckListId(ckId);
				// bookingFormRequest.setCheckListId(mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()));
				List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
				objCustomerCheckListVerificationInfo.setCustId(customerId);
				objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
				objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
				objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
				logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving registration checklist verification details ***");
				bookingFormServiceDaoImpl.saveCustChecklistVerification(
				mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));
				}
			}
			
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getLegalOfficerEmpName()) && Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getAccountsExecutiveEmpName()) && Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getAuthorizedSignatureName())) {
			EmployeePojo regLegalEmployeeDetailsPojo = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getLegalOfficerEmpName(),null,new Department[] {Department.LEGAL}).get(0);
			EmployeePojo regExecEmployeeDetailsPojo = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAccountsExecutiveEmpName(), null, new Department[] {Department.ACCOUNTS}).get(0);
			EmployeePojo regAuthorizedEmployeeDetailsPojo = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAuthorizedSignatureName(),null,new Department[]{Department.MANAGEMENT}).get(0);
			customerBookingFormInfo.getCheckListRegistration().setLegalOfficerEmpId(regLegalEmployeeDetailsPojo.getEmployeeId());
			customerBookingFormInfo.getCheckListRegistration().setAccountsExecutiveEmpid(regExecEmployeeDetailsPojo.getEmployeeId());
			customerBookingFormInfo.getCheckListRegistration().setAuthorizedSignatureId(regAuthorizedEmployeeDetailsPojo.getEmployeeId());
			customerBookingFormInfo.getCheckListRegistration().setCustomerId(customerId);
			customerBookingFormInfo.getCheckListRegistration().setFlatBookingId(flatBookId);
			logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving registration checklist details ***");
			bookingFormServiceDaoImpl.saveCheckListRegistration(mapper.CheckListRegistrationInfoToCheckListRegistrationPojo(customerBookingFormInfo.getCheckListRegistration()));
			bookingFormSavedStatus.setStatus(HttpStatus.success.getDescription());
			}
			}	
		} catch (Exception e) {
			logger.error("**** The Exception detailed informtion is ****" , e);
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving registration checklist details - ");
			logger.info("Error occured while saving registration checklist details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		/* Salesforce New Changes i.e Approving booking record and updating old booking Id data with New Booking Id */
		try {
			List<FlatBookingPojo> flatBookingPojoList = null;
			FlatBookingInfo flatBookingInfo = customerBookingFormInfo.getFlatBookingInfo();
		    /* Approving the new booking record */
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerAppBookingApproval()) && customerBookingFormInfo.getCustomerAppBookingApproval()) {
				bookingFormRequest.setCustomerId(customerId);
			    bookingFormRequest.setFlatBookingId(flatBookId);
			    bookingFormRequest.setActionStr("approve");
			    bookingFormRequest.setStatusId(Status.ACTIVE.getStatus());
			    bookingFormRequest.setRequestUrl("actionBookingDetails");
			    
				//current booking details
				bookingFormRequest.setFlatBookingId(flatBookId);
				bookingFormRequest.setCustomerId(customerId);
				bookingFormRequest.setCustBookInfoId(custBookInfoId);
				bookingFormRequest.setFlatId(flatId);
				bookingFormRequest.setFlatNo(flatNo);
				bookingFormRequest.setBlockId(blockId);
				bookingFormRequest.setSiteId(siteId);
				bookingFormRequest.setStatusId(bookingFormRequest.getStatusId());
				if(Util.isNotEmptyObject(customerSchemeInfo) && Util.isNotEmptyObject(customerSchemeInfo.get(0)) && Util.isNotEmptyObject(customerSchemeInfo.get(0).getSchemeName())) {
					bookingFormRequest.setGenerateDemandNoteForBooking("true");
					//false means don't generate demand note for booking
				} else {
					bookingFormRequest.setGenerateDemandNoteForBooking("false");
				}
			    putActionBookingForm(bookingFormRequest);
			    
			  
			}
			
			//123
			//sending home loan mail after crating 
			if(Util.isNotEmptyObject(siteId)&& Util.isNotEmptyObject(flatNo)&&
			 Util.isNotEmptyObject(flatBookId)&&Util.isNotEmptyObject(blockId)) {
			final CustomerPropertyDetailsPojo customerPropertyDetailsPojo=new CustomerPropertyDetailsPojo();
			customerPropertyDetailsPojo.setSiteId(siteId);
			customerPropertyDetailsPojo.setFlatNo(flatNo);
			customerPropertyDetailsPojo.setSiteName(com.sumadhura.employeeservice.enums.Site.getSiteNameById(siteId));
			customerPropertyDetailsPojo.setFlatBookingId(flatBookId);
			customerPropertyDetailsPojo.setBlockId(blockId);
			 final CustomerInfo customerInfo2 = new CustomerInfo();
			 customerInfo2.setSiteId(siteId);
			
				customerInfo2.setBlockId(blockId);
				customerInfo2.setFlatBookingId(flatBookId);
				customerInfo2.setSiteName(com.sumadhura.employeeservice.enums.Site.getSiteNameById(siteId));
			 new Thread(new Runnable() {
					@Override
					public void run() {
					//	logger.info("***** Control inside the BookingFormServiceImpl.putActionBookingForm() homes loan if condition2 *****"+customerInfo2);
						try {
						sendMailToBankerForLoan(customerInfo2,customerPropertyDetailsPojo);
						} catch (DefaultBankerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					}
			  }).start();
			}
			
			
			/* Updating Old Booking Id with New Booking Id only when two flats are belongs to same site */
			//flatBookingInfo = customerBookingFormInfo.getFlatBookingInfo();
			if(Util.isNotEmptyObject(flatBookingInfo.getOldBookingName())) {
				if(Util.isEmptyObject(flatBookingPojoList)) {
					flatBookingPojoList = bookingFormServiceDaoImpl.getOldFlatBookingDetails(flatBookingInfo, ServiceRequestEnum.SALESFORCE_NEW_BOOKING);
				}
				if(Util.isNotEmptyObject(flatBookingPojoList) && Util.isNotEmptyObject(flatBookingPojoList.get(0))) {
					FlatBookingPojo oldFlatBookingPojo = flatBookingPojoList.get(0);
					if(Util.isNotEmptyObject(flatBookId) && Util.isNotEmptyObject(customerId) && Util.isNotEmptyObject(custBookInfoId)
						&& Util.isNotEmptyObject(oldFlatBookingPojo.getFlatBookingId()) && Util.isNotEmptyObject(oldFlatBookingPojo.getCustomerId()) 
						&& Util.isNotEmptyObject(oldFlatBookingPojo.getCustBookInfoId()) && Util.isNotEmptyObject(siteId)
						&& siteId.equals(oldFlatBookingPojo.getSiteId())) {
						BookingFormRequest newBookingrequest = new BookingFormRequest();
						newBookingrequest.setFlatBookingId(flatBookId);
						newBookingrequest.setCustomerId(customerId);
						newBookingrequest.setCustBookInfoId(custBookInfoId);
						newBookingrequest.setFlatId(flatId);
						newBookingrequest.setBlockId(blockId);
						newBookingrequest.setSiteId(siteId);
						updateNewFlatBookingIdInOtherModules(oldFlatBookingPojo, newBookingrequest);						
					}
				}
			}
		}catch(Exception ex) {
			logger.error("**** The Exception detailed informtion is ****" , ex);
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while updating previous data");
			logger.info("Error occured while updating previous data -"+ex.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
		bookingFormSavedStatus.setStatus(HttpStatus.success.getDescription());
		return bookingFormSavedStatus;
	}

	private void updateNewFlatBookingIdInOtherModules(FlatBookingPojo oldFlatBookingPojo,BookingFormRequest newBookingrequest) {
		logger.info("*** The control is inside of the updateNewBookingIdInOtherModules in BookingFormServiceImpl ***"+oldFlatBookingPojo);
		/* Ticketing Module and App Registration Table */
		if(newBookingrequest.getCustomerId().equals(oldFlatBookingPojo.getCustomerId())) {
			/* Updating New Flat Book Id in Ticket Table */
			bookingFormServiceDaoImpl.updateNewFlatBookingIdInTicketDetails(oldFlatBookingPojo,newBookingrequest,MetadataId.FLAT_BOOKING);
			/* Updating minimum record status to active in App Registration table */
			bookingFormServiceDaoImpl.updateAppRegistrationStatus(newBookingrequest);
		}else {
			/* Updating New Flat Book Id in Ticket Table */
			bookingFormServiceDaoImpl.updateNewFlatBookingIdInTicketDetails(oldFlatBookingPojo,newBookingrequest,MetadataId.FLAT_BOOKING);
			/* Updating New Customer Id in Ticket Table */
			bookingFormServiceDaoImpl.updateNewFlatBookingIdInTicketDetails(oldFlatBookingPojo,newBookingrequest,MetadataId.CUSTOMER);
			/* Updating Ticket Conversation From Id */
			bookingFormServiceDaoImpl.updateNewCustomerIdInTicketConversation(newBookingrequest,Status.RAISED);
			/* Updating Ticket Conversation To Id */
			bookingFormServiceDaoImpl.updateNewCustomerIdInTicketConversation(newBookingrequest,Status.RECEIVED);
		}
		
		/*
		if(!newBookingrequest.getSiteId().equals(oldFlatBookingPojo.getSiteId()) || !newBookingrequest.getBlockId().equals(oldFlatBookingPojo.getBlockId())) {
			// Getting All Active Tickets on Updated Booking Id 
			EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
			employeeTicketRequestInfoObj.setRequestUrl("getTicket");
			employeeTicketRequestInfoObj.setFlatBookingId(newBookingrequest.getFlatBookingId());
			List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfoObj, Status.ACTIVE);
			for(TicketPojo ticketPojo : ticketPojos) {
				if(Util.isNotEmptyObject(ticketPojo) && Util.isNotEmptyObject(ticketPojo.getTicketId()) && Util.isNotEmptyObject(ticketPojo.getTicketTypeId())) {
					// Updating Ticket Owner 
					employeeTicketDaoImpl.updateTicketOwnerForNewFlat(ticketPojo);
				}
			}
		} */
		
		/* Insert into Ticket Statistics Table */
		insertTicketStatistics(newBookingrequest);
		
		/* Messenger Module */
		/* Updating New Flat Book Id in Messenger Table */
		bookingFormServiceDaoImpl.updateNewFlatBookingIdInMessengerDetails(oldFlatBookingPojo,newBookingrequest,Status.CREATED);
		/* Updating New Flat Book Id in Messenger Conversation Table */
		bookingFormServiceDaoImpl.updateNewFlatBookingIdInMessengerDetails(oldFlatBookingPojo,newBookingrequest,Status.RAISED);
		/* Updating New Flat Book Id in Messenger Convers View Status Table */
		bookingFormServiceDaoImpl.updateNewFlatBookingIdInMessengerDetails(oldFlatBookingPojo,newBookingrequest,Status.RECEIVED);
	}
	
	public void insertTicketStatistics(BookingFormRequest newBookingrequest) {
		logger.info("*** The control is inside of the insertTicketStatistics in BookingFormServiceImpl ***"+newBookingrequest);
		/* Getting All Active Tickets on Updated Booking Id for Inserting TicketStatistics */
		EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfoObj.setRequestUrl("getTicket");
		employeeTicketRequestInfoObj.setFlatBookingId(newBookingrequest.getFlatBookingId());
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfoObj, Status.ACTIVE);
		/* Inserting into TicketStatistics table after updating the ticket table */
		for(TicketPojo ticketPojo : ticketPojos) {
			if(Util.isNotEmptyObject(ticketPojo) && Util.isNotEmptyObject(ticketPojo.getTicketId())) {
				EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
				employeeTicketRequestInfo.setTicketId(ticketPojo.getTicketId());
				employeeTicketServiceImpl.insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
			}
		}
	}

	@Override
	public List<CoApplicantCheckListVerificationInfo> getCoAppCheckListVerifications(
			BookingFormRequest bookingFormRequest, Department department, String pancard) {
		List<CoApplicantCheckListVerificationInfo> coAppCheckListVerificationInfos = new ArrayList<>();
		
		List<Co_ApplicantPojo> applicantPojos = bookingFormServiceDaoImpl.getCo_ApplicantByPanCard(pancard,null,null); 
		bookingFormRequest.setCoApplicantId(Util.isNotEmptyObject(applicantPojos)?Util.isNotEmptyObject(applicantPojos.get(0).getCoApplicantId())?applicantPojos.get(0).getCoApplicantId():0l:0l);
		bookingFormRequest.setRequestUrl("insertOrUpdateCoAppChecklistUtil");
		bookingFormRequest.setDeptId(department.getId());
		List<CoApplicantCheckListVerificationPojo> coApplicantCheckListVerificationPojos  = bookingFormServiceDaoImpl.getApplicantChecklistVerification(bookingFormRequest);
		logger.info("*** The coApplicantCheckListVerificationPojo is ***"+coApplicantCheckListVerificationPojos);
		if(Util.isNotEmptyObject(coApplicantCheckListVerificationPojos)) {
		  for(CoApplicantCheckListVerificationPojo pojo : coApplicantCheckListVerificationPojos) {
			bookingFormRequest.setCheckListDeptMapId(pojo.getCheckListDeptMappingId());
			bookingFormRequest.setRequestUrl("checklistDepartmentMappringId");
			List<ChecklistDepartmentMappingPojo> checkListDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
			for (ChecklistDepartmentMappingPojo checkListDepartmentMappingPojo : checkListDepartmentMappingPojos) {
				bookingFormRequest.setCheckListId(checkListDepartmentMappingPojo.getCheckListId());
				List<CheckListPojo> checkListPojos = bookingFormServiceDaoImpl.getCheckList(bookingFormRequest);
				for(CheckListPojo checkListPojo : checkListPojos ) {
					CoApplicantCheckListVerificationInfo coAppCheckListVerificationInfo = bookingFormMapper
							.applicantCheckListVerificationPojo$ChecklistDepartmentMapping$CheckListPojosToCoApplicantCheckListVerificationInfo(
									pojo, checkListDepartmentMappingPojo, checkListPojo, pancard);
					coAppCheckListVerificationInfos.add(coAppCheckListVerificationInfo);
				}
			}
		  }
		}else {
			CoApplicantCheckListVerificationInfo coAppCheckListVerificationInfo = new CoApplicantCheckListVerificationInfo();
			CheckListInfo checkListInfo = new CheckListInfo();
			coAppCheckListVerificationInfo.setCheckListInfo(checkListInfo);
			coAppCheckListVerificationInfos.add(coAppCheckListVerificationInfo);
		}
		return coAppCheckListVerificationInfos;
	}

	@Override
	public int saveBookingChangedStatus(BookingFormRequest bookingFormRequest) {
		logger.info("***** Control inside the BookingFormServiceImpl.saveBookingChangedStatus() *****");
		BookingStatusChangedPojo pojo = bookingFormMapper.constructBookingStatusChangedPojo(bookingFormRequest);
		int result = bookingFormServiceDaoImpl.saveBookingChangedStatus(pojo);
		return result;
	}
	
	/* if booking form is approved successfully we need to send pushnotification and mail to the customer. */
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public List<String> putActionBookingForm(BookingFormRequest bookingFormRequest) throws  SQLInsertionException {
		logger.info("***** Control inside the BookingFormServiceImpl.putActionBookingForm() *****"+bookingFormRequest);
		List<String> errorMsgs = new ArrayList<>();
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo = null;
		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		//employeeFinancialServiceInfo.setSiteId(employeeFinancialTransactionInfo.getSiteId());
		/*employeeFinancialServiceInfo.setBlockIds(employeeFinancialTransactionInfo.getBlockIds()); */
		//employeeFinancialServiceInfo.setFlatIds(employeeFinancialTransactionInfo.getFlatIds());
		employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(new Long[] {bookingFormRequest.getFlatBookingId()}));
		boolean isThisFinalLevel = false;
		
		BookingFormRequest bookingFormRequestCopy = new BookingFormRequest();
		BeanUtils.copyProperties(bookingFormRequest,bookingFormRequestCopy);
		
		Arrays.asList("cancel","Swap","Available","Blocked","Not Open","Price Update","Legal case","PMAY Scheme Eligible","Retained");
		if(true) {
		/* change Customer table status */
		try {
			if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
					bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
				//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
					) {
			  if(checkCustomerAssociatedFlats(bookingFormRequest)) {	
				  bookingFormServiceDaoImpl.updateCustomer(bookingFormRequest,Arrays.asList(bookingFormRequest.getActualStatusId()));
			  }
			}else {
				bookingFormServiceDaoImpl.updateCustomer(bookingFormRequest,Arrays.asList(Status.PENDING.getStatus()));	
			}
		} catch (Exception e) {
			errorMsgs.add("Error occured while updating customer status - " + e.toString());
			logger.error("**** The Exception detailed informtion is ****" , e);
		}
		/* change FlatBooking table status */
		try {
			if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")
					 ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
						bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
						//bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
					) {
				/*  Adding bookingcanceledDate and comments and canceled Employee Id   */
			   bookingFormServiceDaoImpl.updateFlatBooking(bookingFormRequest,bookingFormRequest.getActualStatusId(),true);
			   /*  Disable FlatBooking Table */     
			   bookingFormServiceDaoImpl.updateFlatBooking(bookingFormRequest,bookingFormRequest.getActualStatusId(),false);
			   
			}else {
				 bookingFormServiceDaoImpl.updateFlatBooking(bookingFormRequest,Status.PENDING.getStatus(),false);
			}
		} catch (Exception e) {
			errorMsgs.add("Error occured while updating flat booking status - " + e.toString());
			logger.error("**** The Exception detailed informtion is ****" , e);
		}
		
		/* changing Flatdetails table EoiApplicable and EoiSequenceNumber */
		try {
			if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel") || bookingFormRequest.getActionStr().equalsIgnoreCase("reject")
					 ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
						bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
					//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
					
					) {
				/* updating EoiApplicable and EoiSequence Number */
				FlatBookingInfo flatBookingInfo = new FlatBookingInfo();
				flatBookingInfo.setFlatBookingId(bookingFormRequest.getFlatBookingId());
			 	bookingFormServiceDaoImpl.updateFlatDetails(flatBookingInfo,Status.ACTIVE.getStatus(),false);
			}
		} catch (Exception e) {
			errorMsgs.add("Error occured while updating flat Details status - " + e.toString());
			logger.error("**** The Exception detailed informtion is ****" , e);
		}
		
    	/* chage Flatcost table status */
		try {
			if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel") 
					 ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
						bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
					//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
					) {
				bookingFormServiceDaoImpl.updateFlatCost(bookingFormRequest,bookingFormRequest.getActualStatusId());
			}else {
				bookingFormServiceDaoImpl.updateFlatCost(bookingFormRequest,Status.PENDING.getStatus());	
			}
		} catch (Exception e) {
			errorMsgs.add("Error occured while updating flat cost status - " + e.toString());
			logger.error("**** The Exception detailed informtion is ****" , e);
		}

		List<CustBookInfoPojo> custBookInfoPojos = bookingFormServiceDaoImpl.getCustBookInfo(bookingFormRequest);
		if (custBookInfoPojos != null && custBookInfoPojos.size() > 0) {
			for (CustBookInfoPojo custBookInfoPojo : custBookInfoPojos) {
				/* chage CustBookInfo table status */
				try {
					if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel") 
							 ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
								bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
							//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
							) {
						bookingFormServiceDaoImpl.updateCustBookInfo(bookingFormRequest,bookingFormRequest.getActualStatusId());
					}else {
						bookingFormServiceDaoImpl.updateCustBookInfo(bookingFormRequest,Status.PENDING.getStatus());
					}
				} catch (Exception e) {
					errorMsgs.add("Error occured while updating Cust Book Info status - " + e.toString());
					logger.error("**** The Exception detailed informtion is ****" , e);
				}
				bookingFormRequest.setCustBookInfoId(custBookInfoPojo.getCustBookInfoId());
				/* change CoAppBookInfo table status */
				try {
					if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")
							 ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
								bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
							//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
								bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
								bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
							
							){
					  bookingFormServiceDaoImpl.updateCoAppBookInfo(bookingFormRequest,bookingFormRequest.getActualStatusId());
					}else {
					  bookingFormServiceDaoImpl.updateCoAppBookInfo(bookingFormRequest,Status.PENDING.getStatus());
					}
				} catch (Exception e) {
					errorMsgs.add("Error occured while updating CoApp Book Info status - " + e.toString());
					logger.error("**** The Exception detailed informtion is ****" , e);
				}
				List<CoAppBookInfoPojo> coAppBookInfoPojos = bookingFormServiceDaoImpl
						.getCoAppBookInfo(bookingFormRequest);
				if (coAppBookInfoPojos != null && coAppBookInfoPojos.size() > 0) {
					for (CoAppBookInfoPojo coAppBookInfoPojo : coAppBookInfoPojos) {
						bookingFormRequest.setApplicantId(coAppBookInfoPojo.getCoApplicantId());
						bookingFormRequest.setCoApplicantId(coAppBookInfoPojo.getCoApplicantId());
						/* change CoApplicant table status */
						try {
								if (bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")
										 ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
											bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
										//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
											bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
											bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
										) {
									if (checkCoAplicantStatus(bookingFormRequest)) {
										bookingFormServiceDaoImpl.updateCoApplicant(bookingFormRequest, bookingFormRequest.getActualStatusId());
									}
								} else {
									bookingFormServiceDaoImpl.updateCoApplicant(bookingFormRequest, Status.PENDING.getStatus());
								}
						} catch (Exception e) {
							errorMsgs.add("Error occured while updating CoApp status - " + e.toString());
							logger.error("**** The Exception detailed informtion is ****" , e);
						}
					}
				}  
			}
		} else {
			errorMsgs.add("Error occured while getting Cust Book Info - empty result");
		}
	 }
		employeeFinancialServiceInfo.setCondition(FinEnum.GET_ACTIVE_INACTIVE_FLATS.getName());
		List<Long> listOfBookingStatus = Arrays.asList(Status.ACTIVE.getStatus()
				/*Status.PENDING.getStatus(),Status.SWAP.getStatus(),
				Status.AVAILABLE.getStatus(),Status.BLOCKED.getStatus(),
				Status.NOT_OPEN.getStatus(),Status.PRICE_UPDATE.getStatus(),
				Status.LEGAL_CASE.getStatus(),Status.PMAY_SCHEME_ELIGIBLE.getStatus()
				,Status.RETAINED.getStatus(),Status.ACTIVE.getStatus(),Status.INACTIVE.getStatus()
				,Status.CANCELLED_NOT_REFUNDED.getStatus(),Status.REJECTED.getStatus(),Status.CANCEL.getStatus()*/
				);
		employeeFinancialServiceInfo.setStatusIds(listOfBookingStatus);
		final List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
		
		if(Util.isNotEmptyObject(customerPropertyDetailsPojos)) {
			try {
				customerPropertyDetailsInfo = (CustomerPropertyDetailsInfo) financialMapper.copyPropertiesFromInfoBeanToPojoBean(customerPropertyDetailsPojos.get(0), CustomerPropertyDetailsInfo.class);
			} catch (InstantiationException | IllegalAccessException e) {
				customerPropertyDetailsInfo = null;
				e.printStackTrace();
				logger.error("**** The Exception detailed informtion is ****" , e);
			}
		}
		
		//don't write any code inside method, other than financial
		if(customerPropertyDetailsInfo!=null && (bookingFormRequest.getGenerateDemandNoteForBooking()==null
				|| !bookingFormRequest.getGenerateDemandNoteForBooking().equalsIgnoreCase("False")) ) {
			//if False means don't generate the demand note for booking, for this request
			//if this request from updateApplicant service then not executing these code
			
			if(Status.ACTIVE.getStatus().equals(bookingFormRequest.getStatusId())) {
				bookingFormRequestCopy.setStatusId(Status.ACTIVE.getStatus());//if status id is null then, data is loading using flat id and customer id
			} else {
				bookingFormRequestCopy.setStatusId(null);//if status id is null then, data is loading using flat id and customer id
			}
			
			/*bookingFormRequestCopy.setFlatBookingId(flatBookingId);
			bookingFormRequestCopy.setCustomerId(customerId);
			bookingFormRequestCopy.setEmpId(bookingFormRequest.getEmpId());*/
			List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequestCopy);

			Long customerId, custBookInfoId, longProfessionId, customerOtherDetailsId, flatBookId, flatId, siteId, blockId;
			String flatNo="";
			flatBookId = bookingFormRequest.getFlatBookingId();
			customerId = bookingFormRequest.getCustomerId();
			custBookInfoId = bookingFormRequest.getCustBookInfoId();
			flatId = bookingFormRequest.getFlatId();
			flatNo = bookingFormRequest.getFlatNo();
			blockId = bookingFormRequest.getBlockId();
			siteId = bookingFormRequest.getSiteId();

			FlatBookingInfo flatBookingInfo = new FlatBookingInfo();
			FlatBookingInfo currentflatBookingInfo = new FlatBookingInfo();
			if(Util.isNotEmptyObject(flatBookPojo)) {
				flatBookingInfo.setOldBookingName(flatBookPojo.get(0).getSalesforceOldBookingId());
				currentflatBookingInfo.setFlatId(flatBookPojo.get(0).getFlatId());
				currentflatBookingInfo.setBookingId(flatBookPojo.get(0).getSalesforceBookingId());
			}
			if(Util.isEmptyObject(siteId) || Util.isEmptyObject(blockId) || Util.isEmptyObject(flatId)) {
				//this code is for, loading current booking data, if approving this booking from employee portal
				List<FlatBookingPojo> flatBookingPojos = bookingFormServiceDaoImpl.getOldFlatBookingDetails(currentflatBookingInfo, ServiceRequestEnum.SALESFORCE_UPDATE_BOOKING);
				if(Util.isNotEmptyObject(flatBookingPojos)) {
					flatBookId = flatBookingPojos.get(0).getFlatBookingId();
					customerId = flatBookingPojos.get(0).getCustomerId();
					custBookInfoId = flatBookingPojos.get(0).getCustBookInfoId();
					flatId = flatBookingPojos.get(0).getFlatId();
					flatNo = flatBookingPojos.get(0).getFlatNo();
					blockId = flatBookingPojos.get(0).getBlockId();
					siteId = flatBookingPojos.get(0).getSiteId();
				}
			}
			
			//ACP CODE
					if(Util.isNotEmptyObject(flatBookingInfo.getOldBookingName())) {
						List<FlatBookingPojo> flatBookingPojoList = bookingFormServiceDaoImpl.getOldFlatBookingDetails(flatBookingInfo, ServiceRequestEnum.SALESFORCE_NEW_BOOKING);
						if(Util.isNotEmptyObject(flatBookingPojoList) && Util.isNotEmptyObject(flatBookingPojoList.get(0))) {
							FlatBookingPojo oldFlatBookingPojo = flatBookingPojoList.get(0);
							if(Util.isNotEmptyObject(flatBookId) && Util.isNotEmptyObject(customerId) && Util.isNotEmptyObject(custBookInfoId)
								&& Util.isNotEmptyObject(oldFlatBookingPojo.getFlatBookingId()) && Util.isNotEmptyObject(oldFlatBookingPojo.getCustomerId()) 
								&& Util.isNotEmptyObject(oldFlatBookingPojo.getCustBookInfoId())
								&& Util.isNotEmptyObject(siteId) && siteId.equals(oldFlatBookingPojo.getSiteId())) {
								
								BookingFormRequest oldBookingRequest = new BookingFormRequest();
								oldBookingRequest.setFlatBookingId(oldFlatBookingPojo.getFlatBookingId());
								oldBookingRequest.setCustomerId(oldFlatBookingPojo.getCustomerId());
								oldBookingRequest.setCustBookInfoId(oldFlatBookingPojo.getCustBookInfoId());
								oldBookingRequest.setFlatId(oldFlatBookingPojo.getFlatId());
								oldBookingRequest.setFlatNo(oldFlatBookingPojo.getFlatNo());
								oldBookingRequest.setBlockId(oldFlatBookingPojo.getBlockId());
								oldBookingRequest.setSiteId(oldFlatBookingPojo.getSiteId());
								oldBookingRequest.setStatusId(oldFlatBookingPojo.getStatusId());
								
								BookingFormRequest newBookingRequest = new BookingFormRequest();
								newBookingRequest.setFlatBookingId(flatBookId);
								newBookingRequest.setCustomerId(customerId);
								newBookingRequest.setCustBookInfoId(custBookInfoId);
								newBookingRequest.setFlatId(flatId);
								newBookingRequest.setFlatNo(flatNo);
								newBookingRequest.setBlockId(blockId);
								newBookingRequest.setSiteId(siteId);
								newBookingRequest.setStatusId(bookingFormRequest.getStatusId());
								//customer id and site should same for shifting transaction from old booking to new booking
								if(customerId.equals(oldFlatBookingPojo.getCustomerId()) && siteId.equals(oldFlatBookingPojo.getSiteId())) {
									bookingFormRequestCopy.setOldBookingRequest(oldBookingRequest);
									bookingFormRequestCopy.setNewBookingRequest(newBookingRequest);
								}
								
							}
						}
					}
		
			try {
				if(bookingFormRequestCopy.getRequestUrl()!=null && bookingFormRequestCopy.getRequestUrl().equals("actionBookingDetails") && bookingFormRequestCopy.getActionStr()!=null && bookingFormRequestCopy.getActionStr().equals("approve")) {
					processGenerateDemandNoteDate(null,customerPropertyDetailsInfo,flatBookPojo,customerPropertyDetailsPojos.get(0),bookingFormRequestCopy);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("**** The Exception detailed informtion is ****" , e);
			}
			
		}
		
		try {
			//Home loan mail to Banker code
		//	Integer isMailSent=bookingFormServiceDaoImpl.isMailSentToBankerOnBooking(bookingFormRequest);
			//bookingFormRequestCopy.getRequestUrl()!=null && bookingFormRequestCopy.getRequestUrl().equals("actionBookingDetails") &&
			if( bookingFormRequestCopy.getActionStr()!=null && bookingFormRequestCopy.getActionStr().equals("approve")) {
				logger.info("***** Control inside the BookingFormServiceImpl.putActionBookingForm() homes loan if condition*****"+bookingFormRequestCopy);
				Long customerId, custBookInfoId, longProfessionId, customerOtherDetailsId, flatBookId, flatId, siteId, blockId;
				String flatNo="",siteName = "";
				flatBookId = bookingFormRequest.getFlatBookingId();
				logger.info("flatBookId : "+flatBookId);
				flatNo = bookingFormRequest.getFlatNo();
				siteName = bookingFormRequest.getSiteName();
				blockId = bookingFormRequest.getBlockId();
				siteId = bookingFormRequest.getSiteId();
				if (Util.isEmptyObject(blockId)) {
					blockId = customerPropertyDetailsPojos.get(0).getBlockId();
					logger.info("block id: "+blockId);
				}
				if (Util.isEmptyObject(siteId)) {
					siteId = customerPropertyDetailsPojos.get(0).getSiteId();
					logger.info("siteId id: "+siteId);
				}
			 final CustomerInfo customerInfo2 = new CustomerInfo();
			customerInfo2.setSiteId(siteId);
			customerInfo2.setBlockId(blockId);
			customerInfo2.setFlatBookingId(flatBookId);
			customerInfo2.setSiteName(siteName);
			//sending home loan mail after crating insted of approval
			// new Thread(new Runnable() {
				//	@Override
					//public void run() {
					//	logger.info("***** Control inside the BookingFormServiceImpl.putActionBookingForm() homes loan if condition2 *****"+customerInfo2);
					//	try {
					//		//sendMailToBankerForLoan(customerInfo2,customerPropertyDetailsPojos.get(0));
					//	} catch (DefaultBankerException e) {
					//		// TODO Auto-generated catch block
					//		e.printStackTrace();
					//	}
						/*try {//sending push notification through welcome letter
							sendMailToCustomerForBankLoan(customerPropertyDetailsPojos.get(0),customerInfo2);
						} catch (InSufficeientInputException e) {
							e.printStackTrace();
						}*/
					//}
				// }).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("**** The Exception detailed informtion is ****" , e);
		}
		
		if(Status.ACTIVE.getStatus().equals(bookingFormRequest.getStatusId())) {
			//sendCustomerInfoToLoanBanker(bookingFormRequest,customerPropertyDetailsInfo);
		}
		if(Util.isNotEmptyObject(errorMsgs)) {
			throw new SQLInsertionException(errorMsgs);
		}else {
			/* if booking form is approved successfully we need to send pushnotification and mail to the customer. */
			if (bookingFormRequest.getStatusId().equals(Status.ACTIVE.getStatus())) {
				try {
					Future<Boolean> flag = bookingFormServiceHelper.sendBookingFormCustomerMailAndNotification(bookingFormRequest);
					if(flag.isDone()) {
					logger.info("*** The booking form Approved mail and push notification sent successfully ***");		
					}
				} catch (InformationNotFoundException e) {
					e.printStackTrace();
				}
			/* if booking form is canceled successfully we need to inactive all the tickets on this flat */	
			}else if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
					bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
				//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
					) {	
				
				
				
				if(Util.isNotEmptyObject(bookingFormRequest.getActionStr())&&!bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded")) {

					/* inactivating tickets */
					final EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
							
					/* setting flatbooking Id */
					//employeeTicketRequestInfo.setFlatBookingId(739l);
					employeeTicketRequestInfo.setFlatBookingId(bookingFormRequest.getFlatBookingId());
					Result result = employeeTicketServiceImpl.updateTicketStatusInactive(employeeTicketRequestInfo);
					/*
					if(result.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
					logger.info("*** The Tickets related to that flat is Inactvated sucessfully ***");
					} */
					Thread thread = new Thread() {
						public void run() {
							/* Inserting into ticket statistics table */
							employeeTicketRequestInfo.setRequestUrl("getTicket");
							final List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo,Status.INACTIVE);
							for(TicketPojo ticket : ticketPojos) {
								EmployeeTicketRequestInfo info  = new EmployeeTicketRequestInfo();
								info.setTicketId(Util.isNotEmptyObject(ticket.getTicketId())?ticket.getTicketId():0l);
								/* Inserting TicketStatistics table while updating the ticket table */
								if(Util.isNotEmptyObject(info.getTicketId())) {
									employeeTicketServiceImpl.insertTicketStatisticsInMultithreadedMode(info,Status.INACTIVE);
								} 
							}
						}
					};
					thread.start();
    			}
					
					/* Cancelling Car Parking Slots which are in Alloted and Holded State 
					 * For Cancelled we need to update status in CarParking_Allotment Table and CarParking_Slot 
					 * Getting All CarParkingAllotmentPojoList Based on Flat Book Id */
					CarParkingAllotmentInfo carParkingAllotmentInfo = new CarParkingAllotmentInfo();
					carParkingAllotmentInfo.setFlatBookId(bookingFormRequest.getFlatBookingId());
					carParkingAllotmentInfo.setRequestUrl(MetadataId.EMPLOYEE.name());
					carParkingAllotmentInfo.setEmployeeId(bookingFormRequest.getEmpId());
					List<CarParkingAllotmentSlotPojo> carParkingAllotmentSlotPojoList = null;//carParkingAllotmentDaoImpl.getCustomerCarParkingAllotmentDetails(carParkingAllotmentInfo);
					for(CarParkingAllotmentSlotPojo carParkingAllotmentSlotPojo : carParkingAllotmentSlotPojoList) {
						if(Util.isNotEmptyObject(carParkingAllotmentSlotPojo) && Util.isNotEmptyObject(carParkingAllotmentSlotPojo.getAllotmentId()) 
							&& Util.isNotEmptyObject(carParkingAllotmentSlotPojo.getSlotId())) {
							carParkingAllotmentInfo.setAllotmentId(carParkingAllotmentSlotPojo.getAllotmentId());
							carParkingAllotmentInfo.setSlotId(carParkingAllotmentSlotPojo.getSlotId());
							/* Move Allotment Letter to New Path before cancelling */
							//carParkingAllotmentServiceImpl.moveCancelledCarParkingAllotmentLetterToNewFolder(carParkingAllotmentInfo);
							/* We need to update status to Cancelled in CarParking_Allotment Table */
							//carParkingAllotmentDaoImpl.updateCarParkingAllotmentStatus(carParkingAllotmentInfo, Status.CANCELLED);
							/* We need to update status to open in CarParking_Slot Table */
							//carParkingAllotmentDaoImpl.updateCarParkingSlotStatus(carParkingAllotmentInfo, Status.OPEN);
						}
					}
		    	}
			}
		return errorMsgs;
	}
	
	@Autowired(required = true)
	private ApplyLoanDao applyLoanDao;
	
	@Override
	public void sendMailToCustomerForBankLoan(CustomerPropertyDetailsInfo customerPropertyDetailsPojo, CustomerInfo customerInfo) throws InSufficeientInputException {
		/*String NOTIFICATION_TITLE = "Home Loan !!";
		String NOTIFICATION_BODY = "Your details shared to Bank for Home Loan.";
		String NOTIFICATION_TYPE = "Home Loan";
		String NOTIFICATION_TYPE1 = "Home Loan";*/
		
		Map<String,Object> data = new HashMap<String,Object>();
		logger.info("***** Control inside the BookingFormServiceImpl.sendMailToBankerForLoan() *****");
		BookingFormRequest bookingFormRequest=new BookingFormRequest();
		try {
			bookingFormRequest.setFlatBookingId(customerPropertyDetailsPojo.getFlatBookingId());
			//Integer isMailSent=bookingFormServiceDaoImpl.isMailSentToBankerOnBooking(bookingFormRequest);
			//checking is banker mail email sent or not
			//if(isMailSent.equals(0)) {
				/* creating SiteLevelNotifyRequestDTO and setting whatever values required. */
				SiteLevelNotifyRequestDTO notificationRequest = new SiteLevelNotifyRequestDTO();
				notificationRequest.setFlatIds(Arrays.asList(customerPropertyDetailsPojo.getFlatId()));
				notificationRequest.setSiteIds(Arrays.asList(customerPropertyDetailsPojo.getSiteId()));
				notificationRequest.setEmployeeId(MetadataId.SYSTEM.getId());
				notificationRequest.setRequestAction("BookingForm");
				notificationRequest.setRequestPurpose("HomeLonNotification");
				
				List<BankerList> bankerDetailsList = applyLoanDao.getBankEamilOnBooking(customerInfo);
				if (Util.isNotEmptyObject(bankerDetailsList)) {//ACP added, if banker list not found throw exception
					BankerList bankerDetails = bankerDetailsList.get(0);
					if(Util.isNotEmptyObject(bankerDetails) && Util.isNotEmptyObject(bankerDetails.getBankerName())) {
						data.put("#BankName", bankerDetails.getBankerName());
						notificationRequest.setData(data);
						//notificationRestService.sendProjectNotificationsForApprovals(notificationRequest);
					}
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void sendCustomerInfoToLoanBanker(BookingFormRequest bookingFormRequest,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		
		//boolean flag = bookingFormServiceDaoImpl.isAlreadyMailSentToLoanBanker(bookingFormRequest);
		
	}
	
	public List<FlatBookingInfo> copyFlatBookingProperties(List<FlatBookingPojo> flatBookingPojos,List<FlatBookingInfo> flatBookingInfos)
	{
		 FlatBookingPojo flatBookingPojo =null;
		 List<FlatBookingInfo> flatBookingInfoscopy =new ArrayList<FlatBookingInfo>();
		for(FlatBookingInfo info :flatBookingInfos)
		{
			if(Util.isNotEmptyObject(flatBookingPojos.get(0)))
			{
			  flatBookingPojo =flatBookingPojos.get(0);
			   BeanUtils.copyProperties(flatBookingPojo, info);
			   info.setAgreementDate(flatBookingPojo.getAgreementDate());
			   info.setBookingDate(flatBookingPojo.getBookingDate());
			   info.setFlatBookingId(flatBookingPojo.getFlatBookingId());
			   info.setRegistrationDate(flatBookingPojo.getRegistrationDate());
			   info.setMilestoneDueDays(flatBookingPojo.getMilestoneDueDays());
			   info.setSaleDeedCDno(flatBookingPojo.getSaleDeedCDno());
			   info.setSaleDeedDate(flatBookingPojo.getSaleDeedDate());
			   info.setSaleDeedNo(flatBookingPojo.getSaleDeedNo());
			   info.setRegistrationStatus(flatBookingPojo.getRegistrationStatus());
			   info.setHandingOverDate(flatBookingPojo.getHandingOverDate());
			   info.setPropertySlNo(flatBookingPojo.getPropertySlNo());
			   info.setRrNo(flatBookingPojo.getRrNo());
			   info.setSalesforceTransactionId(flatBookingPojo.getSalesforceTransactionId());
			   info.setSaleDeedValue(flatBookingPojo.getSaleDeedValue());
			   info.setCarParkingSpaces(flatBookingPojo.getCarParkingSpaces());
			   info.setCustomerLoanBank(flatBookingPojo.getCustomerLoanBank());
			   info.setBookingId(flatBookingPojo.getSalesforceBookingId());
			   info.setOldBookingName(flatBookingPojo.getSalesforceOldBookingId());
			   info.setNewBookingReason(flatBookingPojo.getNewBookingReason());
			   info.setCustomerId(flatBookingPojo.getCustomerId());
			   info.setPaymentId(flatBookingPojo.getPaymentId());
			   flatBookingInfoscopy.add(info);
			   break;
			}
		}
		return flatBookingInfoscopy;
		
	}

	@Override
	public Result getBookingForm(BookingFormRequest bookingFormRequest) throws InSufficeientInputException {
		Result result = new Result();
		bookingFormRequest.setRequestUrl("getBookingDetails");
		CustomerBookingFormInfo customerBookingFormInfo = new CustomerBookingFormInfo();
		List<CustomerPojo> customerPojos = bookingFormServiceDaoImpl.getCustomer(bookingFormRequest);
		List<FlatBookingInfo> flatBookingInfos = getFlatBookingInfo(bookingFormRequest);
		if (customerPojos != null && customerPojos.size() > 0 && flatBookingInfos != null
				&& flatBookingInfos.size() > 0) {

			List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequest);
			flatBookingInfos=copyFlatBookingProperties(flatBookPojo,flatBookingInfos);
			CustomerInfo customerInfo = bookingFormMapper.CustomerPojoToCustomerInfo(customerPojos.get(0));
			flatBookingInfos.get(0).setCustomerId(customerInfo.getCustomerId());
			CustomerBookingInfo customerBookingInfo = new CustomerBookingInfo();
			List<CustBookInfoPojo> custBookInfoPojos = bookingFormServiceDaoImpl.getCustBookInfo(bookingFormRequest);

			Co_ApplicantInfo co_ApplicantInfo = new Co_ApplicantInfo();
			ProfessionalInfo professionalInfo = new ProfessionalInfo();
			professionalInfo.setOraganizationDetails(new OraganizationDetails());
			professionalInfo.setSectorDetailsInfo(new SectorDetailsInfo());
			professionalInfo.setWorkFunctionInfo(new WorkFunctionInfo());
			List<CoApplicentDetailsInfo> coApplicentDetailsInfos = new ArrayList<>();
			CoApplicentBookingInfo coApplicentBookingInfo = new CoApplicentBookingInfo();
			List<CoApplicantCheckListVerificationInfo> coappCheckListApps = new ArrayList<>();
			if (custBookInfoPojos != null && custBookInfoPojos.size() > 0) {
				customerBookingInfo = bookingFormMapper.custBookInfoPojoToCustomerBookingInfo(custBookInfoPojos.get(0));
				try {
					List<EmployeePojo> employeePojos = null;
					if(Util.isNotEmptyObject(customerBookingInfo.getSalesTeamEmpId())) {
					employeePojos = bookingFormServiceDaoImpl.getEmployeeDetails(null, customerBookingInfo.getSalesTeamEmpId(), new Department[] {Department.SALES,Department.MANAGEMENT});
					}
					customerBookingInfo.setSalesTeamEmpName(Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0))?employeePojos.get(0).getEmployeeName():"N/A":"N/A");
				} catch (Exception e) {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("Error while getting sales head employee details - " + e);
					throw new InSufficeientInputException(errorMsgs);
				}
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, inside custBookInfoPojos *****");

				bookingFormRequest.setCustBookInfoId(custBookInfoPojos.get(0).getCustBookInfoId());
				List<CoAppBookInfoPojo> coAppBookInfoPojos = null;
				try {
					coAppBookInfoPojos = bookingFormServiceDaoImpl.getCoAppBookInfo(bookingFormRequest);
				} catch (Exception e) {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("Error while getting coApp book info details - " + e);
					throw new InSufficeientInputException(errorMsgs);
				}
				
				if (coAppBookInfoPojos != null && coAppBookInfoPojos.size() > 0) {
					
					/* setting Applicant MetaData Ids */
					List<Long> metadataIds = new ArrayList<Long>();
					metadataIds.add(MetadataId.APPLICANT1.getId());
					metadataIds.add(MetadataId.APPLICANT2.getId());
					metadataIds.add(MetadataId.APPLICANT3.getId());
					metadataIds.add(MetadataId.APPLICANT4.getId());
					metadataIds.add(MetadataId.APPLICANT5.getId());
					metadataIds.add(MetadataId.APPLICANT6.getId());
					metadataIds.add(MetadataId.APPLICANT7.getId());
					metadataIds.add(MetadataId.APPLICANT8.getId());
					metadataIds.add(MetadataId.APPLICANT9.getId());
					metadataIds.add(MetadataId.APPLICANT10.getId());
					
					//Long i = MetadataId.APPLICANT1.getId();
					Integer i = 0;
					for (CoAppBookInfoPojo coAppBookInfoPojo : coAppBookInfoPojos) {
						CoApplicentDetailsInfo coApplicentDetailsInfo = new CoApplicentDetailsInfo();
						coApplicentBookingInfo = bookingFormMapper
								.coAppBookInfoPojoToCoApplicentBookingInfo(coAppBookInfoPojo);
						coApplicentDetailsInfo.setCoApplicentBookingInfo(coApplicentBookingInfo);
						bookingFormRequest.setApplicantId(coAppBookInfoPojo.getCoApplicantId());

						List<Co_ApplicantPojo> co_ApplicantPojos = null;
						try {
							co_ApplicantPojos = bookingFormServiceDaoImpl.getCo_Applicant(bookingFormRequest);
						} catch (Exception e) {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp details - " + e);
							throw new InSufficeientInputException(errorMsgs);
						}
						if (co_ApplicantPojos != null && co_ApplicantPojos.size() > 0) {
							co_ApplicantInfo = bookingFormMapper
									.co_ApplicantPojoToCo_ApplicantInfo(co_ApplicantPojos.get(0));
							coApplicentDetailsInfo.setCo_ApplicantInfo(co_ApplicantInfo);
							bookingFormRequest.setApplicantId(coAppBookInfoPojo.getCoApplicantId());
							List<CoApplicantCheckListVerificationInfo> coApplicantCheckListVerificationInfos = getCoAppCheckListVerifications(
									bookingFormRequest, Department.LEGAL, co_ApplicantInfo.getPancard());
							coappCheckListApps.addAll(coApplicantCheckListVerificationInfos);
						} else {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp book info details");
							throw new InSufficeientInputException(errorMsgs);
						}
						/* setting which applicant type he is */
						bookingFormRequest.setMetadataId(metadataIds.get(i));
						bookingFormRequest.setApplicantId(coAppBookInfoPojo.getCoApplicantId());
						bookingFormRequest.setCoAppBookInfoId(coApplicentBookingInfo.getCoAppBookInfoId());

						List<AddressMappingPojo> addressMappingPojos = null;
						try {
							addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
						} catch (Exception e) {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp address mapping details - " + e);
							throw new InSufficeientInputException(errorMsgs);
						}

						if (addressMappingPojos != null && addressMappingPojos.size() > 0) {
							List<AddressInfo> appAddrInfos = new ArrayList<>();
							for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
								List<AddressPojo> addrPojos = null;
								try {
									addrPojos = bookingFormServiceDaoImpl.getAddress(addressMappingPojo.getAddressId());
								} catch (Exception e) {
									List<String> errorMsgs = new ArrayList<String>();
									errorMsgs.add("Error while getting coApp address - " + e);
									throw new InSufficeientInputException(errorMsgs);
								}

								List<AddressInfo> addressInfos = bookingFormMapper
										.addressPojosTocustomerAddressInfos(addrPojos, addressMappingPojo);
								if (addressInfos != null && addressInfos.size() > 0) {
									appAddrInfos.add(addressInfos.get(0));
								} else {
									List<String> errorMsgs = new ArrayList<String>();
									errorMsgs.add("Error while getting coApp address");
									throw new InSufficeientInputException(errorMsgs);
								}
							}
							coApplicentDetailsInfo.setAddressInfos(appAddrInfos);
						}

						bookingFormRequest.setProffisionalId(coAppBookInfoPojo.getCustProffisionalId());
						List<ProfessionalDetailsPojo> applicantProfessionalDetailsPojos = null;
						try {
							applicantProfessionalDetailsPojos = bookingFormServiceDaoImpl
									.getProfessionalDetails(bookingFormRequest);
						} catch (Exception e) {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp professional Details - " + e);
							throw new InSufficeientInputException(errorMsgs);
						}
						if (applicantProfessionalDetailsPojos != null && applicantProfessionalDetailsPojos.size() > 0) {
							professionalInfo = bookingFormMapper.professionalDetailsPojoToCustProfessionalInfo(
									applicantProfessionalDetailsPojos.get(0));
						} else {
							professionalInfo.setOraganizationDetails(new OraganizationDetails());
							professionalInfo.setSectorDetailsInfo(new SectorDetailsInfo());
							professionalInfo.setWorkFunctionInfo(new WorkFunctionInfo());
						}

						coApplicentDetailsInfo.setProfessionalInfo(professionalInfo);
						coApplicentDetailsInfos.add(coApplicentDetailsInfo);
						//i = MetadataId.APPLICANT2.getId();
						
						/* incrementing the metadata id */
						i++;
					}
				} else {
					/*
					 * CoApplicentDetailsInfo coApplicentDetailsInfo = new CoApplicentDetailsInfo();
					 * coApplicentDetailsInfo.setCoApplicentBookingInfo(coApplicentBookingInfo);
					 * coApplicentDetailsInfo.setCo_ApplicantInfo(co_ApplicantInfo);
					 * coApplicentDetailsInfo.setProfessionalInfo(professionalInfo);
					 * coApplicentDetailsInfos.add(coApplicentDetailsInfo);
					 */
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting customer book info details");
				throw new InSufficeientInputException(errorMsgs);
			}

			bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
			bookingFormRequest.setCustBookInfoId(customerBookingInfo.getCustBookInfoId());
			List<AddressInfo> customerAddressInfos = new ArrayList<>();
			List<AddressMappingPojo> addressMappingPojos = null;
			try {
				addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust address mapping details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}
			if (addressMappingPojos != null && addressMappingPojos.size() > 0) {
				for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
					List<AddressPojo> addrPojos = null;
					try {
						addrPojos = bookingFormServiceDaoImpl.getAddress(addressMappingPojo.getAddressId());
					} catch (Exception e) {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs.add("Error while getting cust address details - " + e);
						throw new InSufficeientInputException(errorMsgs);
					}
					List<AddressInfo> addressInfos = bookingFormMapper.addressPojosTocustomerAddressInfos(addrPojos,
							addressMappingPojo);
					customerAddressInfos.addAll(addressInfos);
				}
			} else {
				customerAddressInfos.add(new AddressInfo());
			}

			ProfessionalInfo custProfessionalInfo;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting ProfessionalDetailsPojo *****");
				bookingFormRequest.setProffisionalId(customerBookingInfo.getCustProffisionalId());
				List<ProfessionalDetailsPojo> custProfessionalDetailsPojos = bookingFormServiceDaoImpl
						.getProfessionalDetails(bookingFormRequest);
				if (custProfessionalDetailsPojos != null && custProfessionalDetailsPojos.size() > 0) {
					custProfessionalInfo = bookingFormMapper
							.professionalDetailsPojoToCustProfessionalInfo(custProfessionalDetailsPojos.get(0));
				} else {
					custProfessionalInfo = new ProfessionalInfo();
					custProfessionalInfo.setOraganizationDetails(new OraganizationDetails());
					custProfessionalInfo.setSectorDetailsInfo(new SectorDetailsInfo());
					custProfessionalInfo.setWorkFunctionInfo(new WorkFunctionInfo());
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust professional details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			CustomerOtherDetailsInfo customerOtherDetailsInfo;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting customerOtherDetailsInfo *****");
				List<CustomerOtherDetailspojo> customerOtherDetailspojos = bookingFormServiceDaoImpl
						.getCustomerOtherDetails(bookingFormRequest);
				if (customerOtherDetailspojos != null && customerOtherDetailspojos.size() > 0) {
					customerOtherDetailsInfo = bookingFormMapper.customerOtherDetailspojoToCustomerOtherDetailsInfo(
							customerOtherDetailspojos.get(0), bookingFormServiceDaoImpl, employeeTicketDaoImpl);
				} else {
					customerOtherDetailsInfo = new CustomerOtherDetailsInfo();
					customerOtherDetailsInfo.setPoadetailsInfo(new POADetailsInfo());
					customerOtherDetailsInfo.setReferencesFriend(new ReferencesFriendInfo());
					customerOtherDetailsInfo.setReferencesCustomer(new ReferencesCustomerInfo());
					customerOtherDetailsInfo.setReferencesMappingInfo(new ReferencesMappingInfo());
					customerOtherDetailsInfo.setReferenceMaster(new ReferenceMaster());
					customerOtherDetailsInfo.setChannelPartnerInfo(new ChanelPartnerInfo());
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust other details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}
  		CheckListCRMInfo checkListCRMInfo;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting checkListCRMInfo *****");
				List<ChecklistCrmPojo> checkListCrmPojos = bookingFormServiceDaoImpl
						.getChecklistCrm(bookingFormRequest);
				if (checkListCrmPojos != null && checkListCrmPojos.size() > 0) {
					checkListCRMInfo = bookingFormMapper.checklistCrmPojoToCheckListCRM(checkListCrmPojos.get(0));
					checkListCRMInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.CRM));
					List<EmployeePojo> authorizedSignatoryeNamePojos = bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListCRMInfo.getAuthorizedSignatoryeId(), new Department[]{Department.MANAGEMENT});
					List<EmployeePojo> crmSignedNamePojos = bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListCRMInfo.getCrmEmpID(), new Department[] {Department.CRM_MIS,Department.CRM});
					checkListCRMInfo.setAuthorizedSignatoryeName(Util.isNotEmptyObject(authorizedSignatoryeNamePojos)?Util.isNotEmptyObject(authorizedSignatoryeNamePojos.get(0))?authorizedSignatoryeNamePojos.get(0).getEmployeeName():"N/A":"N/A");
					checkListCRMInfo.setCrmSignedName(Util.isNotEmptyObject(crmSignedNamePojos)?Util.isNotEmptyObject(crmSignedNamePojos.get(0))?crmSignedNamePojos.get(0).getEmployeeName():"N/A":"N/A");
					checkListCRMInfo.setCrmVerifiedByName(Util.isNotEmptyObject(crmSignedNamePojos)?Util.isNotEmptyObject(crmSignedNamePojos.get(0))?crmSignedNamePojos.get(0).getEmployeeName():"N/A":"N/A");
				} else {
					checkListCRMInfo = new CheckListCRMInfo();
					List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfos = new ArrayList<>();
					CustomerCheckListVerificationInfo customerCheckListVerificationInfo = new CustomerCheckListVerificationInfo();
					CheckListDepartmentMappingInfo checkListDepartmentMappingInfo = new CheckListDepartmentMappingInfo();
					checkListDepartmentMappingInfo.setCheckListInfo(Status.TRUE);
					customerCheckListVerificationInfo.setCheckListInfo(new CheckListInfo());
					customerCheckListVerificationInfos.add(customerCheckListVerificationInfo);
					checkListCRMInfo.setCustomerCheckListVerification(customerCheckListVerificationInfos);
				
					/* written by venkat 
					checkListCRMInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.CRM));
				   */
				
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust CRM checklist details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			CheckListSalesHeadInfo checkListSalesInfo;
			try {
				logger.info("***** inside getBookingFormDetails() in BookingFormRestService, getting checkListSalesInfo *****");
				List<CheckListSalesHeadPojo> checkListSalesHeadPojos = bookingFormServiceDaoImpl.getCheckListSalesHead(bookingFormRequest);
				if (checkListSalesHeadPojos != null && checkListSalesHeadPojos.size() > 0) {
					checkListSalesInfo = bookingFormMapper.checklistSalesPojoToCheckListSales(checkListSalesHeadPojos.get(0));
					checkListSalesInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.SALES));
					List<EmployeePojo> authorizedSignatoryNames = bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListSalesInfo.getAuthorizedSignatoryId(),new Department[] {Department.MANAGEMENT,Department.SALES});
					List<EmployeePojo> projectSalesheadNames   = bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListSalesInfo.getProjectSalesheadId(), Department.SALES);
					checkListSalesInfo.setAuthorizedSignatoryName(Util.isNotEmptyObject(authorizedSignatoryNames)?Util.isNotEmptyObject(authorizedSignatoryNames.get(0))?authorizedSignatoryNames.get(0).getEmployeeName():"N/A":"N/A");
					checkListSalesInfo.setProjectSalesheadName(Util.isNotEmptyObject(projectSalesheadNames)?Util.isNotEmptyObject(projectSalesheadNames.get(0))?projectSalesheadNames.get(0).getEmployeeName():"N/A":"N/A");
				} else {
					checkListSalesInfo = new CheckListSalesHeadInfo();
					List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfos = new ArrayList<>();
					CustomerCheckListVerificationInfo customerCheckListVerificationInfo = new CustomerCheckListVerificationInfo();
					CheckListDepartmentMappingInfo checkListDepartmentMappingInfo = new CheckListDepartmentMappingInfo();
					checkListDepartmentMappingInfo.setCheckListInfo(Status.TRUE);
					customerCheckListVerificationInfo.setCheckListInfo(new CheckListInfo());
					customerCheckListVerificationInfos.add(customerCheckListVerificationInfo);
					checkListSalesInfo.setCustomerCheckListVerification(customerCheckListVerificationInfos); 
					
					/* writtenby venkat 
					checkListSalesInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.SALES));
				    */
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust saleshead checklist details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			CheckListLegalOfficerInfo checkListLEGALInfo;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting checkListLEGALInfo *****");
				List<CheckListLegalOfficerPojo> checkListLegalOfficerPojos = bookingFormServiceDaoImpl
						.getCheckListLegalOfficer(bookingFormRequest);
				if (checkListLegalOfficerPojos != null && checkListLegalOfficerPojos.size() > 0) {
					checkListLEGALInfo = bookingFormMapper.checkListLegalOfficerPojoToCheckListLegalOfficer(checkListLegalOfficerPojos.get(0));
					checkListLEGALInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.LEGAL));
					checkListLEGALInfo.setCoappCheckListApp(coappCheckListApps);
					List<EmployeePojo> authorizedSignatoryNames = bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListLEGALInfo.getAuthorizedSignatoryId(),new Department[] {Department.MANAGEMENT});
					List<EmployeePojo> legalOfficerNames = bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListLEGALInfo.getEmpId(), Department.LEGAL);
					checkListLEGALInfo.setAuthorizedSignatoryName(Util.isNotEmptyObject(authorizedSignatoryNames)?Util.isNotEmptyObject(authorizedSignatoryNames.get(0))?authorizedSignatoryNames.get(0).getEmployeeName():"N/A":"N/A");
					checkListLEGALInfo.setLegalOfficer(Util.isNotEmptyObject(legalOfficerNames)?Util.isNotEmptyObject(legalOfficerNames.get(0))?legalOfficerNames.get(0).getEmployeeName():"N/A":"N/A");
				} else {
					checkListLEGALInfo = new CheckListLegalOfficerInfo();
					List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfos = new ArrayList<>();
					List<CoApplicantCheckListVerificationInfo> coappCheckListsApps = new ArrayList<>();
					CustomerCheckListVerificationInfo customerCheckListVerificationInfo = new CustomerCheckListVerificationInfo();
					CheckListDepartmentMappingInfo checkListDepartmentMappingInfo = new CheckListDepartmentMappingInfo();
					checkListDepartmentMappingInfo.setCheckListInfo(Status.TRUE);
					customerCheckListVerificationInfo.setCheckListInfo(new CheckListInfo());
					customerCheckListVerificationInfos.add(customerCheckListVerificationInfo);
					checkListLEGALInfo.setCustomerCheckListVerification(customerCheckListVerificationInfos);
					
					CoApplicantCheckListVerificationInfo coappCheckListApp = new CoApplicantCheckListVerificationInfo();
					checkListDepartmentMappingInfo.setCheckListInfo(Status.TRUE);
					coappCheckListApp.setCheckListInfo(new CheckListInfo());
					coappCheckListsApps.add(coappCheckListApp);
					checkListLEGALInfo.setCoappCheckListApp(coappCheckListsApps);
					
					/* writtenby venkat 
					checkListLEGALInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.LEGAL));
					checkListLEGALInfo.setCoappCheckListApp(coappCheckListApps);
					*/
					
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust legal officer checklist details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			CheckListRegistrationInfo checkListREGInfo;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting checkListREGInfo *****");
				List<CheckListRegistrationPojo> checkListRegistrationPojos = bookingFormServiceDaoImpl
						.getCheckListRegistration(bookingFormRequest);
				if (checkListRegistrationPojos != null && checkListRegistrationPojos.size() > 0) {
					checkListREGInfo = bookingFormMapper.checkListRegistrationPojoToCheckListRegistration(checkListRegistrationPojos.get(0));
					checkListREGInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.MANAGEMENT));
					List<EmployeePojo> accountsExecutiveEmployee = bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListREGInfo.getAccountsExecutiveEmpid(), new Department[]{Department.ACCOUNTS});
					List<EmployeePojo> authorizedEmployee = bookingFormServiceDaoImpl.getEmployeeDetails(null,checkListREGInfo.getAuthorizedSignatureId(), new Department[] {Department.MANAGEMENT});
					List<EmployeePojo> legalOfficerEmployee =bookingFormServiceDaoImpl.getEmployeeDetails(null, checkListREGInfo.getLegalOfficerEmpId(), new Department[]{Department.LEGAL});
					checkListREGInfo.setAccountsExecutiveEmpName(Util.isNotEmptyObject(accountsExecutiveEmployee)?Util.isNotEmptyObject(accountsExecutiveEmployee.get(0))?Util.isNotEmptyObject(accountsExecutiveEmployee.get(0).getEmployeeName())?accountsExecutiveEmployee.get(0).getEmployeeName():"N/A":"N/A":"N/A");
					checkListREGInfo.setAuthorizedSignatureName(Util.isNotEmptyObject(authorizedEmployee)?Util.isNotEmptyObject(authorizedEmployee.get(0))?Util.isNotEmptyObject(authorizedEmployee.get(0).getEmployeeName())?authorizedEmployee.get(0).getEmployeeName():"N/A":"N/A":"N/A");
					checkListREGInfo.setLegalOfficerEmpName(Util.isNotEmptyObject(legalOfficerEmployee)?Util.isNotEmptyObject(legalOfficerEmployee.get(0))?Util.isNotEmptyObject(legalOfficerEmployee.get(0).getEmployeeName())?legalOfficerEmployee.get(0).getEmployeeName():"N/A":"N/A":"N/A");
				} else {
					checkListREGInfo = new CheckListRegistrationInfo();
					List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfos = new ArrayList<>();
					CustomerCheckListVerificationInfo customerCheckListVerificationInfo = new CustomerCheckListVerificationInfo();
					CheckListDepartmentMappingInfo checkListDepartmentMappingInfo = new CheckListDepartmentMappingInfo();
					checkListDepartmentMappingInfo.setCheckListInfo(Status.TRUE);
					customerCheckListVerificationInfo.setCheckListInfo(new CheckListInfo());
					customerCheckListVerificationInfos.add(customerCheckListVerificationInfo);
					checkListREGInfo.setCustomerCheckListVerification(customerCheckListVerificationInfos);
					
					/* writtenby venkat 
					checkListREGInfo.setCustomerCheckListVerification(getCustomerCheckListVerifications(bookingFormRequest, Department.MANAGEMENT));
				   */
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust registration checklist details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			CustomerApplicationInfo customerApplicationInfo;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting customerApplicationInfo *****");
				List<CustomerApplicationPojo> customerApplicationPojos = bookingFormServiceDaoImpl
						.getCustomerApplication(bookingFormRequest.getFlatBookingId());
				if (customerApplicationPojos != null && customerApplicationPojos.size() > 0) {
					customerApplicationInfo = bookingFormMapper
							.customerApplicationPojoToCustomerApplicationInfo(customerApplicationPojos.get(0));
				} else {
					customerApplicationInfo = new CustomerApplicationInfo();
				}

			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust application details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			List<CustomerKYCDocumentSubmitedInfo> customerKYCDocumentSubmitedInfos;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting CustomerKYCDocumentSubmitedInfo *****");
				List<CustomerKycSubmittedDocPojo> customerKycSubmittedDocPojos = bookingFormServiceDaoImpl
						.getCustomerKycSubmittedDoc(bookingFormRequest.getFlatBookingId(),
								customerBookingInfo.getCustBookInfoId());
				if (customerKycSubmittedDocPojos != null && customerKycSubmittedDocPojos.size() > 0) {
					customerKYCDocumentSubmitedInfos = bookingFormMapper
							.customerKycSubmittedDocPojosTocustomerKYCDocumentSubmitedInfos(
									customerKycSubmittedDocPojos);
				} else {
					customerKYCDocumentSubmitedInfos = new ArrayList<>();
					CustomerKYCDocumentSubmitedInfo customerKYCDocumentSubmitedInfo = new CustomerKYCDocumentSubmitedInfo();
					customerKYCDocumentSubmitedInfos.add(customerKYCDocumentSubmitedInfo);
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust submitted KYC details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
				Properties propData = new ResponceCodesUtil()
						.readPropertiesFromFile(customerBookingInfo.getTermsConditionFileName());
				
				String file = propData.getProperty("data").toString();
				
					file =file
						.replace("$(ackId)", String.valueOf(customerApplicationInfo.getAckId()))
						.replace("$(date)", String.valueOf(dateFormat.format(flatBookPojo.get(0).getBookingDate())))
						.replace("$(place)",
								bookingFormMapper.getCityName(flatBookingInfos.get(0).getSiteInfo().getCityId()))
						.replace("$(stmName)", Util.isNotEmptyObject(customerBookingInfo.getSalesTeamEmpName())?String.valueOf(customerBookingInfo.getSalesTeamEmpName()):"N/A")
						.replace("$(stmEmpId)",Util.isNotEmptyObject(customerBookingInfo.getSalesTeamEmpId())? String.valueOf(customerBookingInfo.getSalesTeamEmpId()):"N/A")
						.replace("$(stmHeadName)",Util.isNotEmptyObject(checkListSalesInfo.getProjectSalesheadName())? String.valueOf(checkListSalesInfo.getProjectSalesheadName()):"N/A")
						.replace("$(stmHeadId)",Util.isNotEmptyObject(checkListSalesInfo.getProjectSalesheadId())? String.valueOf(checkListSalesInfo.getProjectSalesheadId()):"N/A");
						
						
					if(Util.isNotEmptyObject(customerBookingInfo.getTdsAuthorizationId())) {
						if(customerBookingInfo.getTdsAuthorizationId().equals(1l)) {
							file = file.replace("$(OPTION1)"," checked  ");
						}else if(customerBookingInfo.getTdsAuthorizationId().equals(2l)){
							file = file.replace("$(OPTION2)"," checked  ");
						}
					}
					customerBookingInfo.setTermsConditionFileData(file);
			
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting terms and conditions - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}
			List<FinSchemeTaxMappingPojo> flatSchemeDetailsList=null;
			List<CustomerSchemeInfo> schemes = new ArrayList<>();
			try {
				CustomerSchemeInfo scheme = new CustomerSchemeInfo();
				CustomerSchemeInfo customerSchemeInfo = new CustomerSchemeInfo();
				customerSchemeInfo.setCondition("getPastSchemeDetails");
				FlatBookingInfo flatBookingInfo = new FlatBookingInfo();
				flatBookingInfo.setFlatBookingId(flatBookPojo.get(0).getFlatBookingId());
				flatSchemeDetailsList = bookingFormServiceDaoImpl
						.getCustomerSchemeDetailsBySchemeName(customerSchemeInfo, flatBookingInfo);
				scheme.setSchemeName(flatSchemeDetailsList.get(0).getFinSchemeName());
				scheme.setPercentageValue(flatSchemeDetailsList.get(0).getPercentageValue());
				schemes.add(scheme);
			} catch (Exception e) {
				e.printStackTrace();
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting scheme details - " + e);
				//throw new InSufficeientInputException(errorMsgs);
			}
			try {
				if (Util.isNotEmptyObject(flatBookPojo.get(0).getStatusId())) {
					if (flatBookPojo.get(0).getStatusId().equals(6l)) {
						customerBookingFormInfo.setCustomerAppBookingApproval(true);
					} else {
						customerBookingFormInfo.setCustomerAppBookingApproval(false);
					}
				} 
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting customerAppBookingApproval - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}
			customerBookingFormInfo.setCustomerSchemeInfos(schemes);
			customerBookingFormInfo.setAddressInfos(customerAddressInfos);
			customerBookingFormInfo.setCheckListCRM(checkListCRMInfo);
			customerBookingFormInfo.setCheckListRegistration(checkListREGInfo);
			customerBookingFormInfo.setCheckListSalesHead(checkListSalesInfo);
			customerBookingFormInfo.setCheckListLegalOfficer(checkListLEGALInfo);
			customerBookingFormInfo.setCustomerInfo(customerInfo);
			customerBookingFormInfo.setCustomerBookingInfo(customerBookingInfo);
			customerBookingFormInfo.setCustomerOtherDetailsInfo(customerOtherDetailsInfo);
			customerBookingFormInfo.setProfessionalInfo(custProfessionalInfo);
			customerBookingFormInfo.setFlatBookingInfo(flatBookingInfos.get(0));
			customerBookingFormInfo.setCoApplicentDetails(coApplicentDetailsInfos);
			customerBookingFormInfo.setCustomerApplicationInfo(customerApplicationInfo);
			customerBookingFormInfo.setCustomerKYCSubmitedInfo(customerKYCDocumentSubmitedInfos);

			result = customerBookingFormInfo;
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"Error while getting Customer/FlatBooking details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@Override
	public List<FlatBookingInfo>  getRegistrationDetails(BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.getRegistrationDetails() *****"); 
		List<FlatBookingInfo> customerRegistrationDetails = getCustomerRegistrationDetails(bookingFormRequest);
		return customerRegistrationDetails;
	}

	public List<FlatBookingInfo> loadUnitDetails(BookingFormRequest bookingFormRequest) {
		List<FlatBookingInfo> flatBookingInfos = getFlatBookingInfo(bookingFormRequest);
		return flatBookingInfos;
	}
	
	/**
	 * These method is used in multiple places, Welcome letter,Generate NOC letter,View Customer Data
	 */
	@Override
	public CustomerBookingFormInfo getFlatCustomerAndCoAppBookingDetails(BookingFormRequest bookingFormRequest) throws Exception {
		Result result = new Result();
		//String actualRequetURL = bookingFormRequest.getRequestUrl();
		//bookingFormRequest.setRequestUrl("getBookingDetails");
		//bookingFormRequest.setRequestUrl(actualRequetURL);
		CustomerBookingFormInfo customerBookingFormInfo = new CustomerBookingFormInfo();
		List<CustomerPojo> customerPojos = bookingFormServiceDaoImpl.getCustomer(bookingFormRequest);
		BookingFormRequest bookingFormRequestCopy = new BookingFormRequest(); 
		BeanUtils.copyProperties(bookingFormRequest,bookingFormRequestCopy);
		bookingFormRequestCopy.setStatusId(Status.ACTIVE.getStatus());
        
		List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequestCopy);

		//List<FlatBookingInfo> flatBookingInfos = getFlatBookingInfo(bookingFormRequest);
		if (customerPojos != null && customerPojos.size() > 0  && flatBookPojo != null && flatBookPojo.size() > 0) {//
			/*BookingFormRequest bookingFormRequestCopy = new BookingFormRequest(); 
			BeanUtils.copyProperties(bookingFormRequest,bookingFormRequestCopy);
			bookingFormRequestCopy.setStatusId(Status.ACTIVE.getStatus());

			List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequest);
*/			List<FlatBookingInfo> flatBookingInfos = bookingFormMapper.copyPojoPropertyToInfoObject(flatBookPojo, FlatBookingInfo.class);
			if(Util.isNotEmptyObject(flatBookingInfos)) {
				bookingFormRequest.setFlatId(flatBookingInfos.get(0).getFlatId());
			}
			List<Map<String, Object>> customerFinancialDetails = loadFinancialAmountPaidDetails(bookingFormRequest,flatBookingInfos.get(0));
			customerBookingFormInfo.setCustomerFinancialDetails(customerFinancialDetails);
			//FinBookingFormAccountsResponse
			CustomerInfo customerInfo = bookingFormMapper.CustomerPojoToCustomerInfo(customerPojos.get(0));
			//flatBookingInfos.get(0).setCustomerId(customerInfo.getCustomerId());
			CustomerBookingInfo customerBookingInfo = new CustomerBookingInfo();
			List<CustBookInfoPojo> custBookInfoPojos = bookingFormServiceDaoImpl.getCustBookInfo(bookingFormRequest);

			Co_ApplicantInfo co_ApplicantInfo = new Co_ApplicantInfo();
			ProfessionalInfo professionalInfo = new ProfessionalInfo();
			professionalInfo.setOraganizationDetails(new OraganizationDetails());
			professionalInfo.setSectorDetailsInfo(new SectorDetailsInfo());
			professionalInfo.setWorkFunctionInfo(new WorkFunctionInfo());
			List<CoApplicentDetailsInfo> coApplicentDetailsInfos = new ArrayList<>();
			CoApplicentBookingInfo coApplicentBookingInfo = new CoApplicentBookingInfo();
			List<CoApplicantCheckListVerificationInfo> coappCheckListApps = new ArrayList<>();
			if (custBookInfoPojos != null && custBookInfoPojos.size() > 0) {
				customerBookingInfo = bookingFormMapper.custBookInfoPojoToCustomerBookingInfo(custBookInfoPojos.get(0));
				try {
					List<EmployeePojo> employeePojos = null;
					if(Util.isNotEmptyObject(customerBookingInfo.getSalesTeamEmpId())) {
						employeePojos = bookingFormServiceDaoImpl.getEmployeeDetails(null, customerBookingInfo.getSalesTeamEmpId(), new Department[] {Department.SALES,Department.MANAGEMENT});
					}
					customerBookingInfo.setSalesTeamEmpName(Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0))?employeePojos.get(0).getEmployeeName():"N/A":"N/A");
				} catch (Exception e) {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("Error while getting sales head employee details - " + e);
					throw new InSufficeientInputException(errorMsgs);
				}
				logger.info("***** inside getBookingFormDetails() in BookingFormRestService, inside custBookInfoPojos *****");

				bookingFormRequest.setCustBookInfoId(custBookInfoPojos.get(0).getCustBookInfoId());
				List<CoAppBookInfoPojo> coAppBookInfoPojos = null;
				try {
					coAppBookInfoPojos = bookingFormServiceDaoImpl.getCoAppBookInfo(bookingFormRequest);
				} catch (Exception e) {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("Error while getting coApp book info details - " + e);
					throw new InSufficeientInputException(errorMsgs);
				}
				
				if (coAppBookInfoPojos != null && coAppBookInfoPojos.size() > 0) {
					
					/* setting Applicant MetaData Ids */
					List<Long> metadataIds = new ArrayList<Long>();
					metadataIds.add(MetadataId.APPLICANT1.getId());
					metadataIds.add(MetadataId.APPLICANT2.getId());
					metadataIds.add(MetadataId.APPLICANT3.getId());
					metadataIds.add(MetadataId.APPLICANT4.getId());
					metadataIds.add(MetadataId.APPLICANT5.getId());
					metadataIds.add(MetadataId.APPLICANT6.getId());
					metadataIds.add(MetadataId.APPLICANT7.getId());
					metadataIds.add(MetadataId.APPLICANT8.getId());
					metadataIds.add(MetadataId.APPLICANT9.getId());
					metadataIds.add(MetadataId.APPLICANT10.getId());
					
					//Long i = MetadataId.APPLICANT1.getId();
					Integer index = 0;
					for (CoAppBookInfoPojo coAppBookInfoPojo : coAppBookInfoPojos) {
						CoApplicentDetailsInfo coApplicentDetailsInfo = new CoApplicentDetailsInfo();
						coApplicentBookingInfo = bookingFormMapper.coAppBookInfoPojoToCoApplicentBookingInfo(coAppBookInfoPojo);
						coApplicentDetailsInfo.setCoApplicentBookingInfo(coApplicentBookingInfo);
						bookingFormRequest.setApplicantId(coAppBookInfoPojo.getCoApplicantId());

						List<Co_ApplicantPojo> co_ApplicantPojos = null;
						try {
							co_ApplicantPojos = bookingFormServiceDaoImpl.getCo_Applicant(bookingFormRequest);
						} catch (Exception e) {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp details - " + e);
							throw new InSufficeientInputException(errorMsgs);
						}
						
						if (co_ApplicantPojos != null && co_ApplicantPojos.size() > 0) {
							if(index==0) {
								try {//loading this co-applicant booked flat details
									List<CustomerPojo> customedrPojos = bookingFormServiceDaoImpl.getCustomerCoApplicantFlatDetails(co_ApplicantPojos.get(0));
									if(Util.isNotEmptyObject(customedrPojos)) {
										BookingFormRequest coAppBookingFormRequest1 = new BookingFormRequest();
										coAppBookingFormRequest1.setRequestUrl("getCoAppFlatDetails");
										coAppBookingFormRequest1.setCustomerId(customedrPojos.get(0).getCustomerId());
										coAppBookingFormRequest1.setStatusId(Status.ACTIVE.getStatus());
										//loading this co-applicant booked flat details
										List<FlatBookingPojo> flatBookPojo1 = bookingFormServiceDaoImpl.getFlatbookingDetails(coAppBookingFormRequest1);
										if(Util.isNotEmptyObject(flatBookPojo1)) {//if co-applicant has a flat, then loading it's booking date
											co_ApplicantPojos.get(0).setCoApplicantBookingDate(flatBookPojo1.get(0).getBookingDate());
											co_ApplicantPojos.get(0).setHandingOverDate(flatBookPojo1.get(0).getHandingOverDate());
											co_ApplicantPojos.get(0).setRegistrationDate(flatBookPojo1.get(0).getRegistrationDate());
											//co_ApplicantPojos.get(0).setSalesforceBookId(flatBookPojo1.get(0).getSalesforceBookingId());
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}	
							}
							
							co_ApplicantInfo = bookingFormMapper.co_ApplicantPojoToCo_ApplicantInfo(co_ApplicantPojos.get(0));
							coApplicentDetailsInfo.setCo_ApplicantInfo(co_ApplicantInfo);
							bookingFormRequest.setApplicantId(coAppBookInfoPojo.getCoApplicantId());
							List<CoApplicantCheckListVerificationInfo> coApplicantCheckListVerificationInfos = getCoAppCheckListVerifications(
									bookingFormRequest, Department.LEGAL, co_ApplicantInfo.getPancard());
							coappCheckListApps.addAll(coApplicantCheckListVerificationInfos);
						} else {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp book info details");
							throw new InSufficeientInputException(errorMsgs);
						}
						/* setting which applicant type he is */
						bookingFormRequest.setMetadataId(metadataIds.get(index));
						bookingFormRequest.setApplicantId(coAppBookInfoPojo.getCoApplicantId());
						bookingFormRequest.setCoAppBookInfoId(coApplicentBookingInfo.getCoAppBookInfoId());

						List<AddressMappingPojo> addressMappingPojos = null;
						try {
							//co-applicant address mapping details
							addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
						} catch (Exception e) {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp address mapping details - " + e);
							throw new InSufficeientInputException(errorMsgs);
						}

						if (addressMappingPojos != null && addressMappingPojos.size() > 0) {
							List<AddressInfo> appAddrInfos = new ArrayList<>();
							for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
								List<AddressPojo> addrPojos = null;
								try {
									//co-applicant address details
									addrPojos = bookingFormServiceDaoImpl.getAddress(addressMappingPojo.getAddressId());
								} catch (Exception e) {
									List<String> errorMsgs = new ArrayList<String>();
									errorMsgs.add("Error while getting coApp address - " + e);
									throw new InSufficeientInputException(errorMsgs);
								}

								List<AddressInfo> addressInfos = bookingFormMapper.addressPojosTocustomerAddressInfos(addrPojos, addressMappingPojo);
								if (addressInfos != null && addressInfos.size() > 0) {
									appAddrInfos.add(addressInfos.get(0));
								} else {
									List<String> errorMsgs = new ArrayList<String>();
									errorMsgs.add("Error while getting coApp address");
									throw new InSufficeientInputException(errorMsgs);
								}
							}
							coApplicentDetailsInfo.setAddressInfos(appAddrInfos);
						}

						
						
						bookingFormRequest.setProffisionalId(coAppBookInfoPojo.getCustProffisionalId());
						List<ProfessionalDetailsPojo> applicantProfessionalDetailsPojos = null;
						try {
							//loading customer professional details
							applicantProfessionalDetailsPojos = bookingFormServiceDaoImpl.getProfessionalDetails(bookingFormRequest);
						} catch (Exception e) {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add("Error while getting coApp professional Details - " + e);
							throw new InSufficeientInputException(errorMsgs);
						}
						if (applicantProfessionalDetailsPojos != null && applicantProfessionalDetailsPojos.size() > 0) {
							professionalInfo = bookingFormMapper.professionalDetailsPojoToCustProfessionalInfo(applicantProfessionalDetailsPojos.get(0));
						} else {
							professionalInfo.setOraganizationDetails(new OraganizationDetails());
							professionalInfo.setSectorDetailsInfo(new SectorDetailsInfo());
							professionalInfo.setWorkFunctionInfo(new WorkFunctionInfo());
						}

						coApplicentDetailsInfo.setProfessionalInfo(professionalInfo);
						coApplicentDetailsInfos.add(coApplicentDetailsInfo);
						//i = MetadataId.APPLICANT2.getId();
						
						/* incrementing the metadata id */
						index++;
					}
				} else {
					/*
					 * CoApplicentDetailsInfo coApplicentDetailsInfo = new CoApplicentDetailsInfo();
					 * coApplicentDetailsInfo.setCoApplicentBookingInfo(coApplicentBookingInfo);
					 * coApplicentDetailsInfo.setCo_ApplicantInfo(co_ApplicantInfo);
					 * coApplicentDetailsInfo.setProfessionalInfo(professionalInfo);
					 * coApplicentDetailsInfos.add(coApplicentDetailsInfo);
					 */
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting customer book info details");
				throw new InSufficeientInputException(errorMsgs);
			}

			bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
			bookingFormRequest.setCustBookInfoId(customerBookingInfo.getCustBookInfoId());
			List<AddressInfo> customerAddressInfos = new ArrayList<>();
			List<AddressMappingPojo> addressMappingPojos = null;
			try {
				addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust address mapping details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}
			if (addressMappingPojos != null && addressMappingPojos.size() > 0) {
				for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
					List<AddressPojo> addrPojos = null;
					try {
						addrPojos = bookingFormServiceDaoImpl.getAddress(addressMappingPojo.getAddressId());
					} catch (Exception e) {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs.add("Error while getting cust address details - " + e);
						throw new InSufficeientInputException(errorMsgs);
					}
					List<AddressInfo> addressInfos = bookingFormMapper.addressPojosTocustomerAddressInfos(addrPojos,
							addressMappingPojo);
					customerAddressInfos.addAll(addressInfos);
				}
			} else {
				customerAddressInfos.add(new AddressInfo());
			}

			ProfessionalInfo custProfessionalInfo;
			try {//loading applicant professional details
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting ProfessionalDetailsPojo *****");
				bookingFormRequest.setProffisionalId(customerBookingInfo.getCustProffisionalId());
				List<ProfessionalDetailsPojo> custProfessionalDetailsPojos = bookingFormServiceDaoImpl
						.getProfessionalDetails(bookingFormRequest);
				if (custProfessionalDetailsPojos != null && custProfessionalDetailsPojos.size() > 0) {
					custProfessionalInfo = bookingFormMapper
							.professionalDetailsPojoToCustProfessionalInfo(custProfessionalDetailsPojos.get(0));
				} else {
					custProfessionalInfo = new ProfessionalInfo();
					custProfessionalInfo.setOraganizationDetails(new OraganizationDetails());
					custProfessionalInfo.setSectorDetailsInfo(new SectorDetailsInfo());
					custProfessionalInfo.setWorkFunctionInfo(new WorkFunctionInfo());
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust professional details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}

			/*CustomerOtherDetailsInfo customerOtherDetailsInfo;
			try {
				logger.info(
						"***** inside getBookingFormDetails() in BookingFormRestService, getting customerOtherDetailsInfo *****");
				List<CustomerOtherDetailspojo> customerOtherDetailspojos = bookingFormServiceDaoImpl
						.getCustomerOtherDetails(bookingFormRequest);
				if (customerOtherDetailspojos != null && customerOtherDetailspojos.size() > 0) {
					customerOtherDetailsInfo = bookingFormMapper.customerOtherDetailspojoToCustomerOtherDetailsInfo(
							customerOtherDetailspojos.get(0), bookingFormServiceDaoImpl, employeeTicketDaoImpl);
				} else {
					customerOtherDetailsInfo = new CustomerOtherDetailsInfo();
					customerOtherDetailsInfo.setPoadetailsInfo(new POADetailsInfo());
					customerOtherDetailsInfo.setReferencesFriend(new ReferencesFriendInfo());
					customerOtherDetailsInfo.setReferencesCustomer(new ReferencesCustomerInfo());
					customerOtherDetailsInfo.setReferencesMappingInfo(new ReferencesMappingInfo());
					customerOtherDetailsInfo.setReferenceMaster(new ReferenceMaster());
					customerOtherDetailsInfo.setChannelPartnerInfo(new ChanelPartnerInfo());
				}
			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust other details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}*/
  		 
			/*CustomerApplicationInfo customerApplicationInfo;
			try {
				logger.info("***** inside getBookingFormDetails() in BookingFormRestService, getting customerApplicationInfo *****");
				List<CustomerApplicationPojo> customerApplicationPojos = bookingFormServiceDaoImpl
						.getCustomerApplication(bookingFormRequest.getFlatBookingId());
				if (customerApplicationPojos != null && customerApplicationPojos.size() > 0) {
					customerApplicationInfo = bookingFormMapper
							.customerApplicationPojoToCustomerApplicationInfo(customerApplicationPojos.get(0));
				} else {
					customerApplicationInfo = new CustomerApplicationInfo();
				}

			} catch (Exception e) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Error while getting cust application details - " + e);
				throw new InSufficeientInputException(errorMsgs);
			}*/

			customerBookingFormInfo.setAddressInfos(customerAddressInfos);
 			customerBookingFormInfo.setCustomerInfo(customerInfo);
 			customerBookingFormInfo.setCustomerBookingInfo(customerBookingInfo);
			//customerBookingFormInfo.setCustomerOtherDetailsInfo(customerOtherDetailsInfo);
			customerBookingFormInfo.setProfessionalInfo(custProfessionalInfo);
			customerBookingFormInfo.setFlatBookingInfo(flatBookingInfos.get(0));
			customerBookingFormInfo.setCoApplicentDetails(coApplicentDetailsInfos);
			//customerBookingFormInfo.setCustomerApplicationInfo(customerApplicationInfo);
 
			//customerBookingFormInfo.setCustomerAppRegistrationDtls(bookingFormServiceDaoImpl.gettingCustomerAppRegistrationDetails(bookingFormRequest.getCustomerId()));
			//Map<String, Object> customerDetails = bookingFormMapper.customerInfoKeyValuesPair(customerBookingFormInfo);
			result = customerBookingFormInfo;
			//result.setResponseObjList(customerDetails);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Error while getting Customer/FlatBooking details - The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return customerBookingFormInfo;
	}
	
	private List<Map<String, Object>> loadFinancialAmountPaidDetails(BookingFormRequest bookingFormRequest, FlatBookingInfo flatBookingInfo) throws Exception {
		//FinBookingFormAccountsResponse resp = new FinBookingFormAccountsResponse();
		CurrencyUtil currencyUtil = new CurrencyUtil(); 
		RoundingMode roundingMode = RoundingMode.HALF_UP;
		int roundingModeSize = 2;
		double totalFlatCost = 0.0;
		double getMilestoneInitiatedAmount = 0.0;
		double getMilestonePaidAmount = 0.0;
		//double getMilestoneInitiatedBalanceAmount = 0.0;
		double totalPendingAmountOnFlat = 0.0;
		double excessBalanceAmount = 0.0;
		List<Map<String, Object>> map = new ArrayList<>();
		Map<String, Object> milestoneDetails = new HashMap<>();

		CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = new CustomerPropertyDetailsInfo();
		customerPropertyDetailsInfo2.setFlatBookingId(bookingFormRequest.getFlatBookingId());
		customerPropertyDetailsInfo2.setStatusId(Status.ACTIVE.getStatus());
		List<FlatCostPojo> flatCostPojos = bookingFormServiceDaoImpl.getFlatCost(bookingFormRequest);
		if (Util.isNotEmptyObject(flatCostPojos)) {
			totalFlatCost = flatCostPojos.get(0).getTotalCost();
			totalPendingAmountOnFlat = totalFlatCost;
		}
		
		FinancialProjectMileStoneInfo finProjDemandNoteInfo = new FinancialProjectMileStoneInfo();
		finProjDemandNoteInfo.setMilestoneDate(flatBookingInfo.getBookingDate());
		finProjDemandNoteInfo.setSiteId(bookingFormRequest.getSiteId());
		List<FinSchemeTaxMappingPojo> schemeTaxDetails = employeeFinancialServiceDao.getFlatBookDetailsSchemeTaxDetails(customerPropertyDetailsInfo2,finProjDemandNoteInfo);	
		
		List<FinBookingFormAccountsPojo> milestonePaidList = employeeFinancialServiceDao.getFlatPaidAmountDetails(bookingFormRequest,Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()));
		customerPropertyDetailsInfo2.setCondition("PaymentRefund");
		//loading excess amount if any
		List<FinBookingFormExcessAmountPojo> listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()));
		if(Util.isNotEmptyObject(listOfExcessAmount)) {
			for (FinBookingFormExcessAmountPojo excessAmountPojo : listOfExcessAmount) {
				excessBalanceAmount += excessAmountPojo.getBalanceAmount() ==null? 0d :excessAmountPojo.getBalanceAmount();
			}
		}
		long blockWorkCompletion = employeeFinancialServiceDao.loadBlockCompletionPercent(bookingFormRequest);
		
		if (Util.isNotEmptyObject(milestonePaidList)) {
			for (FinBookingFormAccountsPojo pojo : milestonePaidList) {
				
				if(pojo.getMsStatusId()!=null && !pojo.getMsStatusId().equals(Status.EXPLICIT_GENERATED_RECORD.getStatus())) {
					getMilestoneInitiatedAmount += (pojo.getPayAmount() == null ? 0 : pojo.getPayAmount());
				}
				
				getMilestonePaidAmount += (pojo.getPaidAmount() == null ? 0 : pojo.getPaidAmount());
				//getMilestoneInitiatedBalanceAmount += getMilestoneInitiatedAmount - getMilestonePaidAmount;
				totalPendingAmountOnFlat -= (pojo.getPaidAmount() == null ? 0 : pojo.getPaidAmount());	
			}
		}
		
		getMilestonePaidAmount +=excessBalanceAmount;
		
		milestoneDetails.put("blockCompletionPercent", blockWorkCompletion+"%");
		milestoneDetails.put("totalPendingAmountAgainstTotalFlatCost", currencyUtil.getTheAmountWithCommas(Double.valueOf(totalPendingAmountOnFlat),roundingModeSize,roundingMode));
		if(Util.isNotEmptyObject(schemeTaxDetails)) {
			milestoneDetails.put("schemeCode", schemeTaxDetails.get(0).getFinSchemeName());	
		}
		
		if(getMilestonePaidAmount>getMilestoneInitiatedAmount) {
			milestoneDetails.put("totalPendingAmountAsPerWorkCompletion", "-"+currencyUtil.getTheAmountWithCommas(
					Double.valueOf(getMilestonePaidAmount-getMilestoneInitiatedAmount),roundingModeSize,roundingMode) );
		}else {
			milestoneDetails.put("totalPendingAmountAsPerWorkCompletion", currencyUtil.getTheAmountWithCommas(
				Double.valueOf(getMilestoneInitiatedAmount-getMilestonePaidAmount),roundingModeSize,roundingMode) );
		}
		milestoneDetails.put("totalAmountPaid", currencyUtil.getTheAmountWithCommas(Double.valueOf(getMilestonePaidAmount),roundingModeSize,roundingMode) );
		map.add(milestoneDetails);
		System.out.println(map);
		return map;
	}

	private List<FlatBookingInfo> getCustomerRegistrationDetails(BookingFormRequest bookingFormRequest) {
		List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequest);
		List<FlatBookingInfo> flatBookingInfos = bookingFormMapper.copyPojoPropertyToInfoObject(flatBookPojo, FlatBookingInfo.class);
		return flatBookingInfos;
	}
	
	@SuppressWarnings("unused")
	@Override
	public List<String> validateBookingForm(CustomerBookingFormInfo customerBookingForm) throws IllegalAccessException {
		List<String> errorMsgs = new ArrayList<>();

		/* salesTeamLeadId  */
		if (Util.isNotEmptyObject(customerBookingForm)) {
			if (Util.isNotEmptyObject(customerBookingForm.getCustomerBookingInfo())) {
				errorMsgs.add(Util.isNotEmptyObject(customerBookingForm.getCustomerBookingInfo().getSalesTeamLeadId())?customerBookingForm.getCustomerBookingInfo().getSalesTeamLeadId().toString():"");
			} else {
				errorMsgs.add(null);
			}
			if (Util.isNotEmptyObject(customerBookingForm.getCustomerInfo())) {
				errorMsgs.add((Util.isNotEmptyObject(customerBookingForm.getCustomerInfo().getFirstName())?customerBookingForm.getCustomerInfo().getFirstName():"") + (Util.isNotEmptyObject(customerBookingForm.getCustomerInfo().getLastName())?customerBookingForm.getCustomerInfo().getLastName():""));
				
				/* Customer name */
				if (Util.isEmptyObject(customerBookingForm.getCustomerInfo().getFirstName()) && Util.isEmptyObject(customerBookingForm.getCustomerInfo().getLastName())) {
					errorMsgs.add("Customer's name is required");
				}
				
				/*  customer pancard  */
				if(Util.isEmptyObject(customerBookingForm.getCustomerInfo().getPancard())) {
					errorMsgs.add("Customer's pancard details are required");
				}
				
				/*
				if (Util.isEmptyObject(customerBookingForm.getCustomerInfo().getNationality())) {
					errorMsgs.add("Customer's Nationality details are required");
				}else {
					if (Util.isNotEmptyObject(customerBookingForm.getCustomerInfo().getNationality()) && (customerBookingForm.getCustomerInfo().getNationality().equalsIgnoreCase(CountryCode.IN.getName()) || customerBookingForm.getCustomerInfo().getNationality().equalsIgnoreCase(Nationality.INDIAN.getName()))) {
						if(Util.isEmptyObject(customerBookingForm.getCustomerInfo().getPancard())) {
							errorMsgs.add("Customer's pancard details are required");
						}
						if(Util.isEmptyObject(customerBookingForm.getCustomerInfo().getAdharNumber())) {
							errorMsgs.add("Customer's aadhaar number is required");
						}else {
							List<CustomerPojo> listCustomersByPanCardorPassport = null;
							if (Util.isNotEmptyObject(customerBookingForm.getCustomerInfo().getPancard())) {
								listCustomersByPanCardorPassport = bookingFormServiceDaoImpl.getCustomerDetailsByPanCardorPassport(customerBookingForm.getCustomerInfo().getPancard(), null,Status.ACTIVE);
								if (Util.isNotEmptyObject(listCustomersByPanCardorPassport)) {
									if (!listCustomersByPanCardorPassport.get(0).getAdharNumber().equalsIgnoreCase(customerBookingForm.getCustomerInfo().getAdharNumber())) {
										errorMsgs.add("Customer's aadhaar number is not correct.(Customer is already exists with given pancard or passport with different aadharnumber!)");
									}
								}
							}
						}
					}else {
						if (Util.isEmptyObject(customerBookingForm.getCustomerInfo().getPancard()) && Util.isEmptyObject(customerBookingForm.getCustomerInfo().getPassport())) {
							errorMsgs.add("Customer's pancard/passport details are required");
						} 
					}
				} */
				
						
				/* Customer Date Of Birth 
				if(Util.isEmptyObject(customerBookingForm.getCustomerInfo().getDob())) {
					errorMsgs.add("Customer's date of Birth is required");
				} */
				
				/* Customer  Relation Name 
				if(Util.isEmptyObject(customerBookingForm.getCustomerInfo().getRelationName())){
					errorMsgs.add("Customer's Relation Name is required");
				} */
				
				/* Customer Relation with
				if(Util.isEmptyObject(customerBookingForm.getCustomerInfo().getRelationWith())){
					errorMsgs.add("Customer's Relation With is required");
				} */
				
			} else {
				errorMsgs.add(null);
				errorMsgs.add(null);
				errorMsgs.add("Invalid Customer Info");
			}
			/*
		    if(Util.isNotEmptyObject(customerBookingForm.getCustomerBookingInfo().getSalesTeamLeadId())) {
			BookingFormRequest bookingFormRequest = new BookingFormRequest();
			bookingFormRequest.setRequestUrl("saveBookingDetails");
			bookingFormRequest.setSalesTeamLeadId(customerBookingForm.getCustomerBookingInfo().getSalesTeamLeadId());
			List<CustBookInfoPojo> custBookInfoPojos = bookingFormServiceDaoImpl.getCustBookInfo(bookingFormRequest);
			    /*  if LeadId is in Active or Pending state  */
			  /*
			  if(isSalesTeamLeadIdDuplicate(custBookInfoPojos)) {
			     	errorMsgs.add("Invalid Sales Team Lead Id (Sales TeamLead Id is already exists or in Pending State.");
			  } *//*
			}else {
				errorMsgs.add("Invalid Sales Team Lead Id");
			} */
			if (Util.isNotEmptyObject(customerBookingForm.getCustomerBookingInfo())) {
				
				/* Customer Mobile Number  */
			    if (Util.isEmptyObject(customerBookingForm.getCustomerBookingInfo().getPhoneNo())) {
					errorMsgs.add("Customer's mobile number is required");
				}
			   /*  check if customer contains more than one number like 8500085263/8500085263  */
			   else {
			    	String[] mobileNos = null;
			    	if(customerBookingForm.getCustomerBookingInfo().getPhoneNo().contains("/")) {		
			    	mobileNos = StringUtils.split(customerBookingForm.getCustomerBookingInfo().getPhoneNo(),"/");
			    	}else if(customerBookingForm.getCustomerBookingInfo().getPhoneNo().contains(",")) {
			    		mobileNos = StringUtils.split(customerBookingForm.getCustomerBookingInfo().getPhoneNo(),",");
			    	}
			    	if(mobileNos!=null && mobileNos.length>1) {
			    		 customerBookingForm.getCustomerBookingInfo().setPhoneNo(mobileNos[0]);
			    		 customerBookingForm.getCustomerBookingInfo().setAlternatePhoneNo(mobileNos[1]);
			    	}
				}
				/* Customer Email  */
				if (Util.isEmptyObject(customerBookingForm.getCustomerBookingInfo().getEmail())) {
					errorMsgs.add("Customer's email is required");
				}
				
				/* Customer Telephone Number 
				if (Util.isEmptyObject(customerBookingForm.getCustomerBookingInfo().getTelePhone())){
					errorMsgs.add("Customer's Telephone Number is required");
				} */
				
				/*  customer Address 
				if (validateAddress(customerBookingForm.getAddressInfos())){
					errorMsgs.add("Customer's Address is required");
				} */
				
			} else {
				errorMsgs.add("Invalid Customer Booking Info");
			}
			
			/*  stopping validation for customer proffisional Info. */
			if(false) {
			    if (Util.isNotEmptyObject(customerBookingForm.getProfessionalInfo())) {
				   if (Util.isNotEmptyObject(customerBookingForm.getProfessionalInfo().getOraganizationDetails())) {
					  if (Util.isEmptyObject(customerBookingForm.getProfessionalInfo().getOraganizationDetails().getOrganizationTypeName()) && Util.isEmptyObject(customerBookingForm.getProfessionalInfo().getOraganizationDetails().getIfOtherOrgTypeName())) {
						errorMsgs.add("Customer's organization type is required");
					  }
				   } else {
					errorMsgs.add("Customer's organization details are required");
				  }
				if (Util.isNotEmptyObject(customerBookingForm.getProfessionalInfo().getSectorDetailsInfo())) {
					if (Util.isEmptyObject(customerBookingForm.getProfessionalInfo().getSectorDetailsInfo().getWorkSectorName()) && Util.isEmptyObject(customerBookingForm.getProfessionalInfo().getSectorDetailsInfo().getIfOtherWorkSectorName())) {
						errorMsgs.add("Customer's industry sector type is required");
					}
				} else {
					errorMsgs.add("Customer's industry sector details are required");
				}
				if (Util.isNotEmptyObject(customerBookingForm.getProfessionalInfo().getWorkFunctionInfo())) {
					if (Util.isEmptyObject(
							customerBookingForm.getProfessionalInfo().getWorkFunctionInfo().getWorkFunctionName())
							&& Util.isEmptyObject(customerBookingForm.getProfessionalInfo().getWorkFunctionInfo()
									.getIfOtherworkFunctionName())) {
						errorMsgs.add("Customer's work function type is required");
					}
				 } else {
					errorMsgs.add("Customer's work function details are required");
				   }
			  } else {
				errorMsgs.add("Invalid Customer Professional Details");
			   }
			}
			
			/* validating coapplicant Details */
			List<CoApplicentDetailsInfo> coApplicentDetails = customerBookingForm.getCoApplicentDetails();
			for (CoApplicentDetailsInfo coApplicentDetailsInfo : coApplicentDetails) {
			    if (isCoaaplicant(coApplicentDetailsInfo)) {
			    	/*
			    	if( validateCoAplicantDetails(coApplicentDetailsInfo,errorMsgs)) {
			    	// List<Co_ApplicantPojo> listCoApplicantsByPanCard  = null;
			    	/* if CoApplicant is indian we will check Aadhar Number && Pancard 
					if (Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getNationality()) && (coApplicentDetailsInfo.getCo_ApplicantInfo().getNationality().equalsIgnoreCase(CountryCode.IN.getName()) || coApplicentDetailsInfo.getCo_ApplicantInfo().getNationality().equalsIgnoreCase(Nationality.INDIAN.getName()))) {
							 listCoApplicantsByPanCard = bookingFormServiceDaoImpl.getCo_ApplicantByPanCard(coApplicentDetailsInfo.getCo_ApplicantInfo().getPancard(),null,Status.ACTIVE);
							if(Util.isNotEmptyObject(listCoApplicantsByPanCard)) {
								if(!listCoApplicantsByPanCard.get(0).getAadharId().equalsIgnoreCase(coApplicentDetailsInfo.getCo_ApplicantInfo().getAadharId())) {
									errorMsgs.add("CoApplicant aadhaar number is not correct.(CoApplicants is already exists with given pancard or passport with different aadharnumber!)");	
								}
							}
					} 
			    	} */
			    }
			} 
			/*
			if(validateApplicantCoapplicantAadharDetails(customerBookingForm)) {
				errorMsgs.add("Adhar Card Numbers Must be Unique");
			} */
						
			/* Flat/Floor/Block/Site */
			if (Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo())) {
				if (Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getSiteInfo())
						&& Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getSiteInfo().getName())
						&& Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getFlatInfo())
						&& Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getFlatInfo().getFlatNo())
						&& Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getFloorInfo())
						&& Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getFloorInfo().getFloorName())
						&& Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getBlockInfo())
						&& Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getBlockInfo().getName())) {
					BookingFormRequest bookingFormRequest = new BookingFormRequest();
					
					/* project name */
					bookingFormRequest.setSiteName(customerBookingForm.getFlatBookingInfo().getSiteInfo().getName());
					/* Flat No */
					bookingFormRequest.setFlatNo(customerBookingForm.getFlatBookingInfo().getFlatInfo().getFlatNo());
					/* Floor Name  */
					bookingFormRequest.setFloorName(customerBookingForm.getFlatBookingInfo().getFloorInfo().getFloorName());
					/* Block Name  */
					bookingFormRequest.setBlockName(customerBookingForm.getFlatBookingInfo().getBlockInfo().getName());

					List<CustomerPropertyDetailsPojo> masterFlatFloorBlockSiteData = bookingFormServiceDaoImpl
							.getFlatFloorBlockSiteByNames(bookingFormRequest);
					if (masterFlatFloorBlockSiteData == null || masterFlatFloorBlockSiteData.size() <= 0) {
						errorMsgs.add("Invalid Flat/Floor/Block/Site Details");
					} else {
						bookingFormRequest.setRequestUrl("validateBookingForm");
						List<CustomerPropertyDetailsPojo> custPropDetails = bookingFormServiceDaoImpl
								.getCustomerPropertyDetails(bookingFormRequest);
						if (custPropDetails != null && custPropDetails.size() > 0) {
							errorMsgs.add("Pending/Active Flat booking already existed for provided Site/Flat");
						}
					}
				} else {
					errorMsgs.add("Plz provide Flat/Floor/Block/Site Details");
				}
				
				/*  Facing   
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getFacing())){
					errorMsgs.add("Plz provide Flat Facing Details");
				}
				 SBUA 
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getSbua())){
					errorMsgs.add("Plz provide Flat SBUA Details");
				}
				 CarpetArea 
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getCarpetArea())){
					errorMsgs.add("Plz provide Flat carpetArea Details");
				}
				 UDS 
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getUds())){
					errorMsgs.add("Plz provide Flat UDS Details");
				}
				/* FLAT Booking Date */
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getBookingDate())){
					errorMsgs.add("Plz provide Flat Booking Date Details");
				}
				
				/* Agreement Date 
				if(Util.isEmptyObject(customerBookingForm.getCheckListCRM().getExpectedAgreeDate())){
					errorMsgs.add("Plz provide Flat Agreement Date Details");
				}  */
				
				/* Salesforce Booking Id */
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getBookingId())) {
					errorMsgs.add("Plz provide Salesforce Booking Id");
				}
				
				/* Basic Flat Cost */
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getFlatCost().getBasicFlatCost())){
					errorMsgs.add("Plz provide Flat Basic Cost Details");
				}
				
				/*  Total Cost  */
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getFlatCost().getTotalCost())){
					errorMsgs.add("Plz provide Flat Total Cost Details");
				}
				/*  Amenities Information */
				if(Util.isEmptyObject(customerBookingForm.getFlatBookingInfo().getAminitiesInfraCostInfo())) {
					errorMsgs.add("Plz provide Flat Amenities Details");
				}
			} else {
				errorMsgs.add("Invalid Flat Booking Info");
			}
			/*
			if (Util.isEmptyObject(customerBookingForm.getCheckListSalesHead())) {
				errorMsgs.add("Invalid Sales Head Checklist");
			}

			if (Util.isEmptyObject(customerBookingForm.getCheckListCRM())) {
				errorMsgs.add("Invalid CRM Checklist");
			}

			if (Util.isEmptyObject(customerBookingForm.getCheckListLegalOfficer())) {
				errorMsgs.add("Invalid Legal Officer Checklist");
			}

			if (Util.isEmptyObject(customerBookingForm.getCheckListRegistration())) {
				errorMsgs.add("Invalid Registration Checklist");
			}
			if (Util.isEmptyObject(customerBookingForm.getCustomerApplicationInfo())) {
				errorMsgs.add("Invalid Customer Application Details");
			}
			if (Util.isEmptyObject(customerBookingForm.getCustomerKYCSubmitedInfo())) {
				errorMsgs.add("Invalid Customer KYC Submitted Documents");
			} */
		} else {
			errorMsgs.add(null);
			errorMsgs.add(null);
			errorMsgs.add("Invalid Booking Form Details");
		}
		return errorMsgs;
	}
	
	private boolean isCoaaplicant(CoApplicentDetailsInfo coApplicentDetailsInfo) {
		logger.info("**** The control is inside the isCoaaplicants in BookingFormServiceImpl *****");
		if((Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getFirstName()) || Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getAadharId())) && (Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getPancard()) || Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getPassport()))){
			return true;
		}
		return false;
	}
	
	private boolean validateAddress(List<AddressInfo> addressInfos) {
		logger.info("**** The control is inside the validateCustomerAddress in BookingFormServiceImpl *****");
		for(AddressInfo addressInfo  : addressInfos) {
			if(addressInfo.getAddressMappingType().getAddressType().equals(Status.PERMENANT.getDescription())){
				if(Util.isNotEmptyObject(addressInfo.getAddress1())){
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * returns True is if coaplicantsDetails verified.
	 */
	@SuppressWarnings("unused")
	private boolean validateCoAplicantDetails(CoApplicentDetailsInfo coApplicentDetailsInfo,List<String> errorMsgs)
			throws IllegalAccessException {
		logger.info("**** The control is inside the validateCoAplicantDetails in BookingFormServiceImpl *****");
	
		List<String> coApplicantfieldNames = new ArrayList<String>();

		if (Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getNationality()) && (coApplicentDetailsInfo.getCo_ApplicantInfo().getNationality().equalsIgnoreCase(CountryCode.IN.getName()) || coApplicentDetailsInfo.getCo_ApplicantInfo().getNationality().equalsIgnoreCase(Nationality.INDIAN.getName()))) {
			coApplicantfieldNames.add("firstName");
			coApplicantfieldNames.add("dateOfBirth");
			coApplicantfieldNames.add("relationName");
			coApplicantfieldNames.add("relationWith");
			coApplicantfieldNames.add("pancard");
			coApplicantfieldNames.add("aadharId");
			coApplicantfieldNames.add("nationality");	
		}
		else {
			coApplicantfieldNames.add("firstName");
			coApplicantfieldNames.add("pancard");
			//coApplicantfieldNames.add("passport");
			coApplicantfieldNames.add("nationality");
			coApplicantfieldNames.add("dateOfBirth");
			coApplicantfieldNames.add("relationName");
			coApplicantfieldNames.add("relationWith");
		  } 
		List<String> coApplicantBookingInfofieldNames = new ArrayList<String>();
		coApplicantBookingInfofieldNames.add("phoneNo");
		coApplicantBookingInfofieldNames.add("email");
		coApplicantBookingInfofieldNames.add("telePhone");
		
			if (Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo())) {
				for(String name : coApplicantfieldNames) {
					if(Util.checkNull(coApplicentDetailsInfo.getCo_ApplicantInfo(), Arrays.asList(name))) {
						errorMsgs.add("Plz provide "+(Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getFirstName())?coApplicentDetailsInfo.getCo_ApplicantInfo().getFirstName():"")+" coapplicant "+name+"Information");
					}
				}	
				if (Util.isNotEmptyObject(coApplicentDetailsInfo.getCoApplicentBookingInfo())) {
						for(String name : coApplicantBookingInfofieldNames) {
							if(Util.checkNull(coApplicentDetailsInfo.getCoApplicentBookingInfo(),Arrays.asList(name))){
								errorMsgs.add("Plz provide "+(Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getFirstName())?coApplicentDetailsInfo.getCo_ApplicantInfo().getFirstName():"")+" coapplicant "+name+" Information");
							}
							else {
								 /*  check if customer contains more than one number like 8500085263/8500085263  */
								if(name.equalsIgnoreCase("phoneNo")) {
									String[] mobileNos = null;
							    	if(coApplicentDetailsInfo.getCoApplicentBookingInfo().getPhoneNo().contains("/")) {		
							    	mobileNos = StringUtils.split(coApplicentDetailsInfo.getCoApplicentBookingInfo().getPhoneNo(),"/");
							    	}else if(coApplicentDetailsInfo.getCoApplicentBookingInfo().getPhoneNo().contains(",")) {
							    		mobileNos = StringUtils.split(coApplicentDetailsInfo.getCoApplicentBookingInfo().getPhoneNo(),",");
							    	}
									if(mobileNos!=null && mobileNos.length>1) {
							    		coApplicentDetailsInfo.getCoApplicentBookingInfo().setPhoneNo(mobileNos[0]);
							    		coApplicentDetailsInfo.getCoApplicentBookingInfo().setAlternatePhoneNo(mobileNos[1]);
							    	}
								}
							}
						}
					if(Util.isNotEmptyObject(coApplicentDetailsInfo.getAddressInfos())){
						/* Address is not null it returns false */
						if(validateAddress(coApplicentDetailsInfo.getAddressInfos())){
							errorMsgs.add("Plz provide"+ (Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getFirstName())?coApplicentDetailsInfo.getCo_ApplicantInfo().getFirstName():"")  +" coapplicant address Information");
							return false;
						}
					}
					logger.info("**** Coapplicant details verified successfully ****");
				} else {
					return false;
				}
			} else {
				return false;
			}
		return true;
	}
	private boolean validateCoAplicantCheckListVerificationDetails(CoApplicantCheckListVerificationInfo applicantCheckListVerificationInfo)
			throws IllegalAccessException {
		logger.info("**** The control is inside the validateCoAplicantDetails in BookingFormServiceImpl *****");
		List<String> CoApplicantCheckListVerificationInfofieldNames = new ArrayList<String>();
		CoApplicantCheckListVerificationInfofieldNames.add("checkListStatus");
		//CoApplicantCheckListVerificationInfofieldNames.add("CoapplicentPanCard");
	
		if((Util.isNotEmptyObject(applicantCheckListVerificationInfo)) && Util.checkNotNull(applicantCheckListVerificationInfo, CoApplicantCheckListVerificationInfofieldNames)) {
			logger.info("**** Coapplicant CheckListVerification  details verified successfully ****");
			return true;
		}else {
		   return false;
		}
	}
	private boolean validateApplicantCoapplicantAadharDetails(CustomerBookingFormInfo customerBookingForm) {
		logger.info("**** The control is inside the validateApplicantCoapplicantAadharDetails in BookingFormServiceImpl *****");
		Set<String> aadharSet = new HashSet<String>();
		/*  Adding Customer AadharCard */
		String customerAadhar = Util.isNotEmptyObject(customerBookingForm.getCustomerInfo().getAdharNumber())?customerBookingForm.getCustomerInfo().getAdharNumber():"";
		aadharSet.add(customerAadhar);
		List<CoApplicentDetailsInfo> coApplicentDetails = customerBookingForm.getCoApplicentDetails();
		for (CoApplicentDetailsInfo coApplicentDetailsInfo : coApplicentDetails) {
			if (isCoaaplicant(coApplicentDetailsInfo)) {
				if (Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getAadharId()) && !coApplicentDetailsInfo.getCo_ApplicantInfo().getAadharId().equalsIgnoreCase("N/A") ) {
					if (aadharSet.contains(coApplicentDetailsInfo.getCo_ApplicantInfo().getAadharId())) {
						return true;
					} else {
						aadharSet.add(coApplicentDetailsInfo.getCo_ApplicantInfo().getAadharId());
					} 
				}
			}
		}
	    return false;
	}
	
	@Override
	public void cancelBookingFormHelper(@NonNull BookingFormRequest bookingFormRequest) throws InSufficeientInputException {
		logger.info("**** The control is inside the cancelBookingFormHelper in BookingFormServiceImpl *****");
		List<String> errorMsgs = new ArrayList<String>();
		Properties prop = responceCodesUtil.getApplicationProperties();
		boolean EnableAllStatus = Util.isEmptyObject(prop.getProperty("ENABLE_ALL_STATUS"))?true:prop.getProperty("ENABLE_ALL_STATUS").equals("yes");
		/* if request is coming for cancel booking form */
		if (Util.isNotEmptyObject(bookingFormRequest.getActionStr()) && bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")
				 ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Swap") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Available") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case") || 
					bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Retained") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Delete") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("active") ||
				//	bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
					bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
					bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
					
				
				) {
			if (Util.isNotEmptyObject(bookingFormRequest.getSiteName()) && Util.isNotEmptyObject(bookingFormRequest.getBlockName()) && Util.isNotEmptyObject(bookingFormRequest.getFloorName()) && Util.isNotEmptyObject(bookingFormRequest.getFlatNo())
					//&& bookingFormRequest.getBookingformCanceledDate() != null && Util.isNotEmptyObject(bookingFormRequest.getComments())
				) {
				
				if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")) {//acp
    				//this condition required only for cancel status of booking
    				if(bookingFormRequest.getBookingformCanceledDate() == null || Util.isEmptyObject(bookingFormRequest.getComments())) {
    					errorMsgs.add("The Invalid Input is given for requested service.");
    					throw new InSufficeientInputException(errorMsgs);
    				}
    			}
				
				/* Checking Salesforce Booking Id */
				if(Util.isEmptyObject(bookingFormRequest.getBookingId())) {
					logger.info("*** Given Salesforce Booking Id is empty inside the cancelBookingFormHelper in BookingFormRestServiceImpl ****");
					throw new InSufficeientInputException(Arrays.asList("Error occured while getting Flat Related details of customer, Given Salesforce Booking Id is empty"));
				}
				
				List<CustomerPropertyDetailsPojo> masterFlatFloorBlockSiteData = bookingFormServiceDaoImpl.getFlatFloorBlockSiteByNames(bookingFormRequest);
				if (Util.isNotEmptyObject(masterFlatFloorBlockSiteData) && Util.isNotEmptyObject(masterFlatFloorBlockSiteData.get(0)) && Util.isNotEmptyObject(masterFlatFloorBlockSiteData.get(0).getFlatId())) {
					EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
					employeeTicketRequestInfo.setFlatId(masterFlatFloorBlockSiteData.get(0).getFlatId());
					employeeTicketRequestInfo.setRequestUrl("actionBookingDetails");
					//List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl.getFlatbookingDetails(employeeTicketRequestInfo, Status.ACTIVE);
					FlatBookingInfo flatBookingInfo = new FlatBookingInfo();
					flatBookingInfo.setBookingId(bookingFormRequest.getBookingId());
					flatBookingInfo.setFlatId(masterFlatFloorBlockSiteData.get(0).getFlatId());
					List<FlatBookingPojo> flatBookingPojos = bookingFormServiceDaoImpl.getOldFlatBookingDetails(flatBookingInfo, ServiceRequestEnum.SALESFORCE_UPDATE_BOOKING);
					List<EmployeePojo> employeePojos = bookingFormServiceDaoImpl.getEmployeeDetails(bookingFormRequest.getEmployeeName(),null);
					if (Util.isNotEmptyObject(flatBookingPojos) && Util.isNotEmptyObject(flatBookingPojos.get(0).getFlatBookingId()) && Util.isNotEmptyObject(flatBookingPojos.get(0).getCustomerId())) {
						/* setting customerId and flatBookingId */
						bookingFormRequest.setFlatBookingId(flatBookingPojos.get(0).getFlatBookingId());
						bookingFormRequest.setCustomerId(flatBookingPojos.get(0).getCustomerId());
						bookingFormRequest.setFlatId(flatBookingPojos.get(0).getFlatId());
						bookingFormRequest.setEmpId(Util.isNotEmptyObject(employeePojos)? Util.isNotEmptyObject(employeePojos.get(0).getEmployeeId())? employeePojos.get(0).getEmployeeId():0l:0l);
						bookingFormRequest.setActualStatusId(flatBookingPojos.get(0).getStatusId());
						if (bookingFormRequest.getRequestUrl().equalsIgnoreCase("actionBookingDetails")) {
							List<String> listOfBookingStatusNames = Arrays.asList("cancel", "swap", "available","blocked", "not open", "price update", "legal case", "pmay scheme eligible","retained","active","cancelled not refunded","Assignment");
							if (listOfBookingStatusNames.contains(bookingFormRequest.getActionStr().toLowerCase())) {
								List<Long> statusIds = new ArrayList<Long>();
								if(!"active".equalsIgnoreCase(bookingFormRequest.getActionStr())) {
								statusIds.add(Status.PENDING.getStatus());
								}
								statusIds.add(Status.ACTIVE.getStatus());
								statusIds.add(Status.CANCELLED_NOT_REFUNDED.getStatus());
								if (bookingFormRequest.getActualStatusId() != null&& !statusIds.contains(bookingFormRequest.getActualStatusId()) && EnableAllStatus) {
									errorMsgs.add("Sorry.. "+ bookingFormRequest.getActionStr()+" request got failed, we can " + bookingFormRequest.getActionStr()+" the Pending ,Active and Cancelled not refunded state bookings only but here booking status is "+Status.getDescriptionByStatus(bookingFormRequest.getActualStatusId()));
									throw new InSufficeientInputException(errorMsgs);
								}
							}
						}
					}else {
						logger.info("*** Given Salesforce Booking Id is empty inside the cancelBookingFormHelper in BookingFormRestServiceImpl ****");
						throw new InSufficeientInputException(Arrays.asList("Error occured while getting Flat Related details of customer, Given Salesforce Booking Id is incorrect"));
					}
				}else {
					logger.info("*** Error occured while getting Flat Related details of customer in cancelBookingFormHelper in BookingFormRestServiceImpl ****");
					throw new InSufficeientInputException(Arrays.asList("Error occured while getting Flat Related details of customer, Given Salesforce Booking Id is empty"));
				}
		    }else {
				errorMsgs.add("The Invalid Input is given for requested service.");
				throw new InSufficeientInputException(errorMsgs);
			}
		}
	}
	
	private boolean checkCustomerAssociatedFlats(@NonNull BookingFormRequest bookingFormRequest) {
		logger.info("**** The control is inside the checkCustomerAssociatedFlats in BookingFormServiceImpl *****");
		
		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfo.setRequestUrl("customerAssociatedFlats");
		employeeTicketRequestInfo.setCustomerId(bookingFormRequest.getCustomerId());
		
		/* getting customer Associated all Flats if customer have  only one Active flat we can inactive customer.
		 * if customer have any other pending or active flat leave it.
         */
		List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl.getFlatbookingDetails(employeeTicketRequestInfo, Status.ACTIVE);
	
		if(Util.isNotEmptyObject(flatBookingPojos)) {
			for(FlatBookingPojo flatBookingPojo : flatBookingPojos) {
				/* if customer have any other flat ratherthan this. */
				if(Util.isNotEmptyObject(flatBookingPojo) && Util.isNotEmptyObject(flatBookingPojo.getStatusId()) &&  (flatBookingPojo.getStatusId().equals(Status.PENDING.getStatus()) || flatBookingPojo.getStatusId().equals(Status.ACTIVE.getStatus())) && !(flatBookingPojo.getFlatId().equals(bookingFormRequest.getFlatId()))){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkCoAplicantStatus(@NonNull BookingFormRequest bookingFormRequest) {
		logger.info("**** The control is inside the checkCoAplicantStatus in BookingFormServiceImpl *****");
		 BookingFormRequest bookingFormRequestFinal = new BookingFormRequest();
		 bookingFormRequestFinal.setCoApplicantId(bookingFormRequest.getCoApplicantId());
		 bookingFormRequestFinal.setRequestUrl("checkcoaplicants");
		List<CoAppBookInfoPojo> coAppBookInfoPojos = bookingFormServiceDaoImpl.getCoAppBookInfo(bookingFormRequestFinal);
		
		if(Util.isNotEmptyObject(coAppBookInfoPojos)) {
			for(CoAppBookInfoPojo coAppBookInfoPojo : coAppBookInfoPojos) {
				if(Util.isNotEmptyObject(coAppBookInfoPojo.getCustBookInfoId()) && !coAppBookInfoPojo.getCustBookInfoId().equals(bookingFormRequest.getCustBookInfoId()) && (coAppBookInfoPojo.getStatusId().equals(Status.PENDING.getStatus()) || coAppBookInfoPojo.getStatusId().equals(Status.ACTIVE.getStatus()))) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private void insertCheckListSalesHead(CustomerBookingFormInfo customerBookingFormInfo,BookingFormRequest bookingFormRequest,BookingFormSavedStatus bookingFormSavedStatus,Long flatBookId,Long customerId,BookingFormMapper mapper,List<String> errorMsgs) throws InSufficeientInputException {
		logger.info("**** The control is inside the insertCheckListSalesHead in BookingFormServiceImpl *****");
		List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = new ArrayList<CustomerCheckListVerificationInfo>();
		try {
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListSalesHead().getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
						Long departmentId = Department.SALES.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
						Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer Sales Head checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							objCustomerCheckListVerificationInfo.setCustId(customerId);
							objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
							objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
							objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
							logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist verification details ***");
							bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));	
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in saleshead departments checklists) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in saleshead  checklists -");
							throw new InSufficeientInputException(errorMsgs);
						}
					}
				  }
					/* Authorized signatuer names is null we didnt save these details. */
					if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListSalesHead().getAuthorizedSignatoryName())  ||  Util.isNotEmptyObject(customerBookingFormInfo.getCheckListSalesHead().getProjectSalesheadName())){
						List<EmployeePojo> salesHeadAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getAuthorizedSignatoryName(),null, new Department[] {Department.SALES,Department.MANAGEMENT});
						List<EmployeePojo> salesHeadEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getProjectSalesheadName(), null,Department.SALES);
						customerBookingFormInfo.getCheckListSalesHead().setProjectSalesheadId(Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos.get(0))?salesHeadEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListSalesHead().setCustomerId(customerId);
						customerBookingFormInfo.getCheckListSalesHead().setAuthorizedSignatoryId(Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos.get(0))?salesHeadAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListSalesHead().setFlatBookingId(flatBookId);
						logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist details **");
						bookingFormServiceDaoImpl.saveCheckListSalesHead(mapper.CheckListSalesHeadInfoToCheckListSalesHeadPojo(customerBookingFormInfo.getCheckListSalesHead()));
						}
				}else {
				errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				errorMsgs.add(bookingFormSavedStatus.getCustName());
				errorMsgs.add(" saleshead checklist details is empty ");
				logger.error(" saleshead checklist details is empty  -");
				throw new InSufficeientInputException(errorMsgs);
				}
		} catch (Exception e) {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving saleshead checklist details - ");
			logger.error("Error occured while saving saleshead checklist details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	
	 private void updateCheckListSalesHead(List<CustChecklistVerificationPojo> checklistVerificationPojos,CustomerBookingFormInfo customerBookingFormInfo,BookingFormRequest bookingFormRequest,BookingFormSavedStatus bookingFormSavedStatus,Long flatBookId,Long customerId,BookingFormMapper mapper,List<String> errorMsgs) throws InSufficeientInputException {
		List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = new ArrayList<CustomerCheckListVerificationInfo>();
		boolean flag=true;
		logger.info("**** The control is inside the updateCheckListSalesHead in BookingFormServiceImpl *****");
		try {
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListSalesHead().getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
						Long departmentId = Department.SALES.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
						Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer Sales Head checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							for(CustChecklistVerificationPojo  checklistPojo :checklistVerificationPojos){
								/* if  checklist is already present check status if it is modified update details */
								if(checklistPojo.getChecklistDeptMapId().equals(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId())){
									flag = false;
									/* checking verification status */
									Long custChecklistVerifiId = mapper.getStatusId(objCustomerCheckListVerificationInfo.getCheckListStatus());
									if(Util.isNotEmptyObject(checklistPojo.getIs_verified())?checklistPojo.getIs_verified().equals(custChecklistVerifiId):false){
										/* If both status are same no need to update anything   */
										break;
								    }
									/* update status in checklist verification table */
									else {
									 BookingFormRequest request = new BookingFormRequest();
									 request.setStatusId(custChecklistVerifiId);
									 request.setCheckListVerfiId(checklistPojo.getCustCheckVeriId());
									/* based on  checklist verification Id */
									 int result = bookingFormServiceDaoImpl.updateCustChecklistVerification(request);
									 logger.info("*** The following custcheckverifyid is updated ****"+result);
									 break;
								    }
								}
							}
							if(flag) {
								objCustomerCheckListVerificationInfo.setCustId(customerId);
								objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
								objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
								objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
								logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist verification details ***");
								bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));	
							}
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in saleshead departments checklists) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in saleshead  checklists -");
							throw new InSufficeientInputException(errorMsgs);
						}
					  }
					}
				/* updating CHECKLIST_SALESHEAD table */
				logger.info("***** inside getBookingFormDetails() in BookingFormRestService, getting checkListSalesInfo *****");
				List<CheckListSalesHeadPojo> checkListSalesHeadPojos = bookingFormServiceDaoImpl.getCheckListSalesHead(bookingFormRequest);
				/*  Insert if checkListSalesHeadPojos is empty */
				if(Util.isEmptyObject(checkListSalesHeadPojos)) {
					  /* Authorized signatuer names is null we didnt save these details. */
					  if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListSalesHead().getAuthorizedSignatoryName())  ||  Util.isNotEmptyObject(customerBookingFormInfo.getCheckListSalesHead().getProjectSalesheadName())){
					  List<EmployeePojo> salesHeadAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getAuthorizedSignatoryName(),null, new Department[] {Department.SALES,Department.MANAGEMENT});
					  List<EmployeePojo> salesHeadEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getProjectSalesheadName(), null,Department.SALES);
					  customerBookingFormInfo.getCheckListSalesHead().setProjectSalesheadId(Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos.get(0))?salesHeadEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
					  customerBookingFormInfo.getCheckListSalesHead().setCustomerId(customerId);
					  customerBookingFormInfo.getCheckListSalesHead().setAuthorizedSignatoryId(Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos.get(0))?salesHeadAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
					  customerBookingFormInfo.getCheckListSalesHead().setFlatBookingId(flatBookId);
					 logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist details **");
					 bookingFormServiceDaoImpl.saveCheckListSalesHead(mapper.CheckListSalesHeadInfoToCheckListSalesHeadPojo(customerBookingFormInfo.getCheckListSalesHead()));
					 }
			    /*  updating if already exists. */		  
				}else {
					CheckListSalesHeadInfo checkListSalesHeadInfo = customerBookingFormInfo.getCheckListSalesHead();
					StringBuilder query = null;
				       if(Util.isNotEmptyObject(checkListSalesHeadInfo.getSourceofBooking()) 
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getReferralBonusStatus())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getOffersAny())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getAvailability())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getAvailabilityIfOther())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getSalesTeamCommitments())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getRemarks())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getProjectSalesheadName())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getProjectSalesHeadDate())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getAuthorizedSignatoryName())
				    		   || Util.isNotEmptyObject(checkListSalesHeadInfo.getAuthorizedSignatoryDate())
				    		   ){
				    	   query = new StringBuilder(" UPDATE CHECKLIST_SALESHEAD  SET ");
				    	if(Util.isNotEmptyObject(checkListSalesHeadInfo.getSourceofBooking())) {
				    		query.append("SOURCE_OF_BOOKING = '"+checkListSalesHeadInfo.getSourceofBooking()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getReferralBonusStatus())){
				    		query.append("REFERAL_BONUS = '"+checkListSalesHeadInfo.getReferralBonusStatus()+"',");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getOffersAny())){
				    		query.append("OFFER_DETAILS = '"+checkListSalesHeadInfo.getOffersAny()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getAvailability())){
				    		query.append("AVAILABILITY = '"+checkListSalesHeadInfo.getAvailability()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getAvailabilityIfOther())){
				    		query.append("AVAILABILITY_OTHER = '"+checkListSalesHeadInfo.getAvailabilityIfOther()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getSalesTeamCommitments())){
				    		query.append("SALES_TEAM_COMMITMENTS = '"+checkListSalesHeadInfo.getSalesTeamCommitments()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getRemarks())){
				    		query.append("REMARKS = '"+checkListSalesHeadInfo.getRemarks()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getProjectSalesheadName())){
				    		List<EmployeePojo> salesHeadEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getProjectSalesheadName(), null,Department.SALES);				    		
				    		Long projectSalesHeadEmployeeId = Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadEmployeeDetailsPojos.get(0).getEmployeeId())?salesHeadEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l;
				    		query.append("PROJECT_SALES_HEAD_ID = "+projectSalesHeadEmployeeId+" ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getProjectSalesHeadDate())){
				    		query.append("SALES_HEAD_APPROVED_DATE = TO_TIMESTAMP('"+checkListSalesHeadInfo.getProjectSalesHeadDate()+"' ,'YYYY-MM-DD HH24:MI:SS.FF'),");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getAuthorizedSignatoryName())){
				    		  List<EmployeePojo> salesHeadAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListSalesHead().getAuthorizedSignatoryName(),null, new Department[] {Department.SALES,Department.MANAGEMENT});
				    		  Long salesHeadAuthorizedEmployeeId = Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(salesHeadAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId())?salesHeadAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l;
				    		  query.append("APPROVER_EMPLOYEE_ID = "+salesHeadAuthorizedEmployeeId+" ,");
				    	}if(Util.isNotEmptyObject(checkListSalesHeadInfo.getAuthorizedSignatoryDate())){
				    		query.append("APROVER_APROVED_DATE =  TO_TIMESTAMP('"+checkListSalesHeadInfo.getAuthorizedSignatoryDate()+"', 'YYYY-MM-DD HH24:MI:SS.FF')");
				    	}
				    	if(query.toString().endsWith(",")) {
				    	  query = new StringBuilder(StringUtils.chop(query.toString()));
				    	}
				    	query.append(" WHERE CHECKLIST_SALESHEAD_ID = "+(Util.isNotEmptyObject(checkListSalesHeadPojos)?Util.isNotEmptyObject(checkListSalesHeadPojos.get(0).getSalesHeadId())?checkListSalesHeadPojos.get(0).getSalesHeadId():0l:0l));
				    	int result = bookingFormServiceDaoImpl.updateCheckList(query.toString());
				       logger.info("**** The checklist sales head is updated sucessfully ****"+result);
				      }
				}
				}else {
				errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				errorMsgs.add(bookingFormSavedStatus.getCustName());
				errorMsgs.add(" saleshead checklist details is empty ");
				logger.error(" saleshead checklist details is empty ");
				throw new InSufficeientInputException(errorMsgs);
				}
		} catch (Exception e) {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving saleshead checklist details - ");
			logger.error("Error occured while saving saleshead checklist details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	private void insertCheckListCrm(CustomerBookingFormInfo customerBookingFormInfo,BookingFormRequest bookingFormRequest,BookingFormSavedStatus bookingFormSavedStatus,Long flatBookId,Long customerId,BookingFormMapper mapper,List<String> errorMsgs) throws InSufficeientInputException {
		logger.info("**** The control is inside the insertCheckListSalesHead in BookingFormServiceImpl *****");
		List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = new ArrayList<CustomerCheckListVerificationInfo>();
		try {
			
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListCRM().getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
						Long departmentId = Department.CRM.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
		
						Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer CRM checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)){
							objCustomerCheckListVerificationInfo.setCustId(customerId);
							objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
							objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
							objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
							logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving CRM checklist verification details ***");
							bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in CRM departments checklists) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in CRM  checklists -");
							throw new InSufficeientInputException(errorMsgs);
						}
					}
				}
				/* Authorized signater names is null we didnt save these details.  */ 
				if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListCRM().getCrmVerifiedByName()) || Util.isNotEmptyObject(customerBookingFormInfo.getCheckListCRM().getAuthorizedSignatoryeName()) ) {
					List<EmployeePojo> crmEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getCrmVerifiedByName(), null,new Department[] { Department.CRM,Department.CRM_MIS});
					List<EmployeePojo> crmAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getAuthorizedSignatoryeName(), null,new Department[] {Department.MANAGEMENT});
					customerBookingFormInfo.getCheckListCRM().setCrmEmpID(Util.isNotEmptyObject(crmEmployeeDetailsPojos)?Util.isNotEmptyObject(crmEmployeeDetailsPojos.get(0))?crmEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
					customerBookingFormInfo.getCheckListCRM().setCustomerId(customerId);
					customerBookingFormInfo.getCheckListCRM().setAuthorizedSignatoryeId(Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos.get(0))?crmAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
					customerBookingFormInfo.getCheckListCRM().setFlatBookingId(flatBookId);
					logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving CRM checklist details **");
					bookingFormServiceDaoImpl.saveChecklistCrm(mapper.CheckListCRMInfoToChecklistCrmPojo(customerBookingFormInfo.getCheckListCRM()));
				}
				
		  }else {
			  	errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				errorMsgs.add(bookingFormSavedStatus.getCustName());
				errorMsgs.add(" CRM checklist details are empty ");
				logger.error(" CRM checklist details are empty  -");
				throw new InSufficeientInputException(errorMsgs);
		  }
			
		} catch (Exception e) {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving CRM checklist details - ");
			logger.error("Error occured while saving CRM checklist details - "+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	
	private void updateCheckListCrm(List<CustChecklistVerificationPojo> checklistVerificationPojos,CustomerBookingFormInfo customerBookingFormInfo,BookingFormRequest bookingFormRequest,BookingFormSavedStatus bookingFormSavedStatus,Long flatBookId,Long customerId,BookingFormMapper mapper,List<String> errorMsgs) throws InSufficeientInputException {
		List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = new ArrayList<CustomerCheckListVerificationInfo>();
		logger.info("**** The control is inside the updateCheckListCrm in BookingFormServiceImpl *****");
		boolean flag=true;
		try {
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListCRM().getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
						Long departmentId = Department.CRM.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
						Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer crm checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							for(CustChecklistVerificationPojo  checklistPojo :checklistVerificationPojos){
								/* if  checklist is already present check status if it is modified update details */
								if(checklistPojo.getChecklistDeptMapId().equals(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId())){
									flag = false;
									/* checking verification status */
									Long custChecklistVerifiId = mapper.getStatusId(objCustomerCheckListVerificationInfo.getCheckListStatus());
									if(Util.isNotEmptyObject(checklistPojo.getIs_verified())?checklistPojo.getIs_verified().equals(custChecklistVerifiId):false){
										/* If both status are same no need to update anything   */
										break;
								    }
									/* update status in checklist verification table */
									else {
									 BookingFormRequest request = new BookingFormRequest();
									 request.setStatusId(custChecklistVerifiId);
									 request.setCheckListVerfiId(checklistPojo.getCustCheckVeriId());
									/* based on  checklist verification Id */
									int result = bookingFormServiceDaoImpl.updateCustChecklistVerification(request);
									logger.info("*** The following custcheckverifyid is updated ****"+result);
									break;
								    }
								}
							}
							if(flag) {
								objCustomerCheckListVerificationInfo.setCustId(customerId);
								objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
								objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
								objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
								logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist verification details ***");
								bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));	
							}
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in crm departments checklists) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in crm  checklists ");
							throw new InSufficeientInputException(errorMsgs);
						}
					  }
					}
				
				/* updating CHECKLIST_CRM table */
				logger.info("***** inside getBookingFormDetails() in BookingFormRestService, getting checkListSalesInfo *****");
				List<ChecklistCrmPojo> checkListCrmPojos = bookingFormServiceDaoImpl.getChecklistCrm(bookingFormRequest);
				/*  Insert if checkListSalesHeadPojos is empty */
				if(Util.isEmptyObject(checkListCrmPojos)) {
					/* Authorized signater names is null we didnt save these details.  */ 
					if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListCRM().getCrmVerifiedByName()) || Util.isNotEmptyObject(customerBookingFormInfo.getCheckListCRM().getAuthorizedSignatoryeName()) ) {
						List<EmployeePojo> crmEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getCrmVerifiedByName(), null,new Department[] { Department.CRM,Department.CRM_MIS});
						List<EmployeePojo> crmAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getAuthorizedSignatoryeName(), null,new Department[] {Department.MANAGEMENT});
						customerBookingFormInfo.getCheckListCRM().setCrmEmpID(Util.isNotEmptyObject(crmEmployeeDetailsPojos)?Util.isNotEmptyObject(crmEmployeeDetailsPojos.get(0))?crmEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListCRM().setCustomerId(customerId);
						customerBookingFormInfo.getCheckListCRM().setAuthorizedSignatoryeId(Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos.get(0))?crmAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListCRM().setFlatBookingId(flatBookId);
						logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving CRM checklist details **");
						bookingFormServiceDaoImpl.saveChecklistCrm(mapper.CheckListCRMInfoToChecklistCrmPojo(customerBookingFormInfo.getCheckListCRM()));
					}
			    /*  updating if already exists. */		  
				}else {
					CheckListCRMInfo checkListCRMInfo = customerBookingFormInfo.getCheckListCRM();
					StringBuilder query = null;
				       if(Util.isNotEmptyObject(checkListCRMInfo.getCommitmentsFromSTM()) 
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getCrmRemarks())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getCrmPreferenceBankLoan())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getExpectedAgreeDateComment())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getExpectedAgreeDate())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getCrmVerifiedByName())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getCrmSignedDate())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getAuthorizedSignatoryeName())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getAuthorizedSignatoryDate())
				    		   || Util.isNotEmptyObject(checkListCRMInfo.getWelcomeCallRecord())
				    		   ){
				    	   query = new StringBuilder(" UPDATE CHECKLIST_CRM  SET ");
				    	if(Util.isNotEmptyObject(checkListCRMInfo.getCommitmentsFromSTM())) {
				    		query.append("SALES_TEAM_COMMITMENTS = '"+checkListCRMInfo.getCommitmentsFromSTM()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getCrmRemarks())){
				    		query.append("REMARKS = '"+checkListCRMInfo.getCrmRemarks()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getCrmPreferenceBankLoan())){
				    		query.append("PREFER_BANK_NAME = '"+checkListCRMInfo.getCrmPreferenceBankLoan()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getExpectedAgreeDateComment())){
				    		query.append("AGREEMENT_COMMENTS = '"+checkListCRMInfo.getExpectedAgreeDateComment()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getExpectedAgreeDate())){
				    		query.append("EXPECTED_AGREMENT_DATE = TO_TIMESTAMP('"+checkListCRMInfo.getExpectedAgreeDate()+"','YYYY-MM-DD HH24:MI:SS.FF'),");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getCrmVerifiedByName())){
				    		List<EmployeePojo> crmEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getCrmVerifiedByName(), null,new Department[] { Department.CRM,Department.CRM_MIS});
				    		Long crmEmployeeId = Util.isNotEmptyObject(crmEmployeeDetailsPojos)?Util.isNotEmptyObject(crmEmployeeDetailsPojos.get(0).getEmployeeId())?crmEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l;
				    		query.append("CRM_EMPLOYEE_ID = "+crmEmployeeId+" ,");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getCrmSignedDate())){
				    		query.append("CRM_APPROVED_DATE = TO_TIMESTAMP('"+checkListCRMInfo.getCrmSignedDate()+"','YYYY-MM-DD HH24:MI:SS.FF'),");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getAuthorizedSignatoryeName())){
				    		List<EmployeePojo> crmAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListCRM().getAuthorizedSignatoryeName(), null,new Department[] {Department.MANAGEMENT});
				    		Long crmAuthorizedEmployeeId = Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(crmAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId())?crmAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l;
				    		query.append("APROVER_EMPLOYEE_ID = "+crmAuthorizedEmployeeId+" ,");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getAuthorizedSignatoryDate())){
				    		query.append("APROVER_APROVED_DATE =  TO_TIMESTAMP('"+checkListCRMInfo.getAuthorizedSignatoryDate()+"','YYYY-MM-DD HH24:MI:SS.FF'),");
				    	}if(Util.isNotEmptyObject(checkListCRMInfo.getWelcomeCallRecord())){
				    		query.append("WELCOME_CALL_RECORD = '"+checkListCRMInfo.getWelcomeCallRecord()+"'");
				    	}
				    	if(query.toString().endsWith(",")) {
				    	  query = new StringBuilder(StringUtils.chop(query.toString()));
				    	}
				    	query.append(" WHERE CHECKLIST_CRM_ID = "+(Util.isNotEmptyObject(checkListCrmPojos)?Util.isNotEmptyObject(checkListCrmPojos.get(0).getCheckListCrmId())?checkListCrmPojos.get(0).getCheckListCrmId():0l:0l));
				    	int result = bookingFormServiceDaoImpl.updateCheckList(query.toString());
				       logger.info("**** The checklist crm is updated sucessfully ****"+result);
				      }
				}
				}else {
				errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				errorMsgs.add(bookingFormSavedStatus.getCustName());
				errorMsgs.add(" crm checklist details is empty ");
				logger.error(" crm checklist details is empty ");
				throw new InSufficeientInputException(errorMsgs);
				}
		} catch (Exception e) {
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving crm checklist details - ");
			logger.error("Error occured while saving saleshead checklist details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public BookingFormSavedStatus insertOrUpdateCheckListDetails(@NonNull BookingFormRequest bookingFormRequest) throws InSufficeientInputException{
		logger.info("*** The control is inside the insertOrUpdateCheckListSalesDetails in BookingFormRestServiceImpl ***"+bookingFormRequest);	
		List<String> errorMsgs = new ArrayList<String>();
		BookingFormMapper bookingFormMapper = new BookingFormMapper();
		BookingFormSavedStatus bookingFormSavedStatus = new BookingFormSavedStatus();
		
		final CustomerBookingFormInfo customerBookingFormsInfo = bookingFormRequest.getCustomerBookingFormsInfo();
		bookingFormRequest.setSiteName(customerBookingFormsInfo.getFlatBookingInfo().getSiteInfo().getName());
		bookingFormRequest.setBlockName(customerBookingFormsInfo.getFlatBookingInfo().getBlockInfo().getName());
		bookingFormRequest.setFloorName(customerBookingFormsInfo.getFlatBookingInfo().getFloorInfo().getFloorName());
		bookingFormRequest.setFlatNo(customerBookingFormsInfo.getFlatBookingInfo().getFlatInfo().getFlatNo());
		bookingFormSavedStatus.setCustName((Util.isNotEmptyObject(customerBookingFormsInfo.getCustomerInfo().getFirstName())?customerBookingFormsInfo.getCustomerInfo().getFirstName():"")+(Util.isNotEmptyObject(customerBookingFormsInfo.getCustomerInfo().getLastName())?customerBookingFormsInfo.getCustomerInfo().getLastName():""));
		bookingFormSavedStatus.setLeadId(customerBookingFormsInfo.getCustomerBookingInfo().getSalesTeamLeadId());
		
		if (Util.isNotEmptyObject(bookingFormRequest.getSiteName()) && Util.isNotEmptyObject(bookingFormRequest.getBlockName()) && Util.isNotEmptyObject(bookingFormRequest.getFloorName()) && Util.isNotEmptyObject(bookingFormRequest.getFlatNo()) ) {
			List<CustomerPropertyDetailsPojo> masterFlatFloorBlockSiteData = bookingFormServiceDaoImpl.getFlatFloorBlockSiteByNames(bookingFormRequest);
			if (Util.isNotEmptyObject(masterFlatFloorBlockSiteData) && Util.isNotEmptyObject(masterFlatFloorBlockSiteData.get(0)) && Util.isNotEmptyObject(masterFlatFloorBlockSiteData.get(0).getFlatId())) {
				EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
				employeeTicketRequestInfo.setFlatId(masterFlatFloorBlockSiteData.get(0).getFlatId());
				employeeTicketRequestInfo.setRequestUrl("insertOrUpdateCheckListDetails");
				List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl.getFlatbookingDetails(employeeTicketRequestInfo, Status.ACTIVE);
				if (Util.isNotEmptyObject(flatBookingPojos)&& Util.isNotEmptyObject(flatBookingPojos.get(0).getFlatBookingId()) && Util.isNotEmptyObject(flatBookingPojos.get(0).getCustomerId())) {
					/* setting customerId and flatBookingId */
					bookingFormRequest.setFlatBookingId(flatBookingPojos.get(0).getFlatBookingId());
					bookingFormRequest.setCustomerId(flatBookingPojos.get(0).getCustomerId());
					bookingFormRequest.setFlatId(flatBookingPojos.get(0).getFlatId());
				}else {
					errorMsgs.add("The Invalid Input is given for requested service.");
					logger.error("The Invalid Input is given for requested service.-");
					errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					errorMsgs.add(bookingFormSavedStatus.getCustName());
					throw new InSufficeientInputException(errorMsgs);
				}
			}
		}else {
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			logger.error("The Invalid Input is given for requested service.-");
			errorMsgs.add("The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		if(bookingFormRequest.getActionStr().equalsIgnoreCase(CheckList.CheckList_Sales_Head.getName())){
			List<CustChecklistVerificationPojo> checklistVerificationPojos = insertOrUpdateChecklistUtil(bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),Department.SALES.getId());
			if(Util.isEmptyObject(checklistVerificationPojos)) {
		    /* inserting checklist salesHead   */
		    insertCheckListSalesHead(customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
		
			}else {
				/* updating checklist salesHead  */
				updateCheckListSalesHead(checklistVerificationPojos,customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
			}
		}else if(bookingFormRequest.getActionStr().equalsIgnoreCase(CheckList.CheckList_Crm.getName())) {
			List<CustChecklistVerificationPojo> checklistVerificationPojos = insertOrUpdateChecklistUtil(bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),Department.CRM.getId());
			if(Util.isEmptyObject(checklistVerificationPojos)) {
		    /* inserting checklist crm */
			insertCheckListCrm(customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
		
			}else {
				/* updating checklist crm  */
				updateCheckListCrm(checklistVerificationPojos,customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
			}
		}else if(bookingFormRequest.getActionStr().equalsIgnoreCase(CheckList.CheckList_Legal.getName())){
			List<CustChecklistVerificationPojo> checklistVerificationPojos = insertOrUpdateChecklistUtil(bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),Department.LEGAL.getId());
			//List<CoApplicantCheckListVerificationPojo> coApplicantCheckListVerificationPojo = insertOrUpdateCoAppChecklistUtil(bookingFormRequest ); 
			//List<CoApplicantCheckListVerificationPojo> coApplicantCheckListVerificationPojo = insertOrUpdateCoAppChecklistUtil(bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),Department.LEGAL.getId(),bookingFormRequest.GETC ); 
			if(Util.isEmptyObject(checklistVerificationPojos)) {
				/* inserting checklist Legal */
				insertCheckListLegal(customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
			}else {
				/* updating checklist Legal */
				updateCheckListLegal(checklistVerificationPojos,customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
			}
			
		}else if(bookingFormRequest.getActionStr().equalsIgnoreCase(CheckList.CheckList_Registration.getName())) { 
			List<CustChecklistVerificationPojo> checklistVerificationPojos = insertOrUpdateChecklistUtil(bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),Department.MANAGEMENT.getId());
			if(Util.isEmptyObject(checklistVerificationPojos)) {
				/* inserting checklist Registration */
				insertCheckListRegistration(customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
			}else {
				/* updating checklist Registration */
				updateCheckListRegistration(checklistVerificationPojos,customerBookingFormsInfo,bookingFormRequest,bookingFormSavedStatus,bookingFormRequest.getFlatBookingId(),bookingFormRequest.getCustomerId(),bookingFormMapper,errorMsgs);
			}
		}else {
			
		}
		
		bookingFormSavedStatus.setStatus(HttpStatus.success.getDescription());
		bookingFormSavedStatus.setResponseCode(HttpStatus.success.getResponceCode());
		return bookingFormSavedStatus;
	}
	/**
	*
	* @param custBookingFormReq
	* @param bookingFormReq
	* @param bookingFormStatus
	* @param flatBookingId
	* @param customerId
	* @param bookingFormMapper
	* @param errorMsgs
	* @throws InSufficeientInputException 
	*/
	private void insertCheckListLegal(CustomerBookingFormInfo customerBookingFormInfo,BookingFormRequest bookingFormRequest,
	BookingFormSavedStatus bookingFormSavedStatus, Long flatBookId,Long customerId, BookingFormMapper bookingFormMapper, List<String> errorMsgs) throws InSufficeientInputException {
		
		logger.info("*** The control is inside the insertOrUpdateCheckListSalesDetails in BookingFormRestServiceImpl ***"+bookingFormRequest);	
		try {
			List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListLegalOfficer().getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {																				  
						Long departmentId = Department.LEGAL.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());

						Long ckId = bookingFormMapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer Legal officer checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						// bookingFormRequest.setCheckListId(mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()));
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							objCustomerCheckListVerificationInfo.setCustId(customerId);
							objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
							objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
							objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
							logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer checklist verification details ***");
							bookingFormServiceDaoImpl.saveCustChecklistVerification(bookingFormMapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in LEGAL departments checklists) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in LEGAL departments checklists -");
							throw new InSufficeientInputException(errorMsgs);
						}
						
					}	
				}
					
			} 
				  else { 
				  errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				  errorMsgs.add(bookingFormSavedStatus.getCustName());
				  errorMsgs.add(" Legal checklist details are empty ");
				  logger.error(" Legal checklist details are empty  -");
				  throw new InSufficeientInputException(errorMsgs);
				  }
				 
			List<CoApplicantCheckListVerificationInfo> CoApplicantCheckListVerificationInfo = customerBookingFormInfo.getCheckListLegalOfficer().getCoappCheckListApp();
			if(Util.isNotEmptyObject(CoApplicantCheckListVerificationInfo)) {
				for (CoApplicantCheckListVerificationInfo objCoApplicantCheckListVerificationInfo : CoApplicantCheckListVerificationInfo) {
	
					if (Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCheckListStatus())) {
						if (validateCoAplicantCheckListVerificationDetails(objCoApplicantCheckListVerificationInfo)) {
							bookingFormRequest.setDeptId(Department.LEGAL.getId());
							bookingFormRequest.setMetadataId(MetadataId.APPLICANT1.getId());
	
							Long ckId = bookingFormMapper.getCKId(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							if (ckId == null || ckId.equals(0L) || ckId == 0) {
								errorMsgs.add("Error occured while saving co applicant legal officer checklist details -- Invalid Checklist name -- "
												+ objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
								throw new InSufficeientInputException(errorMsgs);
							}
							bookingFormRequest.setCheckListId(ckId);
							// bookingFormRequest.setCheckListId(mapper.getCKId(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName()));
							List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
							List<Co_ApplicantPojo> applicantPojos = bookingFormServiceDaoImpl.getCo_ApplicantByPanCard(objCoApplicantCheckListVerificationInfo.getCoapplicentPanCard(),objCoApplicantCheckListVerificationInfo.getCoapplicentPassport(), null);
							
							if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
								objCoApplicantCheckListVerificationInfo.setCoApplicantId(applicantPojos.get(0).getCoApplicantId());
								objCoApplicantCheckListVerificationInfo.setCheckListDeptMappingId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
								objCoApplicantCheckListVerificationInfo.setFlatBookId(flatBookId);
								objCoApplicantCheckListVerificationInfo.setCustId(customerId);
								logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer CoApplicant checklist verification details ***");
								bookingFormServiceDaoImpl.saveCoApplicationCheckListVerification(bookingFormMapper.CoApplicantCheckListVerificationInfoToCoApplicantCheckListVerificationPojo(objCoApplicantCheckListVerificationInfo));
							}
							else {
								errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
								errorMsgs.add(bookingFormSavedStatus.getCustName());
								errorMsgs.add("Invalid Checklist(Given checklist is not present in LEGAL departments checklists) "+objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
								logger.error(" Given checklist is not present in LEGAL departments checklists -");
								throw new InSufficeientInputException(errorMsgs);
							}
						}
					}
                }
					    if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListLegalOfficer().getLegalOfficer()) || Util.isNotEmptyObject(customerBookingFormInfo.getCheckListLegalOfficer().getAuthorizedSignatoryName())) {	
						List<EmployeePojo> legalEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getLegalOfficer(), null,new Department[] {Department.LEGAL});
						List<EmployeePojo> legalAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getAuthorizedSignatoryName(),null, new Department[] {Department.MANAGEMENT});
						customerBookingFormInfo.getCheckListLegalOfficer().setEmpId(Util.isNotEmptyObject(legalEmployeeDetailsPojos)?Util.isNotEmptyObject(legalEmployeeDetailsPojos.get(0))?legalEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListLegalOfficer().setAuthorizedSignatoryId(Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos.get(0))?legalAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListLegalOfficer().setCustomerId(customerId);
						customerBookingFormInfo.getCheckListLegalOfficer().setFlatBookingId(flatBookId);
						logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer checklist details **");
						bookingFormServiceDaoImpl.saveCheckListLegalOfficer(bookingFormMapper.CheckListLegalOfficerInfoToCheckListLegalOfficerPojo(customerBookingFormInfo.getCheckListLegalOfficer()));
				        } /*
					     * else { errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					     * errorMsgs.add(bookingFormSavedStatus.getCustName());
					     * errorMsgs.add(" Legal checklist details are empty ");
					     * logger.error(" Legal checklist details are empty  -"); throw new
					     * InSufficeientInputException(errorMsgs); }
					     */
				
			  }
				  else {
				  errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				  errorMsgs.add(bookingFormSavedStatus.getCustName());
				  errorMsgs.add(" Legal checklist details are empty ");
				  logger.error(" Legal checklist details are empty  -"); 
				  throw new InSufficeientInputException(errorMsgs); 
				  }
				 
			
		} catch (Exception e) {
			//bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving legal officer checklist details - ");
			logger.error("Error occured while saving legal officer checklist details - " + e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}

	}
	/**
	 * 
	 * @param coApplicantCheckListVerificationPojo
	 * @param customerBookingFormInfo
	 * @param bookingFormRequest
	 * @param bookingFormSavedStatus
	 * @param flatBookId
	 * @param customerId
	 * @param mapper
	 * @param errorMsgs
	 * @throws InSufficeientInputException 
	 */
	private void updateCheckListLegal(List<CustChecklistVerificationPojo> checklistVerificationPojos,CustomerBookingFormInfo customerBookingFormInfo, BookingFormRequest bookingFormRequest,
			BookingFormSavedStatus bookingFormSavedStatus, Long flatBookId, Long customerId,BookingFormMapper mapper, List<String> errorMsgs) throws InSufficeientInputException {
		logger.info("**** The control is inside the updateCheckListLegal in BookingFormServiceImpl *****");
		try {
			/* updating customer CheckList Verification */
			List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListLegalOfficer().getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					boolean flag =true;
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
						Long departmentId = Department.LEGAL.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
						Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer Sales Head checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							for(CustChecklistVerificationPojo  checklistPojo :checklistVerificationPojos){
								/* if  checklist is already present check status if it is modified update details */
								if(checklistPojo.getChecklistDeptMapId().equals(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId())){
									flag= false;
									/* checking verification status */
									Long custChecklistVerifiId = mapper.getStatusId(objCustomerCheckListVerificationInfo.getCheckListStatus());
									if(Util.isNotEmptyObject(checklistPojo.getIs_verified())?checklistPojo.getIs_verified().equals(custChecklistVerifiId):false){
										/* If both status are same no need to update anything   */
										break;
								    }
									/* update status in checklist verification table */
									else {
										BookingFormRequest request = new BookingFormRequest();
										request.setStatusId(custChecklistVerifiId);
										request.setCheckListVerfiId(checklistPojo.getCustCheckVeriId());
										/* based on  checklist verification Id */
										int result = bookingFormServiceDaoImpl.updateCustChecklistVerification(request);
										logger.info("*** The following custcheckverifyid is updated ****"+result);
										break;
								    }
								}
							}
							if(flag) {
								objCustomerCheckListVerificationInfo.setCustId(customerId);
								objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
								objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
								objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
								logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving sales head checklist verification details ***");
								bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));	
							}
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in saleshead departments checklists) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in saleshead  checklists -");
							throw new InSufficeientInputException(errorMsgs);
						}
					  }
					}
			     }
			/* updating CoApplicant CheckList Verification */
			List<CoApplicantCheckListVerificationInfo> CoApplicantCheckListVerificationInfo = customerBookingFormInfo.getCheckListLegalOfficer().getCoappCheckListApp();			
			if(Util.isNotEmptyObject(CoApplicantCheckListVerificationInfo)) {
				for (CoApplicantCheckListVerificationInfo objCoApplicantCheckListVerificationInfo : CoApplicantCheckListVerificationInfo) {
					boolean flag = true;
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCoApplicantCheckListVerificationInfo.getCheckListStatus())) {
						Long departmentId = Department.LEGAL.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.APPLICANT1.getId());
						Long ckId = mapper.getCKId(objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving CoApplicant CheckList details -- Invalid Checklist name -- "+ objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						logger.info(" ***** checklistDepartmentMappingPojos"+checklistDepartmentMappingPojos+" *****");
						List<Co_ApplicantPojo> applicantPojos = bookingFormServiceDaoImpl.getCo_ApplicantByPanCard(objCoApplicantCheckListVerificationInfo.getCoapplicentPanCard(),objCoApplicantCheckListVerificationInfo.getCoapplicentPassport(), null); 
						List<CoApplicantCheckListVerificationPojo> coApplicantCheckListVerificationPojo =insertOrUpdateCoAppChecklistUtil(bookingFormRequest, applicantPojos.get(0));
						
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							for(CoApplicantCheckListVerificationPojo  checklistPojo :coApplicantCheckListVerificationPojo){
								/* if  checklist is already present check status if it is modified update details */
								if(checklistPojo.getCheckListDeptMappingId().equals(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId())){
									flag = false;
									/* co-applicant checklist checking verification status */
									Long custChecklistVerifiId = mapper.getStatusId(objCoApplicantCheckListVerificationInfo.getCheckListStatus());
									if(Util.isNotEmptyObject(checklistPojo.getIs_verified())?checklistPojo.getIs_verified().equals(custChecklistVerifiId):false){
										/* If both status are same no need to update anything   */
										break;
								    }
									/* update status in APPLICANT_CK_VERIFICATION table */
									else {
									 BookingFormRequest request = new BookingFormRequest();
									 request.setStatusId(custChecklistVerifiId);
									 request.setCheckListVerfiId(checklistPojo.getCheckListVerfiId());
									/* based on  checklist verification Id */
									int result = bookingFormServiceDaoImpl.updateCoApplicantChecklistVerification(request);
									logger.info("*** The following custcheckverifyid is updated ****"+result);
								    break;
									}
									
								}
							}
						   if(flag) {
							 if(Util.isNotEmptyObject(applicantPojos)) {
								objCoApplicantCheckListVerificationInfo.setCoApplicantId(applicantPojos.get(0).getCoApplicantId());
								objCoApplicantCheckListVerificationInfo.setCustId(customerId);
								objCoApplicantCheckListVerificationInfo.setDepartmentId(departmentId);
								objCoApplicantCheckListVerificationInfo.setCheckListDeptMappingId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
								objCoApplicantCheckListVerificationInfo.setFlatBookId(flatBookId);
								logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer CoApplicant checklist verification details ***");
								bookingFormServiceDaoImpl.saveCoApplicationCheckListVerification(bookingFormMapper.CoApplicantCheckListVerificationInfoToCoApplicantCheckListVerificationPojo(objCoApplicantCheckListVerificationInfo));
							}else {
								errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
								errorMsgs.add(bookingFormSavedStatus.getCustName());
								errorMsgs.add("Invalid Co-Applicant Details(Given CoApplicant details are not present) "+objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
								logger.error(" Given CoApplicant details are not present ");
								throw new InSufficeientInputException(errorMsgs);
							}
						  }	  
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in saleshead departments checklists) "+objCoApplicantCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in saleshead  checklists -");
							throw new InSufficeientInputException(errorMsgs);
						}
					  }
					}
				   /* updating Legal Officer CheckList */
				   logger.info("***** inside getBookingFormDetails() in BookingFormRestService, getting checkListSalesInfo *****");
				   List<CheckListLegalOfficerPojo> checkListLegalOfficerPojos = bookingFormServiceDaoImpl.getCheckListLegalOfficer(bookingFormRequest);
				   /*  Insert if checkListSalesHeadPojos is empty */
				   if(Util.isEmptyObject(checkListLegalOfficerPojos)) {
						if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListLegalOfficer().getLegalOfficer()) || Util.isNotEmptyObject(customerBookingFormInfo.getCheckListLegalOfficer().getAuthorizedSignatoryName())) {	
							List<EmployeePojo> legalEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getLegalOfficer(), null,new Department[] {Department.LEGAL});
							List<EmployeePojo> legalAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getAuthorizedSignatoryName(),null, new Department[] {Department.MANAGEMENT});
							customerBookingFormInfo.getCheckListLegalOfficer().setEmpId(Util.isNotEmptyObject(legalEmployeeDetailsPojos)?Util.isNotEmptyObject(legalEmployeeDetailsPojos.get(0))?legalEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
							customerBookingFormInfo.getCheckListLegalOfficer().setAuthorizedSignatoryId(Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos.get(0))?legalAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
							customerBookingFormInfo.getCheckListLegalOfficer().setCustomerId(customerId);
							customerBookingFormInfo.getCheckListLegalOfficer().setFlatBookingId(flatBookId);
							logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving legal officer checklist details **");
							bookingFormServiceDaoImpl.saveCheckListLegalOfficer(bookingFormMapper.CheckListLegalOfficerInfoToCheckListLegalOfficerPojo(customerBookingFormInfo.getCheckListLegalOfficer()));
							}else {
								errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
								errorMsgs.add(bookingFormSavedStatus.getCustName());
								errorMsgs.add(" Legal checklist details are empty ");
								logger.error(" Legal checklist details are empty  -");
								throw new InSufficeientInputException(errorMsgs);
							}
					/*  updating if already exists. */		  
					}else {
						CheckListLegalOfficerInfo checkListLegalOfficerInfo = customerBookingFormInfo.getCheckListLegalOfficer();
						StringBuilder query = null;
					       if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getBankerName())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getBank())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getContact())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getBankerEmailAddress())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getOffersIfAny())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getLegelOfficerComments())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getEmpId())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getLegalOfficeSignedate())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getAuthorizedSignatoryId())
					    		   || Util.isNotEmptyObject(checkListLegalOfficerInfo.getAuthorizedSignatoryDate())
					    		   ){
					    	   query = new StringBuilder(" UPDATE CHECKLIST_LEGAL_OFFICIER  SET ");
					    	if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getBankerName())) {
					    		query.append("BANKER_NAME = '"+checkListLegalOfficerInfo.getBankerName()+"' ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getBank())){
					    		query.append("BANK_NAME = '"+checkListLegalOfficerInfo.getBank()+"' ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getContact())){
					    		query.append("BANKER_MOBILE_NO = '"+checkListLegalOfficerInfo.getContact()+"' ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getBankerEmailAddress())){
					    		query.append("BANKER_EMAIL = '"+checkListLegalOfficerInfo.getBankerEmailAddress()+"' ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getOffersIfAny())){
					    		query.append("OFFERS_DETAILS = '"+checkListLegalOfficerInfo.getOffersIfAny()+"' ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getLegelOfficerComments())){
					    		query.append("COMMENTS = '"+checkListLegalOfficerInfo.getLegelOfficerComments()+"' ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getLegalOfficer())){
					    		List<EmployeePojo> legalEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getLegalOfficer(), null,new Department[] {Department.LEGAL});				    		
					    		Long legalEmployeeId = Util.isNotEmptyObject(legalEmployeeDetailsPojos)?Util.isNotEmptyObject(legalEmployeeDetailsPojos.get(0).getEmployeeId())?legalEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l;
					    		query.append("LEGAL_OFFICIER_EMPLOYEE_ID = "+legalEmployeeId+" ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getLegalOfficeSignedate())){
					    		query.append("LEGAL_OFFICIER_APROVED_DATE = TO_TIMESTAMP('"+checkListLegalOfficerInfo.getLegalOfficeSignedate()+"','YYYY-MM-DD HH24:MI:SS.FF'),");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getAuthorizedSignatoryName())){
					    		List<EmployeePojo> legalAuthorizedEmployeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListLegalOfficer().getAuthorizedSignatoryName(),null, new Department[] {Department.MANAGEMENT});
					    		  Long legalAuthorizedEmployeeId = Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos)?Util.isNotEmptyObject(legalAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId())?legalAuthorizedEmployeeDetailsPojos.get(0).getEmployeeId():0l:0l;
					    		  query.append("APROVER_EMPLOYEE_ID = "+legalAuthorizedEmployeeId+" ,");
					    	}if(Util.isNotEmptyObject(checkListLegalOfficerInfo.getAuthorizedSignatoryDate())){
					    		query.append("APROVERA_APROVED_DATE = TO_TIMESTAMP('"+checkListLegalOfficerInfo.getAuthorizedSignatoryDate()+"','YYYY-MM-DD HH24:MI:SS.FF')");
					    	}
					    	//TO_TIMESTAMP('2019-11-19 14:12:46.738', 'YYYY-MM-DD HH24:MI:SS.FF')
					    	if(query.toString().endsWith(",")) {
					    	  query = new StringBuilder(StringUtils.chop(query.toString()));
					    	}
					    	query.append(" WHERE CHECKLIST_LEGAL_OFFICIER_ID = "+(Util.isNotEmptyObject(checkListLegalOfficerPojos)?Util.isNotEmptyObject(checkListLegalOfficerPojos.get(0).getCheckListLegalOfficierId())?checkListLegalOfficerPojos.get(0).getCheckListLegalOfficierId():0l:0l));
					    	int result = bookingFormServiceDaoImpl.updateCheckList(query.toString());
					       logger.info("**** The checklist Legal Officer is updated sucessfully ****"+result);
					      }
					}
			    
				}else {
					errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					errorMsgs.add(bookingFormSavedStatus.getCustName());
					errorMsgs.add(" Legal checklist details are empty ");
					logger.error(" Legal checklist details are empty  -");
					throw new InSufficeientInputException(errorMsgs);
				}
				
			} catch (Exception e) {
				//bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
				errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				errorMsgs.add(bookingFormSavedStatus.getCustName());
				errorMsgs.add("Error occured while saving legal officer checklist details - ");
				logger.error("Error occured while saving legal officer checklist details - " + e.toString());
				throw new InSufficeientInputException(errorMsgs);
			}
	}

	/**
	*
	* @param custBookingFormReq
	* @param bookingFormReq
	* @param bookingFormStatus
	* @param flatBookingId
	* @param customerId
	* @param bookingFormMapper
	* @param errorMsgs
	* @throws InSufficeientInputException 
	*/
	private void insertCheckListRegistration(CustomerBookingFormInfo customerBookingFormInfo,BookingFormRequest bookingFormRequest,
		BookingFormSavedStatus bookingFormSavedStatus, Long flatBookId,Long customerId, BookingFormMapper bookingFormMapper, List<String> errorMsgs) throws InSufficeientInputException {
		try {
			CheckListRegistrationInfo checkListRegiOfficer = customerBookingFormInfo.getCheckListRegistration();
			List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = checkListRegiOfficer.getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
						// Long departmentId =
						// bookingFormMapper.getDepartmentId(objCustomerCheckListVerificationInfo.getDeparmentName());
						Long departmentId = Department.MANAGEMENT.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
			
						Long ckId = bookingFormMapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer registration checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						// bookingFormRequest.setCheckListId(mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()));
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							objCustomerCheckListVerificationInfo.setCustId(customerId);
							objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
							objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
							objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
							logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving registration checklist verification details ***");
							bookingFormServiceDaoImpl.saveCustChecklistVerification(bookingFormMapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));
						}
						else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in MANAGEMENT departments checklists) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in MANAGEMENT departments checklists -");
							throw new InSufficeientInputException(errorMsgs);
						}
					}
				}
				if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getLegalOfficerEmpName()) && Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getAccountsExecutiveEmpName()) && Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getAuthorizedSignatureName())) {
					List<EmployeePojo> regLegalEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getLegalOfficerEmpName(),null,new Department[] {Department.LEGAL});
					List<EmployeePojo> regExecEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAccountsExecutiveEmpName(), null, new Department[] {Department.ACCOUNTS});
					List<EmployeePojo> regAuthorizedEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAuthorizedSignatureName(),null,new Department[]{Department.MANAGEMENT});
					customerBookingFormInfo.getCheckListRegistration().setLegalOfficerEmpId(Util.isNotEmptyObject(regLegalEmployeeDetailsPojoList)?Util.isNotEmptyObject(regLegalEmployeeDetailsPojoList.get(0))?regLegalEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l);
					customerBookingFormInfo.getCheckListRegistration().setAccountsExecutiveEmpid(Util.isNotEmptyObject(regExecEmployeeDetailsPojoList)?Util.isNotEmptyObject(regExecEmployeeDetailsPojoList.get(0))?regExecEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l);
					customerBookingFormInfo.getCheckListRegistration().setAuthorizedSignatureId(Util.isNotEmptyObject(regAuthorizedEmployeeDetailsPojoList)?Util.isNotEmptyObject(regAuthorizedEmployeeDetailsPojoList.get(0))?regAuthorizedEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l);
					customerBookingFormInfo.getCheckListRegistration().setCustomerId(customerId);
					customerBookingFormInfo.getCheckListRegistration().setFlatBookingId(flatBookId);
					logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving registration checklist details **");
					bookingFormServiceDaoImpl.saveCheckListRegistration(bookingFormMapper.CheckListRegistrationInfoToCheckListRegistrationPojo(customerBookingFormInfo.getCheckListRegistration()));
					bookingFormSavedStatus.setStatus(HttpStatus.success.getDescription());
				} /*
					 * else { errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					 * errorMsgs.add(bookingFormSavedStatus.getCustName());
					 * errorMsgs.add(" Checklist Registration details are empty ");
					 * logger.error("  Checklist Registration details are empty "); throw new
					 * InSufficeientInputException(errorMsgs); }
					 */
				}
			  else { 
			  errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			  errorMsgs.add(bookingFormSavedStatus.getCustName());
			  errorMsgs.add(" Checklist Registration details are empty ");
			  logger.error("  Checklist Registration details are empty "); 
			  throw new InSufficeientInputException(errorMsgs); 
			  }
			 
		} catch (Exception e) {
			// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
			errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
			errorMsgs.add(bookingFormSavedStatus.getCustName());
			errorMsgs.add("Error occured while saving registration checklist details - ");
			logger.error("Error occured while saving registration checklist details -"+e.toString());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	/**
	 * 
	 * @param checklistVerificationPojos
	 * @param customerBookingFormsInfo
	 * @param bookingFormRequest
	 * @param bookingFormSavedStatus
	 * @param flatBookingId
	 * @param customerId
	 * @param bookingFormMapper2
	 * @param errorMsgs
	 * @throws InSufficeientInputException 
	 */
	private void updateCheckListRegistration(List<CustChecklistVerificationPojo> checklistVerificationPojos,CustomerBookingFormInfo customerBookingFormInfo, BookingFormRequest bookingFormRequest,
			BookingFormSavedStatus bookingFormSavedStatus, Long flatBookId, Long customerId,BookingFormMapper mapper, List<String> errorMsgs) throws InSufficeientInputException {
		List<CustomerCheckListVerificationInfo> customerCheckListVerificationInfo = new ArrayList<CustomerCheckListVerificationInfo>();
		logger.info("**** The control is inside the updateCheckListSalesHead in BookingFormServiceImpl *****");
		try {
			/* update customer registration */
			customerCheckListVerificationInfo = customerBookingFormInfo.getCheckListRegistration().getCustomerCheckListVerification();
			if(Util.isNotEmptyObject(customerCheckListVerificationInfo)) {
				for (CustomerCheckListVerificationInfo objCustomerCheckListVerificationInfo : customerCheckListVerificationInfo) {
					boolean flag = true;
					/* if checklist name is empty we didn't store checklist into the database. */
					if(Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName()) && Util.isNotEmptyObject(objCustomerCheckListVerificationInfo.getCheckListStatus())) {
						Long departmentId = Department.MANAGEMENT.getId();
						bookingFormRequest.setDeptId(departmentId);
						bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
						Long ckId = mapper.getCKId(objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
						if (ckId == null || ckId.equals(0L) || ckId == 0) {
							errorMsgs.add("Error occured while saving customer Sales Head checklist details -- Invalid Checklist name -- "+ objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							throw new InSufficeientInputException(errorMsgs);
						}
						bookingFormRequest.setCheckListId(ckId);
						List<ChecklistDepartmentMappingPojo> checklistDepartmentMappingPojos = bookingFormServiceDaoImpl.getCheckListDepartmentMapping(bookingFormRequest);
						
						if(Util.isNotEmptyObject(checklistDepartmentMappingPojos)) {
							for(CustChecklistVerificationPojo  checklistPojo :checklistVerificationPojos){
								/* if  checklist is already present check status if it is modified update details */
								if(checklistPojo.getChecklistDeptMapId().equals(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId())){
									flag = false;
									/* checking verification status */
									Long custChecklistVerifiId = mapper.getStatusId(objCustomerCheckListVerificationInfo.getCheckListStatus());
									if(Util.isNotEmptyObject(checklistPojo.getIs_verified())?checklistPojo.getIs_verified().equals(custChecklistVerifiId):false){
										/* If both status are same no need to update anything   */
										break;
								    }
									/* update status in checklist verification table */
									else {
									 BookingFormRequest request = new BookingFormRequest();
									 request.setStatusId(custChecklistVerifiId);
									 request.setCheckListVerfiId(checklistPojo.getCustCheckVeriId());
									/* based on  checklist verification Id */
									int result = bookingFormServiceDaoImpl.updateCustChecklistVerification(request);
									logger.info("*** The following custcheckverifyid is updated ****"+result);
								    break;
									}
								}
							}
							if(flag) {
								objCustomerCheckListVerificationInfo.setCustId(customerId);
								objCustomerCheckListVerificationInfo.setDepartmentId(departmentId);
								objCustomerCheckListVerificationInfo.setChecklistDeptMapId(checklistDepartmentMappingPojos.get(0).getChecklistDeptMapId());
								objCustomerCheckListVerificationInfo.setFlatBookId(flatBookId);
								logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer checklist verification details ***");
								bookingFormServiceDaoImpl.saveCustChecklistVerification(mapper.CustomerCheckListVerificationInfoToCustomerCheckListVerificationPojo(objCustomerCheckListVerificationInfo));	
							}
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add("Invalid Checklist(Given checklist is not present in CheckList Registration) "+objCustomerCheckListVerificationInfo.getCheckListInfo().getCheckListName());
							logger.error(" Given checklist is not present in CheckList Registration -");
							throw new InSufficeientInputException(errorMsgs);
						}
					  }
					}
				/* updating CHECKLIST_REGISTRATION table */
				logger.info("***** inside getBookingFormDetails() in BookingFormRestService, getting checkListSalesInfo *****");
				List<CheckListRegistrationPojo> checkListRegistrationPojos = bookingFormServiceDaoImpl.getCheckListRegistration(bookingFormRequest);
				/*  Insert if checkListSalesHeadPojos is empty */
				if(Util.isEmptyObject(checkListRegistrationPojos)) {
					if(Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getLegalOfficerEmpName()) && Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getAccountsExecutiveEmpName()) && Util.isNotEmptyObject(customerBookingFormInfo.getCheckListRegistration().getAuthorizedSignatureName())) {
						List<EmployeePojo> regLegalEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getLegalOfficerEmpName(),null,new Department[] {Department.LEGAL});
						List<EmployeePojo> regExecEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAccountsExecutiveEmpName(), null, new Department[] {Department.ACCOUNTS});
						List<EmployeePojo> regAuthorizedEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAuthorizedSignatureName(),null,new Department[]{Department.MANAGEMENT});
						customerBookingFormInfo.getCheckListRegistration().setLegalOfficerEmpId(Util.isNotEmptyObject(regLegalEmployeeDetailsPojoList)?Util.isNotEmptyObject(regLegalEmployeeDetailsPojoList.get(0))?regLegalEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListRegistration().setAccountsExecutiveEmpid(Util.isNotEmptyObject(regExecEmployeeDetailsPojoList)?Util.isNotEmptyObject(regExecEmployeeDetailsPojoList.get(0))?regExecEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListRegistration().setAuthorizedSignatureId(Util.isNotEmptyObject(regAuthorizedEmployeeDetailsPojoList)?Util.isNotEmptyObject(regAuthorizedEmployeeDetailsPojoList.get(0))?regAuthorizedEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l);
						customerBookingFormInfo.getCheckListRegistration().setCustomerId(customerId);
						customerBookingFormInfo.getCheckListRegistration().setFlatBookingId(flatBookId);
						logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving registration checklist details **");
						bookingFormServiceDaoImpl.saveCheckListRegistration(bookingFormMapper.CheckListRegistrationInfoToCheckListRegistrationPojo(customerBookingFormInfo.getCheckListRegistration()));
						bookingFormSavedStatus.setStatus(HttpStatus.success.getDescription());
						}else {
							errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
							errorMsgs.add(bookingFormSavedStatus.getCustName());
							errorMsgs.add(" Checklist Registration details are empty ");
							logger.error("  Checklist Registration details are empty ");
							throw new InSufficeientInputException(errorMsgs);
						}
					}/*  updating if already exists. */		  
				else {
					CheckListRegistrationInfo checkListRegistrationInfo = customerBookingFormInfo.getCheckListRegistration();
					StringBuilder query = null;
				       if(Util.isNotEmptyObject(checkListRegistrationInfo.getAgValue()) 
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getSdValue())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getSdNumber())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getLegalComments())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getAccountsComments())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getLegalOfficerEmpName())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getLegalOfficerDate())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getAccountsExecutiveEmpName())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getAccountsExecutiveDate())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getAuthorizedSignatureName())
				    		   || Util.isNotEmptyObject(checkListRegistrationInfo.getAuthorizedDate())
				    		   ){
				    	   query = new StringBuilder(" UPDATE CHECKLIST_REGISTRATION SET ");
				    	if(Util.isNotEmptyObject(checkListRegistrationInfo.getAgValue())) {
				    		query.append("AG_VALUE = '"+checkListRegistrationInfo.getAgValue()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getSdValue())){
				    		query.append("SD_VALUE = '"+checkListRegistrationInfo.getSdValue()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getSdNumber())){
				    		query.append("SD_NUMBER = "+checkListRegistrationInfo.getSdNumber()+" ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getLegalComments())){
				    		query.append("COMMENTS_LEGAL = '"+checkListRegistrationInfo.getLegalComments()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getAccountsComments())){
				    		query.append("COMMENTS_ACCOUNTS = '"+checkListRegistrationInfo.getAccountsComments()+"' ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getLegalOfficerEmpName())){
				    		List<EmployeePojo> regLegalEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getLegalOfficerEmpName(),null,new Department[] {Department.LEGAL});
							Long regLegalEmployeeId = Util.isNotEmptyObject(regLegalEmployeeDetailsPojoList)?Util.isNotEmptyObject(regLegalEmployeeDetailsPojoList.get(0).getEmployeeId())?regLegalEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l;
				    		query.append("LEGAL_OFFICIER_EMPLOYEE_ID = "+regLegalEmployeeId+" ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getLegalOfficerDate())){
				    		query.append("LEGAL_OFFICIER_APROVED_DATE = TO_TIMESTAMP('"+checkListRegistrationInfo.getLegalOfficerDate()+"', 'YYYY-MM-DD HH24:MI:SS.FF') ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getAccountsExecutiveEmpName())){
				    		List<EmployeePojo> regExecEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAccountsExecutiveEmpName(), null, new Department[] {Department.ACCOUNTS});
							Long regExecEmployeeId = Util.isNotEmptyObject(regExecEmployeeDetailsPojoList)?Util.isNotEmptyObject(regExecEmployeeDetailsPojoList.get(0).getEmployeeId())?regExecEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l;
				    		query.append("ACCOUNTS_EMPLOYEEID = "+regExecEmployeeId+" ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getAccountsExecutiveDate())){
				    		query.append("ACCOUNT_APROVED_DATE = TO_TIMESTAMP('"+checkListRegistrationInfo.getAccountsExecutiveDate()+"', 'YYYY-MM-DD HH24:MI:SS.FF') ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getAuthorizedSignatureName())){
				    		List<EmployeePojo> regAuthorizedEmployeeDetailsPojoList = bookingFormServiceDaoImpl.getEmployeeDetails(customerBookingFormInfo.getCheckListRegistration().getAuthorizedSignatureName(),null,new Department[]{Department.MANAGEMENT});
							Long regAuthorizedEmployeeId = Util.isNotEmptyObject(regAuthorizedEmployeeDetailsPojoList)?Util.isNotEmptyObject(regAuthorizedEmployeeDetailsPojoList.get(0).getEmployeeId())?regAuthorizedEmployeeDetailsPojoList.get(0).getEmployeeId():0l:0l;
				    		  query.append("APROVER_EMPLOYEE_ID = "+regAuthorizedEmployeeId+" ,");
				    	}if(Util.isNotEmptyObject(checkListRegistrationInfo.getAuthorizedDate())){
				    		query.append("APROVER_APROVED_DATE = TO_TIMESTAMP('"+checkListRegistrationInfo.getAuthorizedDate()+"', 'YYYY-MM-DD HH24:MI:SS.FF') ");
				    	}
				    	if(query.toString().endsWith(",")) {
				    	  query = new StringBuilder(StringUtils.chop(query.toString()));
				    	}
				    	//TO_TIMESTAMP('2019-11-19 14:12:46.738', 'YYYY-MM-DD HH24:MI:SS.FF')
				    	query.append(" WHERE CHECKLIST_REGISTRATION_ID = "+(Util.isNotEmptyObject(checkListRegistrationPojos)?Util.isNotEmptyObject(checkListRegistrationPojos.get(0).getChecklistRegistrationID())?checkListRegistrationPojos.get(0).getChecklistRegistrationID():0l:0l));
				    	int result = bookingFormServiceDaoImpl.updateCheckList(query.toString());
				       logger.info("**** The Checklist Registration is updated sucessfully ****"+result);
				      }
				  }
				}else {
					errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
					errorMsgs.add(bookingFormSavedStatus.getCustName());
					errorMsgs.add(" Checklist Registration details are empty ");
					logger.error("  Checklist Registration details are empty ");
					throw new InSufficeientInputException(errorMsgs);
				}
			} catch (Exception e) {
				// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
				errorMsgs.add(bookingFormSavedStatus.getLeadId() + "");
				errorMsgs.add(bookingFormSavedStatus.getCustName());
				errorMsgs.add("Error occured while saving registration checklist details - ");
				logger.error("Error occured while saving registration checklist details -"+e.toString());
				throw new InSufficeientInputException(errorMsgs);
			}
	}

   private List<CustChecklistVerificationPojo> insertOrUpdateChecklistUtil(Long flatBookingId,Long custId,Long deptId){
		logger.info("*** The control is inside the insertOrUpdateChecklistUtil in BookingFormRestServiceImpl ***"+flatBookingId+"****"+custId);	
		BookingFormRequest bookingFormRequest= new BookingFormRequest();
		bookingFormRequest.setRequestUrl("insertOrUpdateChecklistUtil");
		bookingFormRequest.setCustomerId(custId);
		bookingFormRequest.setDeptId(deptId);
		bookingFormRequest.setFlatBookingId(flatBookingId);
		List<CustChecklistVerificationPojo> checklistVerificationPojos  = bookingFormServiceDaoImpl.getCustChecklistVerification(bookingFormRequest);
		logger.info("*** The checklistVerificationPojos is ***"+checklistVerificationPojos);
		return checklistVerificationPojos;
	}		
   private List<CoApplicantCheckListVerificationPojo> insertOrUpdateCoAppChecklistUtil(BookingFormRequest bookingFormRequest, Co_ApplicantPojo co_ApplicantPojo){
		logger.info("*** The control is inside the insertOrUpdateChecklistUtil in BookingFormRestServiceImpl ***"+bookingFormRequest.getFlatBookingId()+"****"+bookingFormRequest.getCustomerId());	
		bookingFormRequest.setCoApplicantId(co_ApplicantPojo.getCoApplicantId());
		bookingFormRequest.setRequestUrl("insertOrUpdateCoAppChecklistUtil");
		bookingFormRequest.setDeptId(Department.LEGAL.getId());
		List<CoApplicantCheckListVerificationPojo> coApplicantCheckListVerificationPojo  = bookingFormServiceDaoImpl.getApplicantChecklistVerification(bookingFormRequest);
		logger.info("*** The coApplicantCheckListVerificationPojo is ***"+coApplicantCheckListVerificationPojo);
		return coApplicantCheckListVerificationPojo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void updateApplicantOrCoApplicantData(BookingFormRequest request) throws InSufficeientInputException, IllegalAccessException, SQLInsertionException, InvalidStatusException, Exception {
		logger.info("*** The control is inside the updateApplicantOrCoApplicantData in BookingFormRestServiceImpl **** customerBookingFormInfo"+request);	
		for(CustomerBookingFormInfo customerBookingFormInfo :  request.getCustomerBookingFormsInfos()) {
		customerBookingFormInfo.setEmployeeName(Util.isNotEmptyObject(request.getEmployeeName())?request.getEmployeeName():"N/A");
		boolean flag = false;
		//boolean isDemandNoteServiceExec=false;
		BookingFormRequest bookingFormRequestCopy = new BookingFormRequest();
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo = null;
		CustomerInfo customerInfo = customerBookingFormInfo.getCustomerInfo();
		List<AddressInfo> addressInfos = customerBookingFormInfo.getAddressInfos();
		List<CustomerKYCDocumentSubmitedInfo> kycSubmittedInfo = customerBookingFormInfo.getCustomerKYCSubmitedInfo();
		CustomerOtherDetailsInfo othereDetails = customerBookingFormInfo.getCustomerOtherDetailsInfo();
		ProfessionalInfo proInfo = customerBookingFormInfo.getProfessionalInfo();
		CustomerApplicationInfo custAppInfo = customerBookingFormInfo.getCustomerApplicationInfo();
		List<CustomerSchemeInfo> customerSchemeInfo = customerBookingFormInfo.getCustomerSchemeInfos();
		FlatBookingInfo flatBookingInfo = customerBookingFormInfo.getFlatBookingInfo();
		BookingFormRequest bookingFormReq = prepareFlatRelatedObject(flatBookingInfo);
		CustomerBookingInfo customerBookingInfo = customerBookingFormInfo.getCustomerBookingInfo();
		BookingFormRequest custBookingFormReq = prepareCustomerRelatedObject(customerInfo,customerBookingInfo);

		/* set Employee_Id */
		bookingFormReq.setEmployeeName(Util.isNotEmptyObject(customerBookingFormInfo.getEmployeeName())? customerBookingFormInfo.getEmployeeName(): "N/A");
		List<EmployeePojo> employeePojos = bookingFormServiceDaoImpl.getEmployeeDetails(bookingFormReq.getEmployeeName(), null);
		customerBookingFormInfo.setEmployeeId(Util.isNotEmptyObject(employeePojos)? Util.isNotEmptyObject(employeePojos.get(0)) ? employeePojos.get(0).getEmployeeId() : 0l: 0l);

		/* Update CustomerDetails */
		CustomerPropertyDetailsPojo flatData = checkIsFlatExistOrNot(flatBookingInfo, customerInfo, custAppInfo,bookingFormReq,customerBookingInfo);
		
		List<CustomerPropertyDetailsPojo> flatMasterDetails = bookingFormServiceDaoImpl.getFlatFloorBlockSiteByNames(bookingFormReq);
		if (Util.isNotEmptyObject(flatMasterDetails) && Util.isNotEmptyObject(flatMasterDetails.get(0)) && Util.isNotEmptyObject(flatMasterDetails.get(0).getFlatId())) {
			
		} else {
			List<String> errorMsgs = new ArrayList<>();
			errorMsgs.add(custAppInfo.getLeadId() + "");
			errorMsgs.add(customerInfo.getFirstName() + " " + customerInfo.getLastName());
			errorMsgs.add("Error occured while getting Flat Related details ,Check Flat Details or Flat Data Not exist in System");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		Long flatBookingId = flatData.getFlatBookingId();
		Long custBookInfoId = flatData.getCustBookingId();
		Long custProId = flatData.getProId();
		Long customerId = flatData.getCustomerId();
		Long flatId = flatData.getFlatId();
		Long siteId = flatData.getSiteId();String siteName = flatData.getSiteName();
		Long blockId = flatData.getBlockId();
		String flatNo = flatData.getFlatNo();
		Long bookingStatus = flatData.getBookingStatus();
		customerInfo.setCustomerId(customerId);
		custBookingFormReq.setCustomerId(customerId);
		flatBookingInfo.setCustomerId(customerId);

		customerPropertyDetailsInfo = financialMapper.copyPropertiesFromCustomerPropertyDetailsPojoToCustomerPropertyDetailsInfo(flatData);
		 
		BeanUtils.copyProperties(request,bookingFormRequestCopy);
		bookingFormRequestCopy.setFlatBookingId(flatBookingId);
		bookingFormRequestCopy.setCustomerId(customerId);
		bookingFormRequestCopy.setSiteId(siteId);
		bookingFormRequestCopy.setEmpId(customerBookingFormInfo.getEmployeeId());
		List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequestCopy);
		List<CustomerPojo> customerPojos = bookingFormServiceDaoImpl.getCustomer(custBookingFormReq);
		/* if applicant is changed in middle  */
		for(CustomerPojo pojo : customerPojos) {
			if(!custBookingFormReq.getPancard().equalsIgnoreCase(pojo.getPancard())) {
				flag = true;
				List<CustomerPojo> listCustomersByPanCardorPassport  = bookingFormServiceDaoImpl
					.getCustomerDetailsByPanCardorPassport(custBookingFormReq.getPancard(),null,null);
				/* if customer is not present */
				if(Util.isEmptyObject(listCustomersByPanCardorPassport)) {
					CustomerPojo customerPojo = bookingFormMapper.CustomerInfoToCustomerPojo(customerInfo);
					/* putting status as it is as previous */
					customerPojo.setStatusId(bookingStatus);
					customerId = bookingFormServiceDaoImpl.saveCustomerDetails(customerPojo);
					customerInfo.setCustomerId(customerId);
					flatBookingInfo.setCustomerId(customerId);
					flatData.setCustomerId(customerId);
				}else if(Util.isNotEmptyObject(listCustomersByPanCardorPassport)){
					if(Util.isNotEmptyObject(listCustomersByPanCardorPassport.get(0)) && Util.isNotEmptyObject(listCustomersByPanCardorPassport.get(0).getCustomerId())){
						customerId = listCustomersByPanCardorPassport.get(0).getCustomerId();
						customerInfo.setCustomerId(customerId);
						flatBookingInfo.setCustomerId(customerId);
						flatData.setCustomerId(customerId);
						
						/* Updating Old customer status to Active if it is in cancelled State. Used this code save Booking form details */
						Long statusId = listCustomersByPanCardorPassport.get(0).getStatusId();
						bookingFormRequestCopy = new BookingFormRequest(); 
						bookingFormRequestCopy.setCustomerId(customerId);
						bookingFormRequestCopy.setFlatId(flatId);
						//bvr
						bookingFormRequestCopy.setPortNumber(request.getPortNumber());
						//ACP
						if(statusId!=null && (!statusId.equals(Status.ACTIVE.getStatus()) && !statusId.equals(Status.PENDING.getStatus()))) {
							//if(checkCustomerAssociatedFlats(bookingFormRequestCopy)) {
							if(true) {
								bookingFormRequestCopy.setStatusId(bookingStatus);
								List<Long> listOfBookingStatus = Arrays.asList(Status.CANCEL.getStatus(),Status.PENDING.getStatus(),Status.SWAP.getStatus(),
										Status.AVAILABLE.getStatus(),Status.BLOCKED.getStatus(),Status.NOT_OPEN.getStatus(),Status.PRICE_UPDATE.getStatus(),
										Status.LEGAL_CASE.getStatus(),Status.PMAY_SCHEME_ELIGIBLE.getStatus(),Status.RETAINED.getStatus()
										,Status.CANCELLED_NOT_REFUNDED.getStatus(),Status.ASSIGNMENT.getStatus());
								bookingFormServiceDaoImpl.updateCustomer(bookingFormRequestCopy,listOfBookingStatus);
							}
						}
					}
				}
			}
		}
		
		BeanUtils.copyProperties(request,bookingFormRequestCopy);
		bookingFormRequestCopy.setFlatBookingId(flatBookingId);
		bookingFormRequestCopy.setCustomerId(customerId);
		bookingFormRequestCopy.setSiteId(siteId);
		bookingFormRequestCopy.setEmpId(customerBookingFormInfo.getEmployeeId());
		/* update registration date in flatbooking */
		flatBookingInfo.setFlatBookingId(flatBookingId);
	    if(Util.isNotEmptyObject(flatBookingInfo.getRegistrationDate()) || Util.isNotEmptyObject(flatBookingInfo.getAgreementDate())
	       || Util.isNotEmptyObject(flatBookingInfo.getHandingOverDate()) || flag) {
		   updateFlatBooking(flatBookingInfo);
	    }
	    
	    /* updating EoiApplicable and EoiSequence Number */
	 	customerBookingFormInfo.getFlatBookingInfo().setFlatId(flatId);
	 	bookingFormServiceDaoImpl.updateFlatDetails(customerBookingFormInfo.getFlatBookingInfo(),Status.ACTIVE.getStatus(),true);
	 		
		/*not updating flat cost for financial module and updating customer scheme 
		 * Note : not updating flat cost
		 * */
		updateFinancialModuleFlatCostAndScheme(flatBookingInfo,customerSchemeInfo,customerBookingFormInfo,"updateBookingDetails");
		
		//if(flatBookingInfo.getMilestoneDueDays()!=null) {
			//update milestone due days and sale deed values in flat booking
			updateFlatBookingMileStoneDetails(flatBookingInfo);
		//}
			
		/* updating Customer Information */
		updateCustInfo(customerInfo);

		/* updating Customer Booking Info Details */
		/*  updating Authorization Type */
		customerBookingFormInfo = updateTdsAutherizationType(customerBookingFormInfo);
		updateCustBookInfo(customerBookingFormInfo, flatData);

		/* updating Customer Contact Information */
		updateContactInfo(customerBookingInfo, flatBookingId, flatData.getCustomerId(), null);

		/* updating Customer Professional Information */
		proInfo.setCustProffisionalId(flatData.getProId());
		updateProfessionalInfo(proInfo);

		/* updating Customer Address details and Address Mapping details */
		updateAddressInfo(addressInfos, custBookInfoId);

		/*
		 * updating Customer Other details and POAHolder Details(if customer is
		 * foreigner or nri)
		 */
		updateOtherDetailsInfo(othereDetails, flatBookingId, bookingFormReq);

		/* updating Customer KYCSubmitted Information details */
		updateKYCSubmittedInfo(kycSubmittedInfo, flatBookingId, custBookInfoId);
		
		/* Malladi Changes */
		/* Updating CUSTOMER_APPLICATION details */
		if(Util.isNotEmptyObject(custAppInfo)) {
			custAppInfo.setFlatBookId(flatBookingId);
			updateCustomerApplicationDetails(custAppInfo);
		}

		/* Update or Save Co-Applicant Details */
		List<String> errorMsgs = new ArrayList<>();
		if (validateApplicantCoapplicantAadharDetails(customerBookingFormInfo)) {
			errorMsgs.add("Adhar Card Numbers and PanCard Numbers Must be Unique");
			throw new InSufficeientInputException(errorMsgs);
		}
		if (Util.isEmptyObject(customerBookingFormInfo.getCoApplicentDetails())) {
			errorMsgs.add("Co_Applicant Details are Empty");
			throw new InSufficeientInputException(errorMsgs);
		}
		List<CoApplicentDetailsInfo> coApplicentDetails = customerBookingFormInfo.getCoApplicentDetails();
		updateOrSaveCoApplicantDetails(coApplicentDetails, custBookInfoId, custProId, customerId,flatData.getBookingStatus());
		
		/* if status is given true then approve booking form */
		BookingFormRequest bookingFormRequest =new BookingFormRequest();
		/* For Inserting Booking Changed Details Changed Status */
		bookingFormRequest.setStatusId(bookingStatus);
		if((Util.isNotEmptyObject(customerBookingFormInfo.getCustomerAppBookingApproval())? customerBookingFormInfo.getCustomerAppBookingApproval() :false) && (Util.isNotEmptyObject(flatData.getBookingStatus()) && flatData.getBookingStatus().equals(Status.PENDING.getStatus()))) {
			//BookingFormRequest bookingFormRequest =new BookingFormRequest();
			bookingFormRequest.setCustomerId(customerId);
			bookingFormRequest.setFlatBookingId(flatBookingId);
			bookingFormRequest.setActionStr("approve");
			bookingFormRequest.setStatusId(Status.ACTIVE.getStatus());
			bookingFormRequest.setRequestUrl("actionBookingDetails");
			bookingFormRequest.setSiteId(siteId);
			bookingFormRequest.setBlockId(blockId);
			bookingFormRequest.setGenerateDemandNoteForBooking("false");//false means don't generate demand note for booking
			putActionBookingForm(bookingFormRequest);
		}
		try {
			sendPushNotifications(customerBookingFormInfo,customerPropertyDetailsInfo,flatBookPojo,flatData);	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			if(Util.isNotEmptyObject(flatBookPojo)) {
				if(Status.ACTIVE.getStatus().equals(bookingFormRequest.getStatusId()) || Status.ACTIVE.getStatus().equals(flatBookPojo.get(0).getStatusId())) {
					flatBookingInfo.setOldBookingName(flatBookPojo.get(0).getSalesforceOldBookingId());
					if(Util.isNotEmptyObject(flatBookingInfo.getOldBookingName())) {
						List<FlatBookingPojo> flatBookingPojoList = bookingFormServiceDaoImpl.getOldFlatBookingDetails(flatBookingInfo, ServiceRequestEnum.SALESFORCE_NEW_BOOKING);
						if(Util.isNotEmptyObject(flatBookingPojoList) && Util.isNotEmptyObject(flatBookingPojoList.get(0))) {
							FlatBookingPojo oldFlatBookingPojo = flatBookingPojoList.get(0);
							if(Util.isNotEmptyObject(flatBookingId) && Util.isNotEmptyObject(customerId) && Util.isNotEmptyObject(custBookInfoId)
								&& Util.isNotEmptyObject(oldFlatBookingPojo.getFlatBookingId()) && Util.isNotEmptyObject(oldFlatBookingPojo.getCustomerId()) 
								&& Util.isNotEmptyObject(oldFlatBookingPojo.getCustBookInfoId()) && Util.isNotEmptyObject(siteId)
								&& siteId.equals(oldFlatBookingPojo.getSiteId())) {
								
								BookingFormRequest oldBookingRequest = new BookingFormRequest();
								oldBookingRequest.setFlatBookingId(oldFlatBookingPojo.getFlatBookingId());
								oldBookingRequest.setCustomerId(oldFlatBookingPojo.getCustomerId());
								oldBookingRequest.setCustBookInfoId(oldFlatBookingPojo.getCustBookInfoId());
								oldBookingRequest.setFlatId(oldFlatBookingPojo.getFlatId());
								oldBookingRequest.setFlatNo(oldFlatBookingPojo.getFlatNo());
								oldBookingRequest.setBlockId(oldFlatBookingPojo.getBlockId());
								oldBookingRequest.setSiteId(oldFlatBookingPojo.getSiteId());
								oldBookingRequest.setStatusId(oldFlatBookingPojo.getStatusId());
								
								BookingFormRequest newBookingRequest = new BookingFormRequest();
								newBookingRequest.setFlatBookingId(flatBookingId);
								newBookingRequest.setCustomerId(customerId);
								newBookingRequest.setCustBookInfoId(custBookInfoId);
								newBookingRequest.setFlatId(flatId);
								newBookingRequest.setFlatNo(flatNo);
								newBookingRequest.setBlockId(blockId);
								newBookingRequest.setSiteId(siteId);
								newBookingRequest.setStatusId(bookingFormRequest.getStatusId());
								
								if(customerId.equals(oldFlatBookingPojo.getCustomerId()) && siteId.equals(oldFlatBookingPojo.getSiteId())) {
									bookingFormRequestCopy.setOldBookingRequest(oldBookingRequest);
									bookingFormRequestCopy.setNewBookingRequest(newBookingRequest);
								}
							}
						}
					}
				}
			}
				if (Status.ACTIVE.getStatus().equals(bookingStatus)||Status.PENDING.getStatus().equals(bookingStatus)) {
					if(Util.isNotEmptyObject(customerSchemeInfo) && Util.isNotEmptyObject(customerSchemeInfo.get(0)) && Util.isNotEmptyObject(customerSchemeInfo.get(0).getSchemeName())) {
						processGenerateDemandNoteDate(customerBookingFormInfo, customerPropertyDetailsInfo, flatBookPojo,flatData, bookingFormRequestCopy);
					}
				}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		/* Inserting Statistics in BOOKING_STATUS_CHANGED_DTLS Table */
		bookingFormRequest.setEmpId(customerBookingFormInfo.getEmployeeId());
		bookingFormRequest.setActualStatusId(bookingStatus);
		bookingFormRequest.setFlatBookingId(flatBookingId);
		bookingFormRequest.setRemarks(Status.UPDATE.getDescription());
	    saveBookingChangedStatus(bookingFormRequest); 
	 }
	}
	
	private void updateCustomerApplicationDetails(CustomerApplicationInfo custAppInfo) {
		logger.info("*** The control is inside the updateCustomerApplicationDetails in BookingFormServiceImpl **** custAppInfo"+custAppInfo);
		CustomerApplicationPojo custAppPojo = bookingFormMapper.CustomerApplicationInfoToCustomerApplicationPojo(custAppInfo);
		bookingFormServiceDaoImpl.updateCustomerApplicationDetails(custAppPojo);
	}

	private void processGenerateDemandNoteDate(CustomerBookingFormInfo customerBookingFormInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo, 
			List<FlatBookingPojo> flatBookPojo, CustomerPropertyDetailsPojo flatData, BookingFormRequest request) throws Exception {
		boolean isDemandNoteServiceExec = false;
		//if reg date value and approval value got, then generate first disbursement
		//if customer not approved, but reg date came then not to generate demand note, as customer is in pending status
		if(Util.isNotEmptyObject(flatBookPojo)) {
			//registration date if not empty and (this is booking approval or already booking in active state), then generate the final demand note
			if(customerBookingFormInfo!=null && customerBookingFormInfo.getFlatBookingInfo().getRegistrationDate()!=null && 
					(customerBookingFormInfo.getCustomerAppBookingApproval() || Status.ACTIVE.getStatus().equals(flatBookPojo.get(0).getStatusId())) ) {
					try {
						generateDemandNote(customerBookingFormInfo,customerPropertyDetailsInfo,flatData,flatBookPojo,request,"Final_Demand_Note");
					} catch (Exception ex) {
						ex.printStackTrace();
					}			
					isDemandNoteServiceExec = true;
				}
			}
			if(customerBookingFormInfo!=null && customerBookingFormInfo.getCustomerAppBookingApproval() && !isDemandNoteServiceExec ||
					(request.getRequestUrl()!=null && request.getRequestUrl().equals("actionBookingDetails") && request.getActionStr()!=null && request.getActionStr().equals("approve")) ) {// && "a".equals("Hi"
				try {
					generateDemandNote(customerBookingFormInfo,customerPropertyDetailsInfo,flatData,flatBookPojo,request,"First_Disbursement_Demandnote");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
	}

	private void generateDemandNote(CustomerBookingFormInfo customerBookingFormInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo, CustomerPropertyDetailsPojo flatData, List<FlatBookingPojo> flatBookPojo, final BookingFormRequest request
			, String actionUrl) throws Exception {
		//if(customerBookingFormInfo.getCustomerAppBookingApproval() && "a".equals("Hi")) {// && "a".equals("Hi"
			Properties prop = responceCodesUtil.getApplicationProperties();
			Timestamp previousMsMilestoneDate = null;
			Timestamp previousMsDemandDate = null;
			Timestamp previousMsDemandDueDate = null;
			//EmployeeFinancialMapper financialMapper = new EmployeeFinancialMapper(); 
			final EmployeeFinancialServiceInfo employeeFinancialInfo = new EmployeeFinancialServiceInfo();
			employeeFinancialInfo.setCondition("VIEW_DEMAND_NOTES");
			employeeFinancialInfo.setBlockIds(Arrays.asList(flatData.getBlockId()));
			employeeFinancialInfo.setSiteIds(Arrays.asList(flatData.getSiteId()));
			employeeFinancialInfo.setSiteId(flatData.getSiteId());
			List<FinancialProjectMileStonePojo> financialProjectMileStonePojoList = employeeFinancialService.getMileStoneNameAssociatedWithMilestoneClassifidesIdForDemandNote(employeeFinancialInfo);
			FinancialProjectMileStoneInfo mileStoneInfo = null;
			List<FinancialProjectMileStonePojo> financialProjectMileStonePojoList1 = null;
			long finMilestoneClassifidesId = 0l;
			boolean isFinalDemandNoteGenerated = false;
			for (FinancialProjectMileStonePojo financialProjectMileStonePojo : financialProjectMileStonePojoList) {
				System.out.println(financialProjectMileStonePojo.getMsStatusId());
				if(actionUrl.equals("Final_Demand_Note")) {//taking the last milestone details
					//taken last milestone details site wise
					//mileStoneInfo = financialMapper.copyPropertiesFromInfoBeanToPojoBean(financialProjectMileStonePojoList.get(financialProjectMileStonePojoList.size()-1), FinancialProjectMileStoneInfo.class);
					//if(mileStoneInfo.getMasterDemandNotedate()!=null && (mileStoneInfo.getMileStoneNo()!=1 || mileStoneInfo.getMileStoneNo()!=2)) {//master date is empty then don't take the current milestone objec, previous one will be used
						mileStoneInfo = financialMapper.copyPropertiesFromInfoBeanToPojoBean(financialProjectMileStonePojo, FinancialProjectMileStoneInfo.class);
					//}
					finMilestoneClassifidesId = mileStoneInfo.getFinMilestoneClassifidesId();
					if(mileStoneInfo.getMilestoneDate()!=null) {
						previousMsMilestoneDate = mileStoneInfo.getMilestoneDate();
					}
					if(mileStoneInfo.getMasterDemandNotedate()!=null) {
						previousMsDemandDate = mileStoneInfo.getMasterDemandNotedate();
					}
					if(mileStoneInfo.getMasterDemandDueDate()!=null) {
						previousMsDemandDueDate = mileStoneInfo.getMasterDemandDueDate();
					}
					
					if(Util.isEmptyObject(financialProjectMileStonePojoList1)) {
						//load all the generated milestone details for booking
						financialProjectMileStonePojoList1 = employeeFinancialService.isMileStoneInitiatedForThisFlatBookingFormId(mileStoneInfo,customerPropertyDetailsInfo,null);
					}
					//need to generate the final demand note up-to last milestone, so taken last milestone details
					//and no need to iterate next object, we got the last milestone details so break the loop
					//break;
				} else if(actionUrl.equals("First_Disbursement_Demandnote")) {
					if(financialProjectMileStonePojo.getMsStatusId()!= null && financialProjectMileStonePojo.getMsStatusId().equals(Status.MS_COMPLETED.getStatus())) {
						//if(mileStoneInfo.getMasterDemandNotedate()!=null && (mileStoneInfo.getMileStoneNo()!=1 || mileStoneInfo.getMileStoneNo()!=2)) {
							mileStoneInfo = financialMapper.copyPropertiesFromInfoBeanToPojoBean(financialProjectMileStonePojo, FinancialProjectMileStoneInfo.class);
						//}
						finMilestoneClassifidesId = mileStoneInfo.getFinMilestoneClassifidesId();
						if(mileStoneInfo.getMilestoneDate()!=null) {
							previousMsMilestoneDate = mileStoneInfo.getMilestoneDate();
						}
					}
				} else {
					throw new InSufficeientInputException(Arrays.asList("Error occurred while generating demand note.!"));
				}
			}
			
			if(financialProjectMileStonePojoList1!=null) {
				//get the last milestone details
				FinancialProjectMileStonePojo generatedMileStonePojo = employeeFinancialService.checkMilestoneGeneratedForFlat(financialProjectMileStonePojoList1,mileStoneInfo);
				//check last milestone details and generated milestone details is same or not if same, then isFinalDemandNoteGenerated value is true
				if(Util.isNotEmptyObject(generatedMileStonePojo) && actionUrl.equals("Final_Demand_Note")) {
					if(generatedMileStonePojo.getMsStatusId()!=null && generatedMileStonePojo.getMsStatusId().equals(Status.ACTIVE.getStatus())) {
						isFinalDemandNoteGenerated = true;//if the last milestone found as a generated, then no need to generate the final demand note
					}
				 }
			}
		
		//09 aug 2021 is demand note date and due date is registration date
		//if booking after 09 aug 2021 then demand note date is booking date and due date is booking date +30 days
		//if final demand note is not generated then isFinalDemandNoteGenerated 
		String dueDaysStr = prop.getProperty("DUE_DATE_ADD_DAYS");//taking due days from prop file
		int DUE_DAYS = Integer.valueOf(dueDaysStr);
		if (mileStoneInfo != null && !isFinalDemandNoteGenerated) {//
			if (mileStoneInfo.getMilestoneDate() == null) {
				mileStoneInfo.setMilestoneDate(previousMsMilestoneDate==null?new Timestamp(new Date().getTime()):previousMsMilestoneDate);
				//mileStoneInfo.setDemandNoteDate(flatBookPojo.get(0).getBookingDate());
				//mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(flatBookPojo.get(0).getBookingDate()), DUE_DAYS));
			}
			/*if(mileStoneInfo.getMasterDemandNotedate()==null) {
				mileStoneInfo.setMasterDemandNotedate(previousMsDemandDate);
			}
			if(mileStoneInfo.getMasterDemandDueDate()==null) {
				mileStoneInfo.setMasterDemandDueDate(previousMsDemandDueDate);
			}*/
			if (actionUrl.equals("Final_Demand_Note") && customerBookingFormInfo != null) {
				//if the customer is already booked, and doing registration that time generating final demand note
				//final demand note will be generated up-to the last milestone of the site
				String registrationDateStr = prop.getProperty(flatData.getSiteId() + "_REG_DATE");
				
				Date projectregistrationDate = null;
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				if (Util.isNotEmptyObject(registrationDateStr)) {
					projectregistrationDate = dateFormat.parse(registrationDateStr);
					mileStoneInfo.setDemandNoteDate(new Timestamp(projectregistrationDate.getTime()));
					mileStoneInfo.setMileStoneDueDate(customerBookingFormInfo.getFlatBookingInfo().getRegistrationDate());
					int days = TimeUtil.differenceBetweenDays(new Timestamp(projectregistrationDate.getTime()),flatBookPojo.get(0).getBookingDate());
					if (days > 0) {//if booking date is after project registration date, then take booking date as dn date and due date is customer registration date
						mileStoneInfo.setDemandNoteDate(flatBookPojo.get(0).getBookingDate());
						mileStoneInfo.setMileStoneDueDate(customerBookingFormInfo.getFlatBookingInfo().getRegistrationDate());	
					}
				} else {
					mileStoneInfo.setDemandNoteDate(flatBookPojo.get(0).getBookingDate());
					mileStoneInfo.setMileStoneDueDate(customerBookingFormInfo.getFlatBookingInfo().getRegistrationDate());
				}
			} else if(actionUrl.equals("First_Disbursement_Demandnote")) {//if the customer is new, generating first disbrustment format
				int days = TimeUtil.differenceBetweenDays(flatBookPojo.get(0).getBookingDate(), mileStoneInfo.getMasterDemandNotedate());
				if (days > 0) {//if booking date is after proj registration date, then take booking date as dn date and due date is customer registration date
					mileStoneInfo.setDemandNoteDate(mileStoneInfo.getMasterDemandNotedate());
					mileStoneInfo.setMileStoneDueDate(mileStoneInfo.getMasterDemandDueDate());
				} else {
					if(mileStoneInfo.getMileStoneNo()!=null && (mileStoneInfo.getMileStoneNo() == 1)) {
						mileStoneInfo.setDemandNoteDate(flatBookPojo.get(0).getBookingDate());//for first due date is 15 days
						mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(flatBookPojo.get(0).getBookingDate()),15));
					} else { 
						mileStoneInfo.setDemandNoteDate(flatBookPojo.get(0).getBookingDate());
						mileStoneInfo.setMileStoneDueDate(TimeUtil.addDays(TimeUtil.removeTimePartFromTimeStamp(flatBookPojo.get(0).getBookingDate()),DUE_DAYS));
					}
				}
			}
			//setting details to milestone object
			mileStoneInfo.setReGenerateDemandNote(false);
			mileStoneInfo.setInterestSelectionType(Status.With_Out_Interest.getStatus());
			mileStoneInfo.setFinMilestoneClassifidesId(finMilestoneClassifidesId);
			mileStoneInfo.setDemandNoteSelectionType("Send Single/Multiple");
			mileStoneInfo.setInterestCalculationUptoDate(TimeUtil.removeOneDay(new Timestamp(new Date().getTime())));
			mileStoneInfo.setCreatedBy(request.getEmpId());
			mileStoneInfo.setStatusId(Status.ACTIVE.getStatus());
			
			employeeFinancialInfo.setEmpId(request.getEmpId());
			employeeFinancialInfo.setFinancialProjectMileStoneRequests(Arrays.asList(mileStoneInfo));
			employeeFinancialInfo.setCondition("DoNotSendEmail");
			/*
			 * if(condition.equals("")) { employeeFinancialInfo.setActionUrl(condition); }
			 * else { employeeFinancialInfo.setActionUrl(condition); }
			 */
			employeeFinancialInfo.setActionUrl(actionUrl);
			employeeFinancialInfo.setRequestUrl("saveBookingDetails");
			employeeFinancialInfo.setBlockIds(null);
			employeeFinancialInfo.setFlatIds(Arrays.asList(flatData.getFlatId()));
			employeeFinancialInfo.setReGenerateDemandNote(false);
			employeeFinancialInfo.setIsInterestOrWithOutInterest("With Out Interest");
			employeeFinancialInfo.setPortNumber(request.getPortNumber());
			employeeFinancialInfo.setFinMilestoneClassifidesId(finMilestoneClassifidesId);
			employeeFinancialInfo.setDemandNoteSelectionType("Send Single/Multiple");
			employeeFinancialInfo.setDemandNoteDate(mileStoneInfo.getDemandNoteDate());
			
			/*ExecutorService executorService = Executors.newFixedThreadPool(10);
			try {
				executorService.execute(new Runnable() {
					public void run() {
						*/try {
							long count = 0,anyTrnCreated = 0l;
							long schemeDetailsCount = 0l;
							long accountDetailsCount = 0l;
							EmployeeFinancialTransactionServiceInfo transactionServiceInfo = new EmployeeFinancialTransactionServiceInfo();
							transactionServiceInfo.setBookingFormId(flatData.getFlatBookingId());
							transactionServiceInfo.setSiteId(flatData.getSiteId());
							if(Util.isNotEmptyObject(flatData.getFlatSaleOwnerId())) {
								transactionServiceInfo.setFlatSaleOwnerId(Long.valueOf(flatData.getFlatSaleOwnerId()));
								//we should have account details
								accountDetailsCount = employeeFinancialServiceDao.checkDemandNoteAccountDetails(transactionServiceInfo);
							}
							//we should have scheme details of the customer
							schemeDetailsCount = employeeFinancialServiceDao.checkSchemeDetailsInserted(transactionServiceInfo);
							
							//customerPropertyDetailsInfo.getSiteId();
							if(request.getNewBookingRequest()==null) {//if this is fresh request
								//count  = 0;
								transactionServiceInfo.setBookingFormId(flatData.getFlatBookingId());
								if(Util.isNotEmptyObject(transactionServiceInfo.getBookingFormId()) && !actionUrl.equals("Final_Demand_Note")) {
									//final demand note will generate on registation date, and demand note will generate upto the last milestone 
									count = employeeFinancialServiceDao.checkDemandNoteIsGeneratedOrNotForTransaction(transactionServiceInfo);
								}
							} else {//checking demand note generated or not, if not then generate
								transactionServiceInfo.setBookingFormId(request.getNewBookingRequest().getFlatBookingId());
								count  = employeeFinancialServiceDao.checkDemandNoteIsGeneratedOrNotForTransaction(transactionServiceInfo);
							}
							//String s = null;s.trim();
							if (count == 0 && schemeDetailsCount !=0 && accountDetailsCount!=0) {//if demand note not generated then generate the demand note
								anyTrnCreated = employeeFinancialServiceDao.isAnyTransactionCreated(transactionServiceInfo);
								
								employeeFinancialService.generateDemandNoteService(employeeFinancialInfo);
								//only these three types of status old booking transaction inserting to new booking
								List<Long> insertEntryStatus = Arrays.asList(Status.SWAP.getStatus(),Status.PRICE_UPDATE.getStatus(),Status.PMAY_SCHEME_ELIGIBLE.getStatus(),Status.INACTIVE.getStatus());
							    if(request.getOldBookingRequest()!=null && insertEntryStatus.contains(request.getOldBookingRequest().getStatusId())) {
							    	/*transactionServiceInfo = new EmployeeFinancialTransactionServiceInfo();
							    	transactionServiceInfo.setBookingFormId(request.getNewBookingRequest().getFlatBookingId());
							    	//employeeFinancialServiceDao.isAnyTransactionInApproval(transactionServiceInfo,Status.CREATED);*/
							    	
							    	if(anyTrnCreated == 0) {
							    		employeeFinancialService.insertOldBookingTransactionToNewBooking(request);
							    	}
							    }
							}
							} catch (Exception e) {
								e.printStackTrace();
								sendBookingErrorMail(e,employeeFinancialInfo,customerPropertyDetailsInfo);
							}
						/*
					}
				});
				executorService.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
				executorService.shutdown();
				logger.error("Failed to store file in drive, Please contact to Support Team.");
				throw new EmployeeFinancialServiceException("Failed to store file in drive, Please contact to Support Team.");
			}*/
		}
			//String s = null;s.trim();
		//}	
	}

	private void sendBookingErrorMail(Exception exception, EmployeeFinancialServiceInfo employeeFinancialInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsPojo) {
		Properties prop = responceCodesUtil.getApplicationProperties();
		Email email = new Email();
		StringBuilder msg = new StringBuilder(""); 
		String internalEmpMail = prop.getProperty("DEFAULT_PRODUCTION_ERROR_INTERNAL_EMP_EMAIL");//taking due days from prop file
		String errorMsg = prop.getProperty("DEFAULT_BOOKING_ERROR_MSG");
		email.setSubject("Failed to generate demand note for flat...");
		msg.append(exception+"<br>");
		
		if(exception instanceof RefundAmountException) {
			RefundAmountException exx = (RefundAmountException)exception;
			if (Util.isNotEmptyObject(exx.getMessages())) {
				msg.append(exx.getMessages());
			} else if (Util.isNotEmptyObject(exx.getMessage())) {
				msg.append(Arrays.asList(exx.getMessage()));
			}
		} else if(exception instanceof EmployeeFinancialServiceException) {
			EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)exception;
			if (Util.isNotEmptyObject(exx.getMessages())) {
				msg.append(exx.getMessages());
			} else if (Util.isNotEmptyObject(exx.getMessage())) {
				msg.append(Arrays.asList(exx.getMessage()));
			}
		} else if (exception instanceof InSufficeientInputException) {
			InSufficeientInputException e = (InSufficeientInputException) exception;
			if(Util.isNotEmptyObject(e.getMessages())) {
				msg.append(e.getMessages());
			}
		} else if(exception!=null && exception.getMessage()!=null) {
			msg.append(exception.getMessage()+"<br>");
		}
		
		//errorMsg = errorMsg.replace("#error_Msg","<br>"+exception+"<br> Project "+customerPropertyDetailsPojo.getSiteName()+" Flat No "+customerPropertyDetailsPojo.getFlatNo()+", Time "+ TimeUtil.getTimeInSpecificFormat(TimeUtil.removeOneDay(new Timestamp(new Date().getTime())), "dd-MM-yyyy hh-mm-ss"));
		for(StackTraceElement element : exception.getStackTrace()) {
			msg.append(element+"<br>");
		}
		errorMsg = errorMsg.replace("#error_Msg","<br>"+msg+"<br> Project "+customerPropertyDetailsPojo.getSiteId()+" Flat No "+customerPropertyDetailsPojo.getFlatNo()
		+" Flat Book Id "+customerPropertyDetailsPojo.getFlatBookingId()
		+", Time "+ TimeUtil.getTimeInSpecificFormat(new Timestamp(new Date().getTime()), "dd-MM-yyyy hh-mm-ss"));
		email.setEmailBodyText(errorMsg);
		//internalEmpMail = "aniketchavan75077@gmail.com";
		if(Util.isNotEmptyObject(internalEmpMail)) {
			email.setToMails(internalEmpMail.split(","));//cc in internal team, for default banker error
			mailServiceImpl.sendDefaultBankerErrorMail(email);
		}
	}

	private void updateFinancialModuleFlatCostAndScheme(FlatBookingInfo flatBookingInfo, List<CustomerSchemeInfo> customerSchemeInfos,
			CustomerBookingFormInfo customerBookingFormInfo, String requestUrl) throws InSufficeientInputException {
		logger.info("***** Control inside the BookingFormServiceImpl.updateFinancialModuleFlatCostAndScheme() *****");
		//EmployeeFinancialMapper financialMapper = new EmployeeFinancialMapper();
		//SiteInfo siteInfo =	flatBookingInfo.getSiteInfo();
		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		employeeFinancialServiceInfo.setFlatIds(Arrays.asList(flatBookingInfo.getFlatId()));
		employeeFinancialServiceInfo.setBookingFormId(flatBookingInfo.getFlatBookingId());
		employeeFinancialServiceInfo.setCondition("saveBookingDetails");
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
		if(Util.isNotEmptyObject(customerPropertyDetailsPojoList) && Util.isNotEmptyObject(customerPropertyDetailsPojoList.get(0))) {
			//customerSchemeInfo.setSiteId(customerPropertyDetailsPojoList.get(0).getSiteId());
			//siteInfo.setSiteId(customerPropertyDetailsPojoList.get(0).getSiteId());
		}
		
		int result = 0;
		if(Util.isNotEmptyObject(flatBookingInfo.getFlatCost()) && requestUrl!=null && requestUrl.equals("saveBookingDetails")) {
			FinBookingFormAccountSummaryPojo accountSummaryPojo = bookingFormMapper.copyFlatCostInfoPropToFinBookingFormAccountSummaryPojo(flatBookingInfo,customerBookingFormInfo);
			result = bookingFormServiceDaoImpl.updateFinancialBookingFormAccountSummaryCost(accountSummaryPojo);
			if(result == 0) {
				 result = bookingFormServiceDaoImpl.insertFinancialBookingFormAccountSummaryCost(accountSummaryPojo);
			}
		}
		result = 0;
		if(Util.isNotEmptyObject(customerSchemeInfos)) {
			for (CustomerSchemeInfo customerSchemeInfo : customerSchemeInfos) {
				if (Util.isNotEmptyObject(customerSchemeInfo.getSchemeName())) {//if not empty then inactive previous scheme
					FlatBookingSchemeMappingPojo inActivePastCustomerSchemepojo = financialMapper.constructFlatBookingSchemeMappingPojoBookingTime(flatBookingInfo,customerBookingFormInfo);
					result = bookingFormServiceDaoImpl.inActivePastCustomerSchemeDetails(inActivePastCustomerSchemepojo);
				}
			}
			
			for (CustomerSchemeInfo customerSchemeInfo : customerSchemeInfos) {
				if(Util.isEmptyObject(customerSchemeInfo.getSchemeName())) {//if empty don't processed
					continue;
				}
				/*if(Util.isEmptyObject(customerPropertyDetailsPojoList)) {//if empty don't processed
					continue;
				}*/
				customerSchemeInfo.setSiteId(customerPropertyDetailsPojoList.get(0).getSiteId());
			
				List<FinSchemeTaxMappingPojo> flatSchemeDetailsList = bookingFormServiceDaoImpl.getCustomerSchemeDetailsBySchemeName(customerSchemeInfo,flatBookingInfo);
				if(Util.isEmptyObject(flatSchemeDetailsList)) {
					if(requestUrl!=null && requestUrl.equals("updateBookingDetails")) {
						//throw new InSufficeientInputException(Arrays.asList("Scheme not found, Scheme name : "+customerSchemeInfo.getSchemeName()));
					}else {
						//throw new RuntimeException("Scheme not found, Scheme name : "+customerSchemeInfo.getSchemeName());
					}
				}
				
				/*customerSchemeInfo.setCondition("getPastSchemeDetails");
				@SuppressWarnings("unused")
				List<FinSchemeTaxMappingPojo> flatSchemeDetailsList1 = bookingFormServiceDaoImpl.getCustomerSchemeDetailsBySchemeName(customerSchemeInfo,flatBookingInfo);*/
				 
				for (FinSchemeTaxMappingPojo flatBookingSchemeMappingPojo : flatSchemeDetailsList) {
					FlatBookingSchemeMappingPojo pojo = financialMapper.constructFlatBookingSchemeMappingPojoBookingTime(flatBookingInfo,flatBookingSchemeMappingPojo,customerBookingFormInfo);
					//List<FlatBookingSchemeMappingPojo> flatBookingSchemeMappingPojos = employeeFinancialServiceDao.getFlatBookingSchemeMappingDetails(pojo);
					//if (Util.isEmptyObject(flatBookingSchemeMappingPojos)) {
						//result = bookingFormServiceDaoImpl.updateCustomerSchemeDetails(pojo);
						//if (result == 0) {
							result = employeeFinancialServiceDao.insertFlatBookingSchemeMappingDetails(pojo);
						//}
					/*} else {
						 
					}*/
				}
			}
		}//customerSchemeInfo!=null condition
	}
	
	private void updateCustBookInfo(CustomerBookingFormInfo customerBookingFormInfo,
			CustomerPropertyDetailsPojo flatData) {
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setFlatNo(customerBookingFormInfo.getFlatBookingInfo().getFlatInfo().getFlatNo());
		bookingFormRequest.setFloorName(customerBookingFormInfo.getFlatBookingInfo().getFloorInfo().getFloorName());
		bookingFormRequest.setBlockName(customerBookingFormInfo.getFlatBookingInfo().getBlockInfo().getName());
		bookingFormRequest.setSiteName(customerBookingFormInfo.getFlatBookingInfo().getSiteInfo().getName());
		bookingFormRequest.setCustomerBookingFormsInfo(customerBookingFormInfo);
		logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer booking info details ***");
		List<CustomerPropertyDetailsPojo> masterFlatFloorBlockSiteData = bookingFormServiceDaoImpl.getFlatFloorBlockSiteByNames(bookingFormRequest);
		String stateName = bookingFormMapper.getStateName(masterFlatFloorBlockSiteData.get(0).getStateId()).toUpperCase() + "_TERMSANDCONDITIONS_VERSION";
		customerBookingFormInfo.getCustomerBookingInfo().setTermsConditionFileName(new ResponceCodesUtil().getApplicationProperties().getProperty(stateName) + "-TermsAndConditions");
		CustBookInfoPojo custBookInfoPojo = bookingFormMapper.custBookInfoToCustBookInfoPojo(customerBookingFormInfo.getCustomerBookingInfo(), bookingFormServiceDaoImpl,flatData.getFlatBookingId(), flatData.getCustomerId(), flatData.getProId());
		bookingFormServiceDaoImpl.updateCustomerBookInfo(custBookInfoPojo);
	}

	private CustomerPropertyDetailsPojo checkIsFlatExistOrNot(FlatBookingInfo flatBookingInfo, CustomerInfo customerInfo, CustomerApplicationInfo custAppInfo, BookingFormRequest bookingFormRequest, CustomerBookingInfo customerBookingInfo) throws InSufficeientInputException {
		List<String> errorMsgs = new ArrayList<>();		//this method will return data using site,block,floor and flat no
		List<CustomerPropertyDetailsPojo> flatData = bookingFormServiceDaoImpl.getFlatFloorBlockSiteByNames(bookingFormRequest);
		if (Util.isEmptyObject(flatData) || Util.isEmptyObject(flatData.get(0)) || Util.isEmptyObject(flatData.get(0).getFlatId())) {
			//errorMsgs.add(custAppInfo.getLeadId() + "");
			//errorMsgs.add(customerInfo.getFirstName());
			errorMsgs.add("Error occured while getting Flat Related details ,Check Flat Details or Flat Data Not exist in System");
			throw new InSufficeientInputException(errorMsgs);
		}
		/* List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = bookingFormServiceDaoImpl.getBookingFlatData(flatData.get(0));
		if (Util.isEmptyObject(customerPropertyDetailsPojos) || Util.isEmptyObject(customerPropertyDetailsPojos.get(0))) {
			errorMsgs.add(custAppInfo.getLeadId() + " ");
			errorMsgs.add(customerInfo.getFirstName() + " " + customerInfo.getLastName());
			errorMsgs.add("Error occured while getting Flat Related details of customer,Check Flat Details or Flat Data Not exist in System");
			throw new InSufficeientInputException(errorMsgs);
		} */
		
		/* Mandatory fields validations */
		/* Customer name */
		if (Util.isEmptyObject(customerInfo.getFirstName())) {
			errorMsgs.add("Customer's name is required");
			throw new InSufficeientInputException(errorMsgs);
		}
		/* Customer Pan Number */
		if(Util.isEmptyObject(customerInfo.getPancard())) {
			logger.info("*** Given Customer Pan Number is empty inside the checkIsFlatExistOrNot in BookingFormRestServiceImpl ****");
			errorMsgs.add("Customer's pan number is required");
			throw new InSufficeientInputException(errorMsgs);
		}
		/* Customer Mobile Number */
	    if (Util.isEmptyObject(customerBookingInfo.getPhoneNo())) {
			errorMsgs.add("Customer's mobile number is required");
			throw new InSufficeientInputException(errorMsgs);
		}
		/* Customer Email */
		if (Util.isEmptyObject(customerBookingInfo.getEmail())) {
			errorMsgs.add("Customer's email is required");
			throw new InSufficeientInputException(errorMsgs);
		}
		
		/* Gettting Flat Details By Salesforce Booking Id and Flat Id */
		CustomerPropertyDetailsPojo customerPropertyDetailsPojo = new CustomerPropertyDetailsPojo();
		if(Util.isNotEmptyObject(flatBookingInfo) && Util.isNotEmptyObject(flatBookingInfo.getBookingId())) {
			flatBookingInfo.setFlatId(flatData.get(0).getFlatId());
			List<FlatBookingPojo> flatBookingPojoList = bookingFormServiceDaoImpl.getOldFlatBookingDetails(flatBookingInfo, ServiceRequestEnum.SALESFORCE_UPDATE_BOOKING);
			if(Util.isNotEmptyObject(flatBookingPojoList) && Util.isNotEmptyObject(flatBookingPojoList.get(0))) {
				FlatBookingPojo flatBookingPojo = flatBookingPojoList.get(0);
				customerPropertyDetailsPojo.setFlatBookingId(flatBookingPojo.getFlatBookingId());
				customerPropertyDetailsPojo.setCustBookingId(flatBookingPojo.getCustBookInfoId());
				customerPropertyDetailsPojo.setProId(flatBookingPojo.getCustProId());
				customerPropertyDetailsPojo.setCustomerId(flatBookingPojo.getCustomerId());
				customerPropertyDetailsPojo.setFlatId(flatBookingPojo.getFlatId());
				customerPropertyDetailsPojo.setFlatNo(flatBookingPojo.getFlatNo());
				customerPropertyDetailsPojo.setBookingStatus(flatBookingPojo.getStatusId());
				
				//these are the master details of the flat , like site, block ,floor, sale owner
				customerPropertyDetailsPojo.setBlockId(flatData.get(0).getBlockId());
				customerPropertyDetailsPojo.setSiteId(flatData.get(0).getSiteId());
				customerPropertyDetailsPojo.setSiteName(flatData.get(0).getSiteName());
				customerPropertyDetailsPojo.setFlatSaleOwnerId(flatData.get(0).getFlatSaleOwnerId());
				//customerPropertyDetailsPojo.setBookingDate(flatData.get(0).getBookingDate());
			}else {
				logger.info("*** Given Salesforce Booking Id is empty inside the checkIsFlatExistOrNot in BookingFormRestServiceImpl ****");
				throw new InSufficeientInputException(Arrays.asList("Error occured while getting Flat Related details of customer, Given Salesforce Booking Id is incorrect"));
			}
		}else {
			logger.info("*** Given Salesforce Booking Id is empty inside the checkIsFlatExistOrNot in BookingFormRestServiceImpl ****");
			throw new InSufficeientInputException(Arrays.asList("Error occured while getting Flat Related details of customer, Given Salesforce Booking Id is empty"));
		}
		return customerPropertyDetailsPojo;
	}
	
	private void updateKYCSubmittedInfo(List<CustomerKYCDocumentSubmitedInfo> kycSubmittedInfo, Long flatBookId,
			Long custBookInfoId) throws InSufficeientInputException {
		List<String> errorMsgs = new ArrayList<>();
		/* deleting previous KYCDetails */
		bookingFormServiceDaoImpl.deleteKYCDetails(custBookInfoId);

		/* saving new KYC Details */
		for (CustomerKYCDocumentSubmitedInfo submitedKYCDetails : kycSubmittedInfo) {
			Long kycId = bookingFormMapper.getKYCDocumentId(submitedKYCDetails.getDocName());
			if (kycId == null || kycId.equals(0L) || kycId == 0) {
				errorMsgs.add("Error occured while saving customer KYC document details -- Invalid KYC doc name -- "+ submitedKYCDetails.getDocName());
				throw new InSufficeientInputException(errorMsgs);
			}
			submitedKYCDetails.setDocumentId(kycId);
			submitedKYCDetails.setFlatBookId(flatBookId);
			submitedKYCDetails.setCustBookInfoId(custBookInfoId);
			logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer submitted KYC details ***");
			bookingFormServiceDaoImpl.saveCustomerKycSubmitted(bookingFormMapper.CustomerKYCDocumentSubmitedInfoToCustomerKycSubmittedDocPojo(submitedKYCDetails));
		}
	}

	/**
	 * 
	 * @param othereDetails
	 * @param flatBookId
	 * @param bookingFormReq
	 * @throws InSufficeientInputException
	 */
	private void updateOtherDetailsInfo(CustomerOtherDetailsInfo othereDetails, Long flatBookId,BookingFormRequest bookingFormReq) throws InSufficeientInputException {

		BookingFormMapper mapper = new BookingFormMapper();
		List<String> errorMsgs = new ArrayList<>();
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setFlatBookingId(flatBookId);
		List<CustomerOtherDetailspojo> customerOtherDetailspojo = bookingFormServiceDaoImpl.getCustomerOtherDetails(bookingFormRequest);
		if (Util.isNotEmptyObject(customerOtherDetailspojo) && Util.isNotEmptyObject(customerOtherDetailspojo.get(0))) {
			/*
			 * bookingFormServiceDaoImpl.deleteCustomerPOAHolderDetails(
			 * customerOtherDetailspojo.get(0));
			 * bookingFormServiceDaoImpl.deleteCustomerOtherDetails(flatBookId);
			 */
			Long customerOtherDetailsId = customerOtherDetailspojo.get(0).getId();
			bookingFormServiceDaoImpl.updateCustomerOtherDetails(mapper.CustomerOtherDetailInfoToCustomerOtherDetailspojo(othereDetails, flatBookId, employeeTicketDaoImpl,
							mapper.bookingFormRequestToEmployeeTicketRequestInfo(bookingFormReq)));
			
			try {
				Long intRefrenceId = bookingFormMapper
						.getRefrenceId(othereDetails.getReferenceName());
				if (intRefrenceId == null || intRefrenceId.equals(0L) || intRefrenceId == 0) {
					
				}else {
				Long refrenceTableId = 0L;
				Long refrenceType = 0L;
				if (intRefrenceId == Refrences.EXISTING_OWNER.getId()) {
					CustomerPojo existingCustomerPojo = null;
					logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(existing customer) details ***");
					if(Util.isNotEmptyObject(othereDetails.getReferencesCustomer().getCustomerName()) &&  Util.isNotEmptyObject(othereDetails.getReferencesCustomer().getProjectName()) && Util.isNotEmptyObject(othereDetails.getReferencesCustomer().getUnitNo())){
					List<CustomerPojo> existingCustomerPojos =  bookingFormServiceDaoImpl.getCustomerDetailsByName(othereDetails.getReferencesCustomer().getCustomerName(),othereDetails.getReferencesCustomer().getProjectName(),othereDetails.getReferencesCustomer().getUnitNo());
					    if(Util.isNotEmptyObject(existingCustomerPojos)){
						existingCustomerPojo = existingCustomerPojos.get(0);
						othereDetails.getReferencesCustomer().setCustomerId(Util.isNotEmptyObject(existingCustomerPojo)?Util.isNotEmptyObject(existingCustomerPojo.getCustomerId())?existingCustomerPojo.getCustomerId():0l:0l);
						refrenceTableId = bookingFormServiceDaoImpl.saveReferencesCustomer(mapper.ReferencesCustomerToReferencesCustomerPojo(othereDetails.getReferencesCustomer()));
						refrenceType = MetadataId.REFERENCES_CUSTOMER.getId();
						}
					}
				} else if (intRefrenceId == Refrences.FRIEND_FAMILY.getId()) {
					logger.info(
							"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(friend or family) details ***");
					if(Util.isNotEmptyObject(othereDetails.getReferencesFriend().getReferenceFreindsorFamilyName())) {
					refrenceTableId = bookingFormServiceDaoImpl.saveReferencesFriend(mapper.ReferencesFriendInfoToReferencesFriendPojo(othereDetails.getReferencesFriend()));
					refrenceType = MetadataId.REFERENCES_FRIEND.getId();
					}
				} else if (intRefrenceId == Refrences.CHANEL_PARTNER.getId()) {
					logger.info(
							"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(channel partner) details ***");
					ChanelPartnerInfo cpInfo =othereDetails.getChannelPartnerInfo();
					if(Util.isNotEmptyObject(cpInfo.getChannelPartnerCPID()) && Util.isNotEmptyObject(cpInfo.getChannelPartnerCompanyName())) {
					List<ChannelPartnerMasterPojo> channelPartnerMasterPojos = bookingFormServiceDaoImpl.getChannelPartnerMasterByIdName(cpInfo.getChannelPartnerCPID(),cpInfo.getChannelPartnerCompanyName());
					if (channelPartnerMasterPojos == null || channelPartnerMasterPojos.size() == 0) {
					}else {
					refrenceTableId = channelPartnerMasterPojos.get(0).getChannelPartnerId();
					refrenceType = MetadataId.CHANEL_PARTNER.getId();
					}
					}
				}else { 
				 logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - setting customer reference(others) details ***");
				 refrenceTableId = intRefrenceId; 
				 refrenceType =MetadataId.REFRENCE_MASTER.getId(); 
				}
				if(Util.isNotEmptyObject(refrenceTableId) && Util.isNotEmptyObject(refrenceType)) {
					othereDetails.getReferencesMappingInfo().setTypeId(refrenceTableId);
					othereDetails.getReferencesMappingInfo().setType(refrenceType);
					othereDetails.getReferencesMappingInfo().setCustOtherId(customerOtherDetailsId);
				logger.info(
						"*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving customer reference details ***");
				ReferencesMappingPojo pojo = mapper.ReferencesMappingInfoToReferencesMappingPojo(othereDetails.getReferencesMappingInfo());
				List<ReferencesMappingPojo> referencesMappingPojos = bookingFormServiceDaoImpl.getReferencesMapping(customerOtherDetailsId);
				if(Util.isNotEmptyObject(referencesMappingPojos)) {
					bookingFormServiceDaoImpl.updateReferencesMapping(pojo);
				}else {
					bookingFormServiceDaoImpl.saveReferencesMapping(pojo);
				}
				}
				}
			} catch (Exception e) {
				errorMsgs.add("Error occured while saving reference details - ");
				logger.error("Error occured while saving reference details -"+e.toString());
				throw new InSufficeientInputException(errorMsgs);
			}
			try {
				if (Util.isNotEmptyObject(othereDetails.getPoadetailsInfo()) && Util.isNotEmptyObject(othereDetails.getPoadetailsInfo().getNameOfPOA() )) {
					List<PoaHolderPojo> poaHolderPojos = bookingFormServiceDaoImpl.getPoaHolder(customerOtherDetailsId,null,null);
					PoaHolderPojo poaHolderPojo = mapper.POADetailsInfoToPoaHolderPojo(othereDetails.getPoadetailsInfo(), customerOtherDetailsId);
					if(Util.isNotEmptyObject(poaHolderPojos)) {
						bookingFormServiceDaoImpl.updatePoaHolder(poaHolderPojo);
					}else {
					  bookingFormServiceDaoImpl.savePoaHolder(poaHolderPojo);
					}
				}
			} catch (Exception e) {
				// bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
				errorMsgs.add("Error occured while saving POA details - ");
				logger.error("Error occured while saving POA details -" + e.toString());
				throw new InSufficeientInputException(errorMsgs);
			}
		}
	}

	private void updateAddressInfo(List<AddressInfo> addressInfos, Long custBookInfoId) {

		/* deleting Address Mapping and Address */
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
		bookingFormRequest.setCustBookInfoId(custBookInfoId);
		List<AddressMappingPojo> addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
		bookingFormServiceDaoImpl.deleteAddressMappingOfApplicantOrCoApplicant(custBookInfoId,MetadataId.CUSTOMER.getId());
		if (Util.isNotEmptyObject(addressMappingPojos)) {
			for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
				bookingFormServiceDaoImpl.deleteAddressOfApplicantOrCoApplicant(addressMappingPojo.getAddressId());
			}
		}
		/* updating Address Details */
		List<AddressPojo> custAddressPojos = bookingFormMapper.addressInfosToAddressPojos(addressInfos);
		List<AddressMappingPojo> custAddressMappingPojos = bookingFormMapper.addressInfosToAddressMappingPojos(addressInfos, custBookInfoId);

		int count = 0;
		for (AddressPojo addressMappingPojo : custAddressPojos) {
			Long addrId = bookingFormServiceDaoImpl.saveAddressDetails(addressMappingPojo);
			AddressMappingPojo addressMappingPojo2 = custAddressMappingPojos.get(count);
			addressMappingPojo2.setAddressId(addrId);
			++count;
			bookingFormServiceDaoImpl.saveAddressmapping(addressMappingPojo2);
		}
	}

	private void updateCustInfo(CustomerInfo customerInfo) {
		BookingFormMapper mapper = new BookingFormMapper();
		@SuppressWarnings("unused")
		Long customerId = bookingFormServiceDaoImpl.updateCustomerInfo(mapper.CustomerInfoToCustomerPojo(customerInfo));
	}
	
    private void updateFlatBooking(FlatBookingInfo flatBookingInfo) {
		BookingFormMapper mapper = new BookingFormMapper();
		@SuppressWarnings("unused")
		Long id = bookingFormServiceDaoImpl.updateFlatBookingInfo(mapper.FlatBookingInfoToFlatBookingPojo(flatBookingInfo));
	}
    
    private void updateFlatBookingMileStoneDetails(FlatBookingInfo flatBookingInfo) {
		bookingFormServiceDaoImpl.updateFlatBookingMileStoneDetails(bookingFormMapper.FlatBookingInfoToFlatBookingPojo(flatBookingInfo));
	}

	private Long updateProfessionalInfo(ProfessionalInfo proInfo) {
		ProfessionalDetailsPojo professionalDetailsPojo = bookingFormMapper.professionalInfoToProfessionalDetailsPojo(proInfo);
		Long proId = bookingFormServiceDaoImpl.updateCustomerProfessionalDetails(professionalDetailsPojo);
		return proId;
	}

	public void updateContactInfo(CustomerBookingInfo customerBookingInfo, Long flatBookId, Long customerId,Long longProfessionId) {
		CustBookInfoPojo custBookInfoToCustBookInfoPojo = bookingFormMapper.custBookInfoToCustBookInfoPojo(customerBookingInfo, bookingFormServiceDaoImpl, flatBookId, customerId, longProfessionId);
		ContactInfoPojo contactInfoPojo = new ContactInfoPojo();
		BeanUtils.copyProperties(custBookInfoToCustBookInfoPojo, contactInfoPojo);
		bookingFormServiceDaoImpl.deleteContactInfo(contactInfoPojo);
		bookingFormServiceDaoImpl.saveContactInfo(contactInfoPojo);
	}

	private BookingFormRequest prepareFlatRelatedObject(FlatBookingInfo flatBookingInfo) {
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setRequestUrl("saveBookingDetails");
		bookingFormRequest.setSiteName(flatBookingInfo.getSiteInfo().getName());
		bookingFormRequest.setFlatNo(flatBookingInfo.getFlatInfo().getFlatNo());
		bookingFormRequest.setFloorName(flatBookingInfo.getFloorInfo().getFloorName());
		bookingFormRequest.setBlockName(flatBookingInfo.getBlockInfo().getName());
		return bookingFormRequest;
	}
	
	private BookingFormRequest prepareCustomerRelatedObject(CustomerInfo customerInfo,CustomerBookingInfo customerBookingInfo) throws InSufficeientInputException {
		if (Util.isNotEmptyObject(customerInfo) && Util.isNotEmptyObject(customerBookingInfo)) {
			BookingFormRequest bookingFormRequest = new BookingFormRequest();
			bookingFormRequest.setCustomerName(customerInfo.getFirstName());
			bookingFormRequest.setPancard(customerInfo.getPancard());
			bookingFormRequest.setEmail(customerBookingInfo.getEmail());
			bookingFormRequest.setMobileNo(customerBookingInfo.getPhoneNo());
			return bookingFormRequest;
		} else {
			throw new InSufficeientInputException(Arrays.asList("Invalid Customer Info or Customer Booking Info Details"));
		}
	}

	public void updateOrSaveCoApplicantDetails(List<CoApplicentDetailsInfo> coApplicentDetails, Long custBookInfoId,Long custProId, Long customerId,Long status) throws IllegalAccessException, InSufficeientInputException {
		List<String> errorMsgs = new ArrayList<>();
		
		int i = 0;
		for (CoApplicentDetailsInfo coApplInfo : coApplicentDetails) {
			i+=1;
		 try {
				/* If coapplicant pancard and passport is empty */
		  if (Util.isEmptyObject(coApplInfo.getCo_ApplicantInfo().getPancard()) && Util.isEmptyObject(coApplInfo.getCo_ApplicantInfo().getPassport())) {
			    Long coAppBookInfoId = null;
			    BookingFormRequest bookingFormRequest = new BookingFormRequest();
				bookingFormRequest.setRequestUrl("isAlreadyPresent");
				bookingFormRequest.setCustBookInfoId(custBookInfoId);
			  if(i==1) {
				bookingFormRequest.setType(MetadataId.APPLICANT1.getId());
			  }else if(i==2) {
				bookingFormRequest.setType(MetadataId.APPLICANT2.getId());
			  }else {
				bookingFormRequest.setType(0l);
			  }
			   List<CoAppBookInfoPojo> coAppBookInfos = bookingFormServiceDaoImpl.getCoAppBookInfo(bookingFormRequest);
				if(Util.isNotEmptyObject(coAppBookInfos)) {
					coAppBookInfoId = coAppBookInfos.get(0).getCoAppBookInfoId();
				}
				if(Util.isNotEmptyObject(coAppBookInfoId)){
					/* delete existing coapplicant and insert new one. */
					bookingFormServiceDaoImpl.deleteCoApplicantBookInfoData(coAppBookInfoId);
				}
				/* If coapplicant pancard and passport is not empty */
		 } else {
				List<Co_ApplicantPojo> listCoApplicantsByPanCard = bookingFormServiceDaoImpl.getCo_ApplicantByPanCard(coApplInfo.getCo_ApplicantInfo().getPancard(), coApplInfo.getCo_ApplicantInfo().getPassport(),null);
				/* if coapplicant is new Save Co-Applicant Data */
				if (Util.isEmptyObject(listCoApplicantsByPanCard)) {
					Long coAppBookInfoId = isAlreadyPresent(coApplInfo,custBookInfoId);
					if(Util.isNotEmptyObject(coAppBookInfoId)){
						/* delete existing coapplicant and insert new one. */
						bookingFormServiceDaoImpl.deleteCoApplicantBookInfoData(coAppBookInfoId);
						saveCoApplicantDetails(coApplInfo, custBookInfoId,null,status);
					}else {
						saveCoApplicantDetails(coApplInfo, custBookInfoId,null,status);
					}
					/* if coapplicant is alredy exists Save Co-Applicant Data */
				} else {
					logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - getting existing Co-Applicant details ***");
					Long coAppId = listCoApplicantsByPanCard.get(0).getCoApplicantId();
					List<CoAppBookInfoPojo> CoAppBookInfoPojosList = bookingFormServiceDaoImpl.getSameCoApplicantforMulCustomerCount(coApplInfo, custBookInfoId);

					 /* inorder to eleminate duplicate CustBookInfoIds here we add the values in set   */
					Set<Long> custBookInfoIds = new HashSet<Long>();
					for (CoAppBookInfoPojo coAppBookInfoPojo : CoAppBookInfoPojosList) {
						custBookInfoIds.add(coAppBookInfoPojo.getCustBookInfoId());
					}
					/* When same co-applicant exists for another flat we update the data */
					if (custBookInfoIds.size() > 1) {
						updateCoApplicantDetails(coApplInfo, coAppId, custBookInfoId,status);
						/*
						 * when co-applicant exists only for the given customer we delete previous data
						 * and insert the updated data
						 */
			    	}else {
			    		boolean flag = false;
						/* deleting old data */
			    		/*
						bookingFormRequest.setRequestUrl("checkcoaplicants");
						bookingFormRequest.setCoApplicantId(coAppId);
						*/
			    		BookingFormRequest bookingFormRequest = new BookingFormRequest();
						bookingFormRequest.setRequestUrl("updatecoaplicants");
						bookingFormRequest.setCoApplicantId(coAppId);
						bookingFormRequest.setCustBookInfoId(custBookInfoId);
						
						List<CoAppBookInfoPojo> coAppBookInfos = bookingFormServiceDaoImpl.getCoAppBookInfo(bookingFormRequest);

						/* Check wether coAppBookInfos data present or not */
						if (Util.isNotEmptyObject(coAppBookInfos)) {
							flag = true;
							for (CoAppBookInfoPojo coAppBookInfoPojo : coAppBookInfos) {
								if (Util.isNotEmptyObject(coAppBookInfoPojo)) {
									Long coAppBookInfoId = coAppBookInfoPojo.getCoAppBookInfoId();
									Long coAppProfId = coAppBookInfoPojo.getCustProffisionalId();
									Long type = Util.isNotEmptyObject(coAppBookInfoPojo.getType())? coAppBookInfoPojo.getType() : MetadataId.APPLICANT1.getId() ;
									/* deleting Address Mapping and Address */
									bookingFormRequest.setMetadataId(type);
									bookingFormRequest.setCoAppBookInfoId(coAppBookInfoId);
									List<AddressMappingPojo> addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
									bookingFormServiceDaoImpl.deleteAddressMappingOfApplicantOrCoApplicant(coAppBookInfoId, type);
									if (Util.isNotEmptyObject(addressMappingPojos)) {
										for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
											bookingFormServiceDaoImpl.deleteAddressOfApplicantOrCoApplicant(addressMappingPojo.getAddressId());
										}
									}
									/* deleting Co_Applicant Booking Info Details and Co_App_Professional Details */
									bookingFormServiceDaoImpl.deleteCoApplicantProffesionalDetails(coAppProfId);
									bookingFormServiceDaoImpl.deleteCoApplicantBookInfoData(coAppBookInfoId);
								}
								/* deleting Co_Applicant_Detail */
								bookingFormServiceDaoImpl.delteCoApplicantInfoDetails(coAppId);
							}
						}
						/* save Co_Applicant_Info_Details */
						if(flag) {
							Long coAppBookInfoId = isAlreadyPresent(coApplInfo,custBookInfoId);
							if(Util.isNotEmptyObject(coAppBookInfoId)){
								/* delete existing coapplicant and insert new one. */
								bookingFormServiceDaoImpl.deleteCoApplicantBookInfoData(coAppBookInfoId);
								saveCoApplicantDetails(coApplInfo, custBookInfoId,null,status);
							}else {
								saveCoApplicantDetails(coApplInfo, custBookInfoId,null,status);
							}
						}else {
							Long coAppBookInfoId = isAlreadyPresent(coApplInfo,custBookInfoId);
							if(Util.isNotEmptyObject(coAppBookInfoId)){
								/* delete existing coapplicant and insert new one. */
								bookingFormServiceDaoImpl.deleteCoApplicantBookInfoData(coAppBookInfoId);
								saveCoApplicantDetails(coApplInfo, custBookInfoId,coAppId,status);
							}else {
								saveCoApplicantDetails(coApplInfo, custBookInfoId,coAppId,status);
							}
						}
					}
				}
			}
			
			} catch (Exception e) {
				errorMsgs.add("Error occured while getting existing coapplicant/saving new coapplicant - ");
				logger.error("Error occured while getting existing coapplicant/saving new coapplicant - " + e.toString());
				throw new InSufficeientInputException(errorMsgs);
			}
		}
	}

	private void updateCoApplicantDetails(CoApplicentDetailsInfo coApplInfo, Long coAppId, Long custBookInfoId,Long status) throws InSufficeientInputException {
		
		List<String> errorMsgs = new ArrayList<>();
		/* Updating CoApplicant Info Details */
		if (Util.isNotEmptyObject(coApplInfo.getCo_ApplicantInfo())) {
			Co_ApplicantInfo co_ApplicantInfo = coApplInfo.getCo_ApplicantInfo();
			co_ApplicantInfo.setCoApplicantId(coAppId);
			/* updating previous Booking Status for Co-Applicant */
			co_ApplicantInfo.setStatusId(status);
			bookingFormServiceDaoImpl.updateCoApplicantInfo(co_ApplicantInfo);
		} else {
			errorMsgs.add("Error occured while updating coApp Info details - ");
			logger.error("Error occured while updating coApp Info details - " + coApplInfo.getCo_ApplicantInfo());
			throw new InSufficeientInputException(errorMsgs);
		}

		/* To Get CoApplicantBookInfoId and CoAppProfId */
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setRequestUrl("updatecoaplicants");
		bookingFormRequest.setCoApplicantId(coAppId);
		bookingFormRequest.setCustBookInfoId(custBookInfoId);
		List<CoAppBookInfoPojo> coAppBookInfo = bookingFormServiceDaoImpl.getCoAppBookInfo(bookingFormRequest);
		if (Util.isNotEmptyObject(coAppBookInfo)) {
			Long coAppBookInfoId = coAppBookInfo.get(0).getCoAppBookInfoId();
			Long coAppProfId = coAppBookInfo.get(0).getCustProffisionalId();
			coApplInfo.getCoApplicentBookingInfo().setCoAppBookInfoId(coAppBookInfoId);
			
			/* Updating CoApplicant BookInfo Details */
			if (Util.isNotEmptyObject(coAppBookInfoId)
					&& Util.isNotEmptyObject(coApplInfo.getCoApplicentBookingInfo())) {
				CoApplicentBookingInfo coApplicentBookingInfo = coApplInfo.getCoApplicentBookingInfo();
				coApplicentBookingInfo.setCoApplicantId(coAppId);
				coApplicentBookingInfo.setCustBookInfoId(custBookInfoId);
				bookingFormServiceDaoImpl.updateCoApplicantBookInfo(coApplicentBookingInfo, coAppBookInfoId, coApplInfo);
				//BookingFormMapper.getMetadataId(Util.isNotEmptyObject(coApplicentBookingInfo.getType()) ? coApplicentBookingInfo.getType() : "N/A"));
			} else {
				errorMsgs.add("Error occured while updating coApp Book Info details - ");
				logger.error("Error occured while updating coApp Book Info details - "+ coApplInfo.getCoApplicentBookingInfo());
				throw new InSufficeientInputException(errorMsgs);
			}
			/* Updating CoApplicant Professional Details */
			if (Util.isNotEmptyObject(coAppProfId) && Util.isNotEmptyObject(coApplInfo.getProfessionalInfo())) {
				ProfessionalInfo professionalInfo = coApplInfo.getProfessionalInfo();
				professionalInfo.setCustProffisionalId(coAppProfId);
				updateProfessionalInfo(professionalInfo);
			} else {
				errorMsgs.add("Error occured while updating coApp professional details - ");
				logger.error("Error occured while updating coApp professional details - " + coApplInfo.getProfessionalInfo());
				throw new InSufficeientInputException(errorMsgs);
			}

			/* deleting Address Mapping and Address */
			//CoApplicentBookingInfo coApplicentBookingInfo = coApplInfo.getCoApplicentBookingInfo();
			//Long type = BookingFormMapper.getMetadataId(Util.isNotEmptyObject(coApplicentBookingInfo.getType()) ? coApplicentBookingInfo.getType() : "N/A");			
			Long type = Util.isNotEmptyObject(coAppBookInfo.get(0).getType())?coAppBookInfo.get(0).getType():MetadataId.APPLICANT1.getId();
			bookingFormRequest.setMetadataId(type);
			bookingFormRequest.setCoAppBookInfoId(coAppBookInfoId);
			List<AddressMappingPojo> addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
			bookingFormServiceDaoImpl.deleteAddressMappingOfApplicantOrCoApplicant(coAppBookInfoId, type);
			if (Util.isNotEmptyObject(addressMappingPojos)) {
				for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
					bookingFormServiceDaoImpl.deleteAddressOfApplicantOrCoApplicant(addressMappingPojo.getAddressId());
				}
			}
			/* Save Co-Applicant Address Details */
			List<AddressPojo> coAppAddressPojos = bookingFormMapper.addressInfosToAddressPojos(coApplInfo.getAddressInfos());
			List<AddressMappingPojo> coAppAddressMappingPojos = bookingFormMapper.addressInfosToAddressMappingPojos(coApplInfo.getAddressInfos(), coAppBookInfoId);
			for (int i = 0; i < coAppAddressPojos.size(); i++) {
				logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant address details **");
				Long addrId = bookingFormServiceDaoImpl.saveAddressDetails(coAppAddressPojos.get(i));
				logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant address mapping details **");
				coAppAddressMappingPojos.get(i).setAddressId(addrId);
				bookingFormServiceDaoImpl.saveAddressmapping(coAppAddressMappingPojos.get(i));
			}
		} else {
			Long coAppBookInfoId = isAlreadyPresent(coApplInfo,custBookInfoId);
			if(Util.isNotEmptyObject(coAppBookInfoId)){
				/* delete existing coapplicant and insert new one. */
				bookingFormServiceDaoImpl.deleteCoApplicantBookInfoData(coAppBookInfoId);
				saveCoApplicantDetails(coApplInfo, custBookInfoId,coAppId,status);
			}else {
			  saveCoApplicantDetails(coApplInfo, custBookInfoId,coAppId,status);
			}
		}

	}
	private Long isAlreadyPresent(CoApplicentDetailsInfo coApplInfo,Long custBookInfoId) {
		logger.info("**** The control is inside isAlreadyPresent in BookingformServiceImpl *****");
		Long type = 0l;
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setRequestUrl("isAlreadyPresent");
		bookingFormRequest.setCustBookInfoId(custBookInfoId);
		
		if(Util.isNotEmptyObject(coApplInfo.getAddressInfos())) {
			for(AddressInfo addressInfo : coApplInfo.getAddressInfos()) {
			   AddressMappingInfo adressMappingInfo = addressInfo.getAddressMappingType();
			   if(Util.isNotEmptyObject(adressMappingInfo.getMetaType())) {
				   for(MetadataId id : MetadataId.values()) {
					  if(id.getName().equalsIgnoreCase(adressMappingInfo.getMetaType())){
						  type = id.getId();
					  }
			       } 
			   }
			}
		}
		bookingFormRequest.setType(type);
		List<CoAppBookInfoPojo> coAppBookInfos = bookingFormServiceDaoImpl.getCoAppBookInfo(bookingFormRequest);
		
		/*
		if(Util.isNotEmptyObject(coAppBookInfos)) {
			map.put(Boolean.TRUE,coAppBookInfos.get(0).getCoAppBookInfoId());
		   return map;	
		}else {
			map.put(Boolean.FALSE,coAppBookInfos.get(0).getCoAppBookInfoId());
			return map;	
		} */
		
		if(Util.isNotEmptyObject(coAppBookInfos)) {
			return coAppBookInfos.get(0).getCoAppBookInfoId();
		}else {
			return null;
		}
	}
	private void saveCoApplicantDetails(CoApplicentDetailsInfo coApplInfo, Long custBookInfoId,Long flag,Long status) throws InSufficeientInputException {
		Long coAppId, coAppProfessionId, coAppBookInfoId;
		
		/* Save Co-Applicant Info */
		logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving new Co-Applicant details ***");
		/* To set Co-Applicant status Active */
		coApplInfo.getCo_ApplicantInfo().setActionUrl("updateCoApplicantData");
		
		/*  check if coapplicant is aleady exist or not. */
		if(Util.isEmptyObject(flag)) {
		coAppId = bookingFormServiceDaoImpl.saveCoApplicant(bookingFormMapper.co_ApplicantInfoToCo_ApplicantPojo(coApplInfo.getCo_ApplicantInfo(),status));
		}else {
			//update coapplicant details.
			coAppId = flag;
			Co_ApplicantInfo co_ApplicantInfo = coApplInfo.getCo_ApplicantInfo();
			co_ApplicantInfo.setCoApplicantId(coAppId);
			/* updating previous Booking Status for Co-Applicant */
			co_ApplicantInfo.setStatusId(status);
			bookingFormServiceDaoImpl.updateCoApplicantInfo(co_ApplicantInfo);
		}
		/* Save Co-Applicant Professional Details */
		ProfessionalDetailsPojo coAppProfessionalDetailsPojo = bookingFormMapper.professionalInfoToProfessionalDetailsPojo(coApplInfo.getProfessionalInfo());
		logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant professional details ***");
		coAppProfessionId = bookingFormServiceDaoImpl.saveCustomerProfessionalDetails(coAppProfessionalDetailsPojo);

		/* Save Co-Applicant BookInfo Details */
		logger.info("*** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant booking info details ***");
		CoApplicentBookingInfo coApplicantBookInfo = coApplInfo.getCoApplicentBookingInfo();
		/* To set Co-Applicant status Active */
		coApplicantBookInfo.setActionUrl("updateCoApplicantData");
		//coApplInfo.getAddressInfos().get(0).getAddressMappingType().setAddressType(Util.isNotEmptyObject(coApplicantBookInfo.getType()) ? coApplicantBookInfo.getType() : "N/A");
		coAppBookInfoId = bookingFormServiceDaoImpl.saveCoAppBookInfo(bookingFormMapper.coAppBookInfoToCoAppBookInfoPojo(coApplicantBookInfo,custBookInfoId, coAppId, coAppProfessionId, coApplInfo,status));

		/* Save Co-Applicant Address Details */
		List<AddressPojo> coAppAddressPojos = bookingFormMapper.addressInfosToAddressPojos(coApplInfo.getAddressInfos());
		List<AddressMappingPojo> coAppAddressMappingPojos = bookingFormMapper.addressInfosToAddressMappingPojos(coApplInfo.getAddressInfos(), coAppBookInfoId);
		for (int i = 0; i < coAppAddressPojos.size(); i++) {
			logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant address details **");
			Long addrId = bookingFormServiceDaoImpl.saveAddressDetails(coAppAddressPojos.get(i));
			logger.info("** inside BookingFormServiceImpl{} saveBookingFormDetails() - saving Co-Applicant address mapping details **");
			coAppAddressMappingPojos.get(i).setAddressId(addrId);
			bookingFormServiceDaoImpl.saveAddressmapping(coAppAddressMappingPojos.get(i));
		}
	}
	
	@Override
	public CustomerBookingFormInfo addOneDay(@NonNull CustomerBookingFormInfo customerBookingForm) {
		logger.info("*** The control is inside the addOneDay in BookingFormServiceImpl ****");
		
		/* customer date of birth */
		
		if (Util.isNotEmptyObject(customerBookingForm.getCustomerInfo())) {
			if (Util.isNotEmptyObject(customerBookingForm.getCustomerInfo().getDob())) {
				customerBookingForm.getCustomerInfo()
						.setDob(TimeUtil.addOneDay(customerBookingForm.getCustomerInfo().getDob()));
			}
		}
		
		/* customers Date of Anniversary */
		if (Util.isNotEmptyObject(customerBookingForm.getCustomerBookingInfo())) {
			if (Util.isNotEmptyObject(customerBookingForm.getCustomerBookingInfo().getDateOfAnniversery())) {
				customerBookingForm.getCustomerBookingInfo().setDateOfAnniversery(
						TimeUtil.addOneDay(customerBookingForm.getCustomerBookingInfo().getDateOfAnniversery()));
			}
		}
		
		/* coapplicant date of  Birth and Anniversary */
		if(Util.isNotEmptyObject(customerBookingForm.getCoApplicentDetails())){
			for(CoApplicentDetailsInfo coApplicentDetailsInfo :customerBookingForm.getCoApplicentDetails()) {
				
				if(Util.isNotEmptyObject(coApplicentDetailsInfo.getCo_ApplicantInfo().getDateOfBirth())){
					coApplicentDetailsInfo.getCo_ApplicantInfo().setDateOfBirth(TimeUtil.addOneDay(coApplicentDetailsInfo.getCo_ApplicantInfo().getDateOfBirth()));
				}
				
				if(Util.isNotEmptyObject(coApplicentDetailsInfo.getCoApplicentBookingInfo().getDateOfAnniversery())){
					coApplicentDetailsInfo.getCoApplicentBookingInfo().setDateOfAnniversery(TimeUtil.addOneDay(coApplicentDetailsInfo.getCoApplicentBookingInfo().getDateOfAnniversery()));
				}
			}
		}
		
		
		if (Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo())) {
			/* FlatBooking Booking Date */
			if (Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getBookingDate())) {
				customerBookingForm.getFlatBookingInfo()
						.setBookingDate(TimeUtil.addOneDay(customerBookingForm.getFlatBookingInfo().getBookingDate()));
			}
			/* FlatBooking Registration Date */
			if (Util.isNotEmptyObject(customerBookingForm.getFlatBookingInfo().getRegistrationDate())) {
				customerBookingForm.getFlatBookingInfo().setRegistrationDate(
						TimeUtil.addOneDay(customerBookingForm.getFlatBookingInfo().getRegistrationDate()));
			}
		}
		
		/* Sales Head Checklist */
		if (Util.isNotEmptyObject(customerBookingForm.getCheckListSalesHead())) {
			/* ProjectSalesHeadDate */
			if (Util.isNotEmptyObject(customerBookingForm.getCheckListSalesHead().getProjectSalesHeadDate())) {
				customerBookingForm.getCheckListSalesHead().setProjectSalesHeadDate(
						TimeUtil.addOneDay(customerBookingForm.getCheckListSalesHead().getProjectSalesHeadDate()));
			}
			/* AuthorizedSignatoryDate  */
			if (Util.isNotEmptyObject(customerBookingForm.getCheckListSalesHead().getAuthorizedSignatoryDate())) {
				customerBookingForm.getCheckListSalesHead().setAuthorizedSignatoryDate(
						TimeUtil.addOneDay(customerBookingForm.getCheckListSalesHead().getAuthorizedSignatoryDate()));
			}
		}
		
		/*  Checklist CRM  */
		if (Util.isNotEmptyObject(customerBookingForm.getCheckListCRM())) {
			/* ExpectedAgreeDate */
			if(Util.isNotEmptyObject(customerBookingForm.getCheckListCRM().getExpectedAgreeDate())){
				customerBookingForm.getCheckListCRM().setExpectedAgreeDate(TimeUtil.addOneDay(customerBookingForm.getCheckListCRM().getExpectedAgreeDate()));
			}
			
			/* CrmSignedDate */
			if(Util.isNotEmptyObject(customerBookingForm.getCheckListCRM().getCrmSignedDate())){
				customerBookingForm.getCheckListCRM().setCrmSignedDate(TimeUtil.addOneDay(customerBookingForm.getCheckListCRM().getCrmSignedDate()));
			}
			
			/* authorizedSignatoryDate */
			if(Util.isNotEmptyObject(customerBookingForm.getCheckListCRM().getAuthorizedSignatoryDate())){
				customerBookingForm.getCheckListCRM().setAuthorizedSignatoryDate(TimeUtil.addOneDay(customerBookingForm.getCheckListCRM().getAuthorizedSignatoryDate()));
			}
		}
		
		/* Checklist Legal Officer  */
		if (Util.isNotEmptyObject(customerBookingForm.getCheckListLegalOfficer())) {
			/* legalOfficeSignedate  */
			if (Util.isNotEmptyObject(customerBookingForm.getCheckListLegalOfficer().getLegalOfficeSignedate())) {
				customerBookingForm.getCheckListLegalOfficer().setLegalOfficeSignedate(TimeUtil.addOneDay(customerBookingForm.getCheckListLegalOfficer().getLegalOfficeSignedate()));
			}
			/*  authorizedSignatoryDate */
			if (Util.isNotEmptyObject(customerBookingForm.getCheckListLegalOfficer().getAuthorizedSignatoryDate())) {
				customerBookingForm.getCheckListLegalOfficer().setAuthorizedSignatoryDate(TimeUtil.addOneDay(customerBookingForm.getCheckListLegalOfficer().getAuthorizedSignatoryDate()));
			}
		}
		
		/*  CheckList Registration */
		if (Util.isNotEmptyObject(customerBookingForm.getCheckListRegistration())) {
			/* legalOfficerDate */
			if (Util.isNotEmptyObject(customerBookingForm.getCheckListRegistration().getLegalOfficerDate())) {
				customerBookingForm.getCheckListRegistration().setLegalOfficerDate(TimeUtil.addOneDay(customerBookingForm.getCheckListRegistration().getLegalOfficerDate()));
			}
			
			/* accountsExecutiveDate */
			if (Util.isNotEmptyObject(customerBookingForm.getCheckListRegistration().getAccountsExecutiveDate())) {
				customerBookingForm.getCheckListRegistration().setAccountsExecutiveDate(TimeUtil.addOneDay(customerBookingForm.getCheckListRegistration().getAccountsExecutiveDate()));
			}
			
		   /* authorizedDate */
			if (Util.isNotEmptyObject(customerBookingForm.getCheckListRegistration().getAuthorizedDate())) {
				customerBookingForm.getCheckListRegistration().setAuthorizedDate(TimeUtil.addOneDay(customerBookingForm.getCheckListRegistration().getAuthorizedDate()));
			}
		}
		
		return customerBookingForm;
	}
	
	
	@SuppressWarnings("unused")
	private boolean isSalesTeamLeadIdDuplicate(List<CustBookInfoPojo> custBookInfoPojos) {
		logger.info("*** The control is inside the isSalesTeamLeadIdDuplicate in BookingFormServiceImpl ****");
		for(CustBookInfoPojo custBookInfoPojo : custBookInfoPojos) {
			if(Util.isNotEmptyObject(custBookInfoPojo) && (Util.isNotEmptyObject(custBookInfoPojo.getStatusId())?!(custBookInfoPojo.getStatusId().equals(Status.REJECTED.status) || custBookInfoPojo.getStatusId().equals(Status.INACTIVE.status)):false)){
				return true;
			}
		}
		return false;
	}
	
	private CustomerBookingFormInfo updateTdsAutherizationType(CustomerBookingFormInfo customerBookingFormInfo) {
		logger.info("*** The control is inside the updateTdsAutherizationType in BookingFormServiceImpl ****");
		if(Util.isNotEmptyObject(customerBookingFormInfo) && Util.isNotEmptyObject(customerBookingFormInfo.getCustomerBookingInfo())) {
			if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerBookingInfo().getTdsAuthorizationOption1()) || Util.isNotEmptyObject(customerBookingFormInfo.getCustomerBookingInfo().getTdsAuthorizationOption2())) {
				if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerBookingInfo().getTdsAuthorizationOption1()) && customerBookingFormInfo.getCustomerBookingInfo().getTdsAuthorizationOption1()){
					customerBookingFormInfo.getCustomerBookingInfo().setTdsAuthorizationType("Option:1");
				}
				else if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerBookingInfo().getTdsAuthorizationOption2()) && customerBookingFormInfo.getCustomerBookingInfo().getTdsAuthorizationOption2()){
					customerBookingFormInfo.getCustomerBookingInfo().setTdsAuthorizationType("Option:2");
				}
			}
		}
		return customerBookingFormInfo;
	}
	
	@Override
	public Map<String, Object> inactiveBookings(BookingFormRequest request) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		Result result = null;
		//result = (Result) restTemplate.postForObject("http://129.154.74.18:8090/SumadhuraGateway/employeeservice/bookingFormService/actionBookingDetails.spring", request, Result.class);
		//System.out.println(result);
		request.setRequestUrl("inactiveBookingsBatchUpdate");

		/*EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		//employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(bookingFormRequest.getFlatBookingId()));
		employeeFinancialServiceInfo.setSiteId(bookingFormRequest.getSiteId());*/
		/* getting Customer Details */
		List<Map<String, Object>> customerPropertyDetailsPojoList = bookingFormServiceDaoImpl.getCustomerPropertyDetailsToInactive(request);
		if(Util.isEmptyObject(customerPropertyDetailsPojoList)) {
			throw new EmployeeFinancialServiceException("Failed to generate allotment letter, No data found for customer.");
		}
		List<BookingFormRequest> actionBookingFormRequestList = new ArrayList<>();
		//List<String> flatNoList = new ArrayList<>();
		for (Map<String, Object> customerPropertyDetailsPojo : customerPropertyDetailsPojoList) {
			BookingFormRequest actionBookingFormRequest = null;
			actionBookingFormRequest = new BookingFormRequest();
			
			actionBookingFormRequest.setSessionKey(request.getSessionKey());
			actionBookingFormRequest.setSiteName(customerPropertyDetailsPojo.get("SALES_FORCE_SITE_NAME").toString());
			actionBookingFormRequest.setBlockName(customerPropertyDetailsPojo.get("BLOCK_NAME").toString());
			actionBookingFormRequest.setFloorName(customerPropertyDetailsPojo.get("FLOOR_NAME").toString());
			actionBookingFormRequest.setFlatNo(customerPropertyDetailsPojo.get("FLAT_NO").toString());
			
			if(request.getActionStr()!=null && request.getActionStr().equalsIgnoreCase("reject")) {
				actionBookingFormRequest.setSessionKey("E56524227389EF9C7DD92B58E530D2AC062E7045A229C89ADEE9F757EA97D80B");
				
				actionBookingFormRequest.setBookingId(customerPropertyDetailsPojo.get("SALESFORCE_BOOKING_ID")==null?"":customerPropertyDetailsPojo.get("SALESFORCE_BOOKING_ID").toString());
				actionBookingFormRequest.setComments("Booking Re-Uploaded");
				actionBookingFormRequest.setActionStr("reject");
				List<BookingFormApproveRequest> bookingFormApproveRequestList = new ArrayList<>();
				BookingFormApproveRequest bookingFormApproveRequest = new BookingFormApproveRequest();
				bookingFormApproveRequest.setCustomerId(Long.valueOf(customerPropertyDetailsPojo.get("CUST_ID").toString()));
				bookingFormApproveRequest.setFlatBookingId(Long.valueOf(customerPropertyDetailsPojo.get("FLAT_BOOK_ID").toString()));
				bookingFormApproveRequest.setCustName(customerPropertyDetailsPojo.get("CUST_NAME").toString());
				bookingFormApproveRequest.setLeadId(Long.valueOf(customerPropertyDetailsPojo.get("SALES_TEAM_LEAD_ID").toString()));
				
				bookingFormApproveRequestList.add(bookingFormApproveRequest);
				actionBookingFormRequest.setBookingFormApproveRequest(bookingFormApproveRequestList);		
			} else if(request.getActionStr()!=null && request.getActionStr().equalsIgnoreCase("approve")) {

				actionBookingFormRequest.setSessionKey("E56524227389EF9C7DD92B58E530D2AC062E7045A229C89ADEE9F757EA97D80B");
				
				actionBookingFormRequest.setBookingId(customerPropertyDetailsPojo.get("SALESFORCE_BOOKING_ID")==null?"":customerPropertyDetailsPojo.get("SALESFORCE_BOOKING_ID").toString());
				//actionBookingFormRequest.setComments("Booking Re-Uploaded");
				actionBookingFormRequest.setActionStr("approve");
				List<BookingFormApproveRequest> bookingFormApproveRequestList = new ArrayList<>();
				BookingFormApproveRequest bookingFormApproveRequest = new BookingFormApproveRequest();
				bookingFormApproveRequest.setCustomerId(Long.valueOf(customerPropertyDetailsPojo.get("CUST_ID").toString()));
				bookingFormApproveRequest.setFlatBookingId(Long.valueOf(customerPropertyDetailsPojo.get("FLAT_BOOK_ID").toString()));
				bookingFormApproveRequest.setCustName(customerPropertyDetailsPojo.get("CUST_NAME").toString());
				bookingFormApproveRequest.setLeadId(Long.valueOf(customerPropertyDetailsPojo.get("SALES_TEAM_LEAD_ID").toString()));
				
				bookingFormApproveRequestList.add(bookingFormApproveRequest);
				actionBookingFormRequest.setBookingFormApproveRequest(bookingFormApproveRequestList);		
			
			} else {
				actionBookingFormRequest.setActionStr("Cancel");
				actionBookingFormRequest.setBookingformCanceledDate(new Timestamp(new Date().getTime()));
				actionBookingFormRequest.setBookingId(customerPropertyDetailsPojo.get("SALESFORCE_BOOKING_ID")==null?"":customerPropertyDetailsPojo.get("SALESFORCE_BOOKING_ID").toString());
				actionBookingFormRequest.setComments("Re-uploaded by Aniket, as amount was not matching with SF and uploaded trns, and removed old booking salesforce booking id");
				actionBookingFormRequest.setEmployeeName("Mahalakshmi");
				actionBookingFormRequest.setMerchantId("B97AAF841EA5C46372AF36E4A2F898D2");

			}
						
			actionBookingFormRequestList.add(actionBookingFormRequest);
		}

		for (BookingFormRequest cancelbookingFormRequest : actionBookingFormRequestList) {
			
			logger.info(cancelbookingFormRequest.getSiteName() +" - " + cancelbookingFormRequest.getBlockName() +" - " + cancelbookingFormRequest.getFloorName() +" - " + cancelbookingFormRequest.getFlatNo());

			cancelbookingFormRequest.setRequestUrl("actionBookingDetails");
			//http://129.154.74.18:8090/
			//result = (Result) restTemplate.postForObject("http://129.154.74.18:8090/SumadhuraGateway/employeeservice/bookingFormService/actionBookingDetails.spring", cancelbookingFormRequest, Result.class);
			result = (Result) restTemplate.postForObject("http://localhost:8888/SumadhuraGateway/employeeservice/bookingFormService/actionBookingDetails.spring", cancelbookingFormRequest, Result.class);
			logger.info(result);
			if(!result.getResponseCode().equals(200)) {
				logger.info("Failed to reject : "+result);	
			}
		}
		
		return null;
	}

	
	@Override
	public Map<String, Object> getAgreementTypesList(BookingFormRequest bookingFormRequest) throws Exception {
		Map<String,Object> response = new HashMap<>();
		double getMilestonePaidAmount = 0.0;
		//double totalPendingAmountOnFlat = 0.0;
		double amountToShowWelcomeMailButton = 0.0;
		boolean showWelcomeMailButton = false;
		Long showWelcomeMailButtonValFromDB = 0l;
		Properties prop = responceCodesUtil.getApplicationProperties();
		//bookingFormRequest.setStatusId(Status.ACTIVE.getStatus());
		List<FlatBookingPojo> flatBookPojoList = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequest);
		if(Util.isEmptyObject(flatBookPojoList)) {
			throw new EmployeeFinancialServiceException("Failed to load Agreement types, Please try again later.");
		}
		
		FlatBookingPojo flatBookPojo = flatBookPojoList.get(0);
		//loading list of agreement types
		List<Map<String, Object>> listOfAgreementTypes = bookingFormServiceDaoImpl.getAggrementTypesList(bookingFormRequest);
		//loading flat pad amount
		List<FinBookingFormAccountsPojo> milestonePaidList = employeeFinancialServiceDao.getFlatPaidAmountDetails(bookingFormRequest,Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()));
		//with minimum amount only welcome letter can generate
		String amount = prop.getProperty(bookingFormRequest.getSiteId()+"_MINIMUM_AMOUNT");
		if(Util.isNotEmptyObject(amount)) {
			amountToShowWelcomeMailButton = Double.valueOf(amount);
		}
		
		if (Util.isNotEmptyObject(milestonePaidList)) {
			for (FinBookingFormAccountsPojo pojo : milestonePaidList) {
				getMilestonePaidAmount += (pojo.getPaidAmount() == null ? 0 : pojo.getPaidAmount());
				//getMilestoneInitiatedBalanceAmount += getMilestoneInitiatedAmount - getMilestonePaidAmount;
				//totalPendingAmountOnFlat -= (pojo.getPaidAmount() == null ? 0 : pojo.getPaidAmount());
			}
		}

		//checking is already welcome letter sent to customer, if not then can send welcome letter, with showing button in single page
		showWelcomeMailButtonValFromDB = flatBookPojo.getIsWelcomeMailSend();
		if(Status.YES.getStatus().equals(showWelcomeMailButtonValFromDB)) {
			showWelcomeMailButton = false;
		} else if(getMilestonePaidAmount >= amountToShowWelcomeMailButton) {
			showWelcomeMailButton = true;
		}
		
		if(amountToShowWelcomeMailButton==0 || amountToShowWelcomeMailButton <=0) {
			showWelcomeMailButton = false;//if amount is zero, then not showing the welcome button
		}
		
		//if any side not contain in the array, then no need to show the welcome letter button
		if (!Arrays.asList(134l,124l,131l,126l,139l,114l,133l,130l).contains(bookingFormRequest.getSiteId())) {
			showWelcomeMailButton = false;
		}
		//Olympus,Sushantham Phase1, Aspire Aurum,Folium,Aspire Amber,Horizon,Sumadhura's Gardens by the Brook
		response.put("customerRegisteredInApp", null);
		response.put("listOfAgreementTypes", listOfAgreementTypes);
		response.put("showWelcomeMailButton", showWelcomeMailButton);
		response.put("MilestonePaidAmount", getMilestonePaidAmount);
		return response;
	}
	
	@Override
	public Result getCustomerFlatDocuments(MessengerRequest messengerRequest) throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.getCustomerFaltDocuments() *****");
		Result result=new Result();

		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		//employeeFinancialServiceInfo.setFlatIds(employeeFinancialTransactionInfo.getFlatIds());
		employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(messengerRequest.getFlatBookingId()));
		//employeeFinancialServiceInfo.setSiteId(messengerRequest.getSiteIds().get(0));
		employeeFinancialServiceInfo.setSiteIds(messengerRequest.getSiteIds());
		/* getting Customer Details */
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
		if(Util.isEmptyObject(customerPropertyDetailsPojoList)) {
			throw new EmployeeFinancialServiceException("Failed to generate allotment letter, No data found for customer.");
		}
		CustomerPropertyDetailsInfo customerPropertyDetailsInfo =  new EmployeeFinancialMapper().copyPropertiesFromCustomerPropertyDetailsPojoToCustomerPropertyDetailsInfo(customerPropertyDetailsPojoList.get(0));
		//List<FileInfo> fileInfoList = new ArrayList<>();
		long messengerId = 0l;
		//MessengerRequest messengerRequest = new MessengerRequest();
		//MessengerRequest messengerRequest = new MessengerRequest();
		messengerRequest.setSiteIds(Arrays.asList(customerPropertyDetailsInfo.getSiteId()));
		messengerRequest.setFlatIds(Arrays.asList(messengerRequest.getFlatId()));
		messengerRequest.setRequestUrl("getMessagesList");
		//messengerRequest.setRecipientId(bookingFormRequest.getEmpId());
		//messengerRequest.setRecipientType(MetadataId.EMPLOYEE.getId());
		Properties prop= responceCodesUtil.getApplicationProperties();
		String transactionReceiptPdfFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
		//String transactionGetReceiptFileURL = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		String path = transactionReceiptPdfFilePath + messengerRequest.getSiteIds().get(0) + "/" + messengerRequest.getFlatBookingId() + "/";
		List<FileInfo> listt = CarouselUtils.getWelcomeLetterFileInfosWRTlocation(path,"localserver",messengerRequest);
		if(Util.isNotEmptyObject(listt)) {
			result.setResponseObjList(listt);
			return result;
		}
		if(Util.isEmptyObject(listt)) {
			result.setResponseObjList(null);
			return result;	
		}
		
		MessengerResponce res = (MessengerResponce) messengerRestService.getMessagesList(messengerRequest);
		if(messengerRequest.getType()!=null && messengerRequest.getType().equals("customer")) {
			if(Util.isNotEmptyObject(res) && Util.isNotEmptyObject(res.getDepartmentwisemessengerDetailsPojos())) {
				res.getDepartmentwisemessengerDetailsPojos().get(0);
				for (MessengerDetailsPojo deptPojo : res.getDepartmentwisemessengerDetailsPojos()) {
				
					for (MessengerDetailsPojo pojo :deptPojo.getMessengerDetailsPojos()) {
						System.out.println(pojo.getSubject()+" "+pojo.getStatus());
						if(pojo.getStatus()!=null && pojo.getStatus().equals(Status.ACTIVE.getStatus())){
							if(pojo.getSubject().equalsIgnoreCase("Welcome to "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo())) {
								messengerId = pojo.getMessengerId();//If want to use existing msg		
							}
						}
					}
					
					/*if(pojo.getStatus()!=null && pojo.getStatus().equals(Status.ACTIVE.getStatus())){
						if(pojo.getSubject().equalsIgnoreCase("Welcome to "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo())) {
							messengerId = pojo.getMessengerId();//If want to use existing msg		
						}
					}*/
				}
				//MessengerDetailsPojo pojo = res.getMessengerDetailsPojos().get(0);
			}
		} else {
			if(Util.isNotEmptyObject(res) && Util.isNotEmptyObject(res.getMessengerDetailsPojos())) {
				for (MessengerDetailsPojo pojo : res.getMessengerDetailsPojos()) {
					if(pojo.getStatus()!=null && pojo.getStatus().equals(Status.ACTIVE.getStatus())){
						if(pojo.getSubject().equalsIgnoreCase("Welcome to "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo())) {
							messengerId = pojo.getMessengerId();//If want to use existing msg		
						}
					}
				}
				//MessengerDetailsPojo pojo = res.getMessengerDetailsPojos().get(0);
			}
		}
		

		messengerRequest.setMessengerId(messengerId);
		if(messengerId!=0) {
			MessengerResponce messengerResult = (MessengerResponce) messengerRestService.getChatDetails(messengerRequest);
			if(Util.isNotEmptyObject(messengerResult)) {
				 List<MessengerDetailsPojo> messengerDetailsPojos = messengerResult.getMessengerDetailsPojos();
				 if(Util.isNotEmptyObject(messengerDetailsPojos)) {
					 MessengerDetailsPojo detailsPojo = messengerDetailsPojos.get(0);
					 result.setResponseObjList(detailsPojo.getFileList());				 
				 }			 
			}		
		}
				
		/*FileInfo fileInfo = new FileInfo();
		fileInfo.setName("A1002 -Sumadhura Nandanam - Agreement Draft_4.pdf");
		fileInfo.setFilePath("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Financial_Transaction\\Allotment_Letter\\111/12/A1002 -Sumadhura Nandanam - Agreement Draft_4.pdf");
		fileInfo.setUrl("http://106.51.38.64:9999/images/sumadhura_projects_images/Financial_Transaction/Allotment_Letter/111/12/A1002 -Sumadhura Nandanam - Agreement Draft_4.pdf");
		fileInfoList.add(fileInfo);

		fileInfo = new FileInfo();
		fileInfo.setName("A1003 -Sumadhura Nandanam - Agreement Draft_4.pdf");
		fileInfo.setFilePath("D:\\CUSTOMERAPP_UAT\\images\\sumadhura_projects_images\\Financial_Transaction\\Allotment_Letter\\111/12/A1002 -Sumadhura Nandanam - Agreement Draft_4.pdf");
		fileInfo.setUrl("http://106.51.38.64:9999/images/sumadhura_projects_images/Financial_Transaction/Allotment_Letter/111/12/A1002 -Sumadhura Nandanam - Agreement Draft_4.pdf");
		fileInfoList.add(fileInfo);
		
		result.setResponseObjList(fileInfoList);*/
		return result;
	}
	
	/**
	 * This method send the welcome letter push notification to customer and banker mail also push notification
	 */
	@Override
	public Map<String, Object> generateWelcomeLetter(BookingFormRequest bookingFormRequest) throws Exception {
		
		List<DevicePojo> devicePojos = null;
		ReferedCustomer referedCustomer = new ReferedCustomer();
		boolean isThisPreview = bookingFormRequest.getActionStr().equals("Preview");
		referedCustomer.setFlatBookingId(bookingFormRequest.getFlatBookingId());
		devicePojos = null;//referredCustomerDaoImpl.getDeviceDetails(referedCustomer);
		
		if(Util.isEmptyObject(devicePojos) && !isThisPreview) {
			//response.put("customerRegisteredInApp", "Customer is not registered in app, Could not generate welcome mail..!");	
			throw new EmployeeFinancialServiceException("Customer is not registered in app, Could not generate welcome mail..!");
		} else {
			//response.put("customerRegisteredInApp", null);
		}
		
		logger.info("***** Control inside the BookingFormServiceImpl.generateWelcomeLetter() *****"+isThisPreview);
		BookingFormRequest bookingFormRequestCopy = new BookingFormRequest(); 
		BeanUtils.copyProperties(bookingFormRequest,bookingFormRequestCopy);
		bookingFormRequestCopy.setStatusId(Status.ACTIVE.getStatus());
		final List<FileInfo> fileInfoList = null;

		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		//employeeFinancialServiceInfo.setFlatIds(employeeFinancialTransactionInfo.getFlatIds());
		employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(bookingFormRequest.getFlatBookingId()));
		employeeFinancialServiceInfo.setSiteId(bookingFormRequest.getSiteId());
		employeeFinancialServiceInfo.setSiteIds(Arrays.asList(bookingFormRequest.getSiteId()));
		/* getting Customer Details */
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
		if(Util.isEmptyObject(customerPropertyDetailsPojoList)) {
			throw new EmployeeFinancialServiceException("Failed to generate Welcome letter, No data found for customer.");
		}
		final CustomerPropertyDetailsInfo customerPropertyDetailsInfo =  new EmployeeFinancialMapper().copyPropertiesFromCustomerPropertyDetailsPojoToCustomerPropertyDetailsInfo(customerPropertyDetailsPojoList.get(0));
		employeeFinancialServiceInfo.setCustomerPropertyDetailsInfo(customerPropertyDetailsInfo);
		employeeFinancialServiceInfo.setBookingFormId(bookingFormRequest.getFlatBookingId());
		
		//loading milestone,site office ,site address, and site other charges and terms and condition loading using these method
		final Map<String, Object> dataForGenerateAllotmentLetterHelper = getTheCustomerDetailsForLetter(employeeFinancialServiceInfo,bookingFormRequestCopy,customerPropertyDetailsInfo);
		
		long portNumber = bookingFormRequestCopy.getPortNumber();
		String folderFilePath = "";
		String folderFileUrl = "";

		Properties prop= responceCodesUtil.getApplicationProperties();
		
		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			folderFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			folderFileUrl = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			folderFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			folderFileUrl = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			folderFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			folderFileUrl = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			folderFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			folderFileUrl = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		}  else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			folderFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			folderFileUrl = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		}else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			folderFilePath = prop.getProperty("DOCUMENTS_FILE_DIRECTORY_PATH");
			folderFileUrl = prop.getProperty("DOCUMENTS_FILE_URL_PATH");
		}  else {
			logger.error("Path missing to save the File. Please contact to Support Team");
			throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
		}
		if (folderFilePath == null || folderFilePath.isEmpty()) {
			throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
		}
		if(isThisPreview) {//if this is preview
			dataForGenerateAllotmentLetterHelper.put("folderFilePath", folderFilePath+bookingFormRequest.getActionStr()+"\\");
			dataForGenerateAllotmentLetterHelper.put("folderFileUrl", folderFileUrl+bookingFormRequest.getActionStr()+"/");
			if(true) {
				//deleting previous zip files
				new PdfHelper().folderDeleteAndSubFolder(new File(folderFilePath+"/"+bookingFormRequest.getActionStr()+"/"),0,bookingFormRequest.getSiteId().toString());
			}
			/*employeeFinancialServiceInfo.setFilePath(folderFilePath+"/"+bookingFormRequest.getActionStr());
			employeeFinancialServiceInfo.setFileUrl(folderFileUrl+"/"+bookingFormRequest.getActionStr());*/
		} else {
			dataForGenerateAllotmentLetterHelper.put("folderFilePath", folderFilePath);
			dataForGenerateAllotmentLetterHelper.put("folderFileUrl", folderFileUrl);
			/*employeeFinancialServiceInfo.setFilePath(folderFilePath);
			employeeFinancialServiceInfo.setFileUrl(folderFileUrl);*/
		}
		//final Long siteId = customerPropertyDetailsInfo.getSiteId();
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<List<FileInfo>> future = executorService.submit(new Callable<List<FileInfo>>(){
		    public List<FileInfo> call() throws Exception {
			       return employeeFinancialHelper.generateAllotmentLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    }
		});
		//System.out.println("future.get() = " + future.get());
		dataForGenerateAllotmentLetterHelper.put("allotmentLetterFile", future.get());
		
		//we are not taking number of car parking value new booking or update booking time generate the cost breakup details in use
		generateCostBreakUpLetter(employeeFinancialServiceInfo,dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo,bookingFormRequestCopy);
		
		generateAgreementDraft(employeeFinancialServiceInfo,dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo,bookingFormRequestCopy);

		if(!isThisPreview) {
			sendWelcomeMailLetter(employeeFinancialServiceInfo,bookingFormRequestCopy,customerPropertyDetailsInfo,dataForGenerateAllotmentLetterHelper);
		}
		
		//result.setResponseObjList(customerDetails);
		dataForGenerateAllotmentLetterHelper.put("fileInfoList", fileInfoList);
		logger.info("***** Control end of the BookingFormServiceImpl.generateAgreementDraft() ***** "+bookingFormRequestCopy.getActionStr());
		return dataForGenerateAllotmentLetterHelper;
	}
	
	private void generateAgreementDraft(EmployeeFinancialServiceInfo employeeFinancialServiceInfo,
			final Map<String, Object> dataForGenerateAllotmentLetterHelper,final CustomerPropertyDetailsInfo customerPropertyDetailsInfo, BookingFormRequest bookingFormRequestCopy) 
					throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.generateAgreementDraft() *****");
		//List<FileInfo> fileInfoList = employeeFinancialHelper.generateAgreementDraftHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<List<FileInfo>> future = executorService.submit(new Callable<List<FileInfo>>(){
		    public List<FileInfo> call() throws Exception {
			    //for every site agreement need to create separate methods
		    	if (customerPropertyDetailsInfo.getSiteId().equals(126l)) {
					return employeeFinancialHelper.generateFoliumAgreementDraftHelper(dataForGenerateAllotmentLetterHelper, customerPropertyDetailsInfo);
				} else if (customerPropertyDetailsInfo.getSiteId().equals(134l)) {
					return employeeFinancialHelper.generateOlympusAgreementDraftHelper(dataForGenerateAllotmentLetterHelper, customerPropertyDetailsInfo);
				} else if (customerPropertyDetailsInfo.getSiteId().equals(130l)) {
					return employeeFinancialHelper.generateSST2AgreementDraftHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
				}else if (customerPropertyDetailsInfo.getSiteId().equals(139l)) {
					return employeeFinancialHelper.generateAmberAgreementDraftHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
				} else if (customerPropertyDetailsInfo.getSiteId().equals(114l)) {
					return employeeFinancialHelper.generateHorizonAgreementDraftHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
				} else if (customerPropertyDetailsInfo.getSiteId().equals(133l)) {
					employeeFinancialHelper.generateHorizonAgreementDraftHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
					return null;
				} else {
					return employeeFinancialHelper.generateAgreementDraftHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
				}
		    }
		});
		
		if(future.isDone()) {
			
		}
		
		dataForGenerateAllotmentLetterHelper.put("agreementDraftFile", future.get());
		//dataForGenerateAllotmentLetterHelper.put("agreementDraftFile", fileInfoList);
	}

	@Override
	public Map<String, Object> getWelcomeDocumentLetters(BookingFormRequest bookingFormRequest) {

		return null;
	}
	
	//@SuppressWarnings("unused")
	private void sendWelcomeMailLetter(EmployeeFinancialServiceInfo employeeFinancialServiceInfo,BookingFormRequest bookingFormRequestCopy, 
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, Map<String, Object> dataForGenerateAllotmentLetterHelper)  throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.sendWelcomeMailLetter() *****");
		List<EmployeePojo> employeePojos = bookingFormServiceDaoImpl.getEmployeeDetails(null,bookingFormRequestCopy.getEmpId());
		if(Util.isEmptyObject(employeePojos)) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, No data found for employee.");
		}
		List<FileInfo> fileInfoList = employeeFinancialHelper.welcomeMailLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo,employeePojos);
		
		@SuppressWarnings("unchecked")
		List<FileInfo> costBreakUpfileInfoList = (List<FileInfo>) dataForGenerateAllotmentLetterHelper.get("costBreakUpLetterFile");
		@SuppressWarnings("unchecked")
		List<FileInfo> allotmentLetterfileInfoList = (List<FileInfo>) dataForGenerateAllotmentLetterHelper.get("allotmentLetterFile");
		@SuppressWarnings("unchecked")
		List<FileInfo> agreementDraftFile = (List<FileInfo>) dataForGenerateAllotmentLetterHelper.get("agreementDraftFile");

		/*if(Util.isEmptyObject(costBreakUpfileInfoList)) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, No data found for cost break up letter.");
		} else if ( Util.isEmptyObject(allotmentLetterfileInfoList)) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, No data found for allotment letter.");
		}*/
		
		if(costBreakUpfileInfoList!=null) {
			fileInfoList.addAll(costBreakUpfileInfoList);
		}
		if(allotmentLetterfileInfoList!=null) {
			fileInfoList.addAll(allotmentLetterfileInfoList);
		}
		if(agreementDraftFile!=null) {
			fileInfoList.addAll(agreementDraftFile);
		}
		List<FileInfo> base64FileInfoList = convertImageFileToBase64(fileInfoList);
		
		long messengerId = 0l;
		MessengerRequest messengerRequest = new MessengerRequest();
		
		String emailContent = (String) dataForGenerateAllotmentLetterHelper.get("welcomeLetterEmailContent");	
		//MessengerRequest messengerRequest = new MessengerRequest();
		messengerRequest.setSiteIds(Arrays.asList(bookingFormRequestCopy.getSiteId()));
		messengerRequest.setFlatIds(Arrays.asList(bookingFormRequestCopy.getFlatId()));
		messengerRequest.setRequestUrl("getMessagesList");
		messengerRequest.setRecipientId(bookingFormRequestCopy.getEmpId());
		messengerRequest.setRecipientType(MetadataId.EMPLOYEE.getId());
		
		/*MessengerResponce res = (MessengerResponce) messengerRestService.getMessagesList(messengerRequest);
		if(Util.isNotEmptyObject(res) && Util.isNotEmptyObject(res.getMessengerDetailsPojos())) {
			messengerId = res.getMessengerDetailsPojos().get(0).getMessengerId();//If want to use existing msg 
		}*/

		messengerRequest.setRequestUrl("");
		messengerRequest.setRecipientId(null);
		messengerRequest.setRecipientType(null);
		
		messengerRequest.setFiles(base64FileInfoList);
		//messengerRequest.setFiles(fileInfoList);
		messengerRequest.setMessengerId(messengerId);
		messengerRequest.setFlatBookingId(bookingFormRequestCopy.getFlatBookingId());
		messengerRequest.setCreatedById(bookingFormRequestCopy.getEmpId());
		messengerRequest.setCreatedByType(MetadataId.EMPLOYEE.getId());
		messengerRequest.setEmployeeIds(Arrays.asList(bookingFormRequestCopy.getEmpId()));
		messengerRequest.setFlag(true);
		messengerRequest.setSendType(4l);//FLAT_BOOKING(4L,"FLAT_BOOKING"),
		messengerRequest.setSendTo(bookingFormRequestCopy.getFlatBookingId());
		
		messengerRequest.setSubject("Welcome to "+customerPropertyDetailsInfo.getSiteName()+" - "+customerPropertyDetailsInfo.getFlatNo());
		System.out.println(emailContent);
		emailContent = bookingFormMapper.getTheEmailContent(emailContent);
		//emailContent = Util.html2text(emailContent);
		messengerRequest.setMessage(emailContent);
		System.out.println(emailContent);
		messengerRequest.setRequestUrl("BookingFormGenerateWelcomeLetter");
		messengerRestService.chatSubmit(messengerRequest);
		int count = bookingFormServiceDaoImpl.updateWelcomeMailSendDetails(bookingFormRequestCopy);
		if (count == 0) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Failed to update welcome mail log.");
		}
		
		final CustomerInfo customerInfo2 = new CustomerInfo();
		customerInfo2.setSiteId(customerPropertyDetailsInfo.getSiteId());
		customerInfo2.setBlockId(customerPropertyDetailsInfo.getBlockId());
		customerInfo2.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId());
		customerInfo2.setSiteName(customerPropertyDetailsInfo.getSiteName());

		try {
			//sending bank home loan push notification
			sendMailToCustomerForBankLoan(customerPropertyDetailsInfo,customerInfo2);
		} catch (InSufficeientInputException e) {
			e.printStackTrace();
		}

		dataForGenerateAllotmentLetterHelper.put("welcomeLetterFile", fileInfoList);
	/*	
		Booking amount details:

			Eden Garden - 3,36,000/-

			Nandanam    - 3,36,000/-

			Sushantham Phase 1 - 3,15,000/

			Aspire Aurum - 1,00,000/-

			Horizon -  3,15,0000/-*/
	}

	private List<FileInfo> convertImageFileToBase64(List<FileInfo> fileInfoList) throws Exception {
		List<FileInfo> base64FileInfoList = new ArrayList<>();
		if(Util.isNotEmptyObject(fileInfoList)) {
			for (FileInfo fileInfo : fileInfoList) {
				if (fileInfo != null && fileInfo.getFilePath() != null) {
					FileInfo base64fileInfo = Base64FileUtil.loadFileAsBase64(fileInfo.getFilePath());
					base64FileInfoList.add(base64fileInfo);
				}
			}
		}
		return base64FileInfoList;
	}

	private Map<String, Object> getTheCustomerDetailsForLetter(final EmployeeFinancialServiceInfo employeeFinancialServiceInfo,
			final BookingFormRequest bookingFormRequestCopy, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		//EmployeeFinancialMapper financialMapper = new EmployeeFinancialMapper();
		CurrencyUtil currencyUtil = new CurrencyUtil(); 
		FinancialProjectMileStoneInfo finProjDemandNoteInfo = new FinancialProjectMileStoneInfo();
		double getMilestonePaidAmount = 0.0;
		double getMilestonePayAmount = 0.0;
		double amountDemanded = 0.0;//demand note generated amount -paid amount =amount demanded
		double totalPendingAmountOnFlat = 0.0;
		double schemeGSTPercentage = 0.0;
		/*CustomerBookingInfo customerBookingInfo = new CustomerBookingInfo();
		List<Customer> customerDetailsList = new ArrayList<>();*/
		List<Co_ApplicantInfo> coApplicantDetailsList = new ArrayList<>();
		List<AddressInfo> siteAddressInfoList = null;
		List<AddressInfo> customerAddressInfoList = null;
		List<FinProjectAccountResponse> finProjectAccountResponseList = null;
		List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos = null;
		ProfessionalInfo applicantProfessionalInfo = null;
		List<ProfessionalInfo> coApplicantProfessionalInfo = null;
		List<EmployeeDetailsMailPojo> empList =null;
		String custBookInfoPhoneNo="";
		List<Long> demandNoteGeneratedOrNot = new ArrayList<Long>();
		//final CyclicBarrier barrier = new CyclicBarrier(4);
		
		CustomerBookingFormInfo customerBookingFormInfo = getFlatCustomerAndCoAppBookingDetails(bookingFormRequestCopy);
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Future<List<FlatBookingInfo>> futureLoadUnitDetails = executorService.submit(new Callable<List<FlatBookingInfo>>(){
		    public List<FlatBookingInfo> call() throws Exception {
		    	return loadUnitDetails(bookingFormRequestCopy);
		    }
		});
		
		/*Future<List<MileStoneInfo>> futureLoadFinancialDetails = executorService.submit(new Callable<List<MileStoneInfo>>(){
		    public List<MileStoneInfo> call() throws Exception {
		    	return employeeFinancialService.getFinancialMilestoneDetails(employeeFinancialServiceInfo);
		    }
		});*/
		//These milestone details used in cost-break-up letter and agreement draft
		List<MileStoneInfo> futureLoadFinancialDetails = employeeFinancialService.getFinancialMilestoneDetails(employeeFinancialServiceInfo);
		
		//getCustomerDetailsByPanCardorPassport
		//List<CustomerPojo> customerPojoLists = bookingFormServiceDaoImpl.getCustomer(bookingFormRequestCopy);
		//List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequestCopy);
		//load POI Holder details
		List<PoaHolderPojo> poHolderPojoList = bookingFormServiceDaoImpl.getPoaHolder(0l,bookingFormRequestCopy,"generateWelcomeLetter");
		
		/*List<Co_ApplicantPojo> co_ApplicantPojos = null;
		co_ApplicantPojos = bookingFormServiceDaoImpl.getCo_Applicant(bookingFormRequestCopy);*/
		finProjDemandNoteInfo.setMilestoneDate(customerPropertyDetailsInfo.getBookingDate());
		finProjDemandNoteInfo.setSiteId(customerPropertyDetailsInfo.getSiteId());
		customerPropertyDetailsInfo.setActionUrl("All");
		customerPropertyDetailsInfo.setStatusId(Status.ACTIVE.getStatus());
		/* getting Co_Applicant Details */
		List<CoApplicantPojo> coApplicantPojoList = employeeFinancialServiceDao.getCoApplicantDetails(customerPropertyDetailsInfo);
		if(Util.isNotEmptyObject(coApplicantPojoList)) {
			System.out.println(coApplicantPojoList.get(0));
			CoApplicantPojo po = coApplicantPojoList.get(0);
			System.out.println( po.getNameprefix());
			System.out.println(po.getFirstName()); 
			System.out.println(po.getRelationWith()); 
			System.out.println(po.getRelationNamePrefix());
			System.out.println(po.getRelationName());
			System.out.println( po.getAdharId()); 
			System.out.println(po.getAge());  
			System.out.println(po.getPancard());

			Co_ApplicantInfo ico = customerBookingFormInfo.getCoApplicentDetails().get(0).getCo_ApplicantInfo();
			System.out.println(ico.getNamePrefix());
			System.out.println(ico.getFirstName());
			System.out.println(ico.getRelationWith());
			System.out.println(ico.getRelationNamePrefix());
			System.out.println(ico.getRelationName());
			System.out.println(ico.getAadharId());
			System.out.println(ico.getAge());
			System.out.println(ico.getPancard());
		}
		
		if(Util.isNotEmptyObject(customerBookingFormInfo.getCoApplicentDetails())) {
			coApplicantProfessionalInfo = new ArrayList<>();
			for (CoApplicentDetailsInfo pojo : customerBookingFormInfo.getCoApplicentDetails()) {
				coApplicantProfessionalInfo.add(pojo.getProfessionalInfo());
			}
		}
		
		/* setting customer full Name and Co app full name to customerPropertyDetailsInfo */
		setCustomerAndCoApplicantFullName(coApplicantPojoList, customerPropertyDetailsInfo);
		
		/* getting customer address details *///List<AddressInfo> 
		/*customerAddressInfoList = getCustomerAddressDetails(customerPropertyDetailsInfo);
		System.out.println(customerBookingFormInfo.getAddressInfos());
		System.out.println(customerAddressInfoList);*/

		customerAddressInfoList = customerBookingFormInfo.getAddressInfos();
		//Current project details
		List<SitePojo> siteDetails = bookingFormServiceDaoImpl.getSite(bookingFormRequestCopy);
		if(Util.isEmptyObject(siteDetails)) {
			logger.info("Failed to generate allotment letter, No data found for site.");
			throw new EmployeeFinancialServiceException("Failed to generate allotment letter, No data found for site.");
		}
		//flat booking details , like booking date agreement date
		if(Util.isEmptyObject(customerBookingFormInfo.getFlatBookingInfo())) {
			logger.info("Failed to generate allotment letter, No data found for site.");
			throw new EmployeeFinancialServiceException("Failed to generate allotment letter, No data found for Flat.");
		}
		
		//Long carParkingAllotedNumber  = bookingFormServiceDaoImpl.getNumberOfCarParkingAllotedNumber(bookingFormRequestCopy);
		//office details for the project
		List<OfficeDtlsPojo> officeDetailsPojoList = employeeFinancialServiceDao.getOfficeDetailsBySite(employeeFinancialServiceInfo);
		//loading site details , office and site details are not same
		List<AddressPojo> siteAddressPojoList = employeeFinancialServiceDao.getSiteAddressDetails(employeeFinancialServiceInfo);
		siteAddressInfoList = financialMapper.addressPojoListToAddressInfoList(siteAddressPojoList);		

		/* get site Account Bank Details */
		EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo1 = new EmployeeFinancialTransactionServiceInfo();
		employeeFinancialTransactionServiceInfo1.setSiteIds(employeeFinancialServiceInfo.getSiteIds());
		employeeFinancialTransactionServiceInfo1.setCondition(employeeFinancialServiceInfo.getCondition());
		//loading account details
		List<FinProjectAccountPojo> finProjectAccountPojoList = employeeFinancialServiceDao.getFinProjectAccountData(employeeFinancialTransactionServiceInfo1,customerPropertyDetailsInfo);
		finProjectAccountResponseList = financialMapper.finProjectAccountPojoListToFinProjectAccountResponseList(finProjectAccountPojoList);
		
		final List<SiteOtherChargesDetailsPojo> siteOtherChargesDetailsList =  new ArrayList<>();//bookingFormServiceDaoImpl.getTheSiteOtherChargesDetails(bookingFormRequestCopy);
		//loading paid amount for the booking
		List<FinBookingFormAccountsPojo> milestonePaidList = employeeFinancialServiceDao.getFlatPaidAmountDetails(bookingFormRequestCopy,Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()));
		//loading scheme details from FIN-SCHEME_MAPPING AND FLA_BOK_SCHME_TABLE
		List<FinSchemeTaxMappingPojo> schemeTaxDetails = employeeFinancialServiceDao.getFlatBookDetailsSchemeTaxDetails(customerPropertyDetailsInfo,finProjDemandNoteInfo);
		//loading Tax details
		List<FinPenaltyTaxPojo> taxDetailsList = employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.MAINTENANCE_CHARGE.getId(),MetadataId.LEGAL_COST.getId()));
		
		final List<MileStoneInfo> mileStones = new ArrayList<>();//employeeFinancialService.getFinancialMilestoneDetails(employeeFinancialServiceInfo);
		final List<FlatBookingInfo> flatBookingInfos = new ArrayList<>(); //loadUnitDetails(bookingFormRequestCopy);
		
		/*Thread siteOtherDetailsListThread = new Thread() {
			public void run() {
				try {*/
		//loading site other charges for cost-breakup letter and aggrement draft
					siteOtherChargesDetailsList.addAll(bookingFormServiceDaoImpl.getTheSiteOtherChargesDetails(bookingFormRequestCopy));
					/*barrier.await();
				} catch (Exception e) {
					logger.error("*** The Barrier is Broken ****"+e);
				}
			}
		};
		
		siteOtherDetailsListThread.start();*/
		/*if(!futureLoadFinancialDetails.isDone()) {
			logger.info("Failed to generate Cost breakup letter, No data found for Milestone.");
			throw new EmployeeFinancialServiceException("Failed to generate Cost breakup letter, No data found for Milestone.");
		}*/

		/*Thread milestoneListThread = new Thread() {
			public void run() {
				try {*/
					mileStones.addAll(futureLoadFinancialDetails);
					/*barrier.await();
				} catch (Exception e) {
					logger.error("*** The Barrier is Broken ****"+e);
				}
			}
		};
		milestoneListThread.start();*/
		
		/*Thread unitListThread = new Thread() {
			public void run() {
				try {*/
					flatBookingInfos.addAll(futureLoadUnitDetails.get());
					/*barrier.await();
				} catch (Exception e) {
					logger.error("*** The Barrier is Broken ****"+e);
				}
			}
		};
		unitListThread.start();*/
		
		/*try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			logger.error("*** The Barrier is Broken ****"+e);
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Customer details not found.");
		}*/
		
		if(Util.isEmptyObject(mileStones)) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Milestone details not found.");
		}
		
		if(Util.isEmptyObject(taxDetailsList)) {
			throw new EmployeeFinancialServiceException("GST on Maintenance charges not found, Please add the GST for Maintenance charges.");
		}

		if (Util.isEmptyObject(flatBookingInfos)) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Flat Cost details not found.");
		}
		
		if (Util.isEmptyObject(siteOtherChargesDetailsList)) {
				throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Site Other details not found.");
		}

		if (Util.isNotEmptyObject(milestonePaidList)) {
			//int index = 0;
			for (FinBookingFormAccountsPojo pojo : milestonePaidList) {//same data loading multiple times need to check
				getMilestonePaidAmount += (pojo.getPaidAmount() == null ? 0 : pojo.getPaidAmount());
				if(pojo.getMsStatusId().equals(6l))
				{
				  demandNoteGeneratedOrNot.add(6l);
				  getMilestonePayAmount+=(pojo.getPayAmount()==null?0:pojo.getPayAmount());
				}
				//getMilestoneInitiatedBalanceAmount += getMilestoneInitiatedAmount - getMilestonePaidAmount;
				totalPendingAmountOnFlat -= (pojo.getPaidAmount() == null ? 0 : pojo.getPaidAmount());				
			}
		}
		
		if (!demandNoteGeneratedOrNot.contains(6l) && "loanNocLetter".equals(bookingFormRequestCopy.getRequestUrl()) ) {
				throw new EmployeeFinancialServiceException("Failed to generate Banker NOC letter,Demand note not genetated.");
		}
		EmployeeFinancialTransactionServiceInfo tansactionServiceInfo = new EmployeeFinancialTransactionServiceInfo();
		tansactionServiceInfo.setSiteId(customerPropertyDetailsInfo.getSiteId());
		tansactionServiceInfo.setCondition("GetCustomerDataForBooking");//
		tansactionServiceInfo.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		
		if (Util.isEmptyObject(transactionEntryDetailsInfos)) {
			transactionEntryDetailsInfos = getFinancialTransactions(tansactionServiceInfo);
		}
		
		if (Util.isNotEmptyObject(schemeTaxDetails)) {
			FinSchemeTaxMappingPojo finSchemeTaxMappingPojo = schemeTaxDetails.get(0);
			customerPropertyDetailsInfo.setFinSchemeName(finSchemeTaxMappingPojo.getFinSchemeName());
			customerPropertyDetailsInfo.setFinSchemeId(finSchemeTaxMappingPojo.getFinSchemeId());
			customerPropertyDetailsInfo.setFinSchemeType(finSchemeTaxMappingPojo.getFinSchemeType());
			schemeGSTPercentage = finSchemeTaxMappingPojo.getPercentageValue();
		} else {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Scheme details not found.");	
		}
		/*List<Customer> customerDetailsList = new ArrayList<>();
		List<Co_ApplicantInfo> coApplicantDetailsList = new ArrayList<>();*/
		
		//customerDetailsList = bookingFormMapper.copyCustomerPojoListToInfoList(customerPojoLists);
		//coApplicantDetailsList = bookingFormMapper.copyCoApplicantPojoListToCoApplicantInfoList(coApplicantPojoList);
		
		if(Util.isNotEmptyObject(customerBookingFormInfo.getCoApplicentDetails())) {
			coApplicantDetailsList = Arrays.asList(customerBookingFormInfo.getCoApplicentDetails().get(0).getCo_ApplicantInfo());	
		} else {
			coApplicantDetailsList = new ArrayList<>();
		}
		customerBookingFormInfo.getFlatBookingInfo();
		bookingFormMapper.assignDefaultValues(customerBookingFormInfo);
		Map<String, Object> customerDetails = bookingFormMapper.getCustomerDetails(customerPropertyDetailsInfo,customerAddressInfoList,customerBookingFormInfo,bookingFormRequestCopy,poHolderPojoList);
		//Map<String, Object> customerCoApplicantDetails = bookingFormMapper.getCustomerCoApplicantDetails(customerPropertyDetailsInfo,coApplicantPojoList,bookingFormRequestCopy);
		Map<String, Object> customerUnitDetails = bookingFormMapper.getCustomerFlatDetails(flatBookingInfos,customerBookingFormInfo);
		/*if(Util.isNotEmptyObject(carParkingAllotedNumber)) {
			customerUnitDetails.put("noOfCarParkingsInWord",new NumberToWord().convertNumberToWords(carParkingAllotedNumber)+ "("+carParkingAllotedNumber+") car parking spaces");	
		} else {
			customerUnitDetails.put("noOfCarParkingsInWord","-");
		}*/
		
		
		
		try {
			CustomerDetailsPojo customerDetailsPojo = new CustomerDetailsPojo();
			customerDetailsPojo.setSiteId(customerPropertyDetailsInfo.getSiteId());
			 empList = bookingFormServiceDaoImpl
					.getEmployeeDetails(customerDetailsPojo);
		} catch (Exception e) {
			logger.error(" crm sales employee Details not found",e);
		}
		
		/*bvr*/
		FlatBookingInfo flatBookingInfo = flatBookingInfos.get(0);
		FlatCostInfo flatCostInfo = flatBookingInfo.getFlatCost();
		double totalAgreementCost=flatCostInfo.getTotalCost()==null?0.0:flatCostInfo.getTotalCost();
		double totalPendingBalanceAmount=totalAgreementCost-getMilestonePaidAmount;
		String strGetMilestonePaidAmount =currencyUtil.convertUstoInFormat(BigDecimal.valueOf( Double.valueOf(getMilestonePaidAmount)).setScale(roundingModeSize, roundingMode).toString());
		String strPendingBalanceAmount =currencyUtil.convertUstoInFormat(BigDecimal.valueOf( Double.valueOf(totalPendingBalanceAmount)).setScale(roundingModeSize, roundingMode).toString());
		amountDemanded=getMilestonePayAmount-getMilestonePaidAmount;
		String strAmountDemanded =currencyUtil.convertUstoInFormat(BigDecimal.valueOf( Double.valueOf(amountDemanded)).setScale(roundingModeSize, roundingMode).toString());
		/*bvr*/
		if (customerBookingFormInfo.getFlatBookingInfo().getCustomerLoanBank() != null) {
			customerUnitDetails.put("customerLoanBank", customerBookingFormInfo.getFlatBookingInfo().getCustomerLoanBank() );
		}else
		{
			customerUnitDetails.put("customerLoanBank", "");
		}
		
		if(Util.isNotEmptyObject(customerBookingFormInfo.getCustomerBookingInfo().getPhoneNo())) {
		  custBookInfoPhoneNo=customerBookingFormInfo.getCustomerBookingInfo().getPhoneNo();
		}
		
		
		//customerUnitDetails.put("carParkingAllotedNumber",Util.isEmptyObject(carParkingAllotedNumber) ? "-" : carParkingAllotedNumber);
		//customerDetails.put("carParkingAllotedNumber",Util.isEmptyObject(carParkingAllotedNumber) ? "-" : carParkingAllotedNumber);
		customerDetails.put("RERA",siteDetails.get(0).getRera()==null?"-":siteDetails.get(0).getRera());
		customerDetails.put("applicationNumber",Util.isEmptyObject(customerBookingFormInfo.getFlatBookingInfo().getApplicationNumber()) ? "-" : customerBookingFormInfo.getFlatBookingInfo().getApplicationNumber());
		customerDetails.put("totalMilestonePaidAmount",getMilestonePaidAmount);
		customerDetails.put("totalPendingAmountOnFlat",totalPendingAmountOnFlat);
		customerDetails.put("noOfCustomersIncludedInBooking",coApplicantDetailsList.size());
		customerDetails.put("schemeGSTPercentage",schemeGSTPercentage);
		customerDetails.put("strPendingBalanceAmount", strPendingBalanceAmount.replaceAll("\u00A0", ""));
		customerDetails.put("strtotalMilestonePaidAmount", strGetMilestonePaidAmount.replaceAll("\u00A0", ""));
		customerDetails.put("custBookInfoPhoneNo", custBookInfoPhoneNo);
		customerDetails.put("strAmountDemanded", strAmountDemanded.replaceAll("\u00A0", ""));
		
		System.out.println(customerUnitDetails);
		System.out.println("customerDetails "+customerDetails);
		
		Map<String, Object> dataForGenerateAllotmentLetterHelper = new HashMap<>();
		dataForGenerateAllotmentLetterHelper.put("customerDetails", customerDetails);
		dataForGenerateAllotmentLetterHelper.put("customerUnitDetails", customerUnitDetails);
		
		dataForGenerateAllotmentLetterHelper.put("firstApplicantDetails", Arrays.asList(customerBookingFormInfo.getCustomerInfo()));
		dataForGenerateAllotmentLetterHelper.put("coApplicantDetails", coApplicantDetailsList);
		applicantProfessionalInfo = customerBookingFormInfo.getProfessionalInfo();
		
		dataForGenerateAllotmentLetterHelper.put("firstApplicantProfessionalDetails", Arrays.asList(applicantProfessionalInfo));
		dataForGenerateAllotmentLetterHelper.put("coApplicantProfessionalDetails", coApplicantProfessionalInfo);
		
		dataForGenerateAllotmentLetterHelper.put("flatBookingInfoList", flatBookingInfos);
		dataForGenerateAllotmentLetterHelper.put("taxDetailsList", taxDetailsList);
		
		dataForGenerateAllotmentLetterHelper.put("siteOtherChargesDetailsList", siteOtherChargesDetailsList);
		dataForGenerateAllotmentLetterHelper.put("customerRegisteredinApp", null);
		dataForGenerateAllotmentLetterHelper.put("mileStones", mileStones);
		dataForGenerateAllotmentLetterHelper.put("transactionEntryDetailsInfos", transactionEntryDetailsInfos);
		dataForGenerateAllotmentLetterHelper.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForGenerateAllotmentLetterHelper.put("siteAddressInfoList", siteAddressInfoList);
		dataForGenerateAllotmentLetterHelper.put("finProjectAccountResponseList", finProjectAccountResponseList);
		dataForGenerateAllotmentLetterHelper.put("finProjectAccountResponseList", finProjectAccountResponseList);
		dataForGenerateAllotmentLetterHelper.put("empployeeDetails", empList);
		return dataForGenerateAllotmentLetterHelper;
	}

	private List<FinTransactionEntryDetailsInfo> getFinancialTransactions(EmployeeFinancialTransactionServiceInfo tansactionServiceInfo) {
		List<FinTransactionEntryPojo> finTransactionEntryPojoLists = null;
		List<FinTransactionSetOffPojo> transactionEntryPojoPaymentSetOffLists = null;
		boolean isThisPrincipalTransaction = false;
		FinTransactionEntryPojo finTransactionEntryPojo = null;
		finTransactionEntryPojoLists = employeeFinancialServiceDao.getMisPendingTransactions(tansactionServiceInfo);
		//for (FinTransactionEntryPojo finTransactionEntryPojo : finTransactionEntryPojoLists) {
		Iterator<FinTransactionEntryPojo> iterator = finTransactionEntryPojoLists.iterator();
		for (Iterator<FinTransactionEntryPojo> transactionIterator = iterator; transactionIterator.hasNext();) {
			finTransactionEntryPojo =  transactionIterator.next();
			//index = 0;//avoiding other than principal trasaction's
			if(finTransactionEntryPojo.getTransactionTypeId().equals(FinTransactionType.INTEREST_WAIVER.getId())) {
				finTransactionEntryPojo.setTransactionPaymentSetOff(FinTransactionType.INTEREST_WAIVER.getName());
				continue;
			}
			
			transactionEntryPojoPaymentSetOffLists = employeeFinancialServiceDao.getTransactionPaymenSetOffTypes(finTransactionEntryPojo);

			for (FinTransactionSetOffPojo setOffPojo : transactionEntryPojoPaymentSetOffLists) {
				long setOffType = setOffPojo.getSetOffType();
				String setOffTypeName = setOffPojo.getSetOffTypeName();
				String payemntSetOff = EmployeeFinancialServiceImpl.getConvenienceNameFromMetaData(setOffTypeName);
				if(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId().equals(setOffType)) {
					isThisPrincipalTransaction = true;
					finTransactionEntryPojo.setTransactionAmount(setOffPojo.getSetOffAmount());
					break;
				} else {
					isThisPrincipalTransaction = false;
				}
				System.out.println(payemntSetOff+" "+setOffTypeName+" "+setOffType);
			}
			if(!isThisPrincipalTransaction) {
				//removing payment set off other than milestones
				transactionIterator.remove();
			}
		}//finTransactionEntryPojoLists
		
		return financialMapper.copyTransactionEntryPojoToInfoPojoList(finTransactionEntryPojoLists);
	}

	public void generateCostBreakUpLetter(EmployeeFinancialServiceInfo serviceInfo,final Map<String, Object> dataForGenerateAllotmentLetterHelper,
			final CustomerPropertyDetailsInfo customerPropertyDetailsInfo, BookingFormRequest bookingFormRequestCopy) throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.generateCostBreakUpLetter() *****");
		
		List<DynamicKeyValueInfo> termsAndConditions = bookingFormServiceDaoImpl.getTheTermsAndConditions(bookingFormRequestCopy,customerPropertyDetailsInfo);
		
		if (Util.isEmptyObject(termsAndConditions)) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Terms and condition not found.");
		}
		
		dataForGenerateAllotmentLetterHelper.put("termsAndConditions", termsAndConditions);
		//for Horizon and Garden by brook same template has been used, remaining also u can check
//		ExecutorService executorService = Executors.newFixedThreadPool(2);
//		Future<List<FileInfo>> future = executorService.submit(new Callable<List<FileInfo>>(){
//		    public List<FileInfo> call() throws Exception {
//		    	//NOTE = for some sites same cost break up used, if doing changes in cost break up template then need to cross check other sites also
//		    	if (customerPropertyDetailsInfo.getSiteId().equals(126l)) {
//		    		return employeeFinancialHelper.generateFoliumCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
//		    	}else if (customerPropertyDetailsInfo.getSiteId().equals(114l)) {
//		    		return employeeFinancialHelper.generateHorizonCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
//		    	}  else if (customerPropertyDetailsInfo.getSiteId().equals(134l)) {
//		    		return employeeFinancialHelper.generateOlympusCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
//		    	} else if (customerPropertyDetailsInfo.getSiteId().equals(139l)) {
//		    		return employeeFinancialHelper.generateCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
//		    	} else if (customerPropertyDetailsInfo.getSiteId().equals(133l)) {
//		    		return employeeFinancialHelper.generateGardenByTheBrookBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
//		    	} else {
//			        return employeeFinancialHelper.generateCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
//		    	}
//		    }
//		});

		    List<FileInfo> fileInfo=null;
		    	//NOTE = for some sites same cost break up used, if doing changes in cost break up template then need to cross check other sites also
		    	if (customerPropertyDetailsInfo.getSiteId().equals(126l)) {
		    		fileInfo= employeeFinancialHelper.generateFoliumCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    	}else if (customerPropertyDetailsInfo.getSiteId().equals(114l)) {
		    		fileInfo=employeeFinancialHelper.generateHorizonCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    	}  else if (customerPropertyDetailsInfo.getSiteId().equals(134l)) {
		    		fileInfo= employeeFinancialHelper.generateOlympusCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    	} else if (customerPropertyDetailsInfo.getSiteId().equals(139l)) {
		    		fileInfo= employeeFinancialHelper.generateCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    	} else if (customerPropertyDetailsInfo.getSiteId().equals(133l)) {
		    		fileInfo= employeeFinancialHelper.generateGardenByTheBrookBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    	} else if (customerPropertyDetailsInfo.getSiteId().equals(130l)) {
		    		fileInfo= employeeFinancialHelper.generateSST2BreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    	} else {
		    		fileInfo= employeeFinancialHelper.generateCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		    	}
		//List<FileInfo> fileInfoList = employeeFinancialHelper.generateCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		
		dataForGenerateAllotmentLetterHelper.put("costBreakUpLetterFile", fileInfo);
	}
	
	private void setCustomerAndCoApplicantFullName(List<CoApplicantPojo> coApplicantPojoList, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		//StringBuffer custFullName = new StringBuffer("");
		StringBuffer coAppFullName = new StringBuffer("");
		StringBuffer coAppFullName1 = new StringBuffer("");
		String namePrefix = null;
		String firstName= null;
		String lastName= null;
		for (CoApplicantPojo coApplicantPojo : coApplicantPojoList) {
			coAppFullName.append(Util.isNotEmptyObject(coApplicantPojo.getCoAppFullName())?coApplicantPojo.getCoAppFullName(): "").append("\n");
			
			namePrefix = Util.isNotEmptyObject(coApplicantPojo.getNameprefix())?coApplicantPojo.getNameprefix(): "";
			firstName = Util.isNotEmptyObject(coApplicantPojo.getFirstName())?coApplicantPojo.getFirstName(): "";
			lastName = Util.isNotEmptyObject(coApplicantPojo.getLastName())?coApplicantPojo.getLastName(): "";
			
			coAppFullName1.append(namePrefix).append(" ").append(firstName).append(" ").append(lastName).append("\n");
			
		}
		
		System.out.println("coAppFullName "+coAppFullName);
		System.out.println("coAppFullName "+coAppFullName1);
		
		if(Util.isNotEmptyObject(coApplicantPojoList)) {
			customerPropertyDetailsInfo.setCustFullName(Util.isNotEmptyObject(coApplicantPojoList.get(0).getCustFullName())?coApplicantPojoList.get(0).getCustFullName():customerPropertyDetailsInfo.getCustomerName());
			customerPropertyDetailsInfo.setCoAppFullName(coAppFullName.toString());	
		}
	}
	
	/**
	 * 
	 * @param customerPropertyDetailsInfo
	 * @return List<AddressInfo>
	 */
	public List<AddressInfo> getCustomerAddressDetails(CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		logger.info("*** The control is inside the getCustomerAddressDetails in EmployeeFinancialServiceImpl ***");
		/* preparing booking form request */
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setCustomerId(customerPropertyDetailsInfo.getCustomerId());
		bookingFormRequest.setFlatBookingId(customerPropertyDetailsInfo.getFlatBookingId());
		/* getting CustBookInfoPojo List */
		List<CustBookInfoPojo> custBookInfoPojos = bookingFormServiceDaoImpl.getCustBookInfo(bookingFormRequest);
		List<AddressInfo> customerAddressInfos = new ArrayList<>();
		if (Util.isNotEmptyObject(custBookInfoPojos) ) {
			/* mapping CustBookInfoPojo List to customerBookingInfo */
			CustomerBookingInfo customerBookingInfo = bookingFormMapper.custBookInfoPojoToCustomerBookingInfo(custBookInfoPojos.get(0));
			/* preparing booking form request */
			bookingFormRequest.setMetadataId(MetadataId.CUSTOMER.getId());
			bookingFormRequest.setCustBookInfoId(customerBookingInfo.getCustBookInfoId());
			List<AddressMappingPojo> addressMappingPojos = null;
			/* getting AddressMappingPojos */
			addressMappingPojos = bookingFormServiceDaoImpl.getAddressMapping(bookingFormRequest);
			if (Util.isNotEmptyObject(addressMappingPojos)) {
				for (AddressMappingPojo addressMappingPojo : addressMappingPojos) {
					/* getting AddressPojos by AddressMappingPojos */
					List<AddressPojo> addrPojos = bookingFormServiceDaoImpl.getAddress(addressMappingPojo.getAddressId());
					List<AddressInfo> addressInfos = bookingFormMapper.addressPojosTocustomerAddressInfos(addrPojos,addressMappingPojo);
					customerAddressInfos.addAll(addressInfos);
				}
			}else {
				/* adding empty AddressInfos List */
				customerAddressInfos.add(new AddressInfo());
			}
		}else {
			/* adding empty AddressInfos List */
			customerAddressInfos.add(new AddressInfo());
		}
		return customerAddressInfos;
	}

	private void sendPushNotifications(CustomerBookingFormInfo customerBookingFormInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo, 
			List<FlatBookingPojo> flatBookPojo, CustomerPropertyDetailsPojo flatData) throws Exception {
		logger.info("*** The control is inside the sendPushNotifications in EmployeeFinancialServiceImpl ***");
		if (Util.isNotEmptyObject(flatBookPojo)) {
			// registration date if not empty and (this is booking approval or already
			// booking in active state), then generate the final demand note
			if (customerBookingFormInfo != null && customerBookingFormInfo.getFlatBookingInfo().getRegistrationDate()!=null  && (customerBookingFormInfo.getCustomerAppBookingApproval()|| Status.ACTIVE.getStatus().equals(flatBookPojo.get(0).getStatusId()))) {
				/* here sending project update */
				if (Util.isEmptyObject(flatBookPojo.get(0).getRegistrationDate())) {
					/* creating SiteLevelNotifyRequestDTO and setting whatever values required. */
					SiteLevelNotifyRequestDTO notificationRequest = new SiteLevelNotifyRequestDTO();
					notificationRequest.setFlatIds(Arrays.asList(flatData.getFlatId()));
					notificationRequest.setSiteIds(Arrays.asList(flatData.getSiteId()));
					notificationRequest.setEmployeeId(MetadataId.SYSTEM.getId());
					notificationRequest.setRequestAction("BookingForm");
					//notificationRestService.sendProjectNotificationsForApprovals(notificationRequest);
				}
				System.out.println("flatBookPojo.get(0).getRegistrationDate() "+ flatBookPojo.get(0).getRegistrationDate());
			}
			if (customerBookingFormInfo != null && customerBookingFormInfo.getFlatBookingInfo().getHandingOverDate() != null && (customerBookingFormInfo.getCustomerAppBookingApproval()|| Status.ACTIVE.getStatus().equals(flatBookPojo.get(0).getStatusId()))) {
				/* here sending project update */
				if (Util.isEmptyObject(flatBookPojo.get(0).getHandingOverDate())) {
					/* creating SiteLevelNotifyRequestDTO and setting whatever values required. */
					SiteLevelNotifyRequestDTO notificationRequest = new SiteLevelNotifyRequestDTO();
					notificationRequest.setFlatIds(Arrays.asList(flatData.getFlatId()));
					notificationRequest.setSiteIds(Arrays.asList(flatData.getSiteId()));
					notificationRequest.setEmployeeId(MetadataId.SYSTEM.getId());
					notificationRequest.setRequestAction("BookingForm");
					notificationRequest.setRequestPurpose("handOverDateNotify");
					//notificationRestService.sendProjectNotificationsForApprovals(notificationRequest);
				}
				System.out.println("flatBookPojo.get(0).getHandingOverDate() "+ flatBookPojo.get(0).getHandingOverDate());
			}
		}
	
	}

	@Override
	public Map<String, Object> testingUrl(BookingFormRequest bookingFormRequest) throws Exception {
		
		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(bookingFormRequest.getFlatBookingId()));
		employeeFinancialServiceInfo.setSiteId(bookingFormRequest.getSiteId());
		employeeFinancialServiceInfo.setSiteIds(Arrays.asList(bookingFormRequest.getSiteId()));
		
		/* getting Customer Details */
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
		if (Util.isEmptyObject(customerPropertyDetailsPojoList)) {
			throw new EmployeeFinancialServiceException("Failed to generate NOC letter, No data found for customer.");
		}
		CustomerPropertyDetailsPojo pojo = customerPropertyDetailsPojoList.get(0);
		final CustomerInfo customerInfo2 = new CustomerInfo();
		customerInfo2.setSiteId(pojo.getSiteId());
		customerInfo2.setBlockId(pojo.getBlockId());
		customerInfo2.setFlatBookingId(pojo.getFlatBookingId());
		customerInfo2.setSiteName(pojo.getSiteName());
	
		//sending home loan mail
		logger.info("***** Control inside the BookingFormServiceImpl.testing() homes loan if condition2 *****"+customerInfo2);
		sendMailToBankerForLoan(customerInfo2,pojo);
		//replyMailToBankerForLoan(pojo); 
		//CustomerPropertyDetailsInfo customerPropertyDetailsInfo =  financialMapper.copyPropertiesFromCustomerPropertyDetailsPojoToCustomerPropertyDetailsInfo(pojo);
		//sendMailToBankerForLoan(customerInfo2);
		//sendMailToCustomerForBankLoan(customerPropertyDetailsInfo, customerInfo2);
		
		//return nocFinancialValidation(bookingFormRequest,null,null);
		return  null;
	}
	
	@SuppressWarnings("unused")
	public Map<String, Object> nocFinancialValidation(BookingFormRequest bookingFormRequest, final CustomerPropertyDetailsInfo customerPropertyDetailsInfo, final List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList) throws Exception {
		Map<String,Object> nocValidatonResp = new HashMap<String,Object>();
		Properties prop = responceCodesUtil.getApplicationProperties();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

		EmployeeFinancialServiceInfo testinginfo = new EmployeeFinancialServiceInfo();
		testinginfo.setRequestUrl("ValidateAmoutForNOC");
		testinginfo.setBookingFormId(bookingFormRequest.getFlatBookingId());
		testinginfo.setFlatId(bookingFormRequest.getFlatId());
		testinginfo.setSiteId(bookingFormRequest.getSiteId());
		
		List<Long> listOfNocValidatedDetails = new ArrayList<>();//this arrat will hold validated details from code
		List<Long> listOf_NocCheckList_Details_To_Valiate = new ArrayList<>();//this array will hold DB Details to Validate
		List<String> listOfErrorMsg = new ArrayList<>();
		List<String> listOfExcessErrorMsg = new ArrayList<>();
		List<FinBookingFormExcessAmountPojo> listOfExcessAmount = null;
		
		//holding all the type of payment *Paid amount
		Map<Long,Double> paidAmountsetOffTypeWiseList = new HashMap<>();
		
		//holding all the type of payment *Paying amount
		Map<Long,Double> payAmountSetOffTypeWiseList = new HashMap<>();
		
		
		//holding all the *type of payment
		Map<Long,Boolean> setOffTypeWiseAmountClearedList = new HashMap<>();
		Timestamp bookingDate = null;
		
		
		Timestamp dueDate = null;
		Timestamp paidDate = null;
		//Double totalMilestonePaidAmount = 0.0;
		double totalInterestAmount = 0.0;
		
		Double totalAgreementAmount = 0.0;
		//Double totalInterestPayAmount = 0.0;
		Double isPrincipalAmountPaid= 0.0d;
		Double isLegalAmountPaid = 0.0d;
		Double isModificationAmountPaid = 0.0d;
		Double isPenaltyAmountPaid = 0.0d;
		Double isCorpusAmountPaid = 0.0d;
		Double isMaintainanceAmountPaid = 0.0d;
		Double isKhataBifurcationAmountPaid = 0.0d;
		
		boolean isPrincipalAmountCleared = true;
		boolean isLegalAmountCleared = true;
		boolean isModificationAmountCleared = true;
		boolean isPenaltyAmountCleared = true;
		boolean isCorpusAmountCleared = true;
		boolean isMaintainanceAmountCleared = true;
		boolean isMaintainanceAmountFound = false;
		boolean isKhataBifurcationAmountCleared = true;
		boolean isInterestShouldBeCalculated = false;//if this value true, then need to check interest amount for noc
		double amountToBeIgnored = Util.isEmptyObject(prop.getProperty("NOC_AMOUNT_TO_BE_IGNORED"))?5.0d:Double.valueOf(prop.getProperty("NOC_AMOUNT_TO_BE_IGNORED"));
		
		boolean dataNotFound = true;
		List<Object> interestData = null;
		SiteOtherChargesDetailsPojo siteOtherChargesDetailsPojo = null;
		FinPenaltyTaxPojo finTaxPojo = null;
		List<NOCDetailsPojo> NOC_CheckListSiteWise = null;
		List<FinPenaltyTaxPojo> taxDetailsList = new ArrayList<>();
		List<SiteOtherChargesDetailsPojo> siteOtherChargesDetailsList = null;
		FinancialProjectMileStoneInfo finProjDemandNoteInfo = null;
		if(customerPropertyDetailsInfo!=null) {
			totalAgreementAmount = customerPropertyDetailsInfo.getTotalAgreementCost();
			bookingDate = customerPropertyDetailsInfo.getBookingDate();
		} else {
			 throw new EmployeeFinancialServiceException("Customer details not found, Failed to generate modification invoice.");
			/*customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(testinginfo);
			if(Util.isNotEmptyObject(customerPropertyDetailsPojoList)) {
				customerPropertyDetailsInfo = (CustomerPropertyDetailsInfo) financialMapper.copyPropertiesFromInfoBeanToPojoBean(customerPropertyDetailsPojoList.get(0), CustomerPropertyDetailsInfo.class);
				totalAgreementAmount = customerPropertyDetailsInfo.getTotalAgreementCost();
			} else {
				 throw new EmployeeFinancialServiceException("Customer details not found, Failed to generate modification invoice.");
			}*/
		}
		
		Timestamp startDate = null;
		Timestamp endDateForMaintencenDate = null;		
		if(Util.isNotEmptyObject(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_START_DATE"))) {
			startDate = new Timestamp(format.parse(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_START_DATE")).getTime());
		}
		if(Util.isNotEmptyObject(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_END_DATE"))) {
			endDateForMaintencenDate = new Timestamp(format.parse(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_END_DATE")).getTime());
		}
/*		if(customerPropertyDetailsInfo.getSiteId().equals(111l)) {
			startDate = new Timestamp(format.parse(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_START_DATE")).getTime());
			endDateForMaintencenDate = new Timestamp(format.parse(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_END_DATE")).getTime());
		} else if(customerPropertyDetailsInfo.getSiteId().equals(107l)) {
			startDate = new Timestamp(format.parse(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_START_DATE")).getTime());
			endDateForMaintencenDate = new Timestamp(format.parse(prop.getProperty(customerPropertyDetailsInfo.getSiteId()+"_MAINTENANCE_CHARGES_END_DATE")).getTime());
		}
*/		
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<List<Object>> future = executorService.submit(new Callable<List<Object>>(){
		    public List<Object> call() throws Exception {//loading interest amount
		    	return employeeFinancialService.loadInterestAmount(customerPropertyDetailsPojoList,customerPropertyDetailsInfo);
		    }
		});
		
		finProjDemandNoteInfo = new FinancialProjectMileStoneInfo();
		finProjDemandNoteInfo.setSiteId(customerPropertyDetailsInfo.getSiteId());
		finProjDemandNoteInfo.setStatusId(Status.ACTIVE.getStatus());
		finProjDemandNoteInfo.setCondition("NOC_Validation");
		//loading NOC checklist details site wise
		NOC_CheckListSiteWise = bookingFormServiceDaoImpl.getNOCCheckListDetails(bookingFormRequest);
		//loading tax percentage for the given payment types
		//taxDetailsList = employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId(),MetadataId.MAINTENANCE_CHARGE.getId(),MetadataId.LEGAL_COST.getId()));
		//loading site wise charges details , like corpus funds, maintenance charges, flat khata and other
		siteOtherChargesDetailsList = bookingFormServiceDaoImpl.getTheSiteOtherChargesDetails(bookingFormRequest);
		//loading flat master details, like sbua, carpet area and other details
		List<FlatDetailsPojo> flatDetailsPojos = bookingFormServiceDaoImpl.getFlatDetails(bookingFormRequest);
		
		if(Util.isEmptyObject(NOC_CheckListSiteWise)) {
			throw new EmployeeFinancialServiceException("NOC Checklist is found empty, Cananot generate NOC letter.");
		}
		if(Util.isEmptyObject(flatDetailsPojos)) {
			throw new EmployeeFinancialServiceException("Failed to load flat details, Cananot generate NOC letter.");
		}
		
		for (NOCDetailsPojo nocDetailsPojo : NOC_CheckListSiteWise) {
			if(nocDetailsPojo!=null) {//taking all the checklist type in one list obejct
				listOf_NocCheckList_Details_To_Valiate.add(nocDetailsPojo.getMataDataTypeId());
			}
		}
		
		final FinBookingFormAccountsInfo bookingFormAccountsInfo = new FinBookingFormAccountsInfo();
		bookingFormAccountsInfo.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		bookingFormAccountsInfo.setStatusId(Status.ACTIVE.getStatus());//adding type of payment
		bookingFormAccountsInfo.setTypes(Arrays.asList(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId(),MetadataId.CORPUS_FUND.getId(),MetadataId.MAINTENANCE_CHARGE.getId(),MetadataId.MODIFICATION_COST.getId(),MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(),MetadataId.LEGAL_COST.getId(),MetadataId.FIN_PENALTY.getId()));
		bookingFormAccountsInfo.setPaymentStatusList(Arrays.asList(Status.PENDING.getStatus(), Status.INPROGRESS.getStatus(), Status.PAID.getStatus()));
		bookingFormAccountsInfo.setCondition("NOC Validation");
		
		/*Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId(),MetadataId.PRINCIPAL_AMOUNT.getId(), MetadataId.TDS.getId(),
				MetadataId.MODIFICATION_COST.getId(), MetadataId.LEGAL_COST.getId(), MetadataId.FIN_PENALTY.getId());*/
		//boolean isAmountCleared = true;
		//here loading all the Milestone Payment Details using customer FlatBookingId
		List<FinBookingFormAccountsPojo> bookingFormAccountsPojoLists = null;
		//bookingFormAccountsPojoLists = employeeFinancialSerbookingFormAccountsInfoviceDao.getFinBookingFormAccountsData();

		//interestData = employeeFinancialService.loadInterestAmount(customerPropertyDetailsPojoList,customerPropertyDetailsInfo);
		
		/*Double sumOfPaidAmount = 0.0;
		for(FinBookingFormAccountsPojo bookingFormAccountsPojo : bookingFormAccountsPojoLists) {
			siteOtherChargesDetailsPojo = null;
			System.out.println("BookingFormServiceImpl.nocFinancialValidation() "+bookingFormAccountsPojo.getType());
			if(Util.isNotEmptyObject(bookingFormAccountsPojo)) {
				if(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId().equals(bookingFormAccountsPojo.getType())) {
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					sumOfPaidAmount = employeeFinancialService.roundOffAmount(sumOfPaidAmount+paidAmount);
				}
			}
		}*/

		
/*		if(true) {
			sumOfPaidAmount = employeeFinancialService.roundOffAmount(sumOfPaidAmount);
			totalAgreementAmount = employeeFinancialService.roundOffAmount(totalAgreementAmount);		

			
			if(sumOfPaidAmount<totalAgreementAmount) {
				//listOfErrorMsg.add("Principal data : "+employeeFinancialService.convertUstoInFormat(totalAgreementAmount-sumOfPaidAmount));
			} else  if(employeeFinancialService.roundOffAmount(sumOfPaidAmount) != employeeFinancialService.roundOffAmount(totalAgreementAmount)) {
				//listOfErrorMsg.add("Customer Principal data is not cleared...!, Could not proceed for NOC.");
			} else {
				//calculating interest amount
				try {

					//if set off type contains penalty id, then calculate interest amount
					EmployeeFinancialServiceInfo serviceInfo1 = new EmployeeFinancialServiceInfo();
					Timestamp date = new Timestamp(new Date().getTime());
					serviceInfo1.setPortNumber(bookingFormRequest.getPortNumber());
					serviceInfo1.setEmpId(bookingFormRequest.getEmpId());
					serviceInfo1.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
					serviceInfo1.setBlockIds(Arrays.asList(customerPropertyDetailsInfo.getBlockId()));
					serviceInfo1.setSiteId(customerPropertyDetailsInfo.getSiteId());
					
					//loading last milestone object for calculating interest amount
					List<FinancialProjectMileStonePojo> lastMilestoneObjectPojoList = employeeFinancialServiceDao.getLastMilestoneDetails(serviceInfo1, Status.ACTIVE,"saveFinancialTransaction");
					if(Util.isNotEmptyObject(lastMilestoneObjectPojoList)) {
						FinancialProjectMileStoneInfo mileStoneInfo = financialMapper.copyPropertiesFromInfoBeanToPojoBean(lastMilestoneObjectPojoList.get(0), FinancialProjectMileStoneInfo.class);
						mileStoneInfo.setDemandNoteDate(date);
						mileStoneInfo.setMileStoneDueDate(date);
						mileStoneInfo.setInterestCalculationUptoDate(date);
						
						mileStoneInfo.setSiteId(customerPropertyDetailsInfo.getSiteId());
						serviceInfo1.setFinancialProjectMileStoneRequests(Arrays.asList(mileStoneInfo));	
						serviceInfo1.setBlockIds(null);//making blockids empty, other wise it will calculate all flats interest
						serviceInfo1.setRequestUrl("saveFinancialTransaction");
						//calculating interest upto date, if this is interest paid transaction
						employeeFinancialService.interestCalculateAndConstructTransactionObject(serviceInfo1);
					}
				
				} catch(Exception ex) {
					ex.printStackTrace();
				}				
			}
		}
*/		
		bookingFormAccountsPojoLists = employeeFinancialServiceDao.getFinBookingFormAccountsData(bookingFormAccountsInfo);
		for(FinBookingFormAccountsPojo bookingFormAccountsPojo : bookingFormAccountsPojoLists) {
			siteOtherChargesDetailsPojo = null;
			System.out.println("BookingFormServiceImpl.nocFinancialValidation() "+bookingFormAccountsPojo.getType());
			if(Util.isNotEmptyObject(bookingFormAccountsPojo)) {
				//setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
				//isAmountCleared = setOffTypeWiseAmountClearedList.get(bookingFormAccountsPojo.getType())==null?true:setOffTypeWiseAmountClearedList.get(bookingFormAccountsPojo.getType());
				
/*				if(MetadataId.FIN_PENALTY.getId().equals(bookingFormAccountsPojo.getType()) && isAmountCleared) {
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double interestWaiverAdjAmount = bookingFormAccountsPojo.getInterestWaiverAdjAmount();
					paidAmount += interestWaiverAdjAmount;//adding interest waiver amount in paid amount, as we have nuliified the interest amount using the waiver amount
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					//isPenaltyAmountPaid+=paidAmount;

					Double mappaidAmount = setOffTypeWisePaidAmountList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:mappaidAmount;
					setOffTypeWisePaidAmountList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);

					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						//isPenaltyAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						break;
					} else if(balanceAmount == 0.0) {
						
					} else {
						//isPenaltyAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				} else if(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId().equals(bookingFormAccountsPojo.getType()) && isAmountCleared) {
					//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()  and MetadataId.PRINCIPAL.getId() is trating as same 
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					//isPrincipalAmountPaid += paidAmount;
					
					Double mappaidAmount = setOffTypeWisePaidAmountList.get(MetadataId.PRINCIPAL_AMOUNT.getId());
					mappaidAmount = mappaidAmount == null?0d:mappaidAmount;
					setOffTypeWisePaidAmountList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), mappaidAmount+paidAmount);
					setOffTypeWisePaidAmountList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);
					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						//isPrincipalAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), false);
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						break;
					} else if(balanceAmount == 0.0) {
						
					} else {
						setOffTypeWiseAmountClearedList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), false);
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//isPrincipalAmountCleared = false;
					}
				} else if(isAmountCleared) {
					//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() is the princiapl amount MetadataId.PRINCIPAL.getId() 
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					
					
					Double mappaidAmount = setOffTypeWisePaidAmountList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:mappaidAmount;
					setOffTypeWisePaidAmountList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);
					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						//isPrincipalAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						break;
					} else if(balanceAmount == 0.0) {
						
					} else {
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//isPrincipalAmountCleared = false;
					}
				}
*/
				if(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId().equals(bookingFormAccountsPojo.getType())) {// && isPrincipalAmountCleared
					//MetadataId.FIN_BOOKING_FORM_MILESTONES.getId() is the princiapl amount MetadataId.PRINCIPAL.getId()
					setOffTypeWiseAmountClearedList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), true);
					setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					isPrincipalAmountPaid = employeeFinancialService.roundOffAmount(isPrincipalAmountPaid+paidAmount);
					
					Double mappaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.PRINCIPAL_AMOUNT.getId());
					mappaidAmount = mappaidAmount == null?0d:employeeFinancialService.roundOffAmount(mappaidAmount);;
					paidAmountsetOffTypeWiseList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), mappaidAmount+paidAmount);
					paidAmountsetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);
					
					Double mapPayAmount = payAmountSetOffTypeWiseList.get(MetadataId.PRINCIPAL_AMOUNT.getId());
					mapPayAmount = mapPayAmount == null?0d:employeeFinancialService.roundOffAmount(mapPayAmount);
					payAmountSetOffTypeWiseList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), mapPayAmount+payAmount);
					payAmountSetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mapPayAmount+payAmount);
					
					dueDate = bookingFormAccountsPojo.getMileStoneDueDate()==null?null:TimeUtil.removeTimePartFromTimeStamp1(bookingFormAccountsPojo.getMileStoneDueDate());
					paidDate = bookingFormAccountsPojo.getPaidDate()==null?null:TimeUtil.removeTimePartFromTimeStamp1(bookingFormAccountsPojo.getPaidDate());

					//if paid date is after the due date, then interest has to be calculated already
					if(dueDate!=null && paidDate!=null && paidDate.after(dueDate) && !isInterestShouldBeCalculated) {
						isInterestShouldBeCalculated = true;//if this condition execute, means customer amount paid after the due date of milestone
						//,so interest should be calculated
					} else if(dueDate != null && paidDate == null) {
						//if due date is not null, means demand note is generated,
						//and paid date is null then it means amount is not paid for milestone
						isInterestShouldBeCalculated = true;
					}
					if(dueDate == null && Util.isEmptyObject(listOfErrorMsg)) {//these code is very important, bcoz demand note should be generated for Interest amount calculation
						listOfErrorMsg.add("Customer demand note has not been generated, please generate demand note...");
					}
					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						isPrincipalAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), false);
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//break;
					} else if(balanceAmount == 0.0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						isPrincipalAmountCleared = true;//means the principal amount is cleared for NOC
					} else if(balanceAmount == 0.0 && !Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						
						//throw new EmployeeFinancialServiceException(listOfErrorMsg);
					} else {
						isPrincipalAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(MetadataId.PRINCIPAL_AMOUNT.getId(), false);
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				} else if(MetadataId.LEGAL_COST.getId().equals(bookingFormAccountsPojo.getType())) {// && isLegalAmountCleared
					setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
					
					//loading the maintenance charges gst percentage on the creation date of maintenance charge record
					//EmployeeFinancialServiceImpl.saveFlatKhataBifurcationAndOtherCharges()
					finProjDemandNoteInfo.setMilestoneDate(bookingFormAccountsPojo.getCreatedDate());
					taxDetailsList.addAll(employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.LEGAL_COST.getId())));
					
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					isLegalAmountPaid = employeeFinancialService.roundOffAmount(isLegalAmountPaid+paidAmount);
					
					Double mappaidAmount = paidAmountsetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:employeeFinancialService.roundOffAmount(mappaidAmount);;
					paidAmountsetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);
					
					Double mapPayAmount = payAmountSetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mapPayAmount = mapPayAmount == null?0d:employeeFinancialService.roundOffAmount(mapPayAmount);
					payAmountSetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mapPayAmount+payAmount);

					//siteOtherChargesDetailsPojo = getSiteOtherChargesDetailsObject(siteOtherChargesDetailsList,MetadataId.LEGAL_COST.getId());
					
					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						isLegalAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//break;
					} else if(balanceAmount == 0.0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						isLegalAmountCleared = true;//means the principal amount is cleared for NOC
					} else if(balanceAmount == 0.0) {
						
					} else {
						isLegalAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				} else if(MetadataId.MODIFICATION_COST.getId().equals(bookingFormAccountsPojo.getType())) {// && isModificationAmountCleared
					setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					isModificationAmountPaid = employeeFinancialService.roundOffAmount(isModificationAmountPaid+paidAmount);
					
					Double mappaidAmount = paidAmountsetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:employeeFinancialService.roundOffAmount(mappaidAmount);;
					paidAmountsetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);
					
					Double mapPayAmount = payAmountSetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mapPayAmount = mapPayAmount == null?0d:employeeFinancialService.roundOffAmount(mapPayAmount);
					payAmountSetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mapPayAmount+payAmount);

					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						isModificationAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//break;
					} else if(balanceAmount == 0.0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						isModificationAmountCleared = true;//means the principal amount is cleared for NOC
					} else if(balanceAmount == 0.0) {
						
					} else {
						isModificationAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				} else if(MetadataId.FIN_PENALTY.getId().equals(bookingFormAccountsPojo.getType())) {// && isPenaltyAmountCleared
					setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double interestWaiverAdjAmount = bookingFormAccountsPojo.getInterestWaiverAdjAmount();
					paidAmount += interestWaiverAdjAmount;//adding interest waiver amount in paid amount, as we have nuliified the interest amount using the waiver amount
					
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					isPenaltyAmountPaid = employeeFinancialService.roundOffAmount(isPenaltyAmountPaid+paidAmount);
					//totalInterestPayAmount = employeeFinancialService.roundOffAmount(totalInterestPayAmount+payAmount);
					
					Double mappaidAmount = paidAmountsetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:employeeFinancialService.roundOffAmount(mappaidAmount);;
					paidAmountsetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);

					Double mapPayAmount = payAmountSetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mapPayAmount = mapPayAmount == null?0d:employeeFinancialService.roundOffAmount(mapPayAmount);
					payAmountSetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mapPayAmount+payAmount);

					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						isPenaltyAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//break;
					} else if(balanceAmount == 0.0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						isPenaltyAmountCleared = true;//means the principal amount is cleared for NOC
					} else if(balanceAmount == 0.0) {
						
					} else {
						isPenaltyAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				} else if(MetadataId.MAINTENANCE_CHARGE.getId().equals(bookingFormAccountsPojo.getType())) {// && isMaintainanceAmountCleared
					setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
					isMaintainanceAmountFound = true;
					//loading tax percentage for the given payment types
					//loading the maintenance charges gst percentage on the creation date of maintenance charge record
					//EmployeeFinancialServiceImpl.saveMaintenance_Charges()
					finProjDemandNoteInfo.setMilestoneDate(bookingFormAccountsPojo.getCreatedDate());
					taxDetailsList.addAll(employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.MAINTENANCE_CHARGE.getId())));

					
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					isMaintainanceAmountPaid = employeeFinancialService.roundOffAmount(isMaintainanceAmountPaid+paidAmount);

					Double mappaidAmount = paidAmountsetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:employeeFinancialService.roundOffAmount(mappaidAmount);;
					paidAmountsetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);

					Double mapPayAmount = payAmountSetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mapPayAmount = mapPayAmount == null?0d:employeeFinancialService.roundOffAmount(mapPayAmount);
					payAmountSetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mapPayAmount+payAmount);

					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						isMaintainanceAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//break;
					} else if(balanceAmount == 0.0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						isMaintainanceAmountCleared = true;//means the principal amount is cleared for NOC
					} else if(balanceAmount == 0.0) {
						
					} else {
						isMaintainanceAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				} else if(MetadataId.CORPUS_FUND.getId().equals(bookingFormAccountsPojo.getType())) {//&& isCorpusAmountCleared
					setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					isCorpusAmountPaid = employeeFinancialService.roundOffAmount(isCorpusAmountPaid+paidAmount);
					
					Double mappaidAmount = paidAmountsetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:employeeFinancialService.roundOffAmount(mappaidAmount);;
					paidAmountsetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);

					Double mapPayAmount = payAmountSetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mapPayAmount = mapPayAmount == null?0d:employeeFinancialService.roundOffAmount(mapPayAmount);
					payAmountSetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mapPayAmount+payAmount);

					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						isCorpusAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//break;
					} else if(balanceAmount == 0.0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						isCorpusAmountCleared = true;//means the principal amount is cleared for NOC
					} else if(balanceAmount == 0.0) {
						
					} else {
						isCorpusAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				} else if(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId().equals(bookingFormAccountsPojo.getType())) {// && isKhataBifurcationAmountCleared
					setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), true);
					
					//loading the maintenance charges gst percentage on the creation date of maintenance charge record
					//EmployeeFinancialServiceImpl.saveFlatKhataBifurcationAndOtherCharges()
					finProjDemandNoteInfo.setMilestoneDate(bookingFormAccountsPojo.getCreatedDate());
					
					taxDetailsList.addAll(employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId())));

					double payAmount = bookingFormAccountsPojo.getPayAmount();
					double paidAmount = bookingFormAccountsPojo.getPaidAmount();
					double balanceAmount = employeeFinancialService.roundOffAmount(payAmount-paidAmount);
					isKhataBifurcationAmountPaid = employeeFinancialService.roundOffAmount(isKhataBifurcationAmountPaid+paidAmount);
					
					Double mappaidAmount = paidAmountsetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mappaidAmount = mappaidAmount == null?0d:employeeFinancialService.roundOffAmount(mappaidAmount);;
					paidAmountsetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mappaidAmount+paidAmount);

					Double mapPayAmount = payAmountSetOffTypeWiseList.get(bookingFormAccountsPojo.getType());
					mapPayAmount = mapPayAmount == null?0d:employeeFinancialService.roundOffAmount(mapPayAmount);
					payAmountSetOffTypeWiseList.put(bookingFormAccountsPojo.getType(), mapPayAmount+payAmount);

					if(balanceAmount > 0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						// if Balance amount is greater than zero, means amount is not paid fully
						isKhataBifurcationAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
						//break;
					} else if(balanceAmount == 0.0 && Status.PAID.getStatus().equals(bookingFormAccountsPojo.getPaymentStatus())) {
						isKhataBifurcationAmountCleared = true;//means the principal amount is cleared for NOC
					} else if(balanceAmount == 0.0) {
						
					} else {
						isKhataBifurcationAmountCleared = false;
						setOffTypeWiseAmountClearedList.put(bookingFormAccountsPojo.getType(), false);
					}
				}
			} else {
				dataNotFound = false;
			}
		}
		
		/*if(!setOffTypeWiseAmountClearedList.containsKey(MetadataId.PRINCIPAL_AMOUNT.getId())) {
			//throw new EmployeeFinancialServiceException("Customer Principal data is not cleared...!, Could not proceed for NOC.");
			listOfErrorMsg.add("Customer Principal data is not cleared...!, Could not proceed for NOC.");
		} else if(setOffTypeWiseAmountClearedList.get(MetadataId.PRINCIPAL_AMOUNT.getId()) == false) {
			listOfErrorMsg.add("Customer Principal data is not cleared...!, Could not proceed for NOC.");
		}  else */
		if(true){//listOfNocDetails_To_Valiate.contains(MetadataId.PRINCIPAL_AMOUNT.getId())
			Double pricipalPaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.PRINCIPAL_AMOUNT.getId());
			pricipalPaidAmount = pricipalPaidAmount==null?0d:pricipalPaidAmount;
			Double BalanceAmountInExcessAmount = 0.0;
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			//customerPropertyDetailsInfo2.setType(MetadataId.FIN_PENALTY.getId());
			customerPropertyDetailsInfo2.setCondition(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString());
			//loading excess amount if any
			listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId()));

			BalanceAmountInExcessAmount = getTheExcessAmountBalance(listOfExcessAmount);
			pricipalPaidAmount += BalanceAmountInExcessAmount;//adding excess amount in paid amount
			
			pricipalPaidAmount = employeeFinancialService.roundOffAmount(pricipalPaidAmount);
			totalAgreementAmount = employeeFinancialService.roundOffAmount(totalAgreementAmount);		
			if(pricipalPaidAmount>totalAgreementAmount) {
				listOfExcessErrorMsg.add("Principal data : "+employeeFinancialService.convertUstoInFormat(pricipalPaidAmount-totalAgreementAmount));
				listOfNocValidatedDetails.add(MetadataId.PRINCIPAL_AMOUNT.getId());//if payment has excess amount, so don't have any objection for NOC
			} else if(pricipalPaidAmount<totalAgreementAmount && ((totalAgreementAmount-pricipalPaidAmount)>amountToBeIgnored)) {
				listOfErrorMsg.add("Principal data : "+employeeFinancialService.convertUstoInFormat(totalAgreementAmount-pricipalPaidAmount));
			}/* else  if(employeeFinancialService.roundOffAmount(pricipalPaidAmount) != employeeFinancialService.roundOffAmount(totalAgreementAmount)) {
				listOfErrorMsg.add("Customer Principal data is not cleared...!, Could not proceed for NOC.");
			}*/ else {//if everything is clear then this type validated
				listOfNocValidatedDetails.add(MetadataId.PRINCIPAL_AMOUNT.getId());
			}
		}

		//PRINCIPAL_AMOUNT,FIN_PENALTY,LEGAL_COST
	if(listOf_NocCheckList_Details_To_Valiate.contains(MetadataId.LEGAL_COST.getId())) {
		/*if(!setOffTypeWiseAmountClearedList.containsKey(MetadataId.LEGAL_COST.getId())) {
			//throw new EmployeeFinancialServiceException("Customer Legal data is not cleared...!, Could not proceed for NOC.");
			listOfErrorMsg.add("Customer Legal data is not cleared...!, Could not proceed for NOC.");
		} else if(setOffTypeWiseAmountClearedList.get(MetadataId.LEGAL_COST.getId()) == false) {
			listOfErrorMsg.add("Customer Legal data is not cleared...!, Could not proceed for NOC.");
		} else*/ 
		if(true) {
			Double legalPaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.LEGAL_COST.getId());
			legalPaidAmount = legalPaidAmount==null?0d:legalPaidAmount;
			Double totalLegalCostToPay = 0.0;
			Double flatLegalAndDocumentationChargesAmt = 0.0;
			Double flatLegalCostGstAmount = 0.0;
			Double BalanceAmountInExcessAmount = 0.0;
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			//customerPropertyDetailsInfo2.setType(MetadataId.FIN_PENALTY.getId());
			customerPropertyDetailsInfo2.setCondition(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString());
			//loading excess amount if any
			listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.LEGAL_COST.getId()));
			BalanceAmountInExcessAmount = getTheExcessAmountBalance(listOfExcessAmount);
			legalPaidAmount += BalanceAmountInExcessAmount;//adding excess amount in paid amount
			//if(listOfNocDetails_To_Valiate.contains(MetadataId.LEGAL_COST.getId())) {
				siteOtherChargesDetailsPojo = getSiteOtherChargesDetailsObject(siteOtherChargesDetailsList,MetadataId.LEGAL_COST.getId());
				finTaxPojo = getTaxPercentageDetailsObject(taxDetailsList,MetadataId.LEGAL_COST.getId());
				if (finTaxPojo == null) {
					finProjDemandNoteInfo.setCondition("");//not adding any condition, so taking current date gst percentages
					taxDetailsList.addAll(employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.LEGAL_COST.getId())));
					finTaxPojo = getTaxPercentageDetailsObject(taxDetailsList,MetadataId.LEGAL_COST.getId());
				}
				
				/*FinancialProjectMileStonePojo pojo = new FinancialProjectMileStonePojo();//need to load generate inovice time, gst percentage for legal charges 
				  employeeFinancialServiceDao.getLegalCostDetails(pojo);*/
				//taking principal amount from master tables
				if (MetadataId.LEGAL_COST.getId().equals(siteOtherChargesDetailsPojo.getMeteDataTypeId())) {
					//maintenanceAmount = other.getAmount();
					flatLegalAndDocumentationChargesAmt =  siteOtherChargesDetailsPojo.getAmount();
				}
				if (finTaxPojo == null) {
					throw new EmployeeFinancialServiceException(Arrays.asList("Please add legal charges GST percentage..."));
				}
				//calculating GST amount on principal amounr
				flatLegalCostGstAmount =  (flatLegalAndDocumentationChargesAmt * finTaxPojo.getPercentageValue()) / 100;
				totalLegalCostToPay = flatLegalAndDocumentationChargesAmt+flatLegalCostGstAmount;
				
				legalPaidAmount = employeeFinancialService.roundOffAmount(legalPaidAmount);
				totalLegalCostToPay = employeeFinancialService.roundOffAmount(totalLegalCostToPay);
				
				if(legalPaidAmount>totalLegalCostToPay) {
					listOfExcessErrorMsg.add("Legal Charges : "+employeeFinancialService.convertUstoInFormat(legalPaidAmount-totalLegalCostToPay));
					listOfNocValidatedDetails.add(MetadataId.LEGAL_COST.getId());//if payment has excess amount, so don't have any objection for NOC
				} else if(legalPaidAmount<totalLegalCostToPay && ((totalLegalCostToPay-legalPaidAmount)>amountToBeIgnored)) {
					listOfErrorMsg.add("Legal Charges : "+employeeFinancialService.convertUstoInFormat(totalLegalCostToPay-legalPaidAmount));
				}/* else  if(employeeFinancialService.roundOffAmount(legalPaidAmount) != employeeFinancialService.roundOffAmount(totalLegalCostToPay)) {
					listOfErrorMsg.add("Customer Legal data is not cleared...!, Could not proceed for NOC.");
				}*/ else {//if everything is clear then this type validated
					listOfNocValidatedDetails.add(MetadataId.LEGAL_COST.getId());
				}
			//}
			//listOfNocValidatedDetails.add(MetadataId.LEGAL_COST.getId());
			//FLAT_LEGAL_AND_DOCUMENTATION_CHARGES and LEGAL_COST Is Same only id got changed
		}
	} else {
		listOfNocValidatedDetails.add(MetadataId.LEGAL_COST.getId());
	}
	//PRINCIPAL_AMOUNT,FIN_PENALTY,LEGAL_COST,MAINTENANCE_CHARGE
	if(listOf_NocCheckList_Details_To_Valiate.contains(MetadataId.MAINTENANCE_CHARGE.getId())) {
		/*if(!setOffTypeWiseAmountClearedList.containsKey(MetadataId.MAINTENANCE_CHARGE.getId())) {
			listOfErrorMsg.add("Customer maintenance data is not cleared...!, Could not proceed for NOC.");
		} else if(setOffTypeWiseAmountClearedList.get(MetadataId.MAINTENANCE_CHARGE.getId()) == false) {
			listOfErrorMsg.add("Customer maintenance data is not cleared...!, Could not proceed for NOC.");
		} else {*/
		if(true) {
			Double maintainancePaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.MAINTENANCE_CHARGE.getId());
			double maintainancePayAmount = payAmountSetOffTypeWiseList.get(MetadataId.MAINTENANCE_CHARGE.getId())==null?0d:payAmountSetOffTypeWiseList.get(MetadataId.MAINTENANCE_CHARGE.getId());
			maintainancePaidAmount = maintainancePaidAmount==null?0d:maintainancePaidAmount;
			Double totalMaintainanceCostToPay = 0.0;
			Double flatMaintainanceAndDocumentationChargesAmt = 0.0;
			Double flatMaintainanceCostGstAmount = 0.0;
			Double BalanceAmountInExcessAmount = 0.0;
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			//customerPropertyDetailsInfo2.setType(MetadataId.FIN_PENALTY.getId());
			customerPropertyDetailsInfo2.setCondition(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString());
			//loading excess amount if any
			listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.MAINTENANCE_CHARGE.getId()));
			BalanceAmountInExcessAmount = getTheExcessAmountBalance(listOfExcessAmount);
			maintainancePaidAmount += BalanceAmountInExcessAmount;//adding excess amount in paid amount

				siteOtherChargesDetailsPojo = getSiteOtherChargesDetailsObject(siteOtherChargesDetailsList,MetadataId.MAINTENANCE_CHARGE.getId());
				finTaxPojo = getTaxPercentageDetailsObject(taxDetailsList,MetadataId.MAINTENANCE_CHARGE.getId());
				if (finTaxPojo == null) {
					finProjDemandNoteInfo.setCondition("");//not adding any condition, so taking current date gst percentages
					taxDetailsList.addAll(employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.MAINTENANCE_CHARGE.getId())));
					finTaxPojo = getTaxPercentageDetailsObject(taxDetailsList,MetadataId.MAINTENANCE_CHARGE.getId());
				}
								
				//taking principal amount from master tables
				if (MetadataId.MAINTENANCE_CHARGE.getId().equals(siteOtherChargesDetailsPojo.getMeteDataTypeId())) {
					//maintenanceAmount = other.getAmount();
					flatMaintainanceAndDocumentationChargesAmt = flatDetailsPojos.get(0).getSbua() * siteOtherChargesDetailsPojo.getAmount();
				}
				//30 march 2022
				if(finTaxPojo == null) {
					throw new EmployeeFinancialServiceException(Arrays.asList("Please add maintenance charges GST percentage..."));
				}
				//calculating GST amount on principal amount
				flatMaintainanceCostGstAmount =  (flatMaintainanceAndDocumentationChargesAmt * finTaxPojo.getPercentageValue()) / 100;
				totalMaintainanceCostToPay = flatMaintainanceAndDocumentationChargesAmt+flatMaintainanceCostGstAmount;
				
				maintainancePaidAmount = employeeFinancialService.roundOffAmount(maintainancePaidAmount);
				totalMaintainanceCostToPay = employeeFinancialService.roundOffAmount(totalMaintainanceCostToPay);
				
				if (startDate != null && endDateForMaintencenDate != null 
						&& (customerPropertyDetailsInfo.getSiteId().equals(111l) || customerPropertyDetailsInfo.getSiteId().equals(107l))) {
					//calculating maintenance amount based on booking date and end of the maintenance date
					if(bookingDate.after(startDate) || bookingDate.equals(startDate)) {//if booking date is after the start date then only enter to condition
						totalMaintainanceCostToPay = getTotalMaintainanceCostToPay(startDate,endDateForMaintencenDate,customerPropertyDetailsInfo,flatDetailsPojos,finTaxPojo,siteOtherChargesDetailsPojo)
								.get("totalMaintainanceCostToPay");
					}//46135.32
				}
				if(isMaintainanceAmountFound) {
					//taking DB amount if we have in Table
					if(maintainancePayAmount!=0 && maintainancePayAmount>0) {
						totalMaintainanceCostToPay = maintainancePayAmount;//46665.6 
					}
				}
				
				if(customerPropertyDetailsInfo.getSiteId().equals(107l)) {
					if(!"1".equals(customerPropertyDetailsInfo.getFlatSaleOwnerId())) {
						if(Util.isEmptyObject(customerPropertyDetailsInfo.getSaleDeedDate())) {
							listOfErrorMsg.add("Sale deed date found empty ");
						}
					}
				}
				
				if(maintainancePaidAmount>totalMaintainanceCostToPay) {
					listOfExcessErrorMsg.add("Maintenance Charges : "+employeeFinancialService.convertUstoInFormat(maintainancePaidAmount-totalMaintainanceCostToPay));
					listOfNocValidatedDetails.add(MetadataId.MAINTENANCE_CHARGE.getId());//if payment has excess amount, so don't have any objection for NOC generate
				} else if(maintainancePaidAmount<totalMaintainanceCostToPay && ((totalMaintainanceCostToPay-maintainancePaidAmount)>amountToBeIgnored)) {
					listOfErrorMsg.add("Maintenance Charges : "+employeeFinancialService.convertUstoInFormat(totalMaintainanceCostToPay-maintainancePaidAmount));
				}/* else  if(employeeFinancialService.roundOffAmount(maintainancePaidAmount) != employeeFinancialService.roundOffAmount(totalMaintainanceCostToPay)) {
					listOfErrorMsg.add("Customer maintenance data is not cleared...!, Could not proceed for NOC.");
				}*/ else {//if everything is clear then this type validated
					listOfNocValidatedDetails.add(MetadataId.MAINTENANCE_CHARGE.getId());
				}
		}
	} else {
		listOfNocValidatedDetails.add(MetadataId.MAINTENANCE_CHARGE.getId());
	}
	//PRINCIPAL_AMOUNT,FIN_PENALTY,LEGAL_COST,MAINTENANCE_CHARGE,CORPUS_FUND
	if(listOf_NocCheckList_Details_To_Valiate.contains(MetadataId.CORPUS_FUND.getId())) {
		/*if(!setOffTypeWiseAmountClearedList.containsKey(MetadataId.CORPUS_FUND.getId())) {
			listOfErrorMsg.add("Customer corpus data is not cleared...!, Could not proceed for NOC.");
		} else if(setOffTypeWiseAmountClearedList.get(MetadataId.CORPUS_FUND.getId()) == false) {
			listOfErrorMsg.add("Customer corpus data is not cleared...!, Could not proceed for NOC.");
		} else {*/
		if(true) {
			Double corpusPaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.CORPUS_FUND.getId());
			corpusPaidAmount = corpusPaidAmount==null?0d:corpusPaidAmount;
			Double totalcorpusPaidAmountCostToPay = 0.0;
			Double flatcorpusPaidAmountAndDocumentationChargesAmt = 0.0;
			Double flatcorpusPaidAmountCostGstAmount = 0.0;
			Double BalanceAmountInExcessAmount = 0.0;
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			//customerPropertyDetailsInfo2.setType(MetadataId.FIN_PENALTY.getId());
			customerPropertyDetailsInfo2.setCondition(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString());
			//loading excess amount if any
			listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.CORPUS_FUND.getId()));
			BalanceAmountInExcessAmount = getTheExcessAmountBalance(listOfExcessAmount);
			corpusPaidAmount += BalanceAmountInExcessAmount;//adding excess amount in paid amount

			siteOtherChargesDetailsPojo = getSiteOtherChargesDetailsObject(siteOtherChargesDetailsList,MetadataId.CORPUS_FUND.getId());
			finTaxPojo = getTaxPercentageDetailsObject(taxDetailsList,MetadataId.CORPUS_FUND.getId());
				
				//taking principal amount from master tables
				if (MetadataId.CORPUS_FUND.getId().equals(siteOtherChargesDetailsPojo.getMeteDataTypeId())) {
					//maintenanceAmount = other.getAmount();
					if(customerPropertyDetailsInfo.getSiteId().equals(131l)) {
						flatcorpusPaidAmountAndDocumentationChargesAmt = siteOtherChargesDetailsPojo.getAmount();
					} else {
						flatcorpusPaidAmountAndDocumentationChargesAmt = flatDetailsPojos.get(0).getSbua() * siteOtherChargesDetailsPojo.getAmount();
					}
				}
				
				/*if(finTaxPojo == null) {
					throw new EmployeeFinancialServiceException(Arrays.asList("Please add corpus charges GST percentage..."));
				}*/
				//calculating GST amount on principal amounr
				flatcorpusPaidAmountCostGstAmount =  0.0;//(flatMaintainanceAndDocumentationChargesAmt * finTaxPojo.getPercentageValue()) / 100;
				totalcorpusPaidAmountCostToPay = flatcorpusPaidAmountAndDocumentationChargesAmt+flatcorpusPaidAmountCostGstAmount;

				corpusPaidAmount = employeeFinancialService.roundOffAmount(corpusPaidAmount);
				totalcorpusPaidAmountCostToPay = employeeFinancialService.roundOffAmount(totalcorpusPaidAmountCostToPay);
				
				if(corpusPaidAmount>totalcorpusPaidAmountCostToPay) {
					listOfExcessErrorMsg.add("Corpus Fund : "+employeeFinancialService.convertUstoInFormat(corpusPaidAmount-totalcorpusPaidAmountCostToPay));
					listOfNocValidatedDetails.add(MetadataId.CORPUS_FUND.getId());//if payment has excess amount, so don't have any objection for NOC generation
				} else if(corpusPaidAmount<totalcorpusPaidAmountCostToPay && ((totalcorpusPaidAmountCostToPay-corpusPaidAmount)>amountToBeIgnored)) {
					listOfErrorMsg.add("Corpus Fund : "+employeeFinancialService.convertUstoInFormat(totalcorpusPaidAmountCostToPay-corpusPaidAmount));
				}/* else  if(employeeFinancialService.roundOffAmount(corpusPaidAmount) != employeeFinancialService.roundOffAmount(totalcorpusPaidAmountCostToPay)) {
					listOfErrorMsg.add("Customer corpus data is not cleared...!, Could not proceed for NOC.");
				}*/ else {//if everything is clear then this type validated
					listOfNocValidatedDetails.add(MetadataId.CORPUS_FUND.getId());
				}
		}
	} else {
		listOfNocValidatedDetails.add(MetadataId.CORPUS_FUND.getId());
	}
	//PRINCIPAL_AMOUNT,FIN_PENALTY,LEGAL_COST,MAINTENANCE_CHARGE,CORPUS_FUND,INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES
	if(listOf_NocCheckList_Details_To_Valiate.contains(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId())) {
		/*if(!setOffTypeWiseAmountClearedList.containsKey(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId())) {
			listOfErrorMsg.add("Customer flat khata data is not cleared...!, Could not proceed for NOC.");
		} else if(setOffTypeWiseAmountClearedList.get(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId()) == false) {
			listOfErrorMsg.add("Customer flat khata data is not cleared...!, Could not proceed for NOC.");
		} else {*/
		if(true) {
			Double flatKhataPaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());
			flatKhataPaidAmount = flatKhataPaidAmount==null?0d:flatKhataPaidAmount;
			Double totalFlatKhataCostToPay = 0.0;
			Double flatFlatKhataAndDocumentationChargesAmt = 0.0;
			Double flatFlatKhataCostGstAmount = 0.0;
			Double BalanceAmountInExcessAmount = 0.0;
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			//customerPropertyDetailsInfo2.setType(MetadataId.FIN_PENALTY.getId());
			customerPropertyDetailsInfo2.setCondition(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString());
			//loading excess amount if any
			listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId()));
			BalanceAmountInExcessAmount = getTheExcessAmountBalance(listOfExcessAmount);
			flatKhataPaidAmount += BalanceAmountInExcessAmount;//adding excess amount in paid amount

				siteOtherChargesDetailsPojo = getSiteOtherChargesDetailsObject(siteOtherChargesDetailsList,MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());
				finTaxPojo = getTaxPercentageDetailsObject(taxDetailsList,MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());
				if (finTaxPojo == null) {
					finProjDemandNoteInfo.setCondition("");//not adding any condition, so taking current date gst percentages
					taxDetailsList.addAll(employeeFinancialServiceDao.getTaxOnInterestAmountData(finProjDemandNoteInfo,Arrays.asList(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId())));
					finTaxPojo = getTaxPercentageDetailsObject(taxDetailsList,MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());
				}

				//taking principal amount from master tables
				if (MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId().equals(siteOtherChargesDetailsPojo.getMeteDataTypeId())) {
					//maintenanceAmount = other.getAmount();
					flatFlatKhataAndDocumentationChargesAmt = siteOtherChargesDetailsPojo.getAmount();
				}
				
				if(finTaxPojo == null) {
					throw new EmployeeFinancialServiceException(Arrays.asList("Please add flat khata charges GST percentage..."));
				}
				//calculating GST amount on principal amount
				flatFlatKhataCostGstAmount =  (flatFlatKhataAndDocumentationChargesAmt * finTaxPojo.getPercentageValue()) / 100;
				totalFlatKhataCostToPay = flatFlatKhataAndDocumentationChargesAmt+flatFlatKhataCostGstAmount;

				flatKhataPaidAmount = employeeFinancialService.roundOffAmount(flatKhataPaidAmount);
				totalFlatKhataCostToPay = employeeFinancialService.roundOffAmount(totalFlatKhataCostToPay);
				
				if(flatKhataPaidAmount>totalFlatKhataCostToPay) {
					listOfExcessErrorMsg.add("Flat Khata : "+employeeFinancialService.convertUstoInFormat(flatKhataPaidAmount-totalFlatKhataCostToPay));
					listOfNocValidatedDetails.add(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());//if payment has excess amount, so don't have any objection for NOC
				} else if(flatKhataPaidAmount<totalFlatKhataCostToPay && ((totalFlatKhataCostToPay-flatKhataPaidAmount)>amountToBeIgnored)) {
					listOfErrorMsg.add("Flat Khata : "+employeeFinancialService.convertUstoInFormat(totalFlatKhataCostToPay-flatKhataPaidAmount));
				}/* else  if(employeeFinancialService.roundOffAmount(flatKhataPaidAmount) != employeeFinancialService.roundOffAmount(totalFlatKhataCostToPay)) {
					listOfErrorMsg.add("Customer flat khata data is not cleared...!, Could not proceed for NOC.");
				}*/ else {//if everything is clear then this type validated
					listOfNocValidatedDetails.add(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());
				}
		}
	} else {
		listOfNocValidatedDetails.add(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());
	}

	//PRINCIPAL_AMOUNT,FIN_PENALTY,LEGAL_COST,MAINTENANCE_CHARGE,CORPUS_FUND,INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES,MODIFICATION_COST
	//if any modification invoice created, then this consition will execute
	if(listOf_NocCheckList_Details_To_Valiate.contains(MetadataId.MODIFICATION_COST.getId()) && setOffTypeWiseAmountClearedList.containsKey(MetadataId.MODIFICATION_COST.getId())) {
		if(!setOffTypeWiseAmountClearedList.containsKey(MetadataId.MODIFICATION_COST.getId())) {
			listOfNocValidatedDetails.add(MetadataId.MODIFICATION_COST.getId());
			//listOfErrorMsg.add("Customer Legal data is not cleared...!, Could not proceed for NOC.");
			//modification may or may not be generated for customer
		}/* else if(setOffTypeWiseAmountClearedList.get(MetadataId.MODIFICATION_COST.getId()) == false) {
			listOfErrorMsg.add("Customer modification data is not cleared...!, Could not proceed for NOC.");
			//modification may or may not be generated for customer			
		}*/ else {
			Double modificationPaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.MODIFICATION_COST.getId());
			Double modificationPayAmount = payAmountSetOffTypeWiseList.get(MetadataId.MODIFICATION_COST.getId());

			Double BalanceAmountInExcessAmount = 0.0;
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			//customerPropertyDetailsInfo2.setType(MetadataId.FIN_PENALTY.getId());
			customerPropertyDetailsInfo2.setCondition(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString());
			//loading excess amount if any
			listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.MODIFICATION_COST.getId()));
			BalanceAmountInExcessAmount = getTheExcessAmountBalance(listOfExcessAmount);
			modificationPaidAmount += BalanceAmountInExcessAmount;//adding excess amount in paid amount

			modificationPaidAmount = employeeFinancialService.roundOffAmount(modificationPaidAmount);
			modificationPayAmount = employeeFinancialService.roundOffAmount(modificationPayAmount);
			
			if(modificationPaidAmount>modificationPayAmount) {//if payment has excess amount, so don't have any objection for NOC
				listOfExcessErrorMsg.add("Modification Charges : "+employeeFinancialService.convertUstoInFormat(modificationPaidAmount-modificationPayAmount));
				listOfNocValidatedDetails.add(MetadataId.MODIFICATION_COST.getId());//if payment has excess amount, so don't have any objection for NOC
			} else if(modificationPaidAmount<modificationPayAmount && ((modificationPayAmount-modificationPaidAmount)>amountToBeIgnored)) {
				listOfErrorMsg.add("Modification Charges : "+employeeFinancialService.convertUstoInFormat(modificationPayAmount-modificationPaidAmount));
			}/* else  if(employeeFinancialService.roundOffAmount(modificationPaidAmount) != employeeFinancialService.roundOffAmount(modificationPayAmount)) {
				listOfErrorMsg.add("Customer Modification Charges is not cleared...!, Could not proceed for NOC.");
			}*/ else {//if everything is clear then this type validated
				listOfNocValidatedDetails.add(MetadataId.MODIFICATION_COST.getId());
			}
		}
	} else {
		listOfNocValidatedDetails.add(MetadataId.MODIFICATION_COST.getId());
	}

	//Interest Validation
	interestData = future.get();
	//loading interest amount
	if( Util.isNotEmptyObject(interestData)) {
		@SuppressWarnings("unchecked")
		List<EmployeeFinancialResponse>  employeeFinancialResponseList =  (List<EmployeeFinancialResponse>) interestData.get(0);
		EmployeeFinancialResponse employeeFinancialResponse = employeeFinancialResponseList.get(0);
		//taking interest amount details
		totalInterestAmount = employeeFinancialResponse.getTotalPenaltyAmount()==null?0d:employeeFinancialResponse.getTotalPenaltyAmount();
	}
	
	if(isInterestShouldBeCalculated && listOf_NocCheckList_Details_To_Valiate.contains(MetadataId.FIN_PENALTY.getId())) {//if customer paid amount late for milestones, then interest should be calculated
		//Penalty data
		if(!setOffTypeWiseAmountClearedList.containsKey(MetadataId.FIN_PENALTY.getId())) {
			//throw new EmployeeFinancialServiceException("Customer Principal data is not cleared...!, Could not proceed for NOC.");
			if(totalInterestAmount!=0 ) {
			listOfErrorMsg.add("Interest : "+employeeFinancialService.convertUstoInFormat(totalInterestAmount));
			listOfErrorMsg.add("Customer interest data is not cleared...!, Could not proceed for NOC.");
			}else{//if everything is clear then this type validated
				listOfNocValidatedDetails.add(MetadataId.FIN_PENALTY.getId());
			}
		} else {
			Double interestPaidAmount = paidAmountsetOffTypeWiseList.get(MetadataId.FIN_PENALTY.getId());
			Double BalanceAmountInExcessAmount = 0.0;
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo2 = (CustomerPropertyDetailsInfo) customerPropertyDetailsInfo.clone();
			//customerPropertyDetailsInfo2.setType(MetadataId.FIN_PENALTY.getId());
			customerPropertyDetailsInfo2.setCondition(MetadataId.FIN_BOOKING_FORM_EXCESS_AMOUNT.getId().toString());
			//loading excess amount if any
			listOfExcessAmount = employeeFinancialServiceDao.getExcessAmountDetails(customerPropertyDetailsInfo2,Arrays.asList(MetadataId.FIN_PENALTY.getId()));

			BalanceAmountInExcessAmount = getTheExcessAmountBalance(listOfExcessAmount);
			interestPaidAmount += BalanceAmountInExcessAmount;//adding excess amount in paid amount
			
			interestPaidAmount = employeeFinancialService.roundOffAmount(interestPaidAmount);
			totalInterestAmount = employeeFinancialService.roundOffAmount(totalInterestAmount);
			
			if(interestPaidAmount>totalInterestAmount) {
				listOfExcessErrorMsg.add("Interest : "+employeeFinancialService.convertUstoInFormat(interestPaidAmount-totalInterestAmount));
				listOfNocValidatedDetails.add(MetadataId.FIN_PENALTY.getId());//if payment has excess amount, so don't have any objection for NOC
			} else if(interestPaidAmount<totalInterestAmount && ((totalInterestAmount-interestPaidAmount)>amountToBeIgnored)) {
				listOfErrorMsg.add("Interest : "+employeeFinancialService.convertUstoInFormat(totalInterestAmount-interestPaidAmount));
			}/* else  if(employeeFinancialService.roundOffAmount(interestPaidAmount) != employeeFinancialService.roundOffAmount(totalInterestAmount)) {
				listOfErrorMsg.add("Customer interest data is not cleared...!, Could not proceed for NOC.");
			}*/ else {//if everything is clear then this type validated
				listOfNocValidatedDetails.add(MetadataId.FIN_PENALTY.getId());
			}
		}//PENALTY
	} else {
		listOfNocValidatedDetails.add(MetadataId.FIN_PENALTY.getId());
	}
//Interest Validation
	
	if(listOf_NocCheckList_Details_To_Valiate.contains(MetadataId.EXTRA_CAR_PARKING.getId())) {
		List<Map<String, Object>> carParkingList = bookingFormServiceDaoImpl.loadExtraCarParkingDetails(bookingFormRequest);
		if(Util.isNotEmptyObject(carParkingList)) {//if the object is not empty then *need to validate the car parking amount, else we don't have any validation for extra car parking
			for (Map<String, Object> carParking : carParkingList) {
				Double payAmount = Util.isEmptyObject(carParking.get("CAR_PARKING_AMOUNT"))?0.0:Double.valueOf(carParking.get("CAR_PARKING_AMOUNT").toString());
				Double paidAmount = Util.isEmptyObject(carParking.get("PAID_AMOUNT"))?0.0:Double.valueOf(carParking.get("PAID_AMOUNT").toString());
				
				if(paidAmount>payAmount) {//if payment has excess amount, so don't have any objection for NOC
					listOfExcessErrorMsg.add("Extra car Parking : "+employeeFinancialService.convertUstoInFormat(paidAmount-payAmount));
					listOfNocValidatedDetails.add(MetadataId.EXTRA_CAR_PARKING.getId());//if payment has excess amount, so don't have any objection for NOC
				} else if(paidAmount<payAmount && ((payAmount-paidAmount)>amountToBeIgnored)) {
					listOfErrorMsg.add("Extra car Parking : "+employeeFinancialService.convertUstoInFormat(payAmount-paidAmount));
				}/* else  if(employeeFinancialService.roundOffAmount(paidAmount) != employeeFinancialService.roundOffAmount(payAmount)) {
					listOfErrorMsg.add("Customer Extra car Parking is not cleared...!, Could not proceed for NOC.");
				}*/ else {//if everything is clear then this type validated
					listOfNocValidatedDetails.add(MetadataId.EXTRA_CAR_PARKING.getId());
				}
			}
		} else {
			//if object is empty then add car parking id to validated details, as the booking don't have any extra car parking
			listOfNocValidatedDetails.add(MetadataId.EXTRA_CAR_PARKING.getId());	
		}
	} else {
		listOfNocValidatedDetails.add(MetadataId.EXTRA_CAR_PARKING.getId());
	}
	
		if(Util.isNotEmptyObject(listOfErrorMsg)) {
			listOfErrorMsg.add(0,"Customer is due below : ");
			if(Util.isNotEmptyObject(listOfExcessErrorMsg)) {
				listOfExcessErrorMsg.add(0,"Customer has excess amount on this : ");
				listOfErrorMsg.addAll(listOfExcessErrorMsg);
			}
			
			nocValidatonResp.put("nocCleared","dues");
			//throw all the msg's of dues
			throw new EmployeeFinancialServiceException(listOfErrorMsg);
		} else {
			nocValidatonResp.put("nocCleared","nodues");
		}
		
		//validating check list types and validated payment types is matching or not
		boolean isAllPaymentTypesCleared = validateRequiredTypesOfPaymentForNoc(listOf_NocCheckList_Details_To_Valiate,listOfNocValidatedDetails,listOfErrorMsg);
		if(!isAllPaymentTypesCleared) {
			nocValidatonResp.put("nocCleared","dues");
		}
		
		nocValidatonResp.put("listOfNocErrorMsg",listOfErrorMsg);
		return nocValidatonResp;
	}
	
	/**
	 * NOTE : used these method in NOC letter,Save Maintenance Charges and receipt Entry.calculate Maintenance charges
	 *so if u r doing any changes in this method, so caller method also has to test
	 */
	@Override
	public Map<String, Double> getTotalMaintainanceCostToPay(Timestamp startDate, Timestamp endDateForMaintencenDate,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<FlatDetailsPojo> flatDetailsPojos, FinPenaltyTaxPojo finTaxPojo
			, SiteOtherChargesDetailsPojo siteOtherChargesDetailsPojo) throws Exception {
		Map<String,Double> map = new ConcurrentHashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		double totalMaintainanceCostToPay = 0.0d;
		double totalGSTAmount = 0.0d;
		double basicAmount = 0.0d;
		int addDaysInBookingDate = 30;
		Timestamp bookingDate = customerPropertyDetailsInfo.getBookingDate();
		if(customerPropertyDetailsInfo.getSiteId().equals(107l)) {
			if(Util.isEmptyObject(customerPropertyDetailsInfo.getFlatSaleOwnerId())) {
				throw new EmployeeFinancialServiceException("Flat sale owner found empty, Project "+customerPropertyDetailsInfo.getSiteName()+" and Flat no "+customerPropertyDetailsInfo.getFlatNo()+".");
			}
			if(!"1".equals(customerPropertyDetailsInfo.getFlatSaleOwnerId())) {
				//if the site is eden garden and flat is landlord, then from sale deed date to maintenance date we have to calculate the maintenance charges
				bookingDate = customerPropertyDetailsInfo.getSaleDeedDate();//instead of Booking date taking SaleDeed Date, as this is landlord flats 
				startDate = customerPropertyDetailsInfo.getSaleDeedDate();
				//if this flat sale owner id is landlord then start date will be flat sale deed date
				addDaysInBookingDate = 0;
			}
		}
		//first need to calculate, booking date to month last date
		//for landlord flats we are not adding sale deed date+31 days
		//ex. booking date = 10-04-2022+31 days then month last date = 31-05-2022 means for 21 days we have calculate the maintenance charges using SBUA
		//for remaining calculculation upto end date
		//if maintenance end date is 28-02-2023
		//then from 01-05-2022 to 28-02-2022 has to find the months, and per month maintenance has to calculate using SBUA
		if(Util.isEmptyObject(bookingDate)) {
			map.put("totalMaintainanceCostToPay",0.0d);
			map.put("totalGstAmount",0.0d);
			map.put("totalBasicMaintainanceAmt",0.0d);
			return map;
		}
		String bookingDateInString = "";
		if (startDate != null && endDateForMaintencenDate != null 
				&& (customerPropertyDetailsInfo.getSiteId().equals(111l) || customerPropertyDetailsInfo.getSiteId().equals(107l))) {
			if(bookingDate.after(startDate) || bookingDate.equals(startDate)) {

				RoundingMode roundingMode = RoundingMode.HALF_UP;
				int roundingModeSize = 2;
				bookingDate =  TimeUtil.addDays(bookingDate, addDaysInBookingDate);
				bookingDateInString = format.format(bookingDate);
				bookingDate = TimeUtil.removeTimePartFromTimeStamp1(bookingDate);
				//taking last date of the month from bookingDate
				Timestamp lastDayOfMonth =  TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(TimeUtil.getLastDateOfMonth(bookingDate).getTime()));
				//calculting days difference beetween booking date last day of month
				int days = TimeUtil.differenceBetweenDays(bookingDate, lastDayOfMonth);
				//int days = TimeUtil.differenceBetweenDays(bookingDate, new Timestamp(TimeUtil.getLastDateOfMonth(endDateForMaintencenDate).getTime()));
				days +=1;
				System.out.println(TimeUtil.getLastDateOfMonth(bookingDate)+" "+TimeUtil.addOneDay(new Timestamp(TimeUtil.getLastDateOfMonth(bookingDate).getTime())));
				
				if(lastDayOfMonth.after(endDateForMaintencenDate)) {
					System.out.println(" max date in month is after the end date of maintenance charges");
					days = 0;
					//return 0;
				}
				
				bookingDateInString = format.format(TimeUtil.addOneDay(new Timestamp(TimeUtil.getLastDateOfMonth(bookingDate).getTime())));
				Calendar startDateForMaintenance = new GregorianCalendar(Integer.valueOf(bookingDateInString.split("-")[2]),Integer.valueOf(bookingDateInString.split("-")[1])-1,Integer.valueOf(bookingDateInString.split("-")[0]));
				//birthDay.set(bookingDate.getYear(), bookingDate.getMonth(), bookingDate.getDay());
				Calendar allDaysInMonth = Calendar.getInstance();
				allDaysInMonth.setTime(bookingDate);
				int monthMaxDays = allDaysInMonth.getActualMaximum(Calendar.DAY_OF_MONTH);// taking booking date contains months days like,30 or 31 or 28 
				
				//Long amountForYear = siteOtherChargesDetailsPojo.getAmtForYears();
				//maintenance amount year wise
				Double maintenanceChargesForYear = siteOtherChargesDetailsPojo.getAmount();
				//maintenance amount month wise, every month 3.2 amount
				Double maintenanceChargesForMonth = BigDecimal.valueOf(maintenanceChargesForYear/12).setScale(roundingModeSize, roundingMode).doubleValue();
				//maintenance amount day wise 
				Double maintenanceChargesForDay = maintenanceChargesForMonth/30;//not in use
				maintenanceChargesForDay = BigDecimal.valueOf(maintenanceChargesForDay).setScale(3, roundingMode).doubleValue();
				
				System.out.println("maintenanceAmountForYear "+maintenanceChargesForYear);
				System.out.println("maintenanceAmountForMonth "+maintenanceChargesForMonth);
				System.out.println("maintenanceAmountForDay "+maintenanceChargesForDay);
				double maintenanceCalcWithGstAmount = 0.0d;
				if (days > 0) {
					System.out.println("FinancialTest.main( ) days "+days +" "+(maintenanceChargesForDay*days));
					System.out.println((maintenanceChargesForDay*days)*flatDetailsPojos.get(0).getSbua());
					//maintenanceCalcWithGstAmount = (maintenanceChargesForDay*days)*flatDetailsPojos.get(0).getSbua();
				}
				
				// Using Calendar - calculating number of months between two dates  
		        //Calendar birthDay = new GregorianCalendar(2022, Calendar.MAY, 01);
		        Calendar today = new GregorianCalendar();
		        
		        //today.setTime(format.parse("31-01-2023"));//End Date of maintenance
		        today.setTime(endDateForMaintencenDate);//End Date of maintenance
		        //taking the difference of the months
		        int yearsInBetween = today.get(Calendar.YEAR) - startDateForMaintenance.get(Calendar.YEAR);
		        int monthsDiff = today.get(Calendar.MONTH) - startDateForMaintenance.get(Calendar.MONTH);
		        long ageInMonths = yearsInBetween*12 + monthsDiff;
		        ageInMonths +=1;
		        //long age = yearsInBetween;

		        if(ageInMonths<0) {
		        	ageInMonths = 0;//if the months are in minus make the month zero, so calculation will not happen
		        	//return 0;
		        }
		  
		        maintenanceCalcWithGstAmount = 0.0d;
		        //calculating per month maintenance charges
		        double maintenanceAmountForMonth = maintenanceChargesForMonth*flatDetailsPojos.get(0).getSbua();
		        double basicAmountForMonth = maintenanceChargesForMonth*flatDetailsPojos.get(0).getSbua();
		        
		        maintenanceAmountForMonth = maintenanceAmountForMonth+(maintenanceAmountForMonth*finTaxPojo.getPercentageValue()/100);//amount with gst
		        maintenanceAmountForMonth = BigDecimal.valueOf(maintenanceAmountForMonth).setScale(roundingModeSize, roundingMode).doubleValue();
		        
		        //calculating month wise maintenance amount using SBUA
		        maintenanceCalcWithGstAmount += (maintenanceChargesForMonth*ageInMonths)*flatDetailsPojos.get(0).getSbua();//maintenance basic amount
		        basicAmount += (maintenanceChargesForMonth*ageInMonths)*flatDetailsPojos.get(0).getSbua();//maintenance basic amount
		        maintenanceCalcWithGstAmount = (maintenanceCalcWithGstAmount+(maintenanceCalcWithGstAmount*finTaxPojo.getPercentageValue()/100));//adding GST on maintenance amount
		        totalGSTAmount += (maintenanceCalcWithGstAmount*finTaxPojo.getPercentageValue()/100);
		        System.out.println("Maintenance Amount "+maintenanceCalcWithGstAmount);//amount with gst
		        
		        //calculating per day amount ,maintenanceAmountForMonth already calculated maintenance amount with GST amount, so dividing by in a month how many days coming 
		        double perDayBasicAmount = BigDecimal.valueOf(basicAmountForMonth/monthMaxDays).setScale(roundingModeSize, roundingMode).doubleValue();
		        double perDayAmount = BigDecimal.valueOf(maintenanceAmountForMonth/monthMaxDays).setScale(roundingModeSize, roundingMode).doubleValue();
		        
		        double perDayBasicCalculatedAmount = (perDayBasicAmount)*days;//per day amount into the calculated days
		        double perDayCalculatedAmount = (perDayAmount)*days;//per day amount into the calculated days
		        
		        basicAmount += perDayBasicCalculatedAmount;//Basic amount
		        maintenanceCalcWithGstAmount +=perDayCalculatedAmount;//with GST amount
		        maintenanceCalcWithGstAmount = BigDecimal.valueOf(maintenanceCalcWithGstAmount).setScale(roundingModeSize, roundingMode).doubleValue();
		        totalMaintainanceCostToPay = maintenanceCalcWithGstAmount;
		        System.out.println("per day wise amount in month "+perDayAmount);
		        System.out.println("balance days amount in month "+perDayCalculatedAmount);
				System.out.println("Maintenance Amount "+maintenanceCalcWithGstAmount+" , gst "+(maintenanceCalcWithGstAmount-basicAmount)+", basicAmount"+basicAmount);
				System.out.println((basicAmount*finTaxPojo.getPercentageValue())/100);
				map.put("totalMaintainanceCostToPay",totalMaintainanceCostToPay);
				map.put("totalGstAmount",BigDecimal.valueOf(maintenanceCalcWithGstAmount-basicAmount).setScale(roundingModeSize, roundingMode).doubleValue());
				map.put("totalBasicMaintainanceAmt",basicAmount);
			}
		}
		return map;
	}

	private boolean validateRequiredTypesOfPaymentForNoc(List<Long> listOfNocDetails_To_Valiate,
			List<Long> listOfNocValidatedDetails, List<String> listOfErrorMsg) {
		boolean isAllPaymentTypesCleared = false;
		int count = 0;
		Collections.sort(listOfNocValidatedDetails);
		Collections.sort(listOfNocDetails_To_Valiate);
		System.out.println("BookingFormServiceImpl.validateRequiredTypesOfPaymentForNoc() listOfNocValidatedDetails "+(listOfNocValidatedDetails));
		System.out.println("BookingFormServiceImpl.validateRequiredTypesOfPaymentForNoc() listOfNocDetails_To_Valiate "+(listOfNocDetails_To_Valiate));
		for (Long paymentType : listOfNocValidatedDetails) {
			if(listOfNocDetails_To_Valiate.contains(paymentType)) {
				//isAllPaymentTypesCleared = true;
				count++;
			} else {
				/*isAllPaymentTypesCleared = false;
				break;//if any payment type is not cleared, then no need to check next payment details, here NOC condition failes
*/			}
		}

		//if validated payment count is equal to validate payment, then all the payment is cleared
		if (count == listOfNocDetails_To_Valiate.size()) {
			isAllPaymentTypesCleared = true;
		} else if (count > listOfNocDetails_To_Valiate.size()) {
			///if validated payment count is more than validate payment, then all the payment is cleared
			isAllPaymentTypesCleared = true;
		} else {
			isAllPaymentTypesCleared = false;
		}

		return isAllPaymentTypesCleared;
	}

	private Double getTheExcessAmountBalance(List<FinBookingFormExcessAmountPojo> listOfExcessAmount) {
		double excessBalanceAmount = 0D;
		//if (Util.isNotEmptyObject(listOfExcessAmount)) {
			for (FinBookingFormExcessAmountPojo excessAmountPojo : listOfExcessAmount) {
				excessBalanceAmount =+ excessAmountPojo.getBalanceAmount();
			}
		//}
		return excessBalanceAmount;
	}

	private FinPenaltyTaxPojo getTaxPercentageDetailsObject(List<FinPenaltyTaxPojo> taxDetailsList, Long type) {
		for (FinPenaltyTaxPojo finTaxPojo : taxDetailsList) {
			if (type.equals(finTaxPojo.getTaxType())) {
				return finTaxPojo;
			}
		}
		return null;
	}

	private SiteOtherChargesDetailsPojo getSiteOtherChargesDetailsObject(List<SiteOtherChargesDetailsPojo> siteOtherChargesDetailsList, Long type) {
		for (SiteOtherChargesDetailsPojo siteOtherChargesDetailsPojo : siteOtherChargesDetailsList) {
			if(type.equals(siteOtherChargesDetailsPojo.getMeteDataTypeId())) {
				return siteOtherChargesDetailsPojo;
			}
		}
		return null;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public Map<String, Object> generateNOCLetter(BookingFormRequest bookingFormRequest, List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList) throws Exception  {
		logger.info("*** The control is inside the generateNOCLetter in EmployeeFinancialServiceImpl ***");
		Properties prop= responceCodesUtil.getApplicationProperties();
		Map<String, Object> nocMap = new HashMap<String, Object>();
		boolean isThisPreview = (bookingFormRequest.getActionStr()!=null && bookingFormRequest.getActionStr().equals("Preview"));
		long portNumber = bookingFormRequest.getPortNumber();
		BookingFormRequest bookingFormRequestCopy = new BookingFormRequest(); 
		BeanUtils.copyProperties(bookingFormRequest,bookingFormRequestCopy);
		bookingFormRequestCopy.setStatusId(Status.ACTIVE.getStatus());
		final List<FileInfo> fileInfoList = null;
		Map<String,Object> nocValidatonResp = null;
		String status = "";
		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(bookingFormRequest.getFlatBookingId()));
		employeeFinancialServiceInfo.setSiteId(bookingFormRequest.getSiteId());
		employeeFinancialServiceInfo.setSiteIds(Arrays.asList(bookingFormRequest.getSiteId()));
		
		if(Util.isNotEmptyObject(customerPropertyDetailsPojoList)) {
			
		} else {
			/* getting Customer Details */
			customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
			if(Util.isEmptyObject(customerPropertyDetailsPojoList)) {
				throw new EmployeeFinancialServiceException("Failed to generate NOC letter, No data found for customer.");
			}
		}
		
		final CustomerPropertyDetailsInfo customerPropertyDetailsInfo =  new EmployeeFinancialMapper().copyPropertiesFromCustomerPropertyDetailsPojoToCustomerPropertyDetailsInfo(customerPropertyDetailsPojoList.get(0));
		employeeFinancialServiceInfo.setCustomerPropertyDetailsInfo(customerPropertyDetailsInfo);
		employeeFinancialServiceInfo.setBookingFormId(bookingFormRequest.getFlatBookingId());		
		
		String generateNOCWithoutValidation=prop.getProperty("NOC_WITHOUT_VALIDATION_BY_FLAT_BOOKING");
		
		if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
			generateNOCWithoutValidation=prop.getProperty("CUG_NOC_WITHOUT_VALIDATION_BY_FLAT_BOOKING");
		} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
			generateNOCWithoutValidation=prop.getProperty("CUG_NOC_WITHOUT_VALIDATION_BY_FLAT_BOOKING");
		} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
			generateNOCWithoutValidation=prop.getProperty("CUG_NOC_WITHOUT_VALIDATION_BY_FLAT_BOOKING");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
			generateNOCWithoutValidation=prop.getProperty("CUG_NOC_WITHOUT_VALIDATION_BY_FLAT_BOOKING");
		} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
			generateNOCWithoutValidation=prop.getProperty("LIVE_NOC_WITHOUT_VALIDATION_BY_FLAT_BOOKING");
		} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
			generateNOCWithoutValidation=prop.getProperty("LIVE_NOC_WITHOUT_VALIDATION_BY_FLAT_BOOKING");
		}  else {
			throw new EmployeeFinancialServiceException("Server port number missing, Please contact to Support Team.");
		}
		
		List<String> flats = Arrays.asList(generateNOCWithoutValidation.split("\\s*,\\s*"));
		if (flats.contains(bookingFormRequest.getFlatBookingId().toString())) {
			status="nodues";
		} else {
			// Registration date checking code
			BookingFormRequest coAppBookingFormRequest1 = new BookingFormRequest();
			coAppBookingFormRequest1.setFlatBookingId(bookingFormRequest.getFlatBookingId());
			coAppBookingFormRequest1.setCustomerId(bookingFormRequest.getCustomerId());
			coAppBookingFormRequest1.setStatusId(Status.ACTIVE.getStatus());
			List<FlatBookingPojo> flatBookPojo1 = bookingFormServiceDaoImpl.getFlatbookingDetails(coAppBookingFormRequest1);
			if (Util.isEmptyObject(flatBookPojo1) || flatBookPojo1.get(0).getRegistrationDate() == null) {
				throw new EmployeeFinancialServiceException("Registration date is not updated, Unable to generate the NOC.");
			}

			//financial amount due checking method
			nocValidatonResp = nocFinancialValidation(bookingFormRequest, customerPropertyDetailsInfo, customerPropertyDetailsPojoList);
			if (Util.isEmptyObject(nocValidatonResp)) {
				throw new EmployeeFinancialServiceException("Failed to generate NOC letter, No data found validation.");
			}
			status = nocValidatonResp.get("nocCleared").toString();
			@SuppressWarnings("unchecked")
			List<String> listOfErrorMsg = (List<String>) nocValidatonResp.get("listOfNocErrorMsg");
			if(Util.isNotEmptyObject(listOfErrorMsg)) {//this list of error's need to be shown in front end
				throw new EmployeeFinancialServiceException(listOfErrorMsg);
			}
		}
		

		if (status.equalsIgnoreCase("nodues")) {
			//get the details for NOC letter
			final Map<String, Object> dataForGenerateAllotmentLetterHelper = getTheCustomerDetailsForNOCLetter(employeeFinancialServiceInfo,bookingFormRequestCopy,customerPropertyDetailsInfo);
			System.out.println(dataForGenerateAllotmentLetterHelper);
			
			
			String folderFilePath = "";
			String folderFileUrl = "";

			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("NOC_FILE_URL_PATH");
			}  else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("NOC_FILE_URL_PATH");
			}else {
				logger.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
			}
			if (Util.isEmptyObject(folderFilePath) || Util.isEmptyObject(folderFilePath)) {
				throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
			}
			if(isThisPreview) {//if this is preview
				dataForGenerateAllotmentLetterHelper.put("folderFilePath", folderFilePath+bookingFormRequest.getActionStr()+"\\");
				dataForGenerateAllotmentLetterHelper.put("folderFileUrl", folderFileUrl+bookingFormRequest.getActionStr()+"/");
				if(true) {
					//deleting previous zip files
					new PdfHelper().folderDeleteAndSubFolder(new File(folderFilePath+"/"+bookingFormRequest.getActionStr()+"/"),0,bookingFormRequest.getSiteId().toString());
				}
				/*employeeFinancialServiceInfo.setFilePath(folderFilePath+"/"+bookingFormRequest.getActionStr());
				employeeFinancialServiceInfo.setFileUrl(folderFileUrl+"/"+bookingFormRequest.getActionStr());*/
			} else {
				dataForGenerateAllotmentLetterHelper.put("folderFilePath", folderFilePath);
				dataForGenerateAllotmentLetterHelper.put("folderFileUrl", folderFileUrl);
				/*employeeFinancialServiceInfo.setFilePath(folderFilePath);
				employeeFinancialServiceInfo.setFileUrl(folderFileUrl);*/
			}
			
			generateNOCLetter(employeeFinancialServiceInfo,dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo,bookingFormRequestCopy);
			
			if(!isThisPreview) {//sending email to employees only
				sendNOCLetter(employeeFinancialServiceInfo,bookingFormRequestCopy,customerPropertyDetailsInfo,dataForGenerateAllotmentLetterHelper);
			}
			dataForGenerateAllotmentLetterHelper.put("fileInfoList", fileInfoList);
			dataForGenerateAllotmentLetterHelper.put("successMasg", "success");
			return dataForGenerateAllotmentLetterHelper;
		} else {
			nocMap.put("successMasg", "failed");
			nocMap.put("errorMsg",status);
		}
		return nocMap;
	}
	
	private Map<String, Object> getTheCustomerDetailsForNOCLetter(final EmployeeFinancialServiceInfo employeeFinancialServiceInfo,
			final BookingFormRequest bookingFormRequestCopy, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) throws Exception {
		List<Co_ApplicantInfo> coApplicantDetailsList = new ArrayList<>();
		List<AddressInfo> siteAddressInfoList = null;
		List<AddressInfo> customerAddressInfoList = null;
		List<ProfessionalInfo> coApplicantProfessionalInfo = null;
		
		CustomerBookingFormInfo customerBookingFormInfo = getFlatCustomerAndCoAppBookingDetails(bookingFormRequestCopy);
		
		List<PoaHolderPojo> poHolderPojoList = new ArrayList<PoaHolderPojo>();
        System.out.println(poHolderPojoList);
		customerPropertyDetailsInfo.setActionUrl("All");
		customerPropertyDetailsInfo.setStatusId(Status.ACTIVE.getStatus());
		/* getting Co_Applicant Details */
		List<CoApplicantPojo> coApplicantPojoList = employeeFinancialServiceDao.getCoApplicantDetails(customerPropertyDetailsInfo);
		if(Util.isNotEmptyObject(coApplicantPojoList)) {
			System.out.println(coApplicantPojoList.get(0));
			CoApplicantPojo po = coApplicantPojoList.get(0);
			System.out.println( po.getNameprefix());
			System.out.println(po.getFirstName()); 
			System.out.println(po.getRelationWith()); 
			System.out.println(po.getRelationNamePrefix());
			System.out.println(po.getRelationName());
			System.out.println( po.getAdharId()); 
			System.out.println(po.getAge());  
			System.out.println(po.getPancard());

			Co_ApplicantInfo ico = customerBookingFormInfo.getCoApplicentDetails().get(0).getCo_ApplicantInfo();
			System.out.println(ico.getNamePrefix());
			System.out.println(ico.getFirstName());
			System.out.println(ico.getRelationWith());
			System.out.println(ico.getRelationNamePrefix());
			System.out.println(ico.getRelationName());
			System.out.println(ico.getAadharId());
			System.out.println(ico.getAge());
			System.out.println(ico.getPancard());
		}
		
		if(Util.isNotEmptyObject(customerBookingFormInfo.getCoApplicentDetails())) {
			coApplicantProfessionalInfo = new ArrayList<>();
			for (CoApplicentDetailsInfo pojo : customerBookingFormInfo.getCoApplicentDetails()) {
				coApplicantProfessionalInfo.add(pojo.getProfessionalInfo());
			}
		}
		//milestone details
		List<MileStoneInfo> futureLoadFinancialDetails = employeeFinancialService.getFinancialMilestoneDetails(employeeFinancialServiceInfo);
		/* setting customer full Name and Co app full name to customerPropertyDetailsInfo */
		setCustomerAndCoApplicantFullName(coApplicantPojoList, customerPropertyDetailsInfo);
		
		/* getting customer address details *///List<AddressInfo> 

		customerAddressInfoList = customerBookingFormInfo.getAddressInfos();
		
		List<SitePojo> siteDetails = bookingFormServiceDaoImpl.getSite(bookingFormRequestCopy);
		if(Util.isEmptyObject(siteDetails)) {
			logger.info("Failed to generate allotment letter, No data found for site.");
			throw new EmployeeFinancialServiceException("Failed to generate allotment letter, No data found for site.");
		}
		
		if(Util.isEmptyObject(customerBookingFormInfo.getFlatBookingInfo())) {
			logger.info("Failed to generate allotment letter, No data found for site.");
			throw new EmployeeFinancialServiceException("Failed to generate allotment letter, No data found for Flat.");
		}
		

		List<OfficeDtlsPojo> officeDetailsPojoList = employeeFinancialServiceDao.getOfficeDetailsBySite(employeeFinancialServiceInfo);
		List<AddressPojo> siteAddressPojoList = employeeFinancialServiceDao.getSiteAddressDetails(employeeFinancialServiceInfo);
		siteAddressInfoList = financialMapper.addressPojoListToAddressInfoList(siteAddressPojoList);		

		final List<FlatBookingInfo> flatBookingInfos = new ArrayList<>(); //loadUnitDetails(bookingFormRequestCopy);
		
		List<FlatBookingPojo> flatBookPojo = bookingFormServiceDaoImpl.getFlatbookingDetails(bookingFormRequestCopy);

		flatBookingInfos.addAll(loadUnitDetails(bookingFormRequestCopy));
	

		if (Util.isEmptyObject(flatBookingInfos)) {
			throw new EmployeeFinancialServiceException("Failed to generate welcome letter, Flat Cost details not found.");
		}
		try {
			coApplicantDetailsList = Arrays.asList(customerBookingFormInfo.getCoApplicentDetails().get(0).getCo_ApplicantInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		bookingFormMapper.assignDefaultValues(customerBookingFormInfo);
		Map<String, Object> customerDetails = bookingFormMapper.getCustomerDetails(customerPropertyDetailsInfo,customerAddressInfoList,customerBookingFormInfo,bookingFormRequestCopy,poHolderPojoList);
		//Map<String, Object> customerCoApplicantDetails = bookingFormMapper.getCustomerCoApplicantDetails(customerPropertyDetailsInfo,coApplicantPojoList,bookingFormRequestCopy);
		Map<String, Object> customerUnitDetails = bookingFormMapper.getCustomerFlatDetails(flatBookingInfos,customerBookingFormInfo);
		customerDetails.put("RERA",siteDetails.get(0).getRera());
		customerDetails.put("applicationNumber",Util.isEmptyObject(customerBookingFormInfo.getFlatBookingInfo().getApplicationNumber()) ? "-" : customerBookingFormInfo.getFlatBookingInfo().getApplicationNumber());
		customerDetails.put("noOfCustomersIncludedInBooking",coApplicantDetailsList.size());
		customerDetails.put("customerName", customerPropertyDetailsInfo.getCustomerName());
		System.out.println(customerUnitDetails);
		System.out.println("customerDetails "+customerDetails);
		
		
		
		Map<String, Object> dataForGenerateNOCLetter = new HashMap<>();
		dataForGenerateNOCLetter.put("customerDetails", customerDetails);
		dataForGenerateNOCLetter.put("customerUnitDetails", customerUnitDetails);
		
		dataForGenerateNOCLetter.put("firstApplicantDetails", Arrays.asList(customerBookingFormInfo.getCustomerInfo()));
		dataForGenerateNOCLetter.put("coApplicantDetails", coApplicantDetailsList);
		
		dataForGenerateNOCLetter.put("coApplicantProfessionalDetails", coApplicantProfessionalInfo);
		
		dataForGenerateNOCLetter.put("flatBookingInfoList", flatBookingInfos);
		
		dataForGenerateNOCLetter.put("officeDetailsPojoList", officeDetailsPojoList);
		dataForGenerateNOCLetter.put("siteAddressInfoList", siteAddressInfoList);
		dataForGenerateNOCLetter.put("flatBookPojo", flatBookPojo);
		return dataForGenerateNOCLetter;
	}

	public void generateNOCLetter(EmployeeFinancialServiceInfo serviceInfo,final Map<String, Object> dataForGenerateNOCLetterHelper,
			final CustomerPropertyDetailsInfo customerPropertyDetailsInfo, BookingFormRequest bookingFormRequestCopy) throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.generateNOCLetter() *****");
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<List<FileInfo>> future = executorService.submit(new Callable<List<FileInfo>>(){
		    public List<FileInfo> call() throws Exception {
		    		 return employeeFinancialHelper.generateNOCLetterHelper(dataForGenerateNOCLetterHelper,customerPropertyDetailsInfo);
		    }
		});
		//List<FileInfo> fileInfoList = employeeFinancialHelper.generateCostBreakUpLetterHelper(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo);
		
		dataForGenerateNOCLetterHelper.put("NOCLetterFile", future.get());
	}
	

	@SuppressWarnings("unused")
	private void sendNOCLetter(EmployeeFinancialServiceInfo employeeFinancialServiceInfo,BookingFormRequest bookingFormRequestCopy, 
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, Map<String, Object> dataForGenerateAllotmentLetterHelper)  throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.sendNOCLetter() *****");
		Map<String,Object> map = null;
		List<Long> listOfRolles = null;
		List<Long> listOfDepartments = null;
		List<String> toMails = null;
		List<String> NOC_CcMails = null;
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsInfo.getSiteId());
		site.setName(customerPropertyDetailsInfo.getSiteName());
		List<FileInfo> fileInfoList = new ArrayList<>();
		Properties prop= responceCodesUtil.getApplicationProperties();

		@SuppressWarnings("unchecked")
		List<FileInfo> NOCfileInfoList = (List<FileInfo>) dataForGenerateAllotmentLetterHelper.get("NOCLetterFile");
		if (NOCfileInfoList != null) {
			for (FileInfo info : NOCfileInfoList) {
				Long nocReleaseId = bookingFormServiceDaoImpl.updateNocLetter(info, customerPropertyDetailsInfo,bookingFormRequestCopy);
				
				customerPropertyDetailsInfo.setNocReleaseId(nocReleaseId);
				for (FileInfo fileInfo : NOCfileInfoList) {
					Long result2 = bookingFormServiceDaoImpl.updateNocDocuments(fileInfo, customerPropertyDetailsInfo,bookingFormRequestCopy);
				}
				
				listOfDepartments = new ArrayList<>();
				listOfDepartments.add(Department.CRM.getId());
				listOfDepartments.add(Department.ACCOUNTS.getId());
				listOfDepartments.add(Department.TECH_CRM.getId());
				//Crm, tech crm, tech crm head, account
				listOfRolles = new ArrayList<>();
				//listOfRolles.add(Roles.CRM_SALES_HEAD.getId());
				listOfRolles.add(6l);

				map = new HashMap<>();
				map.put("listOfDepartments", listOfDepartments);
				map.put("listOfRolles", listOfRolles);
				//loading crm,accounts, and tech crm emails
				List<Map<String, Object>>  emails = employeeFinancialServiceDao.getEmployeeDetailsUsingDeptAndRoll(site,map);
				
				if(Util.isNotEmptyObject(emails)) {
					toMails = getTheEmails(emails);//removing duplicate emails from the list
					NOC_CcMails = prop.getProperty("NOC_CC_EMAILS")==null?new ArrayList<String>():Arrays.asList(prop.getProperty("NOC_CC_EMAILS").split(","));

					//NOC_CcMails = Arrays.asList("balaji@sumadhuragroup.com");
					
					dataForGenerateAllotmentLetterHelper.put("toMails", toMails);
					dataForGenerateAllotmentLetterHelper.put("NOC_CcMails", NOC_CcMails);
					if(Util.isNotEmptyObject(toMails)) {
					 	bookingFormServiceHelper.sendBookingNOCMail(dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo,toMails,NOC_CcMails);
					}
				}
				
				break;
			}
		}
	}
	
	private List<String> getTheEmails(List<Map<String, Object>> emails) {
		List<String> emailList = new ArrayList<>();
		
		for (Map<String, Object> map : emails) {
			String email = map.get("EMAIL") == null ? "" : map.get("EMAIL").toString();
			if (email.length() == 0 || emailList.contains(email)) {
				continue;
			}
			emailList.add(email);
		}

		return emailList;
	}

	
	@Override
	public List<NOCReleasePojo>  getNOCReleaseDetails(BookingFormRequest bookingFormRequest) throws EmployeeFinancialServiceException {
		logger.info("***** Control inside the BookingFormServiceImpl.getNOCShowStatus() *****");
		List<NOCReleasePojo> nocReleaseList = new ArrayList<>();
	//	List<NOCReleasePojo> nocList = bookingFormServiceDaoImpl.getNOCReleaseDetails(bookingFormRequest);
		List<NOCReleasePojo> nocList = bookingFormServiceDaoImpl.getNOCDocumentsDetails(bookingFormRequest);
		if (Util.isNotEmptyObject(nocList)) {
			for (NOCReleasePojo pojo : nocList) {
				String content=removeTillWord(pojo.getDocLocation(),"NOC");
				NOCReleasePojo bean = new NOCReleasePojo();
				BeanUtils.copyProperties(pojo, bean);
				bean.setNocShowStatus("hideNocButton");
				bean.setDeptId(Department.ACCOUNTS.getId());
				bean.setRoleId(Roles.ACCOUNTS_HEAD.getId());
				bean.setDocName(content);
				nocReleaseList.add(bean);
			}
		} else {
			NOCReleasePojo bean = new NOCReleasePojo();
			bean.setFlatBookingId(bookingFormRequest.getFlatBookingId());
			bean.setNocShowStatus("showNocButton");
			nocReleaseList.add(bean);
		}
		return nocReleaseList;
	}
	public static String removeTillWord(String input, String word) {
	    return input.substring(input.indexOf(word));
	}
	
	@Override
	public void sendMailToBankerForLoan(CustomerInfo customerInfo, CustomerPropertyDetailsPojo customerPropertyDetailsPojo) throws DefaultBankerException {
		logger.info("***** Control inside the BookingFormServiceImpl.sendMailToBankerForLoan() *****");
		BookingFormRequest bookingFormRequest=new BookingFormRequest();
		try {
			bookingFormRequest.setFlatBookingId(customerInfo.getFlatBookingId());
			Integer isMailSent = bookingFormServiceDaoImpl.isMailSentToBankerOnBooking(bookingFormRequest);
			if(isMailSent.equals(0)) {
				applyLaonService.sendMailToDefaultBankarOnBooking(customerInfo);
				logger.info("***** Control inside the BookingFormServiceImpl.sendMailToBankerForLoan() ***** banker mail sent success");
			}
		} catch (Exception e) {
			try {
				if(e instanceof DefaultBankerException) {
					sendDefaultBankerErrorMail(e, customerPropertyDetailsPojo);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			logger.info("***** Control inside the BookingFormServiceImpl.sendMailToBankerForLoan() *****  banker mail sent failed");
			logger.error("**** The Exception detailed informtion is ****" , e);
			e.printStackTrace();
		}
	}

	
	public void replyMailToBankerForLoan(CustomerPropertyDetailsPojo customerPropertyDetailsPojo) throws DefaultBankerException {
		
	}

		
	
	private void sendDefaultBankerErrorMail(Exception exception, CustomerPropertyDetailsPojo pojo) throws DefaultBankerException {
		CustomerPropertyDetailsPojo customerPropertyDetailsPojo = new CustomerPropertyDetailsPojo();
		BeanUtils.copyProperties(pojo, customerPropertyDetailsPojo);
		customerPropertyDetailsPojo.setBlockId(null);
		Properties prop = responceCodesUtil.getApplicationProperties();
		Email email = new Email();
		
		Site site = new Site();
		site.setSiteId(customerPropertyDetailsPojo.getSiteId());
		site.setName(customerPropertyDetailsPojo.getSiteName());
		List<Long> listOfDepartments = new ArrayList<>();
		listOfDepartments.add(Department.CRM.getId());
		
		List<Long> listOfRolles = new ArrayList<>();
		listOfRolles.add(Roles.SR_CRM_EXECUTIVE.getId());
		listOfRolles.add(Roles.CRM_SALES_HEAD.getId());

		Map<String,Object> map = new HashMap<>();
		map.put("listOfDepartments", listOfDepartments);
		map.put("listOfRolles", listOfRolles);
		//loading CRM details, for sending mail from CRM to banker
		final List<EmployeeDetailsPojo> crmDetailsList = employeeFinancialServiceDao.getEmployeeDetailsUsingDeptAndRollAndBlockId(customerPropertyDetailsPojo,map);
		List<String> emailsList = new ArrayList<>();
		if(Util.isNotEmptyObject(crmDetailsList)) {
			for (EmployeeDetailsPojo employeeDetailsPojo : crmDetailsList) {
				if(Util.isNotEmptyObject(employeeDetailsPojo.getEmail())) {
					if(!emailsList.contains(employeeDetailsPojo.getEmail())) {
						emailsList.add(employeeDetailsPojo.getEmail());
					}
				}
			}
		}
		
		String internalEmpMail = prop.getProperty("DEFAULT_BANKER_ERROR_INTERNAL_EMP_EMAIL");//taking due days from prop file
		String errorMsg = prop.getProperty("DEFAULT_BANKER_ERROR_MSG");//taking due days from prop file
		email.setSubject("Failed to send new lead details to banker");
		email.setToMails(emailsList.toArray(new String[] {}));
		errorMsg = errorMsg.replace("#error_Msg", exception.getMessage()+"<br> Project "+customerPropertyDetailsPojo.getSiteName()+" Flat No "+customerPropertyDetailsPojo.getFlatNo()+".");
		
		email.setEmailBodyText(errorMsg);
		
		if(Util.isNotEmptyObject(internalEmpMail)) {
			email.setCcs(internalEmpMail.split(","));//cc in internal team, for default banker error
		}
		
		mailServiceImpl.sendDefaultBankerErrorMail(email);
	}


	@Override
	public List<NOCDocumentsList>  getNOCDocumentsList(BookingFormRequest bookingFormRequest) {
		logger.info("***** Control inside the BookingFormServiceImpl.getNOCDocumentsList() *****");
		List<NOCDocumentsList> finalDocList =new ArrayList<NOCDocumentsList>();
		Set<Long> flatBookingids =new HashSet<Long>();
		List<NOCDcoumetsUrls> finalUrlsLsit =null;
		List<NOCDocumentsList> docList= bookingFormServiceDaoImpl.getNOCDocumentsList(bookingFormRequest);
		for(NOCDocumentsList pojo :docList)
		{
			flatBookingids.add(pojo.getFlatBookingId());
		}
		for (Long flatBookingId : flatBookingids) {
			NOCDocumentsList finalBean = null;
			finalUrlsLsit =new ArrayList<NOCDcoumetsUrls>();
			for (NOCDocumentsList pojo : docList) {
				if (flatBookingId.equals(pojo.getFlatBookingId())) {
					finalBean= new NOCDocumentsList();
					String docName="";
					NOCDcoumetsUrls urls = new NOCDcoumetsUrls();
					BeanUtils.copyProperties(pojo, finalBean);
					urls.setNocReleaseId(pojo.getNocReleaseId());
					urls.setNocDocumetId(pojo.getNocDocumetId());
					urls.setNocURLLocation(pojo.getNocURLLocation());
					urls.setDocLocation(pojo.getDocLocation());
					if(Util.isNotEmptyObject(pojo.getDocLocation())) {
					 docName=removeTillWord(pojo.getDocLocation(),"NOC");
					}
					urls.setDocName(docName);
					finalBean.setDocLocation(null);
					finalBean.setNocURLLocation(null);
					if (Util.isNotEmptyObject(pojo.getDocLocation())) {
						finalUrlsLsit.add(urls);
						finalBean.setIsNocGenerated("Yes");
					} else {
						finalBean.setIsNocGenerated("No");
					}
					
				}
			}
			if (Util.isNotEmptyObject(finalUrlsLsit)) {
				finalBean.setNocDocUrls(finalUrlsLsit);
				finalUrlsLsit=null;
			}
			if (Util.isNotEmptyObject(finalBean)) {
			finalDocList.add(finalBean);
			}
		}
		return finalDocList;
	}
	
	@Override
	public List<CustomerData> getCustomerData(CustomerInfo customerInfo) {
		logger.info("***** Control inside the BookingFormServiceImpl.getCustomerData() *****");
		List<CustomerData> list = bookingFormServiceDaoImpl.getCustomerData(customerInfo, null);
		return list;
	}
	
	
	public List<FlatBookingInfo> loadUnitDetailsList(BookingFormRequest bookingFormRequest) {
		List<FlatBookingInfo> flatBookingInfos = getFlatBookingInfoList(bookingFormRequest);
		return flatBookingInfos;
	}
	
	@Override
	public List<FlatBookingInfo> getFlatBookingInfoList(BookingFormRequest bookingFormRequest) {
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = bookingFormServiceDaoImpl.getCustomerPropertyDetails(bookingFormRequest);
		if(Util.isEmptyObject(customerPropertyDetailsPojos)) {
			return new ArrayList<FlatBookingInfo>();
		}
		bookingFormRequest.setFlatId(customerPropertyDetailsPojos.get(0).getFlatId());
		List<FlatPojo> flatPojos = bookingFormServiceDaoImpl.getFlat(bookingFormRequest);
		List<FlatDetailsPojo> flatDetailsPojos = bookingFormServiceDaoImpl.getFlatDetails(bookingFormRequest);
		List<FlatCostPojo> flatCostPojos = bookingFormServiceDaoImpl.getFlatCostByFlatBooking(bookingFormRequest);
		bookingFormRequest.setFloorId(customerPropertyDetailsPojos.get(0).getFlooId());
		List<FloorPojo> floorPojos = bookingFormServiceDaoImpl.getFloor(bookingFormRequest);
		bookingFormRequest.setBlockId(customerPropertyDetailsPojos.get(0).getBlockId());
		List<BlockPojo> blockPojos = bookingFormServiceDaoImpl.getBlock(bookingFormRequest);
		bookingFormRequest.setSiteId(customerPropertyDetailsPojos.get(0).getSiteId());
		List<SitePojo> sitePojos = bookingFormServiceDaoImpl.getSite(bookingFormRequest);
		/*
		List<AminitiesInfraCostPojo> aminitiesInfraCostPojos = new ArrayList<>();
		if (flatCostPojos != null && flatCostPojos.size() > 0) {
			bookingFormRequest.setFlatCostId(flatCostPojos.get(0).getFlatCostId());
			aminitiesInfraCostPojos = bookingFormServiceDaoImpl.getAminitiesInfraCost(bookingFormRequest);
		}
		else {
			aminitiesInfraCostPojos.add(new AminitiesInfraCostPojo());
		} */
		
		List<FlatBookingInfo> flatBookingInfos = bookingFormMapper.flat$flatDetails$flatCost$floor$block$sitePojos$aminitiesInfraCostPojosToFlatBookingInfo(flatPojos,
						flatDetailsPojos, flatCostPojos, floorPojos, blockPojos, sitePojos, bookingFormServiceDaoImpl,bookingFormRequest);
		return flatBookingInfos;
	}
	
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public Map<String, Object> generateLoanNOCLetter(BookingFormRequest bookingFormRequest) throws Exception  {
		logger.info("*** The control is inside the generateNOCLetter in EmployeeFinancialServiceImpl ***");
		Properties prop= responceCodesUtil.getApplicationProperties();
		boolean isThisPreview = (bookingFormRequest.getActionStr()!=null && bookingFormRequest.getActionStr().equals("Preview"));
		long portNumber = bookingFormRequest.getPortNumber();
		BookingFormRequest bookingFormRequestCopy = new BookingFormRequest(); 
		BeanUtils.copyProperties(bookingFormRequest,bookingFormRequestCopy);
		bookingFormRequestCopy.setStatusId(Status.ACTIVE.getStatus());
		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo();
		employeeFinancialServiceInfo.setBookingFormIds(Arrays.asList(bookingFormRequest.getFlatBookingId()));
		employeeFinancialServiceInfo.setSiteId(bookingFormRequest.getSiteId());
		employeeFinancialServiceInfo.setSiteIds(Arrays.asList(bookingFormRequest.getSiteId()));
		
		String bankerNOCSites = prop.getProperty("BANKER_NOC_SITES");
		List<String> sites = Arrays.asList(bankerNOCSites.split("\\s*,\\s*"));
		if(!sites.contains(bookingFormRequest.getSiteId().toString())) {
			throw new EmployeeFinancialServiceException("no NOC preview.");
	    }
			/* getting Customer Details */
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList = employeeFinancialServiceDao.getCustomerPropertyDetails(employeeFinancialServiceInfo);
		if(Util.isEmptyObject(customerPropertyDetailsPojoList)) {
				throw new EmployeeFinancialServiceException("Failed to generate NOC letter, No data found for customer.");
		}
		
		
		final CustomerPropertyDetailsInfo customerPropertyDetailsInfo =  new EmployeeFinancialMapper().copyPropertiesFromCustomerPropertyDetailsPojoToCustomerPropertyDetailsInfo(customerPropertyDetailsPojoList.get(0));
		employeeFinancialServiceInfo.setCustomerPropertyDetailsInfo(customerPropertyDetailsInfo);
		employeeFinancialServiceInfo.setBookingFormId(bookingFormRequest.getFlatBookingId());		
	
		
			//get the details for NOC letter
			final Map<String, Object> dataForGenerateAllotmentLetterHelper = getTheCustomerDetailsForLetter(employeeFinancialServiceInfo,bookingFormRequestCopy,customerPropertyDetailsInfo);
			System.out.println(dataForGenerateAllotmentLetterHelper);
			
			
			String folderFilePath = "";
			String folderFileUrl = "";

			if (portNumber == Long.valueOf(MetadataId.LOCAL_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("LOAN_NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("LOAN_NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.UAT_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("LOAN_NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("LOAN_NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.CUG_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("LOAN_NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("LOAN_NOC_FILE_URL_PATH");
			}  else if (portNumber == Long.valueOf(MetadataId.HTTPS_CUG_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("LOAN_NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("LOAN_NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.LIVE_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("LOAN_NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("LOAN_NOC_FILE_URL_PATH");
			} else if (portNumber == Long.valueOf(MetadataId.HTTPS_LIVE_PORT_NUMBER.getName())) {
				folderFilePath = prop.getProperty("LOAN_NOC_FILE_DIRECTORY_PATH");
				folderFileUrl = prop.getProperty("LOAN_NOC_FILE_URL_PATH");
			}else {
				logger.error("Path missing to save the File. Please contact to Support Team");
				throw new EmployeeFinancialServiceException("Path missing to save the File, Please contact to Support Team.");
			}
			if (Util.isEmptyObject(folderFilePath) || Util.isEmptyObject(folderFilePath)) {
				throw new EmployeeFinancialServiceException("File path found empty for storing pdf's.");
			}
			if(isThisPreview) {//if this is preview
				dataForGenerateAllotmentLetterHelper.put("folderFilePath", folderFilePath+bookingFormRequest.getActionStr()+"\\");
				dataForGenerateAllotmentLetterHelper.put("folderFileUrl", folderFileUrl+bookingFormRequest.getActionStr()+"/");
				if(true) {
					//deleting previous zip files
					new PdfHelper().folderDeleteAndSubFolder(new File(folderFilePath+"/"+bookingFormRequest.getActionStr()+"/"),0,bookingFormRequest.getSiteId().toString());
				}
			} else {
				dataForGenerateAllotmentLetterHelper.put("folderFilePath", folderFilePath);
				dataForGenerateAllotmentLetterHelper.put("folderFileUrl", folderFileUrl);
			}
			
			generateLoanNOCLetter(employeeFinancialServiceInfo,dataForGenerateAllotmentLetterHelper,customerPropertyDetailsInfo,bookingFormRequestCopy);
			
			dataForGenerateAllotmentLetterHelper.put("successMasg", "success");
		return dataForGenerateAllotmentLetterHelper;
	}
	
	public void generateLoanNOCLetter(EmployeeFinancialServiceInfo serviceInfo,final Map<String, Object> dataForGenerateNOCLetterHelper,
			final CustomerPropertyDetailsInfo customerPropertyDetailsInfo, BookingFormRequest bookingFormRequestCopy) throws Exception {
		logger.info("***** Control inside the BookingFormServiceImpl.generateLoanNOCLetter() *****");
	    List<FileInfo> files =employeeFinancialHelper.generateLoanNOCLetterHelper(dataForGenerateNOCLetterHelper,customerPropertyDetailsInfo);
		dataForGenerateNOCLetterHelper.put("NOCLetterFile", files);
	}
	
	@Override
	public List<CustomerKYCDocumentSubmitedInfo> getKycDocumentsList(BookingFormRequest bookingFormRequest) {
		logger.info("***** Control inside the BookingFormServiceImpl.getKycDocumentsList() *****");
		List<CustomerKYCDocumentSubmitedInfo> list = bookingFormServiceDaoImpl.getKycDocumentsList( bookingFormRequest);
		return list;
	}
	
	
	@Override
	public Map<String,Object> getFlatDetails(BookingFormRequest bookingFormRequest) {
		logger.info("***** Control inside the BookingFormServiceImpl.getFlatDetails() *****");
		Map<String,Object> map = new HashMap<>();
		List<FlatPojo> flatPojos = bookingFormServiceDaoImpl.getFlat(bookingFormRequest);
		List<FlatDetailsPojo> flatDetailsPojos = bookingFormServiceDaoImpl.getFlatDetails(bookingFormRequest);
		List<CustomerData> list=bookingFormServiceDaoImpl.getflatDetailsByflat(bookingFormRequest);
		bookingFormRequest.setFloorId(list.get(0).getFlooId());
		List<FloorPojo> floorPojos = bookingFormServiceDaoImpl.getFloor(bookingFormRequest);
		bookingFormRequest.setBlockId(list.get(0).getBlockId());
		List<BlockPojo> blockPojos = bookingFormServiceDaoImpl.getBlock(bookingFormRequest);
		bookingFormRequest.setSiteId(list.get(0).getSiteId());
		List<SitePojo> sitePojos = bookingFormServiceDaoImpl.getSite(bookingFormRequest);
		List<AminititesInfraDetails> aminitiesList = bookingFormServiceDaoImpl.getAminities( bookingFormRequest);
		map.put("sitePojos", sitePojos);
		map.put("blockPojos", blockPojos);
		map.put("floorPojos", floorPojos);
		map.put("flatPojos", flatPojos);
		map.put("flatDetailsPojos", flatDetailsPojos);
		map.put("aminitiesList", aminitiesList);
		return map;
	}
	
	
	@Override
	public List<CustomerData> getNonBookedDetails(BookingFormRequest bookingFormRequest) {
		logger.info("***** Control inside the BookingFormServiceImpl.getNonBookedDetails() *****");
		List<CustomerData> list = bookingFormServiceDaoImpl.getNonBookedDetails(bookingFormRequest);
		return list;
	}
	
	@Override
	public List<CustomerAddressPojo> getCityList(CustomerInfo customerInfo) {
		logger.info("***** Control inside the BookingFormServiceImpl.getCityList() *****");
		List<CustomerAddressPojo> list = bookingFormServiceDaoImpl.getcityList(customerInfo);
		return list;
	}
	
		@Override
	public List<CustomerAddressPojo> getCountryList() {
		logger.info("***** Control inside the BookingFormServiceImpl.getCountryList() *****");
		List<CustomerAddressPojo> list = bookingFormServiceDaoImpl.getCountryList();
		return list;
	}

	@Override
	public List<FinSchemePojo> getFinSchemes(BookingFormRequest bookingFormRequest) {
		logger.info("***** Control inside the BookingFormServiceImpl.getFinSchemes() *****");
		List<FinSchemePojo> list = bookingFormServiceDaoImpl.getFinSchemes(bookingFormRequest);
		return list;
	}
	
	@Override
	public List<FlatBookingPojo> getSalesForcesBookingIds(BookingFormRequest bookingFormRequest) {
		logger.info("***** Control inside the BookingFormServiceImpl.getSalesForcesBookingIds() *****");
		List<FlatBookingPojo> list = bookingFormServiceDaoImpl.getSalesForcesBookingIds(bookingFormRequest);
		
		
		ListIterator<FlatBookingPojo> listIterator =list.listIterator();
		while(listIterator.hasNext()) {//deleting null salesbooking flats values
			FlatBookingPojo pojo=listIterator.next();
			if(Util.isEmptyObject(pojo.getSalesforceBookingId())){
				listIterator.remove();
			}
		}
		return list;
	}

}
