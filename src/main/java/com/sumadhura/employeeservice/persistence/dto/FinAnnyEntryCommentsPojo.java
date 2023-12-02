package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinAnnyEntryCommentsPojo {
	@Column(name ="FIN_ANNY_ENTRY_COMMENTS_ID")
	private Long finAnnyEntryCommentsId;
	
	@Column(name ="TYPE_ID")
	private Long typeId;
	
	@Column(name ="TYPE")
	private Long type;
	
	@Column(name ="COMMENTS")
	private String comments;
	
	@Column(name ="EMP_NAME")
	private String empName;
	
	@Column(name ="CREATED_BY")
	private Long createdBy;
	
	@Column(name ="CREATED_DATE")
	private Timestamp createdDate;
	
	@Column(name ="MODIFIED_BY")
	private Long modifiedBy;
	
	@Column(name ="MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name ="METADATA_NAME")
	private String metadataName;

}
