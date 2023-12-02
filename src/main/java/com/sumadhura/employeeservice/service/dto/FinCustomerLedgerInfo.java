package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import com.sumadhura.employeeservice.util.TimeUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FinCustomerLedgerInfo implements Cloneable, Comparable<FinCustomerLedgerInfo> {
	private Long serialNumber;
	private Long id;
	private Long finBookingFormAccountsStatementId;
	private Timestamp date;
	private Timestamp createdDate;
	private String description;
	private String referenceNumber;
	private String creditAmount;
	private String debitAmount;
	private String balanceAmount;
	private String recordType;
	private String isDemandNoteOrTransactionRecord;
	private String prefix;
	private String suffix;
	private String url;
	@Override
	public int hashCode() {
		if (this.getRecordType() != null) {
			if (this.getRecordType().equals("Debit")) {
				//debit is nothing but amount need to pay against milestones
				if (this.getReferenceNumber() != null) {
					if(this.getIsDemandNoteOrTransactionRecord()!=null) {
						return this.getReferenceNumber().hashCode();
					}else {
						return this.getReferenceNumber().hashCode();
					}
				} else {
					return 0;
				}
			} else if (this.getRecordType().equals("Credit")) {
				//credit is nothing but amount paid against milestones
				
				return 0;
			}
		}
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof FinCustomerLedgerInfo))
			return false;
		if (obj == this)
			return true;
		
		FinCustomerLedgerInfo ledgerInfo = (FinCustomerLedgerInfo) obj;
		//System.out.println(this.getRecordType()+"\t"+ledgerInfo.getRecordType());
		if (this.getRecordType()!=null && ledgerInfo.getRecordType()!=null
				&& this.getRecordType().equals("Debit") && ledgerInfo.getRecordType().equals("Debit")) {
			//debit is nothing but amount need to pay against milestones
			/*if(this.getIsDemandNoteOrTransactionRecord()!=null && this.getIsDemandNoteOrTransactionRecord().equals("Transaction")) {
				return false;
			} else {*/
				return this.getReferenceNumber().equals(ledgerInfo.getReferenceNumber());
			//}
			
		}else if (this.getRecordType()!=null && ledgerInfo.getRecordType()!=null
				&& this.getRecordType().equals("Credit") && ledgerInfo.getRecordType().equals("Credit")) {
			//credit is nothing but amount paid against milestones
			return  this.getReferenceNumber().equals(ledgerInfo.getReferenceNumber());
		}else {
			return false;
		}
	}

	@Override
	public int compareTo(FinCustomerLedgerInfo ledgerInfo) {
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
		return "FinCustomerLedgerInfo [id=" + id + ", finBookingFormAccountsStatementId="
				+ finBookingFormAccountsStatementId + ", date=" + date + ", referenceNumber=" + referenceNumber
				+ ", creditAmount=" + creditAmount + ", debitAmount=" + debitAmount + ", balanceAmount=" + balanceAmount
				+ ", recordType=" + recordType + ", isDemandNoteOrTransactionRecord=" + isDemandNoteOrTransactionRecord
				+ "]\n";
	}

}
