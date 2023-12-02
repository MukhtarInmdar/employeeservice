package com.sumadhura.employeeservice.service.mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.CustomerPropertyDetails;
import com.sumadhura.employeeservice.dto.EmployeeFinTranPaymentSetOffRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialResponse;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionResponse;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinAnnyApproveStatResponse;
import com.sumadhura.employeeservice.dto.FinAnnyEntryCommentsResponse;
import com.sumadhura.employeeservice.dto.FinAnnyEntryDocResponse;
import com.sumadhura.employeeservice.dto.FinAnonymousEntryResponse;
import com.sumadhura.employeeservice.dto.FinBankResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormDemandNoteResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormExcessAmountResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormLglCostDtlsResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormModiCostDtlsResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormModiCostResponse;
import com.sumadhura.employeeservice.dto.FinMileStoneCLSMappingBlocksResponse;
import com.sumadhura.employeeservice.dto.FinPenaltyTaxResponse;
import com.sumadhura.employeeservice.dto.FinProjectAccountResponse;
import com.sumadhura.employeeservice.dto.FinTransactionApprStatResponse;
import com.sumadhura.employeeservice.dto.FinTransactionChangedDtlsResponse;
import com.sumadhura.employeeservice.dto.FinTransactionCommentsResponse;
import com.sumadhura.employeeservice.dto.FinTransactionEntryDetailsResponse;
import com.sumadhura.employeeservice.dto.FinTransactionEntryDocResponse;
import com.sumadhura.employeeservice.dto.FinTransactionEntryResponse;
import com.sumadhura.employeeservice.dto.FinTransactionForResponse;
import com.sumadhura.employeeservice.dto.FinTransactionModeResponse;
import com.sumadhura.employeeservice.dto.FinTransactionSetOffResponse;
import com.sumadhura.employeeservice.dto.FinTransactionTypeResponse;
import com.sumadhura.employeeservice.dto.FinTransferModeResponse;
import com.sumadhura.employeeservice.dto.FinancialBookingFormAccountsRequest;
import com.sumadhura.employeeservice.dto.FinancialDemandNoteMS_TRN_Request;
import com.sumadhura.employeeservice.dto.FinancialGstDetailsRequest;
import com.sumadhura.employeeservice.dto.FinancialProjectMileStoneRequest;
import com.sumadhura.employeeservice.dto.FinancialProjectMileStoneResponse;
import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;
import com.sumadhura.employeeservice.enums.FinEnum;
import com.sumadhura.employeeservice.enums.FinTransactionFor;
import com.sumadhura.employeeservice.enums.FinTransactionMode;
import com.sumadhura.employeeservice.enums.FinTransactionType;
import com.sumadhura.employeeservice.enums.FinTransferMode;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.persistence.dto.AddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnnyApproveStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnnyEntryCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnnyEntryDocPojo;
import com.sumadhura.employeeservice.persistence.dto.FinAnonymousEntryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBankPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmDemNteSchTaxMapPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmFlatKhataTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmLglCostTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBokFrmMaintenanceTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountPaymentDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountPaymentPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountSummaryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsResponse;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormAccountsStatementPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormCorpusFundPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormDemandNotePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormExcessAmountPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormExcessAmountUsagePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormFlatKhataDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormFlatKhataPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormLegalCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormLglCostDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMaintenanceDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMaintenancePojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormMilestonesPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormModiCostDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormModiCostPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormReceiptsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinBookingFormRefundableAdvancePojo;
import com.sumadhura.employeeservice.persistence.dto.FinMSChangedDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinModificationInvoicePojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyPojo;
import com.sumadhura.employeeservice.persistence.dto.FinPenaltyTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinProjDemNoteStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinProjectAccountPojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemePojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemeTaxMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTaxPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionChangedDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionEntryDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionEntryDocPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionEntryPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionForPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionModePojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionSetOffPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionTypePojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransferModePojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialMileStoneClassifideMappingBlocksPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialMileStoneClassifidesPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialProjectMileStonePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingSchemeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatCancellationCostPojo;
import com.sumadhura.employeeservice.persistence.dto.ModicationInvoiceAppRej;
import com.sumadhura.employeeservice.persistence.dto.OfficeDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.SiteDetailsPojo;
import com.sumadhura.employeeservice.service.dto.AddressInfo;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinModiCostInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinTranPaymentSetOffInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialMultipleTRNInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialTransactionServiceInfo;
import com.sumadhura.employeeservice.service.dto.FinAnonymousEntryInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormAccountsInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormLegalCostInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormLglCostDtlsInfo;
import com.sumadhura.employeeservice.service.dto.FinBookingFormMstSchTaxMapInfo;
import com.sumadhura.employeeservice.service.dto.FinPenalityInfo;
import com.sumadhura.employeeservice.service.dto.FinTransactionEntryDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinancialDemandNoteMS_TRN_Info;
import com.sumadhura.employeeservice.service.dto.FinancialGstDetailsInfo;
import com.sumadhura.employeeservice.service.dto.FinancialProjectMileStoneInfo;
import com.sumadhura.employeeservice.service.dto.FinancialSchemeInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;
import com.sumadhura.employeeservice.service.dto.SiteDetailsInfo;
import com.sumadhura.employeeservice.util.CurrencyUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;
import com.sun.istack.NotNull;
/**
 * @author @NIKET CH@V@N
 * @since 13-01-2020
 * @time 04:50 PM
 * @description this method is responsible to copy POJO object properties to Response object 
 */

@Component("employeeFinancialMapper")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EmployeeFinancialMapper{

	private Logger log = Logger.getLogger(EmployeeFinancialMapper.class);
	
	private String name;
	
	//private Class clazz;

private BookingFormMapper  bookingFormMapper = new BookingFormMapper();	

	@Autowired
	@Qualifier("dozerBeanMapper")
	Mapper dozerBeanMapper1 = new DozerBeanMapper();
	static Mapper dozerBeanMapper = DozerBeanMapperSingletonWrapper.getInstance();
	
	@Autowired
	private CurrencyUtil currencyUtil;
	
	/*@Autowired
	private ResponceCodesUtil responceCodesUtil;*/
	
	private RoundingMode roundingMode = RoundingMode.HALF_UP;
	private int roundingModeSize = 2;
	
	//Bean mapping
    BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
        @Override
        protected void configure() {
            String dateFormat = "dd-MM-yyyy";
            mapping(FinancialProjectMileStoneRequest.class, 
            		FinancialProjectMileStoneInfo.class, 
                    TypeMappingOptions.wildcard(true), 
                    TypeMappingOptions.dateFormat(dateFormat));//.fields("mileStoneDueDate", "mileStoneDueDate");
        }
    };

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static Timestamp dateForValidation = null;

	public EmployeeFinancialMapper() {
		List<String> str=new ArrayList<>();
		str.add("dozerBeanMapping.xml");
		 
		try {
			dateForValidation = TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(dateFormat.parse("15-12-2012").getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	  //  dozerBeanMapper.addMapping(beanMappingBuilder);
		//List<String> s=new ArrayList<String>();
		/*
		 * s.add("classpath:dozerBeanMapping.xml"); dozerBeanMapper=new
		 * DozerBeanMapper(); dozerBeanMapper.setMappingFiles(s);
		 */
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * @param employeeFinancialRequest
	 * @param actionUrl 
	 * @return
	 * @throws Exception 
	 */
	public EmployeeFinancialServiceInfo employeeFinancialRequestToemployeeFinancialInfo(EmployeeFinancialRequest employeeFinancialRequest, String actionUrl) throws Exception {
		//log.info(" ***** EmployeeFinancialMapper.employeeFinancialRequestToemployeeFinancialInfo() ***** ");
		EmployeeFinancialServiceInfo employeeFinancialInfo = new EmployeeFinancialServiceInfo();
		//BeanUtils.copyProperties(employeeFinancialRequest, employeeFinancialInfo);
		
        employeeFinancialInfo = dozerBeanMapper.map(employeeFinancialRequest, EmployeeFinancialServiceInfo.class);
        //if block id and flat id is not empty then, demand note is generating for single flat
        if(Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds()) && Util.isNotEmptyObject(employeeFinancialRequest.getFlatIds())) {
        	//if these bolth values are not empty then demand note generating using flat wise
        	employeeFinancialRequest.setDemandNoteSelectionType(FinEnum.SEND_SINGLE_MULTIPLE.getName());
        	employeeFinancialRequest.setBlockIds(null);//making block id's null, so data will be loaded using flatIds and siteId
        }/* else if(Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds()) && Util.isEmptyObject(employeeFinancialRequest.getFlatIds())) {
        	employeeFinancialRequest.setDemandNoteSelectionType(FinEnum.BLOCK_WISE.getName());
        	employeeFinancialRequest.setFlatIds(null);
        } else if(Util.isEmptyObject(employeeFinancialRequest.getBlockIds()) && Util.isNotEmptyObject(employeeFinancialRequest.getFlatIds())) {
        	//if these bolth values are not empty then demand note generating using flat wise
        	employeeFinancialRequest.setDemandNoteSelectionType(FinEnum.SEND_SINGLE_MULTIPLE.getName());
        	employeeFinancialRequest.setBlockIds(null);//making block id's null, so data will be loaded using flatIds and siteId
        } else  if(Util.isEmptyObject(employeeFinancialRequest.getBlockIds()) && Util.isEmptyObject(employeeFinancialRequest.getFlatIds())) {
        	employeeFinancialRequest.setDemandNoteSelectionType(FinEnum.ALL_BLOCKS.getName());
        	//if both are empty then selecting all blocks of alias names
        	employeeFinancialRequest.setFlatIds(null);
        	employeeFinancialRequest.setBlockIds(null);
        }*/
        log.info("employeeFinancialRequestToemployeeFinancialInfo, selection : "+employeeFinancialRequest.getDemandNoteSelectionType()+", block : "+employeeFinancialRequest.getBlockIds()+", flat :"+employeeFinancialRequest.getFlatIds());
        //String s = null; s.trim();
        
        EmployeeFinancialMapper map= new EmployeeFinancialMapper();
		map.setName("da");
		@SuppressWarnings("unused")
		EmployeeFinancialMapper map1= new EmployeeFinancialMapper();
 
		Long interestSelectionType=0L;
		//is demand note selected with interest or without interest, if with interest then in demand note pdf one more column will be added as *Interest Amount*
		if(Util.isNotEmptyObject(employeeFinancialRequest.getIsInterestOrWithOutInterest())) {
			interestSelectionType = employeeFinancialRequest.getIsInterestOrWithOutInterest().equalsIgnoreCase("With Interest")?Status.With_Interest.getStatus():
				(employeeFinancialRequest.getIsInterestOrWithOutInterest().equalsIgnoreCase("With Out Interest")?Status.With_Out_Interest.getStatus():0L);

		}
		//
		if(Util.isNotEmptyObject(employeeFinancialRequest.getIsReGenerateDemandNote())) {
			if(employeeFinancialRequest.getIsReGenerateDemandNote().equalsIgnoreCase("true")) {
				employeeFinancialInfo.setReGenerateDemandNote(true);
			}else {
				employeeFinancialInfo.setReGenerateDemandNote(false);
			}
		}else {
			employeeFinancialInfo.setReGenerateDemandNote(false);
		}

		if(Util.isNotEmptyObject(employeeFinancialRequest.getIsThisUpdateDemandNote())) {
			if(employeeFinancialRequest.getIsThisUpdateDemandNote().equalsIgnoreCase("true")) {
				employeeFinancialInfo.setThisUpdateDemandNote(true);
			}else {
				employeeFinancialInfo.setThisUpdateDemandNote(false);
			}
		}else {
			employeeFinancialInfo.setThisUpdateDemandNote(false);
		}
		
		if (Util.isNotEmptyObject(employeeFinancialRequest.getIsNewCustomer())) {//not in use
			if(employeeFinancialRequest.getIsNewCustomer().equalsIgnoreCase("true")) {
				employeeFinancialInfo.setNewCustomer(Boolean.TRUE);
			}else {
				employeeFinancialInfo.setNewCustomer(Boolean.FALSE);
			}
		} else {
			employeeFinancialInfo.setNewCustomer(Boolean.FALSE);
		}
		
		if(Util.isNotEmptyObject(employeeFinancialRequest.getIsShowGstInPDF())) {//not in use
			if(employeeFinancialRequest.getIsShowGstInPDF().equalsIgnoreCase("true")) {
				employeeFinancialInfo.setShowGstInPDF(Boolean.TRUE);
			} else {
				employeeFinancialInfo.setShowGstInPDF(Boolean.FALSE);
			}
		}else {
			employeeFinancialInfo.setShowGstInPDF(Boolean.FALSE);
		}
		final List<Long> projectMileStoneIds = new ArrayList<Long>();//holding unique milestone id
		String requestUrl = employeeFinancialRequest.getRequestUrl()==null?"":employeeFinancialRequest.getRequestUrl();
		//Timestamp msDemandNoteDate = new Timestamp(new Date().getTime());
		if (Util.isNotEmptyObject(employeeFinancialRequest.getFinancialProjectMileStoneRequests())) {
			List<FinancialProjectMileStoneInfo> empFinancialDtlsResponseList = new ArrayList<FinancialProjectMileStoneInfo>();
			for (FinancialProjectMileStoneRequest sourceMileStoneRequest : employeeFinancialRequest.getFinancialProjectMileStoneRequests()) {
				if (Util.isNotEmptyObject(sourceMileStoneRequest)) {
					FinancialProjectMileStoneInfo destMileStoneNameDetailsObject = new FinancialProjectMileStoneInfo();
					//removing duplicate milestone from request object
					if(sourceMileStoneRequest.getProjectMilestoneId()==null || projectMileStoneIds.contains(sourceMileStoneRequest.getProjectMilestoneId())) {
						continue;
					}
					
					//checking dates valid or not
					if(!"Dummy MileStone for Regenerate Demand Note".equals(sourceMileStoneRequest.getMilestoneName())) {
						//if demand note and due date is deafult date like 1967 that time thorw the alert, as dates are wrong
						if (sourceMileStoneRequest.getDemandNoteDate()!=null && sourceMileStoneRequest.getDemandNoteDate().before(dateForValidation)
								) {
							throw new EmployeeFinancialServiceException("Please give correct demand note date, For Milestone name : "+sourceMileStoneRequest.getMilestoneName());	
						} else if (sourceMileStoneRequest.getMileStoneDueDate()!=null && sourceMileStoneRequest.getMileStoneDueDate().before(dateForValidation)
								) {
							throw new EmployeeFinancialServiceException("Please give correct demand note due date, For Milestone name : "+sourceMileStoneRequest.getMilestoneName());
						}
					}
					
					projectMileStoneIds.add(sourceMileStoneRequest.getProjectMilestoneId());
					Timestamp demandNoteDate = employeeFinancialRequest.getDemandNoteDate();
					sourceMileStoneRequest.setMilestoneDemandNoteDate(sourceMileStoneRequest.getDemandNoteDate());
					if(Util.isNotEmptyObject(sourceMileStoneRequest.getDemandNoteDate())) {
						sourceMileStoneRequest.setInterestCalculationUptoDate(
								TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						//msDemandNoteDate = sourceMileStoneRequest.getDemandNoteDate();
					} else {
						if (demandNoteDate == null) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(TimeUtil.removeOneDay(new Timestamp(new Date().getTime())));
						} else {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(demandNoteDate)));
						}
					}
					//IMP code
					/*if(actionUrl.equals("generateDemandNote.spring") && requestUrl.equals("ViewCustomerData")) {
						if(interestSelectionType == Status.With_Interest.getStatus()) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
						if(sourceMileStoneRequest.getMilestoneName()!=null && sourceMileStoneRequest.getMilestoneName().equals("Dummy MileStone for Regenerate Demand Note")) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
					} else if (actionUrl.equals("generatedDemandNotePreview.spring") && requestUrl.equals("ViewCustomerData")) {
						if(interestSelectionType == Status.With_Interest.getStatus()) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
						if(sourceMileStoneRequest.getMilestoneName()!=null && sourceMileStoneRequest.getMilestoneName().equals("Dummy MileStone for Regenerate Demand Note")) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
					} else if (actionUrl.equals("generateDemandNote.spring")) {
						if(interestSelectionType == Status.With_Interest.getStatus()) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
						if(sourceMileStoneRequest.getMilestoneName()!=null && sourceMileStoneRequest.getMilestoneName().equals("Dummy MileStone for Regenerate Demand Note")) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
					} else if (actionUrl.equals("generatedDemandNotePreview.spring")) {
						if(interestSelectionType == Status.With_Interest.getStatus()) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
						if(sourceMileStoneRequest.getMilestoneName()!=null && sourceMileStoneRequest.getMilestoneName().equals("Dummy MileStone for Regenerate Demand Note")) {
							sourceMileStoneRequest.setInterestCalculationUptoDate(
									TimeUtil.removeOneDay(TimeUtil.removeTimePartFromTimeStamp1(sourceMileStoneRequest.getDemandNoteDate())));
						}
					}*/
					
					if(sourceMileStoneRequest.getMilestoneName()!=null && sourceMileStoneRequest.getMilestoneName().equals("Dummy MileStone for Regenerate Demand Note")) {
						if(requestUrl.equals("ViewCustomerData")) {//menas single page
							employeeFinancialRequest.setDemandNoteDate(TimeUtil.removeTimePartFromTimeStamp1(new Timestamp(new Date().getTime())));
							employeeFinancialInfo.setDemandNoteDate(employeeFinancialRequest.getDemandNoteDate());
						}
						sourceMileStoneRequest.setMilestoneDate(sourceMileStoneRequest.getInterestCalculationUptoDate());
						sourceMileStoneRequest.setMileStoneDueDate(sourceMileStoneRequest.getMilestoneDate());
					}
					
					destMileStoneNameDetailsObject = dozerBeanMapper.map(sourceMileStoneRequest, FinancialProjectMileStoneInfo.class);
					
					if(!employeeFinancialInfo.isReGenerateDemandNote()) {//if this is not regenerate demand note, then take demand note date from milestone object
						destMileStoneNameDetailsObject.setDemandNoteDate(sourceMileStoneRequest.getDemandNoteDate());
						employeeFinancialRequest.setDemandNoteDate(sourceMileStoneRequest.getDemandNoteDate());
						employeeFinancialInfo.setDemandNoteDate(sourceMileStoneRequest.getDemandNoteDate());
						if(requestUrl.equals("ViewCustomerData") && sourceMileStoneRequest.getDemandNoteDate() == null && demandNoteDate!=null ) {
							destMileStoneNameDetailsObject.setDemandNoteDate(demandNoteDate);
							employeeFinancialInfo.setDemandNoteDate(demandNoteDate);
						}
						//interest calculation upto demand note date - 1
					} else {
						destMileStoneNameDetailsObject.setDemandNoteDate(employeeFinancialRequest.getDemandNoteDate());
					}

					//destMileStoneNameDetailsObject.setDemandNoteDate(sourceEmpMileStoneDetailsReq.getDemandNoteDate());
					//destMileStoneNameDetailsObject.setMilestoneDemandNoteDate(sourceEmpMileStoneDetailsReq.getMilestoneDemandNoteDate());
					
					destMileStoneNameDetailsObject.setReGenerateDemandNote(employeeFinancialInfo.isReGenerateDemandNote());
					destMileStoneNameDetailsObject.setShowGstInPDF(employeeFinancialInfo.isShowGstInPDF());//not in use
					destMileStoneNameDetailsObject.setInterestSelectionType(interestSelectionType);
					destMileStoneNameDetailsObject.setCreatedBy(Long.valueOf(employeeFinancialRequest.getEmpId()));
				 	destMileStoneNameDetailsObject.setStatusId(Status.ACTIVE.getStatus());
				 	destMileStoneNameDetailsObject.setDemandNoteSelectionType(Util.isEmptyObject(employeeFinancialRequest.getDemandNoteSelectionType())?"":employeeFinancialRequest.getDemandNoteSelectionType());
				 	if(Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
				 		destMileStoneNameDetailsObject.setSiteId(employeeFinancialRequest.getSiteId());	
				 	}else if(Util.isNotEmptyObject(employeeFinancialRequest.getSiteIds())) {
				 		destMileStoneNameDetailsObject.setSiteId(employeeFinancialRequest.getSiteIds().get(0));
				 		employeeFinancialInfo.setSiteId(employeeFinancialRequest.getSiteIds().get(0));
				 	}
				 	
				 	empFinancialDtlsResponseList.add(destMileStoneNameDetailsObject);
				}
			}
			employeeFinancialInfo.setFinancialProjectMileStoneRequests(empFinancialDtlsResponseList);
		}
		if (Util.isNotEmptyObject(employeeFinancialRequest.getDemandNoteSelectionTypeValues())) {
			List<CustomerPropertyDetailsInfo> empFinancialDtlsResponseList = new ArrayList<CustomerPropertyDetailsInfo>();
			for (CustomerPropertyDetails sourceCustomerPropertyDetails : employeeFinancialRequest.getDemandNoteSelectionTypeValues()) {
				CustomerPropertyDetailsInfo destCustomerPropertyDetailsInfo = new CustomerPropertyDetailsInfo();
				//BeanUtils.copyProperties(sourceCustomerPropertyDetails, destCustomerPropertyDetailsInfo);
				destCustomerPropertyDetailsInfo = dozerBeanMapper.map(sourceCustomerPropertyDetails, CustomerPropertyDetailsInfo.class);
				
				empFinancialDtlsResponseList.add(destCustomerPropertyDetailsInfo);
			}
			employeeFinancialInfo.setDemandNoteSelectionTypeValues(empFinancialDtlsResponseList);
		}
		
		if (Util.isNotEmptyObject(employeeFinancialRequest.getBookingFormAccountsRequests())) {
			List<FinBookingFormAccountsInfo> empFinancialDtlsResponseList = new ArrayList<FinBookingFormAccountsInfo>();
			for (FinancialBookingFormAccountsRequest bookingFormAccountsRequest : employeeFinancialRequest.getBookingFormAccountsRequests()) {
				FinBookingFormAccountsInfo accountsInfo =	dozerBeanMapper.map(bookingFormAccountsRequest, FinBookingFormAccountsInfo.class);
				empFinancialDtlsResponseList.add(accountsInfo);
			}
			employeeFinancialInfo.setBookingFormAccountsRequests(empFinancialDtlsResponseList);
		}
		//scheme related regular, PMAY
		if (Util.isNotEmptyObject(employeeFinancialRequest.getFinancialGstDetailsRequests())) {
			List<FinancialGstDetailsInfo> financialGstDetailsInfos = new ArrayList<FinancialGstDetailsInfo>();
			for (FinancialGstDetailsRequest financialGstDetailsRequest : employeeFinancialRequest.getFinancialGstDetailsRequests()) {
				FinancialGstDetailsInfo financialGstDetailsInfo =	dozerBeanMapper.map(financialGstDetailsRequest, FinancialGstDetailsInfo.class);
				financialGstDetailsInfos.add(financialGstDetailsInfo);
			}
			employeeFinancialInfo.setFinancialGstDetailsInfos(financialGstDetailsInfos);
		}
		return employeeFinancialInfo;
	}

	/**
	 * 
	 * @param employeeFinancialPojo
	 * @return
	 */
	public List<EmployeeFinancialResponse> setEmployeeFinancialPojoListTosetEmployeeFinancialResponseList(
			List<FinancialMileStoneClassifidesPojo> employeeFinancialPojo) {
log.info(" ****** EmployeeFinancialMapper.setEmployeeFinancialPojoListTosetEmployeeFinancialResponseList() ***** ");
		List<EmployeeFinancialResponse> financialMileStoneAliasNameList = new ArrayList<EmployeeFinancialResponse>();
		for (FinancialMileStoneClassifidesPojo empMileStoneAliasNamePojo : employeeFinancialPojo) {
			EmployeeFinancialResponse empFinancialResponse = new EmployeeFinancialResponse();
			if (Util.isNotEmptyObject(empMileStoneAliasNamePojo)) {
				BeanUtils.copyProperties(empMileStoneAliasNamePojo, empFinancialResponse);
				financialMileStoneAliasNameList.add(empFinancialResponse);
			}
		
		}
		return financialMileStoneAliasNameList;
	}
	
	private static List<Map<String, Object>> milestoneStatusMapData = new ArrayList<>();
	static {
		milestoneStatusMapData = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("Key", Status.MS_COMPLETED.getDescription());
		map.put("Value", Status.MS_COMPLETED.getStatus());
		milestoneStatusMapData.add(map);
		map = new HashMap<>();
		map.put("Key", Status.MS_INCOMPLETE.getDescription());
		map.put("Value", Status.MS_INCOMPLETE.getStatus());

		 milestoneStatusMapData.add(map);
	}
	/**
	 * @param empFinancialMileStoneDtlsPojo
	 * @param financialMileStoneSetsPojo
	 * @param employeeFinancialInfo 
	 * @return
	 */
 	public List<EmployeeFinancialResponse> empFinancialMileStoneAliasNamePojo$empFinancialMileStoneDtlsPojo(List<FinancialProjectMileStonePojo> empFinancialMileStoneDtlsPojo, FinancialMileStoneClassifidesPojo financialMileStoneSetsPojo, EmployeeFinancialServiceInfo employeeFinancialInfo) {
		log.info(" ***** EmployeeFinancialMapper.empFinancialMileStoneAliasNamePojo$empFinancialMileStoneDtlsPojo() ***** ");

		List<EmployeeFinancialResponse> financialResponseList = new ArrayList<EmployeeFinancialResponse>();
		EmployeeFinancialResponse financialResponseObject = new EmployeeFinancialResponse();

		BeanUtils.copyProperties(financialMileStoneSetsPojo, financialResponseObject);
		String milestoneName = "";

		List<FinancialProjectMileStoneResponse> empFinancialDtlsResponseList = new ArrayList<FinancialProjectMileStoneResponse>();
		for (FinancialProjectMileStonePojo empMileStoneDetailsPojo : empFinancialMileStoneDtlsPojo) {
			FinancialProjectMileStoneResponse mileStoneNameDetailsObject = new FinancialProjectMileStoneResponse();
			if (Util.isNotEmptyObject(empMileStoneDetailsPojo)) {
				//Timestamp mileStoneDate = empMileStoneDetailsPojo.getMilestoneDate();
				Timestamp masterMileStoneDate = empMileStoneDetailsPojo.getMasterDemandNotedate();
				Timestamp masterDemandDueDate = empMileStoneDetailsPojo.getMasterDemandDueDate();
				
				if (empMileStoneDetailsPojo.getBlockName() != null) {
					milestoneName = empMileStoneDetailsPojo.getMilestoneName() + "-" + empMileStoneDetailsPojo.getBlockName();
					empMileStoneDetailsPojo.setMilestoneName(milestoneName);
				}
				if(masterMileStoneDate!=null && masterDemandDueDate!=null) {
					empMileStoneDetailsPojo.setMileStoneDueDate(masterDemandDueDate);
				}
				
				BeanUtils.copyProperties(empMileStoneDetailsPojo, mileStoneNameDetailsObject);
				mileStoneNameDetailsObject.setMapData(milestoneStatusMapData);
				/*empMileStoneDetailsPojo.getprojectMsStatusId();
				if (Util.isEmptyObject(employeeFinancialInfo.getCondition())) {
					//mileStoneNameDetailsObject.setMsStatusId(0l);
				}*/
				
				empFinancialDtlsResponseList.add(mileStoneNameDetailsObject);
			}
		}
		financialResponseObject.setFinancialProjectMileStoneResponse(empFinancialDtlsResponseList);
		financialResponseList.add(financialResponseObject);
		return financialResponseList;
	}

	public List<EmployeeFinancialResponse> empFinancialMileStoneClassifidesPojo$financialProjectMileStonePojo$customerPropertyDetailsPojo(FinancialMileStoneClassifidesPojo financialMileStoneClassifidesPojo, List<FinancialMileStoneClassifideMappingBlocksPojo> mileStoneMappingBlocksPojos, List<CustomerPropertyDetailsPojo> listOfFlats) {
		log.info(" ***** EmployeeFinancialMapper.empFinancialMileStoneClassifidesPojo$financialProjectMileStonePojo$customerPropertyDetailsPojo() ***** ");
	
		List<EmployeeFinancialResponse> financialResponseList = new ArrayList<EmployeeFinancialResponse>();
		EmployeeFinancialResponse financialResponseObject = new EmployeeFinancialResponse();

		BeanUtils.copyProperties(financialMileStoneClassifidesPojo, financialResponseObject);
		
		List<FinMileStoneCLSMappingBlocksResponse> empFinancialDtlsResponseList = new ArrayList<FinMileStoneCLSMappingBlocksResponse>();
		for (FinancialMileStoneClassifideMappingBlocksPojo empMileStoneDetailsPojo : mileStoneMappingBlocksPojos) {
			FinMileStoneCLSMappingBlocksResponse mileStoneNameDetailsObject = new FinMileStoneCLSMappingBlocksResponse();
			if (Util.isNotEmptyObject(empMileStoneDetailsPojo)) {
				BeanUtils.copyProperties(empMileStoneDetailsPojo, mileStoneNameDetailsObject);
				empFinancialDtlsResponseList.add(mileStoneNameDetailsObject);
			}
		}
		
		List<CustomerPropertyDetailsInfo> flatsResponse=new ArrayList<CustomerPropertyDetailsInfo>();;
		if(!listOfFlats.isEmpty()) {
			for (CustomerPropertyDetailsPojo customerPropertyDetailsPojo : listOfFlats) {
				CustomerPropertyDetailsInfo customerPropertyDetailsInfo = dozerBeanMapper.map(customerPropertyDetailsPojo, CustomerPropertyDetailsInfo.class);
				flatsResponse.add(customerPropertyDetailsInfo);
			}
		}
		
 		financialResponseObject.setMappingBlocksResponse(empFinancialDtlsResponseList);
		financialResponseObject.setFlatsResponse(flatsResponse);
		financialResponseList.add(financialResponseObject);
		return financialResponseList;
	}

	public <T> T copyPropertiesFromInfoBeanToPojoBean(@NotNull Object sourceFinProjMileStoneInfo, Class<T> targetClass) throws InstantiationException, IllegalAccessException {
		log.info(" ****** EmployeeFinancialMapper.copyPropertiesFromInfoBeanToPojoBean() ***** ");
		//T destFinProjDemandNotePojo = targetClass.newInstance();
		T destFinProjDemandNotePojo = dozerBeanMapper.map(sourceFinProjMileStoneInfo, targetClass);
		return destFinProjDemandNotePojo;
	}
	
	public <T> T copyPropertiesFromInfoBeanToInfoBean(@NotNull Object sourceFinProjMileStoneInfo, Class<T> targetClass) throws InstantiationException, IllegalAccessException {
		log.info(" ****** EmployeeFinancialMapper.copyPropertiesFromInfoBeanToInfoBean() ***** ");
		//T destFinProjDemandNotePojo = targetClass.newInstance();
		T destFinProjDemandNotePojo = dozerBeanMapper.map(sourceFinProjMileStoneInfo, targetClass);
		return destFinProjDemandNotePojo;
	}
	
	public <T> T copyPropertiesFromRequestBeanToInfoBean(@NotNull Object sourceFinProjMileStoneInfo,
			Class<T> targetClass) throws InstantiationException, IllegalAccessException {
		log.info(" ***** EmployeeFinancialMapper.copyPropertiesFromRequestBeanToInfoBean() ***** ");
		T destFinProjDemandNotePojo = targetClass.newInstance();
		destFinProjDemandNotePojo = dozerBeanMapper.map(sourceFinProjMileStoneInfo, targetClass);
		return destFinProjDemandNotePojo;
	}
	
 	public <T> T copyPropertiesFromPojoBeanToPojoBeanInfoBean(@NotNull Object sourceFinProjMileStoneInfo, Class<T> targetClass) throws InstantiationException, IllegalAccessException {
		log.info(" ***** Control inside EmployeeFinancialMapper.copyPropertiesFromFinancialProjectMileStoneInfoToFinProjDemandNotePojo() ***** ");
		//Object destFinProjDemandNotePojo =  targetClass.newInstance();
		//FinProjectDemandNotePojo destFinProjDemandNotePojo= new FinProjectDemandNotePojo();
		//BeanUtils.copyProperties(sourceFinProjMileStoneInfo, destFinProjDemandNotePojo);
		return dozerBeanMapper.map(sourceFinProjMileStoneInfo, targetClass);
		//return destFinProjDemandNotePojo;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		
		EmployeeFinancialTransactionRequest request = new EmployeeFinancialTransactionRequest();
		request.setEmpId(24L);
		List<FileInfo>  fileInfos1=new ArrayList<>();
		FileInfo fileInfo=new FileInfo();
		fileInfo.setName("Hi");
		FileInfo fileInfo2 =new FileInfo();
		fileInfo2.setName("hRU");
		fileInfos1.add(fileInfo);
		fileInfos1.add(fileInfo2);
		request.setFileInfos(fileInfos1);
		EmployeeFinancialTransactionServiceInfo info = new EmployeeFinancialTransactionServiceInfo();
		
		info  = dozerBeanMapper.map(request, EmployeeFinancialTransactionServiceInfo.class);
		
		List<EmployeeFinModiCostInfo> chargesInfos = new ArrayList<>();
		EmployeeFinModiCostInfo chargesInfo = new EmployeeFinModiCostInfo("dsa", "kg's", 10D, 10D, 10D, 24L);
		EmployeeFinModiCostInfo chargesInfo1 = new EmployeeFinModiCostInfo("wqe", "ltr", 5D, 5D, 5D, 24L);
		chargesInfos.add(chargesInfo);
		chargesInfos.add(chargesInfo1);
		info.setModiCostDtlsInfos(chargesInfos);
		
		//System.out.println(info);
		
		Map<String, String> comment=new HashMap<>();
		comment.put("CRM", "This is CRM");
		//System.out.println(comment.isEmpty());
		if(!comment.isEmpty()) {
			for (Map.Entry<String, String> entry : comment.entrySet()) {
				String k = entry.getKey();
				String v = entry.getValue();
				//System.out.println("Key: " + k + ", Value: " + v);
				//System.out.println(MetadataId.valueOf(entry.getKey()).getId());
				//System.out.println(comment.get(entry.getKey()));
			}
		}
		
//		Map<String, String> map=new HashMap<String, String>();
//		map.put("name", "loac1");
//		
//		Map<String, String> map1=new HashMap<String, String>();
//		map1.put("name2", "loc2");
//		List imageLocationDetails=new ArrayList();
//		imageLocationDetails.add(map1);
//		imageLocationDetails.add(map);
//		info.setImageLocationDetails(imageLocationDetails);
//		for (Map<String, String> fileInfo3 : info.getImageLocationDetails()) {
//			Object destFinProjDemandNotePojo = dozerBeanMapper.map(fileInfo3, FinTransactionEntryDocPojo.class);
//			//System.out.println("EmployeeFinancialMapper.main()");
//			//System.out.println(destFinProjDemandNotePojo);
//		}
		
		
//		List<FileInfo>  fileInfos=new ArrayList<>();
//		FileInfo fileInfo=new FileInfo();
//		fileInfo.setName("Hi");
//		FileInfo fileInfo2 =new FileInfo();
//		fileInfo2.setName("hRU");
//		fileInfos.add(fileInfo);
//		fileInfos.add(fileInfo2);
//		info.setFileInfos(fileInfos);
//		FinTransactionEntryDocPojo pojo=new FinTransactionEntryDocPojo();
//		
//		
//		for (FileInfo fileInfo3 : info.getFileInfos()) {
//			fileInfo3.setCreatedBy("24");
//			Object destFinProjDemandNotePojo = dozerBeanMapper.map(fileInfo3, FinTransactionEntryDocPojo.class);
//			//System.out.println("EmployeeFinancialMapper.main()");
//			//System.out.println(destFinProjDemandNotePojo);
//		}
	}

	public CustomerPropertyDetailsInfo copyPropertiesFromCustomerPropertyDetailsPojoToCustomerPropertyDetailsInfo(CustomerPropertyDetailsPojo sourceCustomerPropertyDetails) {
		CustomerPropertyDetailsInfo destCustomerPropertyDetailsInfo = new CustomerPropertyDetailsInfo();
		//BeanUtils.copyProperties(sourceCustomerPropertyDetails, destCustomerPropertyDetailsInfo);
		destCustomerPropertyDetailsInfo = dozerBeanMapper.map(sourceCustomerPropertyDetails, CustomerPropertyDetailsInfo.class);
		return destCustomerPropertyDetailsInfo;
	}
	
	public List<CustomerPropertyDetailsInfo> copyPropertiesFromCustomerPropertyDetailsPojoListToCustomerPropertyDetailsInfoList(List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojoList, Class<CustomerPropertyDetailsInfo> class1) {
		List<CustomerPropertyDetailsInfo> destCustomerPropertyDetailsInfo = new ArrayList<>();
		for (CustomerPropertyDetailsPojo customerPropertyDetailsPojo : customerPropertyDetailsPojoList) {
			destCustomerPropertyDetailsInfo.add(dozerBeanMapper.map(customerPropertyDetailsPojo, CustomerPropertyDetailsInfo.class));	
		}
		return destCustomerPropertyDetailsInfo;
	}

	public FinProjDemNoteStatisticsPojo copyPropertiesFromCustomerPropertyDetailsInfoTOFinProjDemNoteStatisticsPojo(EmployeeFinancialServiceInfo employeeFinancialDemandNoteInfo, FinancialProjectMileStoneInfo finProjDemandNoteInfo,CustomerPropertyDetailsInfo sourceCustomerPropertyDetails, Class<?> targetClass) throws InstantiationException, IllegalAccessException {
		FinProjDemNoteStatisticsPojo destFinProjDemandNotePojo = (FinProjDemNoteStatisticsPojo) targetClass.newInstance();
		//BeanUtils.copyProperties(sourceCustomerPropertyDetails, destFinProjDemandNotePojo);
		
		destFinProjDemandNotePojo = dozerBeanMapper.map(sourceCustomerPropertyDetails, FinProjDemNoteStatisticsPojo.class);
		
		if(employeeFinancialDemandNoteInfo.getDemandNoteSelectionType().equalsIgnoreCase(FinEnum.ALL_BLOCKS.getName())){
			destFinProjDemandNotePojo.setTypeId(0l);
			destFinProjDemandNotePojo.setType(MetadataId.ALL_BLOCK.getId());
		}else if(employeeFinancialDemandNoteInfo.getDemandNoteSelectionType().equalsIgnoreCase(FinEnum.BLOCK_WISE.getName())) {
			destFinProjDemandNotePojo.setTypeId(sourceCustomerPropertyDetails.getBlockId());
			destFinProjDemandNotePojo.setType(MetadataId.BLOCK.getId());
	 	}else if(employeeFinancialDemandNoteInfo.getDemandNoteSelectionType().equalsIgnoreCase(FinEnum.SEND_SINGLE_MULTIPLE.getName())) {
			destFinProjDemandNotePojo.setTypeId(sourceCustomerPropertyDetails.getFlatBookingId());
			destFinProjDemandNotePojo.setType(MetadataId.FLAT_BOOKING.getId());
		}

		destFinProjDemandNotePojo.setFinProjectDemandNoteId(finProjDemandNoteInfo.getFinProjectDemandNoteId());
		destFinProjDemandNotePojo.setCreatedBy(finProjDemandNoteInfo.getCreatedBy());
		//destFinProjDemandNotePojo.setProjectMilestoneId(finProjDemandNoteInfo.getProjectMilestoneId());
		return destFinProjDemandNotePojo;
	}
	
	public FinBokFrmDemNteSchTaxMapPojo finBokFrmDemNteIdAndfinSchTaxMapIdToFinBokFrmDemNteSchTaxMapPojo(FinBookingFormMstSchTaxMapInfo bokFrmDemNteSchTaxMapInfo) {
		log.info(" **** The control is inside the finBokFrmDemNteIdAndfinSchTaxMapIdToFinBokFrmDemNteSchTaxMapPojo in EmployeeFinancialMapper **** ");
		FinBokFrmDemNteSchTaxMapPojo finBokFrmDemNteSchTaxMapPojo = new FinBokFrmDemNteSchTaxMapPojo();
		//BeanUtils.copyProperties(bokFrmDemNteSchTaxMapInfo, finBokFrmDemNteSchTaxMapPojo);
		finBokFrmDemNteSchTaxMapPojo = dozerBeanMapper.map(bokFrmDemNteSchTaxMapInfo, FinBokFrmDemNteSchTaxMapPojo.class);
		return finBokFrmDemNteSchTaxMapPojo;
	}

	public FinBookingFormAccountsPojo copyPropertiesToFinBookingFormAccountsPojo1(FinancialProjectMileStoneInfo finProjDemandNoteInfo,
			FinancialProjectMileStonePojo pojoObject, EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			FinPenalityInfo finPenalityInfo, Class<?> targetClass, String condition, String requestFrom) throws InstantiationException, IllegalAccessException {
		FinBookingFormAccountsPojo destFinBookingFormAccPojo =  (FinBookingFormAccountsPojo) targetClass.newInstance();
		
		destFinBookingFormAccPojo =  dozerBeanMapper.map(finProjDemandNoteInfo,  FinBookingFormAccountsPojo.class);
		destFinBookingFormAccPojo.setIsRecordUploaded(transactionServiceInfo.getIsRecordUploaded());
		destFinBookingFormAccPojo.setTypeId(finProjDemandNoteInfo.getFinBookingFormMilestonesId());
		destFinBookingFormAccPojo.setType(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId());	
		destFinBookingFormAccPojo.setPayAmount(finProjDemandNoteInfo.getMileStoneTotalAmount());
		destFinBookingFormAccPojo.setTaxAmount(finProjDemandNoteInfo.getMileStoneTaxAmount());
		destFinBookingFormAccPojo.setPrincipalAmount(finProjDemandNoteInfo.getMileStoneBasicAmount());
		//destFinBookingFormAccPojo.setBalanceAmount(finProjDemandNoteInfo.getMileStoneTotalAmount());
		destFinBookingFormAccPojo.setPaidDate(transactionServiceInfo.getTransactionReceiveDate());
		destFinBookingFormAccPojo.setStatusId(Status.ACTIVE.getStatus());
		destFinBookingFormAccPojo.setPaymentStatus(pojoObject.getPaymentStatus());
		destFinBookingFormAccPojo.setPaidAmount(pojoObject.getSetOffAmount());
		
		destFinBookingFormAccPojo.setBalanceAmount(finProjDemandNoteInfo.getMileStoneTotalAmount()-pojoObject.getSetOffAmount());
		return destFinBookingFormAccPojo;
	}
	
	public FinBookingFormAccountsPojo copyPropertiesToFinBookingFormAccountsPojo(FinancialProjectMileStoneInfo finProjDemandNoteInfo,
		 Object infoObject, Class<?> targetClass, String condition) throws InstantiationException, IllegalAccessException {
		
		 FinBookingFormAccountsPojo destFinBookingFormAccPojo =  (FinBookingFormAccountsPojo) targetClass.newInstance();
		 if(Util.isNotEmptyObject(finProjDemandNoteInfo))
			BeanUtils.copyProperties(finProjDemandNoteInfo, destFinBookingFormAccPojo);
		 			
			if(condition.equalsIgnoreCase("MileStoneBasicAmt")) {
				destFinBookingFormAccPojo.setTypeId(finProjDemandNoteInfo.getFinBookingFormMilestonesId());
				destFinBookingFormAccPojo.setType(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId());	
				destFinBookingFormAccPojo.setPayAmount(finProjDemandNoteInfo.getMileStoneTotalAmount());
				destFinBookingFormAccPojo.setTaxAmount(finProjDemandNoteInfo.getMileStoneTaxAmount());
				destFinBookingFormAccPojo.setPrincipalAmount(finProjDemandNoteInfo.getMileStoneBasicAmount());
				destFinBookingFormAccPojo.setBalanceAmount(finProjDemandNoteInfo.getMileStoneTotalAmount());
				if(finProjDemandNoteInfo.isThisUplaodedData()) {
					destFinBookingFormAccPojo.setIsRecordUploaded(Status.YES.getStatus());
				}
			}else if(condition.equalsIgnoreCase("MileStoneTaxAmt")){
				destFinBookingFormAccPojo.setTypeId(finProjDemandNoteInfo.getFinBookingFormMileStoneTaxId());
				destFinBookingFormAccPojo.setType(MetadataId.FIN_BOOKING_FORM_MILESTONE_TAX.getId());	//FIN_BOK_FRM_MST_SCH_TAX_MAP
				destFinBookingFormAccPojo.setPayAmount(finProjDemandNoteInfo.getMileStoneTaxAmount());
			}else if(condition.equalsIgnoreCase("MileStonePenaltyAmt")){
			if (infoObject instanceof FinPenalityInfo) {
				FinPenalityInfo finPenalityInfo = (FinPenalityInfo) infoObject;
				destFinBookingFormAccPojo.setTypeId(finPenalityInfo.getFinPenaltyId());
				destFinBookingFormAccPojo.setType(MetadataId.FIN_PENALTY.getId()); // FIN_BOK_FRM_MST_SCH_TAX_MAP
				destFinBookingFormAccPojo.setPayAmount(finPenalityInfo.getPenaltyTotalAmount());
				destFinBookingFormAccPojo.setTaxAmount(finPenalityInfo.getPenaltyTaxAmount());
				destFinBookingFormAccPojo.setBookingFormId(finPenalityInfo.getBookingFormId());
				destFinBookingFormAccPojo.setPrincipalAmount(finPenalityInfo.getPenaltyAmount());
				destFinBookingFormAccPojo.setBalanceAmount(finPenalityInfo.getPenaltyTotalAmount());
			}
			}else if(condition.equalsIgnoreCase("modificationCost")){
				if (infoObject instanceof FinBookingFormModiCostPojo) {
					FinBookingFormModiCostPojo modiCostPojo = (FinBookingFormModiCostPojo) infoObject;
					destFinBookingFormAccPojo.setTypeId(modiCostPojo.getFinBookingFormModiCostId());
					destFinBookingFormAccPojo.setType(MetadataId.MODIFICATION_COST.getId()); // FIN_BOK_FRM_MST_SCH_TAX_MAP
					destFinBookingFormAccPojo.setPayAmount(modiCostPojo.getTotalAmount());
					destFinBookingFormAccPojo.setTaxAmount(modiCostPojo.getTaxAmount());
					destFinBookingFormAccPojo.setBookingFormId(modiCostPojo.getBookingFormId());
					destFinBookingFormAccPojo.setCreatedBy(modiCostPojo.getCreatedBy());
					
					destFinBookingFormAccPojo.setPrincipalAmount(modiCostPojo.getBasicAmount());
					destFinBookingFormAccPojo.setBalanceAmount(modiCostPojo.getTotalAmount());
				}
			}else if(condition.equalsIgnoreCase(MetadataId.LEGAL_COST.getName())) {
				if(infoObject instanceof FinBookingFormLegalCostPojo) {
					FinBookingFormLegalCostPojo finBookingFormLegalCostPojo = (FinBookingFormLegalCostPojo) infoObject;
					destFinBookingFormAccPojo.setTypeId(finBookingFormLegalCostPojo.getFinBokFrmLegalCostId());
					destFinBookingFormAccPojo.setType(MetadataId.LEGAL_COST.getId()); // FIN_BOK_FRM_MST_SCH_TAX_MAP
					destFinBookingFormAccPojo.setPayAmount(finBookingFormLegalCostPojo.getTotalAmount());
					destFinBookingFormAccPojo.setTaxAmount(finBookingFormLegalCostPojo.getTaxAmount());
					destFinBookingFormAccPojo.setBookingFormId(finBookingFormLegalCostPojo.getBookingFormId());
					destFinBookingFormAccPojo.setCreatedBy(finBookingFormLegalCostPojo.getCreatedBy());
					
					destFinBookingFormAccPojo.setPrincipalAmount(finBookingFormLegalCostPojo.getLegalAmount());
					destFinBookingFormAccPojo.setBalanceAmount(finBookingFormLegalCostPojo.getTotalAmount());
				}
			}
			destFinBookingFormAccPojo.setStatusId(Status.ACTIVE.getStatus());
			destFinBookingFormAccPojo.setPaymentStatus(Status.PENDING.getStatus());
			destFinBookingFormAccPojo.setPaidAmount(0D);
			return destFinBookingFormAccPojo;
	}

	public Object copyPropertiesToFinBookingFormDemandNotePojo(FinancialProjectMileStoneInfo finProjDemandNoteInfo,EmployeeFinancialServiceInfo employeeFinancialDemandNoteInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, Class<?> targetClass) throws InstantiationException, IllegalAccessException {
		FinBookingFormDemandNotePojo finBookingFormAccountsPojo = (FinBookingFormDemandNotePojo) targetClass.newInstance();
		finBookingFormAccountsPojo =  dozerBeanMapper.map(finProjDemandNoteInfo,  FinBookingFormDemandNotePojo.class);
		finBookingFormAccountsPojo.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		/*don't move this line to  live after testing can move*/
		if(finProjDemandNoteInfo.getDemandNoteDate()!=null) {
			finBookingFormAccountsPojo.setDemandNoteDate(TimeUtil.addCurrentTimeToTimeStamp(finProjDemandNoteInfo.getDemandNoteDate()));
		}
		/*--*/
		if(finProjDemandNoteInfo.isReGenerateDemandNote()) {
			finBookingFormAccountsPojo.setStatusId(Status.REGENERATED_DEMAND_NOTE.getStatus());
		}
		if(finProjDemandNoteInfo.isThisUplaodedData()) {
			finBookingFormAccountsPojo.setDemandNoteStatusId(Status.UPLOADED_DATA.getStatus());
		} else {
			finBookingFormAccountsPojo.setDemandNoteStatusId(Status.ACTIVE.getStatus());
		}
		try {
			if(employeeFinancialDemandNoteInfo.getActionUrl()!=null && finProjDemandNoteInfo.isThisUplaodedData()) {
				finBookingFormAccountsPojo.setDemandNoteFormat(MetadataId.valueOf(employeeFinancialDemandNoteInfo.getActionUrl().toUpperCase()).getId());
			} else if(employeeFinancialDemandNoteInfo.getActionUrl()!=null && !finProjDemandNoteInfo.isThisUplaodedData()) {
				finBookingFormAccountsPojo.setDemandNoteFormat(MetadataId.valueOf(employeeFinancialDemandNoteInfo.getActionUrl().toUpperCase()).getId());
			} else {
				finBookingFormAccountsPojo.setDemandNoteFormat(MetadataId.MILESTONE_COMPLETION_DEMANDNOTE.getId());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return finBookingFormAccountsPojo;
	}

	public Object copyPropertiesToFinBookingFormMileStonePojo(FinancialProjectMileStoneInfo finProjDemandNoteInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, Class<?> targetClass) throws InstantiationException, IllegalAccessException {
		FinBookingFormMilestonesPojo finBookingFormAccountsPojo = (FinBookingFormMilestonesPojo) targetClass.newInstance();
		finBookingFormAccountsPojo =  dozerBeanMapper.map(finProjDemandNoteInfo,  FinBookingFormMilestonesPojo.class);
		finBookingFormAccountsPojo.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		if(finProjDemandNoteInfo.isReGenerateDemandNote()) {
			finBookingFormAccountsPojo.setStatusId(Status.REGENERATED_DEMAND_NOTE.getStatus());
		} else {
			finBookingFormAccountsPojo.setStatusId(Status.ACTIVE.getStatus());
		}
		
		if(finProjDemandNoteInfo.isThisExplicitGeneratedRecord()) {
			finBookingFormAccountsPojo.setMsStatusId(Status.EXPLICIT_GENERATED_RECORD.getStatus());
		} else {
			finBookingFormAccountsPojo.setMsStatusId(Status.ACTIVE.getStatus());
		}
		
		/*if(finProjDemandNoteInfo.isThisUplaodedData()) {
			finBookingFormAccountsPojo.setMsStatusId(Status.UPLOADED_DATA.getStatus());
		}else {
			finBookingFormAccountsPojo.setMsStatusId(Status.ACTIVE.getStatus());
		}*/
		return finBookingFormAccountsPojo;
	}
	
	/*public Object copyPropertiesToFinBookingFormMileStonePojo1(FinancialProjectMileStoneInfo finProjDemandNoteInfo,
			CustomerPropertyDetailsInfo customerPropertyDetailsInfo, Class<?> targetClass) throws InstantiationException, IllegalAccessException {
		FinBookingFormMilestonesPojo finBookingFormAccountsPojo = (FinBookingFormMilestonesPojo) targetClass.newInstance();
		finBookingFormAccountsPojo =  dozerBeanMapper.map(finProjDemandNoteInfo,  FinBookingFormMilestonesPojo.class);
		finBookingFormAccountsPojo.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		if(finProjDemandNoteInfo.isReGenerateDemandNote()) {
			finBookingFormAccountsPojo.setStatusId(Status.REGENERATED_DEMAND_NOTE.getStatus());
		}else {
			finBookingFormAccountsPojo.setStatusId(Status.ACTIVE.getStatus());
		}
		return finBookingFormAccountsPojo;
	}*/

	@Override
	public String toString() {
		return "EmployeeFinancialMapper [name=" + name + "]";
	}

	public FinBookingFormExcessAmountPojo copyPropertiesFromFinBookingFormExcessAmountPojo_FinBookingFormAccountsInfoToFinBookingFormExcessAmountPojo(FinBookingFormAccountsInfo accountsInfo,
			FinBookingFormExcessAmountPojo excessAmountPojo, FinancialProjectMileStoneInfo finProjDemandNoteInfo,
			Class<?> targetClass) throws MappingException, InstantiationException, IllegalAccessException {
		FinBookingFormExcessAmountPojo amountPojo = (FinBookingFormExcessAmountPojo) targetClass.newInstance();
		BeanUtils.copyProperties(accountsInfo, amountPojo);
		//BeanUtils.copyProperties(excessAmountPojo, amountPojo);
		amountPojo.setFinBookingFormExcessAmountId(excessAmountPojo.getFinBookingFormExcessAmountId());
		amountPojo.setModifiedBy(finProjDemandNoteInfo.getModifiedBy());
		return amountPojo;
	}

	public FinBookingFormExcessAmountUsagePojo copyPropertiesFinBookingFormAccountsInfoToBookingFormExcessAmountUsage(
			FinBookingFormAccountsInfo accountsInfo, FinBookingFormExcessAmountPojo excessAmountPojo,
			FinancialProjectMileStoneInfo finProjDemandNoteInfo, Class<?> targetClass, String condition) throws InstantiationException, IllegalAccessException {
		FinBookingFormExcessAmountUsagePojo amountPojo = (FinBookingFormExcessAmountUsagePojo) targetClass.newInstance();
		BeanUtils.copyProperties(excessAmountPojo, amountPojo, "usedAmount","balanceAmount","createdBy","createdDate");
		BeanUtils.copyProperties(accountsInfo, amountPojo,"finBookingFormExcessAmountId","excessAmount");
		
		amountPojo.setConvertedAmount(accountsInfo.getUsedAmount());
		
		if(condition.equalsIgnoreCase("MileStoneBasicAmt")) {
			amountPojo.setConvertType(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId());
		}else if(condition.equalsIgnoreCase("MileStoneTaxAmt")){
			amountPojo.setConvertType(MetadataId.FIN_BOOKING_FORM_MILESTONE_TAX.getId());
		} else if(condition.equalsIgnoreCase(MetadataId.MODIFICATION_COST.getName())) {
			amountPojo.setConvertType(MetadataId.MODIFICATION_COST.getId());
		}else if(condition.equalsIgnoreCase(MetadataId.FIN_PENALTY.getName())) {
			amountPojo.setConvertType(MetadataId.FIN_PENALTY.getId());
		}
		amountPojo.setCreatedBy(finProjDemandNoteInfo.getModifiedBy());
		return amountPojo;
	}
	
	public FinancialMileStoneClassifidesPojo employeeFinancialRequestToFinancialMileStoneClassifidesPojo(EmployeeFinancialRequest employeeFinancialRequest) {
		log.info(" ***** The control is inside the employeeFinancialRequestTofinancialMileStoneClassifidesPojo in EmployeeFinancialMapper ***** ");
		FinancialMileStoneClassifidesPojo financialMileStoneClassifidesPojo = new FinancialMileStoneClassifidesPojo();
		BeanUtils.copyProperties(employeeFinancialRequest, financialMileStoneClassifidesPojo);
		financialMileStoneClassifidesPojo.setCreatedBy(Util.isNotEmptyObject(employeeFinancialRequest.getEmpId())?employeeFinancialRequest.getEmpId():0l);
		log.debug("*** employeeFinancialRequestTofinancialMileStoneClassifidesPojo ***"+financialMileStoneClassifidesPojo);
		return financialMileStoneClassifidesPojo;
	}

	public List<FinancialProjectMileStonePojo> employeeFinancialRequestToFinancialProjectMileStonePojosList(EmployeeFinancialRequest employeeFinancialRequest) {
		log.info(" ***** The control is inside the employeeFinancialRequestToFinancialProjectMileStonePojo in EmployeeFinancialMapper ***** ");
		List<FinancialProjectMileStonePojo> financialProjectMileStonePojosList = new ArrayList<FinancialProjectMileStonePojo>();	
		Long mileStoneNo  = 1l;
		for(FinancialProjectMileStoneRequest financialProjectMileStoneRequest : employeeFinancialRequest.getFinancialProjectMileStoneRequests()) {
			FinancialProjectMileStonePojo financialProjectMileStonePojo = new FinancialProjectMileStonePojo();
			BeanUtils.copyProperties(financialProjectMileStoneRequest, financialProjectMileStonePojo);
			financialProjectMileStonePojo.setCreatedBy(Util.isNotEmptyObject(employeeFinancialRequest.getEmpId())?employeeFinancialRequest.getEmpId():0l);
			financialProjectMileStonePojo.setFinMilestoneClassifidesId(employeeFinancialRequest.getFinMilestoneClassifidesId());
			financialProjectMileStonePojo.setMileStoneNo(mileStoneNo++);
			financialProjectMileStonePojosList.add(financialProjectMileStonePojo);
		}
		return financialProjectMileStonePojosList;
	}

	public List<SiteDetailsInfo> siteDetailsPojoListToSiteDetailsInfoList(List<SiteDetailsPojo> siteDetailsPojoList) {
		log.info(" ***** The control is inside the employeeFinancialRequestToFinancialProjectMileStonePojo in EmployeeFinancialMapper ***** ");
		List<SiteDetailsInfo> SiteDetailsInfoList = new ArrayList<>();
		for(SiteDetailsPojo siteDetailsPojo : siteDetailsPojoList) {
			SiteDetailsInfo siteDetailsInfo = new SiteDetailsInfo();
			BeanUtils.copyProperties(siteDetailsPojo, siteDetailsInfo);
			SiteDetailsInfoList.add(siteDetailsInfo);
		}
		log.debug("** The Converted Object from Pojo to Info is **"+SiteDetailsInfoList);
		return SiteDetailsInfoList;
	}

	public List<FinBookingFormDemandNoteResponse> finBookingFormDemandNotePojoListToFinBookingFormDemandNoteResponseList(List<FinBookingFormDemandNotePojo> finBookingFormDemandNotePojoList) {
		log.info(" ***** The control is inside the finBookingFormDemandNotePojoListToFinBookingFormDemandNoteResponseList in EmployeeFinancialMapper ***** ");
		List<FinBookingFormDemandNoteResponse> finBookingFormDemandNoteResponseList = new ArrayList<>();
		for(FinBookingFormDemandNotePojo finBookingFormDemandNotePojo : finBookingFormDemandNotePojoList) {
			FinBookingFormDemandNoteResponse finBookingFormDemandNoteResponse = new FinBookingFormDemandNoteResponse();
			BeanUtils.copyProperties(finBookingFormDemandNotePojo, finBookingFormDemandNoteResponse);
			
			if(Util.isNotEmptyObject(finBookingFormDemandNotePojo.getMileStoneWiseTotalAmount())) {
				finBookingFormDemandNoteResponse.setMileStoneTotalAmount(finBookingFormDemandNotePojo.getMileStoneWiseTotalAmount());				
			}
			
			if(Util.isNotEmptyObject(finBookingFormDemandNoteResponse.getDemandNoteStatus())) {
				if(finBookingFormDemandNoteResponse.getDemandNoteStatus().equalsIgnoreCase("Active")) {
					finBookingFormDemandNoteResponse.setDemandNoteStatus("Regular DN");
				} else {
					finBookingFormDemandNoteResponse.setDueDate(finBookingFormDemandNotePojo.getRegularMsDueDate());
					finBookingFormDemandNoteResponse.setMileStoneBalanceAmount(finBookingFormDemandNotePojo.getRegularBalMsAmount());
					finBookingFormDemandNoteResponse.setDemandNoteStatus("Regenerated DN");
				}
			}
			finBookingFormDemandNoteResponseList.add(finBookingFormDemandNoteResponse);
		}
		return finBookingFormDemandNoteResponseList;
	}

	public FinancialProjectMileStoneResponse financialProjectMileStoneInfoToFinancialProjectMileStoneResponse(FinancialProjectMileStoneInfo financialProjectMileStoneInfo) {
		log.info(" ***** The control is inside the financialProjectMileStoneInfoToFinancialProjectMileStoneResponse in EmployeeFinancialMapper ***** ");
		FinancialProjectMileStoneResponse financialProjectMileStoneResponse = new FinancialProjectMileStoneResponse();
		BeanUtils.copyProperties(financialProjectMileStoneInfo, financialProjectMileStoneResponse);
		return financialProjectMileStoneResponse;
	}

	public List<FinancialProjectMileStoneResponse> financialProjectMileStonePojoListToFinancialProjectMileStoneResponseList(List<FinancialProjectMileStonePojo> financialProjectMileStonePojoList) {
		log.info(" ***** The control is inside the financialProjectMileStoneInfoToFinancialProjectMileStoneResponse in EmployeeFinancialMapper ***** ");
		List<FinancialProjectMileStoneResponse> financialProjectMileStoneResponseList = new ArrayList<>();
		for(FinancialProjectMileStonePojo financialProjectMileStonePojo : financialProjectMileStonePojoList) {
			FinancialProjectMileStoneResponse financialProjectMileStoneResponse = new FinancialProjectMileStoneResponse();
			BeanUtils.copyProperties(financialProjectMileStonePojo, financialProjectMileStoneResponse);
			Long msStatusId = financialProjectMileStoneResponse.getMsStatusId();
			 if(msStatusId==null || !msStatusId.equals(Status.EXPLICIT_GENERATED_RECORD.getStatus())) {
				 financialProjectMileStoneResponseList.add(financialProjectMileStoneResponse);
			 }
		}
		return financialProjectMileStoneResponseList;
	}
	
public Object copyPropertiesFromFinancialTransactionServiceInfoBeanToTransactionCommentsPojoBean(
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Map<String, String> comment, Class<?> targetClass) throws BeansException, InstantiationException, IllegalAccessException {
		FinTransactionCommentsPojo pojo =null;
		FinAnnyEntryCommentsPojo entryCommentsPojo=null;
		//BeanUtils.copyProperties(transactionServiceInfo,pojo);
		Object destFinProjDemandNotePojo = targetClass.newInstance();
		
		if(!comment.isEmpty()) {
			for (Map.Entry<String, String> entry : comment.entrySet()) {
				String k = entry.getKey();
				String v = entry.getValue();
				log.info("Key: " + k + ", Value: " + v);
				if(destFinProjDemandNotePojo instanceof FinTransactionCommentsPojo) {
					pojo = (FinTransactionCommentsPojo) dozerBeanMapper.map(transactionServiceInfo,FinTransactionCommentsPojo.class);	
					pojo.setType(MetadataId.valueOf(entry.getKey()).getId());
					pojo.setComments(comment.get(entry.getKey()));
				}else if(destFinProjDemandNotePojo instanceof FinAnnyEntryCommentsPojo) {
					entryCommentsPojo = (FinAnnyEntryCommentsPojo) dozerBeanMapper.map(transactionServiceInfo,FinAnnyEntryCommentsPojo.class);
					entryCommentsPojo.setType(MetadataId.valueOf(entry.getKey()).getId());
					entryCommentsPojo.setComments(comment.get(entry.getKey()));
					//entryCommentsPojo.setTypeId(transactionServiceInfo.getAnonymousEntryId());
					return entryCommentsPojo;
				}
			}
		}
		return pojo;
	}

	public List<FinBankResponse> finBankPojoListTofinBankResponseList(List<FinBankPojo> finBankPojoList) {
		log.info(" ***** The control is inside the finBankPojoListTofinBankResponseList in EmployeeFinancialMapper ***** ");
		List<FinBankResponse> finBankResponseList = new ArrayList<>();
		for(FinBankPojo finBankPojo : finBankPojoList) {
			FinBankResponse finBankResponse = new FinBankResponse();
			BeanUtils.copyProperties(finBankPojo, finBankResponse);
			finBankResponseList.add(finBankResponse);
		}
		return finBankResponseList;
	}

	public List<FinTransactionModeResponse> finTransactionModePojoListToFinTransactionModeResponseList(List<FinTransactionModePojo> finTransactionModePojoList) {
		log.info(" ***** The control is inside the finTransactionModePojoListToFinTransactionModeInfoList in EmployeeFinancialMapper ***** ");
		List<FinTransactionModeResponse> finTransactionModeResponseList = new ArrayList<>();
		for(FinTransactionModePojo finTransactionModePojo : finTransactionModePojoList) {
			FinTransactionModeResponse finTransactionModeResponse = new FinTransactionModeResponse();
			BeanUtils.copyProperties(finTransactionModePojo, finTransactionModeResponse);
			finTransactionModeResponseList.add(finTransactionModeResponse);
		}
		return finTransactionModeResponseList;
	}

	public List<FinTransactionTypeResponse> finTransactionTypePojoListToFinTransactionTypeResponseList(List<FinTransactionTypePojo> finTransactionTypePojoList) {
		log.info(" ***** The control is inside the finTransactionTypePojoListTofinTransactionTypeInfoList in EmployeeFinancialMapper ***** ");
		List<FinTransactionTypeResponse> finTransactionTypeResponseList = new ArrayList<>();
		for(FinTransactionTypePojo finTransactionTypePojo : finTransactionTypePojoList) {
			FinTransactionTypeResponse finTransactionTypeResponse = new FinTransactionTypeResponse();
			BeanUtils.copyProperties(finTransactionTypePojo, finTransactionTypeResponse);
			finTransactionTypeResponseList.add(finTransactionTypeResponse);
		}
		return finTransactionTypeResponseList;
	}

	public List<AddressInfo> addressPojoListToAddressInfoList(List<AddressPojo> addressPojoList) {
		log.info(" ***** The control is inside the addressPojoListToAddressInfoList in EmployeeFinancialMapper ***** ");
		List<AddressInfo> addressInfoList = new ArrayList<>();
		for(AddressPojo addressPojo : addressPojoList) {
			AddressInfo addressInfo = new AddressInfo();
			BeanUtils.copyProperties(addressPojo, addressInfo);
			addressInfo.setCity(bookingFormMapper.getCityName(addressPojo.getCityId()));
			addressInfo.setState(bookingFormMapper.getStateName(addressPojo.getStateId()));
			addressInfoList.add(addressInfo);
		}
		return addressInfoList;
	}

	public List<FinProjectAccountResponse> finProjectAccountPojoListToFinProjectAccountResponseList(List<FinProjectAccountPojo> finProjectAccountPojoList) {
		log.info(" ***** The control is inside the finProjectAccountPojoListToFinProjectAccountResponseList in EmployeeFinancialMapper ***** ");
		List<FinProjectAccountResponse> finProjectAccountResponseList = new ArrayList<>();
		for(FinProjectAccountPojo finProjectAccountPojo : finProjectAccountPojoList) {
			FinProjectAccountResponse finProjectAccountResponse = new FinProjectAccountResponse();
			BeanUtils.copyProperties(finProjectAccountPojo, finProjectAccountResponse);
			finProjectAccountResponse.setSiteBankAccountNumber(finProjectAccountPojo.getAccountNoWithSaleOwner());
			//setting site project acc map id to account id
			finProjectAccountResponse.setSiteAccountId(finProjectAccountPojo.getFinSiteProjAccMapId());
			finProjectAccountPojo.setSiteAccountId    (finProjectAccountPojo.getFinSiteProjAccMapId());
			finProjectAccountResponseList.add(finProjectAccountResponse);
		}
		return finProjectAccountResponseList;
	}

	public List<FinTransactionEntryResponse> finTransactionEntryPojoListToFinTransactionEntryResponseList(List<FinTransactionEntryPojo> finTransactionEntryPojoList, EmployeeFinancialTransactionServiceInfo tansactionServiceInfo) {
		log.info(" ***** The control is inside the finTransactionEntryPojoListToFinTransactionEntryResponseList in EmployeeFinancialMapper ***** ");
		List<FinTransactionEntryResponse> finTransactionEntryResponseList = new ArrayList<>();
		String condition = tansactionServiceInfo.getCondition()==null?"":tansactionServiceInfo.getCondition();
		//String actionUrl = tansactionServiceInfo.getActionUrl()==null?"":tansactionServiceInfo.getActionUrl();
		boolean isViewCustomerData = (tansactionServiceInfo.getCondition()!=null && tansactionServiceInfo.getCondition().equalsIgnoreCase("ViewCustomerData"));
		boolean isThisCompletedTransaction = (condition.equalsIgnoreCase("loadCompletedTransaction"));
		boolean loadCompletedTrnActiveBookingData = (condition.equalsIgnoreCase("loadCompletedTrnActiveBookingData"));
		boolean clearedCompletedTransaction = (condition.equalsIgnoreCase("clearedCompletedTransaction"));
		boolean transactionStatus = tansactionServiceInfo.getCondition().equalsIgnoreCase("transactionStatus");
		//boolean isRequestedInterestWaiverData = (actionUrl.equalsIgnoreCase("Interest Waiver"));
		List<Long> listOfTRNStatus = Arrays.asList(Status.NEW.getStatus(),Status.MODIFY.getStatus(),Status.APPROVED.getStatus(),Status.PENDING.getStatus());
		for(FinTransactionEntryPojo finTransactionEntryPojo : finTransactionEntryPojoList) {
			finTransactionEntryPojo.setSiteAccountId(finTransactionEntryPojo.getFinSiteProjAccountMapId());//assigning site map acc id to site id, as we have to use getFinSiteProjAccountMapId
			FinTransactionEntryResponse finTransactionEntryResponse = new FinTransactionEntryResponse();
			BeanUtils.copyProperties(finTransactionEntryPojo, finTransactionEntryResponse);
			
			long transactionStatusId = finTransactionEntryPojo.getTransactionStatusId();
			if((transactionStatusId == Status.REJECTED.getStatus() || transactionStatusId == Status.CHEQUE_BOUNCED.getStatus())) {
				//if this is rejected and cheque bounced transaction, then last pending by need to update as who approved in last
				finTransactionEntryPojo.setPendingByLevel(finTransactionEntryPojo.getLastApprovalby());
			}
			if(isThisCompletedTransaction || loadCompletedTrnActiveBookingData ||clearedCompletedTransaction || transactionStatus) {
				//if this request from view completed transaction, then assign set off amount to transaction amount
				//as we are showing transaction payment set off wise
				finTransactionEntryPojo.setTransactionAmount(finTransactionEntryPojo.getTrnSetOffAmount());
				finTransactionEntryResponse.setTransactionAmount(finTransactionEntryPojo.getTrnSetOffAmount());
			}
			if (listOfTRNStatus.contains(transactionStatusId)) {
				finTransactionEntryResponse.setReceiptStage(Status.RECEIVED.getDescription());
			} else if (transactionStatusId == Status.TRANSACTION_COMPLETED.getStatus()) {
				finTransactionEntryResponse.setReceiptStage(Status.CLEARED.getDescription());
			} else if (transactionStatusId == Status.CHEQUE_BOUNCED.getStatus()) {
				finTransactionEntryResponse.setReceiptStage(Status.CHEQUE_BOUNCED.getDescription());
			} else {
				finTransactionEntryResponse.setReceiptStage(Status.RECEIVED.getDescription());
			}
			if(transactionStatusId == Status.TRANSACTION_COMPLETED.getStatus() && isViewCustomerData ) {
				
				if(finTransactionEntryResponse.getTransactionTypeName()!=null && finTransactionEntryResponse.getTransactionTypeName().equals("Payment")) {//isViewCustomerData && 
					finTransactionEntryResponse.setTransactionAmount((finTransactionEntryResponse.getTransactionAmount()-finTransactionEntryResponse.getTransactionAmount())-finTransactionEntryResponse.getTransactionAmount());
				}
				
				if(finTransactionEntryResponse.getTransactionModeName()!=null && finTransactionEntryResponse.getTransactionModeName().equals("Online")) {

					if(finTransactionEntryResponse.getTransactionTypeName()!=null && finTransactionEntryResponse.getTransactionTypeName().equals("Payment")) {
						finTransactionEntryResponse.setChequeOrOnlineDate(finTransactionEntryResponse.getTransactionDate());
						//finTransactionEntryResponse.setTransactionReceiveDate(finTransactionEntryResponse.getTransactionDate());//making check date and receive date same for online
					} else {
						finTransactionEntryResponse.setChequeOrOnlineDate(finTransactionEntryResponse.getTransactionReceiveDate());
						finTransactionEntryResponse.setChequeClearanceDate(finTransactionEntryResponse.getTransactionReceiveDate());
						finTransactionEntryResponse.setChequeDepositedDate(finTransactionEntryResponse.getTransactionReceiveDate());
						finTransactionEntryResponse.setTransactionDate(finTransactionEntryResponse.getTransactionReceiveDate());
					}
				}
			} if(transactionStatusId == Status.TRANSACTION_COMPLETED.getStatus() && (isThisCompletedTransaction || loadCompletedTrnActiveBookingData || clearedCompletedTransaction)) {
				if(finTransactionEntryResponse.getTransactionModeName()!=null && finTransactionEntryResponse.getTransactionModeName().equals("Online")) {

					if(finTransactionEntryResponse.getTransactionTypeName()!=null && finTransactionEntryResponse.getTransactionTypeName().equals("Payment")) {
						finTransactionEntryResponse.setChequeOrOnlineDate(finTransactionEntryResponse.getTransactionDate());
						//finTransactionEntryResponse.setTransactionReceiveDate(finTransactionEntryResponse.getTransactionDate());//making check date and receive date same for online
					} else {
						finTransactionEntryResponse.setChequeOrOnlineDate(finTransactionEntryResponse.getTransactionReceiveDate());
						finTransactionEntryResponse.setChequeClearanceDate(finTransactionEntryResponse.getTransactionReceiveDate());
						finTransactionEntryResponse.setChequeDepositedDate(finTransactionEntryResponse.getTransactionReceiveDate());
						finTransactionEntryResponse.setTransactionDate(finTransactionEntryResponse.getTransactionReceiveDate());
					}
				}
				
			} else if(finTransactionEntryResponse.getTransactionTypeName()!=null && finTransactionEntryResponse.getTransactionTypeName().equals("Payment")
					&& tansactionServiceInfo.getCondition()!=null && tansactionServiceInfo.getCondition().equalsIgnoreCase("approveTransaction")
					){
				finTransactionEntryResponse.setChequeOrOnlineDate(finTransactionEntryResponse.getTransactionDate());
				//finTransactionEntryResponse.setTransactionReceiveDate(finTransactionEntryResponse.getTransactionDate());
			}
			
			/*if(isViewCustomerData && finTransactionEntryResponse.getTransactionTypeName()!=null && finTransactionEntryResponse.getTransactionTypeName().equals("Payment")) {
				finTransactionEntryResponse.setTransactionAmount((finTransactionEntryResponse.getTransactionAmount()-finTransactionEntryResponse.getTransactionAmount())-finTransactionEntryResponse.getTransactionAmount());
			}*/
			
			finTransactionEntryResponse.setFinBankId(finTransactionEntryPojo.getBankId());
			finTransactionEntryResponse.setOperationType(tansactionServiceInfo.getCondition());
			List<Map<String,String>> map = new ArrayList<>();
			
			if(Util.isNotEmptyObject(finTransactionEntryPojo.getMultipleLocation())) {
				String[] locations = finTransactionEntryPojo.getMultipleLocation().split("@@");
				for (String location : locations) {
					Map<String,String> multipleLocations = new HashMap<>();
					String[] str =location.split("##");
					//multipleLocations.put(str[0], str[1]);
					multipleLocations.put("location", str[0]);
					//if(str.length>1) {
						multipleLocations.put("docName", str[1]);
					/*}else {
						multipleLocations.put("docName", "");
					}*/
					map.add(multipleLocations);
				}
			}
			finTransactionEntryResponse.setMultipleLocations(map);
			map = new ArrayList<>();
			if(Util.isNotEmptyObject(finTransactionEntryPojo.getTransactionReceipt())) {
				String[] locations = finTransactionEntryPojo.getTransactionReceipt().split("@@");
				for (String location : locations) {
					Map<String,String> multipleLocations = new HashMap<>();
					String[] str =location.split("##");
					//multipleLocations.put(str[0], str[1]);
					multipleLocations.put("location", str[0]);
					multipleLocations.put("docName", str[1]);
					map.add(multipleLocations);
				}
			}
			finTransactionEntryResponse.setTransactionReceipts(map);
			
			
			
			
		/*	new changes by bvr   */
			if (isThisCompletedTransaction) {
				finTransactionEntryResponse.setFinTransactionNo(finTransactionEntryPojo.getTransactionEntryId().toString());
				if (transactionStatusId == Status.CHEQUE_BOUNCED.getStatus()) {
					finTransactionEntryResponse.setReceiptStage("Bounce");
					//finTransactionEntryResponse.setTransactionClosedDate(null);
				}
			}
			
			finTransactionEntryResponseList.add(finTransactionEntryResponse);
		}
		return finTransactionEntryResponseList;
	}

	public FinTransactionEntryDetailsResponse finTransactionEntryDetailsPojoToFinTransactionEntryDetailsResponse(FinTransactionEntryDetailsPojo finTransactionEntryDetailsPojo, 
			List<FinTransactionSetOffResponse> finTransactionSetOffResponse, List<FinTransactionCommentsResponse> finTransactionCommentsResponseList, List<FinTransactionApprStatResponse> finTransactionApprStatResponseList, List<FinTransactionChangedDtlsResponse> transactionChangedDetailsResponseList,
			List<FinTransactionEntryDocResponse> transactionEntryDocResponses, List<FinTransactionEntryDocResponse> transactionReceiptResponses, List<FinancialProjectMileStoneResponse> previouseFinancialProjectMileStoneResponseList) throws Exception {
		log.info(" ***** The control is inside the finTransactionPojoToFinTransactionChequeResponse in EmployeeFinancialMapper ***** ");
		FinTransactionEntryDetailsResponse finTransactionEntryDetailsResponse = new FinTransactionEntryDetailsResponse();
		//BeanUtils.copyProperties(finTransactionEntryDetailsPojo, finTransactionEntryDetailsResponse);
		
		finTransactionEntryDetailsResponse = dozerBeanMapper.map(finTransactionEntryDetailsPojo, FinTransactionEntryDetailsResponse.class);
		finTransactionEntryDetailsResponse.setFinTransactionSetOffResponseList(finTransactionSetOffResponse);
		finTransactionEntryDetailsResponse.setFinTransactionCommentsResponseList(finTransactionCommentsResponseList);
		finTransactionEntryDetailsResponse.setFinTransactionApprStatResponseList(finTransactionApprStatResponseList);
		finTransactionEntryDetailsResponse.setTransactionChangedDetailsResponseList(transactionChangedDetailsResponseList);
		finTransactionEntryDetailsResponse.setTransactionEntryDocResponsesList(transactionEntryDocResponses);
		finTransactionEntryDetailsResponse.setTransactionReceiptDocResponsesList(transactionReceiptResponses);
		
		if (Util.isNotEmptyObject(previouseFinancialProjectMileStoneResponseList)) {
			if (finTransactionSetOffResponse.size() != previouseFinancialProjectMileStoneResponseList.size()) {
				throw new EmployeeFinancialServiceException("Failed to load Interest waiver");
			}

			for (int index = 0; index < previouseFinancialProjectMileStoneResponseList.size(); index++) {
				FinancialProjectMileStoneResponse msResp = previouseFinancialProjectMileStoneResponseList.get(index);
					msResp.setSetOffAmount(finTransactionSetOffResponse.get(index).getSetOffAmount());
					if(finTransactionEntryDetailsPojo.getTransactionStatusId()!=null && finTransactionEntryDetailsPojo.getTransactionStatusId().equals(Status.TRANSACTION_COMPLETED.getStatus())) {
						double adjAmount = msResp.getInterestWaiverAdjAmount()==null?0d:msResp.getInterestWaiverAdjAmount();
						double setOffAmount = msResp.getSetOffAmount()==null?0d:msResp.getSetOffAmount();
						msResp.setInterestWaiverAdjAmount(new BigDecimal(adjAmount-setOffAmount).setScale(roundingModeSize, roundingMode).doubleValue());
					}
					/*if(finTransactionSetOffResponse.get(index).getFinBokAccInvoiceNo().equals("")) {
					
				}*/
				
			}
		}		
		
		finTransactionEntryDetailsResponse.setFinancialProjectMileStoneResponseList(previouseFinancialProjectMileStoneResponseList);
		return finTransactionEntryDetailsResponse;
	}

	public FinTransactionEntryDetailsInfo finTransactionEntryDetailsPojoToFinTransactionEntryDetailsInfo(FinTransactionEntryDetailsPojo finTransactionEntryDetailsPojo) {
		log.info(" ***** The control is inside the finTransactionChequePojoToFinTransactionChequeInfo in EmployeeFinancialMapper ***** ");
		FinTransactionEntryDetailsInfo finTransactionEntryDetailsInfo = new FinTransactionEntryDetailsInfo();
		BeanUtils.copyProperties(finTransactionEntryDetailsPojo, finTransactionEntryDetailsInfo);
		return finTransactionEntryDetailsInfo;
	}

	public List<FinTransactionSetOffResponse> finTransactionSetOffPojoListToFinTransactionSetOffResponseList(List<FinTransactionSetOffPojo> finTransactionSetOffPojoList) {
		log.info(" ***** The control is inside the finTransactionEntryPojoListToFinTransactionEntryResponseList in EmployeeFinancialMapper ***** ");
		List<FinTransactionSetOffResponse> finTransactionSetOffResponseList = new ArrayList<>();
		for(FinTransactionSetOffPojo finTransactionSetOffPojo : finTransactionSetOffPojoList) {
			FinTransactionSetOffResponse finTransactionSetOffResponse = new FinTransactionSetOffResponse();
			BeanUtils.copyProperties(finTransactionSetOffPojo, finTransactionSetOffResponse);
			finTransactionSetOffResponseList.add(finTransactionSetOffResponse);
		}
		return finTransactionSetOffResponseList;
	}

	public List<FinTransactionCommentsResponse> finTransactionCommentsPojoListToFinTransactionCommentsResponseList(List<FinTransactionCommentsPojo> finTransactionCommentsPojoList) {
		log.info(" ***** The control is inside the finTransactionEntryPojoListToFinTransactionEntryResponseList in EmployeeFinancialMapper ***** ");
		List<FinTransactionCommentsResponse> finTransactionCommentsResponseList = new ArrayList<>();
		for(FinTransactionCommentsPojo finTransactionCommentsPojo : finTransactionCommentsPojoList) {
			FinTransactionCommentsResponse finTransactionCommentsResponse = new FinTransactionCommentsResponse();
			BeanUtils.copyProperties(finTransactionCommentsPojo, finTransactionCommentsResponse);
			finTransactionCommentsResponseList.add(finTransactionCommentsResponse);
		}
		return finTransactionCommentsResponseList;
	}

	public List<FinBookingFormAccountsResponse> finBookingFormAccountsPojoListToFinBookingFormAccountsResponseList(List<FinBookingFormAccountsPojo> finBookingFormAccountsInvoicesPojoList) {
		log.info(" ***** The control is inside the finBookingFormAccountsPojoListToFinBookingFormAccountsResponseList in EmployeeFinancialMapper ***** ");
		List<FinBookingFormAccountsResponse> finBookingFormAccountsResponseList = new ArrayList<>();
		for(FinBookingFormAccountsPojo finBookingFormAccountsPojo : finBookingFormAccountsInvoicesPojoList) {
			FinBookingFormAccountsResponse finBookingFormAccountsResponse = new FinBookingFormAccountsResponse();
			BeanUtils.copyProperties(finBookingFormAccountsPojo, finBookingFormAccountsResponse);
			finBookingFormAccountsResponseList.add(finBookingFormAccountsResponse);
		}
		return finBookingFormAccountsResponseList;
	}

	public FinBookingFormAccountPaymentPojo constructFinBookingFormAccountPaymentPojo(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			Double refundAmount, FinTransactionEntryDetailsPojo entryDetailsPojo, Class<?> targetClass) {
		FinBookingFormAccountPaymentPojo accountPaymentPojo = new FinBookingFormAccountPaymentPojo();
		BeanUtils.copyProperties(transactionServiceInfo, accountPaymentPojo);
		accountPaymentPojo.setRefundAmount(refundAmount);
		
		if (Util.isNotEmptyObject(entryDetailsPojo)) {
			accountPaymentPojo.setType(entryDetailsPojo.getType());
			accountPaymentPojo.setTypeId(entryDetailsPojo.getTypeId());
		}
		accountPaymentPojo.setStatusId(Status.ACTIVE.getStatus());
		return accountPaymentPojo;
	}
	
	public FinBookingFormAccountPaymentPojo constructFinBookingFormAccountPaymentOfStatementPojo(EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			Double refundAmount, FinBookingFormAccountsStatementPojo accStmtPojo, Class<?> targetClass) {
		FinBookingFormAccountPaymentPojo accountPaymentPojo = new FinBookingFormAccountPaymentPojo();
		BeanUtils.copyProperties(transactionServiceInfo, accountPaymentPojo);
		accountPaymentPojo.setRefundAmount(refundAmount);
		
		if (Util.isNotEmptyObject(accStmtPojo)) {
			accountPaymentPojo.setType(MetadataId.FIN_BOOKING_FORM_ACCOUNTS_STATEMENT.getId());
			accountPaymentPojo.setTypeId(accStmtPojo.getFinBookingFormAccountsStatementId());
			accountPaymentPojo.setComments("Do not include booking statement amount to FIN_BOOKING_FORM_ACCOUNT_PAYMENT table sum of refund amount, as it is already as milestone refund amount");
		}
		accountPaymentPojo.setStatusId(Status.ACTIVE.getStatus());
		return accountPaymentPojo;
	}

public FinAnonymousEntryInfo finAnonymousEntryPojoToFinAnonymousEntryInfo(FinAnonymousEntryPojo finAnonymousEntryPojo) {
		log.info(" ***** The control is inside the finAnonymousEntryPojoToFinAnonymousEntryInfo in EmployeeFinancialMapper ***** ");
		FinAnonymousEntryInfo finAnonymousEntryInfo = dozerBeanMapper.map(finAnonymousEntryPojo, FinAnonymousEntryInfo.class);
		return finAnonymousEntryInfo;
	}

	public List<FinAnnyEntryDocResponse> finAnnyEntryDocPojoListToFinAnnyEntryDocResponseList(List<FinAnnyEntryDocPojo> finAnnyEntryDocPojoList) {
		log.info(" ***** The control is inside the finAnnyEntryDocPojoListToFinAnnyEntryDocResponseList in EmployeeFinancialMapper ***** ");
		List<FinAnnyEntryDocResponse> finAnnyEntryDocResponseList = new ArrayList<>();
		for(FinAnnyEntryDocPojo finAnnyEntryDocPojo : finAnnyEntryDocPojoList) {
			FinAnnyEntryDocResponse finAnnyEntryDocResponse = dozerBeanMapper.map(finAnnyEntryDocPojo, FinAnnyEntryDocResponse.class);
			finAnnyEntryDocResponseList.add(finAnnyEntryDocResponse);
		}
		return finAnnyEntryDocResponseList;
	}

	public FinAnonymousEntryResponse finAnonymousEntryInfoTofinAnonymousEntryResponse(FinAnonymousEntryInfo finAnonymousEntryInfo) {
		log.info(" ***** The control is inside the finAnonymousEntryInfoTofinAnonymousEntryResponse in EmployeeFinancialMapper ***** ");
	    return dozerBeanMapper.map(finAnonymousEntryInfo, FinAnonymousEntryResponse.class);
	}

	
	public List<FinAnnyEntryCommentsResponse> finAnnyEntryCommentsPojoListToFinAnnyEntryCommentsResponseList(List<FinAnnyEntryCommentsPojo> finAnnyEntryCommentsPojoList, 
			EmployeeFinancialTransactionServiceInfo serviceInfo) {
		log.info(" ***** The control is inside the finAnnyEntryCommentsPojoListToFinAnnyEntryCommentsResponseList in EmployeeFinancialMapper ***** ");
		List<FinAnnyEntryCommentsResponse> finAnnyEntryCommentsResponseList = new ArrayList<>();
		for(FinAnnyEntryCommentsPojo finAnnyEntryCommentsPojo : finAnnyEntryCommentsPojoList) {
			FinAnnyEntryCommentsResponse finAnnyEntryCommentsResponse = dozerBeanMapper.map(finAnnyEntryCommentsPojo, FinAnnyEntryCommentsResponse.class);
			finAnnyEntryCommentsResponseList.add(finAnnyEntryCommentsResponse);
			/*if(serviceInfo.getRequestUrl()==null || !serviceInfo.getRequestUrl().equals("LoadModifySuspenceEntry")) {
				
			}*/
			
		}
		return finAnnyEntryCommentsResponseList;
	}

	public List<FinTransferModeResponse> finTransferModePojoListToFinTransferModeResponseList(List<FinTransferModePojo> finTransferModePojoList) {
		log.info(" ***** The control is inside the finTransferModePojoListToFinTransferModeResponseList in EmployeeFinancialMapper ***** ");
		List<FinTransferModeResponse> finTransferModeResponseList = new ArrayList<>();
		for(FinTransferModePojo finTransferModePojo : finTransferModePojoList) {
			FinTransferModeResponse finTransferModeResponse = dozerBeanMapper.map(finTransferModePojo, FinTransferModeResponse.class);
			finTransferModeResponseList.add(finTransferModeResponse);
		}
		return finTransferModeResponseList;
	}

	public List<FinTransactionApprStatResponse> finTransactionApprStatPojoListToFinTransactionApprStatResponseList(List<FinTransactionApprStatPojo> finTransactionApprStatPojoList) {
		log.info(" ***** The control is inside the finTransactionApprStatPojoListToFinTransactionApprStatResponseList in EmployeeFinancialMapper ***** ");
		List<FinTransactionApprStatResponse> finTransactionApprStatResponseList = new ArrayList<>();
		for(FinTransactionApprStatPojo finTransactionApprStatPojo : finTransactionApprStatPojoList) {
			if(Util.isNotEmptyObject(finTransactionApprStatPojo.getComments())) {
				FinTransactionApprStatResponse finTransactionApprStatResponse = dozerBeanMapper.map(finTransactionApprStatPojo, FinTransactionApprStatResponse.class);
				finTransactionApprStatResponseList.add(finTransactionApprStatResponse);
			}
		}
		return finTransactionApprStatResponseList;
	}
	
	public List<FinAnnyApproveStatResponse> finAnonymousApprStatPojoListToFinAnonymousApprStatResponseList(List<FinAnnyApproveStatPojo> finTransactionApprStatPojoList, 
			List<FinAnnyEntryCommentsResponse> finAnnyEntryCommentsResponseList, EmployeeFinancialTransactionServiceInfo serviceInfo) {
		List<FinAnnyApproveStatResponse> finTransactionApprStatResponseList = new ArrayList<>();
		//int number = 2;
		for(FinAnnyApproveStatPojo finTransactionApprStatPojo : finTransactionApprStatPojoList) {
			FinAnnyApproveStatResponse finTransactionApprStatResponse = dozerBeanMapper.map(finTransactionApprStatPojo, FinAnnyApproveStatResponse.class);
			finTransactionApprStatResponseList.add(finTransactionApprStatResponse);
			if(serviceInfo.getRequestUrl()==null || !serviceInfo.getRequestUrl().equals("LoadModifySuspenceEntry")) {
				if(Util.isNotEmptyObject(finTransactionApprStatResponse.getComment())) {
					finTransactionApprStatResponse.setComment(finTransactionApprStatResponse.getEmpName()+" - "+finTransactionApprStatResponse.getComment());
				}
			}
			if(serviceInfo.getRequestUrl()==null || !serviceInfo.getRequestUrl().equals("LoadModifySuspenceEntry")) {
				if(finAnnyEntryCommentsResponseList!=null)  {
					//adding CRM comments to approve data
					for (Iterator<FinAnnyEntryCommentsResponse> iterator = finAnnyEntryCommentsResponseList.iterator(); iterator.hasNext();) {
						FinAnnyEntryCommentsResponse commentResponse = iterator.next();
						//finTransactionApprStatResponse.setComment(commentResponse.getComments());
						
						if(MetadataId.CRM.getId().equals(commentResponse.getType()) && Util.isNotEmptyObject(commentResponse.getComments())) {
							FinAnnyApproveStatResponse apprStatResponse  = new FinAnnyApproveStatResponse();
							apprStatResponse.setComment(commentResponse.getEmpName()+" - "+commentResponse.getComments());
							apprStatResponse.setActionType(null);
							apprStatResponse.setEmpId(commentResponse.getCreatedBy());
							//apprStatResponse.setComment(finTransactionApprStatResponse.getEmpName()+" - "+finTransactionApprStatResponse.getComment());
							iterator.remove();
							finTransactionApprStatResponseList.add(apprStatResponse);
							break;
						}
					}
				}
				//number = 3;
			}
		}
		
		
		return finTransactionApprStatResponseList;
	}

	public List<FinPenaltyTaxResponse> finPenaltyTaxPojoListToFinPenaltyTaxResponseList(List<FinPenaltyTaxPojo> finPenaltyTaxPojoList) {
		log.info(" ***** The control is inside the finPenaltyTaxPojoListToFinPenaltyTaxResponseList in EmployeeFinancialMapper ***** ");
		List<FinPenaltyTaxResponse> finPenaltyTaxResponseList = new ArrayList<>();
		List<String> avoidDupObject = new ArrayList<>();
		for(FinPenaltyTaxPojo finPenaltyTaxPojo : finPenaltyTaxPojoList) {
			String str = (finPenaltyTaxPojo.getStartDate()==null?"":finPenaltyTaxPojo.getStartDate().toString())+(finPenaltyTaxPojo.getEndDate()==null?"":finPenaltyTaxPojo.getEndDate().toString())
					+(finPenaltyTaxPojo.getPercentageValue()==null?"":finPenaltyTaxPojo.getPercentageValue());
			if(avoidDupObject.contains(str)) {
				continue;
			}
			avoidDupObject.add(str);
			FinPenaltyTaxResponse finPenaltyTaxResponse = dozerBeanMapper.map(finPenaltyTaxPojo, FinPenaltyTaxResponse.class);
			finPenaltyTaxResponseList.add(finPenaltyTaxResponse);
		}
		return finPenaltyTaxResponseList;
	}

	public List<FinBookingFormLglCostDtlsPojo> finBookingFormLglCostDtlsInfoListToFinBookingFormLglCostDtlsPojoList(List<FinBookingFormLglCostDtlsInfo> finBookingFormLglCostDtlsInfoList, 
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo, FinBookingFormLegalCostPojo finBookingFormLegalCostPojo) {
		log.info(" ***** The control is inside the finBookingFormLglCostDtlsInfoListToFinBookingFormLglCostDtlsPojoList in EmployeeFinancialMapper ***** ");
		List<FinBookingFormLglCostDtlsPojo> finBookingFormLglCostDtlsPojoList = new ArrayList<>();
		for(FinBookingFormLglCostDtlsInfo finBookingFormLglCostDtlsInfo : finBookingFormLglCostDtlsInfoList) {
			FinBookingFormLglCostDtlsPojo finBookingFormLglCostDtlsPojo = dozerBeanMapper.map(finBookingFormLglCostDtlsInfo, FinBookingFormLglCostDtlsPojo.class);
			finBookingFormLglCostDtlsPojo.setCreatedBy(employeeFinancialTransactionServiceInfo.getEmployeeId());
			finBookingFormLglCostDtlsPojo.setFinBokFrmLegalCostId(finBookingFormLegalCostPojo.getFinBokFrmLegalCostId());
			finBookingFormLglCostDtlsPojoList.add(finBookingFormLglCostDtlsPojo);
		}
		return finBookingFormLglCostDtlsPojoList;
	}

	public FinBookingFormLegalCostPojo finBookingFormLegalCostInfoToFinBookingFormLegalCostPojo(FinBookingFormLegalCostInfo finBookingFormLegalCostInfo,
			EmployeeFinancialTransactionServiceInfo serviceInfo, CustomerPropertyDetailsInfo customerPropertyDetailsInfo) {
		log.info(" ***** The control is inside the finBookingFormLegalCostInfoToFinBookingFormLegalCostPojo in EmployeeFinancialMapper ***** ");
		FinBookingFormLegalCostPojo finBookingFormLegalCostPojo = dozerBeanMapper.map(finBookingFormLegalCostInfo, FinBookingFormLegalCostPojo.class);
		finBookingFormLegalCostPojo.setBookingFormId(customerPropertyDetailsInfo.getFlatBookingId());
		finBookingFormLegalCostPojo.setCreatedBy(serviceInfo.getEmployeeId());
		finBookingFormLegalCostPojo.setFinSiteProjAccountMapId(serviceInfo.getSiteAccountId());
		return finBookingFormLegalCostPojo;
	}

	public FinBokFrmLglCostTaxPojo finBookingFormLegalCostPojoTofinBokFrmLglCostTaxPojo(FinBookingFormLegalCostPojo finBookingFormLegalCostPojo, EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo) {
		log.info(" ***** The control is inside the finBookingFormLegalCostPojoTofinBokFrmLglCostTaxPojo in EmployeeFinancialMapper ***** ");
		FinBokFrmLglCostTaxPojo finBokFrmLglCostTaxPojo = new FinBokFrmLglCostTaxPojo();
		finBokFrmLglCostTaxPojo.setFinTaxMappingId(employeeFinancialTransactionServiceInfo.getFinTaxMappingId());
		finBokFrmLglCostTaxPojo.setFinBokFrmLegalCostId(finBookingFormLegalCostPojo.getFinBokFrmLegalCostId());
		finBokFrmLglCostTaxPojo.setAmount(finBookingFormLegalCostPojo.getTaxAmount());
		finBokFrmLglCostTaxPojo.setCreatedBy(finBookingFormLegalCostPojo.getCreatedBy());
		return finBokFrmLglCostTaxPojo;
	}

	public List<FinBookingFormLglCostDtlsResponse> finBookingFormLglCostDtlsPojoListTofinBookingFormLglCostDtlsResponseList(List<FinBookingFormLglCostDtlsPojo> finBookingFormLglCostDtlsPojoList) {
		log.info(" ***** The control is inside the finBookingFormLglCostDtlsPojoListTofinBookingFormLglCostDtlsResponseList in EmployeeFinancialMapper ***** ");
		List<FinBookingFormLglCostDtlsResponse> finBookingFormLglCostDtlsResponseList = new ArrayList<>();
		for(FinBookingFormLglCostDtlsPojo finBookingFormLglCostDtlsPojo : finBookingFormLglCostDtlsPojoList) {
			FinBookingFormLglCostDtlsResponse finBookingFormLglCostDtlsResponse = dozerBeanMapper.map(finBookingFormLglCostDtlsPojo, FinBookingFormLglCostDtlsResponse.class);
			finBookingFormLglCostDtlsResponseList.add(finBookingFormLglCostDtlsResponse);
		}
		return finBookingFormLglCostDtlsResponseList;
	}

	public List<FinBookingFormModiCostDtlsResponse> employeeFinModiCostInfoListToFinBookingFormModiCostDtlsPojoList(List<EmployeeFinModiCostInfo> modiCostDtlsInfosList) {
		log.info(" ***** The control is inside the employeeFinModiCostInfoListToFinBookingFormModiCostDtlsPojoList in EmployeeFinancialMapper ***** ");
		List<FinBookingFormModiCostDtlsResponse> finBookingFormModiCostDtlsResponseList = new ArrayList<>();
		for(EmployeeFinModiCostInfo employeeFinModiCostInfo : modiCostDtlsInfosList) {
			FinBookingFormModiCostDtlsResponse finBookingFormModiCostDtlsResponse = dozerBeanMapper.map(employeeFinModiCostInfo, FinBookingFormModiCostDtlsResponse.class);
			finBookingFormModiCostDtlsResponseList.add(finBookingFormModiCostDtlsResponse);
		}
		return finBookingFormModiCostDtlsResponseList;
	}

	public List<EmployeeFinancialTransactionResponse> employeeFinancialResponseToFinancialTransactionResponse(List<EmployeeFinancialResponse> employeeFinancialResponseList) {
		log.info(" ***** The control is inside the employeeFinancialResponseToFinancialTransactionResponse in EmployeeFinancialMapper ***** ");
		List<EmployeeFinancialTransactionResponse> employeeFinancialTransactionResponseList = new ArrayList<>();
		for(EmployeeFinancialResponse employeeFinancialResponse : employeeFinancialResponseList) {
			EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = dozerBeanMapper.map(employeeFinancialResponse, EmployeeFinancialTransactionResponse.class);
			employeeFinancialTransactionResponseList.add(employeeFinancialTransactionResponse);
		}
		return employeeFinancialTransactionResponseList;
	}

	public List<FinTransactionForResponse> finFinTransactionForPojoListToFinTransactionForResponseList(List<FinTransactionForPojo> transactionForPojos) {
		log.info(" ***** EmployeeFinancialMapper.finFinTransactionForPojoListToFinTransactionForResponseList()  ***** ");
		List<FinTransactionForResponse> transactionForResponses = new ArrayList<>();
		for (FinTransactionForPojo finTransactionForPojo : transactionForPojos) {
			FinTransactionForResponse transactionForResponse = dozerBeanMapper.map(finTransactionForPojo, FinTransactionForResponse.class);
			transactionForResponses.add(transactionForResponse);
		}
		return transactionForResponses;
	}

	public List<FinTransactionChangedDtlsResponse> copyPropertiesFromFinTransactionChangedDtlsPojoTOFinTransactionChangedDtlsResponseList(
			List<FinTransactionChangedDtlsPojo> changedDetailsPojoList) {
			log.info("***** Control inside EmployeeFinancialMapper.copyPropertiesFromFinTransactionChangedDtlsPojoTOFinTransactionChangedDtlsResponseList()");
			List<FinTransactionChangedDtlsResponse> changedDtlsResponses = new ArrayList<>();
			for (FinTransactionChangedDtlsPojo changedDtlsPojo : changedDetailsPojoList) {
				FinTransactionChangedDtlsResponse transactionForResponse = dozerBeanMapper.map(changedDtlsPojo, FinTransactionChangedDtlsResponse.class);
				changedDtlsResponses.add(transactionForResponse);
			}
			return changedDtlsResponses;
	}

	public List<FinTransactionEntryDocResponse> copyPropertiesFromFinTransactionEntryDocPojoTOFinTransactionEntryDocResponseList(
			List<FinTransactionEntryDocPojo> transactionEntryDocPojos) {
		log.info("EmployeeFinancialMapper.copyPropertiesFromFinTransactionEntryDocPojoTOFinTransactionEntryDocResponseList()");
		List<FinTransactionEntryDocResponse> entryDocResponses = new ArrayList<>();
		for (FinTransactionEntryDocPojo entryDocPojo : transactionEntryDocPojos) {
			FinTransactionEntryDocResponse transactionForResponse = dozerBeanMapper.map(entryDocPojo, FinTransactionEntryDocResponse.class);
			entryDocResponses.add(transactionForResponse);
		}
		return entryDocResponses;
	}
	
	
	public FlatCancellationCostPojo createFlatCancellationCostPojo(Long bookingFormId,Long transactionEntryId,Double amount) {
		FlatCancellationCostPojo pojo = new FlatCancellationCostPojo();
		pojo.setBookingFormId(bookingFormId);
		pojo.setFinTransactionEntryId(transactionEntryId);
		pojo.setAmount(amount);
		pojo.setStatus(Status.ACTIVE.getStatus());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return pojo;
	}

	public FinBookingFormRefundableAdvancePojo createFinBookingFormRefundableAdvancePojo(Long bookingFormId,
			Long transactionEntryId, double amount, Long employeeId) {
		FinBookingFormRefundableAdvancePojo pojo = new FinBookingFormRefundableAdvancePojo();
		pojo.setFinTransactionEntryId(transactionEntryId);
		pojo.setBookingFormId(bookingFormId);
		pojo.setAmount(amount);
		pojo.setStatus(Status.ACTIVE.getStatus());
		pojo.setCreatedBy(employeeId);
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		return pojo;
	}
	
	public  FinBookingFormAccountsPojo createFinBookingFormAccountsPojo(Long pk,Long metaDataId,EmployeeFinTranPaymentSetOffInfo paymentSetOffDetails,EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		FinBookingFormAccountsPojo finBookingFormAccountsPojo = new FinBookingFormAccountsPojo();
		finBookingFormAccountsPojo.setTypeId(pk);
		finBookingFormAccountsPojo.setType(metaDataId);
		finBookingFormAccountsPojo.setPayAmount(paymentSetOffDetails.getAmount());
		//finBookingFormAccountsPojo.setPaidAmount(paymentSetOffDetails.getAmount());
		finBookingFormAccountsPojo.setPaidAmount(0d);
		finBookingFormAccountsPojo.setMileStoneDueDate(null);
		finBookingFormAccountsPojo.setPaidDate(new Timestamp(new Date().getTime()));
		finBookingFormAccountsPojo.setStatusId(Status.ACTIVE.getStatus());
		finBookingFormAccountsPojo.setBookingFormId(transactionServiceInfo.getBookingFormId());
		finBookingFormAccountsPojo.setCreatedBy(transactionServiceInfo.getEmployeeId());
		finBookingFormAccountsPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		finBookingFormAccountsPojo.setModifiedBy(null);
		finBookingFormAccountsPojo.setModifiedDate(null);
		finBookingFormAccountsPojo.setPaymentStatus(Status.PENDING.getStatus());
		finBookingFormAccountsPojo.setFinBokAccInvoiceNo(null);
		finBookingFormAccountsPojo.setTaxAmount(null);
		finBookingFormAccountsPojo.setPrincipalAmount(paymentSetOffDetails.getAmount());
		//finBookingFormAccountsPojo.setBalanceAmount(0d);
		finBookingFormAccountsPojo.setBalanceAmount(paymentSetOffDetails.getAmount());
		return finBookingFormAccountsPojo;
	}
	
	public FinBookingFormAccountSummaryPojo createFinBookingFormAccountSummaryPojo(Long metaDataId,EmployeeFinTranPaymentSetOffInfo paymentSetOffDetails,EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		FinBookingFormAccountSummaryPojo accSummaryPojo = new FinBookingFormAccountSummaryPojo();
		accSummaryPojo.setBookingFormId(transactionServiceInfo.getBookingFormId());		
		accSummaryPojo.setPayableAmount(paymentSetOffDetails.getAmount());
		accSummaryPojo.setCreatedBy(transactionServiceInfo.getEmployeeId());
		accSummaryPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		accSummaryPojo.setModifiedBy(null);
		accSummaryPojo.setModifiedDate(null);
		//accSummaryPojo.setPaidAmount(paymentSetOffDetails.getAmount());
		accSummaryPojo.setPaidAmount(0d);
		accSummaryPojo.setBalanceAmount(paymentSetOffDetails.getAmount());
		accSummaryPojo.setType(metaDataId);
		if(metaDataId.equals(MetadataId.REFUNDABLE_ADVANCE.getId())) {
			accSummaryPojo.setRefundAmount(paymentSetOffDetails.getAmount());
		} else {
			accSummaryPojo.setRefundAmount(0d);
		}
		accSummaryPojo.setStatusId(Status.ACTIVE.getStatus());
		return accSummaryPojo;
	}
	
	public List<FinBookingFormMstSchTaxMapInfo> copyFinBokFrmDemNteSchTaxMapPojoTOFinBookingFormMstSchTaxMapInfo(
			List<FinBokFrmDemNteSchTaxMapPojo> demNteSchTaxMapPojos) {
		List<FinBookingFormMstSchTaxMapInfo> bookingFormMstSchTaxMapInfos = new ArrayList<>();
		for (FinBokFrmDemNteSchTaxMapPojo finBokFrmDemNteSchTaxMapPojo : demNteSchTaxMapPojos) {
			FinBookingFormMstSchTaxMapInfo transactionForResponse = dozerBeanMapper.map(finBokFrmDemNteSchTaxMapPojo, FinBookingFormMstSchTaxMapInfo.class);
			bookingFormMstSchTaxMapInfos.add(transactionForResponse);
		}
		return bookingFormMstSchTaxMapInfos;
	}
	
	
	public List<FlatBookingInfo> flatBookingPojoListToFlatBookingInfoList(List<FlatBookingPojo> flatBookingPojoList) {
		log.info("*** The control is inside the flatBookingPojoListToFlatBookingInfoList method in EmployeeFinancialMapper ***");
		List<FlatBookingInfo> flatBookingInfoList = new ArrayList<>();
		for(FlatBookingPojo flatBookingPojo : flatBookingPojoList) {
			FlatBookingInfo flatBookingInfo = dozerBeanMapper.map(flatBookingPojo, FlatBookingInfo.class);
			flatBookingInfoList.add(flatBookingInfo);
		}
		return flatBookingInfoList;
	}

	public List<FinBookingFormExcessAmountResponse> finBookingFormExcessAmountPojoListToFinBookingFormExcessAmountResponseList(List<FinBookingFormExcessAmountPojo> finBookingFormExcessAmountPojoList) {
		log.info("*** The control is inside the finBookingFormExcessAmountPojoListToFinBookingFormExcessAmountResponseList method in EmployeeFinancialMapper ***");
		List<FinBookingFormExcessAmountResponse> finBookingFormExcessAmountResponseList = new ArrayList<>();
		for(FinBookingFormExcessAmountPojo finBookingFormExcessAmountPojo : finBookingFormExcessAmountPojoList) {
			FinBookingFormExcessAmountResponse finBookingFormExcessAmountResponse = dozerBeanMapper.map(finBookingFormExcessAmountPojo, FinBookingFormExcessAmountResponse.class);
			finBookingFormExcessAmountResponseList.add(finBookingFormExcessAmountResponse);
		}
		return finBookingFormExcessAmountResponseList;
	}

	public List<OfficeDtlsResponse> officeDetailsPojoListToOfficeDetailsResponseList(List<OfficeDtlsPojo> officeDetailsPojoList) {
		log.info("*** The control is inside the officeDetailsPojoListToOfficeDetailsResponseList method in EmployeeFinancialMapper ***");
		List<OfficeDtlsResponse> officeDtlsResponseList = new ArrayList<>();
		for(OfficeDtlsPojo officeDtlsPojo : officeDetailsPojoList) {
			OfficeDtlsResponse officeDtlsResponse = dozerBeanMapper.map(officeDtlsPojo, OfficeDtlsResponse.class);
			officeDtlsResponseList.add(officeDtlsResponse);
		}
		return officeDtlsResponseList;
	}

	public FinBookingFormAccountsPojo constructFinBookingFormAccountsPojo(
			EmployeeFinancialTransactionServiceInfo serviceInfo, FinTransactionEntryDetailsPojo entryDetailsPojo,
			FinBookingFormReceiptsPojo bookingFormReceiptsPojo, Class<?> class1) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFinBookingFormAccountsPojo() *****");
		FinBookingFormAccountsPojo pojo = new FinBookingFormAccountsPojo();
		if(Util.isNotEmptyObject(bookingFormReceiptsPojo)) {
			//pojo.setPaidDate(null);
			pojo.setPaidDate(bookingFormReceiptsPojo.getPaidDate());
		}else {
			pojo.setPaidDate(null);
		}
		
		if (entryDetailsPojo != null) {
			if (entryDetailsPojo.getType() != null) {
				if ((entryDetailsPojo.getType().equals(MetadataId.REFUNDABLE_ADVANCE.getId()) || entryDetailsPojo.getType().equals(MetadataId.TDS.getId()))
						&& serviceInfo.getCondition()!=null && 
						(serviceInfo.getCondition().equals(FinEnum.DELETE_TRANSACTION.getName()) || serviceInfo.getCondition().equals(FinEnum.EDIT_TRANSACTION.getName()))
						) {
					//if the paymeny type is REFUNDABLE_ADVANCE or TDS then inactive the records, which we refunding amount
					pojo.setStatusId(Status.INACTIVE.getStatus());
				} else {
					pojo.setStatusId(Status.ACTIVE.getStatus());
				}
			}
			//System.out.println(entryDetailsPojo.getType());
		}
		
		pojo.setPaidAmount(entryDetailsPojo.getSetOffAmount());
		pojo.setPaymentStatus(Status.INPROGRESS.getStatus());
		pojo.setFinBookingFormAccountsId(entryDetailsPojo.getFinBookingFormAccountsId());
		pojo.setModifiedBy(serviceInfo.getEmployeeId());
		pojo.setCreatedBy(serviceInfo.getEmployeeId());
		pojo.setActiveStatusId(Status.ACTIVE.getStatus());
		pojo.setBookingFormId(serviceInfo.getBookingFormId());
		return pojo;
	}
	
	

	public FinBookingFormAccountSummaryPojo constructFinBookingFormAccountSummaryPojo(EmployeeFinancialTransactionServiceInfo serviceInfo,
			FinTransactionEntryDetailsPojo entryDetailsPojo, FinBookingFormReceiptsPojo bookingFormReceiptsPojoNotInUse,
			Class<FinBookingFormAccountSummaryPojo> class1) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFinBookingFormAccountSummaryPojo() *****");
		FinBookingFormAccountSummaryPojo accountSummaryPojo = new FinBookingFormAccountSummaryPojo();
		accountSummaryPojo.setPaidAmount(entryDetailsPojo.getSetOffAmount());
		accountSummaryPojo.setBookingFormId(serviceInfo.getBookingFormId());
		accountSummaryPojo.setModifiedBy(serviceInfo.getEmployeeId());
		accountSummaryPojo.setCreatedBy(serviceInfo.getEmployeeId());
		accountSummaryPojo.setStatusId(Status.ACTIVE.getStatus());
		if (entryDetailsPojo != null) {
			if (entryDetailsPojo.getType() != null) {
				if (entryDetailsPojo.getType().equals(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId())) {
					accountSummaryPojo.setType(MetadataId.PRINCIPAL_AMOUNT.getId());
				} else {
					accountSummaryPojo.setType(entryDetailsPojo.getType());
				}
			}
			//System.out.println(entryDetailsPojo.getType());
		}
		return accountSummaryPojo;
	}

	public FinBookingFormExcessAmountPojo constructFinBookingFormExcessAmountPojo(FinBookingFormAccountPaymentDetailsPojo paymentRefundDetailsPojo, 
			EmployeeFinancialTransactionServiceInfo serviceInfo) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFinBookingFormExcessAmountPojo() *****");
		FinBookingFormExcessAmountPojo excessAmountPojo = new FinBookingFormExcessAmountPojo();
		excessAmountPojo.setFinBookingFormExcessAmountId(paymentRefundDetailsPojo.getTypeId());
		excessAmountPojo.setUsedAmount(paymentRefundDetailsPojo.getRefundAmount());
		excessAmountPojo.setCreatedBy(serviceInfo.getEmployeeId());
		excessAmountPojo.setModifiedBy(serviceInfo.getEmployeeId());
		return excessAmountPojo;
	}

	public FinBookingFormAccountsPojo constructFinBookingFormAccountsPojo(
			FinBookingFormAccountPaymentDetailsPojo paymentRefundDetailsPojo,
			EmployeeFinancialTransactionServiceInfo serviceInfo) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFinBookingFormAccountsPojo() *****");
		FinBookingFormAccountsPojo pojo = new FinBookingFormAccountsPojo();
		pojo.setFinBookingFormAccountsId(paymentRefundDetailsPojo.getTypeId());
		pojo.setPaidAmount(paymentRefundDetailsPojo.getRefundAmount());
		pojo.setCreatedBy(serviceInfo.getEmployeeId());
		pojo.setModifiedBy(serviceInfo.getEmployeeId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setBookingFormId(serviceInfo.getBookingFormId());
		return pojo;
	}

	public FinPenaltyPojo constructFinPenaltyPojo(FinBookingFormAccountsPojo prevData, EmployeeFinancialTransactionServiceInfo serviceInfo) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFinPenaltyPojo() *****");
		FinPenaltyPojo finPenaltyPojo = new FinPenaltyPojo ();
		finPenaltyPojo.setFinPenaltyId(prevData.getTypeId());
		finPenaltyPojo.setActiveStatusId(Status.SHIFT_RECORD_DETAILS.getStatus());
		finPenaltyPojo.setStatusId(Status.INACTIVE.getStatus());
		finPenaltyPojo.setCreatedBy(serviceInfo.getEmployeeId());
		return finPenaltyPojo;
	}
	
	public EmployeeFinancialRequest arrangeMilestoneDetailsForUpload(EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialMapper.arrangeMilestoneDetailsForUpload() *****");
		//List<FinancialDemandNoteMS_TRN_Info> demandNoteMSRequests = new ArrayList<>();
		List<FinancialDemandNoteMS_TRN_Request> listOfMilestones = employeeFinancialRequest.getDemandNoteMSRequests();
		boolean flag = Util.isNotEmptyObject(listOfMilestones);
		if (flag) {
			String previousFlatNo = "";
			Timestamp previousDemandNote = null;
			long sizeOfMilestones = employeeFinancialRequest.getDemandNoteMSRequests().size();
			//for (FinancialDemandNoteMS_TRN_Request trn_Request : employeeFinancialRequest.getDemandNoteMSRequests()) {
			long index = -1;
			for (Iterator<FinancialDemandNoteMS_TRN_Request> iterator = listOfMilestones.iterator(); iterator.hasNext();) {
				FinancialDemandNoteMS_TRN_Request trn_Request = iterator.next();
				index++;
				/*if(index == 0) {
					previousFlatNo = trn_Request.getFlatNo(); 
					previousDemandNote = trn_Request.getDemandNoteDate();
				}*/
				if(!trn_Request.getFlatNo().equals(previousFlatNo)) {
					previousFlatNo = trn_Request.getFlatNo(); 
					previousDemandNote = trn_Request.getDemandNoteDate();
				}
				log.info("***** Control inside the EmployeeFinancialMapper.arrangeMilestoneDetailsForUpload() *****"+trn_Request.getFlatNo()+" "+trn_Request.getDemandNoteDate());
				if(trn_Request.getFlatNo().equals(previousFlatNo)) {
					if(trn_Request.getDemandNoteDate()==null) {
						log.info("*****Please give demand date, For flat no *****"+trn_Request);
						throw new EmployeeFinancialServiceException("Please give demand date, For flat no "+trn_Request.getFlatNo()+" and milestone no is "+trn_Request.getMileStoneNo());
					}
					if(previousDemandNote!=null && TimeUtil.removeTimePartFromTimeStamp1(previousDemandNote).equals(TimeUtil.removeTimePartFromTimeStamp1(trn_Request.getDemandNoteDate()))) {
						if(index < (sizeOfMilestones-1)) {
							FinancialDemandNoteMS_TRN_Request nexttrn_Request = listOfMilestones.get((int) (index+1));
							if(trn_Request.getFlatNo().equals(nexttrn_Request.getFlatNo()) && trn_Request.getDemandNoteDate().equals(nexttrn_Request.getDemandNoteDate())) {
								//iterator.remove();
								//index--;
								trn_Request.setMileStoneNo(null);
								trn_Request.setMileStoneDueDate(null);//removing object values so we delete it from next object iteration
								//nexttrn_Request.setExcelRecordNo("LoadALL_MS_"+index);
								nexttrn_Request.setDataUploadCondition("LoadALL_MS_"+index);
							}
						}
					} else {
						previousDemandNote = trn_Request.getDemandNoteDate();
					}
				} else {
					previousFlatNo = trn_Request.getFlatNo(); 
					previousDemandNote = trn_Request.getDemandNoteDate();
				}
				//demandNoteMSRequests.add(trn_Info);
			}
			
			for (Iterator<FinancialDemandNoteMS_TRN_Request> iterator = listOfMilestones.iterator(); iterator.hasNext();) {
				FinancialDemandNoteMS_TRN_Request trn_Request = iterator.next();
				//if the excel record no LoadALL_MS, then assign to setDataUploadCondition, 
				//using these setDataUploadCondition it will load the previous milestone data
				if(trn_Request.getExcelRecordNo()!=null && trn_Request.getExcelRecordNo().contains("LoadALL_MS")) {
					 trn_Request.setDataUploadCondition("LoadALL_MS_"+index);
				}
				if(trn_Request.getMileStoneDueDate() == null && trn_Request.getMileStoneNo() == null) {
					
					iterator.remove();
				}
			}
			System.out.println("EmployeeFinancialMapper.arrangeMilestoneDetailsForUpload() size : "+listOfMilestones.size()+"\n Milestone : "+listOfMilestones);	
		}
		return employeeFinancialRequest;
	}

	public EmployeeFinancialServiceInfo uploadEmployeeFinancialRequestToemployeeFinancialInfo(
			EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialMapper.uploadEmployeeFinancialRequestToemployeeFinancialInfo() *****");
		EmployeeFinancialServiceInfo employeeFinancialServiceInfo = dozerBeanMapper.map(employeeFinancialRequest, EmployeeFinancialServiceInfo.class);
		List<FinancialDemandNoteMS_TRN_Info> demandNoteTransactionRequests = new ArrayList<>();
		List<FinancialDemandNoteMS_TRN_Info> demandNoteMSRequests = new ArrayList<>();
		List<String> errorList = new ArrayList<String>();
		List<EmployeeFinTranPaymentSetOffRequest> paymentSetOffDetailsList = null;
		EmployeeFinTranPaymentSetOffRequest paymentSetOffRequest  = null;
		FinancialDemandNoteMS_TRN_Info trn_Info = null;
		List<String> arrays = Arrays.asList("Corpus Fund","Maintenance","Khata Charges");//,"Maintenance Charges"
		if (Util.isNotEmptyObject(employeeFinancialRequest.getDemandNoteTransactionRequests())) {
			for (FinancialDemandNoteMS_TRN_Request trn_Request : employeeFinancialRequest.getDemandNoteTransactionRequests()) {
				trn_Info = null;
				paymentSetOffDetailsList = null;
				paymentSetOffRequest  = null;
				
				trn_Info = dozerBeanMapper.map(trn_Request,FinancialDemandNoteMS_TRN_Info.class);
				
				if(Util.isEmptyObject(trn_Info.getTransactionTypeName())) {
					errorList.add("Invalid Receipt Type found, For flat no "+trn_Info.getFlatNo()+" and amount is "+trn_Info.getTransactionAmount());	
				}
				
				if(Util.isEmptyObject(trn_Info.getTransactionModeName())) {
					errorList.add("Invalid Transaction Mode found, For flat no "+trn_Info.getFlatNo()+" and amount is "+trn_Info.getTransactionAmount());
				}
				
				if(Util.isEmptyObject(trn_Info.getTransactionFor())) {
					errorList.add("Invalid Transaction For found, For flat no "+trn_Info.getFlatNo()+" and amount is "+trn_Info.getTransactionAmount());
				}
				
				
				if (trn_Info.getTransactionTypeName() != null && trn_Info.getTransactionTypeName().length() != 0
						&& trn_Info.getTransactionModeName().equals(MetadataId.CHEQUE.getName())) {
					trn_Info.setTransactionDate(trn_Info.getChequeDate());
					trn_Info.setTransactionReceiveDate(trn_Info.getChequeReceiveDate());
					trn_Info.setDN_or_TRN_date(trn_Info.getChequeReceiveDate());
					if(Util.isNotEmptyObject(trn_Info.getChequeClearanceDate()) && Util.isEmptyObject(trn_Info.getChequeDepositedDate())) {
						trn_Info.setChequeDepositedDate(trn_Info.getChequeClearanceDate());
						if(Util.isEmptyObject(trn_Info.getTransactionClosedDate())) {
							trn_Info.setTransactionClosedDate(trn_Info.getChequeClearanceDate());	
						}
					}
					
					if(trn_Info.getTransactionTypeName().equals(MetadataId.RECEIPT.getName()) ) {
						System.out.println(trn_Info.getTransactionReceiveDate()+" "+trn_Info.getTransactionDate()+" "+trn_Info.getTransactionTypeName());
						if(trn_Info.getTransactionReceiveDate().before(trn_Info.getTransactionDate())) {
							//throw new EmployeeFinancialServiceException("Transaction receive date found before the cheque date, Please give correct cheque receive date, For flat no "+trn_Info.getFlatNo()+" and amount is "+trn_Info.getTransactionAmount());
							errorList.add("Transaction receive date found before the cheque date, Please give correct cheque receive date, For flat no "+trn_Info.getFlatNo()+" and amount is "+trn_Info.getTransactionAmount());
						}
					}
					
					if(trn_Info.getTransactionTypeName().equals(MetadataId.PAYMENT.getName()) && trn_Info.getTransactionFor()!=null) {
						trn_Info.setDN_or_TRN_date(trn_Info.getChequeDate());
					}
				} else {
					trn_Info.setTransactionReceiveDate(trn_Info.getOnlineReceiveDate());
					trn_Info.setDN_or_TRN_date(trn_Info.getOnlineReceiveDate());
					/*if(trn_Info.getTransactionTypeName().equals("Payment") && trn_Info.getTransactionFor()!=null) {
						trn_Info.setDN_or_TRN_date(trn_Info.getOnlineReceiveDate());
					}*/
				}
				
				if(arrays.contains(trn_Info.getTransactionFor().trim())) {
					paymentSetOffDetailsList = new ArrayList<>();
					paymentSetOffRequest  = new EmployeeFinTranPaymentSetOffRequest();
					
					trn_Request.setPaymentSetOffDetails(null);
					trn_Info.setPaymentSetOffDetails(null);
					 
					if("Corpus Fund".equalsIgnoreCase(trn_Info.getTransactionFor().trim())) {
						paymentSetOffRequest.setSetOffTypeName("Corpus_Fund");
						paymentSetOffRequest.setAmount(trn_Info.getTransactionAmount());
					} else if("Maintenance".equalsIgnoreCase(trn_Info.getTransactionFor().trim())) {
						paymentSetOffRequest.setSetOffTypeName("Maintenance_Charge");
						paymentSetOffRequest.setAmount(trn_Info.getTransactionAmount());
					} else if("Khata Charges".equalsIgnoreCase(trn_Info.getTransactionFor().trim())) {
						paymentSetOffRequest.setSetOffTypeName("Individual_Flat_Khata_bifurcation_and_other_charges");
						paymentSetOffRequest.setAmount(trn_Info.getTransactionAmount());
					}
					
					paymentSetOffDetailsList.add(paymentSetOffRequest);
					
					trn_Request.setPaymentSetOffDetails(paymentSetOffDetailsList);
					trn_Info.setPaymentSetOffDetails(paymentSetOffDetailsList);
					System.out.println(trn_Request.getPaymentSetOffDetails());
				}
				
				demandNoteTransactionRequests.add(trn_Info);
			}
		}
		
		trn_Info = null;
		if (Util.isNotEmptyObject(employeeFinancialRequest.getDemandNoteMSRequests())) {
			for (FinancialDemandNoteMS_TRN_Request trn_Request : employeeFinancialRequest.getDemandNoteMSRequests()) {
				trn_Info = null;		
				trn_Info = dozerBeanMapper.map(trn_Request, FinancialDemandNoteMS_TRN_Info.class);
				trn_Info.setDN_or_TRN_date(trn_Request.getDemandNoteDate());
				demandNoteMSRequests.add(trn_Info);
			}
		}
		employeeFinancialServiceInfo.setDemandNoteMSRequests(demandNoteMSRequests);
		employeeFinancialServiceInfo.setDemandNoteTransactionRequests(demandNoteTransactionRequests);
		
		if(Util.isNotEmptyObject(errorList)) {
			throw new EmployeeFinancialServiceException(errorList);
		}
		
		return employeeFinancialServiceInfo;
	}

	public EmployeeFinancialTransactionServiceInfo copyFinancialDemandNoteMS_TRN_InfoToEmployeeFinancialTransactionServiceInfo(
			EmployeeFinancialServiceInfo financialServiceInfo, FinancialDemandNoteMS_TRN_Info trn_Request, Class<?> class1) throws Exception {
		log.info("***** Control inside the EmployeeFinancialMapper.copyFinancialDemandNoteMS_TRN_InfoToEmployeeFinancialTransactionServiceInfo() *****");
			
		/*if(trn_Request.getReceiptStage()==null) {
			throw new EmployeeFinancialServiceException("Please give receipt stage, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
		} else if (!trn_Request.getReceiptStage().equals("Cleared") && !trn_Request.getReceiptStage().equals("Received") && !trn_Request.getReceiptStage().equals("Bounce")) {
			throw new EmployeeFinancialServiceException("Please give valid receipt stage, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
		}*/
		
		EmployeeFinancialTransactionServiceInfo transactionServiceInfo = dozerBeanMapper.map(trn_Request, EmployeeFinancialTransactionServiceInfo.class);
		if (Util.isNotEmptyObject(trn_Request.getIsShowGstInPDF())) {
			if (trn_Request.getIsShowGstInPDF().equalsIgnoreCase(Boolean.TRUE.toString())) {
				transactionServiceInfo.setIsShowGstInPDF(Boolean.TRUE);
			} else {
				transactionServiceInfo.setIsShowGstInPDF(Boolean.FALSE);
			}
		} else {
			transactionServiceInfo.setIsShowGstInPDF(Boolean.FALSE);
		}
		transactionServiceInfo.setBookingFormId(trn_Request.getFlatBookingId());
		transactionServiceInfo.setPortNumber(financialServiceInfo.getPortNumber());
		transactionServiceInfo.setEmployeeId(financialServiceInfo.getEmpId());
		if (transactionServiceInfo.getTransactionTypeName() != null && transactionServiceInfo.getTransactionModeName() != null) {
			if (transactionServiceInfo.getTransactionTypeName().equals(MetadataId.PAYMENT.getName())) {
				if (transactionServiceInfo.getTransactionModeName().equals(MetadataId.CHEQUE.getName())) {
					if(transactionServiceInfo.getTransactionDate()==null) {
						throw new EmployeeFinancialServiceException("Please give cheque date, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
					}
					/*transactionServiceInfo.setTransferModeId(null);
					transactionServiceInfo.setTransferModeName(null);*/
				} else if (transactionServiceInfo.getTransactionModeName().equals(MetadataId.ONLINE.getName())) {
					if(transactionServiceInfo.getTransactionReceiveDate()!=null && transactionServiceInfo.getTransactionDate()==null) {
						transactionServiceInfo.setTransactionDate(transactionServiceInfo.getTransactionReceiveDate());
					}
					if(transactionServiceInfo.getTransactionDate()==null) {
						throw new EmployeeFinancialServiceException("Please give cheque date, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
					}
					
					/*if(transactionServiceInfo.getTransactionReceiveDate()==null) {
						throw new EmployeeFinancialServiceException("Please give cheque receive date, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
					}*/
					
				}
			} else {//for receipt cheque and online
				if (transactionServiceInfo.getTransactionModeName().equals(MetadataId.CHEQUE.getName())) {
					transactionServiceInfo.setTransferModeId(null);//we don't have transfer mode for Cheque transaction
					transactionServiceInfo.setTransferModeName(null);
					if(Util.isEmptyObject(transactionServiceInfo.getTransactionDate())) {
						throw new EmployeeFinancialServiceException("Please give cheque date, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
					}
					
					if(Util.isEmptyObject(transactionServiceInfo.getTransactionReceiveDate())) {
						throw new EmployeeFinancialServiceException("Please give cheque receive date, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
					}
					System.out.println(transactionServiceInfo.getTransactionReceiveDate()+" "+(transactionServiceInfo.getTransactionDate()));
					if(transactionServiceInfo.getTransactionReceiveDate().before(transactionServiceInfo.getTransactionDate())) {
						throw new EmployeeFinancialServiceException("Transaction receive date found before the cheque date, Please give correct cheque receive date, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
					}
					
					//transactionServiceInfo.setTransactionForId(null);
					//transactionServiceInfo.setTransactionFor(null);
				} else if (transactionServiceInfo.getTransactionModeName().equals(MetadataId.ONLINE.getName())) {
					//transactionServiceInfo.setTransactionForId(null);
					//transactionServiceInfo.setTransactionFor(null);
					if(transactionServiceInfo.getTransactionReceiveDate()==null) {
						throw new EmployeeFinancialServiceException("Please give cheque receive date, For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
					}
				}	
			}
		}
			if (Util.isEmptyObject(transactionServiceInfo.getTransactionTypeId()) && Util.isNotEmptyObject(transactionServiceInfo.getTransactionTypeName())) {
				try {
					transactionServiceInfo.setTransactionTypeId(FinTransactionType.valueOf(transactionServiceInfo.getTransactionTypeName().toUpperCase()).getId());
				} catch (Exception ex) {
					throw new EmployeeFinancialServiceException("Please give valid transaction type, Invalid Type "+transactionServiceInfo.getTransactionTypeName()+", For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
				}				
			} 
			if (Util.isEmptyObject(transactionServiceInfo.getTransactionModeId()) && Util.isNotEmptyObject(transactionServiceInfo.getTransactionModeName())) {
				try {
					if (transactionServiceInfo.getTransactionModeName().equalsIgnoreCase("waived off")) {
						transactionServiceInfo.setTransactionModeName("waived_off");
					}
					transactionServiceInfo.setTransactionModeId(FinTransactionMode.valueOf(transactionServiceInfo.getTransactionModeName().toUpperCase()).getId());
					if (transactionServiceInfo.getTransactionModeName().equalsIgnoreCase("waived_off")) {
						transactionServiceInfo.setTransactionModeName("waived off");
					}
				} catch (Exception ex) {
					throw new EmployeeFinancialServiceException("Please give valid transaction mode, Invalid mode "+transactionServiceInfo.getTransactionModeName()+", For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());	
				}
			} 
			if (Util.isEmptyObject(transactionServiceInfo.getTransferModeId()) && Util.isNotEmptyObject(transactionServiceInfo.getTransferModeName())) {
				try {
				transactionServiceInfo.setTransferModeId(FinTransferMode.valueOf(transactionServiceInfo.getTransferModeName().toUpperCase()).getId());
				} catch (Exception ex) {
					throw new EmployeeFinancialServiceException("Please give valid transaction transfer mode, Invalid transfer mode "+transactionServiceInfo.getTransferModeName()+", For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
				}
			}
			if (Util.isEmptyObject(transactionServiceInfo.getTransactionForId()) && Util.isNotEmptyObject(transactionServiceInfo.getTransactionFor()) 
					&& transactionServiceInfo.getTransactionTypeName().equals(MetadataId.PAYMENT.getName())) {
				if(transactionServiceInfo.getTransactionFor().equals(MetadataId.AMOUNT_REFUND.getName())) {
					transactionServiceInfo.setTransactionFor("Refund");//we are using refund word so ,we need to changed amount refund to refund
				}
				try {
					transactionServiceInfo.setTransactionForId(FinTransactionFor.valueOf(transactionServiceInfo.getTransactionFor().toUpperCase()).getId());
				} catch (Exception ex) {
					throw new EmployeeFinancialServiceException("Please give valid transaction for, Invalid transaction for "+transactionServiceInfo.getTransactionFor()+", For flat no "+trn_Request.getFlatNo()+" and amount is "+trn_Request.getTransactionAmount());
				}
			}
			
		transactionServiceInfo.setSendNotification(Boolean.FALSE);
		transactionServiceInfo.setThisUplaodedData(Boolean.TRUE);
		transactionServiceInfo.setOperationType(FinEnum.UPLOADED_TRANSACTION.getName());
		transactionServiceInfo.setFlatNos(Arrays.asList(trn_Request.getFlatNo()));
		return transactionServiceInfo;
	}

	public EmployeeFinancialTransactionServiceInfo constructAnonymouesEntryInfo(EmployeeFinancialTransactionServiceInfo transactionServiceInfo) throws CloneNotSupportedException {
		log.info("***** Control inside the EmployeeFinancialMapper.constructAnonymouesEntryInfo() *****");
		EmployeeFinancialTransactionServiceInfo transactionServiceInfo1 = (EmployeeFinancialTransactionServiceInfo) transactionServiceInfo.clone();
		transactionServiceInfo1.setReceivedAmount(transactionServiceInfo.getTransactionAmount());
		if (Util.isEmptyObject(transactionServiceInfo1.getTransferModeId()) && Util.isNotEmptyObject(transactionServiceInfo1.getTransferModeName())) {
			transactionServiceInfo1.setTransferModeId(FinTransferMode.valueOf(transactionServiceInfo1.getTransferModeName().toUpperCase()).getId());
		}
		return transactionServiceInfo1;
	}

	public FinBankPojo constructFinBankPojo(EmployeeFinancialTransactionServiceInfo clonetransactionServiceInfo) {
		FinBankPojo pojo = new FinBankPojo();
		pojo.setBankName(clonetransactionServiceInfo.getBankName());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setCreatedBy(clonetransactionServiceInfo.getEmployeeId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}

	public EmployeeFinancialServiceInfo copyFinancialDemandNoteMS_TRN_InfoToEmployeeFinancialServiceInfo(
			EmployeeFinancialServiceInfo financialServiceInfo, FinancialDemandNoteMS_TRN_Info trn_MilestoneRequest,
			Class<EmployeeFinancialTransactionServiceInfo> class1) {
		EmployeeFinancialServiceInfo info = dozerBeanMapper.map(trn_MilestoneRequest,EmployeeFinancialServiceInfo.class);
		log.info( "***** Control inside the EmployeeFinancialMapper.copyFinancialDemandNoteMS_TRN_InfoToEmployeeFinancialServiceInfo() *****");
		List<FinancialProjectMileStoneInfo> financialProjectMileStoneRequests = new ArrayList<>();
		FinancialProjectMileStoneInfo projectMileStoneInfo = new FinancialProjectMileStoneInfo(); 
		BeanUtils.copyProperties(trn_MilestoneRequest, projectMileStoneInfo);
		projectMileStoneInfo.setMilestoneDate(trn_MilestoneRequest.getMilestoneCompletedDate());
		financialProjectMileStoneRequests.add(projectMileStoneInfo);
		info.setFinancialProjectMileStoneRequests(financialProjectMileStoneRequests);
		info.setDemandNoteSelectionType(FinEnum.SEND_SINGLE_MULTIPLE.getName());
		
		if (Util.isNotEmptyObject(trn_MilestoneRequest.getIsShowGstInPDF())) {
			if (trn_MilestoneRequest.getIsShowGstInPDF().equalsIgnoreCase(Boolean.TRUE.toString())) {
				info.setShowGstInPDF(Boolean.TRUE);
			} else {
				info.setShowGstInPDF(Boolean.FALSE);
			}
		} else {
			info.setShowGstInPDF(Boolean.FALSE);
		}
		
		//info.setShowGstInPDF(true);
		
		info.setReGenerateDemandNote(false);
		info.setThisUplaodedData(Boolean.TRUE);
		info.setSendNotification(Boolean.FALSE);
		info.setEmpId(financialServiceInfo.getEmpId());
		info.setPortNumber(financialServiceInfo.getPortNumber());
		info.setFlatNos(Arrays.asList(trn_MilestoneRequest.getFlatNo()));
		return info;
	}

	public FinSchemePojo constructFinSchemePojo(FinancialSchemeInfo financialSchemeInfo, EmployeeFinancialServiceInfo financialServiceInfo) {
		FinSchemePojo pojo = new FinSchemePojo();
		pojo.setFinSchemeName(financialSchemeInfo.getSchemeName());
		pojo.setFinSchemeDescription("");
		pojo.setCreatedBy(financialServiceInfo.getEmpId());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}

	public FinTaxPojo constructFinTaxPojo(FinancialSchemeInfo financialSchemeInfo, EmployeeFinancialServiceInfo financialServiceInfo) {
		FinTaxPojo pojo = new FinTaxPojo();
		pojo.setTaxName(financialSchemeInfo.getTaxName());
		if(financialSchemeInfo.getTaxName()!=null && financialSchemeInfo.getTaxName().equals(MetadataId.GST.getName())) {
			pojo.setAliasNameId(MetadataId.GST.getId());	
		}
		
		pojo.setCreatedBy(financialServiceInfo.getEmpId());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}

	public FinSchemeTaxMappingPojo constructFinSchemeTaxMappingPojo(FinancialSchemeInfo schemeDetails,EmployeeFinancialServiceInfo financialServiceInfo) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFinSchemeTaxMappingPojo() *****");
		
		FinSchemeTaxMappingPojo pojo = new FinSchemeTaxMappingPojo();
		pojo.setFinSchemeId(schemeDetails.getFinSchemeId());
		pojo.setFinTaxId(schemeDetails.getFinTaxId());
		pojo.setSiteId(financialServiceInfo.getSiteId());
		pojo.setPercentageId(schemeDetails.getPercentageId());
		pojo.setStartDate(schemeDetails.getStartDate());
		pojo.setEndDate(schemeDetails.getEndDate());
		pojo.setCreatedBy(financialServiceInfo.getEmpId());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}

	public FlatBookingSchemeMappingPojo constructFlatBookingSchemeMappingPojo(FinancialSchemeInfo schemeDetails,
			EmployeeFinancialServiceInfo financialServiceInfo) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFlatBookingSchemeMappingPojo() *****");
		
		FlatBookingSchemeMappingPojo pojo = new FlatBookingSchemeMappingPojo();
		pojo.setFinSchemeTaxMappingId(schemeDetails.getFinSchemeTaxMappingId());
		pojo.setBookingFormId(schemeDetails.getBookingFormId());
		pojo.setCreatedBy(financialServiceInfo.getEmpId());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}
	
	public FlatBookingSchemeMappingPojo constructFlatBookingSchemeMappingPojoBookingTime(FlatBookingInfo flatBookingInfo,
			FinSchemeTaxMappingPojo flatBookingSchemeMappingPojo, CustomerBookingFormInfo customerBookingFormInfo ) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFlatBookingSchemeMappingPojoBookingTime() *****");
		FlatBookingSchemeMappingPojo pojo = new FlatBookingSchemeMappingPojo();
		pojo.setFinSchemeTaxMappingId(flatBookingSchemeMappingPojo.getFinSchemeTaxMappingId());
		pojo.setBookingFormId(flatBookingInfo.getFlatBookingId());
		pojo.setModifiedBy(customerBookingFormInfo.getEmployeeId());
		pojo.setCreatedBy(customerBookingFormInfo.getEmployeeId());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}

	public FlatBookingSchemeMappingPojo constructFlatBookingSchemeMappingPojoBookingTime(
			FlatBookingInfo flatBookingInfo, CustomerBookingFormInfo customerBookingFormInfo) {
		FlatBookingSchemeMappingPojo pojo = new FlatBookingSchemeMappingPojo();
		pojo.setBookingFormId(flatBookingInfo.getFlatBookingId());
		pojo.setModifiedBy(customerBookingFormInfo.getEmployeeId());
		pojo.setCreatedBy(customerBookingFormInfo.getEmployeeId());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setStatusId(Status.INACTIVE.getStatus());
		pojo.setPastStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}
	
	public FinBookingFormAccountSummaryPojo constructFinBookingFormAccountSummaryPojo1(
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, FinancialProjectMileStonePojo accPojo,
			Class<FinBookingFormAccountSummaryPojo> class1) {
		log.info("***** Control inside the EmployeeFinancialMapper.constructFinBookingFormAccountSummaryPojo1() *****");
		FinBookingFormAccountSummaryPojo pojo = new FinBookingFormAccountSummaryPojo();
		if(accPojo.getType().equals(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId())) {
			pojo.setType(MetadataId.PRINCIPAL_AMOUNT.getId());
		}else {
			pojo.setType(accPojo.getType());
		}
		
		pojo.setBookingFormId(transactionServiceInfo.getBookingFormId());
		pojo.setPaidAmount(accPojo.getSetOffAmount());
		pojo.setCreatedBy(transactionServiceInfo.getEmployeeId());
		pojo.setModifiedBy(transactionServiceInfo.getEmployeeId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}

	public EmployeeFinancialTransactionServiceInfo constructEmployeeFinancialTransactionServiceInfoBean(
			FinTransactionEntryDetailsResponse dataForRecall, EmployeeFinancialMultipleTRNInfo employeeFinancialMultipleTRNInfo, Class<EmployeeFinancialTransactionServiceInfo> class1) throws Exception {
		copyPropertiesFromInfoBeanToPojoBean(dataForRecall, EmployeeFinancialTransactionServiceInfo.class);
		EmployeeFinancialTransactionServiceInfo transactionServiceInfoForRecall = dozerBeanMapper.map(dataForRecall, EmployeeFinancialTransactionServiceInfo.class);
		transactionServiceInfoForRecall.setButtonType(employeeFinancialMultipleTRNInfo.getButtonType());
		transactionServiceInfoForRecall.setChequeDepositedDate(employeeFinancialMultipleTRNInfo.getChequeDepositedDate());
		transactionServiceInfoForRecall.setChequeClearanceDate(employeeFinancialMultipleTRNInfo.getChequeClearanceDate());
		transactionServiceInfoForRecall.setComment(employeeFinancialMultipleTRNInfo.getComment());
		transactionServiceInfoForRecall.setTransferModeName(dataForRecall.getTransferMode());
		transactionServiceInfoForRecall.setOptionalButtonType(employeeFinancialMultipleTRNInfo.getOptionalButtonType());
		
		transactionServiceInfoForRecall.setChequeBounceComment(employeeFinancialMultipleTRNInfo.getChequeBounceComment());
		transactionServiceInfoForRecall.setChequeBounceDate(employeeFinancialMultipleTRNInfo.getChequeBounceDate());
		
		if(employeeFinancialMultipleTRNInfo.getTransactionTypeName().equals("Payment")) {
			if(dataForRecall.getChequeHandoverDate()==null) {
				transactionServiceInfoForRecall.setChequeHandoverDate(employeeFinancialMultipleTRNInfo.getChequeHandoverDate());
			}
			if( dataForRecall.getChequeNumber() == null) {
				transactionServiceInfoForRecall.setChequeNumber(employeeFinancialMultipleTRNInfo.getChequeNumber());
			}
			if(dataForRecall.getTransactionDate() == null) {
				transactionServiceInfoForRecall.setTransactionDate(employeeFinancialMultipleTRNInfo.getTransactionDate());
			}
		}
		
		if(dataForRecall.getChequeDepositedDate()!=null) {
			transactionServiceInfoForRecall.setChequeDepositedDate(dataForRecall.getChequeDepositedDate());	
		}
		
		if(dataForRecall.getChequeHandoverDate()!=null) {
			transactionServiceInfoForRecall.setChequeHandoverDate(dataForRecall.getChequeHandoverDate());	
		}
		
		if(employeeFinancialMultipleTRNInfo.getChequeBounceReasonValue()==null &&
		   employeeFinancialMultipleTRNInfo.getOptionalButtonType()!=null && 
		   employeeFinancialMultipleTRNInfo.getOptionalButtonType().equals("Cheque Bounced")) {
			transactionServiceInfoForRecall.setChequeBounceReasonValue(MetadataId.OTHER.getName());	
		}else {
			transactionServiceInfoForRecall.setChequeBounceReasonValue(employeeFinancialMultipleTRNInfo.getChequeBounceReasonValue());
		}
		
		return transactionServiceInfoForRecall;
	}

	public List<Map<String, Object>> constructNonRefundAmountAlert(List<Map<String, Object>> list) {
		List<String> refundEntryAlertMsgs = new ArrayList<>();
		
		for (Map<String, Object> map : list) {
			String customerName = map.get("CUST_NAME") == null ? "" : map.get("CUST_NAME").toString();
			String siteName = map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString();
			String custPaidAmount = map.get("CUST_PAID_AMOUNT") == null ? "" : map.get("CUST_PAID_AMOUNT").toString();
			String newStatus = map.get("NEW_STATUS") == null ? "" : map.get("NEW_STATUS").toString();
			String actualStatus = map.get("ACTUAL_STATUS") == null ? "" : map.get("ACTUAL_STATUS").toString();
			if(actualStatus.equals("active")) {
				actualStatus = "Booked";
			}
			String flatNo = map.get("FLAT_NO") == null ? "" : map.get("FLAT_NO").toString();
			StringBuffer alertMsg = new StringBuffer("");
			//Mr Aniket Chavan  i.e Sumadhura Nandanam A505 status changed from active to Swap, Please do refund entry of  8,51,930.50
			//Mr Aniket Chavan  i.e Sumadhura Nandhanam A505 status changed from "Booked" to "Swap", Please do refund entry of  8,51,930.50,
			alertMsg.append(customerName.trim()).append(" i.e ").append(siteName).append(" ").append(flatNo).append(" status changed from ")
			.append(actualStatus).append(" to ").append(newStatus)
			.append(", Please do refund entry of ").append(currencyUtil.getTheAmountWithCommas(Double.valueOf(custPaidAmount),roundingModeSize,roundingMode));
			refundEntryAlertMsgs.add(alertMsg.toString());
			map.put("customerName_FlatNo", customerName + "- " + flatNo);
			map.put("alertMsg",alertMsg);
		}
		
		return list;
	}

	public List<Map<String, Object>> constructNonRefundCustomerNameWithFlatNo(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			String customerName = map.get("CUST_NAME") == null ? "" : map.get("CUST_NAME").toString();
			String flatNo = map.get("FLAT_NO") == null ? "" : map.get("FLAT_NO").toString();
			map.put("customerName_FlatNo", customerName + "-" + flatNo);
		}
		return list;
	}

	public FinTransactionEntryDocPojo constructFinTransactionEntryDocPojoObject(
			FinTransactionEntryDocResponse entryDocResponse,
			EmployeeFinancialTransactionServiceInfo reConstructedtransactionServiceInfo) {
		FinTransactionEntryDocPojo pojo = new FinTransactionEntryDocPojo();
		pojo.setTransactionEntryId(reConstructedtransactionServiceInfo.getTransactionEntryId());
		pojo.setDocumentType(entryDocResponse.getDocumentType());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setDocumentLocation(entryDocResponse.getDocumentLocation());
		pojo.setDocumentName(entryDocResponse.getDocumentName());
		pojo.setFilePath(entryDocResponse.getFilePath());
		return pojo;
	}

	public FinTransactionEntryDocPojo copyPropertiesFromInfoBeanToModificationPojoBean(FileInfo fileInfoForModification,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Class<FinTransactionEntryDocPojo> class1) {
		FinTransactionEntryDocPojo pojo = dozerBeanMapper.map(fileInfoForModification, class1);
		pojo.setTransactionEntryId(transactionServiceInfo.getTransactionEntryId());
		pojo.setDocumentType(MetadataId.GENERATED.getId());//type of document, uploaded file or generated file
		pojo.setReceiptType(MetadataId.MODIFICATION_COST.getId());// ReceiptType can be princial, legal, modification
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setSaveFiles(true);
		if(FinTransactionMode.WAIVED_OFF.getId().equals(transactionServiceInfo.getTransactionModeId())) {
			pojo.setSaveFiles(false);
			pojo.setDocumentLocation(null);
			pojo.setDocumentName("-");
		}
		return pojo;
	}

	public FinTransactionEntryDocPojo copyPropertiesFromInfoBeanToLegalPojoBean(FileInfo fileInfoForLegal,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Class<FinTransactionEntryDocPojo> class1) {
		FinTransactionEntryDocPojo pojo = dozerBeanMapper.map(fileInfoForLegal, class1);
		pojo.setTransactionEntryId(transactionServiceInfo.getTransactionEntryId());
		pojo.setDocumentType(MetadataId.GENERATED.getId());
		pojo.setReceiptType(MetadataId.LEGAL_COST.getId());
		pojo.setStatusId(Status.ACTIVE.getStatus());	
		pojo.setSaveFiles(true);
		if(FinTransactionMode.WAIVED_OFF.getId().equals(transactionServiceInfo.getTransactionModeId())) {
			pojo.setSaveFiles(false);
			pojo.setDocumentLocation(null);
			pojo.setDocumentName("-");
		}
		return pojo;
	}
	public FinTransactionEntryDocPojo constructTransactionDocumentPojo(FileInfo fileInfoForLegal,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Long receiptType, Class<FinTransactionEntryDocPojo> class1) {
		FinTransactionEntryDocPojo pojo = dozerBeanMapper.map(fileInfoForLegal, class1);
		pojo.setTransactionEntryId(transactionServiceInfo.getTransactionEntryId());
		pojo.setDocumentType(MetadataId.GENERATED.getId());
		pojo.setReceiptType(receiptType);
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setSaveFiles(true);
		if(FinTransactionMode.WAIVED_OFF.getId().equals(transactionServiceInfo.getTransactionModeId())) {
			pojo.setSaveFiles(false);
			pojo.setDocumentLocation(null);
			pojo.setDocumentName("-");
		}
		return pojo;
	}

	
	public FinTransactionEntryDocPojo copyPropertiesFromInfoBeanToInterestPojoBean(FileInfo fileInfoForLegal,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Class<FinTransactionEntryDocPojo> class1) {
		FinTransactionEntryDocPojo pojo = dozerBeanMapper.map(fileInfoForLegal, class1);
		pojo.setTransactionEntryId(transactionServiceInfo.getTransactionEntryId());
		pojo.setDocumentType(MetadataId.GENERATED.getId());
		pojo.setReceiptType(MetadataId.FIN_PENALTY.getId());
		pojo.setStatusId(Status.ACTIVE.getStatus());	
		pojo.setSaveFiles(true);
		if(FinTransactionMode.WAIVED_OFF.getId().equals(transactionServiceInfo.getTransactionModeId())) {
			pojo.setSaveFiles(false);
			pojo.setDocumentLocation(null);
			pojo.setDocumentName("-");
		}
		return pojo;
	}
	
	public ModicationInvoiceAppRej constructModificationInvoiceApprRejectDetailsPojo(
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo, Status status) {
		ModicationInvoiceAppRej modicationInvoiceAppRej =new ModicationInvoiceAppRej();
		modicationInvoiceAppRej.setFinBookingFormModiCostId(transactionServiceInfo.getFinBookingFormModiCostId());
		modicationInvoiceAppRej.setEmpId(transactionServiceInfo.getEmployeeId());
		modicationInvoiceAppRej.setActionType(status.getStatus());
		modicationInvoiceAppRej.setComments(transactionServiceInfo.getComment()==null?"":transactionServiceInfo.getComment().trim());
		return modicationInvoiceAppRej;
	}
	
	public FinTransactionSetOffPojo copyPropertiesFromInfoBeanToTransactionSetOffPojoBean(
			double setOffGstAmount, EmployeeFinancialTransactionServiceInfo transactionServiceInfo,
			MetadataId metadataId, Class<FinTransactionSetOffPojo> class1) {
		FinTransactionSetOffPojo setOffPojo = new FinTransactionSetOffPojo();
		setOffPojo.setSetOffGstAmount(setOffGstAmount);
		//setOffPojo.setFinTransactionSetOffId(transactionServiceInfo.getTransactionSetOffEntryId());
		setOffPojo.setTransactionSetOffEntryId(transactionServiceInfo.getTransactionSetOffEntryId());
		setOffPojo.setStatusId(Status.ACTIVE.getStatus());
		setOffPojo.setSetOffType(metadataId.getId());
		return setOffPojo;
	}

	//SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	public List<FinMSChangedDtlsPojo> constructMilestoneChangedDetailsPojo(
			FinancialProjectMileStoneInfo changedMileStoneDetails, FinancialProjectMileStonePojo actualMileStonePojo, EmployeeFinancialServiceInfo serviceInfo
			, List<FinMSChangedDtlsPojo> listOfChangedDetails) throws Exception {
			
		 FinMSChangedDtlsPojo changedDtlsPojo = null;
		 if(Util.isNotEmptyObject(actualMileStonePojo.getMilestoneDemandNoteDate()) && Util.isNotEmptyObject(changedMileStoneDetails.getDemandNoteDate())) {
			 if(!TimeUtil.removeTimePartFromTimeStamp1(actualMileStonePojo.getMilestoneDemandNoteDate()).equals(TimeUtil.removeTimePartFromTimeStamp1(changedMileStoneDetails.getDemandNoteDate()))) {
				 changedDtlsPojo = new FinMSChangedDtlsPojo();
				 	
				    String previousMilestoneDate = dateFormat.format(actualMileStonePojo.getMilestoneDemandNoteDate());
					String currentMilestoneDate = dateFormat.format(changedMileStoneDetails.getDemandNoteDate());
					changedDtlsPojo.setActualValue(previousMilestoneDate);
					changedDtlsPojo.setChangedValue(currentMilestoneDate);
					
					changedDtlsPojo.setType(actualMileStonePojo.getType());
					changedDtlsPojo.setTypeId(actualMileStonePojo.getTypeId());
					changedDtlsPojo.setEmpId(serviceInfo.getEmpId());
					
					changedDtlsPojo.setColumnId(MetadataId.DEMAND_NOTE_DATE.getId());
					changedDtlsPojo.setActionType(Status.CHANGED.getStatus());
					changedDtlsPojo.setRemarks(new StringBuilder("")//transactionServiceInfo.getEmployeeName()
							.append(" changed demand note date ")
							.append(previousMilestoneDate)
							.append(" to ")
							.append(currentMilestoneDate).toString());
					listOfChangedDetails.add(changedDtlsPojo);
			 }
		 }
		 
		 if(Util.isNotEmptyObject(actualMileStonePojo.getMileStoneDueDate()) && Util.isNotEmptyObject(changedMileStoneDetails.getMileStoneDueDate())) {
			 if(!TimeUtil.removeTimePartFromTimeStamp1(actualMileStonePojo.getMileStoneDueDate()).equals(TimeUtil.removeTimePartFromTimeStamp1(changedMileStoneDetails.getMileStoneDueDate()))) {
				 	changedDtlsPojo = new FinMSChangedDtlsPojo();
				 	
				    String previousMilestoneDueDate = dateFormat.format(actualMileStonePojo.getMileStoneDueDate());
					String currentMilestoneDueDate = dateFormat.format(changedMileStoneDetails.getMileStoneDueDate());

					changedDtlsPojo.setActualValue(previousMilestoneDueDate);
					changedDtlsPojo.setChangedValue(currentMilestoneDueDate);
					
					changedDtlsPojo.setType(actualMileStonePojo.getType());
					changedDtlsPojo.setTypeId(actualMileStonePojo.getTypeId());
					changedDtlsPojo.setEmpId(serviceInfo.getEmpId());
					
					changedDtlsPojo.setColumnId(MetadataId.DUE_DATE.getId());
					changedDtlsPojo.setActionType(Status.CHANGED.getStatus());
					changedDtlsPojo.setRemarks(new StringBuilder("")//transactionServiceInfo.getEmployeeName()
							.append(" changed demand note due date ")
							.append(previousMilestoneDueDate)
							.append(" to ")
							.append(currentMilestoneDueDate).toString());
					listOfChangedDetails.add(changedDtlsPojo);
			 }
		 }
		
		return listOfChangedDetails;
	}

	public FinBookingFormMilestonesPojo constructEdit_Milestone_DetailsPojo(
			FinancialProjectMileStoneInfo finProjDemandNoteInfo, FinancialProjectMileStonePojo mileStonePojo) {
		FinBookingFormMilestonesPojo bookingFormMilestonesPojo = new FinBookingFormMilestonesPojo();
		
		bookingFormMilestonesPojo.setDemandNoteDate(finProjDemandNoteInfo.getDemandNoteDate());
		bookingFormMilestonesPojo.setMilestoneDemandNoteDate(finProjDemandNoteInfo.getDemandNoteDate());
		bookingFormMilestonesPojo.setFinBookingFormMilestonesId(mileStonePojo.getFinBookingFormMilestonesId());
		bookingFormMilestonesPojo.setStatusId(Status.ACTIVE.getStatus());
		bookingFormMilestonesPojo.setBookingFormId(mileStonePojo.getBookingFormId());
		bookingFormMilestonesPojo.setCreatedBy(finProjDemandNoteInfo.getCreatedBy());
		bookingFormMilestonesPojo.setModifiedBy(finProjDemandNoteInfo.getCreatedBy());
		
		return bookingFormMilestonesPojo;
	}

	public FinBookingFormAccountsPojo constructEdit_Accounts_DetailsPojo(
			FinancialProjectMileStoneInfo finProjDemandNoteInfo, FinancialProjectMileStonePojo mileStonePojo) {
		FinBookingFormAccountsPojo editAccountsPojo =  new FinBookingFormAccountsPojo();  

		editAccountsPojo.setMileStoneDueDate(finProjDemandNoteInfo.getMileStoneDueDate());
		editAccountsPojo.setFinBookingFormAccountsId(mileStonePojo.getFinBookingFormAccountsId());
		editAccountsPojo.setTypeId(mileStonePojo.getFinBookingFormMilestonesId());
		
		editAccountsPojo.setBookingFormId(mileStonePojo.getBookingFormId());
		editAccountsPojo.setCreatedBy(finProjDemandNoteInfo.getCreatedBy());
		editAccountsPojo.setModifiedBy(finProjDemandNoteInfo.getCreatedBy());
		editAccountsPojo.setType(MetadataId.FIN_BOOKING_FORM_MILESTONES.getId());
		editAccountsPojo.setStatusId(Status.ACTIVE.getStatus());
		editAccountsPojo.setIsInterestCalcCompleted(null);
		
		return editAccountsPojo;
	}

	public EmployeeFinancialTransactionServiceInfo constructTransactionObject(EmployeeFinancialRequest employeeFinancialRequest) {
		EmployeeFinancialTransactionServiceInfo info = new EmployeeFinancialTransactionServiceInfo();
		double amount = 0d;
		info.setSiteId(employeeFinancialRequest.getSiteId());
		info.setFlatIds(employeeFinancialRequest.getFlatIds());
		info.setRequestUrl(employeeFinancialRequest.getRequestUrl());
		info.setBookingFormId(employeeFinancialRequest.getBookingFormId());
		info.setTransactionTypeId(FinTransactionType.INTEREST_WAIVER.getId());
		info.setTransactionTypeName(FinTransactionType.INTEREST_WAIVER.getName());
		info.setPortNumber(employeeFinancialRequest.getPortNumber());
		info.setCreatedBy(Long.valueOf(employeeFinancialRequest.getEmpId()));
		info.setEmployeeId(Long.valueOf(employeeFinancialRequest.getEmpId()));
		info.setTransactionModeId(FinTransactionMode.INTEREST_WAIVER.getId());
		info.setTransactionModeName(FinTransactionMode.INTEREST_WAIVER.getName());
		
		info.setTransactionDate(new Timestamp(new Date().getTime()));
		
		info.setFileInfos(employeeFinancialRequest.getFileInfos());
		List<EmployeeFinTranPaymentSetOffInfo> paymentSetOffDetails = new ArrayList<EmployeeFinTranPaymentSetOffInfo>();
		List<FinancialProjectMileStoneRequest> milestoneRequest = employeeFinancialRequest.getFinancialProjectMileStoneRequests();
		
		for (FinancialProjectMileStoneRequest financialProjectMileStoneRequest : milestoneRequest) {
			EmployeeFinTranPaymentSetOffInfo setOffInfo = new EmployeeFinTranPaymentSetOffInfo();	
			setOffInfo.setAmount(financialProjectMileStoneRequest.getSetOffAmount());
			setOffInfo.setSetOffTypeName(MetadataId.INTEREST_WAIVER.getName());
			amount += financialProjectMileStoneRequest.getSetOffAmount();
			paymentSetOffDetails.add(setOffInfo);
		}
		info.setTransactionAmount(amount);
		info.setPaymentSetOffDetails(paymentSetOffDetails);
		return info;
	}

	public List<FinModificationInvoicePojo> EmployeeFinancialRequestToEmployeeFinancialRequest(List<FinModificationInvoicePojo> list) {
		log.info(" ***** The control is inside the EmployeeFinancialRequestToEmployeeFinancialRequest in EmployeeFinancialMapper ***** ");
		List<FinModificationInvoicePojo> finModificationInvoicePojoList = new ArrayList<>();
		for(FinModificationInvoicePojo finModificationInvoicePojo : list) {
			FinModificationInvoicePojo finModificationInvoiceinfo = new FinModificationInvoicePojo();
			BeanUtils.copyProperties(finModificationInvoicePojo, finModificationInvoiceinfo);
			finModificationInvoiceinfo.setInvocieType("Modification Invoice");
			finModificationInvoicePojoList.add(finModificationInvoiceinfo);
		}
		//log.debug("** The Converted Object from Pojo to Info is **"+finModificationInvoicePojoList);
		return finModificationInvoicePojoList;
	}

	public List<FinBookingFormModiCostResponse> copyModificationPojosToResponse(List<FinBookingFormModiCostPojo> modificationPojos,
			List<FinBookingFormModiCostDtlsPojo> modificationDetailsPojosList, CustomerPropertyDetailsInfo customerPropertyDetailsInfo, List<ModicationInvoiceAppRej> listOfApprRejectDetailsList) {
		List<FinBookingFormModiCostResponse> modificationResponseList = new ArrayList<>();
		List<FinBookingFormModiCostDtlsResponse> detailsResponseList = new ArrayList<>();
		FinBookingFormModiCostResponse modificationResp = new FinBookingFormModiCostResponse();
		modificationResp.setCustomerPropertyDetailsInfo(customerPropertyDetailsInfo);
		modificationResp.setApproveRejectDetailsList(listOfApprRejectDetailsList);
		
		if(Util.isNotEmptyObject(modificationPojos) && Util.isNotEmptyObject(modificationDetailsPojosList)) {
			//main details like booking form id and approval id

			BeanUtils.copyProperties(modificationPojos.get(0), modificationResp);
		
			//modification details like description, rate, unit
			for (FinBookingFormModiCostDtlsPojo finBookingFormModiCostDtlsPojo : modificationDetailsPojosList) {
				FinBookingFormModiCostDtlsResponse detailsResp = dozerBeanMapper.map(finBookingFormModiCostDtlsPojo, FinBookingFormModiCostDtlsResponse.class);
				detailsResponseList.add(detailsResp);
			}
			modificationResp.setFinBookingFormModiCostDtlsList(detailsResponseList);
			modificationResponseList.add(modificationResp);
			
		}
		
		return modificationResponseList;
	}

	public List<FinTransactionEntryDetailsInfo> copyTransactionEntryPojoToInfoPojoList(List<FinTransactionEntryPojo> finTransactionEntryPojoLists) {
		log.info("***** Control inside the EmployeeFinancialMapper.copyTransactionEntryPojoToInfoPojoList() *****");
		List<FinTransactionEntryDetailsInfo> transactionEntryDetailsInfos = new ArrayList<>();
		for(FinTransactionEntryPojo transactionEntryPojo : finTransactionEntryPojoLists){
			FinTransactionEntryDetailsInfo info = new FinTransactionEntryDetailsInfo();
			BeanUtils.copyProperties(transactionEntryPojo, info);
			transactionEntryDetailsInfos.add(info);
		}	
		return transactionEntryDetailsInfos;
	}

	public void validateInterestWaiverData(EmployeeFinancialServiceInfo serviceInfo1) throws Exception {
		List<FinancialProjectMileStoneInfo> milestoneRequest = serviceInfo1.getFinancialProjectMileStoneRequests();
		for (FinancialProjectMileStoneInfo finProjDemandNoteInfo : milestoneRequest) {
			if(finProjDemandNoteInfo.getMilestoneName()!=null && finProjDemandNoteInfo.getMilestoneName().equalsIgnoreCase("Dummy MileStone for Regenerate Demand Note")) {
				 continue;
			}
			
			if(Util.isEmptyObject(finProjDemandNoteInfo.getSetOffAmount())) {
				throw new EmployeeFinancialServiceException("Waiver Amount should be more than Zero, For Milestone Name : "+finProjDemandNoteInfo.getMilestoneName());
			}
		}
	}

	public FinBookingFormAccountsPojo constructMaintenanceChargesAccoutsRecord(FinBookingFormMaintenancePojo bookingFormMaintenancePojo) {
		FinBookingFormAccountsPojo accountsPojo = new FinBookingFormAccountsPojo();
		accountsPojo.setType(MetadataId.MAINTENANCE_CHARGE.getId());
		accountsPojo.setTypeId(bookingFormMaintenancePojo.getFinBokFrmMaintenanceId());
		accountsPojo.setPayAmount(bookingFormMaintenancePojo.getTotalAmount());
		accountsPojo.setTaxAmount(bookingFormMaintenancePojo.getTaxAmount());
		accountsPojo.setPrincipalAmount(bookingFormMaintenancePojo.getBasicAmount());
		accountsPojo.setBalanceAmount(bookingFormMaintenancePojo.getTotalAmount());
		accountsPojo.setBookingFormId(bookingFormMaintenancePojo.getBookingFormId());
		accountsPojo.setStatusId(Status.ACTIVE.getStatus());
		accountsPojo.setPaymentStatus(Status.PENDING.getStatus());
		accountsPojo.setCreatedBy(bookingFormMaintenancePojo.getCreatedBy());
		return accountsPojo;
	}

	public FinBokFrmMaintenanceTaxPojo constructMaintenanceTaxPojo(
			FinBookingFormMaintenanceDtlsPojo bookingFormMaintenanceDtlsPojo, Long maintenanceId) {
		FinBokFrmMaintenanceTaxPojo bokFrmMaintenanceTaxPojo = new FinBokFrmMaintenanceTaxPojo();
		bokFrmMaintenanceTaxPojo.setAmount(bookingFormMaintenanceDtlsPojo.getTaxAmount());
		bokFrmMaintenanceTaxPojo.setFinBokFormMaintenanceDtlsId(bookingFormMaintenanceDtlsPojo.getFinBokFormMaintenanceDtlsId());
		bokFrmMaintenanceTaxPojo.setFinTaxMappingId(maintenanceId);
		return bokFrmMaintenanceTaxPojo;
	}

	public FinBookingFormMaintenanceDtlsPojo constructMaintenanceDetailsPojo(
			FinBookingFormMaintenancePojo bookingFormMaintenancePojo,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {
		FinBookingFormMaintenanceDtlsPojo bookingFormMaintenanceDtlsPojo = new FinBookingFormMaintenanceDtlsPojo();
		bookingFormMaintenanceDtlsPojo.setBasicAmount(bookingFormMaintenancePojo.getBasicAmount());
		bookingFormMaintenanceDtlsPojo.setTaxAmount(bookingFormMaintenancePojo.getTaxAmount());
		bookingFormMaintenanceDtlsPojo.setFinBokFrmMaintenanceId(bookingFormMaintenancePojo.getFinBokFrmMaintenanceId());
		bookingFormMaintenanceDtlsPojo.setCreatedBy(transactionServiceInfo.getEmployeeId());
		//bookingFormMaintenanceDtlsPojo.setDescription("Maintenance_Charge");
		//by default take *Maintenance_Charge* for Maintenance Charges

		return bookingFormMaintenanceDtlsPojo;
	}

	public FinBookingFormAccountsPojo constructFlatKhataAccoutsRecord(FinBookingFormFlatKhataPojo bookingFormMaintenancePojo) {
		FinBookingFormAccountsPojo accountsPojo = new FinBookingFormAccountsPojo();
		accountsPojo.setType(MetadataId.INDIVIDUAL_FLAT_KHATA_BIFURCATION_AND_OTHER_CHARGES.getId());
		accountsPojo.setTypeId(bookingFormMaintenancePojo.getFinBokFrmFlatKhataId());
		accountsPojo.setPayAmount(bookingFormMaintenancePojo.getTotalAmount());
		accountsPojo.setTaxAmount(bookingFormMaintenancePojo.getTaxAmount());
		accountsPojo.setPrincipalAmount(bookingFormMaintenancePojo.getBasicAmount());
		accountsPojo.setBalanceAmount(bookingFormMaintenancePojo.getTotalAmount());
		accountsPojo.setBookingFormId(bookingFormMaintenancePojo.getBookingFormId());
		accountsPojo.setStatusId(Status.ACTIVE.getStatus());
		accountsPojo.setPaymentStatus(Status.PENDING.getStatus());
		accountsPojo.setCreatedBy(bookingFormMaintenancePojo.getCreatedBy());
		return accountsPojo;
	}

	public FinBookingFormFlatKhataDtlsPojo constructFlatKhataDetailsPojo(
			FinBookingFormFlatKhataPojo bookingFormMaintenancePojo,
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo) {

		FinBookingFormFlatKhataDtlsPojo bookingFormMaintenanceDtlsPojo = new FinBookingFormFlatKhataDtlsPojo();
		bookingFormMaintenanceDtlsPojo.setBasicAmount(bookingFormMaintenancePojo.getBasicAmount());
		bookingFormMaintenanceDtlsPojo.setTaxAmount(bookingFormMaintenancePojo.getTaxAmount());
		bookingFormMaintenanceDtlsPojo.setFinBokFrmFlatKhataId(bookingFormMaintenancePojo.getFinBokFrmFlatKhataId());
		bookingFormMaintenanceDtlsPojo.setCreatedBy(transactionServiceInfo.getEmployeeId());
		//bookingFormMaintenanceDtlsPojo.setDescription("Maintenance_Charge");
		//by default take *Maintenance_Charge* for Maintenance Charges

		return bookingFormMaintenanceDtlsPojo;
	}

	public FinBokFrmFlatKhataTaxPojo constructFlatKhataTaxPojo(
			FinBookingFormFlatKhataDtlsPojo bookingFormMaintenanceDtlsPojo, Long flatKhataId) {
		FinBokFrmFlatKhataTaxPojo bokFrmMaintenanceTaxPojo = new FinBokFrmFlatKhataTaxPojo();
		bokFrmMaintenanceTaxPojo.setAmount(bookingFormMaintenanceDtlsPojo.getTaxAmount());
		bokFrmMaintenanceTaxPojo.setFinBokFormFlatKhataDetailsId(bookingFormMaintenanceDtlsPojo.getFinBokFormFlatKhataDetailsId());
		bokFrmMaintenanceTaxPojo.setFinTaxMappingId(flatKhataId);
		return bokFrmMaintenanceTaxPojo;
	}

	public FinBookingFormAccountsPojo constructCorpusFundAccoutsRecord(
			FinBookingFormCorpusFundPojo bookingFormMaintenancePojo) {
		FinBookingFormAccountsPojo accountsPojo = new FinBookingFormAccountsPojo();
		accountsPojo.setType(MetadataId.CORPUS_FUND.getId());
		accountsPojo.setTypeId(bookingFormMaintenancePojo.getFinBokFrmCorpusFundId());
		accountsPojo.setPayAmount(bookingFormMaintenancePojo.getTotalAmount());
		accountsPojo.setTaxAmount(bookingFormMaintenancePojo.getTaxAmount());
		accountsPojo.setPrincipalAmount(bookingFormMaintenancePojo.getBasicAmount());
		accountsPojo.setBalanceAmount(bookingFormMaintenancePojo.getTotalAmount());
		accountsPojo.setBookingFormId(bookingFormMaintenancePojo.getBookingFormId());
		accountsPojo.setStatusId(Status.ACTIVE.getStatus());
		accountsPojo.setPaymentStatus(Status.PENDING.getStatus());
		accountsPojo.setCreatedBy(bookingFormMaintenancePojo.getCreatedBy());
		return accountsPojo;
	}

}
