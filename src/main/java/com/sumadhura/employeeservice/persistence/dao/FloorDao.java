package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.FloorRequest;
import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.persistence.dto.FloorDetailsPojo;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;

public interface FloorDao {

	public List<DropDownPojo> getFloorNames(DropDownRequest blockList);
	public List<DropDownPojo> getFloorNamesBySite(DropDownRequest siteList);
	public List<NotificationDetails> getFloorDetailList(Set<Long> set);
	public List<FloorDetailsPojo> getFloorDetails(FloorRequest floorReq);
	
}
