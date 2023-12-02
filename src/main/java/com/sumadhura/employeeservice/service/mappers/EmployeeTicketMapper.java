/**
 * 
 */
package com.sumadhura.employeeservice.service.mappers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.dto.CoApplicant;
import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.dto.CustomerAddress;
import com.sumadhura.employeeservice.dto.CustomerProfileResponse;
import com.sumadhura.employeeservice.dto.CustomerPropertyDetails;
import com.sumadhura.employeeservice.dto.Department;
import com.sumadhura.employeeservice.dto.DepartmentResponse;
import com.sumadhura.employeeservice.dto.EmployeeDetails;
import com.sumadhura.employeeservice.dto.EmployeeTicketRequest;
import com.sumadhura.employeeservice.dto.EmployeeTicketResponse;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.GenericTicketSeekInfo;
import com.sumadhura.employeeservice.dto.TicketComment;
import com.sumadhura.employeeservice.dto.TicketEscalationResponse;
import com.sumadhura.employeeservice.dto.TicketExtendedEscalationApproval;
import com.sumadhura.employeeservice.dto.TicketForwardMenu;
import com.sumadhura.employeeservice.dto.TicketForwardMenuResponse;
import com.sumadhura.employeeservice.dto.TicketResponse;
import com.sumadhura.employeeservice.dto.TicketSeekInfo;
import com.sumadhura.employeeservice.dto.TicketType;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.enums.Type;
import com.sumadhura.employeeservice.enums.Visibility;
import com.sumadhura.employeeservice.exception.InvalidStatusException;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLeaveDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.TicketCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketConversationDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketExtendedEscalationApprovalPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketForwardMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypePojo;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * EmployeeTicketMapper class provides Employee Ticket specific converstions.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 06:00PM
 */

@Component
public class EmployeeTicketMapper {

	private Logger LOGGER = Logger.getLogger(EmployeeTicketMapper.class);

	public EmployeeTicketRequestInfo employeeTicketRequestToemployeeTicketRequestInfo(EmployeeTicketRequest employeeTicketRequest) {
		LOGGER.info("***** The control is inside the employeeToEmployeeInfo in  EmployeeTicketMapper ******");
		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		BeanUtils.copyProperties(employeeTicketRequest, employeeTicketRequestInfo);
		return employeeTicketRequestInfo;
	}
	public EmployeeTicketResponse employeeTicketListToemployeeTicketResponse(
			List<TicketPojo> employeeTicketList) {
		LOGGER.info("***** The control is inside the employeeTicketListToemployeeTicketResponse in  EmployeeTicketMapper ******");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketResponse> ticketResponseList = new ArrayList<TicketResponse>();
		for (TicketPojo pojo : employeeTicketList) {
			TicketResponse response = new TicketResponse();
			BeanUtils.copyProperties(pojo, response);
			ticketResponseList.add(response);
		}
		//employeeTicketResponse.setTicketResponseList(ticketResponseList);
		employeeTicketResponse.setTotalTicketResponseList(ticketResponseList);
		return employeeTicketResponse;
	}

public TicketResponse employeeTicketList$ticketEscalationPojos$customerPropertyDetailsPojos$departmentPojos$ticketTypePojos$EmployeeDetailsPojosToemployeeTicketResponse(
			TicketPojo employeeTicket, /*List<TicketEscalationPojo> ticketEscalationPojos,*/
			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos, List<DepartmentPojo> departmentPojos,
			List<TicketTypePojo> ticketTypePojos,List<EmployeeDetailsPojo> employeeDetailsPojos,List<EmployeePojo> employeePojos,List<EmployeePojo> ticketOwnerPojo) {
		LOGGER.info("***** The control is inside the employeeTicketList$ticketEscalationPojos$customerPropertyDetailsPojos$departmentPojos$ticketTypePojosToemployeeTicketResponse in  EmployeeTicketMapper ******");
		TicketResponse response = new TicketResponse();
		
		/* To set Ticket_Owner_Name */
		if(Util.isNotEmptyObject(ticketOwnerPojo)) {
			response.setTicketOwnerName(Util.isNotEmptyObject(ticketOwnerPojo.get(0))?ticketOwnerPojo.get(0).getEmployeeName():"N/A");
		}
		
		BeanUtils.copyProperties(employeeTicket, response);
		if(Util.isNotEmptyObject(employeeTicket.getDepartmentTicketStatusId()))
		response.setStatus(employeeTicket.getDepartmentTicketStatusId().equals(Status.REPLIED.getStatus())?Status.REPLIED.getDescription():employeeTicket.getDepartmentTicketStatusId().equals(Status.NEW.getStatus())?Status.NEW.getDescription():employeeTicket.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())?Status.OPEN.getDescription():employeeTicket.getDepartmentTicketStatusId().equals(Status.CLOSED.getStatus())?Status.CLOSED.getDescription():Status.INPROGRESS.getDescription());	
		//response.setStatus(employeeTicket.getDepartmentTicketStatusId().equals(12l)?"Open":employeeTicket.getDepartmentTicketStatusId().equals(11l)?"Closed":"Inprogress");
		else
		response.setStatus(employeeTicket.getTicketStatusId().equals(Status.REOPEN.getStatus())?Status.REOPEN.getDescription():employeeTicket.getTicketStatusId().equals(Status.NEW.getStatus())?Status.NEW.getDescription():employeeTicket.getTicketStatusId().equals(Status.OPEN.getStatus())?Status.OPEN.getDescription():employeeTicket.getTicketStatusId().equals(Status.CLOSED.getStatus())?Status.CLOSED.getDescription():Status.INPROGRESS.getDescription());	
		//response.setStatus(employeeTicket.getTicketStatusId().equals(12l)?"Open":employeeTicket.getTicketStatusId().equals(11l)?"Closed":"Inprogress");	
		
		/*List<TicketEscalationResponse> escalationResponses = new ArrayList<TicketEscalationResponse>();
		for (TicketEscalationPojo escalationPojo : ticketEscalationPojos) {
			TicketEscalationResponse escalationResponse = new TicketEscalationResponse();
			BeanUtils.copyProperties(escalationPojo, escalationResponse);
			escalationResponses.add(escalationResponse);
		}*/
		
		CustomerPropertyDetails  customerPropertyDetails = new CustomerPropertyDetails();
		BeanUtils.copyProperties(customerPropertyDetailsPojos.get(0)!=null?customerPropertyDetailsPojos.get(0):new CustomerPropertyDetailsPojo(),customerPropertyDetails);
		
		/*  Pending department for the Closed Ticket is Put Space .*/
		Department department = new Department();
		if(departmentPojos!=null && !departmentPojos.isEmpty()) {
		BeanUtils.copyProperties(departmentPojos.get(0),department);
		}else {
			
			//department.setDepartmentName(employeePojos.get(0)!=null?(employeePojos.get(0).getFirstName()!=null || employeePojos.get(0).getLastName()!=null)?employeePojos.get(0).getFirstName()+" "+employeePojos.get(0).getLastName():"NA":"NA");
			department.setDepartmentName(Util.isNotEmptyObject(employeePojos)?employeePojos.get(0).getEmployeeName():"");
			department.setDepartmentId(employeePojos.get(0)!=null?employeePojos.get(0).getEmployeeId()!=null?employeePojos.get(0).getEmployeeId():0l:0l);
			//department.setDepartmentName(EmployeeDetailsPojos.get(0)!=null?EmployeeDetailsPojos.get(0).getEmployeeName()!=null?EmployeeDetailsPojos.get(0).getEmployeeName():"NA":"NA");
			//department.setDepartmentId(EmployeeDetailsPojos.get(0)!=null?EmployeeDetailsPojos.get(0).getEmployeeId()!=null?EmployeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			//department.setDepartmentId(EmployeeDetailsPojos.get(0)!=null?EmployeeDetailsPojos.get(0).getDepartmentId()!=null?EmployeeDetailsPojos.get(0).getDepartmentId():0l:0l);
		}
		TicketType  ticketType = new TicketType();
		BeanUtils.copyProperties(ticketTypePojos.get(0),ticketType);
	//	response.setTicketEscalationResponses(escalationResponses);
		response.setCustomerPropertyDetails(customerPropertyDetails);
		response.setDepartment(department);
		response.setTicketType(ticketType);
		return response;
		
	}
	public  TicketComment TicketCommentsPojoToTicketComments(TicketCommentsPojo ticketCommentsPojo,List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos) {
		LOGGER.info("***** The control is inside the TicketCommentsPojoToTicketComments in  EmployeeTicketMapper ******");
		TicketComment comment = new TicketComment();
	    BeanUtils.copyProperties(ticketCommentsPojo,comment);
	   /* comment.setName(customerPojos.get(0).getFirstName()+" "+customerPojos.get(0).getLastName());*/
	    
	    if(Util.isNotEmptyObject(customerPropertyDetailsPojos)) {
	    //comment.setName((Util.isNotEmptyObject(customerPojos.get(0).getFirstName())?customerPojos.get(0).getFirstName():"")+(Util.isNotEmptyObject(customerPojos.get(0).getLastName())?customerPojos.get(0).getLastName():""));
		//comment.setProfilepic(Util.isNotEmptyObject(customerPojos.get(0).getProfilePic())?customerPojos.get(0).getProfilePic():new ResponceCodesUtil().getApplicationProperties().get("CUSTOMER_CARE").toString());
		//comment.setName((Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerName())?customerPropertyDetailsPojos.get(0).getCustomerName():""));
		comment.setProfilepic(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerProfilePic())?customerPropertyDetailsPojos.get(0).getCustomerProfilePic():new ResponceCodesUtil().getApplicationProperties().get("CUSTOMER_CARE").toString());
	    }else {
	    	 //comment.setName("N/A");
	    	 comment.setProfilepic(new ResponceCodesUtil().getApplicationProperties().get("CUSTOMER_CARE").toString());
	    }
		return comment;
	}
	public TicketComment TicketCommentsPojoToTicketComments(List<EmployeeDetailsPojo> employeeDetailsPojos,TicketCommentsPojo ticketCommentsPojo,List<EmployeePojo> employeePojoObjs ) {
		LOGGER.info("***** The control is inside the TicketCommentsPojoToTicketComments in  EmployeeTicketMapper ******");
		TicketComment comment = new TicketComment();
	    BeanUtils.copyProperties(ticketCommentsPojo,comment);
	   // comment.setName(!(employeePojoObjs.isEmpty())?employeePojoObjs.get(0).getFirstName()+" "+employeePojoObjs.get(0).getLastName():"");
	    //comment.setName(Util.isNotEmptyObject(employeePojoObjs)?employeePojoObjs.get(0).getEmployeeName():"");
	    //comment.setProfilepic(!(employeePojoObjs.isEmpty())?employeePojoObjs.get(0).getUserProfile():new ResponceCodesUtil().getApplicationProperties().get("CUSTOMER_CARE").toString());
	    comment.setProfilepic(new ResponceCodesUtil().getApplicationProperties().get("CUSTOMER_CARE").toString());
		return comment;
	}
	public TicketComment TicketCommentsPojoToTicketComments(TicketCommentsPojo ticketCommentsPojo,com.sumadhura.employeeservice.enums.Department system) {
		LOGGER.info("***** The control is inside the TicketCommentsPojoToTicketComments in  EmployeeTicketMapper ******");
		TicketComment comment = new TicketComment();
	    BeanUtils.copyProperties(ticketCommentsPojo,comment);
	    comment.setName(system.getName());
		//comment.setProfilepic("http://129.154.74.18:9999/images/sumadhura_projects_images/system.png");
		comment.setProfilepic(new ResponceCodesUtil().getApplicationProperties().get("SYSTEM_IMAGE").toString());
		return comment;
	}

	public TicketResponse TicketPojo$CustomerPropertyDetailsPojo$TicketComment(List<TicketPojo> ticketPojoList,
			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos, List<TicketComment> ticketComments,List<TicketTypePojo> ticketTypePojos,List<DepartmentPojo> departmentPojos,List<EmployeeDetailsPojo> EmployeeDetailsPojos,List<EmployeePojo> employeePojos ) {
		
		LOGGER.info("***** The control is inside the TicketPojo$CustomerPropertyDetailsPojo$TicketComment in  EmployeeTicketMapper ******");
		TicketResponse response = new TicketResponse();
		BeanUtils.copyProperties(ticketPojoList.get(0),response);
		
	    /* setting ticket Status   
		 if(Util.isNotEmptyObject(ticketPojoList.get(0).getDepartmentTicketStatusId()))
			response.setStatus(ticketPojoList.get(0).getDepartmentTicketStatusId().equals(12l)?"Open":ticketPojoList.get(0).getDepartmentTicketStatusId().equals(11l)?"Closed":"Inprogress");
			else
			response.setStatus(ticketPojoList.get(0).getTicketStatusId().equals(12l)?"Open":ticketPojoList.get(0).getTicketStatusId().equals(11l)?"Closed":"Inprogress");	
		*/
		 
		 if(Util.isNotEmptyObject(ticketPojoList.get(0).getDepartmentTicketStatusId()))
		 response.setStatus(ticketPojoList.get(0).getDepartmentTicketStatusId().equals(Status.REPLIED.getStatus())?Status.REPLIED.getDescription():ticketPojoList.get(0).getDepartmentTicketStatusId().equals(Status.NEW.getStatus())?Status.NEW.getDescription():ticketPojoList.get(0).getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())?Status.OPEN.getDescription():ticketPojoList.get(0).getDepartmentTicketStatusId().equals(Status.CLOSED.getStatus())?Status.CLOSED.getDescription():Status.INPROGRESS.getDescription());	
		 else
		 response.setStatus(ticketPojoList.get(0).getTicketStatusId().equals(Status.REOPEN.getStatus())?Status.REOPEN.getDescription():ticketPojoList.get(0).getTicketStatusId().equals(Status.NEW.getStatus())?Status.NEW.getDescription():ticketPojoList.get(0).getTicketStatusId().equals(Status.OPEN.getStatus())?Status.OPEN.getDescription():ticketPojoList.get(0).getTicketStatusId().equals(Status.CLOSED.getStatus())?Status.CLOSED.getDescription():Status.INPROGRESS.getDescription());	
				 
		/* Pending department for the Closed Ticket is Put Space .*/
		Department department = new Department();
		if(departmentPojos!=null && !departmentPojos.isEmpty()) {
		BeanUtils.copyProperties(departmentPojos.get(0),department);
		department.setTypeOf(com.sumadhura.employeeservice.enums.Department.DEPARTMENT.getName());
		}else {
			//department.setDepartmentName(employeePojos.get(0)!=null?(employeePojos.get(0).getFirstName()!=null || employeePojos.get(0).getLastName()!=null)?employeePojos.get(0).getFirstName()+" "+employeePojos.get(0).getLastName():"NA":"NA");
			//department.setDepartmentId(employeePojos.get(0)!=null?employeePojos.get(0).getEmployeeId()!=null?employeePojos.get(0).getEmployeeId():0l:0l);
			department.setDepartmentName(Util.isNotEmptyObject(employeePojos)?employeePojos.get(0).getEmployeeName():"NA");
			department.setDepartmentId(employeePojos.get(0)!=null?employeePojos.get(0).getEmployeeId()!=null?employeePojos.get(0).getEmployeeId():0l:0l);
			department.setTypeOf(com.sumadhura.employeeservice.enums.Department.EMPLOYEE.getName());
		}
		TicketType ticketType= new TicketType();
		BeanUtils.copyProperties(ticketTypePojos.get(0),ticketType);
		
		CustomerPropertyDetails  customerPropertyDetails = new CustomerPropertyDetails();
		BeanUtils.copyProperties(Util.isNotEmptyObject(customerPropertyDetailsPojos)?Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0))?customerPropertyDetailsPojos.get(0):new CustomerPropertyDetailsPojo():new CustomerPropertyDetailsPojo() ,customerPropertyDetails);
		
		response.setCustomerPropertyDetails(customerPropertyDetails);
		response.setDepartment(department);
		response.setTicketComments(ticketComments);
		response.setTicketType(ticketType);
		return response;
	}

	public CustomerProfileResponse customerPojos$applicantAddressPojos$coApplicants(List<CustomerPojo> customerPojos, List<CustomerAddressPojo> applicantAddressPojos,List<CoApplicant> coApplicants) {
		
		LOGGER.info("***** The control is inside the customerPojos$applicantAddressPojos$coApplicantPojos$coApplicantAddressPojos in  EmployeeTicketMapper ******");
		
		CustomerProfileResponse customerProfileResponse = new CustomerProfileResponse();
		Customer customer = new Customer();
		CustomerAddress customerAddress = new CustomerAddress();
		
		BeanUtils.copyProperties(customerPojos.get(0), customer);
		BeanUtils.copyProperties(applicantAddressPojos.get(0), customerAddress);
		customer.setCustomerAddress(customerAddress);
		
		customerProfileResponse.setCustomer(customer);
		customerProfileResponse.setCoApplicants(coApplicants);
	
		return customerProfileResponse;
	}

	public CoApplicant CoApplicantPojo$CustomerAddressPojo(CoApplicantPojo pojo,List<CustomerAddressPojo> coApplicantAddressPojos) {
		LOGGER.info("***** The control is inside the CoApplicantPojo$CustomerAddressPojo in  EmployeeTicketMapper ******");
		CoApplicant coApplicant = new CoApplicant();
		BeanUtils.copyProperties(pojo, coApplicant);
		CustomerAddress customerAddress = new CustomerAddress();
		BeanUtils.copyProperties(coApplicantAddressPojos.get(0), customerAddress);
		coApplicant.setCustomerAddress(customerAddress);
		return coApplicant;
	}

	public TicketForwardMenu ticketForwardMenuPojoToTicketForwardMenu(TicketForwardMenuPojo pojo,@SuppressWarnings("rawtypes") List ItemList) {
		LOGGER.info("***** The control is inside the ticketForwardMenuPojoToTicketForwardMenu in  EmployeeTicketMapper ******");
		TicketForwardMenu menu = new TicketForwardMenu();
		Object item = ItemList.get(0);
		BeanUtils.copyProperties(pojo, menu);
		if(item instanceof DepartmentPojo)
		menu.setItem(((DepartmentPojo)(ItemList.get(0))).getDepartmentName());
		else if(item instanceof EmployeePojo)
		menu.setItem(((EmployeePojo)(ItemList.get(0))).getEmployeeName());	
		return menu;
	}

	public TicketForwardMenuResponse ticketForwardMenusToTicketForwardMenuResponse(List<TicketForwardMenu> ticketForwardMenus) {
	  LOGGER.info("***** The control is inside the ticketForwardMenuPojoToTicketForwardMenu in  EmployeeTicketMapper ******");
	  TicketForwardMenuResponse ticketForwardMenuResponse = new TicketForwardMenuResponse();
	  ticketForwardMenuResponse.setTicketForwardMenuList(ticketForwardMenus);
	  return ticketForwardMenuResponse;
	}
	public DepartmentResponse departmentPojosToDepartmentResponse(List<DepartmentPojo> departmentPojos) {
		LOGGER.info("***** The control is inside the departmentPojosToDepartmentResponse in  EmployeeTicketMapper ******");
		DepartmentResponse departmentResponse = new DepartmentResponse(); 
		List<Department> departments = new ArrayList<Department>();
		
		for(DepartmentPojo pojo : departmentPojos) {
			Department department = new Department();
		    BeanUtils.copyProperties(pojo, department);
		    departments.add(department);
		}
		departmentResponse.setDepartmentList(departments);
		return departmentResponse;
	}
	public GenericTicketSeekInfo departmentSpecificTicketSeekInfo(List<TicketSeekInfoPojo> departmentSpecificTicketSeekInfo,EmployeeTicketRequestInfo employeeTicketRequestInfo,List<DepartmentPojo> departmentPojos ) throws InvalidStatusException {
	LOGGER.info("***** The control is inside the departmentSpecificTicketSeekInfo in  EmployeeTicketMapper ******");
	
		GenericTicketSeekInfo genericTicketSeekInfo = new GenericTicketSeekInfo();
		if (Util.isNotEmptyObject(departmentSpecificTicketSeekInfo)) {
			Set<Long> requestIdSet = new HashSet<Long>();
			for (TicketSeekInfoPojo pojo : departmentSpecificTicketSeekInfo) {
				requestIdSet.add(pojo.getTicketSeekInfoRequestId());
			}
			LOGGER.debug("***** The requestId is *****"+requestIdSet);
			/* removing nulls from requestIdSet */
			CollectionUtils.filter(requestIdSet, PredicateUtils.notNullPredicate());

			if (requestIdSet.size() <= 1) {
				List<TicketSeekInfo> infos = new ArrayList<TicketSeekInfo>();
				for (TicketSeekInfoPojo pojo : departmentSpecificTicketSeekInfo) {
					
					/* host chat */
					if (pojo.getFromId().equals(employeeTicketRequestInfo.getFromId())
							/*&& pojo.getFromDeptId().equals(employeeTicketRequestInfo.getFromDeptId())*/) {
						TicketSeekInfo info = new TicketSeekInfo();
						info.setTypeOfMessage(Type.HOST.getName());
						BeanUtils.copyProperties(pojo, info);
						infos.add(info);
					}
					/* guest chat */
					else {
						TicketSeekInfo info = new TicketSeekInfo();
						info.setTypeOfMessage(Type.GUEST.getName());
						BeanUtils.copyProperties(pojo, info);
						infos.add(info);
					}
				}
				genericTicketSeekInfo.setTicketSeekInfo(infos);
				genericTicketSeekInfo.setId(((DepartmentPojo)CollectionUtils.get(departmentPojos,0)).getDepartmentId());
				genericTicketSeekInfo.setName(((DepartmentPojo)CollectionUtils.get(departmentPojos,0)).getDepartmentName());
				genericTicketSeekInfo.setRequestId(Util.isNotEmptyObject(requestIdSet)?(Long)CollectionUtils.get(requestIdSet,0):0l);
				genericTicketSeekInfo.setType(com.sumadhura.employeeservice.enums.Department.DEPARTMENT.getId());
				return genericTicketSeekInfo;
			} else {
				/* Invalid Service request Id exception */
				List<String> errorMsg = new ArrayList<String>();
				errorMsg.add("*** Invalid Request Id is found for TicketSeekInfo ***");
				throw new InvalidStatusException(errorMsg);
			}
		}
		return genericTicketSeekInfo;
	}
	
	public GenericTicketSeekInfo employeeSpecificTicketSeekInfo(List<TicketSeekInfoPojo> employeeSpecificTicketSeekInfo,
			EmployeeTicketRequestInfo employeeTicketRequestInfo, List<EmployeeDetailsPojo> employeeDetailsPojos,List<EmployeePojo> employeePojos) throws InvalidStatusException {
		LOGGER.info("***** The control is inside the departmentSpecificTicketSeekInfo in  EmployeeTicketMapper ******");
		GenericTicketSeekInfo genericTicketSeekInfo = new GenericTicketSeekInfo();
		if (Util.isNotEmptyObject(employeeSpecificTicketSeekInfo)) {
			Set<Long> requestIdSet = new HashSet<Long>();
			for (TicketSeekInfoPojo pojo : employeeSpecificTicketSeekInfo) {
				requestIdSet.add(pojo.getTicketSeekInfoRequestId());
			}
			/* removing nulls from requestIdSet */
			CollectionUtils.filter(requestIdSet, PredicateUtils.notNullPredicate());

			if (requestIdSet.size() <= 1) {
				List<TicketSeekInfo> infos = new ArrayList<TicketSeekInfo>();
				for (TicketSeekInfoPojo pojo : employeeSpecificTicketSeekInfo) {
					
					/* host chat */
					if (pojo.getFromId().equals(employeeTicketRequestInfo.getFromId())
							/*&& pojo.getFromDeptId().equals(employeeTicketRequestInfo.getFromDeptId())*/) {
						TicketSeekInfo info = new TicketSeekInfo();
						//info.setEmployeeName(Util.isNotEmptyObject(employeePojos)?employeePojos.get(0).getFirstName()+" "+employeePojos.get(0).getLastName():"NA");
						info.setEmployeeName(Util.isNotEmptyObject(employeePojos)?employeePojos.get(0).getEmployeeName():"NA");
						info.setEmployeeProfilePic(Util.isNotEmptyObject(employeePojos)?employeePojos.get(0).getUserProfile():"NA");
						info.setTypeOfMessage(Type.HOST.getName());
						BeanUtils.copyProperties(pojo, info);
						infos.add(info);
					}
					/* guest chat */
					else {
						TicketSeekInfo info = new TicketSeekInfo();
						info.setEmployeeName(Util.isNotEmptyObject(employeePojos)?employeePojos.get(0).getEmployeeName():"NA");
						info.setEmployeeProfilePic(Util.isNotEmptyObject(employeePojos)?employeePojos.get(0).getUserProfile():"NA");
						info.setTypeOfMessage(Type.GUEST.getName());
						BeanUtils.copyProperties(pojo, info);
						infos.add(info);
					}
				}
				genericTicketSeekInfo.setTicketSeekInfo(infos);
				genericTicketSeekInfo.setId(((EmployeeDetailsPojo)CollectionUtils.get(employeeDetailsPojos,0)).getEmployeeId());
				genericTicketSeekInfo.setName((((EmployeePojo)CollectionUtils.get(employeePojos,0))).getEmployeeName());
				genericTicketSeekInfo.setRequestId(Util.isNotEmptyObject(requestIdSet)?(Long)CollectionUtils.get(requestIdSet,0):0l);
				genericTicketSeekInfo.setType(com.sumadhura.employeeservice.enums.Department.EMPLOYEE.getId());
				return genericTicketSeekInfo;
			} else {
				/* Invalid Service request Id exception */
				List<String> errorMsg = new ArrayList<String>();
				errorMsg.add("*** Invalid Request Id is found for TicketSeekInfo ***");
				throw new InvalidStatusException(errorMsg);
			}
		}
		return genericTicketSeekInfo;
	}

	public TicketResponse ticketPojoToticketEscalationPojolIST(TicketPojo ticketPojo /*,List<TicketEscalationPojo> ticketEscalationPojolIST*/) {
		LOGGER.info("***** The control is inside the ticketPojoToticketEscalationPojolIST in  EmployeeTicketMapper ******");
		TicketResponse response = new TicketResponse();
		/*List<TicketEscalationResponse> escalationResponses = new ArrayList<TicketEscalationResponse>();*/
		BeanUtils.copyProperties(ticketPojo, response);
		/*for (TicketEscalationPojo escalationPojo : ticketEscalationPojolIST) {
			TicketEscalationResponse escalationResponse = new TicketEscalationResponse();
			BeanUtils.copyProperties(escalationPojo, escalationResponse);
			escalationResponses.add(escalationResponse);
		}*/
		response.setStatus(ticketPojo.getTicketStatusId().equals(12l)?"Open":ticketPojo.getTicketStatusId().equals(11l)?"Closed":"Inprogress");
		/*response.setTicketEscalationResponses(escalationResponses);*/
		LOGGER.debug("**** The TicketResponse object *****"+response);
		return response;
	}

	public TicketPojo employeeTicketRequestInfoToTicketPojoForClose(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the employeeTicketRequestInfoToTicketPojoForClose in  EmployeeTicketMapper ******");
		TicketPojo pojo = new TicketPojo();
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setDepartmentTicketStatusId(Status.CLOSED.getStatus());
		pojo.setTicketStatusId(Status.CLOSED.getStatus());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setResolvedDate(new Timestamp(new Date().getTime()));
		pojo.setStatusUpdateBy(employeeTicketRequestInfo.getFromId());
		pojo.setStatusUpdateType(employeeTicketRequestInfo.getFromType());
		pojo.setEstimatedResolvedDateStatus(null);
		LOGGER.debug("***** The TicketPojo object is ******"+pojo);
		return pojo;
	}
	public TicketPojo employeeTicketRequestInfoToTicketPojoForReOpen(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the employeeTicketRequestInfoToTicketPojoForReOpen in  EmployeeTicketMapper ******");
		TicketPojo pojo = new TicketPojo();
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setDepartmentTicketStatusId(null);
		pojo.setTicketStatusId(Status.REOPEN.getStatus());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setResolvedDate(null);
		pojo.setStatusUpdateBy(employeeTicketRequestInfo.getFromId());
		pojo.setStatusUpdateType(employeeTicketRequestInfo.getFromType());
		//pojo.setStatusUpdateType(com.sumadhura.employeeservice.enums.Department.CUSTOMER.getId());
		LOGGER.debug("***** The TicketPojo object is ******"+pojo);
		return pojo;
	}
	
	public TicketPojo employeeTicketRequestInfoToTicketPojoForOpen(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the employeeTicketRequestInfoToTicketPojoForReOpen in  EmployeeTicketMapper ******");
		TicketPojo pojo = new TicketPojo();
		if(employeeTicketRequestInfo.getGeneric().equalsIgnoreCase("highlevel")){
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setDepartmentTicketStatusId(null);
		pojo.setTicketStatusId(Status.OPEN.getStatus());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setResolvedDate(null);
		pojo.setStatusUpdateBy(employeeTicketRequestInfo.getFromId());
		pojo.setStatusUpdateType(employeeTicketRequestInfo.getFromType());
		//pojo.setStatusUpdateType(com.sumadhura.employeeservice.enums.Department.CUSTOMER.getId());
		LOGGER.debug("***** The TicketPojo object is ******"+pojo);
		return pojo;
		}else {
			pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
			pojo.setDepartmentTicketStatusId(Status.OPEN.getStatus());
			pojo.setTicketStatusId(Status.INPROGRESS.getStatus());
			pojo.setStatusId(Status.ACTIVE.getStatus());
			pojo.setModifiedDate(new Timestamp(new Date().getTime()));
			pojo.setResolvedDate(null);
			pojo.setStatusUpdateBy(employeeTicketRequestInfo.getFromId());
			pojo.setStatusUpdateType(employeeTicketRequestInfo.getFromType());
			//pojo.setStatusUpdateType(com.sumadhura.employeeservice.enums.Department.CUSTOMER.getId());
			LOGGER.debug("***** The TicketPojo object is ******"+pojo);
			return pojo;
		}
	}
	
	public TicketPojo employeeTicketRequestInfoToTicketPojoForUpdate(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("***** The control is inside the employeeTicketRequestInfoToTicketPojo in  EmployeeTicketMapper ******");
		TicketPojo pojo = new TicketPojo();
		if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("updateTicketConversation.spring") || employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("seekInfoTicketDetails.spring") )) {
		if(employeeTicketRequestInfo.getGeneric().equalsIgnoreCase("highlevel")){
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setDepartmentTicketStatusId(null);
		pojo.setTicketStatusId(Status.INPROGRESS.getStatus());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setResolvedDate(null);
		pojo.setStatusUpdateBy(employeeTicketRequestInfo.getFromId());
		pojo.setStatusUpdateType(employeeTicketRequestInfo.getFromType());
		LOGGER.debug("***** The TicketPojo object is ******"+pojo);
		}else if(employeeTicketRequestInfo.getGeneric().equalsIgnoreCase("Invidual")) {
			pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
			pojo.setDepartmentTicketStatusId(Status.INPROGRESS.getStatus());
			pojo.setTicketStatusId(Status.INPROGRESS.getStatus());
			pojo.setStatusId(Status.ACTIVE.getStatus());
			pojo.setModifiedDate(new Timestamp(new Date().getTime()));
			pojo.setResolvedDate(null);
			pojo.setStatusUpdateBy(employeeTicketRequestInfo.getFromId());
			pojo.setStatusUpdateType(employeeTicketRequestInfo.getFromType());
			LOGGER.debug("***** The TicketPojo object is ******"+pojo);
		}
	}
		return pojo;
	}

	public List<TicketConversationDocumentsPojo> employeeTicketRequestInfoToTicketConversationDocumentsPojo(@NonNull
			EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		
		LOGGER.info("***** The control is inside the employeeTicketRequestInfoToTicketConversationDocumentsPojo in  EmployeeTicketMapper ******");
		List<TicketConversationDocumentsPojo> conversationDocumentsPojos = new ArrayList<TicketConversationDocumentsPojo>();
		/* getting public fileNames from  FileInfo */
		Set<Integer> ticketConversationDocumetIds = new HashSet<Integer>();
		if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFileInfos())) {
			for(FileInfo fileInfo : employeeTicketRequestInfo.getFileInfos()) {
				ticketConversationDocumetIds.add(fileInfo.getId());
			}
			LOGGER.debug("*** The Ticket Conversation DocumentIds are ***"+ticketConversationDocumetIds);
			/*  Adding TicketConversationDocumentId specific Public FileNames to  the  TicketConversation Table */
			for( Integer id : ticketConversationDocumetIds) {
				TicketConversationDocumentsPojo ticketConversationDocumentsPojo= new TicketConversationDocumentsPojo();
				  StringBuilder files = new StringBuilder();
				for(FileInfo fileInfo : employeeTicketRequestInfo.getFileInfos()) {
					if(Util.isNotEmptyObject(fileInfo.getId()) && Util.isNotEmptyObject(fileInfo.getName()) && fileInfo.getId().equals(id)) {
						files.append(fileInfo.getName()+",");
					}
				}
				LOGGER.debug("*** The files is ***"+files);
				ticketConversationDocumentsPojo.setTicketConversationDocumentId(id.longValue());
				ticketConversationDocumentsPojo.setModifiedDate(new Timestamp(new Date().getTime()));
				//ticketConversationDocumentsPojo.setPublicDocuments(Util.isNotEmptyObject(files.toString())?StringUtils.substring(files.toString(), 0, files.toString().length() - 1):"");
				ticketConversationDocumentsPojo.setPublicDocuments(Util.isNotEmptyObject(files.toString())?files.toString():"");
				ticketConversationDocumentsPojo.setStatusId(Status.ACTIVE.getStatus());
				conversationDocumentsPojos.add(ticketConversationDocumentsPojo);
				LOGGER.debug("**** The conversationDocumentsPojos ****"+conversationDocumentsPojos);
			}
		}
		return conversationDocumentsPojos;
	}
	
	public TicketResponse ticketPojoToTicketResponse(List<TicketPojo> ticketPojoslIST) {
		LOGGER.info("***** The control is inside the ticketPojoToTicketResponse in  EmployeeTicketMapper ******");
		TicketResponse response = new TicketResponse();
		if(Util.isNotEmptyObject(ticketPojoslIST)) {
			LOGGER.debug("*** The ticketPojoslIST object is ****"+ticketPojoslIST);
			BeanUtils.copyProperties(ticketPojoslIST.get(0), response);
		}
		return response;
	}

	public TicketComment TicketCommentsPojoToTicketComments(TicketCommentsPojo ticketCommentsPojo) {
		LOGGER.info("***** The control is inside the TicketCommentsPojoToTicketComments in  EmployeeTicketMapper ******");
		TicketComment comment = new TicketComment();
		if(Util.isNotEmptyObject(ticketCommentsPojo)) {
		 BeanUtils.copyProperties(ticketCommentsPojo,comment);
		}
		return comment;
	}
	
	public TicketCommentsPojo employeeTicketRequestInfoToTicketCommentsPojo(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The control is inside the CustomerTicketRequestInfoToTicketCommentsPojo in CustomerTicketingMapper *****");
		TicketCommentsPojo pojo = new TicketCommentsPojo();
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setDepartmentId(null);
		pojo.setFromId(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())?employeeTicketRequestInfo.getCustomerId():0l);
		pojo.setFromType(com.sumadhura.employeeservice.enums.Department.CUSTOMER.getId());
		pojo.setToId(Util.isNotEmptyObject(employeeTicketRequestInfo.getEmployeeId())?employeeTicketRequestInfo.getEmployeeId():0l);
		pojo.setToType(com.sumadhura.employeeservice.enums.Department.EMPLOYEE.getId());
		pojo.setMessage(Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())?employeeTicketRequestInfo.getMessage():"NA");
		pojo.setCommentsDate(new Timestamp(new Date().getTime()));
		pojo.setVisibleType(Visibility.PUBLIC.getDescription());
		pojo.setDocumentLocation("NA");
		pojo.setStatusId(Status.ACTIVE.status);
		pojo.setTicketConversationDocumentId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketConversationDocumentId())?employeeTicketRequestInfo.getTicketConversationDocumentId():0l);
		pojo.setFromDeptId(null);
		pojo.setToDeptId(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId())?employeeTicketRequestInfo.getDepartmentId():0l);
		LOGGER.debug("***** The TicketCommentsPojo is *****"+pojo);
		return pojo;
	}
	public TicketExtendedEscalationApprovalPojo employeeTicketRequestInfoToTicketExtendedEscalationApprovalPojo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, List<TicketPojo> ticketPojos) {
		LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToTicketExtendedEscalationApprovalPojo in CustomerTicketingMapper *****");
		
		TicketExtendedEscalationApprovalPojo pojo = new TicketExtendedEscalationApprovalPojo();
		/*pojo.setId(id);*/
		pojo.setTicketId(employeeTicketRequestInfo.getTicketId());
		pojo.setCurrentEscalationDateTimestamp(Util.isNotEmptyObject(ticketPojos)?Util.isNotEmptyObject(ticketPojos.get(0).getEstimatedResolvedDate())?ticketPojos.get(0).getEstimatedResolvedDate():null:null);
		pojo.setExtendedEscalationDate(employeeTicketRequestInfo.getExtendedEscalationTime());
		pojo.setRequestedBy(employeeTicketRequestInfo.getEmployeeId());
		/*pojo.setRequestedTo(Util.isNotEmptyObject(employeeTicketRequestInfo.getToId())?employeeTicketRequestInfo.getToId():0l); */
		pojo.setTicketEscalationExtenstionApprovalLevelMappingId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketEscalationExtenstionApprovalLevelMappingId())?employeeTicketRequestInfo.getTicketEscalationExtenstionApprovalLevelMappingId():0l);
		pojo.setApprovedStatus(Status.NOTAPPROVED.getStatus());
		pojo.setComments(employeeTicketRequestInfo.getMessage());
		pojo.setCreatedDate(new Timestamp(new Date().getTime()));
		pojo.setModifiedDate(null);
		pojo.setStatus(Status.ACTIVE.getStatus());
		LOGGER.debug("**** The TicketExtendedEscalationApprovalPojo object is *****"+pojo);
		return pojo;
	}
	public EmployeeTicketResponse ticketExtendedEscalationApprovalPojosToticketExtendedEscalationApproval(List<TicketExtendedEscalationApprovalPojo> ticketExtendedEscalationApprovalPojos) {
		LOGGER.info("**** The Control is inside the ticketExtendedEscalationApprovalPojosToticketExtendedEscalationApproval in EmployeeTicketMapper *****");
		EmployeeTicketResponse response = new EmployeeTicketResponse();
		List<TicketExtendedEscalationApproval> ticketExtendedEscalationApprovals = new ArrayList<TicketExtendedEscalationApproval>();
		for(TicketExtendedEscalationApprovalPojo pojo : ticketExtendedEscalationApprovalPojos) {
			TicketExtendedEscalationApproval escalationApproval = new TicketExtendedEscalationApproval();
			BeanUtils.copyProperties(pojo, escalationApproval);
			ticketExtendedEscalationApprovals.add(escalationApproval);
		}
		response.setEscalationApprovals(ticketExtendedEscalationApprovals);
		LOGGER.debug("**** The  EmployeeTicketResponse Object is *****"+response);
		return response;
	}
	public TicketExtendedEscalationApprovalPojo employeeTicketRequestInfoToTicketExtendedEscalationApprovalPojo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToTicketExtendedEscalationApprovalPojo in EmployeeTicketMapper *****");
		
		TicketExtendedEscalationApprovalPojo pojo = new TicketExtendedEscalationApprovalPojo();
		pojo.setApprovedStatus(employeeTicketRequestInfo.getTicketExtendedEscalationApprovalStatusId());
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setId(employeeTicketRequestInfo.getTicketExtendedEscalationApprovalId());
		pojo.setTicketId(employeeTicketRequestInfo.getTicketId());
		pojo.setStatus(Status.ACTIVE.getStatus());
		/* if it is approval we will update aapoval date otherwise no need */
		if(employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("approval")) {
		pojo.setApprovedEscalationDate(employeeTicketRequestInfo.getApprovedEscalationDate());
		}
		pojo.setApprovedBy(employeeTicketRequestInfo.getEmployeeId());
		LOGGER.debug("**** The TicketExtendedEscalationApprovalPojo object is *****"+pojo);
		return pojo;
	}
	public TicketPojo employeeTicketRequestInfoToTicketPojo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToTicketPojo in EmployeeTicketMapper *****");
		TicketPojo pojo = new TicketPojo();
		if(Util.isNotEmptyObject(employeeTicketRequestInfo.getApprovedEscalationDate()))
		pojo.setEstimatedResolvedDate(employeeTicketRequestInfo.getApprovedEscalationDate());
		pojo.setEstimatedResolvedDateStatus(null);
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setTicketId(employeeTicketRequestInfo.getTicketId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}
	public TicketPojo employeeTicketRequestInfoToTicketPojoSystemEscalationUpdateEstimatedResolvedDate(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToTicketPojo in EmployeeTicketMapper *****");
		TicketPojo pojo = new TicketPojo();
		
		/* Adding  hours */
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
	    calendar.add(Calendar.HOUR_OF_DAY, employeeTicketRequestInfo.getExtendedTime().intValue());
		Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
		
		pojo.setEstimatedResolvedDate(timestamp);
		pojo.setEstimatedResolvedDateStatus(null);
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setTicketId(employeeTicketRequestInfo.getTicketId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		return pojo;
	}
	public TicketPojo EmployeeTicketRequestInfoToticketPojoForEstimatedResolvedDateStatus(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside the EmployeeTicketRequestInfoToticketPojoForEstimatedResolvedDateStatus in EmployeeTicketMapper ****");
		TicketPojo pojo = new TicketPojo();
		pojo.setEstimatedResolvedDateStatus(Status.ESCALATED.getStatus());
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setTicketId(employeeTicketRequestInfo.getTicketId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		LOGGER.debug("***** The TicketPojo is ******"+pojo);
		return pojo;
	}
	public TicketEscalationPojo employeeTicketRequestInfoToticketEscalationPojoForSystemEscalation(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
       LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToticketEscalationPojo in EmployeeTicketMapper *****");
       TicketEscalationPojo ticketEscalationPojo = new TicketEscalationPojo();
       ticketEscalationPojo.setTicketId(employeeTicketRequestInfo.getTicketId());
       ticketEscalationPojo.setComments(Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())?employeeTicketRequestInfo.getMessage():"N/A");
       ticketEscalationPojo.setEscalationBy(com.sumadhura.employeeservice.enums.Department.SYSTEM.getName());
       ticketEscalationPojo.setEscalationById(null);
       ticketEscalationPojo.setEscalationTo(Util.isNotEmptyObject(employeeTicketRequestInfo.getEscalationTo())?employeeTicketRequestInfo.getEscalationTo():0l);
       ticketEscalationPojo.setEscalationDate(new Timestamp(new Date().getTime()));
       ticketEscalationPojo.setModifiedDate(null);
       ticketEscalationPojo.setCreatedDate(new Timestamp(new Date().getTime()));
       ticketEscalationPojo.setStatus(Status.ACTIVE.getStatus());
       ticketEscalationPojo.setMailOtpApproval(((Integer)(Util.generateOTP())).toString());
       ticketEscalationPojo.setActionEmployeeId(employeeTicketRequestInfo.getActionEmployeeId());
       ticketEscalationPojo.setTicketEscalationLevelMappingId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketEscalationLevelMappingId())?employeeTicketRequestInfo.getTicketEscalationLevelMappingId():0l);
       ticketEscalationPojo.setAssignedDate(new Timestamp(new Date().getTime()));
       LOGGER.debug("**** The TicketEscalationPojo is ****"+ticketEscalationPojo);
	   return ticketEscalationPojo;
	}
	public TicketEscalationResponse ticketEscalationPojoToTicketEscalationResponse(@NonNull TicketEscalationPojo ticketEscalationPojo) {
		LOGGER.info("**** The Control is inside the ticketEscalationPojoToTicketEscalationResponse in EmployeeTicketMapper *****");
		TicketEscalationResponse ticketEscalationResponse = new TicketEscalationResponse();
		BeanUtils.copyProperties(ticketEscalationPojo, ticketEscalationResponse);
		LOGGER.debug("*** The ticketEscalationResponse object is ***"+ticketEscalationResponse);
		return ticketEscalationResponse;
	}
	public TicketCommentsPojo employeeTicketRequestInfoToTicketCommentsPojoForSystemEscalation(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToTicketCommentsPojoForSystemEscalation in EmployeeTicketMapper *****");
		TicketCommentsPojo pojo = new TicketCommentsPojo();
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setDepartmentId(null);
		pojo.setFromId(Util.isNotEmptyObject(employeeTicketRequestInfo.getCustomerId())?employeeTicketRequestInfo.getCustomerId():0l);
		pojo.setFromType(com.sumadhura.employeeservice.enums.Department.SYSTEM.getId());
		pojo.setToId(Util.isNotEmptyObject(employeeTicketRequestInfo.getToId())?employeeTicketRequestInfo.getToId():0l);
		pojo.setToType(com.sumadhura.employeeservice.enums.Department.EMPLOYEE.getId());
		pojo.setMessage(Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())?employeeTicketRequestInfo.getMessage():"NA");
		pojo.setCommentsDate(new Timestamp(new Date().getTime()));
		pojo.setVisibleType(Visibility.PRIVATE.getDescription());
		pojo.setDocumentLocation("NA");
		pojo.setStatusId(Status.ACTIVE.status);
		pojo.setTicketConversationDocumentId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketConversationDocumentId())?employeeTicketRequestInfo.getTicketConversationDocumentId():0l);
		pojo.setFromDeptId(null);
		pojo.setToDeptId(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId())?employeeTicketRequestInfo.getDepartmentId():0l);
		LOGGER.debug("***** The TicketCommentsPojo is *****"+pojo);
		return pojo;
	}
	
	public TicketCommentsPojo employeeTicketRequestInfoToTicketCommentsPojoForChangeTicketType(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToTicketCommentsPojoForSystemEscalation in EmployeeTicketMapper *****");
		TicketCommentsPojo pojo = new TicketCommentsPojo();
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setDepartmentId(null);
		pojo.setFromId(0l);
		pojo.setFromType(com.sumadhura.employeeservice.enums.Department.SYSTEM.getId());
		pojo.setToId(Util.isNotEmptyObject(employeeTicketRequestInfo.getToId())?employeeTicketRequestInfo.getToId():0l);
		pojo.setToType(com.sumadhura.employeeservice.enums.Department.CUSTOMER.getId());
		pojo.setMessage(Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())?employeeTicketRequestInfo.getMessage():"NA");
		pojo.setCommentsDate(new Timestamp(new Date().getTime()));
		pojo.setVisibleType(Visibility.PUBLIC.getDescription());
		pojo.setDocumentLocation("NA");
		pojo.setStatusId(Status.ACTIVE.status);
		pojo.setTicketConversationDocumentId(0l);
		pojo.setFromDeptId(0l);
		pojo.setToDeptId(0l);
		LOGGER.debug("***** The TicketCommentsPojo is *****"+pojo);
		return pojo;
	}
	
	public EmployeeLeaveDetailsPojo employeeTicketRequestInfoToEmployeeLeaveDetailsPojo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		  LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToEmployeeLeaveDetailsPojo in EmployeeTicketMapper *****");
		  EmployeeLeaveDetailsPojo employeeLeaveDetailsPojo = new EmployeeLeaveDetailsPojo();
		  //employeeLeaveDetailsPojo.setEmployeeLeaveDetailsId(null);
		    employeeLeaveDetailsPojo.setEmployeeId(Util.isNotEmptyObject(employeeTicketRequestInfo.getEmployeeId())?employeeTicketRequestInfo.getEmployeeId():0l);
		    employeeLeaveDetailsPojo.setStartDate(employeeTicketRequestInfo.getStartDate());
		    employeeLeaveDetailsPojo.setEndDate(employeeTicketRequestInfo.getEndDate());
		    employeeLeaveDetailsPojo.setRejoinDate(Util.isNotEmptyObject(employeeTicketRequestInfo.getRejoinDate())?employeeTicketRequestInfo.getRejoinDate():null);
		    employeeLeaveDetailsPojo.setApprovedBy(employeeTicketRequestInfo.getApprovedBy());
		    employeeLeaveDetailsPojo.setComments(Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())?employeeTicketRequestInfo.getMessage():"N/A");
		    employeeLeaveDetailsPojo.setStatus(Status.ACTIVE.getStatus());
		    employeeLeaveDetailsPojo.setCreatedDate(new Timestamp(new Date().getTime()));
		    employeeLeaveDetailsPojo.setModifiedDate(null);
		  LOGGER.debug("**** The employeeLeaveDetailsPojo is ****"+employeeLeaveDetailsPojo);
		  return employeeLeaveDetailsPojo;
	}
	public EmployeeDetails employeeDetailsPojosToEmployeeDetails(List<EmployeeDetailsPojo> employeeDetailsPojos,EmployeePojo employeePojo) {
		LOGGER.info("**** The Control is inside the employeeDetailsPojosToEmployeeDetails in EmployeeTicketMapper *****");
		//EmployeeTicketResponse response = new EmployeeTicketResponse();
		//List<EmployeeDetails> employeeDetailsList = new ArrayList<EmployeeDetails>();
		  EmployeeDetails employeeDetails = new EmployeeDetails(); 
		for(EmployeeDetailsPojo pojo : employeeDetailsPojos) {
		       BeanUtils.copyProperties(pojo, employeeDetails);
		       employeeDetails.setEmployeeName(employeePojo.getEmployeeName()+"-"+pojo.getEmployeeDesignation());
		      // employeeDetailsList.add(employeeDetails);
		}
		LOGGER.debug("**** The employeeDetails object is ****"+employeeDetails);
		//response.setEmployeeDetailsList(employeeDetailsList);
		//LOGGER.debug("**** The EmployeeTicketResponse object is ****"+response);
		return employeeDetails;
	}
	
	public EmployeeTicketResponse employeeDetailsPojosToEmployeeDetails(List<EmployeeDetailsPojo> employeeDetailsPojos) {
		LOGGER.info("**** The Control is inside the employeeDetailsPojosToEmployeeDetails in EmployeeTicketMapper *****");
		EmployeeTicketResponse response = new EmployeeTicketResponse();
		List<EmployeeDetails> employeeDetailsList = new ArrayList<EmployeeDetails>();
		for(EmployeeDetailsPojo pojo : employeeDetailsPojos) {
			 EmployeeDetails employeeDetails = new EmployeeDetails(); 
		     BeanUtils.copyProperties(pojo, employeeDetails);
		     employeeDetailsList.add(employeeDetails);
		}
		LOGGER.debug("**** The employeeDetails object is ****"+employeeDetailsList);
		response.setEmployeeDetailsList(employeeDetailsList);
		LOGGER.debug("**** The EmployeeTicketResponse object is ****"+response);
		return response;
	}
	
	public TicketStatisticsPojo ticketPojosToTicketStatisticsPojo(@NonNull TicketPojo pojo) {
    LOGGER.info("**** The control is inside the ticketPojosToTicketStatisticsPojo in EmployeeTicketMapper ****");
    TicketStatisticsPojo ticketStatisticsPojo = new TicketStatisticsPojo();
    ticketStatisticsPojo.setTicketId(Util.isNotEmptyObject(pojo.getTicketId())?pojo.getTicketId():0l);
    ticketStatisticsPojo.setDepartmentId(Util.isNotEmptyObject(pojo.getDepartmentId())?pojo.getDepartmentId():0l);
    ticketStatisticsPojo.setAssignmentTo(Util.isNotEmptyObject(pojo.getAssignmentTo())?pojo.getAssignmentTo():0l);
    ticketStatisticsPojo.setAssignedBy(Util.isNotEmptyObject(pojo.getAssignedBy())?pojo.getAssignedBy():0l);
    ticketStatisticsPojo.setAssignedDate(Util.isNotEmptyObject(pojo.getAssignedDate())?pojo.getAssignedDate():null);
    ticketStatisticsPojo.setEstimatedResolvedDate(Util.isNotEmptyObject(pojo.getEstimatedResolvedDate())?pojo.getEstimatedResolvedDate():null);
    ticketStatisticsPojo.setEstimatedResolvedDateStatus(Util.isNotEmptyObject(pojo.getEstimatedResolvedDateStatus())?pojo.getEstimatedResolvedDateStatus():0l);	
    ticketStatisticsPojo.setResolvedDate(Util.isNotEmptyObject(pojo.getResolvedDate())?pojo.getResolvedDate():null);
    ticketStatisticsPojo.setStatusUpdatedBy(Util.isNotEmptyObject(pojo.getStatusUpdateBy())?pojo.getStatusUpdateBy():0l);
    ticketStatisticsPojo.setStatusUpdatedType(Util.isNotEmptyObject(pojo.getStatusUpdateType())?pojo.getStatusUpdateType():0l);
    ticketStatisticsPojo.setTicketStatus(Util.isNotEmptyObject(pojo.getTicketStatusId())?pojo.getTicketStatusId():0l);
    ticketStatisticsPojo.setInvidualTicketStatus(Util.isNotEmptyObject(pojo.getDepartmentTicketStatusId())?pojo.getDepartmentTicketStatusId():0l);
    ticketStatisticsPojo.setStatus(Util.isNotEmptyObject(pojo.getStatusId())?pojo.getStatusId():0l);
    ticketStatisticsPojo.setCreatedDate(new Timestamp(new Date().getTime()));
    ticketStatisticsPojo.setTicketTypeId(Util.isNotEmptyObject(pojo.getTicketTypeId())?pojo.getTicketTypeId():0l);
    ticketStatisticsPojo.setTicketTypeDetailsId(Util.isNotEmptyObject(pojo.getTicketTypeDetailsId())?pojo.getTicketTypeDetailsId():0l);
		if (Util.isNotEmptyObject(pojo.getRating())) {
			ticketStatisticsPojo.setRating(
					Util.isNotEmptyObject(Long.parseLong(pojo.getRating())) ? Long.parseLong(pojo.getRating()) : 0l);
		}else
		{
			ticketStatisticsPojo.setRating(0l);
		}
   
    ticketStatisticsPojo.setFeedbackDesc(Util.isNotEmptyObject(pojo.getFeedbackDesc())?pojo.getFeedbackDesc():"N/A");
    ticketStatisticsPojo.setTicketTypeChangeRequest(Util.isNotEmptyObject(pojo.getTicketTypeChangeRequest())?pojo.getTicketTypeChangeRequest():0l);
    ticketStatisticsPojo.setModifiedDate(pojo.getModifiedDate());
    if(Util.isNotEmptyObject(pojo.getComplaintCreatedBy()) || Util.isNotEmptyObject(pojo.getComplaintCreatedDate())) {
    	ticketStatisticsPojo.setComplaintStatus(pojo.getComplaintStatus());
    	ticketStatisticsPojo.setComplaintCreatedBy(pojo.getComplaintCreatedBy());
    	ticketStatisticsPojo.setComplaintCreatedDate(pojo.getComplaintCreatedDate());
    }
    ticketStatisticsPojo.setFlatBookingId(pojo.getFlatBookingId());
    ticketStatisticsPojo.setOldBookingId(pojo.getOldBookingId());
    LOGGER.debug("**** The TicketStatisticsPojo object is ****"+ticketStatisticsPojo);
    return ticketStatisticsPojo;
	}
	public TicketPojo employeeTicketRequestInfoToTicketPojoForTicketExtendedEscalation(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside the employeeTicketRequestInfoToTicketPojo in EmployeeTicketMapper *****");
		TicketPojo pojo = new TicketPojo();
		/* Adding 24 hours */
	    Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
	    calendar.add(Calendar.HOUR, 24);
		Timestamp timestamp = new Timestamp(calendar.getTimeInMillis()); 
		
		pojo.setEstimatedResolvedDate(timestamp);
		//pojo.setEstimatedResolvedDateStatus(null); //previos code
		/*bvr */
		/* upadting estimated_resolved_date_status column as escalated in ticket table */
		pojo.setEstimatedResolvedDateStatus(Status.ESCALATED.getStatus());
		pojo.setModifiedDate(new Timestamp(new Date().getTime()));
		pojo.setTicketId(employeeTicketRequestInfo.getTicketId());
		pojo.setStatusId(Status.ACTIVE.getStatus());
		 LOGGER.debug("**** The TicketPojo object is ****"+pojo);
		return pojo;
	}
	
	public TicketEscalationPojo employeeTicketInfoToTicketEscalationPojo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The control is inside the employeeTicketInfoToTicketEscalationPojo in EmployeeTicket Mapper  ****");
		TicketEscalationPojo pojo = new TicketEscalationPojo();
		pojo.setTicketEscalationId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketEscalationId())?employeeTicketRequestInfo.getTicketEscalationId():0l);
		pojo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		pojo.setStatus(Status.ACTIVE.getStatus());
		pojo.setActionEmployeeId(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())?employeeTicketRequestInfo.getFromId():0l);
		LOGGER.debug("**** The TicketEscalationPojo obj is ****"+pojo);
		return pojo;
	}
	
	public CustomerPropertyDetails CustomerPropertyDetailsPojoToCustomerPropertyDetails(@NonNull CustomerPropertyDetailsPojo customerPropertyDetailsPojo) {
		LOGGER.info("*** The control is inside the CustomerPropertyDetailsPojoToCustomerPropertyDetails in EmployeeTicket Mapper ****");
		CustomerPropertyDetails customerPropertyDetails = new CustomerPropertyDetails();
		 BeanUtils.copyProperties(customerPropertyDetailsPojo, customerPropertyDetails);
		 LOGGER.debug("**** The customerPropertyDetails obj is *****"+customerPropertyDetails);
		 return customerPropertyDetails;
	}
	
	public List<TicketResponse> ticketPojosToTicketResponses(List<TicketPojo> ticketPojoslIST) {
		LOGGER.info("***** The control is inside the ticketPojosToTicketResponses in  EmployeeTicketMapper ******");
		List<TicketResponse> responses = new ArrayList<TicketResponse>();
		for(TicketPojo pojo  : ticketPojoslIST) {
		TicketResponse response = new TicketResponse();
		if(Util.isNotEmptyObject(pojo)) {
			BeanUtils.copyProperties(pojo,response);
		}
		responses.add(response);
	 }
		return responses;
	}
    
  public List<EmployeeDetails> employeePojoListToemployeeDetailsList(List<EmployeeDetailsPojo> employeeDetailsPojoList) {
		LOGGER.info("***** The control is inside the employeePojoListToemployeeDetailsList in  EmployeeTicketMapper ******");
		List<EmployeeDetails> employeeDetailsList = new ArrayList<EmployeeDetails>();
		for(EmployeeDetailsPojo employeeDetailsPojo  : employeeDetailsPojoList) {
			EmployeeDetails employeeDetails = new EmployeeDetails();
			if(Util.isNotEmptyObject(employeeDetailsPojo)) {
				BeanUtils.copyProperties(employeeDetailsPojo,employeeDetails);
			}
			employeeDetailsList.add(employeeDetails);
		 }
			return employeeDetailsList;
	}
	public List<TicketEscalationResponse> employeeTicketEscaltionPojoListToemployeeTicketEscalationResponseList(List<TicketEscalationPojo> ticketEscalationPojoList) {
		LOGGER.info("***** The control is inside the employeeTicketEscaltionPojoListToemployeeTicketEscalationResponseList in  EmployeeTicketMapper ******");
		List<TicketEscalationResponse> ticketEscalationResponseList = new ArrayList<TicketEscalationResponse>();
		for(TicketEscalationPojo ticketEscalationPojo  : ticketEscalationPojoList) {
			TicketEscalationResponse ticketEscalationResponse = new TicketEscalationResponse();
			if(Util.isNotEmptyObject(ticketEscalationPojo)) {
				BeanUtils.copyProperties(ticketEscalationPojo,ticketEscalationResponse);
			}
			ticketEscalationResponseList.add(ticketEscalationResponse);
		 }
			return ticketEscalationResponseList;
	}
	public ChangeTicketType employeeRequestInfoToChangeTicketTypeResponce(final EmployeeTicketRequestInfo employeeTicketRequestInfo) {
	LOGGER.info("***** The control is inside the employeeRequestInfoToChangeTicketTypeResponce in  EmployeeTicketMapper ******");
	ChangeTicketType changeTicketTypeResponce = new ChangeTicketType();	
	changeTicketTypeResponce.setTo(StringUtils.join(employeeTicketRequestInfo.getMails(),","));
	changeTicketTypeResponce.setCc(StringUtils.join(employeeTicketRequestInfo.getCcMails(),","));
	changeTicketTypeResponce.setSubject(employeeTicketRequestInfo.getTicketSubject());
	changeTicketTypeResponce.setEmployeeName(employeeTicketRequestInfo.getEmployeeName());
	changeTicketTypeResponce.setTicketId(employeeTicketRequestInfo.getTicketId());
	changeTicketTypeResponce.setRaisedUnderCategory(employeeTicketRequestInfo.getTicketType());
	changeTicketTypeResponce.setDescription(employeeTicketRequestInfo.getMessage());
	changeTicketTypeResponce.setCategoryToBeChanged("N/A");
	changeTicketTypeResponce.setMessageBody(employeeTicketRequestInfo.getTemplateContent());
	changeTicketTypeResponce.setTicketTypeId(employeeTicketRequestInfo.getTicketTypeId());
	return changeTicketTypeResponce;
	}
	
	public TicketResponse employeeTicketPojoToTicketResponse(TicketPojo ticketPojo) {
		LOGGER.info("***** The control is inside the employeeTicketPojoToTicketResponse in  EmployeeTicketMapper ******");
		TicketResponse response = new TicketResponse();
		BeanUtils.copyProperties(ticketPojo, response);
		response.setRating(Util.isNotEmptyObject(ticketPojo.getRating())?ticketPojo.getRating():"-");
		/* To set Ticket_Owner_Name */
		response.setTicketOwnerName(Util.isNotEmptyObject(ticketPojo.getTicketOwner())?ticketPojo.getTicketOwner():"N/A");
		
		/* setting ticket status */
		
		/*
		if(Util.isNotEmptyObject(employeeTicket.getEstimatedResolvedDateStatus()) && employeeTicket.getEstimatedResolvedDateStatus().equals(Status.ESCALATED.getStatus())){
		response.setStatus(Status.ESCALATED.getDescription());
		}else {	*/
		if(Util.isNotEmptyObject(ticketPojo.getDepartmentTicketStatusId())) {
		response.setStatus(ticketPojo.getDepartmentTicketStatusId().equals(Status.REPLIED.getStatus())?Status.REPLIED.getDescription():ticketPojo.getDepartmentTicketStatusId().equals(Status.NEW.getStatus())?Status.NEW.getDescription():ticketPojo.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())?Status.OPEN.getDescription():ticketPojo.getDepartmentTicketStatusId().equals(Status.CLOSED.getStatus())?Status.CLOSED.getDescription():Status.INPROGRESS.getDescription());	
		}
		else {
		response.setStatus(ticketPojo.getTicketStatusId().equals(Status.REOPEN.getStatus())?Status.REOPEN.getDescription():ticketPojo.getTicketStatusId().equals(Status.NEW.getStatus())?Status.NEW.getDescription():ticketPojo.getTicketStatusId().equals(Status.OPEN.getStatus())?Status.OPEN.getDescription():ticketPojo.getTicketStatusId().equals(Status.CLOSED.getStatus())?Status.CLOSED.getDescription():Status.INPROGRESS.getDescription());	
		}
		/* } */
		
		/* Setting Customer Property Details */
		CustomerPropertyDetails  customerPropertyDetails = new CustomerPropertyDetails();
		customerPropertyDetails.setCustomerName(ticketPojo.getCustName());
		customerPropertyDetails.setCustomerId(ticketPojo.getCustId());
		customerPropertyDetails.setFlatBookingId(ticketPojo.getFlatBookingId());
		customerPropertyDetails.setSiteName(ticketPojo.getSiteName());
		customerPropertyDetails.setFlatNo(ticketPojo.getFlatNo());
		
		/*  Pending department for the Closed Ticket is Put Space .*/
		Department department = new Department();
		department.setDepartmentName(Util.isNotEmptyObject(ticketPojo.getPendingDeptEmp())?ticketPojo.getPendingDeptEmp():"");
		department.setDepartmentId(0l);
		
		/* setting Ticket Type Details */
		TicketType  ticketType = new TicketType();
		ticketType.setTicketType(ticketPojo.getTicketType());
		ticketType.setTicketTypeId(ticketPojo.getTicketTypeId());
		
		/* Setting Ticket Closed By */
		response.setTicketClosedBy(ticketPojo.getClosedBy());
		
		response.setCustomerPropertyDetails(customerPropertyDetails);
		response.setDepartment(department);
		response.setTicketType(ticketType);
		return response;
	}
	
}