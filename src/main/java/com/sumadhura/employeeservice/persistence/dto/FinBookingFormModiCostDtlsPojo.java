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
public class FinBookingFormModiCostDtlsPojo{
	//@Column(name = "FIN_MODI_CHARGES_ID") private Long modificationChargesId;
	@Column(name = "FIN_BOK_MODI_COST_DTLS_ID") private Long finBookingModiCostDetailsId;
	@Column(name = "FIN_BOK_FRM_MODI_COST_ID") private Long finBookingFormModiCostId;
	@Column(name = "MODIFICATION_DESC") private String modificationChargeDesc;
	@Column(name = "UNITS") private String units;
	@Column(name = "QUANTITY") private Double quantity;
	@Column(name = "RATE") private Double rate;	
	@Column(name = "MODIFICATION_AMOUNT") private Double basicAmount;
	
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DOC_NAME") private String documentName;

}