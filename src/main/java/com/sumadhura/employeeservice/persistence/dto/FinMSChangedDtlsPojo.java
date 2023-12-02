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
public class FinMSChangedDtlsPojo {
	@Column(name = "FIN_MS_CHANGED_DTLS_ID") private Long finMsChangedDtlsId;
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "ACTUAL_VALUE") private String actualValue;
	@Column(name = "CHANGED_VALUE") private String changedValue;
	@Column(name = "EMP_ID") private Long empId;
	@Column(name = "EMP_NAME") private String empName;
	@Column(name = "REMARKS") private String remarks;
	@Column(name = "COLUMN_ID") private Long columnId;
	@Column(name = "METADATA_NAME") private String metaDataName;
	
	@Column(name = "ACTION_TYPE") private Long actionType;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	
	//@Column(name = "MODIFIED_BY") private Long modifiedBy;
	//@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
