package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;

public class TicketEscExtLevelMapPojo {
	
	
	@Column(name="TICKET_ESCA_EXT_LVL_MAP_ID")
	private Long ticketEscExtLevelMapId;
	
	@Column(name="LEVEL_ID")
	private Long levelId;
	

	@Column(name="TICKET_ESC_EXT_REQ_NXT_LVL_ID")
	private Long ticketEscaExcNextLevelMapId;
	
	@Column(name="TICKET_ESC_EXT_APR_LVL_ID")
	private Long ticketEscExcAppLevelId;
	
	@Column(name="NO_OF_DAYS")
	private Long noOfDays;
	

}
