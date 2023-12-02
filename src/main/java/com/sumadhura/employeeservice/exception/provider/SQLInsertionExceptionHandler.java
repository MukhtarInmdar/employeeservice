package com.sumadhura.employeeservice.exception.provider;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.SQLInsertionException;


/**
 * This class is responsible for Handling the SQLInsertionException.
 * 
 * @author Venkat_Koniki
 * @since 24.04.2019
 * @time 1:20PM
 */
@Provider
public class SQLInsertionExceptionHandler implements ExceptionMapper<SQLInsertionException> {

	private final Logger logger = Logger.getLogger(SQLInsertionExceptionHandler.class);
	
	@Override
	public Response toResponse(SQLInsertionException exception) {

		logger.info("**** The control is inside the SQLInsertionExceptionHandler *****");
		logger.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
	//	errors.addAll(exception.getMessages());
		errors.add("Internal server error, Please try again later...!");
		Response response = Response.status(Status.OK).entity(new Result(HttpStatus.SQLInsertionException.getResponceCode(),
				HttpStatus.SQLInsertionException.getDescription(), errors,exception.toString())).type(MediaType.APPLICATION_JSON).build();
		logger.info("*** The Response object while sending response is ***"+response);
		return response;
		
	}

}
