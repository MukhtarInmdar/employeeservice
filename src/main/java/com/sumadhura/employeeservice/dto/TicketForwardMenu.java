/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * TicketForwardMenu bean class provides Employee Ticket ForwardMenu specific properties.
 * 
 * @author Venkat_Koniki
 * @since 10.05.2019
 * @time 12:53PM
 */
@Getter
@Setter
@ToString
public class TicketForwardMenu implements Serializable{

	private static final long serialVersionUID = 3580379102794686010L;
	
	private Long menuId;
	private String item;
	private Long typeOf;
	private Long genericId;
	private Long statusId;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private Long departmentId;
	

}
