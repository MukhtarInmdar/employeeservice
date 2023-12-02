package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinBookingFormDemandNoteResponse {

	 private Long finBookingFormDemandNoteId;
	 private Long bookingFormId;
	 private Double mileStoneTaxAmount;
	 private Double mileStoneBasicAmount;
	 private Double mileStoneTotalAmount;
	 private Double mileStoneBalanceAmount;//this is due amount
	 private String demandNoteStatus;
	 private Long createdBy;
	 private Timestamp createdDate;
	 private Long modifiedBy;
	 private Timestamp modifiedDate;
	 private String demandNoteNo;
	 private Timestamp demandNoteDate;
	 private String custName;
	 private String status;
	 private Timestamp dueDate;
	 private Timestamp agreementDate;
	 private Timestamp bookingDate;
	 private String documentLocation;
	 private Long demandNoteType;
	 private Double interestAmount;
	 private String employeeName;
	 private String interestAmountWithFormat;
	 private String flatNo;
	 private String milestoneName;	
		
		
}
