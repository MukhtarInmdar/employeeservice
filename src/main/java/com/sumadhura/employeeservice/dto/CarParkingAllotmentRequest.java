package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.service.dto.CarParkingAllotmentBasementInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarParkingAllotmentRequest extends Result implements Serializable{

	private static final long serialVersionUID = 9066884249155214194L;
	
	private String requestUrl;
	private Long employeeId;
	private Long siteId;
	private Long basementId;
	private Long slotId;
	private Long flatBookId;
	private List<CarParkingAllotmentBasementInfo> carParkingAllotmentBasementInfoList;
	private String slotStatusName;
	private Long allotmentId;
	private String comments;
}
