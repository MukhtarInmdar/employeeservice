package com.sumadhura.employeeservice.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumadhura.employeeservice.dto.NotificationMenuDetailsDTO;
import com.sumadhura.employeeservice.persistence.dao.MenuDao;
import com.sumadhura.employeeservice.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuDao menuDao;
	
	@Override
	public List<NotificationMenuDetailsDTO> getMenuLevelMappingDetailsByMenuName(String menuName,Long empId) {
		
		List<NotificationMenuDetailsDTO> menuLevelMappingDetailsByMenuName = menuDao.getMenuLevelMappingDetailsByMenuName(menuName, empId);
		return menuLevelMappingDetailsByMenuName;
	}

}
