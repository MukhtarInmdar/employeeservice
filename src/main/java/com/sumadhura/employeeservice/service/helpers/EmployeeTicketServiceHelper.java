/**
 * 
 */
package com.sumadhura.employeeservice.service.helpers;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

//import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.enums.Visibility;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.persistence.dao.BookingFormServiceDao;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;
import com.sumadhura.employeeservice.persistence.dao.LoginDao;
import com.sumadhura.employeeservice.persistence.dto.CustBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.DepartmentRoleMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDepartmentMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.FlatPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketConversationDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelEmployeeMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypePojo;
import com.sumadhura.employeeservice.service.MailService;
import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.service.dto.LoginInfo;
import com.sumadhura.employeeservice.service.dto.TicketConversationInfo;
import com.sumadhura.employeeservice.util.CarouselUtils;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * EmployeeTicketServiceHelper bean class provides helper methods for EmployeeTicketServiceImpl.
 * 
 * @author Venkat_Koniki
 * @since 17.08.2019
 * @time 02:40PM
 */
@Component("EmployeeTicketServiceHelper")
//@Named(value = "EmployeeTicketServiceHelper")
public class EmployeeTicketServiceHelper {
	
	@Autowired(required = true)
	@Qualifier("EmployeeTicketDaoImpl")
	private EmployeeTicketDao employeeTicketDaoImpl;
	
	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;
	
	@Autowired(required = true)
	@Qualifier("BookingFormServiceDaoImpl")
	private BookingFormServiceDao bookingFormServiceDaoImpl;
	
	@Autowired(required=true)
	@Qualifier("LoginDaoImpl")
	private LoginDao loginDaoImpl;
	
	private static final Logger LOGGER = Logger.getLogger(EmployeeTicketServiceHelper.class);

	//@Async("myexecutor")
	public Boolean isTicketOwner(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo){
		System.out.println("****Thread name****"+Thread.currentThread().getName());
		LOGGER.info("**** The control is inside the isTicketOwner in  EmployeeTicketServiceImpl ****");
		List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
		if(Util.isNotEmptyObject(employeeDetailsPojos)) {
		employeeTicketRequestInfo.setEmployeeDetailsId(Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmpDetailsId())?employeeDetailsPojos.get(0).getEmpDetailsId():0l);
		List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);
		for(TicketTypeDetailsPojo pojo : ticketTypeDetailsPojos) {
			if(Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) && pojo.getTicketTypeDetailsId().equals(employeeTicketRequestInfo.getTicketTypeDetailsId())) {
				// return new AsyncResult<Boolean>(Boolean.TRUE);
				 return (Boolean.TRUE);
			}
		  }	
		}
      //  return new AsyncResult<Boolean>(Boolean.FALSE);
        return (Boolean.FALSE);
	}
	
	public Boolean isTicketOwnerByEmpId(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo){
		LOGGER.info("**** The control is inside the isTicketOwner in  EmployeeTicketServiceImpl ****");
		return employeeTicketDaoImpl.isTicketOwner(employeeTicketRequestInfo);
	}
	
	//@Async("myexecutor")
	public Boolean isTicketPresent(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, List<TicketPojo> ticketPojoList){
		System.out.println("****Thread name****"+Thread.currentThread().getName());
		LOGGER.info("**** The control is inside the isTicketPresent in  EmployeeTicketServiceImpl ****");
		employeeTicketRequestInfo.setRequestUrl("getTicket");
		//List<TicketPojo> ticketPojoList = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE);
		if (Util.isNotEmptyObject(ticketPojoList)) {
			if (Util.isNotEmptyObject(ticketPojoList.get(0).getAssignmentTo())
					&& employeeTicketRequestInfo.getEmployeeId().equals(ticketPojoList.get(0).getAssignmentTo())) {
				// return new AsyncResult<Boolean>(Boolean.TRUE); 
				 return (Boolean.TRUE); 
			} else {
				/*List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
				if(Util.isNotEmptyObject(employeeDetailsPojos)) {
				for(EmployeeDetailsPojo pojo : employeeDetailsPojos){
					if(Util.isNotEmptyObject(pojo.getDepartmentId()) && pojo.getDepartmentId().equals(ticketPojoList.get(0).getDepartmentId())){
						// return new AsyncResult<Boolean>(Boolean.TRUE); 
						 return (Boolean.TRUE); 
					}
				   }	
				} */
				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId()) && employeeTicketRequestInfo.getDepartmentId().equals(ticketPojoList.get(0).getDepartmentId())){
					// return new AsyncResult<Boolean>(Boolean.TRUE); 
					 return (Boolean.TRUE); 
				}
			}
		}
      //  return new AsyncResult<Boolean>(Boolean.FALSE);
        return (Boolean.FALSE);
	}

	@Async("myexecutor")
	public Future<List<FileInfo>> createDocumentUrls(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		 LOGGER.info("**** The Control is inside createDocumentUrls in EmployeeTicketServiceImpl ******"+Thread.currentThread().getName());
		 List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		 CarouselUtils carouselUtils = new CarouselUtils();
		
		 /*  adding Ticket Documents urls to the list   */
		// for(Long id : createdDocumentIds(employeeTicketRequestInfo)) {
		 for(TicketConversationInfo conversationInfo : createdDocumentIds(employeeTicketRequestInfo)) {	
			 
				employeeTicketRequestInfo.setTicketConversationDocumentId(Util.isNotEmptyObject(conversationInfo.getTicketConversationDocumentId())?conversationInfo.getTicketConversationDocumentId():0l);
				List<TicketConversationDocumentsPojo> ticketConversationDocumentsPojos = employeeTicketDaoImpl.getTicketConversationDocumentsDetails(employeeTicketRequestInfo, Status.ACTIVE);
				
				for(TicketConversationDocumentsPojo documentsPojo : ticketConversationDocumentsPojos) {
					if(Util.isNotEmptyObject(documentsPojo.getExternalDriveFileLocation())) {
						FileInfo info = new FileInfo();
						/* Ticket Conversation Document Id. */
						info.setId(Util.isNotEmptyObject(conversationInfo.getTicketConversationDocumentId())?conversationInfo.getTicketConversationDocumentId().intValue():0);
						info.setName("Sumadhura_Employee_File_"+info.getId());
						info.setUrl(documentsPojo.getExternalDriveFileLocation());
						info.setExtension("N/A");
						/* visibility of documents */
						if(documentsPojo.getVisibleType().equals(Visibility.PUBLIC.getDescription())) {
							info.setVisibilty(Visibility.PUBLIC.getStatus());	
						}else {
							info.setVisibilty(Visibility.PRIVATE.getStatus());
						}
						if(conversationInfo.getFromType().equals(Department.EMPLOYEE.getId())) {
						    //employeeTicketRequestInfo.setDepartmentId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromDeptId())? conversationInfo.getFromDeptId(): 0l: 0l);
						    //List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo, Status.ACTIVE);
						    employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromId())? conversationInfo.getFromId(): 0l: 0l);
						    employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");
						    List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
						    //info.setCreatedBy((Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A":"N/A")+"/"+(Util.isNotEmptyObject(Pojos)?Util.isNotEmptyObject(Pojos.get(0).getDepartmentName())?Pojos.get(0).getDepartmentName():"N/A":"N/A"));
						    info.setCreatedBy((Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A":"N/A"));
							info.setCreatedType(Department.EMPLOYEE.getName());
						}else if(conversationInfo.getFromType().equals(Department.CUSTOMER.getId())) {
							employeeTicketRequestInfo.setCustomerId(conversationInfo.getFromId() != null ? conversationInfo.getFromId() : 0l);
							List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo, Status.ACTIVE);
							info.setCreatedBy((Util.isNotEmptyObject(customerPojos.get(0).getFirstName())?customerPojos.get(0).getFirstName():"")+(Util.isNotEmptyObject(customerPojos.get(0).getLastName())?customerPojos.get(0).getLastName():""));
							info.setCreatedType(Department.CUSTOMER.getName());
						}
						fileInfos.add(info);
					}else {
						Map<String,String> urlsMap = carouselUtils.findMapUrls(documentsPojo.getDocumentsLocation()!=null?documentsPojo.getDocumentsLocation():"");
						for (Map.Entry<String, String> entry : urlsMap.entrySet()) {
							FileInfo info = new FileInfo();
							LOGGER.debug("**** The file name is ****"+entry.getValue()+"******url is ******"+entry.getKey());
							/* Ticket Conversation Document Id. */
							info.setId(conversationInfo.getTicketConversationDocumentId().intValue());
							info.setName(entry.getValue());
							
							if(documentsPojo.getVisibleType().equals(Visibility.PUBLIC.getDescription())) {
								info.setVisibilty(Visibility.PUBLIC.getStatus());	
							}else {
								if(Util.isNotEmptyObject(documentsPojo.getPublicDocuments()) && Arrays.asList(documentsPojo.getPublicDocuments().split(",")).contains(entry.getValue())){
									info.setVisibilty(Visibility.PUBLIC.getStatus());
								}else {
									info.setVisibilty(Visibility.PRIVATE.getStatus());
								}
							}
						    info.setUrl(entry.getKey());
							info.setExtension(FilenameUtils.getExtension(entry.getValue()));
							
							if(conversationInfo.getFromType().equals(Department.EMPLOYEE.getId())) {
							    //employeeTicketRequestInfo.setDepartmentId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromDeptId())? conversationInfo.getFromDeptId(): 0l: 0l);
							    //List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo, Status.ACTIVE);
							    employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromId())? conversationInfo.getFromId(): 0l: 0l);
							    employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");
							    List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
							    //info.setCreatedBy((Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A":"N/A")+"/"+(Util.isNotEmptyObject(Pojos)?Util.isNotEmptyObject(Pojos.get(0).getDepartmentName())?Pojos.get(0).getDepartmentName():"N/A":"N/A"));
							    info.setCreatedBy((Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A":"N/A"));
								info.setCreatedType(Department.EMPLOYEE.getName());
							}else if(conversationInfo.getFromType().equals(Department.CUSTOMER.getId())) {
								employeeTicketRequestInfo.setCustomerId(conversationInfo.getFromId() != null ? conversationInfo.getFromId() : 0l);
								List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo, Status.ACTIVE);
								info.setCreatedBy((Util.isNotEmptyObject(customerPojos.get(0).getFirstName())?customerPojos.get(0).getFirstName():"")+(Util.isNotEmptyObject(customerPojos.get(0).getLastName())?customerPojos.get(0).getLastName():""));
								info.setCreatedType(Department.CUSTOMER.getName());
							}
							fileInfos.add(info);
						 }	
					}
		     }	
	    }
		// return  fileInfos;
		   return new AsyncResult<List<FileInfo>>(fileInfos);
    }
	
	@Async("myexecutor")
	public Future<List<FileInfo>> createCustomerDocumentUrls(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The Control is inside the createCustomerDocumentUrls in EmployeeTicketServiceImpl *****"+Thread.currentThread().getName());

		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		CarouselUtils carouselUtils = new CarouselUtils();

		/* adding Ticket Documents urls to the list */
		for (TicketConversationInfo conversationInfo : createdDocumentIds(employeeTicketRequestInfo)) {

			employeeTicketRequestInfo.setTicketConversationDocumentId(Util.isNotEmptyObject(conversationInfo.getTicketConversationDocumentId())?conversationInfo.getTicketConversationDocumentId():0l);
			List<TicketConversationDocumentsPojo> ticketConversationDocumentsPojos = employeeTicketDaoImpl.getTicketConversationDocumentsDetails(employeeTicketRequestInfo, Status.ACTIVE);
			for (TicketConversationDocumentsPojo documentsPojo : ticketConversationDocumentsPojos) {
				if(Util.isNotEmptyObject(documentsPojo.getExternalDriveFileLocation())) {
					if(documentsPojo.getVisibleType().equals(Visibility.PUBLIC.getDescription())) {
						FileInfo info = new FileInfo();
						/* Ticket Conversation Document Id. */
						info.setId(Util.isNotEmptyObject(conversationInfo.getTicketConversationDocumentId())?conversationInfo.getTicketConversationDocumentId().intValue():0);
						info.setName("Sumadhura_Employee_File_"+info.getId());
						info.setVisibilty(Visibility.PUBLIC.getStatus());	
						info.setUrl(documentsPojo.getExternalDriveFileLocation());
						info.setExtension("N/A");
						if(conversationInfo.getFromType().equals(Department.EMPLOYEE.getId())) {
						    employeeTicketRequestInfo.setDepartmentId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromDeptId())? conversationInfo.getFromDeptId(): 0l: 0l);
						    List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo, Status.ACTIVE);
						    employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromId())? conversationInfo.getFromId(): 0l: 0l);
							List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
						    info.setCreatedBy((Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A":"N/A")+"/"+(Util.isNotEmptyObject(Pojos)?Util.isNotEmptyObject(Pojos.get(0).getDepartmentName())?Pojos.get(0).getDepartmentName():"N/A":"N/A"));
							info.setCreatedType(Department.EMPLOYEE.getName());
						}else if(conversationInfo.getFromType().equals(Department.CUSTOMER.getId())) {
							employeeTicketRequestInfo.setCustomerId(conversationInfo.getFromId() != null ? conversationInfo.getFromId() : 0l);
							List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo, Status.ACTIVE);
							info.setCreatedBy((Util.isNotEmptyObject(customerPojos.get(0).getFirstName())?customerPojos.get(0).getFirstName():"")+(Util.isNotEmptyObject(customerPojos.get(0).getLastName())?customerPojos.get(0).getLastName():""));
							info.setCreatedType(Department.CUSTOMER.getName());
						}
						fileInfos.add(info);
					}
				}else {
					if(documentsPojo.getVisibleType().equals(Visibility.PUBLIC.getDescription())) {
						Map<String,String> urlsMap = carouselUtils.findMapUrls(documentsPojo.getDocumentsLocation()!=null?documentsPojo.getDocumentsLocation():"");
						for (Map.Entry<String, String> entry : urlsMap.entrySet()) {
							FileInfo info = new FileInfo();
							LOGGER.debug("**** The file name is ****"+entry.getValue()+"******url is ******"+entry.getKey());
							/* Ticket Conversation Document Id. */
							info.setId(Util.isNotEmptyObject(conversationInfo.getTicketConversationDocumentId())?conversationInfo.getTicketConversationDocumentId().intValue():0);
							info.setName(entry.getValue());
							info.setVisibilty(Visibility.PUBLIC.getStatus());	
							info.setUrl(entry.getKey());
							info.setExtension(FilenameUtils.getExtension(entry.getValue()));
							if(conversationInfo.getFromType().equals(Department.EMPLOYEE.getId())) {
							    employeeTicketRequestInfo.setDepartmentId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromDeptId())? conversationInfo.getFromDeptId(): 0l: 0l);
							    List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo, Status.ACTIVE);
							    employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromId())? conversationInfo.getFromId(): 0l: 0l);
								List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
							    info.setCreatedBy((Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A":"N/A")+"/"+(Util.isNotEmptyObject(Pojos)?Util.isNotEmptyObject(Pojos.get(0).getDepartmentName())?Pojos.get(0).getDepartmentName():"N/A":"N/A"));
								info.setCreatedType(Department.EMPLOYEE.getName());
							}else if(conversationInfo.getFromType().equals(Department.CUSTOMER.getId())) {
								employeeTicketRequestInfo.setCustomerId(conversationInfo.getFromId() != null ? conversationInfo.getFromId() : 0l);
								List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo, Status.ACTIVE);
								info.setCreatedBy((Util.isNotEmptyObject(customerPojos.get(0).getFirstName())?customerPojos.get(0).getFirstName():"")+(Util.isNotEmptyObject(customerPojos.get(0).getLastName())?customerPojos.get(0).getLastName():""));
								info.setCreatedType(Department.CUSTOMER.getName());
							}
							fileInfos.add(info);
						 }
					}else if(Util.isNotEmptyObject(documentsPojo.getPublicDocuments())) {
						for (String filename : documentsPojo.getPublicDocuments().split(",")) {
							/* creating public fileUrls */
							StringBuilder filePath = new StringBuilder(documentsPojo.getDocumentsLocation()).append(File.separator).append(filename);
							Map<String, String> urlsMap = carouselUtils.findMapUrls(filePath.toString());
							for (Map.Entry<String, String> entry : urlsMap.entrySet()) {
								FileInfo info = new FileInfo();
								LOGGER.debug("**** The file name is ****" + entry.getValue() + "******url is ******"+ entry.getKey());
								/* Ticket Conversation Document Id. */
								info.setId(Util.isNotEmptyObject(conversationInfo.getTicketConversationDocumentId())?conversationInfo.getTicketConversationDocumentId().intValue():0);
								info.setName(entry.getValue());
								info.setVisibilty(Visibility.PUBLIC.getStatus());
								info.setUrl(entry.getKey());
								info.setExtension(FilenameUtils.getExtension(entry.getValue()));
								if(conversationInfo.getFromType().equals(Department.EMPLOYEE.getId())) {
								    employeeTicketRequestInfo.setDepartmentId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromDeptId())? conversationInfo.getFromDeptId(): 0l: 0l);
								    List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo, Status.ACTIVE);
								    employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(conversationInfo)? Util.isNotEmptyObject(conversationInfo.getFromId())? conversationInfo.getFromId(): 0l: 0l);
									List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
								    info.setCreatedBy((Util.isNotEmptyObject(employeePojos)?Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())?employeePojos.get(0).getEmployeeName():"N/A":"N/A")+"/"+(Util.isNotEmptyObject(Pojos)?Util.isNotEmptyObject(Pojos.get(0).getDepartmentName())?Pojos.get(0).getDepartmentName():"N/A":"N/A"));
									info.setCreatedType(Department.EMPLOYEE.getName());
								}else if(conversationInfo.getFromType().equals(Department.CUSTOMER.getId())) {
									employeeTicketRequestInfo.setCustomerId(conversationInfo.getFromId() != null ? conversationInfo.getFromId() : 0l);
									List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo, Status.ACTIVE);
									info.setCreatedBy((Util.isNotEmptyObject(customerPojos.get(0).getFirstName())?customerPojos.get(0).getFirstName():"")+(Util.isNotEmptyObject(customerPojos.get(0).getLastName())?customerPojos.get(0).getLastName():""));
									info.setCreatedType(Department.CUSTOMER.getName());
								}
								fileInfos.add(info);
							}
						}
					}
				}	
			}
		}
		//return fileInfos;
		return new AsyncResult<List<FileInfo>>(fileInfos);
	}
	
	private Set<TicketConversationInfo> createdDocumentIds(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The Control is inside the createdDocumentIds in EmployeeTicketServiceImpl *****"+Thread.currentThread().getName());

		//List<Long> documentsIds = new ArrayList<Long>();
		Set<TicketConversationInfo> ticketConversationInfos= new LinkedHashSet<TicketConversationInfo>();

		List<TicketCommentsPojo> ticketCommentsPojos = employeeTicketDaoImpl.getTicketComments(employeeTicketRequestInfo, Status.ACTIVE);
		List<TicketSeekInfoPojo> ticketSeekInfoPojos = employeeTicketDaoImpl.getTicketSeekInfoDetails(employeeTicketRequestInfo, Status.ACTIVE);

		for (TicketCommentsPojo ticketCommentsPojo : ticketCommentsPojos) {
			TicketConversationInfo info = new TicketConversationInfo();
			info.setTicketConversationDocumentId(ticketCommentsPojo.getTicketConversationDocumentId());
			info.setFromType(ticketCommentsPojo.getFromType());
			info.setFromId(ticketCommentsPojo.getFromId());
			info.setFromDeptId(ticketCommentsPojo.getFromDeptId());
			ticketConversationInfos.add(info);
			//documentsIds.add(ticketCommentsPojo.getTicketConversationDocumentId());
		}
		for (TicketSeekInfoPojo pojo : ticketSeekInfoPojos) {
			TicketConversationInfo info = new TicketConversationInfo();
			info.setTicketConversationDocumentId(pojo.getTicketConversationDocId());
			info.setFromType(pojo.getFromType());
			info.setFromId(pojo.getFromId());
			info.setFromDeptId(pojo.getFromDeptId());
			ticketConversationInfos.add(info);
			//documentsIds.add(pojo.getTicketConversationDocId() != null ? pojo.getTicketConversationDocId() : 0l);
		}
		//LinkedHashSet<Long> documentSet = new LinkedHashSet<Long>(documentsIds);
		//return new ArrayList<Long>(documentSet);
		return ticketConversationInfos;
	}
	
	@Async("myexecutor")
	public Future<Boolean> sendticketResponseMail(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo){
		LOGGER.info("*** The Control is inside the sendticketResponseMail in EmployeeTicketServiceImpl *****"+Thread.currentThread().getName());
		
		 EmployeeTicketRequestInfo  info = new EmployeeTicketRequestInfo();
		 /*  setting Ticket Number  */
		 info.setTicketId(employeeTicketRequestInfo.getTicketId());
		 info.setRequestUrl("getTicket");
		 List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
		 info.setFlatBookingId(Util.isNotEmptyObject(ticketPojos.get(0).getFlatBookingId())?ticketPojos.get(0).getFlatBookingId():0l);
		 
		 info.setRequestUrl("updateTicketConversation.spring");
		 List<FlatBookingPojo> flatBookingPojos =  employeeTicketDaoImpl.getFlatbookingDetails(info, Status.ACTIVE);
		
		 BookingFormRequest bookingFormRequest = new BookingFormRequest();
		 bookingFormRequest.setFlatId(Util.isNotEmptyObject(flatBookingPojos.get(0).getFlatId())?flatBookingPojos.get(0).getFlatId():0l);
		 List<FlatPojo> flatPojos = bookingFormServiceDaoImpl.getFlat(bookingFormRequest);
		 /*  setting FlatNumber   */
		 info.setFlatNo(Util.isNotEmptyObject(flatPojos.get(0).getFlatNo())?flatPojos.get(0).getFlatNo():"N/A");
		 info.setTicketTypeDetailsId(Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeDetailsId())?ticketPojos.get(0).getTicketTypeDetailsId():0l);
		 info.setRequestUrl("getCustomerTicketDetails");
  	     List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(info, Status.ACTIVE);
  	     info.setDepartmentId(Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())?ticketTypeDetailsPojos.get(0).getDepartmentId():0l);
	     List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(info, Status.ACTIVE);
	     /*  setting Department Name  */
	     info.setDeptName(Util.isNotEmptyObject(departmentPojos.get(0).getDepartmentName())?departmentPojos.get(0).getDepartmentName():"N/A");
	     /*  setting Message  */
	     info.setMessage(employeeTicketRequestInfo.getMessage());
	     List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = employeeTicketDaoImpl.getCustomerPropertyDetails(info, Status.ACTIVE);
	     /* setting Customer Name */
	     info.setCustomerName(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerName())?customerPropertyDetailsPojos.get(0).getCustomerName():"N/A");
		 /* setting customer Mail */
	     info.setMails(new String[] {Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerEmail())?customerPropertyDetailsPojos.get(0).getCustomerEmail():"N/A"});
	     if(mailServiceImpl.sendCustomerTicketUpdateMail(info)){
	    	 return new AsyncResult<Boolean>(Boolean.TRUE);
	     }
		 return new AsyncResult<Boolean>(Boolean.FALSE);
	}
	@Async("myexecutor")
	public  Future<Boolean> sendTicketForwardMail(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) throws InformationNotFoundException{
		LOGGER.info("*** The Control is inside the sendCustomerTicketForwardMail in EmployeeTicketServiceImpl *****"+Thread.currentThread().getName());
		EmployeeTicketRequestInfo  info = new EmployeeTicketRequestInfo();
		
		/* setting TicketId */
		info.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())?employeeTicketRequestInfo.getTicketId():0l);
		
		info.setRequestUrl("getTicket");
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
		info.setTicketTypeDetailsId(Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeDetailsId())?ticketPojos.get(0).getTicketTypeDetailsId():0l);
		info.setRequestUrl("getCustomerTicketDetails");
		List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(info, Status.ACTIVE);
		info.setEmployeeDetailsId(Util.isNotEmptyObject(ticketTypeDetailsPojos)?Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())?ticketTypeDetailsPojos.get(0).getEmployeeDetailsId():0l:0l);
		List<EmployeeDetailsPojo> employeeDetailsPojos  = employeeTicketDaoImpl.getEmployeeDetails(info, Status.ACTIVE);
		info.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)?Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())?employeeDetailsPojos.get(0).getEmployeeId():0l:0l);
		
		List<EmployeePojo> employeePojosObj = employeeTicketDaoImpl.getEmployee(info, Status.ACTIVE);
		
		/* setting TicketOwner name */
		info.setTicketOwner(Util.isNotEmptyObject(employeePojosObj.get(0).getEmployeeName())?employeePojosObj.get(0).getEmployeeName():"N/A");
		
		info.setFlatBookingId(Util.isNotEmptyObject(ticketPojos.get(0).getFlatBookingId())?ticketPojos.get(0).getFlatBookingId():0l);
		
		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setRequestUrl("venkat");
		bookingFormRequest.setFlatBookingId(info.getFlatBookingId());
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = bookingFormServiceDaoImpl.getCustomerPropertyDetails(bookingFormRequest);
		
		info.setSiteName(customerPropertyDetailsPojos.get(0).getSiteName());
		/*  setting FlatNumber   */
		info.setFlatNo(Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getFlatNo())?customerPropertyDetailsPojos.get(0).getFlatNo():"N/A");
	
		/* setting customerName   */ 
		 info.setCustomerName((Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerName())?customerPropertyDetailsPojos.get(0).getCustomerName():""));
		
		/* setting Message */
		info.setMessage(Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())?employeeTicketRequestInfo.getMessage():"N/A");
		
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getToDeptId())) {
			info.setDepartmentId(employeeTicketRequestInfo.getToDeptId());
			//List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(info,Status.ACTIVE);
			List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentEmployeeDetails(info);
			/* adding Department Name   */
			info.setDeptName(Util.isNotEmptyObject(departmentPojos.get(0).getDepartmentName())?departmentPojos.get(0).getDepartmentName():"N/A");
			/* adding Generic Name  */
			info.setGeneric(Util.isNotEmptyObject(departmentPojos.get(0).getDepartmentName())?departmentPojos.get(0).getDepartmentName():"N/A");
			
			if (Util.isNotEmptyObject(departmentPojos)) {
				String[] mailArray =  new String[departmentPojos.size()];
				List<String> mailsList = new ArrayList<>();
				for (DepartmentPojo pojo : departmentPojos) {
					LOGGER.debug("***** The Department Mails is *****" + pojo.getDepartmentMail());
					mailsList.add(pojo.getEmpMail());
				}
				info.setMails(mailsList.toArray(mailArray));
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("***** The Exception is raised in while sending forwarding mail to department while forwarding ticket. ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		}else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getToId())) {
			info.setEmployeeId(employeeTicketRequestInfo.getToId());
			List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(info, Status.ACTIVE);
			if (Util.isNotEmptyObject(employeePojos)) {
				for (EmployeePojo pojo : employeePojos) {
					String employeeMail = pojo.getEmail();
					String[] mailArray = new String[1];
					mailArray[0] = employeeMail;
					/* setting mails */
					info.setMails(mailArray);
					
					/* setting Employee Name */
					info.setGeneric(Util.isNotEmptyObject(pojo.getEmployeeName())?pojo.getEmployeeName():"N/A");
					info.setEmployeeId(Util.isNotEmptyObject(pojo.getEmployeeId())?pojo.getEmployeeId():0l);
					
					LoginInfo loginInfo = new LoginInfo();
					loginInfo.setEmployeeId(info.getEmployeeId());
					List<EmployeeDepartmentMappingPojo> employeeDepartmentMappingPojos = loginDaoImpl.getEmployeeDepartmentMappingDetails(loginInfo, Status.ACTIVE);
					loginInfo.setDepartmentRoleMappingId(Util.isNotEmptyObject(employeeDepartmentMappingPojos.get(0).getDepartmentRoleMappingId())?employeeDepartmentMappingPojos.get(0).getDepartmentRoleMappingId():0l);
					/* getting departmentRoleMapping Details  */
					List<DepartmentRoleMappingPojo> departmentRoleMappingPojos = loginDaoImpl.getDepartmentRoleMappingDetails(loginInfo, Status.ACTIVE);
					loginInfo.setDepartmentId(Util.isNotEmptyObject(departmentRoleMappingPojos.get(0).getDepartmentId())?departmentRoleMappingPojos.get(0).getDepartmentId():0l);
					info.setDepartmentId(loginInfo.getDepartmentId());
					List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(info,Status.ACTIVE);
					/* setting department Name */
					info.setDeptName(Util.isNotEmptyObject(departmentPojos.get(0).getDepartmentName())?departmentPojos.get(0).getDepartmentName():"N/A");
				}
			
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"***** The Exception is raised in while sending forwarding mail to employee or department either of those is given. ******");
			throw new InformationNotFoundException(errorMsgs);
		}
	 }
		/* sending Ticket Forward mails to the Employee   */
		 Boolean employeeMailStatus =  mailServiceImpl.sendEmployeeTicketForwardMail(info);
		 
		/*sending Ticket Forwrad Mail to the customer */
		 info.setMails(new String[]{Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerEmail())?(customerPropertyDetailsPojos.get(0).getCustomerEmail()):"N/A"});
		 Boolean customerMailStatus  = mailServiceImpl.sendCustomerTicketForwardMail(info); 
		 
		 if(employeeMailStatus && customerMailStatus) {
			 LOGGER.info("*** The TicketForwrd Mails sent successfully ****");
		 }
		
		return new AsyncResult<Boolean>(Boolean.FALSE);
   }
	
	@Async("myexecutor")
	public Future<Boolean> sendTicketCloseMail(EmployeeTicketRequestInfo emailReq) throws InformationNotFoundException {
		LOGGER.info("**** The control is inside the sendTicketCloseMail in EmployeeTicketServiceImpl ****");
		if(Util.isNotEmptyObject(emailReq.getTicketId())) {
			@SuppressWarnings("unused")
			boolean customerMailStatus = false;
			@SuppressWarnings("unused")
			boolean employeeMailStatus = false;
			EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
			info.setTicketId(Util.isNotEmptyObject(emailReq.getTicketId())?emailReq.getTicketId():0l);
			info.setRequestUrl("getTicket");
			info.setDeptName(emailReq.getDeptName());
			List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
			info.setFlatBookingId(Util.isNotEmptyObject(ticketPojos)?Util.isNotEmptyObject(ticketPojos.get(0).getFlatBookingId())?ticketPojos.get(0).getFlatBookingId():0l:0l);
			/* getting customerId from the FlatBooking Details */
			info.setRequestUrl("closeTicket.spring");
			List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl.getFlatbookingDetails(info,Status.ACTIVE);
			info.setCustomerId(Util.isNotEmptyObject(flatBookingPojos)?Util.isNotEmptyObject(flatBookingPojos.get(0).getCustomerId())?flatBookingPojos.get(0).getCustomerId():0l:0l);
			List<CustBookInfoPojo> custBookInfoPojos = employeeTicketDaoImpl.getCustBookInfo(info, Status.ACTIVE);
			
			List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(info, Status.ACTIVE);
			CustomerPojo customerPojo = customerPojos.get(0);
			info.setFlatNo(flatBookingPojos.get(0).getFlatNo());
			String lastName=customerPojo.getLastName();
			lastName=(lastName!=null && "".equalsIgnoreCase(lastName.trim()) && lastName.equalsIgnoreCase("null"))?lastName:"";
			info.setCustomerName(customerPojo.getFirstName()+" "+lastName);
			//info.setFlatNo(flatNo);
			//info.setTicketId(ticketId);
			
			if (Util.isNotEmptyObject(custBookInfoPojos) && Util.isNotEmptyObject(customerPojos)){
				for (CustBookInfoPojo pojo : custBookInfoPojos) {
					LOGGER.debug("***** The Customer Mails is *****" + pojo.getEmail());
					String customerMail = pojo.getEmail();
					String[] mailArray = new String[1];
					//mailArray[0] = "rayudu3java@gmail.com";
					mailArray[0] = customerMail;
					for (String mail : mailArray) {
						LOGGER.debug("**** The Customer mail is ****" + mail);
						info.setMails(mailArray);
						info.setGeneric(Util.isNotEmptyObject(customerPojos.get(0).getFirstName())?customerPojos.get(0).getFirstName():""+" "+(Util.isNotEmptyObject(customerPojos.get(0).getLastName())?customerPojos.get(0).getLastName():""));
						customerMailStatus = mailServiceImpl.sendCustomerTicketCloseMail(info);
					}
				}
			}
			
			/* send Ticket close mail to employee */
			info = new EmployeeTicketRequestInfo();
			/* calling garbage collector to free up the space  */
			/*System.gc();
			
			info.setTicketTypeDetailsId(Util.isNotEmptyObject(ticketPojos)?Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeDetailsId())?ticketPojos.get(0).getTicketTypeDetailsId():0l:0l);
			info.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(info, Status.ACTIVE);
			
			info.setEmployeeDetailsId(Util.isNotEmptyObject(ticketTypeDetailsPojos)?Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())?ticketTypeDetailsPojos.get(0).getEmployeeDetailsId():0l:0l);
		    List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(info, Status.ACTIVE);
		     
		    info.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)?Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())?employeeDetailsPojos.get(0).getEmployeeId():0l:0l);
			List<EmployeePojo> employeePojos = employeeTicketDaoImpl
					.getEmployee(info, Status.ACTIVE);
			if (Util.isNotEmptyObject(employeePojos)) {
				for (EmployeePojo pojo : employeePojos) {
					LOGGER.debug("***** The Employee Mails is *****" + pojo.getEmail());
					String employeeMail = pojo.getEmail();
					String[] mailArray = new String[1];
					mailArray[0] = employeeMail;
					for (String mail : mailArray) {
						LOGGER.debug("**** The Employee mail is ****" + mail);
						info.setMails(mailArray);
						info.setGeneric(pojo.getEmployeeName());
						employeeMailStatus = mailServiceImpl.sendEmployeeTicketCloseMail(info);
					}
				}
			}
			
			if(customerMailStatus && employeeMailStatus) {
				LOGGER.info("*** The customerMail and employeeMail both sent successfully! *****");
				return true;
			}*/
			
		}else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"***** The Exception is raised in while sending Ticket Close mail to employee and customer. ******");
			throw new InformationNotFoundException(errorMsgs);
		}
		return new AsyncResult<Boolean>(Boolean.FALSE);
		
	}
	
	@Async("myexecutor")
	public Future<List<TicketPojo>> getTicketList(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo){
		LOGGER.info("**** The control is inside the getTicketList in EmployeeTicketServiceHelper ****");
		return new AsyncResult<List<TicketPojo>>(employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE));
	}

	@Async("myexecutor")
	public Future<List<CustomerPropertyDetailsPojo>> getTicketOwner(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The control is inside the getTicketOwner in EmployeeTicketServiceHelper ****");
		EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
		/* setting Ticket Number */
		info.setTicketId(employeeTicketRequestInfo.getTicketId());
		info.setRequestUrl("getTicket");
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
		info.setFlatBookingId(Util.isNotEmptyObject(ticketPojos.get(0).getFlatBookingId()) ? ticketPojos.get(0).getFlatBookingId():0l);
		info.setRequestUrl("systemescalate");
		return new AsyncResult<List<CustomerPropertyDetailsPojo>>(employeeTicketDaoImpl.getCustomerPropertyDetails(info, Status.ACTIVE));
	}
	
	@Async("myexecutor")
	public Future<Boolean> sendTicketReOpenMail(@NonNull EmployeeTicketRequestInfo emailReq) throws InformationNotFoundException {
		LOGGER.info("**** The control is inside the sendTicketReOpenMail in EmployeeTicketServiceImpl ****");
		
		final EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
		
		/* setting Ticket Number */
		info.setTicketId(emailReq.getTicketId());
		info.setRequestUrl("getTicket");
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
		info.setFlatBookingId(Util.isNotEmptyObject(ticketPojos.get(0).getFlatBookingId()) ? ticketPojos.get(0).getFlatBookingId():0l);
		info.setRequestUrl("systemescalate");
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos  = employeeTicketDaoImpl.getCustomerPropertyDetails(info, Status.ACTIVE);

	    /*  setting FlatId  */
		info.setFlatNo(Util.isNotEmptyObject(customerPropertyDetailsPojos)?Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getFlatNo())?customerPropertyDetailsPojos.get(0).getFlatNo():"N/A":"N/A");
		
	     /* setting customer Name */ 
		info.setCustomerName(Util.isNotEmptyObject(customerPropertyDetailsPojos)?Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getCustomerName())?customerPropertyDetailsPojos.get(0).getCustomerName():"N/A":"N/A");
	    
	    /* setting project Name */
	    info.setSiteName(Util.isNotEmptyObject(customerPropertyDetailsPojos)?Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0).getSiteName())?customerPropertyDetailsPojos.get(0).getSiteName():"N/A":"N/A");

	    /* setting ticket type details Id */
	    info.setTicketTypeDetailsId(Util.isNotEmptyObject(ticketPojos)?Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeDetailsId())?ticketPojos.get(0).getTicketTypeDetailsId():0l:0l);
		info.setRequestUrl("getCustomerTicketDetails");
		List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(info, Status.ACTIVE);
		info.setEmployeeDetailsId(Util.isNotEmptyObject(ticketTypeDetailsPojos)?Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())?ticketTypeDetailsPojos.get(0).getEmployeeDetailsId():0l:0l);
		List<EmployeeDetailsPojo> employeeDetailsPojos  = employeeTicketDaoImpl.getEmployeeDetails(info, Status.ACTIVE);
		info.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)?Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())?employeeDetailsPojos.get(0).getEmployeeId():0l:0l);
		
		List<EmployeePojo> employeePojosObj = employeeTicketDaoImpl.getEmployee(info, Status.ACTIVE);
		
		/* setting department name  */
		info.setDepartmentId(Util.isNotEmptyObject(employeeDetailsPojos)?Util.isNotEmptyObject(employeeDetailsPojos.get(0).getDepartmentId())?employeeDetailsPojos.get(0).getDepartmentId():0l:0l);
		List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(info,Status.ACTIVE);
		
		/* setting TicketOwner name */
		info.setTicketOwner(Util.isNotEmptyObject(employeePojosObj.get(0).getEmployeeName())?employeePojosObj.get(0).getEmployeeName():"N/A");
		
		/* setting Ticket owner mail */
		info.setMails(new String[] {Util.isNotEmptyObject(employeePojosObj.get(0).getEmail())?employeePojosObj.get(0).getEmail():"N/A"});
		
		List<TicketEscalationLevelMappingPojo> levelMappingPojos = employeeTicketDaoImpl.getTicketEscaltionAprovalLevelDetails(info,Status.ACTIVE);
    	info.setTicketEscalationLevelMappingId(Util.isNotEmptyObject(levelMappingPojos)?Util.isNotEmptyObject(levelMappingPojos.get(0).getId())?levelMappingPojos.get(0).getId():0l:0l);
    	List<TicketEscalationLevelEmployeeMappingPojo> escalationLevelEmployeeMappingPojos = employeeTicketDaoImpl.getTicketscalationEmployeeLevelMappingDetails(info,Status.ACTIVE);
    	
		/* setting next level employee mail 
		info.setEscalatedTicketAssignedEmployeeId(Util.isNotEmptyObject(escalationLevelEmployeeMappingPojos)?Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getSystemEscalatedAssignedEmployeeId())?ticketTypeDetailsPojos.get(0).getSystemEscalatedAssignedEmployeeId():0l:0l);
		info.setEmployeeDetailsId(info.getEscalatedTicketAssignedEmployeeId());
		List<EmployeeDetailsPojo> systemEscalatedemployeeDetailsPojos  = employeeTicketDaoImpl.getEmployeeDetails(info, Status.ACTIVE);
		*/
		
		info.setEmployeeId(Util.isNotEmptyObject(escalationLevelEmployeeMappingPojos)?Util.isNotEmptyObject(escalationLevelEmployeeMappingPojos.get(0).getEmployeeId())?escalationLevelEmployeeMappingPojos.get(0).getEmployeeId():0l:0l);
		
		List<EmployeePojo> systemEscalatedemployeePojosObj = employeeTicketDaoImpl.getEmployee(info, Status.ACTIVE);
		info.setEscalatedTicketAssignedEmployeeMail(Util.isNotEmptyObject(systemEscalatedemployeePojosObj.get(0).getEmail())?systemEscalatedemployeePojosObj.get(0).getEmail():"N/A");
		
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setEmployeeId(info.getEmployeeId());
		List<EmployeeDepartmentMappingPojo> employeeDepartmentMappingPojos = loginDaoImpl.getEmployeeDepartmentMappingDetails(loginInfo, Status.ACTIVE);
		loginInfo.setDepartmentRoleMappingId(Util.isNotEmptyObject(employeeDepartmentMappingPojos.get(0).getDepartmentRoleMappingId())?employeeDepartmentMappingPojos.get(0).getDepartmentRoleMappingId():0l);
		
		/*getting departmentRoleMapping Details 
		List<DepartmentRoleMappingPojo> departmentRoleMappingPojos = loginDaoImpl.getDepartmentRoleMappingDetails(loginInfo, Status.ACTIVE);
		loginInfo.setDepartmentId(Util.isNotEmptyObject(departmentRoleMappingPojos.get(0).getDepartmentId())?departmentRoleMappingPojos.get(0).getDepartmentId():0l);
		info.setDepartmentId(loginInfo.getDepartmentId());
	    List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(info,Status.ACTIVE);
		*/
		
		/* setting department Name */
		info.setDeptName(Util.isNotEmptyObject(departmentPojos.get(0).getDepartmentName())?departmentPojos.get(0).getDepartmentName():"N/A");

		info.setTicketTypeId(Util.isNotEmptyObject(ticketPojos)?Util.isNotEmptyObject(ticketPojos.get(0))?Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeId())?ticketPojos.get(0).getTicketTypeId():0l:0l:0l);
		List<TicketTypePojo> ticketTypePojos = employeeTicketDaoImpl.getTicketTypeInfo(info, Status.ACTIVE);
		
		/*  setting ticket type */
		info.setTicketType(Util.isNotEmptyObject(ticketTypePojos)?Util.isNotEmptyObject(ticketTypePojos.get(0).getTicketType())?ticketTypePojos.get(0).getTicketType():"N/A":"N/A");
	    
		/*  setting Ticket created Date */
		info.setTicketCreatedDate(Util.isNotEmptyObject(ticketPojos)?Util.isNotEmptyObject(ticketPojos.get(0))?Util.isNotEmptyObject(ticketPojos.get(0).getCreatedDate())?ticketPojos.get(0).getCreatedDate():new Timestamp(new Date().getTime()):new Timestamp(new Date().getTime()):new Timestamp(new Date().getTime()));
		
		/* setting Ticket expected closer date*/
		info.setTicketExpectedCloserDate(Util.isNotEmptyObject(ticketPojos)?Util.isNotEmptyObject(ticketPojos.get(0))?Util.isNotEmptyObject(ticketPojos.get(0).getEstimatedResolvedDate())?ticketPojos.get(0).getEstimatedResolvedDate():new Timestamp(new Date().getTime()):new Timestamp(new Date().getTime()):new Timestamp(new Date().getTime()));
		
		if (Util.isNotEmptyObject(info)){
				boolean emailStatus = mailServiceImpl.sendEmployeeTicketReOpenMail(info);
				return new AsyncResult<Boolean>(emailStatus);
		}
		
	 	return new AsyncResult<Boolean>(Boolean.FALSE);
	}  
	
	@Async("myexecutor")
	public Future<Boolean> sendChangeTicketTypeMailToAdmin(@NonNull ChangeTicketType emailReq){
		LOGGER.info("**** The control is inside the sendChangeTicketTypeMail in EmployeeTicketServiceImpl ****");
		if(mailServiceImpl.sendChangeTicketTypeMailToAdmin(emailReq)) {
			return new AsyncResult<Boolean>(Boolean.TRUE);
		}
		return new AsyncResult<Boolean>(Boolean.FALSE);
	}
	
	@Async("myexecutor")
	public Future<Boolean> sendChangeTicketTypeReminderMailToAdmin(@NonNull EmployeeTicketRequestInfo emailReq){
		LOGGER.info("**** The control is inside the sendChangeTicketTypeReminderMailToAdmin in EmployeeTicketServiceImpl ****");
		if(mailServiceImpl.sendChangeTicketTypeReminderMailToAdmin(emailReq)) {
			return new AsyncResult<Boolean>(Boolean.TRUE);
		}
		return new AsyncResult<Boolean>(Boolean.FALSE);
	}
	
	
//	@Async("myexecutor")
	public Future<Boolean> sendChangeTicketTypeTicketOwnersMail(@NonNull ChangeTicketType emailReq){
		LOGGER.info("**** The control is inside the sendChangeTicketTypeReminderMailToAdmin in EmployeeTicketServiceImpl ****");
		if(mailServiceImpl.sendChangeTicketTypeTicketOwnersMail(emailReq)) {
			return new AsyncResult<Boolean>(Boolean.TRUE);
		}
		return new AsyncResult<Boolean>(Boolean.FALSE);
	}
	
	
}
