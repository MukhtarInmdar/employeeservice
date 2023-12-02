package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
@Table(name="CO_APP_BOOK_INFO")
public class CoAppBookInfoPojo {

	@Column(name="CO_APP_BOOK_INFO_ID")
	private Long coAppBookInfoId;
	
	
	@Column(name="PHONE_NO")
	private String phoneNo;

	@Column(name="ALTERNATE_PHONE_NO")
	private String alternatePhoneNo;
	
	/*
	@Column(name="PHONE_NO")
	private Long phoneNo;
	@Column(name="ALTERNATE_PHONE_NO")
	private Long alternatePhoneNo;
	*/
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="TELE_PHONE")
	private String telePhone;
	
	@Column(name="MARITAL_STATUS")
	private String maritalStatus;
	
	@Column(name="DATE_OF_ANNIVERSERY")
	private Timestamp dateOfAnniversery;
	
	@Column(name="WORK_EXPERIENCE")
	private String workExperience;
	
	@Column(name="EDUCATIONAL_QUALIFICATION")
	private String educationalQualification;
	
	@Column(name="ANNUAL_HOUSE_HOLD_INCOME")
	private String annualHouseHoldIncome;
	
	@Column(name="CUST_PROFFISIONAL_ID")
	private Long custProffisionalId;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	
	@Column(name="CO_APPLICANT_ID")
	private Long coApplicantId;
	
	@Column(name="CUST_BOOK_INFO_ID")
	private Long custBookInfoId;
	
	@Column(name="TYPE")
	private Long type;
}
