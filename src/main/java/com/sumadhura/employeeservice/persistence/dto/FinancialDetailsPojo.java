/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * FinancialDetailsPojo class provides Financial specific properties.
 * 
 * @author Venkat_Koniki
 * @since 01.04.2020
 * @time 10:00AM
 */

@Entity
@Data
public class FinancialDetailsPojo {

	@Column(name="PROJECT_MILESTONE_ID")
	private Long projectMileStoneId;
	
	@Column(name="MILESTONE_NAME")
	private String milestoneName;
	
	@Column(name="MILESTONE_STATUS")
	private String mileStoneStatus;
	
	@Column(name="FIN_MILESTONE_CLASSIFIDES_ID")
	private Long finMilestoneClassifidesId;
	
	@Column(name="PERCENTAGE_ID")
	private Long percentageId;
	
	@Column(name="MILESTONE_DATE")
	private Timestamp milestoneDate;
	
	@Column(name="MILE_STONE_NO")
	private Long milestoneNo;
	
	@Column(name="STATUS_ID")
	private Long milestoneStatusId;
	
	@Column(name = "MS_DMD_NOTE_DATE") private Timestamp milestoneDemandNoteDate;
	@Column(name = "MS_STATUS_ID") private Long msStatusId;
	@Column(name="PERCENTAGE")
	private Double percentage;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="PAY_AMOUNT")
	private Double payAmount;
	
	@Column(name="PAID_AMOUNT")
	private Double paidAmount;
	
	@Column(name="INTEREST_WAIVER_PENDING_AMOUNT")
	private String interestWaiverPendingAmount;
	
	@Column(name="MILESTONE_AMOUNT_DUE")
	private Double milestoneAmountDue;
	
	@Column(name="TOTAL_PENALITY_AMOUNT")
	private Double totalPenalityAmount;
	
	@Column(name="PAYMENT_STATUS")
	private Long paymentStatusId;
	
	@Column(name = "DEMAND_NOTE_DATE") 
	private Timestamp demandNoteDate;
	
	@Column(name = "MASTER_DMD_NOTE_DATE") 
	private Timestamp masterDemandNoteDate;
	
	@Column(name = "MASTER_DMD_DUE_DATE") 
	private Timestamp masterDemandNoteDueDate;
	
	@Column(name = "LAST_RECEIPT_DATE") 
	private Timestamp milestoneLastReceiptDate;
	
	@Column(name="DUE_DATE")
	private Timestamp dueDate;
	
	@Column(name="PAYABLE_AMOUNT")
	private Double totalAmount;
	
	@Column(name="FIN_BOOKING_FORM_MILESTONES_ID")
	private Long finBookingFormMilestoneId;
	
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DOC_NAME") private String documentName;
	
	@Column(name="FB_STATUS_ID")
	private int statusId;
	
}
