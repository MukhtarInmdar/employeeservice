/**
 * 
 */
package com.sumadhura.employeeservice.service.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.sumadhura.employeeservice.dto.NotificationApprovalResponse;
import com.sumadhura.employeeservice.dto.NotificationRequest;
import com.sumadhura.employeeservice.dto.SiteLevelNotifyRequestDTO;
import com.sumadhura.employeeservice.enums.NotificationSendType;
import com.sumadhura.employeeservice.enums.Status;
import com.sumadhura.employeeservice.persistence.dto.NotificationApprovalLevelsPojo;
import com.sumadhura.employeeservice.persistence.dto.NotificationApprovalPojo;
import com.sumadhura.employeeservice.persistence.dto.NotificationApprovalStatisticsPojo;
import com.sumadhura.employeeservice.persistence.dto.NotificationChangesPojo;
import com.sumadhura.employeeservice.service.dto.NotificationRequestInfo;
import com.sumadhura.employeeservice.util.Util;

/**
 * NotificationMapper class provides  Notification specific mapping.
 * 
 * @author Venkat_Koniki
 * @since 03.05.2019
 * @time 05:43PM
 */
@Component("notificationMapper")
public class NotificationMapper {

	private final  Logger logger = Logger.getLogger(NotificationMapper.class);
	
	public NotificationRequestInfo notificationRequestToNotificationRequestInfo(NotificationRequest notificationRequest) {
		logger.info("**** The control is inside the notificationRequestToNotificationRequestInfo in NotificationMapper *****");
		NotificationRequestInfo notificationRequestInfo = new NotificationRequestInfo();
		BeanUtils.copyProperties(notificationRequestInfo, notificationRequest);
		return notificationRequestInfo;
		
	}

	public NotificationApprovalPojo getNotificationApprovalPojoByNotificationRequest(NotificationApprovalLevelsPojo notificationApprovalLevelsPojo, 
			SiteLevelNotifyRequestDTO notificationRequest, Status status) {
		logger.info("**** The control is inside the getNotificationApprovalPojoByNotificationRequest in NotificationMapper *****");
		NotificationApprovalPojo notificationApprovalPojo = new NotificationApprovalPojo();
		notificationApprovalPojo.setNotificationId(notificationRequest.getId());
		notificationApprovalPojo.setNotAprLevId(notificationApprovalLevelsPojo.getNotAprLevId());
		notificationApprovalPojo.setCreatedBy(notificationRequest.getEmployeeId());
		notificationApprovalPojo.setStatusId(status.getStatus());
		if(Util.isNotEmptyObject(notificationRequest.getComments())) {
			notificationApprovalPojo.setComments(notificationRequest.getComments());
		}
		return notificationApprovalPojo;
	}

	public NotificationApprovalStatisticsPojo getNotificationApprovalStatisticsPojoByNotificationRequest(NotificationApprovalLevelsPojo notificationApprovalLevelsPojo,
			SiteLevelNotifyRequestDTO notificationRequest, Status status) {
		logger.info("**** The control is inside the getNotificationApprovalStatisticsPojoByNotificationRequest in NotificationMapper *****");
		NotificationApprovalStatisticsPojo notificationApprovalStatisticsPojo = new NotificationApprovalStatisticsPojo();
		notificationApprovalStatisticsPojo.setNotificationId(notificationRequest.getId());
		notificationApprovalStatisticsPojo.setNotAprLevId(notificationApprovalLevelsPojo.getNotAprLevId());
		notificationApprovalStatisticsPojo.setCreatedBy(notificationRequest.getEmployeeId());
		notificationApprovalStatisticsPojo.setStatusId(status.getStatus());
		if(Util.isNotEmptyObject(notificationRequest.getComments())) {
			notificationApprovalStatisticsPojo.setComments(notificationRequest.getComments());
		}
		return notificationApprovalStatisticsPojo;
	}

	public List<NotificationApprovalResponse> getNotificationApprovalResponseListByNotificationApprovalPojoList(List<NotificationApprovalPojo> notificationApprovalPojoList) {
		logger.info("**** The control is inside the getNotificationApprovalResponseListByNotificationApprovalPojoList in NotificationMapper *****");
		List<NotificationApprovalResponse> notificationApprovalResponseList = new ArrayList<>();
		for(NotificationApprovalPojo notificationApprovalPojo : notificationApprovalPojoList) {
			NotificationApprovalResponse notificationApprovalResponse = new NotificationApprovalResponse();
			BeanUtils.copyProperties(notificationApprovalPojo, notificationApprovalResponse);
			notificationApprovalResponseList.add(notificationApprovalResponse);
		}
		return notificationApprovalResponseList;
	}

	public List<NotificationRequest> getNotificationRequestListByNotificationChagesPojoList(List<NotificationChangesPojo> notificationChangesPojoList, NotificationRequest oldNotificationRequest) {
		logger.info("**** The control is inside the getNotificationRequestListByNotificationChagesPojoList in NotificationMapper *****");
		List<NotificationRequest> notificatoinRequestList = new ArrayList<>();
		NotificationRequest notificationRequest = new NotificationRequest();
		notificationRequest.setId(oldNotificationRequest.getId());
		for(NotificationChangesPojo notificationChangesPojo : notificationChangesPojoList) {
			if(Util.isNotEmptyObject(notificationChangesPojo) && Util.isNotEmptyObject(notificationChangesPojo.getSendType())
				&& Util.isNotEmptyObject(notificationChangesPojo.getActual())){
				
				if(notificationChangesPojo.getSendType().equals(NotificationSendType.MESSAGE.getId())) {
					notificationRequest.setMessage(notificationChangesPojo.getActual());
				}else if(notificationChangesPojo.getSendType().equals(NotificationSendType.DESCRIPTION.getId())) {
					notificationRequest.setDescription(notificationChangesPojo.getActual());
				}else if(notificationChangesPojo.getSendType().equals(NotificationSendType.IMAGE_LOCATION.getId())) {
					notificationRequest.setImgLoc(notificationChangesPojo.getActual());
				}else if(notificationChangesPojo.getSendType().equals(NotificationSendType.FILE_LOCATION.getId())) {
					notificationRequest.setLinkFileLoc(notificationChangesPojo.getActual());
				}else if(notificationChangesPojo.getSendType().equals(NotificationSendType.NOTIFICATION_TEXT.getId())) {
					notificationRequest.setNotificationText(notificationChangesPojo.getActual());
				}else if(notificationChangesPojo.getSendType().equals(NotificationSendType.OS_TYPE.getId())) {
					notificationRequest.setOsType(notificationChangesPojo.getActual());
				}
			}	
		}
		notificatoinRequestList.add(notificationRequest);
		return notificatoinRequestList;
	  }
		

}