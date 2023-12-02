package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CarParkingAllotmentSlotPojo {
	
	@Column(name="BASEMENT_ID")
	private Long basementId;
	
	@Column(name="BASEMENT_NAME")
	private String basementName;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
	@Column(name="SLOT_ID")
	private Long slotId;
	
	@Column(name="SLOT_NAME")
	private String slotName;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="SLOT_STATUS_ID")
	private Long slotStatusId;
	
	@Column(name="SLOT_STATUS_NAME")
	private String slotStatusName;
	
	@Column(name="ALLOTMENT_ID")
	private Long allotmentId;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	
	@Column(name="ALLOTMENT_LETTER_PATH")
	private String allotmentLetterPath;
	
	@Column(name="ALLOTMENT_DATE")
	private Timestamp allotmentDate;
	
	@Column(name="ALLOTMENT_STATUS_ID")
	private Long allotmentStatusId;
	
	@Column(name="ALLOTMENT_STATUS_NAME")
	private String allotmentStatusName;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="BLOCK_NAME")
	private String blockName;
	
	@Column(name="FLAT_NO")
	private String flatNo;
	
	@Column(name="CUST_NAME")
	private String custName;
	
	@Column(name="SITE_NAME")
	private String siteName;
	
	@Column(name="APPROVER_EMP_ID")
	private Long approverEmpId;
	
	@Column(name="APPROVAL_COMMENTS")
	private String approvalComments;
}
