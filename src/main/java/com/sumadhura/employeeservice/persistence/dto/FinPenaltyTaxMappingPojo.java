package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinPenaltyTaxMappingPojo {
	@Column(name = "FIN_PEN_TAX_MAP_ID") private Long finPenTaxMapId;
	@Column(name = "AMOUNT") private Double amount;//only tax amount on interest amount
	@Column(name = "TAX_PAID_AMOUNT") private Double taxPaidPmount;//only total tax paid amount
	
	@Column(name = "TOTAL_INTEREST_AMOUNT") private Double totalInterestAmount;//sum of interest basic amount and tax amount
	@Column(name = "PAID_TOTAL_INTEREST_AMOUNT") private Double paidTotalInterestAmount;//sum of paid interest basic amount and tax amount
	
	@Column(name = "FIN_PENALTY_ID") private Long finPenaltyId;
	@Column(name = "FIN_TAX_MAPING_ID") private Long finTaxMapingId;
	@Column(name = "FIN_PNT_STAT_ID") private Long finPenaltyStatisticsId;
	
	@Column(name = "START_DATE") private Timestamp gstStartDate;
	@Column(name = "END_DATE") private Timestamp gstEndDate;
	
	@Column(name = "GST_CALC_ON_AMT") private Double gstCalculationOnAmount;//holds interest basic amount, on this amount gst will be calculated

	
	@Column(name = "DAYS_DIFF") private Integer daysDifference;
	
	@Column(name = "PERCENTAGE_ID") private Long gstPercentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double gstInterestPercentage;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
