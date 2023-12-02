package com.sumadhura.employeeservice.persistence.dto;

import lombok.Data;

/**
 * 
 * @author rayudu
 *
 */
@Data
public class NotificationDetailsPojo {

	private Long sendTo;
	private Long notificationId;
	private Long sendType;
	private Integer status;
}