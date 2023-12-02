/**
 * 
 */
package com.sumadhura.employeeservice.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * TicketComment class provides TicketComment specific fields.
 * 
 * @author Venkat_Koniki
 * @since 04.05.2019
 * @time 04:46PM
 */
public class TicketComment extends Result implements Serializable {

	private static final long serialVersionUID = 8550713541679808127L;

	private Long ticketCommentId;
	private Long ticketId;
	private Long departmentId;
	private Long fromId;
	private Long fromType;
	private Long toId;
	private Long toType;
	private String message;
	private Timestamp commentsDate;
	private String visibleType;
	private String documentLocation;
	private Long statusId;
	private String name;
	private String profilepic;
	/**
	 * @return the ticketCommentId
	 */
	public Long getTicketCommentId() {
		return ticketCommentId;
	}
	/**
	 * @param ticketCommentId the ticketCommentId to set
	 */
	public void setTicketCommentId(Long ticketCommentId) {
		this.ticketCommentId = ticketCommentId;
	}
	/**
	 * @return the ticketId
	 */
	public Long getTicketId() {
		return ticketId;
	}
	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	/**
	 * @return the departmentId
	 */
	public Long getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return the fromId
	 */
	public Long getFromId() {
		return fromId;
	}
	/**
	 * @param fromId the fromId to set
	 */
	public void setFromId(Long fromId) {
		this.fromId = fromId;
	}
	/**
	 * @return the fromType
	 */
	public Long getFromType() {
		return fromType;
	}
	/**
	 * @param fromType the fromType to set
	 */
	public void setFromType(Long fromType) {
		this.fromType = fromType;
	}
	/**
	 * @return the toId
	 */
	public Long getToId() {
		return toId;
	}
	/**
	 * @param toId the toId to set
	 */
	public void setToId(Long toId) {
		this.toId = toId;
	}
	/**
	 * @return the toType
	 */
	public Long getToType() {
		return toType;
	}
	/**
	 * @param toType the toType to set
	 */
	public void setToType(Long toType) {
		this.toType = toType;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the commentsDate
	 */
	public Timestamp getCommentsDate() {
		return commentsDate;
	}
	/**
	 * @param commentsDate the commentsDate to set
	 */
	public void setCommentsDate(Timestamp commentsDate) {
		this.commentsDate = commentsDate;
	}
	/**
	 * @return the visibleType
	 */
	public String getVisibleType() {
		return visibleType;
	}
	/**
	 * @param visibleType the visibleType to set
	 */
	public void setVisibleType(String visibleType) {
		this.visibleType = visibleType;
	}
	/**
	 * @return the documentLocation
	 */
	public String getDocumentLocation() {
		return documentLocation;
	}
	/**
	 * @param documentLocation the documentLocation to set
	 */
	public void setDocumentLocation(String documentLocation) {
		this.documentLocation = documentLocation;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the profilepic
	 */
	public String getProfilepic() {
		return profilepic;
	}
	/**
	 * @param profilepic the profilepic to set
	 */
	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TicketComment [ticketCommentId=" + ticketCommentId + ", ticketId=" + ticketId + ", departmentId="
				+ departmentId + ", fromId=" + fromId + ", fromType=" + fromType + ", toId=" + toId + ", toType="
				+ toType + ", message=" + message + ", commentsDate=" + commentsDate + ", visibleType=" + visibleType
				+ ", documentLocation=" + documentLocation + ", statusId=" + statusId + ", name=" + name
				+ ", profilepic=" + profilepic + "]";
	}
	
	
}
