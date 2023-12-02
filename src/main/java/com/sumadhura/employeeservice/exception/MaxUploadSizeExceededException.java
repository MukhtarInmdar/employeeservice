package com.sumadhura.employeeservice.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author @NIKET CH@V@N
 * @Description custom exception class for throwing an exception when file size exceeds 1 MB size
 * @Date 14-02-2020
 * @Time 11:30
 *
 */
public class MaxUploadSizeExceededException extends Exception implements Serializable {

	private static final long serialVersionUID = 7615593371665670108L;

	private List<String> messages;

	public MaxUploadSizeExceededException() {
		super();
	}

	public MaxUploadSizeExceededException(List<String> messages) {
		super();
		this.messages = messages;
	}

	public MaxUploadSizeExceededException(Throwable t) {
		super();
		List<String> messages = new ArrayList<String>();
		messages.add(t.getMessage());
		this.messages = messages;
	}

	public List<String> getgetMessage() {
		return messages;
	}
}
