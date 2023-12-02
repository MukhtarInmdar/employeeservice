package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancialSchemeRequest {
	private String recordType;
	private String siteName;
	private String blockName;
	private String flatNo;
	private String schemeName;
	private String taxName;
	private String schemeNameDesc;
	private String taxNameDesc;
	private Timestamp startDate;
	private Timestamp endDate;
	//private Long percentageId;
	private Double percentageValue;
}
