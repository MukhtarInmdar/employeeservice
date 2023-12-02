package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.service.dto.ApptmtSlotTimesInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentRequest extends Result implements Serializable{

	private static final long serialVersionUID = 968255728750069172L;
	
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
	private List<Long> employeeIds;
	private String apptmtStatusName;
	private Long apptmtBookingsId;
	private String slotStatusName;
	private String apptmtSummary;
	private List<AppointmentDetailsRequest> apptmtDtlsReqstList;
}
