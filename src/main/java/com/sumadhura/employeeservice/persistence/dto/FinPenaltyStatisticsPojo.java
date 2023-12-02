package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class FinPenaltyStatisticsPojo implements Cloneable{
	@Column(name = "FIN_PNT_STAT_ID") private Long finPenaltyStatisticsId;
	@Column(name = "FIN_PENALTY_ID") private Long finPenaltyId;
	@Column(name = "FIN_INTEREST_RATES_ID") private Long finInterestRatesId;
	@Column(name = "DAYS_DIFFERENCE") private Integer daysDifference;
	@Column(name = "INTEREST_CAL_ON_AMOUNT") private Double interestCalculationOnAmount;
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double interestPercentage;
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;//ms accounts id
	
	private Double mileStonePaidBasicAmount;
	private Timestamp mileStonePaidDate;
	
	@Column(name = "AMOUNT") private Double penaltyAmount;
	@Column(name = "TAX_AMOUNT") private Double penaltyTaxAmount;
	@Column(name = "STAT_TOTAL_AMOUNT") private Double statTotalAmount;
	
	@Column(name = "PAID_AMOUNT") private Double paidPenaltyAmount;
	@Column(name = "PAID_TAX_AMOUNT") private Double paidPenaltyTaxAmount;
	
	@Column(name = "START_DATE") private Timestamp startDate;
	@Column(name = "END_DATE") private Timestamp endDate;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "STATUS_ID") private Long statusId;
	
	private List<FinPenaltyTaxMappingPojo> taxMappingPojos;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinPenaltyStatisticsPojo [finInterestRatesId=").append(finInterestRatesId)
				.append(", daysDifference=").append(daysDifference).append(", interestCalculationOnAmount=")
				.append(interestCalculationOnAmount).append(", percentageId=").append(percentageId)
				.append(", interestPercentage=").append(interestPercentage).append(", penaltyAmount=")
				.append(penaltyAmount).append(", penaltyTaxAmount=").append(penaltyTaxAmount).append(", startDate=")
				.append(startDate).append(", endDate=").append(endDate).append("]");
		return builder.toString();
	}
	

	
	
}
