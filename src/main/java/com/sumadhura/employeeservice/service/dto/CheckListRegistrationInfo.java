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
 * CheckListRegistrationInfo class provides CHECKLIST_REGISTRATION Table specific fields.
 * 
 * @author Srivenu
 * @since 30.05.2019
 * @time 12:00PM
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CheckListRegistrationInfo {
	
	//private CustomerInfo customerInfo;
	private List<CustomerCheckListVerificationInfo> customerCheckListVerification= new ArrayList<CustomerCheckListVerificationInfo>();
	//private SiteInfo siteInfo;
	//private FlatInfo flatInfo;
	private String agValue;
	private String sdValue;
	private String legalComments;
	private String accountsComments;
	private Long legalOfficerEmpId;
	private String legalOfficerEmpName;
	private Timestamp legalOfficerDate;
	private Long accountsExecutiveEmpid;
	private String accountsExecutiveEmpName;
	private Timestamp accountsExecutiveDate;
	private Long authorizedSignatureId;
	private String authorizedSignatureName;
	private Timestamp authorizedDate;
	private Long sdNumber;
	private Long customerId;
    private Long flatBookingId;
    
	/*public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}*/
	public List<CustomerCheckListVerificationInfo> getCustomerCheckListVerification() {
		return customerCheckListVerification;
	}
	public void setCustomerCheckListVerification(List<CustomerCheckListVerificationInfo> customerCheckListVerification) {
		this.customerCheckListVerification = customerCheckListVerification;
	}
	/*public SiteInfo getSiteInfo() {
		return siteInfo;
	}
	public void setSiteInfo(SiteInfo siteInfo) {
		this.siteInfo = siteInfo;
	}
	public FlatInfo getFlatInfo() {
		return flatInfo;
	}
	public void setFlatInfo(FlatInfo flatInfo) {
		this.flatInfo = flatInfo;
	}*/
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
	public String getLegalOfficerEmpName() {
		return legalOfficerEmpName;
	}
	public void setLegalOfficerEmpName(String legalOfficerEmpName) {
		this.legalOfficerEmpName = legalOfficerEmpName;
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
	public String getAccountsExecutiveEmpName() {
		return accountsExecutiveEmpName;
	}
	public void setAccountsExecutiveEmpName(String accountsExecutiveEmpName) {
		this.accountsExecutiveEmpName = accountsExecutiveEmpName;
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
	public String getAuthorizedSignatureName() {
		return authorizedSignatureName;
	}
	public void setAuthorizedSignatureName(String authorizedSignatureName) {
		this.authorizedSignatureName = authorizedSignatureName;
	}
	public Timestamp getAuthorizedDate() {
		return authorizedDate;
	}
	public void setAuthorizedDate(Timestamp authorizedDate) {
		this.authorizedDate = authorizedDate;
	}
	public Long getSdNumber() {
		return sdNumber;
	}
	public void setSdNumber(Long sdNumber) {
		this.sdNumber = sdNumber;
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
