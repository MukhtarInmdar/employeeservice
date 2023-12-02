/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.util.Date;
import java.util.List;


import lombok.Data;




/**
 * CustomerAddressPojo class provides ADDRESS Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 06.05.2019
 * @time 05:52PM
 */


@Data
public class CheckListLegalOfficer {
	
	private CustomerInfo customerInfo;
	private List<CustomerCheckListVerification> customerCheckListVerification;
	private List<ApplicantCheckListVerification> coApplicentCheckListVerification;
	private String bankerName;
	private String bankName;
	private String contact;
	private String bankerEmailAddress;
	private String offersIfAny;
	private String legelOfficerComments;
	private Long empId;
	private Date legalOfficeSignedate;
	private Long authorizedSignatoryId;
	private Date authorizedSignatoryDate;
	private Long checkListLegalOfficierId;
	private String bankerMobileNo;
	private String bankerEmail;
	private String offersDetails;
	private String comments;
	private Long legalOfficerEmployeeId;
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}
	public List<CustomerCheckListVerification> getCustomerCheckListVerification() {
		return customerCheckListVerification;
	}
	public void setCustomerCheckListVerification(List<CustomerCheckListVerification> customerCheckListVerification) {
		this.customerCheckListVerification = customerCheckListVerification;
	}
	public List<ApplicantCheckListVerification> getCoApplicentCheckListVerification() {
		return coApplicentCheckListVerification;
	}
	public void setCoApplicentCheckListVerification(List<ApplicantCheckListVerification> coApplicentCheckListVerification) {
		this.coApplicentCheckListVerification = coApplicentCheckListVerification;
	}
	public String getBankerName() {
		return bankerName;
	}
	public void setBankerName(String bankerName) {
		this.bankerName = bankerName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public Date getLegalOfficeSignedate() {
		return legalOfficeSignedate;
	}
	public void setLegalOfficeSignedate(Date legalOfficeSignedate) {
		this.legalOfficeSignedate = legalOfficeSignedate;
	}
	public Long getAuthorizedSignatoryId() {
		return authorizedSignatoryId;
	}
	public void setAuthorizedSignatoryId(Long authorizedSignatoryId) {
		this.authorizedSignatoryId = authorizedSignatoryId;
	}
	public Date getAuthorizedSignatoryDate() {
		return authorizedSignatoryDate;
	}
	public void setAuthorizedSignatoryDate(Date authorizedSignatoryDate) {
		this.authorizedSignatoryDate = authorizedSignatoryDate;
	}
	public Long getCheckListLegalOfficierId() {
		return checkListLegalOfficierId;
	}
	public void setCheckListLegalOfficierId(Long checkListLegalOfficierId) {
		this.checkListLegalOfficierId = checkListLegalOfficierId;
	}
	public String getBankerMobileNo() {
		return bankerMobileNo;
	}
	public void setBankerMobileNo(String bankerMobileNo) {
		this.bankerMobileNo = bankerMobileNo;
	}
	public String getBankerEmail() {
		return bankerEmail;
	}
	public void setBankerEmail(String bankerEmail) {
		this.bankerEmail = bankerEmail;
	}
	public String getOffersDetails() {
		return offersDetails;
	}
	public void setOffersDetails(String offersDetails) {
		this.offersDetails = offersDetails;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getLegalOfficerEmployeeId() {
		return legalOfficerEmployeeId;
	}
	public void setLegalOfficerEmployeeId(Long legalOfficerEmployeeId) {
		this.legalOfficerEmployeeId = legalOfficerEmployeeId;
	}

}
