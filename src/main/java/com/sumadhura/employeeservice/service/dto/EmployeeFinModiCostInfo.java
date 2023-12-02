package com.sumadhura.employeeservice.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeFinModiCostInfo {
	private Long finBookingModiCostDetailsId;
	private String actionType;
	private Long siteId;
	private Long finBookingFormModiCostId;
	private Long bookingFormId;
	private String modificationChargeDesc;
	private String units;
	private Double quantity;
	private Double basicAmount;
	private Double rate;
	private Double taxAmount;
	private Double totalAmount;
	private Long createdBy;
	private Long statusId;
	private Long finsetOffAppLevelId;
	private Long modifiacationStatusId;
private Long siteAccountId;	
	public EmployeeFinModiCostInfo() {
	
	}

	public EmployeeFinModiCostInfo(String modificationChargeDesc, String units, Double quantity, Double amount,
			Double rate, Long createdBy) {
		super();
		this.modificationChargeDesc = modificationChargeDesc;
		this.units = units;
		this.quantity = quantity;
		this.basicAmount = amount;
		this.rate = rate;
		this.createdBy = createdBy;
	}
}
