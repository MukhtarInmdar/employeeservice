package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormRefundableAdvancePojo {
	
	private Long id;
	private Long finTransactionEntryId;
	private Long bookingFormId;
	private Double amount;
	private Long status;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;

}
