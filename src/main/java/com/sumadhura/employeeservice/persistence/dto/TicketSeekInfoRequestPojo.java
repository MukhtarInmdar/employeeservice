/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TicketSeekInfoRequestPojo class provides  TicketSeekInfoRequestPojo specific properties.
 * 
 * @author Venkat_Koniki
 * @since 15.05.2019
 * @time 06:56PM
 */

@Entity
@Setter
@Getter
@ToString
public class TicketSeekInfoRequestPojo {

	@Column(name="TICKET_SEEKINFO_REQUEST_ID")
	private Long ticketSeekInfoRequestId;

	@Column(name="TICKET_ID")
	private Long ticketId;
	
	@Column(name="FROM_ID")
	private Long fromId;

	@Column(name="FROM_TYPE")
	private Long fromType;
	
	@Column(name="FROM_DEPT_ID")
	private Long fromDeptId;

	@Column(name="TO_ID")
	private Long toId;

	@Column(name="TO_TYPE")
	private Long toType;
	
	@Column(name="TO_DEPT_ID")
	private Long toDeptId;

	@Column(name="STATUS_ID")
	private Long statusId;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
}
