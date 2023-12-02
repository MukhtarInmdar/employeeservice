package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class TicketEscLevelMapPojo {

	
	@Column(name="TICKET_ESCA_LVL_MAP_ID")
	private Long ticketEscLevelMapId;
	
	@Column(name="LEVEL_ID")
	private Long levelId;
	

	@Column(name="TICKET_ESCA_NXT_LVL_MAP_ID")
	private Long ticketEscaNextLevelMapId;
	
	@Column(name="TICKET_ESC_APR_LVL_ID")
	private Long ticketEscAppLevelId;
	
	@Column(name="TICKET_HOLD_TIME")
	private Long ticketHoldTime;
	
	@Column(name="TICKET_EXTENDED_TIME")
	private Long ticketExtendedTime;
	
	
	@Column(name="TICKET_ESCA_EXT_LVL_MAP_ID")
	private Long ticketEscExtLevelMapId;
	

	@Column(name="TICKET_ESC_EXT_REQ_NXT_LVL_ID")
	private Long ticketEscaExcNextLevelMapId;
	
	@Column(name="TICKET_ESC_EXT_APR_LVL_ID")
	private Long ticketEscExcAppLevelId;
	
	@Column(name="NO_OF_DAYS")
	private Long noOfDays;
	
	
	
}
