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
public class CheckListCRM {
	
	private List<CustomerCheckListVerification> customerCheckListVerification;
	private String crmCommitments;
	private String crmRemarks;
	private String crmPreferenceBankLoan;
	private String comment;
	private Date expectedAgreementDate;
	private Long crmEmpID;
	private Date crmSignedDate;
	private Long authorizedSignatoryeId;
	private Date authorizedSignatoryDate;
	public List<CustomerCheckListVerification> getCustomerCheckListVerification() {
		return customerCheckListVerification;
	}
	public void setCustomerCheckListVerification(List<CustomerCheckListVerification> customerCheckListVerification) {
		this.customerCheckListVerification = customerCheckListVerification;
	}
	public String getCrmCommitments() {
		return crmCommitments;
	}
	public void setCrmCommitments(String crmCommitments) {
		this.crmCommitments = crmCommitments;
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
	public Date getExpectedAgreementDate() {
		return expectedAgreementDate;
	}
	public void setExpectedAgreementDate(Date expectedAgreementDate) {
		this.expectedAgreementDate = expectedAgreementDate;
	}
	public Long getCrmEmpID() {
		return crmEmpID;
	}
	public void setCrmEmpID(Long crmEmpID) {
		this.crmEmpID = crmEmpID;
	}
	public Date getCrmSignedDate() {
		return crmSignedDate;
	}
	public void setCrmSignedDate(Date crmSignedDate) {
		this.crmSignedDate = crmSignedDate;
	}
	public Long getAuthorizedSignatoryeId() {
		return authorizedSignatoryeId;
	}
	public void setAuthorizedSignatoryeId(Long authorizedSignatoryeId) {
		this.authorizedSignatoryeId = authorizedSignatoryeId;
	}
	public Date getAuthorizedSignatoryDate() {
		return authorizedSignatoryDate;
	}
	public void setAuthorizedSignatoryDate(Date authorizedSignatoryDate) {
		this.authorizedSignatoryDate = authorizedSignatoryDate;
	}
	
	
	
}
