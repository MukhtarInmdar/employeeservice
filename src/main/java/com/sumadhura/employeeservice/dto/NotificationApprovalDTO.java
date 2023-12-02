package com.sumadhura.employeeservice.dto;

import lombok.Data;

@Data
public class NotificationApprovalDTO {
	
	private Long notificationId;
	private Long StatusId;
	private Long approvalLevelId;
	private Long notificationApprovalId;

}
