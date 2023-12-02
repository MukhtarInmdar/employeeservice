package com.sumadhura.employeeservice.dto;

import java.util.List;

import com.sumadhura.employeeservice.dto.BlockDetailsResponse;

import lombok.Data;

@Data
public class FloorResponse {
	
	private List<BlockDetailsResponse> blockDetRes;

}
