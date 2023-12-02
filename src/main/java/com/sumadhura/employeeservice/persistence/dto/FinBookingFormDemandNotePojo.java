package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormDemandNotePojo {

	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "FIN_BOK_FOM_DMD_NOTE_ID")private Long finBookingFormDemandNoteId;	
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DEMAND_NOTE_DATE") private Timestamp demandNoteDate;
	//@Column(name = "TYPE_OF_INTEREST") private Long interestSelectionType;
	@Column(name = "INTEREST_SELECTION_TYPE") private Long interestSelectionType;
	@Column(name = "TAX_AMOUNT") private Double mileStoneTaxAmount;
	@Column(name = "DEMAND_NOTE_STATUS") private String demandNoteStatus;
	@Column(name = "BASIC_AMOUNT") private Double mileStoneBasicAmount;
	@Column(name = "BALANCE_AMOUNT") private Double mileStoneBalanceAmount;//this is due amount
	@Column(name = "REGULAR_BAL_MS_AMOUNT") private Double regularBalMsAmount;//this is due amount
	
	@Column(name = "TOTAL_AMOUNT") 	private Double mileStoneTotalAmount;
	@Column(name = "PAY_AMOUNT") private Double mileStoneWiseTotalAmount;
	
	@Column(name = "DEMAND_NOTE_FORMAT") private Long demandNoteFormat;
	
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "DEMAND_NOTE_STATUS_ID") private Long demandNoteStatusId;
	
	@Column(name = "CREATED_BY") 	private Long createdBy;
	
	@Column(name = "CREATED_DATE")	private Timestamp createdDate;
	
	@Column(name = "MODIFIED_BY") 	private Long modifiedBy;
	
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	
	@Column(name ="DEMAND_NOTE_NO") private String demandNoteNo;
	
 	@Column(name ="CUST_NAME")private String custName;
	
	@Column(name ="STATUS")	private String status;
	
	@Column(name ="DUE_DATE")private Timestamp dueDate;
	@Column(name ="REGULAR_MS_DUE_DATE")private Timestamp regularMsDueDate;

	@Column(name = "AGREEMENT_DATE")	private Timestamp agreementDate;
	
	@Column(name = "BOOKING_DATE")	private Timestamp bookingDate;

	@Column(name ="DEMAND_NOTE_TYPE") private Long demandNoteType;
	
	@Column(name ="INTEREST_AMOUNT")	private Double interestAmount;
	
	private String actionUrl;
	@Column(name ="EMP_NAME")	private String employeeName;
	
	@Column(name ="FLAT_NO")	private String flatNo;
	
	@Column(name = "MILESTONE_NAME")	private String milestoneName;
	
	
	
}
