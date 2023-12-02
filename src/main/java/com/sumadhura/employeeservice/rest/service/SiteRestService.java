package com.sumadhura.employeeservice.rest.service;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.dto.Site;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.SiteService;

import lombok.NonNull;

/**
 * 
 * @author rayudu
 *
 */
@Path("/site")
public class SiteRestService {
	private final Logger LOGGER = Logger.getLogger(SiteRestService.class);
	@Autowired
	private SiteService siteService;
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/site.spring")
	public Result getSiteList(@NonNull List<Long> siteReqList) {
		LOGGER.info("******* The control inside of the getSiteList  in  SiteRestService ********");
		List<DropDownPojo> siteList = siteService.getSiteList(siteReqList);
		Result result=new Result();
		result.setResponseObjList(siteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
	    return 	result;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/allSiteList.spring")
	public Result getAllSiteList() {
		LOGGER.info("******* The control inside of the getSiteList  in  SiteRestService ********");
		List<DropDownPojo> siteList = siteService.getSiteList();
		Result result=new Result();
		result.setResponseObjList(siteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
	    return 	result;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/allSitesList.spring")
	public Result getAllSitesList() {
		LOGGER.info("******* The control inside of the getSiteList  in  SiteRestService ********");
		List<DropDownPojo> siteList = siteService.getAllSitesList(false);
		Result result=new Result();
		result.setResponseObjList(siteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
	    return 	result;
	}
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/siteList.spring")
	public Result getAllSitesList(Site site) {
		LOGGER.info("******* The control inside of the getSiteList  in  SiteRestService ********");
		List<DropDownPojo> siteList = siteService.getAllSitesList(true);
		Result result=new Result();
		result.setResponseObjList(siteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());;
	    return 	result;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/stateWiseSites.spring")
	public Result getStateWiseSiteList(@NonNull List<Long> siteReqList) {
		LOGGER.info("******* The control inside of the getSiteList  in  SiteRestService ********");
		List<DropDownPojo> siteList = siteService.getStatewiseSiteList(siteReqList);
		Result result=new Result();
		result.setResponseObjList(siteList);
		result.setResponseCode(HttpStatus.success.getResponceCode());
	    return 	result;
	}
	
}
