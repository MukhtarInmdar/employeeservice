package com.sumadhura.employeeservice.persistence.dto;

import java.util.List;


public class LoginDeptPojo {

	private Long deptId;	
	private String deptName;
	private Long roleId;	
	private String roleName;
	private List<LoginModulePojo> loginModule;
	
	public List<LoginModulePojo> getLoginModule() {
		return loginModule;
	}
	public void setLoginModule(List<LoginModulePojo> loginModule) {
		this.loginModule = loginModule;
	}
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
	


	
}
