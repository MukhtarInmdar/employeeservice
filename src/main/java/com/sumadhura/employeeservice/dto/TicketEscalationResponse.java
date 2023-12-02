package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
 * TicketEscalationResponse bean class provides Employee Ticket escalation Response specific properties.
 * 
 * @author Venkat_Koniki
 * @since 30.04.2019
 * @time 11:50AM
 */
@Data
public class TicketEscalationResponse implements Serializable {

	private static final long serialVersionUID = 8841933404917952127L;
	private Long ticketEscalationId;
	private Long ticketId;
	private Timestamp escalationDate;
	private Long escalationById;
	private String escalationBy;
	private String comments;
	private Long status;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	private Long escalationTo;
	private String mailOtpApproval;	
	
}
