package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultBankerException extends Exception implements Serializable{
 
	private static final long serialVersionUID = 6724483167103839500L;

	private List<String> messages;
	@SuppressWarnings("unused")
	private String errorMsg;

	public DefaultBankerException() {
		super();
	}
	public DefaultBankerException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}
	
	public DefaultBankerException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}
	
	public DefaultBankerException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}
	
}
