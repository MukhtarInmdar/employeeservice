package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class EmployeeDetails {
	private Long empDetailsId;
	private String employeeName;
	private String employeeDesignation;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private Long statusId;
	private String email;
	private Long departmentId;
	private String mobileNo;
	private String userProfile;
	private Long siteId;
	private Long employeeId;
	private String employeeNameDesg;
}
