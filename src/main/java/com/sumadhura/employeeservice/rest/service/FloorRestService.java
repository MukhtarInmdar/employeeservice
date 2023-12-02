package com.sumadhura.employeeservice.rest.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sumadhura.employeeservice.dto.FloorRequest;
import com.sumadhura.employeeservice.dto.FloorResponse;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.FloorService;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;

import lombok.NonNull;

@Path("/floor")
public class FloorRestService {

private final Logger logger = Logger.getLogger(FloorRestService.class);

@Autowired(required = true)
private FloorService floorService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/floor.spring")
	public Result getFloorDataByBlock(@NonNull DropDownRequest blockList) throws InSufficeientInputException {
		logger.info("******* The control inside of the getFloorDataByBlock  in  FloorRestService ********");

		List<DropDownPojo> floorNames = floorService.getFloorNames(blockList);
		Result result=new Result();
		result.setResponseObjList(floorNames);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/floorSite.spring")
	public Result getFloorsDataBySite(@NonNull DropDownRequest siteList) throws InSufficeientInputException {
		logger.info("******* The control inside of the getFloorsDataBySite  in  FloorRestService ********");
		
		List<DropDownPojo> floorNames = floorService.getFloorNamesBySite(siteList);
		Result result=new Result();
		result.setResponseObjList(floorNames);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFloors.spring")
	public Result getFloors(@NonNull FloorRequest floorReq) throws InSufficeientInputException {
		logger.info("*** The control is inside of the getFloors in FloorService ***");
		Result result = new Result();
		FloorResponse floorResp = floorService.getFloors(floorReq);	
		result.setResponseObjList(floorResp);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
}
