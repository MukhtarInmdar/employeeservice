package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBokFrmMaintenanceTaxPojo {
	@Column(name = "FIN_BOK_FRM_MAINTENANCE_TAX_ID")
	private Long finBokFrmMaintenanceTaxId;

	@Column(name = "FIN_TAX_MAPING_ID")
	private Long finTaxMappingId;

	@Column(name = "FIN_BOK_FORM_MAINTENANCE_DTLS_ID")
	private Long finBokFormMaintenanceDtlsId;
	
	@Column(name = "AMOUNT")
	private Double amount;

	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;

	/*@Column(name = "CREATED_BY")
	private Long createdBy;*/

	/*@Column(name = "CREATED_DATE")
	private Timestamp createdDate;*/
}
