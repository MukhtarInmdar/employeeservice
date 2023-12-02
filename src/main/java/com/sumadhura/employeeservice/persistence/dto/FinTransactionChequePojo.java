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
public class FinTransactionChequePojo {
	@Column(name = "FIN_TRANSACTION_CHEQUE_ID") private Long transactionChequeId;
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "CHEQUE_NO") private String chequeNumber;
	@Column(name = "CHEQUE_DATE") private Timestamp chequeDate;
	@Column(name = "AMOUNT") private Double chequeAmount;
	@Column(name = "CLEARANCE_DATE") private Timestamp chequeClearanceDate;
	@Column(name = "CHEQUE_DEPOSITED_DATE") private Timestamp chequeDepositedDate;
	@Column(name = "CHEQUE_BOUNCE_REASON_ID") private Long chequeBounceReasonId;
	@Column(name = "CHEQUE_BOUNCE_REASON_VALUE") private String chequeBounceReasonValue;
	@Column(name = "CHEQUE_BOUNCE_COMMENT") private String chequeBounceComment;
	@Column(name = "CHEQUE_HANDOVER_DATE") private Timestamp chequeHandoverDate;
	@Column(name = "CHEQUE_BOUNCE_DATE") private Timestamp chequeBounceDate;
	@Column(name = "CREATED_BY")private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	
//	@Column(name = "RECEIVED_DATE")private Timestamp receivedDate;
//	@Column(name = "COMMENTS")private String comments;
//	@Column(name = "FIN_TRANSACTION_NO") private String finTransactionNo;
//	@Column(name = "BANK_NAME") private String bankName;
//	@Column(name = "ACCOUNT_NO") private Long companyAccountNo;
//	@Column(name = "PAYABLE_AMOUNT") private Double payableAmount;
//	@Column(name = "BOOKING_FORM_ID")private Long bookingFormId;
}
