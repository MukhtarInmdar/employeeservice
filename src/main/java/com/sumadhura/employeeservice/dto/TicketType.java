/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * TicketType class provides TicketType specific fields.
 * 
 * @author Venkat_Koniki
 * @since 02.05.2019
 * @time 1:20PM
 */
public class TicketType implements Serializable{

	private static final long serialVersionUID = 6227620365392043626L;
	private Long ticketTypeId;
	private String ticketType;
	private String description;
	private Float resolutionDayTime;
	private Long statusId;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	/**
	 * @return the ticketTypeId
	 */
	public Long getTicketTypeId() {
		return ticketTypeId;
	}
	/**
	 * @param ticketTypeId the ticketTypeId to set
	 */
	public void setTicketTypeId(Long ticketTypeId) {
		this.ticketTypeId = ticketTypeId;
	}
	/**
	 * @return the ticketType
	 */
	public String getTicketType() {
		return ticketType;
	}
	/**
	 * @param ticketType the ticketType to set
	 */
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
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
	/**
	 * @return the resolutionDayTime
	 */
	public Float getResolutionDayTime() {
		return resolutionDayTime;
	}
	/**
	 * @param resolutionDayTime the resolutionDayTime to set
	 */
	public void setResolutionDayTime(Float resolutionDayTime) {
		this.resolutionDayTime = resolutionDayTime;
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
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the modifiedDate
	 */
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}	
	
}
