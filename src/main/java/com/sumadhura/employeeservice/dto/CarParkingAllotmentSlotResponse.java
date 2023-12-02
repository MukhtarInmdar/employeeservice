package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CarParkingAllotmentSlotResponse {
	
	private Long basementId;
	private String basementName;
	private Long siteId;
	private Long slotId;
	private String slotName;
	private Long statusId;
	private Long slotStatusId;
	private String slotStatusName;
	private Long allotmentId;
	private Long flatBookId;
	private String allotmentLetterPath;
	private Timestamp allotmentDate;
	private Long allotmentStatusId;
	private String allotmentStatusName;
	private Long createdBy;
	private Timestamp createdDate;
	private String blockName;
	private String flatNo;
	private String custName;
	private String siteName;
	private Long approverEmpId;
	private String approvalComments;
}
