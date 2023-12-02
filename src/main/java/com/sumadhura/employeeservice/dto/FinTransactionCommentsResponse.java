package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FinTransactionCommentsResponse {
	private Long transactionCommentId;
	private Long typeId;
	private Long type;
	private String comments;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private String metadataName;
}
