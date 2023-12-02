/**
 * 
 */
package com.sumadhura.employeeservice.rest.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.dto.EmployeeFinancialPushNotificationInfo;
import com.sumadhura.employeeservice.dto.EmployeeTicketRequest;
import com.sumadhura.employeeservice.dto.EmployeeTicketResponse;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.dto.TicketEscalationResponse;
import com.sumadhura.employeeservice.dto.TicketPushNotificationRequest;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.exception.InvalidStatusException;
import com.sumadhura.employeeservice.exception.SQLInsertionException;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLevelDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.LevelPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketReportingPojo;
import com.sumadhura.employeeservice.service.EmployeeTicketService;
import com.sumadhura.employeeservice.service.helpers.EmployeeFinancialPushNotificationHelper;
import com.sumadhura.employeeservice.service.mappers.EmployeeTicketMapper;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * EmployeeTicketRestService class provides Employee Ticketing specific.
 * services.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 04:30PM
 */

@Path("/employeeTicket")
public class EmployeeTicketRestService {

	@Autowired(required = true)
	@Qualifier("EmployeeTicketServiceImpl")
	private EmployeeTicketService employeeTicketServiceImpl;
	private Logger LOGGER = Logger.getLogger(EmployeeTicketRestService.class);
	
	@Autowired(required=true)
	private EmployeeTicketMapper mapper;
	
	@Autowired(required = true)
	@Qualifier("EmployeeFinancialPushNotificationHelper")
	private EmployeeFinancialPushNotificationHelper pushNotificationHelper;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTicketList.spring")
	public Result getTicketList(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getTicketList  in  EmployeeTicketRestService ********");
		Result result = null;
		if (employeeTicketRequest.getEmployeeId() != null && employeeTicketRequest.getSiteIds() != null && employeeTicketRequest.getDepartmentId()!=null && employeeTicketRequest.getRequestUrl()!=null) {
		    result = employeeTicketServiceImpl.getTicketList(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
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
	@Path("/getTicket.spring")
	public Result getTicketDtls(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {
		LOGGER.info("******* The control inside of the getTicket  in  EmployeeTicketRestService ********");
		Result result = new Result();
		if (Util.isNotEmptyObject(employeeTicketRequest.getTicketId()) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl()) && Util.isNotEmptyObject(employeeTicketRequest.getEmployeeId())){
			result = employeeTicketServiceImpl.getTicketDtls(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
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
	@Path("/getCustomerRaisedTicketList.spring")
	public Result getCustomerRaisedTicketList(EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException {
		LOGGER.info("******* The control inside of the getTicket  in  EmployeeTicketRestService ********");
		Result result = new Result();
		if (employeeTicketRequest.getFlatBookingId()!= null && employeeTicketRequest.getRequestUrl()!=null) {
			result = employeeTicketServiceImpl.getCustomerRaisedTicketList(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
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
	@Path("/getCustomerTicketDetails.spring")
	public Result getCustomerTicketDetails(EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {
		LOGGER.info("******* The control inside of the getTicket  in  EmployeeTicketRestService ********");
		Result result = new Result();
		if (employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getStatusId()!=null) {
			result = employeeTicketServiceImpl.getCustomerTicketDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
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
	@Path("/getCustomerProfileDetails.spring")
	public Result getCustomerProfileDetails(EmployeeTicketRequest employeeTicketRequest)throws InSufficeientInputException {
		LOGGER.info("******* The control inside of the getCustomerProfileDetails in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getCustomerId()!=null && employeeTicketRequest.getFlatId()!=null) {
			result = employeeTicketServiceImpl.getCustomerProfileDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/updateTicketConversation.spring")
	public Result updateTicketConversation(@NonNull EmployeeTicketRequest employeeTicketRequest) throws Exception {
		LOGGER.info("******* The control inside of the getDepartmentDetails in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getDepartmentId()!=null  && employeeTicketRequest.getFromDeptId()!=null && employeeTicketRequest.getFromId()!=null  && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getRequestUrl()!=null && employeeTicketRequest.getToId()!=null && employeeTicketRequest.getToType()!=null && employeeTicketRequest.getMessage()!=null ){
			result = employeeTicketServiceImpl.updateTicketConversation(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for updateTicketConversation service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTicketForwardMenuDetails.spring")
	public Result getTicketForwardMenuDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, SQLInsertionException {
		LOGGER.info("******* The control inside of the getDepartmentDetails in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getEmployeeId()!=null){
			result = employeeTicketServiceImpl.getTicketForwardMenuDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/forwardTicketDetails.spring")
	public Result forwardTicketDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws Exception {
		LOGGER.info("******* The control inside of the getDepartmentDetails in  EmployeeTicketRestService ********");
		Result result = new Result();
		//if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getFromId()!=null  && employeeTicketRequest.getFromDeptId()!=null && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getMessage()!=null && employeeTicketRequest.getDepartmentId()!=null && employeeTicketRequest.getRequestUrl()!=null  /*(employeeTicketRequest.getToId()!=null || employeeTicketRequest.getToDeptId()!=null) && employeeTicketRequest.getToType()!=null */){
		if(employeeTicketRequest.getTicketId()!=null   && employeeTicketRequest.getMessage()!=null &&  employeeTicketRequest.getRequestUrl()!=null){
			result = employeeTicketServiceImpl.forwardTicketDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/getDepartmentDetails.spring")
	public Result getDepartmentDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException {
		LOGGER.info("******* The control inside of the getDepartmentDetails in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getEmployeeId()!=null){
			result = employeeTicketServiceImpl.getDepartmentDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/seekInfoTicketDetails.spring")
	public Result seekInfoTicketDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, SQLInsertionException, IllegalAccessException, InvocationTargetException, InformationNotFoundException {
		LOGGER.info("******* The control inside of the seekInfoTicketDetails in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getRequestUrl()!=null && employeeTicketRequest.getFromId()!=null  && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getFromDeptId()!=null && employeeTicketRequest.getMessage()!=null &&  (Util.isNotEmptyObject(employeeTicketRequest.getToId()) || Util.isNotEmptyObject(employeeTicketRequest.getToDeptId()))){
			result = employeeTicketServiceImpl.seekInfoTicketDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/ticketSpecifictviewRequestInfo.spring")
	public Result ticketViewRequestInfo(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, SQLInsertionException, IllegalAccessException, InvocationTargetException, InvalidStatusException, InformationNotFoundException, InterruptedException, ExecutionException {
		LOGGER.info("******* The control inside of the viewRequestInfo in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getFromId()!=null  && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getFromDeptId()!=null && employeeTicketRequest.getRequestUrl()!=null){
			result = employeeTicketServiceImpl.ticketViewRequestInfo(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested viewRequestInfo service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/viewRequestInfo.spring")
	public Result viewRequestInfo(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, SQLInsertionException, IllegalAccessException, InvocationTargetException, InvalidStatusException, InformationNotFoundException {
		LOGGER.info("******* The control inside of the viewRequestInfo in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getFromId()!=null  && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getFromDeptId()!=null && employeeTicketRequest.getRequestUrl()!=null){
			result = employeeTicketServiceImpl.viewRequestInfo(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested viewRequestInfo service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insertSeekInfoDetails.spring")
	public Result insertSeekInfoDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, SQLInsertionException, IllegalAccessException, InvocationTargetException, InvalidStatusException, InformationNotFoundException {
		LOGGER.info("******* The control inside of the insertSeekInfoDetails in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getFromId()!=null  && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getFromDeptId()!=null && employeeTicketRequest.getMessage()!=null && employeeTicketRequest.getTicketSeekInforequestId()!=null && employeeTicketRequest.getToId()!=null && employeeTicketRequest.getToDeptId()!=null && employeeTicketRequest.getToType()!=null){
			result = employeeTicketServiceImpl.insertSeekInfoDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested viewRequestInfo service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/closeTicket.spring")
	public Result closeTicket(@NonNull EmployeeTicketRequest employeeTicketRequest) throws Exception {
		LOGGER.info("******* The control inside of the closeTicket in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getFromDeptId()!=null &&  employeeTicketRequest.getFromId()!=null && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getRequestUrl()!=null){
			result = employeeTicketServiceImpl.closeTicket(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested viewRequestInfo service.");
			throw new InSufficeientInputException(errorMsgs);
		}
	  return result;
	 }
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reOpenTicket.spring")
	public Result reOpenTicket(@NonNull EmployeeTicketRequest employeeTicketRequest) throws Exception {
		LOGGER.info("******* The control inside of the closeTicket in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null  &&  employeeTicketRequest.getFromId()!=null && employeeTicketRequest.getFromType()!=null && employeeTicketRequest.getRequestUrl()!=null){
			result = employeeTicketServiceImpl.reOpenTicket(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested viewRequestInfo service.");
			throw new InSufficeientInputException(errorMsgs);
		}
	  return result; 
	 }
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/makeAsPublic.spring")
	public Result makeAsPublic(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException {
		LOGGER.info("******* The control inside of the closeTicket in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getFileInfos()!=null && employeeTicketRequest.getRequestUrl()!=null){
			result = employeeTicketServiceImpl.makeAsPublic(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested viewRequestInfo service.");
			throw new InSufficeientInputException(errorMsgs);
		}
	  return result; 
	 }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/chatSubmit.spring")
	public Result chatSubmit(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, SQLInsertionException, IllegalAccessException, InvocationTargetException {
		LOGGER.info("***** The control is inside the chatSubmit service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getMessage()!=null && employeeTicketRequest.getCustomerId()!=null ) {
			result =  employeeTicketServiceImpl.chatSubmit(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given chatSubmit");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insertExtendEsacalationTime.spring")
	public Result insertExtendEsacalationTime(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, SQLInsertionException, InterruptedException, ExecutionException {
		LOGGER.info("***** The control is inside the extendEscalationTime service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if(employeeTicketRequest.getTicketId()!=null && employeeTicketRequest.getExtendedEscalationTime()!=null && employeeTicketRequest.getEmployeeId()!=null && employeeTicketRequest.getMessage()!=null) {
			result =  employeeTicketServiceImpl.insertExtendEsacalationTime(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given insertExtendEsacalationTime");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getExtendEsacalationTimeDetails.spring")
	public Result getExtendEsacalationTimeDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, SQLInsertionException {
		LOGGER.info("***** The control is inside the getExtendEsacalationTimeDetails service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if( employeeTicketRequest.getEmployeeId()!=null) {
			result =  employeeTicketServiceImpl.getExtendEsacalationTimeDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given getExtendEsacalationTimeDetails");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateExtendEsacalationTimeDetailsStatus.spring")
	public Result updateExtendEsacalationTimeDetailsStatus(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, SQLInsertionException {
		LOGGER.info("***** The control is inside the updateExtendEsacalationTimeDetailsStatus service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getEmployeeId()) && Util.isNotEmptyObject(employeeTicketRequest.getTicketId()) && Util.isNotEmptyObject(employeeTicketRequest.getTicketExtendedEscalationApprovalId()) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl())){
			result =  employeeTicketServiceImpl.updateExtendEsacalationTimeDetailsStatus(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for updateExtendEsacalationTimeDetailsStatus");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getSystemEscalatedTicketDetails.spring")
	public Result getSystemEscalatedTicketDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, InvalidStatusException{
		LOGGER.info("***** The control is inside the getSystemEscalatedTicketDetails service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if( Util.isNotEmptyObject(employeeTicketRequest.getEmployeeId()) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl())){
			result =  employeeTicketServiceImpl.getSystemEscalatedTicketDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for getSystemEscalatedTicketDetails");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getEmployeeDetails.spring")
	public Result getEmployeeDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException{
		LOGGER.info("***** The control is inside the getSystemEscalatedTicketDetails service in  EmployeeTicketRestService *******");
		Result result = new Result();
		//if( Util.isNotEmptyObject(employeeTicketRequest.getEmployeeName()) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl())){
		if(Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl())){	
			result =  employeeTicketServiceImpl.getEmployeeDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for getEmployeeDetails");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insertEmployeeLeaveDetails.spring")
	public Result insertEmployeeLeaveDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, InvalidStatusException, SQLInsertionException{
		LOGGER.info("***** The control is inside the getSystemEscalatedTicketDetails service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if( Util.isNotEmptyObject(employeeTicketRequest.getEmployeeId()) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl()) && Util.isNotEmptyObject(employeeTicketRequest.getStartDate()) && Util.isNotEmptyObject(employeeTicketRequest.getEndDate()) /*&& Util.isNotEmptyObject(employeeTicketRequest.getRejoinDate())*/ && Util.isNotEmptyObject(employeeTicketRequest.getApprovedBy())){
			result =  employeeTicketServiceImpl.insertEmployeeLeaveDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for insertEmployeeLeaveDetails");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/isEmployeeAvailable.spring")
	public Result isEmployeeAvailable(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, InvalidStatusException, SQLInsertionException{
		LOGGER.info("***** The control is inside the getSystemEscalatedTicketDetails service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if((Util.isNotEmptyObject(employeeTicketRequest.getEmployeeId()) || Util.isNotEmptyObject(employeeTicketRequest.getDepartmentId())) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl())){
			result =  employeeTicketServiceImpl.isEmployeeAvailable(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			/*result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());*/
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for isEmployeeAvailable");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}

    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTicketEscaltionDtls.spring")
	public Result getTicketEscaltionDtls(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, InvalidStatusException, SQLInsertionException{
		LOGGER.info("**** The control is inside the getTicketEscaltionDtls service in  EmployeeTicketRestService ******");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getTicketEscalationId())){
			List<TicketEscalationResponse> ticketEscalationResponses = employeeTicketServiceImpl.getTicketEscaltionDtls(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			if(Util.isNotEmptyObject(ticketEscalationResponses)){
			result.setTicketEscalationResponses(ticketEscalationResponses);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
			}else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Insuffient input is given for getTicketEscaltionDtls");
				throw new InSufficeientInputException(errorMsgs);
			}
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for getTicketEscaltionDtls");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}	
    @POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/changeTicketOwnerDropDown.spring")
	public Result changeTicketOwnerDropDown(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, InvalidStatusException, SQLInsertionException{
		LOGGER.info("***** The control is inside the changeTicketOwnerDropDown service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getTicketIds())){
			result = employeeTicketServiceImpl.changeTicketOwnerDropDown(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for changeTicketOwnerDropDown");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/changeTicketOwner.spring")
	public Result changeTicketOwner(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InformationNotFoundException, InvalidStatusException, SQLInsertionException{
		LOGGER.info("***** The control is inside the changeTicketOwner service in  EmployeeTicketRestService *******");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getSessionKey()) && Util.isNotEmptyObject(employeeTicketRequest.getTicketIds()) 
		 && Util.isNotEmptyObject(employeeTicketRequest.getDepartmentId()) 
				&& Util.isNotEmptyObject(employeeTicketRequest.getEmpDetailsId())){
			Integer updatedRows =  employeeTicketServiceImpl.changeTicketOwner(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			if(updatedRows>0) {
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			}else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("Change Ticket Owner not updated successfully (Insuffient input is given for changeTicketOwner)");
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
				result.setErrors(errorMsgs);
			}	
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for changeTicketOwner");
			throw new InSufficeientInputException(errorMsgs);
		}
	    return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getExtendedEscalationTimeApprovalLevel.spring")
	public Result getExtendedEscalationTimeApprovalLevel (@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException {
		LOGGER.info("***** The control is inside the getExtendedEscalationTimeApprovalLevel service in  employeeTicketRestService *******");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getTicketId()) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl())){
			result = employeeTicketServiceImpl.getExtendedEscalationTimeApprovalLevel(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given for getExtendedEscalationTimeApprovalLevel");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getChangeTicketTypeMailDetails.spring")
	public Result getChangeTicketTypeMailDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws FileNotFoundException, IOException {
		LOGGER.info(" **** The control is inside the getChangeTicketTypeMailDetails service in  employeeTicketRestService *****");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getTicketId()) && Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl())){
			result = employeeTicketServiceImpl.getChangeTicketTypeMailDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sendChangeTicketTypeMail.spring")
	public Result sendChangeTicketTypeMail(@NonNull ChangeTicketType ChangeTicketTypeRequest){
		LOGGER.info(" **** The control is inside the sendChangeTicketTypeMail service in  employeeTicketRestService *****");
		Result result = new Result();
		result = employeeTicketServiceImpl.sendChangeTicketTypeMail(ChangeTicketTypeRequest);
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/remindAgainAction.spring")
	public Result changeTicketTypeRemindAgain(@NonNull ChangeTicketType changeTicketTypeRequest){
	 LOGGER.info(" **** The control is inside the changeTicketTypeRemindMail service in  employeeTicketRestService *****");
	  Result result = new Result();
	  if(Util.isNotEmptyObject(changeTicketTypeRequest.getTicketId())){
		  result = employeeTicketServiceImpl.changeTicketTypeRemindAgain(changeTicketTypeRequest);  
	  }
	  result.setResponseCode(HttpStatus.success.getResponceCode());
	  result.setDescription(HttpStatus.success.getDescription());
	  return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/actionChangeTicketType.spring")
	public Result changeTicketTypeAction(@NonNull ChangeTicketType changeTicketTypeRequest){
		 LOGGER.info(" **** The control is inside the changeTicketTypeAction service in  employeeTicketRestService *****");
		 Result result = new Result();
		 if(Util.isNotEmptyObject(changeTicketTypeRequest.getTicketId()) && Util.isNotEmptyObject(changeTicketTypeRequest.getChangeTicketTypeAction()) && Util.isNotEmptyObject(changeTicketTypeRequest.getEmployeeId())){
			try {
				result = employeeTicketServiceImpl.changeTicketTypeAction(changeTicketTypeRequest);
			} catch (Exception ex) {
				LOGGER.error(ex);
				result.setResponseCode(HttpStatus.CHANGE_TICKETTYPE_UNAVIALABLE.getResponceCode());
				result.setDescription(HttpStatus.CHANGE_TICKETTYPE_UNAVIALABLE.getDescription());
				return result;
			}
		 }
		 return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCustomerTicketList.spring")
	public Result getCustomerTicketList(@NonNull EmployeeTicketRequest employeeTicketRequest) throws Exception {
		LOGGER.info("******* The control inside of the getCustomerTicketList in  EmployeeTicketRestService ********");
		Result result = new Result();
		List<TicketReportingPojo> TicketReportingPojoList= employeeTicketServiceImpl.getCustomerTicketList(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
		result.setResponseObjList(TicketReportingPojoList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveTicketsComplaint.spring")
	public Result saveTicketsComplaint(@NonNull EmployeeTicketRequest employeeTicketRequest) throws Exception {
		LOGGER.info("******* The control inside of the saveTicketsComplaint in  EmployeeTicketRestService ********");
		return employeeTicketServiceImpl.saveTicketsComplaint(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTicketComplaintList.spring")
	public Result getTicketComplaintList(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getTicketComplaintList  in  EmployeeTicketRestService ********");
		Result result = new Result();
		if (employeeTicketRequest.getEmployeeId() != null && employeeTicketRequest.getSiteIds() != null && employeeTicketRequest.getDepartmentId()!=null && employeeTicketRequest.getRequestUrl()!=null) {
			Result employeeTicketResponse = employeeTicketServiceImpl.getTicketComplaintList(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
		    result.setResponseObjList(employeeTicketResponse);
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
	@Path("/getTicketAdditionalDetails.spring")
	public Result getTicketAdditionalDetails(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InvalidStatusException, InterruptedException, ExecutionException {
		LOGGER.info("******* The control inside of the getTicketAdditionalDetails  in  EmployeeTicketRestService ********");
		Result result = new Result();
		result = employeeTicketServiceImpl.getTicketAdditionalDetails(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getTicketCountList.spring")
	public Result getTicketCountList(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InvalidStatusException, InterruptedException, ExecutionException, InSufficeientInputException {	
		LOGGER.info("******* The control inside of the getTicketCountList  in  EmployeeTicketRestService ********");
		Result result = null;
		if (employeeTicketRequest.getEmployeeId() != null && employeeTicketRequest.getSiteIds() != null && employeeTicketRequest.getDepartmentId()!=null && employeeTicketRequest.getRequestUrl()!=null) {
		    result = employeeTicketServiceImpl.getTicketCountList(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
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
	@Path("/getClosedTicketList.spring")
	public Result getClosedTicketList(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getClosedTicketList  in  EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getEmployeeId() != null && employeeTicketRequest.getSiteIds() != null && employeeTicketRequest.getDepartmentId()!=null && employeeTicketRequest.getRequestEnum()!=null) {
		    EmployeeTicketResponse employeeTicketResponse = employeeTicketServiceImpl.getClosedTicketList(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseObjList(employeeTicketResponse);
		    result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/getTicketPendingDeptDtls.spring")
	public Result getTicketPendingDeptDtls(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getTicketPendingDeptDtls in EmployeeTicketRestService ********");
		Result result = new Result();
		if(employeeTicketRequest.getEmployeeId() != null && employeeTicketRequest.getDepartmentId()!=null && employeeTicketRequest.getRoleId()!=null) {
		    EmployeeTicketResponse employeeTicketResponse = employeeTicketServiceImpl.getTicketPendingDeptDtls(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseObjList(employeeTicketResponse);
		    result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/getCrmEmployees.spring")
	public Result getCrmEmployees(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getCrmEmployees in EmployeeTicketRestService ********");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getType())) {
			List<EmployeeDetailsMailPojo>  crmdetails = employeeTicketServiceImpl.getCrmEmployees(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseObjList(crmdetails);
		    result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/createTicketTypeDetailsForCRM.spring")
	public Result createTicketTypeDetailsForCRM(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getTicketPendingDeptDtls in EmployeeTicketRestService ********");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest.getTicketTypeRequestList())) {
		    EmployeeTicketResponse employeeTicketResponse = employeeTicketServiceImpl.createTicketTypeDetailsForCRM(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseObjList(employeeTicketResponse);
			if (employeeTicketResponse.getResponseCode() != 200) {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(employeeTicketResponse.getDescription());
			} else {
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(employeeTicketResponse.getDescription());
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
	@Path("/createTicketEscalationsLevels.spring")
	public Result createTicketEscalationsLevels(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the createTicketEscalationsLevels in EmployeeTicketRestService ********");
		Result result = new Result();
		EmployeeTicketResponse employeeTicketResponse = new    EmployeeTicketResponse();
		if(Util.isNotEmptyObject(employeeTicketRequest)) {
			try {
				if(Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl()) && "EscalationsLevels".equalsIgnoreCase(employeeTicketRequest.getRequestUrl())) {
				employeeTicketResponse = employeeTicketServiceImpl.createTicketEscalationsLevels(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
				}else if(Util.isNotEmptyObject(employeeTicketRequest.getRequestUrl()) && "EscalationsExtLevels".equalsIgnoreCase(employeeTicketRequest.getRequestUrl()))
				{
					employeeTicketResponse = employeeTicketServiceImpl.createTicketEscalationsEcxtentionLevels(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
				}
				result.setResponseObjList(employeeTicketResponse);
				if (employeeTicketResponse.getResponseCode() != 200) {
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(employeeTicketResponse.getDescription());
				} else {
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription(employeeTicketResponse.getDescription());
				}
			} catch (InformationNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setResponseObjList(employeeTicketResponse);
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
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
	@Path("/getEscalationLevel.spring")
	public Result getEscalationLevel(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getEscalationLevel in EmployeeTicketRestService ********");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest)) {
			List<LevelPojo> list = employeeTicketServiceImpl.getEscalationLevel(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseObjList(list);
		    result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/getEscalationLevelEmployees.spring")
	public Result getEscalationLevelEmployees(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the getEscalationLevelEmployees in EmployeeTicketRestService ********");
		Result result = new Result();
		if(Util.isNotEmptyObject(employeeTicketRequest)) {
			List<EmployeeLevelDetailsPojo> list = employeeTicketServiceImpl.getEscalationLevelEmployees(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
			result.setResponseObjList(list);
		    result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
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
	@Path("/createTicketEscalationsEcxtentionLevels.spring")
	public Result createTicketEscalationsEcxtentionLevels(@NonNull EmployeeTicketRequest employeeTicketRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the createTicketEscalationsEcxtentionLevels in EmployeeTicketRestService ********");
		Result result = new Result();
		EmployeeTicketResponse employeeTicketResponse = new    EmployeeTicketResponse();
		if(Util.isNotEmptyObject(employeeTicketRequest)) {
			try {
				employeeTicketResponse = employeeTicketServiceImpl.createTicketEscalationsEcxtentionLevels(mapper.employeeTicketRequestToemployeeTicketRequestInfo(employeeTicketRequest));
				result.setResponseObjList(employeeTicketResponse);
				if (employeeTicketResponse.getResponseCode() != 200) {
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
				} else {
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription(HttpStatus.success.getDescription());
				}
			} catch (InformationNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setResponseObjList(employeeTicketResponse);
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
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
	@Path("/sendTicketPushNotification.spring")
	public Result sendTicketPushNotification(@NonNull TicketPushNotificationRequest TicketPushNotificationRequest) throws InSufficeientInputException, InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {	
		LOGGER.info("******* The control inside of the sendTicketPushNotification in EmployeeTicketRestService ********");
		Result result = new Result();
			final EmployeeFinancialPushNotificationInfo financialPushNotification = new EmployeeFinancialPushNotificationInfo();
			financialPushNotification.setBookingFormId(TicketPushNotificationRequest.getFlatBookingId());
			financialPushNotification.setNotificationTitle("Notification : TicketNo - " + TicketPushNotificationRequest.getTicketId());
			financialPushNotification.setNotificationBody("<b>Message</b> :" +TicketPushNotificationRequest.getNotificationText());
			financialPushNotification.setNotificationDescription("");
			financialPushNotification.setTypeMsg("Sumadhura Ticket Notification");
			financialPushNotification.setSiteId(TicketPushNotificationRequest.getSiteId());
			financialPushNotification.setTicketId(TicketPushNotificationRequest.getTicketId());
			try {
				pushNotificationHelper.sendFinancialStatusNotification(financialPushNotification,null);
			} catch (InformationNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		
		return result;
	}
}