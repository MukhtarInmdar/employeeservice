package com.sumadhura.employeeservice.service.dto;

import lombok.Data;

@Data
public class CarParkingAllotmentPdfInfo {
	
	private String basementName;
	private String slotName;
	private String flatNo;
	private String custName;
	private String siteName;
	private String siteAddress;
	private Long siteId;
	private Long flatBookId;
	private Long slotId;
	private Long approverEmpId;
	private Long flatsSaleOwnersId;
	private String flatSaleOwner;
}
