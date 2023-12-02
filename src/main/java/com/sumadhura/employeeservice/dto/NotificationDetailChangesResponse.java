package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class NotificationDetailChangesResponse {
	private List<DropDownResponse> siteList;
	private List<DropDownResponse> blockList;
	private List<DropDownResponse> floorList;
	private List<DropDownResponse> flatList;
	private List<Long> sbuaList;
	private List<String> flatSeriesList;
	private List<String> facingList;
	private List<String> bhkTypeList;
	private List<DropDownResponse> notificationTypeList;
	private List<DropDownResponse> stateList;
	private List<String> flatsByDate;
	private Integer flatCountByDate;
	private Timestamp startDate;
	private Timestamp endDate;
}
