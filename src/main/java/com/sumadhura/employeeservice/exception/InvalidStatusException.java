/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when receiving an Invalid Status
 * situation.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 1:25PM
 */
public class InvalidStatusException extends Exception implements Serializable {

	private static final long serialVersionUID = -562154358591934705L;

	private List<String> messages;
	private String errorMsg;

	public InvalidStatusException() {
		super();
	}

	public InvalidStatusException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public InvalidStatusException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}

	public InvalidStatusException(Throwable t) {
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
