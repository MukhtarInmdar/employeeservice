/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * TicketConversationDocumentsPojo class provides TicketConversationDocument specific properties.
 * 
 * @author Venkat_Koniki
 * @since 29.04.2019
 * @time 07:36AM
 */

@Entity
@Data
public class TicketConversationDocumentsPojo {

	@Column(name="TICKET_ESCALATION_ID")
	private Long ticketConversationDocumentId;
	
	@Column(name="DOCUMENTS_LOCATION")
	private String documentsLocation;
	
	@Column(name="VISIBLE_TYPE")
	private String visibleType;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
   @Column(name="PUBLIC_DOCUMENTS")
   private String publicDocuments;
   
   @Column(name ="EXTERNAL_DRIVE_FILE_LOCATION")
   private String externalDriveFileLocation;
	
}
