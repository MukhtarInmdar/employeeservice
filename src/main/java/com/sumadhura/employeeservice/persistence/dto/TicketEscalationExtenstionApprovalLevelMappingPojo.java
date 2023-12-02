/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketEscalationExtenstionApprovalLevelMappingPojo class provides TICKET_ESCA_EXT_APR_LVL_MAP Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 21.01.2020
 * @time 03:27PM
 */

@Entity
@Data
public class TicketEscalationExtenstionApprovalLevelMappingPojo {

	@Column(name="ID")
	private Long id;
	@Column(name="LEVEL_ID")
	private Long levelId;
	@Column(name="TICKET_ESC_EXT_REQ_NXT_LVL_ID")
	private Long ticketEscalationExtenstionRequestNextLevelId;
	@Column(name="TICKET_ESC_EXT_APR_LVL_ID")
	private Long ticketEscalationExtenstionApprovalLevelId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate; 
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate; 
	
}
