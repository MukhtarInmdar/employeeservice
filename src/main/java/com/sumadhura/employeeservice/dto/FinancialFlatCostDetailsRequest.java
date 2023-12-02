package com.sumadhura.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialFlatCostDetailsRequest {
	private String siteName;
	private String blockName;
	private String flatNo;
	private Double basicFlatCost;
	private Double amenitiesCost;
	private Double flatCostWithBasicAndAmenities;
	private Double gstAmount;
	private Double flatCostWithGST;
	private Double percentageValue;
	private String actionOfRecord;
}
