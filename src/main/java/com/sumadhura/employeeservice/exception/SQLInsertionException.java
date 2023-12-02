/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when we receiving an an error while
 * Inserting records into the database.
 * 
 * @author Venkat_Koniki
 * @since 24.04.2019
 * @time 1:02PM
 */
public final class SQLInsertionException extends Exception implements Serializable {

	private static final long serialVersionUID = 3325901703723525114L;

	private List<String> messages;
	private String errorMsg;

	public SQLInsertionException() {
		super();
	}

	public SQLInsertionException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public SQLInsertionException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}

	public SQLInsertionException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}

	public String getMessage() {
		return this.errorMsg;
	}

}
