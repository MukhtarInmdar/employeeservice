package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinBookingFormAccountsResponse {
	private Long finBookingFormAccountsId;
	private Long typeId;
	private Long type;
	private Double payAmount;
	private Double paidAmount;
	private Double taxAmount;
	private Double principalAmount;
	private Double balanceAmount;
	private Double refundAmount;
	private Timestamp mileStoneDueDate;
	private Long paymentStatus;
	private Timestamp paidDate;
	private Long statusId;
	private Long bookingFormId;
	private Long createdBy;
	private Timestamp createdDate;
	
	private String finBokAccInvoiceNo;

	private Double basicAmount;
	private Double totalAmount;
	private Double interestAmount;
	private Double pendingAmount;
	private String metadataName;
	private String metadataDesc;
	private String documentLocation;
	private String documentName;
	
	/* Malladi Changes */
	private Long customerId;
	private String customerName;
	private String siteName;
	private Long flatId;
	private String flatNo;
}
