package com.sumadhura.employeeservice.exception;

import java.util.ArrayList;
import java.util.List;

public class SupplierServiceException extends Exception {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6559572452635198462L;

	protected List<String> messages;

	public SupplierServiceException(List<String> messages) {
		super();
		this.messages = messages;
	}

	public SupplierServiceException(Throwable t) {
		super();

		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());

		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}
}