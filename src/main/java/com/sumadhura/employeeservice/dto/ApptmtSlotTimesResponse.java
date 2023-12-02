package com.sumadhura.employeeservice.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class ApptmtSlotTimesResponse {
	
	private Long apptmtSlotTimesId;
	private Long apptmtSlotDatesId;
	private Timestamp startTime;
	private Timestamp endTime;
	private Date apptmtDate;
	private String slotTime;

}
