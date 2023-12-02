package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class FloorDetailsResponse {
	
	private Long floorDetId;
	private Long floorId;
	private String floorName;
	private Long blockDetId;
	private String blockName;
	private Long blockId;
	private List<FlatDetailsResponse> flatDetRespList;

}
