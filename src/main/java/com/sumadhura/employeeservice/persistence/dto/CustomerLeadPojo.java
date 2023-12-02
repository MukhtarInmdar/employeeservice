package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerLeadPojo {

	@Column(name="LEAD_ID")
	private Long leadId;
	
	@Column(name="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name="COMPANY")
	private String company;
	
	@Column(name="DESIGNATION")
	private String designation;
	
	@Column(name="REGISTERED_CREATED")
	private Timestamp registeredCreated;
	
	@Column(name="LEAD_CREATED_TO_SITE_SCHEDULED")
	private String leadCreatedToSiteScheduled;
		
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="ALTERNATE_EMAIL1")
	private String aleternativeEmail1;
	
	@Column(name="ALTERNATE_EMAIL2")
	private String alternativeEmail2;
	
	@Column(name="MOBILE")
	private Long mobile;
	
	@Column(name="ADDITIONAL_MOBILE1")
	private Long additionalMobile1;
	
	@Column(name="ADDITIONAL_MOBILE2")
	private Long additionalMobile2;
	
	@Column(name="PHONE")
	private Long phone;
		
	@Column(name="EXTENSION")
	private Long extension;
	
	@Column(name="PROJECT_ID")
	private Long projectId;
	
	@Column(name="PREFERED_PROJECT_LOCATION")
	private Long preferdProjectLocation;
	
	@Column(name="FIRST_SOURCE_ID")
	private Long firstSourceId;
	
	@Column(name="LAST_SOURCE_ID")
	private Long lastSourceId;

	@Column(name="LEAD_OWNER_ID")
	private Long leadOwnerId;


	@Column(name="MIN_BUDGET")
	private Long minBudget;

	@Column(name="MAX_BUDGET")
	private Long maxBudget;

	@Column(name="BUDGET_RANGE")
	private String budgetRange;
	
	@Column(name="REQUIREMENT_TYPE")
	private String requirementType;
	
	@Column(name="MIN_FLAT_AREA")
	private Long minFlate;
	
	@Column(name="MIN_FLAT_AREA")
	private Long MAX_FLAT_AREA;
	
	@Column(name="TIME_FRAME_TO_PURCHASE")
	private Long timeFrameToPurchase;
	
	@Column(name="HOUSING_REQUIREMENT")
	private Long housingRequirement;

	@Column(name="customerComments")
	private String customerComments;

	@Column(name="CUSTOMER_ADDRESS_AREA")
	private String customerAddressArea;

	@Column(name="CUSTOMER_LOCALITY")
	private String customerLocality;

	@Column(name="CUSTOMER_ALTERNATE_ADDRESS")
	private String customerAlterAddress;

	@Column(name="CUSTOMER_CITY")
	private int customerCity;

	@Column(name="CUSTOMER_STATE")
	private int customerState;

	@Column(name="MARKETING_ID")
	private Long marketingId;

	@Column(name="LEAD_CREATION_STATUS")
	private String leadCreationStatus;

	@Column(name="LEAD_SUB_STATUS_ID")
	private int leadSubStatusId;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="LEAD_SUB_STATUS_TYPE")
	private String leadSubStatusType;

	@Column(name="LEAD_SUB_STATUS")
	private String leadSubstatus;

	@Column(name="LEAD_TASK_COMMENTS")
	private String leadTaskComments;

	@Column(name="CHANNEL_PARTNER_LEAD_ID")
	private String channelPartnerLeadId;

	@Column(name="CHANNEL_PARTNER_NAME")
	private String channelPartnerName;

	@Column(name="CHANNEL_PARTNER_ADDRESS")
	private String channelPartnerAddres;

	@Column(name="CHANNEL_PARTNER_NUMBER")
	private Long channelPartnerNumber;

	@Column(name="LEAD_INACTIVE_COMMENTS")
	private String leadInactiveComments;

	@Column(name="MARK_AS_SITE_VISIT")
	private String markAsSiteVisit;

	@Column(name="MARK_AS_BOOKING")
	private String markAsBooking;
	
	@Column(name="PROJECT_NAME")
	private String projectName;
	
	@Column(name="SALES_REPLY")
	private String salesReply;

	@Column(name="MEET_COMMENTS")
	private String meetComments;
	
	@Column(name="total_lead")
	private int totalLead;
	
	@Column(name="total_lead_generated")
	private int totalLeadGenerated;
	
	@Column(name="total_lead_opportunity")
	private int totalLeadOpportunity;
	
	@Column(name="total_lead_booking")
	private int totalLeadBooking;
	
	public Long getLeadId() {
		return leadId;
	}

	public void setLeadId(Long leadId) {
		this.leadId = leadId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Timestamp getRegisteredCreated() {
		return registeredCreated;
	}

	public void setRegisteredCreated(Timestamp registeredCreated) {
		this.registeredCreated = registeredCreated;
	}

	public String getLeadCreatedToSiteScheduled() {
		return leadCreatedToSiteScheduled;
	}

	public void setLeadCreatedToSiteScheduled(String leadCreatedToSiteScheduled) {
		this.leadCreatedToSiteScheduled = leadCreatedToSiteScheduled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAleternativeEmail1() {
		return aleternativeEmail1;
	}

	public void setAleternativeEmail1(String aleternativeEmail1) {
		this.aleternativeEmail1 = aleternativeEmail1;
	}

	public String getAlternativeEmail2() {
		return alternativeEmail2;
	}

	public void setAlternativeEmail2(String alternativeEmail2) {
		this.alternativeEmail2 = alternativeEmail2;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public Long getAdditionalMobile1() {
		return additionalMobile1;
	}

	public void setAdditionalMobile1(Long additionalMobile1) {
		this.additionalMobile1 = additionalMobile1;
	}

	public Long getAdditionalMobile2() {
		return additionalMobile2;
	}

	public void setAdditionalMobile2(Long additionalMobile2) {
		this.additionalMobile2 = additionalMobile2;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public Long getExtension() {
		return extension;
	}

	public void setExtension(Long extension) {
		this.extension = extension;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getPreferdProjectLocation() {
		return preferdProjectLocation;
	}

	public void setPreferdProjectLocation(Long preferdProjectLocation) {
		this.preferdProjectLocation = preferdProjectLocation;
	}

	public Long getFirstSourceId() {
		return firstSourceId;
	}

	public void setFirstSourceId(Long firstSourceId) {
		this.firstSourceId = firstSourceId;
	}

	public Long getLastSourceId() {
		return lastSourceId;
	}

	public void setLastSourceId(Long lastSourceId) {
		this.lastSourceId = lastSourceId;
	}

	public Long getLeadOwnerId() {
		return leadOwnerId;
	}

	public void setLeadOwnerId(Long leadOwnerId) {
		this.leadOwnerId = leadOwnerId;
	}

	public Long getMinBudget() {
		return minBudget;
	}

	public void setMinBudget(Long minBudget) {
		this.minBudget = minBudget;
	}

	public Long getMaxBudget() {
		return maxBudget;
	}

	public void setMaxBudget(Long maxBudget) {
		this.maxBudget = maxBudget;
	}

	public String getBudgetRange() {
		return budgetRange;
	}

	public void setBudgetRange(String budgetRange) {
		this.budgetRange = budgetRange;
	}

	public String getRequirementType() {
		return requirementType;
	}

	public void setRequirementType(String requirementType) {
		this.requirementType = requirementType;
	}

	public Long getMinFlate() {
		return minFlate;
	}

	public void setMinFlate(Long minFlate) {
		this.minFlate = minFlate;
	}

	public Long getMAX_FLAT_AREA() {
		return MAX_FLAT_AREA;
	}

	public void setMAX_FLAT_AREA(Long mAX_FLAT_AREA) {
		MAX_FLAT_AREA = mAX_FLAT_AREA;
	}

	public Long getTimeFrameToPurchase() {
		return timeFrameToPurchase;
	}

	public void setTimeFrameToPurchase(Long timeFrameToPurchase) {
		this.timeFrameToPurchase = timeFrameToPurchase;
	}

	public Long getHousingRequirement() {
		return housingRequirement;
	}

	public void setHousingRequirement(Long housingRequirement) {
		this.housingRequirement = housingRequirement;
	}

	public String getCustomerComments() {
		return customerComments;
	}

	public void setCustomerComments(String customerComments) {
		this.customerComments = customerComments;
	}

	public String getCustomerAddressArea() {
		return customerAddressArea;
	}

	public void setCustomerAddressArea(String customerAddressArea) {
		this.customerAddressArea = customerAddressArea;
	}

	public String getCustomerLocality() {
		return customerLocality;
	}

	public void setCustomerLocality(String customerLocality) {
		this.customerLocality = customerLocality;
	}

	public String getCustomerAlterAddress() {
		return customerAlterAddress;
	}

	public void setCustomerAlterAddress(String customerAlterAddress) {
		this.customerAlterAddress = customerAlterAddress;
	}

	public int getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(int customerCity) {
		this.customerCity = customerCity;
	}

	public int getCustomerState() {
		return customerState;
	}

	public void setCustomerState(int customerState) {
		this.customerState = customerState;
	}

	public Long getMarketingId() {
		return marketingId;
	}

	public void setMarketingId(Long marketingId) {
		this.marketingId = marketingId;
	}

	public String getLeadCreationStatus() {
		return leadCreationStatus;
	}

	public void setLeadCreationStatus(String leadCreationStatus) {
		this.leadCreationStatus = leadCreationStatus;
	}

	public int getLeadSubStatusId() {
		return leadSubStatusId;
	}

	public void setLeadSubStatusId(int leadSubStatusId) {
		this.leadSubStatusId = leadSubStatusId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getLeadSubStatusType() {
		return leadSubStatusType;
	}

	public void setLeadSubStatusType(String leadSubStatusType) {
		this.leadSubStatusType = leadSubStatusType;
	}

	public String getLeadSubstatus() {
		return leadSubstatus;
	}

	public void setLeadSubstatus(String leadSubstatus) {
		this.leadSubstatus = leadSubstatus;
	}

	public String getLeadTaskComments() {
		return leadTaskComments;
	}

	public void setLeadTaskComments(String leadTaskComments) {
		this.leadTaskComments = leadTaskComments;
	}

	public String getChannelPartnerLeadId() {
		return channelPartnerLeadId;
	}

	public void setChannelPartnerLeadId(String channelPartnerLeadId) {
		this.channelPartnerLeadId = channelPartnerLeadId;
	}

	public String getChannelPartnerName() {
		return channelPartnerName;
	}

	public void setChannelPartnerName(String channelPartnerName) {
		this.channelPartnerName = channelPartnerName;
	}



	public Long getChannelPartnerNumber() {
		return channelPartnerNumber;
	}

	public void setChannelPartnerNumber(Long channelPartnerNumber) {
		this.channelPartnerNumber = channelPartnerNumber;
	}

	public String getLeadInactiveComments() {
		return leadInactiveComments;
	}

	public void setLeadInactiveComments(String leadInactiveComments) {
		this.leadInactiveComments = leadInactiveComments;
	}

	public String getMarkAsSiteVisit() {
		return markAsSiteVisit;
	}

	public void setMarkAsSiteVisit(String markAsSiteVisit) {
		this.markAsSiteVisit = markAsSiteVisit;
	}

	public String getMarkAsBooking() {
		return markAsBooking;
	}

	public void setMarkAsBooking(String markAsBooking) {
		this.markAsBooking = markAsBooking;
	}

	public String getChannelPartnerAddres() {
		return channelPartnerAddres;
	}

	public void setChannelPartnerAddres(String channelPartnerAddres) {
		this.channelPartnerAddres = channelPartnerAddres;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSalesReply() {
		return salesReply;
	}

	public void setSalesReply(String salesReply) {
		this.salesReply = salesReply;
	}

	public String getMeetComments() {
		return meetComments;
	}

	public void setMeetComments(String meetComments) {
		this.meetComments = meetComments;
	}

	public int getTotalLead() {
		return totalLead;
	}

	public void setTotalLead(int totalLead) {
		this.totalLead = totalLead;
	}

	public int getTotalLeadGenerated() {
		return totalLeadGenerated;
	}

	public void setTotalLeadGenerated(int totalLeadGenerated) {
		this.totalLeadGenerated = totalLeadGenerated;
	}

	public int getTotalLeadOpportunity() {
		return totalLeadOpportunity;
	}

	public void setTotalLeadOpportunity(int totalLeadOpportunity) {
		this.totalLeadOpportunity = totalLeadOpportunity;
	}

	public int getTotalLeadBooking() {
		return totalLeadBooking;
	}

	public void setTotalLeadBooking(int totalLeadBooking) {
		this.totalLeadBooking = totalLeadBooking;
	}

	

	
}
