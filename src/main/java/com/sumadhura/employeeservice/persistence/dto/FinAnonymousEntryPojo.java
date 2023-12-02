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
public class FinAnonymousEntryPojo {
	@Column(name = "FIN_ANMS_ENTRY_ID") private Long anonymousEntryId ;
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "FIN_TRANSFER_MODE_ID") private Long transferModeId ;
	@Column(name = "FIN_BANK_ID") private Long bankId ;
	//@Column(name = "BANK_ACCOUNT_NO") private Long bankAccountNumber ;
	@Column(name = "FIN_SITE_PROJ_ACC_MAP_ID") private Long siteAccountId;
	@Column(name = "SITE_BANK_ACCOUNT_NUMBER") private String siteBankAccountNumber;
	
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "SITE_NAME") private String siteName;
	@Column(name = "AMOUNT") private Double amount;
	@Column(name = "REFERENCE_NUMBER") private String referenceNo;
	@Column(name = "RECEIVED_DATE") private Timestamp transactionReceiveDate;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "ACTIVE_STATUS_ID") private Long activeStatusId;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	@Column(name = "BANK_NAME")private String bankName;
	@Column(name = "TRANSFER_MODE")private String transferMode;
	@Column(name = "IS_RECORD_UPLOADED") private Long isRecordUploaded;
	@Column(name = "LAST_APPROVED_DATE") private Timestamp lastApprovedDate;
}
