/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketEscalationLevelEmployeeMappingPojo class provides TICKET_ESC_LEVEL_EMP_MAP Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 24.01.2020
 * @time 11:06AM
 */

@Entity
@Data
public class TicketEscalationLevelEmployeeMappingPojo {

	@Column(name="ID")
	private Long  id;
	@Column(name="EMPLOYEE_ID")
	private Long employeeId;
	@Column(name="TICKET_ESC_LVL_MAP_ID")
	private Long ticketEscalationLevelMappingId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
}


