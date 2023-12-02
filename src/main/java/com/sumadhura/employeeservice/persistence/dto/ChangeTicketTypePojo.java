/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * ChangeTicketTypePojo class provides CHANGE_TICKET_TYPE_REQUEST Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 09.06.2020
 * @time 04:25PM
 */

@Entity
@Data
public class ChangeTicketTypePojo {

	@Column(name="ID")
	private Long id;
	
	@Column(name="TICKET_ID")
	private Long ticketId;
	
	@Column(name="REQUESTED_TICKET_TYPE")
	private String requestedTicketType;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="REQUEST_RAISED_BY")
	private Long requestRaisedBy;
	
	@Column(name="STATUS")
	private Long status;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="ACTUAL_TICKET_TYPE")
	private Long actualTicketType;
	
	@Column(name="CHANGED_TICKET_TYPE")
	private Long changedTicketType;
	
	@Column(name="NO_OF_TIME_REQUESTED")
	private Long noOfTimeRequested;
	
	@Column(name="STATUS_UPDATED_BY")
	private Long statusUpdatedBy;
	
}





