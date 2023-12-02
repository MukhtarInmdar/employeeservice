package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ApptmtSlotTimesPojo {
	
	@Column(name="APPTMT_SLOT_TIMES_ID")
	private Long apptmtSlotTimesId;
	
	@Column(name="APPTMT_SLOT_DATES_ID")
	private Long apptmtSlotDatesId;
	
	@Column(name="START_TIME")
	private Timestamp startTime;
	
	@Column(name="END_TIME")
	private Timestamp endTime;
	
	@Column(name="APPTMT_DATE")
	private Date apptmtDate;
	
	@Column(name="SLOT_TIME")
	private String slotTime;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="SLOT_STATUS_ID")
	private Long slotStatusId;

}
