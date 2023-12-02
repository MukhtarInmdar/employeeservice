package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ApptmtSlotDatesPojo {
	
	@Column(name="APPTMT_SLOT_DATES_ID")
	private Long apptmtSlotDatesId;
	
	@Column(name="APPTMT_DATE")
	private Date apptmtDate;
	
	@Column(name="TYPE")
	private Long type;
	
	@Column(name="TYPE_ID")
	private Long typeId;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="STATUS_ID")
	private Long statusId;
}
