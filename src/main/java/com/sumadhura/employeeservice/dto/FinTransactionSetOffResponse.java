package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionSetOffResponse {
	private Long finTransactionSetOffId;
	// private Long transactionSetOffEntryId;
	// private Long transactionEntryId;
	private Long finBookingFormAccountsId;
	private Long setOffType;
	private String setOffTypeName;
	private String paidByName;
	private Long paidById;
	private Double payableAmount;
	private Double setOffAmount;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private String finBokAccInvoiceNo;
	// private String accInvoiceNoForRefund;
	
	/* Malladi */
	private Long finTransactionEntryId;
	private Long transactionSetOffEntryId;
	private String siteName;
	private String flatNo;
	private Long flatId;
	private Long flatBookId;
	private Double interestWaivedAmount;
	private Double paidPenaltyAmount;
	private Timestamp transactionClosedDate;
	private Timestamp paymentDate;
	private String location;
	private String docName;
	
}
