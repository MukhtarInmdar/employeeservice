/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketExtendedEscalationApprovalPojo class provides Employee TICKET_ESCA_EXT_APPROVAL specific properties.
 * 
 * @author Venkat_Koniki
 * @since 04.06.2019
 * @time 03:06PM
 */
@Entity
@Data
public class TicketExtendedEscalationApprovalPojo {
 
	@Column(name="ID")
	private Long id;
	@Column(name="TICKET_ID")
	private Long ticketId;
	@Column(name="CURRENT_ESCALATION_DATE")
	private Timestamp currentEscalationDateTimestamp;
	@Column(name="EXTENDED_ESCALATION_DATE")
	private Timestamp extendedEscalationDate;
	@Column(name="REQUESTED_BY")
	private Long requestedBy;
	@Column(name="REQUESTED_TO")
	private Long requestedTo;
	@Column(name="APPROVED_STATUS")
	private Long approvedStatus;
	@Column(name="COMMENTS")
	private String comments;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="STATUS")
	private Long status;
	@Column(name="TICKET_ESCA_EXT_APR_LVL_MAP_ID")
	private Long ticketEscalationExtenstionApprovalLevelMappingId;
	@Column(name="APPROVED_ESCALATION_DATE")
	private Timestamp approvedEscalationDate;
	@Column(name="APPROVED_BY")
	private Long approvedBy;
	@Column(name="LEVEL_ID")
	private Long levelId;
	@Column(name="NO_OF_DAYS")
	private Long noOfDays;
	
	
}
