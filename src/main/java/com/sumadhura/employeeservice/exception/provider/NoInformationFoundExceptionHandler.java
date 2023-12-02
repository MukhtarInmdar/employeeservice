package com.sumadhura.employeeservice.exception.provider;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.exception.NoInformationFoundException;

/**
 * This class is responsible for Handling the NoInformationFoundException related exceptions.
 * 
 * @author Malladi Venkatesh
 * @since 2021.04.15
 * @time 06:17PM
 */

public class NoInformationFoundExceptionHandler implements ExceptionMapper<NoInformationFoundException> {
	
	private static final Logger LOGGER = Logger.getLogger(NoInformationFoundExceptionHandler.class);
	
	@Override
	public Response toResponse(NoInformationFoundException exception) {
		LOGGER.info("**** The control is inside the NoInformationFoundExceptionHandler ****");
		LOGGER.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<>();
	//	errors.add(exception.getMessage());
		errors.add("Internal server error, Please try again later...!");
		return Response.status(Status.OK)
				.entity(new Result(HttpStatus.NO_CONTENT.value(),HttpStatus.NO_CONTENT.getReasonPhrase(),errors,exception.toString()))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
