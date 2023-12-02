package com.sumadhura.employeeservice.service.dto;

import java.util.List;



public class LoginRolesInfo {

	
	private Long roleId;	
	private Long roleName;
	private List<LoginModuleInfo> loginModuleInfo;
	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the roleName
	 */
	public Long getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(Long roleName) {
		this.roleName = roleName;
	}
	/**
	 * @return the loginModuleInfo
	 */
	public List<LoginModuleInfo> getLoginModuleInfo() {
		return loginModuleInfo;
	}
	/**
	 * @param loginModuleInfo the loginModuleInfo to set
	 */
	public void setLoginModuleInfo(List<LoginModuleInfo> loginModuleInfo) {
		this.loginModuleInfo = loginModuleInfo;
	}

}
