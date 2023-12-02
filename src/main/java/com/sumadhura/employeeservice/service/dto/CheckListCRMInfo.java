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
public class CheckListCRMInfo {

	private List<CustomerCheckListVerificationInfo> customerCheckListVerification= new ArrayList<CustomerCheckListVerificationInfo>();
	private Long checkListCrmId;
	private String commitmentsFromSTM;
	private String crmRemarks;
	private String crmPreferenceBankLoan;
	private String comment;
	private Timestamp expectedAgreeDate;
	private String expectedAgreeDateComment;
	private Timestamp crmSignedDate;
	private Long authorizedSignatoryeId;
	private String authorizedSignatoryeName;
	private Timestamp authorizedSignatoryDate;
	private Long crmEmpID;
	private String crmVerifiedByName;
	private Long flatBookingId;
	private String crmSignedName;
	private String welcomeCallRecord;
	private Long customerId;
	private Timestamp createdDate;
	private Long statusId;
	
	public List<CustomerCheckListVerificationInfo> getCustomerCheckListVerification() {
		return customerCheckListVerification;
	}
	public void setCustomerCheckListVerification(List<CustomerCheckListVerificationInfo> customerCheckListVerification) {
		this.customerCheckListVerification = customerCheckListVerification;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Timestamp getExpectedAgreeDate() {
		return expectedAgreeDate;
	}
	public void setExpectedAgreeDate(Timestamp expectedAgreeDate) {
		this.expectedAgreeDate = expectedAgreeDate;
	}
	public String getExpectedAgreeDateComment() {
		return expectedAgreeDateComment;
	}
	public void setExpectedAgreeDateComment(String expectedAgreeDateComment) {
		this.expectedAgreeDateComment = expectedAgreeDateComment;
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
	public String getAuthorizedSignatoryeName() {
		return authorizedSignatoryeName;
	}
	public void setAuthorizedSignatoryeName(String authorizedSignatoryeName) {
		this.authorizedSignatoryeName = authorizedSignatoryeName;
	}
	public Timestamp getAuthorizedSignatoryDate() {
		return authorizedSignatoryDate;
	}
	public void setAuthorizedSignatoryDate(Timestamp authorizedSignatoryDate) {
		this.authorizedSignatoryDate = authorizedSignatoryDate;
	}
	public Long getCrmEmpID() {
		return crmEmpID;
	}
	public void setCrmEmpID(Long crmEmpID) {
		this.crmEmpID = crmEmpID;
	}
	public String getCrmVerifiedByName() {
		return crmVerifiedByName;
	}
	public void setCrmVerifiedByName(String crmVerifiedByName) {
		this.crmVerifiedByName = crmVerifiedByName;
	}
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	public String getCrmSignedName() {
		return crmSignedName;
	}
	public void setCrmSignedName(String crmSignedName) {
		this.crmSignedName = crmSignedName;
	}
	public String getWelcomeCallRecord() {
		return welcomeCallRecord;
	}
	public void setWelcomeCallRecord(String welcomeCallRecord) {
		this.welcomeCallRecord = welcomeCallRecord;
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
	
}
