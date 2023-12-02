package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CarParkingBasementSlotsResponse {
	
	private Long basementId;
	private String basementName;
	private Long siteId;
	private Long createdBy;
	private Timestamp createdDate;
	private Long slotId;
	private String slotName;
	private Long statusId;
	private Long slotStatusId;
	private String slotStatusName;
}
