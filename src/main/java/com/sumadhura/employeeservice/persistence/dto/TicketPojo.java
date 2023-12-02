/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketPojo class provides Employee Ticket specific properties.
 * 
 * @author Venkat_Koniki
 * @since 29.04.2019
 * @time 11:42AM
 */
@Entity
@Data
public class TicketPojo  implements Comparable<TicketPojo> {
	
	@Column(name="TICKET_ID")
	private Long ticketId;
	
	@Column(name="TITLE")
	private String title;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="STATUS")
	private Long statusId;
	
	@Column(name="TICKET_TYPE_DETAILS_ID")
	private Long ticketTypeDetailsId;
	
	@Column(name="DEPARTMENT_ID")
	private Long departmentId;
	
	@Column(name="ASSIGNMENT_TO")
	private Long assignmentTo;
	
	@Column(name="ASSIGNED_BY")
	private Long assignedBy;
	
	@Column(name="ASSIGNED_DATE")
	private Timestamp assignedDate;
	
	@Column(name="BOOKING_ID")
	private Long flatBookingId;
	
	@Column(name="ESTIMATED_RESOLVED_DATE")
	private Timestamp estimatedResolvedDate;
	
	@Column(name="ESTIMATED_RESOLVED_DATE_STATUS")
	private Long estimatedResolvedDateStatus;
	
	@Column(name="EXT_ESTIMATED_RESOLVED_DATE")
	private Timestamp extendedEstimatedResolvedDate;
	
	@Column(name="RESOLVED_DATE")
	private Timestamp resolvedDate;
	
	
	@Column(name="STATUS_UPDATED_BY")
	private Long statusUpdateBy;
	
	@Column(name="STATUS_UPDATE_TYPE")
	private Long statusUpdateType;
	
	@Column(name="DOCUMENT_LOCATION")
	private String documentLocation;
	
	@Column(name="TICKET_TYPE_ID")
	private Long ticketTypeId;
	
	@Column(name="TICKET_STATUS")
	private Long ticketStatusId;
	
	@Column(name="DEPARTMENT_TICKET_STATUS")
	private Long departmentTicketStatusId;
	
//	@Column(name="RATING")
//	private int rating;
	
	@Column(name="RATING")
	private String rating;
	
	@Column(name="FEEDBACK_DESC")
	private String feedbackDesc;
	
	@Column(name="TICKET_TYPE_CHANGE_REQUEST")
	private Long ticketTypeChangeRequest;
	
	@Column(name="CUST_NAME")
	private String custName;
	
	@Column(name="CUST_ID")
	private Long custId;
	
	@Column(name="SITE_NAME")
	private String siteName;
	
	@Column(name="TICKET_TYPE")
	private String ticketType;
	
	@Column(name="TICKET_OWNER")
	private String ticketOwner;
	
	@Column(name="PENDING_DEPT_EMP")
	private String pendingDeptEmp;
	
	@Column(name="CLOSED_BY")
	private String closedBy;
	
	@Column(name="COMPLAINT_STATUS")
	private Long complaintStatus;
	
	@Column(name="COMPLAINT_CREATED_BY")
	private Long complaintCreatedBy;
	
	@Column(name="COMPLAINT_CREATED_DATE")
	private Timestamp complaintCreatedDate;
	
	@Column(name="COMPLAINT_CREATED_BY_EMP_NAME")
	private String complaintCreatedByEmpName;
	
	@Column(name="FLAT_NO")
	private String flatNo;
	
	@Column(name="OLD_BOOKING_ID")
	private Long oldBookingId;
	
	@Column(name="IS_TICKET_REOPEN_ENABLE")
    private Boolean isTicketReopenEnable;
	
	@Column(name="PENDING_EMP_ID")
	private Long pendingEmpId;
	
	@Column(name="PENDING_DEPT_ID")
	private Long pendingDeptId;
	
	@Column(name="REOPEN_END_DATE")
	private Timestamp reopenenDate;

	@Override
	public int compareTo(TicketPojo pojo) {
		return this.getTicketId().compareTo(pojo.getTicketId());
	}

	

}
