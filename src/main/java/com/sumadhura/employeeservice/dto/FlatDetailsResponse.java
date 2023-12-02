package com.sumadhura.employeeservice.dto;

import lombok.Data;

@Data
public class FlatDetailsResponse {
	
	private Long flatId;
	private String flatNo;
	private Long floorDetId;
	private Long floorId;
	private String floorName;
	private Long blockDetId;
	private Long blockId;
	private String blockName;

}
