package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;

import lombok.Data;

@Data
public class CarParkingAllotmentDetailInfo {
	
	private CarParkingAllotmentPdfInfo carParkingAllotmentPdfInfo;
	private List<OfficeDtlsResponse> officeDetailsResponseList;
}
