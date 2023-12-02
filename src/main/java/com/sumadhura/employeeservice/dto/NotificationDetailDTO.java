package com.sumadhura.employeeservice.dto;

import lombok.Data;

@Data
public class NotificationDetailDTO {

	private Long siteId;
	private Long blockId;
	private Long floorId;
	private Long flatId;
	private String flatNo;
	
}
