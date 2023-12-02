package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class NotificationChangesResponse {
	private Long notificationChangesId;
	private Long notificationId;
	private Long sendType;
	private String actual;
	private String modifiedTo;
	private Long status;
	private Long modifiedBy;
	private Long createdBy;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
}
