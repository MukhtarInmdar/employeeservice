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
public class CheckListSalesHead {
	
	private CustomerInfo customerInfo;
	private List<CustomerCheckListVerification> customerCheckListVerification;
	private String name;
	private Long leadId;
	private String stm;
	private String sourceofBooking;
	private String referralBonusStatus;
	private String offersAny;
	private String availability;
	private String salesHeadcommitments;
	private String remarks;
	private Long projectSalesheadId;
	private Date projectSalesHeadDate;
	private Long authorizedSignatoryId;
	private Date authorizedSignatoryDate;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getLeadId() {
		return leadId;
	}
	public void setLeadId(Long leadId) {
		this.leadId = leadId;
	}
	public String getStm() {
		return stm;
	}
	public void setStm(String stm) {
		this.stm = stm;
	}
	public String getSourceofBooking() {
		return sourceofBooking;
	}
	public void setSourceofBooking(String sourceofBooking) {
		this.sourceofBooking = sourceofBooking;
	}
	public String getReferralBonusStatus() {
		return referralBonusStatus;
	}
	public void setReferralBonusStatus(String referralBonusStatus) {
		this.referralBonusStatus = referralBonusStatus;
	}
	public String getOffersAny() {
		return offersAny;
	}
	public void setOffersAny(String offersAny) {
		this.offersAny = offersAny;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public String getSalesHeadcommitments() {
		return salesHeadcommitments;
	}
	public void setSalesHeadcommitments(String salesHeadcommitments) {
		this.salesHeadcommitments = salesHeadcommitments;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getProjectSalesheadId() {
		return projectSalesheadId;
	}
	public void setProjectSalesheadId(Long projectSalesheadId) {
		this.projectSalesheadId = projectSalesheadId;
	}
	public Date getProjectSalesHeadDate() {
		return projectSalesHeadDate;
	}
	public void setProjectSalesHeadDate(Date projectSalesHeadDate) {
		this.projectSalesHeadDate = projectSalesHeadDate;
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
	
	
}
