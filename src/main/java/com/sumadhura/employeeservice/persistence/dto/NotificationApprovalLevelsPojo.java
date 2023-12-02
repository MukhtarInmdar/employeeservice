package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class NotificationApprovalLevelsPojo {
	@Column(name = "NOT_APR_LEV_ID")
	private Long notAprLevId;
	
	@Column(name = "NOT_SET_OF_LEV_ID")
	private Long notSetOfLevId;
	
	@Column(name = "NXT_NOT_SET_OF_LEV_ID")
	private Long nxtNotSetOfLevId;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
}
