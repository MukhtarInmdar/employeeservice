/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.enums.ServiceRequestEnum;

import lombok.Data;

/**
 * @author Venkat_Koniki
 * @since 14.09.2020
 * @time 05:11PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MessengerRequest implements Serializable{

	private static final long serialVersionUID = 4171222515534574256L;
	private List<Long> siteIds;
	private List<Long> blockIds;
	private List<Long> floorIds;
	private List<Long> flatIds;
	private Timestamp startDate;
	private Timestamp endDate;
	private String requestUrl;
	private Long flatId;
	private String subject;
	private String message;
	private List<FileInfo> files;
	private Long employeeId;
	private Long flatBookingId;
	private Long messengerId;
	private Long createdById;
	private Long createdByType;
	private Long sendType;
	private Long sendTo;
	private List<Long> employeeIds;
	private Long messengerConversationId;
	private Map<String, Map<Long, Long>> reciepientMap;
	//private Map<Long, Map<Long, Long>> managementMap;
	private Long recipientType;
	private Long recipientId;
	private Boolean flag;
	private List<Long> deptIds;
	private String type;
	private Set<Long> conversationIds;
	private List<String> googleDriveLinks;
	private List<Long> messengerIds;
	private Long pageNo;
	private Long pageSize;
	private Long rowCount;
	private ServiceRequestEnum requestEnum;
	private String chatMsgWithoutTags;
	private String requestType;
	private List<ExcelObject> ExcelObject;
	private String flatNo;
	private String appStatus;
	private String flatStatus;
}
