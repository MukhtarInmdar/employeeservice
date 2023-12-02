package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import lombok.Data;

@Data
public class CarParkingAllotmentInfo {
	
	private String requestUrl;
	private Long employeeId;
	private Long siteId;
	private Long basementId;
	private Long slotId;
	private Long flatBookId;
	private String allotmentLetterPath;
	private List<CarParkingAllotmentBasementInfo> carParkingAllotmentBasementInfoList;
	private String slotStatusName;
	private Long allotmentId;
	private String comments;
	private Long cpSetOfLevId;
	private Long cpAprLevId;
}
