/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CoApplicantPojo class provides CoApplicant Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 03:17PM
 */
@Entity
@Getter
@Setter
@ToString
public  class CoApplicantPojo {

	@Column(name = "APPLICANT_ID")
	private Long applicantId;

	@Column(name = "CUST_ID")
	private Long customerId;

	@Column(name = "ADDRESS_ID")
	private Long addressId;

	@Column(name = "APPLICATE_NUMBER")
	private String applicateNumber;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "AGE")
	private Integer age;

	@Column(name = "AADHAR_ID")
	private String adharId;

	@Column(name = "VOTER_ID")
	private String voterId;

	@Column(name = "PANCARD")
	private String pancard;

	@Column(name = "PHONE_NO")
	private Long phoneNo;

	@Column(name = "ALTERNATE_PHONE_NO")
	private Long alternativePhoneNo;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@Column(name = "UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name = "DATE_OF_BIRTH")
	private Timestamp dateOfBirth;

	@Column(name = "RELATION_WITH_CUST")
	private String relationWithCustomer;

	@Column(name="RELATION_NAME_PREFIX")
	private String relationNamePrefix;
	
	@Column(name = "NATIONALITY")
	private String nationality;

	@Column(name = "RELATION_WITH")
	private String relationWith;

	@Column(name = "RALATION_NAME")
	private String relationName;

	@Column(name = "TELE_PHONE")
	private String telephone;

	@Column(name = "FLAT_BOOK_ID")
	private Long flatBookingId;

	@Column(name = "NAME_PREFIX")
	private String nameprefix;

	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;

	@Column(name = "WORK_EXPERIENCE")
	private String workExperince;

	@Column(name = "EDUCATIONAL_QUALIFICATION")
	private String educationalQualification;

	@Column(name = "ANNUAL_HOUSE_HOLD_INCOME")
	private String annualHouseHoldIncome;

	@Column(name = "CUST_PROFFISIONAL_ID")
	private Long custProffisionalId;

	@Column(name = "STATUS_ID")
	private Long statusId;

	@Column(name = "CO_APP_FULL_NAME")
	private String coAppFullName;
	
	@Column(name = "CUST_FULL_NAME")
	private String custFullName;

}
