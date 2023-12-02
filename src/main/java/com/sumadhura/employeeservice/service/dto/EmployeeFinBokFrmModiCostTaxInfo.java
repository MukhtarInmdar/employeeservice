package com.sumadhura.employeeservice.service.dto;

import lombok.Data;

@Data
public class EmployeeFinBokFrmModiCostTaxInfo {
	private Long modificationTaxMapId;
	private Long finBookingFormModiCostId;
	private Long finTaxMappingId;
	private Double amount;
	private Long percentageId;
	private Double percentageValue;
	private Long createdBy;
}
