package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;


import lombok.Data;

@Data
public class FinBookingFormMstSchTaxMapInfo {

	private Long finBokFrmMstSchTaxMapId;
	private Long finBookingFormMileStoneTaxId;
	private Long finScheTaxMapId;
	private Long fltBookingSchemeMappingId;
	private Long bookingFormId;
	private Double basicAmount;
	private Double totalAmount;
	private Double gstAmount;
	private Double CGST_Amount;
	private Double SGST_amount;
	private Double mileStoneDueAmount;
	
	private Long finSchemeTaxMappingId;
	private Long finSchemeTaxId;
	private Long percentageId;
	private Double percentageValue;
	private String finSchemeType;
	private Long finSchemeId;
	private String finSchemeName;
	
	private Long taxTypeId;
	private String taxType;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifieDate;

}
