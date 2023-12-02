package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class ChannelPartnerMasterPojo {

	@Column(name="CHANNEL_PARTNER_MASTER_ID")
	private Long channelPartnerId;
	@Column(name="CP_ID")
	private String channelPartnerCPID;
	@Column(name="CP_COMPANY")
	private String channelPartnerCompanyName;
	@Column(name="CP_NAME")
	private String channelPartnerName;
	@Column(name="CP_RERA_NO")
	private String channelPartnerRERANO;
	@Column(name="STATUS_ID")
	private String statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	public Long getChannelPartnerId() {
		return channelPartnerId;
	}
	public void setChannelPartnerId(Long channelPartnerId) {
		this.channelPartnerId = channelPartnerId;
	}
	public String getChannelPartnerCPID() {
		return channelPartnerCPID;
	}
	public void setChannelPartnerCPID(String channelPartnerCPID) {
		this.channelPartnerCPID = channelPartnerCPID;
	}
	public String getChannelPartnerCompanyName() {
		return channelPartnerCompanyName;
	}
	public void setChannelPartnerCompanyName(String channelPartnerCompanyName) {
		this.channelPartnerCompanyName = channelPartnerCompanyName;
	}
	public String getChannelPartnerName() {
		return channelPartnerName;
	}
	public void setChannelPartnerName(String channelPartnerName) {
		this.channelPartnerName = channelPartnerName;
	}
	public String getChannelPartnerRERANO() {
		return channelPartnerRERANO;
	}
	public void setChannelPartnerRERANO(String channelPartnerRERANO) {
		this.channelPartnerRERANO = channelPartnerRERANO;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}	
	
}
