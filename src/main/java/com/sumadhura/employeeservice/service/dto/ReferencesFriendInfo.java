package com.sumadhura.employeeservice.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ReferencesFriendInfo {
	
	private Long referencesFriendId;
	private String referenceFreindsorFamilyName;
	
}
