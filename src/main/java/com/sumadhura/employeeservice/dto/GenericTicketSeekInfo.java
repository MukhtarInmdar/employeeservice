/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * GenericTicketSeekInfo class provides GenericTicketSeekInfo specific fields.
 * 
 * @author Venkat_Koniki
 * @since 17.05.2019
 * @time 09:54PM
 */
@Data
public class GenericTicketSeekInfo implements Serializable{

	private static final long serialVersionUID = 7664837666689662167L;
	private List<TicketSeekInfo> ticketSeekInfo;
	private String name;
	private Long id;
	private Long requestId;
	private Long type;
	
}
