package com.sumadhura.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class EmployeeFinModiChargesRequest {
	private Long finBookingModiCostDetailsId;
	private String actionType;
	private String modificationChargeDesc;
	private String units;
	private Double basicAmount;
	private Double rate;
	private Double quantity;
	
}