/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * CoApplicant class provides CoApplicant specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 06:45PM
 */
public class CoApplicant implements Serializable{

	private static final long serialVersionUID = -6920185816495787579L;
	private Long applicantId;
	private Long customerId;
	private Long addressId;
	private String applicateNumber;
	private String firstName;
	private String lastName;
	private String middleName;
	private String gender;
	private Integer age;
	private String adharId;
	private String voterId;
	private String pancard;
	private Long phoneNo;
	private Long alternativePhoneNo;
	private String email;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private Timestamp dateOfBirth;
	private String relationWithCustomer;
	private String nationality;
	private String relationWith;
	private String relationName;
	private String telephone;
	private Long flatBookingId;
	private String nameprefix;
	private Long custProffisionalId;
	private Long statusId;
	private CustomerAddress customerAddress; 
	
	
	/**
	 * @return the applicantId
	 */
	public Long getApplicantId() {
		return applicantId;
	}
	/**
	 * @param applicantId the applicantId to set
	 */
	public void setApplicantId(Long applicantId) {
		this.applicantId = applicantId;
	}
	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the addressId
	 */
	public Long getAddressId() {
		return addressId;
	}
	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	/**
	 * @return the applicateNumber
	 */
	public String getApplicateNumber() {
		return applicateNumber;
	}
	/**
	 * @param applicateNumber the applicateNumber to set
	 */
	public void setApplicateNumber(String applicateNumber) {
		this.applicateNumber = applicateNumber;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * @return the adharId
	 */
	public String getAdharId() {
		return adharId;
	}
	/**
	 * @param adharId the adharId to set
	 */
	public void setAdharId(String adharId) {
		this.adharId = adharId;
	}
	/**
	 * @return the voterId
	 */
	public String getVoterId() {
		return voterId;
	}
	/**
	 * @param voterId the voterId to set
	 */
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}
	/**
	 * @return the pancard
	 */
	public String getPancard() {
		return pancard;
	}
	/**
	 * @param pancard the pancard to set
	 */
	public void setPancard(String pancard) {
		this.pancard = pancard;
	}
	/**
	 * @return the phoneNo
	 */
	public Long getPhoneNo() {
		return phoneNo;
	}
	/**
	 * @param phoneNo the phoneNo to set
	 */
	public void setPhoneNo(Long phoneNo) {
		this.phoneNo = phoneNo;
	}
	/**
	 * @return the alternativePhoneNo
	 */
	public Long getAlternativePhoneNo() {
		return alternativePhoneNo;
	}
	/**
	 * @param alternativePhoneNo the alternativePhoneNo to set
	 */
	public void setAlternativePhoneNo(Long alternativePhoneNo) {
		this.alternativePhoneNo = alternativePhoneNo;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the updatedDate
	 */
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	/**
	 * @return the dateOfBirth
	 */
	public Timestamp getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Timestamp dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the relationWithCustomer
	 */
	public String getRelationWithCustomer() {
		return relationWithCustomer;
	}
	/**
	 * @param relationWithCustomer the relationWithCustomer to set
	 */
	public void setRelationWithCustomer(String relationWithCustomer) {
		this.relationWithCustomer = relationWithCustomer;
	}
	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}
	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	/**
	 * @return the relationWith
	 */
	public String getRelationWith() {
		return relationWith;
	}
	/**
	 * @param relationWith the relationWith to set
	 */
	public void setRelationWith(String relationWith) {
		this.relationWith = relationWith;
	}
	/**
	 * @return the relationName
	 */
	public String getRelationName() {
		return relationName;
	}
	/**
	 * @param relationName the relationName to set
	 */
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * @return the flatBookingId
	 */
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	/**
	 * @param flatBookingId the flatBookingId to set
	 */
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	/**
	 * @return the nameprefix
	 */
	public String getNameprefix() {
		return nameprefix;
	}
	/**
	 * @param nameprefix the nameprefix to set
	 */
	public void setNameprefix(String nameprefix) {
		this.nameprefix = nameprefix;
	}
	/**
	 * @return the custProffisionalId
	 */
	public Long getCustProffisionalId() {
		return custProffisionalId;
	}
	/**
	 * @param custProffisionalId the custProffisionalId to set
	 */
	public void setCustProffisionalId(Long custProffisionalId) {
		this.custProffisionalId = custProffisionalId;
	}
	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the customerAddress
	 */
	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}
	/**
	 * @param customerAddress the customerAddress to set
	 */
	public void setCustomerAddress(CustomerAddress customerAddress) {
		this.customerAddress = customerAddress;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CoApplicant [applicantId=" + applicantId + ", customerId=" + customerId + ", addressId=" + addressId
				+ ", applicateNumber=" + applicateNumber + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", middleName=" + middleName + ", gender=" + gender + ", age=" + age + ", adharId=" + adharId
				+ ", voterId=" + voterId + ", pancard=" + pancard + ", phoneNo=" + phoneNo + ", alternativePhoneNo="
				+ alternativePhoneNo + ", email=" + email + ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", dateOfBirth=" + dateOfBirth + ", relationWithCustomer=" + relationWithCustomer
				+ ", nationality=" + nationality + ", relationWith=" + relationWith + ", relationName=" + relationName
				+ ", telephone=" + telephone + ", flatBookingId=" + flatBookingId + ", nameprefix=" + nameprefix
				+ ", custProffisionalId=" + custProffisionalId + ", statusId=" + statusId + ", customerAddress="
				+ customerAddress + "]";
	}
	
}
