package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class FinancialGstDetailsInfo {
	private Long rowNumber;
	private Long siteId;
	/*private Long finSchemeTaxMappingId;
	private Long finSchemeTaxId;*/
	private Long percentageId;
	private Double percentageValue;
	private Timestamp startDate;
	private Timestamp endDate;
	private Timestamp actualStartDate;
	private Timestamp actualEndDate;
	private Long actualPercentageId;
	private Double actualPercentageValue;
	private Long statusId;
	/*private Long finSchemeId;
	private String finSchemeName;
	private String finSchemeDescription;
	private String finSchemeTaxName;
	private String finSchemeTaxDescription;
	private Long fltBookingSchemeMappingId;
	private Long taxTypeId;
	private String taxType;*/
	private Long flatId;
	private Long bookingFormId;
	private String rowStatus;
}
