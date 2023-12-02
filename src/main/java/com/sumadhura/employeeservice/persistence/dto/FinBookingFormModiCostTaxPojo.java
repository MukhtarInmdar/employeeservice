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
public class FinBookingFormModiCostTaxPojo {
	@Column(name = "FIN_MODI_TAX_MAP_ID") private Long finBookingFormModiCostTaxId;
	@Column(name = "FIN_TAX_MAPING_ID") private Long finTaxMappingId;
	@Column(name = "FIN_BOK_FRM_MODI_COST_ID") private Long finBookingFormModiCostId;
	@Column(name = "AMOUNT") private Double amount;
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
