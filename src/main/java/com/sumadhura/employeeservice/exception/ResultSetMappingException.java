/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import org.springframework.dao.DataAccessException;

/**
 * This class is responsible for Thrown when we receiving an an error while
 * mapping resultset object with the bean.
 * 
 * @author Venkat_Koniki
 * @since 24.04.2019
 * @time 1:02PM
 */

public class ResultSetMappingException extends DataAccessException{

	private static final long serialVersionUID = 9071512128526411136L;

	public ResultSetMappingException(String msg) {
		super(msg);
	}
	
	public ResultSetMappingException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
