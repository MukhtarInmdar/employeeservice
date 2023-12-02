/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

/**
 * CustomerPojo class provides customer Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 04.05.2019
 * @time 02:53PM
 */
@Entity
@Data
public class CustomerPojo {

	@Column(name="CUST_ID")
	private Long customerId;
	@Column(name="NAME_PREFIX")
	private String namePrefix;
	@Column(name="FIRST_NAME")
	private String firstName;
	@Column(name="LAST_NAME")
	private String lastName;
	@Column(name="GENDER")
	private String gender;
	@Column(name="AGE")
	private Long age;
	@Column(name="DATE_OF_BIRTH")
	private Timestamp dob;
	@Column(name="AADHAR_NUMBER")
	private String adharNumber;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	@Column(name="PANCARD")
	private String pancard;
	@Column(name="VOTER_ID")
	private String voterId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="RELATION_WITH")
	private String relationWith;
	@Column(name="RELATION_NAME")
	private String relationName;
	@Column(name="RELATION_NAME_PREFIX")
	private String relationNamePrefix;
	@Column(name="NATIONALITY")
	private String nationality;
	@Column(name="MARITAL_STATUS")
	private String maritalStatus;
	@Column(name="DOCUMENTS_UPLOAD")
	private String documentsUpload;
	@Column(name="PROFILE_PIC")
	private String profilePic;
	@Column(name="PASSPORT")
	private String passport;
	
	/*@Column(name="CHECKLIST_REGISTRATION_ID")
	private Long checkListRegistrationId;
	@Column(name="CHECKLIST_LEGAL_OFFICIER_ID")
	private Long checkListLegalOfficierId;
	@Column(name="CHECKLIST_CRM_ID")
	private Long checkListCrmId;
	@Column(name="PHONE_NO")
	private Long phoneNumber;
	@Column(name="ALTERNATE_PHONE_NO")
	private Long alternatePhoneno;
	@Column(name="TELE_PHONE")
	private String telephone;
	@Column(name="EMAIL")
	private String email;
	@Column(name="ADDRESS_ID")
	private Long addressId;
	@Column(name="CUST_PROFILE_ID")
	private Long customerProfileId;
	@Column(name="CUST_APP_ID")
	private Long customerAppId;
	@Column(name="DATE_OF_ANNIVERSERY")
	private Timestamp doa;
	@Column(name="SALES_TEAM_EMP_ID")
	private Long salesTeamEmployeeId;
	@Column(name="SALES_TEAM_LEAD_ID")
	private Long salesTeamLeadId;
	@Column(name="WORK_EXPERIENCE")
	private String workExperince;
	@Column(name="EDUCATIONAL_QUALIFICATION")
	private String educationalQualification;
	@Column(name="ANNUAL_HOUSE_HOLD_INCOME")
	private String annualHouseHoldIncome;
	@Column(name="CUST_PROFFISIONAL_ID")
	private Long customerProffisionalId;
	@Column(name="CUST_OTHERDTLS_ID")
	private Long custOtherDetailsId;
	@Column(name="COMMENTS")
	private String comments;
	@Column(name="TDS_AUTHORIZATION_ID")
	private Long tdsAuthorizationId;
	*/
}
