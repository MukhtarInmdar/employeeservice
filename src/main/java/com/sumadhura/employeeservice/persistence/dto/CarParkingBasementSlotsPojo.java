package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CarParkingBasementSlotsPojo {
	
	@Column(name="BASEMENT_ID")
	private Long basementId;
	
	@Column(name="BASEMENT_NAME")
	private String basementName;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="SLOT_ID")
	private Long slotId;
	
	@Column(name="SLOT_NAME")
	private String slotName;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="SLOT_STATUS_ID")
	private Long slotStatusId;
	
	@Column(name="SLOT_STATUS_NAME")
	private String slotStatusName;
	
}
