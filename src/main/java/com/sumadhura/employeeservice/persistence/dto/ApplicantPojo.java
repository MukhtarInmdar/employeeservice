package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class ApplicantPojo {

	
	@Column(name="APPLICANT_ID")
	private Long applicantId;
	@Column(name="CUST_ID")
	private Long custId;
	@Column(name="ADDRESS_ID")
	private Long addressId;
	@Column(name="APPLICATE_NUMBER")
	private String applicationNumber;
	@Column(name="FIRST_NAME")
	private String firstName;
	@Column(name="LAST_NAME")
	private String lastName;
	@Column(name="MIDDLE_NAME")
	private String middleName;
	@Column(name="GENDER")
	private String gender;
	@Column(name="AGE")
	private Long age;
	@Column(name="AADHAR_ID")
	private String aadharId;
	@Column(name="VOTER_ID")
	private String voterId;
	@Column(name="PANCARD")
	private String pancard;
	@Column(name="PHONE_NO")
	private Long phonNo;
	@Column(name="ALTERNATE_PHONE_NO")
	private Long alternatePhoneNO;
	@Column(name="EMAIL")
	private String email;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;
	@Column(name="DATE_OF_BIRTH")
	private Timestamp dateOfBirth;
	@Column(name="RELATION_WITH_CUST")
	private String relationWithCust;
	@Column(name="NATIONALITY")
	private String nationality;
	@Column(name="RELATION_WITH")
	private String relationWith;
	@Column(name="RALATION_NAME")
	private String relationName;
	@Column(name="TELE_PHONE")
	private String telephone;
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookingId;
	@Column(name="NAME_PREFIX")
	private String namePrefix;
	@Column(name="MARITAL_STATUS")
	private String maritalStatus;
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
	@Column(name="RELATION_NAME_PREFIX")
	private String relationNamePrefix;
	
	public Long getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(Long applicantId) {
		this.applicantId = applicantId;
	}
	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}
	public String getAadharId() {
		return aadharId;
	}
	public void setAadharId(String aadharId) {
		this.aadharId = aadharId;
	}
	public String getVoterId() {
		return voterId;
	}
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}
	public String getPancard() {
		return pancard;
	}
	public void setPancard(String pancard) {
		this.pancard = pancard;
	}
	public Long getPhonNo() {
		return phonNo;
	}
	public void setPhonNo(Long phonNo) {
		this.phonNo = phonNo;
	}
	public Long getAlternatePhoneNO() {
		return alternatePhoneNO;
	}
	public void setAlternatePhoneNO(Long alternatePhoneNO) {
		this.alternatePhoneNO = alternatePhoneNO;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Timestamp getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Timestamp dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getRelationWithCust() {
		return relationWithCust;
	}
	public void setRelationWithCust(String relationWithCust) {
		this.relationWithCust = relationWithCust;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getRelationWith() {
		return relationWith;
	}
	public void setRelationWith(String relationWith) {
		this.relationWith = relationWith;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	public String getNamePrefix() {
		return namePrefix;
	}
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getWorkExperience() {
		return workExperience;
	}
	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}
	public String getEducationalQualification() {
		return educationalQualification;
	}
	public void setEducationalQualification(String educationalQualification) {
		this.educationalQualification = educationalQualification;
	}
	public String getAnnualHouseHoldIncome() {
		return annualHouseHoldIncome;
	}
	public void setAnnualHouseHoldIncome(String annualHouseHoldIncome) {
		this.annualHouseHoldIncome = annualHouseHoldIncome;
	}
	public Long getCustProffisionalId() {
		return custProffisionalId;
	}
	public void setCustProffisionalId(Long custProffisionalId) {
		this.custProffisionalId = custProffisionalId;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	
	
		
	
}
