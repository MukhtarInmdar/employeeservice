package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FinTransactionForPojo {
	@Column(name ="FIN_TRANSACTION_FOR_ID") private Long transactionforId;
	@Column(name ="NAME") private String name;
	@Column(name ="DESCRIPTION") private String description;
	@Column(name ="CREATED_BY") private Long createdBy;
	@Column(name ="CREATED_DATE") private Timestamp createdDate;
	@Column(name ="STATUS_ID") private Long statusId;
}
