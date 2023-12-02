package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class FinBookingFormAccountsInfo {

	private Long finBookingFormAccountsId;
	private Long typeId;
	private Long type;
	private List<Long> types;
	private Double payAmount;
	private Double paidAmount;
	private Double actualPaidAmount;
	private Timestamp mileStoneDueDate;
	private Long paymentStatus;
	private List<Long> paymentStatusList;
	private Timestamp paidDate;
	private Long statusId;
	private Long bookingFormId;
	private String condition;
	private Long createdBy;
	private Timestamp createdDate;
	private Long modifiedBy;
	private Timestamp modifiedDate;

	private Long finBookingFormReceiptsId;
	private Long finBookingFormExcessAmountId;
	private Double receiptPaidAmount;
	private String finBokAccInvoiceNo;

	private Double excessAmount;
	private Double usedAmount;
	private Double balanceAmount;
	private Double refundAmount;
	private Double interestWaiverAdjAmount;
}