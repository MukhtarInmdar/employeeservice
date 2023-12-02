package com.sumadhura.employeeservice.rest.service;

import static com.sumadhura.employeeservice.util.Util.isNotEmptyObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.BadSqlGrammarException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.itextpdf.text.DocumentException;
import com.sumadhura.employeeservice.dto.EmployeeFinancialMultipleTRNRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialResponse;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionRequest;
import com.sumadhura.employeeservice.dto.EmployeeFinancialTransactionResponse;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.FinBookingFormExcessAmountResponse;
import com.sumadhura.employeeservice.dto.FinBookingFormModiCostResponse;
import com.sumadhura.employeeservice.dto.FinTransactionEntryDetailsResponse;
import com.sumadhura.employeeservice.dto.FinancialProjectMileStoneResponse;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.FinEnum;
import com.sumadhura.employeeservice.enums.FinTransactionFor;
import com.sumadhura.employeeservice.enums.FinTransactionType;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.exception.MaxUploadSizeExceededException;
import com.sumadhura.employeeservice.exception.RefundAmountException;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.FinClearedUncleareTXPojo;
import com.sumadhura.employeeservice.persistence.dto.FinModificationInvoicePojo;
import com.sumadhura.employeeservice.persistence.dto.FinProjectAccountPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprStatPojo;
import com.sumadhura.employeeservice.persistence.dto.FinTransactionApprovalDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialMileStoneClassifidesPojo;
import com.sumadhura.employeeservice.persistence.dto.FinancialProjectMileStonePojo;
import com.sumadhura.employeeservice.persistence.dto.PercentagesPojo;
import com.sumadhura.employeeservice.persistence.dto.StatePojo;
import com.sumadhura.employeeservice.persistence.dto.StatusPojo;
import com.sumadhura.employeeservice.schedulers.PaymentDueReminderScheduler;
import com.sumadhura.employeeservice.service.EmployeeFinancialService;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerPropertyDetailsInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialMultipleTRNInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialServiceInfo;
import com.sumadhura.employeeservice.service.dto.EmployeeFinancialTransactionServiceInfo;
import com.sumadhura.employeeservice.service.dto.FinTransactionClearedInfo;
import com.sumadhura.employeeservice.service.dto.FinancialProjectMileStoneInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;
import com.sumadhura.employeeservice.service.dto.SiteDetailsInfo;
import com.sumadhura.employeeservice.service.helpers.EmployeeFinancialHelper;
import com.sumadhura.employeeservice.service.mappers.EmployeeFinancialMapper;
import com.sumadhura.employeeservice.util.AESEncryptDecrypt;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;
import com.sun.istack.NotNull;

import lombok.NonNull;

/**
 * @author ANIKET CHAVAN
 * @description this class for financial operation , for demand note to client for flat payment 
 * @since 11-01-2020
 * @time 05:40 PM
 */
 @Path("/financial") 
public class EmployeeFinancialRestService {

	    /*  ,"siteId":"134","customerId":"1148","flatBookingId":"1212","flatId":"9358"
	        ,"siteId":"134","customerId":"1186","flatBookingId":"1256","flatId":"9202"
			,"siteId":"124","customerId":"892","flatBookingId":"896","flatId":"8422"        
	        */

	 //live
	 //,"siteId":"134","customerId":"2233","flatBookingId":"2285","flatId":"8823"
	//Sub module adding query helper.txt
	private final Logger log = Logger.getLogger(EmployeeFinancialRestService.class);

	@Autowired
	@Qualifier("EmployeeFinancialService")
	private EmployeeFinancialService employeeFinancialService;

	@Autowired(required=true)
	@Qualifier("employeeFinancialHelper")
	private EmployeeFinancialHelper employeeFinancialHelper;
	
	@Autowired
	private EmployeeFinancialMapper financialMapper;
	
	@Autowired(required=true)
	private static Map<Long,Object> holdApprovingTransactions = new HashMap<>();
	
	@Autowired
	PaymentDueReminderScheduler paymentDueReminderScheduler;
	
	/*ObjectMapper mapper = new ObjectMapper();
	//Java objects to JSON string - compact-print
    String jsonString = mapper.writeValueAsString(bookingFormRequest);
    //System.out.println(jsonString);
	int PRETTY_PRINT_INDENT_FACTOR = 4;
	JSONObject xmlJSONObj = XML.toJSONObject(bookingFormRequest.toString());
	String 	responseMsg = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
	LOGGER.info("New Booking Reqeust Object \n"+responseMsg); */
	
	/**
	 * @description This service is responsible to return the milestone alias name details site wise
	 * @param employeeFinancialRequest
	 * @return Result
	 * @throws Exception 
	 * @{@link SeeTree}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMileStoneSetsDtls.spring")
	public Result getMileStoneSetsDtls(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info("***** Control inside EmployeeFinancialRestController.getMileStoneSetsDtls()  *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			EmployeeFinancialMapper financialMapper = new EmployeeFinancialMapper();

			List<EmployeeFinancialResponse> employeeFinancialResponse = employeeFinancialService.getAllMileStoneAliasNameAssociatedWithSite(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
			if (Util.isNotEmptyObject(employeeFinancialResponse)) {
				//result.setEmployeeFinancialResponses(employeeFinancialResponse);
				result.setResponseObjList(employeeFinancialResponse);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Insuffient input is given for getMileStoneSetsDetails");
				throw new InSufficeientInputException(errorMsgs);
			}
			 
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			//errorMsgs.add("The Site Id missing. Please Send Site Id to get site mile stone alias names");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/loadDemandNoteFormats.spring")
	public Result loadDemandNoteFormats(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("EmployeeFinancialRestService.loadDemandNoteFormats()");
		Result result = new Result();
		if (Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			EmployeeFinancialMapper financialMapper = new EmployeeFinancialMapper();

			List<Map<String, Object>> employeeFinancialResponse = employeeFinancialService.loadDemandNoteFormats(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
			if (Util.isNotEmptyObject(employeeFinancialResponse)) {
				result.setResponseObjList(employeeFinancialResponse);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Insuffient input is given for getMileStoneSetsDetails");
				throw new InSufficeientInputException(errorMsgs);
			}
			 
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			//errorMsgs.add("The Site Id missing. Please Send Site Id to get site mile stone alias names");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	
	/**
	 * @Description this method is responsible to return alias name related milestone details 
	 * @param employeeFinancialRequest
	 * @return Result
	 * @throws Exception 
	 * @{@link SeeTree}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMileStoneDemandNoteDetails.spring")
	 public Result getMileStoneDetails(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info(" ***** EmployeeFinancialRestService.getMileStoneDetails() ***** ");
		Result result = new Result();
		List<EmployeeFinancialResponse> financialMileStoneDemandNoteList = null;
		if (Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())&&Util.isNotEmptyObject(employeeFinancialRequest.getFinMilestoneClassifidesId())&&Util.isNotEmptyObject(employeeFinancialRequest.getMileStoneAliasName())) {
	 		 financialMileStoneDemandNoteList = employeeFinancialService.getMileStoneNameAssociatedWithMilestoneClassifidesId(new EmployeeFinancialMapper().employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
		}else if(Util.isNotEmptyObject(employeeFinancialRequest.getSiteIds()) && Util.isNotEmptyObject(employeeFinancialRequest.getCondition())
				 && (Util.isNotEmptyObject(employeeFinancialRequest.getCondition().equalsIgnoreCase("VIEW_DEMAND_NOTES"))  || employeeFinancialRequest.getCondition().equalsIgnoreCase("loadAllMileStone"))  ){
			 financialMileStoneDemandNoteList = employeeFinancialService.getMileStoneNameAssociatedWithMilestoneClassifidesId(new EmployeeFinancialMapper().employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		//result.setFinancialMileStoneDemandNoteRespList(financialMileStoneDemandNoteList);
		result.setResponseObjList(financialMileStoneDemandNoteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	/**
	 * @Description This method will return the milestone generated details 
	 * @param employeeFinancialRequest
	 * @return Result object contains the response of the milestone details associated with Flat Booking Id  
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMileStoneDetailsForTDS.spring")
	 public Result getMileStoneDetailsForTDS(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info(" ***** EmployeeFinancialRestService.getMileStoneDetails() ***** ");
		Result result = new Result();
		List<EmployeeFinancialResponse> financialMileStoneDemandNoteList = null; 
		if(Util.isNotEmptyObject(employeeFinancialRequest.getSiteId()) && Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds()) && Util.isNotEmptyObject(employeeFinancialRequest.getFlatIds())){
			financialMileStoneDemandNoteList = employeeFinancialService.getMileStoneDetailsForTDS(new EmployeeFinancialMapper().employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		//result.setFinancialMileStoneDemandNoteRespList(financialMileStoneDemandNoteList);
		result.setResponseObjList(financialMileStoneDemandNoteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	 
	/**
	 * @Description this service will update the TDS details of milestone using booking form id and booking form accounts id
	 * @param employeeFinancialRequest
	 * @return Result
	 * @throws Exception 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateMileStoneTDSDetails.spring")
	 public Result updateMileStoneTDSDetails(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info(" ***** EmployeeFinancialRestService.updateMileStoneTDSDetails() ***** ");
		Result result = new Result();
		EmployeeFinancialResponse financialMileStoneTdsDtls = null; 
		if(Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())   && Util.isNotEmptyObject(employeeFinancialRequest.getFinancialProjectMileStoneRequests())){
			financialMileStoneTdsDtls = employeeFinancialService.updateMileStoneTDSDetails(new EmployeeFinancialMapper().employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		//result.setFinancialMileStoneDemandNoteRespList(financialMileStoneDemandNoteList);
		result.setResponseObjList(financialMileStoneTdsDtls);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	/**
	 * @Description this serbice will return the all the blocks and flat details which is associated with site alias names
	 * @param employeeFinancialRequest
	 * @return Result
	 * @throws Exception 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getDemandNoteBlockSelectionDetails.spring")
	 public Result getDemandNoteBlockSelectionDetails(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//System.out.println(" ***** EmployeeFinancialRestService.getMileStoneDetails() ***** ");
		Result result = new Result();
		if (Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())&&Util.isNotEmptyObject(employeeFinancialRequest.getFinMilestoneClassifidesId())) {
			List<EmployeeFinancialResponse> list = employeeFinancialService.getDemandNoteBlockDetails(new EmployeeFinancialMapper().employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	/**
	 * @Description this service will generate the demand note for customer,<br/>
	 * this demand note contains the milestone details which customer need to pay the amount,<br/>
	 * In Demand note we will see the Demand note Date and customer name, Demand note amount,<br/>
	 * In Demand note we can have multiple milestone in single demand note with the different due dates,<br/>
	 * If the customer paid the amount after the due date of the milestone, We have to calculate interest <br/>
	 * based on the interest percentage,<br/>
	 * for the milestone we have a GST amount as well, We have to calculate the GST amount based on the government provided percentage<br/>
	 * we are maintaining this percentage in one Master Table.<br/>
	 * While paying milestone amount if customer paid more than milestone amount, then we are maintaining this amount in excess amount <br/>
	 * so next time when he will generate demand note that excess amount adjust to current generating milestone<br/>
	 * @param employeeFinancialRequest
	 * @return Result
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/generateDemandNote.spring")
	public Result generateDemandNote(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info(" ***** EmployeeFinancialRestService.generateDemandNote() ***** ");
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getSiteId()) && isNotEmptyObject(employeeFinancialRequest.getFinMilestoneClassifidesId())) {
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generateDemandNote.spring");
			EmployeeFinancialResponse resp = employeeFinancialService.generateDemandNoteService(serviceInfo);
			result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	/**
	 * Edit Milestone does't send the nofification to the customer 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editDemandNoteDetails.spring")
	public Result editDemandNoteDetails(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.editDemandNoteDetails() *****");
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getSiteId()) && isNotEmptyObject(employeeFinancialRequest.getFinMilestoneClassifidesId())) {
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generateDemandNote.spring");
			EmployeeFinancialResponse resp = employeeFinancialService.editDemandNoteDetails(serviceInfo);
			result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadDemandNoteMilestones.spring")
	public Result uploadDemandNoteMilestones(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.uploadDemandNoteMilestones() *****");
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getDemandNoteTransactionRequests()) || isNotEmptyObject(employeeFinancialRequest.getDemandNoteMSRequests())
				 || isNotEmptyObject(employeeFinancialRequest.getFinancialSchemeRequests())
				){
			if(employeeFinancialRequest.getRequestUrl()!=null && employeeFinancialRequest.getRequestUrl().equals("uploadTDS")) {
				//employeeFinancialHelper.validateUploadExcelData(employeeFinancialRequest);	
			}
			employeeFinancialRequest = financialMapper.arrangeMilestoneDetailsForUpload(employeeFinancialRequest);
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.uploadEmployeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest);
			EmployeeFinancialResponse resp = employeeFinancialService.uploadDemandNoteMilestones(serviceInfo);
			result.setResponseObjList(resp);
			// employeeFinancialService.generateDemandNotePdf(serviceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	/**
	 * this is used to upload financial transaction inovices from excel
	 * Note only generating PDF
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadFinancialTransaction.spring")
	public Result uploadFinancialTransaction(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.uploadFinancialTransaction() *****");
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getFinancialUploadDataRequests())){
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.uploadEmployeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest);
			serviceInfo.setThisUplaodedData(true);
			List<Object> fileInfoList = employeeFinancialService.uploadFinancialTransaction(serviceInfo);
			result.setResponseObjList(fileInfoList);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription("PDF files(s) generated successfully!");
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	/*
	 * Old booking to new booking we can insert the data
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insertOldBookingTransactionToNewBooking.spring")
	public Result insertOldBookingTransactionToNewBooking(BookingFormRequest newBookingRequest) throws Exception {
		
		Result result = new Result();
		if (isNotEmptyObject(newBookingRequest) && isNotEmptyObject(newBookingRequest.getOldBookingRequest())
				&& isNotEmptyObject(newBookingRequest.getNewBookingRequest())
				){
			employeeFinancialService.insertOldBookingTransactionToNewBooking(newBookingRequest);

			result.setResponseObjList(null);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;

	}

	
	/**
	 * @Description This service is responsible to create the transaction we have different transaction type<br/>
	 * <b>1) Receipt Cheque</b>  - with transaction customer will pay his milestone amount or Interest amount, or any other charges using this service
	 * 						customer will give the bank cheque and after if the cheque Got deposited in bank then customer paid amount will nullify against milestone or other charges<br/>
	 * <b>2) Receipt Online</b>  - this transaction also similar to the Receipt Cheque but here customer paid the amount through online
	 *  					with transaction customer will pay his milestone amount or Interest amount, or any other charges using this service<br/>
	 * <b>3) Payment Refund</b>  - With This transaction what ever the excess amount is exists for milestone or other charges we have return back to customer on the request
	 * 						this transaction will process through the Cheque<br/>
	 * <b>4) Payment Flat cancellation</b> -  With this transaction we will pay the whole amount back to the customer,
	 *						on the Flat Cancellation request , for this flat cancellation we have some charges like Flat Cancellation charges, Company will deduct some amount of paid amount and other charges, 
	 * 						And company will return the amount through the Cheque process as of now <br/>
	 * 
	 * Here we some some payment set off Fields <br/>
	 * <b>Principal Amount</b>  - Nothing but milestone amount<br/>
	 * <b>Modification Charges</b>  - {@link #doModificationChargesEntry(EmployeeFinancialTransactionRequest)}<br/>
	 * <b>Legal Charges</b>  - {@link #saveLegalCharges(EmployeeFinancialTransactionRequest)}<br/>
	 * <b>Interest</b> - If Customer paid the amount after the milestone due date, for that we will calculate interest and customer will pay that amount through the Interest amount payment set off<br/>
	 * {@link #generateDemandNote(EmployeeFinancialRequest)}<br/>
	 * <b>Refundable Advance</b>  - Customer will pay some advance amount for Flat<br/>
	 * @param financialTransactionRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveFinancialTransactionReceiptRequest.spring")
	public Result saveFinancialTransactionReceiptRequest(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info(" ***** control inside the EmployeeFinancialRestService.saveFinancialTransactionReceiptRequest() ***** ");
		Result result = new Result();
		
		if(Util.isNotEmptyObject(financialTransactionRequest.getOperationType())) {
			if(financialTransactionRequest.getOperationType().equals("RejectSuspenceEntry") || financialTransactionRequest.getOperationType().equals("ModifySuspenceEntry")) {
				if (Util.isNotEmptyObject(financialTransactionRequest.getAnonymousEntryId()) ) {
					EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
					EmployeeFinancialTransactionResponse resp = employeeFinancialService.modifyOrRejectSuspenseEntry(serviceInfo);
					result.setResponseObjList(resp); 
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription(HttpStatus.success.getDescription());
					return result;
				} else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("The Insufficient Input is given for requested service.");
					throw new InSufficeientInputException(errorMsgs);
				}
			}
		}
		
		//reciept cheque module
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteId()) && Util.isNotEmptyObject(financialTransactionRequest.getBookingFormId())) {
			employeeFinancialHelper.validateSaveTransactionServiceData(financialTransactionRequest);
			EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			/*if (Util.isNotEmptyObject(financialTransactionRequest.getIsShowGstInPDF())) {
				if (financialTransactionRequest.getIsShowGstInPDF().equals("true")) {
					serviceInfo.setIsShowGstInPDF(Boolean.TRUE);
				} else {
					serviceInfo.setIsShowGstInPDF(Boolean.FALSE);
				}
			} else {
				serviceInfo.setIsShowGstInPDF(Boolean.FALSE);
			}*/
			EmployeeFinancialTransactionResponse resp;
			try {
				resp = employeeFinancialService.saveFinancialTransactionRequest(serviceInfo);
			
			result.setResponseObjList(resp); 
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			} catch (Exception ex) {
				ex.printStackTrace();
				if(ex instanceof RefundAmountException) {
					log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					RefundAmountException exx = (RefundAmountException)ex;
					//result.setErrors(Arrays.asList(ex.getMessage()));
					if (Util.isNotEmptyObject(exx.getMessages())) {
						result.setErrors(exx.getMessages());
					} else {
						result.setErrors(Arrays.asList(exx.getMessage()));
					}
				} else if(ex instanceof EmployeeFinancialServiceException) {
					log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)ex;
					//result.setErrors(Arrays.asList(ex.getMessage()));
					if (Util.isNotEmptyObject(exx.getMessages())) {
						result.setErrors( exx.getMessages());
					} else {
						result.setErrors(Arrays.asList(exx.getMessage()));
					}
				}
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	/*
	 * Saveing interest wavier details in transaction tables
	 * these methiod is copy pasted of saveFinancialTransactionReceiptRequest.spring
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveInterestWaiver.spring")
	public Result saveInterestWaiver(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.saveInterestWaiver() *****");
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getSiteId()) && isNotEmptyObject(employeeFinancialRequest.getFlatIds())  && isNotEmptyObject(employeeFinancialRequest.getBookingFormId())) {
			EmployeeFinancialServiceInfo serviceInfo1 = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generateDemandNote.spring");
			try {
				financialMapper.validateInterestWaiverData(serviceInfo1);
			}catch(Exception ex) {
				log.info("Failed to saveInterestWaiver ."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());

				if(ex instanceof EmployeeFinancialServiceException) {
					result.setErrors(Arrays.asList(ex.getMessage()));
				} else {
					result.setErrors(Arrays.asList("Error occurred while approving transaction plz try again once !"));
				}
				return result;
			}//catch part
			Comparator<FinancialProjectMileStoneInfo> mileStoneSorter = new Comparator<FinancialProjectMileStoneInfo>(){
		        @Override//sorting data based on milestoneId
		        public int compare(FinancialProjectMileStoneInfo o1, FinancialProjectMileStoneInfo o2) {
		            return (int) (o1.getProjectMilestoneId() - o2.getProjectMilestoneId());
		        }
		    };
			Collections.sort(serviceInfo1.getFinancialProjectMileStoneRequests(), mileStoneSorter);
			EmployeeFinancialTransactionServiceInfo serviceInfo = employeeFinancialService.interestCalculateAndConstructTransactionObject(serviceInfo1);
			serviceInfo.setEmployeeName(employeeFinancialRequest.getEmployeeName());
			//EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generateDemandNote.spring");
			EmployeeFinancialTransactionResponse resp = employeeFinancialService.saveInterestWaiver(serviceInfo);
			result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	/**
	 * We can edit financial transaction details
	 * only we can edit Active flats transaction
	 * after edit the transaction amount, old record status id will be changed to edited transaction
	 * @param financialTransactionRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editFinancialTransaction.spring")
	public Result editFinancialTransaction(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.editFinancialTransaction() *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionTypeId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionModeId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionTypeName())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionModeName())
				) {
				EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
				if(financialTransactionRequest.getIsRecievedDateORSetOffChanged().equals("true")) {
					serviceInfo.setRecievedDateORSetOffChanged(Boolean.TRUE);
				}else {
					serviceInfo.setRecievedDateORSetOffChanged(Boolean.FALSE);
				}
				@SuppressWarnings("unused")
				EmployeeFinancialTransactionResponse resp = employeeFinancialService.editFinancialTransaction(serviceInfo);			
				//result.setResponseObjList(resp); 
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	/**
	 * we can delete financial transaction
	 * Only we can delete active booking records
	 * once transation is deleted we cannot see that transaction in completed transaction list
	 * In mobile app also the deleted amount will not show
	 * @param financialTransactionRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteFinancialTransaction.spring")
	public Result deleteFinancialTransaction(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.deleteFinancialTransaction() *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionEntryId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionSetOffEntryId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionTypeId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionModeId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionTypeName())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionModeName())
				) {
			EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			@SuppressWarnings("unused")
			EmployeeFinancialTransactionResponse resp = employeeFinancialService.deleteFinancialTransaction(serviceInfo);			
			//result.setResponseObjList(resp); 
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reAdjustTransaction.spring")
	public Result reAdjustTransaction(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.reAdjustTransaction() *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionTypeId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionModeId())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionTypeName())
				&& Util.isNotEmptyObject(financialTransactionRequest.getTransactionModeName())
				) {
				EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
				@SuppressWarnings("unused")
				EmployeeFinancialTransactionResponse resp = employeeFinancialService.reAdjustTransaction(serviceInfo);			
				//result.setResponseObjList(resp); 
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCustomerLedger.spring")
	public @JsonInclude(Include.NON_NULL) Result getCustomerLedger(@NonNull EmployeeFinancialRequest financialTransactionRequest) throws Exception {
		//log.info(" ***** control inside the EmployeeFinancialRestService.getCustomerLedger() ***** ");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteIds())) {
			EmployeeFinancialServiceInfo serviceInfo = (EmployeeFinancialServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialServiceInfo.class);
			EmployeeFinancialResponse resp = employeeFinancialService.getCustomerLedger(serviceInfo);
			result.setResponseObjList(resp); 
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCustomerLedgerDetails.spring")
	public Result getCustomerLedgerDetails(@NonNull EmployeeFinancialRequest financialTransactionRequest) throws Exception {
		//log.info(" ***** control inside the EmployeeFinancialRestService.getCustomerLedger() ***** ");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteIds())) {
			EmployeeFinancialServiceInfo serviceInfo = (EmployeeFinancialServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialServiceInfo.class);
			EmployeeFinancialResponse resp = employeeFinancialService.getCustomerLedgerDetails(serviceInfo);
			result.setResponseObjList(resp); 
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/loadRequestedData.spring")
	public Result loadRequestedData(@NonNull EmployeeFinancialRequest financialTransactionRequest) throws Exception {
		//log.info("***** Control inside the EmployeeFinancialRestService.loadRequestedData() *****");
		Result result = new Result();
		if(Util.isNotEmptyObject(financialTransactionRequest.getActionUrl())) {
			if(financialTransactionRequest.getActionUrl().equals(FinEnum.LOAD_INTEREST_DATA.getName())) {
				if(Util.isEmptyObject(financialTransactionRequest.getSiteIds())) {
					throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
				}
			}
			EmployeeFinancialServiceInfo serviceInfo = (EmployeeFinancialServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialServiceInfo.class);
			EmployeeFinancialResponse resp = employeeFinancialService.loadRequestedData(serviceInfo);
			result.setResponseObjList(resp); 
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}
	
	/**
	 * Updaing interest rates record
	 * Same dates are not allowed and percentage can give 0 also
	 * If u are updating percentage make FIN_PENALTY AND FIN_PENALTY_STATISTICS status id to inactive
	 * and FIN_penalty table PK is already store in FIN_BOOKING_FROM_ACCOUNTS table
	 * use TYPE and TYPE_ID column and make PAY_AMOUNT ZERO, don't touch to status id and Paid amount column and make PAYMENT_STATUS COLUMN to inprogress
	 * @param financialTransactionRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateInterestRates.spring")
	public Result updateGST(@NonNull EmployeeFinancialRequest financialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.updateGST() *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteIds())) {
			//EmployeeFinancialServiceInfo serviceInfo = (EmployeeFinancialServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialServiceInfo.class);
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(financialTransactionRequest,"");
			EmployeeFinancialResponse resp = employeeFinancialService.updateInterestRates(serviceInfo);
			result.setResponseObjList(resp); 
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}
	
	/**
	 * @Desctiption This service will create the anonymous entry of the online transaction<br/>
	 * after that customer will send the screen shot of the transaction and CRM and MIS Team will check the amount and reference number<br/>
	 * @param financialTransactionRequest
	 * @return Result
	 * @throws InSufficeientInputException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws MaxUploadSizeExceededException
	 * @throws RefundAmountException
	 * @throws EmployeeFinancialServiceException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/doOnlinePaymentAnonymousEntry.spring")
	public Result doOnlinePaymentAnonymousEntry(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info(" ***** control inside EmployeeFinancialRestService.doOnlinePaymentAnonymousEntry() ***** ");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteId())) {
			EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			employeeFinancialService.doOnlinePaymentAnonymousEntry(serviceInfo);
			//result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}
	
	/* We can update the suspense entry details using this service
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateOnlinePaymentAnonymousEntry.spring")
	public Result updateOnlinePaymentAnonymousEntry(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.updateOnlinePaymentAnonymousEntry() *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteId())) {
			EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			employeeFinancialService.updateOnlinePaymentAnonymousEntry(serviceInfo);
			//result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateOldPenaltyRecords.spring")
	public Result updateOldPenaltyRecords(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.updateOldPenaltyRecords() *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteIds())) {
			EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			employeeFinancialService.updatePenaltyOldGstAmountRecordsAndAdjustAmount(serviceInfo);
			//result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

	/*
	 * checking for milestone, interest calulcation is completed or not
	 * if completed in FIN_BOOKING_FORM_ACCOUNTS table IS_INTEREST_CALC_COMPLETED column making values to yes
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/validatePenaltyRecords.spring")
	public Result validatePenaltyRecords(@NonNull EmployeeFinancialRequest financialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.validatePenaltyRecords() *****");
		Result result = new Result();
		if (Util.isNotEmptyObject(financialTransactionRequest.getSiteIds())) {
			EmployeeFinancialServiceInfo serviceInfo = (EmployeeFinancialServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialServiceInfo.class);
			employeeFinancialService.validatePenaltyRecords(serviceInfo);
			//result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

	
	/**
	 * @Description This Service will create the Modification Invoice as per the request and guideline of the customer,
	 * If customer want any changes in tiles or windows or kitchen then customer has to do request to the company
	 * and after that company will check the modification changes and will raise the Invoice for customer
	 * Customer will get the email of Modification Charges Invoice, 
	 * If customer paid the amount of modification invoice so company will start the modification changes in Flat as per the Customer instruction
	 * @param financialTransactionRequest
	 * @return Result invoice details for the employee
	 * @throws InSufficeientInputException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws MaxUploadSizeExceededException
	 * @throws RefundAmountException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/doModificationChargesEntry.spring")
	public Result doModificationChargesEntry(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		log.info(" ***** control inside EmployeeFinancialRestService.doModificationChargesEntry() ***** ");
		Result result = new Result();
		if (isNotEmptyObject(financialTransactionRequest.getSiteId()) && isNotEmptyObject(financialTransactionRequest.getBookingFormId()) && isNotEmptyObject(financialTransactionRequest.getModiCostDtlsRequests())
				&& isNotEmptyObject(financialTransactionRequest.getSiteAccountId())
				) {
			EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			serviceInfo.setSiteIds(Arrays.asList(serviceInfo.getSiteId()));
			FileInfo fileInfo = employeeFinancialService.doModificationChargesEntry(serviceInfo);
			result.setResponseObjList(fileInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}
	
	/**
	 * @Description This service will return the incomplete site details means the Site Construction in going on<br/>
	 * after that only we can generate the demand note for Flat
	 * {@link #employeeFinancialRequest}
	 * @param employeeFinancialRequest
	 * @return
	 * @throws InSufficeientInputException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/incompletedEmpSitesList.spring")
	public Result getAllIncompletedEmpSitesList(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException {
		//log.info("******* The control inside of the getAllIncompletedEmpSitesList  in  EmployeeFinancialRestService ********");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && Util.isNotEmptyObject(employeeFinancialRequest.getSiteIds())) {
			employeeFinancialRequest.setActionUrl("Sites_List");
			List<DropDownPojo> siteList = employeeFinancialService.getAllIncompletedEmpSitesList(employeeFinancialRequest);
			Result result=new Result();
			result.setResponseObjList(siteList);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	} 
	
	private static Map<String,Object> holdApprovingInvoices = new HashMap<>();
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/approveModificationChargesEntry.spring")
	public Result approveModificationChargesEntry(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception{
		log.info(" ***** control inside EmployeeFinancialRestService.doModificationChargesEntry() ***** ");
		Result result = new Result();
		if (isNotEmptyObject(financialTransactionRequest.getSiteId()) && isNotEmptyObject(financialTransactionRequest.getButtonType())) {//&& isNotEmptyObject(financialTransactionRequest.getModiCostDtlsRequests())
			EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(financialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			
			String inv = financialTransactionRequest.getFinBookingFormModiCostId().toString()+financialTransactionRequest.getFinsetOffAppLevelId().toString();
			if(holdApprovingInvoices.containsKey(inv)) {
				//throw new InSufficeientInputException(Arrays.asList("Found duplicate request for transaction, failed to process transaction please try again..."));
				throw new InSufficeientInputException(Arrays.asList("Oops !!! There was a improper request found."));
			}
			holdApprovingInvoices.put(inv, financialTransactionRequest.getBookingFormId());
			try {
				if(serviceInfo.getButtonType().equalsIgnoreCase("Reject")) {
					 employeeFinancialService.rejectModificationChargesEntry(serviceInfo);
				} else {
					FileInfo fileInfo = employeeFinancialService.approveModificationChargesEntry(serviceInfo);
					result.setResponseObjList(fileInfo);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				holdApprovingInvoices.remove(inv);
				if(ex instanceof InSufficeientInputException) {
					InSufficeientInputException ex1= (InSufficeientInputException) ex;
					throw new InSufficeientInputException(ex1.getMessages());
				}
				throw new InSufficeientInputException(Arrays.asList("Error occured while saving records to the database plz try again once !"));
			}

			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Insufficient Input is given for requested service."));
		}
		return result;
	}

	
	/**
	 * @Description This service will return the all incomple block details for milestone
	 * @param employeeFinancialRequest
	 * @return
	 * @throws InSufficeientInputException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/incompleteBlocksListForMileStone.spring")
	public Result getAllIncompleteBlocksListForMileStone(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException {
		//log.info("******* The control inside of the getAllIncompleteBlocksListForMileStone  in  EmployeeFinancialRestService ********");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			employeeFinancialRequest.setActionUrl("Blocks_List");
			List<DropDownPojo> blockList = employeeFinancialService.getAllIncompletedEmpSitesList(employeeFinancialRequest);
			Result result=new Result();
			result.setResponseObjList(blockList);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	} 
	
	/**
	 * @Description This service will return all the alias names associated with Site 
	 * @param employeeFinancialRequest
	 * @return
	 * @throws InSufficeientInputException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/aliasNamesForMileStone.spring")
	public Result getAllAliasNamesForMileStone(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException {
		//log.info("******* The control inside of the getAllAliasNamesForMileStone  in  EmployeeFinancialRestService ********");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			List<FinancialMileStoneClassifidesPojo> aliasNamesList = employeeFinancialService.getAllAliasNamesForMileStone(employeeFinancialRequest);
			Result result=new Result();
			result.setResponseObjList(aliasNamesList);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	} 
	
	/**
	 * @Description This service will return the percentage value for createing milestone details for Site 
	 * @param employeeFinancialRequest
	 * @return
	 * @throws InSufficeientInputException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/mileStonePercentages.spring")
	public Result getMileStonePercentages(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException {
		//log.info("******* The control inside of the getMileStonePercentages  in  EmployeeFinancialRestService ********");
		List<PercentagesPojo> mileStonePercentagesList = employeeFinancialService.getMileStonePercentages(employeeFinancialRequest);
		Result result=new Result();
		result.setResponseObjList(mileStonePercentagesList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	/**
	 * @Description This service will generate the milestone details for Site
	 * details like milestone names and milestone percentage , milestone completion date
	 * so using this data we can generate the demand note for Customer 
	 * @param employeeFinancialRequest
	 * @return
	 * @throws InSufficeientInputException
	 * @throws InformationNotFoundException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/createMileStoneDataForDemandNote.spring")
	public Result createMileStoneDataForDemandNote(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException, InformationNotFoundException {
		log.info("******* The control inside of the createMileStoneDataForDemandNote  in  EmployeeFinancialRestService ********"+employeeFinancialRequest);
		if(Util.isNotEmptyObject(employeeFinancialRequest) && Util.isNotEmptyObject(employeeFinancialRequest.getMileStoneAliasName())
				&& Util.isNotEmptyObject(employeeFinancialRequest.getSiteId()) && Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds())
				&& Util.isNotEmptyObject(employeeFinancialRequest.getFinancialProjectMileStoneRequests())) {
			employeeFinancialService.createMileStoneDataForDemandNote(employeeFinancialRequest);
			Result result=new Result();
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	/**
	 * @Description This service will return the site names, for which site the milestone is generated
	 * for any site if we did not generated milestone details then we can not see that site details,
	 * while generating the demand note for customer 
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateMileStoneDataForDemandNote.spring")
	public Result updateMileStoneDataForDemandNote(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException, InformationNotFoundException {
		log.info("***** Control inside the EmployeeFinancialRestService.updateMileStoneDataForDemandNote() *****");
		if ( Util.isNotEmptyObject(employeeFinancialRequest.getFinMilestoneClassifidesId())  
				&& Util.isNotEmptyObject(employeeFinancialRequest.getMileStoneAliasName())
				&& Util.isNotEmptyObject(employeeFinancialRequest.getSiteId())
				//&& Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds())
				&& Util.isNotEmptyObject(employeeFinancialRequest.getFinancialProjectMileStoneRequests())
				&& Util.isNotEmptyObject(employeeFinancialRequest.getRequestUrl())
				) {
			Result result=new Result();
			try {
				employeeFinancialService.updateMileStoneDataForDemandNote(employeeFinancialRequest);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
				return result;	
			} catch (Exception e) {e.printStackTrace();
				result.setErrors(Arrays.asList("Failed to update milestone details, Please try again later.!"));
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				return result;	
			}
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	/**
	 * @Description This service will return the site names, for which site the milestone is generated
	 * for any site if we did not generated milestone details then we can not see that site details,
	 * while generating the demand note for customer 
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/raisedMilestoneSites.spring")
	public Result getRaisedMilestoneSites(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info("*** The control is inside of the getRaisedMilestoneSites in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && Util.isNotEmptyObject(employeeFinancialRequest.getSiteIds())) {
			Result result = new Result();
			List<SiteDetailsInfo> siteDetailsInfoList = employeeFinancialService.getRaisedMilestoneSites(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(siteDetailsInfoList);
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	/**
	 * @Description This service will return the active block details for generating Milestone details or Demand Note details for Customer
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/activeBlocksFlats.spring")
	public Result getActiveBlocksFlats(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info("*** The contro is inside of the getActiveBlocksFlats in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && (Util.isNotEmptyObject(employeeFinancialRequest.getSiteIds()) || 
				Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds()) || Util.isNotEmptyObject(employeeFinancialRequest.getFloorIds()))
				 || Util.isNotEmptyObject(employeeFinancialRequest.getFlatIds())
				) {
			Result result=new Result();
			List<SiteDetailsInfo> siteDetailsInfoList = employeeFinancialService.getActiveBlocksFlats(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(siteDetailsInfoList);
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	/**
	 * @Description This service will return the all generated Demand Note details 
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewDemandNotes.spring")
	public Result getDemandNotes(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info("*** The contro is inside of the getDemandNotes in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && (Util.isNotEmptyObject(employeeFinancialRequest.getSiteIds()) ||
				Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds()) || Util.isNotEmptyObject(employeeFinancialRequest.getFlatIds()) ||
				Util.isNotEmptyObject(employeeFinancialRequest.getProjectMileStoneIds()))){
			Result result=new Result();
			EmployeeFinancialResponse employeeFinancialResponse = employeeFinancialService.getDemandNotes(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(employeeFinancialResponse);
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	/**
	 * @description This service is responsible to Demand Note Preview, the employee will check how the demand note will generate
	 * if everything is OK then that demand note will send to customer
	 * here is the link for the sending the Demand Note to Customer
	 * @see
	 * #generateDemandNote(EmployeeFinancialRequest)
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/generatedDemandNotePreview.spring")
	public Result getDemandNotesPreview(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info("*** The control is inside of the generatedDemandNoteviewPreview in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && (Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds())
				|| Util.isNotEmptyObject(employeeFinancialRequest.getFlatIds()) || Util.isNotEmptyObject(employeeFinancialRequest.getBookingFormIds())) 
				&& Util.isNotEmptyObject(employeeFinancialRequest.getFinancialProjectMileStoneRequests()) && Util.isNotEmptyObject(employeeFinancialRequest.getIsInterestOrWithOutInterest())){
			// && Util.isNotEmptyObject(employeeFinancialRequest.getDemandNoteDate())
			Result result=new Result();
			String requestUrl = employeeFinancialRequest.getRequestUrl()==null?"":employeeFinancialRequest.getRequestUrl();
			if(!requestUrl.equals("ViewCustomerDataGetInterest")) {
				employeeFinancialRequest.setDemandNoteSelectionType("");
			}
			//List<EmployeeFinancialResponse> employeeFinancialResponseList = employeeFinancialService.generatedDemandNotePreview(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest),new ArrayList<Long>());
			List<Object> fileInfoList = employeeFinancialService.generateDemandNotePreview(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generatedDemandNotePreview.spring"),new HashMap<Long, Long>(),""
					,new HashMap<Long, List<FinancialProjectMileStonePojo>>(),new HashMap<Long, List<FinancialProjectMileStonePojo>>());
			if(!requestUrl.equals("ViewCustomerDataGetInterest")) {
				@SuppressWarnings("unchecked")
				List<FileInfo> fileList = (List<FileInfo>) fileInfoList.get(0);
				result.setResponseObjList(fileList);
			} else {
				@SuppressWarnings("unchecked")
				List<EmployeeFinancialResponse> employeeFinancialResponseList = (List<EmployeeFinancialResponse>) fileInfoList.get(0);
				result.setResponseObjList(employeeFinancialResponseList);
				return employeeFinancialResponseList.get(0);
			}
			
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCustomerFinancialDetails.spring")
	public Result getCustomerFinancialDetails(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info(" ***** EmployeeFinancialRestService.getCustomerFinancialDetails() ***** ");
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			String requestUrl = employeeFinancialRequest.getRequestUrl()==null?"":employeeFinancialRequest.getRequestUrl();
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generatedDemandNotePreview.spring");
			List<Object> fileInfoList = employeeFinancialService.getCustomerFinancialDetails(serviceInfo);
			if(!requestUrl.equals("ViewCustomerDataGetInterest") && !requestUrl.equals("CalculateCustomerInterestBreakUpData")) {
				@SuppressWarnings("unchecked")
				List<FileInfo> fileList = (List<FileInfo>) fileInfoList.get(0);
				result.setResponseObjList(fileList);
			} else if(requestUrl.equals("CalculateCustomerInterestBreakUpData")) {
				@SuppressWarnings("unchecked")
				List<Map<String,Object>> interestBreakUpData = (List<Map<String,Object>>) fileInfoList.get(0);
				result.setResponseObjList(interestBreakUpData);
			} else {
				@SuppressWarnings("unchecked")
				List<EmployeeFinancialResponse> employeeFinancialResponseList = (List<EmployeeFinancialResponse>) fileInfoList.get(0);
				result.setResponseObjList(employeeFinancialResponseList);
				return employeeFinancialResponseList.get(0);
			}
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCustomerInterestWaiverDetails.spring")
	public Result getCustomerInterestWaiverDetails(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info(" ***** EmployeeFinancialRestService.getCustomerInterestWaiverDetails() ***** ");
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			//String requestUrl = employeeFinancialRequest.getRequestUrl()==null?"":employeeFinancialRequest.getRequestUrl();
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generatedDemandNotePreview.spring");
			List<Object> fileInfoList = employeeFinancialService.getCustomerFinancialDetails(serviceInfo);
			result.setResponseObjList(fileInfoList);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getTempInterestCalculation.spring")
	public Result getTempInterestCalculation(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info("*** The control is inside of the generatedDemandNoteviewPreview in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) ){
			Result result=new Result();
			//List<EmployeeFinancialResponse> employeeFinancialResponseList = employeeFinancialService.generatedDemandNotePreview(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""),new ArrayList<Long>());
			List<FileInfo> fileInfoList = employeeFinancialService.getTempInterestCalculation(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""),new HashMap<Long, Long>(),"",new HashMap<Long, List<FinancialProjectMileStonePojo>>());	
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(fileInfoList);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	
	/**
	 * @Description This service will delete the zip file which is generated for Demand Note Preview
	 * @see
	 * {@link #getDemandNotesPreview(EmployeeFinancialRequest)}
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deleteDemandNoteZipFile.spring")
	public Result deleteDemandNoteZipFile(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info(" ***** EmployeeFinancialRestService.deleteDemandNoteZipFile() ***** ");
		if(Util.isNotEmptyObject(employeeFinancialRequest)){
			Result result=new Result();
			employeeFinancialService.deleteDemandNoteZipFile(financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,""));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			//result.setResponseObjList(employeeFinancialResponse);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		} 
	}
	
	/**
	 * @Description This service is used to return the demand note PDF file which is generated in Generate Demand Note time
	 * @param employeeFinancialRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/loadDemandNotePDFFile.spring")//not in use
	public Result loadDemandNotePDFFile(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info(" ***** EmployeeFinancialRestService.loadDemandNotePDFFile() ***** ");
		Result result = new Result();
		if ( isNotEmptyObject(employeeFinancialRequest.getSiteId())&& isNotEmptyObject(employeeFinancialRequest.getFinBookingFormDemandNoteId())) {
			EmployeeFinancialServiceInfo serviceInfo =new EmployeeFinancialMapper().employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"");
			EmployeeFinancialResponse resp = employeeFinancialService.loadDemandNotePDFFile(serviceInfo);
			result.setResponseObjList(resp);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	/**
	 * @Desciption This serivice will load the drop down values
	 * like Reciept, Payment, Flat Refund, Flat Cancellation
	 * this type of data this service will return
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InSufficeientInputException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewFinTransactionTypeModeData.spring")
	public Result getFinTransactionTypeModeData(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws InstantiationException, IllegalAccessException, InSufficeientInputException {
		//log.info("*** The contro is inside of the getFinTransactionTypeModeData in EmployeeFinancialRestService ***");
		Result result = new Result();
		EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
		EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getFinTransactionTypeModeData(employeeFinancialTransactionServiceInfo);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(employeeFinancialTransactionResponse);
		return result;
	}
	
	/**
	 * @Description This service will return the Site Account Number for payment through Online and Cheque payment against milestone 
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewFinProjectAccountData.spring")
	public Result getFinProjectAccountData(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		//log.info("*** The contro is inside of the getFinProjectAccountData in EmployeeFinancialRestService ***");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getSiteIds())) {
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			if (Util.isEmptyObject(employeeFinancialTransactionRequest.getCondition())) {
				employeeFinancialTransactionServiceInfo.setCondition("LoadAllAccountNo");
			}
			EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getFinProjectAccountData(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(employeeFinancialTransactionResponse);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewFinProjectAccountDataForInvoices.spring")
	public Result viewFinProjectAccountDataForInvoices(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		//log.info("*** The contro is inside of the getFinProjectAccountData in EmployeeFinancialRestService ***");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getSiteIds())
				 && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getCondition())
				) {
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.viewFinProjectAccountDataForInvoices(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(employeeFinancialTransactionResponse);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}	
	
	/**
	 * @Description This service will return the pending amount of the Flat and Interest amount  
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws InSufficeientInputException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewPendingAmount.spring")
	public Result getPendingAmountData(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws InSufficeientInputException, InstantiationException, IllegalAccessException {
		log.info("*** The contro is inside of the getPendingAmountData in EmployeeFinancialRestService *** \n"+employeeFinancialTransactionRequest.getTransactionModeName());
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && (Util.isNotEmptyObject(employeeFinancialTransactionRequest.getFlatIds()))
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionTypeId())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionModeId())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionTypeName())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionModeName())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getRequestUrl())
				){
			
			if(Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionForId()) && FinTransactionType.PAYMENT.getId().equals(employeeFinancialTransactionRequest.getTransactionTypeId())) {
				log.info("*** The control is inside of the getPendingAmountData Request From Refund in EmployeeFinancialRestService ***");
				//return getExcessAmountDetailsForRefund(employeeFinancialTransactionRequest);
				//throw new InSufficeientInputException(Arrays.asList("Failed to load the Invoice numbers"));
			}	
			
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getPendingAmountData(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(employeeFinancialTransactionResponse);
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			//log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getExcessAmountDetailsForRefund.spring")
	public Result getExcessAmountDetailsForRefund(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws InSufficeientInputException, InstantiationException, IllegalAccessException {
		log.info("***** Control inside the EmployeeFinancialRestService.getExcessAmountDetailsForRefund() *****");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getBookingFormId())) {
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			List<FinBookingFormExcessAmountResponse> finBookingFormExcessAmountResponseList = employeeFinancialService.getExcessAmountDetailsForRefund(employeeFinancialTransactionServiceInfo);
			result.setResponseObjList(finBookingFormExcessAmountResponseList);
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCustomerInvoices.spring")
	public Result getCustomerInvoices(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		log.info("*** The contro is inside of the getCustomerInvoices in EmployeeFinancialRestService ***");;
		Result result = new Result();
		FinancialProjectMileStoneResponse employeeFinancialTransactionResponse = new FinancialProjectMileStoneResponse();
		/* Getting Legal Invoice and Modification Invoice in Customer App Side based on request */
		if("Customer_Invoices".equalsIgnoreCase(employeeFinancialTransactionRequest.getActionUrl())) {
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			employeeFinancialTransactionResponse = employeeFinancialService.getCustomerInvoices(employeeFinancialTransactionServiceInfo);
		/* Getting Both Legal Invoice and Modification Invoice in Employee Portal Side */
		}else if(Util.isNotEmptyObject(employeeFinancialTransactionRequest.getSiteIds()) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getFlatIds())
		    //&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getBlockIds())
		    && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getBookingFormIds())) {
			//employeeFinancialTransactionRequest.setBlockIds(null);
			employeeFinancialTransactionRequest.setBookingFormId(employeeFinancialTransactionRequest.getBookingFormIds().get(0));
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			employeeFinancialTransactionResponse = employeeFinancialService.getCustomerInvoices(employeeFinancialTransactionServiceInfo);
		}else {
			List<String> errorMsgs = new ArrayList<>();
			//log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(employeeFinancialTransactionResponse);
		return result;
	}
	
	/**
	 * @Description This service will return the pending transaction details,
	 * and Transaction Status
	 * and Completed Transaction details
	 * @see
	 * {@link #saveFinancialTransactionReceiptRequest(EmployeeFinancialTransactionRequest)}
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewMisPendingTransactions.spring")
	public Result getMisPendingTransactions(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		//log.info("*** The contro is inside of the getMisPendingTransactions in EmployeeFinancialRestService ***");;
		Result result = new Result();
		EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
		EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getMisPendingTransactions(employeeFinancialTransactionServiceInfo);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(employeeFinancialTransactionResponse);
		return result;		
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewInterestWaiverPendingTransactions.spring")
	public Result viewInterestWaiverPendingTransactions(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws InstantiationException, IllegalAccessException {
		log.info("***** Control inside the EmployeeFinancialRestService.viewInterestWaiverPendingTransactions() *****");
		Result result = new Result();
		EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
		EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getPendingInterestWaiverTransactions(employeeFinancialTransactionServiceInfo);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(employeeFinancialTransactionResponse);
		return result;		
	}

	
	/**
	 * @Description This service will return the details of Pending Transaction
	 * @see
	 * {@link #saveFinancialTransactionReceiptRequest(EmployeeFinancialTransactionRequest)}
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws Exception 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewMisReceiptChequeOnlineData.spring")// @JsonInclude(Include.NON_NULL) 
	public Result getMisReceiptChequeOnlineData(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		//log.info("*** The control is inside of the getMisReceiptChequeOnlineData in EmployeeFinancialRestService ***"+employeeFinancialTransactionRequest.getBookingFormId() +"\t"+employeeFinancialTransactionRequest.getTransactionEntryId()+" "+employeeFinancialTransactionRequest.getTransactionModeName());
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && (Util.isNotEmptyObject(employeeFinancialTransactionRequest.getBookingFormId()))
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionEntryId()) && 
				Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionModeName())){
			Result result = new Result();

			if(employeeFinancialTransactionRequest.getRequestUrl()!=null && employeeFinancialTransactionRequest.getEmpIdFromUrl()!=null) {
				try {
					String key = "SUAMSCUSTOMERAPP";
					employeeFinancialTransactionRequest.setRequestUrl(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getRequestUrl(),AESEncryptDecrypt.convertKeyToHex(key)));
					employeeFinancialTransactionRequest.setTransactionModeName(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getTransactionModeName(),AESEncryptDecrypt.convertKeyToHex(key)));
				}catch(Exception ex) {
					ex.printStackTrace();
				}	
			}
			
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			try {
				EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getMisReceiptChequeOnlineData(employeeFinancialTransactionServiceInfo,new ArrayList<Long>());
				
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
				result.setResponseObjList(employeeFinancialTransactionResponse);
				return result;
			}catch(Exception ex) {
				ex.printStackTrace();
				if(ex.getMessage()!=null && ex.getMessage().contains("Invalid request for transaction,")) {
					if(employeeFinancialTransactionRequest.getRequestUrl()!=null && employeeFinancialTransactionRequest.getEmpIdFromUrl()!=null) {
						try {//21,445.1 
							//String key = "SUAMSCUSTOMERAPP";
							//employeeFinancialTransactionRequest.setRequestUrl(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getRequestUrl(),AESEncryptDecrypt.convertKeyToHex(key)));
							//employeeFinancialTransactionRequest.setTransactionModeName(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getTransactionModeName(),AESEncryptDecrypt.convertKeyToHex(key)));
							if("approveFromMail".equals(employeeFinancialTransactionRequest.getRequestUrl())) {
								if(true) {
									EmployeeFinancialTransactionRequest transactionRequest = (EmployeeFinancialTransactionRequest) employeeFinancialTransactionRequest.clone();
									transactionRequest.setCondition("approveTransaction");
									transactionRequest.setActionUrl("Interest Waiver");
									//EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
									Result resp = getMisPendingTransactions(transactionRequest);
									result.setResponseCode(HttpStatus.approvalFailed.getResponceCode());
									result.setDescription(HttpStatus.approvalFailed.getDescription());
									result.setErrors(Arrays.asList(HttpStatus.approvalFailed.getDescription()));
									result.setResponseObjList(resp);
									return result;
								}
							}
						}catch(Exception ex1) {
							ex1.printStackTrace();
						}
					}

				} else {
					log.info("Failed to approve transaction."+HttpStatus.failure.getDescription());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					result.setErrors(Arrays.asList("Error occurred while loading transaction plz try again once !"));
				}
			}
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewInterestWaiverData.spring")// @JsonInclude(Include.NON_NULL) 
	public Result viewInterestWaiverData(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.viewInterestWaiverData() *****");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && (Util.isNotEmptyObject(employeeFinancialTransactionRequest.getBookingFormId()))
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionEntryId()) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionModeName())){
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getMisReceiptChequeOnlineData(employeeFinancialTransactionServiceInfo,new ArrayList<Long>());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(employeeFinancialTransactionResponse);
			return result;		
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/clearStaticObjects.spring")
	public Result clearStaticObjects(EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		System.out.println("EmployeeFinancialRestService.clearStaticObjects()");
		Result result = new Result();
		//reset class level static object
		holdApprovingTransactions = new HashMap<>();
		holdApprovingInvoices = new HashMap<>();
		return result;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/checkTemplates.spring")
	public Result checkTemplates(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		System.out.println("EmployeeFinancialRestService.clearStaticObjects()");
		Result result = new Result();
		//reset class level static object
		employeeFinancialService.checkTemplates(financialTransactionRequest);
		return result;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/checkPushNotification.spring")
	public Result checkPushNotification(@NonNull EmployeeFinancialTransactionRequest financialTransactionRequest) throws Exception {
		System.out.println("EmployeeFinancialRestService.clearStaticObjects()");
		Result result = new Result();
		//reset class level static object
		employeeFinancialService.checkPushNotification(financialTransactionRequest);
		return result;
	}

	
	/**
	 * @Descriptoin This service will send the Transaction to the next Level if that transaction is approved
	 * if that transaction is rejected the Transaction will be stop in that level only
	 * we have modify option for the Transaction if that transaction is send to modify that transaction will go to Level1
	 * if this is final Level for the Transaction what ever the payment set off amount is provided that amount will
	 * get nullify against milestone amount or other charges of the Flat like Legal Charges, Corpus Fund and etc...
	 * @see
	 * {@link #saveFinancialTransactionReceiptRequest(EmployeeFinancialTransactionRequest)}
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/approveOrRejectMisReceiptOrPayment.spring")
	public Result setApproveOrRejectMisReceiptOrPayment(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		//log.info("*** The control is inside of the setApproveOrRejectMisReceiptOrPayment in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && (Util.isNotEmptyObject(employeeFinancialTransactionRequest.getBookingFormId()))
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionEntryId())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionTypeId())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionModeId())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionTypeName())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTransactionModeName())
				){
			if(Util.isNotEmptyObject(employeeFinancialTransactionRequest.getSiteBankAccountNumber()))
			{
				String siteAccountNumber=employeeFinancialTransactionRequest.getSiteBankAccountNumber();
				siteAccountNumber=siteAccountNumber.split("-")[0];
				employeeFinancialTransactionRequest.setSiteBankAccountNumber(siteAccountNumber.trim());
			}
			if(employeeFinancialTransactionRequest.getRequestUrl()!=null && employeeFinancialTransactionRequest.getEmpIdFromUrl()!=null) {
				try {
					String key = "SUAMSCUSTOMERAPP";
					employeeFinancialTransactionRequest.setRequestUrl(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getRequestUrl(),AESEncryptDecrypt.convertKeyToHex(key)));
					//employeeFinancialTransactionRequest.setTransactionModeName(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getTransactionModeName(),AESEncryptDecrypt.convertKeyToHex(key)));
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo transactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			if(Util.isNotEmptyObject(employeeFinancialTransactionRequest.getIsShowGstInPDF())) {
				if (employeeFinancialTransactionRequest.getIsShowGstInPDF().equals("true")) {
					transactionServiceInfo.setIsShowGstInPDF(Boolean.TRUE);
				} else {
					transactionServiceInfo.setIsShowGstInPDF(Boolean.FALSE);
				}
			} else {
				transactionServiceInfo.setIsShowGstInPDF(Boolean.FALSE);
			}
			if(holdApprovingTransactions.containsKey(transactionServiceInfo.getBookingFormId())) {
				throw new InSufficeientInputException(Arrays.asList("Found duplicate request for transaction, Failed to process transaction please try again..."));
			}
			
			try {
				//employeeFinancialHelper.validateDateForApproval(employeeFinancialTransactionServiceInfo);
				
				EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.setApproveOrRejectMisReceiptOrPayment(transactionServiceInfo,FinEnum.SINGLE_TRANSACTION_APPROVAL);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
				result.setResponseObjList(employeeFinancialTransactionResponse);
			}catch(Exception ex) {
				ex.printStackTrace();
				if(ex.getMessage()!=null && ex.getMessage().contains("Invalid request for transaction,")) {
					if(employeeFinancialTransactionRequest.getRequestUrl()!=null && employeeFinancialTransactionRequest.getEmpIdFromUrl()!=null) {
						try {
							//String key = "SUAMSCUSTOMERAPP";
							//employeeFinancialTransactionRequest.setRequestUrl(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getRequestUrl(),AESEncryptDecrypt.convertKeyToHex(key)));
							//employeeFinancialTransactionRequest.setTransactionModeName(AESEncryptDecrypt.decrypt(employeeFinancialTransactionRequest.getTransactionModeName(),AESEncryptDecrypt.convertKeyToHex(key)));
							if("approveFromMail".equals(employeeFinancialTransactionRequest.getRequestUrl())) {
								if(true) {
									EmployeeFinancialTransactionRequest transactionRequest = (EmployeeFinancialTransactionRequest) employeeFinancialTransactionRequest.clone();
									transactionRequest.setCondition("approveTransaction");
									transactionRequest.setActionUrl("Interest Waiver");
									//EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
									Result resp = getMisPendingTransactions(transactionRequest);
									result.setResponseCode(HttpStatus.approvalFailed.getResponceCode());
									result.setDescription(HttpStatus.approvalFailed.getDescription());
									result.setErrors(Arrays.asList(HttpStatus.approvalFailed.getDescription()));
									result.setResponseObjList(resp);
									return result;
								}
							}
						}catch(Exception ex1) {
							ex1.printStackTrace();
						}
					}else
					{
						log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
						result.setResponseCode(HttpStatus.failure.getResponceCode());
						result.setDescription(ex.getMessage());
						result.setErrors(Arrays.asList(ex.getMessage()));
					}
				} else {
					if(ex instanceof RefundAmountException) {
						log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
						result.setResponseCode(HttpStatus.failure.getResponceCode());
						result.setDescription(HttpStatus.failure.getDescription());
						RefundAmountException exx = (RefundAmountException)ex;
						//result.setErrors(Arrays.asList(ex.getMessage()));
						if (Util.isNotEmptyObject(exx.getMessages())) {
							result.setErrors(exx.getMessages());
						} else {
							result.setErrors(Arrays.asList(exx.getMessage()));
						}
					} else if(ex instanceof EmployeeFinancialServiceException) {
						log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
						result.setResponseCode(HttpStatus.failure.getResponceCode());
						result.setDescription(HttpStatus.failure.getDescription());
						EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)ex;
						//result.setErrors(Arrays.asList(ex.getMessage()));
						if (Util.isNotEmptyObject(exx.getMessages())) {
							result.setErrors( exx.getMessages());
						} else {
							result.setErrors(Arrays.asList(exx.getMessage()));
						}
					} else if (ex instanceof InSufficeientInputException) {
						InSufficeientInputException e = (InSufficeientInputException) ex;
					
						if(Util.isNotEmptyObject(e.getMessages())) {
							log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+e.getMessage());
							result.setResponseCode(HttpStatus.failure.getResponceCode());
							result.setDescription(HttpStatus.failure.getDescription());
							result.setErrors(e.getMessages());
						} else {
							log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
							result.setResponseCode(HttpStatus.failure.getResponceCode());
							result.setDescription(HttpStatus.failure.getDescription());
							result.setErrors(Arrays.asList("Error occurred while approving transaction plz try again once !"));
						}
					} else {
						log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
						result.setResponseCode(HttpStatus.failure.getResponceCode());
						result.setDescription(HttpStatus.failure.getDescription());
						result.setErrors(Arrays.asList("Error occurred while approving transaction plz try again once !"+ex.getMessage()));
					}
				}
			}			
			return result;		
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	/**
	 * @Descriptoin This multiple approval service will send the Transaction to the next Level if that transaction is approved
	 * if that transaction is rejected the Transaction will be stop in that level only
	 * we have modify option for the Transaction if that transaction is send to modify that transaction will go to Level1,
	 * if this is final Level for the Transaction what ever the payment set off amount is provided that amount will
	 * get adjusted against milestone amount or other charges of the Flat like Legal Charges, Corpus Fund and etc...
	 * @see
	 * {@link #saveFinancialTransactionReceiptRequest(EmployeeFinancialTransactionRequest)}
	 * @param EmployeeFinancialMultipleTRNRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/approveFinancialMultipleTransaction.spring")
	public Result approveMultipleTransaction(@NonNull EmployeeFinancialMultipleTRNRequest transactionRequest) throws Exception {
		log.info("***** Control inside the EmployeeFinancialRestService.approveMultipleTransaction() *****");
List<Long> listOfBookingStatusAppr = Arrays.asList(Status.ACTIVE.getStatus(),Status.SWAP.getStatus(), Status.PRICE_UPDATE.getStatus(), Status.CANCEL.getStatus(),
				Status.RETAINED.getStatus(), Status.INACTIVE.getStatus(), Status.PMAY_SCHEME_ELIGIBLE.getStatus(),Status.CANCELLED_NOT_REFUNDED.getStatus());
	EmployeeFinancialServiceInfo employeeFinancialServiceInfo = null; 
	CustomerPropertyDetailsInfo customerPropertyDetailsInfo = null;
		if(Util.isNotEmptyObject(transactionRequest) && Util.isNotEmptyObject(transactionRequest.getFinancialTRNRequests()) ){
			Result result = new Result();
			EmployeeFinancialMultipleTRNInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialMultipleTRNInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(transactionRequest, EmployeeFinancialMultipleTRNInfo.class);
			//Object employeeFinancialTransactionResponse = employeeFinancialService.approveFinancialMultipleTransaction(employeeFinancialTransactionServiceInfo);
			List<EmployeeFinancialTransactionServiceInfo> tempTransactionServiceInfoList = new ArrayList<>();
			List<EmployeeFinancialTransactionServiceInfo> permanentTransactionServiceInfoList = new ArrayList<>();
			Map<String, List<EmployeeFinancialTransactionServiceInfo>> distributeTransactionEmpWise = new HashMap<>();
			StringBuilder approveRejectTransactionStrMsg = new StringBuilder();
			List<String> approveRejectTransactionMsg = new ArrayList<>();
			List<EmployeeFinancialTransactionResponse> transactionRespList = new ArrayList<>();
			Map<String,CustomerPropertyDetailsInfo> customersList = new HashMap<>();
			List<Long> doNotProcessTransactionForBookingId = new ArrayList<>();
			Map<String,Object> response = new HashMap<>();
			List<Long> transactionEntryIds = new ArrayList<Long>();
			List<CustomerPropertyDetailsInfo> customerPropertyDetailsInfoList = null;
			boolean isApproveTransaction = false;
			boolean isRejectTransaction = false;
			boolean isModifyTransaction = false;
			String buttonType = "";
			//Long doNotProcessTransactionForBookingId = 0l;
			List<EmployeeFinancialMultipleTRNInfo> multipleTrnRequestForApproval = employeeFinancialTransactionServiceInfo.getFinancialTRNRequests();
			if(Util.isNotEmptyObject(multipleTrnRequestForApproval)) {
				employeeFinancialHelper.sortApprovalData(multipleTrnRequestForApproval);
				for (EmployeeFinancialMultipleTRNInfo multipleTRNInfo : multipleTrnRequestForApproval) {
					log.info("Processing current Transaction is : "+multipleTRNInfo.getFinTransactionNo());
					if(multipleTRNInfo.getTransactionTypeId() == null ||  multipleTRNInfo.getTransactionModeId() == null 
					 || multipleTRNInfo.getTransactionTypeName() == null || multipleTRNInfo.getTransactionModeName() == null
					 || multipleTRNInfo.getTransactionEntryId() == null 
					 || multipleTRNInfo.getFlatIds() == null || multipleTRNInfo.getBookingFormId() == null
					 || multipleTRNInfo.getSiteId() == null || multipleTRNInfo.getSiteName() == null
					  ) {
						//|| multipleTRNInfo.getTransactionSetOffEntryId() == null
						List<String> errorMsgs = new ArrayList<>();
						//log.error(HttpStatus.insufficientInput.getDescription());
						errorMsgs.add(HttpStatus.insufficientInput.getDescription());
						throw new InSufficeientInputException(errorMsgs);
					}
					if(transactionEntryIds.contains(multipleTRNInfo.getTransactionEntryId())) {
	            		approveRejectTransactionMsg.add("Transaction failed to process got duplicate request, Transaction Id : "+multipleTRNInfo.getFinTransactionNo()+"\n");
	            		continue;
	            	}
	            	transactionEntryIds.add(multipleTRNInfo.getTransactionEntryId());
					
					if (Util.isEmptyObject(multipleTRNInfo.getButtonType())) {
						approveRejectTransactionMsg.add("Transaction failed to process, Transaction Id : "+multipleTRNInfo.getFinTransactionNo()+"\n");
					} else {
						
						if (multipleTRNInfo.getButtonType().equalsIgnoreCase("Reject")) {
							buttonType = Status.REJECTED.getDescription();
							isRejectTransaction = true;
							if(multipleTRNInfo.getOptionalButtonType()!=null && 
									multipleTRNInfo.getOptionalButtonType().equals("Cheque Bounced")) {
								buttonType = Status.CHEQUE_BOUNCED.getDescription();
							}
						} else if (multipleTRNInfo.getButtonType().equalsIgnoreCase("Modify")) {
							buttonType = Status.MODIFY.getDescription();
							isModifyTransaction = true;
						} else {//pending transaction for approval,uncleared cheque
							buttonType = Status.APPROVED.getDescription();
							isApproveTransaction = true;
						}
						
						multipleTRNInfo.setPortNumber(employeeFinancialTransactionServiceInfo.getPortNumber());
						multipleTRNInfo.setEmpId(employeeFinancialTransactionServiceInfo.getEmpId());
						//employeeFinancialMultipleTRNInfo.setEmpId(transactionServiceInfo.getEmpId());
						//this object is for loading all the data of transaction using id
						EmployeeFinancialTransactionServiceInfo serviceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromInfoBeanToPojoBean(multipleTRNInfo, EmployeeFinancialTransactionServiceInfo.class);
						EmployeeFinancialTransactionResponse transactionDetails = null;
						try {//pending transction for approval
			    			if(holdApprovingTransactions.containsKey(multipleTRNInfo.getBookingFormId())) {
			    				//throw new InSufficeientInputException(Arrays.asList("Found duplicate request for transaction, Failed to process transaction please try again..."));
			    				log.info("Oops !!! There was a improper request found. Please check transaction in transaction status, Transaction id : "+multipleTRNInfo.getFinTransactionNo());
			    				throw new InSufficeientInputException(Arrays.asList("Oops !!! There was a improper request found. Please check transaction in transaction status, Transaction id : "+multipleTRNInfo.getFinTransactionNo()));
			    			}
			    			holdApprovingTransactions.put(multipleTRNInfo.getBookingFormId(), multipleTRNInfo.getBookingFormId());
			    			//loading complete transaction details, as we have only id's in request
							transactionDetails = employeeFinancialService.getMisReceiptChequeOnlineData(serviceInfo,Arrays.asList(FinEnum.APPROVE_MULTIPLE_TRANSACTION.getId()));
						} catch (Exception ex) {//pending transction for approval
							holdApprovingTransactions.remove(multipleTRNInfo.getBookingFormId());							
							getTheApproveTransactionMsg(multipleTRNInfo,approveRejectTransactionMsg,isRejectTransaction,isModifyTransaction,isApproveTransaction);
							log.info(ex.getMessage(), ex);
						}
						if (Util.isEmptyObject(transactionDetails) || Util.isEmptyObject(transactionDetails.getFinTransactionEntryDetailsResponseList())) {//pending transction for approval
							getTheApproveTransactionMsg(multipleTRNInfo,approveRejectTransactionMsg,isRejectTransaction,isModifyTransaction,isApproveTransaction);
							//throw new EmployeeFinancialServiceException("No data found for Approve Transaction, Transaction Id : "+employeeFinancialMultipleTRNInfo.getFinTransactionNo());
							holdApprovingTransactions.remove(multipleTRNInfo.getBookingFormId());
						} else {//uncleared  cheque
							
							FinTransactionEntryDetailsResponse dataForRecall = transactionDetails.getFinTransactionEntryDetailsResponseList().get(0);
							//here all data loaded of the request, for processing to next level
							EmployeeFinancialTransactionServiceInfo transactionServiceInfoForRecall = (EmployeeFinancialTransactionServiceInfo) 
									financialMapper.constructEmployeeFinancialTransactionServiceInfoBean(dataForRecall,multipleTRNInfo, EmployeeFinancialTransactionServiceInfo.class);
							
							employeeFinancialService.reConstructTransactionRequest(transactionDetails,serviceInfo,transactionServiceInfoForRecall,FinEnum.APPROVE_MULTIPLE_TRANSACTION.getName());
							
							/*ObjectMapper mapper = new ObjectMapper();
							//Java objects to JSON string - compact-print
				            String jsonString = mapper.writeValueAsString(transactionServiceInfoForRecall);
				            System.out.println(jsonString);*/
				            try {
				            	customerPropertyDetailsInfo = null;//for every iteration the object has to be null, bacoz every transaction has differant flats
				            	employeeFinancialServiceInfo = new EmployeeFinancialServiceInfo(); 
				            	employeeFinancialServiceInfo.setSiteId(multipleTRNInfo.getSiteId());
				            	employeeFinancialServiceInfo.setBookingFormId(multipleTRNInfo.getBookingFormId());
				            	
				    			if (transactionServiceInfoForRecall.getTransactionForId()!=null && transactionServiceInfoForRecall.getTransactionForId().equals(FinTransactionFor.FLAT_CANCELLATION.getId())) {
				    				//if this is the payment refund request, then loading all types of status data
				    				employeeFinancialServiceInfo.setStatusIds(listOfBookingStatusAppr);
				    				employeeFinancialServiceInfo.setCondition(FinEnum.GET_INACTIVE_BOOKINGS.getName());
				    			}
				            	
				            	if(customersList.containsKey(multipleTRNInfo.getBookingFormId().toString())) {
				            		customerPropertyDetailsInfo = customersList.get(multipleTRNInfo.getBookingFormId().toString());
				            	} else {//pending transcation for approval,uncleared  cheque
				            		customerPropertyDetailsInfoList = employeeFinancialService.getCustomerPropertyDetails(employeeFinancialServiceInfo);
				            		if(Util.isNotEmptyObject(customerPropertyDetailsInfoList)) {//pending transcation for approval
				            			customerPropertyDetailsInfo = customerPropertyDetailsInfoList.get(0);
				            			customersList.put(multipleTRNInfo.getBookingFormId().toString(), customerPropertyDetailsInfo);
				            		}
				            	}
				            	if(doNotProcessTransactionForBookingId.contains(multipleTRNInfo.getBookingFormId())) {
				            		String str = "You cannot approve receipt/payment on Flat "+customerPropertyDetailsInfo.getFlatNo()+" of Project "+customerPropertyDetailsInfo.getSiteName()+" with received date "+TimeUtil.timestampToDD_MM_YYYY(transactionServiceInfoForRecall.getTransactionReceiveDate())+", as there are previous payment(s)/receipt(s) pending on that Flat.";
									throw new EmployeeFinancialServiceException(str);
				            	}
				            	
				            	multipleTRNInfo.setCustomerPropertyDetailsInfo(customerPropertyDetailsInfo);
				            	transactionServiceInfoForRecall.setCustomerPropertyDetailsInfo(customerPropertyDetailsInfo);
				            	transactionServiceInfoForRecall.setPortNumber(employeeFinancialTransactionServiceInfo.getPortNumber());
				            	transactionServiceInfoForRecall.setEmployeeId(employeeFinancialTransactionServiceInfo.getEmpId());
				            	transactionServiceInfoForRecall.setEmployeeName(transactionRequest.getEmployeeName());
				            	//employeeFinancialHelper.validateDateForApproval(transactionServiceInfoForRecall);
								EmployeeFinancialTransactionResponse transactionResp = employeeFinancialService.setApproveOrRejectMisReceiptOrPayment(transactionServiceInfoForRecall,FinEnum.MULTIPLE_TRANSACTION_APPROVAL);//pending transcation for approval
								multipleTRNInfo.setCustomerPropertyDetailsInfo(transactionServiceInfoForRecall.getCustomerPropertyDetailsInfo());
								
								//log.info("***** Transaction Status is Final *****"+transactionServiceInfoForRecall.isThisIsFinalLevel());
								if(transactionServiceInfoForRecall.isThisIsFinalLevel()) {
									
									if(FinTransactionType.RECEIPT.getId().equals(transactionServiceInfoForRecall.getTransactionTypeId())) {
										permanentTransactionServiceInfoList.add(transactionServiceInfoForRecall);
									} else if(transactionServiceInfoForRecall.isThisPaymentTransaction() && buttonType.equalsIgnoreCase(Status.APPROVED.getDescription())) {
										//payment refund transaction sending mail through setApproveOrRejectMisReceiptOrPayment
										//if this is payment transaction and approve operation, for sending email
									} else if(FinTransactionType.INTEREST_WAIVER.getId().equals(transactionServiceInfoForRecall.getTransactionTypeId())) {
										//no send email for interest waiver from here, email format is not same for Receipt and interest waiver
									}  else {
										permanentTransactionServiceInfoList.add(transactionServiceInfoForRecall);
									}
								} else {//pending transcation for approval
									//for payment type transaction and waiver transaction, Form here not sending the email's, sending email from service class
									if(FinTransactionType.RECEIPT.getId().equals(transactionServiceInfoForRecall.getTransactionTypeId())) {//pending transcation for approval
										tempTransactionServiceInfoList.add(transactionServiceInfoForRecall);
									} else if(transactionServiceInfoForRecall.isThisPaymentTransaction() && buttonType.equalsIgnoreCase(Status.APPROVED.getDescription())) {
										//payment refund transaction sending mail through setApproveOrRejectMisReceiptOrPayment
										//if this is payment transaction and approve operation, for sending email
									} else if(FinTransactionType.INTEREST_WAIVER.getId().equals(transactionServiceInfoForRecall.getTransactionTypeId())) {
										//no send email for interest waiver from here, email format is not same for Receipt and interest waiver	
									} else {
										tempTransactionServiceInfoList.add(transactionServiceInfoForRecall);
									}
									/*if(buttonType.equalsIgnoreCase(Status.CHEQUE_BOUNCED.getDescription())) {
										tempTransactionServiceInfoList.add(transactionServiceInfoForRecall);
									}*/
								}
								holdApprovingTransactions.remove(transactionServiceInfoForRecall.getBookingFormId());
								if(isRejectTransaction) {
									approveRejectTransactionMsg.add("Transaction rejected successfully, Transaction Id : "+multipleTRNInfo.getFinTransactionNo()+"\n");
									//approveRejectTransactionMsg.add("Transaction rejected successfully. Flat No : "+multipleTRNInfo.getCustomerPropertyDetailsInfo().getFlatNo()+" Trn Amount : "+multipleTRNInfo.getTransactionAmount());
								}else if(isModifyTransaction) {
									approveRejectTransactionMsg.add("Transaction send to modify successfully, Transaction Id : "+multipleTRNInfo.getFinTransactionNo()+"\n");
									//approveRejectTransactionMsg.add("Transaction send to modify successfully. Flat No : "+multipleTRNInfo.getCustomerPropertyDetailsInfo().getFlatNo()+" Trn Amount : "+multipleTRNInfo.getTransactionAmount());
								} else {
									transactionRespList.add(transactionResp);
									approveRejectTransactionMsg.add("Transaction approved successfully, Transaction Id : "+multipleTRNInfo.getFinTransactionNo()+"\n");
									//approveRejectTransactionMsg.add("Transaction approved successfully. Flat No : "+multipleTRNInfo.getCustomerPropertyDetailsInfo().getFlatNo()+" Trn Amount : "+multipleTRNInfo.getTransactionAmount());		
								}
							} catch (Exception ex) {
								holdApprovingTransactions.remove(transactionServiceInfoForRecall.getBookingFormId());
								if(ex instanceof RefundAmountException) {
									log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
									RefundAmountException exx = (RefundAmountException)ex;
									if (Util.isNotEmptyObject(exx.getMessages())) {
										approveRejectTransactionMsg.add(exx.getMessages().get(0)+", Transaction Id : "+multipleTRNInfo.getFinTransactionNo());
									} else {
										approveRejectTransactionMsg.add(exx.getMessage()+", Transaction Id : "+multipleTRNInfo.getFinTransactionNo());
									}
								} else 	if (ex instanceof EmployeeFinancialServiceException) {
									log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
									EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)ex;
									if (Util.isNotEmptyObject(exx.getMessages())) {
										approveRejectTransactionMsg.add(exx.getMessages().get(0)+", Transaction Id : "+multipleTRNInfo.getFinTransactionNo());
									} else {
										approveRejectTransactionMsg.add(exx.getMessage()+", Transaction Id : "+multipleTRNInfo.getFinTransactionNo());	
									}
								} else if (ex instanceof InSufficeientInputException) {
									InSufficeientInputException e = (InSufficeientInputException) ex;
									if (Util.isNotEmptyObject(e.getMessages())) {
										log.info("Failed to approve transaction."+HttpStatus.failure.getDescription()+" "+e.getMessages());
										approveRejectTransactionMsg.add(e.getMessages().get(0)+", Transaction Id : "+multipleTRNInfo.getFinTransactionNo());
									} else {
										getTheApproveTransactionMsg(multipleTRNInfo,approveRejectTransactionMsg,isRejectTransaction,isModifyTransaction,isApproveTransaction);
									}
								} else if (ex instanceof BadSqlGrammarException) {
									approveRejectTransactionMsg.add("Error occured while saving records, Transaction Id : "+multipleTRNInfo.getFinTransactionNo());
								} else {
				            		holdApprovingTransactions.remove(transactionServiceInfoForRecall.getBookingFormId());
				            		getTheApproveTransactionMsg(multipleTRNInfo,approveRejectTransactionMsg,isRejectTransaction,isModifyTransaction,isApproveTransaction);
				            	}
				            	
				            	log.info(ex.getMessage(), ex);
				            }
						}
					}//buttonType
				}
			}
			for (String msg : approveRejectTransactionMsg) {//pending transction for approval
				approveRejectTransactionStrMsg.append(msg+"\n");
			}
			//System.out.println(approveRejectTransactionStrMsg);
			response.put("approvedAndFailedTRN", approveRejectTransactionMsg);
			response.put("approvedAndFailedStrMsg", approveRejectTransactionStrMsg);
			response.put("transactionRespList", transactionRespList);
			
			//for (EmployeeFinancialTransactionServiceInfo financialTransactionServiceInfo : transactionServiceInfoList) {
			if (Util.isNotEmptyObject(tempTransactionServiceInfoList)) {
				for (EmployeeFinancialTransactionServiceInfo financialTransactionServiceInfo : tempTransactionServiceInfoList) {
					//List<EmployeeFinancialTransactionServiceInfo> transactionServiceInfoList = new ArrayList<>();
					if (buttonType.equalsIgnoreCase(Status.APPROVED.getDescription())) {
						List<FinTransactionApprovalDetailsPojo> listOfNextApprovalEmaEmails = financialTransactionServiceInfo.getListOfNextApprovalEmaEmails();
						if (Util.isNotEmptyObject(listOfNextApprovalEmaEmails)) {
							for (FinTransactionApprovalDetailsPojo approvalDetailsPojo : listOfNextApprovalEmaEmails) {
								if (distributeTransactionEmpWise.containsKey(approvalDetailsPojo.getEmpEmail())) {
									List<EmployeeFinancialTransactionServiceInfo> transactionServiceInfoList = distributeTransactionEmpWise.get(approvalDetailsPojo.getEmpEmail());
									transactionServiceInfoList.add(financialTransactionServiceInfo);
								} else {
									List<EmployeeFinancialTransactionServiceInfo> transactionServiceInfoList = new ArrayList<>();
									transactionServiceInfoList.add(financialTransactionServiceInfo);
									distributeTransactionEmpWise.put(approvalDetailsPojo.getEmpEmail(), transactionServiceInfoList);
								}
							}
						}
					} else {// if rejected or cheque bounced
						List<FinTransactionApprStatPojo> finTransactionApprStatPojoList = financialTransactionServiceInfo.getFinTransactionApprStatPojoList();
						if (Util.isNotEmptyObject(finTransactionApprStatPojoList)) {
							for (FinTransactionApprStatPojo apprStatPojo : finTransactionApprStatPojoList) {
								if (distributeTransactionEmpWise.containsKey(apprStatPojo.getEmpEmail())) {
									List<EmployeeFinancialTransactionServiceInfo> transactionServiceInfoList = distributeTransactionEmpWise.get(apprStatPojo.getEmpEmail());
									transactionServiceInfoList.add(financialTransactionServiceInfo);
								} else {
									List<EmployeeFinancialTransactionServiceInfo> transactionServiceInfoList = new ArrayList<>();
									transactionServiceInfoList.add(financialTransactionServiceInfo);
									distributeTransactionEmpWise.put(apprStatPojo.getEmpEmail(), transactionServiceInfoList);
								}
							}
						}
						// employeeFinancialHelper.sendFinancialApprovalEmail(tempTransactionServiceInfoList,buttonType);
					}
				}
				for (Entry<String, List<EmployeeFinancialTransactionServiceInfo>> employeeFinancialMultipleTRNInfo : distributeTransactionEmpWise.entrySet()) {
					log.info("Send approval email to : " + employeeFinancialMultipleTRNInfo.getKey());
					employeeFinancialHelper.sendFinancialApprovalEmail(employeeFinancialMultipleTRNInfo.getValue(), buttonType,Arrays.asList(employeeFinancialMultipleTRNInfo.getKey()));
				}
			} // temp transactions
				/*if(Util.isNotEmptyObject(permanentTransactionServiceInfoList)) {
					employeeFinancialHelper.sendFinancialApprovalEmail(permanentTransactionServiceInfoList,Status.TRANSACTION_COMPLETED.getDescription());
				}*/
			//}
			
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(response);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription());
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	@POST
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/mailService.spring")
	public Result approveTransactionFromMail(EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception {
		System.out.println("***** Control inside the EmployeeFinancialRestService.approveTransactionFromMail() *****");
		
		return null;
	}

	private void getTheApproveTransactionMsg(EmployeeFinancialMultipleTRNInfo employeeFinancialMultipleTRNInfo, List<String> approveRejectTransactionMsg, boolean isRejectTransaction,
			boolean isModifyTransaction, boolean isApproveTransaction) {
		if(isRejectTransaction) {
			approveRejectTransactionMsg.add("Failed to Reject Transaction, Transaction Id : "+employeeFinancialMultipleTRNInfo.getFinTransactionNo()+"\n");
			//approveRejectTransactionMsg.add("Failed to Reject Transaction. Flat No : "+employeeFinancialMultipleTRNInfo.getCustomerPropertyDetailsInfo().getFlatNo()+" Trn Amount : "+employeeFinancialMultipleTRNInfo.getTransactionAmount());
		}else if(isModifyTransaction) {
			approveRejectTransactionMsg.add("Failed to Modify Transaction, Transaction Id : "+employeeFinancialMultipleTRNInfo.getFinTransactionNo()+"\n");
			//approveRejectTransactionMsg.add("Failed to Modify Transaction. Flat No : "+employeeFinancialMultipleTRNInfo.getCustomerPropertyDetailsInfo().getFlatNo()+" Trn Amount : "+employeeFinancialMultipleTRNInfo.getTransactionAmount());
		} else {
			approveRejectTransactionMsg.add("Failed to Approve Transaction, Transaction Id : "+employeeFinancialMultipleTRNInfo.getFinTransactionNo()+"\n");
			//approveRejectTransactionMsg.add("Failed to Approve Transaction. Flat No : "+employeeFinancialMultipleTRNInfo.getCustomerPropertyDetailsInfo().getFlatNo()+" Trn Amount : "+employeeFinancialMultipleTRNInfo.getTransactionAmount());	
		}
	}
	
	/**
	 * @Description This service will return all the data which is the anonymous transaction
	 * @see
	 * {@link #doOnlinePaymentAnonymousEntry(EmployeeFinancialTransactionRequest)}//this service will create the anonymous entry 
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InSufficeientInputException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewAnonymousEntriesData.spring")
	public Result getAnonymousEntriesData(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws InstantiationException, IllegalAccessException, InSufficeientInputException {
		//log.info("***** Control inside the EmployeeFinancialRestService.getAnonymousEntriesData() *****");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest.getSiteId())) {
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getAnonymousEntriesData(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(employeeFinancialTransactionResponse);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() );
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
    @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/viewAnonymousEntriesDataReport.spring")
	public Result getSuspenseEntryReport(@NonNull EmployeeFinancialTransactionRequest empTransReq) throws Exception {
		log.info("*** The contro is inside of the viewAnonymousEntriesDataReport in EmployeeFinancialRestService ***");
		Result result = new Result();
		
		EmployeeFinancialTransactionServiceInfo empFinTranSerInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(empTransReq, EmployeeFinancialTransactionServiceInfo.class);
		
		List<Map<String, Object>> list = employeeFinancialService.getSuspenseEntryReport(empFinTranSerInfo);
		
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(list);
		return result;
	}
	
	/**
	 * @Description This service will return all the tax percetages for GST amount calculation
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InSufficeientInputException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getTaxPercentage.spring")
	public Result getTaxPercentageData(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws InstantiationException, IllegalAccessException, InSufficeientInputException {
		//log.info("*** The contro is inside of the getTaxPercentageData in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && (Util.isNotEmptyObject(employeeFinancialTransactionRequest.getTypeName()))){
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			EmployeeFinancialTransactionResponse employeeFinancialTransactionResponse = employeeFinancialService.getTaxPercentageData(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(employeeFinancialTransactionResponse);
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	/**
	 * @Description This service will create the Legal Changes entry
	 * customer will get the invoice to there registered email, and this invoice 
	 * will be get attached to the email,
	 * And customer need to pay this amount using Receipt Cheque or Online
	 * @see
	 * #saveFinancialTransactionReceiptRequest(EmployeeFinancialTransactionRequest)
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InSufficeientInputException
	 * @throws IOException
	 * @throws DocumentException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveLegalCharges.spring")
	public Result saveLegalCharges(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception{
		log.info("*** The contro is inside of the saveLegalCharges in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getBookingFormIds())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getSiteIds()) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getFinBookingFormLegalCostList())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getFinBookingFormLegalCostList().get(0).getFinBookingFormLglCostDtlsList())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getFinTaxMappingId()) && Util.isNotEmptyObject(employeeFinancialTransactionRequest.getPercentageValue())
				&& Util.isNotEmptyObject(employeeFinancialTransactionRequest.getEmpId())){
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			 List<FileInfo> fileInfoList = employeeFinancialService.saveLegalCharges(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(fileInfoList);
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveMaintenance_Charges.spring")
	public Result saveMaintenance_Charges(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception{
		log.info("*** The contro is inside of the saveMaintenance_Charges in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest)){
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			List<Object> fileInfoList = employeeFinancialService.saveMaintenance_Charges(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(fileInfoList);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	/**
	 * This method will update only maintenance charges amount
	 * @param employeeFinancialTransactionRequest
	 * @return
	 * @throws Exception
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateMaintenance_ChargesPayAmount.spring")
	public Result updateMaintenance_Charges(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception{
		log.info("*** The contro is inside of the saveMaintenance_Charges in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest)){
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			List<Object> fileInfoList = employeeFinancialService.updateMaintenance_ChargesPayAmount(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(fileInfoList);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveFlatKhataBifurcationAndOtherCharges.spring")
	public Result saveFlatKhataBifurcationAndOtherCharges(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception{
		log.info("*** The contro is inside of the saveFlat_Khata_Bifurcation_And_Other_Charges in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest)){
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			List<Object> fileInfoList = employeeFinancialService.saveFlatKhataBifurcationAndOtherCharges(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(fileInfoList);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/saveCorpusFund.spring")
	public Result saveCorpusFund(@NonNull EmployeeFinancialTransactionRequest employeeFinancialTransactionRequest) throws Exception{
		log.info("*** The contro is inside of the saveFlat_Khata_Bifurcation_And_Other_Charges in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialTransactionRequest)){
			Result result = new Result();
			EmployeeFinancialTransactionServiceInfo employeeFinancialTransactionServiceInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialTransactionRequest, EmployeeFinancialTransactionServiceInfo.class);
			List<Object> fileInfoList = employeeFinancialService.saveCorpusFund(employeeFinancialTransactionServiceInfo);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(fileInfoList);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialTransactionRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getCustomerDetailsAndPendingAmounts.spring")
	public Result getCustomerDetailsAndPendingAmounts(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		//log.info("*** The control is inside of the getCustomerDetailsAndPendingAmounts in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) && Util.isNotEmptyObject(employeeFinancialRequest.getProjectMileStoneIds())
		    && Util.isNotEmptyObject(employeeFinancialRequest.getBlockIds())) {
			Result result = new Result();
			EmployeeFinancialServiceInfo employeeFinancialServiceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"");
			List<FlatBookingInfo> flatBookingInfoList = employeeFinancialService.getCustomerDetailsAndPendingAmounts(employeeFinancialServiceInfo);
			result.setResponseObjList(flatBookingInfoList);
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getNonRefundFlats.spring")
	public Result getNonRefundFlats(@NotNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException, InstantiationException, IllegalAccessException {
		//log.info("*** The control is inside of the getExcessAmountDetailsForRefund in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) &&  Util.isNotEmptyObject(employeeFinancialRequest.getRequestUrl())  &&  Util.isNotEmptyObject(employeeFinancialRequest.getCondition()) ) {
			Result result = new Result();
			List<Map<String, Object>> list = employeeFinancialService.getNonRefundFlats(employeeFinancialRequest);
			if(employeeFinancialRequest.getCondition()!=null && employeeFinancialRequest.getCondition().equals("LoadNonRefundAlertMsg")) {
				//here return the alert msg of non refund flat amount
				List<Map<String, Object>> refundEntryAlertMsgs = financialMapper.constructNonRefundAmountAlert(list);
				result.setResponseObjList(refundEntryAlertMsgs);
			} else if(employeeFinancialRequest.getCondition()!=null && employeeFinancialRequest.getCondition().equals("LoadNonRefundFlats")) {
				//her return the Non refund flat details
				//List<String> refundEntryAlertMsgs = financialMapper.constructNonRefundAmountAlert(list);
				result.setResponseObjList(list);
			} else  if(employeeFinancialRequest.getCondition()!=null && employeeFinancialRequest.getCondition().equals("FlatsWithCustName")) {
				//here return the non refund flat with customer name
				List<Map<String, Object>> customerNameWithFlatNo = financialMapper.constructNonRefundCustomerNameWithFlatNo(list);
				result.setResponseObjList(customerNameWithFlatNo);
			}
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getNonRefundFlatfdss.spring")
	public Result getNonRefundFl1ats(@NotNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException, InstantiationException, IllegalAccessException {
		//log.info("*** The control is inside of the getExcessAmountDetailsForRefund in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest) ) {
			Result result = new Result();
			List<Map<String, Object>> list = employeeFinancialService.getNonRefundFlats(employeeFinancialRequest);
			List<Map<String, Object>> refundEntryAlertMsgs = financialMapper.constructNonRefundAmountAlert(list);
			result.setResponseObjList(refundEntryAlertMsgs);
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/generateConsolidatedReceipt.spring")
	public Result generateConsolidatedReceipt(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("*** The control is inside of the generateConsolidatedReceipt in EmployeeFinancialRestService ***");
		Result result = new Result();
		if(isNotEmptyObject(employeeFinancialRequest.getBookingFormId())) {
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,null);
			try {
				EmployeeFinancialResponse resp = employeeFinancialService.generateConsolidatedReceipt(serviceInfo);
				result.setResponseObjList(resp);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			}catch(Exception ex) {
				ex.printStackTrace();
				if(ex.getMessage()!=null && ex.getMessage().contains("No transaction found")) {
					result.setDescription(ex.getMessage());
					result.setResponseCode(HttpStatus.exceptionRaisedInFlow.getResponceCode());
					result.setErrors(Arrays.asList(ex.getMessage()));
				} else {
					result.setDescription("Failed to genereate statement!");
					result.setResponseCode(HttpStatus.exceptionRaisedInFlow.getResponceCode());
					result.setErrors(Arrays.asList("Failed to genereate statement!"));
				}
			}			
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/generateClosingBalanceReport.spring")
	public Result generateClosingBalance(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
		log.info("*** The control is inside of the generateConsolidatedReceipt in EmployeeFinancialRestService ***");
		Result result = new Result();
		if( isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.copyPropertiesFromRequestBeanToInfoBean(employeeFinancialRequest,EmployeeFinancialServiceInfo.class);
			try {
				Map<String, List<Map<String, Object>>> resp = employeeFinancialService.generateClosingBalance(serviceInfo);
				result.setResponseObjList(resp);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			}catch(Exception ex) {
				ex.printStackTrace();
				if(ex.getMessage()!=null && ex.getMessage().contains("No transaction found")) {
					result.setDescription(ex.getMessage());
					result.setResponseCode(HttpStatus.exceptionRaisedInFlow.getResponceCode());
					result.setErrors(Arrays.asList(ex.getMessage()));
				} else {
					result.setDescription("Failed to genereate balance report!");
					result.setResponseCode(HttpStatus.exceptionRaisedInFlow.getResponceCode());
					result.setErrors(Arrays.asList("Failed to genereate balance report!"));
				}
			}			
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deleteUploadedAttachments.spring")
	public Result deleteUploadedAttachments(@NotNull EmployeeFinancialRequest employeeFinancialRequest) throws InSufficeientInputException, InstantiationException, IllegalAccessException {
		//log.info("*** The control is inside of the getExcessAmountDetailsForRefund in EmployeeFinancialRestService ***");
		if(Util.isNotEmptyObject(employeeFinancialRequest.getFileInfos())) {
			Result result = new Result();
			employeeFinancialService.deleteUploadedAttachments(employeeFinancialRequest);
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getPendingModificationInvoices.spring")
	public Result getPendingModificationInvoices( EmployeeFinancialRequest employeeFinancialRequest)throws InSufficeientInputException, InstantiationException, IllegalAccessException, Exception
	{
		if(Util.isNotEmptyObject(employeeFinancialRequest) ) {
			Result result = new Result();
			try {
				List<FinModificationInvoicePojo> list= employeeFinancialService.getPendingModificationInvoices(employeeFinancialRequest);
				result.setResponseObjList(list);	
			}catch(Exception ex){
				if(ex.getMessage()!=null) {
					if(ex.getMessage().equals("approvalLevelIdFoundNull")) {
						throw new EmployeeFinancialServiceException("Approval mapping not found, Please contact to support team..!");
					}
				}
				throw new EmployeeFinancialServiceException("Error occurred while loading invoice data..!");
			}
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}
	}
	
    @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sendFinancialInterestUpdateEmail.spring")
	public Result sendFinancialInterestUpdateEmail(EmployeeFinancialRequest employeeFinancialRequest) throws Exception, InstantiationException, IllegalAccessException {
		//log.info("*** The control is inside of the getExcessAmountDetailsForRefund in EmployeeFinancialRestService ***");
		//if(Util.isEmptyObject(employeeFinancialRequest.getSiteId())) {
			Result result = new Result();
			employeeFinancialService.sendFinancialInterestUpdateEmail(employeeFinancialRequest);
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseCode(HttpStatus.success.getResponceCode());
			return result;
		/*} else {
			List<String> errorMsgs = new ArrayList<>();
			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
			throw new InSufficeientInputException(errorMsgs);
		}*/
	}
    
    @POST
  	@Produces(MediaType.APPLICATION_JSON)
  	@Consumes(MediaType.APPLICATION_JSON)
  	@Path("/getModificationInvoiceDetails.spring")
  	public Result getModificationInvoiceDetails( EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
  		if(Util.isNotEmptyObject(employeeFinancialRequest)&& Util.isNotEmptyObject(employeeFinancialRequest.getFinBookingFormModiCostId()) ) {
  			Result result = new Result();
  			List<FinBookingFormModiCostResponse> resp = employeeFinancialService.getModificationInvoiceDetails(employeeFinancialRequest);
  			result.setResponseObjList(resp);
  			result.setDescription(HttpStatus.success.getDescription());
  			result.setResponseCode(HttpStatus.success.getResponceCode());
  			return result;
  		} else {
  			List<String> errorMsgs = new ArrayList<>();
  			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
  			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
  			throw new InSufficeientInputException(errorMsgs);
  		}
  	}
	
	@Consumes(MediaType.APPLICATION_JSON)
  	@Path("/downloadGeneratedDemandNote.spring")
  	public Result downloadGeneratedDemandNote( EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
  		if(Util.isNotEmptyObject(employeeFinancialRequest)&& Util.isNotEmptyObject(employeeFinancialRequest.getFinBookingFormModiCostId()) ) {
  			Result result = new Result();
  			//List<Object> resp = employeeFinancialService.downloadGeneratedDemandNote(employeeFinancialRequest);
  			//result.setResponseObjList(resp);
  			result.setDescription(HttpStatus.success.getDescription());
  			result.setResponseCode(HttpStatus.success.getResponceCode());
  			return result;
  		}else {
  			List<String> errorMsgs = new ArrayList<>();
  			log.error(HttpStatus.insufficientInput.getDescription() +employeeFinancialRequest);
  			errorMsgs.add(HttpStatus.insufficientInput.getDescription());
  			throw new InSufficeientInputException(errorMsgs);
  		}
  	}
    
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getInterestWaiverReportDetails.spring")
	public Result getInterestWaiverReportDetails(@NonNull EmployeeFinancialRequest employeeFinancialRequest) throws Exception {
    	log.info("*** The Control is inside the getInterestWaiverReportDetails in EmployeeFinancialRestService ***"+employeeFinancialRequest);
		Result result = new Result();
		if (isNotEmptyObject(employeeFinancialRequest.getSiteId())) {
			String requestUrl = employeeFinancialRequest.getRequestUrl()==null?"":employeeFinancialRequest.getRequestUrl();
			EmployeeFinancialServiceInfo serviceInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(employeeFinancialRequest,"generatedDemandNotePreview.spring");
			List<?> fileInfoList = employeeFinancialService.getCustomerFinancialDetails(serviceInfo);
			if(requestUrl.equals("GiveInterestWaiverReport")) {
				List<FileInfo> fileList = new ArrayList<>();
				if(Util.isNotEmptyObject(fileInfoList)) {
					List<?> responseList = (List<?>) fileInfoList.get(0);
					for(Object responseObject : responseList) {
						if(Util.isNotEmptyObject(responseObject) && responseObject instanceof FileInfo) {
							fileList.add((FileInfo)responseObject);
						}
					}
				}
				EmployeeFinancialResponse empFinResponse = new EmployeeFinancialResponse();
				empFinResponse.setDemandNoteGeneratorInfoList(serviceInfo.getDemandNoteGeneratorInfoList());
				empFinResponse.setFileInfoList(fileList);
				result.setResponseObjList(empFinResponse);
			}
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
    
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getInterestWaivedAndPaidDetails.spring")
	public Result getInterestWaivedAndPaidDetails(@NonNull EmployeeFinancialRequest empFinRequest) throws Exception {
    	log.info("*** The Control is inside the getInterestWaivedAndPaidDetails in EmployeeFinancialRestService ***"+empFinRequest);
		if(Util.isNotEmptyObject(empFinRequest.getSiteId()) || Util.isNotEmptyObject(empFinRequest.getFlatId())
			|| Util.isNotEmptyObject(empFinRequest.getStartDate()) || Util.isNotEmptyObject(empFinRequest.getEndDate())) {
			Result result = new Result();
			EmployeeFinancialServiceInfo empFinSerInfo = financialMapper.employeeFinancialRequestToemployeeFinancialInfo(empFinRequest,"generatedDemandNotePreview.spring");
			EmployeeFinancialTransactionResponse empFinTransResponse = employeeFinancialService.getInterestWaivedAndPaidDetails(empFinSerInfo);
			result.setResponseObjList(empFinTransResponse);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			return result;
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
	}
    
    @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getAllCustomersInvoices.spring")
	public Result getAllCustomersInvoices(@NonNull EmployeeFinancialTransactionRequest empTransReq) throws Exception {
		log.info("*** The contro is inside of the getAllCustomersInvoices in EmployeeFinancialRestService ***");;
		Result result = new Result();
		FinancialProjectMileStoneResponse finProjMilestoneResp = new FinancialProjectMileStoneResponse();
		EmployeeFinancialTransactionServiceInfo empFinTranSerInfo = (EmployeeFinancialTransactionServiceInfo) financialMapper.copyPropertiesFromRequestBeanToInfoBean(empTransReq, EmployeeFinancialTransactionServiceInfo.class);
		finProjMilestoneResp = employeeFinancialService.getAllCustomersInvoices(empFinTranSerInfo);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(finProjMilestoneResp);
		return result;
	}

    @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/loadTransactionStatusData.spring")
	public Result loadPendingLevelEmployeeList(@NonNull EmployeeFinancialTransactionRequest empTransReq) throws Exception {
		log.info("*** The control is inside of the loadTransactionStatusData() in EmployeeFinancialRestService ***");;
		Result result = new Result();
		Map<String, Object> response = employeeFinancialService.loadTransactionStatusModuleData(empTransReq);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		result.setResponseObjList(response);
		return result;
	}

    @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getClearedTransactionReport.spring")
	public Result getClearedTransactionReport(@NonNull EmployeeFinancialTransactionRequest empTransReq) throws Exception {
		log.info("*** The control is inside of the getClearedTransactionReport() in EmployeeFinancialRestService ***");;
		Result result = new Result();
		//Util.isNotEmptyObject(empTransReq.getSiteIds()) ||
		//if(Util.isNotEmptyObject(empTransReq.getClearanceFromDate()) || Util.isNotEmptyObject(empTransReq.getClearanceToDate())) {
		Map<String,List<FinTransactionClearedInfo>> response = employeeFinancialService.getClearedTransactionReport(empTransReq);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(response);
		/*} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}*/
		return result;
	}


 @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getPendingTransactionReport.spring")
	public Result getPendingransactionReport(@NonNull EmployeeFinancialTransactionRequest empTransReq) throws Exception {
		log.info("*** The control is inside of the getPendingTransactionReport() in EmployeeFinancialRestService ***");;
		Result result = new Result();
		//Util.isNotEmptyObject(empTransReq.getSiteIds()) ||
		//if(Util.isNotEmptyObject(empTransReq.getClearanceFromDate()) || Util.isNotEmptyObject(empTransReq.getClearanceToDate())) {
		Map<String,List<FinTransactionClearedInfo>> response = employeeFinancialService.getPendingTransactionReport(empTransReq);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(response);
		/*} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}*/
		return result;
	}
    @POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getSuspnesEntryTransactionReport.spring")
	public Result getSuspnesEntryTransactionReport(@NonNull EmployeeFinancialTransactionRequest empTransReq) throws Exception {
		log.info("*** The control is inside of the getSuspnesEntryTransactionReport() in EmployeeFinancialRestService ***");;
		Result result = new Result();
		//Util.isNotEmptyObject(empTransReq.getSiteIds()) ||
		//if(Util.isNotEmptyObject(empTransReq.getClearanceFromDate()) || Util.isNotEmptyObject(empTransReq.getClearanceToDate())) {
			List<FinTransactionClearedInfo> response = employeeFinancialService.getSuspnesEntryTransactionReport(empTransReq);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			result.setResponseObjList(response);
		/*} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}*/
		return result;
	}
    
    
    @POST
  	@Produces(MediaType.APPLICATION_JSON)
  	@Consumes(MediaType.APPLICATION_JSON)
  	@Path("/getCleredUnclearedTransactionReport.spring")
  	public Result getClearedUnclearedTransactionReport(@NonNull EmployeeFinancialTransactionRequest empTransReq)  {
  		log.info("*** The control is inside of the getClearedUnclearedTransactionReport() in EmployeeFinancialRestService ***"+empTransReq);;
  		Result result = new Result();
  		Map<String, List<FinClearedUncleareTXPojo>> response;
		try {
			employeeFinancialService.dateWiseSearchFunctionalityUtil(empTransReq);
			response = employeeFinancialService.getClearedUnclearedTxReport(empTransReq);
			result.setResponseCode(HttpStatus.success.getResponceCode());
  			result.setDescription(HttpStatus.success.getDescription());
  			result.setResponseObjList(response);
		} catch (Exception e) {
			e.printStackTrace();
			result.setInternalExcetion(ExceptionUtils.getStackTrace(e));
		}
  		return result;
  	}
    
    @POST
  	@Produces(MediaType.APPLICATION_JSON)
  	@Consumes(MediaType.APPLICATION_JSON)
  	@Path("/getAccountNumbers.spring")
  	public Result getAccountNumbers(@NonNull EmployeeFinancialTransactionRequest empTransReq)  {
  		log.info("*** The control is inside of the getAccountNumbers() in EmployeeFinancialRestService ***"+empTransReq);;
  		Result result = new Result();
  		List<FinProjectAccountPojo> response;
		try {
			response = employeeFinancialService.getAccountNumbers(empTransReq);
			result.setResponseCode(HttpStatus.success.getResponceCode());
  			result.setDescription(HttpStatus.success.getDescription());
  			result.setResponseObjList(response);
		} catch (Exception e) {
			result.setResponseCode(HttpStatus.failure.getResponceCode());
  			result.setDescription(HttpStatus.failure.getDescription());
			e.printStackTrace();
			result.setInternalExcetion(ExceptionUtils.getStackTrace(e));
		}
  		return result;
  	}
    
    @POST
   	@Produces(MediaType.APPLICATION_JSON)
   	@Consumes(MediaType.APPLICATION_JSON)
   	@Path("/getBookingStatuses.spring")
   	public Result getBookingStatuses(@NonNull EmployeeFinancialTransactionRequest empTransReq)  {
   		log.info("*** The control is inside of the getBookingStatuses() in EmployeeFinancialRestService ***"+empTransReq);;
   		Result result = new Result();
   		List<StatusPojo> response;
 		try {
 			response = employeeFinancialService.getBookingStatuses(empTransReq);
 			result.setResponseCode(HttpStatus.success.getResponceCode());
   			result.setDescription(HttpStatus.success.getDescription());
   			result.setResponseObjList(response);
 		} catch (Exception e) {
 			result.setResponseCode(HttpStatus.failure.getResponceCode());
   			result.setDescription(HttpStatus.failure.getDescription());
 			e.printStackTrace();
 			result.setInternalExcetion(ExceptionUtils.getStackTrace(e));
 		}
   		return result;
   	}
    
    @POST
   	@Produces(MediaType.APPLICATION_JSON)
   	@Consumes(MediaType.APPLICATION_JSON)
   	@Path("/getStates.spring")
   	public Result getStates(@NonNull EmployeeFinancialTransactionRequest empTransReq)  {
   		log.info("*** The control is inside of the getStates() in EmployeeFinancialRestService ***"+empTransReq);;
   		Result result = new Result();
   		List<StatePojo> response;
 		try {
 			response = employeeFinancialService.getStates(empTransReq);
 			result.setResponseCode(HttpStatus.success.getResponceCode());
   			result.setDescription(HttpStatus.success.getDescription());
   			result.setResponseObjList(response);
 		} catch (Exception e) {
 			result.setResponseCode(HttpStatus.failure.getResponceCode());
   			result.setDescription(HttpStatus.failure.getDescription());
 			e.printStackTrace();
 			result.setInternalExcetion(ExceptionUtils.getStackTrace(e));
 		}
   		return result;
   	}
    
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateOldBookingPendingTransactionToNewBooking.spring")
	public Result updateOldBookingPendingTransactionToNewBooking(BookingFormRequest newBookingRequest) throws Exception {
		
		Result result = new Result();
		if (isNotEmptyObject(newBookingRequest) && isNotEmptyObject(newBookingRequest.getOldBookingRequest())
				&& isNotEmptyObject(newBookingRequest.getNewBookingRequest())
				){
			employeeFinancialService.updateOldBookingPendingTransactionToNewBooking(newBookingRequest);

			result.setResponseObjList(null);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;

	}
    
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/paymentDueReminderScheduler.spring")
	public Result paymentDueReminderScheduler(CustomerInfo customerInfo) throws Exception {

		paymentDueReminderScheduler.paymentDueReminder();
		return null;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/checkDuplicateTransactionOrNot.spring")
	public Result checkDuplicateTransactionOrNot(@NonNull EmployeeFinancialTransactionRequest empTransReq) {
		log.info("*** The control is inside of the checkDuplicateTransactionOrNot() in EmployeeFinancialRestService ***"+ empTransReq);;
		Result result = new Result();
		int response;
		try {
			response = employeeFinancialService.checkDuplicateTransactionOrNot(empTransReq);
			if(response>=1) {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription("Sorry, duplicate transactions are not allowed. This transaction already exists.");
			}else
			{
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			}
		} catch (Exception e) {
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			e.printStackTrace();
			result.setInternalExcetion(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}
}
