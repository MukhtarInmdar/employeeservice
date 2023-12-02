package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FinAnnyEntryDocPojo {
	@Column(name = "FIN_ANNY_ENTRY_DOC_ID")
	private Long finAnnyEntryDocId;
	
	@Column(name = "FIN_ANMS_ENTRY_ID")
	private Long finAnmsEntryId;
	
	@Column(name = "LOCATION")
	private String location;
	
	@Column(name = "DOC_NAME")
	private String docName;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "DOC_PATH") private String filePath;
	
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
	@Column(name = "CREATED_DATE")
	private Timestamp createdDate;

}
