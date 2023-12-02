package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

/**
 * Result class is parent class for bean in EmployeeService.
 * 
 * @author Venkat_Koniki
 * @since 22.04.2019
 * @time 10:20AM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public  class Result implements Serializable {

	private static final long serialVersionUID = -3689983897738030831L;
	Integer responseCode;
	String description;
	List<String> errors;
	String sessionKey;
	private Object responseObjList;
	private List<TicketEscalationResponse> ticketEscalationResponses;
	private Timestamp startDate;
	private Timestamp endDate;
	private String internalExcetion;
	private Object ObjList;
	
	
	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Result() {
		super();
	}

	public Result(int responseCode, String description, List<String> errors) {
		super();
		this.responseCode = responseCode;
		this.description = description;
		this.errors = errors;
	}
	public Result(int responseCode, String description, List<String> errors,String internalExcetion) {
		super();
		this.responseCode = responseCode;
		this.description = description;
		this.errors = errors;
		this.internalExcetion = internalExcetion;
	}

	/**
	 * @return the responseCode
	 */
	public Integer getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	/**
	 * @return the sessionKey
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * @return the responseObjList
	 */
	public Object getResponseObjList() {
		return responseObjList;
	}

	/**
	 * @param responseObjList the responseObjList to set
	 */
	public void setResponseObjList(Object responseObjList) {
		this.responseObjList = responseObjList;
	}

	public List<TicketEscalationResponse> getTicketEscalationResponses() {
		return ticketEscalationResponses;
	}

	public void setTicketEscalationResponses(List<TicketEscalationResponse> ticketEscalationResponses) {
		this.ticketEscalationResponses = ticketEscalationResponses;
	}

	public String getInternalExcetion() {
		return internalExcetion;
	}

	public void setInternalExcetion(String internalExcetion) {
		this.internalExcetion = internalExcetion;
	}

}
