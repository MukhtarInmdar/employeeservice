/**
 * 
 */
package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
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
public class CheckListRegistration {
	
	private CustomerInfo customerInfo;
	private List<CustomerCheckListVerification> customerCheckListVerification;
	private SiteInfo siteInfo;
	private FlatInfo flatInfo;
	private String agValue;
	private String sdValue;
	private String legalComments;
	private String accountsComments;
	private Long legalOfficerEmpId;
	private Date legalOfficerDate;
	private Long accountsExecutiveEmpid;
	private Date accountsExecutiveDate;
	private Long authorizedSignatureId;
	private Date authorizedDate;
	private Timestamp createdDate;
	private Long statusId;
	private Long flatBookingId;
	
	
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
	public SiteInfo getSiteInfo() {
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
	public Date getLegalOfficerDate() {
		return legalOfficerDate;
	}
	public void setLegalOfficerDate(Date legalOfficerDate) {
		this.legalOfficerDate = legalOfficerDate;
	}
	public Long getAccountsExecutiveEmpid() {
		return accountsExecutiveEmpid;
	}
	public void setAccountsExecutiveEmpid(Long accountsExecutiveEmpid) {
		this.accountsExecutiveEmpid = accountsExecutiveEmpid;
	}
	public Date getAccountsExecutiveDate() {
		return accountsExecutiveDate;
	}
	public void setAccountsExecutiveDate(Date accountsExecutiveDate) {
		this.accountsExecutiveDate = accountsExecutiveDate;
	}
	public Long getAuthorizedSignatureId() {
		return authorizedSignatureId;
	}
	public void setAuthorizedSignatureId(Long authorizedSignatureId) {
		this.authorizedSignatureId = authorizedSignatureId;
	}
	public Date getAuthorizedDate() {
		return authorizedDate;
	}
	public void setAuthorizedDate(Date authorizedDate) {
		this.authorizedDate = authorizedDate;
	}
	
}
