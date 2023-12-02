package com.sumadhura.employeeservice.dto;

import java.util.List;

import com.sumadhura.employeeservice.persistence.dto.FlatBookingPojo;

import lombok.Data;

@Data
public class FlatResponse {
	
	private List<BlockDetailsResponse> blockDetRespList;
	private List<FlatBookingPojo> flatBookingPojoList;
}
