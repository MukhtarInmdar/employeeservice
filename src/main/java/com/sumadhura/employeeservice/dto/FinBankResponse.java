package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinBankResponse{
	private Long finBankId;
	private String bankName;
	private Long createdBy;
	private Timestamp createdDate;
	private Long statusId;
}
