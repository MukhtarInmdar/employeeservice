package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
//@Table("FIN_BOOKING_FORM_ACCOUNT_SUMMARY")
public class FinBookingFormAccountSummaryPojo {
	@Column(name = "FIN_BOK_FRM_ACC_SMRY_ID") private Long finBokFrmAccountSummryId;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "PAYABLE_AMOUNT") private Double payableAmount;
	@Column(name = "PAID_AMOUNT") private Double paidAmount;
	@Column(name = "BALANCE_AMOUNT") private Double balanceAmount;
	@Column(name = "REFUND_AMOUNT") private Double refundAmount;
	@Column(name = "FLAT_GST_AMOUNT") private Double flatGstAmount;
	@Column(name = "STATUS_ID") private Long statusId;
	//@Column(name = "MS_STATUS_ID")  private Long msStatusId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
