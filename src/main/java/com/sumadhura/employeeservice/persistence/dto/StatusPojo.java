/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * StatusPojo class provides Employee Ticketing  status properties.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 04:50PM
 */

@Entity
public class StatusPojo {

	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="DESCRIPTION")
	private String description;

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
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatusPojo [statusId=" + statusId + ", status=" + status + ", description=" + description + "]";
	}
	
}
