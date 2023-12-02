package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ThirdPartyMultiLoginPojo {

	@Column(name = "SITE_ID")
	private Long siteId;

	@Column(name = "SITE_NAME")
	private String siteName;

	@Column(name = "EMP_ID")
	private String empId;
	
	private String portalLoginUrl;
	private String portalRedirectUrl;
	private String portalName;
	private Boolean isRequestFromUAT;
	private Boolean isRequestFromCUG;
	private Boolean isRequestFromLive;
	
}
