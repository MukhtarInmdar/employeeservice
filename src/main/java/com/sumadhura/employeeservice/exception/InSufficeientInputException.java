package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when receiving an Insufficient Input situation.
 * 
 * @author Venkat_Koniki
 * @since 08.04.2019
 * @time 1:25PM
 */

public final class InSufficeientInputException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 1164875410872559054L;
	
	private List<String> messages;

	public InSufficeientInputException() {
		super();
	}
	
	public InSufficeientInputException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}
	
	public InSufficeientInputException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}
	
}
