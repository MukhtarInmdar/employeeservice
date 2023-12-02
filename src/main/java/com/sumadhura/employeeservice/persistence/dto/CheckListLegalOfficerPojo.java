package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CheckListLegalOfficerPojo {
 
	@Column(name ="CHECKLIST_LEGAL_OFFICIER_ID")
	private Long checkListLegalOfficierId;
	@Column(name ="BANKER_NAME")
	private String bankerName;
	@Column(name ="BANK_NAME")
	private String bank;
	@Column(name ="BANKER_MOBILE_NO")
	private String contact;
	@Column(name ="BANKER_EMAIL")
	private String bankerEmailAddress;
	@Column(name ="OFFERS_DETAILS")
	private String offersIfAny;
	@Column(name ="COMMENTS")
	private String legelOfficerComments;
	@Column(name ="LEGAL_OFFICIER_EMPLOYEE_ID")
	private Long empId;
	@Column(name ="LEGAL_OFFICIER_APROVED_DATE")
	private Timestamp legalOfficeSignedate;
	@Column(name ="APROVER_EMPLOYEE_ID")
	private Long authorizedSignatoryId;
	@Column(name ="APROVERA_APROVED_DATE")
	private Timestamp authorizedSignatoryDate;
	@Column(name ="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name ="STATUS_ID")
	private Long statusId;
	@Column(name ="CUSTOMER_ID")
	private Long customerId;
	@Column(name ="FLAT_BOOK_ID")
    private Long flatBookingId;
	
	
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
	public Timestamp getAuthorizedSignatoryDate() {
		return authorizedSignatoryDate;
	}
	public void setAuthorizedSignatoryDate(Timestamp authorizedSignatoryDate) {
		this.authorizedSignatoryDate = authorizedSignatoryDate;
	}
	public Long getCheckListLegalOfficierId() {
		return checkListLegalOfficierId;
	}
	public void setCheckListLegalOfficierId(Long checkListLegalOfficierId) {
		this.checkListLegalOfficierId = checkListLegalOfficierId;
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
	

	
	
	
}
