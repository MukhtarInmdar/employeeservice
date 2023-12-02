package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CarParkingAllotmentPojo {
	
	@Column(name="ALLOTMENT_ID")
	private Long allotmentId;
	
	@Column(name="SLOT_ID")
	private Long slotId;
	
	@Column(name="STATUS_ID")
	private Long statusId;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="MODIFIED_BY")
	private Long modifiedBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	
	@Column(name="ALLOTMENT_LETTER_PATH")
	private String allotmentLetterPath;
	
	@Column(name="ALLOTMENT_DATE")
	private Timestamp allotmentDate;
	
	@Column(name="SITE_ID")
	private Long siteId;
	
}
