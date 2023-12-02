package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.enums.Status;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomerBookingInfo {
	private Long custBookInfoId;
	//private Long phoneNo;
	private String phoneNo;
	private String alternatePhoneNo;
   //private Long alternatePhoneNo;
	private String telePhone;
	private String email;
	private String maritalStatus;
	private String documentsUpload;
	private Long custAppId;
	private Timestamp dateOfAnniversery;
	private Long salesTeamEmpId;
	private String salesTeamEmpName;
	private Long salesTeamLeadId;
	private String salesTeamLeadName;
	private String workExperience;
	private String educationalQualification;
	private String annualHouseHoldIncome;
	private Long custProffisionalId;
	private String overallExperienceWithSumadhura;
	private Long tdsAuthorizationId;
	private String tdsAuthorization;
	private String tdsAuthorizationType;
	private Boolean tdsAuthorizationOption1;
	private Boolean tdsAuthorizationOption2;
	private String termsConditionFileName;
	private String termsConditionFileData;
	private Long statusId;
	private Status status;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private Long flatBookId;
	private Long custId;
	
}
