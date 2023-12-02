package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
@Entity
@Data
public class FinBookingFormMilestoneTaxPojo {
	@Column(name = "FIN_BOK_FRM_MST_TAX_ID") private Long finBookingFormMileStoneTaxId;
	@Column(name = "FIN_BOOKING_FORM_MILESTONES_ID") private Long finBookingFormMilestonesId;
	@Column(name = "TAX_AMOUNT") private Double mileStoneTaxAmount;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
