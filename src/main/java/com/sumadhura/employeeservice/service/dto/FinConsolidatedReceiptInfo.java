package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.sumadhura.employeeservice.util.TimeUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FinConsolidatedReceiptInfo implements Cloneable, Comparable<FinConsolidatedReceiptInfo> {
	
	private Long transactionEntryId;
	private Long transactionModeId;
	private Long transactionTypeId;
	private Long finBookingFormAccountsStatementId;
	private Timestamp date;
	private Timestamp createdDate;
	
	private String chequeOrReferenceNo; // chequeOrOnline reference number
	private Timestamp transactionDate;// cheque or online transaction date
	private Timestamp transactionReceiveDate;// cheque or online receive date
	private String transactionReceiptNo;
	private String transactionTypeName;
	private Double transactionAmount;
	private Double setOffAmount;
	private Double principalAmount;
	private Double gstAmount;
	private Double cgstAmount;
	private Double sgstAmount;
	private String transactionStatusName;
	private Long transactionStatusId;
	
	@Override
	public int hashCode() {
		if (this.getTransactionTypeName() != null) {
			//if (this.getTransactionTypeName().equals("Debit")) {
				//debit is nothing but amount need to pay against milestones
				if (this.getTransactionReceiptNo() != null) {
					if(this.getTransactionTypeName()!=null) {
						return this.getTransactionReceiptNo().hashCode();
					}else {
						return this.getTransactionReceiptNo().hashCode();
					}
				} else {
					return 0;
				}
			/*} else if (this.getTransactionTypeName().equals("Credit")) {
				//credit is nothing but amount paid against milestones
				
				return 0;
			}*/
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof FinConsolidatedReceiptInfo))
			return false;
		if (obj == this)
			return true;
		
		FinConsolidatedReceiptInfo ledgerInfo = (FinConsolidatedReceiptInfo) obj;
		//System.out.println(this.getTransactionTypeName()+"\t"+ledgerInfo.getTransactionTypeName());
		if (this.getTransactionTypeName()!=null && ledgerInfo.getTransactionTypeName()!=null) {
				return this.getTransactionReceiptNo().equals(ledgerInfo.getTransactionReceiptNo());
				
		}else if (this.getTransactionTypeName()!=null && ledgerInfo.getTransactionTypeName()!=null) {
			//credit is nothing but amount paid against milestones
				return  this.getTransactionReceiptNo().equals(ledgerInfo.getTransactionReceiptNo());
		}else {
			return false;
		}
	}

	@Override
	public int compareTo(FinConsolidatedReceiptInfo ledgerInfo) {
		if (ledgerInfo == null || this ==null) {
			return 0;
		}

		if (this.getDate() == null) {
			return 0;
		}

		if (ledgerInfo.getDate() == null) {
			return 1;
		}

		try {
			if (TimeUtil.removeTimePartFromTimeStamp1(this.getDate()).equals(TimeUtil.removeTimePartFromTimeStamp1(ledgerInfo.getDate()))) {
				if (this.getCreatedDate()!=null && ledgerInfo.getCreatedDate() !=null &&  this.getCreatedDate().after(ledgerInfo.getCreatedDate())) {
					return 1;
				}
				return 0;
			} else if (this.getDate().after(ledgerInfo.getDate())) {
				return 1;
			} 
			/*else if (TimeUtil.removeTimePartFromTimeStamp1(this.getDate()).compareTo(TimeUtil.removeTimePartFromTimeStamp1(ledgerInfo.getDate())) >= 1) {
				return 1;
			}*/
			else {
				return -1;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FinConsolidatedReceiptInfo [transactionEntryId=").append(transactionEntryId)
				.append(", transactionModeId=").append(transactionModeId).append(", transactionTypeId=")
				.append(transactionTypeId).append(", date=").append(date).append(", createdDate=").append(createdDate)
				.append(", chequeOrReferenceNo=").append(chequeOrReferenceNo).append(", transactionDate=")
				.append(transactionDate).append(", transactionReceiveDate=").append(transactionReceiveDate)
				.append(", transactionReceiptNo=").append(transactionReceiptNo).append(", transactionTypeName=")
				.append(transactionTypeName).append(", transactionAmount=").append(transactionAmount)
				.append(", setOffAmount=").append(setOffAmount).append(", principalAmount=").append(principalAmount)
				.append(", cgstAmount=").append(cgstAmount).append(", sgstAmount=").append(sgstAmount)
				.append(", transactionStatusId=").append(transactionStatusId).append("]");
		return builder.toString();
	}

}
