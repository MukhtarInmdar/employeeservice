package com.sumadhura.employeeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Getter
@Setter
public class EmployeeFinTranPaymentSetOffRequest {
	private String setOffTypeName;
	//private String type;
	private String paidByName;
	private Double amount;
	private String invoiceNo;
	private String refundFromMileStone;
	private Long finTransactionSetOffId;
}