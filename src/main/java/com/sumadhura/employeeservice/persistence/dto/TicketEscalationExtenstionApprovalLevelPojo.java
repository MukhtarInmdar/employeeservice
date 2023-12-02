/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketEscalationExtenstionApprovalLevelPojo class provides TicketEscalationExtenstionApprovalLevel Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 23.07.2019
 * @time 03:13PM
 */

@Entity
@Data
public class TicketEscalationExtenstionApprovalLevelPojo {

	@Column(name="ID")
	private Long id;
	
	@Column(name="TICKET_TYPE_ID")
	private Long ticketTypeId;
	
	@Column(name="APPROVER_ID")
	private Long approverId;
	
	@Column(name="NO_OF_DAYS")
	private Long noOfDays;
	
	@Column(name="TYPE")
	private Long type;
	
	@Column(name="STATUS")
	private Long status;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	
}
