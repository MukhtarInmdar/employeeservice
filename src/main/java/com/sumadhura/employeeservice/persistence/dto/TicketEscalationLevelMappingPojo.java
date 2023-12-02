/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketEscalationLevelMappingPojo class provides Employee Ticket Escalation specific properties.
 * 
 * @author Venkat_Koniki
 * @since 23.01.2020
 * @time 04:19PM
 */

@Entity
@Data
public class TicketEscalationLevelMappingPojo {
	@Column(name="ID")
	private Long id;
	@Column(name="LEVEL_ID")
	private Long levelId;
	@Column(name="TICKET_ESCA_NXT_LVL_MAP_ID")
	private Long ticketEscalationNextLevelMappingId;
	@Column(name="TICKET_ESC_APR_LVL_ID")
	private Long ticketEscalationApprovalLevelId;
	@Column(name="TICKET_HOLD_TIME")
	private Long ticketHoldTime;
	@Column(name="TICKET_EXTENDED_DAYS")
	private Long ticketExtendedTime;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
}
