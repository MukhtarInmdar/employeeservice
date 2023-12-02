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
import com.sumadhura.employeeservice.exception.TicketAssignFailedException;

/**
 * This class is responsible for Handling the TicketAssignFailedException.
 * 
 * @author Venkat_Koniki
 * @since 10.05.2019
 * @time 07:20PM
 */
@Provider
public class TicketAssignFailedExceptionHandler implements ExceptionMapper<TicketAssignFailedException> {

	private final Logger logger = Logger.getLogger(TicketAssignFailedExceptionHandler.class);
	
	@Override
	public Response toResponse(TicketAssignFailedException exception) {
		logger.info("**** The control is inside the TicketAssignFailedExceptionHandler *****");
		logger.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
		//errors.addAll(exception.getMessages());
		errors.add("Internal server error, Please try again later...!");
		Response response = Response.status(Status.OK).entity(new Result(HttpStatus.ticketAssignFailed.getResponceCode(),
				HttpStatus.ticketAssignFailed.getDescription(), errors,exception.toString())).type(MediaType.APPLICATION_JSON).build();
		logger.info("*** The Response object while sending response is ***"+response);
		return response;
	}

}
