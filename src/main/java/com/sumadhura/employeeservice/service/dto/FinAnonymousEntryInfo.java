package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FinAnonymousEntryInfo {
	private Long anonymousEntryId;
	private Long transferModeId;
	private Long bankId;
	// private Long bankAccountNumber ;
	private Double amount;
	private String referenceNo;
	private Timestamp transactionReceiveDate;
	private Long statusId;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;
	private String bankName;
	private String transferMode;

	private Long siteId;
	private String siteName;

	private Long siteAccountId;
	private String siteBankAccountNumber;
	private Long suspenseAging;
	private Timestamp lastApprovedDate;
}
