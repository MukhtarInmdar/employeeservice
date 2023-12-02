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
public class CheckListSalesHeadInfo {
	
	private List<CustomerCheckListVerificationInfo> customerCheckListVerification = new ArrayList<CustomerCheckListVerificationInfo>();
	private String name;
	private Long leadId;
	private String stm;
	private String sourceofBooking;
	private String referralBonusStatus;
	private String offersAny;
	private String availability;
	private String availabilityIfOther;
	private String salesHeadcommitments;
	private String remarks;
	private Long projectSalesheadId;
	private String projectSalesheadName;
	private Timestamp projectSalesHeadDate;
	private Long authorizedSignatoryId;
	private String authorizedSignatoryName;
	private Timestamp authorizedSignatoryDate;
	private String erpDetails;
	private Long customerId;
	private String salesTeamCommitments;
	private Long flatBookingId;
	private Long salesHeadId;
	private Timestamp createdDate;
	private Long statusId;
	
	public List<CustomerCheckListVerificationInfo> getCustomerCheckListVerification() {
		return customerCheckListVerification;
	}
	public void setCustomerCheckListVerification(List<CustomerCheckListVerificationInfo> customerCheckListVerification) {
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
	/*public String getReferralBonus() {
		return referralBonus;
	}
	public void setReferralBonus(String referralBonus) {
		this.referralBonus = referralBonus;
	}*/
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
	public String getProjectSalesheadName() {
		return projectSalesheadName;
	}
	public void setProjectSalesheadName(String projectSalesheadName) {
		this.projectSalesheadName = projectSalesheadName;
	}
	public Timestamp getProjectSalesHeadDate() {
		return projectSalesHeadDate;
	}
	public void setProjectSalesHeadDate(Timestamp projectSalesHeadDate) {
		this.projectSalesHeadDate = projectSalesHeadDate;
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
	public String getErpDetails() {
		return erpDetails;
	}
	public void setErpDetails(String erpDetails) {
		this.erpDetails = erpDetails;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getSalesTeamCommitments() {
		return salesTeamCommitments;
	}
	public void setSalesTeamCommitments(String salesTeamCommitments) {
		this.salesTeamCommitments = salesTeamCommitments;
	}
	public Long getFlatBookingId() {
		return flatBookingId;
	}
	public void setFlatBookingId(Long flatBookingId) {
		this.flatBookingId = flatBookingId;
	}
	public Long getSalesHeadId() {
		return salesHeadId;
	}
	public void setSalesHeadId(Long salesHeadId) {
		this.salesHeadId = salesHeadId;
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
	public String getAvailabilityIfOther() {
		return availabilityIfOther;
	}
	public void setAvailabilityIfOther(String availabilityIfOther) {
		this.availabilityIfOther = availabilityIfOther;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	
	
	
}
