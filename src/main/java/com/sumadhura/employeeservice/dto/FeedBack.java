package com.sumadhura.employeeservice.dto;

public class FeedBack extends Result {

	private static final long serialVersionUID = 7942966704808491147L;
	private Integer id;
	private String feedbackMsg;
	private String feedbackBy;
	private String feedbackProfile;
	private String deviceToken;
	private Long custId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFeedbackMsg() {
		return feedbackMsg;
	}
	public void setFeedbackMsg(String feedbackMsg) {
		this.feedbackMsg = feedbackMsg;
	}
	public String getFeedbackBy() {
		return feedbackBy;
	}
	public void setFeedbackBy(String feedbackBy) {
		this.feedbackBy = feedbackBy;
	}
	public String getFeedbackProfile() {
		return feedbackProfile;
	}
	public void setFeedbackProfile(String feedbackProfile) {
		this.feedbackProfile = feedbackProfile;
	}
	
	public String getDeviceToken() {
		return deviceToken;
	}
	
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedBack [id=" + id + ", feedbackMsg=" + feedbackMsg + ", feedbackBy=" + feedbackBy
				+ ", feedbackProfile=" + feedbackProfile + ", deviceToken=" + deviceToken + ", custId=" + custId + "]";
	}
	
}
