package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name="CUST_BOOK_INFO")
@Data
public class CustBookInfoPojo {

	@Column(name="CUST_BOOK_INFO_ID")
	private Long custBookInfoId;

	/*
	@Column(name="PHONE_NO")
	private Long phoneNo;
	@Column(name="ALTERNATE_PHONE_NO")
	private Long alternatePhoneNo;
	*/
	
	@Column(name="PHONE_NO")
	private String phoneNo;

	@Column(name="ALTERNATE_PHONE_NO")
	private String alternatePhoneNo;

	@Column(name="TELE_PHONE")
	private String telePhone;

	@Column(name="EMAIL")
	private String email;

	@Column(name="MARITAL_STATUS")
	private String maritalStatus;

	@Column(name="DOCUMENTS_UPLOAD")
	private String documentsUpload;

	@Column(name="CUST_APP_ID")
	private Long custAppId;

	@Column(name="DATE_OF_ANNIVERSERY")
	private Timestamp dateOfAnniversery;

	@Column(name="SALES_TEAM_EMP_ID")
	private Long salesTeamEmpId;

	@Column(name="SALES_TEAM_LEAD_ID")
	private Long salesTeamLeadId;

	@Column(name="WORK_EXPERIENCE")
	private String workExperience;

	@Column(name="EDUCATIONAL_QUALIFICATION")
	private String educationalQualification;

	@Column(name="ANNUAL_HOUSE_HOLD_INCOME")
	private String annualHouseHoldIncome;

	@Column(name="CUST_PROFFISIONAL_ID")
	private Long custProffisionalId;

	@Column(name="COMMENTS")
	private String overallExperienceWithSumadhura;

	@Column(name="TDS_AUTHORIZATION_ID")
	private Long tdsAuthorizationId;

	@Column(name="TERMS_CONDITION_FILE_NAME")
	private String termsConditionFileName;

	@Column(name="STATUS_ID")
	private Long statusId;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;

	@Column(name="CUST_ID")
	private Long custId;
	
	@Column(name="FLAT_NO")
	private String flatNo;	
	
	
}
