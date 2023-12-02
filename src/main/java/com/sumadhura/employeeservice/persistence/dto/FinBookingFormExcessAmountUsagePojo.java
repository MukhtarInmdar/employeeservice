package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
//@Table("")
public class FinBookingFormExcessAmountUsagePojo {
	@Column(name = "FIN_BOK_FRM_EXS_AMT_USG_ID") private Long finBookingFormExcessAmtUsageId;
	@Column(name = "FIN_BOK_FRM_EXS_AMT_ID") private Long finBookingFormExcessAmountId;
	@Column(name = "FIN_BOK_FRM_ACC_ID") private Long finBookingFormAccountsId;
	@Column(name = "EXCESS_AMOUNT") private Double excessAmount;
	@Column(name = "CONVERT_TYPE") private Long convertType;
	@Column(name = "CONVERTED_AMOUNT") private Double convertedAmount;
	@Column(name = "BALANCE_AMOUNT") private Double balanceAmount;
	
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	
	//Not table columns
	@Column(name = "TOTAL_USAGE_AMOUNT") private Double totalUsageAmount;
}
