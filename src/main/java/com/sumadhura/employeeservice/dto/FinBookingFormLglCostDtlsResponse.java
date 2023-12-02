package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinBookingFormLglCostDtlsResponse {
	private Long finBokingFormLglCostDtlsId;
	private Long finBokFrmLegalCostId;
	private String legalAmount;
	private String description;
	private Long percentageId;
	private String percentageValue;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
}
