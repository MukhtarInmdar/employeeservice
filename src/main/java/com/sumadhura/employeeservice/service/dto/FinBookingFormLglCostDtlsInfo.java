package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinBookingFormLglCostDtlsInfo {
	private Long finBokingFormLglCostDtlsId;
	private Long finBokFrmLegalCostId;
	private Double legalAmount;
	private String description;
	private Long percentageId;
	private Double percentageValue;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
}
