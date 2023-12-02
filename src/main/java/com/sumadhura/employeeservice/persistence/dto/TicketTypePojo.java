/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketTypePojo class provides TicketType specific fields.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 1:20PM
 */

@Entity
@Data
public class TicketTypePojo {

	@Column(name = "TICKET_TYPE_ID")
	private Long ticketTypeId;
	
	@Column(name = "NAME")
	private String ticketType;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "RESOLUTION_DAY_TIME")
	private Float resolutionDayTime;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
}
