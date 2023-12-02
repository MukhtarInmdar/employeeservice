package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;



import lombok.Data;

@Entity
@Data
public class EmployeeLevelDetailsPojo {

	@Column (name = "EMP_ID")
	private Long empId;
	

	@Column (name = "EMP_NAME")
	private String empName;
	
	@Column (name = "EMP_DETAILS_ID")
	private Long empDetailsId;
	

	@Column (name = "DEPT_ID")
	private Long deptId;
	
	@Column (name = "DEPT_NAME")
	private String deptName;
	
	@Column (name = "ROLE_ID")
	private Long roleId;
	
	@Column (name = "ROLE_NAME")
	private String roleName;
	
	
}
