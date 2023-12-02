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
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;


/**
 * This class is responsible for Handling is more generic family of runtime
 * exceptions, in order to abstract away any specific underlying spring jdbc
 * template database implementation.
 * 
 * @author Venkat_Koniki
 * @since 24.04.2019
 * @time 3:33PM
 */
@Provider
public class DataAccessExceptionHandler implements ExceptionMapper<DataAccessException> {

	private final Logger logger = Logger.getLogger(DataAccessExceptionHandler.class);

	@Override
	public Response toResponse(DataAccessException exception) {

		logger.info("**** The control is inside the DataAccessExceptionHandler *****"+exception.getStackTrace());
		logger.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
		
		System.out.println(exception instanceof BadSqlGrammarException);
		if(exception instanceof CannotGetJdbcConnectionException) {
			errors.add("Server internet connection failed, Please try again later...!");
		} else if(exception instanceof DataIntegrityViolationException) {
			logger.info(exception.getMessage());
			errors.add("Error occured while saving some records to the database plz try again once !");
		} else if(exception instanceof BadSqlGrammarException) {
			errors.add("Error occured while loading some records from the database plz try again once !");
		}else if(exception instanceof RecoverableDataAccessException) {
			errors.add("Server internet connection failed, Please try again later...!");
		} else {
			//errors.add(exception.getMessage());
			logger.error("**** The Exception detailed informtion is ****" , exception);
			errors.add("Internal server error, Please try again later...!");
		}
		Response response = Response.status(Status.OK)
				.entity(new Result(HttpStatus.DataAccessException.getResponceCode(),
						HttpStatus.DataAccessException.getDescription(), errors,exception.toString()))
				.type(MediaType.APPLICATION_JSON).build();
		logger.info("*** The Response object while sending response is ***" + response);
		return response;

	}

}

