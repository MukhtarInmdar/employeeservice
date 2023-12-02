package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class NotificationDetailChangesPojo {
	@Column (name = "NOTIFICATION_DET_CHANGES_ID")
	private Long notificationDetChangesId;
	
	@Column (name = "NOTIFICATION_ID")
	private Long notificationId;
	
	@Column (name = "SEND_TYPE")
	private Long sendType;
	
	@Column (name = "ACTUAL")
	private Long actual;
	
	@Column (name = "MODIFIED_TO")
	private Long modifiedTo;
	
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
