package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerTimeFrameToPurchaseMasterPojo {

	@Column(name="TIME_FRAME_TO_PURCHASE_ID")
	private Long timeFrameToPurchaseId;
	
	@Column(name="TIME_FRAME")
	private String timeFrame;
	
	@Column(name="TIME_FRAME_STATUS")
	private String timeFrameStatus;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;

	public Long getTimeFrameToPurchaseId() {
		return timeFrameToPurchaseId;
	}

	public void setTimeFrameToPurchaseId(Long timeFrameToPurchaseId) {
		this.timeFrameToPurchaseId = timeFrameToPurchaseId;
	}

	public String getTimeFrame() {
		return timeFrame;
	}

	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}

	public String getTimeFrameStatus() {
		return timeFrameStatus;
	}

	public void setTimeFrameStatus(String timeFrameStatus) {
		this.timeFrameStatus = timeFrameStatus;
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
	
	
	
	
	
	
}
