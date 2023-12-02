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
public class FinSchemePojo {
	@Column(name = "FIN_SCHEME_ID") private Long finSchemeId;
	@Column(name = "FIN_SCHEME_NAME") private String finSchemeName;
	@Column(name = "FIN_SCHEME_DESCRIPTION") private String finSchemeDescription;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "CREATED_BY")private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	
	@Column(name = "PERCENTAGE_ID")private Long percentageId;
	@Column(name = "PERCENTAGE")private Long percentage;
	@Column(name = "FIN_TAX_ID")private Long finTaxId;
	@Column(name = "TAX_NAME")private Long taxName;
}
