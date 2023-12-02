package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NotificationResponse {
	private String multicast_id;
	private Integer success;
	private Integer failure;
	private Integer canonical_ids;
	private List<NotificationStatus> results;
	private List<NotificationRequest> notificationRequests;
	private Boolean isNotificationRequestsAvailable;
	private List<SiteLevelNotifyResponseDTO> siteLevelNotifyResponseDto;
	private Boolean isSiteLevelNotifyResponseDtoAvailable;
	private Long pagecount;
	private List<NotificationApprovalResponse> notificationApprovalResponse;
	private List<NotificationDetailChangesResponse> notificationDetailChangesResonse;
	private String isAppRegisted;
}
