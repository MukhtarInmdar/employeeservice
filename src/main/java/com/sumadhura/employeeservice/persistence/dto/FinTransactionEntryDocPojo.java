package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
public class FinTransactionEntryDocPojo {
	@Column(name = "FIN_TRANSACTION_ENTRY_DOC_ID") private Long transactionEntryDocId;
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DOC_NAME") private String documentName;
	@Column(name = "DOC_PATH") private String filePath;
	@Column(name = "DOC_TYPE") private Long documentType;
	@Column(name = "RECEIPT_TYPE") private Long receiptType;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "ACTIVE_STATUS_ID") private Long activeStatusId;
	//@Column(name = "TYPE_ID") private String typeId;
	//@Column(name = "TYPE") private String type;

	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	private Boolean saveFiles=true;
}
