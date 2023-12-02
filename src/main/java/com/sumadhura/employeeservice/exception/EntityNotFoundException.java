/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import org.springframework.dao.DataAccessException;

/**
 * This class is responsible for Thrown when we receiving an an error while
 * mapping resultset object with the bean .
 * The Pojo is not annotated with @Entity .
 * 
 * @author Venkat_Koniki
 * @since 27.04.2019
 * @time 10:49AM
 */
public class EntityNotFoundException extends DataAccessException {

	
	private static final long serialVersionUID = 8042778687174700094L;

	public EntityNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public EntityNotFoundException(String msg) {
		super(msg);
	}

}
