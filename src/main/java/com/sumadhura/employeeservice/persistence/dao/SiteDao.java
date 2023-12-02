package com.sumadhura.employeeservice.persistence.dao;

import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;

public interface SiteDao {
	public List<DropDownPojo> getSiteList() ;

	public List<NotificationDetails> getSiteDetailList(Set<Long> set);

	public List<DropDownPojo> getAllSitesList();

	public List<NotificationDetails> getStateWiseSiteDetailList(Set<Long> set);
}
