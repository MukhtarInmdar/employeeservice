package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFinancialServiceException extends Exception implements Serializable{
 
	private static final long serialVersionUID = 6724483167103839500L;

	private List<String> messages;
	@SuppressWarnings("unused")
	private String errorMsg;

	public EmployeeFinancialServiceException() {
		super();
	}
	public EmployeeFinancialServiceException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}
	
	public EmployeeFinancialServiceException(List<String> errorMsgs) {
		super();
		this.messages = errorMsgs;
	}
	
	public EmployeeFinancialServiceException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}
	
}
