package com.sumadhura.employeeservice.persistence.dto;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
//@Embeddable
@Setter
@Getter
public class FinPenaltyPojo {
	@Column(name = "FIN_PENALTY_ID") private Long finPenaltyId;
	//@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "FIN_INTEREST_RATES_ID") private Long finInterestRatesId;
	@Column(name = "AMOUNT") private Double penaltyAmount;
	@Column(name = "TAX_AMOUNT") private Double penaltyTaxAmount;
	@Column(name = "TOTAL_AMOUNT") private Double penaltyTotalAmount;
	//@Column(name = "INTEREST_FROM") private String interestFrom;
	@Column(name = "INTEREST_PERCENTAGE") private Double interestPercentage;
	
	@Column(name = "START_DATE") private Timestamp startDate;
	@Column(name = "END_DATE") private Timestamp endDate;
	
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "ACTIVE_STATUS_ID") private Long activeStatusId;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinPenaltyPojo [finBookingFormAccountsId=").append(finBookingFormAccountsId)
				.append(", startDate=").append(startDate).append(", endDate=").append(endDate)
				.append(", finInterestRatesId=").append(finInterestRatesId).append(", penaltyAmount=")
				.append(penaltyAmount).append(", penaltyTaxAmount=").append(penaltyTaxAmount)
				.append(", penaltyTotalAmount=").append(penaltyTotalAmount).append(", interestPercentage=")
				.append(interestPercentage).append("]");
		return builder.toString();
	}
}
