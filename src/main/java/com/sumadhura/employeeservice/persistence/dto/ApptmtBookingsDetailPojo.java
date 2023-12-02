package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ApptmtBookingsDetailPojo {
	
	@Column(name="APPTMT_BOOKINGS_ID")
	private Long apptmtBookingsId;
	
	@Column(name="APPTMT_SLOT_TIMES_ID")
	private Long apptmtSlotTimesId;
	
	@Column(name="APPTMT_SUB_TYPE_ID")
	private Long apptmtSubTypeId;
	
	@Column(name="APPTMT_DATE")
	private Timestamp apptmtDate;
	
	@Column(name="STR_APPTMT_DATE")
	private String  strApptmtDate;
	
	
	@Column(name="SLOT_TIME")
	private String slotTime;
	
	@Column(name="APPTMT_SUB_TYPE_NAME")
	private String apptmtSubTypeName;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="APPTMT_STATUS_ID")
	private Long apptmtStatusId;
	
	@Column(name="APPTMT_STATUS_NAME")
	private String apptmtStatusName;
	
	@Column(name="TYPE")
	private Long type;
	
	@Column(name="TYPE_ID")
	private Long typeId;
	
	@Column(name="ASSIGNED_TYPE")
	private String assignedType;
	
	@Column(name="ASSIGNED_TYPE_NAME")
	private String assignedTypeName;
	
	@Column(name="APPTMT_REQ_COMMENTS")
	private String apptmtReqComments;
	
	@Column(name="CUST_NAME")
	private String custName;
	
	@Column(name="SITE_NAME")
	private String siteName;
	
	@Column(name="BLOCK_NAME")
	private String blockName;
	
	@Column(name="FLAT_NO")
	private String flatNo;
	
	@Column(name="START_TIME")
	private Timestamp startTime;
	
	@Column(name="ASSIGNED_TYPE_MAIL")
	private String assignedTypeMail;
	
	@Column(name="SLOT_TIME_FOR_SORTING")
	private Timestamp slotTimeForSorting;
	
	@Column(name="IS_TIME_COMPLETED")
	private Boolean isTimeCompleted;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	
	@Column(name="EMP_OR_DEPT_NAME")
	private String empOrDeptName;
	
	@Column(name="EMP_OR_DEPT_ID")
	private Long empOrDeptId;
	
	@Column(name="EMP_OR_DEPT_TYPE")
	private Long empOrDeptType;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="APPTMT_SUMMARY")
	private String apptmtSummary;
	
	@Column(name="END_TIME")
	private Timestamp endTime;
	
	@Column(name="APPOINTMENT_TIME")
	private String appointmentTime;
	
	@Column(name="GOOGLE_CALENDAR_EVENT_ID")
	private String googleCalendarEventId;
}
