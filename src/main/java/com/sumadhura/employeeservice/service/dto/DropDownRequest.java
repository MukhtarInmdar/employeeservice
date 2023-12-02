package com.sumadhura.employeeservice.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.dto.Result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DropDownRequest extends Result {
	
	private static final long serialVersionUID = 348006467260696842L;
	private List<Long> ids;
	private Long loginUserId;
	private List<Long> siteIds;
	private List<Long> blockDetIds;
	private List<Long> floorDetIds;
	private List<String> flatSeriesList;
	private List<Long> sbuaList;
	private List<String> facingList;
	private List<String> bhkTypeList;
	private String requestUrl;
}
