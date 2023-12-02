package com.sumadhura.employeeservice.service.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FinTransactionClearedInfo {
	private Long siteId;
	private String siteName;

	private Long flatSaleOwnerId;
	private String flatSaleOwner;
	private String amount;
	private Long transactionTypeId;
	
	private Timestamp receivedFromDate;
	private Timestamp receivedToDate;
	
	private Timestamp clearanceFromDate;
	private Timestamp clearanceToDate;
	private String operationType;

	@Override
	public int hashCode() {

		return siteId.hashCode()+flatSaleOwner.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FinTransactionClearedInfo other = (FinTransactionClearedInfo) obj;

		if(other.flatSaleOwner==null || this.flatSaleOwner==null) {
			return false;
		}
		
		if(other.flatSaleOwner.equals(this.flatSaleOwner) && other.siteName.equals(this.siteName) ) {
			return true;
		} else {
			return false;
		}
		
	}

}
