package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormAccountsStatementPojo {
	@Column(name = "FIN_BOK_FRM_ACC_SMT_ID") private Long finBookingFormAccountsStatementId;
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "FIN_BOK_FRM_RCPT_ID") private Long finBookingFormReceiptsId;
	@Column(name = "PAID_AMOUNT") private Double paidAmount;
	
	@Column(name = "PAID_TAX_AMOUNT") private Double paidTaxAmount;
	
	@Column(name = "STATEMENT_PAID_AMOUNT") private Double statementPaidAmount;
	@Column(name = "PAID_DATE") private Timestamp paidDate;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "STATEMENT_REFUND_AMOUNT") private Double statementRefundAmount;
	@Column(name = "ACTUAL_PAID_AMOUNT") private Double actualPaidAmount;
	@Column(name = "INTEREST_WAIVER_AMT") private Double interestWaiverAdjAmount;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
