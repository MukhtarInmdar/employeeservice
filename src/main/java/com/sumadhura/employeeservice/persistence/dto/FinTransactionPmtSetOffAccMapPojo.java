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
public class FinTransactionPmtSetOffAccMapPojo {
	@Column(name = "FIN_TRN_PMT_SET_OFF_ACC_MAP_ID") private Long finTrnPaymentSetOffAccMapId;
	@Column(name = "FIN_TRN_SET_OFF_ID") private Long finTransactionSetOffId;
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long setOffTypeId;
	@Column(name = "STATUS_ID") private Long statusId;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE")private Timestamp createdDate;
	@Column(name = "MODIFIED_BY")private Long modifiedBy;
	@Column(name = "MODIFIED_DATE")private Timestamp modifiedDate;
}
