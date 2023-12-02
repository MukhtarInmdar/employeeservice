package com.sumadhura.employeeservice.dto;

import lombok.Data;

@Data
public class NotificationDetails {
	
	public Long siteId;
	public String siteName;
	public String salesForceSiteName;
	
	public Long blockId;
	public String blockName;
	public Long blockDetId;
	
	public Long floorId;
	public Long floorDetId;
	public String floorName;
	
	public Long flatId;
	public Long flatDetId;
	public String flatName;
	
	
	
}
