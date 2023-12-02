package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FinPenalityInfo {
	private Long finPenaltyId;
	private Long finBookingFormAccountsId;
	private Long bookingFormId;
	private Long finInterestRatesId;
	private Double penaltyAmount;
	private Double penaltyTaxAmount;
	private Double penaltyTotalAmount;
	private Long type;
	private Timestamp startDate;
	private Timestamp endDate;	
	private Integer daysDifference;
	private Double interestCalculationOnAmount;
	private Double mileStonePaidBasicAmount;//used in interest calculations
	private Timestamp mileStonePaidDate;
	private String condition;
	private String interestFrom;
	private Double interestPercentage;
	private Long createdBy;
	private Long modifiedBy;
	private Long statusId;
	private Timestamp createdDate;

}
