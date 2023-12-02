package com.sumadhura.employeeservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteDetailsInfo {
	
	private Long flatId;
	
	private String flatNo;
	
	private Long floorId;
	
	private String floorName;
	
	private Long blockId;
	
	private String blockName;
	
	private Long siteId;
	
	private String siteName;
	
	private Long stateId;
	
	private Long flatBookingId;
	
	private String customerName;
	
	private String customerId;

}
