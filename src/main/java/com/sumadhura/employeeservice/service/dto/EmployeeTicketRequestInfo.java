/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.dto.TicketEscalationLevelRequest;
import com.sumadhura.employeeservice.dto.TicketEscalationRequest;
import com.sumadhura.employeeservice.dto.TicketTypeRequest;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;
import com.sumadhura.employeeservice.persistence.dto.TicketEscalationLevelMappingPojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * EmployeeTicketRequestInfo class provides Employee specific properties.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:50PM
 */
@ToString
@Getter
@Setter
public class EmployeeTicketRequestInfo {

	private Long employeeId;
	private Long siteId;
	private Long ticketId;
	private Long deviceId;
	private Long departmentId;
	private String deptName;
	private Long forwardedDepartmentId;
	private Long ticketTypeDetailsId;
	private String requestUrl;
	private Long serviceRequestId;
	private Long employeeDetailsId;
	private Long ticketTypeId;
	private Long flatBookingId;
	private Long statusId;
	private Long customerId;
	private String generic;
	private Long applicantId;
	private Long flatId;
	private Long customerAddressId;
	private String message;
	private Long fromId;
	private Long fromType;
	private Long fromDeptId;
	private Long toDeptId;
	private Long toId;
	private Long toType;
	private Long typeOf;
	private String visibleType;
	private String documentsLocation;
	private Long ticketConversationDocumentId;
	private Long ticketSeekInforequestId;
	private Long departmentTicketStatusId;
	private Long ticketStatusId;
	private String[] mails;
	private List<FileInfo> fileInfos;
	private Timestamp extendedEscalationTime;
	private Timestamp escalationTime;
	private Long ticketExtendedEscalationApprovalId;
	private Long ticketExtendedEscalationApprovalStatusId;
	private Long escalationById;
	private Long escalationTo;
	private String escalationBy;
	private String type;
	private Timestamp startDate;
	private Timestamp endDate;
	private List<Long> ticketStatusIds;
	private Timestamp rejoinDate;
	private Long ApprovedBy;
	private String employeeName;
	private Long ticketEscalationId;
	private List<Long> siteIds;
	private String siteName;
	private String flatNo;
	private Long ticketExtendedEscalationApprovalLevelId;
	private String description;
	private String customerName;
	private String ticketOwner;
	private Long ticketOwnerId;
	private Long pageNo;
	private Long pageSize;
	private Set<Long> ticketTypeDetailsIds;
	private Long pageCount;
    private Long empDetailsId;
    private Long escalatedTicketAssignedEmployeeId;
    private String escalatedTicketAssignedEmployeeMail;
	private List<Long> ticketIds;
    private String pendingEmployeeName;
	private String ticketType;
	private String ticketSubject;
	private String mailOtpApproval;
	private String merchantId;
	private Timestamp ticketCreatedDate;
	private Timestamp ticketExpectedCloserDate;
	private Long ticketEscalationExtenstionApprovalLevelMappingId;
	private Long ticketEscalationLevelMappingId;
	private Timestamp approvedEscalationDate;
	private Long actionEmployeeId;
	private Timestamp assignedDate;
	private Long extendedTime;
	private Long departmentRoleMappingId;
	private Long roleId;
	private String[] ccMails;
	private String templateContent;
	private String externalDriveFileLocation;
	private List<Long> ticketSiteIds;
	private Long rowCount;
	private Timestamp fromDate;
	private Timestamp toDate;
	private ServiceRequestEnum requestEnum;
	private Long pendingEmpOrDeptType;
	private Long pendingEmpOrDeptId;
	private String siteShortName;
	private  List<TicketTypeRequest> ticketTypeRequestList;
	private Long blockId;
	private  List<TicketEscalationRequest> ticketEscalationRequest;
	private Long complaintStatus;
}