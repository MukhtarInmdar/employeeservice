/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import com.sumadhura.employeeservice.util.Util;

import lombok.Data;

/**
 * TicketConversationInfo  class provides Info about TicketConversation specific fields.
 * 
 * @author Venkat_Koniki
 * @since 21.08.2019
 * @time 04:58PM
 */

@Data
public class TicketConversationInfo {

	private Long ticketConversationDocumentId;
	private Long fromType;
	private Long fromId;
	private Long fromDeptId;
	
	public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof TicketConversationInfo) {
          final TicketConversationInfo other = (TicketConversationInfo) o;
          final Object this$ticketConversationDocumentId = this.ticketConversationDocumentId;
          final Object other$ticketConversationDocumentId = other.ticketConversationDocumentId;
          return Util.isNotEmptyObject(this$ticketConversationDocumentId)&&Util.isNotEmptyObject(other$ticketConversationDocumentId)?this$ticketConversationDocumentId.equals(other$ticketConversationDocumentId)?true:false:false;
        }else {
           return false;
        }
    }
	public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object ticketConversationDocumentId = this.ticketConversationDocumentId;
        result = result * PRIME + (ticketConversationDocumentId == null ? 43 : ticketConversationDocumentId.hashCode());
        return result;
    }
	
}
