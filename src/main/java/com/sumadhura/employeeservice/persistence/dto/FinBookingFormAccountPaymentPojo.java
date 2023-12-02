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
public class FinBookingFormAccountPaymentPojo {
	@Column(name = "FIN_BOK_FRM_ACC_PMT_ID") private Long bookingFormAccountPaymentId;
	@Column(name = "FIN_TRN_SET_OFF_APPR_ID") private Long transactionSetOffApprovalId;
	@Column(name = "FIN_TRN_SET_OFF_ENT_ID")   private Long transactionSetOffEntryId;
	@Column(name = "REFUND_AMOUNT") private Double refundAmount;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "FIN_BOK_FRM_ACC_PMT_DTLS_ID") private Long bookingFormAccountPaymentDetailsId;
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "COMMENTS") private String Comments;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
