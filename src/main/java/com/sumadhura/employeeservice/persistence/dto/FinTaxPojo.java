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
public class FinTaxPojo {
	@Column(name = "FIN_TAX_ID") private Long finTaxId;
	@Column(name = "TAX_NAME") private String taxName;
	@Column(name = "TAX_DESCRIPTION") private String description;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "CREATED_BY")private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	@Column(name = "ALIAS_NAME") private String aliasName;
	@Column(name = "ALIAS_NAME_ID") private Long aliasNameId;
}
