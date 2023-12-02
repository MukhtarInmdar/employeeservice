package com.sumadhura.employeeservice.persistence.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumadhura.employeeservice.dto.EmployeeFinTranPaymentSetOffRequest;

import lombok.Getter;
import lombok.Setter;
@Entity
@Setter
@Getter
public class FinancialDemandNoteMS_TRN_Pojo implements Cloneable{
	//Demand notes
	@Column(name = "TYPE") private String recordType;
	@Column(name = "FIN_TRANSACTION_ENTRY_ID") private Long transactionEntryId;
	@Column(name = "FIN_TRN_SET_OFF_ENT_ID") private Long transactionSetOffEntryId;
	@Column(name = "MILESTONE_NO") private Long mileStoneNo;
	@Column(name = "MILESTONE_NAME") private String milestoneName;
	@Column(name = "BLOCK_NAME") private String blockName;
//	@Column(name = "FLAT_ID") private Long flatId;

	@Column(name = "FLAT_NO") private String flatNo;
	@Column(name = "SITE_NAME") private String siteName;
	@Column(name = "SITE_ID") private String siteId;
	@Column(name = "BOOKING_FORM_ID") private Long bookingFormId;
	
	@Column(name = "DEMAND_NOTE_NO") private String demandNoteNo;

	@Column(name = "FLAT_ID") private Long flatId;
	@Column(name = "FLAT_BOOK_ID") private Long flatBookingId;
	@Column(name = "DEMAND_NOTE_DATE") 	private Timestamp demandNoteDate;
	
	@Column(name = "IS_INTEREST_OR_WITH_OUT_INTEREST") 	private String isWithInterestOrWithOut;
	@Column(name = "PROJECT_MILESTONE_ID") 	private Long projectMilestoneId;
	
	@Column(name = "MILSSTONE_PERCENTAGE")	private Double mileStonePercentage;
	@Column(name = "MILESTONE_DATE")	private Timestamp milestoneDate;
	@Column(name = "DEMAND_NOTE_DATE") private Timestamp milestoneCompletedDate;
	
	@Column(name = "DUE_DATE") private Timestamp mileStoneDueDate;
	@Column(name = "TOTAL_FLAT_AMOUNT_INCLUDE_GST") private Double totalFlatAmountIncludeGST;
	@Column(name = "AMOUNT_RECEIVED_INCLUDE_GST") private Double amountReceivedIncludeGST;
	@Column(name = "DUE_AMOUNT_EXCLUDING_GST") private Double dueAmountExcludinggst;
	@Column(name = "CGST") private Double cgst;
	@Column(name = "SGST") private Double sgst;
	@Column(name = "TOTAL_DUE_AMOUNT") private Double totalDueAmount;
	@Column(name = "SHOW_GST_IN_PDF") private String isShowGstInPDF;
	//Transactions
	
	@Column(name = "FIN_BOK_ACC_INVOICE_NO") private String finBokAccInvoiceNo;
	@Column(name = "PAID_BY_NAME")private String paidByName;
	
	//@Column(name = "TRANSACTION_RECEIPT_NO") private String transactionReceiptNo;
	@Column(name = "TRANSACTION_RECEIPT_NO") private String salesFrTransactionReceiptNo;
	@Column(name = "TRANSACTION_TYPE") private String transactionTypeName;
	@Column(name = "TRANSACTION_MODE") private String transactionModeName;
	@Column(name = "ONLINE_TRANSFER_MODE") private String transferModeName;
	@Column(name = "TRANSACTION_FOR") private String transactionFor;
	
	@Column(name = "FIN_TRANSACTION_TYPE_ID") private Long transactionTypeId;
	@Column(name = "FIN_TRANSACTION_MODE_ID") private Long transactionModeId;
	@Column(name = "TRANSFER_MODE_ID") private Long transferModeId;
	@Column(name = "TRANSACTION_FOR_ID") private Long transactionForId;
	
	//private String transactionNo;
	@Column(name = "ONLINE_TR_REFERENCE_NO") private String referenceNo;
	@Column(name = "ONLINE_TR_RECEIVE_DATE") private Timestamp onlineReceiveDate;
	@Column(name = "CHEQUE_DATE") private Timestamp chequeDate;
	@Column(name = "CHEQUE_DEPOSITED_DATE") private Timestamp chequeDepositedDate;
	@Column(name = "CHEQUE_CLEARECENCE_DATE") private Timestamp chequeClearanceDate;
	@Column(name = "CHEQUE_NUMBER") private String chequeNumber;
	@Column(name = "CHEQUE_RECEIVE_DATE") private Timestamp chequeReceiveDate;
	@Column(name = "TRANSACTION_AMOUNT") private Double transactionAmount;
	@Column(name = "BANK_NAME") private String bankName;
	@Column(name = "SITE_BANK_ACCOUNT_ID") private Long siteAccountId;
	@Column(name = "SITE_BANK_ACCOUNT_NUMBER") private String siteBankAccountNumber;
	
	//private Double receivedAmount;
	//private Timestamp chequeDate;
	
	@Column(name = "PRINCIPAL_AMOUNT") private Double principal_amount;
	@Column(name = "PENALTY_AMOUNT") private Double penalty_amount;
	@Column(name = "MODIFICATION_COST") private Double modification_cost;
	@Column(name = "LEGAL_COST") private Double legal_cost;
	@Column(name = "TDS_AMOUNT") private Double tds_amount;
	@Column(name = "FLAT_KHATA_AMOUNT") private Double flat_khata_amount;
	@Column(name = "MAINTENANCE_AMOUNT") private Double maintenance_amount;
	@Column(name = "CORPUS_AMOUNT") private Double corpus_amount;
	@Column(name = "REFUNDABLE_ADVANCE") private Double refundable_Advance;
	
	//@Column(name = "CHEQUE_DATE") 	private Timestamp transactionDate;
	//@Column(name = "ONLINE_TR_RECEIVE_DATE") 	private Timestamp transactionReceiveDate;
	
	@Column(name = "COMMENTS") private String comment;
	@Column(name = "TRANSACTION_CLOSED_DATE") private Timestamp transactionClosedDate;
	@Column(name = "RECEIPT_STAGE") private String receiptStage;
	//private String isShowGstInPDF;
	@Column(name = "EXCEL_RECORD_NO") private String excelRecordNo;
	@Column(name = "SOURCE_OF_FUNDS") private String sourceOfFunds;
	
	//private String dataUploadCondition;
	//private Long bankId;
	//private Long bankAccountId;
	//private String bankAccountNumber;
	
	//private String typeOfDemandNoteFormat;
	private List<EmployeeFinTranPaymentSetOffRequest> paymentSetOffDetails;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinancialDemandNoteMS_TRN_Request [recordType=").append(recordType)
		.append(", mileStoneNo=").append(mileStoneNo)
		.append(", transactionEntryId=").append(transactionEntryId).append(", transactionFor=").append(transactionFor)
		.append(", chequeDate=").append(chequeDate)
				.append(", milestoneName=").append(milestoneName).append(", blockName=")
				.append(blockName).append(", flatNo=").append(flatNo).append(", siteName=").append(siteName)
				.append(", flatId=").append(flatId).append(", demandNoteDate=").append(demandNoteDate)
				.append(", excelRecordNo=").append(excelRecordNo)
				.append(", SiteAccountId=").append(siteAccountId)
				.append(", SiteAccountNumber=").append(siteBankAccountNumber).append("]\n");
		return builder.toString();
	}	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
