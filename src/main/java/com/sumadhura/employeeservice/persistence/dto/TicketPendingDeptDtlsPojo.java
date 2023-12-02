package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class TicketPendingDeptDtlsPojo {
	
	@Column(name="PENDING_EMP_OR_DEPT_ID")
	private Long pendingEmpOrDeptId;
	
	@Column(name="PENDING_EMP_OR_DEPT_NAME")
	private String pendingEmpOrDeptName;
	
	@Column(name="TYPE_ID")
	private Long typeId;
	
}
