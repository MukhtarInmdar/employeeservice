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
import org.dozer.converters.ConversionException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.UnexpectedRollbackException;

import com.sumadhura.employeeservice.dto.Result;
import com.sumadhura.employeeservice.enums.HttpStatus;


/**
 * This class is responsible for Handling the Global Exceptions(Every
 * exceptions). situation.
 * 
 * @author Venkat_Koniki
 * @since 10.04.2019
 * @time 12:00PM
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

	private final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

	@Override
	public Response toResponse(Throwable exception) {
		logger.info("**** The Control is inside the GlobalExceptionHandler *****");
		logger.info("**** The Exception informtion is ****" + exception.getMessage());
		logger.error("**** The Exception detailed informtion is ****" , exception);
		List<String> errors = new ArrayList<String>();
		//exception.printStackTrace();
		System.out.println(exception instanceof ConversionException);
		if (exception instanceof TransactionSystemException) {
			TransactionSystemException ex = (TransactionSystemException) exception;
			logger.info("***** Control inside the TransactionFailedExceptionHandler.toResponse() *****"+ ex.getOriginalException());
			errors.add("Server Internet Connection Failed, Please try again later...!");
			logger.error("**** The Exception detailed informtion is ****" , exception);
		} else if (exception instanceof UnexpectedRollbackException) {
			errors.add("Your transaction has been failed unexpectedly, Please try again later...!");
		} else if (exception instanceof TransactionTimedOutException) {
			errors.add("Your transaction has been failed, Please try again later...!");
		} else if (exception instanceof CannotCreateTransactionException) {
			errors.add("Server Internet Connection Failed, Please try again later...!");
		}else if (exception instanceof CannotGetJdbcConnectionException) {
			errors.add("Server internet connection failed, Please try again later...!");
		}else if(exception instanceof NullPointerException) {
			errors.add("Internal server error, Please try again later...!");
		}else if(exception instanceof ConversionException) {
			errors.add("Internal data conversion error, Please try again later...!");
		}else if(exception instanceof IllegalArgumentException) {
			errors.add("Internal data conversion error, Please try again later...!");
		}
		
		/*else if (exception instanceof DataIntegrityViolationException) {
			logger.info(exception.getMessage());
			errors.add("Error occured while saving some records to the database plz try again once !");
		} else if (exception instanceof BadSqlGrammarException) {
			errors.add("Error occured while loading some records from the database plz try again once !");
		}*/ else {
			logger.error("**** The Exception detailed informtion is ****" , exception);
			errors.add("Internal server error, Please try again later...!");
		}
		Response response = Response.status(Status.OK).entity(new Result(HttpStatus.exceptionRaisedInFlow.getResponceCode(),
				HttpStatus.exceptionRaisedInFlow.getDescription(), errors,exception.toString())).type(MediaType.APPLICATION_JSON).build();
		logger.info("*** The Response object while sending response is ***"+response);
		return response;
	}

}
