package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class NotificationSetOfLvlEmpMapPojo {
	@Column(name = "NOT_SET_OF_LVL_EMP_MAP_ID")
	private Long notSetOfLvlEmpMapId;
	
	@Column(name = "EMP_ID")
	private Long empId;
	
	@Column(name = "NOT_SET_OF_LEV_ID")
	private Long notSetOfLevId;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
}
