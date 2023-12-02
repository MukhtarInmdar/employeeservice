/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TicketSeekInfoWrapper class provides TicketSeekInfoWrapper specific fields.
 * 
 * @author Venkat_Koniki
 * @since 18.05.2019
 * @time 06:54PM
 */
@Getter
@Setter
@ToString
public class TicketSeekInfoWrapper extends Result implements Serializable{

	private static final long serialVersionUID = 8427745529586344673L;
	private List<GenericTicketSeekInfo> genericTicketSeekInfos ;
	private TicketResponse ticketResponse; 
	
}
