package com.sumadhura.employeeservice.rest.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.dto.Customer;
import com.sumadhura.employeeservice.dto.FlatRequest;
import com.sumadhura.employeeservice.dto.FlatResponse;
import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.InSufficeientInputException;
import com.sumadhura.employeeservice.persistence.dto.CustomerPropertyDetailsPojo;
import com.sumadhura.employeeservice.persistence.dto.DropDownPojo;
import com.sumadhura.employeeservice.service.FlatService;
import com.sumadhura.employeeservice.service.dto.DropDownRequest;

import lombok.NonNull;

@Path("/flat")
public class FlatRestService {

private final Logger logger = Logger.getLogger(FlatRestService.class);

@Autowired(required = true)
private FlatService flatService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/flat.spring")
	public Result getFlatsNames(@NonNull DropDownRequest dropDownRequest) throws InSufficeientInputException {
		logger.info("******* The control inside of the getFlatsNames  in  FlatRestService ********");
		List<DropDownPojo> flatsNames = flatService.getFlatsNames(dropDownRequest);
		Result result=new Result();
		result.setResponseObjList(flatsNames);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/flatSite.spring")
	public Result getFlatsNamesBySite(@NonNull DropDownRequest dropDownRequest) throws InSufficeientInputException {
		logger.info("******* The control inside of the getFlatsNamesBySite  in  FlatRestService ********");
		List<DropDownPojo> flatsNames = flatService.getFlatsNamesBySite(dropDownRequest);
		Result result=new Result();
		result.setResponseObjList(flatsNames);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/flatBlock.spring")
	public Result getFlatsNamesByBlock(@NonNull DropDownRequest dropDownRequest) throws InSufficeientInputException {
		logger.info("******* The control inside of the getFlatsNamesBySite  in  FlatRestService ********");
		List<DropDownPojo> flatsNames = flatService.getFlatsNamesByBlock(dropDownRequest);
		Result result=new Result();
		result.setResponseObjList(flatsNames);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/flatsbua.spring")
	public Result getFlatsNamesBySbuaSeries(@NonNull DropDownRequest dropDownRequest) throws InSufficeientInputException {
		logger.info("**** The control inside of the getFlatsNamesBySbuaSeriesFacingBhkType  in  FlatRestService ****");
		List<DropDownPojo> flatsNames = flatService.getFlatsNamesBySbuaSeries(dropDownRequest);
		Result result=new Result();
		result.setResponseObjList(flatsNames);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		return result;
	}
	
	@POST
	@Path("/getCustomerDetailsByFlatId.spring")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnoreProperties
	public Result getCustomerDetailsByFlatId(@NonNull Customer customer) {
		logger.info("******* The control inside of the getCustomerDetailsByFlatId  in  FlatRestService ********");
		Result result=new Result();
		List<CustomerPropertyDetailsPojo> customerPropertyDetails = flatService.getCustomerDetailsByFlatId(customer);
		result.setResponseObjList(customerPropertyDetails);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Path("/getFlats.spring")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnoreProperties
	public Result getFlats(@NonNull FlatRequest flatRequest) {
		Result result=new Result();
		logger.info("*** The control inside of the getFlats in FlatRestService ***");
		FlatResponse flatResponse = flatService.getFlats(flatRequest);
		result.setResponseObjList(flatResponse);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
	
	@POST
	@Path("/getBookingFlats.spring")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JsonIgnoreProperties
	public Result getBookingFlats(@NonNull FlatRequest flatRequest) {
		Result result=new Result();
		logger.info("*** The control inside of the getBookingFlats in FlatRestService ***");
		FlatResponse flatResponse = flatService.getBookingFlats(flatRequest);
		result.setResponseObjList(flatResponse);
		result.setResponseCode(HttpStatus.success.getResponceCode());
		result.setDescription(HttpStatus.success.getDescription());
		return result;
	}
}
