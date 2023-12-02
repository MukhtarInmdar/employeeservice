package com.sumadhura.employeeservice.service.dto;

import java.util.List;



public class LoginSubModuleInfo {

	
	private Long siteId;	
	private Long siteName;
	private List<LoginSubModuleSiteInfo> loginSubModuleSiteInfo;
	
	
	
	
	
	public List<LoginSubModuleSiteInfo> getLoginSubModuleSiteInfo() {
		return loginSubModuleSiteInfo;
	}
	public void setLoginSubModuleSiteInfo(
			List<LoginSubModuleSiteInfo> loginSubModuleSiteInfo) {
		this.loginSubModuleSiteInfo = loginSubModuleSiteInfo;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Long getSiteName() {
		return siteName;
	}
	public void setSiteName(Long siteName) {
		this.siteName = siteName;
	}
	
	
	
	
	
	
}
