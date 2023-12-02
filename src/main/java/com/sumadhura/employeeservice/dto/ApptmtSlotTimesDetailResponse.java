package com.sumadhura.employeeservice.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.ApptmtSlotTypesPojo;

import lombok.Data;

@Data
public class ApptmtSlotTimesDetailResponse {
	
	private Long apptmtSlotDatesId;
	private Long apptmtSlotTimesId;
	private Long apptmtSlotEmpProjectWiseId;
	private Long apptmtBookingsId;
	private Long apptmtSubTypeId;
	private Date apptmtDate;
	private Timestamp startTime;
	private String slotTime;
	private Long createdBy;
	private Timestamp createdDate;
	private Long statusId;
	private Long slotStatusId;
	private String slotStatusName;
	private Long siteId;
	private String siteName;
	private String custName;
	private Long flatBookId;
	private Boolean isTimeCompleted;
	private List<ApptmtSlotTypesPojo> apptmtSlotTypesPojos;
}
