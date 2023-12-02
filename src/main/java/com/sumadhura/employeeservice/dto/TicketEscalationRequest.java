package com.sumadhura.employeeservice.dto;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class TicketEscalationRequest {
	
	private String ticketType;
	private  List<TicketEscalationLevelRequest> ticketEscalationLevelRequestList;

}
