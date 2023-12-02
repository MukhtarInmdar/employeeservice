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
public class FinBookingFormModiCostPojo {
	@Column(name = "FIN_BOK_FRM_MODI_COST_ID") private Long finBookingFormModiCostId;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "TAX_AMOUNT") private Double taxAmount;
	@Column(name = "BASIC_AMOUNT") private Double basicAmount;
	@Column(name = "TOTAL_AMOUNT") private Double totalAmount;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	@Column(name = "FIN_SET_OFF_APPR_LVL_ID") private Long finsetOffAppLevelId;
	@Column(name = "MODIFICATION_STATUS_ID") private Long modifiacationStatusId;
	
	@Column(name = "FIN_SITE_PROJ_ACC_MAP_ID") private Long siteAccountId;
	@Column(name = "ACCOUNT_NO") private String siteBankAccountNumber;

}
