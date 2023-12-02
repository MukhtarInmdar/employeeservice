package com.sumadhura.employeeservice.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumadhura.employeeservice.dto.NotificationDetails;
import com.sumadhura.employeeservice.persistence.dao.SiteDao;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.SiteService;
import com.sumadhura.employeeservice.util.Util;

@Service
public class SiteServiceImpl implements SiteService {
	private static final Logger LOGGER = Logger.getLogger(SiteServiceImpl.class);

	@Autowired(required = true)
	private SiteDao siteDao;

	

	@Override
	public List<DropDownPojo> getSiteList() {
		LOGGER.debug("**** The control is inside the getSiteList in SiteServiceImpl *****");
		List<DropDownPojo> siteList = siteDao.getSiteList();
		return siteList;
	}

	@Override
	public List<NotificationDetails> getSiteDetailList(Set<Long> set) {
		List<NotificationDetails>  siteData=siteDao.getSiteDetailList(set);
		
		return siteData;
	}
	
	@Override
	public List<DropDownPojo> getSiteList(List<Long> siteReqList) {
		List<NotificationDetails>  siteData=siteDao.getSiteDetailList(new HashSet<Long>(siteReqList));
		List<DropDownPojo> siteList=new ArrayList<>();
		for (NotificationDetails notificationDetails : siteData) {
			DropDownPojo siteObj=new DropDownPojo();
			siteObj.setId(notificationDetails.getSiteId());
			siteObj.setName(notificationDetails.getSiteName());
			siteObj.setSalesforceSiteName(notificationDetails.getSalesForceSiteName());
			siteList.add(siteObj);
		}
		
		//	List<DropDownPojo> siteList = siteDao.getSiteList();
		return siteList;
	}
	
	@Override
	public List<DropDownPojo> getAllSitesList(boolean flag) {
		LOGGER.debug("**** The control is inside the getSiteList in SiteServiceImpl *****");
		List<DropDownPojo> siteList = siteDao.getAllSitesList();
		if(Util.isNotEmptyObject(siteList) && flag){
			DropDownPojo pojo = new DropDownPojo();
			pojo.setId(null);
			pojo.setName("N/A");
			siteList.add(pojo);
		}
		return siteList;
	}

	@Override
	public List<DropDownPojo> getStatewiseSiteList(List<Long> siteReqList) {
		List<NotificationDetails>  siteData=siteDao.getStateWiseSiteDetailList(new HashSet<Long>(siteReqList));
		List<DropDownPojo> siteList=new ArrayList<>();
		for (NotificationDetails notificationDetails : siteData) {
			DropDownPojo siteObj=new DropDownPojo();
			siteObj.setId(notificationDetails.getSiteId());
			siteObj.setName(notificationDetails.getSiteName());
			siteList.add(siteObj);
		}
		
		//	List<DropDownPojo> siteList = siteDao.getSiteList();
		return siteList;
	}
	

}
