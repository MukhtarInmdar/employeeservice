package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name="CO_APPLICANT")
public class Co_ApplicantPojo {

	@Column(name="CO_APPLICANT_ID")
	private Long coApplicantId;

	@Column(name="CO_APPLICANT_NUMBER")
	private String coApplicantNumber;

	@Column(name="NAME_PREFIX")
	private String namePrefix;

	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="LAST_NAME")
	private String lastName;

	@Column(name="MIDDLE_NAME")
	private String middleName;

	@Column(name="GENDER")
	private String gender;

	@Column(name="DATE_OF_BIRTH")
	private Timestamp dateOfBirth;

	@Column(name="AGE")
	private Long age;

	@Column(name="AADHAR_ID")
	private String aadharId;

	@Column(name="VOTER_ID")
	private String voterId;

	@Column(name="PANCARD")
	private String pancard;
	
	@Column(name="PASSPORT")
	private String passport;
	
	@Column(name="NATIONALITY")
	private String nationality;

	@Column(name="RELATION_WITH")
	private String relationWith;

	@Column(name="RELATION_WITH_CUST")
	private String relationWithCust;

	@Column(name="RELATION_NAME")
	private String relationName;

	@Column(name="RELATION_NAME_PREFIX")
	private String relationNamePrefix;

	@Column(name="STATUS_ID")
	private Long statusId;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name = "CO_APPLICANT_BOOKING_DATE")
	private Timestamp coApplicantBookingDate;
	
	@Column(name = "HANDING_OVER_DATE")
	private Timestamp handingOverDate;
	
	@Column(name = "REG_DATE")
	private Timestamp registrationDate;
}
