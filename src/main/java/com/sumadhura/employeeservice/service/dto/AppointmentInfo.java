package com.sumadhura.employeeservice.service.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.dto.AppointmentDetailsRequest;

import lombok.Data;

@Data
public class AppointmentInfo {
	
	private Long custId;
	private Long flatBookingId;
	private Date apptmtDate;
	private Long apptmtSlotTimesId;
	private Long apptmtSubTypeId;
	private String apptmtReqComments;
	private String requestUrl;
	private Long employeeId;
	private Timestamp startDate;
	private Timestamp endDate;
	private Long siteId;
	private Long blockId;
	private String requestFrom;
	private List<ApptmtSlotTimesInfo> apptmtSlotTimesInfoList;
	private List<Date> slotDatesList;
	private Long apptmtSlotDatesId;
	private List<Long> employeeIds;
	private String apptmtStatusName;
	private Long apptmtBookingsId;
	private String slotStatusName;
	private String apptmtSummary;
	private String googleCalendarEventId;
	private Long type;
	private List<Long> typeIds;
	private List<AppointmentDetailsRequest> apptmtDtlsReqstList;
	private List<Long> apptmtSlotTimesIds;
}
