package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity
public class CheckListRegistrationPojo {

	@Column(name = "CHECKLIST_REGISTRATION_ID")
	private String checklistRegistrationID;
	@Column(name = "AG_VALUE")
	private String agValue;
	@Column(name = "SD_VALUE")
	private String sdValue;
	@Column(name = "SD_NUMBER")
	private Long sdNumber;
	@Column(name = "COMMENTS_LEGAL")
	private String legalComments;
	@Column(name = "COMMENTS_ACCOUNTS")
	private String accountsComments;
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	@Column(name = "LEGAL_OFFICIER_EMPLOYEE_ID")
	private Long legalOfficerEmpId;
	@Column(name = "LEGAL_OFFICIER_APROVED_DATE")
	private Timestamp legalOfficerDate;
	@Column(name = "ACCOUNTS_EMPLOYEEID")
	private Long accountsExecutiveEmpid;
	@Column(name = "ACCOUNT_APROVED_DATE")
	private Timestamp accountsExecutiveDate;
	@Column(name = "APROVER_EMPLOYEE_ID")
	private Long authorizedSignatureId;
	@Column(name = "APROVER_APROVED_DATE")
	private Timestamp authorizedDate;
	@Column(name = "CUSTOMER_ID")
	private Long customerId;
	@Column(name = "STATUS_ID")
	private Long statusId;
	@Column(name ="FLAT_BOOK_ID")
    private Long flatBookingId;

	
	public String getChecklistRegistrationID() {
		return checklistRegistrationID;
	}
	public void setChecklistRegistrationID(String checklistRegistrationID) {
		this.checklistRegistrationID = checklistRegistrationID;
	}
	public String getAgValue() {
		return agValue;
	}
	public void setAgValue(String agValue) {
		this.agValue = agValue;
	}
	public String getSdValue() {
		return sdValue;
	}
	public void setSdValue(String sdValue) {
		this.sdValue = sdValue;
	}
	public Long getSdNumber() {
		return sdNumber;
	}
	public void setSdNumber(Long sdNumber) {
		this.sdNumber = sdNumber;
	}
	public String getLegalComments() {
		return legalComments;
	}
	public void setLegalComments(String legalComments) {
		this.legalComments = legalComments;
	}
	public String getAccountsComments() {
		return accountsComments;
	}
	public void setAccountsComments(String accountsComments) {
		this.accountsComments = accountsComments;
	}
	public Long getLegalOfficerEmpId() {
		return legalOfficerEmpId;
	}
	public void setLegalOfficerEmpId(Long legalOfficerEmpId) {
		this.legalOfficerEmpId = legalOfficerEmpId;
	}
	public Timestamp getLegalOfficerDate() {
		return legalOfficerDate;
	}
	public void setLegalOfficerDate(Timestamp legalOfficerDate) {
		this.legalOfficerDate = legalOfficerDate;
	}
	public Long getAccountsExecutiveEmpid() {
		return accountsExecutiveEmpid;
	}
	public void setAccountsExecutiveEmpid(Long accountsExecutiveEmpid) {
		this.accountsExecutiveEmpid = accountsExecutiveEmpid;
	}
	public Timestamp getAccountsExecutiveDate() {
		return accountsExecutiveDate;
	}
	public void setAccountsExecutiveDate(Timestamp accountsExecutiveDate) {
		this.accountsExecutiveDate = accountsExecutiveDate;
	}
	public Long getAuthorizedSignatureId() {
		return authorizedSignatureId;
	}
	public void setAuthorizedSignatureId(Long authorizedSignatureId) {
		this.authorizedSignatureId = authorizedSignatureId;
	}
	public Timestamp getAuthorizedDate() {
		return authorizedDate;
	}
	public void setAuthorizedDate(Timestamp authorizedDate) {
		this.authorizedDate = authorizedDate;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	
}
