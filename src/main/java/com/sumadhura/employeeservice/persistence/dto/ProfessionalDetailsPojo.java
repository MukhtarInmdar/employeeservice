package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.ToString;

@Entity
@ToString
public class ProfessionalDetailsPojo {

	
	@Column(name="CUST_PROFFISIONAL_ID")
	private Long custProffisionalId;
	@Column(name="DESIGNATION")
	private String designation;
	@Column(name="ADDRESS_OF_ORGANIZATION")
	private String addressOfOrganization;
	@Column(name="OFFICE_NUMBER")
	private String officeNumber;
	@Column(name="OFFICE_EMAIL_ID")
	private String officeEmailId;
	@Column(name="ORGANIZATION_ID")
	private Long organizationTypeId;
	@Column(name="ORGANIZATION_TYPE_OTHERS")
	private String ifOtherOrgTypeName;
	@Column(name="SECTOR_ID")
	private Long workSectorId;
	@Column(name="SECTOR_OTHERS")
	private String ifOtherWorkSectorName;
	@Column(name="WORKFUNCTION_ID")
	private Long workFunctionId;
	@Column(name="WORKFUNCTION_OTHERS")
	private String ifOtherworkFunctionName;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="YRS_OF_EXP")
	private String yearsOfExperience;
	@Column(name="NAME_OF_ORGANIZATION")
	private String nameOfOrganization;
	
	
	public String getNameOfOrganization() {
		return nameOfOrganization;
	}
	public void setNameOfOrganization(String nameOfOrganization) {
		this.nameOfOrganization = nameOfOrganization;
	}
	public Long getCustProffisionalId() {
		return custProffisionalId;
	}
	public void setCustProffisionalId(Long custProffisionalId) {
		this.custProffisionalId = custProffisionalId;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getAddressOfOrganization() {
		return addressOfOrganization;
	}
	public void setAddressOfOrganization(String addressOfOrganization) {
		this.addressOfOrganization = addressOfOrganization;
	}
	public String getOfficeNumber() {
		return officeNumber;
	}
	public void setOfficeNumber(String officeNumber) {
		this.officeNumber = officeNumber;
	}
	public String getOfficeEmailId() {
		return officeEmailId;
	}
	public void setOfficeEmailId(String officeEmailId) {
		this.officeEmailId = officeEmailId;
	}
	public Long getOrganizationTypeId() {
		return organizationTypeId;
	}
	public void setOrganizationTypeId(Long organizationTypeId) {
		this.organizationTypeId = organizationTypeId;
	}
	public String getIfOtherOrgTypeName() {
		return ifOtherOrgTypeName;
	}
	public void setIfOtherOrgTypeName(String ifOtherOrgTypeName) {
		this.ifOtherOrgTypeName = ifOtherOrgTypeName;
	}
	public Long getWorkSectorId() {
		return workSectorId;
	}
	public void setWorkSectorId(Long workSectorId) {
		this.workSectorId = workSectorId;
	}
	public String getIfOtherWorkSectorName() {
		return ifOtherWorkSectorName;
	}
	public void setIfOtherWorkSectorName(String ifOtherWorkSectorName) {
		this.ifOtherWorkSectorName = ifOtherWorkSectorName;
	}
	public Long getWorkFunctionId() {
		return workFunctionId;
	}
	public void setWorkFunctionId(Long workFunctionId) {
		this.workFunctionId = workFunctionId;
	}
	public String getIfOtherworkFunctionName() {
		return ifOtherworkFunctionName;
	}
	public void setIfOtherworkFunctionName(String ifOtherworkFunctionName) {
		this.ifOtherworkFunctionName = ifOtherworkFunctionName;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getYearsOfExperience() {
		return yearsOfExperience;
	}
	public void setYearsOfExperience(String yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}

}
	

	
	

