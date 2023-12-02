package com.sumadhura.employeeservice.persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class TicketTypesPojo {
	
	private Long siteId;
	private Long ticketTypeId;
	private Long deptId;
	private Long employeeDetailsId;
	private Long blockId;

}
