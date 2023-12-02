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
public class FinTransactionSetOffAccMapPojo {
	@Column(name = "FIN_TRN_SET_OFF_ACC_MAP_ID") private Long finTransactionSetOffAccMapId;
	@Column(name = "FIN_TRN_SET_OFF_ID") private Long finTransactionSetOffId;
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "AMOUNT_TO_SET_OFF") private Double msWiseAmountToSetOff;//ms milestone wise amount to set off
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "ACTIVE_STATUS_ID") private Long activeStatusId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
}
