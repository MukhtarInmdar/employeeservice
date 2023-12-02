package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class InvoiceDocumentLocationPojo {
	@Column(name = "INV_DOC_LOC_ID")
	private Long invDocId;
	 
	@Column(name = "DOC_NAME")
	private String docName;
	
	@Column(name = "DOC_LOCATION")
	private String docLocation;
	
	@Column(name = "DOC_PATH")
	private String docPath;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name = "TYPE")
	private Long type;
	
	@Column(name = "TYPE_ID")
	private Long typeId;
}
