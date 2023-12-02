/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when receiving an Bad Login credentials
 * situation.
 * 
 * @author Venkat_Koniki
 * @since 10.04.2019
 * @time 11:00PM
 */
public class AuthenticationException extends Exception implements Serializable {

	private static final long serialVersionUID = -4948454577108745690L;

	private List<String> messages;

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}

	public AuthenticationException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}

}
