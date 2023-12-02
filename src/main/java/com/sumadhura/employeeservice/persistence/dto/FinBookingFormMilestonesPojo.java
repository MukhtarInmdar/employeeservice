package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormMilestonesPojo {
	@Column(name = "FIN_BOOKING_FORM_MILESTONES_ID") private Long finBookingFormMilestonesId;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "PROJECT_MILESTONE_ID") private Long projectMilestoneId;
	@Column(name = "FIN_BOK_FOM_DMD_NOTE_ID") private Long finBookingFormDemandNoteId;
	@Column(name = "DEMAND_NOTE_DATE") private Timestamp demandNoteDate;
	@Column(name = "MS_DMD_NOTE_DATE") private Timestamp milestoneDemandNoteDate;//milestone wise demand note date
	//@Column(name = "MS_DMD_NOTE_DATE") private Timestamp milestoneDemandNoteDate;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "MS_STATUS_ID") private Long msStatusId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	@Column(name = "TAX_AMOUNT") private Double mileStoneTaxAmount;
	@Column(name = "BASIC_AMOUNT") private Double mileStoneBasicAmount;
	@Column(name = "TOTAL_AMOUNT") private Double mileStoneTotalAmount;
}
