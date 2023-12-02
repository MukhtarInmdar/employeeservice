package com.sumadhura.employeeservice.service;

import java.util.List;
import java.util.Set;

import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;

public interface SiteService {

	public List<DropDownPojo> getSiteList();

	public List<NotificationDetails> getSiteDetailList(Set<Long> set);

	List<DropDownPojo> getSiteList(List<Long> siteReqList);

	List<DropDownPojo> getAllSitesList(boolean flag);

	public List<DropDownPojo> getStatewiseSiteList(List<Long> siteReqList);

}
