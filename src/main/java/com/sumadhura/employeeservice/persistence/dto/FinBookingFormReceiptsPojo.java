package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormReceiptsPojo {
	@Column(name = "FIN_BOK_FRM_RCPT_ID") private Long finBookingFormReceiptId;
	@Column(name = "PAID_AMOUNT") private Double paidAmount;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "FIN_TRN_SET_OFF_ENT_ID") private Long transactionSetOffEntryId;
	@Column(name = "PAID_DATE") private Timestamp paidDate;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	
	public FinBookingFormReceiptsPojo() {
		 
	}
	
	public FinBookingFormReceiptsPojo(Long finBookingFormReceiptId, Double paidAmount, Long bookingFormId, Long createdBy,
			Timestamp createdDate) {
		super();
		this.finBookingFormReceiptId = finBookingFormReceiptId;
		this.paidAmount = paidAmount;
		this.bookingFormId = bookingFormId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}
}
