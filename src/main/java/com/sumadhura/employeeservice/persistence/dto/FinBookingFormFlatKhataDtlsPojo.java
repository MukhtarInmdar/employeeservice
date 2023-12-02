package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormFlatKhataDtlsPojo {
	@Column(name = "FIN_BOK_FORM_FLAT_KHATA_DTLS_ID")
	private Long finBokFormFlatKhataDetailsId;
	
	@Column(name = "FIN_BOK_FRM_FLAT_KHATA_ID")
	private Long finBokFrmFlatKhataId;
		
	@Column(name = "BASIC_AMOUNT")
	private Double basicAmount;
	
	@Column(name = "TAX_AMOUNT")
	private Double taxAmount;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DOC_NAME") private String documentName;
	
	private List<FinBokFrmFlatKhataTaxPojo> flatKhataTAXDetailsPojo;
	
}
