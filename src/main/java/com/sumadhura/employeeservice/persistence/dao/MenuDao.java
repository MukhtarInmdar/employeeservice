package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;

import com.sumadhura.employeeservice.dto.NotificationMenuDetailsDTO;

public interface MenuDao {

	public List<NotificationMenuDetailsDTO> getMenuLevelMappingDetailsByMenuName(String menuName,Long empId);
	
}
