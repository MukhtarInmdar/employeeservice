package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class FinModificationInvoicePojo {
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "FIN_BOK_FRM_MODI_COST_ID") private Long finBookingFormModiCostId;
	@Column(name = "FIN_SET_OFF_APPR_LVL_ID") private Long finsetOffAppLevelId;
	@Column(name = "TAX_AMOUNT") private Double taxAmount;
	@Column(name = "BASIC_AMOUNT") private Double basicAmount;
	@Column(name = "TOTAL_AMOUNT") private Double totalAmount;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFICATION_STATUS_ID") private Long modifiacationStatusId;
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "SITE_NAME") private String siteName;
	@Column(name = "SITE_SHORT_FORM") private String siteShortName;
	@Column(name = "FLAT_ID") private Long flatId;
	@Column(name = "CUST_ID") private Long customerId;
	@Column(name = "FLAT_NO") private String flatNo;
	@Column(name = "CUSTOMER_NAME") private String customerName;
	@Column(name = "EMP_NAME") private String pendingEmpName;
	@Column(name = "INVOICE_TYPE") private String invocieType;
	@Column(name = "EMP_ID") private Long employeeId;
	@Column(name = "BLOCK_ID") private Long blockId;
	@Column(name = "FLOOR_ID") private Long floorId;
	@Column(name = "FLAT_IDs") private List<Long> flatIds;
	@Column(name = "BLOCK_IDs") private List<Long> blockIds;
	@Column(name = "SITE_IDs") private List<Long> siteIds;
	@Column(name = "FLOOR_IDs") private List<Long> floorIds;
	
}
