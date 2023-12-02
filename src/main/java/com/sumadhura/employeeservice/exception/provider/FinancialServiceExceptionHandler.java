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
import com.sumadhura.employeeservice.exception.EmployeeFinancialServiceException;
import com.sumadhura.employeeservice.util.Util;


/**
 * * This class is responsible for Handling the refund amount exceed exception.
 * situation.
 * @author Aniket Chavan
 * @since 24-02-2020
 * @time 1:25PM
 */

@Provider
public class FinancialServiceExceptionHandler implements ExceptionMapper<EmployeeFinancialServiceException>{
	
	private final static Logger logger = Logger.getLogger(FinancialServiceExceptionHandler.class);

	 /**
     * Create a new Response for the supplied exception.
     *
     * @param InSufficeientInputException is the exception.
     * @return a new response Object.
     * @throws IllegalArgumentException if status is less than {@code 100} or greater
     *                                  than {@code 599}.
     */
	@Override
	public Response toResponse(EmployeeFinancialServiceException exception) {
		
		logger.info(" ***** control inside FinancialServiceExceptionHandler.toResponse() ***** ");
		logger.info("**** The Exception informtion is ****"+exception);
		logger.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
		if(Util.isEmptyObject(exception.getMessages())) {
			errors.add(exception.getMessage());	
		}else {
			errors.addAll(exception.getMessages());
		}
		
		Response response = Response.status(Status.OK).entity(
				new Result(HttpStatus.insufficientInput.getResponceCode(), HttpStatus.insufficientInput.getDescription(), errors))
				.type(MediaType.APPLICATION_JSON).build();
		logger.info("****** Response object ******"+response.readEntity(Result.class));
		return response;
	}
	
}
