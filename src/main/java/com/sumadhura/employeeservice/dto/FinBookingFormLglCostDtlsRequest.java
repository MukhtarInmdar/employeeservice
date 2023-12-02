package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinBookingFormLglCostDtlsRequest {
	private Long finBokingFormLglCostDtlsId;
	private Long finBokFrmLegalCostId;
	private Double legalAmount;
	private String description;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
}
