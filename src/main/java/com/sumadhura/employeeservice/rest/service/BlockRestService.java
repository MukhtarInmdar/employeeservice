package com.sumadhura.employeeservice.rest.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.NonNull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sumadhura.employeeservice.dto.BlockRequest;
import com.sumadhura.employeeservice.dto.BlockResponse;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.BlockService;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;

@Path("/block")
public class BlockRestService {

	private final Logger LOGGER = Logger.getLogger(BlockRestService.class);
	

	@Autowired(required = true)
	private BlockService blockService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/blocks.spring")
	public Result getBlockNames(@NonNull DropDownRequest siteList) throws InSufficeientInputException {
		LOGGER.debug("******* The control inside of the getBlocksNames  in  BlockRestService ********");
		List<DropDownPojo> blockNames = blockService.getBlockNames(siteList);	
		Result result = new Result();
		result.setResponseObjList(blockNames);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getBlocks.spring")
	public Result getBlocks(@NonNull BlockRequest blockReq) throws InSufficeientInputException {
		LOGGER.info("*** The control is inside of the getBlocks in BlockRestService ***");
		Result result = new Result();
		BlockResponse blockResp = blockService.getBlocks(blockReq);	
		result.setResponseObjList(blockResp);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
}