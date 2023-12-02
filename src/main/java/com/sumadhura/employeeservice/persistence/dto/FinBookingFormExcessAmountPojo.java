package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormExcessAmountPojo {
	@Column(name = "FIN_BOK_FRM_EXS_AMT_ID") private Long finBookingFormExcessAmountId;
	@Column(name = "FIN_BOK_FRM_RCPT_ID") private Long finBookingFormReceiptsId;
	@Column(name = "EXCESS_AMOUNT") private Double excessAmount;
	@Column(name = "USED_AMOUNT") private Double usedAmount;
	@Column(name = "BALANCE_AMOUNT") private Double balanceAmount;
	@Column(name = "TYPE") private Long type;
	//@Column(name = "METADATA_NAME") private String metadataName;
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	@Column(name = "FIN_BOK_ACC_INVOICE_NO") private String finBokAccInvoiceNo;
	@Column(name = "METADATA_NAME") private String metadataName; 
	
	//booking form Statement POJO Properties
	@Column(name = "PAID_AMOUNT") private Double paidAmount;
	@Column(name = "PAID_DATE") private Timestamp paidDate;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DOC_NAME") private String documentName;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinBookingFormExcessAmountPojo [excessAmount=");
		builder.append(excessAmount);
		builder.append(", usedAmount=");
		builder.append(usedAmount);
		builder.append(", balanceAmount=");
		builder.append(balanceAmount);
		builder.append(", type=");
		builder.append(type);
		builder.append(", finBokAccInvoiceNo=");
		builder.append(finBokAccInvoiceNo);
		builder.append(", metadataName=");
		builder.append(metadataName);
		builder.append(", paidAmount=");
		builder.append(paidAmount);
		builder.append(", paidDate=");
		builder.append(paidDate);
		builder.append(", bookingFormId=");
		builder.append(bookingFormId);
		builder.append("]\n");
		return builder.toString();
	}
	
	
}
