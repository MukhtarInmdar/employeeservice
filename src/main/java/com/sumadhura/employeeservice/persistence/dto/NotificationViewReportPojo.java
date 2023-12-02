package com.sumadhura.employeeservice.persistence.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class NotificationViewReportPojo {

	@Column (name = "NOTIFICATION_ID")
	private Long notificationId;
	
	@Column (name = "SEND_TYPE")
	private Long sendType;
	
//	@Column (name = "ACTUAL")
//	private String actual;
	
	@Column (name = "SITE_NAME")
	private String siteName;
	
	@Column (name = "MESSAGE")
	private String message;
	
	@Column (name = "DESCRIPTION")
	private String description;
	
	@Column (name = "NOTIFICATIONTEXT")
	private String notificationText;
	
	@Column (name = "OS_TYPE")
	private String osType;
	
	
	@Column (name = "NOTIFICATION_TYPE")
	private String notificationType;
	
	@Column (name = "NUMBER_OF_FLATS_TO_SENT")
	private Long numOfFlatsToBeSent;
	
	@Column (name = "RECIEVED_COUNT")
	private Long recievedCount;
	
	@Column (name = "VIEW_COUNT")
	private Long viewCount;
	
	@Column (name = "STATE_NAME")
	private String stateName;
	
	
	@Column (name = "SEND_TO")
	private Long SEND_TO;
	
	@Column (name = "NON_OF_DEVICES_TO_SENT")
	private Long numOfDevicesToSent;
	
	private List<NotificationViewDetailsListPojo>  companyNotificationViewDetails;
	
	private List<NotificationViewDetailsListPojo>  notificationViewDetails;
}
