package com.sumadhura.employeeservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ReferencesCustomerInfo {

	private Long referencesCustomerId;
	private Long customerId;
	private String customerName;
	private String projectName;
	private String unitNo;
	private Long id;
	
}
