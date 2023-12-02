/**
 * 
 */
package com.sumadhura.employeeservice.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumadhura.employeeservice.dto.ChangeTicketType;
import com.sumadhura.employeeservice.dto.CoApplicant;
import com.sumadhura.employeeservice.dto.CustomerProfileResponse;
import com.sumadhura.employeeservice.dto.DepartmentResponse;
import com.sumadhura.employeeservice.dto.EmployeeDetails;
import com.sumadhura.employeeservice.dto.EmployeeTicketResponse;
import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.GenericTicketSeekInfo;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.dto.TicketComment;
import com.sumadhura.employeeservice.dto.TicketEscalationLevelRequest;
import com.sumadhura.employeeservice.dto.TicketEscalationRequest;
import com.sumadhura.employeeservice.dto.TicketEscalationResponse;
import com.sumadhura.employeeservice.dto.TicketForwardMenu;
import com.sumadhura.employeeservice.dto.TicketForwardMenuResponse;
import com.sumadhura.employeeservice.dto.TicketResponse;
import com.sumadhura.employeeservice.dto.TicketSeekInfo;
import com.sumadhura.employeeservice.dto.TicketSeekInfoWrapper;
import com.sumadhura.employeeservice.dto.TicketTypeRequest;
import com.sumadhura.employeeservice.enums.Department;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.enums.MetadataId;
import com.sumadhura.employeeservice.enums.Roles;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.enums.Type;
import com.sumadhura.employeeservice.enums.Visibility;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;
import com.sumadhura.employeeservice.exception.InvalidStatusException;
import com.sumadhura.employeeservice.exception.SQLInsertionException;
import com.sumadhura.employeeservice.exception.TicketAssignFailedException;
import com.sumadhura.employeeservice.persistence.dao.BookingFormServiceDao;
import com.sumadhura.employeeservice.persistence.dao.EmployeeTicketDao;

import com.sumadhura.employeeservice.persistence.dto.AppRegistrationPojo;
import com.sumadhura.employeeservice.persistence.dto.ChangeTicketTypePojo;
import com.sumadhura.employeeservice.persistence.dto.CoApplicantPojo;
import com.sumadhura.employeeservice.persistence.dto.CustBookInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerAddressPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPojo;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DepartmentPojo;
import com.sumadhura.employeeservice.persistence.dto.DevicePojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsMailPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLeaveDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeeLevelDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.EmployeePojo;
import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;
import com.sumadhura.employeeservice.persistence.dto.LevelPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketCommentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketConversationDocumentsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscLevelMapPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscaLevelEmpMap;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationExtenstionApprovalLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelMappingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketExtendedEscalationApprovalPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketForwardMenuPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPendingDeptDtlsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketReportingPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketSeekInfoRequestPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypeDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypePojo;
import com.sumadhura.employeeservice.persistence.dto.TicketTypesPojo;

import com.sumadhura.employeeservice.service.dto.BookingFormRequest;
import com.sumadhura.employeeservice.service.dto.EmployeeTicketRequestInfo;
import com.sumadhura.employeeservice.service.dto.NotificationRequestInfo;
import com.sumadhura.employeeservice.service.dto.PushNotificationInfo;
import com.sumadhura.employeeservice.service.helpers.EmployeeTicketServiceHelper;
import com.sumadhura.employeeservice.service.mappers.EmployeeTicketMapper;
import com.sumadhura.employeeservice.util.Base64FileUtil;
import com.sumadhura.employeeservice.util.IOSPushNotificationUtil;
import com.sumadhura.employeeservice.util.Page;
import com.sumadhura.employeeservice.util.PushNotificationUtil;
import com.sumadhura.employeeservice.util.ResponceCodesUtil;
import com.sumadhura.employeeservice.util.TimeUtil;
import com.sumadhura.employeeservice.util.Util;

import lombok.NonNull;

/**
 * EmployeeTicketServiceImpl class provides Implementation for
 * EmployeeTicketService.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:47PM
 */
@Service("EmployeeTicketServiceImpl")
public class EmployeeTicketServiceImpl implements EmployeeTicketService {

	@Autowired(required = true)
	@Qualifier("EmployeeTicketDaoImpl")
	private EmployeeTicketDao employeeTicketDaoImpl;

	@Autowired(required = true)
	@Qualifier("EmployeeTicketServiceHelper")
	private EmployeeTicketServiceHelper employeeTicketServiceHelper;

	

	@Autowired(required = true)
	@Qualifier("BookingFormServiceDaoImpl")
	private BookingFormServiceDao bookingFormServiceDaoImpl;

	/*
	 * @Autowired(required = true) private SiteService siteService;
	 */

	@Autowired(required = true)
	@Qualifier("mailService")
	private MailService mailServiceImpl;

	@Autowired(required = true)
	private ResponceCodesUtil responceCodesUtil;

	@Autowired(required = true)
	private EmployeeTicketMapper mapper;
	
	

	/*
	 * @Autowired(required = true) private SiteService siteService;
	 */

	private static final Logger LOGGER = Logger.getLogger(EmployeeTicketServiceImpl.class);

	@Override
	public EmployeeTicketResponse getTicketList(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) throws InterruptedException, ExecutionException, InvalidStatusException {
		LOGGER.info("******* The control inside of the getTicketList  in  EmployeeTicketServiceImpl ********");
		final EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
		Set<Long> ticketTypeDetailsId = new HashSet<Long>();
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();
		//final EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		/* setting employee Accessed SitList */
		employeeTicketResponse.setSiteIds(employeeTicketRequestInfo.getSiteIds());
		/* Getting Ticket Type Details Ids by Employee Details Id */
		if (Util.isNotEmptyObject(employeeDetailsPojos)) {
			employeeTicketRequestInfo.setEmployeeDetailsId(employeeDetailsPojos.size() == 1 ? employeeDetailsPojos.get(0).getEmpDetailsId() : 0l);
			/* if it is coming from crm employee only we will see the TicketType details */
			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) 
				&& ((employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")
				|| (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets"))))) {
				List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = new ArrayList<TicketTypeDetailsPojo>();
				
				ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);
				/* Adding Ticket Type Details Ids */
				if (!ticketTypeDetailsPojos.isEmpty()) {
					for (TicketTypeDetailsPojo typeDetailsPojo : ticketTypeDetailsPojos) {
						if (Util.isNotEmptyObject(typeDetailsPojo.getTicketTypeDetailsId())) {
							/* TicketType Details Id is more when he deals Multiple Process Types */
							ticketTypeDetailsId.add(typeDetailsPojo.getTicketTypeDetailsId());
						}
					}
					/* setting ticket type details Id */
					employeeTicketRequestInfo.setTicketTypeDetailsIds(ticketTypeDetailsId);
				}
			}
			/* Getting Ticket List based on the ticket owners i.e ticket type details ids */
			if (Util.isNotEmptyObject(ticketTypeDetailsId)) {
				//Future<List<TicketPojo>> future = employeeTicketServiceHelper.getTicketList(employeeTicketRequestInfo);
				ticketResponses.addAll(getTicketResponse(employeeTicketRequestInfo));
				/* getting all tickets irrespective to the pagination */
				/*while (true) {
					if (future.isDone()) {
						employeeTicketResponse.setTotalTicketResponseList(mapper.ticketPojosToTicketResponses(future.get()));
						break;
					}
				}*/
			} else {
				/* Employee doesn't have ticketTypeDetailsId and have multiple siteId's */
				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getSiteIds())) {
					//Future<List<TicketPojo>> future = employeeTicketServiceHelper.getTicketList(employeeTicketRequestInfo);
					ticketResponses.addAll(getTicketResponse(employeeTicketRequestInfo));
					/* getting all tickets irrespective to the pagination */
					/*(while (true) {
						if (future.isDone()) {
							employeeTicketResponse
									.setTotalTicketResponseList(mapper.ticketPojosToTicketResponses(future.get()));
							break;
						}
					}*/
				}
			}
			/* removing duplicate tickets */
			Set<TicketResponse> ticketResponseSet = new LinkedHashSet<TicketResponse>(ticketResponses);

			/* Adding Ticket Responces */
			employeeTicketResponse.setTicketResponseList(new ArrayList<TicketResponse>(ticketResponseSet));

			/* adding pageCount */
			employeeTicketResponse.setPageCount(Util.isNotEmptyObject(employeeTicketRequestInfo.getPageCount())? employeeTicketRequestInfo.getPageCount(): 0l);
			
			/* setting rowCount */
			employeeTicketResponse.setRowCount(Util.isNotEmptyObject(employeeTicketRequestInfo.getRowCount())?employeeTicketRequestInfo.getRowCount(): 0l);

			/*if (employeeTicketResponse.getTotalTicketResponseList() != null && !employeeTicketResponse.getTotalTicketResponseList().isEmpty()) {
				addAdditionalInfo(employeeTicketResponse, true);
			}*/
		}
		return employeeTicketResponse;
	}

	private List<TicketResponse> getTicketResponse(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The Control is inside the getTicketResponse in EmployeeTicketServiceImpl ****");
		Page<TicketPojo> page = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE,
				Util.isNotEmptyObject(employeeTicketRequestInfo.getPageNo())
						? employeeTicketRequestInfo.getPageNo().intValue()
						: 1,
				Util.isNotEmptyObject(employeeTicketRequestInfo.getPageSize())
						? employeeTicketRequestInfo.getPageSize().intValue()
						: 1);
		employeeTicketRequestInfo.setPageCount(Integer.valueOf(page.getPagesAvailable()).longValue());
		employeeTicketRequestInfo.setRowCount(Util.isNotEmptyObject(page.getRowCount())?page.getRowCount():0l);
		List<TicketPojo> employeeTicketList = page.getPageItems();
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();
		for (TicketPojo pojo : employeeTicketList) {
			/*employeeTicketRequestInfo.setFlatBookingId(pojo.getFlatBookingId() != null ? pojo.getFlatBookingId() : 0l);
			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = employeeTicketDaoImpl
					.getCustomerPropertyDetails(employeeTicketRequestInfo, Status.ACTIVE);
			if (Util.isNotEmptyObject(customerPropertyDetailsPojos)) {
				/**
				 * if departmentId is given then ticket is pending at the department otherwise
				 * pending at employee
				 */
				/*List<DepartmentPojo> departmentPojos = null;
				List<EmployeeDetailsPojo> employeeDetailsPojos = null;
				List<EmployeePojo> employeePojos = null;

				/* if ticket is escalated get employee name from Ticket escalation table 
				if (Util.isNotEmptyObject(pojo.getEstimatedResolvedDateStatus())
						&& pojo.getEstimatedResolvedDateStatus().equals(Status.ESCALATED.getStatus())) {
					EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
					info.setRequestUrl("getEscaltedEmployee");
					info.setTicketId(Util.isNotEmptyObject(pojo.getTicketId()) ? pojo.getTicketId() : 0l);
					employeePojos = employeeTicketDaoImpl.getEmployee(info, Status.ACTIVE);
					Object obj = getPendingTicket(pojo);
					if(obj instanceof DepartmentPojo){
						departmentPojos = new ArrayList<>();
						DepartmentPojo obj1 = (DepartmentPojo) obj;
						for(EmployeePojo obj2 : employeePojos ) {
							//obj2.setEmployeeId(Util.isNotEmptyObject(obj1.getDepartmentId())?obj1.getDepartmentId():0l);
							obj1.setDepartmentName(obj2.getEmployeeName());
						}
						departmentPojos.add(obj1);
					}else if(obj instanceof EmployeePojo) {
						EmployeePojo obj1 = (EmployeePojo) obj;
						for(EmployeePojo obj2 : employeePojos ) {
							//obj2.setEmployeeId(Util.isNotEmptyObject(obj1.getEmployeeId())?obj1.getEmployeeId():0l);
							obj1.setEmployeeName(obj2.getEmployeeName());
						}
						employeePojos = new ArrayList<>();
						employeePojos.add(obj1);
					}else {
						for(EmployeePojo obj2 : employeePojos ) {
							obj2.setEmployeeId(0l);
						}
					}
				} else {
					if (pojo.getDepartmentId() != null && !pojo.getDepartmentId().equals(0l)) {
						EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
						infoObj.setDepartmentId(pojo.getDepartmentId());
						departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
					} else if (pojo.getAssignmentTo() != null && !pojo.getAssignmentTo().equals(0l)) {
						EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
						infoObj.setTicketTypeDetailsId(
								Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId()
										: 0l);
						infoObj.setRequestUrl("getCustomerTicketDetails");
						List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
								.getTicketTypeDetails(infoObj, Status.ACTIVE);
						infoObj.setDepartmentId(
								Util.isNotEmptyObject(ticketTypeDetailsPojos)
										? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
												? ticketTypeDetailsPojos.get(0).getDepartmentId()
												: 0l
										: 0l);
						List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
						infoObj.setEmployeeId(pojo.getAssignmentTo() != null ? pojo.getAssignmentTo() : 0l);
						employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
						employeePojos.get(0).setEmployeeName(
								employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName());
					} else {
						EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
						infoObj.setTicketTypeDetailsId(
								Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId()
										: 0l);
						infoObj.setRequestUrl("getCustomerTicketDetails");
						List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
								.getTicketTypeDetails(infoObj, Status.ACTIVE);

						if (Util.isNotEmptyObject(ticketTypeDetailsPojos)) {
							infoObj.setDepartmentId(
									Util.isNotEmptyObject(ticketTypeDetailsPojos)
											? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
													? ticketTypeDetailsPojos.get(0).getDepartmentId()
													: 0l
											: 0l);
							List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj,
									Status.ACTIVE);
							infoObj.setEmployeeDetailsId(Util.isNotEmptyObject(ticketTypeDetailsPojos)
									? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())
											? ticketTypeDetailsPojos.get(0).getEmployeeDetailsId()
											: 0l
									: 0l);
							employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(infoObj, Status.ACTIVE);
							employeeTicketRequestInfo
									.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)
											? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
													? employeeDetailsPojos.get(0).getEmployeeId()
													: 0l
											: 0l);
							employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
							employeePojos.get(0).setEmployeeName(
									employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName());
						}
					}
				}
				if ((departmentPojos != null && !departmentPojos.isEmpty())
						|| (employeePojos != null && !employeePojos.isEmpty())) {
					employeeTicketRequestInfo
							.setTicketTypeId(pojo.getTicketTypeId() != null ? pojo.getTicketTypeId() : 0l);
					List<TicketTypePojo> ticketTypePojos = employeeTicketDaoImpl
							.getTicketTypeInfo(employeeTicketRequestInfo, Status.ACTIVE);
					if (!ticketTypePojos.isEmpty()) {
						employeeTicketRequestInfo.setTicketId(pojo.getTicketId() != null ? pojo.getTicketId() : 0l);
						EmployeeTicketMapper mapper = new EmployeeTicketMapper();
						List<EmployeePojo> ticketOwnerPojo = employeeTicketDaoImpl.getTicketOwnerEmployee(pojo,
								Status.ACTIVE);   */
						/*TicketResponse ticketResponse = mapper
								.employeeTicketList$ticketEscalationPojos$customerPropertyDetailsPojos$departmentPojos$ticketTypePojos$EmployeeDetailsPojosToemployeeTicketResponse(
										pojo, customerPropertyDetailsPojos, departmentPojos, ticketTypePojos,
										employeeDetailsPojos, employeePojos, ticketOwnerPojo); */
						//TicketResponse ticketResponse = mapper.employeeTicketPojoToTicketResponse(pojo);
						
						/* to view closed tickets we need to show who closed the ticket 
						if(Util.isNotEmptyObject(employeeTicketRequestInfo.getType()) && employeeTicketRequestInfo.getType().equalsIgnoreCase("closedTickets")
							&& Util.isNotEmptyObject(pojo) && Util.isNotEmptyObject(pojo.getStatusUpdateType())) {
							if(pojo.getStatusUpdateType().equals(MetadataId.CUSTOMER.getId())) {
								String customerName = Util.isEmptyObject(ticketResponse.getCustomerPropertyDetails())?"N/A":
									Util.isEmptyObject(ticketResponse.getCustomerPropertyDetails().getCustomerName())?"N/A":ticketResponse.getCustomerPropertyDetails().getCustomerName();
								ticketResponse.setTicketClosedBy(MetadataId.CUSTOMER.getName()+"-"+customerName);
							}else if(pojo.getStatusUpdateType().equals(MetadataId.EMPLOYEE.getId())) {
								EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
								infoObj.setEmployeeId(Util.isNotEmptyObject(pojo.getStatusUpdateBy())? pojo.getStatusUpdateBy() : 0l);
								List<EmployeePojo> closedEmployeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
								if(Util.isNotEmptyObject(closedEmployeePojos) && Util.isNotEmptyObject(closedEmployeePojos.get(0)) 
									&& Util.isNotEmptyObject(closedEmployeePojos.get(0).getEmployeeName())) {
									ticketResponse.setTicketClosedBy(MetadataId.EMPLOYEE.getName()+"-"+closedEmployeePojos.get(0).getEmployeeName());
								}else {
									ticketResponse.setTicketClosedBy(MetadataId.EMPLOYEE.getName()+"-N/A");
								}
							}
						}
						//ticketResponses.add(ticketResponse);
					}
				}
			}  */
            TicketResponse ticketResponse = mapper.employeeTicketPojoToTicketResponse(pojo);
            ticketResponses.add(ticketResponse);
		}
		return ticketResponses;
	}

	private EmployeeTicketResponse addAdditionalInfo(EmployeeTicketResponse employeeTicketResponse, boolean isEmployee)
			throws InvalidStatusException {
		LOGGER.info("******* The control inside of the addAdditionalInfo  in  EmployeeTicketServiceImpl ********");
		Integer TOTAL_TICKETS = Util.isNotEmptyObject(employeeTicketResponse.getTotalTicketResponseList())
				? employeeTicketResponse.getTotalTicketResponseList().size()
				: 0;
		Integer NEW = 0;
		Integer OPEN = 0;
		Integer INPROGRESS = 0;
		Integer CLOSED = 0;
		Integer REOPEN = 0;
		Integer REPLIED = 0;
		Integer ESCALATED = 0;

		for (TicketResponse response : employeeTicketResponse.getTotalTicketResponseList()) {
			if (isEmployee) {
				/*
				 * if(Util.isNotEmptyObject(response.getEstimatedResolvedDateStatus()) &&
				 * response.getEstimatedResolvedDateStatus().equals(Status.ESCALATED.getStatus()
				 * )){ ESCALATED+=1; }else {
				 */
				if (Util.isNotEmptyObject(response.getTicketStatusId())
						&& (response.getTicketStatusId().equals(Status.INPROGRESS.getStatus())
								|| response.getTicketStatusId().equals(Status.CLOSED.getStatus()))) {

					if (Util.isNotEmptyObject(response.getDepartmentTicketStatusId())
							? response.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())
							: false) {
						// OPEN+= 1;
						INPROGRESS += 1;
					} else if ((Util.isNotEmptyObject(response.getDepartmentTicketStatusId())
							? response.getDepartmentTicketStatusId().equals(Status.INPROGRESS.getStatus())
							: false)) {
						INPROGRESS += 1;
					} else if ((Util.isNotEmptyObject(response.getDepartmentTicketStatusId())
							? response.getDepartmentTicketStatusId().equals(Status.CLOSED.getStatus())
							: false)) {
						CLOSED += 1;
					} else if ((Util.isNotEmptyObject(response.getDepartmentTicketStatusId())
							? response.getDepartmentTicketStatusId().equals(Status.NEW.getStatus())
							: false)) {
						// NEW+=1;
						INPROGRESS += 1;
					} else if ((Util.isNotEmptyObject(response.getDepartmentTicketStatusId())
							? response.getDepartmentTicketStatusId().equals(Status.REPLIED.getStatus())
							: false)) {
						REPLIED += 1;
					} else {
						INPROGRESS += 1;
						// throw new
						// InvalidStatusException(HttpStatus.invalidStatusCode.getErrorMsgs());
					}
				} else {
					if (response.getTicketStatusId().equals(Status.NEW.getStatus())) {
						NEW += 1;
					} else if (response.getTicketStatusId().equals(Status.OPEN.getStatus())) {
						OPEN += 1;
					} else if (response.getTicketStatusId().equals(Status.REOPEN.getStatus())) {
						REOPEN += 1;
					} else if (response.getTicketStatusId().equals(Status.CLOSED.getStatus())) {
						CLOSED += 1;
					}
				}
			} else {
				if (response.getTicketStatusId().equals(Status.CLOSED.getStatus())) {
					CLOSED += 1;
				} else {
					OPEN += 1;
				}
			}
			
			/* For Counting Escalated Tickets which are in not closed state */
			if(!Status.CLOSED.getStatus().equals(response.getTicketStatusId()) || !Status.CLOSED.getStatus().equals(response.getDepartmentTicketStatusId())) {
				if(Status.ESCALATED.status.equals(response.getEstimatedResolvedDateStatus())) {
					ESCALATED +=1;					
				}
			}
		}
		employeeTicketResponse.setTotalTickets(TOTAL_TICKETS);
		employeeTicketResponse.setNewState(NEW);
		employeeTicketResponse.setOpen(OPEN);
		employeeTicketResponse.setInProgress(INPROGRESS);
		employeeTicketResponse.setClosed(CLOSED);
		employeeTicketResponse.setReOpen(REOPEN);
		employeeTicketResponse.setReplied(REPLIED);
		employeeTicketResponse.setTotalOpen(TOTAL_TICKETS-CLOSED);
		employeeTicketResponse.setEscalated(ESCALATED);
		employeeTicketResponse.setTotalTicketResponseList(null);
		return employeeTicketResponse;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public EmployeeTicketResponse getTicketDtls(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InvalidStatusException, IllegalAccessException, InvocationTargetException, InterruptedException,
			ExecutionException {
		LOGGER.info("******* The control inside of the getTicketDtls  in  EmployeeTicketServiceImpl ********"
				+ Thread.currentThread().getName());
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();
		// Future<Boolean> isTicketOwner = null;
		// Future<Boolean> isTicketPresent = null;
		Boolean isTicketOwner = false;
		Boolean isTicketPresent = false;

		/* get Ticket specific to TicketId */
		List<TicketPojo> ticketPojoList = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE);
		if (Util.isNotEmptyObject(ticketPojoList)) {
			//EmployeeTicketRequestInfo requestInfo = null;
			/* checking ticket owner status */
			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsId()) && Util.isNotEmptyObject(employeeTicketRequestInfo.getEmployeeId())) {
				//requestInfo = new EmployeeTicketRequestInfo();
				//BeanUtils.copyProperties(requestInfo, employeeTicketRequestInfo);
				System.out.println("****Thread name****" + Thread.currentThread().getName());
				isTicketOwner = employeeTicketServiceHelper.isTicketOwnerByEmpId(employeeTicketRequestInfo);
			}

			/* is Ticket owner update read and unread */
			/* if ticket is present at login employee then only update ticket status */
			if (Util.isEmptyObject(ticketPojoList.get(0).getDepartmentId())
					&& Util.isEmptyObject(ticketPojoList.get(0).getAssignmentTo())) {

				/*
				 * while (true) { if (isTicketOwner.isDone()) { if (isTicketOwner.get()) { //
				 * updating ticket status New to Open
				 * updateTicketStatus(employeeTicketRequestInfo, ticketPojoList.get(0)); }
				 * break; } }
				 */
				if (Util.isNotEmptyObject(isTicketOwner) ? isTicketOwner : false) {
					/* if ticket is present at me and it is at new state only update status. */
					if ((Util.isNotEmptyObject(ticketPojoList.get(0).getTicketStatusId())
							&& ticketPojoList.get(0).getTicketStatusId().equals(Status.NEW.getStatus()))
							|| (Util.isNotEmptyObject(ticketPojoList.get(0).getDepartmentTicketStatusId())
									&& ticketPojoList.get(0).getDepartmentTicketStatusId()
											.equals(Status.NEW.getStatus()))) {
						updateTicketStatus(employeeTicketRequestInfo, ticketPojoList.get(0));
						/* inserting into Ticket statistics table */
						insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
					}
				}
			} else {
				isTicketPresent = employeeTicketServiceHelper.isTicketPresent(employeeTicketRequestInfo, ticketPojoList);
				/*
				 * while (true) { if (isTicketPresent.isDone()) { if (isTicketPresent.get()) {
				 * // updating ticket status New to Open
				 * updateTicketStatus(employeeTicketRequestInfo, ticketPojoList.get(0)); }
				 * break; } }
				 */

				if (Util.isNotEmptyObject(isTicketPresent) ? isTicketPresent : false) {
					/* if ticket is present at me and it is at new state only update status. */
					if ((Util.isNotEmptyObject(ticketPojoList.get(0).getTicketStatusId())
							&& ticketPojoList.get(0).getTicketStatusId().equals(Status.NEW.getStatus()))
							|| (Util.isNotEmptyObject(ticketPojoList.get(0).getDepartmentTicketStatusId())
									&& ticketPojoList.get(0).getDepartmentTicketStatusId()
											.equals(Status.NEW.getStatus()))) {
						updateTicketStatus(employeeTicketRequestInfo, ticketPojoList.get(0));
						/* inserting into Ticket statistics table */
						insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
					}
				}
			}

			EmployeeTicketMapper mapper = new EmployeeTicketMapper();
			employeeTicketRequestInfo.setFlatBookingId(
					ticketPojoList.get(0).getFlatBookingId() != null ? ticketPojoList.get(0).getFlatBookingId() : 0l);
			/* here customer raised ticket List for this specific flat. */
			//List<TicketPojo> customerRaisedTicketPojoList = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE);
			//EmployeeTicketResponse customerTicketResponse = addAdditionalInfo(mapper.employeeTicketListToemployeeTicketResponse(customerRaisedTicketPojoList), false);
			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = employeeTicketDaoImpl.getCustomerPropertyDetails(employeeTicketRequestInfo, Status.ACTIVE);
			List<TicketCommentsPojo> ticketCommentsPojos = employeeTicketDaoImpl.getTicketComments(employeeTicketRequestInfo, Status.ACTIVE);

			/* getting TicketType and pending department and TicketStatus */
			EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
			info.setTicketId(employeeTicketRequestInfo.getTicketId());
			info.setRequestUrl("getTicket");
			//List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
			List<TicketPojo> ticketPojos = ticketPojoList ;
			info.setTicketTypeId(Util.isNotEmptyObject(ticketPojos) ? Util.isNotEmptyObject(ticketPojos.get(0))? Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeId()) ? ticketPojos.get(0).getTicketTypeId(): 0l: 0l : 0l);
			List<TicketTypePojo> ticketTypePojos = employeeTicketDaoImpl.getTicketTypeInfo(info, Status.ACTIVE);

			/*
			 * if departmentId is given then ticket is pending at the department otherwise
			 * pending at employee
			 */
			TicketPojo pojo = ticketPojos.get(0);
			List<DepartmentPojo> departmentPojos = null;
			List<EmployeeDetailsPojo> EmployeeDetailsPojos = null;
			List<EmployeePojo> employeePojos = null;

			/* if ticket is escalated get employee name from Ticket escalation table */
			if (Util.isNotEmptyObject(pojo.getEstimatedResolvedDateStatus())
					&& pojo.getEstimatedResolvedDateStatus().equals(Status.ESCALATED.getStatus())) {
				EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
				infoObj.setRequestUrl("getEscaltedEmployee");
				infoObj.setTicketId(Util.isNotEmptyObject(pojo.getTicketId()) ? pojo.getTicketId() : 0l);
				employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
				Object obj = getPendingTicket(pojo);
				if(obj instanceof DepartmentPojo){
					departmentPojos = new ArrayList<>();
					DepartmentPojo obj1 = (DepartmentPojo) obj;
					for(EmployeePojo obj2 : employeePojos ) {
						//obj2.setEmployeeId(Util.isNotEmptyObject(obj1.getDepartmentId())?obj1.getDepartmentId():0l);
						obj1.setDepartmentName(obj2.getEmployeeName());
					}
					departmentPojos.add(obj1);
				}else if(obj instanceof EmployeePojo) {
					EmployeePojo obj1 = (EmployeePojo) obj;
					for(EmployeePojo obj2 : employeePojos ) {
						//obj2.setEmployeeId(Util.isNotEmptyObject(obj1.getEmployeeId())?obj1.getEmployeeId():0l);
						obj1.setEmployeeName(obj2.getEmployeeName());
					}
					employeePojos = new ArrayList<>();
					employeePojos.add(obj1);
				}else {
					for(EmployeePojo obj2 : employeePojos ) {
						obj2.setEmployeeId(0l);
					}
				}
			} else {
				if (pojo.getDepartmentId() != null && !pojo.getDepartmentId().equals(0l)) {
					EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
					infoObj.setDepartmentId(pojo.getDepartmentId());
					departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
				} else if (pojo.getAssignmentTo() != null && !pojo.getAssignmentTo().equals(0l)) {
					EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
					infoObj.setTicketTypeDetailsId(Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
					infoObj.setRequestUrl("getCustomerTicketDetails");
					/*List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
							.getTicketTypeDetails(infoObj, Status.ACTIVE);
					infoObj.setDepartmentId(
							Util.isNotEmptyObject(ticketTypeDetailsPojos)
									? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
											? ticketTypeDetailsPojos.get(0).getDepartmentId()
											: 0l
									: 0l);
					List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
					infoObj.setEmployeeId(pojo.getAssignmentTo() != null ? pojo.getAssignmentTo() : 0l);
					employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
					employeePojos.get(0).setEmployeeName(
							employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName());*/
					infoObj.setEmployeeId(pojo.getAssignmentTo() != null ? pojo.getAssignmentTo() : 0l);
					employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
					
				} else {
					EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
					infoObj.setTicketTypeDetailsId(Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
					/*infoObj.setRequestUrl("getCustomerTicketDetails");
					List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
							.getTicketTypeDetails(infoObj, Status.ACTIVE);
					infoObj.setDepartmentId(
							Util.isNotEmptyObject(ticketTypeDetailsPojos)
									? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
											? ticketTypeDetailsPojos.get(0).getDepartmentId()
											: 0l
									: 0l);
					List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
					infoObj.setEmployeeDetailsId(
							Util.isNotEmptyObject(ticketTypeDetailsPojos)
									? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())
											? ticketTypeDetailsPojos.get(0).getEmployeeDetailsId()
											: 0l
									: 0l);
					EmployeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(infoObj, Status.ACTIVE);
					infoObj.setEmployeeId(
							Util.isNotEmptyObject(EmployeeDetailsPojos)
									? Util.isNotEmptyObject(EmployeeDetailsPojos.get(0).getEmployeeId())
											? EmployeeDetailsPojos.get(0).getEmployeeId()
											: 0l
									: 0l); 
					employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
					employeePojos.get(0).setEmployeeName(employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName()); */
					infoObj.setRequestUrl("getTicketOwnerName");
					employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
				}
			}
			List<TicketComment> ticketComments = new ArrayList<TicketComment>();
			if (!ticketCommentsPojos.isEmpty()) {
				for (TicketCommentsPojo ticketCommentsPojo : ticketCommentsPojos) {
					if (ticketCommentsPojo.getFromType().equals(Department.CUSTOMER.getId())) {
						//employeeTicketRequestInfo.setCustomerId(ticketCommentsPojo.getFromId() != null ? ticketCommentsPojo.getFromId() : 0l);
						//List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo, Status.ACTIVE);
						ticketComments.add(mapper.TicketCommentsPojoToTicketComments(ticketCommentsPojo, customerPropertyDetailsPojos));
					} else if (ticketCommentsPojo.getFromType().equals(Department.SYSTEM.getId())) {
						ticketComments.add(mapper.TicketCommentsPojoToTicketComments(ticketCommentsPojo, Department.SYSTEM));
					} else {
						/*employeeTicketRequestInfo.setEmployeeId(
								ticketCommentsPojo.getFromId() != null ? ticketCommentsPojo.getFromId() : 0l);
						List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl
								.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
						employeeTicketRequestInfo
								.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)
										? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
												? employeeDetailsPojos.get(0).getEmployeeId()
												: 0l
										: 0l);
						List<EmployeePojo> employeePojoObjs = employeeTicketDaoImpl
								.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
						ticketComments.add(mapper.TicketCommentsPojoToTicketComments(employeeDetailsPojos,ticketCommentsPojo, employeePojoObjs)); */
						ticketComments.add(mapper.TicketCommentsPojoToTicketComments(null,ticketCommentsPojo, null));
					}
				}
			}
			TicketResponse ticketResponse = mapper.TicketPojo$CustomerPropertyDetailsPojo$TicketComment(ticketPojos,
					customerPropertyDetailsPojos, ticketComments, ticketTypePojos, departmentPojos,
					EmployeeDetailsPojos, employeePojos);
			// urList.stream().distinct().collect(Collectors.toList());
			/*EmployeeTicketRequestInfo fileEmployeeTicketRequestInfo = new EmployeeTicketRequestInfo();
			fileEmployeeTicketRequestInfo.setTicketId(employeeTicketRequestInfo.getTicketId());

			Future<List<FileInfo>> fileInfos = employeeTicketServiceHelper
					.createDocumentUrls(fileEmployeeTicketRequestInfo); */

			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketTypeDetailsId())) {
				/*
				 * do { // adding ticket FileInfos
				 *//*
					 * adding Ticket owner status. isTicketOwner =
					 * employeeTicketServiceHelper.isTicketOwner(requestInfo); if
					 * (isTicketOwner.isDone()) {
					 * ticketResponse.setIsTicketOwner(isTicketOwner.get()); break; } } while
					 * (true);
					 */
				if (Util.isNotEmptyObject(isTicketOwner)) {
					ticketResponse.setIsTicketOwner(isTicketOwner);
				}
			}

			/*do {
				/* adding ticket FileInfos */
				/*if (fileInfos.isDone()) {
					ticketResponse.setFileInfos(fileInfos.get());
					break;
				}
			} while (true); */

			ticketResponses.add(ticketResponse);
			employeeTicketResponse.setTicketResponseList(ticketResponses);
			//employeeTicketResponse.setTotalTickets(customerTicketResponse.getTotalTickets());
			//employeeTicketResponse.setOpen(customerTicketResponse.getOpen());
			//employeeTicketResponse.setInProgress(customerTicketResponse.getInProgress());
			//employeeTicketResponse.setClosed(customerTicketResponse.getClosed());

		}
		return employeeTicketResponse;
	}

	@Override
	public CustomerProfileResponse getCustomerProfileDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(
				"******* The control inside of the getCustomerProfileDetails  in  EmployeeTicketServiceImpl ********");
		CustomerProfileResponse customerProfileResponse = new CustomerProfileResponse();

		List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo,
				Status.ACTIVE);
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();

		if (!customerPojos.isEmpty()) {
			List<CoApplicant> coApplicants = new ArrayList<CoApplicant>();
			List<CustomerAddressPojo> coApplicantAddressPojos = null;
			// employeeTicketRequestInfo.setCustomerAddressId(customerPojos.get(0).getAddressId()!=null?customerPojos.get(0).getAddressId():0l);
			List<CustomerAddressPojo> applicantAddressPojos = employeeTicketDaoImpl
					.getCustomerAddressDetails(employeeTicketRequestInfo, Status.ACTIVE);
			List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl
					.getFlatbookingDetails(employeeTicketRequestInfo, Status.ACTIVE);
			if (!flatBookingPojos.isEmpty()) {
				employeeTicketRequestInfo.setFlatBookingId(
						flatBookingPojos.get(0).getFlatBookingId() != null ? flatBookingPojos.get(0).getFlatBookingId()
								: 0l);
				List<CoApplicantPojo> coApplicantPojos = employeeTicketDaoImpl
						.getCoApplicantDetails(employeeTicketRequestInfo, Status.ACTIVE);

				if (!coApplicantPojos.isEmpty()) {
					for (CoApplicantPojo pojo : coApplicantPojos) {
						employeeTicketRequestInfo
								.setCustomerAddressId(pojo.getAddressId() != null ? pojo.getAddressId() : 0l);
						coApplicantAddressPojos = employeeTicketDaoImpl
								.getCustomerAddressDetails(employeeTicketRequestInfo, Status.ACTIVE);
						coApplicants.add(mapper.CoApplicantPojo$CustomerAddressPojo(pojo, coApplicantAddressPojos));
					}
				}
				customerProfileResponse = mapper.customerPojos$applicantAddressPojos$coApplicants(customerPojos,
						applicantAddressPojos, coApplicants);
			}
		}
		return customerProfileResponse;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result updateTicketConversation(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws Exception {
		LOGGER.info(
				"******* The control inside of the updateTicketConversation  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();
		employeeTicketRequestInfo
				.setDocumentsLocation(createTicketConversationDocumentsLocation(employeeTicketRequestInfo));
		/* from || to = customer public. */
		if (employeeTicketRequestInfo.getFromType().equals(Department.CUSTOMER.getId())
				|| Util.isNotEmptyObject(employeeTicketRequestInfo.getToType())
						? employeeTicketRequestInfo.getToType().equals(Department.CUSTOMER.getId())
						: false) {
			// if
			// (employeeTicketRequestInfo.getDepartmentId().equals(Department.CRM.getId())
			// && employeeTicketRequestInfo.getToType().equals(Department.CUSTOMER.getId()))
			// {
			employeeTicketRequestInfo.setVisibleType(Visibility.PUBLIC.getDescription());
		} else {
			employeeTicketRequestInfo.setVisibleType(Visibility.PRIVATE.getDescription());
		}
		Long record_No = employeeTicketDaoImpl.insertTicketConversationDocuments(employeeTicketRequestInfo,
				Status.ACTIVE);
		if (record_No != null && record_No >= 0) {
			employeeTicketRequestInfo.setTicketConversationDocumentId(record_No);
			Long record_NO = employeeTicketDaoImpl.insertTicketConversation(employeeTicketRequestInfo, Status.ACTIVE);

			/* updating ticket status. */
			if (record_NO != null && record_NO >= 0) {
				Result ticket_No = null;
				/* if request coming from updateconversation.spring */
				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo
						.getRequestUrl().equalsIgnoreCase("updateTicketConversation.spring")) {
					/*
					 * update only if it is coming from update Conversation service.( UPDATE only
					 * ticket status column only.)
					 */
					ticket_No = updateTicketStatus(employeeTicketRequestInfo);

					/* Inserting TicketStatistics table while updating the ticket table */
					if (Util.isNotEmptyObject(ticket_No)) {
						insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
					}
					/* sending mail to customer while CRM giveing reply to customer */
					employeeTicketServiceHelper.sendticketResponseMail(employeeTicketRequestInfo);
				}
				/* if request coming from forwardTicketDetails.spring */
				else {
					ticket_No = new Result();
					ticket_No.setResponseCode(HttpStatus.success.getResponceCode());
				}

				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo
						.getRequestUrl().equalsIgnoreCase("updateTicketConversation.spring")) {
					for (PushNotificationInfo pushNotificationInfo : createNotification(employeeTicketRequestInfo)) {
						final PushNotificationInfo info = pushNotificationInfo;
						Thread thread = new Thread() {
							public synchronized void run() {
								try {
									if (Util.isNotEmptyObject(info.getOsType())
											&& info.getOsType().equalsIgnoreCase(Type.ANDRIOD.getName())) {
										/*
										 * if request is coming from update and forward only we need to send push
										 * notification
										 */
										final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
										try {
										pushNotificationUtil.pushFCMNotification(info);
										}catch(Exception ex) {
											 LOGGER.error("**** The Error Message ****"+ex);
									    }
									} else if (Util.isNotEmptyObject(info.getOsType())
											&& info.getOsType().equalsIgnoreCase(Type.IOS.getName())) {
										/*
										 * if request is coming from update and forward only we need to send push
										 * notification
										 */
										final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
										try {
										ioSPushNotificationUtil.sendIosPushNotification(
												Arrays.asList(info.getDeviceToken()), ioSPushNotificationUtil.getTicketingPushNotificationPayLoad(info).toString(),
												false);
										}catch(Exception ex){
											 LOGGER.error("**** The Error Message ****"+ex);
									    }
									}
								} catch (Exception e) {
									LOGGER.error(
											"**** The Exception is raised while inserting record into the TicketStatics Table ****");
								}
							}
						};
						thread.setName("Ticket PushNotification Thread");
						thread.start();
					}
				}
				if (ticket_No != null && ticket_No.getResponseCode() >= 0) {
					final EmployeeTicketRequestInfo employeeTicketRequestInfoFinal = new EmployeeTicketRequestInfo();
					BeanUtils.copyProperties(employeeTicketRequestInfoFinal, employeeTicketRequestInfo);
					Thread thread = new Thread() {
						public synchronized void run() {
							LOGGER.debug("**** The control is inside upload ticket documents into the server *****");
							insertTicketConversationDocuments(employeeTicketRequestInfoFinal);
						}
					};
					thread.start();
					LOGGER.debug("**** The Ticket Status is updated successfully ! ****");
					result.setResponseCode(HttpStatus.success.getResponceCode());
					return result;
				} else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("***** The Exception is raised in while updating Ticket Status. *****");
					throw new SQLInsertionException(errorMsgs);
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation ******");
				throw new SQLInsertionException(errorMsgs);
			}

		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation Documents******");
			throw new SQLInsertionException(errorMsgs);
		}
	}

	@Override
	public TicketForwardMenuResponse getTicketForwardMenuDetails(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(
				"******* The control inside of the getTicketForwardMenuDetails  in  EmployeeTicketServiceImpl ********");
		TicketForwardMenuResponse ticketForwardMenuResponse = new TicketForwardMenuResponse();
		List<TicketForwardMenu> ticketForwardMenus = new ArrayList<TicketForwardMenu>();

		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		List<TicketForwardMenuPojo> forwardMenuPojos = employeeTicketDaoImpl
				.getTicketForwardMenuDetails(employeeTicketRequestInfo, Status.ACTIVE);

		if (!forwardMenuPojos.isEmpty()) {
			for (TicketForwardMenuPojo pojo : forwardMenuPojos) {
				if (pojo.getTypeOf().equals(Department.DEPARTMENT.getId())) {
					// employeeTicketRequestInfo.setDepartmentId(pojo.getGenericId()!=null ?
					// pojo.getGenericId() : 0l);
					employeeTicketRequestInfo.setDepartmentId((Util.isNotEmptyObject(pojo.getGenericId())
							&& (!pojo.getGenericId().equals(employeeTicketRequestInfo.getFromDeptId())))
									? pojo.getGenericId()
									: 0l);
					List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl
							.getDepartmentDetails(employeeTicketRequestInfo, Status.ACTIVE);
					if (!departmentPojos.isEmpty()) {
						TicketForwardMenu menu = mapper.ticketForwardMenuPojoToTicketForwardMenu(pojo, departmentPojos);
						ticketForwardMenus.add(menu);
					}
				} else if (pojo.getTypeOf().equals(Department.EMPLOYEE.getId())) {
					employeeTicketRequestInfo.setEmployeeId((Util.isNotEmptyObject(pojo.getGenericId())
							&& (!pojo.getGenericId().equals(employeeTicketRequestInfo.getFromId())))
									? pojo.getGenericId()
									: 0l);
					List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl
							.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
					if (!employeeDetailsPojos.isEmpty()) {
						employeeTicketRequestInfo
								.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)
										? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
												? employeeDetailsPojos.get(0).getEmployeeId()
												: 0l
										: 0l);
						List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
								Status.ACTIVE);
						TicketForwardMenu menu = mapper.ticketForwardMenuPojoToTicketForwardMenu(pojo, employeePojos);
						ticketForwardMenus.add(menu);
					}
				}
			}
		}
		if (!ticketForwardMenus.isEmpty()) {
			ticketForwardMenuResponse = mapper.ticketForwardMenusToTicketForwardMenuResponse(ticketForwardMenus);
		}
		return ticketForwardMenuResponse;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result forwardTicketDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) throws Exception {
		LOGGER.info("******* The control inside of the forwardTicketDetails  in  EmployeeTicketServiceImpl ********");
		Result res = new Result();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		/* Ticket is forwarded type is setting. */
		if (employeeTicketRequestInfo.getToDeptId() != null && !employeeTicketRequestInfo.getToDeptId().equals(0l)) {
			employeeTicketRequestInfo.setToType(Department.DEPARTMENT.getId());
			employeeTicketRequestInfo.setTypeOf(Department.DEPARTMENT.getId());
		} else {
			employeeTicketRequestInfo.setToType(Department.EMPLOYEE.getId());
			employeeTicketRequestInfo.setTypeOf(Department.EMPLOYEE.getId());
		}
		/* ----------------check toDept || toPerson------------------ */
		if (((Util.isNotEmptyObject(employeeTicketRequestInfo.getToId()))
				|| Util.isNotEmptyObject(employeeTicketRequestInfo.getToDeptId()))
				&& employeeTicketRequestInfo.getToType() != null) {
			if (((Util.isNotEmptyObject(employeeTicketRequestInfo.getToId()))
					|| Util.isNotEmptyObject(employeeTicketRequestInfo.getToDeptId()))
					&& employeeTicketRequestInfo.getToType() != null) {
				/* inserting Ticket Conversation */
				Result resObj = updateTicketConversation(employeeTicketRequestInfo);
				if (resObj.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
					/* updating Ticket assignmentTo&assignmentBy&department details */
					Integer result = employeeTicketDaoImpl.updateTicketDetails(employeeTicketRequestInfo,
							Status.ACTIVE);
					if (result > 0) {
						/* Inserting TicketStatistics table while updating the ticket table */
						insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);

						/* sending Ticket Forwarding mail. */
						employeeTicketServiceHelper.sendTicketForwardMail(employeeTicketRequestInfo);
						/* ToClean up heap space we are calling gc. */
						System.gc();
						/* updating TicketStatistics table */
						if (Util.isNotEmptyObject(employeeTicketRequestInfo.getType())
								? employeeTicketRequestInfo.getType().equalsIgnoreCase(Department.PM.getName())
								: false) {
							employeeTicketRequestInfo.setRequestUrl("forwardTicketDetails.spring");
							/* get Extended time dynamically from backend. */
							List<TicketEscalationLevelMappingPojo> levelMappingPojos = employeeTicketDaoImpl
									.getTicketEscaltionAprovalLevelDetails(employeeTicketRequestInfo, Status.ACTIVE);

							/* setting extended time */
							employeeTicketRequestInfo
									.setExtendedTime(Util.isNotEmptyObject(levelMappingPojos)
											? Util.isNotEmptyObject(levelMappingPojos.get(0).getTicketExtendedTime())
													? levelMappingPojos.get(0).getTicketExtendedTime()
													: 0l
											: 0l);

							TicketPojo pojo = mapper
									.employeeTicketRequestInfoToTicketPojoSystemEscalationUpdateEstimatedResolvedDate(
											employeeTicketRequestInfo);
							Long NoOfUpdatedRows = employeeTicketDaoImpl.updateTicketEstimatedResolvedDate(pojo);
							/* Updating the TicketEscalation status */
							Long ticketEscalationStatus = employeeTicketDaoImpl.updateTicketEscalationStatus(
									mapper.employeeTicketInfoToTicketEscalationPojo(employeeTicketRequestInfo));
							if (Util.isNotEmptyObject(NoOfUpdatedRows) && Util.isNotEmptyObject(ticketEscalationStatus))
								/* Inserting TicketStatistics table while updating the ticket table */
								insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
							LOGGER.debug("**** The Noumber of rows Updated is ****" + NoOfUpdatedRows);
						}

						final EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
						BeanUtils.copyProperties(info, employeeTicketRequestInfo);
						/* sending ticket forward push notification */
						Thread thread = new Thread() {
							public synchronized void run() {
								try {
									/* sending ticket forwarding notification */
									sendTicketForwardNotification(info);
								} catch (Exception e) {
									LOGGER.error(
											"**** The Exception is raised while inserting record into the TicketStatics Table ****");
								}
							}
						};
						thread.setName("Ticket PushNotification Thread");
						thread.start();

						LOGGER.info("*** The Ticket details is updated successfully ***");
						res.setResponseCode(HttpStatus.success.getResponceCode());
						return res;
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("The error occurring while assigning Ticket to another Employee (or) Department.");
						throw new TicketAssignFailedException(errorMsgs);
					}
				} else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation ******");
					throw new SQLInsertionException(errorMsgs);
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("The Insufficient Input is given for Ticket Conversation details service.");
				throw new InSufficeientInputException(errorMsgs);
			}
		} else {
			employeeTicketRequestInfo.setRequestUrl("getTicket");
			List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo,
					Status.ACTIVE);
			if (!ticketPojos.isEmpty() && ((Util.isNotEmptyObject(ticketPojos.get(0).getAssignmentTo())
					? ticketPojos.get(0).getAssignmentTo().equals(employeeTicketRequestInfo.getFromId())
					: false)
					|| ((Util.isNotEmptyObject(ticketPojos.get(0).getDepartmentId())
							? ticketPojos.get(0).getDepartmentId().equals(employeeTicketRequestInfo.getFromDeptId())
							: false)))) {
				/* Ticket is assigning back to TicketOwner */
				if (Util.isNotEmptyObject(ticketPojos)
						&& Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeDetailsId())) {
					EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
					info.setTicketTypeDetailsId(ticketPojos.get(0).getTicketTypeDetailsId());
					info.setRequestUrl("changeTicketOwner");
					List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(info,
							Status.ACTIVE);
					employeeTicketRequestInfo
							.setToId(Util.isNotEmptyObject(employeeDetailsPojos)
									? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
											? employeeDetailsPojos.get(0).getEmployeeId()
											: 0l
									: 0l);
				}
				// employeeTicketRequestInfo.setToId(ticketPojos.get(0).getAssignedBy()!=null?ticketPojos.get(0).getAssignedBy():0l);
				// employeeTicketRequestInfo.setToDeptId(ticketPojos.get(0).getDepartmentId()!=null?ticketPojos.get(0).getDepartmentId():0l);
				if ((Util.isNotEmptyObject(employeeTicketRequestInfo.getToId())
						|| Util.isNotEmptyObject(employeeTicketRequestInfo.getToDeptId() != null))
						&& employeeTicketRequestInfo.getToType() != null
						&& employeeTicketRequestInfo.getMessage() != null) {
					/* inserting Ticket Conversation */
					employeeTicketRequestInfo.setRequestUrl("forwardTicketDetails.spring");
					Result ticketConversationResult = updateTicketConversation(employeeTicketRequestInfo);
					if (ticketConversationResult.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
						/* If Ticket is Replied Back To The Crm */
						employeeTicketRequestInfo.setType(Status.REPLIED.getDescription());
						Integer result = employeeTicketDaoImpl.updateTicketDetails(employeeTicketRequestInfo,
								Status.ACTIVE);
						if (result > 0 /* && ticketStatistics >0 */) {
							/* Inserting TicketStatistics table while updating the ticket table */
							insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);

							/* sending Ticket Forwarding mail. */
							employeeTicketServiceHelper.sendTicketForwardMail(employeeTicketRequestInfo);

							final EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
							BeanUtils.copyProperties(info, employeeTicketRequestInfo);
							/* sending ticket forward push notification */
							Thread thread = new Thread() {
								public synchronized void run() {
									try {
										/* sending ticket forwarding notification */
										sendTicketForwardNotification(info);
									} catch (Exception e) {
										LOGGER.error(
												"**** The Exception is raised while inserting record into the TicketStatics Table ****");
									}
								}
							};
							thread.setName("Ticket PushNotification Thread");
							thread.start();

							LOGGER.info("*** The Ticket details is updated successfully ***");
							res.setResponseCode(HttpStatus.success.getResponceCode());
							return res;
						} else {
							List<String> errorMsgs = new ArrayList<String>();
							errorMsgs.add(
									"The error occurring while assigning Ticket to another Employee (or) Department.");
							throw new TicketAssignFailedException(errorMsgs);
						}
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs.add(
								"The Exception is raised while storing ticket conversation in TicketForwardDetails. ");
						throw new SQLInsertionException(errorMsgs);
					}
				} else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("The Insufficient Input is given for forwardTicketDetails service.");
					throw new InSufficeientInputException(errorMsgs);
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"The Insufficient Input is given for forwardTicketDetails the ticket is currently not in this employee or his department.");
				throw new InSufficeientInputException(errorMsgs);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result seekInfoTicketDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws SQLInsertionException, InSufficeientInputException, IllegalAccessException,
			InvocationTargetException, InformationNotFoundException {
		LOGGER.info("******* The control inside of the seekInfoTicketDetails  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();

		employeeTicketRequestInfo
				.setDocumentsLocation(createTicketConversationDocumentsLocation(employeeTicketRequestInfo));
		// employeeTicketRequestInfo.setDocumentsLocation(documentLocation != null ?
		// documentLocation :"NA");

		/* SeekInfo is asking in between employees or departments. */
		employeeTicketRequestInfo.setVisibleType(Visibility.PRIVATE.getDescription());
		if (employeeTicketRequestInfo.getToDeptId() != null && !employeeTicketRequestInfo.getToDeptId().equals(0l)) {
			employeeTicketRequestInfo.setTypeOf(Department.DEPARTMENT.getId());
			employeeTicketRequestInfo.setToType(Department.DEPARTMENT.getId());
		} else {
			employeeTicketRequestInfo.setTypeOf(Department.EMPLOYEE.getId());
			employeeTicketRequestInfo.setToType(Department.EMPLOYEE.getId());
		}
		Long record_No = employeeTicketDaoImpl.insertTicketConversationDocuments(employeeTicketRequestInfo,
				Status.ACTIVE);
		if (record_No != null && record_No >= 0) {
			/*
			 * creating cloned object and passing as a argument to createSeekInfoRequestId
			 * method.
			 */
			EmployeeTicketRequestInfo clonedEmployeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
			BeanUtils.copyProperties(clonedEmployeeTicketRequestInfoObj, employeeTicketRequestInfo);

			/* generating seekinfo request Id */
			Long requestId = createSeekInfoRequestId(clonedEmployeeTicketRequestInfoObj);
			employeeTicketRequestInfo.setTicketSeekInforequestId(requestId != null ? requestId : 0l);
			employeeTicketRequestInfo.setTicketConversationDocumentId(record_No);
			Long pk = employeeTicketDaoImpl.insertTicketSeekInfoDetails(employeeTicketRequestInfo, Status.ACTIVE);
			if (pk <= 0) {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"***** The Exception is raised in while inserting Ticket seek info details conversation ******");
				throw new SQLInsertionException(errorMsgs);
			} else {
				/* update Ticket Status. */
				Result resultObj = updateTicketStatus(employeeTicketRequestInfo);
				if (resultObj.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
					/* Inserting TicketStatistics table while updating the ticket table */
					insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
				}
				final EmployeeTicketRequestInfo employeeTicketRequestInfoFinal = new EmployeeTicketRequestInfo();
				BeanUtils.copyProperties(employeeTicketRequestInfoFinal, employeeTicketRequestInfo);
				Thread thread = new Thread() {
					public synchronized void run() {
						LOGGER.debug("**** The control is inside upload ticket documents into the server *****");
						insertTicketConversationDocuments(employeeTicketRequestInfoFinal);
					}
				};
				thread.start();
				if (resultObj.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
					result.setResponseCode(Status.ACTIVE.getStatus().intValue());
					return result;
				}
			}
		}
		return result;
	}

	@Override
	public DepartmentResponse getDepartmentDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("******* The control inside of the getDepartmentDetails  in  EmployeeTicketServiceImpl ********");
		DepartmentResponse departmentResponse = new DepartmentResponse();
		employeeTicketRequestInfo
				.setTypeOf(employeeTicketRequestInfo.getTypeOf() != null ? employeeTicketRequestInfo.getTypeOf() : 0l);
		List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo,
				Status.ACTIVE);

		if (!departmentPojos.isEmpty()) {
			EmployeeTicketMapper mapper = new EmployeeTicketMapper();
			departmentResponse = mapper.departmentPojosToDepartmentResponse(departmentPojos);
		}
		return departmentResponse;
	}

	private Long createSeekInfoRequestId(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InSufficeientInputException, InformationNotFoundException {
		LOGGER.info(
				"******* The control inside of the createSeekInfoRequestId  in  EmployeeTicketServiceImpl ********");
		Boolean flag = Boolean.FALSE;
		Long requestId = null;
		Integer decisionMaker = 0;
		if (employeeTicketRequestInfo.getFromId() != null && employeeTicketRequestInfo.getFromType() != null
				&& employeeTicketRequestInfo.getFromDeptId() != null
				&& ((employeeTicketRequestInfo.getToId() != null && !employeeTicketRequestInfo.getToId().equals(0l))
						|| (employeeTicketRequestInfo.getToDeptId() != null
								&& !employeeTicketRequestInfo.getToDeptId().equals(0l)))
				&& employeeTicketRequestInfo.getToType() != null && employeeTicketRequestInfo.getTicketId() != null) {
			// employeeTicketRequestInfo.setToId(employeeTicketRequestInfo.getToId()!=null?employeeTicketRequestInfo.getToId():employeeTicketRequestInfo.getToDeptId());
			List<TicketSeekInfoRequestPojo> ticketSeekInfoRequestPojos = employeeTicketDaoImpl
					.getTicketSeekInfoRequestDetails(employeeTicketRequestInfo, Status.ALL);

			if (Util.isNotEmptyObject(ticketSeekInfoRequestPojos)) {
				for (TicketSeekInfoRequestPojo pojo : ticketSeekInfoRequestPojos) {
					/* Department asking seekinfo to another Department */
					if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())
							&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
							&& employeeTicketRequestInfo.getToType().equals(Department.DEPARTMENT.getId())) {

						LOGGER.info("****** pojo ******FromId****" + pojo.getFromDeptId() + "********ToId*********"
								+ pojo.getToDeptId() + "****" + pojo.getToType());
						LOGGER.info(
								"******employeeTicketRequestInfo *******" + employeeTicketRequestInfo.getFromDeptId()
										+ "****TOID********" + employeeTicketRequestInfo.getToDeptId());
						decisionMaker = 1;
						if ((pojo.getTicketId().equals(employeeTicketRequestInfo.getTicketId()))
								&& ((pojo.getFromDeptId().equals(employeeTicketRequestInfo.getFromDeptId())
										&& pojo.getToDeptId().equals(employeeTicketRequestInfo.getToDeptId())
										&& pojo.getFromType().equals(Department.DEPARTMENT.getId())
										&& pojo.getToType().equals(Department.DEPARTMENT.getId()))

										|| (pojo.getFromDeptId().equals(employeeTicketRequestInfo.getToDeptId())
												&& pojo.getFromType().equals(Department.DEPARTMENT.getId())
												&& pojo.getToDeptId().equals(employeeTicketRequestInfo.getFromDeptId())
												&& pojo.getToType().equals(Department.DEPARTMENT.getId())))) {

							requestId = pojo.getTicketSeekInfoRequestId();
							LOGGER.info("**** The RequestId is ****" + requestId);
							flag = Boolean.TRUE;
							break;
						}
					} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())
							&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
							&& employeeTicketRequestInfo.getToType().equals(Department.EMPLOYEE.getId())) {
						decisionMaker = 2;
						if ((pojo.getTicketId().equals(employeeTicketRequestInfo.getTicketId()))
								&& ((pojo.getFromDeptId().equals(employeeTicketRequestInfo.getFromDeptId())
										&& pojo.getFromType().equals(Department.DEPARTMENT.getId())
										&& pojo.getToId().equals(employeeTicketRequestInfo.getToId())
										&& pojo.getToType().equals(Department.EMPLOYEE.getId()))

										|| (pojo.getFromId().equals(employeeTicketRequestInfo.getToId())
												&& pojo.getFromType().equals(Department.EMPLOYEE.getId())
												&& pojo.getToDeptId().equals(employeeTicketRequestInfo.getFromDeptId())
												&& pojo.getToType().equals(Department.DEPARTMENT.getId())))) {

							requestId = pojo.getTicketSeekInfoRequestId();
							LOGGER.info("**** The RequestId is ****" + requestId);
							flag = Boolean.TRUE;
							break;
						}
					} else if (Util.isEmptyObject(employeeTicketRequestInfo.getFromDeptId())
							&& Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())
							&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
							&& employeeTicketRequestInfo.getToType().equals(Department.DEPARTMENT.getId())) {
						decisionMaker = 3;
						if ((pojo.getTicketId().equals(employeeTicketRequestInfo.getTicketId()))
								&& ((pojo.getFromId().equals(employeeTicketRequestInfo.getFromId())
										&& pojo.getFromType().equals(Department.EMPLOYEE.getId())
										&& pojo.getToDeptId().equals(employeeTicketRequestInfo.getToDeptId())
										&& pojo.getToType().equals(Department.DEPARTMENT.getId()))

										|| (pojo.getFromDeptId().equals(employeeTicketRequestInfo.getToDeptId())
												&& pojo.getToType().equals(Department.DEPARTMENT.getId())
												&& pojo.getToId().equals(employeeTicketRequestInfo.getFromId())
												&& pojo.getToType().equals(Department.EMPLOYEE.getId())))) {

							requestId = pojo.getTicketSeekInfoRequestId();
							LOGGER.info("**** The RequestId is ****" + requestId);
							flag = Boolean.TRUE;
							break;
						}
					} else if (Util.isEmptyObject(employeeTicketRequestInfo.getFromDeptId())
							&& Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())
							&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
							&& employeeTicketRequestInfo.getToType().equals(Department.EMPLOYEE.getId())) {
						decisionMaker = 4;
						if ((pojo.getTicketId().equals(employeeTicketRequestInfo.getTicketId()))
								&& ((pojo.getFromId().equals(employeeTicketRequestInfo.getFromId())
										&& pojo.getFromType().equals(Department.EMPLOYEE.getId())
										&& pojo.getToDeptId().equals(employeeTicketRequestInfo.getToId())
										&& pojo.getToType().equals(Department.EMPLOYEE.getId()))

										|| (pojo.getFromId().equals(employeeTicketRequestInfo.getToId())
												&& pojo.getFromType().equals(Department.EMPLOYEE.getId())
												&& pojo.getToId().equals(employeeTicketRequestInfo.getFromId())
												&& pojo.getToType().equals(Department.EMPLOYEE.getId())))) {

							requestId = pojo.getTicketSeekInfoRequestId();
							LOGGER.info("**** The RequestId is ****" + requestId);
							flag = Boolean.TRUE;
							break;
						}
					}
				}
			}
			/* if Ticket Seekinfo request is empty making decision maker */
			else {
				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())
						&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
						&& employeeTicketRequestInfo.getToType().equals(Department.DEPARTMENT.getId())) {
					decisionMaker = 1;
				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())
						&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
						&& employeeTicketRequestInfo.getToType().equals(Department.EMPLOYEE.getId())) {
					decisionMaker = 2;
				} else if (Util.isEmptyObject(employeeTicketRequestInfo.getFromDeptId())
						&& Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())
						&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
						&& employeeTicketRequestInfo.getToType().equals(Department.DEPARTMENT.getId())) {
					decisionMaker = 3;
				} else if (Util.isEmptyObject(employeeTicketRequestInfo.getFromDeptId())
						&& Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())
						&& (Util.isNotEmptyObject(employeeTicketRequestInfo.getToType()))
						&& employeeTicketRequestInfo.getToType().equals(Department.EMPLOYEE.getId())) {
					decisionMaker = 4;
				}
			}
			if (flag) {
				return requestId;
			} else {
				if (Util.isNotEmptyObject(decisionMaker)) {
					if (decisionMaker.equals(1)) {
						EmployeeTicketRequestInfo requestInfo = new EmployeeTicketRequestInfo();
						requestInfo.setFromDeptId(employeeTicketRequestInfo.getFromDeptId());
						requestInfo.setFromType(Department.DEPARTMENT.getId());
						requestInfo.setToDeptId(employeeTicketRequestInfo.getToDeptId());
						requestInfo.setToType(Department.DEPARTMENT.getId());
						requestInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						requestId = employeeTicketDaoImpl.insertTicketSeekInfoRequest(requestInfo, Status.ACTIVE);
					} else if (decisionMaker.equals(2)) {
						EmployeeTicketRequestInfo requestInfo = new EmployeeTicketRequestInfo();
						requestInfo.setFromDeptId(employeeTicketRequestInfo.getFromDeptId());
						requestInfo.setFromType(Department.DEPARTMENT.getId());
						requestInfo.setToId(employeeTicketRequestInfo.getToDeptId());
						requestInfo.setToType(Department.EMPLOYEE.getId());
						requestInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						requestId = employeeTicketDaoImpl.insertTicketSeekInfoRequest(requestInfo, Status.ACTIVE);
					} else if (decisionMaker.equals(3)) {
						EmployeeTicketRequestInfo requestInfo = new EmployeeTicketRequestInfo();
						requestInfo.setFromId(employeeTicketRequestInfo.getFromId());
						requestInfo.setFromType(Department.EMPLOYEE.getId());
						requestInfo.setToDeptId(employeeTicketRequestInfo.getToDeptId());
						requestInfo.setToType(Department.DEPARTMENT.getId());
						requestInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						requestId = employeeTicketDaoImpl.insertTicketSeekInfoRequest(requestInfo, Status.ACTIVE);
					} else {
						EmployeeTicketRequestInfo requestInfo = new EmployeeTicketRequestInfo();
						requestInfo.setFromId(employeeTicketRequestInfo.getFromId());
						requestInfo.setFromType(Department.EMPLOYEE.getId());
						requestInfo.setToId(employeeTicketRequestInfo.getToId());
						requestInfo.setToType(Department.EMPLOYEE.getId());
						requestInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						requestId = employeeTicketDaoImpl.insertTicketSeekInfoRequest(requestInfo, Status.ACTIVE);
					}
				} else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add("***** The Exception is raised if  Decision Maker is Not Updated. ******");
					throw new InformationNotFoundException(errorMsgs);
				}
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"The Insufficient Input is given for forwardTicketDetails non other than CRM the ticket is currently not in this employee service.");
			throw new InSufficeientInputException(errorMsgs);
		}
		return requestId;
	}

	@Override
	public Result ticketViewRequestInfo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InvalidStatusException, InformationNotFoundException, InterruptedException, ExecutionException {
		LOGGER.info("******* The control inside of the viewRequestInfo  in  EmployeeTicketServiceImpl ********"
				+ employeeTicketRequestInfo);

		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		TicketSeekInfoWrapper wrapper = new TicketSeekInfoWrapper();
		List<GenericTicketSeekInfo> genericTicketSeekInfos = new ArrayList<GenericTicketSeekInfo>();
		List<TicketSeekInfoPojo> ticketSeekInfoPojos = employeeTicketDaoImpl
				.getTicketSeekInfoDetails(employeeTicketRequestInfo, Status.ACTIVE);
		List<TicketSeekInfoPojo> employeeSpecificTicketSeekInfoPojos = new ArrayList<TicketSeekInfoPojo>();
		Set<Long> employeeSet = new HashSet<Long>();
		Set<Long> departmentSet = new HashSet<Long>();

		if (Util.isNotEmptyObject(ticketSeekInfoPojos)) {
			/* If EmployeeId and DepartmentId Specific TicketSeekInfo details */
			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())
					&& Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())) {

				/* if tickets is raising from one department to another dempartment */
				for (TicketSeekInfoPojo pojo : ticketSeekInfoPojos) {
					if ((pojo.getFromDeptId().equals(employeeTicketRequestInfo.getFromDeptId()))
							|| ((pojo.getToDeptId().equals(employeeTicketRequestInfo.getFromDeptId())))) {
						employeeSpecificTicketSeekInfoPojos.add(pojo);
					}
				}
				LOGGER.debug(
						"**** Employee Specific ticket seekInfo  pojos *****" + employeeSpecificTicketSeekInfoPojos);
				/* If EmployeeId Specific TicketSeekInfo details */
			} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())) {
				for (TicketSeekInfoPojo pojo : ticketSeekInfoPojos) {
					if (pojo.getFromId().equals(employeeTicketRequestInfo.getFromId())
							|| pojo.getToId().equals(employeeTicketRequestInfo.getFromId())) {
						employeeSpecificTicketSeekInfoPojos.add(pojo);
					}
				}
				LOGGER.debug(
						"**** Employee Specific ticket seekInfo  pojos *****" + employeeSpecificTicketSeekInfoPojos);
			}
			for (TicketSeekInfoPojo pojo : employeeSpecificTicketSeekInfoPojos) {
				/* adding toId and toDept departmentIds and employeeIds. */
				if (Util.isNotEmptyObject(pojo.getToType())) {
					if (pojo.getToType().equals(Department.EMPLOYEE.getId())) {
						employeeSet.add(pojo.getToId());
					} else if (pojo.getToType().equals(Department.DEPARTMENT.getId())) {
						departmentSet.add(pojo.getToDeptId());
					}
				}
				/* adding fromDeptId departmentIds. */
				if (Util.isNotEmptyObject(pojo.getFromDeptId())) {
					departmentSet.add(pojo.getFromDeptId());
					continue;
				}
				/* adding fromId employeeIds. */
				if (Util.isNotEmptyObject(pojo.getFromId())) {
					employeeSet.add(pojo.getFromId());
					continue;
				}
			}
			/* removing nulls from employeeset */
			CollectionUtils.filter(employeeSet, PredicateUtils.notNullPredicate());
			/* employeeSet contains including fromEmployeeId also */
			employeeSet.remove(employeeTicketRequestInfo.getFromId());

			/* removing nulls from departmentSet */
			CollectionUtils.filter(departmentSet, PredicateUtils.notNullPredicate());
			/* departmentSet contains including fromdepartmentId also */
			departmentSet.remove(employeeTicketRequestInfo.getFromDeptId());

			LOGGER.debug("**** The login employee is asking seekInfo to the following employees ****" + employeeSet);
			LOGGER.debug(
					"**** The login employee is asking seekInfo to the following departments ****" + departmentSet);

			/* department specific */
			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())) {
				for (Long deptId : departmentSet) {
					List<TicketSeekInfoPojo> departmentSpecificTicketSeekInfo = new ArrayList<TicketSeekInfoPojo>();
					for (TicketSeekInfoPojo pojo : employeeSpecificTicketSeekInfoPojos) {
						if (((pojo.getFromDeptId().equals(employeeTicketRequestInfo.getFromDeptId()))
								&& pojo.getToDeptId().equals(deptId))
								|| (pojo.getFromDeptId().equals(deptId)
										&& pojo.getToDeptId().equals(employeeTicketRequestInfo.getFromDeptId()))) {
							{
								departmentSpecificTicketSeekInfo.add(pojo);
							}
						}
					}
					LOGGER.debug("******fromidmapping******" + deptId + "*******"
							+ employeeTicketRequestInfo.getFromDeptId());
					LOGGER.debug("*** The noumber of seekinfo object is " + deptId + "****"
							+ departmentSpecificTicketSeekInfo.size());
					EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
					info.setDepartmentId(deptId);
					List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(info,
							Status.ACTIVE);
					/* mapper to form department specific conversation. */
					genericTicketSeekInfos.add(mapper.departmentSpecificTicketSeekInfo(departmentSpecificTicketSeekInfo,
							employeeTicketRequestInfo, departmentPojos));
				}
				for (Long employeeId : employeeSet) {
					List<TicketSeekInfoPojo> employeeSpecificTicketSeekInfo = new ArrayList<TicketSeekInfoPojo>();
					for (TicketSeekInfoPojo ticketSeekInfoPojo : employeeSpecificTicketSeekInfoPojos) {
						if ((ticketSeekInfoPojo.getFromDeptId().equals(employeeTicketRequestInfo.getFromDeptId()))
								&& ticketSeekInfoPojo.getToId().equals(employeeId)
								|| ((Util.isEmptyObject(ticketSeekInfoPojo.getFromDeptId())
										&& ticketSeekInfoPojo.getFromId().equals(employeeId))
										&& ticketSeekInfoPojo.getToDeptId()
												.equals(employeeTicketRequestInfo.getFromDeptId()))) {
							employeeSpecificTicketSeekInfo.add(ticketSeekInfoPojo);
						}
					}
					LOGGER.debug("*** The " + employeeId + " of seekinfo is ****" + employeeSpecificTicketSeekInfo);
					LOGGER.debug("*** The noumber of seekinfo object is " + employeeId + "****"
							+ employeeSpecificTicketSeekInfo.size());
					EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
					info.setEmployeeId(employeeId);
					List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(info,
							Status.ACTIVE);

					employeeTicketRequestInfo
							.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)
									? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
											? employeeDetailsPojos.get(0).getEmployeeId()
											: 0l
									: 0l);
					List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
							Status.ACTIVE);
					/* mapper to form employee specific conversation. */
					genericTicketSeekInfos.add(mapper.employeeSpecificTicketSeekInfo(employeeSpecificTicketSeekInfo,
							employeeTicketRequestInfo, employeeDetailsPojos, employeePojos));
				}
				/* employee specific */
			} else {
				for (Long deptId : departmentSet) {
					List<TicketSeekInfoPojo> departmentSpecificTicketSeekInfo = new ArrayList<TicketSeekInfoPojo>();
					for (TicketSeekInfoPojo pojo : employeeSpecificTicketSeekInfoPojos) {
						if (((pojo.getFromId().equals(employeeTicketRequestInfo.getFromId()))
								&& pojo.getToDeptId().equals(deptId))
								|| (pojo.getFromDeptId().equals(deptId)
										&& pojo.getToId().equals(employeeTicketRequestInfo.getFromId()))) {
							{
								departmentSpecificTicketSeekInfo.add(pojo);
							}
						}
					}
					LOGGER.debug("*** The " + deptId + " of seekinfo is ****" + departmentSpecificTicketSeekInfo);
					LOGGER.debug("*** The noumber of seekinfo object is " + deptId + "****"
							+ departmentSpecificTicketSeekInfo.size());
					EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
					info.setDepartmentId(deptId);
					List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(info,
							Status.ACTIVE);
					/* mapper to form department specific conversation. */
					genericTicketSeekInfos.add(mapper.departmentSpecificTicketSeekInfo(departmentSpecificTicketSeekInfo,
							employeeTicketRequestInfo, departmentPojos));
				}
				for (Long employeeId : employeeSet) {
					List<TicketSeekInfoPojo> employeeSpecificTicketSeekInfo = new ArrayList<TicketSeekInfoPojo>();
					for (TicketSeekInfoPojo ticketSeekInfoPojo : employeeSpecificTicketSeekInfoPojos) {
						if ((ticketSeekInfoPojo.getFromId().equals(employeeTicketRequestInfo.getFromId()))
								&& ticketSeekInfoPojo.getToId().equals(employeeId)
								|| (ticketSeekInfoPojo.getFromId().equals(employeeId) && ticketSeekInfoPojo.getToId()
										.equals(employeeTicketRequestInfo.getFromId()))) {
							employeeSpecificTicketSeekInfo.add(ticketSeekInfoPojo);
						}
					}
					LOGGER.debug("*** The " + employeeId + " of seekinfo is ****" + employeeSpecificTicketSeekInfo);
					LOGGER.debug("*** The noumber of seekinfo object is " + employeeId + "****"
							+ employeeSpecificTicketSeekInfo.size());
					EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
					info.setEmployeeId(employeeId);
					List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(info,
							Status.ACTIVE);
					employeeTicketRequestInfo
							.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)
									? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
											? employeeDetailsPojos.get(0).getEmployeeId()
											: 0l
									: 0l);
					List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
							Status.ACTIVE);
					/* mapper to form employee specific conversation. */
					genericTicketSeekInfos.add(mapper.employeeSpecificTicketSeekInfo(employeeSpecificTicketSeekInfo,
							employeeTicketRequestInfo, employeeDetailsPojos, employeePojos));
				}
			}
			employeeTicketRequestInfo.setRequestUrl("getTicket");
			List<TicketPojo> ticketPojoslIST = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo,
					Status.ACTIVE);
			/* adding employee specific details */
			for (GenericTicketSeekInfo genericTicketSeekInfo : genericTicketSeekInfos) {
				for (TicketSeekInfo info : genericTicketSeekInfo.getTicketSeekInfo()) {
					employeeTicketRequestInfo.setEmployeeId(info.getFromId());
					List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl
							.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
					employeeTicketRequestInfo
							.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)
									? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
											? employeeDetailsPojos.get(0).getEmployeeId()
											: 0l
									: 0l);
					List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
							Status.ACTIVE);
					info.setEmployeeName(
							Util.isNotEmptyObject(employeePojos) ? employeePojos.get(0).getEmployeeName() : "NA");
					info.setEmployeeProfilePic(
							Util.isNotEmptyObject(employeePojos) ? employeePojos.get(0).getUserProfile() : "NA");
				}
			}
			TicketResponse ticketResponse = mapper.ticketPojoToTicketResponse(ticketPojoslIST);
			/* customer property details */

			EmployeeTicketRequestInfo obj = new EmployeeTicketRequestInfo();
			obj.setRequestUrl("venkat");
			obj.setFlatBookingId(
					Util.isNotEmptyObject(ticketPojoslIST)
							? Util.isNotEmptyObject(ticketPojoslIST.get(0).getFlatBookingId())
									? ticketPojoslIST.get(0).getFlatBookingId()
									: 0l
							: 0l);

			List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = employeeTicketDaoImpl
					.getCustomerPropertyDetails(obj, Status.ACTIVE);

			ticketResponse.setCustomerPropertyDetails(mapper.CustomerPropertyDetailsPojoToCustomerPropertyDetails(
					Util.isNotEmptyObject(customerPropertyDetailsPojos)
							? Util.isNotEmptyObject(customerPropertyDetailsPojos.get(0))
									? customerPropertyDetailsPojos.get(0)
									: null
							: null));
			/* pending department */
			ticketResponse.setPendingDepartmentName(getPendingDepartmentName(Util.isNotEmptyObject(ticketPojoslIST)
					? Util.isNotEmptyObject(ticketPojoslIST.get(0)) ? ticketPojoslIST.get(0) : null
					: null));
			ticketResponse.setStatus(getTicketStatus(Util.isNotEmptyObject(ticketPojoslIST)
					? Util.isNotEmptyObject(ticketPojoslIST.get(0)) ? ticketPojoslIST.get(0) : null
					: null));

			// ticketResponse.setFileInfos(createDocumentUrls(employeeTicketRequestInfo));
			Future<List<FileInfo>> fileInfos = employeeTicketServiceHelper
					.createDocumentUrls(employeeTicketRequestInfo);
			do {
				/* adding ticket FileInfos */
				if (fileInfos.isDone()) {
					ticketResponse.setFileInfos(fileInfos.get());
					break;
				}
			} while (true);

			wrapper.setTicketResponse(ticketResponse);
			wrapper.setGenericTicketSeekInfos(genericTicketSeekInfos);
			LOGGER.debug("::::::::::::::::::::: Ticketseekinfo wrapper is :::::::::::::::::::" + wrapper);
		} /*
			 * else { //no seekinfo is asked in this ticket List<String> errorMsg = new
			 * ArrayList<String>(); errorMsg.
			 * add(":::::::::::::::::::Required Information not found for TicketSeekInfo.");
			 * throw new InformationNotFoundException(errorMsg); }
			 */
		return wrapper;
	}

	@Override
	public Result viewRequestInfo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("******* The control inside of the viewRequestInfo  in  EmployeeTicketServiceImpl ********");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();

		Page<TicketSeekInfoPojo> page = employeeTicketDaoImpl.getTicketSeekInfoDetails(employeeTicketRequestInfo,
				Status.ALL,
				Util.isNotEmptyObject(employeeTicketRequestInfo.getPageNo())
						? employeeTicketRequestInfo.getPageNo().intValue()
						: 1,
				Util.isNotEmptyObject(employeeTicketRequestInfo.getPageSize())
						? employeeTicketRequestInfo.getPageSize().intValue()
						: 1);
		List<TicketSeekInfoPojo> seekInfoPojos = page.getPageItems();
		// List<TicketSeekInfoPojo> seekInfoPojos =
		// employeeTicketDaoImpl.getTicketSeekInfoDetails(employeeTicketRequestInfo,Status.ALL
		// );
		employeeTicketResponse.setPageCount(Integer.valueOf(page.getPagesAvailable()).longValue());

		Set<Long> ticketSet = new LinkedHashSet<Long>();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		for (TicketSeekInfoPojo pojo : seekInfoPojos) {
			ticketSet.add(Util.isNotEmptyObject(pojo.getTicketId()) ? pojo.getTicketId() : 0l);
		}
		LOGGER.debug("***** The TicketSet object is *****" + ticketSet);
		for (Long id : ticketSet) {
			employeeTicketRequestInfo.setRequestUrl("getTicket");
			employeeTicketRequestInfo.setTicketId(id);
			List<TicketPojo> ticketPojoslIST = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo,
					Status.ACTIVE);
			for (TicketPojo ticketPojo : ticketPojoslIST) {
				employeeTicketRequestInfo
						.setTicketId(Util.isNotEmptyObject(ticketPojo.getTicketId()) ? ticketPojo.getTicketId() : 0l);
				/*
				 * List<TicketEscalationPojo> ticketEscalationPojolIST =
				 * employeeTicketDaoImpl.getTicketEscalationDtls(employeeTicketRequestInfo,
				 * Status.ACTIVE);
				 */
				ticketResponses.add(mapper.ticketPojoToticketEscalationPojolIST(ticketPojo));
			}
		}
		LOGGER.debug("**** The TicketResponses list is *****" + ticketResponses);
		employeeTicketResponse.setTicketResponseList(ticketResponses);
		LOGGER.debug("**** The employeeTicketResponse object is ****" + employeeTicketResponse);
		return employeeTicketResponse;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result insertSeekInfoDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws SQLInsertionException, IllegalAccessException, InvocationTargetException {
		LOGGER.info("******* The control inside of the insertSeekInfoDetails  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();
		employeeTicketRequestInfo
				.setDocumentsLocation(createTicketConversationDocumentsLocation(employeeTicketRequestInfo));
		employeeTicketRequestInfo.setVisibleType(Visibility.PRIVATE.getDescription());
		Long record_No = employeeTicketDaoImpl.insertTicketConversationDocuments(employeeTicketRequestInfo,
				Status.ACTIVE);
		if (Util.isNotEmptyObject(record_No)) {
			LOGGER.debug("***** The TicketConversationDocuments is updated sucessfully. *****" + record_No);
			Long seekInfoDetailsId = employeeTicketDaoImpl.insertTicketSeekInfoDetails(employeeTicketRequestInfo,
					Status.ACTIVE);
			if (Util.isNotEmptyObject(seekInfoDetailsId)) {
				final EmployeeTicketRequestInfo employeeTicketRequestInfoFinal = new EmployeeTicketRequestInfo();
				BeanUtils.copyProperties(employeeTicketRequestInfoFinal, employeeTicketRequestInfo);
				Thread thread = new Thread() {
					public synchronized void run() {
						LOGGER.debug("**** The control is inside upload ticket documents into the server *****");
						insertTicketConversationDocuments(employeeTicketRequestInfoFinal);
					}
				};
				thread.start();
				LOGGER.debug("***** The SeekInfoDetails is updated sucessfully. *****" + seekInfoDetailsId);
				return result;
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("***** The Exception is raised in while inserting Ticket seekinfo details ******");
				throw new SQLInsertionException(errorMsgs);
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation documents ******");
			throw new SQLInsertionException(errorMsgs);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result closeTicket(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) throws Exception {
		LOGGER.info("******* The control inside of the closeTicket  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();
		String msg="";
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		/* if ticket is closing from the employee and update conversation */
		if ((Util.isNotEmptyObject(employeeTicketRequestInfo.getFromType()) ? employeeTicketRequestInfo.getFromType().equals(Department.EMPLOYEE.getId())
				: false) && Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())) {
			Result objResult = updateMessageToCustomer(employeeTicketRequestInfo);
			if (objResult.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
				LOGGER.info("**** The message is inserted successfully ***");
			}
		}
		EmployeeTicketRequestInfo info1=new EmployeeTicketRequestInfo();
		info1.setTicketId(employeeTicketRequestInfo.getTicketId());
		info1.setRequestUrl("getTicket");
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info1, Status.ACTIVE);
		employeeTicketRequestInfo.setComplaintStatus(ticketPojos.get(0).getComplaintStatus());
		/* send automatic mesage to customer while closing ticket */
		EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
		if(Util.isNotEmptyObject(employeeTicketRequestInfo.getFromType()) && employeeTicketRequestInfo.getFromType().equals(Department.EMPLOYEE.getId())) {
			EmployeeTicketRequestInfo empTicketRequestInfo = new EmployeeTicketRequestInfo();
			empTicketRequestInfo.setEmployeeId(employeeTicketRequestInfo.getFromId());
			List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(empTicketRequestInfo, Status.ACTIVE);
			 if(Util.isNotEmptyObject(employeeTicketRequestInfo.getComplaintStatus())&&employeeTicketRequestInfo.getComplaintStatus().equals( MetadataId.YES.getId())) {
				  //complaint tickets msg
				
				 employeeTicketRequestInfoObj.setMessage("Dear Customer, Hope your issue/concerns has been addressed and resolved to your satisfaction. Your complaint is closed now. If you are not satisfied with the resolution provided , kindly reopen the ticket and share your concerns. Our escalation team will get back to you shortly.");//+" "
						//	+ (Util.isNotEmptyObject(employeePojos) ? Util.isNotEmptyObject(employeePojos.get(0)) ? employeePojos.get(0).getEmployeeName():"N/A":"N/A"));
							
				 
			 }else {
			employeeTicketRequestInfoObj.setMessage(new ResponceCodesUtil().getApplicationProperties().getProperty("TICKET_CLOSING_CUSTOMER_MSG")+" "
			+ (Util.isNotEmptyObject(employeePojos) ? Util.isNotEmptyObject(employeePojos.get(0)) ? employeePojos.get(0).getEmployeeName():"N/A":"N/A"));
			
				 
			 }
			 
			 }else {
			/* getting Customer Name */
				
			Future<List<CustomerPropertyDetailsPojo>> future = employeeTicketServiceHelper.getTicketOwner(employeeTicketRequestInfo);
			while (true) {
				if (future.isDone()) {
					List<CustomerPropertyDetailsPojo> customerPropertyDetails = future.get();
					
					 
					  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getComplaintStatus())&&employeeTicketRequestInfo.getComplaintStatus().equals( MetadataId.YES.getId())) {
						  //complaint tickets msg
							employeeTicketRequestInfoObj.setMessage("Thank you! Hope you are satisfied with the resolution.");//+" "
							
							//+ (Util.isNotEmptyObject(customerPropertyDetails) ? Util.isNotEmptyObject(customerPropertyDetails.get(0)) ? customerPropertyDetails.get(0).getCustomerName():"N/A":"N/A") );
						  
					  }else {
					//not complaint ticktes msg
					employeeTicketRequestInfoObj.setMessage(new ResponceCodesUtil().getApplicationProperties().getProperty("TICKET_CLOSING_CUSTOMER_MSG")+" "
					  
							+ (Util.isNotEmptyObject(customerPropertyDetails) ? Util.isNotEmptyObject(customerPropertyDetails.get(0)) ? customerPropertyDetails.get(0).getCustomerName():"N/A":"N/A") );
					  }
					System.out.println(employeeTicketRequestInfoObj.getMessage()+"message");
					break;
				}
			}
		}
		// employeeTicketRequestInfoObj.setMessage(new
		// ResponceCodesUtil().getApplicationProperties().getProperty("TICKET_CLOSING_CUSTOMER_MSG"));
		employeeTicketRequestInfoObj.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) ? employeeTicketRequestInfo.getTicketId(): 0l);
		employeeTicketRequestInfoObj.setFromType(Department.SYSTEM.getId());
		employeeTicketRequestInfoObj.setFromId(0l);
		employeeTicketRequestInfoObj.setFromDeptId(0l);
		 employeeTicketRequestInfoObj.setDepartmentId(0l);
		 msg=employeeTicketRequestInfoObj.getMessage();
		
		Result objResult = updateMessageToCustomer(employeeTicketRequestInfoObj);
		if (objResult.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
			LOGGER.info("**** The message is inserted successfully ***");
		}
		/* calling garbage collector to free up the space */
		System.gc();
		employeeTicketRequestInfo.setMessage(msg);
		for (PushNotificationInfo pushNotificationInfo : createNotification(employeeTicketRequestInfo)) {
			final PushNotificationInfo info = pushNotificationInfo;
			Thread thread = new Thread() {
				public synchronized void run() {
					LOGGER.debug("**** The control is inside upload ticket documents into the server *****");
					try {
						if (Util.isNotEmptyObject(info.getOsType())
								&& info.getOsType().equalsIgnoreCase(Type.ANDRIOD.getName())) {
							/* send push notification to customer while closing the ticket. */
							final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
							try {
							pushNotificationUtil.pushFCMNotification(info);
							}catch(Exception ex) {
							 LOGGER.error("**** The Error Message ****"+ex);
							}
						} else if (Util.isNotEmptyObject(info.getOsType())
								&& info.getOsType().equalsIgnoreCase(Type.IOS.getName())) {
							/* send push notification to customer while closing the ticket. */
							final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
							try {
							ioSPushNotificationUtil.sendIosPushNotification(Arrays.asList(info.getDeviceToken()),
									ioSPushNotificationUtil.getTicketingPushNotificationPayLoad(info).toString(), false);
							}catch(Exception ex) {
								 LOGGER.error("**** The Error Message ****"+ex);
							}
						}
					} catch (Exception e) {
						LOGGER.error(
								"**** The Exception is raised while inserting record into the TicketStatics Table ****");
					}
				}
			};
			thread.setName("Ticket PushNotification Thread");
			thread.start();
		}
		TicketPojo pojo = mapper.employeeTicketRequestInfoToTicketPojoForClose(employeeTicketRequestInfo);
		Long updatedRows = employeeTicketDaoImpl.updateTicketCloseStatus(pojo);

		/* if ticket is closed remove ticket from Escalation tickets. */
		@SuppressWarnings("unused")
		Long ticketEscalationStatus = employeeTicketDaoImpl.updateTicketEscalationStatus(
				mapper.employeeTicketInfoToTicketEscalationPojo(employeeTicketRequestInfo));

		if (updatedRows > 0) {
			/* Inserting TicketStatistics table while updating the ticket table */
			insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
		}
		/* sending mail to customer and employee while closing the ticket */
		final EmployeeTicketRequestInfo employeeTicketRequestInfoForMailObj = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfoForMailObj.setDeptName(employeeTicketRequestInfo.getDeptName());

		employeeTicketRequestInfoForMailObj.setTicketId(
				Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) ? employeeTicketRequestInfo.getTicketId()
						: 0l);
		employeeTicketServiceHelper.sendTicketCloseMail(employeeTicketRequestInfoForMailObj);

		result.setResponseCode(updatedRows.intValue());
		LOGGER.debug("***** The Number of effected Rows is *****" + updatedRows);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result makeAsPublic(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("******* The control inside of the makeAsPublic in  EmployeeTicketServiceImpl ********");
		Result result = new Result();
		Long count = 0l;
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		List<TicketConversationDocumentsPojo> ticketConversationDocumentsPojos = mapper
				.employeeTicketRequestInfoToTicketConversationDocumentsPojo(employeeTicketRequestInfo);
		for (TicketConversationDocumentsPojo pojo : ticketConversationDocumentsPojos) {
			Long updatedRows = employeeTicketDaoImpl.makeAsPublic(pojo);
			count += updatedRows;
		}
		result.setResponseCode(count.intValue());
		LOGGER.debug("***** The Number of effected Rows is *****" + count);
		return result;
	}

	private Result updateTicketStatus(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException, IllegalAccessException, InvocationTargetException {
		LOGGER.info("******* The control inside of the updateTicketStatus in  EmployeeTicketServiceImpl ********");
		Result result = new Result();

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
			EmployeeTicketRequestInfo ticketEmployeeTicketRequestInfo = new EmployeeTicketRequestInfo();
			BeanUtils.copyProperties(ticketEmployeeTicketRequestInfo, employeeTicketRequestInfo);
			ticketEmployeeTicketRequestInfo.setRequestUrl("getTicket");
			List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(ticketEmployeeTicketRequestInfo,
					Status.ACTIVE);
			EmployeeTicketMapper mapper = new EmployeeTicketMapper();
			LOGGER.debug("**** The Ticket Object is ****" + ticketPojos);

			if (Util.isNotEmptyObject(ticketPojos)) {
				if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("updateTicketConversation.spring")) {
					for (TicketPojo pojo : ticketPojos) {
						/*
						 * if Ticket is updated second time check departmentid and assignedTo both are
						 * Notnull && Notzero's
						 */
						if (Util.isNotEmptyObject(pojo) && ((Util.isNotEmptyObject(pojo.getAssignmentTo())
								&& !pojo.getAssignmentTo().equals(0l))
								|| (Util.isNotEmptyObject(pojo.getDepartmentId())
										&& !pojo.getDepartmentId().equals(0l)))) {
							/* second Time or (Invidual) Updating */
							if (Util.isNotEmptyObject(pojo)
									&& (Util.isNotEmptyObject(pojo.getStatusId())
											&& pojo.getStatusId().equals(Status.ACTIVE.getStatus()))
									&& ((Util.isNotEmptyObject(pojo.getTicketStatusId())
											&& pojo.getTicketStatusId().equals(Status.INPROGRESS.getStatus())))) {
								// if(Util.isNotEmptyObject(pojo) && (Util.isNotEmptyObject(pojo.getStatusId())
								// && pojo.getStatusId().equals(Status.ACTIVE.getStatus())) &&
								// ((Util.isNotEmptyObject(pojo.getTicketStatusId()) &&
								// pojo.getTicketStatusId().equals(Status.OPEN.getStatus())) ||
								// (Util.isNotEmptyObject(pojo.getDepartmentTicketStatusId()) &&
								// pojo.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())))) {
								/* second Time or (Invidual) Updating */
								employeeTicketRequestInfo.setGeneric("Invidual");
								Long ticket_No = employeeTicketDaoImpl.updateTicketStatus(mapper
										.employeeTicketRequestInfoToTicketPojoForUpdate(employeeTicketRequestInfo));
								if (ticket_No != null && ticket_No >= 0) {
									LOGGER.debug("**** The Ticket Status is updated successfully ! ****");
									result.setResponseCode(HttpStatus.success.getResponceCode());
									return result;
								}
							} else {
								LOGGER.debug(
										"**** The high Ticket Status is not changed if it alredy some operation performed on that one ! ****");
								result.setResponseCode(HttpStatus.success.getResponceCode());
								return result;
							}
						} else {
							if (Util.isNotEmptyObject(pojo)
									&& (Util.isNotEmptyObject(pojo.getStatusId())
											&& pojo.getStatusId().equals(Status.ACTIVE.getStatus()))
									&& ((Util.isNotEmptyObject(pojo.getTicketStatusId())
											&& pojo.getTicketStatusId().equals(Status.OPEN.getStatus()))
											|| (Util.isNotEmptyObject(pojo.getTicketStatusId())
													&& pojo.getTicketStatusId().equals(Status.REOPEN.getStatus())))) {
								// if(Util.isNotEmptyObject(pojo) && (Util.isNotEmptyObject(pojo.getStatusId())
								// && pojo.getStatusId().equals(Status.ACTIVE.getStatus())) &&
								// ((Util.isNotEmptyObject(pojo.getTicketStatusId()) &&
								// pojo.getTicketStatusId().equals(Status.OPEN.getStatus())) ||
								// (Util.isNotEmptyObject(pojo.getDepartmentTicketStatusId()) &&
								// pojo.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())))) {
								employeeTicketRequestInfo.setGeneric("highlevel");
								Long ticket_No = employeeTicketDaoImpl.updateTicketStatus(mapper
										.employeeTicketRequestInfoToTicketPojoForUpdate(employeeTicketRequestInfo));
								if (ticket_No != null && ticket_No >= 0) {
									LOGGER.debug("**** The Ticket Status is updated successfully ! ****");
									result.setResponseCode(HttpStatus.success.getResponceCode());
									return result;
								}
							} else {
								LOGGER.debug("**** The Ticket Status is alredy updated successfully ! ****");
								result.setResponseCode(HttpStatus.success.getResponceCode());
								return result;
							}
						}
					}
				}
				/* updating ticket status while taking ticket seekinfo. */
				else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("seekInfoTicketDetails.spring")) {
					for (TicketPojo pojo : ticketPojos) {
						/*
						 * if Ticket is updated second time check departmentid and assignedTo both are
						 * Notnull && Notzero's
						 */
						if (Util.isNotEmptyObject(pojo) && ((Util.isNotEmptyObject(pojo.getAssignmentTo())
								&& !pojo.getAssignmentTo().equals(0l))
								|| (Util.isNotEmptyObject(pojo.getDepartmentId())
										&& !pojo.getDepartmentId().equals(0l)))) {
							if (Util.isNotEmptyObject(pojo)
									&& (Util.isNotEmptyObject(pojo.getStatusId())
											&& pojo.getStatusId().equals(Status.ACTIVE.getStatus()))
									&& ((Util.isNotEmptyObject(pojo.getTicketStatusId())
											&& pojo.getTicketStatusId().equals(Status.INPROGRESS.getStatus())))) {
								// if(Util.isNotEmptyObject(pojo) && (Util.isNotEmptyObject(pojo.getStatusId())
								// && pojo.getStatusId().equals(Status.ACTIVE.getStatus())) &&
								// ((Util.isNotEmptyObject(pojo.getTicketStatusId()) &&
								// pojo.getTicketStatusId().equals(Status.OPEN.getStatus())) ||
								// (Util.isNotEmptyObject(pojo.getDepartmentTicketStatusId()) &&
								// pojo.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())))) {
								/* second Time or (Invidual) Updating */
								employeeTicketRequestInfo.setGeneric("Invidual");
								Long ticket_No = employeeTicketDaoImpl.updateTicketStatus(mapper
										.employeeTicketRequestInfoToTicketPojoForUpdate(employeeTicketRequestInfo));
								if (ticket_No != null && ticket_No >= 0) {
									LOGGER.debug("**** The Ticket Status is updated successfully ! ****");
									result.setResponseCode(HttpStatus.success.getResponceCode());
									return result;
								}
							} else {
								LOGGER.debug("**** The Ticket Status is alredy updated successfully ! ****");
								result.setResponseCode(HttpStatus.success.getResponceCode());
								return result;
							}
						} else {
							if (Util.isNotEmptyObject(pojo)
									&& (Util.isNotEmptyObject(pojo.getStatusId())
											&& pojo.getStatusId().equals(Status.ACTIVE.getStatus()))
									&& ((Util.isNotEmptyObject(pojo.getTicketStatusId())
											&& pojo.getTicketStatusId().equals(Status.OPEN.getStatus())))) {
								// if(Util.isNotEmptyObject(pojo) && (Util.isNotEmptyObject(pojo.getStatusId())
								// && pojo.getStatusId().equals(Status.ACTIVE.getStatus())) &&
								// ((Util.isNotEmptyObject(pojo.getTicketStatusId()) &&
								// pojo.getTicketStatusId().equals(Status.OPEN.getStatus())) ||
								// (Util.isNotEmptyObject(pojo.getDepartmentTicketStatusId()) &&
								// pojo.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())))) {
								employeeTicketRequestInfo.setGeneric("highlevel");
								Long ticket_No = employeeTicketDaoImpl.updateTicketStatus(mapper
										.employeeTicketRequestInfoToTicketPojoForUpdate(employeeTicketRequestInfo));
								if (ticket_No != null && ticket_No >= 0) {
									LOGGER.debug("**** The Ticket Status is updated successfully ! ****");
									result.setResponseCode(HttpStatus.success.getResponceCode());
									return result;
								}
							} else {
								LOGGER.debug("**** The Ticket Status is alredy updated successfully ! ****");
								result.setResponseCode(HttpStatus.success.getResponceCode());
								return result;
							}
						}
					}
				} else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add(
							"***** The Exception is raised in updating Ticket if it is not updateConveresation and not forwardConversation. ******");
					throw new InformationNotFoundException(errorMsgs);
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("***** The Exception is raised in while no Ticket is Found For given Ticket Id. ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"***** The Exception is raised in while no TicketId is Found For given Method request. ******");
			throw new InformationNotFoundException(errorMsgs);
		}
		return result;
	}

	/*
	 * send ticket closing mail to customer and employee.
	 * 
	 * @param TicketId only we are sending.
	 */
	@SuppressWarnings("unused")
	private boolean sendTicketCloseMail(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException {
		LOGGER.info("**** The control is inside the sendTicketCloseMail in EmployeeTicketServiceImpl ****");
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())) {
			boolean customerMailStatus = false;
			boolean employeeMailStatus = false;
			EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
			info.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())
					? employeeTicketRequestInfo.getTicketId()
					: 0l);
			info.setRequestUrl("getTicket");
			List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
			info.setFlatBookingId(
					Util.isNotEmptyObject(ticketPojos)
							? Util.isNotEmptyObject(ticketPojos.get(0).getFlatBookingId())
									? ticketPojos.get(0).getFlatBookingId()
									: 0l
							: 0l);
			/* getting customerId from the FlatBooking Details */
			info.setRequestUrl("closeTicket.spring");
			List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl.getFlatbookingDetails(info, Status.ACTIVE);
			info.setCustomerId(
					Util.isNotEmptyObject(flatBookingPojos)
							? Util.isNotEmptyObject(flatBookingPojos.get(0).getCustomerId())
									? flatBookingPojos.get(0).getCustomerId()
									: 0l
							: 0l);
			List<CustBookInfoPojo> custBookInfoPojos = employeeTicketDaoImpl.getCustBookInfo(info, Status.ACTIVE);
			List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(info, Status.ACTIVE);
			if (Util.isNotEmptyObject(custBookInfoPojos) && Util.isNotEmptyObject(customerPojos)) {
				for (CustBookInfoPojo pojo : custBookInfoPojos) {
					LOGGER.debug("***** The Customer Mails is *****" + pojo.getEmail());
					String customerMail = pojo.getEmail();
					String[] mailArray = new String[1];
					mailArray[0] = customerMail;
					for (String mail : mailArray) {
						LOGGER.debug("**** The Customer mail is ****" + mail);
						info.setMails(mailArray);
						info.setGeneric(Util.isNotEmptyObject(customerPojos.get(0).getFirstName())
								? customerPojos.get(0).getFirstName()
								: "" + " "
										+ (Util.isNotEmptyObject(customerPojos.get(0).getLastName())
												? customerPojos.get(0).getLastName()
												: ""));
						customerMailStatus = mailServiceImpl.sendCustomerTicketCloseMail(info);
					}
				}
			}
			/* send Ticket close mail to employee */
			info = new EmployeeTicketRequestInfo();
			/* calling garbage collector to free up the space */
			System.gc();

			info.setTicketTypeDetailsId(
					Util.isNotEmptyObject(ticketPojos)
							? Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeDetailsId())
									? ticketPojos.get(0).getTicketTypeDetailsId()
									: 0l
							: 0l);
			info.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(info,
					Status.ACTIVE);

			info.setEmployeeDetailsId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())
									? ticketTypeDetailsPojos.get(0).getEmployeeDetailsId()
									: 0l
							: 0l);
			List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(info,
					Status.ACTIVE);

			info.setEmployeeId(
					Util.isNotEmptyObject(employeeDetailsPojos)
							? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
									? employeeDetailsPojos.get(0).getEmployeeId()
									: 0l
							: 0l);
			List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(info, Status.ACTIVE);
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
			if (customerMailStatus && employeeMailStatus) {
				LOGGER.info("*** The customerMail and employeeMail both sent successfully! *****");
				return true;
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"***** The Exception is raised in while sending Ticket Close mail to employee and customer. ******");
			throw new InformationNotFoundException(errorMsgs);
		}
		return false;
	}

	@Override
	public boolean sendEmployeeTicketForwardMail(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException {
		LOGGER.info("**** The control is inside the sendEmployeeTicketForwardMail in EmployeeTicketServiceImpl ****");

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getToDeptId())) {
			LOGGER.debug("**** The ticket Forward mail forwarded to department mail ****");
			employeeTicketRequestInfo.setDepartmentId(employeeTicketRequestInfo.getToDeptId());
			List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo,
					Status.ACTIVE);
			if (Util.isNotEmptyObject(departmentPojos)) {
				for (DepartmentPojo pojo : departmentPojos) {
					LOGGER.debug("***** The Department Mails is *****" + pojo.getDepartmentMail());
					String departmentMail = pojo.getDepartmentMail();
					String[] mailArray = departmentMail.split(",");
					for (String mail : mailArray) {
						LOGGER.debug("**** The Employee mail is ****" + mail);
						employeeTicketRequestInfo.setMails(mailArray);
						employeeTicketRequestInfo.setGeneric(pojo.getDepartmentName());
						/*
						 * If request is coming from system escalate it goes to systemEscalatae mail
						 * otherwise
						 */
						if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
								? employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemescalate-admin")
								: false) {
							return mailServiceImpl.sendEmployeeTicketEscalationMail(employeeTicketRequestInfo);
						} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
								? employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemescalate-employees")
								: false) {
							return mailServiceImpl.sendAdminTicketEscalationMail(employeeTicketRequestInfo);
						} else {
							return mailServiceImpl.sendEmployeeTicketForwardMail(employeeTicketRequestInfo);
						}
					}
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"***** The Exception is raised in while sending forwarding mail to department while forwarding ticket. ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getToId())) {
			employeeTicketRequestInfo.setEmployeeId(employeeTicketRequestInfo.getToId());
			List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
					Status.ACTIVE);
			if (Util.isNotEmptyObject(employeePojos)) {
				for (EmployeePojo pojo : employeePojos) {
					LOGGER.debug("***** The Employee Mails is *****" + pojo.getEmail());
					String employeeMail = pojo.getEmail();
					String[] mailArray = new String[1];
					mailArray[0] = employeeMail;
					for (String mail : mailArray) {
						LOGGER.debug("**** The Employee mail is ****" + mail);
						employeeTicketRequestInfo.setMails(mailArray);
						employeeTicketRequestInfo.setGeneric(pojo.getEmployeeName());
						/*
						 * If request is coming from system escalate it goes to systemEscalatae mail
						 * otherwise
						 */
						if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
								? employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemescalate-admin")
								: false) {
							return mailServiceImpl.sendEmployeeTicketEscalationMail(employeeTicketRequestInfo);
						} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
								? employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemescalate-employees")
								: false) {
							return mailServiceImpl.sendAdminTicketEscalationMail(employeeTicketRequestInfo);
						} else {
							return mailServiceImpl.sendEmployeeTicketForwardMail(employeeTicketRequestInfo);
						}
					}
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"***** The Exception is raised in while sending forwarding mail to employee while forwarding ticket. ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"***** The Exception is raised in while sending forwarding mail to employee or department either of those is given. ******");
			throw new InformationNotFoundException(errorMsgs);
		}
		return false;
	}

	private List<DevicePojo> getDeviceInfo(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.debug("**** The control is inside the getDeviceInfo in EmployeeTicketServiceImpl *****");
		employeeTicketRequestInfo.setRequestUrl("getTicket");
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE);
		List<DevicePojo> devicePojos = new ArrayList<DevicePojo>();

		for (TicketPojo pojo : ticketPojos) {
			employeeTicketRequestInfo.setRequestUrl("updateTicketConversation.spring");
			employeeTicketRequestInfo
					.setFlatBookingId(Util.isNotEmptyObject(pojo.getFlatBookingId()) ? pojo.getFlatBookingId() : 0l);
			List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl
					.getFlatbookingDetails(employeeTicketRequestInfo, Status.ACTIVE);
			for (FlatBookingPojo flatBookingPojo : flatBookingPojos) {
				employeeTicketRequestInfo.setCustomerId(
						Util.isNotEmptyObject(flatBookingPojo.getCustomerId()) ? flatBookingPojo.getCustomerId() : 0l);
				List<CustomerPojo> customerPojos = employeeTicketDaoImpl.getCustomerDetails(employeeTicketRequestInfo,
						Status.ACTIVE);
				for (CustomerPojo customerPojo : customerPojos) {
					employeeTicketRequestInfo.setCustomerId(
							Util.isNotEmptyObject(customerPojo.getCustomerId()) ? customerPojo.getCustomerId() : 0l);
					List<AppRegistrationPojo> appRegistrationPojos = employeeTicketDaoImpl
							.getAppregistrationDetails(employeeTicketRequestInfo, Status.ACTIVE);
					for (AppRegistrationPojo appRegistrationPojo : appRegistrationPojos) {
						employeeTicketRequestInfo.setDeviceId(Util.isNotEmptyObject(appRegistrationPojo.getDeviceId())
								? appRegistrationPojo.getDeviceId()
								: 0l);
						NotificationRequestInfo notificationRequestInfo = new NotificationRequestInfo();
						notificationRequestInfo.setRequestUrl(employeeTicketRequestInfo.getRequestUrl());
						notificationRequestInfo.setDeviceId(Util.isNotEmptyObject(appRegistrationPojo.getDeviceId())
								? appRegistrationPojo.getDeviceId()
								: 0l);
						//devicePojos.addAll(
								//notificationServiceDaoImpl.getDeviceList(notificationRequestInfo, Status.ACTIVE));
						// devicePojos =
						// notificationServiceDaoImpl.getDeviceList(notificationRequestInfo,Status.ACTIVE);
						// return Util.isNotEmptyObject(devicePojos)?devicePojos:null;
					}
				}
			}
		}
		return devicePojos;
	}

	@Override
	public List<PushNotificationInfo> createNotification(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException, IllegalAccessException, InvocationTargetException {
		LOGGER.debug("**** The control is inside the PushNotificationInfo in EmployeeTicketServiceImpl *****");

		List<PushNotificationInfo> pushNotificationInfoList = new ArrayList<PushNotificationInfo>();
		EmployeeTicketRequestInfo deviceEmployeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		BeanUtils.copyProperties(deviceEmployeeTicketRequestInfo, employeeTicketRequestInfo);

		for (DevicePojo devicePojo : getDeviceInfo(deviceEmployeeTicketRequestInfo)) {
			PushNotificationInfo pushNotificationInfo = new PushNotificationInfo();
			/* sending push notifications to IOS */
			if (Util.isNotEmptyObject(devicePojo.getOstype())
					&& devicePojo.getOstype().equalsIgnoreCase(Type.IOS.getName())) {

				pushNotificationInfo.setOsType(Type.IOS.getName());
				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo
						.getRequestUrl().equalsIgnoreCase("updateTicketConversation.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setNotificationTitle("Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage("Message: " + Util.html2text(employeeTicketRequestInfo.getMessage()));
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				}

				else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("forwardTicketDetails.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setNotificationTitle("Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage("Message: " + Util.html2text(employeeTicketRequestInfo.getMessage()));
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfoList.add(pushNotificationInfo);
						pushNotificationInfoList.add(pushNotificationInfo);

					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("closeTicket.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setNotificationTitle("Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						  if(Util.isNotEmptyObject(employeeTicketRequestInfo.getComplaintStatus())&&employeeTicketRequestInfo.getComplaintStatus().equals( MetadataId.YES.getId())) {
							  pushNotificationInfo.setNotificationMessage("Message:Dear Customer, Hope your issue/concerns has been addressed and resolved to your satisfaction. Your complaint is closed now. If you are not satisfied with the resolution provided , kindly reopen the ticket and share your concerns. Our escalation team will get back to you shortly.");

						  }else {
						pushNotificationInfo.setNotificationMessage("Message: Dear Customer your Ticket has been closed successfully!");
						
						  }
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}

				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemescalate")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setNotificationTitle("Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage("Message: Dear Customer your Ticket has been Escalated to Higher Authority for necessary Action! ");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("reOpenTicket.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setNotificationTitle("Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage("Message: Dear Customer your Ticket has been ReOpen Successfully! ");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("changeTicketType.spring")){
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setNotificationTitle("Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage("Message: We are assigning your ticket to the concern department, who will further assist you on the query. Thank you for your patience.. ");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				}else if(Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("comaplaint")){
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setNotificationTitle("Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage("Message: Dear Customer, Your query with Ticket No."+ employeeTicketRequestInfo.getTicketId()+"  has been considered as a complaint and escalated to the concerns to resolve the issue. At the earliest, we will get back to you with the resolution. ");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				}
				
			} else if (Util.isNotEmptyObject(devicePojo.getOstype())
					&& devicePojo.getOstype().equalsIgnoreCase(Type.ANDRIOD.getName())) {
				pushNotificationInfo.setOsType(Type.ANDRIOD.getName());

				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) && employeeTicketRequestInfo
						.getRequestUrl().equalsIgnoreCase("updateTicketConversation.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfo.setNotificationTitle(
								"Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage(
								"<b>Message</b> :" + Util.html2text(employeeTicketRequestInfo.getMessage()));
						pushNotificationInfo
								.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationPriority("1");
						pushNotificationInfo
								.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationStyle("picture");
						pushNotificationInfo
								.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificatioTitleColor("red");
						pushNotificationInfo.setNotificationType("message");
						pushNotificationInfo.setNotificationSound("default");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("forwardTicketDetails.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfo.setNotificationTitle(
								"Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage(
								"<b>Message</b> :" + Util.html2text(employeeTicketRequestInfo.getMessage()));
						pushNotificationInfo
								.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationPriority("high");
						pushNotificationInfo
								.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationStyle("picture");
						pushNotificationInfo
								.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificatioTitleColor("red");
						pushNotificationInfo.setNotificationType("message");
						pushNotificationInfo.setNotificationSound("default");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}

				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("closeTicket.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfo.setNotificationTitle(
								"Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						
			if(Util.isNotEmptyObject(employeeTicketRequestInfo.getComplaintStatus())&&employeeTicketRequestInfo.getComplaintStatus().equals( MetadataId.YES.getId())) {
				
				
					 pushNotificationInfo.setNotificationMessage("Message: Thank you! Hope you are satisfied with the resolution.");

		      }else {
						      pushNotificationInfo.setNotificationMessage(
								"<b>Message</b> : Dear Customer your Ticket has been closed successfully! ");
						
		        }
			//it is checking from type is emp thenit works
			   if(Util.isNotEmptyObject(employeeTicketRequestInfo.getComplaintStatus())&&employeeTicketRequestInfo.getMessage().contains("issue/concerns")) {
			         	pushNotificationInfo.setNotificationMessage("Message:Dear Customer, Hope your issue/concerns has been addressed and resolved to your satisfaction. Your complaint is closed now. If you are not satisfied with the resolution provided , kindly reopen the ticket and share your concerns. Our escalation team will get back to you shortly.");
			      }
						pushNotificationInfo
								.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationPriority("high");
						pushNotificationInfo
								.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationStyle("picture");
						pushNotificationInfo
								.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificatioTitleColor("red");
						pushNotificationInfo.setNotificationType("message");
						pushNotificationInfo.setNotificationSound("default");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("systemescalate")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfo.setNotificationTitle(
								"Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage(
								"<b>Message</b> : Dear Customer your Ticket has been Escalated to Higher Authority for necessary Action! ");
						pushNotificationInfo.setNotificationPriority("high");
						pushNotificationInfo
								.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationStyle("picture");
						pushNotificationInfo
								.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificatioTitleColor("red");
						pushNotificationInfo.setNotificationType("message");
						pushNotificationInfo.setNotificationSound("default");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("reOpenTicket.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfo.setNotificationTitle(
								"Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage(
								"<b>Message</b> : Dear Customer your Ticket has been ReOpen Successfully! ");
						pushNotificationInfo
								.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationPriority("high");
						pushNotificationInfo
								.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationStyle("picture");
						pushNotificationInfo
								.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificatioTitleColor("red");
						pushNotificationInfo.setNotificationType("message");
						pushNotificationInfo.setNotificationSound("default");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				}else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("changeTicketType.spring")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfo.setNotificationTitle(
								"Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage(
								"<b>Message</b> : We are assigning your ticket to the concern department, who will further assist you on the query. Thank you for your patience. ");
						pushNotificationInfo
								.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationPriority("high");
						pushNotificationInfo
								.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationStyle("picture");
						pushNotificationInfo
								.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificatioTitleColor("red");
						pushNotificationInfo.setNotificationType("message");
						pushNotificationInfo.setNotificationSound("default");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				}else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
						&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("complaint")) {
					if (Util.isNotEmptyObject(devicePojo) && Util.isNotEmptyObject(devicePojo.getDevicetoken())) {
						pushNotificationInfo.setDeviceToken(devicePojo.getDevicetoken());
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setStatusId(Status.ACTIVE.getStatus());
						pushNotificationInfo.setNotificationTitle(
								"Notification : TicketNo - " + employeeTicketRequestInfo.getTicketId());
						pushNotificationInfo.setNotificationMessage(
								"<b>Message</b> : Dear Customer Your query with Ticket No "+employeeTicketRequestInfo.getTicketId()+" has been considered as a complaint and escalated to the concerns to resolve the issue. At the earliest, we will get back to you with the resolution." + 
								". ");
						pushNotificationInfo
								.setNotificationImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationPriority("high");
						pushNotificationInfo
								.setNotificationIcon("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificationStyle("picture");
						pushNotificationInfo
								.setNotificationPicture("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
						pushNotificationInfo.setNotificatioTitleColor("red");
						pushNotificationInfo.setNotificationType("message");
						pushNotificationInfo.setNotificationSound("default");
						pushNotificationInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
						pushNotificationInfoList.add(pushNotificationInfo);
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs
								.add("***** The Exception is raised in while sending Notification to Customer. ******");
						throw new InformationNotFoundException(errorMsgs);
					}
				}
			}
		}
		return pushNotificationInfoList;
	}

	private String insertTicketConversationDocuments(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside insertTicketConversationDocuments in EmployeeTicketServiceImpl ******"
				+ employeeTicketRequestInfo.getDocumentsLocation());
		Base64FileUtil util = new Base64FileUtil();
		String documentLocation = null;
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFileInfos())
				&& employeeTicketRequestInfo.getFileInfos() != null
				&& !employeeTicketRequestInfo.getFileInfos().isEmpty()) {
			documentLocation = util.store(employeeTicketRequestInfo.getFileInfos(),
					employeeTicketRequestInfo.getDocumentsLocation());
		}
		employeeTicketRequestInfo.setDocumentsLocation(documentLocation != null ? documentLocation : "NA");
		LOGGER.debug("**** The  insertTicketConversationDocuments location is ****" + documentLocation);
		return documentLocation;
	}

	private String createTicketConversationDocumentsLocation(
			@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(
				"**** The Control is inside createTicketConversationDocumentsLocation in EmployeeTicketServiceImpl ******");
		String TICKETING_DIRCTORY_PATH = new ResponceCodesUtil().getApplicationProperties()
				.getProperty("TICKETING_DIRCTORY_PATH");
		String id = employeeTicketDaoImpl.getTicketConversationDocumentsDetailsSeqId(employeeTicketRequestInfo);
		employeeTicketRequestInfo.setTicketConversationDocumentId(Long.valueOf(id));
		String path = TICKETING_DIRCTORY_PATH + File.separator + employeeTicketRequestInfo.getTicketId()
				+ File.separator + id;
		LOGGER.debug("**** The file storing path returned is *****" + path);
		return path;
	}

	@Override
	public Result getCustomerRaisedTicketList(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("**** The Control is inside getCustomerRaisedTicketList in EmployeeTicketServiceImpl ******");
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketPojo> employeeTicketList = new ArrayList<TicketPojo>();
		Page<TicketPojo> page = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE,
		employeeTicketRequestInfo.getPageNo().intValue(), employeeTicketRequestInfo.getPageSize().intValue());
		employeeTicketResponse.setPageCount(Util.isNotEmptyObject(page.getPagesAvailable()) ? page.getPagesAvailable() : 0l);
		List<TicketPojo> customerTicketList = page.getPageItems();
		// List<TicketPojo> employeeInactiveTicketList =
		// employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo,Status.INACTIVE);
		employeeTicketList.addAll(customerTicketList);
		// employeeTicketList.addAll(employeeInactiveTicketList);
		/* Tickets are sorted based on the default natural sorting order */
		// Collections.sort(employeeTicketList);
		Collections.sort(employeeTicketList, Collections.reverseOrder());
		if (Util.isNotEmptyObject(employeeTicketList)) {
			employeeTicketResponse = addingTicketResponse(mapper.employeeTicketListToemployeeTicketResponse(employeeTicketList));
			employeeTicketResponse.setTicketResponseList(Util.isNotEmptyObject(employeeTicketResponse.getTotalTicketResponseList())
		    ? employeeTicketResponse.getTotalTicketResponseList() : null);
		}
		LOGGER.debug("*** The employeeTicketResponse is ***" + employeeTicketResponse);
		return employeeTicketResponse;
	}

	private EmployeeTicketResponse addingTicketResponse(@NonNull EmployeeTicketResponse employeeTicketResponse) {
		LOGGER.info("**** The Control is inside the addingTicketResponse in EmployeeTicketServiceImpl *****");
		for (TicketResponse response : employeeTicketResponse.getTotalTicketResponseList()) {
			if (Util.isNotEmptyObject(response.getTicketStatusId())) {
				if (response.getTicketStatusId().equals(Status.NEW.getStatus())) {
					response.setStatus(Status.NEW.getDescription());
				} else if (response.getTicketStatusId().equals(Status.OPEN.getStatus())) {
					response.setStatus(Status.OPEN.getDescription());
				} else if (response.getTicketStatusId().equals(Status.INPROGRESS.getStatus())) {
					response.setStatus(Status.INPROGRESS.getDescription());
				} else if (response.getTicketStatusId().equals(Status.CLOSED.getStatus())) {
					response.setStatus(Status.CLOSED.getDescription());
				} else {
					response.setStatus(Status.REOPEN.getDescription());
				}
			}
		}
		return employeeTicketResponse;
	}

	@Override
	public Result getCustomerTicketDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws IllegalAccessException, InvocationTargetException, InterruptedException, ExecutionException {
		LOGGER.info("**** The Control is inside the getCustomerTicketDetails in EmployeeTicketServiceImpl *****");
		Status status = null;
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();
		TicketResponse response = new TicketResponse();
		if (employeeTicketRequestInfo.getStatusId().equals(Status.ACTIVE.getStatus())) {
			status = Status.ACTIVE;
		} else {
			status = Status.INACTIVE;
		}
		List<TicketPojo> customerTicketList = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, status);
		/* Ticket Reopen button enabled only when the d/f b/w create and current hours must be < 96 hours */
		Integer ticketCreateCurrentHoursDifference = 0;
		try {
			ticketCreateCurrentHoursDifference = Integer.valueOf(responceCodesUtil.getApplicationNamePropeties("TICKET_CREATE_CURRENT_HOURS_DIFFERENCE"));
		}catch(Exception ex) {
			LOGGER.error("*** The Exception is occurred inside getCustomerRaisedTicketList in EmployeeTicketServiceImpl ***"+ex.getMessage());
		}
		for(TicketPojo ticketPojo : customerTicketList) {
			Timestamp currentTime = new Timestamp(new Date().getTime());
		
			if(Util.isNotEmptyObject(ticketPojo) && Util.isNotEmptyObject(ticketPojo.getCreatedDate())) {
				if((Status.CLOSED.status.equals(ticketPojo.getTicketStatusId()) || Status.CLOSED.status.equals(ticketPojo.getDepartmentTicketStatusId()))
					&& (TimeUtil.differenceBetweenTwoTimestampsInHours(currentTime, ticketPojo.getCreatedDate()) < ticketCreateCurrentHoursDifference)){
					ticketPojo.setIsTicketReopenEnable(true);
					
					ticketPojo.setReopenenDate(TimeUtil.addHours(ticketPojo.getCreatedDate(), 96));
					response.setReopenenDate(TimeUtil.addHours(ticketPojo.getCreatedDate(), 96));
					
				}else {
					ticketPojo.setReopenenDate(TimeUtil.addHours(ticketPojo.getCreatedDate(), 96));
					response.setReopenenDate(TimeUtil.addHours(ticketPojo.getCreatedDate(), 96));
				}
			}
		}
		

		BookingFormRequest bookingFormRequest = new BookingFormRequest();
		bookingFormRequest.setRequestUrl("venkat");
		bookingFormRequest.setFlatBookingId(Util.isNotEmptyObject(customerTicketList)
						? Util.isNotEmptyObject(customerTicketList.get(0).getFlatBookingId())? customerTicketList.get(0).getFlatBookingId(): 0l: 0l);
		List<CustomerPropertyDetailsPojo> customerPropertyDetailsPojos = bookingFormServiceDaoImpl.getCustomerPropertyDetails(bookingFormRequest);

		if (Util.isNotEmptyObject(customerTicketList)) {
			BeanUtils.copyProperties(response, customerTicketList.get(0));
		
			if (Util.isNotEmptyObject(response.getTicketStatusId())) {
				if (response.getTicketStatusId().equals(Status.NEW.getStatus())) {
					response.setStatus(Status.NEW.getDescription());
				} else if (response.getTicketStatusId().equals(Status.OPEN.getStatus())) {
					response.setStatus(Status.OPEN.getDescription());
				} else if (response.getTicketStatusId().equals(Status.INPROGRESS.getStatus())) {
					response.setStatus(Status.INPROGRESS.getDescription());
				} else if (response.getTicketStatusId().equals(Status.REOPEN.getStatus())) {
					response.setStatus(Status.REOPEN.getDescription());
					
				} else {
					response.setStatus(Status.CLOSED.getDescription());
				}
			}
			employeeTicketRequestInfo.setTicketTypeDetailsId(response.getTicketTypeDetailsId());
			employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
					.getTicketTypeDetails(employeeTicketRequestInfo, status);
			if (Util.isNotEmptyObject(ticketTypeDetailsPojos)) {
				employeeTicketRequestInfo.setEmployeeDetailsId(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId());
				employeeTicketRequestInfo
						.setDepartmentId(Util.isNotEmptyObject(ticketTypeDetailsPojos)
								? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
										? ticketTypeDetailsPojos.get(0).getDepartmentId()
										: 0l
								: 0l);
				List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(employeeTicketRequestInfo,
						Status.ACTIVE);
				List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl
						.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
				if (Util.isNotEmptyObject(employeeDetailsPojos)) {
					employeeTicketRequestInfo
							.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)
									? Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())
											? employeeDetailsPojos.get(0).getEmployeeId()
											: 0l
									: 0l);
					List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
							Status.ACTIVE);
					// response.setAssignedEmployee(employeePojos.get(0).getFirstName()+"
					// "+employeePojos.get(0).getLastName()+"/"+employeeDetailsPojos.get(0).getEmployeeDesignation());
					
					
					response.setAssignedEmployee(
							(Util.isNotEmptyObject(employeePojos)
									? Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())
											? employeePojos.get(0).getEmployeeName()
											: "N/A"
									: "N/A")
									+ "/"
									+ (Util.isNotEmptyObject(Pojos)
											? Util.isNotEmptyObject(Pojos.get(0).getDepartmentName())
													? Pojos.get(0).getDepartmentName()
													: "N/A"
											: "N/A"));
					// response.setAssignedEmployee((Util.isNotEmptyObject(employeePojos.get(0).getFirstName())?employeePojos.get(0).getFirstName():"")+"
					// "+(Util.isNotEmptyObject(employeePojos.get(0).getLastName())?employeePojos.get(0).getLastName():"")+"/"+employeeDetailsPojos.get(0).getEmployeeDesignation());
				}
			}

			// List<FileInfo> fileInfos =
			// createCustomerDocumentUrls(employeeTicketRequestInfo);
			Future<List<FileInfo>> fileInfos = employeeTicketServiceHelper
					.createCustomerDocumentUrls(employeeTicketRequestInfo);
			// ticketResponse.setFileInfos(createDocumentUrls(fileEmployeeTicketRequestInfo));
			do {
				/* adding ticket FileInfos */
				if (fileInfos.isDone()) {
					response.setFileInfos(fileInfos.get());
					break;
				}
			} while (true);
			// List<FileInfo> fileInfos = createDocumentUrls(employeeTicketRequestInfo);

			List<TicketCommentsPojo> ticketCommentsPojos = employeeTicketDaoImpl
					.getTicketComments(employeeTicketRequestInfo, Status.ACTIVE);
			List<TicketComment> ticketComments = new ArrayList<TicketComment>();
			/* create customer chat */
			if (Util.isNotEmptyObject(ticketCommentsPojos)) {
				EmployeeTicketMapper mapper = new EmployeeTicketMapper();
				/* getting the all Ticket Comments */
				for (TicketCommentsPojo ticketCommentsPojo : ticketCommentsPojos) {
					/* here conversation is happend in between the customer and crm employee . */
					if (ticketCommentsPojo.getVisibleType().equalsIgnoreCase("PUBLIC")
							&& (ticketCommentsPojo.getFromType().equals(Department.CUSTOMER.getId())
									|| ticketCommentsPojo.getToType().equals(Department.CUSTOMER.getId()))) {
						TicketComment ticketComment = mapper.TicketCommentsPojoToTicketComments(ticketCommentsPojo);
						ticketComments.add(ticketComment);
					}
					/* here conversation is happend in between the employees and employees. */
					else {
						/* Ticket must have either fromdeptId or fromId and TodeptId or ToId. */
						if ((Util.isNotEmptyObject(ticketCommentsPojo.getFromDeptId())
								|| Util.isNotEmptyObject(ticketCommentsPojo.getFromId()))
								&& (Util.isNotEmptyObject(ticketCommentsPojo.getToDeptId())
										|| Util.isNotEmptyObject(ticketCommentsPojo.getToId()))) {
							StringBuilder customerMsg = new StringBuilder(
									"Dear " + (Util.isNotEmptyObject(customerPropertyDetailsPojos)
											? Util.isNotEmptyObject(
													customerPropertyDetailsPojos.get(0).getCustomerName())
															? customerPropertyDetailsPojos.get(0).getCustomerName()
															: "customer"
											: "customer") + " Your Ticket  has been Forwarded From ");

							/* If TicketConversation is Raised from Employee */
							//if (Util.isNotEmptyObject(ticketCommentsPojo.getFromId())) {
								//employeeTicketRequestInfo.setEmployeeId(ticketCommentsPojo.getFromId());
								// List<EmployeeDetailsPojo> employeeDetailsPojos =
								// employeeTicketDaoImpl.getEmployeeDetails(employeeTicketRequestInfo,
								// Status.ACTIVE);
								//List<EmployeePojo> employeePojos = employeeTicketDaoImpl
									//	.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
								//if (Util.isNotEmptyObject(employeePojos)) {
									//employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)?Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())?employeeDetailsPojos.get(0).getEmployeeId():0l:0l);
									// List<EmployeePojo> employeePojos =
									// employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,Status.ACTIVE);
									// customerMsg.append((Util.isNotEmptyObject(employeePojos.get(0).getFirstName())?employeePojos.get(0).getFirstName():"")+"
									// "+(Util.isNotEmptyObject(employeePojos.get(0).getLastName())?employeePojos.get(0).getLastName():""));
								//	//customerMsg.append(employeePojos.get(0).getEmployeeName() + " to ");
							//	}
							//}
							/* If TicketConversation is Raised from Department */
							 if (Util.isNotEmptyObject(ticketCommentsPojo.getFromDeptId())) {
								EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
								employeeTicketRequestInfoObj.setDepartmentId(ticketCommentsPojo.getFromDeptId());
								List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl
										.getDepartmentDetails(employeeTicketRequestInfoObj, Status.ACTIVE);
								if (Util.isNotEmptyObject(departmentPojos)) {
									customerMsg.append(departmentPojos.get(0).getDepartmentName() + " to ");
								}
							}
							if (Util.isNotEmptyObject(ticketCommentsPojo.getToDeptId())) {
								EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
								employeeTicketRequestInfoObj.setDepartmentId(ticketCommentsPojo.getToDeptId());
								List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl
										.getDepartmentDetails(employeeTicketRequestInfoObj, Status.ACTIVE);
								if (Util.isNotEmptyObject(departmentPojos)) {
									customerMsg.append(departmentPojos.get(0).getDepartmentName());
								}
							} 
							else if (Util.isNotEmptyObject(ticketCommentsPojo.getToId())) {
								employeeTicketRequestInfo.setEmployeeId(ticketCommentsPojo.getToId());
								// List<EmployeeDetailsPojo> employeeDetailsPojos =
								// employeeTicketDaoImpl.getEmployeeDetails(employeeTicketRequestInfo,
								// Status.ACTIVE);
								List<EmployeePojo> employeePojos = employeeTicketDaoImpl
										.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
								if (Util.isNotEmptyObject(employeePojos)) {
									// employeeTicketRequestInfo.setEmployeeId(Util.isNotEmptyObject(employeeDetailsPojos)?Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())?employeeDetailsPojos.get(0).getEmployeeId():0l:0l);
									// List<EmployeePojo> employeePojos =
									// employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
									// customerMsg.append((Util.isNotEmptyObject(employeePojos.get(0).getFirstName())?employeePojos.get(0).getFirstName():"")+"
									// "+(Util.isNotEmptyObject(employeePojos.get(0).getLastName())?employeePojos.get(0).getLastName():""));
									customerMsg.append(employeePojos.get(0).getEmployeeName().split("-")[1]);
								}
							}
							ticketCommentsPojo.setMessage(customerMsg.toString());
							TicketComment ticketComment = mapper.TicketCommentsPojoToTicketComments(ticketCommentsPojo);
							ticketComments.add(ticketComment);
						}
					}
				}
				/* adding Ticket Comments */
				response.setTicketComments(ticketComments);
			}
		}
		ticketResponses.add(response);
		employeeTicketResponse.setTicketResponseList(ticketResponses);
		return employeeTicketResponse;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result chatSubmit(EmployeeTicketRequestInfo employeeTicketRequestInfo) throws InformationNotFoundException,
			SQLInsertionException, IllegalAccessException, InvocationTargetException {
		LOGGER.info("**** The Control is inside the chatSubmit in EmployeeTicketServiceImpl *****");
		Result result = new Result();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFileInfos())) {
			/* creating ticket conversation document location */
			employeeTicketRequestInfo
					.setDocumentsLocation(createTicketConversationDocumentsLocation(employeeTicketRequestInfo));
			/* setting visible type public */
			employeeTicketRequestInfo.setVisibleType(Visibility.PUBLIC.getDescription());
			/* inserting Ticket conversation Documentation */
			Long record_No = employeeTicketDaoImpl.insertTicketConversationDocuments(employeeTicketRequestInfo,
					Status.ACTIVE);
			employeeTicketRequestInfo.setTicketConversationDocumentId(record_No);
		}
		employeeTicketRequestInfo.setRequestUrl("getTicket");
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE);
		if (Util.isNotEmptyObject(ticketPojos)) {
			employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");
			employeeTicketRequestInfo.setTicketTypeDetailsId(ticketPojos.get(0).getTicketTypeDetailsId());
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
					.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);
			if (Util.isNotEmptyObject(ticketTypeDetailsPojos)) {
				employeeTicketRequestInfo.setDepartmentId(ticketTypeDetailsPojos.get(0).getDepartmentId());
				employeeTicketRequestInfo.setEmployeeDetailsId(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId());
				List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl
						.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
				if (Util.isNotEmptyObject(employeeDetailsPojos)) {
					employeeTicketRequestInfo.setEmployeeId(employeeDetailsPojos.get(0).getEmployeeId());
					mapper.employeeTicketRequestInfoToTicketCommentsPojo(employeeTicketRequestInfo);
					Long pk = employeeTicketDaoImpl.insertTicketConversation(
							mapper.employeeTicketRequestInfoToTicketCommentsPojo(employeeTicketRequestInfo));

					if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFileInfos())) {
						final EmployeeTicketRequestInfo employeeTicketRequestInfoFinal = new EmployeeTicketRequestInfo();
						BeanUtils.copyProperties(employeeTicketRequestInfoFinal, employeeTicketRequestInfo);
						Thread thread = new Thread() {
							public void run() {
								LOGGER.debug(
										"**** The control is inside upload ticket documents into the server *****");
								insertTicketConversationDocuments(employeeTicketRequestInfoFinal);
							}
						};
						thread.start();
					}
					if (pk > 0) {
						result.setResponseCode(pk.intValue());
						return result;
					} else {
						List<String> errorMsgs = new ArrayList<String>();
						errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation ******");
						throw new SQLInsertionException(errorMsgs);
					}
				} else {
					List<String> errorMsgs = new ArrayList<String>();
					errorMsgs.add(
							"***** The Exception is raised in while chatSubmit from Customer.in ticketTypeDetailsPojos ******");
					throw new InformationNotFoundException(errorMsgs);
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"***** The Exception is raised in while chatSubmit from Customer.in ticketTypeDetailsPojos ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** The Exception is raised in while chatSubmit from customer.in ticketPojos ******");
			throw new InformationNotFoundException(errorMsgs);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result insertExtendEsacalationTime(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws SQLInsertionException, InterruptedException, ExecutionException {
		LOGGER.info("**** The Control is inside the extendEscalationTime in EmployeeTicketServiceImpl ****");
		Result result = new Result();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		List<Long> ticketExtendedEscalationApprovalList = new ArrayList<Long>();

		employeeTicketRequestInfo.setRequestUrl("getTicket");
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE);

		employeeTicketRequestInfo
				.setTicketTypeDetailsId(Util.isNotEmptyObject(ticketPojos)
						? Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeDetailsId())
								? ticketPojos.get(0).getTicketTypeDetailsId()
								: 0l
						: 0l);

		employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");

		// List<TicketTypeDetailsPojo> ticketTypeDetailsPojos =
		// employeeTicketDaoImpl.getTicketTypeDetails(employeeTicketRequestInfo,
		// Status.ACTIVE);

		employeeTicketRequestInfo.setTicketTypeId(Util.isNotEmptyObject(ticketPojos)
				? Util.isNotEmptyObject(ticketPojos.get(0).getTicketTypeId()) ? ticketPojos.get(0).getTicketTypeId()
						: 0l
				: 0l);
		// employeeTicketRequestInfo.setTicketTypeId(Util.isNotEmptyObject(ticketTypeDetailsPojos)?Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getTicketTypeId())?ticketTypeDetailsPojos.get(0).getTicketTypeId():0l:0l);
		employeeTicketRequestInfo.setTypeOf(com.sumadhura.employeeservice.enums.Type.EXTENDED_ESCALATION_TIME.getId());

		/* Adding 23 hours 59 minutes extended escalation time */
		employeeTicketRequestInfo
				.setExtendedEscalationTime(TimeUtil.add23Hours(employeeTicketRequestInfo.getExtendedEscalationTime()));

		/* setting current escalation time */
		employeeTicketRequestInfo
				.setEscalationTime(Util.isNotEmptyObject(ticketPojos)
						? Util.isNotEmptyObject(ticketPojos.get(0).getEstimatedResolvedDate())
								? ticketPojos.get(0).getEstimatedResolvedDate()
								: new Timestamp(new Date().getTime())
						: new Timestamp(new Date().getTime()));

		/*
		 * getting Ticket Owner name Future<List<CustomerPropertyDetailsPojo>> future =
		 * employeeTicketServiceHelper.getTicketOwner(employeeTicketRequestInfo);
		 * while(true) { if(future.isDone()) { List<CustomerPropertyDetailsPojo>
		 * customerPropertyDetails = future.get(); /* setting siteId
		 * employeeTicketRequestInfo.setSiteId(Util.isNotEmptyObject(
		 * customerPropertyDetails)?Util.isNotEmptyObject(customerPropertyDetails.get(0)
		 * .getSiteId())?customerPropertyDetails.get(0).getSiteId():0l:0l); break; } }
		 */

		employeeTicketRequestInfo.setRequestUrl("getExtendedEscalationTimeApprovalLevel");
		/* check If ticket is already approved or not */
		List<TicketExtendedEscalationApprovalPojo> ticketExtendedEscalationApprovalPojos = employeeTicketDaoImpl
				.getExtendEsacalationTimeDetails(employeeTicketRequestInfo, Status.APPROVED);

		/* Ticket is not approved atleast once upto now send to first level */
		if (Util.isEmptyObject(ticketExtendedEscalationApprovalPojos)) {
			List<TicketEscalationExtenstionApprovalLevelMappingPojo> approvalLevelMappingPojos = employeeTicketDaoImpl
					.getTicketEscaltionExtentionAprovalLevelDetails(employeeTicketRequestInfo, Status.ACTIVE);
			/*    */
			if (Util.isNotEmptyObject(approvalLevelMappingPojos)
					&& Util.isNotEmptyObject(approvalLevelMappingPojos.get(0))
					&& Util.isNotEmptyObject(approvalLevelMappingPojos.get(0).getId())) {
				employeeTicketRequestInfo
						.setTicketEscalationExtenstionApprovalLevelMappingId(approvalLevelMappingPojos.get(0).getId());
			}
			/* Ticket is approved already now we need to send next level */
		} else {
			if (Util.isNotEmptyObject(ticketExtendedEscalationApprovalPojos)) {
				/* finding last level mapping id */
				for (TicketExtendedEscalationApprovalPojo pojo : ticketExtendedEscalationApprovalPojos) {
					if (Util.isNotEmptyObject(pojo.getTicketEscalationExtenstionApprovalLevelMappingId())) {
						ticketExtendedEscalationApprovalList
								.add(pojo.getTicketEscalationExtenstionApprovalLevelMappingId());
					}
				}
				employeeTicketRequestInfo.setRequestUrl("insertExtendEsacalationTime");
				employeeTicketRequestInfo.setTicketEscalationExtenstionApprovalLevelMappingId(
						ticketExtendedEscalationApprovalList.get(ticketExtendedEscalationApprovalList.size() - 1));
				List<TicketEscalationExtenstionApprovalLevelMappingPojo> approvalLevelMappingPojos = employeeTicketDaoImpl
						.getTicketEscaltionExtentionAprovalLevelDetails(employeeTicketRequestInfo, Status.ACTIVE);

				if (Util.isNotEmptyObject(approvalLevelMappingPojos)
						&& Util.isNotEmptyObject(approvalLevelMappingPojos.get(0))
						&& Util.isNotEmptyObject(approvalLevelMappingPojos.get(0).getId())) {
					employeeTicketRequestInfo.setTicketEscalationExtenstionApprovalLevelMappingId(
							approvalLevelMappingPojos.get(0).getTicketEscalationExtenstionRequestNextLevelId());
				}
			}
		}
		// List<TicketEscalationExtenstionApprovalLevelPojo> approvalLevelPojos =
		// employeeTicketDaoImpl.getTicketEscalationExtenstionApprovalLevalDetails(employeeTicketRequestInfo,Status.ACTIVE);
		/*
		 * employeeTicketRequestInfo.setEmployeeDetailsId(Util.isNotEmptyObject(
		 * ticketTypeDetailsPojos)?Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).
		 * getEscalatedTicketAssignedEmployeeId())?ticketTypeDetailsPojos.get(0).
		 * getEscalatedTicketAssignedEmployeeId():0l:0l); List<EmployeeDetailsPojo>
		 * employeeDetailsPojos =
		 * employeeTicketDaoImpl.getEmployeeDetails(employeeTicketRequestInfo,
		 * Status.ACTIVE);
		 * employeeTicketRequestInfo.setToId(Util.isNotEmptyObject(employeeDetailsPojos)
		 * ?Util.isNotEmptyObject(employeeDetailsPojos.get(0).getEmployeeId())?
		 * employeeDetailsPojos.get(0).getEmployeeId():0l:0l);
		 */
		// employeeTicketRequestInfo.setToId(Util.isNotEmptyObject(approvalLevelPojos)?Util.isNotEmptyObject(approvalLevelPojos.get(0).getId())?approvalLevelPojos.get(0).getId():0l:0l);
		// Long effectedRows =
		// employeeTicketDaoImpl.updateTicketEscalationExtenstionApprovalStatus(employeeTicketRequestInfo.getTicketId());

		TicketExtendedEscalationApprovalPojo ticketExtendedEscalationApprovalPojo = mapper
				.employeeTicketRequestInfoToTicketExtendedEscalationApprovalPojo(employeeTicketRequestInfo,
						ticketPojos);
		Long pk = employeeTicketDaoImpl.insertExtendEsacalationTime(ticketExtendedEscalationApprovalPojo);
		if (pk > 0) {
			LOGGER.debug("**** The PrimaryKey returned is ****" + pk);
			result.setResponseCode(pk.intValue());
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation ******");
			throw new SQLInsertionException(errorMsgs);
		}
	}

	@Override
	public Result getExtendedEscalationTimeApprovalLevel(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(
				"**** The Control is inside the getExtendedEscalationTimeApprovalLevel in EmployeeTicketServiceImpl *****");
		Result result = new Result();
		List<Long> ticketEscalationExtenstionApprovalLevelList = new ArrayList<Long>();
		List<Long> ticketEscalationExtenstionApprovedLevelList = new ArrayList<Long>();

		List<TicketEscalationExtenstionApprovalLevelMappingPojo> approvalLevelMappingPojos = employeeTicketDaoImpl
				.getTicketEscaltionExtentionAprovalLevelDetails(employeeTicketRequestInfo, Status.ACTIVE);
		if (Util.isNotEmptyObject(approvalLevelMappingPojos)) {
			/* adding ticketEscalationExtenstionApprovalLevelList */
			for (TicketEscalationExtenstionApprovalLevelMappingPojo pojo : approvalLevelMappingPojos) {
				if (Util.isNotEmptyObject(pojo.getLevelId()))
					ticketEscalationExtenstionApprovalLevelList.add(pojo.getLevelId());
			}

			List<TicketExtendedEscalationApprovalPojo> approvalPojos = employeeTicketDaoImpl
					.getExtendEsacalationTimeDetails(employeeTicketRequestInfo, Status.NOTAPPROVED);
			if (Util.isEmptyObject(approvalPojos)) {
				List<TicketExtendedEscalationApprovalPojo> ticketExtendedEscalationApprovalPojos = employeeTicketDaoImpl
						.getExtendEsacalationTimeDetails(employeeTicketRequestInfo, Status.APPROVED);
				/* Ticket Doesnot get Approve Upto now */
				if (Util.isEmptyObject(ticketExtendedEscalationApprovalPojos)) {
					/* given access to ask request */
					result.setResponseCode(HttpStatus.EligibleExtendedEscalationApprovalLevel.getResponceCode());
					result.setDescription(HttpStatus.EligibleExtendedEscalationApprovalLevel.getDescription());

				} else {
					for (TicketExtendedEscalationApprovalPojo pojo : ticketExtendedEscalationApprovalPojos) {
						if (Util.isNotEmptyObject(pojo.getLevelId())) {
							ticketEscalationExtenstionApprovedLevelList.add(pojo.getLevelId());
						}
					}
					/* Employee can ask extended escalation request */
					if (ticketEscalationExtenstionApprovalLevelList.size() > ticketEscalationExtenstionApprovedLevelList
							.size()) {
						result.setResponseCode(HttpStatus.EligibleExtendedEscalationApprovalLevel.getResponceCode());
						result.setDescription(HttpStatus.EligibleExtendedEscalationApprovalLevel.getDescription());
						/*
						 * Employee cant ask extended escalation request already escalation level
						 * reached .
						 */
					} else {
						result.setResponseCode(HttpStatus.ExaustedExtendedEscalationApprovalLevel.getResponceCode());
						result.setDescription(HttpStatus.ExaustedExtendedEscalationApprovalLevel.getDescription());
					}
				}
				/* Ticket Extended escalation Request is already in pending state */
			} else {
				result.setResponseCode(HttpStatus.PendingExtendedEscalationApprovalLevel.getResponceCode());
				result.setDescription(HttpStatus.PendingExtendedEscalationApprovalLevel.getDescription());
			}
			/* Ticket doesnot have approval levels */
		} else {
			result.setResponseCode(HttpStatus.NoExtendedEscalationApprovalLevel.getResponceCode());
			result.setDescription(HttpStatus.NoExtendedEscalationApprovalLevel.getDescription());
		}
		return result;
	}

	@Override
	public Result getExtendEsacalationTimeDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException {
		LOGGER.info(
				"**** The Control is inside the getExtendEsacalationTimeDetails in EmployeeTicketServiceImpl *****");
		List<TicketExtendedEscalationApprovalPojo> approvalPojos = new ArrayList<TicketExtendedEscalationApprovalPojo>();
		employeeTicketRequestInfo.setRequestUrl("getExtendEsacalationTimeDetails");
		/*
		 * List<TicketEscalationExtenstionApprovalLevelPojo> approvalLevelPojos =
		 * employeeTicketDaoImpl.getTicketEscalationExtenstionApprovalLevalDetails(
		 * employeeTicketRequestInfo,Status.ACTIVE);
		 * if(Util.isNotEmptyObject(approvalLevelPojos)) { for(
		 * TicketEscalationExtenstionApprovalLevelPojo pojo : approvalLevelPojos) {
		 * employeeTicketRequestInfo.setTicketExtendedEscalationApprovalLevelId(Util.
		 * isNotEmptyObject(pojo.getId())?pojo.getId():0l);
		 * List<TicketExtendedEscalationApprovalPojo>
		 * ticketExtendedEscalationApprovalPojos =
		 * employeeTicketDaoImpl.getExtendEsacalationTimeDetails(
		 * employeeTicketRequestInfo, Status.ACTIVE); if
		 * (!ticketExtendedEscalationApprovalPojos.isEmpty()) {
		 * approvalPojos.addAll(ticketExtendedEscalationApprovalPojos); } }
		 */
		List<TicketExtendedEscalationApprovalPojo> ticketExtendedEscalationApprovalPojos = employeeTicketDaoImpl
				.getExtendEsacalationTimeDetails(employeeTicketRequestInfo, Status.NOTAPPROVED);
		System.out.println("emloyeedept"+employeeTicketRequestInfo.getDepartmentId());
		
		for(TicketExtendedEscalationApprovalPojo pojo:ticketExtendedEscalationApprovalPojos) {
		if(Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId())&&employeeTicketRequestInfo.getDepartmentId()==988l) {//for management dept we can show 2 days for other depts 1 day only  
			//ticketExtendedEscalationApprovalPojos.get(0).setNoOfDays(2l);
			pojo.setNoOfDays(2l);
		}else {
			//ticketExtendedEscalationApprovalPojos.get(0).setNoOfDays(1l);
			pojo.setNoOfDays(1l);
		}
		}
		
		if (!ticketExtendedEscalationApprovalPojos.isEmpty()) {
			approvalPojos.addAll(ticketExtendedEscalationApprovalPojos);
		}

		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		return mapper.ticketExtendedEscalationApprovalPojosToticketExtendedEscalationApproval(approvalPojos);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result updateExtendEsacalationTimeDetailsStatus(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InSufficeientInputException {
		LOGGER.info(
				"**** The Control is inside the updateExtendEsacalationTimeDetailsStatus in EmployeeTicketServiceImpl *****");
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("approval")
				&& Util.isNotEmptyObject(employeeTicketRequestInfo.getApprovedEscalationDate())) {
			Result result = new Result();
			employeeTicketRequestInfo.setTicketExtendedEscalationApprovalStatusId(Status.APPROVED.getStatus());
			TicketExtendedEscalationApprovalPojo pojo = mapper
					.employeeTicketRequestInfoToTicketExtendedEscalationApprovalPojo(employeeTicketRequestInfo);
			Long ticketEscalationExtenstionApprovalEffectedRows = employeeTicketDaoImpl
					.updateTicketEscalationExtenstionApprovalStatus(pojo);
			// List<TicketExtendedEscalationApprovalPojo> approvalPojos =
			// employeeTicketDaoImpl.getExtendEsacalationTimeDetails(employeeTicketRequestInfo,
			// Status.INACTIVE);
			Long ticketEffectedRows = employeeTicketDaoImpl.updateTicketEstimatedResolvedDate(
					mapper.employeeTicketRequestInfoToTicketPojo(employeeTicketRequestInfo));
			/* Inserting TicketStatistics table while updating the ticket table */
			if (Util.isNotEmptyObject(ticketEffectedRows)) {
				insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
			}
			LOGGER.debug("**** The Number of effected Rows ticketEscalationExtenstionApprovalEffectedRows ****"
					+ ticketEscalationExtenstionApprovalEffectedRows);
			LOGGER.debug("**** The Number of effected Rows ticketEffectedRows ****" + ticketEffectedRows);
			return result;
		} else if (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("reject")) {
			Result result = new Result();
			employeeTicketRequestInfo.setTicketExtendedEscalationApprovalStatusId(Status.REJECTED.getStatus());
			TicketExtendedEscalationApprovalPojo pojo = mapper
					.employeeTicketRequestInfoToTicketExtendedEscalationApprovalPojo(employeeTicketRequestInfo);
			Long effectedRows = employeeTicketDaoImpl.updateTicketEscalationExtenstionApprovalStatus(pojo);
			result.setResponseCode(effectedRows.intValue());
			LOGGER.debug("**** The Number of effected Rows ****" + effectedRows);
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Insuffient input is given");
			throw new InSufficeientInputException(errorMsgs);
		}
	}

	@Override
	public Result getSystemEscalatedTicketDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException, InvalidStatusException {
		LOGGER.info("**** The Control is inside the getSystemEscalatedTicketDetails in EmployeeTicketServiceImpl ****");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();

		/*
		 * employeeTicketRequestInfo.setRequestUrl(
		 * "getSystemEscalatedTicketDetails.spring");
		 */

		Page<TicketEscalationPojo> page = employeeTicketDaoImpl.getTicketEscalationDtls(employeeTicketRequestInfo,
				Status.ACTIVE, employeeTicketRequestInfo.getPageNo().intValue(),
				employeeTicketRequestInfo.getPageSize().intValue());
		employeeTicketResponse
				.setPageCount(Util.isNotEmptyObject(page.getPagesAvailable()) ? page.getPagesAvailable() : 0l);
		List<TicketEscalationPojo> ticketEscalationPojos = page.getPageItems();
		LOGGER.debug("*** The ticketEscalationPojos is ***" + ticketEscalationPojos);
		if (Util.isNotEmptyObject(ticketEscalationPojos)) {
			for (TicketEscalationPojo pojo : ticketEscalationPojos) {
				EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
				employeeTicketRequestInfoObj.setRequestUrl("getTicket");
				employeeTicketRequestInfoObj
						.setTicketId(Util.isNotEmptyObject(pojo.getTicketId()) ? pojo.getTicketId() : 0l);
				List<TicketResponse> ticketResponsesObj = getTicketResponse(employeeTicketRequestInfoObj);
				if (Util.isNotEmptyObject(ticketResponsesObj)) {
					ticketResponsesObj.get(0).setTicketEscalationId(pojo.getTicketEscalationId());
					ticketResponses.addAll(ticketResponsesObj);
				}
			}
			employeeTicketResponse.setTicketResponseList(ticketResponses);
			// addAdditionalInfo(employeeTicketResponse);
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"***** The Exception is raised in while getSystemEscalatedTicketDetails from No Data Found in ticketEscalationPojos ******");
			throw new InformationNotFoundException(errorMsgs);
		}
		return employeeTicketResponse;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result insertEmployeeLeaveDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws SQLInsertionException {
		LOGGER.info("**** The Control is inside the insertEmployeeLeaveDetails in EmployeeTicketServiceImpl ****");
		Result result = new Result();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		EmployeeLeaveDetailsPojo employeeLeaveDetailsPojo = mapper
				.employeeTicketRequestInfoToEmployeeLeaveDetailsPojo(employeeTicketRequestInfo);
		Long pk = employeeTicketDaoImpl.insertEmployeeLeaveDetails(employeeLeaveDetailsPojo);
		if (pk > 0) {
			LOGGER.debug("**** The PrimaryKey returned is ****" + pk);
			result.setResponseCode(pk.intValue());
			return result;
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation ******");
			throw new SQLInsertionException(errorMsgs);
		}
	}

	@Override
	public Result getEmployeeDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException {
		LOGGER.info("**** The Control is inside the getEmployeeDetails in EmployeeTicketServiceImpl ****");
		EmployeeTicketResponse response = new EmployeeTicketResponse();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		List<EmployeeDetailsPojo> employeeDetailsPojos = bookingFormServiceDaoImpl.getEmployeeDetails(Status.ACTIVE);
		if (Util.isNotEmptyObject(employeeDetailsPojos)) {
			response = mapper.employeeDetailsPojosToEmployeeDetails(employeeDetailsPojos);
			return response;
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("***** The Exception is raised in while getEmployeeDetails from No Data Found  ******");
			throw new InformationNotFoundException(errorMsgs);
		}
	}

	/*
	 * success means not in leave failure means in leave (non-Javadoc)
	 * 
	 * @see com.sumadhura.employeeservice.service.EmployeeTicketService#
	 * isEmployeeAvailable(com.sumadhura.employeeservice.service.dto.
	 * EmployeeTicketRequestInfo)
	 */
	@Override
	public Result isEmployeeAvailable(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException {
		LOGGER.info("**** The Control is inside the isEmployeeAvailable in EmployeeTicketServiceImpl ****");
		Result result = new Result();
		boolean flag = false;
		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getEmployeeId())) {
			LOGGER.debug("**** The Employeeid is ****" + employeeTicketRequestInfo.getEmployeeId());
			List<EmployeeLeaveDetailsPojo> employeeLeaveDetailsPojos = employeeTicketDaoImpl
					.getEmployeeLeaveDetails(employeeTicketRequestInfo, Status.ACTIVE);
			if (employeeLeaveDetailsPojos.isEmpty()) {
				flag = true;
			}
			if (flag) {
				result.setResponseCode(HttpStatus.success.getResponceCode());
				result.setDescription(HttpStatus.success.getDescription());
			} else {
				result.setResponseCode(HttpStatus.failure.getResponceCode());
				result.setDescription(HttpStatus.failure.getDescription());
			}
		} else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getDepartmentId())) {
			LOGGER.debug("**** The DepartmentId is ****" + employeeTicketRequestInfo.getDepartmentId());
			List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl
					.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);

			if (Util.isNotEmptyObject(employeeDetailsPojos)) {
				for (EmployeeDetailsPojo pojo : employeeDetailsPojos) {
					employeeTicketRequestInfo.setEmployeeId(pojo.getEmployeeId());
					List<EmployeeLeaveDetailsPojo> employeeLeaveDetailsPojos = employeeTicketDaoImpl
							.getEmployeeLeaveDetails(employeeTicketRequestInfo, Status.ACTIVE);
					if (employeeLeaveDetailsPojos.isEmpty()) {
						flag = true;
						break;
					}
				}
				if (flag) {
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription(HttpStatus.success.getDescription());
				} else {
					result.setResponseCode(HttpStatus.failure.getResponceCode());
					result.setDescription(HttpStatus.failure.getDescription());
				}
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add(
						"***** The Exception is raised in while isEmployeeAvailable from No Data Found in employeeDetailsPojos IN isEmployeeAvailable ******");
				throw new InformationNotFoundException(errorMsgs);
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add(
					"***** The Exception is raised in while isEmployeeAvailable from No Data Found in getDepartmentId IN  isEmployeeAvailable ******");
			throw new InformationNotFoundException(errorMsgs);
		}
		return result;
	}

	/**
	 *
	 */
	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result reOpenTicket(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) throws Exception {
		LOGGER.info("******* The control inside of the reOpenTicket  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();

		/* send automatic mesage to customer while Reopen ticket */
		/* updating Ticket Conversation while escalating the ticket */
		EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfoObj.setTicketId(
				Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) ? employeeTicketRequestInfo.getTicketId()
						: 0l);
		employeeTicketRequestInfoObj.setFromType(Department.SYSTEM.getId());
		employeeTicketRequestInfoObj.setFromId(0l);
		employeeTicketRequestInfoObj.setFromDeptId(0l);
		employeeTicketRequestInfoObj.setDepartmentId(0l);

		/* getting Ticket Owner name */
		Future<List<CustomerPropertyDetailsPojo>> future = employeeTicketServiceHelper
				.getTicketOwner(employeeTicketRequestInfo);
		while (true) {
			if (future.isDone()) {
				List<CustomerPropertyDetailsPojo> customerPropertyDetails = future.get();
				employeeTicketRequestInfoObj.setMessage("Dear "
						+ (Util.isNotEmptyObject(customerPropertyDetails)
								? Util.isNotEmptyObject(customerPropertyDetails.get(0))
										? customerPropertyDetails.get(0).getCustomerName()
										: ""
								: "")
						+ new ResponceCodesUtil().getApplicationProperties().getProperty("TICKET_REOPEN_CUSTOMER_MSG"));
				break;
			}
		}
		// employeeTicketRequestInfoObj.setMessage(new
		// ResponceCodesUtil().getApplicationProperties().getProperty("TICKET_REOPEN_CUSTOMER_MSG"));
		Result objResult = updateMessageToCustomer(employeeTicketRequestInfoObj);
		if (objResult.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
			LOGGER.info("**** The message is inserted successfully ***");
		}
		for (PushNotificationInfo pushNotificationInfo : createNotification(employeeTicketRequestInfo)) {
			final PushNotificationInfo info = pushNotificationInfo;
			Thread thread = new Thread() {
				public synchronized void run() {
					LOGGER.debug("**** The control is inside upload ticket documents into the server *****");
					try {
						if (Util.isNotEmptyObject(info.getOsType())
								&& info.getOsType().equalsIgnoreCase(Type.ANDRIOD.getName())) {
							/* send push notification to customer while reopening the ticket. */
							final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
							try {
							pushNotificationUtil.pushFCMNotification(info);
							}catch(Exception ex) {
								 LOGGER.error("**** The Error Message ****"+ex);
						    }
						} else if (Util.isNotEmptyObject(info.getOsType())
								&& info.getOsType().equalsIgnoreCase(Type.IOS.getName())) {
							/* send push notification to customer while reopening the ticket. */
							final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
							try {
							ioSPushNotificationUtil.sendIosPushNotification(Arrays.asList(info.getDeviceToken()),
									ioSPushNotificationUtil.getTicketingPushNotificationPayLoad(info).toString(), false);
							}catch(Exception ex) {
								 LOGGER.error("**** The Error Message ****"+ex);
						    }
						}
					} catch (Exception e) {
						LOGGER.error(
								"**** The Exception is raised while inserting record into the TicketStatics Table ****");
					}
				}
			};
			thread.setName("TicketPushNotificationThread");
			thread.start();
		}
		TicketPojo pojo = mapper.employeeTicketRequestInfoToTicketPojoForReOpen(employeeTicketRequestInfo);
		Long updatedRows = employeeTicketDaoImpl.updateTicketReopenStatus(pojo);
		if (updatedRows > 0) {
			/* while reopen ticket ticket escalation time will be improved to 24 hours */
			TicketPojo ticketPojo = mapper
					.employeeTicketRequestInfoToTicketPojoForTicketExtendedEscalation(employeeTicketRequestInfo);
			employeeTicketDaoImpl.updateTicketEstimatedResolvedDate(ticketPojo);
			/* if ticket is already in already escalated state we will remove from pm. */
			// @SuppressWarnings("unused")
			// Long ticketEscalationStatus =
			// employeeTicketDaoImpl.updateTicketEscalationStatus(mapper.employeeTicketInfoToTicketEscalationPojo(employeeTicketRequestInfo));
			/* Inserting TicketStatistics table while updating the ticket table */
			
			/*bvr */
			String crmFinanceType=new ResponceCodesUtil().getApplicationProperties().get("CRM_FINANCE_TICKETS").toString();
			String crmTechType=new ResponceCodesUtil().getApplicationProperties().get("CRM_TECH_TICKETS").toString();
			/* inserting int ticket_escaltion table for escalating reopen ticket to grievence cell*/
			final EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
			info.setTicketId(employeeTicketRequestInfo.getTicketId());
			List<TicketEscalationLevelMappingPojo> levelMappingPojos = employeeTicketDaoImpl.getTicketEscaltionAprovalLevelDetails(info,Status.ACTIVE);
			
			employeeTicketRequestInfo.setTicketId(Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) ? employeeTicketRequestInfo.getTicketId()
					: 0l);
			employeeTicketRequestInfo.setRequestUrl("getTicket");
			List<TicketPojo> ticketPojoList = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo,
					Status.ACTIVE);

			/* Getting Old TicketTypeDetail */
				employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");
				employeeTicketRequestInfo.setTicketTypeDetailsId(ticketPojoList.get(0).getTicketTypeDetailsId());
				List<TicketTypeDetailsPojo> TicketTypeDetailsPojoList = employeeTicketDaoImpl
						.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);
			if(TicketTypeDetailsPojoList.get(0).getDepartmentId().equals(Department.CRM_SALES.getId()))
			{
				for (TicketEscalationLevelMappingPojo mappingPojo : levelMappingPojos) {
					if (mappingPojo.getLevelId().equals(Long.valueOf(crmFinanceType))) {
						employeeTicketRequestInfo.setTicketEscalationLevelMappingId(mappingPojo.getId());
					}
				}
			}
			if(TicketTypeDetailsPojoList.get(0).getDepartmentId().equals(Department.CRM_TECH.getId()))
			{
				for (TicketEscalationLevelMappingPojo mappingPojo : levelMappingPojos) {
					if (mappingPojo.getLevelId().equals(Long.valueOf(crmTechType))) {
						employeeTicketRequestInfo.setTicketEscalationLevelMappingId(mappingPojo.getId());
					}
				}
			}
			TicketEscalationPojo ticketEscalationPojo = mapper.employeeTicketRequestInfoToticketEscalationPojoForSystemEscalation(employeeTicketRequestInfo);
			Long results = employeeTicketDaoImpl.insertTicketEscalationDetails(ticketEscalationPojo);
			LOGGER.debug("**** The TicketEscalation  Record Number is ****"+results);
			
			/* sending Admin mails to next level employees */
			employeeTicketRequestInfo.setTicketEscalationId(Util.isNotEmptyObject(results)?results:0l);
			employeeTicketRequestInfo.setMailOtpApproval(Util.isNotEmptyObject(ticketEscalationPojo.getMailOtpApproval())?ticketEscalationPojo.getMailOtpApproval():"0l");
			boolean mailStatus2 = false;//ticketEscalationScheduler.sendAdminTicketEscalationMail(employeeTicketRequestInfo);
			
			if(mailStatus2) {
				LOGGER.info("*** Admin mails sent successfully ***");
			}
			/*bvr*/
			
			insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
			/* sending Ticket reopen mail to employee */
			Future<Boolean> mailStatus = employeeTicketServiceHelper.sendTicketReOpenMail(employeeTicketRequestInfo);
			if (mailStatus.isDone()) {
				LOGGER.info("*** The Reopen mail sent successfully to the employee ***");
			}
		}
		result.setResponseCode(updatedRows.intValue());
		LOGGER.debug("***** The Number of effected Rows is *****" + updatedRows);
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result insertTicketStatistics(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo, Status status)
			throws SQLInsertionException {
		LOGGER.info("******* The control inside of the insertTicketStatistics  in  EmployeeTicketServiceImpl ********");
		LOGGER.debug("*** The current Thread is ***" + Thread.currentThread().getName());
		Result result = new Result();
		EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		employeeTicketRequestInfoObj.setRequestUrl("getTicket");
		employeeTicketRequestInfoObj.setTicketId(
				Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) ? employeeTicketRequestInfo.getTicketId()
						: 0l);
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfoObj, status);
		for (TicketPojo pojo : ticketPojos) {
			TicketStatisticsPojo ticketStatisticsPojo = mapper.ticketPojosToTicketStatisticsPojo(pojo);
			Long pk = employeeTicketDaoImpl.insertTicketStatistics(ticketStatisticsPojo);
			if (pk > 0) {
				result.setResponseCode(HttpStatus.success.getResponceCode());
				LOGGER.debug("**** The Record is inserted successfully into the TicketStatistics Table *****");
			} else {
				List<String> errorMsgs = new ArrayList<String>();
				errorMsgs.add("***** The Exception is raised in while inserting Ticket conversation ******");
				throw new SQLInsertionException(errorMsgs);
			}
		}
		return result;
	}

	@Override
	/*
	 * This method is used for inserting TicketStatistics Into the MultiThreaded
	 * Model
	 */
	public void insertTicketStatisticsInMultithreadedMode(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			final Status status) {
		LOGGER.info(
				"******* The control inside of the insertTicketStatisticsInMultithreadedMode  in  EmployeeTicketServiceImpl ********");
		final EmployeeTicketRequestInfo employeeTicketRequestInfoFinal = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfoFinal.setTicketId(
				Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId()) ? employeeTicketRequestInfo.getTicketId()
						: 0l);
		/*
		 * BeanUtils.copyProperties(employeeTicketRequestInfoFinal,
		 * employeeTicketRequestInfo);
		 */
		try {
			insertTicketStatistics(employeeTicketRequestInfoFinal, status);
		} catch (SQLInsertionException e) {
			LOGGER.error("**** The Exception is raised while inserting record into the TicketStatics Table ****");
			e.printStackTrace();
		}
		/*
		 * LOGGER.debug("*** The current Thread is ***"+Thread.currentThread().getName()
		 * ); Thread thread = new Thread() { public synchronized void run() { LOGGER.
		 * debug("**** The control is inside upload ticket documents into the server *****"
		 * ); try { insertTicketStatistics(employeeTicketRequestInfoFinal,status); }
		 * catch (SQLInsertionException e) { LOGGER.
		 * error("**** The Exception is raised while inserting record into the TicketStatics Table ****"
		 * ); } } }; thread.setName("TicketStatisticsThread"); thread.start();
		 */
	}

	@Override
	public Result updateMessageToCustomer(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfoObj)
			throws Exception {
		LOGGER.info(
				"******* The control inside of the updateMessageToCustomer  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();

		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		BeanUtils.copyProperties(employeeTicketRequestInfo, employeeTicketRequestInfoObj);

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getTicketId())
				&& employeeTicketRequestInfo.getFromDeptId() != null && employeeTicketRequestInfo.getFromId() != null
				&& Util.isNotEmptyObject(employeeTicketRequestInfo.getFromType())
				&& Util.isNotEmptyObject(employeeTicketRequestInfo.getMessage())) {
			employeeTicketRequestInfo.setDepartmentId(employeeTicketRequestInfo.getFromDeptId());
			employeeTicketRequestInfo.setToType(Department.CUSTOMER.getId());
			/* Customer doesnot have any department. */
			employeeTicketRequestInfo.setToDeptId(0l);

			EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
			info.setTicketId(employeeTicketRequestInfo.getTicketId());
			info.setRequestUrl("getTicket");
			List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(info, Status.ACTIVE);
			employeeTicketRequestInfo
					.setFlatBookingId(Util.isNotEmptyObject(ticketPojos)
							? Util.isNotEmptyObject(ticketPojos.get(0).getFlatBookingId())
									? ticketPojos.get(0).getFlatBookingId()
									: 0l
							: 0l);
			employeeTicketRequestInfo.setRequestUrl("closeTicket.spring");
			//for complaint msg
			employeeTicketRequestInfo.setComplaintStatus(ticketPojos.get(0).getComplaintStatus());
			//employeeTicketRequestInfo.
			List<FlatBookingPojo> flatBookingPojos = employeeTicketDaoImpl
					.getFlatbookingDetails(employeeTicketRequestInfo, Status.ACTIVE);
			employeeTicketRequestInfo
					.setToId(Util.isNotEmptyObject(flatBookingPojos)
							? Util.isNotEmptyObject(flatBookingPojos.get(0).getCustomerId())
									? flatBookingPojos.get(0).getCustomerId()
									: 0l
							: 0l);
			result = updateTicketConversation(employeeTicketRequestInfo);
			if (result.getResponseCode().equals(HttpStatus.success.getResponceCode())) {
				LOGGER.info("**** The conversation is updated successfully while closing the Ticket****");
			}
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private Result updateTicketStatus(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo,
			@NonNull TicketPojo pojo) {
		LOGGER.info("******* The control inside of the updateTicketOpenStatus  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		/* updating ticket status New & Replied to Open */
		/*
		 * if Ticket is updated second time check departmentid and assignedTo both are
		 * Notnull && Notzero's
		 */
		if (Util.isNotEmptyObject(pojo) && ((Util.isNotEmptyObject(pojo.getAssignmentTo()))
				|| (Util.isNotEmptyObject(pojo.getDepartmentId())))) {
			if ((Util.isNotEmptyObject(pojo.getDepartmentTicketStatusId())
					&& (((pojo.getDepartmentTicketStatusId()).equals(Status.NEW.getStatus()))
							|| (pojo.getDepartmentTicketStatusId()).equals(Status.REPLIED.getStatus())))) {
				employeeTicketRequestInfo.setFromType(Department.EMPLOYEE.getId());
				employeeTicketRequestInfo.setGeneric("lowlevel");
				TicketPojo ticketPojo = mapper.employeeTicketRequestInfoToTicketPojoForOpen(employeeTicketRequestInfo);
				Long updatedRows = employeeTicketDaoImpl.updateTicketStatus(ticketPojo);
				if (updatedRows > 0) {
					LOGGER.info("*** The Ticket Status is updated to open state at invidual level");
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription(HttpStatus.success.getDescription());
				}
			}
		} else {
			if (Util.isNotEmptyObject(pojo)
					&& (Util.isNotEmptyObject(pojo.getStatusId())
							&& pojo.getStatusId().equals(Status.ACTIVE.getStatus()))
					&& ((Util.isNotEmptyObject(pojo.getTicketStatusId())
							&& pojo.getTicketStatusId().equals(Status.NEW.getStatus())))) {
				employeeTicketRequestInfo.setFromType(Department.EMPLOYEE.getId());
				employeeTicketRequestInfo.setGeneric("highlevel");
				TicketPojo ticketPojo = mapper.employeeTicketRequestInfoToTicketPojoForOpen(employeeTicketRequestInfo);
				Long updatedRows = employeeTicketDaoImpl.updateTicketStatus(ticketPojo);
				if (updatedRows > 0) {
					LOGGER.info("*** The Ticket Status is updated to open state at high level");
					result.setResponseCode(HttpStatus.success.getResponceCode());
					result.setDescription(HttpStatus.success.getDescription());
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result updateTicketStatusInactive(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(
				"******* The control inside of the updateTicketStatusInactive  in  EmployeeTicketServiceImpl ********");
		Result result = new Result();
		@SuppressWarnings("unused")
		Long updatedRows = employeeTicketDaoImpl.updateTicketStatusInactive(employeeTicketRequestInfo);
		LOGGER.info("*** The Ticket Status is updated to InActive ");
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}

	private String getPendingDepartmentName(TicketPojo pojo) {
		LOGGER.info("**** The control is inside the getPendingDepartmentName in  EmployeeTicketServiceImpl ****");
		/**
		 * if departmentId is given then ticket is pending at the department otherwise
		 * pending at employee
		 */
		List<DepartmentPojo> departmentPojos = null;
		List<EmployeeDetailsPojo> EmployeeDetailsPojos = null;
		List<EmployeePojo> employeePojos = null;
		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();

		if (pojo.getDepartmentId() != null && !pojo.getDepartmentId().equals(0l)) {
			EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setDepartmentId(pojo.getDepartmentId());
			departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
		} else if (pojo.getAssignmentTo() != null && !pojo.getAssignmentTo().equals(0l)) {
			EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setTicketTypeDetailsId(
					Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
			infoObj.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(infoObj,
					Status.ACTIVE);
			infoObj.setDepartmentId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
									? ticketTypeDetailsPojos.get(0).getDepartmentId()
									: 0l
							: 0l);
			List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
			infoObj.setEmployeeId(pojo.getAssignmentTo() != null ? pojo.getAssignmentTo() : 0l);
			employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
			employeePojos.get(0)
					.setEmployeeName(employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName());
		} else {
			EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setTicketTypeDetailsId(
					Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
			infoObj.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(infoObj,
					Status.ACTIVE);
			infoObj.setDepartmentId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
									? ticketTypeDetailsPojos.get(0).getDepartmentId()
									: 0l
							: 0l);
			List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
			infoObj.setEmployeeDetailsId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())
									? ticketTypeDetailsPojos.get(0).getEmployeeDetailsId()
									: 0l
							: 0l);
			EmployeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(infoObj, Status.ACTIVE);
			employeeTicketRequestInfo
					.setEmployeeId(Util.isNotEmptyObject(EmployeeDetailsPojos)
							? Util.isNotEmptyObject(EmployeeDetailsPojos.get(0).getEmployeeId())
									? EmployeeDetailsPojos.get(0).getEmployeeId()
									: 0l
							: 0l);
			employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
			employeePojos.get(0)
					.setEmployeeName(employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName());
		}
		if (Util.isNotEmptyObject(departmentPojos)) {
			return Util.isNotEmptyObject(departmentPojos.get(0).getDepartmentName())
					? departmentPojos.get(0).getDepartmentName()
					: "N/A";
		} else if (Util.isNotEmptyObject(employeePojos)) {
			return Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())
					? employeePojos.get(0).getEmployeeName()
					: "N/A";
		} else {
			return "N/A";
		}
	}

	private String getTicketStatus(TicketPojo employeeTicket) {
		LOGGER.info("**** The control is inside the getTicketStatus in  EmployeeTicketServiceImpl ****");
		if (Util.isNotEmptyObject(employeeTicket.getDepartmentTicketStatusId()))
			return employeeTicket.getDepartmentTicketStatusId().equals(Status.REPLIED.getStatus())
					? Status.REPLIED.getDescription()
					: employeeTicket.getDepartmentTicketStatusId().equals(Status.NEW.getStatus())
							? Status.NEW.getDescription()
							: employeeTicket.getDepartmentTicketStatusId().equals(Status.OPEN.getStatus())
									? Status.OPEN.getDescription()
									: employeeTicket.getDepartmentTicketStatusId().equals(Status.CLOSED.getStatus())
											? Status.CLOSED.getDescription()
											: Status.INPROGRESS.getDescription();
		else
			return employeeTicket.getTicketStatusId().equals(Status.REOPEN.getStatus()) ? Status.REOPEN.getDescription()
					: employeeTicket.getTicketStatusId().equals(Status.NEW.getStatus()) ? Status.NEW.getDescription()
							: employeeTicket.getTicketStatusId().equals(Status.OPEN.getStatus())
									? Status.OPEN.getDescription()
									: employeeTicket.getTicketStatusId().equals(Status.CLOSED.getStatus())
											? Status.CLOSED.getDescription()
											: Status.INPROGRESS.getDescription();
	}
	
	private Object getPendingTicket(TicketPojo pojo) {
		LOGGER.info("**** The control is inside the getPendingTicket in  EmployeeTicketServiceImpl ****");
		/**
		 * if departmentId is given then ticket is pending at the department otherwise
		 * pending at employee
		 */
		List<DepartmentPojo> departmentPojos = null;
		//List<EmployeeDetailsPojo> EmployeeDetailsPojos = null;
		List<EmployeePojo> employeePojos = null;
		//EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();

		if (pojo.getDepartmentId() != null && !pojo.getDepartmentId().equals(0l)) {
			EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setDepartmentId(pojo.getDepartmentId());
			departmentPojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
		} else if (pojo.getAssignmentTo() != null && !pojo.getAssignmentTo().equals(0l)) {
			/*EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setTicketTypeDetailsId(
					Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
			infoObj.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(infoObj,
					Status.ACTIVE);
			infoObj.setDepartmentId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
									? ticketTypeDetailsPojos.get(0).getDepartmentId()
									: 0l
							: 0l);
			List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
			infoObj.setEmployeeId(pojo.getAssignmentTo() != null ? pojo.getAssignmentTo() : 0l);
			employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
			employeePojos.get(0)
					.setEmployeeName(employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName()); */
			EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setTicketTypeDetailsId(Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
			infoObj.setRequestUrl("getCustomerTicketDetails");
			/*List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
					.getTicketTypeDetails(infoObj, Status.ACTIVE);
			infoObj.setDepartmentId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
									? ticketTypeDetailsPojos.get(0).getDepartmentId()
									: 0l
							: 0l);
			List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
			infoObj.setEmployeeId(pojo.getAssignmentTo() != null ? pojo.getAssignmentTo() : 0l);
			employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
			employeePojos.get(0).setEmployeeName(
					employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName());*/
			infoObj.setEmployeeId(pojo.getAssignmentTo() != null ? pojo.getAssignmentTo() : 0l);
			employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
		} else {
			/*EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setTicketTypeDetailsId(
					Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
			infoObj.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(infoObj,
					Status.ACTIVE);
			infoObj.setDepartmentId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
									? ticketTypeDetailsPojos.get(0).getDepartmentId()
									: 0l
							: 0l);
			List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
			infoObj.setEmployeeDetailsId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())
									? ticketTypeDetailsPojos.get(0).getEmployeeDetailsId()
									: 0l
							: 0l);
			EmployeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(infoObj, Status.ACTIVE);
			employeeTicketRequestInfo
					.setEmployeeId(Util.isNotEmptyObject(EmployeeDetailsPojos)
							? Util.isNotEmptyObject(EmployeeDetailsPojos.get(0).getEmployeeId())
									? EmployeeDetailsPojos.get(0).getEmployeeId()
									: 0l
							: 0l);
			employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo, Status.ACTIVE);
			employeePojos.get(0)
					.setEmployeeName(employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName()); */
			EmployeeTicketRequestInfo infoObj = new EmployeeTicketRequestInfo();
			infoObj.setTicketTypeDetailsId(Util.isNotEmptyObject(pojo.getTicketTypeDetailsId()) ? pojo.getTicketTypeDetailsId() : 0l);
			/*infoObj.setRequestUrl("getCustomerTicketDetails");
			List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = employeeTicketDaoImpl
					.getTicketTypeDetails(infoObj, Status.ACTIVE);
			infoObj.setDepartmentId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getDepartmentId())
									? ticketTypeDetailsPojos.get(0).getDepartmentId()
									: 0l
							: 0l);
			List<DepartmentPojo> Pojos = employeeTicketDaoImpl.getDepartmentDetails(infoObj, Status.ACTIVE);
			infoObj.setEmployeeDetailsId(
					Util.isNotEmptyObject(ticketTypeDetailsPojos)
							? Util.isNotEmptyObject(ticketTypeDetailsPojos.get(0).getEmployeeDetailsId())
									? ticketTypeDetailsPojos.get(0).getEmployeeDetailsId()
									: 0l
							: 0l);
			EmployeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(infoObj, Status.ACTIVE);
			infoObj.setEmployeeId(
					Util.isNotEmptyObject(EmployeeDetailsPojos)
							? Util.isNotEmptyObject(EmployeeDetailsPojos.get(0).getEmployeeId())
									? EmployeeDetailsPojos.get(0).getEmployeeId()
									: 0l
							: 0l); 
			employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
			employeePojos.get(0).setEmployeeName(employeePojos.get(0).getEmployeeName() + "-" + Pojos.get(0).getDepartmentName()); */
			infoObj.setRequestUrl("getTicketOwnerName");
			employeePojos = employeeTicketDaoImpl.getEmployee(infoObj, Status.ACTIVE);
		}
		if (Util.isNotEmptyObject(departmentPojos)) {
			/*
			return Util.isNotEmptyObject(departmentPojos.get(0).getDepartmentName())
					? departmentPojos.get(0).getDepartmentName()
					: "N/A"; */
			return 	departmentPojos.get(0);	
		} else if (Util.isNotEmptyObject(employeePojos)) {
			/*
			return Util.isNotEmptyObject(employeePojos.get(0).getEmployeeName())
					? employeePojos.get(0).getEmployeeName()
					: "N/A"; */
			return 	employeePojos.get(0);
		} else {
			return "";
		}
	}

	@Override
	public EmployeeTicketResponse changeTicketOwnerDropDown(EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InformationNotFoundException {
		LOGGER.info("**** The control is inside the changeTicketOwnerDropDown in  EmployeeTicketServiceImpl ****");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<Long> ownerIds = new ArrayList<Long>();

		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		List<EmployeeDetailsPojo> employeeDetailsPojoList = employeeTicketDaoImpl
				.changeTicketOwnerDropDown(employeeTicketRequestInfo, Status.ACTIVE);

		/* To add next level head and central crm(ex: crmSalesHead,centralcrm) */

		/* to set the changeTicketOwnerDropDownList in EmployeeTicketResponse */
		List<EmployeeDetails> changeTicketOwnerDropDownList = mapper
				.employeePojoListToemployeeDetailsList(employeeDetailsPojoList);

		employeeTicketRequestInfo.setRequestUrl("changeTicketOwnerDropDown");
		List<TicketTypeDetailsPojo> ticketTypeDetailsPojoList = employeeTicketDaoImpl
				.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);

		for (TicketTypeDetailsPojo pojo : ticketTypeDetailsPojoList) {
			if (Util.isNotEmptyObject(pojo.getEmployeeDetailsId())) {
				ownerIds.add(pojo.getEmployeeDetailsId());
			}
		}

		/*
		 * removing owners from dropdown for(EmployeeDetails pojo :
		 * changeTicketOwnerDropDownList) {
		 * if(ownerIds.contains(pojo.getEmpDetailsId())) {
		 * changeTicketOwnerDropDownList.remove(pojo); } }
		 */

		/* to avoid concurrent modification exception. removing owners from dropdown. */
		ListIterator<EmployeeDetails> itr = changeTicketOwnerDropDownList.listIterator();
		while (itr.hasNext()) {
			EmployeeDetails pojo = itr.next();
			if (ownerIds.contains(pojo.getEmpDetailsId())) {
				itr.remove();
			}
		}

		employeeTicketResponse.setEmployeeDetailsList(changeTicketOwnerDropDownList);
		return employeeTicketResponse;

		/*
		 * employeeTicketRequestInfo.setRequestUrl("getEmployeeDetails");
		 * employeeTicketResponse = (EmployeeTicketResponse)
		 * getEmployeeDetails(employeeTicketRequestInfo);
		 * if(Util.isNotEmptyObject(employeeTicketResponse) &&
		 * Util.isNotEmptyObject(employeeTicketResponse.getEmployeeDetailsList())) {
		 * employeeTicketResponse.setEmployeeDetailsList(employeeTicketResponse.
		 * getEmployeeDetailsList()); return employeeTicketResponse; }
		 */
	}

	/* written by venkatesh Malladi */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Integer changeTicketOwner(EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws InSufficeientInputException {
		LOGGER.info("**** The control is inside the changeTicketOwner in  EmployeeTicketServiceImpl ****");
		Integer updatedRows = null;
		Boolean isAssignmentToEqual = false;

		/* Error Prone code */
		employeeTicketRequestInfo.setRequestUrl("changeTicketOwner");
		employeeTicketRequestInfo.setTicketId(employeeTicketRequestInfo.getTicketIds().get(0));
		List<TicketTypeDetailsPojo> ticketTypeDetailsPojo = employeeTicketDaoImpl
				.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);

		/* Setting the TicketTypeDetails Id */
		if (Util.isNotEmptyObject(ticketTypeDetailsPojo) && Util.isNotEmptyObject(ticketTypeDetailsPojo.get(0))) {
			for (Long ticketId : employeeTicketRequestInfo.getTicketIds()) {

				employeeTicketRequestInfo.setTicketId(Util.isNotEmptyObject(ticketId) ? ticketId : 0l);
				employeeTicketRequestInfo.setRequestUrl("getTicket");
				List<TicketPojo> ticketPojoList = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo,
						Status.ACTIVE);

				/* Getting Old TicketTypeDetail */
				if (Util.isNotEmptyObject(ticketPojoList) && Util.isNotEmptyObject(ticketPojoList.get(0))
						&& (Util.isNotEmptyObject(ticketPojoList.get(0).getAssignmentTo()))
						&& Util.isNotEmptyObject(ticketPojoList.get(0).getTicketTypeDetailsId())) {
					employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");
					employeeTicketRequestInfo.setTicketTypeDetailsId(ticketPojoList.get(0).getTicketTypeDetailsId());
					List<TicketTypeDetailsPojo> TicketTypeDetailsPojoList = employeeTicketDaoImpl
							.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);

					/* Getting OldTicket Owner */
					if (Util.isNotEmptyObject(TicketTypeDetailsPojoList)
							&& Util.isNotEmptyObject(TicketTypeDetailsPojoList.get(0))
							&& Util.isNotEmptyObject(TicketTypeDetailsPojoList.get(0).getEmployeeDetailsId())) {
						employeeTicketRequestInfo
								.setEmployeeDetailsId(TicketTypeDetailsPojoList.get(0).getEmployeeDetailsId());
						employeeTicketRequestInfo.setRequestUrl("getCustomerTicketDetails");
						List<EmployeeDetailsPojo> EmployeeDetailsPojos = employeeTicketDaoImpl
								.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);

						/*
						 * if AssignedEmployee is equal to oldTicketOwner we need to update Assigned
						 * Employee (New Ticket Owner) Also
						 */
						if (Util.isNotEmptyObject(EmployeeDetailsPojos)
								&& Util.isNotEmptyObject(EmployeeDetailsPojos.get(0))
								&& Util.isNotEmptyObject(EmployeeDetailsPojos.get(0).getEmployeeId()) && ticketPojoList
										.get(0).getAssignmentTo().equals(EmployeeDetailsPojos.get(0).getEmployeeId())) {
							isAssignmentToEqual = true;
						}
					}
				}
				/* Setting the TicketTypeDetails Id New Ticket Owner */
				employeeTicketRequestInfo.setTicketTypeDetailsId(ticketTypeDetailsPojo.get(0).getTicketTypeDetailsId());
				updatedRows = employeeTicketDaoImpl.changeTicketOwner(employeeTicketRequestInfo, Status.ACTIVE,
						isAssignmentToEqual);
				/* resetting the flag value */
				isAssignmentToEqual = false;
				if (updatedRows > 0) {
					/* Inserting TicketStatistics table while updating the ticket table */
					insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
				}
			}
		} else {
			List<String> errorMsgs = new ArrayList<String>();
			errorMsgs.add("Selected Employee has no access to be a Ticket Owner");
			throw new InSufficeientInputException(errorMsgs);
		}
		return updatedRows;
	}

	@Override
	public List<TicketEscalationResponse> getTicketEscaltionDtls(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control is inside the getTicketEscaltionDtls in  EmployeeTicketServiceImpl ***");
		EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		employeeTicketRequestInfo.setRequestUrl("getTicketEscaltionDtls.spring");
		List<TicketEscalationPojo> ticketEscalationPojoList = employeeTicketDaoImpl
				.getTicketEscalationDtls(employeeTicketRequestInfo, Status.ACTIVE);
		List<TicketEscalationResponse> ticketEscalationResponse = mapper
				.employeeTicketEscaltionPojoListToemployeeTicketEscalationResponseList(ticketEscalationPojoList);
		return ticketEscalationResponse;
	}
	
	
	public void sendTicketForwardNotification(EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws IllegalAccessException, InvocationTargetException, InformationNotFoundException {
		LOGGER.info("*** The control is inside the sendTicketForwardNotification in  EmployeeTicketServiceImpl ***");

		if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())
				&& employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("forwardTicketDetails.spring")) {

			if ((((Util.isNotEmptyObject(employeeTicketRequestInfo.getToId()))
					|| Util.isNotEmptyObject(employeeTicketRequestInfo.getToDeptId()))
					&& employeeTicketRequestInfo.getToType() != null)
					&& ((Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())
							|| Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId()))
							&& Util.isNotEmptyObject(employeeTicketRequestInfo.getFromType()))) {

				StringBuilder customerMsg = new StringBuilder("Dear Customer Your Ticket has been  Forwarded From ");
				/* If TicketConversation is Raised from Department */
				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromDeptId())) {
					EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
					employeeTicketRequestInfoObj.setDepartmentId(employeeTicketRequestInfo.getFromDeptId());
					List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl
							.getDepartmentDetails(employeeTicketRequestInfoObj, Status.ACTIVE);
					if (Util.isNotEmptyObject(departmentPojos)) {
						customerMsg.append(departmentPojos.get(0).getDepartmentName() + " to ");
					}
				}
				/* If TicketConversation is Raised from Employee */
				else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getFromId())) {
					employeeTicketRequestInfo.setEmployeeId(employeeTicketRequestInfo.getFromId());
					List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
							Status.ACTIVE);
					if (Util.isNotEmptyObject(employeePojos)) {
						//customerMsg.append(employeePojos.get(0).getEmployeeName() + " to ");
					}
				}

				if (Util.isNotEmptyObject(employeeTicketRequestInfo.getToDeptId())) {
					EmployeeTicketRequestInfo employeeTicketRequestInfoObj = new EmployeeTicketRequestInfo();
					employeeTicketRequestInfoObj.setDepartmentId(employeeTicketRequestInfo.getToDeptId());
					List<DepartmentPojo> departmentPojos = employeeTicketDaoImpl
							.getDepartmentDetails(employeeTicketRequestInfoObj, Status.ACTIVE);
					if (Util.isNotEmptyObject(departmentPojos)) {
						customerMsg.append(departmentPojos.get(0).getDepartmentName());
					}
				} 
				else if (Util.isNotEmptyObject(employeeTicketRequestInfo.getToId())) {
					employeeTicketRequestInfo.setEmployeeId(employeeTicketRequestInfo.getToId());
					List<EmployeePojo> employeePojos = employeeTicketDaoImpl.getEmployee(employeeTicketRequestInfo,
							Status.ACTIVE);
					if (Util.isNotEmptyObject(employeePojos)) {
						customerMsg.append(employeePojos.get(0).getEmployeeName().split("-")[1]);
					}
				}

				/* setting push notification image */
				employeeTicketRequestInfo.setMessage(Util.isNotEmptyObject(customerMsg) ? customerMsg.toString() : "");

				for (PushNotificationInfo pushNotificationInfo : createNotification(employeeTicketRequestInfo)) {
					final PushNotificationInfo info = pushNotificationInfo;
					Thread thread = new Thread() {
						public synchronized void run() {
							try {
								if (Util.isNotEmptyObject(info.getOsType())
										&& info.getOsType().equalsIgnoreCase(Type.ANDRIOD.getName())) {
									/* if request is coming from forward only we need to send push notification */
									final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
									try {
									pushNotificationUtil.pushFCMNotification(info);
									}catch(Exception ex) {
										 LOGGER.error("**** The Error Message ****"+ex);
								    }
								} else if (Util.isNotEmptyObject(info.getOsType())
										&& info.getOsType().equalsIgnoreCase(Type.IOS.getName())) {
									/* if request is coming from forward only we need to send push notification */
									final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
									try {
									ioSPushNotificationUtil.sendIosPushNotification(
											Arrays.asList(info.getDeviceToken()),ioSPushNotificationUtil.getTicketingPushNotificationPayLoad(info).toString(), false);
									}catch(Exception ex) {
										 LOGGER.error("**** The Error Message ****"+ex);
								    }
								}
							} catch (Exception e) {
								LOGGER.error(
										"**** The Exception is raised while inserting record into the TicketStatics Table ****");
							}
						}
					};
					thread.setName("Ticket PushNotification Thread");
					thread.start();
				}
			}
		}
	}

	@Override
	public Result getChangeTicketTypeMailDetails(@NonNull final EmployeeTicketRequestInfo employeeTicketRequestInfo)
			throws FileNotFoundException, IOException {
		LOGGER.info("*** The control is inside the getChangeTicketTypeMailDetails in  EmployeeTicketServiceImpl ***");
		Set<String> ccMails = new HashSet<>();
		EmployeeTicketResponse resp = new EmployeeTicketResponse();
		/* we can give change ticketype access only two times */
		List<ChangeTicketTypePojo> list = getChangeTicketTypeRequestDetails(employeeTicketRequestInfo);
		
		/*  if the request is coming is first time */
		if(Boolean.valueOf(responceCodesUtil.getApplicationNamePropeties("IS_CHANGE_TICKET_TYPE_MAX_REQ_LVL_AVIALABLE")) && (Util.isEmptyObject(list) || list.get(0).getNoOfTimeRequested() < Integer.valueOf(responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_MAX_REQ_LVL")))) {
			/* Getting 1st Level Emp_Id and Specific Role Id and Department Id i.e AGM - Management */
			employeeTicketRequestInfo.setDepartmentId(Department.MANAGEMENT.getId());
			/* Purnima in CUG and LIVE */
			employeeTicketRequestInfo.setRoleId(Roles.ASSISTENT_GENERAL_MANAGER.getId());
			/* Rahul in UAT */
			//employeeTicketRequestInfo.setRoleId(Roles.DIRECTORS.getId());
			employeeTicketRequestInfo.setDepartmentId(Department.MANAGEMENT.getId());
			List<TicketDetailsPojo> pojos = employeeTicketDaoImpl.getChangeTicketTypeDetails(employeeTicketRequestInfo,Status.ACTIVE);
			employeeTicketRequestInfo.setRequestUrl("getChangeTicketTypeDetails");
			List<TicketDetailsPojo> ticketDetailsPojos = employeeTicketDaoImpl
					.getChangeTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);
	
			for (TicketDetailsPojo pojo : pojos) {
				if (Util.isNotEmptyObject(pojo) && Util.isNotEmptyObject(pojo.getLevelId())) {
					/* send change ticket type mail to Level1 employee */
					if (pojo.getLevelId().equalsIgnoreCase("1") || pojo.getLevelId().equalsIgnoreCase("1L")) {
						/* setting employee toMail */
						employeeTicketRequestInfo.setMails(new String[] {
								Util.isNotEmptyObject(pojo.getEmployeeMail()) ? pojo.getEmployeeMail() : "N/A" });
						/* setting employeeName */
						employeeTicketRequestInfo.setEmployeeName(
								Util.isNotEmptyObject(pojo.getEmployeeName()) ? pojo.getEmployeeName() : "N/A");
					} else {
						ccMails.add(Util.isNotEmptyObject(pojo.getEmployeeMail()) ? pojo.getEmployeeMail() : "");
					}
				}
			}
	
			/* setting employee CC Mails */
			employeeTicketRequestInfo.setCcMails(ccMails.toArray(new String[0]));
	
			for (TicketDetailsPojo pojo : ticketDetailsPojos) {
				if (Util.isNotEmptyObject(pojo)) {
					/* setting FlatNo */
					employeeTicketRequestInfo.setFlatNo(Util.isNotEmptyObject(pojo.getFlatNo()) ? pojo.getFlatNo() : "N/A");
					/* setting Project name */
					employeeTicketRequestInfo
							.setSiteName(Util.isNotEmptyObject(pojo.getSiteName()) ? pojo.getSiteName() : "N/A");
					/* setting DepartmentName */
					employeeTicketRequestInfo.setDeptName(
							Util.isNotEmptyObject(pojo.getDepartmentName()) ? pojo.getDepartmentName() : "N/A");
					
					/* setting TicketTypeId  */
					employeeTicketRequestInfo.setTicketTypeId(Util.isNotEmptyObject(pojo.getTicketTypeId()) ? pojo.getTicketTypeId() : 0l);
					
					/* setting TicketType */
					employeeTicketRequestInfo
							.setTicketType(Util.isNotEmptyObject(pojo.getTitle()) ? pojo.getTitle() : "N/A");
					/* setting Ticket Message */
					employeeTicketRequestInfo
							.setMessage(Util.isNotEmptyObject(pojo.getDescription()) ? pojo.getDescription() : "N/A");
				}
			}
	
			// Request to Change Ticket Type - Ticket Number(75)|NAN-B106
			/* employeeMail subject */
			String mailSubject = new StringBuilder(
					responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_MAIL_SUBJECT")).append("-<")
							.append(employeeTicketRequestInfo.getTicketId()).append(">-").append("|")
							.append(employeeTicketRequestInfo.getSiteName()).append("-")
							.append("<")
							.append(employeeTicketRequestInfo.getFlatNo())
							.append(">")
							.toString();
			employeeTicketRequestInfo.setTicketSubject(mailSubject);
			employeeTicketRequestInfo.setTemplateContent(new StringBuilder(responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_MAIL_MSG_BODY")).toString()
					.replace("${dept}", ""+employeeTicketRequestInfo.getDeptName()+""));
			
			resp.setChangeTicketTypeResponce(
					mapper.employeeRequestInfoToChangeTicketTypeResponce(employeeTicketRequestInfo));
			
			resp.setResponseCode(HttpStatus.success.getResponceCode());
			resp.setDescription(HttpStatus.success.getDescription());
		}else {
			resp.setResponseCode(HttpStatus.ExaustedchangeTicketTypeApprovalLevel.getResponceCode());
			resp.setDescription(HttpStatus.ExaustedchangeTicketTypeApprovalLevel.getDescription());
		}
		return resp;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result sendChangeTicketTypeMail(@NonNull final ChangeTicketType changeTicketTypeRequest) {
	LOGGER.info("*** The control is inside the sendChangeTicketTypeMail in  EmployeeTicketServiceImpl ***");
	Result result = new Result();
	Long pk = 0l;
	/* setting createdDate and statusId  */
	changeTicketTypeRequest.setStatusId(Status.RAISED.getStatus());
	changeTicketTypeRequest.setCreatedDate(new Timestamp(new Date().getTime()));
	/*  first time insert   */
	/* we can give change ticketype access only two times */
	EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
	employeeTicketRequestInfo.setTicketId(changeTicketTypeRequest.getTicketId());
	List<ChangeTicketTypePojo> list = getChangeTicketTypeRequestDetails(employeeTicketRequestInfo);
	if(Util.isEmptyObject(list)){
		changeTicketTypeRequest.setNoOfTimesRequested(1l);
	/*  inserting record into the change ticket type request table */
	   pk = employeeTicketDaoImpl.insertChangeTicketTypeRequest(changeTicketTypeRequest);
	}else {
		changeTicketTypeRequest.setModifiedDate(new Timestamp(new Date().getTime()));
		changeTicketTypeRequest.setNoOfTimesRequested((Util.isNotEmptyObject(list)?list.get(0).getNoOfTimeRequested():0l)+1l);
	   /* updating record into the change ticket type request table */
	   pk = employeeTicketDaoImpl.updateChangeTicketTypeRequest(changeTicketTypeRequest);
	}
	if(pk>0) {
	/* changing change ticket type request status in ticket table */
	changeTicketTypeRequest.setChangeTicketTypeStatus(Status.RAISED.getStatus());	
	employeeTicketDaoImpl.updateTicketTypeRequestStatus(changeTicketTypeRequest,Status.ACTIVE);	
	/* inserting into Ticket statistics table */
	insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
	/* inserting into the insertChangeTicketTypeRequestStatistics  */
	 employeeTicketDaoImpl.insertChangeTicketTypeRequestStatistics(changeTicketTypeRequest);
	/* sending ticket type change mail to Admin */
	employeeTicketServiceHelper.sendChangeTicketTypeMailToAdmin(changeTicketTypeRequest);	
	result.setResponseCode(HttpStatus.CHANGE_TICKETTYPE_REMINDER.getResponceCode());
	result.setDescription(HttpStatus.CHANGE_TICKETTYPE_REMINDER.getDescription());
	}else {
	result.setResponseCode(HttpStatus.failure.getResponceCode());
	result.setDescription(HttpStatus.failure.getDescription());
	}
	return result;
	}
	
	public List<ChangeTicketTypePojo> getChangeTicketTypeRequestDetails(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(" **** The control is inside the getChangeTicketTypeRequestDetails service in EmployeeTicketServiceImpl *****");
		ChangeTicketType changeTicketType = new ChangeTicketType();
		changeTicketType.setTicketId(employeeTicketRequestInfo.getTicketId());
		List<ChangeTicketTypePojo> ticketTypePojos = employeeTicketDaoImpl.getChangeTicketTypeRequestDetails(changeTicketType);
		return ticketTypePojos;
	}
	
	@Override
	public Result changeTicketTypeRemindAgain(@NonNull ChangeTicketType ChangeTicketTypeRequest){
		LOGGER.info(" **** The control is inside the changeTicketTypeRemindAgain service in EmployeeTicketServiceImpl *****");
		EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		Result result = new Result();
		employeeTicketRequestInfo.setTicketId(ChangeTicketTypeRequest.getTicketId());
		employeeTicketRequestInfo.setRequestUrl("getChangeTicketTypeMailDetails");
		/* Purnima Role in CUG and LIVE */
		employeeTicketRequestInfo.setRoleId(Roles.ASSISTENT_GENERAL_MANAGER.getId());
		/* Rahul Role in UAT */
		//employeeTicketRequestInfo.setRoleId(Roles.DIRECTORS.getId());
		employeeTicketRequestInfo.setDepartmentId(Department.MANAGEMENT.getId());
		List<TicketDetailsPojo> pojos = employeeTicketDaoImpl.getChangeTicketTypeDetails(employeeTicketRequestInfo,Status.ACTIVE);
		LOGGER.info("**** The employee Level details ******"+pojos);
		Set<String> ccMails = new HashSet<>();
		for (TicketDetailsPojo pojo : pojos) {
			if (Util.isNotEmptyObject(pojo) && Util.isNotEmptyObject(pojo.getLevelId())) {
				/* send change ticket type mail to Level1 employee */
				if (pojo.getLevelId().equalsIgnoreCase("1") || pojo.getLevelId().equalsIgnoreCase("1L")) {
					/* setting employee toMail */
					employeeTicketRequestInfo.setMails(new String[] {
							Util.isNotEmptyObject(pojo.getEmployeeMail()) ? pojo.getEmployeeMail() : "N/A" });
					/* setting employeeName */
					employeeTicketRequestInfo.setEmployeeName(
							Util.isNotEmptyObject(pojo.getEmployeeName()) ? pojo.getEmployeeName() : "N/A");
				} else {
					ccMails.add(Util.isNotEmptyObject(pojo.getEmployeeMail()) ? pojo.getEmployeeMail() : "");
				}
			}
		}
		/* setting employee CC Mails */
		employeeTicketRequestInfo.setCcMails(ccMails.toArray(new String[0]));
		/* sending ticket type change Reminder mail to Admin */
		employeeTicketServiceHelper.sendChangeTicketTypeReminderMailToAdmin(employeeTicketRequestInfo);
		return result;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result changeTicketTypeAction(@NonNull final ChangeTicketType changeTicketTypeRequest){
		LOGGER.info(" **** The control is inside the changeTicketTypeAction service in EmployeeTicketServiceImpl *****");
		Result result = new Result();
		
		/* if change ticket type request is approved  */
		if(Util.isNotEmptyObject(changeTicketTypeRequest.getChangeTicketTypeAction()) && changeTicketTypeRequest.getChangeTicketTypeAction().equalsIgnoreCase(Status.APPROVED.getDescription())) {
		final EmployeeTicketRequestInfo info = new EmployeeTicketRequestInfo();
		info.setTicketId(changeTicketTypeRequest.getTicketId());
		info.setRequestUrl("changeTicketType.spring");
		
		/*  getting ticket past details  */
		EmployeeTicketRequestInfo requestInfo = new EmployeeTicketRequestInfo();
		requestInfo.setTicketId(changeTicketTypeRequest.getTicketId());
		requestInfo.setRequestUrl("getChangeTicketTypeDetails");
		final List<TicketDetailsPojo> pastTicketDetailsPojos = employeeTicketDaoImpl.getChangeTicketTypeDetails(requestInfo,Status.ACTIVE);
		
		@SuppressWarnings("unused")
		Long noOfRowsUpdated = employeeTicketDaoImpl.updateChangeTicketTypeRequestStatus(changeTicketTypeRequest,Status.APPROVED);
		/* inserting into the insertChangeTicketTypeRequestStatistics  */
		employeeTicketDaoImpl.insertChangeTicketTypeRequestStatistics(changeTicketTypeRequest);	
		/* updating Ticket Type in Ticket */
		//try {
		employeeTicketDaoImpl.updateChangeTicketType(changeTicketTypeRequest,Status.APPROVED);
		/*
		}catch(Exception ex) {
			LOGGER.error(ex);
			result.setResponseCode(HttpStatus.CHANGE_TICKETTYPE_UNAVIALABLE.getResponceCode());
			result.setDescription(HttpStatus.CHANGE_TICKETTYPE_UNAVIALABLE.getDescription());
			return result;
		} */
		final EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
		employeeTicketRequestInfo.setTicketId(changeTicketTypeRequest.getTicketId());
		/* inserting into Ticket statistics table */
		insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
		
		/* sending mails to old and new ticket owners and send push notification to customer adding automated message to customer */
		Thread thread = new Thread() {
			@SuppressWarnings("unused")
			public void run() {
				try {
					  String departmentName = null;
					  String siteName = null;
					  String flatNo = null;
					  String mailSubject = null;
					  String customerName = null;
					  Long custNo= null;
					  String namePrefix = null;
					  
					 for(TicketDetailsPojo pojo : pastTicketDetailsPojos){
						changeTicketTypeRequest.setTicketId(Util.isNotEmptyObject(pojo.getTicketId())?pojo.getTicketId():0l);
						changeTicketTypeRequest.setRaisedUnderCategory(Util.isNotEmptyObject(pojo.getTitle())?pojo.getTitle():"N/A");
						siteName = Util.isNotEmptyObject(pojo.getSiteName())?pojo.getSiteName():"N/A";
						flatNo = Util.isNotEmptyObject(pojo.getFlatNo())?pojo.getFlatNo():"N/A";
						customerName = Util.isNotEmptyObject(pojo.getCustomerName())?pojo.getCustomerName():"Customer";
						custNo =  Util.isNotEmptyObject(pojo.getCustomerId())?pojo.getCustomerId():0l;
						namePrefix =  Util.isNotEmptyObject(pojo.getNamePrefix())?pojo.getNamePrefix():"N/A";
						mailSubject = new StringBuilder(
								responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_OWNER_MAIL_SUBJECT")).append("-<")
										.append(changeTicketTypeRequest.getTicketId()).append(">-").append("|")
										.append(siteName).append("-")
										.append("<")
										.append(flatNo)
										.append(">")
										.toString();
						
						 /* Alert for Change Ticket Type - Ticket Number-<1021>-|Sumadhura Nandanam-<B705> */
						changeTicketTypeRequest.setSubject(mailSubject);
						/* getting category to be changed from front end */
						//changeTicketTypeRequest.setCategoryToBeChanged("N/A");
						changeTicketTypeRequest.setDescription(Util.isNotEmptyObject(pojo.getDescription())?pojo.getDescription():"N/A");
					 }
					/* sending mail to new owner  */ 
					List<TicketDetailsPojo> pojos  = employeeTicketDaoImpl.getChangeTicketTypeDetails(changeTicketTypeRequest);	
                    for(TicketDetailsPojo pojo : pojos) {
                    	departmentName = Util.isNotEmptyObject(pojo.getDepartmentName())?pojo.getDepartmentName():"N/A";
                    	changeTicketTypeRequest.setMessageBody(new StringBuilder(responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_OWNER_MAIL_MSG_BODY")).toString()
        				.replace("${dept}", "<strong>"+(Util.isNotEmptyObject(departmentName)?departmentName:" N/A ")+"</strong>"));
                    	changeTicketTypeRequest.setTo(Util.isNotEmptyObject(pojo.getEmployeeMail())?pojo.getEmployeeMail():"N/A");	
                    	changeTicketTypeRequest.setEmployeeName(Util.isNotEmptyObject(pojo.getEmployeeName()) ? pojo.getEmployeeName() : "N/A");
    					/* sending mails to new  ticket owners */
    					employeeTicketServiceHelper.sendChangeTicketTypeTicketOwnersMail(changeTicketTypeRequest);
                    }
                    
                    /* sending mail to old owner */ 
                    for(TicketDetailsPojo pojo : pastTicketDetailsPojos){
                    	changeTicketTypeRequest.setMessageBody(new StringBuilder(responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_OWNER_MAIL_MSG_BODY")).toString()
                				.replace("${dept}", "<strong>"+(Util.isNotEmptyObject(departmentName)?departmentName:" N/A ")+"</strong>"));
                    	changeTicketTypeRequest.setTo(Util.isNotEmptyObject(pojo.getEmployeeMail())?pojo.getEmployeeMail():"N/A");	
                    	changeTicketTypeRequest.setEmployeeName(Util.isNotEmptyObject(pojo.getEmployeeName()) ? pojo.getEmployeeName() : "N/A");
    					/* sending mails to new  ticket owners */
    					employeeTicketServiceHelper.sendChangeTicketTypeTicketOwnersMail(changeTicketTypeRequest);
                    }
						for (PushNotificationInfo pushNotificationInfo : createNotification(info)) {
							final PushNotificationInfo info = pushNotificationInfo;
									if (Util.isNotEmptyObject(info.getOsType())
												&& info.getOsType().equalsIgnoreCase(Type.ANDRIOD.getName())) {
											/* send push notification to customer while closing the ticket. */
											final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
											try {
											pushNotificationUtil.pushFCMNotification(info);
											}catch(Exception ex) {
											 LOGGER.error("**** The Error Message ****"+ex);
											}
										} else if (Util.isNotEmptyObject(info.getOsType())
												&& info.getOsType().equalsIgnoreCase(Type.IOS.getName())) {
											/* send push notification to customer while closing the ticket. */
											final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
											try {
											ioSPushNotificationUtil.sendIosPushNotification(Arrays.asList(info.getDeviceToken()),
													ioSPushNotificationUtil.getTicketingPushNotificationPayLoad(info).toString(), false);
											}catch(Exception ex) {
												 LOGGER.error("**** The Error Message ****"+ex);
											}
										}
								}
					
				    /*  adding system generated message to customer while ticket type has been changed */
						employeeTicketRequestInfo.setCustomerId(custNo);
						employeeTicketRequestInfo.setToId(custNo);
						employeeTicketRequestInfo.setMessage(
						new StringBuilder(responceCodesUtil.getApplicationNamePropeties("CHANGE_TICKET_TYPE_SYSTEM_GENERATED_MSG")).toString()
						.replace("${Customer}", ""+(Util.isNotEmptyObject(namePrefix)?namePrefix:"MR")+" "+(Util.isNotEmptyObject(customerName)?customerName:" Customer ")+""));
					   TicketCommentsPojo ticketCommentsPojo = mapper.employeeTicketRequestInfoToTicketCommentsPojoForChangeTicketType(employeeTicketRequestInfo);
					   Long pk = employeeTicketDaoImpl.insertTicketConversation(ticketCommentsPojo);
				} catch (IllegalAccessException | InvocationTargetException | InformationNotFoundException |IOException e) {
						e.printStackTrace();
				}
			}
		};
		thread.setName("change Ticket Type");
		thread.start();
		result.setResponseCode(HttpStatus.CHANGE_TICKETTYPE_APPROVED.getResponceCode());
		result.setDescription(HttpStatus.CHANGE_TICKETTYPE_APPROVED.getDescription());
		return result;
		
		/* if change ticket type request is rejected. */	
		}else {
			@SuppressWarnings("unused")
			Long noOfRowsUpdated = employeeTicketDaoImpl.updateChangeTicketTypeRequestStatus(changeTicketTypeRequest,Status.REJECTED);
			/* inserting into the insertChangeTicketTypeRequestStatistics  */
			employeeTicketDaoImpl.insertChangeTicketTypeRequestStatistics(changeTicketTypeRequest);
			/* if change ticket type request is rejected changeTickettypestatus in Ticket table to reject */
			changeTicketTypeRequest.setChangeTicketTypeStatus(Status.REJECTED.getStatus());
			employeeTicketDaoImpl.updateTicketTypeRequestStatus(changeTicketTypeRequest,Status.ACTIVE);
			EmployeeTicketRequestInfo employeeTicketRequestInfo = new EmployeeTicketRequestInfo();
			employeeTicketRequestInfo.setTicketId(changeTicketTypeRequest.getTicketId());
			/* inserting into Ticket statistics table */
			insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
			result.setResponseCode(HttpStatus.CHANGE_TICKETTYPE_REJECTED.getResponceCode());
			result.setDescription(HttpStatus.CHANGE_TICKETTYPE_REJECTED.getDescription());
			return result;
		}
	}

	@Override
	public List<TicketReportingPojo> getCustomerTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(" **** The control is inside the getCustomerTicketList in EmployeeTicketServiceImpl *****");
		return employeeTicketDaoImpl.getCustomerTicketList(employeeTicketRequestInfo);
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Result saveTicketsComplaint(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(" **** The control is inside the saveTicketsComplaint in EmployeeTicketServiceImpl *****");
		Result result = new Result();//getTicket
		EmployeeTicketRequestInfo employeeTicketRequestInfo1=new EmployeeTicketRequestInfo();
		employeeTicketRequestInfo1.setRequestUrl("getTicket");
		employeeTicketRequestInfo1.setTicketId(employeeTicketRequestInfo.getTicketIds().get(0));
		List<TicketPojo> ticketPojos = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo1, Status.ACTIVE);
		
		//TimeUtil.addHours( 72);
		
		 //ticket assigned date
		List<TicketEscalationPojo> ticketescpojo=employeeTicketDaoImpl.getEscalationpojo(employeeTicketRequestInfo1);
		
		
		/* updating Ticket Table about Complaints */
		Integer rowsUpdated = employeeTicketDaoImpl.updateTicketsComplaint(employeeTicketRequestInfo);
		
		
		// adding 3 days only for complaint
		if ("Complaint".equalsIgnoreCase(employeeTicketRequestInfo.getRequestUrl())) {

			if (Util.isNotEmptyObject(ticketescpojo) && Util.isNotEmptyObject(ticketescpojo.get(0).getAssignedDate())) {// ticket
																														// Escalation																									// table
				ticketescpojo.get(0).setTicketId(employeeTicketRequestInfo1.getTicketId());
				ticketescpojo.get(0).setEscalationDate(TimeUtil.addHours(ticketescpojo.get(0).getAssignedDate(), 72));
				Integer count = employeeTicketDaoImpl.updateTicketsESCComplaint(ticketescpojo.get(0));
				// employeeTicketRequestInfo1.setEscalationTime(TimeUtil.addHours(ticketescpojo.get(0).getAssignedDate(),
				// 72));

			} else {// ticket tabe we are changing
				employeeTicketRequestInfo1.setEscalationTime(TimeUtil.addHours(ticketPojos.get(0).getEstimatedResolvedDate(), 72));
				Integer count = employeeTicketDaoImpl.updateTicketsESCDATE(employeeTicketRequestInfo1);
			}

		}
		
		/* Inserting into TicketStatistics table*/
		if(Util.isNotEmptyObject(rowsUpdated)) {
			for(Long ticketId : employeeTicketRequestInfo.getTicketIds()) {
				employeeTicketRequestInfo.setTicketId(ticketId);
				insertTicketStatisticsInMultithreadedMode(employeeTicketRequestInfo, Status.ACTIVE);
			}
		}
		//sending complaint message to customers  
		if(Util.isNotEmptyObject(rowsUpdated)&& Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl())&&employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("Complaint") ) {
		
			//notification msg code complaint
		
			EmployeeTicketRequestInfo empTicketRequestInfoobj = new EmployeeTicketRequestInfo();
			empTicketRequestInfoobj.setRequestUrl("complaint");
			empTicketRequestInfoobj.setFromType(Department.SYSTEM.getId());
			empTicketRequestInfoobj.setFromId(employeeTicketRequestInfo.getEmployeeId());
			empTicketRequestInfoobj.setFromDeptId(employeeTicketRequestInfo.getDepartmentId());
			//empTicketRequestInfoobj.setDepartmentId(employeeTicketRequestInfo.getDepartmentId());
			empTicketRequestInfoobj.setVisibleType(Visibility.PUBLIC.getDescription());
			empTicketRequestInfoobj.setToType(Department.CUSTOMER.getId());
			empTicketRequestInfoobj.setToDeptId(0l);
			empTicketRequestInfoobj.setTicketId(employeeTicketRequestInfo.getTicketIds().get(0));
			empTicketRequestInfoobj.setTicketConversationDocumentId(0l);
			//Message: Dear Customer your query and the purpose of the ticket status is updated as complaint .
			empTicketRequestInfoobj.setMessage(" Dear Customer, Your query with Ticket No."+employeeTicketRequestInfo.getTicketIds().get(0)+" has been considered as a complaint and escalated to the concerns to resolve the issue. At the earliest, we will get back to you with the resolution.");
			Long record_NO = employeeTicketDaoImpl.insertTicketConversation(empTicketRequestInfoobj, Status.ACTIVE);
			for(Long ticketId : employeeTicketRequestInfo.getTicketIds()) {
			empTicketRequestInfoobj.setTicketId(ticketId);
			
			try {	
			for (PushNotificationInfo pushNotificationInfo : createNotification(empTicketRequestInfoobj)) {
				final PushNotificationInfo info = pushNotificationInfo;
				Thread thread = new Thread() {
					public synchronized void run() {
						LOGGER.debug("**** The control is inside upload ticket documents into the server *****");
						try {
							if (Util.isNotEmptyObject(info.getOsType())
									&& info.getOsType().equalsIgnoreCase(Type.ANDRIOD.getName())) {
								/* send push notification to customer while closing the ticket. */
								final PushNotificationUtil pushNotificationUtil = new PushNotificationUtil();
								try {
								pushNotificationUtil.pushFCMNotification(info);
								}catch(Exception ex) {
								 LOGGER.error("**** The Error Message ****"+ex);
								}
							} else if (Util.isNotEmptyObject(info.getOsType())
									&& info.getOsType().equalsIgnoreCase(Type.IOS.getName())) {
								/* send push notification to customer while closing the ticket. */
								final IOSPushNotificationUtil ioSPushNotificationUtil = new IOSPushNotificationUtil();
								try {
								ioSPushNotificationUtil.sendIosPushNotification(Arrays.asList(info.getDeviceToken()),
										ioSPushNotificationUtil.getTicketingPushNotificationPayLoad(info).toString(), false);
								}catch(Exception ex) {
									 LOGGER.error("**** The Error Message ****"+ex);
								}
							}
						} catch (Exception e) {
							LOGGER.error(
									"**** The Exception is raised while sending notification to customer ****");
						}
					}
				};
				thread.setName("Ticket PushNotification Thread");
				thread.start();
			}
			
			} catch (Exception e) {
				LOGGER.error(
						"**** The Exception is raised is raised while sending notification to customer ****");
			}
			}
		}
		if(Util.isNotEmptyObject(rowsUpdated)) {
			result.setResponseCode(HttpStatus.success.getResponceCode());
			result.setDescription(HttpStatus.success.getDescription());
		}else {
			result.setResponseCode(HttpStatus.failure.getResponceCode());
			result.setDescription(HttpStatus.failure.getDescription());
		}
		return result;
	}

	@Override
	public EmployeeTicketResponse getTicketComplaintList(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info(" **** The control is inside the getTicketComplaintList in EmployeeTicketServiceImpl *****");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		Page<TicketPojo> page = employeeTicketDaoImpl.getTotalTicketList(employeeTicketRequestInfo,
				Util.isNotEmptyObject(employeeTicketRequestInfo.getPageNo())
						? employeeTicketRequestInfo.getPageNo().intValue()
						: 1,
				Util.isNotEmptyObject(employeeTicketRequestInfo.getPageSize())
						? employeeTicketRequestInfo.getPageSize().intValue()
						: 1);
		employeeTicketRequestInfo.setPageCount(Integer.valueOf(page.getPagesAvailable()).longValue());
		employeeTicketRequestInfo.setRowCount(Util.isNotEmptyObject(page.getRowCount())?page.getRowCount():0l);
		List<TicketPojo> employeeTicketList = page.getPageItems();
		List<TicketResponse> ticketResponseList = new ArrayList<TicketResponse>();
		for (TicketPojo pojo : employeeTicketList) {
			TicketResponse ticketResponse = mapper.employeeTicketPojoToTicketResponse(pojo);
			ticketResponseList.add(ticketResponse);
		}
		/* setting pageCount */
		employeeTicketResponse.setPageCount(Util.isNotEmptyObject(employeeTicketRequestInfo.getPageCount())?employeeTicketRequestInfo.getPageCount():0l);
		
		/* setting rowCount */
		employeeTicketResponse.setRowCount(Util.isNotEmptyObject(employeeTicketRequestInfo.getRowCount())?employeeTicketRequestInfo.getRowCount():0l);
		
		/* setting Ticket Responce List */
		employeeTicketResponse.setTicketResponseList(ticketResponseList);

		return employeeTicketResponse;
	}

	@Override
	public EmployeeTicketResponse getTicketAdditionalDetails(EmployeeTicketRequestInfo employeeTicketRequestInfo) throws InvalidStatusException, InterruptedException, ExecutionException {
		LOGGER.info(" **** The control is inside the getTicketAdditionalDetails in EmployeeTicketServiceImpl *****");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();
		TicketResponse ticketResponse = new TicketResponse();
		if("getAttachments".equalsIgnoreCase(employeeTicketRequestInfo.getType())) {
			EmployeeTicketRequestInfo fileEmployeeTicketRequestInfo = new EmployeeTicketRequestInfo();
			fileEmployeeTicketRequestInfo.setTicketId(employeeTicketRequestInfo.getTicketId());
			Future<List<FileInfo>> fileInfos = employeeTicketServiceHelper.createDocumentUrls(fileEmployeeTicketRequestInfo);
			/* After Completion of Execution of Future Task */
			do {
				/* adding ticket FileInfos */
				if (fileInfos.isDone()) {
					ticketResponse.setFileInfos(fileInfos.get());
					break;
				}
			} while (true);

			ticketResponses.add(ticketResponse);
		}else if("getPastTicketHistory".equalsIgnoreCase(employeeTicketRequestInfo.getType())) {
			List<TicketPojo> customerRaisedTicketPojoList = employeeTicketDaoImpl.getTicketList(employeeTicketRequestInfo, Status.ACTIVE);
			EmployeeTicketResponse customerTicketResponse = addAdditionalInfo(mapper.employeeTicketListToemployeeTicketResponse(customerRaisedTicketPojoList), false);
			employeeTicketResponse.setTotalTickets(customerTicketResponse.getTotalTickets());
			employeeTicketResponse.setOpen(customerTicketResponse.getOpen());
			employeeTicketResponse.setInProgress(customerTicketResponse.getInProgress());
			employeeTicketResponse.setClosed(customerTicketResponse.getClosed());
		}
		employeeTicketResponse.setTicketResponseList(ticketResponses);
		return employeeTicketResponse;
	}
	
	@Override
	public EmployeeTicketResponse getTicketCountList(@NonNull EmployeeTicketRequestInfo employeeTicketRequestInfo) throws InvalidStatusException, InterruptedException, ExecutionException {
		LOGGER.info("******* The control inside of the getTicketCountList in EmployeeTicketServiceImpl ********");
		final EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<EmployeeDetailsPojo> employeeDetailsPojos = employeeTicketDaoImpl.getEmployeeDetails(employeeTicketRequestInfo, Status.ACTIVE);
		Set<Long> ticketTypeDetailsId = new HashSet<Long>();
		final EmployeeTicketMapper mapper = new EmployeeTicketMapper();
		/* setting employee Accessed SitList */
		employeeTicketResponse.setSiteIds(employeeTicketRequestInfo.getSiteIds());
		/* Getting ticket type details id by employee details id */
		if (Util.isNotEmptyObject(employeeDetailsPojos)) {
			employeeTicketRequestInfo.setEmployeeDetailsId(employeeDetailsPojos.size() == 1 ? employeeDetailsPojos.get(0).getEmpDetailsId() : 0l);
			/* if it is coming from crm employee only we will see the TicketType details */
			if (Util.isNotEmptyObject(employeeTicketRequestInfo.getRequestUrl()) 
				&& ((employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/AllTickets")
				|| (employeeTicketRequestInfo.getRequestUrl().equalsIgnoreCase("getTickets/MyTickets"))))) {
				List<TicketTypeDetailsPojo> ticketTypeDetailsPojos = new ArrayList<TicketTypeDetailsPojo>();
				ticketTypeDetailsPojos = employeeTicketDaoImpl.getTicketTypeDetails(employeeTicketRequestInfo, Status.ACTIVE);
				/* Adding all Ticket Type Details Ids */
				if(!ticketTypeDetailsPojos.isEmpty()) {
					for(TicketTypeDetailsPojo typeDetailsPojo : ticketTypeDetailsPojos) {
						if(Util.isNotEmptyObject(typeDetailsPojo.getTicketTypeDetailsId())) {
							/* TicketType Details Id is more when he deals Multiple Process Types */
							ticketTypeDetailsId.add(typeDetailsPojo.getTicketTypeDetailsId());
						}
					}
					/* setting ticket type details Id */
					employeeTicketRequestInfo.setTicketTypeDetailsIds(ticketTypeDetailsId);
				}
			}
			/* Getting Ticket List based on the ticket owners i.e ticket type details ids */
			if(Util.isNotEmptyObject(ticketTypeDetailsId)) {
				Future<List<TicketPojo>> future = employeeTicketServiceHelper.getTicketList(employeeTicketRequestInfo);
				while(true) {
					if(future.isDone()) {
						employeeTicketResponse.setTotalTicketResponseList(mapper.ticketPojosToTicketResponses(future.get()));
						break;
					}
				}
			}else {
				/* Employee doesn't have ticketTypeDetailsId and have multiple siteId's */
				if(Util.isNotEmptyObject(employeeTicketRequestInfo.getSiteIds())) {
					Future<List<TicketPojo>> future = employeeTicketServiceHelper.getTicketList(employeeTicketRequestInfo);
					while(true) {
						if(future.isDone()) {
							employeeTicketResponse.setTotalTicketResponseList(mapper.ticketPojosToTicketResponses(future.get()));
							break;
						}
					}
				}
			}
			/* Adding all Ticket Count Details */
			if (employeeTicketResponse.getTotalTicketResponseList() != null && !employeeTicketResponse.getTotalTicketResponseList().isEmpty()) {
				addAdditionalInfo(employeeTicketResponse, true);
			}
		}
		return employeeTicketResponse;
	}

	@Override
	public EmployeeTicketResponse getClosedTicketList(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control inside of the getClosedTicketList in EmployeeTicketServiceImpl ***");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketResponse> ticketResponses = new ArrayList<TicketResponse>();
		List<TicketPojo> ticketPojoList = employeeTicketDaoImpl.getClosedTicketList(employeeTicketRequestInfo);
		for (TicketPojo pojo : ticketPojoList) {
            TicketResponse ticketResponse = mapper.employeeTicketPojoToTicketResponse(pojo);
            ticketResponse.setTicketStatusId(null);
            ticketResponse.setDepartmentTicketStatusId(null);
            ticketResponse.setTicketOwnerName(null);
            ticketResponses.add(ticketResponse);
		}
		employeeTicketResponse.setTicketResponseList(ticketResponses);
		return employeeTicketResponse;
	}

	@Override
	public EmployeeTicketResponse getTicketPendingDeptDtls(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control inside of the getTicketPendingDeptDtls in EmployeeTicketServiceImpl ***");
		EmployeeTicketResponse employeeTicketResponse = new EmployeeTicketResponse();
		List<TicketPendingDeptDtlsPojo> ticketPendingDeptDtlsPojoList = employeeTicketDaoImpl.getTicketPendingDeptDtls(employeeTicketRequestInfo);
		employeeTicketResponse.setTicketPendingDeptDtlsPojoList(ticketPendingDeptDtlsPojoList);
		return employeeTicketResponse;
	}
	@Override
	public List<EmployeeDetailsMailPojo> getCrmEmployees(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control inside of the getCrmEmployees in EmployeeTicketServiceImpl ***");
		List<EmployeeDetailsMailPojo>  crmdetails =employeeTicketDaoImpl.getCrmEmployees( employeeTicketRequestInfo);
		return crmdetails;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public EmployeeTicketResponse createTicketTypeDetailsForCRM(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control inside of the createTicketTypeDetailsForCRM in EmployeeTicketServiceImpl ***");
		List<TicketTypesPojo> ticketTypesPojoList = new ArrayList<TicketTypesPojo>();
		EmployeeTicketResponse resp = validateObject(employeeTicketRequestInfo);
		if (resp.getResponseCode() != null) {
			return resp;
		}
		List<TicketTypeRequest> ticketTypeRequestList =employeeTicketRequestInfo.getTicketTypeRequestList();
	    for(TicketTypeRequest pojo :ticketTypeRequestList)
	    {
			List<Long> blocks = null;
			blocks = pojo.getBlockIds();
			Long deptId=null;
			if ("CRM Technical".equalsIgnoreCase(pojo.getTicketType())) {
				employeeTicketRequestInfo.setRequestUrl("crmtech");
				deptId=Department.CRM_TECH.getId();
			}
			if ("CRM Financial".equalsIgnoreCase(pojo.getTicketType())) {
				employeeTicketRequestInfo.setRequestUrl("crmfinance");
				deptId=Department.CRM.getId();
			}
			List<Long> ticketTypes = employeeTicketDaoImpl.getTicketTypes(employeeTicketRequestInfo);
			for (Long block : blocks) {
				EmployeeTicketRequestInfo bean = new EmployeeTicketRequestInfo();		
				bean.setSiteId(pojo.getSiteId());
				bean.setDepartmentId(deptId);
				bean.setBlockId(block);
				Long count = employeeTicketDaoImpl.isEmployeeConfigured(bean);
				if (count > 0) {
					resp.setResponseCode(HttpStatus.failure.getResponceCode());
					resp.setDescription(" Sorry.. "+pojo.getTicketType()+" ticket types mappings are configured earlier please remove that and try again.");
					return resp;
				}
				for (Long ticketType : ticketTypes) {
					TicketTypesPojo ticketTypesPojo = new TicketTypesPojo();
					ticketTypesPojo.setBlockId(block);
					ticketTypesPojo.setDeptId(deptId);
					ticketTypesPojo.setSiteId(pojo.getSiteId());
					ticketTypesPojo.setTicketTypeId(ticketType);
					ticketTypesPojo.setEmployeeDetailsId(pojo.getEmployeeDetailsId());
					ticketTypesPojoList.add(ticketTypesPojo);
				}

			}
		}
		LOGGER.info("*** The ticket Type Pojos List ***"+ticketTypesPojoList);
	    employeeTicketDaoImpl.saveTicketTypeDetails(ticketTypesPojoList);
	    resp.setResponseCode(HttpStatus.success.getResponceCode());
		resp.setDescription("Ticket types have been mapped successfully.");
		return resp;
	}
	
	private EmployeeTicketResponse validateObject(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		EmployeeTicketResponse resp = new EmployeeTicketResponse();
		resp.setResponseCode(null);
		resp.setDescription(null);
		List<TicketTypeRequest> ticketTypeRequestList =employeeTicketRequestInfo.getTicketTypeRequestList();
		for (TicketTypeRequest pojo : ticketTypeRequestList) {
			if (Util.isEmptyObject(pojo.getBlockIds())) {
				resp.setResponseCode(HttpStatus.failure.getResponceCode());
				resp.setDescription("Insufficeint Input is Given.Given Block is Empty.");
				break;
			}
			if (Util.isEmptyObject(pojo.getEmployeeDetailsId())) {
				resp.setResponseCode(HttpStatus.failure.getResponceCode());
				resp.setDescription("Insufficeint Input is Given.Given EmployeeId is Empty.");
				break;
			} 
			if (Util.isEmptyObject(pojo.getTicketType())) {
				resp.setResponseCode(HttpStatus.failure.getResponceCode());
				resp.setDescription("Insufficeint Input is Given.Given Ticket Type is Empty.");
				break;
			}
			if (Util.isEmptyObject(pojo.getSiteId())) {
				resp.setResponseCode(HttpStatus.failure.getResponceCode());
				resp.setDescription("Insufficeint Input is Given.Given Site is Empty.");
				break;
			} 
		}
		return resp;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public EmployeeTicketResponse createTicketEscalationsLevels(EmployeeTicketRequestInfo employeeTicketRequestInfo) throws InformationNotFoundException {
		LOGGER.info("*** The control inside of the createTicketEscalationsLevels in EmployeeTicketServiceImpl ***");
	   /*validating request object*/
		EmployeeTicketResponse resp = validateService(employeeTicketRequestInfo,"ticketEscLevels");
		if (resp.getResponseCode() != null) {
			return resp;
		}
		List<TicketEscLevelMapPojo> mapPojoList = new ArrayList<TicketEscLevelMapPojo>();
	    List<TicketEscaLevelEmpMap> mapEmpPojoList = new ArrayList<TicketEscaLevelEmpMap>();
		/*getting max from TICKET_ESCA_EXT_APPROVAL_LEVAL */
	    Long maxTicketEsclevelId=employeeTicketDaoImpl.getMaxTicketEscalevelMapId( employeeTicketRequestInfo);
	    List<TicketEscalationRequest> ticketEscalationRequestList = employeeTicketRequestInfo.getTicketEscalationRequest();
		for (TicketEscalationRequest pojo : ticketEscalationRequestList) {
			 /*checking TICKET_ESCA_EXT_APPROVAL_LEVAL created or not for particular ,if created using old records otherwise creating again*/
			Long count = employeeTicketDaoImpl.isTicketEscaExtApprovalLevelCreated(employeeTicketRequestInfo,pojo.getTicketType());
			if (count == 0) {
				employeeTicketDaoImpl.saveTicketEscaExtApprovalLevel(employeeTicketRequestInfo,pojo.getTicketType());
			}
			List<TicketEscalationLevelRequest> ticketEscalationLevelRequestList= pojo.getTicketEscalationLevelRequestList();
			/*this code for making request in order based on level id*/
			Collections.sort(ticketEscalationLevelRequestList, new Comparator<TicketEscalationLevelRequest>(){
				   public int compare(TicketEscalationLevelRequest o1, TicketEscalationLevelRequest o2){
				      return Integer.parseInt(o1.getLevelId().toString()) - Integer.parseInt(o2.getLevelId().toString());
				   }
				});
			/*geting TICKET_ESCA_EXT_APPROVAL_LEVAL ids*/
			List<Long> escExtLevelIds=employeeTicketDaoImpl.getTicketEscExtApprovalLevelIds(employeeTicketRequestInfo,pojo.getTicketType());
			
			for (Long escExtLevelId : escExtLevelIds) {
				for (TicketEscalationLevelRequest reqBean : ticketEscalationLevelRequestList) {
					int maxlevel =ticketEscalationLevelRequestList.size();
					/* TICKET_ESCA_LVL_MAP table object*/
					TicketEscLevelMapPojo mapPojo = new TicketEscLevelMapPojo();
					/* TICKET_ESC_LEVEL_EMP_MAP table object*/
					TicketEscaLevelEmpMap empMapPojo = new TicketEscaLevelEmpMap();
					mapPojo.setTicketEscLevelMapId(maxTicketEsclevelId);
					if (!reqBean.getLevelId().equals(Long.valueOf(maxlevel))) {
						mapPojo.setTicketEscaNextLevelMapId(maxTicketEsclevelId + 1);
					} else {
						mapPojo.setTicketEscaNextLevelMapId(null);
					}
					mapPojo.setTicketEscAppLevelId(escExtLevelId);
					mapPojo.setLevelId(reqBean.getLevelId());
					mapPojo.setTicketExtendedTime(24l);
					if (reqBean.getLevelId().equals(1l)) {
						mapPojo.setTicketHoldTime(34l);
					} else {
						mapPojo.setTicketHoldTime(24l);
					}
					
					empMapPojo.setTicektEscLevelMapId(maxTicketEsclevelId);
					empMapPojo.setEmpId(reqBean.getEmpId());
					empMapPojo.setTicketEscLevelEmpMapId(maxTicketEsclevelId);
					
					mapPojoList.add(mapPojo);
					mapEmpPojoList.add(empMapPojo);
					maxTicketEsclevelId++;
				}
			}
			}
		Collections.sort(mapPojoList, new Comparator<TicketEscLevelMapPojo>(){
			   public int compare(TicketEscLevelMapPojo o1, TicketEscLevelMapPojo o2){
			      return Integer.parseInt(o1.getTicketEscLevelMapId().toString()) - Integer.parseInt(o2.getTicketEscLevelMapId().toString());
			   }
			});
		Collections.reverse(mapPojoList);
		/*saving TICKET_ESCA_LVL_MAP table*/
		employeeTicketDaoImpl.saveTicketEscaLevelMap(mapPojoList);
		/*saving TICKET_ESC_LEVEL_EMP_MAP table*/
		employeeTicketDaoImpl.saveTicketEscaLevelEmpMap(mapEmpPojoList);
		resp.setResponseCode(HttpStatus.success.getResponceCode());
		resp.setDescription("Ticket escalation levels have been configured successfully.");
		return resp;
	}
	
	private EmployeeTicketResponse validateService(EmployeeTicketRequestInfo employeeTicketRequestInfo,String type) throws InformationNotFoundException {
		EmployeeTicketResponse resp = new EmployeeTicketResponse();
		resp.setResponseCode(null);
		resp.setDescription(null);
		List<Long> values= new ArrayList<Long>();
	    List<TicketEscalationRequest> ticketEscalationRequestList = employeeTicketRequestInfo.getTicketEscalationRequest();
		for (TicketEscalationRequest pojo : ticketEscalationRequestList) {
			Long count =null;
			/*here we are checking ticket mapping configured are not*/
			if(type.equals("ticketEscExtLevels"))
			{
				 count = employeeTicketDaoImpl.isEscaltionLevelCreatedForExt(employeeTicketRequestInfo,pojo.getTicketType());
			}else {
			      count = employeeTicketDaoImpl.isEscaltionLevelCreated(employeeTicketRequestInfo,pojo.getTicketType());
			}
			if (count > 0) {
				resp.setResponseCode(HttpStatus.failure.getResponceCode());
				resp.setDescription(" Sorry.. "+pojo.getTicketType()+" ticket types escalations are configured earlier please remove that and try again.");
				break;
			}
			values.clear();
			if (Util.isEmptyObject(pojo.getTicketEscalationLevelRequestList())) {
				resp.setResponseCode(HttpStatus.failure.getResponceCode());
				resp.setDescription("Insufficeint Input is Given.");
				break;
			}
			if (Util.isEmptyObject(pojo.getTicketType())) {
				resp.setResponseCode(HttpStatus.failure.getResponceCode());
				resp.setDescription("Insufficeint Input is Given.Given Ticket Type is Empty.");
				break;
			} 
			List<TicketEscalationLevelRequest> ticketEscalationLevelRequestList= pojo.getTicketEscalationLevelRequestList();
			Collections.sort(ticketEscalationLevelRequestList, new Comparator<TicketEscalationLevelRequest>(){
				   public int compare(TicketEscalationLevelRequest o1, TicketEscalationLevelRequest o2){
				      return Integer.parseInt(o1.getLevelId().toString()) - Integer.parseInt(o2.getLevelId().toString());
				   }
				});
			for (TicketEscalationLevelRequest reqBean : ticketEscalationLevelRequestList) {
				Long value;
				if (Util.isEmptyObject(reqBean)) {
					resp.setResponseCode(HttpStatus.failure.getResponceCode());
					resp.setDescription("Insufficeint Input is Given.");
					break;
				} 
				value=reqBean.getLevelId();
				if (values.contains(value)) {
					resp.setResponseCode(HttpStatus.failure.getResponceCode());
					resp.setDescription(" Sorry.. Ticket escalation level(s) are duplicated, level-"+value+" mentioned multiple times.");
					return resp;
				//	break;
					
				} 
				values.add(value);
			}
			Long[] array = (Long[]) values.toArray(new Long[values.size()]);
			int[] intValues = new int[array.length];
			int i = 0;
			for (Long val : array) {
				intValues[i] = val.intValue();
				i++;
			}
			/*if given levels missid in middel ,we are stopping*/
			 int missidNumber =findMissingNumbers(intValues, 1, values.size());
			 if (missidNumber!=100) {
					resp.setResponseCode(HttpStatus.failure.getResponceCode());
					resp.setDescription("Sorry.. Ticket escalation levels were not configured properly, in the configuration didn't mention level-"+missidNumber+".");
					break;
				} 
		}
		return resp;
	}
	

	@Override
	public List<LevelPojo> getEscalationLevel(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control inside of the getEscalationLevel in EmployeeTicketServiceImpl ***");
		List<LevelPojo> list = employeeTicketDaoImpl.getEscalationLevel(employeeTicketRequestInfo);
		return list;
	}
	
	@Override
	public List<EmployeeLevelDetailsPojo> getEscalationLevelEmployees(EmployeeTicketRequestInfo employeeTicketRequestInfo) {
		LOGGER.info("*** The control inside of the getEscalationLevel in EmployeeTicketServiceImpl ***");
		List<EmployeeLevelDetailsPojo> list = employeeTicketDaoImpl.getEscalationLevelEmployees(employeeTicketRequestInfo);
		return list;
	}
	
	private  int findMissingNumbers(int[] a, int first, int last) {
		// before the array: numbers between first and a[0]-1
		int missidNumber=100;
		for (int i = first; i < a[0]; i++) {
			System.out.println(i);
			missidNumber=i;
			return missidNumber;
		}
		// inside the array: at index i, a number is missing if it is between a[i-1]+1
		// and a[i]-1
		for (int i = 1; i < a.length; i++) {
			for (int j = 1 + a[i - 1]; j < a[i]; j++) {
				System.out.println(j);
				missidNumber=i+1;
				return missidNumber;
			}
		}
		// after the array: numbers between a[a.length-1] and last
		for (int i = 1 + a[a.length - 1]; i <= last; i++) {
			System.out.println(i);
			missidNumber=i;
			return missidNumber;
		}
		return missidNumber;
	}
	
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public EmployeeTicketResponse createTicketEscalationsEcxtentionLevels(EmployeeTicketRequestInfo employeeTicketRequestInfo) throws InformationNotFoundException {
		LOGGER.info("*** The control inside of the createTicketEscalationsEcxtentionLevels in EmployeeTicketServiceImpl ***");
          /* validate request Object*/        
		EmployeeTicketResponse resp = validateService(employeeTicketRequestInfo,"ticketEscExtLevels");
		if (resp.getResponseCode() != null) {
			return resp;
		}
		List<TicketEscLevelMapPojo> mapPojoList = new ArrayList<TicketEscLevelMapPojo>();
	    List<TicketEscaLevelEmpMap> mapEmpPojoList = new ArrayList<TicketEscaLevelEmpMap>();
	   
		/*getting max from TICKET_ESCA_EXT_APPROVAL_LEVAL */
	    Long maxTicketEsclevelId=employeeTicketDaoImpl.getMaxTicketEscaExtensionlevelMapId( employeeTicketRequestInfo);
	    List<TicketEscalationRequest> ticketEscalationRequestList = employeeTicketRequestInfo.getTicketEscalationRequest();
		for (TicketEscalationRequest pojo : ticketEscalationRequestList) {
			
			
			 /*checking TICKET_ESCA_EXT_APPROVAL_LEVAL created or not for particular ,if created using old records otherwise creating again*/
			Long count = employeeTicketDaoImpl.isTicketEscaExtApprovalLevelCreated(employeeTicketRequestInfo,pojo.getTicketType());
			if (count == 0) {
				employeeTicketDaoImpl.saveTicketEscaExtApprovalLevel(employeeTicketRequestInfo,pojo.getTicketType());
			}
			
			List<TicketEscalationLevelRequest> ticketEscalationLevelRequestList= pojo.getTicketEscalationLevelRequestList();
			/*this code for making request in order based on level id*/
			Collections.sort(ticketEscalationLevelRequestList, new Comparator<TicketEscalationLevelRequest>(){
				   public int compare(TicketEscalationLevelRequest o1, TicketEscalationLevelRequest o2){
				      return Integer.parseInt(o1.getLevelId().toString()) - Integer.parseInt(o2.getLevelId().toString());
				   }
				});
			
             /* getting TICKET_ESCA_EXT_APPROVAL_LEVAL ids here based on ticket */
			List<Long> escExtLevelIds=employeeTicketDaoImpl.getTicketEscExtApprovalLevelIds(employeeTicketRequestInfo,pojo.getTicketType());
			
			for (Long escExtLevelId : escExtLevelIds) {
				for (TicketEscalationLevelRequest reqBean : ticketEscalationLevelRequestList) {
					/*getting max id*/
					int maxlevel =ticketEscalationLevelRequestList.size();
					/* TICKET_ESCA_EXT_APR_LVL_MAP table object*/
					TicketEscLevelMapPojo mapPojo = new TicketEscLevelMapPojo();
					/* TICKET_ESC_EXT_APR_LVL_EMP_MAP table  object*/
					TicketEscaLevelEmpMap empMapPojo = new TicketEscaLevelEmpMap();
					mapPojo.setTicketEscExtLevelMapId(maxTicketEsclevelId);
					if (!reqBean.getLevelId().equals(Long.valueOf(maxlevel))) {
						mapPojo.setTicketEscaExcNextLevelMapId(maxTicketEsclevelId + 1);
					} else {
						mapPojo.setTicketEscaExcNextLevelMapId(null);
					}
					mapPojo.setTicketEscExcAppLevelId(escExtLevelId);
					mapPojo.setLevelId(reqBean.getLevelId());
					/*if it is level 1 we are giveing ticket escalation time 2 days and for other levels 2 days*/
					if (reqBean.getLevelId().equals(1l)) {
						mapPojo.setNoOfDays(2l);
					} else {
						mapPojo.setNoOfDays(1l);
					}
					
					empMapPojo.setTicektEscLevelMapId(maxTicketEsclevelId);
					empMapPojo.setEmpId(reqBean.getEmpId());
					empMapPojo.setTicketEscLevelEmpMapId(maxTicketEsclevelId);
					
					mapPojoList.add(mapPojo);
					mapEmpPojoList.add(empMapPojo);
					maxTicketEsclevelId++;
				}
			}
			}
		
		Collections.sort(mapPojoList, new Comparator<TicketEscLevelMapPojo>(){
			   public int compare(TicketEscLevelMapPojo o1, TicketEscLevelMapPojo o2){
			      return Integer.parseInt(o1.getTicketEscExtLevelMapId().toString()) - Integer.parseInt(o2.getTicketEscExtLevelMapId().toString());
			   }
			});
		Collections.reverse(mapPojoList);
		/* saving TICKET_ESCA_EXT_APR_LVL_MAP table  */
		employeeTicketDaoImpl.saveTicketEscaExtLevelMap(mapPojoList);
		/* saving TICKET_ESC_EXT_APR_LVL_EMP_MAP table  */
		employeeTicketDaoImpl.saveTicketEscaExtLevelEmpMap(mapEmpPojoList);
		resp.setResponseCode(HttpStatus.success.getResponceCode());
		resp.setDescription("Ticket escalation extension time request levels have been configured successfully.");
		return resp;
	}
}
