package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinBookingFormExcessAmountResponse {
	private Long finBookingFormExcessAmountId;
	private Long finBookingFormReceiptsId;
	private Double excessAmount;
	private Double usedAmount;
	private Double balanceAmount;
	private Long type;
	//private String metadataName;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private String finBokAccInvoiceNo;
	private Long bookingFormId;
	private String metadataName;
	private String documentLocation;
	private String documentName;
}
