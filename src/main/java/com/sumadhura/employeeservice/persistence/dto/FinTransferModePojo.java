package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinTransferModePojo {
	@Column(name = "TRANSFER_MODE_ID")
	private Long transferModeId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CREATED_BY")
	private Long createdBy;

	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

	@Column(name = "MODIFIED_BY")
	private Long modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "STATUS_ID")
	private Long statusId;
}
