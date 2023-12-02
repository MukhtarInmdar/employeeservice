/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketCommentsPojo class provides TICKET_COMMENTS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 30.04.2019
 * @time 06:15PM
 */

@Entity
@Data
public class TicketCommentsPojo {

	@Column(name="TICKET_COMMENTS_ID")
	private Long ticketCommentId;
	
	@Column(name="TICKET_ID")
	private Long ticketId;
	
	@Column(name="DEPARTMENT_ID")
	private Long departmentId;
	
	@Column(name="FROM_ID")
	private Long fromId;
	
	@Column(name="FROM_TYPE")
	private Long fromType;
	
	@Column(name="TO_ID")
	private Long toId;
	
	@Column(name="TO_TYPE")
	private Long toType;
	
	@Column(name="MESSAGE")
	private String message;
	
	@Column(name="COMMENTS_DATE")
	private Timestamp commentsDate;
	
	@Column(name="VISIBLE_TYPE")
	private String visibleType;
	
	@Column(name="DOCUMENT_LOCATION")
	private String documentLocation;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="TICKET_CONVERSATION_DOC_ID")
	private Long ticketConversationDocumentId;
	
	@Column(name="FROM_DEPT_ID")
	private Long fromDeptId;
	
	@Column(name="TO_DEPT_ID")
	private Long toDeptId;
	
	@Column(name="NAME")
	private String name;
	
}
