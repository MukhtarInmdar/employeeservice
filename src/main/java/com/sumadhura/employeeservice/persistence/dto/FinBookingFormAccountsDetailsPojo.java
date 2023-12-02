package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
//not in use
public class FinBookingFormAccountsDetailsPojo {

	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "PAY_AMOUNT") private Double payAmount;
	@Column(name = "PAID_AMOUNT") private Double paidAmount;
	@Column(name = "DUE_DATE") private Timestamp mileStoneDueDate;
	@Column(name = "PAYMENT_STATUS") private Long paymentStatus;
	@Column(name = "PAID_DATE") private Timestamp paidDate;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	
}