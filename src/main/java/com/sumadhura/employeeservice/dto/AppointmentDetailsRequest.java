package com.sumadhura.employeeservice.dto;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.service.dto.ApptmtSlotTimesInfo;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentDetailsRequest {
	
	private Long type;
	private List<Long> typeIds;
	private List<ApptmtSlotTimesInfo> apptmtSlotTimesInfoList;
	private List<Date> slotDatesList;
	private Long siteId;
	private String setName;
	
}
