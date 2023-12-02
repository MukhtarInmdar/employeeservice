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
public class FinTransactionOnlinePojo {
	@Column(name = "FIN_TRANSACTION_ONLINE_ID") private Long transactionOnlineId;
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "FIN_TRANSFER_MODE_ID") private Long transferModeId;
	@Column(name = "REFERENCE_NO") private String referenceNo;
	@Column(name = "TRANSACTION_DATE") private Timestamp transactionDate;
	@Column(name = "AMOUNT") private Double amount;
	@Column(name = "FIN_BANK_ID") private Long finBankId;
	@Column(name = "ACCOUNT_NUMBER") private String bankAccountNumber;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	
}
