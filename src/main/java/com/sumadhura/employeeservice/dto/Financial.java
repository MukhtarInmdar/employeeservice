package com.sumadhura.employeeservice.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class Financial extends Result {

	private String TotoalAmount;
	private String nextMileStoneAmount;
	private String modificationAmount;
	private Date dueDate;
	private List<MileStone> mileStones;
	private String totalMilestonePaidAmount;
	private String totalMilestoneDueAmount;
	private String totalPenaltyAmount;
	
	private String sumOfTotalPendingPenaltyAmount;
	private String sumOfTotalPenalityPaidAmount;
	private String sumOfInterestWaiverAdjAmount;
	
	private String totalDaysDelayed;
	private Boolean showInterestDetailsButton;
	private String interestCalDateMsg;
	private List<Map<String,String>> financialAmtDetails;
}