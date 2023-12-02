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
 * TicketForwardMenuResponse bean class provides Employee TicketForwardMenuResponse specific properties.
 * 
 * @author Venkat_Koniki
 * @since 10.05.2019
 * @time 02:59PM
 */
@Getter
@Setter
@ToString
public class TicketForwardMenuResponse extends Result implements Serializable {

	private static final long serialVersionUID = 3580379102794686010L;
	private List<TicketForwardMenu> ticketForwardMenuList;
		
}
