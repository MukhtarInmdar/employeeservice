package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class FinancialSchemeInfo {
	private String recordType;
	private Long siteId;
	private String siteName;
	private String blockName;
	private Long blockId;
	private Long flatId;
	private List<Long> flatIds;
	private String flatNo;
	private Long bookingFormId;
	private Long finSchemeId;
	private Long finTaxId;
	private Long aliasNameId;
	private Long aliasName;
	private String schemeName;
	private String taxName;
	private String schemeNameDesc;
	private String taxNameDesc;
	private Timestamp startDate;
	private Timestamp endDate;
	private Long percentageId;
	private Double percentageValue;
	private Long finSchemeTaxMappingId;
}
