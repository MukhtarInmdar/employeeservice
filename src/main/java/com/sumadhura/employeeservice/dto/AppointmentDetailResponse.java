package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class AppointmentDetailResponse {
	
	private List<ApptmtBookingsDetailResponse> apptmtBookingsDetailResponseList;
	private List<ApptmtSlotTimesResponse> apptmtSlotTimesResponseList;
	private List<ApptmtSlotTimesDetailResponse> apptmtSlotTimesDetailResponseList;
	private List<ApptmtSlotTimesResponse> apptmtSlotTimesCompletedResponseList;
}
