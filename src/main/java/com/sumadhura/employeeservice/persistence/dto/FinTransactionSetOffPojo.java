package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class FinTransactionSetOffPojo {
	@Column(name = "FIN_TRN_SET_OFF_ID") private Long finTransactionSetOffId;
	@Column(name = "FIN_TRN_SET_OFF_ENT_ID")  private Long transactionSetOffEntryId;
	//@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "SET_OFF_TYPE") private Long setOffType;
	@Column(name = "PAYABLE_AMOUNT") private Double payableAmount;
	@Column(name = "SET_OFF_AMOUNT") private Double setOffAmount;
	@Column(name = "SET_OFF_GST_AMOUNT") private Double setOffGstAmount;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "PAID_BY_NAME")private String paidByName;
	@Column(name = "PAID_BY") private Long paidById;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	@Column(name = "FIN_BOK_ACC_INVOICE_NO") private String finBokAccInvoiceNo;
	//@Column(name = "ACC_INVOICE_NO_FOR_REFUND") private String accInvoiceNoForRefund;
	@Column(name = "METADATA_NAME")private String setOffTypeName;
	
	/* Malladi */
	@Column(name = "FIN_TRANSACTION_ENTRY_ID")
	private Long finTransactionEntryId;
	
	@Column(name="SITE_NAME")
	private String siteName;
	
	@Column(name="FLAT_NO")
	private String flatNo;
	
	@Column(name="FLAT_ID")
	private Long flatId;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	
	@Column(name="INTEREST_WAIVED_AMOUNT")
	private Double interestWaivedAmount;
	
	@Column(name="PAID_PENALTY_AMOUNT")
	private Double paidPenaltyAmount;
	
	@Column(name="TRANSACTION_CLOSED_DATE")
	private Timestamp transactionClosedDate;
	
	@Column(name="PAYMENT_DATE")
	private Timestamp paymentDate;
	
	@Column(name="LOCATION")
	private String location;
	
	@Column(name="DOC_NAME")
	private String docName;
	
}
