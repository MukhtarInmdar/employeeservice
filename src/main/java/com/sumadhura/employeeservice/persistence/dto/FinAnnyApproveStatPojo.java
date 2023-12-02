package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FinAnnyApproveStatPojo {
	@Column(name = "FIN_ANNY_APPR_STAT_ID") private Long finAnnyApproveStatId;
	@Column(name = "FIN_ANMS_ENTRY_ID") private Long anonymousEntryId;
	@Column(name = "ACTION_TYPE") private Long actionType;
	@Column(name = "ACTION_TYPE_NAME") private String actionTypeName;
	@Column(name = "EMP_ID") private Long empId;
	@Column(name ="EMP_NAME") private String empName;
	@Column(name = "COMMENTS") private String comment;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
}
