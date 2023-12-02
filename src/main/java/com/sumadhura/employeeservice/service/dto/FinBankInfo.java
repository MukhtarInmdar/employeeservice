package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinBankInfo{
	private Long finBankId;
	private String bankName;
	private Long createdBy;
	private Timestamp createdDate;
	private Long statusId;
}
