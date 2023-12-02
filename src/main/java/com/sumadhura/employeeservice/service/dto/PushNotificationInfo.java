/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import org.json.JSONObject;

import com.sumadhura.employeeservice.dto.ApptmtBookingsDetailResponse;
import com.sumadhura.employeeservice.dto.EmployeeFinancialPushNotificationInfo;
import com.sumadhura.employeeservice.dto.ReferedCustomer;

import lombok.Data;

/**
 * PushNotificationInfo class provides PushNotificationInfo specific properties.
 * 
 * @author Venkat_Koniki
 * @since 22.05.2019
 * @time 12:46PM
 */
@Data
public class PushNotificationInfo {
	
	private String deviceToken;
	private String notificationTitle;
	private String notificationImage;
	private String notificationPriority;
	private String notificationIcon;
	private String notificationStyle;
	private String notificationPicture;
	private String notificatioTitleColor;
	private String notificationType;
	private String notificationSound;
	private JSONObject payload;
	private String osType;
	private String notificationMessage;
	private Long ticketId;
	private Long statusId;
	private ReferedCustomer referedCustomer;
	private EmployeeFinancialPushNotificationInfo financialPushNotification;
	private ApptmtBookingsDetailResponse apptmtBookingsDetailResponse;
	private String typeMsg;
	private List<String> deviceTokens;
}


