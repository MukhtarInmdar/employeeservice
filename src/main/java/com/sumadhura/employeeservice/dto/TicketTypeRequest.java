package com.sumadhura.employeeservice.dto;



import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class TicketTypeRequest {
	
	private Long siteId;
	
	private List<Long> blockIds;

	private Long employeeId;
	
	private String ticketType;
	
	private Long employeeDetailsId;
}
