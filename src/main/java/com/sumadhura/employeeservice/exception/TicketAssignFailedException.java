/**
 * 
 */
package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Thrown when Ticket is assigning to department
 * situation.
 * 
 * @author Venkat_Koniki
 * @since 10.05.2019
 * @time 07:18PM
 */
public class TicketAssignFailedException extends Exception implements Serializable {

	private static final long serialVersionUID = -6392154371436002870L;

	private List<String> messages;
	private String errorMsg;

	public TicketAssignFailedException() {
		super();
	}

	public TicketAssignFailedException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public TicketAssignFailedException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}

	public TicketAssignFailedException(Throwable t) {
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
