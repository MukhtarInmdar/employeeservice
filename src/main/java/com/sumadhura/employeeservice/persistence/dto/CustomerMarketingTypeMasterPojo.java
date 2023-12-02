package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerMarketingTypeMasterPojo {

	@Column(name="MARKETING_TYPE_ID")
	private Long marketinTypeId;
	
	@Column(name="MARKETING_TYPE_NAME")
	private String maketinTypeName;
	
	@Column(name="CHANNEL_PARTNER_LEAD")
	private String channelPartnerLead;
	
	@Column(name="CHANNEL_PARTNER_ADDRESS")
	private String channelPartnerAddress;
	
	@Column(name="CHANNEL_PARTNER_NUMBER")
	private String channelPartnerNumber;
	
	@Column(name="CHANNEL_PARTNER_NAME")
	private String channelPartnerName;
	
	@Column(name="MARKETING_TYPE_STATUS")
	private String marketingTypeStatus;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	public Long getMarketinTypeId() {
		return marketinTypeId;
	}

	public void setMarketinTypeId(Long marketinTypeId) {
		this.marketinTypeId = marketinTypeId;
	}

	public String getMaketinTypeName() {
		return maketinTypeName;
	}

	public void setMaketinTypeName(String maketinTypeName) {
		this.maketinTypeName = maketinTypeName;
	}

	public String getChannelPartnerLead() {
		return channelPartnerLead;
	}

	public void setChannelPartnerLead(String channelPartnerLead) {
		this.channelPartnerLead = channelPartnerLead;
	}

	public String getChannelPartnerAddress() {
		return channelPartnerAddress;
	}

	public void setChannelPartnerAddress(String channelPartnerAddress) {
		this.channelPartnerAddress = channelPartnerAddress;
	}

	public String getChannelPartnerNumber() {
		return channelPartnerNumber;
	}

	public void setChannelPartnerNumber(String channelPartnerNumber) {
		this.channelPartnerNumber = channelPartnerNumber;
	}

	public String getChannelPartnerName() {
		return channelPartnerName;
	}

	public void setChannelPartnerName(String channelPartnerName) {
		this.channelPartnerName = channelPartnerName;
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

	public String getMarketingTypeStatus() {
		return marketingTypeStatus;
	}

	public void setMarketingTypeStatus(String marketingTypeStatus) {
		this.marketingTypeStatus = marketingTypeStatus;
	}
	
	
	
	
	
}
