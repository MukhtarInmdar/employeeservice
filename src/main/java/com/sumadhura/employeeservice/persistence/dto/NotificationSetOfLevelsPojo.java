package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class NotificationSetOfLevelsPojo {
	@Column(name = "NOT_SET_OF_LEV_ID")
	private Long notSetOfLevId;
	
	@Column(name = "LEVEL_ID")
	private Long levelId;
	
	@Column(name = "TYPE")
	private Long type;
	
	@Column(name = "TYPE_ID")
	private Long typeId;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
}
