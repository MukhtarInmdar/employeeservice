/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

/**
 * EmployeeTicketResponseWrapper bean class provides Employee Ticket specific properties.
 * 
 * @author Venkat_Koniki
 * @since 27.04.2019
 * @time 12:00PM
 */
public class EmployeeTicketResponseWrapper extends Result implements Serializable{

	private static final long serialVersionUID = 3106207650432795279L;
	
	private List<EmployeeTicketResponse> employeeTicketResponseList ;

	/**
	 * @return the employeeTicketResponseList
	 */
	public List<EmployeeTicketResponse> getEmployeeTicketResponseList() {
		return employeeTicketResponseList;
	}

	/**
	 * @param employeeTicketResponseList the employeeTicketResponseList to set
	 */
	public void setEmployeeTicketResponseList(List<EmployeeTicketResponse> employeeTicketResponseList) {
		this.employeeTicketResponseList = employeeTicketResponseList;
	}
	
	

}
