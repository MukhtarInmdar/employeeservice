package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;



import lombok.Data;

@Entity
@Data
public class InboxSiteDetails {
	
	@Column(name="SITE_ID")
	public Long siteId;
	@Column(name="SITE_NAME")
	public String siteName;
	@Column(name="siteStatus")
	public String siteStatus;
	
}
