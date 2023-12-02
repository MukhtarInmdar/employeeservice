/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketTypeDetailsPojo class provides TICKETTYPEDETAILS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 30.04.2019
 * @time 02:27PM
 */

@Entity
@Data
public class TicketTypeDetailsPojo {
	
	@Column(name="TICKET_TYPE_DETAILS_ID")
	private Long ticketTypeDetailsId;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="TICKET_TYPE_ID")
	private Long ticketTypeId;
	
	@Column(name="DEPARTMENT_ID")
	private Long departmentId;
	
	@Column(name="EMPLOYEE_DETAILS_ID")
	private Long employeeDetailsId;
	
	@Column(name="TICKET_ESC_REC_EMP_DET_ID")
	private Long escalatedTicketAssignedEmployeeId;
	
	@Column(name="SYS_ESC_TICK_ASG_EMP_DET_ID")
	private Long systemEscalatedAssignedEmployeeId;
	
	@Column(name="STATUS")
	private Long status;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	
}
