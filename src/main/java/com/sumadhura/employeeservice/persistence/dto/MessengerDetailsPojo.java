/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;


import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumadhura.employeeservice.dto.FileInfo;
import com.sumadhura.employeeservice.util.Util;

import lombok.Data;

/**
 * Messenger pojo provides specific properties wto Messenger.
 * 
 * @author Venkat_Koniki
 * @since 16.09.2020
 * @time 11:34PM
 */

@Entity
@Data
public class MessengerDetailsPojo implements Comparable<MessengerDetailsPojo>{
	
	@Column(name="MESSENGER_ID")
	private Long messengerId;
	
	@Column(name="MESSAGE_ID")
	private String messageId;
	
	@Column(name="CREATED_BY_ID")
	private Long createdById;
	
	@Column(name="CREATED_BY_TYPE")
	private Long createdByType;
	
	@Column(name="STATUS")
	private Long status;
	
	@Column(name="MESSANGER_STATUS")
	private String messengerStatus;
	
	@Column(name="SEND_TO")
	private Long sendTo;
	
	@Column(name="SEND_TYPE")
	private Long sendType;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="LAST_CHATTED_DATE")
	private Timestamp lastChattedDate;
	
	@Column(name="CONV_CREATED_DATE")
	private Timestamp conversationCreatedDate;
	
	@Column(name="SUBJECT")
	private String subject;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookingId;
	
	@Column(name="CUST_ID")
	private Long custId;
	
	@Column(name="CUST_NAME")
	private String customerName;
	
	@Column(name="FLAT_ID")
	private Long flatId;
	
	@Column(name="FLAT_NO")
	private String flatName;
	
	@Column(name="FLOOR_NAME")
	private String floorName;
	
	@Column(name="BLOCK_NAME")
	private String blockName;
	
	@Column(name="SITE_NAME")
	private String siteName;
	
	@Column(name="MESSENGER_CONVERSATION_ID")
	private Long messengerConversationId;

	@Column(name="CREATED_BY_NAME")
	private String createdByName;
	
	@Column(name="CHAT_MSG")
	private String messege;

	@Column(name="CHAT_CREATED_BY")
    private Long chatCreatedBy;

	@Column(name="CHAT_CREATED_BY_TYPE")
	private Long chatCreatedByType;

	@Column(name="VIEW_STATUS")
    private Long viewStatus;

	@Column(name="LOCATION")
    private String location;
	
	@Column(name="ID")
    private Long id;
	
	@Column(name="LEVELID")
    private Long levelId;
	
	@Column(name="SITE_ID")
    private Long siteId;
	
	@Column(name="DEPT_ID")
    private Long deptId;
	
	@Column(name="EMP_ID")
    private Long employeeId;
	
	@Column(name="EMP_NAME")
    private String employeeName;
	
	@Column(name="DEPT_NAME")
    private String departmentName;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="VIEW_COUNT")
	private Long viewCount;
	
	@Column(name="CHAT_MSG_WITHOUT_TAGS")
	private String chatMsgWithoutTags;
	
	private List<FileInfo> fileList;
	
	private String chatType;
	
	private Long customerViewStatus;
	
	private List<MessengerDetailsPojo> messengerDetailsPojos;
	
	@Column(name="APP_STATUS")
	private String appStatus;
	
	@Column(name="VIEW_DETAILS")
    private String viewDetails;
	
	@Override
	public int compareTo(MessengerDetailsPojo o) {
		if(Util.isNotEmptyObject(o.getMessengerConversationId()) && Util.isNotEmptyObject(getMessengerConversationId())
			&&	Util.isNotEmptyObject(o.getCreatedDate())  && Util.isNotEmptyObject(getCreatedDate())){
			return (this.getMessengerConversationId().compareTo(o.getMessengerConversationId()));
		}
		 return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MessengerDetailsPojo)) {
			return false;
		}
		MessengerDetailsPojo other = (MessengerDetailsPojo) obj;
		return Objects.equals(messengerConversationId, other.messengerConversationId);
	}


	@Override
	public int hashCode() {
		return Objects.hash(messengerConversationId);
	}
   
}
