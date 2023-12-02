package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FinBookingFormModiCostDtlsResponse {
	private Long modificationChargesId;
	private Long finBookingModiCostDetailsId;
	private Long finBookingFormModiCostId;
	private String modificationChargeDesc;
	private String units;
	private String quantity;
	private String rate;	
	private String basicAmount;
 	private Long createdBy;
	private Timestamp createdDate;
	/*private Long modifiedBy;
	private Timestamp modifiedDate;*/
	private String basAmount;
	private Long percentageId;
	private String percentageValue;
}
