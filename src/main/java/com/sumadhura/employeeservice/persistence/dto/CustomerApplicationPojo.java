package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class CustomerApplicationPojo {


	@Column(name="CUST_APP_ID")
	private Long custAppId;
	@Column(name="SITE_ID")
	private Long siteId;
	@Column(name="UNIT")
	private Long unit;
	@Column(name="LEAD_ID")
	private Long leadId;
	//@Column(name="ACK_ID")
	//private Long ackId;
	@Column(name="ACK_ID")
	private String ackId;
	@Column(name="APP_NO")
	private String appNo;
	@Column(name="BLOCK_ID")
	private Long blockId;
	@Column(name="STATUS_ID")
	private Long statusId;
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	@Column(name="STM_ID")
	private Long stmId;	
	@Column(name="FLAT_BOOK_ID")
	private Long flatBookId;
	@Column(name="REASON_FOR_NOKYC")
	private String reasonForNoKYC;
	
	
		
}
