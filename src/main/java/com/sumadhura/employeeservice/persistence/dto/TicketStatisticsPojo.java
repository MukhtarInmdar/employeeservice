/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketStatisticsPojo class provides TICKET_STATISTICS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 16.05.2019
 * @time 08:43PM
 */
@Entity
@Data
public class TicketStatisticsPojo {

	@Column(name="TICKET_STATISTICS_ID")
	private Long ticketStatisticsId;
	@Column(name="TICKET_ID")
	private Long ticketId;
	@Column(name="DEPARTMENT_ID")
	private Long departmentId;
	@Column(name="ASSIGNMENT_TO")
	private Long assignmentTo;
	@Column(name="ASSIGNED_BY")
	private Long assignedBy;
	@Column(name="ASSIGNED_DATE")
	private Timestamp assignedDate;
	@Column(name="ESTIMATED_RESOLVED_DATE")
	private Timestamp estimatedResolvedDate;
	@Column(name="ESTIMATED_RESOLVED_DATE_STATUS")
	private Long estimatedResolvedDateStatus;
	@Column(name="RESOLVED_DATE")
	private Timestamp resolvedDate;
	@Column(name="STATUS_UPDATED_BY")
	private Long statusUpdatedBy;
	@Column(name="STATUS_UPDATED_TYPE")
	private Long statusUpdatedType;
	@Column(name="TICKET_STATUS")
	private Long ticketStatus;
	@Column(name="INVIDUAL_TICKET_STATUS")
	private Long invidualTicketStatus;
	@Column(name="STATUS")
	private Long status;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="TICKET_TYPE_ID")
	private Long ticketTypeId;
	@Column(name="TICKET_TYPE_DETAILS_ID")
	private Long ticketTypeDetailsId;
	@Column(name="RATING")
	private Long rating;
	@Column(name="FEEDBACK_DESC")
	private String feedbackDesc;
	@Column(name="TICKET_TYPE_CHANGE_REQUEST")
	private Long ticketTypeChangeRequest;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="COMPLAINT_STATUS")
	private Long complaintStatus;

	@Column(name="COMPLAINT_CREATED_BY")
	private Long complaintCreatedBy;

	@Column(name="COMPLAINT_CREATED_DATE")
	private Timestamp complaintCreatedDate;
	
	@Column(name="BOOKING_ID")
	private Long flatBookingId;
	
	@Column(name="OLD_BOOKING_ID")
	private Long oldBookingId;

}



