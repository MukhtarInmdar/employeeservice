/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.util.Date;

/**
 * @author VENKAT
 * DATE 15-MAR-2019
 * TIME 04.26 PM
 */

public class FeedBackDTO {

	private Long Id;
	private String feedback;
	private Date postedDate;
	private Long custId;
	private Long statusId;
	private String customerName;
	private Long customerId;
	private String emailId;
	private String flatNo;
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}
	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}
	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	/**
	 * @return the postedDate
	 */
	public Date getPostedDate() {
		return postedDate;
	}
	/**
	 * @param postedDate the postedDate to set
	 */
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	/**
	 * @return the custId
	 */
	public Long getCustId() {
		return custId;
	}
	/**
	 * @param custId the custId to set
	 */
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedBackDTO [Id=" + Id + ", feedback=" + feedback + ", postedDate=" + postedDate + ", custId=" + custId
				+ ", statusId=" + statusId + "]";
	}
	public Long getCustomerId() {
		return customerId;
	}
	public String getEmailId() {
		return emailId;
	}
	public String getFlatNo() {
		return flatNo;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}
	
}
