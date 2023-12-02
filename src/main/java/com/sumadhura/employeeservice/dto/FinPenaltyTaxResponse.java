package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinPenaltyTaxResponse {
	private Long finTaxMappingId;
	private Timestamp startDate;
	private Timestamp endDate;
	private Long percentageId;
	private Double percentageValue;
	private Long finTaxId;
	private Long taxType;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;

}
