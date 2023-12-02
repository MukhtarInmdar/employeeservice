package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class NotificationDetailChangesInfo {
	private Long notificationDetChangesId;
	private Long notificationId;
	private Long sendType;
	private Long actual;
	private Long modifiedTo;
	private Long status;
	private Long modifiedBy;
	private Long createdBy;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
}
