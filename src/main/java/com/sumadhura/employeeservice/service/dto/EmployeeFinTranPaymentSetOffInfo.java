package com.sumadhura.employeeservice.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EmployeeFinTranPaymentSetOffInfo {
	//private String type;
	private Long typeId;//set off type id 
	private String setOffTypeName;
	private Long setOffTypeId;
	private String paidByName;
	private Long paidById;
	private Double amount;
	private String invoiceNo;
	private String refundFromMileStone;
	private Double payableAmount;
	private Long finTransactionSetOffId;
	private Long transactionSetOffEntryId;
	private Long transactionEntryId;
	private Long finBookingFormAccountsId;
	private Double msWiseAmountToSetOff;//ms milestone wise amount to set off
	private Long statusId;
	private Long createdBy;
}
