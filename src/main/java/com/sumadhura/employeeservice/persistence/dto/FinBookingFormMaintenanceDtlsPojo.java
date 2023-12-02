package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormMaintenanceDtlsPojo {
	@Column(name = "FIN_BOK_FORM_MAINTENANCE_DTLS_ID")
	private Long finBokFormMaintenanceDtlsId;
	
	@Column(name = "FIN_BOK_FRM_MAINTENANCE_ID")
	private Long finBokFrmMaintenanceId;
	
	@Column(name = "BASIC_AMOUNT")
	private Double basicAmount;
	
	@Column(name = "TAX_AMOUNT")
	private Double taxAmount;

	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	
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
	
	private List<FinBokFrmMaintenanceTaxPojo> maintenanceTaxPojos;

}
