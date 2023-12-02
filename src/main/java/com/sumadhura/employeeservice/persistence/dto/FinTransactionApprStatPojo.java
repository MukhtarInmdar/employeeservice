package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinTransactionApprStatPojo {
	@Column(name = "FIN_TRN_APPR_STAT_ID")
	private Long finTrnApprStatId;

	@Column(name = "PREV_FIN_TRN_APPR_STAT_ID")
	private Long prevFinTrnApprStatId;

	@Column(name = "FIN_TRN_SET_OFF_ENT_ID")
	private Long finTrnSetOffEntId;
	private Long transactionSetOffEntryId;

	@Column(name = "ACTION_TYPE")
	private Long actionType;

	@Column(name = "EMP_ID")
	private Long empId;

	@Column(name = "COMMENTS")
	private String comments;
	private String comment;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@Column(name = "EMP_NAME") private String empName;
	@Column(name = "EMP_EMAIL") private String empEmail;
	@Column(name = "LEVEL_NAME") private String levelName;

	
}
