package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class FinBookingFormAccountsPojo implements Cloneable{
	@Column(name = "FIN_BOOKING_FORM_ACCOUNTS_ID") private Long finBookingFormAccountsId;
	@Column(name = "TYPE_ID") private Long typeId;
	@Column(name = "TYPE") private Long type;
	@Column(name = "PAY_AMOUNT") private Double payAmount;
	@Column(name = "PAID_AMOUNT") private Double paidAmount;
	@Column(name = "TAX_AMOUNT") private Double taxAmount;
	@Column(name = "PRINCIPAL_AMOUNT") private Double principalAmount;
	@Column(name = "BALANCE_AMOUNT") private Double balanceAmount;
	
	@Column(name = "DUE_DATE") private Timestamp mileStoneDueDate;
	@Column(name = "PAYMENT_STATUS") private Long paymentStatus;
	@Column(name = "PAID_DATE") private Timestamp paidDate;
	@Column(name = "STATUS_ID") private Long statusId;
	@Column(name = "ACTIVE_STATUS_ID") private Long activeStatusId;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	@Column(name = "IS_INTEREST_APPLICABLE") private Long isInterestApplicable;
	@Column(name = "IS_INTEREST_CALC_COMPLETED") private Long isInterestCalcCompleted;
	
	@Column(name = "CREATED_BY") private Long createdBy;
	@Column(name = "CREATED_DATE") private Timestamp createdDate;
	@Column(name = "MODIFIED_BY") private Long modifiedBy;
	@Column(name = "MODIFIED_DATE") private Timestamp modifiedDate;
	@Column(name = "FIN_BOK_ACC_INVOICE_NO")private String finBokAccInvoiceNo;
	
	@Column(name ="AMOUNT") private Double basicAmount;
	@Column(name ="TOTAL_AMOUNT")private Double totalAmount;
	@Column(name ="INTEREST_AMOUNT") private Double interestAmount;
	@Column(name ="PENDING_AMOUNT") private Double pendingAmount;
	@Column(name = "METADATA_NAME")private String metadataName;
	@Column(name = "METADATA_DESC")private String metadataDesc;
	@Column(name = "IS_RECORD_UPLOADED") private Long isRecordUploaded;
	@Column(name = "REFUND_AMOUNT") private Double refundAmount;
	@Column(name = "INTEREST_WAIVER_AMT") private Double interestWaiverAdjAmount;
	
	//DON'T ADD MORE FEILDS AFTER THIS
	@Column(name = "LOCATION") private String documentLocation;
	@Column(name = "DOC_NAME") private String documentName;
	@Column(name = "MS_STATUS_ID")  private Long msStatusId;
	
	/* Malladi Changes */
	@Column(name = "CUST_ID")
	private Long customerId;

	@Column(name = "CUST_NAME")
	private String customerName;
	
	@Column(name = "SITE_NAME")
	private String siteName;
	
	@Column(name = "FLAT_ID")
	private Long flatId;

	@Column(name = "FLAT_NO")
	private String flatNo;
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}