package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FlatCancellationCostPojo {

	private Long id;
	private Long finTransactionEntryId;
	private Long bookingFormId;
	private Double amount;
	private Long status;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	
}
