package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinInterestRatesPojo {
	@Column(name = "FIN_INTEREST_RATES_ID") private Long finInterestRatesId;
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "START_DATE") private Timestamp startDate;
	@Column(name = "END_DATE") private Timestamp endDate;
	
	@Column(name = "ACTUAL_START_DATE") private Timestamp actualStartDate;
	@Column(name = "ACTUAL_END_DATE") private Timestamp actualEndDate;
	
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "ACTIVE_STATUS_ID") private Long activeStatusId;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	@Column(name = "EMP_NAME") private String employeeName;
	@Column(name = "MODIFIED_EMP_NAME") private String modifiedEmpName;
}
