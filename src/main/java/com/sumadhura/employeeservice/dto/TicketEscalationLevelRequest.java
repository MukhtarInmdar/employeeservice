package com.sumadhura.employeeservice.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class TicketEscalationLevelRequest {
	
	private Long siteId;
	private Long empId;
	private Long levelId;
	private String ticketType;

}
