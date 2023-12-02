package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class EmployeeDetailsMailPojo {

	@Column (name = "EMPLOYEE_ID")
	private Long empId;
	
	@Column (name = "EMP_DETAILS_ID")
	private Long empDetailId;

	@Column (name = "EMP_NAME")
	private String employeeName;
	
	@Column (name = "BLOCK_ID")
	private Long blockId;
	
	@Column (name = "DEPT_ID")
	private Long deptId;
	
	@Column (name = "ROLE_ID")
	private Long roleId;
	
	@Column (name = "EMP_EMAIL")
	private String empEmail;
	
	@Column (name = "SITE_ID")
	private String siteId;
	
	@Column (name = "MOBILE_NO")
	private String mobileNumber;
	
}
