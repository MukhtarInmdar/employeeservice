/**
 * 
 */
package com.sumadhura.employeeservice.rest.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InvalidStatusException;
import com.sumadhura.employeeservice.exception.SQLInsertionException;
import com.sumadhura.employeeservice.persistence.dao.BookingFormServiceDao;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerData;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FinSchemePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.NOCDocumentsList;
import com.sumadhura.employeeservice.persistence.dto.NOCReleasePojo;
import com.sumadhura.employeeservice.service.BookingFormService;
import com.sumadhura.employeeservice.service.EmployeeTicketService;
import com.sumadhura.employeeservice.service.dto.BookingFormApproveRequest;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.BookingFormSavedStatus;
import com.sumadhura.employeeservice.service.dto.BookingFormSavedStatusResponse;
import com.sumadhura.employeeservice.service.dto.CustomerBookingFormInfo;
import com.sumadhura.employeeservice.service.dto.CustomerInfo;
import com.sumadhura.employeeservice.service.dto.CustomerKYCDocumentSubmitedInfo;
import com.sumadhura.employeeservice.service.dto.FlatBookingInfo;
import com.sumadhura.employeeservice.service.mappers.BookingFormMapper;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * BookingFormRestService class provides BookingForm specific Service .
 * services.
 * 
 * @author Srivenu
 * @since 21.05.2019
 * @time 12:30PM
 */

@Path("/bookingFormService")
public class BookingFormRestService {

	@Autowired(required = true)
	@Qualifier("EmployeeTicketServiceImpl")
	private EmployeeTicketService employeeTicketServiceImpl;

	@Autowired(required = true)
	@Qualifier("EmployeeTicketDaoImpl")
	private EmployeeTicketDao employeeTicketDaoImpl;

	@Autowired(required = true)
	@Qualifier("BookingFormServiceDaoImpl")
	private BookingFormServiceDao bookingFormServiceDaoImpl;
	
	@Autowired(required = true)
	@Qualifier("BookingFormServiceImpl")
	private BookingFormService bookingFormServiceImpl;

	private BookingFormMapper bookingFormMapper = new BookingFormMapper();
	
	private Logger logger = Logger.getLogger(BookingFormRestService.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getBookingDetails.spring")
	public Result getBookingFormDetails(@NonNull BookingFormRequest bookingFormRequest)
			throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the getBookingFormData in BookingFormRestService ********");
		Result result = new Result();
		if (bookingFormRequest.getCustomerId() != null && bookingFormRequest.getFlatBookingId() != null) {
			result = bookingFormServiceImpl.getBookingForm(bookingFormRequest);
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
	@Path("/getRegistrationDetails.spring")
	public Result getRegistrationDetails(@NonNull BookingFormRequest bookingFormRequest)
			throws Exception {
		logger.info("******* The control inside of the getRegistrationDetails in BookingFormRestService ********");
		Result result = new Result();
		if (bookingFormRequest.getFlatBookingId() != null) {
			List<FlatBookingInfo> customerRegistrationDetails =  bookingFormServiceImpl.getRegistrationDetails(bookingFormRequest);
			result.setResponseObjList(customerRegistrationDetails);
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
	@Path("/getCustomerAndCo_AppDetails.spring")
	public Result getCustomerAndCo_AppDetails(@NonNull BookingFormRequest bookingFormRequest)throws Exception {
		logger.info("***** Control inside the BookingFormRestService.getCustomerAndCo_AppDetails() *****");
		Result result = new Result();
		if (bookingFormRequest.getFlatBookingId() != null && bookingFormRequest.getCustomerId() != null) {
			CustomerBookingFormInfo customerBookingFormInfo  = bookingFormServiceImpl.getFlatCustomerAndCoAppBookingDetails(bookingFormRequest);
			Map<String, Object> customerDetails = bookingFormMapper.customerInfoKeyValuesPair(customerBookingFormInfo);
			result.setResponseObjList(customerDetails);
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
	@Path("/getUnitDetails.spring")
	public Result loadUnitDetails(@NonNull BookingFormRequest bookingFormRequest)throws Exception {
		logger.info("***** Control inside the BookingFormRestService.getUnitDetails() *****");
		Result result = new Result();
		if (bookingFormRequest.getFlatBookingId() != null && bookingFormRequest.getCustomerId() != null) {
			List<FlatBookingInfo> flatBookingInfos = bookingFormServiceImpl.loadUnitDetailsList(bookingFormRequest);
			Map<String, Object> customerDetails = bookingFormMapper.customerFlatInfoKeyValuesPair(flatBookingInfos);
			result.setResponseObjList(customerDetails);
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
	 * Mail : sending default mail to banker
	 * NOTE* If added any new Status for Booking then we need to add for  
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveBookingDetails.spring")
	public Result saveBookingDetails(@NonNull BookingFormRequest bookingFormRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException {
		logger.info("******* The control inside of the saveBookingDetails in BookingFormRestService ********"+bookingFormRequest);
		Result result = null;
		BookingFormSavedStatusResponse bookingFormSavedStatusResponse = new BookingFormSavedStatusResponse();
		List<BookingFormSavedStatus> bookingFormSavedStatuses = new ArrayList<>();
		if(bookingFormRequest.getCustomerBookingFormsInfos()!=null && bookingFormRequest.getCustomerBookingFormsInfos().size()>0) {
		    /*  If request is coming from SalesForce   */
			if(Util.isNotEmptyObject(bookingFormRequest.getMerchantId())){
			   /* setting employeeId and department Id */
				List<EmployeePojo> employeePojos = bookingFormServiceDaoImpl.getEmployeeDetails(bookingFormRequest.getEmployeeName(),null);
				bookingFormRequest.setEmpId(Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeId())?employeePojos.get(0).getEmployeeId():0l:0l);
				bookingFormRequest.setDeptId(Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getDepartmentId())?employeePojos.get(0).getDepartmentId():0l:0l);
			}
			
			for(CustomerBookingFormInfo customerBookingForm : bookingFormRequest.getCustomerBookingFormsInfos())
			{
				//if ((customerBookingForm.getCustomerInfo().getPancard() != null  && !customerBookingForm.getCustomerInfo().getPancard().equals("")) || (customerBookingForm.getCustomerInfo().getPassport()!=null && customerBookingForm.getCustomerInfo().getPassport().equals(""))) {
				List<String> errorMsgs = bookingFormServiceImpl.validateBookingForm(customerBookingForm);
				logger.info("******* The control inside of the saveBookingDetails in BookingFormRestService errorMsgs ********"+errorMsgs);
				BookingFormSavedStatus bookingFormSavedStatus = new BookingFormSavedStatus();
				if(errorMsgs!=null && errorMsgs.size()>2) {
					bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
					bookingFormSavedStatus.setLeadId(Long.parseLong(errorMsgs.get(0)));
					bookingFormSavedStatus.setCustName(errorMsgs.get(1));
					errorMsgs.remove(0);
					errorMsgs.remove(0);
					bookingFormSavedStatus.setError(errorMsgs);
					logger.info("******* The control inside of the saveBookingDetails in BookingFormRestService failure ********"+bookingFormSavedStatus);
				} else {
					try {
						 /*  If request is coming from XL   */	
						if(Util.isEmptyObject(bookingFormRequest.getMerchantId())){
						  /* adding one Day to the Booking form keys */
						   bookingFormServiceImpl.addOneDay(customerBookingForm);
						}
						/*  adding booking form uploaded employee */
						customerBookingForm.setEmployeeId(Util.isNotEmptyObject(bookingFormRequest.getEmpId())?bookingFormRequest.getEmpId():0l);
						customerBookingForm.setMenuId(Util.isNotEmptyObject(bookingFormRequest.getMenuId())?bookingFormRequest.getMenuId():0l);
						customerBookingForm.setPortNumber(bookingFormRequest.getPortNumber());
						logger.info("***** Booking started to save BookingFormRestService.saveBookingDetails() ******");
						bookingFormSavedStatus = bookingFormServiceImpl.saveBookingFormDetails(customerBookingForm);
						logger.info("***** Booking saved successfully BookingFormRestService.saveBookingDetails() ******");
					} catch(InSufficeientInputException inE) {
						logger.error("**** The Exception detailed informtion is ****" , inE);
						bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
						List<String> errorMessages = inE.getMessages();
						bookingFormSavedStatus.setLeadId(Long.parseLong(errorMessages.get(0)));
						bookingFormSavedStatus.setCustName(errorMessages.get(1));
						errorMessages.remove(0);
						errorMessages.remove(0);
						bookingFormSavedStatus.setError(errorMessages);
					}
				}
				bookingFormSavedStatuses.add(bookingFormSavedStatus);
			}
			
			bookingFormSavedStatusResponse.setBookingFormSavedStatuses(bookingFormSavedStatuses);
			result = bookingFormSavedStatusResponse;
			if ("failure".equalsIgnoreCase(bookingFormSavedStatuses.get(0).getStatus())) {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
			} else {
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
				
			}
//			result.setResponseCode(HttpStatus.success.getResponceCode());
//			result.setDescription(HttpStatus.success.getDescription());
			
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		result = bookingFormSavedStatusResponse;
		logger.info("******* The Response of the saveBookingDetails in BookingFormRestService ********"+result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFormsList.spring")
	public Result getBookingFormsList(@NonNull BookingFormRequest bookingFormRequest) throws InSufficeientInputException, InvalidStatusException {
		Result result = null;
		
		bookingFormRequest.setRequestUrl("getFormsList");
		StringBuilder siteIdSb = new StringBuilder();
		siteIdSb.append("(");
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+bookingFormRequest.getEmpSiteId());
		if(bookingFormRequest.getEmpSiteId()!=null)
		{
			int length = bookingFormRequest.getEmpSiteId().size();
			for(int i=0;i<length;i++)
			{
				siteIdSb.append(bookingFormRequest.getEmpSiteId().get(i));
				if(i<length-1)
				{
					siteIdSb.append(",");
				}
			}
			siteIdSb.append(")");
			String siteIdStr = siteIdSb.toString();
			bookingFormRequest.setSiteName(siteIdStr);
		}
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = bookingFormServiceDaoImpl.getCustomerPropertyDetails(bookingFormRequest);
		if(customerPropertyDetailsPojos!=null&&customerPropertyDetailsPojos.size()>0)
		{
			result = bookingFormMapper.customerPropertyDetailsPojosTocustomerPropertyDetailsInfos(customerPropertyDetailsPojos);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			
		} else {
			result = new Result();
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("No results found");
			result.setErrors(errorMsgs);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/actionBookingDetails.spring")
	public Result actionBookingDetails(@NonNull BookingFormRequest bookingFormRequest) throws InSufficeientInputException, InvalidStatusException {
		logger.info("******* The control inside of the actionBookingDetails in BookingFormRestService ********"+bookingFormRequest);
	
		/* To Approve or Reject or cancel Multiple Customers for EmployeeService  */
		List<BookingFormApproveRequest> bookingFormApproveRequestList = bookingFormRequest.getBookingFormApproveRequest();
		if(Util.isNotEmptyObject(bookingFormApproveRequestList) && bookingFormApproveRequestList.size() > 0) {
			
			BookingFormSavedStatusResponse bookingFormSavedStatusResponse = new BookingFormSavedStatusResponse();
			List<BookingFormSavedStatus> bookingFormSavedStatuses = new ArrayList<>();
			
			for(BookingFormApproveRequest bookingFormApproveRequest : bookingFormApproveRequestList) {
					
				List<String> errorMsgsList = new ArrayList<String>();
				BookingFormSavedStatus bookingFormSavedStatus = new BookingFormSavedStatus();
				
				/* This is responsible for approve or reject a customer */
				if(Util.isNotEmptyObject(bookingFormApproveRequest.getCustomerId()) && Util.isNotEmptyObject(bookingFormApproveRequest.getFlatBookingId())) {
					bookingFormRequest.setCustomerId(bookingFormApproveRequest.getCustomerId());
					bookingFormRequest.setFlatBookingId(bookingFormApproveRequest.getFlatBookingId());
				}
				
				//if approving from tool then current approving or rejecting booking status is Pending 
				bookingFormRequest.setActualStatusId(Status.PENDING.getStatus());
				
				/* This is responsible for cancel a customer */	
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
							bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
							bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
						
						) {
					if (Util.isNotEmptyObject(bookingFormApproveRequest.getSiteName()) && Util.isNotEmptyObject(bookingFormApproveRequest.getBlockName())
							&& Util.isNotEmptyObject(bookingFormApproveRequest.getFloorName()) && Util.isNotEmptyObject(bookingFormApproveRequest.getFlatNo())
							&& bookingFormApproveRequest.getBookingformCanceledDate() != null && Util.isNotEmptyObject(bookingFormApproveRequest.getComments()) && Util.isNotEmptyObject(bookingFormApproveRequest.getEmployeeName())){
						bookingFormRequest.setFlatNo(bookingFormApproveRequest.getFlatNo());
						bookingFormRequest.setFloorName(bookingFormApproveRequest.getFloorName());
						bookingFormRequest.setBlockName(bookingFormApproveRequest.getBlockName());
						bookingFormRequest.setSiteName(bookingFormApproveRequest.getSiteName());
						bookingFormRequest.setBookingformCanceledDate(bookingFormApproveRequest.getBookingformCanceledDate());
						bookingFormRequest.setComments(bookingFormApproveRequest.getComments());
						bookingFormRequest.setEmployeeName(bookingFormApproveRequest.getEmployeeName());
						bookingFormServiceImpl.cancelBookingFormHelper(bookingFormRequest);
					}		
				}
					
				if (bookingFormRequest.getCustomerId() != null && bookingFormRequest.getFlatBookingId() != null && bookingFormRequest.getActionStr()!=null && !bookingFormRequest.getActionStr().equals("")) {
					if(bookingFormRequest.getActionStr().equalsIgnoreCase("approve") || bookingFormRequest.getActionStr().equalsIgnoreCase("active")) {
						bookingFormRequest.setStatusId(Status.ACTIVE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("reject")) {
							bookingFormRequest.setStatusId(Status.REJECTED.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("pending")) {
							bookingFormRequest.setStatusId(Status.PENDING.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("inactive")) {// || bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")
							bookingFormRequest.setStatusId(Status.INACTIVE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")) {
						bookingFormRequest.setStatusId(Status.CANCEL.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded")) {
						bookingFormRequest.setStatusId(Status.CANCELLED_NOT_REFUNDED.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Swap")) {
						bookingFormRequest.setStatusId(Status.SWAP.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Available")) {
						bookingFormRequest.setStatusId(Status.AVAILABLE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked")) {
						bookingFormRequest.setStatusId(Status.BLOCKED.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open")) {
						bookingFormRequest.setStatusId(Status.NOT_OPEN.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update")) {
						bookingFormRequest.setStatusId(Status.PRICE_UPDATE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case")) {
						bookingFormRequest.setStatusId(Status.LEGAL_CASE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible")) {
						bookingFormRequest.setStatusId(Status.PMAY_SCHEME_ELIGIBLE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Retained")) {
						bookingFormRequest.setStatusId(Status.RETAINED.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Delete")) {
						bookingFormRequest.setStatusId(Status.INACTIVE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")) {
						bookingFormRequest.setStatusId(Status.ASSIGNMENT.getStatus());
					}else {
							errorMsgsList.add("The Invalid Input is given for requested service.");
							throw new InSufficeientInputException(errorMsgsList);
					}
					try {
						bookingFormServiceImpl.saveBookingChangedStatus(bookingFormRequest);
						
						errorMsgsList = bookingFormServiceImpl.putActionBookingForm(bookingFormRequest);
						bookingFormSavedStatus.setLeadId((Util.isNotEmptyObject(bookingFormApproveRequest.getLeadId()))?bookingFormApproveRequest.getLeadId():0l);
						bookingFormSavedStatus.setCustName((Util.isNotEmptyObject(bookingFormApproveRequest.getCustName()))?bookingFormApproveRequest.getCustName():"");
						bookingFormSavedStatus.setStatus(HttpStatus.success.getDescription());
						bookingFormSavedStatus.setError(errorMsgsList);
					} catch (Exception ex) {
						logger.error("**** The Exception detailed informtion is ****" , ex);
						bookingFormSavedStatus.setLeadId((Util.isNotEmptyObject(bookingFormApproveRequest.getLeadId()))?bookingFormApproveRequest.getLeadId():0l);
						bookingFormSavedStatus.setCustName((Util.isNotEmptyObject(bookingFormApproveRequest.getCustName()))?bookingFormApproveRequest.getCustName():"");
						bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
						errorMsgsList.add(ex.getMessage());
						bookingFormSavedStatus.setErrors(errorMsgsList);
					}
				} else {
						errorMsgsList.add("The Invalid Input is given for requested service.");
						bookingFormSavedStatus.setLeadId((Util.isNotEmptyObject(bookingFormApproveRequest.getLeadId()))?bookingFormApproveRequest.getLeadId():0l);
						bookingFormSavedStatus.setCustName((Util.isNotEmptyObject(bookingFormApproveRequest.getCustName()))?bookingFormApproveRequest.getCustName():"");
						bookingFormSavedStatus.setStatus(HttpStatus.failure.getDescription());
						bookingFormSavedStatus.setError(errorMsgsList);
					}
					bookingFormSavedStatuses.add(bookingFormSavedStatus);
				}
				bookingFormSavedStatusResponse.setBookingFormSavedStatuses(bookingFormSavedStatuses);
				bookingFormSavedStatusResponse.setResponseCode(HttpStatus.success.getResponceCode());
				bookingFormSavedStatusResponse.setDescription(HttpStatus.success.getDescription());
				logger.info("******* The Response of the actionBookingDetails in BookingFormRestService ********"+bookingFormSavedStatusResponse);
				return bookingFormSavedStatusResponse;
		}else {
			/* This control is coming from  SALES FORCE TEAM for cancel bookingform */	
			List<String> errorMsgs = new ArrayList<String>();
			Result result = new Result();
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
						bookingFormRequest.getActionStr().equalsIgnoreCase("pending") ||
						bookingFormRequest.getActionStr().equalsIgnoreCase("reject")||
						bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")
					
					) {
				 bookingFormServiceImpl.cancelBookingFormHelper(bookingFormRequest);
			}
				
			if (bookingFormRequest.getCustomerId() != null && bookingFormRequest.getFlatBookingId() != null &&
					bookingFormRequest.getActionStr()!=null && !bookingFormRequest.getActionStr().equals("")) {
					
					if(bookingFormRequest.getActionStr().equalsIgnoreCase("approve") || bookingFormRequest.getActionStr().equalsIgnoreCase("active")) {
						bookingFormRequest.setStatusId(Status.ACTIVE.getStatus());

					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("reject")) {
						bookingFormRequest.setStatusId(Status.REJECTED.getStatus());

					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("pending")) {
						bookingFormRequest.setStatusId(Status.PENDING.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("inactive")) {// || bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")
						bookingFormRequest.setStatusId(Status.INACTIVE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("cancel")) {
						bookingFormRequest.setStatusId(Status.CANCEL.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Cancelled not refunded")) {
						bookingFormRequest.setStatusId(Status.CANCELLED_NOT_REFUNDED.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Swap")) {
						bookingFormRequest.setStatusId(Status.SWAP.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Available")) {
						bookingFormRequest.setStatusId(Status.AVAILABLE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Blocked")) {
						bookingFormRequest.setStatusId(Status.BLOCKED.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Not Open")) {
						bookingFormRequest.setStatusId(Status.NOT_OPEN.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Price Update")) {
						bookingFormRequest.setStatusId(Status.PRICE_UPDATE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Legal case")) {
						bookingFormRequest.setStatusId(Status.LEGAL_CASE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("PMAY Scheme Eligible")) {
						bookingFormRequest.setStatusId(Status.PMAY_SCHEME_ELIGIBLE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Retained")) {
						bookingFormRequest.setStatusId(Status.RETAINED.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Delete")) {
						bookingFormRequest.setStatusId(Status.INACTIVE.getStatus());
					} else if(bookingFormRequest.getActionStr().equalsIgnoreCase("Assignment")) {
						bookingFormRequest.setStatusId(Status.ASSIGNMENT.getStatus());
					}else {
						
						errorMsgs.add("The Invalid Input is given for requested service.");
						throw new InSufficeientInputException(errorMsgs);
					}
					try {
						bookingFormServiceImpl.saveBookingChangedStatus(bookingFormRequest);
						
						errorMsgs = bookingFormServiceImpl.putActionBookingForm(bookingFormRequest);
						
					} catch (SQLInsertionException ex) {
						result.setResponseCode(HttpStatus.failure.getResponceCode());
						result.setDescription(HttpStatus.failure.getDescription());
						result.setErrors(ex.getMessages());
						logger.info("******* The Response of the actionBookingDetails in BookingFormRestService ********"+result);
						return result;
					}
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription(HttpStatus.success.getDescription());
			} else {
				errorMsgs.add("The Invalid Input is given for requested service.");
				throw new InSufficeientInputException(errorMsgs);
			}
			logger.info("******* The Response of the actionBookingDetails in BookingFormRestService ********"+result);
			return result;
		}		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insertOrUpdateCheckListDetails.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result insertOrUpdateCheckListDetails(@NonNull BookingFormRequest bookingFormRequest) throws InSufficeientInputException{
		logger.info("*** The control is inside the insertOrUpdateCheckListSalesDetails in BookingFormRestService ***"+bookingFormRequest);	
		List<String> errorMsgs = new ArrayList<String>();
		BookingFormSavedStatus bookingFormSavedStatus = null;
		
		if(true) {
		final CustomerBookingFormInfo customerBookingFormsInfo = bookingFormRequest.getCustomerBookingFormsInfo();
			if (Util.isNotEmptyObject(customerBookingFormsInfo.getFlatBookingInfo().getFlatInfo().getFlatNo())
					&& Util.isNotEmptyObject(
							customerBookingFormsInfo.getFlatBookingInfo().getFloorInfo().getFloorName())
					&& Util.isNotEmptyObject(customerBookingFormsInfo.getFlatBookingInfo().getBlockInfo().getName())
					&& Util.isNotEmptyObject(customerBookingFormsInfo.getFlatBookingInfo().getSiteInfo().getName())
					&& Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())
					&& Util.isNotEmptyObject(bookingFormRequest.getActionStr())
					&& Util.isNotEmptyObject(Util
							.isNotEmptyObject(customerBookingFormsInfo.getCustomerBookingInfo().getSalesTeamLeadId()))
					&& (Util.isNotEmptyObject(customerBookingFormsInfo.getCustomerInfo().getFirstName())
							|| Util.isNotEmptyObject(customerBookingFormsInfo.getCustomerInfo().getLastName()))) {
			 bookingFormSavedStatus = bookingFormServiceImpl.insertOrUpdateCheckListDetails(bookingFormRequest);
		}else {
			errorMsgs.add("The Invalid Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		}
		logger.info("******* The Response of the insertOrUpdateCheckListDetails in BookingFormRestService ********"+bookingFormSavedStatus);
		return bookingFormSavedStatus;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateApplicantOrCoapplicant.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result updateApplicantOrCoApplicantData(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the updateApplicantOrCoApplicantData in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			bookingFormServiceImpl.updateApplicantOrCoApplicantData(bookingFormRequest);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription("Data updated successfully.");
		}catch (InSufficeientInputException insEx) {
			logger.error("**** The Exception detailed informtion is ****" , insEx);
			result.setResponseCode(HttpStatus.insufficientInput.getResponceCode());
			result.setDescription(HttpStatus.insufficientInput.getDescription());
			result.setErrors(insEx.getMessages());
		}catch (Exception ex) {
			logger.error("**** The Exception detailed informtion is ****" , ex);
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("**** The updateApplicantOrCoApplicantData result ****  "+  result);
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAgreementTypesList.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getAggrementTypes(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getAggrementTypes in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && Util.isNotEmptyObject(bookingFormRequest.getSiteId())) {
			try {
				Map<String,Object> response = bookingFormServiceImpl.getAgreementTypesList(bookingFormRequest);
				result.setResponseObjList(response);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} catch (Exception ex) {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(ex.getMessage());
				result.setErrors(errorMsgs);
			}
		} else {
			throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
		}

		logger.info("******* The Response of the getAgreementTypesList in BookingFormRestService ********"+result);
		return result;
	}

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/generateBookingAllotmentLetter.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result generateWelcomeLetter(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the generateAllotmentLetter in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && Util.isNotEmptyObject(bookingFormRequest.getFlatBookingId()) 
					&& Util.isNotEmptyObject(bookingFormRequest.getSiteId()) && Util.isNotEmptyObject(bookingFormRequest.getFlatId())) {
				if(Util.isEmptyObject(bookingFormRequest.getActionStr())) {
					bookingFormRequest.setActionStr("Regular");
				}
				
				Map<String, Object> dataForGenerateAllotmentLetterHelper  = bookingFormServiceImpl.generateWelcomeLetter(bookingFormRequest);
				result.setResponseObjList(dataForGenerateAllotmentLetterHelper);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex instanceof EmployeeFinancialServiceException) {
				EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)ex;
				logger.info("Failed to generate Welcome letter."+HttpStatus.failure.getDescription()+" "+exx.getMessage());
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
				result.setErrors(exx.getMessages());
				if (Util.isEmptyObject(exx.getMessages())) {
					result.setErrors(Arrays.asList(exx.getMessage()));
				}
			} else if (ex instanceof InSufficeientInputException) {
				InSufficeientInputException e = (InSufficeientInputException) ex;
				
				if(Util.isNotEmptyObject(e.getMessages())) {
					logger.info("Failed to generating welcome letter."+HttpStatus.failure.getDescription()+" "+e.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
					result.setErrors(e.getMessages());
				} else {
					logger.info("Failed to generating welcome letter."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
					result.setErrors(Arrays.asList("Error occurred while generating welcome letter, plz try again !"));
				}
			} else {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
				/*List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(ex.getMessage());*/
				result.setErrors(Arrays.asList("Error occurred while generating welcome letter, plz try again !"+" "+ex.getMessage()));
			}
		}
		logger.info("******* The Response of the generateAllotmentLetter in BookingFormRestService ********"+result);
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCustomerFaltDocuments.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getCustomerFlatDocuments(@NonNull MessengerRequest messengerRequest) throws Exception {
		logger.info("***** Control inside the BookingFormRestService.getCustomerFaltDocuments() *****");
		Result result=new Result();
		//try {
			if(Util.isNotEmptyObject(messengerRequest.getRequestUrl()) && Util.isNotEmptyObject(messengerRequest.getFlatBookingId()) 
				&& Util.isNotEmptyObject(messengerRequest.getFlatId())) {
				result  = bookingFormServiceImpl.getCustomerFlatDocuments(messengerRequest);
				//result.setResponseObjList(dataForGenerateAllotmentLetterHelper);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		/*} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}*/
		logger.info("******* The Response of the getCustomerFaltDocuments in BookingFormRestService ********"+result);
		return result;
	}

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getWelcomeDocumentLetters.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getWelcomeDocumentLetters(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the generateAllotmentLetter in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl()) && Util.isNotEmptyObject(bookingFormRequest.getFlatBookingId()) 
					&& Util.isNotEmptyObject(bookingFormRequest.getSiteId()) && Util.isNotEmptyObject(bookingFormRequest.getFlatId())) {
				//Map<String, Object> dataForGenerateAllotmentLetterHelper = bookingFormServiceImpl.getWelcomeDocumentLetters(bookingFormRequest);
				//bookingFormRequest.result.setResponseObjList(dataForGenerateAllotmentLetterHelper);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the generateAllotmentLetter in BookingFormRestService ********"+result);
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/inactiveBookings.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result inactiveBookings(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the inactiveBookings in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())  && Util.isNotEmptyObject(bookingFormRequest.getSiteId()) ) {
				Map<String, Object> dataForGenerateAllotmentLetterHelper = bookingFormServiceImpl.inactiveBookings(bookingFormRequest);
				result.setResponseObjList(dataForGenerateAllotmentLetterHelper);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the generateAllotmentLetter in BookingFormRestService ********"+result);
		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/testingUrl.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result testingUrl(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the inactiveBookings in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())  && Util.isNotEmptyObject(bookingFormRequest.getSiteId()) ) {
				Map<String, Object> dataForGenerateAllotmentLetterHelper = bookingFormServiceImpl.testingUrl(bookingFormRequest);
				result.setResponseObjList(dataForGenerateAllotmentLetterHelper);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			if(ex instanceof EmployeeFinancialServiceException) {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)ex;
				//result.setErrors(Arrays.asList(ex.getMessage()));
				if (Util.isNotEmptyObject(exx.getMessages())) {
					result.setErrors( exx.getMessages());
				} else {
					result.setErrors(Arrays.asList(exx.getMessage()));
				}
			} else {
				ex.printStackTrace();
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				result.setErrors(Arrays.asList("Error Ocuured"));
			}
		}
		logger.info("******* The Response of the generateAllotmentLetter in BookingFormRestService ********"+result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/generateBookingNOCLetter.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result generateBookingNOCLetter(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the generateBookingNOCLetter in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())  && Util.isNotEmptyObject(bookingFormRequest.getSiteId()) ) {
				Map<String, Object> nocMap = bookingFormServiceImpl.generateNOCLetter(bookingFormRequest,null);
				if ("success".equals(nocMap.get("successMasg"))) {
					nocMap.put("successMasg","NOC Generated Successfully.");
					result.setResponseObjList(nocMap);
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription("NOC Generated Successfully.");
				} else {
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add((String) nocMap.get("errorMsg"));
					result.setErrors(errorMsgs);
				}
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex instanceof EmployeeFinancialServiceException) {
				EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)ex;
				logger.info("Failed to generate noc letter."+HttpStatus.failure.getDescription()+" "+exx.getMessage());
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				result.setErrors(exx.getMessages());
				if (Util.isEmptyObject(exx.getMessages())) {
					result.setErrors(Arrays.asList(exx.getMessage()));
				}
			} else if (ex instanceof InSufficeientInputException) {
				InSufficeientInputException e = (InSufficeientInputException) ex;
				
				if(Util.isNotEmptyObject(e.getMessages())) {
					logger.info("Failed to noc transaction."+HttpStatus.failure.getDescription()+" "+e.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					result.setErrors(e.getMessages());
				} else {
					logger.info("Failed to noc transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					result.setErrors(Arrays.asList("Error occurred while generating noc letter, plz try again once !"));
				}
			} else {
				ex.printStackTrace();
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Failed to generate NOC letter, Please try again.");
				result.setErrors(errorMsgs);
			}
		}
		logger.info("******* The Response of the generateBookingNOCLetter in BookingFormRestService ********"+result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNOCDocuments.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getNOCDocuments(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getNOCDocuments in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest.getFlatBookingId()) ) {
				List<NOCReleasePojo> list = bookingFormServiceImpl.getNOCReleaseDetails(bookingFormRequest);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getNOCDocuments in BookingFormRestService ********"+result);
		return result;
	}
	
	
   /* testing purpuse
	http://localhost:8888/employeeservice/bookingFormService/sendMailToBankerForLoan.spring
	{
	    "flatBookingId":1234,
	     "BlockId":257,
	      "siteId":139
	}
	*/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sendMailToBankerForLoan.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result sendMailToBankerForLoan(@NonNull CustomerInfo customerInfo) throws Exception {
//		customerInfo.setSiteId(107l);
//		customerInfo.setBlockId(154l);
//		customerInfo.setFlatBookingId(1304l);
		bookingFormServiceImpl.sendMailToBankerForLoan(customerInfo,null);
		return null;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNOCDocumentsList.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getNOCDocumentsList(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getNOCDocumentsList in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest) ) {
				List<NOCDocumentsList> list = bookingFormServiceImpl.getNOCDocumentsList(bookingFormRequest);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getNOCDocumentsList in BookingFormRestService ********"+result);
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCustomerData.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getCustomerData(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getCustomerData in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest) ) {
				CustomerInfo customerInfo =new CustomerInfo();
				customerInfo.setFlatBookingId(bookingFormRequest.getFlatBookingId());
				List<CustomerData> list = bookingFormServiceImpl.getCustomerData(customerInfo);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getCustomerData in BookingFormRestService ********"+result);
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/generateLoanNOCLetter.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result generateLoanNOCLetter(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the generateLoanNOCLetter in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if(Util.isNotEmptyObject(bookingFormRequest.getRequestUrl())  && Util.isNotEmptyObject(bookingFormRequest.getSiteId()) ) {
				Map<String, Object> nocMap = bookingFormServiceImpl.generateLoanNOCLetter(bookingFormRequest);
				if ("success".equals(nocMap.get("successMasg"))) {
					nocMap.put("successMasg","NOC Generated Successfully.");
					result.setResponseObjList(nocMap);
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription("NOC Generated Successfully.");
				} else {
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add((String) nocMap.get("errorMsg"));
					result.setErrors(errorMsgs);
				}
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			if(ex instanceof EmployeeFinancialServiceException) {
				EmployeeFinancialServiceException exx = (EmployeeFinancialServiceException)ex;
				logger.info("Failed to generate noc letter."+HttpStatus.failure.getDescription()+" "+exx.getMessage());
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				result.setErrors(exx.getMessages());
				result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
				if (Util.isEmptyObject(exx.getMessages())) {
					result.setErrors(Arrays.asList(exx.getMessage()));
				}
			} else if (ex instanceof InSufficeientInputException) {
				InSufficeientInputException e = (InSufficeientInputException) ex;
				
				if(Util.isNotEmptyObject(e.getMessages())) {
					logger.info("Failed to noc transaction."+HttpStatus.failure.getDescription()+" "+e.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
					result.setErrors(e.getMessages());
				} else {
					logger.info("Failed to noc transaction."+HttpStatus.failure.getDescription()+" "+ex.getMessage());
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
					result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
					result.setErrors(Arrays.asList("Error occurred while generating noc letter, plz try again once !"));
				}
			} else {
				ex.printStackTrace();
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				result.setInternalExcetion(ExceptionUtils.getStackTrace(ex));
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Failed to generate NOC letter, Please try again.");
				result.setErrors(errorMsgs);
			}
		}
		logger.info("******* The Response of the generateLoanNOCLetter in BookingFormRestService ********"+result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getKycDocumentsList.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getKycDocumentsList(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getKycDocumentsList in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest) ) {
				List<CustomerKYCDocumentSubmitedInfo> list = bookingFormServiceImpl.getKycDocumentsList(bookingFormRequest);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getKycDocumentsList in BookingFormRestService ********"+result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFlatDetails.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getFlatDetails(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getFlatDetails in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest) ) {
				Map<String,Object> map = bookingFormServiceImpl.getFlatDetails(bookingFormRequest);
				result.setResponseObjList(map);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getFlatDetails in BookingFormRestService ********"+result);
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNonBookedDetails.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getNonBookedDetails(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getNonBookedDetails in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest) ) {
				List<CustomerData> list = bookingFormServiceImpl.getNonBookedDetails(bookingFormRequest);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getNonBookedDetails in BookingFormRestService ********"+result);
		return result;
	}
	
		@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCityList.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getcityList(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getCityList in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			
				CustomerInfo customerInfo =new CustomerInfo();
			if(Util.isNotEmptyObject(bookingFormRequest.getStateId())) {
				customerInfo.setStateId(bookingFormRequest.getStateId());
			}
				List<CustomerAddressPojo> list = bookingFormServiceImpl.getCityList(customerInfo);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
		
		
			
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getCityList in BookingFormRestService ********"+result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCountryList.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getCountryList(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getCountryList in BookingFormRestService ***"+bookingFormRequest);	
		Result result = new Result();
		try {

			List<CustomerAddressPojo> list = bookingFormServiceImpl.getCountryList();
			result.setResponseObjList(list);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());

		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getCountryList in BookingFormRestService ********" + result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFinSchemes.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getFinSchemes(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getFinSchemes in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest) ) {
				List<FinSchemePojo> list = bookingFormServiceImpl.getFinSchemes(bookingFormRequest);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getFinSchemes in BookingFormRestService ********"+result);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOldSalesForesBookingIds.spring")
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Result getOldSalesForesBookingIds(@NonNull BookingFormRequest bookingFormRequest) throws Exception {
		logger.info("*** The control is inside the getOldSalesForesBookingIds in BookingFormRestService ***"+bookingFormRequest);	
		Result result=new Result();
		try {
			if( Util.isNotEmptyObject(bookingFormRequest) ) {
				List<FlatBookingPojo> list = bookingFormServiceImpl.getSalesForcesBookingIds(bookingFormRequest);
				result.setResponseObjList(list);
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());	
			} else {
				throw new InSufficeientInputException(Arrays.asList("The Invalid Input is given for requested service."));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(ex.getMessage());
			result.setErrors(errorMsgs);
		}
		logger.info("******* The Response of the getOldSalesForesBookingIds in BookingFormRestService ********"+result);
		return result;
	}
	
}