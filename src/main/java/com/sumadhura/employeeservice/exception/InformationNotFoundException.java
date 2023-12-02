/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when receiving an Information is NotFound 
 * situation.
 * 
 * @author Venkat_Koniki
 * @since 18.05.2019
 * @time 06:25PM
 */
public class InformationNotFoundException extends Exception implements Serializable {

	private static final long serialVersionUID = -5834064194606053169L;
	
	private List<String> messages;
	private String errorMsg;

	public InformationNotFoundException() {
		super();
	}

	public InformationNotFoundException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public InformationNotFoundException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}

	public InformationNotFoundException(Throwable t) {
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
