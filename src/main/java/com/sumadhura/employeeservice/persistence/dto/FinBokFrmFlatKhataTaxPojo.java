package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBokFrmFlatKhataTaxPojo {
	@Column(name = "FIN_BOK_FRM_FLAT_KHATA_TAX_ID")
	private Long finBokFrmFlatKhataTaxId;

	@Column(name = "FIN_TAX_MAPING_ID")
	private Long finTaxMappingId;

	@Column(name = "FIN_BOK_FORM_FLAT_KHATA_DTLS_ID")
	private Long finBokFormFlatKhataDetailsId;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;

	/*@Column(name = "CREATED_BY")
	private Long createdBy;*/

	/*@Column(name = "CREATED_DATE")
	private Timestamp createdDate;*/
}
