package com.sumadhura.employeeservice.service;

import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.FloorRequest;
import com.sumadhura.employeeservice.dto.FloorResponse;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;

public interface FloorService {
	
	public List<DropDownPojo> getFloorNames(DropDownRequest blockList);
	public List<DropDownPojo> getFloorNamesBySite(DropDownRequest siteList);
	public List<NotificationDetails> getFloorDetailList(Set<Long> set);
	public FloorResponse getFloors(FloorRequest floorReq);
	
}
