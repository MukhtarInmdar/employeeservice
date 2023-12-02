package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
@Entity
@Data//not in use
public class FinModificationTaxPojo {
	@Column(name = "FIN_MODI_TAX_ID") private Long finmodificationTaxId;
	@Column(name = "START_DATE") private Timestamp startDate;
	@Column(name = "END_DATE") private Timestamp endDate;
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	@Column(name = "FIN_TAX_ID") private Long finTaxId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;

}
