package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;
import java.util.List;

import com.sumadhura.employeeservice.dto.EmployeeFinTranPaymentSetOffRequest;
import com.sumadhura.employeeservice.dto.FinProjectAccountResponse;
import com.sumadhura.employeeservice.dto.OfficeDtlsResponse;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
//@ToString
public class FinancialDemandNoteMS_TRN_Info  implements Cloneable{
	//Demand notes
	private String recordType;
	private Timestamp DN_or_TRN_date;
	private Long siteId;
	private String siteName;
	private String demandNoteNo;
	private String blockName;
	private String flatNo;
	private Long flatId;
	private List<Long> flatIds;
	private List<Long> blockIds;
	private Long flatBookingId;
	private Long bookingFormId;
	
	private Long finMilestoneClassifidesId;
	
	private Timestamp demandNoteDate;
	private String isWithInterestOrWithOut;
	private Long projectMilestoneId;
	private Long mileStoneNo;
	private String milestoneName;
	private Double mileStonePercentage;
	private Timestamp milestoneDate;
	private Timestamp milestoneCompletedDate;
	private Timestamp mileStoneDueDate;
	private Double totalFlatAmountIncludeGST;
	private Double amountReceivedIncludeGST;
	private Double dueAmountExcludinggst;
	private Double cgst;
	private Double sgst;
	private Double totalDueAmount;
	
	//Transactions
	
	private Long transactionEntryId;
	private Long prevTransactionEntryId;
	private String prevTransactionReceiptNo;
	private Long transactionSetOffEntryId;
	private Long prevTransactionSetOffEntryId;
	
	private String transactionReceiptNo;
	private String salesFrTransactionReceiptNo;
	private String transactionTypeName;
	private String transactionModeName;
	private String transferModeName;
	private String transactionFor;
	
	private Long transactionTypeId;
	private Long transactionModeId;
	private Long transferModeId;
	private Long transactionForId;
	
	private String transactionNo;
	private String chequeNumber;
	private String referenceNo;
	private Double receivedAmount;
	//private Timestamp chequeDate;
	private Double transactionAmount;	
	private Timestamp transactionDate;
	private Timestamp transactionReceiveDate;
	private Timestamp chequeDate;
	private Timestamp onlineReceiveDate;
	private Timestamp chequeReceiveDate;
	private Timestamp chequeClearanceDate;
	private Timestamp chequeDepositedDate;
	private Timestamp transactionClosedDate;
	private String receiptStage;
	private String isShowGstInPDF;
	private String excelRecordNo;
	private String finBokAccInvoiceNo;
	private String paidByName;
	
	private Long bankId;
	private String bankName;
	private Long siteAccountId;
	private String siteBankAccountNumber;
	private Long bankAccountId;
	private String bankAccountNumber;
	private String comment;
	private String sourceOfFunds;
	private String dataUploadCondition;
	private String typeOfDemandNoteFormat;
	private CustomerPropertyDetailsInfo customerPropertyDetailsInfo;
	private List<EmployeeFinTranPaymentSetOffRequest> paymentSetOffDetails;
	
	private List<AddressInfo> customerAddressInfoList;
	private List<AddressInfo> siteAddressInfoList;
	private List<FinProjectAccountResponse> finProjectAccountResponseList;
	private List<OfficeDtlsResponse> officeDetailsList;
	private List<CustomerPropertyDetailsInfo> flatsResponse;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinancialDemandNoteMS_TRN_Info [recordType=").append(recordType).append(", milestoneName=").append(milestoneName)
		.append(", DN_or_TRN_date=")
				.append(DN_or_TRN_date).append(", siteId=").append(siteId).append(", siteName=").append(siteName)
				.append(", flatNo=").append(flatNo).append(", flatId=").append(flatId).append(", bookingFormId=")
				.append(bookingFormId).append(", projectMilestoneId=").append(projectMilestoneId)
				.append(", mileStoneNo=").append(mileStoneNo)
				.append(", mileStonePercentage=").append(mileStonePercentage).append("]");
		return builder.toString();
	}	
	
	/*@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}*/
}
