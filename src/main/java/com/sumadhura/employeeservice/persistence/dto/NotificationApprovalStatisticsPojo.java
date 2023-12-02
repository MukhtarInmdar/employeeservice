package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class NotificationApprovalStatisticsPojo {
	@Column(name = "NOT_APR_STCS_ID")
	private Long notAprStcsId;
	
	@Column(name = "NOTIFICATION_ID")
	private Long notificationId;
	
	@Column(name = "NOT_APR_LEV_ID")
	private Long notAprLevId;
	
	@Column(name = "COMMENTS")
	private String comments;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
	
}
