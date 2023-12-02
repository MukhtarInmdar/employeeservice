package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBokFrmLglCostTaxPojo {
	@Column(name = "FIN_BOK_FRM_LGL_COST_TAX_ID")
	private Long finBokFrmLglCostTaxId;

	@Column(name = "FIN_TAX_MAPING_ID")
	private Long finTaxMappingId;

	@Column(name = "FIN_BOK_FRM_LEGAL_COST_ID")
	private Long finBokFrmLegalCostId;

	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@Column(name = "MODIFIED_BY")
	private Long modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
}
