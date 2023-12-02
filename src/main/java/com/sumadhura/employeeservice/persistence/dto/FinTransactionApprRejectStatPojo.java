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
public class FinTransactionApprRejectStatPojo {
	@Column(name = "FIN_TRN_APPR_STAT_ID") private Long transactionApproveStatId;
	@Column(name = "FIN_TRN_SET_OFF_ENT_ID") private Long transactionSetOffEntryId;
	@Column(name = "ACTION_TYPE") private Long actionType;
	@Column(name = "EMP_ID") private Long empId;
	@Column(name = "COMMENTS") private String comment;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
}
