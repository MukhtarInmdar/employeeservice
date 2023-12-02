package com.sumadhura.employeeservice.persistence.dto;

import java.util.List;



public class LoginModulePojo {

	
	public List<LoginSubModulePojo> getLoginSubModuleInfo() {
		return loginSubModuleInfo;
	}
	public void setLoginSubModuleInfo(List<LoginSubModulePojo> loginSubModuleInfo) {
		this.loginSubModuleInfo = loginSubModuleInfo;
	}
	private Long moduleId;	
	private Long moduleName;
	private List<LoginSubModulePojo> loginSubModuleInfo;
	
	
	
	
	
	
	
	
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}
	public Long getModuleName() {
		return moduleName;
	}
	public void setModuleName(Long moduleName) {
		this.moduleName = moduleName;
	}
	
	
	
	
	
	
	
}
