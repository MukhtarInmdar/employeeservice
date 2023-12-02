package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.sumadhura.employeeservice.util.Util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class MprResponse {
	private Long mprId;
	private String mprName;
	private String remarks;
	private Long type;
	private Long typeId;
	private Timestamp createdDate;
	private Long createdBy;
	private Timestamp modifiedDate;
	private Long modifiedBy;
	private Long statusId;
	private String typeName;
	private List<MprDocumentsResponse> mprDocumentsResponseList;
	private String multicast_id;
	private Integer success;
	private Integer failure;
	private Integer canonical_ids;
	private List<NotificationRequest> notificationRequests;
	private Boolean isNotificationRequestsAvailable;
	private List<SiteLevelNotifyResponseDTO> siteLevelNotifyResponseDto;
	private Boolean isSiteLevelNotifyResponseDtoAvailable;
	private Long pagecount;
	private List<NotificationApprovalResponse> notificationApprovalResponse;
	private List<NotificationDetailChangesResponse> notificationDetailChangesResonse; 
	private String isViewed;
	private Long appRegId;
	private Long deviceViewStatus;
	private List<MprViewDetails> devices = new ArrayList<MprViewDetails>();
	
	@Override
	public boolean equals(Object object) {
		if(Util.isNotEmptyObject(object) && this.getClass()!=object.getClass()) {
			return false;
		}else if(this == object) {
			return true;
		}else if(object instanceof MprResponse) {
			MprResponse mprResponse = (MprResponse) object;
			return Objects.equals(this.getMprId(), mprResponse.getMprId());
		}else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getMprId());
	}
}
