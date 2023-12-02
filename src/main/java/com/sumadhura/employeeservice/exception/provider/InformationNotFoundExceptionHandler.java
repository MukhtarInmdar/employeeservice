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
import org.springframework.http.HttpStatus;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.exception.InformationNotFoundException;

/**
 * This class is responsible for Handling the InformationNotFound related exceptions.
 * 
 * @author Venkat_Koniki
 * @since 18.05.2019
 * @time 06:17PM
 */
@Provider
public class InformationNotFoundExceptionHandler implements ExceptionMapper<InformationNotFoundException>{

	private static final Logger LOGGER = Logger.getLogger(InformationNotFoundExceptionHandler.class);
	
	@Override
	public Response toResponse(InformationNotFoundException exception) {
		LOGGER.info("**** The control is inside the InformationNotFoundExceptionHandler *****");
		LOGGER.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
		//errors.add(exception.getMessage());
		errors.add("Internal server error, Please try again later...!");
		Response response = Response.status(Status.OK)
				.entity(new Result(HttpStatus.NO_CONTENT.value(),
						HttpStatus.NO_CONTENT.getReasonPhrase(), errors,exception.toString()))
				.type(MediaType.APPLICATION_JSON).build();
		return response;		
		
	}

}
