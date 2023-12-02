package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormLegalCostPojo {
	
	@Column(name = "FIN_BOK_FRM_LEGAL_COST_ID")
	private Long finBokFrmLegalCostId;
	
	@Column(name = "BOOKING_FORM_ID")
	private Long bookingFormId;
	
	@Column(name = "LEGAL_AMOUNT")
	private Double legalAmount;
	
	@Column(name = "TAX_AMOUNT")
	private Double taxAmount;
	
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "MODIFIED_BY")
	private Long modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "FIN_SITE_PROJ_ACC_MAP_ID") private Long finSiteProjAccountMapId;
}