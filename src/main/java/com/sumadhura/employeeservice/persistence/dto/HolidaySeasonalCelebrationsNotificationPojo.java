package com.sumadhura.employeeservice.persistence.dto;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class HolidaySeasonalCelebrationsNotificationPojo {
	
	@Column(name="ID")
	private Long id;

	@Column(name="NOTIFICATIONTEXT")
	private String notificationText;
	
	@Column(name="MESSAGE")
	private String message;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="IMAGELOCATION")
	private String imgLoc;
	
	@Column(name="FILELOCATION")
	private String fileLoc;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="EVENT_NAME")
	private String eventName;
	
	@Column(name="CUST_ID")
	private Long customerId;
	
	@Column(name="CUST_NAME")
	private String customerName;
	
	@Column(name="DATE_OF_BIRTH")
	private Timestamp dateOfBirth;
	
	@Column(name="DATE_OF_ANNIVERSERY")
	private Timestamp dateOfAnniversary;
	
	@Column(name="DEVICE_ID")
	private Long deviceId;
	
	@Column(name="DEVICE_TOKEN")
	private String deviceToken;
	
	@Column(name="OS_TYPE")
	private String osType;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="DATE_OF_BIRTH_STATUS")
	private Long dateOfBirthStatus;
	
	@Column(name="DATE_OF_ANNIVERSERY_STATUS")
	private Long dateOfAnniversaryStatus;
	
	@Column(name="HOLIDAY_SEASONAL_CELEBRATIONS_NOTIFICATIONS_EVENT_ID")
	private Long eventId;
	
	@Column(name="FLAT_ID")
	private Long flatId;
	
	@Column(name="NAME")
	private String name;
	
	
}
