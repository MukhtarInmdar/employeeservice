package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinancialBookingFormAccountsRequest {
	private Long paidAmount;
	private Timestamp dueDate;
	private Timestamp amountPaidDate;
	private Long bookingFormId;
	private Long flatId;
	private String flatNo; 
}
