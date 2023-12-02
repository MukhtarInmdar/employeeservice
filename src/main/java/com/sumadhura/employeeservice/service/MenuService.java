package com.sumadhura.employeeservice.service;

import java.util.List;

import com.sumadhura.employeeservice.dto.NotificationMenuDetailsDTO;

public interface MenuService {
	
	public List<NotificationMenuDetailsDTO> getMenuLevelMappingDetailsByMenuName(String menuName,Long empId);

}
