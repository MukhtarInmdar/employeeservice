/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketSeekInfoPojo class provides Employee Ticket specific properties.
 * 
 * @author Venkat_Koniki
 * @since 14.05.2019
 * @time 09:30PM
 */

@Entity
@Data
public class TicketSeekInfoPojo {
	
	@Column(name="TICKET_SEEK_INFO_ID")
	private Long ticketSeekInfoId;
	@Column(name="TICKET_ID")
	private Long ticketId;
	@Column(name="FROM_DEPT_ID")
	private Long fromDeptId;
	@Column(name="FROM_ID")
	private Long fromId;
	@Column(name="FROM_TYPE")
	private Long fromType;
	@Column(name="TO_ID")
	private Long toId;
	@Column(name="TO_TYPE")
	private Long toType;
	@Column(name="VISIBLE_TYPE")
	private String visibleType;
	@Column(name="DOCUMENT_LOCATION")
	private String documentLocation;
	@Column(name="CHAT_DATE")
	private Timestamp chatDate;
	@Column(name="TO_DEPT_ID")
	private Long toDeptId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="TICKET_CONVERSATION_DOC_ID")
	private Long ticketConversationDocId;
	@Column(name="TICKET_SEEKINFO_REQUEST_ID")
	private Long ticketSeekInfoRequestId;
	@Column(name="MESSAGE")
	private String message;
		
}
