/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when receiving an OTP is Not Sent.
 * situation.
 * 
 * @author Venkat_Koniki
 * @since 15.04.2019
 * @time 06:00PM
 */
public class OtpException extends Exception implements Serializable{

	private static final long serialVersionUID = -8203620327835379083L;
	private List<String> messages;

	public OtpException() {
		super();
	}

	public OtpException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}

	public OtpException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}

	
	
}
