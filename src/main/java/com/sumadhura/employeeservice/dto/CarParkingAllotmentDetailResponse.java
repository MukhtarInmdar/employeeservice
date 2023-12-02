package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class CarParkingAllotmentDetailResponse {
	private List<CarParkingBasementSlotsResponse> carParkingBasementSlotsResponseList;
	private List<CarParkingAllotmentSlotResponse> carParkingAllotmentSlotResponseList;
	private List<String> allotmentLetterPathList;
}
