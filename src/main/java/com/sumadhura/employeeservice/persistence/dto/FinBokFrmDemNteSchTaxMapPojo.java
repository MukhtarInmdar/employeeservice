package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBokFrmDemNteSchTaxMapPojo {
	
	@Column(name = "FIN_BOK_FRM_MST_SCH_TAX_MAP_ID") private Long finBokFrmMstSchTaxMapId;
	@Column(name = "FIN_BOK_FRM_MST_TAX_ID") private Long finBookingFormMileStoneTaxId;
	@Column(name ="FIN_SCHE_TAX_MAP_ID") private Long finScheTaxMapId;
	@Column(name = "FLT_BOK_SCHM_MAP_ID") private Long fltBookingSchemeMappingId;
	@Column(name ="CREATED_BY") private Long createdBy;
	@Column(name ="CREATED_DATE") private Timestamp createdDate;
	@Column(name ="MODIFIED_BY") private Long modifiedBy;
	@Column(name ="MODIFIED_DATE") private Timestamp modifieDate;
	@Column(name ="AMOUNT") private Double basicAmount;

	@Column(name = "FIN_SCHE_TAX_MAP_ID") private Long finSchemeTaxMappingId;
	@Column(name = "FIN_SCHEME_TAX_ID") private Long finSchemeTaxId;
	@Column(name = "PERCENTAGE_ID") private Long percentageId;
	@Column(name = "PERCENTAGE_VALUE") private Double percentageValue;
	
	@Column(name = "FIN_SCHEME_ID") private Long finSchemeId;
	@Column(name = "FIN_SCHEME_NAME") private String finSchemeName;
	@Column(name = "SCHEME_TYPE") private String finSchemeType;
	
	@Column(name = "TAX_TYPE_ID") private Long taxTypeId;
	@Column(name = "TAX_TYPE") private String taxType;
}
