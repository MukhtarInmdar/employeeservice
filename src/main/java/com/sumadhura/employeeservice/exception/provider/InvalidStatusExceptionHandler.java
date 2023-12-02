/**
 * 
 */
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
import com.sumadhura.employeeservice.exception.InvalidStatusException;

/**
 * This class is responsible for Handling the InvalidStatusException.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 06:00PM
 */
@Provider
public class InvalidStatusExceptionHandler implements ExceptionMapper<InvalidStatusException> {

	
	private final static Logger logger = Logger.getLogger(InvalidStatusExceptionHandler.class);

	 /**
    * Create a new Response for the supplied exception.
    *
    * @param InSufficeientInputException is the exception.
    * @return a new response Object.
    * @throws IllegalArgumentException if status is less than {@code 100} or greater
    *                                  than {@code 599}.
    */
	@Override
	public Response toResponse(InvalidStatusException exception) {
		
		logger.info("**** The Control is inside the InvalidStatusExceptionHandler *****");
		logger.info("**** The Exception informtion is ****"+exception);
		logger.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
	//	errors.addAll(exception.getMessages());
		errors.add("Internal server error, Please try again later...!");
		Response response = Response.status(Status.OK).entity(
				new Result(HttpStatus.invalidStatusCode.getResponceCode(), HttpStatus.invalidStatusCode.getDescription(), errors,exception.toString()))
				.type(MediaType.APPLICATION_JSON).build();
		logger.info("****** Response object ******"+response.readEntity(Result.class));
		return response;
	}
	
	
}
