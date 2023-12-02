package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ReferenceMaster {
	private Long referenceId;
	private String referenceType;
	private Long statusId;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
}
