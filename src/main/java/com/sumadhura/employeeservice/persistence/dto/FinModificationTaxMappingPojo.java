package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;

public class FinModificationTaxMappingPojo {
	@Column(name = "FIN_MODI_TAX_MAP_ID") private Long modificationTaxMapId;
	@Column(name = "FIN_MODI_CHARGES_ID") private Long modificationChargesId;
	@Column(name = "FIN_MODI_TAX_ID") private Long finmodificationTaxId;
	@Column(name = "AMOUNT") private Double amount;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;

}
