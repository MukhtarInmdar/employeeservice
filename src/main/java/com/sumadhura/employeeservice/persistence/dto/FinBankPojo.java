package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FinBankPojo{
	@Column(name ="FIN_BANK_ID")
	private Long finBankId;
	
	@Column(name ="BANK_NAME")
	private String bankName;
	
	@Column(name ="CREATED_BY")
	private Long createdBy;
	
	@Column(name ="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name ="STATUS_ID")
	private Long statusId;
	
}
