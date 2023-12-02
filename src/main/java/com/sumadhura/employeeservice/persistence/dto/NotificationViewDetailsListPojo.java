package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class NotificationViewDetailsListPojo {

	
	@Column (name = "OS_TYPE")
	private String osType;
	
	@Column (name = "FLAT_NO")
	private String flatNo;
	
	@Column (name = "CUST_NAME")
	private String customerName;
	
	//@Column (name = "ISVIEWED")
	//private Long isViewed;
	
	@Column (name = "VIEW_STATUS")
	private String viewStatus;
	
	@Column (name = "DELIVERED_STATUS")
	private String deliveredStatus;
	
	@Column (name = "DELIVERED_TIME")
	private Timestamp deliveredTime;
	
	@Column (name = "VIEWED_TIME")
	private Timestamp viewedTime;
	
	@Column (name = "DEVICE_MODEL")
	private String deviceModel;
}
