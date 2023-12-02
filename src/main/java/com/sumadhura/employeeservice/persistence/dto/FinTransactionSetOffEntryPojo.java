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
public class FinTransactionSetOffEntryPojo {

	@Column(name = "FIN_TRN_SET_OFF_ENT_ID") private Long transactionSetOffEntryId;
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
}
