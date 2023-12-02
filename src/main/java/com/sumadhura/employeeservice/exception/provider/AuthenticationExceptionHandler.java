/**
 * 
 */
package com.sumadhura.employeeservice.exception.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;
import com.sumadhura.employeeservice.exception.AuthenticationException;


/**
 * This class is responsible for Handling the AuthenticationException.
 * situation.
 * 
 * @author Venkat_Koniki
 * @since 10.04.2019
 * @time 11:00PM
 */
@Provider
public class AuthenticationExceptionHandler implements ExceptionMapper<AuthenticationException> {

	private final static Logger logger = Logger.getLogger(AuthenticationExceptionHandler.class);

	@Override
	public Response toResponse(AuthenticationException exception) {
		logger.info("**** The Control is inside the AuthenticationExceptionHandler *****");
		logger.info("**** The Exception informtion is ****" + exception);
		logger.error("**** The Exception detailed informtion is ****" , exception);
		return Response.status(Status.OK)
				.entity(new Result(HttpStatus.authenticationError.getResponceCode(),
						HttpStatus.authenticationError.getDescription(), HttpStatus.authenticationError.getErrorMsgs(),exception.toString()))
				.type(MediaType.APPLICATION_JSON).build();
	}

}
