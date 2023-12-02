package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormLglCostDtlsPojo {
	@Column(name = "FIN_BOOKING_FORM_LGL_COST_DTLS_ID")
	private Long finBokingFormLglCostDtlsId;
	
	@Column(name = "FIN_BOK_FRM_LEGAL_COST_ID")
	private Long finBokFrmLegalCostId;
	
	@Column(name = "LEGAL_AMOUNT")
	private Double legalAmount;
	
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
}
