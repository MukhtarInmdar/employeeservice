package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class TicketEscaLevelEmpMap {
	
	@Column(name="TICKET_ESC_LEVEL_EMP_MAP_ID")
	private Long ticketEscLevelEmpMapId;
	
	@Column(name="EMPLOYEE_ID")
	private Long empId;
	
	
	@Column(name="TICKET_ESC_LVL_MAP_ID")
	private Long ticektEscLevelMapId;

}
