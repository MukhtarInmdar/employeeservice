/**
 * 
 */
package com.sumadhura.employeeservice.rest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sumadhura.employeeservice.dto.ExcelObject;
import com.sumadhura.employeeservice.dto.MessengerRequest;
import com.sumadhura.employeeservice.service.MessengerService;
import com.sumadhura.employeeservice.util.Util;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.persistence.dto.InboxSiteDetails;

import lombok.NonNull;

/**
 * MessengerRestService class provides  ChatBot specific services.
 * 
 * @author Venkat_Koniki
 * @since 25.08.2020
 * @time 10:37AM
 */

@Path("/messenger")
public class MessengerRestService {
	@Resource
	@Qualifier("MessengerServiceImpl")
	private MessengerService messengerServiceImpl;
	
	private static final Logger LOGGER = Logger.getLogger(MessengerRestService.class);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMessagesList.spring")
	public Result getMessagesList(@NonNull MessengerRequest request) {
		LOGGER.info("******* The control inside of the getMessagesList  in  MessengerRestService ******");
		Result result = new Result();
		//if(Util.isEmptyObject(request.getStartDate()) || Util.isEmptyObject(request.getEndDate())){
			messengerServiceImpl.dateWiseSearchFunctionalityUtil(request);
		//}
		result = messengerServiceImpl.getMessagesList(request);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/startNewChat.spring")
	public Result startNewChat(@NonNull MessengerRequest request) throws InSufficeientInputException {
		LOGGER.info("******* The control inside of the startNewChat  in  MessengerRestService ******");
		Result result = new Result();
		if (Util.isNotEmptyObject(request.getFlatIds())) {
			result = messengerServiceImpl.startNewChat(request);
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
	@Path("/chatSubmit.spring")
	public Result chatSubmit(@NonNull MessengerRequest request) throws InSufficeientInputException {
		LOGGER.info("******* The control inside of the chatSubmit  in  MessengerRestService ****** getRequestUrl "+request.getRequestUrl());
		Result result = new Result();
		/* Excel sheet executed code */
		if (Util.isNotEmptyObject(request.getRequestType())&& request.getRequestType().equalsIgnoreCase("RegistrationProcess")) {
			HashSet<String> flatset = new HashSet<String>();
			if (Util.isEmptyObject(request.getEmployeeIds())) {
				List<Long> empIds = new ArrayList<Long>();
				request.setEmployeeIds(empIds);
			}
			List<ExcelObject> list = request.getExcelObject();
			for (ExcelObject excelObject : list) {
				if(Util.isEmptyObject(excelObject.getFiled_A()) 
					||Util.isEmptyObject(excelObject.getFiled_B())
					||Util.isEmptyObject(excelObject.getFiled_C())
					||Util.isEmptyObject(excelObject.getFiled_D())
					||Util.isEmptyObject(excelObject.getFiled_E())
					||Util.isEmptyObject(excelObject.getFiled_F())
					||Util.isEmptyObject(excelObject.getFiled_G())
					||Util.isEmptyObject(excelObject.getFiled_H())
					||Util.isEmptyObject(excelObject.getFiled_I())
					)
				{
			    result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setResponseObjList("The file you have upload is an  incorrect format.Please check all the columns names and it's type, and do re-upload.");
				return result;
				}
			}
			for (ExcelObject excelObject : list) {
				/*
				 * creating request Object and repleacing static content with dynamic values
				 * coming from excel Object
				 */
				messengerServiceImpl.createRequestObject(excelObject, request);
				if (Util.isNotEmptyObject(request.getSubject()) && Util.isNotEmptyObject(request.getMessage())
						&& Util.isNotEmptyObject(request.getFlatBookingId())
						&& Util.isNotEmptyObject(request.getCreatedById())
						&& Util.isNotEmptyObject(request.getCreatedByType())
						&& Util.isNotEmptyObject(request.getSendTo()) && Util.isNotEmptyObject(request.getSendType())
						&& Util.isNotEmptyObject(request.getChatMsgWithoutTags())
						&& "notempty".equals(request.getFlatStatus())) {
					result = messengerServiceImpl.chatSubmit(request);
				}else
				{
					flatset.add(Util.isNotEmptyObject(excelObject.getFiled_A())?excelObject.getFiled_A():"-");
				}
			}
			if (Util.isNotEmptyObject(flatset)) {
				result.setResponseObjList("Message/chat didn't send to the following flats.."
						+ flatset.toString().replace("[", "").replace("]", ""));
			}
			else {
				result.setResponseObjList(HttpStatus.success.getDescription());
			}
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		} else {
			if (Util.isNotEmptyObject(request.getSubject()) && Util.isNotEmptyObject(request.getMessage())
					&& Util.isNotEmptyObject(request.getFlatBookingId())
					&& Util.isNotEmptyObject(request.getCreatedById())
					&& Util.isNotEmptyObject(request.getCreatedByType()) && Util.isNotEmptyObject(request.getSendTo())
					&& Util.isNotEmptyObject(request.getSendType())) {
				result = messengerServiceImpl.chatSubmit(request);
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("The Insufficient Input is given for requested service.");
				throw new InSufficeientInputException(errorMsgs);
			}
		}

		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getChatDetails.spring")
	public Result getChatDetails(@NonNull MessengerRequest request) throws InSufficeientInputException{
		LOGGER.info("******* The control inside of the getChatDetails  in  MessengerRestService ******");
		Result result = new Result();
		if(Util.isNotEmptyObject(request.getRecipientType()) 
				&& Util.isNotEmptyObject(request.getRecipientId()) 
				&& Util.isNotEmptyObject(request.getMessengerId())) {
			result = messengerServiceImpl.getChatDetails(request);
		}else{
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUnviewChatCount.spring")
	public Result getUnviewChatCount(@NonNull MessengerRequest request) throws InSufficeientInputException{
		LOGGER.info("******* The control inside of the getUnviewChatCount  in  MessengerRestService ******");
		Result result = new Result();
		if(Util.isNotEmptyObject(request.getRecipientType()) 
				&& Util.isNotEmptyObject(request.getRecipientId()) 
			){
			result = messengerServiceImpl.getUnviewChatCount(request);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else{
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getEmployeeDropDown.spring")
	public Result getEmployeeDropDown(@NonNull MessengerRequest request) throws InSufficeientInputException{
		LOGGER.info("******* The control inside of the getEmployeeDropDown  in  MessengerRestService ******");
		    Result result = new Result();
			result = messengerServiceImpl.getEmployeeDropDown(request);
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		    return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateViewStatusAsUnread.spring")
	public Result updateMessengerViewStatusAsUnread(@NonNull MessengerRequest request) throws InSufficeientInputException{
		LOGGER.info("******* The control inside of the updateMessengerViewStatusAsUnread  in  MessengerRestService ******");
		Result result = new Result();
		if(Util.isNotEmptyObject(request.getRecipientType()) && Util.isNotEmptyObject(request.getRecipientId()) 
			&& Util.isNotEmptyObject(request.getMessengerConversationId())) {
			Integer count = messengerServiceImpl.updateMessengerViewStatusAsUnread(request);
			if(Util.isNotEmptyObject(count)) {
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			}else {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
			}
		}else{
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("The Insufficient Input is given for requested service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return result;
	}
	/**
	 * @Description This service will return the site id ,site name of cc and non cc emplyees
	 * @param messengerRequest
	 * @return
	 * @throws Exception
	 * @author Bvr
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/sitesForInbox.spring")
	public Result getSiteList(MessengerRequest messengerRequest) {
		LOGGER.info("******* The control inside of the getSiteList  in  MessengerRestService ******");
		List<InboxSiteDetails> siteList = messengerServiceImpl.getSiteList(messengerRequest);
		Result result=new Result();
		result.setResponseObjList(siteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
	    return 	result;
	}

	
}