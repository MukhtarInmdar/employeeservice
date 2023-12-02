package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialProjectMileStoneRequest {
	private Long projectMilestoneId;
	private Long finBookingFormMilestonesId;
	private String milestoneName;
	private Long finMilestoneClassifidesId;
	private Long percentagesId;
	private Double mileStonePercentage;
	private Timestamp milestoneDate;
	private Timestamp mileStoneDueDate;
	private Double mileStoneDueAmount;
	private Double interestPercentage;
	private String isInterestOrWithOutInterest; 
	private Long mileStoneNo;
	private Timestamp demandNoteDate;
	private Timestamp milestoneDemandNoteDate;//not in use
	private Timestamp interestCalculationUptoDate;
	private Long statusId;
	private Double setOffAmount;
	private Long projectMsStatusId;
	private Long submitedById;
	private String submitedByName; 
	private String statusName;
	private Long tdsStatusId;
	private String tdsStatusName;
	private Long finBookingFormTdsDtlsId;
	
	private String createdBy;
	private Timestamp createdDate;
	private String modifiedBy;
 	private Timestamp modifiedDate;
}
