/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Data;

/**
 * NotificationRequest class provides Employee Notification specific properties.
 * 
 * @author Venkat_Koniki
 * @since 03.05.2019
 * @time 05:46PM
 */

@Data
public class NotificationRequestInfo {

	private String message;
	private String description;
	private Long typeOf;
	private String imgLoc;
	private Long employeeId;
	private Long genericId;
	private Long deviceId;
	private String requestUrl;
	private String email;
	private String mobile;
	private List<String> osTypes;
}
