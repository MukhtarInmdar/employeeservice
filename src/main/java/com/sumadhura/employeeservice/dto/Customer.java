package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends Result implements Serializable{
	
	private static final long serialVersionUID = -8296260841682256265L;
	private Long customerId;
	private Long id;
	private Long flatId;
	private Long flatBookingId;
	private String namePrefix;
	private String firstName;
	private String lastName;
	private String gender;
	private Integer age;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	private Timestamp dob;
	private String adharNumber;
	private Timestamp createdDate;
	private Timestamp updatedDate;
	private Long phoneNumber;
	private Long alternatePhoneno;
	private String telephone;
	private String pancard;
	private String voterId;
	private String email;
	private Long statusId;
	private Long addressId;
	private String relationNamePrefix;
	private String relationWith;
	private String relationName;
	private String nationality;
	private String maritalStatus;
	private String documentsUpload;
	private String profilePic;
	private Long customerProfileId;
	private Long customerAppId;
	private Timestamp doa;
	private Long salesTeamEmployeeId;
	private Long salesTeamLeadId;
	private String workExperince;
	private String educationalQualification;
	private String annualHouseHoldIncome;
	private Long customerProffisionalId;
	private Long custOtherDetailsId;
	private String comments;
	private Long tdsAuthorizationId;
	private Long checkListRegistrationId;
	private Long checkListLegalOfficierId;
	private Long checkListCrmId;
	private Long checkListSalesHeadId;
	private String deviceId;
	private CustomerAddress customerAddress;
	private String requestUrl;
	private Long portNumber;
	private Long appRegId;
	
	public Long getAppRegId() {
		return appRegId;
	}

	public void setAppRegId(Long appRegId) {
		this.appRegId = appRegId;
	}
	public String getRelationNamePrefix() {
		return relationNamePrefix;
	}

	public void setRelationNamePrefix(String relationNamePrefix) {
		this.relationNamePrefix = relationNamePrefix;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Long getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(Long portNumber) {
		this.portNumber = portNumber;
	}

	public Long getFlatBookingId() {
		return flatBookingId;
	}

	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}

	public Long getFlatId() {
		return flatId;
	}
	
	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	 * @return the namePrefix
	 */
	public String getNamePrefix() {
		return namePrefix;
	}
	/**
	 * @param namePrefix the namePrefix to set
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
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
	 * @return the dob
	 */
	public Timestamp getDob() {
		return dob;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(Timestamp dob) {
		this.dob = dob;
	}
	/**
	 * @return the adharNumber
	 */
	public String getAdharNumber() {
		return adharNumber;
	}
	/**
	 * @param adharNumber the adharNumber to set
	 */
	public void setAdharNumber(String adharNumber) {
		this.adharNumber = adharNumber;
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
	 * @return the phoneNumber
	 */
	public Long getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the alternatePhoneno
	 */
	public Long getAlternatePhoneno() {
		return alternatePhoneno;
	}
	/**
	 * @param alternatePhoneno the alternatePhoneno to set
	 */
	public void setAlternatePhoneno(Long alternatePhoneno) {
		this.alternatePhoneno = alternatePhoneno;
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
	 * @return the maritalStatus
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}
	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	/**
	 * @return the documentsUpload
	 */
	public String getDocumentsUpload() {
		return documentsUpload;
	}
	/**
	 * @param documentsUpload the documentsUpload to set
	 */
	public void setDocumentsUpload(String documentsUpload) {
		this.documentsUpload = documentsUpload;
	}
	/**
	 * @return the profilePic
	 */
	public String getProfilePic() {
		return profilePic;
	}
	/**
	 * @param profilePic the profilePic to set
	 */
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	/**
	 * @return the customerProfileId
	 */
	public Long getCustomerProfileId() {
		return customerProfileId;
	}
	/**
	 * @param customerProfileId the customerProfileId to set
	 */
	public void setCustomerProfileId(Long customerProfileId) {
		this.customerProfileId = customerProfileId;
	}
	/**
	 * @return the customerAppId
	 */
	public Long getCustomerAppId() {
		return customerAppId;
	}
	/**
	 * @param customerAppId the customerAppId to set
	 */
	public void setCustomerAppId(Long customerAppId) {
		this.customerAppId = customerAppId;
	}
	/**
	 * @return the doa
	 */
	public Timestamp getDoa() {
		return doa;
	}
	/**
	 * @param doa the doa to set
	 */
	public void setDoa(Timestamp doa) {
		this.doa = doa;
	}
	/**
	 * @return the salesTeamEmployeeId
	 */
	public Long getSalesTeamEmployeeId() {
		return salesTeamEmployeeId;
	}
	/**
	 * @param salesTeamEmployeeId the salesTeamEmployeeId to set
	 */
	public void setSalesTeamEmployeeId(Long salesTeamEmployeeId) {
		this.salesTeamEmployeeId = salesTeamEmployeeId;
	}
	/**
	 * @return the salesTeamLeadId
	 */
	public Long getSalesTeamLeadId() {
		return salesTeamLeadId;
	}
	/**
	 * @param salesTeamLeadId the salesTeamLeadId to set
	 */
	public void setSalesTeamLeadId(Long salesTeamLeadId) {
		this.salesTeamLeadId = salesTeamLeadId;
	}
	/**
	 * @return the workExperince
	 */
	public String getWorkExperince() {
		return workExperince;
	}
	/**
	 * @param workExperince the workExperince to set
	 */
	public void setWorkExperince(String workExperince) {
		this.workExperince = workExperince;
	}
	/**
	 * @return the educationalQualification
	 */
	public String getEducationalQualification() {
		return educationalQualification;
	}
	/**
	 * @param educationalQualification the educationalQualification to set
	 */
	public void setEducationalQualification(String educationalQualification) {
		this.educationalQualification = educationalQualification;
	}
	/**
	 * @return the annualHouseHoldIncome
	 */
	public String getAnnualHouseHoldIncome() {
		return annualHouseHoldIncome;
	}
	/**
	 * @param annualHouseHoldIncome the annualHouseHoldIncome to set
	 */
	public void setAnnualHouseHoldIncome(String annualHouseHoldIncome) {
		this.annualHouseHoldIncome = annualHouseHoldIncome;
	}
	/**
	 * @return the customerProffisionalId
	 */
	public Long getCustomerProffisionalId() {
		return customerProffisionalId;
	}
	/**
	 * @param customerProffisionalId the customerProffisionalId to set
	 */
	public void setCustomerProffisionalId(Long customerProffisionalId) {
		this.customerProffisionalId = customerProffisionalId;
	}
	/**
	 * @return the custOtherDetailsId
	 */
	public Long getCustOtherDetailsId() {
		return custOtherDetailsId;
	}
	/**
	 * @param custOtherDetailsId the custOtherDetailsId to set
	 */
	public void setCustOtherDetailsId(Long custOtherDetailsId) {
		this.custOtherDetailsId = custOtherDetailsId;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the tdsAuthorizationId
	 */
	public Long getTdsAuthorizationId() {
		return tdsAuthorizationId;
	}
	/**
	 * @param tdsAuthorizationId the tdsAuthorizationId to set
	 */
	public void setTdsAuthorizationId(Long tdsAuthorizationId) {
		this.tdsAuthorizationId = tdsAuthorizationId;
	}
	/**
	 * @return the checkListRegistrationId
	 */
	public Long getCheckListRegistrationId() {
		return checkListRegistrationId;
	}
	/**
	 * @param checkListRegistrationId the checkListRegistrationId to set
	 */
	public void setCheckListRegistrationId(Long checkListRegistrationId) {
		this.checkListRegistrationId = checkListRegistrationId;
	}
	/**
	 * @return the checkListLegalOfficierId
	 */
	public Long getCheckListLegalOfficierId() {
		return checkListLegalOfficierId;
	}
	/**
	 * @param checkListLegalOfficierId the checkListLegalOfficierId to set
	 */
	public void setCheckListLegalOfficierId(Long checkListLegalOfficierId) {
		this.checkListLegalOfficierId = checkListLegalOfficierId;
	}
	/**
	 * @return the checkListCrmId
	 */
	public Long getCheckListCrmId() {
		return checkListCrmId;
	}
	/**
	 * @param checkListCrmId the checkListCrmId to set
	 */
	public void setCheckListCrmId(Long checkListCrmId) {
		this.checkListCrmId = checkListCrmId;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public Long getCheckListSalesHeadId() {
		return checkListSalesHeadId;
	}
	public void setCheckListSalesHeadId(Long checkListSalesHeadId) {
		this.checkListSalesHeadId = checkListSalesHeadId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", namePrefix=" + namePrefix + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", gender=" + gender + ", age=" + age + ", dob=" + dob + ", adharNumber="
				+ adharNumber + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", phoneNumber="
				+ phoneNumber + ", alternatePhoneno=" + alternatePhoneno + ", telephone=" + telephone + ", pancard="
				+ pancard + ", voterId=" + voterId + ", email=" + email + ", statusId=" + statusId + ", addressId="
				+ addressId + ", relationWith=" + relationWith + ", relationName=" + relationName + ", nationality="
				+ nationality + ", maritalStatus=" + maritalStatus + ", documentsUpload=" + documentsUpload
				+ ", profilePic=" + profilePic + ", customerProfileId=" + customerProfileId + ", customerAppId="
				+ customerAppId + ", doa=" + doa + ", salesTeamEmployeeId=" + salesTeamEmployeeId + ", salesTeamLeadId="
				+ salesTeamLeadId + ", workExperince=" + workExperince + ", educationalQualification="
				+ educationalQualification + ", annualHouseHoldIncome=" + annualHouseHoldIncome
				+ ", customerProffisionalId=" + customerProffisionalId + ", custOtherDetailsId=" + custOtherDetailsId
				+ ", comments=" + comments + ", tdsAuthorizationId=" + tdsAuthorizationId + ", checkListRegistrationId="
				+ checkListRegistrationId + ", checkListLegalOfficierId=" + checkListLegalOfficierId
				+ ", checkListCrmId=" + checkListCrmId + ", deviceId=" + deviceId + ", customerAddress="
				+ customerAddress + "]";
	}
	

}