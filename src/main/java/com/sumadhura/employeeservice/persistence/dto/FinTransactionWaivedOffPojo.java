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
public class FinTransactionWaivedOffPojo {
	@Column(name = "FIN_TRANSACTION_WAIVED_OFF_ID") private Long finTransactionWaivedOffId;
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "FIN_TRANSFER_MODE_ID") private Long transferModeId;
	@Column(name = "WAIVED_OFF_DATE") private Timestamp waivedOffDate;
	@Column(name = "AMOUNT") private Double transactionAmount;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
	
}
