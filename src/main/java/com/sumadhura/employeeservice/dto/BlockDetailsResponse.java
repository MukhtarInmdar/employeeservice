package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class BlockDetailsResponse {
	
	private Long blockDetId;
	private Long siteId;
	private Long blockId;
	private String blockName;
	private List<FloorDetailsResponse> floorDetRespList;
	
}
