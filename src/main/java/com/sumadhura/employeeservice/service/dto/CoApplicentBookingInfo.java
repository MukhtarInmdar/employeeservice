package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.enums.Status;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CoApplicentBookingInfo {

	private Long coAppBookInfoId;
	//private Long phoneNo;
	private String phoneNo;
	private String alternatePhoneNo;
	//private Long alternatePhoneNo;
	private String email;
	private String telePhone;
	private String maritalStatus;
	private Timestamp dateOfAnniversery;
	private String workExperience;
	private String educationalQualification;
	private String annualHouseHoldIncome;
	private Long custProffisionalId;
	private Long statusId;
	private Status status;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private Long coApplicantId;
	private Long custBookInfoId;
	private String type;
	private String actionUrl;
	
}
