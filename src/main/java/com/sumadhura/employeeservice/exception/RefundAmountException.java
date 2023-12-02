package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RefundAmountException extends RuntimeException implements Serializable{
 
	private static final long serialVersionUID = 6724483167103839500L;

	private List<String> messages;

	public RefundAmountException() {
		super();
	}
	
	public RefundAmountException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}
	
	public RefundAmountException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}
}
