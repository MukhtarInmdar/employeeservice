package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;


import lombok.Data;

@Data
public class ApplicantCheckListVerification {
	
	private Long  applicantCheckListVerfiId;
	private Long  applicationId;
	private Long  checkListDeptMappingId;
	private Long statusId;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	private Long flatBookId;

}
