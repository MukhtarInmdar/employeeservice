/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;




/**
 * CustomerAddressPojo class provides ADDRESS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 05:52PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CheckListLegalOfficerInfo {

	//private CustomerInfo customerInfo;
	private List<CustomerCheckListVerificationInfo> customerCheckListVerification=new ArrayList<CustomerCheckListVerificationInfo>();
	private List<CoApplicantCheckListVerificationInfo> coappCheckListApp = new ArrayList<CoApplicantCheckListVerificationInfo>();
	private String bankerName;
	private String bank;
	private String contact;
	private String bankerEmailAddress;
	private String offersIfAny;
	private String legelOfficerComments;
	private String legalOfficer;
	private Long empId;
	private Timestamp legalOfficeSignedate;
	private Long authorizedSignatoryId;
	private String authorizedSignatoryName;
	private Timestamp authorizedSignatoryDate;
	private Long customerId;
	private Long flatBookingId;
	private Long checkListLegalOfficierId;
	private String bankName;
	private Timestamp createdDate;
	private Long statusId;
	
	public List<CustomerCheckListVerificationInfo> getCustomerCheckListVerification() {
		return customerCheckListVerification;
	}
	public void setCustomerCheckListVerification(List<CustomerCheckListVerificationInfo> customerCheckListVerification) {
		this.customerCheckListVerification = customerCheckListVerification;
	}
	public List<CoApplicantCheckListVerificationInfo> getCoappCheckListApp() {
		return coappCheckListApp;
	}
	public void setCoappCheckListApp(List<CoApplicantCheckListVerificationInfo> coappCheckListApp) {
		this.coappCheckListApp = coappCheckListApp;
	}
	public String getBankerName() {
		return bankerName;
	}
	public void setBankerName(String bankerName) {
		this.bankerName = bankerName;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getBankerEmailAddress() {
		return bankerEmailAddress;
	}
	public void setBankerEmailAddress(String bankerEmailAddress) {
		this.bankerEmailAddress = bankerEmailAddress;
	}
	public String getOffersIfAny() {
		return offersIfAny;
	}
	public void setOffersIfAny(String offersIfAny) {
		this.offersIfAny = offersIfAny;
	}
	public String getLegelOfficerComments() {
		return legelOfficerComments;
	}
	public void setLegelOfficerComments(String legelOfficerComments) {
		this.legelOfficerComments = legelOfficerComments;
	}
	public String getLegalOfficer() {
		return legalOfficer;
	}
	public void setLegalOfficer(String legalOfficer) {
		this.legalOfficer = legalOfficer;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public Timestamp getLegalOfficeSignedate() {
		return legalOfficeSignedate;
	}
	public void setLegalOfficeSignedate(Timestamp legalOfficeSignedate) {
		this.legalOfficeSignedate = legalOfficeSignedate;
	}
	public Long getAuthorizedSignatoryId() {
		return authorizedSignatoryId;
	}
	public void setAuthorizedSignatoryId(Long authorizedSignatoryId) {
		this.authorizedSignatoryId = authorizedSignatoryId;
	}
	public String getAuthorizedSignatoryName() {
		return authorizedSignatoryName;
	}
	public void setAuthorizedSignatoryName(String authorizedSignatoryName) {
		this.authorizedSignatoryName = authorizedSignatoryName;
	}
	public Timestamp getAuthorizedSignatoryDate() {
		return authorizedSignatoryDate;
	}
	public void setAuthorizedSignatoryDate(Timestamp authorizedSignatoryDate) {
		this.authorizedSignatoryDate = authorizedSignatoryDate;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	public Long getCheckListLegalOfficierId() {
		return checkListLegalOfficierId;
	}
	public void setCheckListLegalOfficierId(Long checkListLegalOfficierId) {
		this.checkListLegalOfficierId = checkListLegalOfficierId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	
	
}
