package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class NotificationChangesPojo {
	@Column (name = "NOTIFICATION_CHANGES_ID")
	private Long notificationChangesId;
	
	@Column (name = "NOTIFICATION_ID")
	private Long notificationId;
	
	@Column (name = "SEND_TYPE")
	private Long sendType;
	
	@Column (name = "ACTUAL")
	private String actual;
	
	@Column (name = "MODIFIED_TO")
	private String modifiedTo;
	
	@Column (name = "STATUS")
	private Long status;
	
	@Column (name = "MODIFIED_BY")
	private Long modifiedBy;
	
	@Column (name = "CREATED_BY")
	private Long createdBy;
	
	@Column (name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column (name = "MODIFIED_DATE")
	private Timestamp modifiedDate;

}
