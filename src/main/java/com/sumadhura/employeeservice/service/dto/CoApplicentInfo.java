/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.Date;

import lombok.Data;




/**
 * CustomerAddressPojo class provides ADDRESS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 05:52PM
 */


@Data
public class CoApplicentInfo {
	
	
	private Long id;
	private Long custNo;
	private String addaharNo;
	private String firstname;
	private String lastname ;
	private String name ;
	private String address  ; 
	private int phone ; 
	private Long mobile    ; 
	private String email ;
	private Long customerId;
	private String namePrefix;
	private String gender;
	private Integer age;
	private Date dateOfBirth;
	private String aadharNumber;
	private Date createdDate;
	private Date updatedDate;
	private Long stateId;
	private String phoneNo;
	private Integer pincode  ; 
	private Integer alternatePhoneNo;
	private String telephone;
	private String pancard;
	private String voterId;
	private Integer statusId;
	private Integer addressId;
	private String relationWith;
	private String relationname;
	private String nationality;
	private String maritalStatus;
	private String documentsUpload;
	private String profilePic;
	private Integer customerProfileId;
	private String deviceId;
	private Long siteId;
	private Long flatId;
	private Long cityId;
	private String relationNamePrefix;
	private String relationName;
	private String termsAndConditionVersion;
	
	
	
	private Date dateOfAniversery;
	private Date educationQualification;
	private String annualHouseHoldIncome;
	private  String customerComments;
	private String workExperience;
	
	private Long professionId;
	
	
	
	public Long getProfessionId() {
		return professionId;
	}
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustNo() {
		return custNo;
	}
	public void setCustNo(Long custNo) {
		this.custNo = custNo;
	}
	public String getAddaharNo() {
		return addaharNo;
	}
	public void setAddaharNo(String addaharNo) {
		this.addaharNo = addaharNo;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getNamePrefix() {
		return namePrefix;
	}
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAadharNumber() {
		return aadharNumber;
	}
	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Integer getPincode() {
		return pincode;
	}
	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}
	public Integer getAlternatePhoneNo() {
		return alternatePhoneNo;
	}
	public void setAlternatePhoneNo(Integer alternatePhoneNo) {
		this.alternatePhoneNo = alternatePhoneNo;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPancard() {
		return pancard;
	}
	public void setPancard(String pancard) {
		this.pancard = pancard;
	}
	public String getVoterId() {
		return voterId;
	}
	public void setVoterId(String voterId) {
		this.voterId = voterId;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	public String getRelationWith() {
		return relationWith;
	}
	public void setRelationWith(String relationWith) {
		this.relationWith = relationWith;
	}
	public String getRelationname() {
		return relationname;
	}
	public void setRelationname(String relationname) {
		this.relationname = relationname;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getDocumentsUpload() {
		return documentsUpload;
	}
	public void setDocumentsUpload(String documentsUpload) {
		this.documentsUpload = documentsUpload;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	public Integer getCustomerProfileId() {
		return customerProfileId;
	}
	public void setCustomerProfileId(Integer customerProfileId) {
		this.customerProfileId = customerProfileId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public Long getFlatId() {
		return flatId;
	}
	public void setFlatId(Long flatId) {
		this.flatId = flatId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getTermsAndConditionVersion() {
		return termsAndConditionVersion;
	}
	public void setTermsAndConditionVersion(String termsAndConditionVersion) {
		this.termsAndConditionVersion = termsAndConditionVersion;
	}
	public Date getDateOfAniversery() {
		return dateOfAniversery;
	}
	public void setDateOfAniversery(Date dateOfAniversery) {
		this.dateOfAniversery = dateOfAniversery;
	}
	public Date getEducationQualification() {
		return educationQualification;
	}
	public void setEducationQualification(Date educationQualification) {
		this.educationQualification = educationQualification;
	}
	public String getAnnualHouseHoldIncome() {
		return annualHouseHoldIncome;
	}
	public void setAnnualHouseHoldIncome(String annualHouseHoldIncome) {
		this.annualHouseHoldIncome = annualHouseHoldIncome;
	}
	public String getCustomerComments() {
		return customerComments;
	}
	public void setCustomerComments(String customerComments) {
		this.customerComments = customerComments;
	}
	public String getWorkExperience() {
		return workExperience;
	}
	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getRelationNamePrefix() {
		return relationNamePrefix;
	}
	public void setRelationNamePrefix(String relationNamePrefix) {
		this.relationNamePrefix = relationNamePrefix;
	}
	

	
}
