package com.sumadhura.employeeservice.service.dto;

import java.util.List;


public class LoginDeptInfo {

	
	private Long deptId;	
	private String deptName;
	private Long roleId;	
	private String roleName;
	private List<LoginModuleInfo> loginModule;
	
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public List<LoginModuleInfo> getLoginModule() {
		return loginModule;
	}
	public void setLoginModule(List<LoginModuleInfo> loginModule) {
		this.loginModule = loginModule;
	}
	
}
