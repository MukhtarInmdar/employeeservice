package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CustomerLeadCommentsPojo {

	@Column(name="LEAD_COMMENT_ID")
	private Long leadCommentId;
	
	@Column(name="LEAD_COMMENT")
	private String leadComment;
	
	@Column(name="LEAD_COMMENT_OWNER")
	private String leadCommentOwner;
	
	@Column(name="LEAD_COMMENT_DATE")
	private Date createdDate;

	
	

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLeadCommentId() {
		return leadCommentId;
	}

	public void setLeadCommentId(Long leadCommentId) {
		this.leadCommentId = leadCommentId;
	}

	public String getLeadComment() {
		return leadComment;
	}

	public void setLeadComment(String leadComment) {
		this.leadComment = leadComment;
	}

	public String getLeadCommentOwner() {
		return leadCommentOwner;
	}

	public void setLeadCommentOwner(String leadCommentOwner) {
		this.leadCommentOwner = leadCommentOwner;
	}

	
	
	
}
