package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sumadhura.employeeservice.enums.Status;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class Co_ApplicantInfo {

	private Long coApplicantId;
	private String coApplicantNumber;
	private String namePrefix;
	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private Timestamp dateOfBirth;
	private Long age;
	private String aadharId;
	private String passport;
	private String voterId;
	private String pancard;
	private String nationality;
	private String relationWith;
	private String relationWithCust;
	private String relationName;
	private String relationNamePrefix;
	private Long statusId;
	private Status status;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private String actionUrl;
	private Timestamp coApplicantBookingDate;
	private Timestamp handingOverDate;
	private Timestamp registrationDate;
}
