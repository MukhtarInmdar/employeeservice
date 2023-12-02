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
public class FlatBookingSchemeMappingPojo {
	@Column(name = "FLT_BOK_SCHM_MAP_ID") private Long fltBookingSchemeMappingId;
	@Column(name = "FIN_SCHE_TAX_MAP_ID") private Long finSchemeTaxMappingId;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "PAST_STATUS_ID") private Long pastStatusId;
	@Column(name = "FLAT_BOOK_ID") private Long bookingFormId;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "CREATED_BY")private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
}
