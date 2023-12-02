/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;


import lombok.Data;

/**
 * TicketSeekInfo class provides TicketSeekInfo specific fields.
 * 
 * @author Venkat_Koniki
 * @since 17.05.2019
 * @time 09:54PM
 */
@Data
public class TicketSeekInfo implements Serializable {

	private static final long serialVersionUID = -8908252571882953275L;
	private Long ticketSeekInfoId;
	private Long ticketId;
	private Long fromDeptId;
	private Long fromId;
	private Long fromType;
	private Long toId;
	private Long toType;
	private String visibleType;
	private Timestamp chatDate;
	private Long toDeptId;
	private Long statusId;
	private Timestamp createdDate;
	private Long ticketConversationDocId;
	private Long ticketSeekInfoRequestId;
	private String message;
	private String typeOfMessage;
	private String employeeName;
	private String employeeProfilePic;
		
}
