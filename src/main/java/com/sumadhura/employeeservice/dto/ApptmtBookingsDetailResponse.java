package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ApptmtBookingsDetailResponse {
	
	private Long apptmtBookingsId;
	private Long apptmtSlotTimesId;
	private Long apptmtSubTypeId;
	private Timestamp apptmtDate;
	private String strApptmtDate;
	private String slotTime;
	private String apptmtSubTypeName;
	private Long statusId;
	private Long apptmtStatusId;
	private String apptmtStatusName;
	private Long type;
	private Long typeId;
	private String assignedType;
	private String assignedTypeName;
	private String apptmtReqComments;
	private String custName;
	private String siteName;
	private String blockName;
	private String flatNo;
	private Timestamp startTime;
	private String assignedTypeMail;
	private Boolean isTimeCompleted;
	private Long flatBookId;
	private String empOrDeptName;
	private Long empOrDeptId;
	private Long empOrDeptType;
	private Long siteId;
	private String apptmtSummary;
	private Timestamp endTime;
	private String appointmentTime;
}
