package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@ToString
public class FinTransactionChangedDtlsPojo {
	@Column(name = "FIN_TRN_CHANGED_DTLS_ID") private Long transactionChangedDtlsId;
	@Column(name = "FIN_ANMS_ENTRY_ID") private Long anonymousEntryId ;
	@Column(name = "FIN_TRN_APPR_STAT_ID") private Long transactionApproveStatId;
	@Column(name = "ACTUAL_VALUE") private String actualValue;
	@Column(name = "CHANGED_VALUE") private String changedValue;
	@Column(name = "EMP_ID") private Long empId;
	@Column(name = "EMP_NAME") private String empName;
	@Column(name = "REMARKS") private String remarks;
	@Column(name = "COLUMN_ID") private Long columnId;
	@Column(name = "METADATA_NAME") private String metaDataName;
	@Column(name = "ACTION_TYPE") private Long actionType;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
