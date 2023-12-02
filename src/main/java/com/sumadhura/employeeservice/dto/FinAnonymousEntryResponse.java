package com.sumadhura.employeeservice.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FinAnonymousEntryResponse {
	private Long anonymousEntryId ;
	private Long transferModeId ;
	private Long bankId ;
	//private Long bankAccountNumber ;
	private Double amount;
	private String referenceNo;
	private Timestamp transactionReceiveDate;
	private Long suspenseAging;
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
	private List<?> finAnnyEntryCommentsResponseList;
	private List<FinAnnyEntryDocResponse> finAnnyEntryDocResponseList;
	private List<FinAnnyApproveStatResponse> finAnnyApproveStatResponseList;
	private Timestamp lastApprovedDate;
}
