package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when receiving an Information is NotFound 
 * situation.
 * 
 * @author Malladi Venkatesh
 * @since 2021-.04-15
 * @time 06:25PM
 */
public class NoInformationFoundException extends Exception implements Serializable{

	private static final long serialVersionUID = -1429621299118722437L;
	
	private List<String> messages;
	private String errorMsg;

	public NoInformationFoundException() {
		super();
	}

	public NoInformationFoundException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public NoInformationFoundException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}

	public NoInformationFoundException(Throwable t) {
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
