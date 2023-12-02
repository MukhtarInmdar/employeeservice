package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinSchemeTaxMappingPojo {

	@Column(name = "FIN_SCHE_TAX_MAP_ID") private Long finSchemeTaxMappingId;
	@Column(name = "FIN_SCHEME_TAX_ID") private Long finSchemeTaxId;
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	@Column(name = "START_DATE") private Timestamp startDate;
	@Column(name = "END_DATE") private Timestamp endDate;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "FIN_SCHEME_ID") private Long finSchemeId;
	@Column(name = "FIN_TAX_ID") private Long finTaxId;
	@Column(name = "FIN_SCHEME_NAME") private String finSchemeName;
	
	@Column(name = "SCHEME_TYPE") private String finSchemeType;
	
	@Column(name = "FIN_SCHEME_DESCRIPTION") private String finSchemeDescription;
	@Column(name = "FIN_SCHEME_TAX_NAME") private String finSchemeTaxName;
	@Column(name = "FIN_SCHEME_TAX_DESCRIPTION") private String finSchemeTaxDescription;
	@Column(name = "FLT_BOK_SCHM_MAP_ID") private Long fltBookingSchemeMappingId;
	@Column(name = "FLAT_BOOK_ID") private Long flatBookId;
	@Column(name = "CUST_ID") private Long custId;
	@Column(name = "FLAT_ID") private Long flatId;
	@Column(name = "ALIASNAME") private Long aliasName;
	@Column(name = "FIN_BOK_FRM_MST_SCH_TAX_MAP_ID") private Long finBokFrmMstSchTaxMapId;
	@Column(name = "FIN_BOK_FRM_MST_TAX_ID") private Long finBookingFormMileStoneTaxId;
	@Column(name = "TAX_TYPE_ID") private Long taxTypeId;
	@Column(name = "TAX_TYPE") private String taxType;
	@Column(name = "CREATED_BY")private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "SITE_ID") private Long siteId;
	
}
