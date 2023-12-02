package com.sumadhura.employeeservice.persistence.dto;

import java.util.List;



public class LoginSubModulePojo {

	
	private Long subModuleId;	
	private Long subModuleName;
	private List<LoginSubModuleSitePojo> loginSubModuleSiteInfo;
	
	
	
	
	
	
	
	public List<LoginSubModuleSitePojo> getLoginSubModuleSiteInfo() {
		return loginSubModuleSiteInfo;
	}
	public void setLoginSubModuleSiteInfo(
			List<LoginSubModuleSitePojo> loginSubModuleSiteInfo) {
		this.loginSubModuleSiteInfo = loginSubModuleSiteInfo;
	}
	public Long getSubModuleId() {
		return subModuleId;
	}
	public void setSubModuleId(Long subModuleId) {
		this.subModuleId = subModuleId;
	}
	public Long getSubModuleName() {
		return subModuleName;
	}
	public void setSubModuleName(Long subModuleName) {
		this.subModuleName = subModuleName;
	}
	
	
	
	
	
}
