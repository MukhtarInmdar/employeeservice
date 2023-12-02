package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinPenaltyTaxPojo {
	@Column(name = "FIN_TAX_MAPING_ID") private Long finTaxMappingId;
	@Column(name = "START_DATE") private Timestamp startDate;
	@Column(name = "END_DATE") private Timestamp endDate;
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	@Column(name = "FIN_TAX_ID") private Long finTaxId;
	
	@Column(name = "TAX_TYPE") private Long taxType;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
