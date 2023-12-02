package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomerApplicationInfo {
	
	private Long custAppId;
	private Long siteId;
	private Long unit;	
	private Long leadId;
	private String ackId;
	//private Long ackId;	
	private Long appNo;	
	private Long blockId;	
	private Long statusId;	
	private Timestamp createdDate;	
	private Timestamp modifiedDate;
	private Long stmId;	
	private Long flatBookId;
	private String reasonForNoKYC;

}
