/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * EmployeeTicketRequest bean class provides EmployeeTicketRequest specific properties.
 * 
 * @author Venkat_Koniki
 * @since 26.04.2019
 * @time 05:50PM
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString
@Getter
@Setter
public class EmployeeTicketRequest extends Result implements Serializable{

	private static final long serialVersionUID = -7544225839361332443L;
	private Long employeeId;
	private Long siteId;
	private Long ticketId;
	private Long departmentId;
	private Long forwardedDepartmentId;
	private Long ticketTypeDetailsId;
	private String requestUrl;
	private Long serviceRequestId;
	private Long ticketSeekInforequestId;
	private Long ticketTypeId;
	private Long customerId;
	private Long flatId;
	private String message;
	private Long fromId;
	private Long fromType;
	private Long toId;
	private Long toType;
	private Long typeOf;
	private Long fromDeptId;
	private Long toDeptId;
	private Long departmentTicketStatusId;
	private Long ticketStatusId;
	private Long statusId;
	private String visibleType;
	private List<FileInfo> fileInfos;
	private Long ticketConversationDocumentId;
	private Long flatBookingId;
	private Timestamp extendedEscalationTime;
	private Long ticketExtendedEscalationApprovalId;
	private Long escalationById;
	private String escalationBy;
	private String type;
	private Timestamp startDate;
	private Timestamp endDate;
	private Timestamp rejoinDate;
	private Long approvedBy;
	private String employeeName;
	private Long ticketEscalationId;
	private List<Long> siteIds;
    private int rating;
	private String feedbackDesc;	
	private String deptName;
	private Long pageNo;
	private Long pageSize;
	private Long empDetailsId;
	private List<Long> ticketStatusIds;
	private List<Long> ticketIds;
	private Timestamp approvedEscalationDate;
	private Long departmentRoleMappingId;
	private Long roleId;
	private String externalDriveFileLocation;
	private List<Long> ticketSiteIds;
	private Timestamp fromDate;
	private Timestamp toDate;
	private ServiceRequestEnum requestEnum;
	private Long pendingEmpOrDeptType;
	private Long pendingEmpOrDeptId;
	private  List<TicketTypeRequest> ticketTypeRequestList;
	private  List<TicketEscalationRequest> ticketEscalationRequest;
}
