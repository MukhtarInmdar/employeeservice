package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class CarParkingApprovalPojo {
	
	@Column(name="CP_APR_ID")
	private Long cpAprId;
	
	@Column(name="ALLOTMENT_ID")
	private Long allotmentId;
	
	@Column(name="CP_APR_LEV_ID")
	private Long cpAprLevId;
	
	@Column(name="COMMENTS")
	private String comments;
	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name="CREATED_BY")
	private Long createdBy;
	
	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name="MODIFIED_BY")
	private Long modifiedBy;
	
	@Column(name="STATUS_ID")
	private Long statusId;

}
