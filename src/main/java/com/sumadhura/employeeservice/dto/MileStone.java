package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MileStone {
	private Long projectMilestoneId;
	private Long milStoneNo;
	private String mileStoneName;
	private String mileStoneAmount;
	private Timestamp mileStoneDate;
	private Timestamp demandNoteDate;
	private String mileStonePaidAmount;
	private String milestoneAmountDue;
	private Timestamp milestoneLastReceiptDate;
	private Integer daysDiffInDN_Date_AndLastReceipt;
	private String totalPenalityAmount;
	private String totalPendingPenaltyAmount;
	private String totalPenalityPaidAmount;
	private String interestWaiverAdjAmount;
	private String interestWaiverPendingAmount;//interest waiver amount to approve
	private Date dueDate;
	private Long msStatusId;
	private Double milStonePercentage;
	private String mileStoneStatus;
	private Long paymentStatusId;
	private Long percentageId;
	private Double paymentPercentageInMileStone;
	private Long paymentScheduleId;
	private Long payStgMapId;
	private Long finMilestoneClassifidesId;
	private String documentLocation;
	private String documentName;
	private String daysDelayed;
	private Long siteId;
	private String siteName;
	private int statusId;
}