package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ChecklistCrmPojo {

	@Column(name ="CHECKLIST_CRM_ID")
    private Long checkListCrmId;
	@Column(name ="SALES_TEAM_COMMITMENTS")
    private String commitmentsFromSTM;
	@Column(name ="REMARKS")
    private String crmRemarks;
	@Column(name ="PREFER_BANK_NAME")
   	private String crmPreferenceBankLoan;
	@Column(name ="AGREEMENT_COMMENTS")
    private String expectedAgreeDateComment;
	@Column(name ="CRM_EMPLOYEE_ID")
    private Long crmEmpID;
	@Column(name ="CRM_APPROVED_DATE")
    private Timestamp crmSignedDate;
	@Column(name ="APROVER_EMPLOYEE_ID")
    private Long authorizedSignatoryeId;
	@Column(name ="APROVER_APROVED_DATE")
  	private Timestamp authorizedSignatoryDate;
	@Column(name ="EXPECTED_AGREMENT_DATE")
    private Timestamp expectedAgreeDate;
	@Column(name ="CUSTOMER_ID")
    private Long customerId;
	@Column(name ="CREATED_DATE")
    private Timestamp createdDate;
	@Column(name ="STATUS_ID")
    private Long statusId;
	@Column(name ="FLAT_BOOK_ID")
    private Long flatBookingId;
	@Column(name ="WELCOME_CALL_RECORD")
    private String welcomeCallRecord;
	
	public String getWelcomeCallRecord() {
		return welcomeCallRecord;
	}
	public void setWelcomeCallRecord(String welcomeCallRecord) {
		this.welcomeCallRecord = welcomeCallRecord;
	}
	public Long getCheckListCrmId() {
		return checkListCrmId;
	}
	public void setCheckListCrmId(Long checkListCrmId) {
		this.checkListCrmId = checkListCrmId;
	}
	public String getCommitmentsFromSTM() {
		return commitmentsFromSTM;
	}
	public void setCommitmentsFromSTM(String commitmentsFromSTM) {
		this.commitmentsFromSTM = commitmentsFromSTM;
	}
	public String getCrmRemarks() {
		return crmRemarks;
	}
	public void setCrmRemarks(String crmRemarks) {
		this.crmRemarks = crmRemarks;
	}
	public String getCrmPreferenceBankLoan() {
		return crmPreferenceBankLoan;
	}
	public void setCrmPreferenceBankLoan(String crmPreferenceBankLoan) {
		this.crmPreferenceBankLoan = crmPreferenceBankLoan;
	}
	public String getExpectedAgreeDateComment() {
		return expectedAgreeDateComment;
	}
	public void setExpectedAgreeDateComment(String expectedAgreeDateComment) {
		this.expectedAgreeDateComment = expectedAgreeDateComment;
	}
	public Long getCrmEmpID() {
		return crmEmpID;
	}
	public void setCrmEmpID(Long crmEmpID) {
		this.crmEmpID = crmEmpID;
	}
	public Timestamp getCrmSignedDate() {
		return crmSignedDate;
	}
	public void setCrmSignedDate(Timestamp crmSignedDate) {
		this.crmSignedDate = crmSignedDate;
	}
	public Long getAuthorizedSignatoryeId() {
		return authorizedSignatoryeId;
	}
	public void setAuthorizedSignatoryeId(Long authorizedSignatoryeId) {
		this.authorizedSignatoryeId = authorizedSignatoryeId;
	}
	public Timestamp getAuthorizedSignatoryDate() {
		return authorizedSignatoryDate;
	}
	public void setAuthorizedSignatoryDate(Timestamp authorizedSignatoryDate) {
		this.authorizedSignatoryDate = authorizedSignatoryDate;
	}
	public Timestamp getExpectedAgreeDate() {
		return expectedAgreeDate;
	}
	public void setExpectedAgreeDate(Timestamp expectedAgreeDate) {
		this.expectedAgreeDate = expectedAgreeDate;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	
	
}
