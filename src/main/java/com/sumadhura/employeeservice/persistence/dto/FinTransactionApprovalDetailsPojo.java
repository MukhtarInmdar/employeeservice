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
public class FinTransactionApprovalDetailsPojo {
	@Column(name = "FIN_TRN_SET_OFF_APPR_LVL_ID") private Long transactionSetOffApprovalLevelId;
	@Column(name = "FIN_TRAN_SET_OFF_LEVEL_ID") private Long transactionSetOffLevelId;
	@Column(name = "NEXT_TRN_SET_OFF_APPR_LVL_ID") private Long nextTransactionSetOffApprovalLevelId;
	@Column(name = "FIN_TRN_SET_OFF_APPR_ID") private Long transactionSetOffApprovalId;
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "EMP_ID") private Long empId;
	@Column(name = "EMP_NAME") private String empName;
	@Column(name = "EMP_EMAIL") private String empEmail;
	@Column(name = "LEVEL_NAME") private String levelName;
	@Column(name = "TRN_REQUEST_LEVEL") private Long trnRequestLevel;
	@Column(name = "NEXT_APPROVAL_MODULE") private Long nextApprovalModule;
	@Column(name = "MODIFY_TRAN_SET_OFF_LEVEL_ID") private Long modifyTranSetOffLevelId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
