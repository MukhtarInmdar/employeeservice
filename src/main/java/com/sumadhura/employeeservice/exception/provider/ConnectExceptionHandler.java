/**
 * 
 */
package com.sumadhura.employeeservice.exception.provider;

import java.net.ConnectException;
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


/**
 * This class is responsible for Handling the Connection related exceptions.
 * 
 * @author Venkat_Koniki
 * @since 24.04.2019
 * @time 04:04PM
 */
@Provider
public class ConnectExceptionHandler implements ExceptionMapper<ConnectException> {

	private static final Logger logger = Logger.getLogger(ConnectExceptionHandler.class);

	@Override
	public Response toResponse(ConnectException exception) {
		logger.info("**** The control is inside the ConnectExceptionHandler *****");
		logger.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
	//	errors.add(exception.getMessage());
		errors.add("Internal server error, Please try again later...!");
		Response response = Response.status(Status.OK)
				.entity(new Result(HttpStatus.SERVICE_UNAVAILABLE.value(),
						HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), errors,exception.toString()))
				.type(MediaType.APPLICATION_JSON).build();
		return response;

	}
}
