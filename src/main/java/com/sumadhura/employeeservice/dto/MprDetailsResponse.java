package com.sumadhura.employeeservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class MprDetailsResponse {
	private List<MprResponse> mprResponseList;
	private Long pageCount;
	private Long rowCount;
}
