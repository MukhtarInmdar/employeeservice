package com.sumadhura.employeeservice.persistence.dto;



import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class SiteInfo {
	@Column (name = "SITE_ID")
	private Long siteId;
	
	@Column (name = "SITE_NAME")
	private String siteName;
	
	@Column (name = "IMAGE_LOCATION")
	private String imageLocation;
	
}
