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
public class FinClearedUncleareTXPojo {
	
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "FIN_ANMS_ENTRY_ID") private Long finAnonymousEntryId;
	@Column(name = "FIN_TRANSACTION_MODE_ID") private Long transactionModeId;
	@Column(name = "FIN_TRANSACTION_TYPE_ID") private Long transactionTypeId;
	@Column(name = "FIN_SITE_PROJ_ACC_MAP_ID") private Long finSiteProjAccountMapId;
	@Column(name = "SITE_ID") private Long siteId;
	@Column(name = "SITE_NAME") private String siteName;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "FIN_BANK_ID") private Long bankId;	
	@Column(name = "PAYMENT_DATE") private Timestamp transactionDate;
	@Column(name = "RECEIVED_DATE") private Timestamp transactionReceiveDate;
	@Column(name = "TRANSACTION_STATUS_ID") private Long transactionStatusId;
	@Column(name = "FIN_PROJ_ACC_ID") private Long siteAccountId;
	@Column(name = "ACCOUNT_NO") private String siteBankAccountNumber;
	@Column(name = "SET_OFF_TYPE") private Long setOffType;
	@Column(name = "FLAT_NO") private String flatNo;
	@Column(name = "SOURCE_OF_FUNDS") private String sourceOfFunds;
	
	@Column(name = "TX_CLEARANCE_DATE") private Timestamp txClearanceDate;
	@Column(name = "CHEQUE_OR_REFERENCE_NO") private String chequeOrReferenceNo;
	@Column(name = "AMOUNT") private String amount;
	@Column(name = "DR_CR") private String drCr;
	@Column(name = "PENDING_MODULE_NAME") private String pendingModuleName;
	@Column(name = "PENDING_BY_LEVEL") private String pendingByLevel;
	@Column(name = "PENDING_BY_LEVEL_WITH_MODULE") private String pendingByLevelWithModule;
}
