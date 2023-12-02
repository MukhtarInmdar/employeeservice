package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketEscalationPojo class provides Employee Ticket Escalation specific properties.
 * 
 * @author Venkat_Koniki
 * @since 29.04.2019
 * @time 07:36AM
 */

@Entity
@Data
public class TicketEscalationPojo {

	@Column(name="TICKET_ESCALATION_ID")
	private Long ticketEscalationId;
	@Column(name="TICKET_ID")
	private Long ticketId;
	@Column(name="ESCALATION_DATE")
	private Timestamp escalationDate;
	@Column(name="ESCALATION_BY_ID")
	private Long escalationById;
	@Column(name="ESCALATION_BY")
	private String escalationBy;
	@Column(name="ESCALATION_TO")
	private Long escalationTo;
	@Column(name="COMMENTS")
	private String comments;
	@Column(name="STATUS")
	private Long status;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="MAIL_OTP_APPROVAL")
	private String mailOtpApproval;
	@Column(name="ACTION_EMP_ID")
	private Long actionEmployeeId;
	@Column(name="TICKET_ESC_LVL_MAP_ID")
	private Long ticketEscalationLevelMappingId;
	@Column(name="ASSIGNED_DATE")
	private Timestamp  assignedDate;
	@Column(name="TICKET_HOLD_TIME")
	private Long ticketHoldTime;
	
	
}

