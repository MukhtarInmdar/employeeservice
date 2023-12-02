/**
 * 
 */
package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * ChatInfoPojo class provides chat Table specific fields.
 * 
 * @author Venkat_Koniki
 * @since 27.04.2019
 * @time 05:35PM
 */
@Entity
public class ChatInfoPojo {

	@Column(name="TICKETID")
	private Long ticketId;
	
	@Column(name="TICKET_DESCRIPTION")
	private String ticketDescription;
	
	@Column(name="TICKET_STATUS")
	private String ticketStatus;
	
	@Column(name="TICKET_TITLE")
	private String ticketTitle;
	
	@Column(name="ACTUAL_CUSTNO")
	private Long createdBy;
	
	@Column(name="ASSIGNED_EMPLOYEE")
	private String assignedEmployee;
	
	@Column(name="TICKET_CREATED_DATE")
	private Timestamp ticketCreatedDate;
	
	@Column(name="DEPT_ID")
	private Long departmentId;
	
	@Column(name="DEPT_NAME")
	private String departmentName;
	
	@Column(name="DEPT_DESC")
	private String departmentDescription;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="CUSTOMER_NO")
	private Long custId;
	
	@Column(name="EMPLOYEE_ID")
	private Long employeeId;
	
	@Column(name="EMP_NAME")
	private String employeeName;
	
	@Column(name="CUST_NAME")
	private String customerName;
	
	@Column(name="COMMENT_TIMESTAMP")
	private Timestamp chatMsgTime;
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
	 * @return the ticketDescription
	 */
	public String getTicketDescription() {
		return ticketDescription;
	}
	/**
	 * @param ticketDescription the ticketDescription to set
	 */
	public void setTicketDescription(String ticketDescription) {
		this.ticketDescription = ticketDescription;
	}
	/**
	 * @return the ticketStatus
	 */
	public String getTicketStatus() {
		return ticketStatus;
	}
	/**
	 * @param ticketStatus the ticketStatus to set
	 */
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	/**
	 * @return the ticketTitle
	 */
	public String getTicketTitle() {
		return ticketTitle;
	}
	/**
	 * @param ticketTitle the ticketTitle to set
	 */
	public void setTicketTitle(String ticketTitle) {
		this.ticketTitle = ticketTitle;
	}
	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the assignedEmployee
	 */
	public String getAssignedEmployee() {
		return assignedEmployee;
	}
	/**
	 * @param assignedEmployee the assignedEmployee to set
	 */
	public void setAssignedEmployee(String assignedEmployee) {
		this.assignedEmployee = assignedEmployee;
	}
	/**
	 * @return the ticketCreatedDate
	 */
	public Timestamp getTicketCreatedDate() {
		return ticketCreatedDate;
	}
	/**
	 * @param ticketCreatedDate the ticketCreatedDate to set
	 */
	public void setTicketCreatedDate(Timestamp ticketCreatedDate) {
		this.ticketCreatedDate = ticketCreatedDate;
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
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	/**
	 * @return the departmentDescription
	 */
	public String getDepartmentDescription() {
		return departmentDescription;
	}
	/**
	 * @param departmentDescription the departmentDescription to set
	 */
	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
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
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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
	/**
	 * @return the chatMsgTime
	 */
	public Timestamp getChatMsgTime() {
		return chatMsgTime;
	}
	/**
	 * @param chatMsgTime the chatMsgTime to set
	 */
	public void setChatMsgTime(Timestamp chatMsgTime) {
		this.chatMsgTime = chatMsgTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ChatInfoPojo [ticketId=" + ticketId + ", ticketDescription=" + ticketDescription + ", ticketStatus="
				+ ticketStatus + ", ticketTitle=" + ticketTitle + ", createdBy=" + createdBy + ", assignedEmployee="
				+ assignedEmployee + ", ticketCreatedDate=" + ticketCreatedDate + ", departmentId=" + departmentId
				+ ", departmentName=" + departmentName + ", departmentDescription=" + departmentDescription
				+ ", comments=" + comments + ", custId=" + custId + ", employeeId=" + employeeId + ", employeeName="
				+ employeeName + ", customerName=" + customerName + ", chatMsgTime=" + chatMsgTime + "]";
	}
	
	
}
