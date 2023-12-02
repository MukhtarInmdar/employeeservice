package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class NotificationApprovalResponse {
	private Long notAprId;
	private Long notificationId;
	private Long notAprLevId;
	private String comments;
	private Timestamp createdDate;
	private Long createdBy;
	private Long statusId;
	private String message;
	private String siteName;
	private String createdByName;
}
