package com.sumadhura.employeeservice.service.dto;

import java.util.List;

public class LoginResponseInfo{

	
	private List<LoginDeptInfo> loginDeptInfo;
	private String empName;
	private Long empId;
	
	
	public List<LoginDeptInfo> getLoginDeptInfo() {
		return loginDeptInfo;
	}
	public void setLoginDeptInfo(List<LoginDeptInfo> loginDeptInfo) {
		this.loginDeptInfo = loginDeptInfo;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	
	
	
	
}
