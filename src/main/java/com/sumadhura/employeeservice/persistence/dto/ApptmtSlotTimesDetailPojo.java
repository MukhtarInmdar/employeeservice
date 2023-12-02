package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ApptmtSlotTimesDetailPojo {
	
	@Column(name="APPTMT_SLOT_DATES_ID")
	private Long apptmtSlotDatesId;
	
	@Column(name="APPTMT_SLOT_TIMES_ID")
	private Long apptmtSlotTimesId;
	
	@Column(name="APPTMT_SLOT_EMP_PROJECT_WISE_ID")
	private Long apptmtSlotEmpProjectWiseId;
	
	@Column(name="APPTMT_BOOKINGS_ID")
	private Long apptmtBookingsId;
	
	@Column(name="APPTMT_SUB_TYPE_ID")
	private Long apptmtSubTypeId;
	
	@Column(name="APPTMT_DATE")
	private Date apptmtDate;
	
	@Column(name="START_TIME")
	private Timestamp startTime;
	
	@Column(name="END_TIME")
	private Timestamp endTime;
	
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
	
	@Column(name="SLOT_STATUS_NAME")
	private String slotStatusName;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="SITE_NAME")
	private String siteName;
	
	@Column(name="CUST_NAME")
	private String custName;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	
	@Column(name="IS_TIME_COMPLETED")
	private Boolean isTimeCompleted;
	
	private List<ApptmtSlotTypesPojo> apptmtSlotTypesPojos;
	
}
